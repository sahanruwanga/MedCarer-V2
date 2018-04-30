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

public class UserProfileNameFragment extends DialogFragment {
    private EditText name;
    private Button saveBtn;
    private Button cancelBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user_profile_name, null);

        this.setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        // Initializing EditText and Buttons
        this.name = view.findViewById(R.id.userProfileName);
        this.saveBtn = view.findViewById(R.id.userProfileNameSave);
        this.cancelBtn = view.findViewById(R.id.userProfileNameCancel);

        // OnClick Listeners for buttons
        getSaveBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveName();
                dismiss();
            }
        });

        getCancelBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelName();
                dismiss();
            }
        });

        return view;
    }

    private void saveName(){
        Toast.makeText(getActivity(), "Save Button", Toast.LENGTH_SHORT).show();
    }

    private void cancelName(){
        Toast.makeText(getActivity(), "Cancel Button", Toast.LENGTH_SHORT).show();
    }

    //region Getters and Setters
    public EditText getName() {
        return name;
    }

    public void setName(EditText name) {
        this.name = name;
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
