package com.sahanruwanga.medcarer.Frament;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.MyProfileActivity;
import com.sahanruwanga.medcarer.app.User;

/**
 * Created by Sahan Ruwanga on 5/1/2018.
 */

public class UserProfileNoteFragment extends DialogFragment {
    private EditText note;
    private Button saveBtn;
    private Button cancelBtn;
    private ImageView clearIcon;

    private User user;
    private MyProfileActivity myProfileActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user_profile_note, null);
        this.setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        // Get User form activity
        Bundle bundle = getArguments();
        this.user = bundle.getParcelable(User.USER);

        // Initializing EditText, ImageView icon and Buttons
        this.note = view.findViewById(R.id.userProfileNote);
        this.saveBtn = view.findViewById(R.id.userProfileNoteSave);
        this.cancelBtn = view.findViewById(R.id.userProfileNoteCancel);
        this.clearIcon = view.findViewById(R.id.userProfileNoteClear);

        // OnClick listeners for buttons and icon
        getSaveBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
                dismiss();
            }
        });

        getCancelBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        getClearIcon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearNote();
            }
        });

        fillData();
        return view;
    }


    private void fillData(){
        getNote().setText(getUser().getNote());
    }

    private void saveNote(){
        String note = getNote().getText().toString().trim();
        if(!note.equals(getUser().getNote())){
            new User(getActivity()).updateUserNote(note);

            this.myProfileActivity = (MyProfileActivity) getActivity();
            getMyProfileActivity().updateNote(note);
        }
    }

    private void clearNote(){
        getNote().setText("");
    }

    //region Getters and Setters
    public EditText getNote() {
        return note;
    }

    public void setNote(EditText note) {
        this.note = note;
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

    public ImageView getClearIcon() {
        return clearIcon;
    }

    public void setClearIcon(ImageView clearIcon) {
        this.clearIcon = clearIcon;
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
