package com.sahanruwanga.medcarer.app;

/**
 * Created by Sahan Ruwanga on 5/12/2018.
 */

public class AlternativeMedicine {
    private String medicine;
    private String genericName;
    private String price;
    private String note;
    private int medicineId;

    public AlternativeMedicine(){}

    public AlternativeMedicine(int medicineId, String medicine, String genericName, String price,
                               String note){
        this.medicineId = medicineId;
        this.medicine = medicine;
        this.genericName = genericName;
        this.price = price;
        this.note = note;
    }


    //region Getters and Setters
    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }
    //endregion
}
