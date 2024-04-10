package com.example.backend.Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.backend.Entities.CareContext;
import com.example.backend.Entities.Patients;
import com.example.backend.Entities.Records;
import com.example.backend.Models.AccessTokenModel;
import com.example.backend.Models.AuthInit;
import com.example.backend.Models.ConfirmAuthModel;
import com.example.backend.Models.PatientProfileModel;
import com.example.backend.Models.ReceptionistPatientModel;
import com.example.backend.Models.ResendAuthModel;
import com.example.backend.Models.SessionModel;
import com.example.backend.Models.TxnIDModel;
import com.example.backend.Models.XTokenModel;
import com.example.backend.Models.abdm.CareContextModel;
import com.example.backend.Models.abdm.CareContextPatient;
import com.example.backend.Models.abdm.GenerateLinkingTokenReq;
import com.example.backend.Models.abdm.LinkCareContextReq;
import com.example.backend.Repositories.PatientRepository;
import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Service
@Getter
@Setter
public class ABDMServices {
    
    @Value("${api.clientId}")
    private String apiClientId;

    @Value("${api.clientSecret}")
    private String apiClientSecret;

    private String rsaKey;

    private String accessToken;

    private String xToken;


    private static final String BASE_URI = "https://healthidsbx.abdm.gov.in";
    private static final String BASE_PATH = "api";

    @Autowired
    private PatientRepository patientRepository;

    public String getTimeStamp() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    public void getPublicKey() {
        WebClient webClient = WebClient.create();
        String keyMono = webClient.get()
                                      .uri("https://healthidsbx.abdm.gov.in/api/v1/auth/cert")
                                      .retrieve()
                                      .bodyToMono(String.class).block();
        setRsaKey(keyMono); 
    }

        

    public void initiateSession() {
        try {
            SessionModel session = new SessionModel(apiClientId, apiClientSecret);
            URL url = new URL("https://dev.abdm.gov.in/gateway/v0.5/sessions");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoOutput(true);

            Gson gson = new Gson();
            String requestBody = gson.toJson(session);

            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            int responseCode = httpURLConnection.getResponseCode();

            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            AccessTokenModel accessTokenModel = gson.fromJson(response.toString(), AccessTokenModel.class);

            setAccessToken(accessTokenModel.getAccessToken());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TxnIDModel initializeAuth(AuthInit authInit) {
        try {
            initiateSession();
            URL url = new URL(BASE_URI + "/" + BASE_PATH + "/v1/auth/init");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            httpURLConnection.setDoOutput(true);
            // System.out.println("here");
            Gson gson = new Gson();
            String requestBody = gson.toJson(authInit);
            // System.out.println(authInit.getAuthMethod());
            // System.out.println(authInit.getHealthid());
            // System.out.println(requestBody.toString());
            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            int responseCode = httpURLConnection.getResponseCode();
            System.out.println(responseCode);
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            // System.out.println("here");
            System.out.println(response.toString());
            TxnIDModel txnIDModel = gson.fromJson(response.toString(), TxnIDModel.class);

            return txnIDModel;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TxnIDModel resendAuth(ResendAuthModel resendAuth) {
        try {
            initiateSession();
            URL url = new URL(BASE_URI + "/" + BASE_PATH + "/v1/auth/resendAuthOTP");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            httpURLConnection.setDoOutput(true);
            System.out.println(resendAuth.getAuthMethod());
            System.out.println(resendAuth.getTxnId());
            Gson gson = new Gson();
            String requestBody = gson.toJson(resendAuth);

            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            int responseCode = httpURLConnection.getResponseCode();
            System.out.println(responseCode);
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            System.out.println(response.toString());

            TxnIDModel txnIDModel = gson.fromJson(response.toString(), TxnIDModel.class);

            return txnIDModel;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public XTokenModel confirmAuth(ConfirmAuthModel confirmAuthModel, String authType) {
        try {
            initiateSession();
            URL url;
            if (authType == "AADHAAR") {
                url = new URL(BASE_URI + "/" + BASE_PATH + "/v1/auth/confirmWithAadhaarOTP");
            } else {
                url = new URL(BASE_URI + "/" + BASE_PATH + "/v1/auth/confirmWithMobileOTP");
            }
            
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Authorization","Bearer " + getAccessToken());
            httpURLConnection.setDoOutput(true);

            Gson gson = new Gson();
            String requestBody = gson.toJson(confirmAuthModel);

            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            int responseCode = httpURLConnection.getResponseCode();

            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            System.out.println(responseCode);
            System.out.println(response.toString());
            XTokenModel accessTokenModel = gson.fromJson(response.toString(), XTokenModel.class);
            setXToken(accessTokenModel.getToken());

            return accessTokenModel;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PatientProfileModel fetchProfile() {
        try {
            initiateSession();
            URL url = new URL(BASE_URI + "/" + BASE_PATH + "/v2/account/profile");
            
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Authorization","Bearer " + getAccessToken());
            httpURLConnection.setRequestProperty("X-Token", "Bearer " + getXToken());
            // httpURLConnection.setDoOutput(false);

            Gson gson = new Gson();
            // String requestBody = gson.toJson(confirmAuthModel);

            // try (OutputStream os = httpURLConnection.getOutputStream()) {
            //     byte[] input = requestBody.getBytes("utf-8");
            //     os.write(input, 0, input.length);
            // }
            
            int responseCode = httpURLConnection.getResponseCode();

            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            System.out.println(responseCode);
            System.out.println(response.toString());
            PatientProfileModel patientProfileModel = gson.fromJson(response.toString(), PatientProfileModel.class);

            return patientProfileModel;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Mono<Object> generateLinkingToken(String abhaAddress) throws Exception {
        System.out.println("Generating link token request");
        Patients patient = patientRepository.findByAbhaAddress(abhaAddress);
        GenerateLinkingTokenReq req = new GenerateLinkingTokenReq();
        req.setName(patient.getName());
        req.setGender(patient.getGender());
        req.setYearOfBirth(Integer.parseInt(patient.getDob().toString().substring(0, 4)));
        req.setAbhaAddress(abhaAddress);
        System.out.println("Name:" + req.getName());
        System.out.println("Gender:" + req.getGender());
        System.out.println("Year:" + req.getYearOfBirth());
        System.out.println("ABHA address:" + req.getAbhaAddress());
        WebClient webClient = WebClient.create();

        try {
            this.initiateSession();
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            df.setTimeZone(tz);
            String nowAsISO = df.format(new Date());
            System.out.println("Date:" + nowAsISO);
            System.out.println("Sending request");
            Mono<Object> res = webClient
            .post()
            .uri(new URI("https://dev.abdm.gov.in/hiecm/api/v3/token/generate-token"))
            .header("REQUEST-ID", UUID.randomUUID().toString())
            .header("TIMESTAMP", nowAsISO)
            .header("Authorization", "Bearer " + this.getAccessToken())
            .header("X-HIP-ID", "HAD_01")
            .header("X-CM-ID", "sbx")
            .bodyValue(req)
            .retrieve()
            .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                            response -> response.bodyToMono(String.class).flatMap(body -> {
                                System.out.println("Error response body: " + body);
                                return null;
                            }))
            .bodyToMono(Object.class);
            return res;
        }
        catch (Exception e) {
            System.out.println("Error in generating linking token: " + e.getLocalizedMessage());
            throw e;
        }
    }

    public Mono<Void> linkRecord(Records record, Patients patient, CareContext careContext) throws Exception {
        System.out.println("Inside linking record");
        LinkCareContextReq req = new LinkCareContextReq();
        req.setAbhaAddress(patient.getAbhaAddress());
        CareContextPatient careContextPatient = new CareContextPatient();
        careContextPatient.setReferenceNumber(UUID.randomUUID().toString());
        careContextPatient.setDisplay(record.getDisplay());

        CareContextModel careContextModel = new CareContextModel();
        careContextModel.setDisplay(record.getDisplay());
        careContextModel.setReferenceNumber(UUID.randomUUID().toString());
        careContextPatient.setCareContexts(new ArrayList<>());
        careContextPatient.getCareContexts().add(careContextModel);
        careContextPatient.setHiType(record.getRecordType());
        careContextPatient.setCount(careContextPatient.getCareContexts().size());
        req.setPatient(new ArrayList<>());
        req.getPatient().add(careContextPatient);


        System.out.println("Sending linking request");
        System.out.println(req);

        try {
            this.initiateSession();
            WebClient webClient = WebClient.create();
            System.out.println("Sending linking request");
            System.out.println(req);
            return webClient
                .post()
                .uri(new URI("https://dev.abdm.gov.in/hiecm/api/v3/link/carecontext"))
                .header("REQUEST-ID", UUID.randomUUID().toString())
                .header("TIMESTAMP", getTimeStamp())
                .header("Authorization", "Bearer " + getAccessToken())
                .header("X-HIP-ID", "HAD_01")
                .header("X-CM-ID", "sbx")
                .header("X-LINK-TOKEN", patient.getLinkToken())
                .bodyValue(req)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                            response -> response.bodyToMono(String.class).flatMap(body -> {
                                System.out.println("Error response body: " + body);
                                return null;
                            }))
                .bodyToMono(Void.class);
        }
        catch (Exception e) {
            System.out.println("Error in linkRecord service: " + e.getLocalizedMessage());
            throw e;
        }

    }

}
