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
import com.sahanruwanga.medcarer.app.User;
import com.sahanruwanga.medcarer.helper.DateTimeFormatting;

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
    private User user;
    private DateTimeFormatting dateTimeFormatting;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user_profile_personal_info, null);
        this.setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        this.dateTimeFormatting = new DateTimeFormatting();

        // Get User form activity
        Bundle bundle = getArguments();
        this.user =  bundle.getParcelable(User.USER);

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
                dismiss();
            }
        });

        // Fill data from Activity
        fillData();
        return view;
    }

    private void fillData(){
        String dob = getUser().getDob();
        if(dob != null) {
            getDobYear().setText(getUser().getDob().substring(0, 4));
            getDobDay().setText(getUser().getDob().substring(8));
            getSpinnerMonth().setSelection(Integer.parseInt(getUser().getDob().substring(5, 7)) - 1);
        }
        String gender = getUser().getGender();
        if(gender != null) {
            if (gender.equals("Male"))
                getSpinnerGender().setSelection(0);
            else
                getSpinnerGender().setSelection(1);
        }
        getBloodType().setText(getUser().getBloodType());
    }

    private void savePersonalInfo(){
            String dob2 = spinnerMonth.getSelectedItem().toString().trim() + " " +
                    getDobDay().getText().toString().trim() + ", " + getDobYear().getText().toString().trim();
            String gender = spinnerGender.getSelectedItem().toString().trim();
            String bloodType = getBloodType().getText().toString().trim();
            if(isDoBChanged(dob2) || !gender.equals(getUser().getGender()) ||
                    !bloodType.equals(getUser().getBloodType())) {

                new User(getActivity()).updateUserProfilePersonalInfo(
                        getDateTimeFormatting().getDateToSaveInDB(dob2), gender, bloodType);

                this.myProfileActivity = (MyProfileActivity) getActivity();
                getMyProfileActivity().updatePersonalInfo(dob2, gender, bloodType);
            }
    }

    private boolean isDoBChanged(String dob){
        boolean isChanged = false;
        String dob2 = getUser().getDob();
        if(dob2 != null) {
            if (!getDobYear().getText().toString().trim().equals(getUser().getDob().substring(0, 4)))
                isChanged = true;
            if(!getDobDay().getText().toString().trim().equals(getUser().getDob().substring(8)))
                isChanged = true;
            if(!getSpinnerMonth().getSelectedItem().equals(getUser().getDob().substring(5, 7)))
                isChanged = true;
        }else {
            if (dob.length() > 6)
                isChanged = true;
        }
        return isChanged;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DateTimeFormatting getDateTimeFormatting() {
        return dateTimeFormatting;
    }

    public void setDateTimeFormatting(DateTimeFormatting dateTimeFormatting) {
        this.dateTimeFormatting = dateTimeFormatting;
    }
    //endregion
}
