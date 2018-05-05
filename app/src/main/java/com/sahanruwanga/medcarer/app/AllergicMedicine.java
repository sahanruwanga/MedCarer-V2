package com.sahanruwanga.medcarer.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sahan Ruwanga on 3/22/2018.
 */

public class AllergicMedicine implements Parcelable{
    private int allergicMedicineId;
    private String medicineName;
    private String description;
    private String createdAt;
    private int syncStatus;
    private int statusType;

    public static final String ALLERGIC_MEDICINE = "allergicMedicine";

    public AllergicMedicine(){}

    protected AllergicMedicine(Parcel in) {
        allergicMedicineId = in.readInt();
        medicineName = in.readString();
        description = in.readString();
        createdAt = in.readString();
        syncStatus = in.readInt();
        statusType = in.readInt();
    }

    public static final Creator<AllergicMedicine> CREATOR = new Creator<AllergicMedicine>() {
        @Override
        public AllergicMedicine createFromParcel(Parcel in) {
            return new AllergicMedicine(in);
        }

        @Override
        public AllergicMedicine[] newArray(int size) {
            return new AllergicMedicine[size];
        }
    };

    //region Getters and Setters
    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getAllergicMedicineId() {
        return allergicMedicineId;
    }

    public void setAllergicMedicineId(int allergicMedicineId) {
        this.allergicMedicineId = allergicMedicineId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(allergicMedicineId);
        parcel.writeString(medicineName);
        parcel.writeString(description);
        parcel.writeString(createdAt);
        parcel.writeInt(syncStatus);
        parcel.writeInt(statusType);
    }
    //endregion
}
