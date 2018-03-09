package com.sahanruwanga.medcarer.app;

/**
 * Created by Sahan Ruwanga on 3/8/2018.
 */

public class MedicalRecord {
    private String disease;
    private String medicine;
    private String duration;
    private String allergic;
    private String doctor;
    private String contact;
    private String description;
    private int record_id;

    public MedicalRecord(){}

    public MedicalRecord(int record_id, String disease, String medicine, String duration,
                         String allergic, String doctor, String contact, String description){
        this.setRecord_id(record_id);
        this.setDisease(disease);
        this.setMedicine(medicine);
        this.setDuration(duration);
        this.setAllergic(allergic);
        this.setDoctor(doctor);
        this.setContact(contact);
        this.setDescription(description);
    }


    //region Getters and Setters
    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAllergic() {
        return allergic;
    }

    public void setAllergic(String allergic) {
        this.allergic = allergic;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRecord_id() {
        return record_id;
    }

    public void setRecord_id(int record_id) {
        this.record_id = record_id;
    }
    //endregion
}
