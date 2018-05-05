package com.sahanruwanga.medcarer.Frament;

import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.MyProfileActivity;

/**
 * Created by Sahan Ruwanga on 5/1/2018.
 */

public class UserProfilePersonalInfoFragment extends DialogFragment{
    private Spinner spinnerMonth;
    private Spinner spinnerGender;
    private EditText dobDay;
    private EditText dobYear;
    private EditText bloodType;
    private Button saveBtn;
    private Button cancelBtn;

    private MyProfileActivity myProfileActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user_profile_personal_info, null);
        this.setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        // Month Spinner initializing
        this.spinnerMonth = view.findViewById(R.id.personalInfoMonth);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterMonth = ArrayAdapter.createFromResource(getActivity(),
                R.array.month_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        getSpinnerMonth().setAdapter(adapterMonth);

        // Gender Spinner initializing
        this.spinnerGender = view.findViewById(R.id.personalInfoGender);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        getSpinnerGender().setAdapter(adapterGender);

        // Initializing EditTexts
        this.dobDay = view.findViewById(R.id.personalInfoDate);
        this.dobYear = view.findViewById(R.id.personalInfoYear);
        this.bloodType = view.findViewById(R.id.personalInfoBloodType);

        // Initializing buttons and their onClic events
        this.saveBtn = view.findViewById(R.id.personalInfoSave);
        this.cancelBtn = view.findViewById(R.id.personalInfoCancel);

        getSaveBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePersonalInfo();
                dismiss();
            }
        });

        getCancelBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelPersonalInfo();
                dismiss();
            }
        });
        return view;
    }

    private void savePersonalInfo(){
//        Toast.makeText(getActivity(), "Save Button", Toast.LENGTH_SHORT).show();
        String dob = spinnerMonth.getSelectedItem().toString().trim() + " " +
                getDobDay().getText().toString().trim() + ", " + getDobYear().getText().toString().trim();


        if(getMyProfileActivity() == null)
            setMyProfileActivity((MyProfileActivity) getActivity());
        getMyProfileActivity().updatePersonaloInfo(dob, spinnerGender.getSelectedItem().toString().trim(),
                getBloodType().getText().toString().trim());
    }

    private void cancelPersonalInfo(){
        Toast.makeText(getActivity(), "Cancel Button", Toast.LENGTH_SHORT).show();

    }

    // region Getters and Setters
    public Spinner getSpinnerMonth() {
        return spinnerMonth;
    }

    public void setSpinnerMonth(Spinner spinnerMonth) {
        this.spinnerMonth = spinnerMonth;
    }

    public Spinner getSpinnerGender() {
        return spinnerGender;
    }

    public void setSpinnerGender(Spinner spinnerGender) {
        this.spinnerGender = spinnerGender;
    }

    public EditText getDobDay() {
        return dobDay;
    }

    public void setDobDay(EditText dobDay) {
        this.dobDay = dobDay;
    }

    public EditText getDobYear() {
        return dobYear;
    }

    public void setDobYear(EditText dobYear) {
        this.dobYear = dobYear;
    }

    public EditText getBloodType() {
        return bloodType;
    }

    public void setBloodType(EditText bloodType) {
        this.bloodType = bloodType;
    }

    public Button getSaveBtn() {
        return saveBtn;
    }

    public void setSaveBtn(Button saveBtn) {
        this.saveBtn = saveBtn;
    }

    public Button getCancelBtn() {
        return cancelBtn;
    }

    public void setCancelBtn(Button cancelBtn) {
        this.cancelBtn = cancelBtn;
    }

    public MyProfileActivity getMyProfileActivity() {
        return myProfileActivity;
    }

    public void setMyProfileActivity(MyProfileActivity myProfileActivity) {
        this.myProfileActivity = myProfileActivity;
    }
    //endregion
}
