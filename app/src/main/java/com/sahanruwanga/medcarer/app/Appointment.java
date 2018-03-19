package com.sahanruwanga.medcarer.app;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Sahan Ruwanga on 3/11/2018.
 */

public class Appointment implements Parcelable {
    private int appointmentId;
    private String reason;
    private String date;
    private String time;
    private String venue;
    private String doctor;
    private String clinicContact;
    private String notifyTime;
    private String created_at;

    public Appointment(){}

    protected Appointment(Parcel in){
        appointmentId = in.readInt();
        reason = in.readString();
        date = in.readString();
        time = in.readString();
        venue = in.readString();
        doctor = in.readString();
        clinicContact = in.readString();
        notifyTime = in.readString();
        created_at = in.readString();
    }

    public Appointment(int appointmentId, String reason, String date, String time,
                       String venue, String doctor, String clinicContact, String notifyTime, String created_at){
        this.setAppointmentId(appointmentId);
        this.setReason(reason);
        this.setDate(date);
        this.setTime(time);
        this.setVenue(venue);
        this.setDoctor(doctor);
        this.setClinicContact(clinicContact);
        this.setNotifyTime(notifyTime);
        this.created_at = created_at;
    }

    public static final Creator<Appointment> CREATOR = new Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel in) {
            return new Appointment(in);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };

    //region Getters and Setters
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getClinicContact() {
        return clinicContact;
    }

    public void setClinicContact(String clinicContact) {
        this.clinicContact = clinicContact;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(appointmentId);
        parcel.writeString(reason);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(venue);
        parcel.writeString(doctor);
        parcel.writeString(clinicContact);
        parcel.writeString(notifyTime);
        parcel.writeString(created_at);
    }
    //endregion
}
