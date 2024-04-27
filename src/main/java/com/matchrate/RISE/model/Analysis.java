package com.matchrate.RISE.model;


import jakarta.persistence.*;
import jakarta.persistence.GenerationType;

//import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "target_table")
public class Analysis {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name= "facility_id")
    private int facilityID;

    @Column(name = "Person_uuid")
    private UUID personUuid;

    @Column(name = "biometric_id")
    private UUID biometricId;

    @Column(columnDefinition= "bytea", name ="baseline")
    private byte[] biometricData;

    @Column(name ="enrollment_date")
    private Date enrollmentDate;

    @Column(name = "template_type")
    private String templateType;

    @Column(name="archived")
    private int archived;

    private int recapture;

    @Column(name="replacement_date")
    private Date replacementDate;

    @Column(name="match_status")
    private String matchStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(int facilityID) {
        this.facilityID = facilityID;
    }

    public UUID getPersonUuid() {
        return personUuid;
    }

    public void setPersonUuid(UUID personUuid) {
        this.personUuid = personUuid;
    }

    public UUID getBiometricId() {
        return biometricId;
    }

    public void setBiometricId(UUID biometricId) {
        this.biometricId = biometricId;
    }

    public byte[] getBiometricData() {
        return biometricData;
    }

    public void setBiometricData(byte[] biometricData) {
        this.biometricData = biometricData;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public int getArchived() {
        return archived;
    }

    public void setArchived(int archived) {
        this.archived = archived;
    }

    public int getRecapture() {
        return recapture;
    }

    public void setRecapture(int recapture) {
        this.recapture = recapture;
    }

    public Date getReplacementDate() {
        return replacementDate;
    }

    public void setReplacementDate(Date replacementDate) {
        this.replacementDate = replacementDate;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }


    // Getters and setters
}
