package com.example.backend.Models.abdm;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CareContextPatient {
    private String referenceNumber;
    private String display;
    List<CareContextModel> careContexts;
    private String hiType;
    private int count;

    @Override
    public String toString() {
        return "CareContextPatient [referenceNumber=" + referenceNumber + ", display=" + display + ", careContexts="
                + careContexts + ", hiType=" + hiType + ", count=" + count + "]";
    }

}
