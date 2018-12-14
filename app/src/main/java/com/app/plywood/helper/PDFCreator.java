package com.app.plywood.helper;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import com.app.plywood.activity.InvoiceActivity;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class PDFCreator {

    Context context;
    File pdfFile;
    Document document;
    PdfWriter pdfWriter;
    Paragraph paragraph;
    Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    Font subTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD, BaseColor.RED);
    Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    Font higlightTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, BaseColor.RED);

    public PDFCreator(Context context) {
        this.context = context;
    }

    public void openDocument(){
        createFile();
        try {
            document = new Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
        }catch (Exception e){
            Log.e("OpenDocument", e.toString());
        }
    }
    private void createFile(){

        File folder = new File(Environment.getExternalStorageDirectory().toString(), "PDF");

        if (!folder.exists()){
            folder.mkdir();
            pdfFile = new File(folder, "Invoice.pdf");
        }
    }

    public void closeDocument(){
        document.close();
    }

    public void addMetaData(String title, String subject, String author){

        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);

    }

    public void addTitle(String title, String subTitle, String date){
        try {
            paragraph = new Paragraph();
            addChildParagraph(new Paragraph(title, titleFont));
            addChildParagraph(new Paragraph(subTitle, subTitleFont));
            addChildParagraph(new Paragraph(date, higlightTextFont));
            paragraph.setSpacingAfter(30);
            document.add(paragraph);
        }catch (Exception e){
            Log.e("AddTitle", e.toString());
        }

    }

    public void addChildParagraph(Paragraph childParagraph){
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }

    public void addParagraph(String text){

        try {
            paragraph = new Paragraph(text, textFont);
            paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(5);
            document.add(paragraph);
        }catch (Exception e){
            Log.e("AddParagraph", e.toString());
        }

    }

    public void createTable(String[] header, ArrayList<String[]> customer){

        try {
        paragraph = new Paragraph();
        paragraph.setFont(textFont);
        PdfPTable pdfPTable = new PdfPTable(header.length);
        pdfPTable.setWidthPercentage(100);
        pdfPTable.setSpacingBefore(20);
        PdfPCell pdfPCell;
        int indexCell = 0;
        while (indexCell < header.length){
            pdfPCell = new PdfPCell(new Phrase(header[indexCell++], subTitleFont));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pdfPTable.addCell(pdfPCell);
        }

        for (int indexRow = 0; indexRow < customer.size(); indexRow++){

            String[] row = customer.get(indexRow);

            for (indexCell = 0; indexCell < header.length; indexCell++){

                pdfPCell = new PdfPCell(new Phrase(row[indexCell]));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setFixedHeight(40);
                pdfPTable.addCell(pdfPCell);
            }
        }

        paragraph.add(pdfPTable);
        document.add(paragraph);
        } catch (Exception e) {
            Log.e("CreateTable", e.toString());
        }
    }

    public void viewPdf(Activity activity){


            Intent intent = new Intent(context, InvoiceActivity.class);
            intent.putExtra("path", pdfFile.getAbsolutePath());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

    }

}
