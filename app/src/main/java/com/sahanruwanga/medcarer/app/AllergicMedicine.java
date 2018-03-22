package com.sahanruwanga.medcarer.app;

/**
 * Created by Sahan Ruwanga on 3/22/2018.
 */

public class AllergicMedicine {
    private int allergicMedicineId;
    private String medicineName;
    private String description;
    private String createdAt;

    public AllergicMedicine(){}

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
    //endregion
}
