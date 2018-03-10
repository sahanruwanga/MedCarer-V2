package com.sahanruwanga.medcarer.app;

/**
 * Created by Sahan Ruwanga on 3/11/2018.
 */

public class Appointment {
    private int appointmentId;
    private String reason;
    private String date;
    private String time;
    private String venue;
    private String doctor;
    private String clinicContact;
    private String notifyTime;

    public Appointment(){}

    public Appointment(int appointmentId, String reason, String date, String time,
                       String venue, String doctor, String clinicContact, String notifyTime){
        this.setAppointmentId(appointmentId);
        this.setReason(reason);
        this.setDate(date);
        this.setTime(time);
        this.setVenue(venue);
        this.setDoctor(doctor);
        this.setClinicContact(clinicContact);
        this.setNotifyTime(notifyTime);
    }

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
    //endregion
}
