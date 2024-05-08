package com.example.backend.Services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.backend.Entities.CareContext;
import com.example.backend.Entities.Consents;
import com.example.backend.Entities.DataRequests;
import com.example.backend.Entities.DataTransfers;
import com.example.backend.Entities.ExternalRecords;
import com.example.backend.Entities.Patients;
import com.example.backend.Entities.Records;
import com.example.backend.Models.abdm.CareContextModel;
import com.example.backend.Models.abdm.ConsentAck;
import com.example.backend.Models.abdm.ConsentReqNotify;
import com.example.backend.Models.abdm.HIUConsentReqNotify;
import com.example.backend.Models.abdm.ConsentReqOnInit;
import com.example.backend.Models.abdm.ConsentTypeReq;
import com.example.backend.Models.abdm.DataTransferAck;
import com.example.backend.Models.abdm.DataTransferNotify;
import com.example.backend.Models.abdm.DataTransferOnReq;
import com.example.backend.Models.abdm.DataTransferReq;
import com.example.backend.Models.abdm.DataTransferRes;
import com.example.backend.Models.abdm.DataTransferRes.Entries;
import com.example.backend.Models.abdm.GenerateLinkingTokenRes;
import com.example.backend.Models.abdm.PatientCareContextDiscoverReq;
import com.example.backend.Models.abdm.PatientCareContextDiscoverRes;
import com.example.backend.Models.abdm.DataTransferNotify.Notification.StatusNotification;
import com.example.backend.Models.abdm.DataTransferNotify.Notification.StatusNotification.StatusResponses;
import com.example.backend.Repositories.ConsentRepository;
import com.example.backend.Repositories.DataRequestRepository;
import com.example.backend.Repositories.DataTransferRepository;
import com.example.backend.Repositories.ExternalRecordsRepository;
import com.example.backend.Repositories.PatientRepository;
import com.example.backend.cryptography.CryptographyUtil;
import com.google.gson.Gson;

import reactor.core.publisher.Mono;

@Service
public class CallbackServices {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    ABDMServices abdmServices;

    @Autowired
    ConsentRepository consentRepository;

    @Autowired
    DataRequestRepository dataRequestRepository;

    @Autowired
    CryptographyUtil cryptographyUtil;

    @Autowired
    DataTransferRepository dataTransferRepository;

    @Value("${record_base_path}")
    private String recordBasePath;

    @Value("${hmis_id}")
    private String hmisId;

    @Autowired
    private ExternalRecordsRepository externalRecordsRepository;

    @Autowired
    private FHIRServices fhirServices;

    public void onGenerateLinkingToken(GenerateLinkingTokenRes req) {
        Patients patient = patientRepository.findByAbhaAddress(req.getAbhaAddress());
        patient.setLinkToken(req.getLinkToken());
        patientRepository.save(patient);
    }

    public Mono<Void> onPatientDiscoverCareContext(String reqId, PatientCareContextDiscoverReq req) throws Exception {
        String abhaAddress = req.getPatient().getId();

        Patients patient = patientRepository.findByAbhaAddress(abhaAddress);
        List<Records> records = patient.getRecords();

        PatientCareContextDiscoverRes res = new PatientCareContextDiscoverRes();
        res.setRequestId(UUID.randomUUID().toString());
        res.setTimestamp(abdmServices.getTimeStamp());
        res.setTransactionId(req.getTransactionId());
        res.setPatient(new PatientCareContextDiscoverRes.Patient());
        res.getPatient().setReferenceNumber(UUID.randomUUID().toString());
        res.getPatient().setDisplay("Records");

        List<CareContextModel> careContexts = new ArrayList<>();
        for(Records r: records) {
            CareContext careContext = r.getCareContext();
            CareContextModel toAdd = new CareContextModel();
            toAdd.setDisplay(careContext.getDisplay());
            toAdd.setReferenceNumber(careContext.getReferenceNumber());
            careContexts.add(toAdd);
        }

        res.getPatient().setCareContexts(careContexts);
        res.getPatient().setMatchedBy(new ArrayList<String>());
        res.getPatient().getMatchedBy().add(req.getPatient().getVerifiedIdentifiers().get(0).getType());
        res.setResp(new PatientCareContextDiscoverRes.Response(reqId));

        try {
            WebClient webClient = WebClient.create();
            abdmServices.initiateSession();
            Mono<Void> mono = 
                webClient
                .post()
                .uri(new URI("https://dev.abdm.gov.in/gateway/v0.5/care-contexts/on-discover"))
                .header("Authorization", "Bearer " + abdmServices.getAccessToken())
                .header("REQUEST-ID", UUID.randomUUID().toString())
                .header("TIMESTAMP", abdmServices.getTimeStamp())
                .header("X-CM-ID", "sbx")
                .bodyValue(res)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                                response -> response.bodyToMono(String.class).flatMap(body -> {
                                    System.out.println("Error response body: " + body);
                                    return null;
                                }))
                .bodyToMono(Void.class);
            return mono;
        }
        catch (Exception e) {
            System.out.println("Error in CallbackServices->onPatientDiscoverCareContext: " + e.getLocalizedMessage());
            return Mono.error(e);
        }
    }

    public void consentRequestOnInit(ConsentReqOnInit req) {
        Consents consent = consentRepository.findById(req.getResponse().getRequestId()).get();
        consent.setConsentReqId(req.getConsentRequest().getId());
        consentRepository.save(consent);
    }

    public void consentRequestNotify(HIUConsentReqNotify req) {
        String consentReqId = req.getNotification().getConsentRequestId();
        if(req.getNotification().getStatus().equals("GRANTED")) {
            Consents consent = consentRepository.findByConsentReqId(consentReqId);
            consent.setStatus(req.getNotification().getStatus());
            consent.setConsentArtefactId(req.getNotification().getConsentArtefacts().get(0).getId());
            consentRepository.save(consent);
            // consentRepository.flush();
            try {
                Thread.sleep(3000);
                abdmServices.requestHealthData(req);
            }
            catch (Exception e) {
                System.out.println("Error in CallbackServices->consentRequestNotify: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        else if(req.getNotification().getStatus().equals("REVOKED")) {
            this.deleteRevokedRecords(req);
        }
    }

    public void consentRequestNotifyDummy(HIUConsentReqNotify req) {
        String consentReqId = req.getNotification().getConsentRequestId();
        if(req.getNotification().getStatus().equals("GRANTED")) {
            Consents consent = consentRepository.findByConsentReqId(consentReqId);
            consent.setStatus(req.getNotification().getStatus());
            consent.setConsentArtefactId(req.getNotification().getConsentArtefacts().get(0).getId());
            consentRepository.save(consent);
            // consentRepository.flush();
            try {
                Thread.sleep(3000);
                abdmServices.requestHealthDataDummy(req);
            }
            catch (Exception e) {
                System.out.println("Error in CallbackServices->consentRequestNotify: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        else if(req.getNotification().getStatus().equals("REVOKED")) {
            this.deleteRevokedRecords(req);
        }
    }

    @Async
    public void healthInformationOnRequest(DataTransferOnReq req) {
        if(req.getError() != null) {
            System.out.println("CallbackServices->healthInformationOnRequest error: " + req.getError().getMessage());
        }
        else {
            DataRequests dataRequest = dataRequestRepository.findByRequestId(req.getResponse().getRequestId());
            dataRequest.setTransactionId(req.getHiRequest().getTransactionId());
            dataRequest.setStatus(req.getHiRequest().getSessionStatus());
            dataRequestRepository.save(dataRequest);
        }
    }

    @Async
    public void receiveDataRequest(DataTransferReq req) {
        
    }

    @Async
    public void acknowledgeDataRequest(DataTransferReq req) {
        DataTransferAck ack = new DataTransferAck();
        ack.setRequestId(UUID.randomUUID().toString());
        ack.setTimestamp(abdmServices.getTimeStamp());
        
        DataTransferAck.HiRequest hiRequest = new DataTransferAck.HiRequest();
        hiRequest.setTransactionId(req.getTransactionId());
        hiRequest.setSessionStatus("ACKNOWLEDGED");
        DataTransferAck.Resp resp = new DataTransferAck.Resp();
        resp.setRequestId(req.getRequestId());
        ack.setHiRequest(hiRequest);
        ack.setResp(resp);

        try {
            WebClient webClient = WebClient.create();
            abdmServices.initiateSession();

            webClient
            .post()
            .uri(new URI("https://dev.abdm.gov.in/gateway/v0.5/health-information/hip/on-request"))
            .header("Authorization", "Bearer " + abdmServices.getAccessToken())
            .header("X-CM-ID", "sbx")
            .bodyValue(ack)
            .retrieve()
            .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        response -> response.bodyToMono(String.class).flatMap(body -> {
                            System.out.println("Error response body: " + body);
                            return null;
                        }))
            .bodyToMono(Void.class)
            .subscribe(args -> {
                System.out.println("CallbackServices->acknowledgeDataRequest: acknowledged");
            });

        }
        catch (Exception e) {
            System.out.println("Error in CallbackServices->acknowledgeDataRequest: " + e.getLocalizedMessage());
        }
    }

    @Async
    public void hipHealthInformationRequest(DataTransferReq req) {
        DataTransferAck ack = new DataTransferAck();
        
        ack.setRequestId(UUID.randomUUID().toString());
        ack.setTimestamp(abdmServices.getTimeStamp());
        
        DataTransferAck.HiRequest hiRequest = new DataTransferAck.HiRequest();
        hiRequest.setTransactionId(req.getTransactionId());
        hiRequest.setSessionStatus("ACKNOWLEDGED");
        ack.setHiRequest(hiRequest);

        DataTransferAck.Resp resp = new DataTransferAck.Resp();
        resp.setRequestId(req.getRequestId());
        ack.setResp(resp);

        try {
            abdmServices.initiateSession();
            WebClient webClient = WebClient.create();
            webClient
            .post()
            .uri(new URI("https://dev.abdm.gov.in/gateway/v0.5/health-information/hip/on-request"))
            .header("Authorization", "Bearer " + abdmServices.getAccessToken())
            .header("X-CM-ID", "sbx")
            .bodyValue(ack)
            .retrieve()
            .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        response -> response.bodyToMono(String.class).flatMap(body -> {
                            System.out.println("Error response body: " + body);
                            return null;
                        }))
            .bodyToMono(Void.class)
            .subscribe((args) -> {
                this.getRequestedHiTypes(req);
            });
        }
        catch (Exception e) {
            System.out.println("Error in CallbackServices->hipHealthInformationRequest: " + e.getLocalizedMessage());
        }
    }
    
    public void getRequestedHiTypes(DataTransferReq req) {
        ConsentTypeReq consentTypeReq = new ConsentTypeReq();
        String id = UUID.randomUUID().toString();
        consentTypeReq.setRequestId(id);
        consentTypeReq.setTimestamp(abdmServices.getTimeStamp());
        consentTypeReq.setConsentId(req.getHiRequest().getConsent().getId());
        
        DataTransfers transfer = new DataTransfers();
        transfer.setReqId(id);
        transfer.setDataPushUrl(req.getHiRequest().getDataPushUrl());
        transfer.setFromDate(req.getHiRequest().getDateRange().getFrom());
        transfer.setToDate(req.getHiRequest().getDateRange().getTo());
        transfer.setKey(req.getHiRequest().getKeyMaterial().getDhPublicKey().getKeyValue());
        transfer.setNonce(req.getHiRequest().getKeyMaterial().getNonce());
        transfer.setTransactionId(req.getTransactionId());
        dataTransferRepository.save(transfer);

        try {
            abdmServices.initiateSession();
            
            WebClient webClient = WebClient.create();

            webClient
            .post()
            .uri(new URI("https://dev.abdm.gov.in/gateway/v0.5/consents/fetch"))
            .header("Authorization", "Bearer " + abdmServices.getAccessToken())
            .header("X-CM-ID", "sbx")
            .bodyValue(consentTypeReq)
            .retrieve()
            .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        response -> response.bodyToMono(String.class).flatMap(body -> {
                            System.out.println("Error response body: " + body);
                            return null;
                        }))
            .bodyToMono(Void.class)
            .subscribe((args) -> {
                System.out.println("CallbackServices->getRequestedHiTypes: requested HiTypes");
            });
        }
        catch (Exception e) {
            System.out.println("Error in CallbackServices->getRequestedHiTypes: " + e.getLocalizedMessage());
        }

    }

    public void transferHealthData(DataTransferReq req) {
        DataTransfers transferInfo = dataTransferRepository.findByReqId(req.getHiRequest().getConsent().getId());
        if(transferInfo == null) {
            return;
        }
        Patients patient = patientRepository.findByAbhaAddress(transferInfo.getAbhaAddress());
        List<Records> allRecords = patient.getRecords();
        List<Records> requiredRecords = new ArrayList<>();
        HashSet<String> careContextSet = new HashSet<>();
        
        System.out.println("Size of carecontexts: " + transferInfo.getCareContexts().size());
        for(ConsentReqNotify.CareContext c: transferInfo.getCareContexts()) {
            careContextSet.add(c.getCareContextReference());
        }
        System.out.println("Size of carecontextSet: " + careContextSet.size());
        System.out.println("Contents of careContextSet: " + careContextSet);
        System.out.println("All records: " + allRecords);
        for(Records r: allRecords) {
            if(careContextSet.contains(r.getCareContext().getArtefactId())) {
                requiredRecords.add(r);
            }
        }

        DataTransferRes toSend = new DataTransferRes();
        toSend.setTransactionId(req.getTransactionId());
        
        DataTransferRes.KeyMaterial keyMaterial = new DataTransferRes.KeyMaterial();
        keyMaterial.setCryptoAlg("ECDH");
        keyMaterial.setCurve("Curve25519");

        DataTransferRes.DhPublicKey dhPublicKey = new DataTransferRes.DhPublicKey();
        dhPublicKey.setKeyValue(cryptographyUtil.getKeys().getPublicKey());
        dhPublicKey.setParameters("Curve25519/32byte random key");
        long expiry = new Date().getTime() + (24l * 60l * 60l * 1000l);
        Date expiryDate = new Date(expiry);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dhPublicKey.setExpiry(df.format(expiryDate));
        keyMaterial.setDhPublicKey(dhPublicKey);
        keyMaterial.setNonce(cryptographyUtil.getKeys().getNonce());
        
        toSend.setKeyMaterial(keyMaterial);

        List<DataTransferRes.Entries> entries = new ArrayList<>();

        List<StatusResponses> notifyList = new ArrayList<>();

        for(Records r: requiredRecords) {
            try {
                File f = new File(r.getFilePath());
                DataTransferRes.Entries toAdd = new DataTransferRes.Entries();

                Scanner sc = new Scanner(f);
                StringBuilder sb = new StringBuilder();
                while(sc.hasNextLine()) {
                    sb.append(sc.nextLine());
                }
                sc.close();
                if(r.getRecordType().equals("Prescription")) {
                    String json = sb.toString();
                    json = cryptographyUtil.decrypt(json);
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("content", json);
                    hm.put("careContextReference", r.getCareContext().getReferenceNumber());
                    hm.put("date", df.format(r.getDate()));
                    hm.put("doctor", r.getDoctor().getUser().getName());
                    hm.put("display", r.getDisplay());
                    hm.put("type", r.getRecordType());
                    Gson gson = new Gson();
                    json = gson.toJson(hm);
                    String encryptedJson = cryptographyUtil.fideliusEncrypt(json, req.getHiRequest().getKeyMaterial().getNonce(), req.getHiRequest().getKeyMaterial().getDhPublicKey().getKeyValue());
                    Checksum checksum = new CRC32();
                    checksum.update(encryptedJson.getBytes(), 0, encryptedJson.getBytes().length);
                    toAdd.setMedia("application/fhir+json");
                    toAdd.setCareContextReference(r.getCareContext().getReferenceNumber());
                    toAdd.setContent(encryptedJson);
                    toAdd.setChecksum(Long.toString(checksum.getValue()));
                }
                else if(r.getRecordType().equals("HealthDocumentRecord")) {
                    String text = sb.toString();
                    text = cryptographyUtil.decrypt(text);
                    String json = fhirServices.createHealthDocument(r, text);
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("content", json);
                    hm.put("careContextReference", r.getCareContext().getReferenceNumber());
                    hm.put("date", df.format(r.getDate()));
                    hm.put("doctor", r.getDoctor().getUser().getName());
                    hm.put("display", r.getDisplay());
                    hm.put("type", r.getRecordType());
                    hm.put("text", text);
                    Gson gson = new Gson();
                    json = gson.toJson(hm);
                    String encryptedJson = cryptographyUtil.fideliusEncrypt(json, req.getHiRequest().getKeyMaterial().getNonce(), req.getHiRequest().getKeyMaterial().getDhPublicKey().getKeyValue());
                    Checksum checksum = new CRC32();
                    checksum.update(encryptedJson.getBytes(), 0, encryptedJson.getBytes().length);
                    toAdd.setMedia("application/fhir+json");
                    toAdd.setCareContextReference(r.getCareContext().getReferenceNumber());
                    toAdd.setContent(encryptedJson);
                    toAdd.setChecksum(Long.toString(checksum.getValue()));
                }
                else {
                    String text = sb.toString();
                    text = cryptographyUtil.decrypt(text);
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("content", text);
                    hm.put("careContextReference", r.getCareContext().getReferenceNumber());
                    hm.put("date", df.format(r.getDate()));
                    hm.put("doctor", r.getDoctor().getUser().getName());
                    hm.put("display", r.getDisplay());
                    hm.put("type", r.getRecordType());

                    Gson gson = new Gson();
                    String json = gson.toJson(hm);
                    String encryptedJson = cryptographyUtil.fideliusEncrypt(json, req.getHiRequest().getKeyMaterial().getNonce(), req.getHiRequest().getKeyMaterial().getDhPublicKey().getKeyValue());
                    Checksum checksum = new CRC32();
                    checksum.update(encryptedJson.getBytes(), 0, encryptedJson.getBytes().length);
                    
                    toAdd.setMedia("application/json");
                    toAdd.setCareContextReference(r.getCareContext().getReferenceNumber());
                    toAdd.setContent(encryptedJson);
                    toAdd.setChecksum(Long.toString(checksum.getValue()));
                }
                entries.add(toAdd);
                
                StatusResponses statusResponse = new StatusResponses();
                statusResponse.setCareContextReference(r.getCareContext().getReferenceNumber());
                statusResponse.setDescription("TRANSFERRED");
                statusResponse.setHiStatus("OK");
                notifyList.add(statusResponse);
            }
            catch (FileNotFoundException e) {
                System.out.println("File not found for care context: " + r.getCareContext().getArtefactId());
                e.printStackTrace();
                StatusResponses statusResponse = new StatusResponses();
                statusResponse.setCareContextReference(r.getCareContext().getReferenceNumber());
                statusResponse.setDescription("FAILED");
                statusResponse.setHiStatus("FAILED");
                notifyList.add(statusResponse);
            }
            catch (Exception e) {
                System.out.println("Error for care context: " + r.getCareContext().getArtefactId());
                e.printStackTrace();
                StatusResponses statusResponse = new StatusResponses();
                statusResponse.setCareContextReference(r.getCareContext().getReferenceNumber());
                statusResponse.setDescription("FAILED");
                statusResponse.setHiStatus("FAILED");
                notifyList.add(statusResponse);
            }
        }

        toSend.setEntries(entries);

        try {
            WebClient webClient = WebClient.create();
            System.out.println("Sending:");
            System.out.println(toSend);
            System.out.println();
            System.out.println();
            webClient
            .post()
            .uri(new URI(req.getHiRequest().getDataPushUrl()))
            .bodyValue(toSend)
            .retrieve()
            .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        response -> response.bodyToMono(String.class).flatMap(body -> {
                            System.out.println("Error response body: " + body);
                            return null;
                        }))
            .bodyToMono(Void.class)
            .subscribe((args) -> {
                System.out.println("CallbackServices->transferHealthData: sent data");

                DataTransferNotify notifyABDM = new DataTransferNotify();
                notifyABDM.setRequestId(UUID.randomUUID().toString());
                notifyABDM.setTimestamp(abdmServices.getTimeStamp());
                
                DataTransferNotify.Notification notification = new DataTransferNotify.Notification();
                notification.setConsentId(transferInfo.getReqId());
                notification.setTransactionId(transferInfo.getTransactionId());

                DataTransferNotify.Notifier notifier = new DataTransferNotify.Notifier();
                notifier.setId(hmisId);
                notifier.setId("HIP");
                notification.setNotifier(notifier);

                DataTransferNotify.Notification.StatusNotification statusNotification = new StatusNotification();
                statusNotification.setSessionStatus("TRANSFERRED");
                statusNotification.setHipId(hmisId);
                statusNotification.setStatusResponses(notifyList);
                notification.setStatusNotification(statusNotification);
                
                notifyABDM.setNotification(notification);

                try {
                    WebClient webClient2 = WebClient.create();
                    abdmServices.initiateSession();
                    webClient2
                    .post()
                    .uri(new URI("https://dev.abdm.gov.in/gateway/v0.5/health-information/notify"))
                    .header("Authorization", "Bearer " + abdmServices.getAccessToken())
                    .header("X-CM-ID", "sbx")
                    .bodyValue(notifyABDM)
                    .retrieve()
                    .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                            response -> response.bodyToMono(String.class).flatMap(body -> {
                                System.out.println("Error response body: " + body);
                                return null;
                            }))
                    .bodyToMono(Void.class)
                    .subscribe((args2) -> {
                        System.out.println("ABDM notified");
                    });
                }
                catch (Exception e) {
                    System.out.println("Error in notifying ABDM");
                }
            });
        }
        catch (Exception e) {
            System.out.println("Error in CallbackServices->transferHealthData: " + e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public void transferHealthDataDummy(DataTransferReq req) {
        DataTransfers transferInfo = dataTransferRepository.findByReqId(req.getHiRequest().getConsent().getId());
        if(transferInfo == null) {
            return;
        }
        Patients patient = patientRepository.findByAbhaAddress(transferInfo.getAbhaAddress());
        List<Records> allRecords = patient.getRecords();
        List<Records> requiredRecords = new ArrayList<>();
        HashSet<String> careContextSet = new HashSet<>();
        
        System.out.println("Size of carecontexts: " + transferInfo.getCareContexts().size());
        for(ConsentReqNotify.CareContext c: transferInfo.getCareContexts()) {
            careContextSet.add(c.getCareContextReference());
        }
        System.out.println("Size of carecontextSet: " + careContextSet.size());
        System.out.println("Contents of careContextSet: " + careContextSet);
        System.out.println("All records: " + allRecords);
        for(Records r: allRecords) {
            if(careContextSet.contains(r.getCareContext().getArtefactId())) {
                requiredRecords.add(r);
            }
        }

        DataTransferRes toSend = new DataTransferRes();
        toSend.setTransactionId(req.getTransactionId());
        
        DataTransferRes.KeyMaterial keyMaterial = new DataTransferRes.KeyMaterial();
        keyMaterial.setCryptoAlg("ECDH");
        keyMaterial.setCurve("Curve25519");

        DataTransferRes.DhPublicKey dhPublicKey = new DataTransferRes.DhPublicKey();
        dhPublicKey.setKeyValue(cryptographyUtil.getKeys().getPublicKey());
        dhPublicKey.setParameters("Curve25519/32byte random key");
        long expiry = new Date().getTime() + (24l * 60l * 60l * 1000l);
        Date expiryDate = new Date(expiry);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dhPublicKey.setExpiry(df.format(expiryDate));
        keyMaterial.setDhPublicKey(dhPublicKey);
        keyMaterial.setNonce(cryptographyUtil.getKeys().getNonce());
        
        toSend.setKeyMaterial(keyMaterial);

        List<DataTransferRes.Entries> entries = new ArrayList<>();

        List<StatusResponses> notifyList = new ArrayList<>();

        for(Records r: requiredRecords) {
            try {
                File f = new File(r.getFilePath());
                DataTransferRes.Entries toAdd = new DataTransferRes.Entries();

                Scanner sc = new Scanner(f);
                StringBuilder sb = new StringBuilder();
                while(sc.hasNextLine()) {
                    sb.append(sc.nextLine());
                }
                sc.close();
                if(r.getRecordType().equals("Prescription")) {
                    String json = sb.toString();
                    json = cryptographyUtil.decrypt(json);
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("content", json);
                    hm.put("careContextReference", r.getCareContext().getReferenceNumber());
                    hm.put("date", df.format(r.getDate()));
                    hm.put("doctor", r.getDoctor().getUser().getName());
                    hm.put("display", r.getDisplay());
                    hm.put("type", r.getRecordType());
                    Gson gson = new Gson();
                    json = gson.toJson(hm);
                    String encryptedJson = cryptographyUtil.fideliusEncrypt(json, req.getHiRequest().getKeyMaterial().getNonce(), req.getHiRequest().getKeyMaterial().getDhPublicKey().getKeyValue());
                    Checksum checksum = new CRC32();
                    checksum.update(encryptedJson.getBytes(), 0, encryptedJson.getBytes().length);
                    toAdd.setMedia("application/fhir+json");
                    toAdd.setCareContextReference(r.getCareContext().getReferenceNumber());
                    toAdd.setContent(encryptedJson);
                    toAdd.setChecksum(Long.toString(checksum.getValue()));
                }
                else if(r.getRecordType().equals("HealthDocumentRecord")) {
                    String text = sb.toString();
                    text = cryptographyUtil.decrypt(text);
                    String json = fhirServices.createHealthDocument(r, text);
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("content", json);
                    hm.put("careContextReference", r.getCareContext().getReferenceNumber());
                    hm.put("date", df.format(r.getDate()));
                    hm.put("doctor", r.getDoctor().getUser().getName());
                    hm.put("display", r.getDisplay());
                    hm.put("type", r.getRecordType());
                    hm.put("text", text);
                    Gson gson = new Gson();
                    json = gson.toJson(hm);
                    String encryptedJson = cryptographyUtil.fideliusEncrypt(json, req.getHiRequest().getKeyMaterial().getNonce(), req.getHiRequest().getKeyMaterial().getDhPublicKey().getKeyValue());
                    Checksum checksum = new CRC32();
                    checksum.update(encryptedJson.getBytes(), 0, encryptedJson.getBytes().length);
                    toAdd.setMedia("application/fhir+json");
                    toAdd.setCareContextReference(r.getCareContext().getReferenceNumber());
                    toAdd.setContent(encryptedJson);
                    toAdd.setChecksum(Long.toString(checksum.getValue()));
                }
                else {
                    String text = sb.toString();
                    text = cryptographyUtil.decrypt(text);
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("content", text);
                    hm.put("careContextReference", r.getCareContext().getReferenceNumber());
                    hm.put("date", df.format(r.getDate()));
                    hm.put("doctor", r.getDoctor().getUser().getName());
                    hm.put("display", r.getDisplay());
                    hm.put("type", r.getRecordType());

                    Gson gson = new Gson();
                    String json = gson.toJson(hm);
                    String encryptedJson = cryptographyUtil.fideliusEncrypt(json, req.getHiRequest().getKeyMaterial().getNonce(), req.getHiRequest().getKeyMaterial().getDhPublicKey().getKeyValue());
                    Checksum checksum = new CRC32();
                    checksum.update(encryptedJson.getBytes(), 0, encryptedJson.getBytes().length);
                    
                    toAdd.setMedia("application/json");
                    toAdd.setCareContextReference(r.getCareContext().getReferenceNumber());
                    toAdd.setContent(encryptedJson);
                    toAdd.setChecksum(Long.toString(checksum.getValue()));
                }
                entries.add(toAdd);
                
                StatusResponses statusResponse = new StatusResponses();
                statusResponse.setCareContextReference(r.getCareContext().getReferenceNumber());
                statusResponse.setDescription("TRANSFERRED");
                statusResponse.setHiStatus("OK");
                notifyList.add(statusResponse);
            }
            catch (FileNotFoundException e) {
                System.out.println("File not found for care context: " + r.getCareContext().getArtefactId());
                e.printStackTrace();
                StatusResponses statusResponse = new StatusResponses();
                statusResponse.setCareContextReference(r.getCareContext().getReferenceNumber());
                statusResponse.setDescription("FAILED");
                statusResponse.setHiStatus("FAILED");
                notifyList.add(statusResponse);
            }
            catch (Exception e) {
                System.out.println("Error for care context: " + r.getCareContext().getArtefactId());
                e.printStackTrace();
                StatusResponses statusResponse = new StatusResponses();
                statusResponse.setCareContextReference(r.getCareContext().getReferenceNumber());
                statusResponse.setDescription("FAILED");
                statusResponse.setHiStatus("FAILED");
                notifyList.add(statusResponse);
            }
        }

        toSend.setEntries(entries);

        try {
            WebClient webClient = WebClient.create();
            System.out.println("Sending:");
            System.out.println(toSend);
            System.out.println();
            System.out.println();
            webClient
            .post()
            .uri(new URI(req.getHiRequest().getDataPushUrl()))
            .bodyValue(toSend)
            .retrieve()
            .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        response -> response.bodyToMono(String.class).flatMap(body -> {
                            System.out.println("Error response body: " + body);
                            return null;
                        }))
            .bodyToMono(Void.class)
            .subscribe((args) -> {
                System.out.println("CallbackServices->transferHealthData: sent data");

                DataTransferNotify notifyABDM = new DataTransferNotify();
                notifyABDM.setRequestId(UUID.randomUUID().toString());
                notifyABDM.setTimestamp(abdmServices.getTimeStamp());
                
                DataTransferNotify.Notification notification = new DataTransferNotify.Notification();
                notification.setConsentId(transferInfo.getReqId());
                notification.setTransactionId(transferInfo.getTransactionId());

                DataTransferNotify.Notifier notifier = new DataTransferNotify.Notifier();
                notifier.setId(hmisId);
                notifier.setId("HIP");
                notification.setNotifier(notifier);

                DataTransferNotify.Notification.StatusNotification statusNotification = new StatusNotification();
                statusNotification.setSessionStatus("TRANSFERRED");
                statusNotification.setHipId(hmisId);
                statusNotification.setStatusResponses(notifyList);
                notification.setStatusNotification(statusNotification);
                
                notifyABDM.setNotification(notification);

                try {
                    // WebClient webClient2 = WebClient.create();
                    // abdmServices.initiateSession();
                    // webClient2
                    // .post()
                    // .uri(new URI("https://dev.abdm.gov.in/gateway/v0.5/health-information/notify"))
                    // .header("Authorization", "Bearer " + abdmServices.getAccessToken())
                    // .header("X-CM-ID", "sbx")
                    // .bodyValue(notifyABDM)
                    // .retrieve()
                    // .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                    //         response -> response.bodyToMono(String.class).flatMap(body -> {
                    //             System.out.println("Error response body: " + body);
                    //             return null;
                    //         }))
                    // .bodyToMono(Void.class)
                    // .subscribe((args2) -> {
                    //     System.out.println("ABDM notified");
                    // });
                }
                catch (Exception e) {
                    System.out.println("Error in notifying ABDM");
                }
            });
        }
        catch (Exception e) {
            System.out.println("Error in CallbackServices->transferHealthData: " + e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public void receiveHealthData(DataTransferRes data) throws ParseException, java.io.IOException {
        DataRequests request = dataRequestRepository.findByTransactionId(data.getTransactionId());
        if(request == null) {
            return;
        }
        Patients patient = patientRepository.findByAbhaAddress(request.getAbhaAddress());
        String senderNonce = data.getKeyMaterial().getNonce();
        String senderKey = data.getKeyMaterial().getDhPublicKey().getKeyValue();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    

        List<StatusResponses> notifyList = new ArrayList<>();

        for(Entries e: data.getEntries()) {
            ExternalRecords toAdd = new ExternalRecords();
            toAdd.setPatient(patient);

            String media = e.getMedia();
            String encryptedContent = e.getContent();
            String decryptedContent = "";
            
            try {
                decryptedContent = cryptographyUtil.fideliusDecrypt(encryptedContent, senderKey, senderNonce);
            }
            catch (Exception ex) {
                System.out.println("Error in CallbackServices->receiveHealthData: " + ex.getLocalizedMessage());
                ex.printStackTrace();
            }
            
            toAdd.setExpiry(request.getExpiry());
            Gson gson = new Gson();
            HashMap<String, String> hm = new HashMap<>();
            System.out.println("Decrypted content: " + decryptedContent);
            hm = gson.fromJson(decryptedContent, hm.getClass());
            hm = gson.fromJson(hm.get("decryptedData"), HashMap.class);
            toAdd.setDate(df.parse(hm.getOrDefault("date", "1970-01-01T00:00:00.000Z")));
            toAdd.setDoctorName(hm.getOrDefault("doctor", "-"));
            toAdd.setDisplay(hm.getOrDefault("display", "-"));
            toAdd.setFilePath(recordBasePath + patient.getPatientId() + "_" + toAdd.getDate());
            toAdd.setRecordType(hm.get("type"));
            toAdd.setConsentArtefactId(request.getConsentArtefactId());

            File recordFile = new File(recordBasePath + patient.getPatientId() + "_" + df.parse(hm.getOrDefault("date", "1970-01-01T00:00:00.000Z")));
            if(toAdd.getRecordType().equals("HealthDocumentRecord")) {
                if(recordFile.createNewFile()) {
                    FileWriter writer = new FileWriter(recordFile);
                    String content = hm.get("text");
                    content = cryptographyUtil.encrypt(content);
                    writer.write(content);
                    writer.close();
                }

                String fhirContent = hm.get("content");
                toAdd = externalRecordsRepository.save(toAdd);
                fhirServices.saveHealthRecordFiles(fhirContent, toAdd, request.getTransactionId());
            }
            else {
                if(recordFile.createNewFile()) {
                    FileWriter writer = new FileWriter(recordFile);
                    String content = hm.get("content");
                    content = cryptographyUtil.encrypt(content);
                    writer.write(content);
                    writer.close();
                }
            }
            toAdd = externalRecordsRepository.save(toAdd);

            StatusResponses statusResponse = new StatusResponses();
            statusResponse.setCareContextReference(e.getCareContextReference());
            statusResponse.setDescription("TRANSFERRED");
            statusResponse.setHiStatus("OK");
            notifyList.add(statusResponse);
        }

        DataTransferNotify notifyABDM = new DataTransferNotify();
        notifyABDM.setRequestId(UUID.randomUUID().toString());
        notifyABDM.setTimestamp(abdmServices.getTimeStamp());
        
        DataTransferNotify.Notification notification = new DataTransferNotify.Notification();
        notification.setConsentId(request.getConsentArtefactId());
        notification.setTransactionId(request.getTransactionId());

        DataTransferNotify.Notifier notifier = new DataTransferNotify.Notifier();
        notifier.setId(hmisId);
        notifier.setId("HIU");
        notification.setNotifier(notifier);

        DataTransferNotify.Notification.StatusNotification statusNotification = new StatusNotification();
        statusNotification.setSessionStatus("TRANSFERRED");
        statusNotification.setHipId(hmisId);
        statusNotification.setStatusResponses(notifyList);
        notification.setStatusNotification(statusNotification);
        
        notifyABDM.setNotification(notification);

        try {

            // TODO: Remove comment before pushing

            WebClient webClient2 = WebClient.create();
            abdmServices.initiateSession();
            webClient2
            .post()
            .uri(new URI("https://dev.abdm.gov.in/gateway/v0.5/health-information/notify"))
            .header("Authorization", "Bearer " + abdmServices.getAccessToken())
            .header("X-CM-ID", "sbx")
            .bodyValue(notifyABDM)
            .retrieve()
            .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                    response -> response.bodyToMono(String.class).flatMap(body -> {
                        System.out.println("Error response body: " + body);
                        return null;
                    }))
            .bodyToMono(Void.class)
            .subscribe((args2) -> {
                System.out.println("ABDM notified");
            });
        }
        catch (Exception e) {
            System.out.println("Error in notifying ABDM");
        }
        
    }
    
    public void deleteRevokedRecords(HIUConsentReqNotify req) {
        String consentArtefactId = req.getNotification().getConsentArtefacts().get(0).getId();

        List<ExternalRecords> externalRecords = externalRecordsRepository.findByConsentArtefactId(consentArtefactId);

        List<ExternalRecords> toRemove = new ArrayList<>();
        for(ExternalRecords e: externalRecords) {
            if(e.getConsentArtefactId().equals(consentArtefactId)) {
                toRemove.add(e);
                try {
                    Files.deleteIfExists(Paths.get(new URI(e.getFilePath())));                
                }
                catch (Exception ex) {
                    System.out.println("Error in CallbackServices->deleteRevokedRecords: " + ex.getLocalizedMessage());
                }
            }
        }
        externalRecordsRepository.deleteAll(toRemove);
    }

    @Async
    public void hipConsentNotify(String reqId, ConsentReqNotify req) {
        ConsentAck res = new ConsentAck();
        res.setRequestId(UUID.randomUUID().toString());
        res.setTimestamp(abdmServices.getTimeStamp());
        
        ConsentAck.Resp resp = new ConsentAck.Resp();
        resp.setRequestId(reqId);
        res.setResp(resp);

        ConsentAck.Acknowledgement ack = new ConsentAck.Acknowledgement();
        ack.setStatus("OK");
        ack.setConsentId(req.getNotification().getConsentDetail().getConsentId());
        res.setAcknowledgement(ack);

        try {
            abdmServices.initiateSession();

            WebClient webClient = WebClient.create();

            webClient
            .post()
            .uri(new URI("https://dev.abdm.gov.in/gateway/v0.5/consents/hip/on-notify"))
            .header("Authorization", "Bearer " + abdmServices.getAccessToken())
            .header("X-CM-ID", "sbx")
            .bodyValue(res)
            .retrieve()
            .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                    response -> response.bodyToMono(String.class).flatMap(body -> {
                        System.out.println("Error response body: " + body);
                        return null;
                    }))
            .bodyToMono(Void.class)
            .subscribe((args2) -> {
                System.out.println("ABDM notified");
            });
        }
        catch (Exception e) {
            System.out.println("Error in CallbackServices->hipConsentNotify: " + e.getLocalizedMessage());
        }
        DataTransfers transfer = new DataTransfers();
        transfer.setReqId(req.getNotification().getConsentDetail().getConsentId());
        transfer.setFromDate(req.getNotification().getConsentDetail().getPermission().getDateRange().getFrom());
        transfer.setToDate(req.getNotification().getConsentDetail().getPermission().getDateRange().getTo());
        transfer.setCareContexts(req.getNotification().getConsentDetail().getCareContexts());
        transfer.setAbhaAddress(req.getNotification().getConsentDetail().getPatient().getId());
        dataTransferRepository.save(transfer);
    }

    public void hipConsentNotifyDummy(String reqId, ConsentReqNotify req) {
        ConsentAck res = new ConsentAck();
        res.setRequestId(UUID.randomUUID().toString());
        res.setTimestamp(abdmServices.getTimeStamp());
        
        ConsentAck.Resp resp = new ConsentAck.Resp();
        resp.setRequestId(reqId);
        res.setResp(resp);

        ConsentAck.Acknowledgement ack = new ConsentAck.Acknowledgement();
        ack.setStatus("OK");
        ack.setConsentId(req.getNotification().getConsentDetail().getConsentId());
        res.setAcknowledgement(ack);

        try {
            // abdmServices.initiateSession();

            // WebClient webClient = WebClient.create();

            // webClient
            // .post()
            // .uri(new URI("https://dev.abdm.gov.in/gateway/v0.5/consents/hip/on-notify"))
            // .header("Authorization", "Bearer " + abdmServices.getAccessToken())
            // .header("X-CM-ID", "sbx")
            // .bodyValue(res)
            // .retrieve()
            // .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
            //         response -> response.bodyToMono(String.class).flatMap(body -> {
            //             System.out.println("Error response body: " + body);
            //             return null;
            //         }))
            // .bodyToMono(Void.class)
            // .subscribe((args2) -> {
            //     System.out.println("ABDM notified");
            // });
        }
        catch (Exception e) {
            System.out.println("Error in CallbackServices->dummyHipConsentNotify: " + e.getLocalizedMessage());
        }

        DataTransfers transfer = new DataTransfers();
        transfer.setReqId(req.getNotification().getConsentDetail().getConsentId());
        transfer.setFromDate(req.getNotification().getConsentDetail().getPermission().getDateRange().getFrom());
        transfer.setToDate(req.getNotification().getConsentDetail().getPermission().getDateRange().getTo());
        transfer.setCareContexts(req.getNotification().getConsentDetail().getCareContexts());
        transfer.setAbhaAddress(req.getNotification().getConsentDetail().getPatient().getId());
        dataTransferRepository.save(transfer);
    }
}

