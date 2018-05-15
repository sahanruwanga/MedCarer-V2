package com.sahanruwanga.medcarer.app;

import android.os.Environment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Sahan Ruwanga on 3/9/2018.
 */

public class PDFCreator {
    private List<MedicalRecord> medicalRecords;
    private User user;

    private static final String TITLE = "Medical History Report";
    private static final String DATE_TEXT = "Date: ";
    private static final String AGE_TEXT = "Age - ";
    private static final String PAGE_TEXT = "Page ";
    private static final String DISEASE = "Disease";
    private static final String MEDICINE = "Medicine";
    private static final String ALLERGIC = "Allergic";
    private static final String DURATION = "Duration";
    private static final String DESCRIPTION = "Description";
    private static final String CONTACT = "Contact";
    private static final String DOCTOR = "Doctor";

    public PDFCreator(List<MedicalRecord> medicalRecords, User user){
        this.medicalRecords = medicalRecords;
        this.user = user;
    }

    public void createPdf() {
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "Medical_History");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
        }

        File myFile = new File(pdfFolder + "/" + "MedicalHistory.pdf");
        OutputStream output = null;

        try {
            output = new FileOutputStream(myFile);
            // Initialization of a document
            Document document = new Document(PageSize.A4);
            // Get writer instant
            PdfWriter docWriter = PdfWriter.getInstance(document, output);
            // Open document
            document.open();

            PdfContentByte canvas = docWriter.getDirectContent();

            // Add Header and Footer to the document
            setHeader(canvas);
            setFooter(canvas, docWriter);

            //Step 4 Add content
            float[] columnWidths = {2f, 2f, 2.2f, 2.5f, 5f, 2.5f, 2.5f};
            PdfPTable table = new PdfPTable(columnWidths);
            PdfPCell Data=new PdfPCell();
            createTableWithColumns(table, Data);

            //region Adding Data into table
            for(MedicalRecord medicalRecord: medicalRecords){
                table.addCell(medicalRecord.getDisease()); // Disease
                table.addCell(medicalRecord.getMedicine());   // Medicine
                PdfPCell allerg = new PdfPCell(new Phrase(medicalRecord.getAllergic()));  // Allergic
                allerg.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(allerg);
                PdfPCell durat = new PdfPCell(new Phrase(medicalRecord.getDuration().substring(0,12) + "\n" +
                        medicalRecord.getDuration().substring(13, 14) + "\n" +
                        medicalRecord.getDuration().substring(15))); // Duration
                durat.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(durat);
                PdfPCell des = new PdfPCell(new Phrase(medicalRecord.getDescription()));    // Description
                des.setPaddingBottom(10.0f);
                des.setPaddingLeft(3.0f);
                table.addCell(des);
                table.addCell(medicalRecord.getDoctor());       // Doctor
                table.addCell(medicalRecord.getContact());      // Contact
            }
            //endregion

            //region Adding new page to continue table
            ColumnText columnTable=new ColumnText(docWriter.getDirectContent());
            columnTable.setSimpleColumn(-50,725,650,50); //740 - 2
            columnTable.addElement(table);
            int status=columnTable.go();

            while(columnTable.hasMoreText(status))
            {
                setFooter(canvas, docWriter);
                document.newPage();
                columnTable.setSimpleColumn(-50,800,650,30);
                status=columnTable.go();
                setFooter(canvas, docWriter);
            }
            //endregion

            // Close the document
            document.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void createTableWithColumns(PdfPTable table, PdfPCell Data){
        String disease = DISEASE;
        Data=new PdfPCell(new Phrase(disease,FontFactory.getFont(FontFactory.TIMES_BOLDITALIC,10,Font.BOLD,BaseColor.WHITE)));
        Data.setBackgroundColor(BaseColor.DARK_GRAY);
        Data.setHorizontalAlignment(Element.ALIGN_CENTER);
        Data.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Data.setFixedHeight(30f);
        Data.setBorderWidth(1);
        table.addCell(Data);

        String medicine = MEDICINE;
        Data=new PdfPCell(new Phrase(medicine,FontFactory.getFont(FontFactory.TIMES_BOLDITALIC,10,Font.BOLD,BaseColor.WHITE)));
        Data.setBackgroundColor(BaseColor.DARK_GRAY);
        Data.setHorizontalAlignment(Element.ALIGN_CENTER);
        Data.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Data.setFixedHeight(30f);
        Data.setBorderWidth(1);
        table.addCell(Data);

        String allergic = ALLERGIC;
        Data=new PdfPCell(new Phrase(allergic,FontFactory.getFont(FontFactory.TIMES_BOLDITALIC,10,Font.BOLD,BaseColor.WHITE)));
        Data.setBackgroundColor(BaseColor.DARK_GRAY);
        Data.setHorizontalAlignment(Element.ALIGN_CENTER);
        Data.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Data.setFixedHeight(30f);
        Data.setBorderWidth(1);
        table.addCell(Data);

        String duration = DURATION;
        Data=new PdfPCell(new Phrase(duration,FontFactory.getFont(FontFactory.TIMES_BOLDITALIC,10,Font.BOLD,BaseColor.WHITE)));
        Data.setBackgroundColor(BaseColor.DARK_GRAY);
        Data.setHorizontalAlignment(Element.ALIGN_CENTER);
        Data.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Data.setFixedHeight(30f);
        Data.setBorderWidth(1);
        table.addCell(Data);

        String description = DESCRIPTION;
        Data=new PdfPCell(new Phrase(description,FontFactory.getFont(FontFactory.TIMES_BOLDITALIC,10,Font.BOLD,BaseColor.WHITE)));
        Data.setBackgroundColor(BaseColor.DARK_GRAY);
        Data.setHorizontalAlignment(Element.ALIGN_CENTER);
        Data.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Data.setFixedHeight(30f);
        Data.setBorderWidth(1);
        table.addCell(Data);


        String doctor = DOCTOR;
        Data=new PdfPCell(new Phrase(doctor,FontFactory.getFont(FontFactory.TIMES_BOLDITALIC,10,Font.BOLD,BaseColor.WHITE)));
        Data.setBackgroundColor(BaseColor.DARK_GRAY);
        Data.setHorizontalAlignment(Element.ALIGN_CENTER);
        Data.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Data.setFixedHeight(30f);
        Data.setBorderWidth(1);
        table.addCell(Data);

        String contact = CONTACT;
        Data=new PdfPCell(new Phrase(contact,FontFactory.getFont(FontFactory.TIMES_BOLDITALIC,10,Font.BOLD,BaseColor.WHITE)));
        Data.setBackgroundColor(BaseColor.DARK_GRAY);
        Data.setHorizontalAlignment(Element.ALIGN_CENTER);
        Data.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Data.setFixedHeight(30f);
        Data.setBorderWidth(1);
        table.addCell(Data);
    }

    private void setFooter(PdfContentByte canvas, PdfWriter docWriter){
        Rectangle footer = new Rectangle(1,1,594,30);
        footer.setBorder(Rectangle.BOX);
        footer.setBackgroundColor(new BaseColor(89, 156, 194));
        footer.setBorderWidth(2);
        footer.setBorderColor(new BaseColor(89, 156, 194));
        canvas.rectangle(footer);

        String page = PAGE_TEXT + String.valueOf(docWriter.getPageNumber());
        Paragraph paraPage = new Paragraph(page,FontFactory.getFont(FontFactory.HELVETICA,12,Font.ITALIC,BaseColor.BLACK));
        paraPage.setAlignment(Element.ALIGN_RIGHT);
        paraPage.setIndentationRight(20);

        ColumnText columnFooter = new ColumnText(canvas);
        columnFooter.setSimpleColumn(footer);
        columnFooter.addElement(paraPage);
        try {
            columnFooter.go();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void setHeader(PdfContentByte canvas){
        Rectangle header = new Rectangle(1,750,594,841);
        header.setBorder(Rectangle.BOX);
        header.setBackgroundColor(new BaseColor(89, 156, 194));
        header.setBorderWidth(2);
        header.setBorderColor(new BaseColor(89, 156, 194));
        canvas.rectangle(header);

        String title = TITLE;
        Paragraph paraTitle=new Paragraph(title, FontFactory.getFont(FontFactory.TIMES_ITALIC,22,Font.ITALIC,BaseColor.BLACK));
        paraTitle.setAlignment(Element.ALIGN_CENTER);
        paraTitle.setIndentationLeft(30);

        String name = getUser().getUserDetails().getName();
        Paragraph paraName = new Paragraph(name,FontFactory.getFont(FontFactory.HELVETICA,18,Font.ITALIC,BaseColor.BLACK));
        paraName.setAlignment(Element.ALIGN_LEFT);
        paraName.setIndentationLeft(30);
        paraName.setPaddingTop(0);

        String age = AGE_TEXT + getUser().calculateAge();
        Paragraph paraAge = new Paragraph(age,FontFactory.getFont(FontFactory.HELVETICA,12,Font.ITALIC,BaseColor.BLACK));
        paraAge.setAlignment(Element.ALIGN_LEFT);
        paraAge.setIndentationLeft(30);

        ColumnText columnHeader=new ColumnText(canvas);
        columnHeader.setSimpleColumn(header);
        columnHeader.addElement(paraTitle);
        columnHeader.addElement(paraName);
        columnHeader.addElement(paraAge);

        String date = DATE_TEXT + getCurrentDate();
        Paragraph paraDate = new Paragraph(date,FontFactory.getFont(FontFactory.HELVETICA,12,Font.ITALIC,BaseColor.BLACK));
        paraName.setAlignment(Element.ALIGN_RIGHT);
        paraDate.setIndentationRight(30);
        ColumnText dateColumn = new ColumnText(canvas);
        dateColumn.setSimpleColumn(470, 750, 770, 781);
        dateColumn.addElement(paraDate);

        try {
            columnHeader.go();
            dateColumn.go();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentDate(){
        // Get current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(new Date());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
