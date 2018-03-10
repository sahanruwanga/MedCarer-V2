package com.sahanruwanga.medcarer.app;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;


/**
 * Created by Sahan Ruwanga on 3/9/2018.
 */

public class PDFCreator extends AppCompatActivity{
    private List<MedicalRecord> medicalRecords;
    private static final String TAG = "PDFCreator";
    private final int REQUEST_CODE_ASK_PERMISSION = 111;
    Context context;
    private File pdfFile;

    public PDFCreator(List<MedicalRecord> medicalRecords, Context context){
        this.medicalRecords = medicalRecords;
        this.context = context;
    }

    public void convertToPDF(){
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)){
                    showMessageOKCAncel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSION);
                                    }
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSION);
            }
            return;
        }else {
            try {
                createPdf();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    private void createPdf() throws FileNotFoundException, DocumentException {
        File docFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if(!docFolder.exists()){
            docFolder.mkdir();
            Log.i(TAG, "Created a new directory for pdf");
        }

        pdfFile = new File(docFolder.getAbsolutePath(), "MedicalHistory.pdf");
        OutputStream output = new FileOutputStream(pdfFile);

        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();
        document.add(new Paragraph("Hi Sahan, DO NOT worry give it try again"));

        document.close();
        previewpdf();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    convertToPDF();
                } else {
                    Toast.makeText(context, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void previewpdf(){
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if(list.size() > 0){
            Intent intnt =new Intent();
            intnt.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intnt.setDataAndType(uri, "application/pdf");
            startActivity(intnt);
        }else{
            Toast.makeText(this, "Download a PDF viewer to see the PDF", Toast.LENGTH_LONG).show();
        }
    }

    private void showMessageOKCAncel(String messsage, DialogInterface.OnClickListener okLisrener){
        new AlertDialog.Builder(context)
                .setMessage(messsage)
                .setPositiveButton("ok", okLisrener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }
}
