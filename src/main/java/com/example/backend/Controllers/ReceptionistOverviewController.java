package com.example.backend.Controllers;

import com.example.backend.Models.ReceptionistOverviewModel;
import com.example.backend.Services.ReceptionistOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/receptionist/overview")
public class ReceptionistOverviewController {

    @Autowired
    private ReceptionistOverviewService receptionistOverviewService;
    @GetMapping("")
    public ResponseEntity<ReceptionistOverviewModel> getReceptionOverview() {
        try {
            ReceptionistOverviewModel receptionistOverview= receptionistOverviewService.getRecepOverviewData();
            return ResponseEntity.of(Optional.of(receptionistOverview));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
