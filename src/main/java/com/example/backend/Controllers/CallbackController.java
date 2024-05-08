package com.example.backend.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Models.abdm.ConsentReqNotify;
import com.example.backend.Models.abdm.HIUConsentReqNotify;
import com.example.backend.Models.abdm.ConsentReqOnInit;
import com.example.backend.Models.abdm.DataTransferOnReq;
import com.example.backend.Models.abdm.DataTransferReq;
import com.example.backend.Models.abdm.DataTransferRes;
import com.example.backend.Models.abdm.GenerateLinkingTokenRes;
import com.example.backend.Models.abdm.LinkCareContextRes;
import com.example.backend.Models.abdm.PatientCareContextDiscoverReq;
import com.example.backend.Services.CallbackServices;

import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
public class CallbackController {

    @Autowired
    CallbackServices callbackServices;

    @PostMapping("/api/v3/hip/token/on-generate-token")
    public ResponseEntity<Void> onGenerateLinkingToken(@RequestBody GenerateLinkingTokenRes req) {
        try {
            callbackServices.onGenerateLinkingToken(req);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            System.out.println("Error occurred in on-generate-token: " + e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/api/v3/link/on_carecontext")
    public ResponseEntity<Void> onLinkCareContext(@RequestBody LinkCareContextRes res) {
        System.out.println("Status: " + res.getStatus());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v3/hip/patient/care-context/discover")
    public Mono<ResponseEntity<Void>> onPatientDiscoverCareContext(@RequestHeader Map<String, String> allHeaders, @RequestBody PatientCareContextDiscoverReq req) throws Exception {
        return callbackServices.onPatientDiscoverCareContext(allHeaders.get("request-id"), req)
            .then(Mono.just(ResponseEntity.ok().<Void>build()))
            .onErrorResume(e -> {
                System.out.println("Error in CallbackController->onPatientDiscoverCareContext: " + e.getLocalizedMessage());
                return Mono.just(ResponseEntity.internalServerError().<Void>build());
            });
    }

    @PostMapping("/api/v3/hip/health-information/request")
    public ResponseEntity<Void> transferHealthData(@RequestBody DataTransferReq req) {
        callbackServices.transferHealthData(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v3/hip/health-information/request/dummy")
    public ResponseEntity<Void> transferHealthDataDummy(@RequestBody DataTransferReq req) {
        callbackServices.transferHealthDataDummy(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v3/hiu/consent/request/on-init")
    public ResponseEntity<Void> consentRequestOnInit(@RequestBody ConsentReqOnInit req) {
        callbackServices.consentRequestOnInit(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v3/hiu/consent/request/notify")
    public ResponseEntity<Void> consentRequestNotify(@RequestBody HIUConsentReqNotify req) {
        callbackServices.consentRequestNotify(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v3/hiu/consent/request/notify/dummy")
    public ResponseEntity<Void> consentRequestNotifyDummy(@RequestBody HIUConsentReqNotify req) {
        callbackServices.consentRequestNotifyDummy(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v3/hiu/health-information/on-request")
    public ResponseEntity<Void> healthInformationOnRequest(@RequestBody DataTransferOnReq req) {
        callbackServices.healthInformationOnRequest(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v0.5/health-information/hip/request")
    public ResponseEntity<Void> hipHealthInformationRequest(@RequestBody DataTransferReq req) {
        callbackServices.hipHealthInformationRequest(req);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/v0.5/health-information/transfer")
    public ResponseEntity<Void> healthInformationTransfer(@RequestBody DataTransferRes req) {
        try {
            callbackServices.receiveHealthData(req);
        }
        catch (Exception e) {
            System.out.println("Error in CallbackController->heathInformationTransfer: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v3/consent/request/hip/notify")
    public ResponseEntity<Void> hipConsentNotify(@RequestHeader(value = "request-id") String reqId, @RequestBody ConsentReqNotify req) {
        callbackServices.hipConsentNotify(reqId, req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v3/consent/request/hip/notify/dummy")
    public ResponseEntity<Void> hipConsentNotifyDummy(@RequestHeader(value = "request-id") String reqId, @RequestBody ConsentReqNotify req) {
        callbackServices.hipConsentNotifyDummy(reqId, req);
        return ResponseEntity.ok().build();
    }
}
