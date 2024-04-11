package com.example.backend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Models.abdm.GenerateLinkingTokenRes;
import com.example.backend.Models.abdm.LinkCareContextRes;
import com.example.backend.Services.CallbackServices;

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

}
