package com.sahanruwanga.medcarer.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import com.sahanruwanga.medcarer.R;

/**
 * Created by Sahan Ruwanga on 3/13/2018.
 */

public class UpdateMedicalRecordFragment extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.activity_update_medical_record, null);
        builder.setView(view);
        return builder.show();
    }

}
