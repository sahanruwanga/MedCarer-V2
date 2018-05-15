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
import com.sahanruwanga.medcarer.activity.MyProfileActivity;
import com.sahanruwanga.medcarer.app.User;

/**
 * Created by Sahan Ruwanga on 5/1/2018.
 */

public class UserProfileNameFragment extends DialogFragment {
    private EditText name;
    private Button saveBtn;
    private Button cancelBtn;

    private User user;
    private MyProfileActivity myProfileActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user_profile_name, null);
        this.setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        // Get User form activity
        Bundle bundle = getArguments();
        this.user = bundle.getParcelable(User.USER);

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
                dismiss();
            }
        });

        fillData();
        return view;
    }

    private void fillData(){
        getName().setText(getUser().getName());
    }

    private void saveName(){
        String name = getName().getText().toString().trim();
        if(!name.equals(getUser().getName())){
            new User(getActivity()).updateUserName(name);

            this.myProfileActivity = (MyProfileActivity) getActivity();
            getMyProfileActivity().updateName(name);
        }
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MyProfileActivity getMyProfileActivity() {
        return myProfileActivity;
    }

    public void setMyProfileActivity(MyProfileActivity myProfileActivity) {
        this.myProfileActivity = myProfileActivity;
    }
    //endregion
}
