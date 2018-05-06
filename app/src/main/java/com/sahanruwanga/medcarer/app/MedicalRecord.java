package com.sahanruwanga.medcarer.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sahan Ruwanga on 3/8/2018.
 */

public class MedicalRecord implements Parcelable{
    private String disease;
    private String medicine;
    private String duration;
    private String allergic;
    private String doctor;
    private String contact;
    private String description;
    private String createdAt;
    private int record_id;
    private int syncStatus;
    private int statusType;

    public static final String MEDICAL_RECORD = "medicalRecord";

    public MedicalRecord(){}

    public MedicalRecord(int record_id, String disease, String medicine, String duration, String allergic,
                         String doctor, String contact, String description, int syncStatus){
        this.setRecord_id(record_id);
        this.setDisease(disease);
        this.setMedicine(medicine);
        this.setDuration(duration);
        this.setAllergic(allergic);
        this.setDoctor(doctor);
        this.setContact(contact);
        this.setDescription(description);
        this.syncStatus = syncStatus;
    }


    protected MedicalRecord(Parcel in) {
        disease = in.readString();
        medicine = in.readString();
        duration = in.readString();
        allergic = in.readString();
        doctor = in.readString();
        contact = in.readString();
        description = in.readString();
        createdAt = in.readString();
        record_id = in.readInt();
        syncStatus = in.readInt();
        statusType = in.readInt();
    }

    public static final Creator<MedicalRecord> CREATOR = new Creator<MedicalRecord>() {
        @Override
        public MedicalRecord createFromParcel(Parcel in) {
            return new MedicalRecord(in);
        }

        @Override
        public MedicalRecord[] newArray(int size) {
            return new MedicalRecord[size];
        }
    };

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

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatusType() {
        return statusType;
    }

    public void setStatusType(int statusType) {
        this.statusType = statusType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(disease);
        parcel.writeString(medicine);
        parcel.writeString(duration);
        parcel.writeString(allergic);
        parcel.writeString(doctor);
        parcel.writeString(contact);
        parcel.writeString(description);
        parcel.writeString(createdAt);
        parcel.writeInt(record_id);
        parcel.writeInt(syncStatus);
        parcel.writeInt(statusType);
    }
    //endregion
}
