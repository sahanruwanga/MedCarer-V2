package com.sahanruwanga.medcarer.Frament;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;

/**
 * Created by Sahan Ruwanga on 5/1/2018.
 */

public class UserProfileContactInfoFragment extends DialogFragment {
    private EditText phone;
    private EditText address;
    private Button saveBtn;
    private Button cancelBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user_profile_contact_info, null);
        this.setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        // Initializing EditTexts and Buttons
        this.phone = view.findViewById(R.id.contactInfoPhone);
        this.address = view.findViewById(R.id.contactInfoAddress);
        this.saveBtn = view.findViewById(R.id.contactInfoSave);
        this.cancelBtn = view.findViewById(R.id.contactInfoCancel);

        // Set onClick listeners for buttons
        getSaveBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContactInfo();
                dismiss();
            }
        });

        getCancelBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



        return view;
    }

    private void saveContactInfo(){
        Toast.makeText(getActivity(), getAddress().toString(), Toast.LENGTH_SHORT).show();
    }

    //region Getters and Setters
    public EditText getPhone() {
        return phone;
    }

    public void setPhone(EditText phone) {
        this.phone = phone;
    }

    public EditText getAddress() {
        return address;
    }

    public void setAddress(EditText address) {
        this.address = address;
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
    //endregion
}
