package com.sahanruwanga.medcarer.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sahan Ruwanga on 3/23/2018.
 */

public class MedicationSchedule implements Parcelable {

    private int scheduleId;
    private String medicine;
    private String quantity;
    private String startTime;
    private String period;
    private String notifyTime;
    private String nextNotifyTime;
    private String createdAt;
    private int notificationStatus;
    private int syncStatus;
    private int statusType;

    // Constructor call
    public MedicationSchedule(){}

    protected MedicationSchedule(Parcel in) {
        scheduleId = in.readInt();
        medicine = in.readString();
        quantity = in.readString();
        startTime = in.readString();
        period = in.readString();
        notifyTime = in.readString();
        createdAt = in.readString();
    }

    public static final Creator<MedicationSchedule> CREATOR = new Creator<MedicationSchedule>() {
        @Override
        public MedicationSchedule createFromParcel(Parcel in) {
            return new MedicationSchedule(in);
        }

        @Override
        public MedicationSchedule[] newArray(int size) {
            return new MedicationSchedule[size];
        }
    };

    //region Getters and Setters
    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(scheduleId);
        parcel.writeString(medicine);
        parcel.writeString(quantity);
        parcel.writeString(startTime);
        parcel.writeString(period);
        parcel.writeString(notifyTime);
        parcel.writeString(createdAt);
    }

    public int getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(int notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }

    public int getStatusType() {
        return statusType;
    }

    public void setStatusType(int statusType) {
        this.statusType = statusType;
    }

    public String getNextNotifyTime() {
        return nextNotifyTime;
    }

    public void setNextNotifyTime(String nextNotifyTime) {
        this.nextNotifyTime = nextNotifyTime;
    }
    //endregion
}
