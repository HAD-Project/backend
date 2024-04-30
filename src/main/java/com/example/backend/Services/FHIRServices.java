package com.example.backend.Services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MedicationRequest.MedicationRequestIntent;
import org.hl7.fhir.r4.model.MedicationRequest.MedicationRequestStatus;
import org.springframework.stereotype.Service;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Composition.CompositionStatus;
import org.hl7.fhir.r4.model.Composition.SectionComponent;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;

import com.example.backend.Entities.Doctors;
import com.example.backend.Entities.Patients;
import com.example.backend.Models.PrescriptionModel;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ValidationResult;


@Service
public class FHIRServices {

    public String createPrescription(Doctors doctor, Patients patient, List<PrescriptionModel> prescriptionList) {

        String bundleId = UUID.randomUUID().toString();
        String compositionId = UUID.randomUUID().toString();

        Bundle bundle = new Bundle();
        bundle.setId(bundleId);
        bundle.setMeta(new Meta().setLastUpdated(new Date()));
        bundle.setIdentifier(new Identifier().setSystem("https://www.max.in/bundle").setValue(UUID.randomUUID().toString()));
        bundle.setType(BundleType.DOCUMENT);
        bundle.setTimestamp(new Date());
        List<BundleEntryComponent> entry = new ArrayList<>();

        Composition composition = new Composition();
        composition.setId(compositionId);
        composition.setIdentifier(
            new Identifier().setSystem("https://www.max.in/document").setValue(UUID.randomUUID().toString())
        );
        composition.setStatus(CompositionStatus.FINAL);
        composition.setType(
            new CodeableConcept()
            .setCoding(
                Arrays.asList(
                    new Coding()
                    .setSystem("https://projecteka.in/sct")
                    .setCode("440545006")
                    .setDisplay("Prescription Record")
                )
            )
        );

        composition.setSubject(
            new Reference().setReference("Patient/" + patient.getPatientId())
        );
        composition.setDate(new Date());
        composition.setAuthor(
            Arrays.asList(
                new Reference()
                .setReference("Practitioner/" + doctor.getDoctorId())
                .setDisplay(doctor.getUser().getName())
            )
        );
        composition.setTitle("Prescription");
        composition.setSection(
            Arrays.asList(
                new SectionComponent()
                .setTitle("OPD Prescription")
                .setCode(
                    new CodeableConcept()
                    .setCoding(
                        Arrays.asList(
                            new Coding()
                            .setSystem("https://projecteka.in/sct")
                            .setCode("440545006")
                            .setDisplay("Prescription record")
                        )
                    )
                )
                .setEntry(new ArrayList<>())
            )
        );

        BundleEntryComponent compositionEntry = new BundleEntryComponent();
        compositionEntry.setFullUrl("Composition/" + UUID.randomUUID().toString());
        compositionEntry.setResource(composition);
        entry.add(compositionEntry);


        Practitioner practitioner = new Practitioner();
        practitioner.setId(Integer.toString(doctor.getDoctorId()));
        practitioner.setIdentifier(
            Arrays.asList(
                new Identifier()
                .setSystem("https://www.mciindia.in/doctor")
                .setValue("Practitioner/" + Integer.toString(doctor.getDoctorId()))
            )
        );
        
        practitioner.setName(
            Arrays.asList(
                new HumanName()
                .setText("Name")
                .setPrefix(Arrays.asList(new StringType("Dr")))
                .setSuffix(Arrays.asList(new StringType(doctor.getQualifications())))
            )
        );


        BundleEntryComponent practitionerComponent = new BundleEntryComponent();
        practitionerComponent.setFullUrl("Practitioner/" + doctor.getDoctorId());
        practitionerComponent.setResource(practitioner);
        entry.add(practitionerComponent);


        Patient pat = new Patient();
        pat.setId(Integer.toString(patient.getPatientId()));
        pat.setName(Arrays.asList(new HumanName().setText(patient.getName())));
        pat.setGender(patient.getGender().toLowerCase().charAt(0) == 'm' ? AdministrativeGender.MALE : AdministrativeGender.FEMALE);

        BundleEntryComponent patientComponent = new BundleEntryComponent();
        patientComponent.setFullUrl(UUID.randomUUID().toString());
        patientComponent.setResource(pat);
        entry.add(patientComponent);

        for(PrescriptionModel prescription: prescriptionList) {

            String medicationId = UUID.randomUUID().toString();
            Medication medication = new Medication();
            medication.setId(medicationId);
            medication.setCode(
                new CodeableConcept()
                .setCoding(
                    Arrays.asList(
                        new Coding()
                        .setSystem("https://projecteka.in/act")
                        .setCode(prescription.getCode())
                        .setDisplay(prescription.getName())
                    )
                )
            );
            BundleEntryComponent medicationComponent = new BundleEntryComponent();
            medicationComponent.setFullUrl("Medication/" + medicationId);
            medicationComponent.setResource(medication);
            entry.add(medicationComponent);

            MedicationRequest medicationRequest = new MedicationRequest();
            medicationRequest.setId(UUID.randomUUID().toString());
            medicationRequest.setStatus(MedicationRequestStatus.ACTIVE);
            medicationRequest.setIntent(MedicationRequestIntent.ORDER);
            medicationRequest.setMedication(new Reference(medication));
            medicationRequest.setSubject(new Reference().setReference("Patient/" + patient.getPatientId()));
            medicationRequest.setRequester(new Reference().setReference("Practitioner/" + doctor.getDoctorId()));
            medicationRequest.setAuthoredOn(new Date());
            medicationRequest.setDosageInstruction(
                Arrays.asList(
                    new Dosage().setText(prescription.getInstruction())
                )
            );
            composition.getSection().get(0).getEntry().add(new Reference(medicationRequest));
            BundleEntryComponent medicationRequesComponent = new BundleEntryComponent();
            medicationRequesComponent.setFullUrl("MedicationRequest/" + UUID.randomUUID().toString());
            medicationRequesComponent.setResource(medicationRequest);
            entry.add(medicationRequesComponent);
        }

        bundle.setEntry(entry);

        FhirContext ctx = FhirContext.forR4();
        String prescriptioString = ctx.newJsonParser().encodeResourceToString(bundle);

        FhirValidator validator = ctx.newValidator();
        IValidatorModule module = new FhirInstanceValidator(ctx);
        validator.registerValidatorModule(module);

        ValidationResult result = validator.validateWithResult(prescriptioString);

        System.out.println("Success: " + result.isSuccessful());
        System.out.println("Messages: " + result.getMessages());
        
        return prescriptioString;
    }

    public List<PrescriptionModel> toPrescriptionModel(String prescriptionString) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        Bundle bundle = parser.parseResource(Bundle.class, prescriptionString);

        List<PrescriptionModel> res = new ArrayList<>();

        List<BundleEntryComponent> entries = bundle.getEntry();

        List<MedicationRequest> medicationRequests = new ArrayList<>();
        HashMap<String, Medication> medicationMap = new HashMap<>();
        
        for(BundleEntryComponent e: entries) {
            Resource resource = e.getResource();
            if(resource instanceof Composition) {

            }
            else if(resource instanceof Practitioner) {

            }
            else if(resource instanceof Patient) {

            }
            else if(resource instanceof Medication) {
                Medication medication = (Medication)resource;
                medicationMap.put(medication.getId(), medication);
            }
            else if(resource instanceof MedicationRequest) {
                MedicationRequest medicationRequest = (MedicationRequest)resource;
                medicationRequests.add(medicationRequest);
            }
            else {
                System.out.println("Found unknown resource of type while parsing prescription: " + resource.getResourceType().toString());
            }
        }
        System.out.println("Medication map: " + medicationMap);
        for(MedicationRequest medicationRequest: medicationRequests) {
            String medicationId = medicationRequest.getMedicationReference().getReferenceElement().getValue();
            System.out.println("Medication id: " + medicationId);
            PrescriptionModel toAdd = new PrescriptionModel();
            Medication medication = medicationMap.get(medicationId);
            toAdd.setId(medicationId);
            toAdd.setCode(medication.getCode().getCoding().get(0).getCode());
            toAdd.setName(medication.getCode().getCoding().get(0).getDisplay());
            toAdd.setInstruction(medicationRequest.getDosageInstruction().get(0).getText());

            res.add(toAdd);
        }

        return res;
    }
}
