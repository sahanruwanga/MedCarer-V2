package com.sahanruwanga.medcarer.activity;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sahanruwanga.medcarer.Frament.UserProfileContactInfoFragment;
import com.sahanruwanga.medcarer.Frament.UserProfileNameFragment;
import com.sahanruwanga.medcarer.Frament.UserProfileNoteFragment;
import com.sahanruwanga.medcarer.Frament.UserProfilePersonalInfoFragment;
import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.app.User;

public class MyProfileActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView ageTextView;
    private TextView dobTextView;
    private TextView genderTextView;
    private TextView bloodTypeTextView;
    private TextView phoneNoTextView;
    private TextView addressTextView;
    private TextView emailTextView;
    private TextView noteTextView;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        this.nameTextView = findViewById(R.id.myProfileName);
        this.ageTextView = findViewById(R.id.myProfileAge);
        this.dobTextView = findViewById(R.id.myProfileDOB);
        this.genderTextView = findViewById(R.id.myProfileGender);
        this.bloodTypeTextView = findViewById(R.id.myProfileBloodType);
        this.phoneNoTextView = findViewById(R.id.myProfilePhoneNo);
        this.addressTextView = findViewById(R.id.myProfileAddress);
        this.emailTextView = findViewById(R.id.myProfileEmail);
        this.noteTextView = findViewById(R.id.myProfileNote);


        loadData();
    }

    private void loadData(){
        if(getUser() == null)
            this.user = new User(this);
        getNameTextView().setText(getUser().getUserProfile().getName());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    //region Getters and Setters
    public TextView getDobTextView() {
        return dobTextView;
    }

    public void setDobTextView(TextView dobTextView) {
        this.dobTextView = dobTextView;
    }

    public TextView getGenderTextView() {
        return genderTextView;
    }

    public void setGenderTextView(TextView genderTextView) {
        this.genderTextView = genderTextView;
    }

    public TextView getBloodTypeTextView() {
        return bloodTypeTextView;
    }

    public void setBloodTypeTextView(TextView bloodTypeTextView) {
        this.bloodTypeTextView = bloodTypeTextView;
    }

    public TextView getPhoneNoTextView() {
        return phoneNoTextView;
    }

    public void setPhoneNoTextView(TextView phoneNoTextView) {
        this.phoneNoTextView = phoneNoTextView;
    }

    public TextView getAddressTextView() {
        return addressTextView;
    }

    public void setAddressTextView(TextView addressTextView) {
        this.addressTextView = addressTextView;
    }

    public TextView getEmailTextView() {
        return emailTextView;
    }

    public void setEmailTextView(TextView emailTextView) {
        this.emailTextView = emailTextView;
    }

    public TextView getNoteTextView() {
        return noteTextView;
    }

    public void setNoteTextView(TextView noteTextView) {
        this.noteTextView = noteTextView;
    }

    public TextView getNameTextView() {
        return nameTextView;
    }

    public void setNameTextView(TextView nameTextView) {
        this.nameTextView = nameTextView;
    }

    public TextView getAgeTextView() {
        return ageTextView;
    }

    public void setAgeTextView(TextView ageTextView) {
        this.ageTextView = ageTextView;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //endregion

    public void openPersonalInfoForm(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        UserProfilePersonalInfoFragment userProfilePersonalInfoFragment = new UserProfilePersonalInfoFragment();
        userProfilePersonalInfoFragment.show(fragmentManager, "PersonalInfo");

    }

    public void openContactInfoForm(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        UserProfileContactInfoFragment userProfileContactInfoFragment = new UserProfileContactInfoFragment();
        userProfileContactInfoFragment.show(fragmentManager, "ContactInfo");
    }

    public void openNoteForm(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        UserProfileNoteFragment noteFragment = new UserProfileNoteFragment();
        noteFragment.show(fragmentManager, "Note");
    }

    public void openNameForm(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        UserProfileNameFragment nameFragment = new UserProfileNameFragment();
        nameFragment.show(fragmentManager, "Note");
    }

    public void chooseImage(View view) {
    }

    public void updatePersonaloInfo(String dob, String gender, String bloodType){
        getDobTextView().setText(dob);
        getGenderTextView().setText(gender);
        getBloodTypeTextView().setText(bloodType);
    }
}
