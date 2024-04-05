package com.example.backend.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;


@Controller
@CrossOrigin
public class ABDMController_Shrutik {

    @PostMapping(value = "/v0.5/users/auth/on-fetch-modes")
    public ResponseEntity<String> requestMethodName(@RequestBody HashMap<String, Object> req) {
        System.out.println("Body got");
        System.out.println(req);
        return ResponseEntity.ok().body("ok");
    }

}
