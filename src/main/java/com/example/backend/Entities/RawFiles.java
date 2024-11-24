package com.example.backend.Entities;

import java.io.Serializable;

import com.example.backend.cryptography.ConverterUtil;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "raw_files")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RawFiles implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String txnId;

    @Convert(converter = ConverterUtil.class)
    private String path;

    @Convert(converter = ConverterUtil.class)
    private String name;

    @Convert(converter = ConverterUtil.class)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Records record;

    @ManyToOne(fetch = FetchType.LAZY)
    private ExternalRecords externalRecord;
}
