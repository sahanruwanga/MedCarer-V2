package com.sahanruwanga.medcarer.app;

import android.database.sqlite.SQLiteDatabase;

import com.sahanruwanga.medcarer.helper.SQLiteHandler;

/**
 * Created by Sahan Ruwanga on 3/7/2018.
 */

public class User {
    private static String userId;
    private String name;
    private String address;
    private String bod;
    private String phoneNo;
    private String image;
    private String gender;
    private String email;
    private String password;

    public void register(){}

    public void updateUserProfile(){}

    private void changeImage(){}

    public void login(){}

    public void logout(){}

    public void seeMedicalHistory(){}

    public void seeAppointment(){}

    public void seeMedicationSchedule(){}

    public void seeAllergicMedicine(){}

    public void searchAlternative(){}

    public void shareMedicalHistory(){}

    public void saveMediaclHistory(){}

    public void downloadUserManual(){}

    public void readAboutApp(){}

    public void readAboutDevSR(){}

    public void rateApp(){}

    //region Getters and Setters
    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        User.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBod() {
        return bod;
    }

    public void setBod(String bod) {
        this.bod = bod;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    //endregion
}
