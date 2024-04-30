package com.example.backend.Models.frontend;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestRecords {
    private String abhaId;
    private Date expiryDate;
    private String explanation;
    private Date fromDate;
    private String metaCode;
    private String patientName;
    private Date toDate;
    private List<String> recordType;    
}
