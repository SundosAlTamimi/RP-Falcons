package com.tamimi.sundos.restpos;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tamimi.sundos.restpos.BackOffice.BackOfficeActivity;
import com.tamimi.sundos.restpos.Models.Announcemet;
import com.tamimi.sundos.restpos.Models.BlindClose;
import com.tamimi.sundos.restpos.Models.CancleOrder;
import com.tamimi.sundos.restpos.Models.OrderHeader;
import com.tamimi.sundos.restpos.Models.OrderTransactions;
import com.tamimi.sundos.restpos.Models.Pay;
import com.tamimi.sundos.restpos.Models.PayMethod;
import com.tamimi.sundos.restpos.Models.TableActions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import static com.itextpdf.text.Element.ALIGN_CENTER;

public class ExportToPdf   {

    int pageNumber = 0;
    Document doc;
    File file;
    PdfWriter docWriter = null;
    //    PDFView pdfView;
    String pdfFileName;

    BaseFont base;

    {
        try {
            base = BaseFont.createFont("/assets/arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Font  arabicFont = new Font(base, 11f);
    Font arabicFontHeader = new Font(base, 12f);
    Context context;

    public ExportToPdf(Context context) {

        this.context=context;
    }

    public void RecancelReport(List <BlindClose>blindClosePdf,List<String> headerData){

         float[] fromDat = {1.5f,1f,1f,1f,1.5f,1f,1.10f,1.5f,1.5f,1f};
        PdfPTable pdfPTable = new PdfPTable(fromDat);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("Re_CancellationReport_"  + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, context.getString(R.string.date_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.pos), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.shift), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.user), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.change_over), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.sales_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.physical), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.difference), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.update_by), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.till_ok), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < blindClosePdf.size(); i++) {
            insertCell(pdfPTable, String.valueOf(blindClosePdf.get(i).getDate()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(blindClosePdf.get(i).getPOSNo()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(blindClosePdf.get(i).getShiftName()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(blindClosePdf.get(i).getUserName()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(blindClosePdf.get(i).getUserCash()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(blindClosePdf.get(i).getSysSales()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(blindClosePdf.get(i).getUserSales()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(blindClosePdf.get(i).getSalesDiff()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(blindClosePdf.get(i).getReason()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf((blindClosePdf.get(i).getTillOk() == 0 ? "no" : "yes")), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader,context.getString(R.string.re_cancellation_report), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) + headerData.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name) +" : "+ headerData.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) + headerData.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no) +" : "+ headerData.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale)+" : " + headerData.get(4), Element.ALIGN_LEFT, 1, arabicFont, BaseColor.WHITE);

        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, "Export to pdf Successful", Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();
//


    }


    public void X_report(List <OrderTransactions> orderTransactionDataPdf,List<String> orderHeder,List <String> otherInfo,List <OrderTransactions>orderTransactionDataPdf2){

        PdfPTable pdfPTable = new PdfPTable(4);
        PdfPTable pdfPTableDetal = new PdfPTable(4);
        PdfPTable pdfPTableHeader = new PdfPTable(4);
        PdfPTable pdfPTableTax = new PdfPTable(3);

        createPDF("X_Report_"  + "_.pdf");

        pdfPTable.setWidthPercentage(100f);
        pdfPTableDetal.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableTax.setWidthPercentage(100f);

        pdfPTableHeader.setSpacingAfter(20);
        pdfPTable.setSpacingAfter(5);
        pdfPTableDetal.setSpacingAfter(15);

        insertCell(pdfPTable,  context.getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,  context.getString(R.string.total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,  context.getString(R.string.tax), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,  context.getString(R.string.net_total_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);

        for (int i = 0; i < orderTransactionDataPdf.size(); i++) {
            insertCell(pdfPTable, String.valueOf(orderTransactionDataPdf.get(i).getItemName()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(orderTransactionDataPdf.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(orderTransactionDataPdf.get(i).getTaxValue()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(orderTransactionDataPdf.get(i).getTotal() - (orderTransactionDataPdf.get(i).getTaxValue() + orderTransactionDataPdf.get(i).getDiscount())), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.x_report), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name)+" : " + orderHeder.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) + orderHeder.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale)+" : " +orderHeder.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) + orderHeder.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);

        insertCell(pdfPTableDetal, context.getString(R.string.total_before_tax)+" : " + otherInfo.get(0), Element.ALIGN_RIGHT, 1, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.tax)+" : "  + otherInfo.get(1), Element.ALIGN_CENTER, 2, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.total_after_tax)+" : "  + otherInfo.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.service)+" : " + otherInfo.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.service_tax)+" : "  + otherInfo.get(4), Element.ALIGN_CENTER, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.total_tax)+" : "  + otherInfo.get(5), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.net_total_)+" : "  + otherInfo.get(6), Element.ALIGN_RIGHT, 3, arabicFont, BaseColor.WHITE);

        insertCell(pdfPTableTax, context.getString(R.string.tax_percent) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTableTax, context.getString(R.string.total) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTableTax, context.getString(R.string.tax) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTableTax.setHeaderRows(1);

        for (int i = 0; i < orderTransactionDataPdf2.size(); i++) {
            insertCell(pdfPTableTax, String.valueOf(orderTransactionDataPdf2.get(i).getTaxPerc()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTableTax, String.valueOf(orderTransactionDataPdf2.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTableTax, String.valueOf(orderTransactionDataPdf2.get(i).getTaxValue()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            doc.add(pdfPTableDetal);
            doc.add(pdfPTableTax);

            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();


    }


    public void cashierInOutReport(List <Pay> PayCashier,List <String>cashierHeader){

        PdfPTable pdfPTable = new PdfPTable(7);
                PdfPTable pdfPTableHeader = new PdfPTable(4);

                createPDF("CashierInOutReport_"  + "_.pdf");
                pdfPTable.setWidthPercentage(100f);
                pdfPTableHeader.setWidthPercentage(100f);
                pdfPTableHeader.setSpacingAfter(20);

                insertCell(pdfPTable, context.getString(R.string.number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable,context. getString(R.string.cashier_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable,context.getString(R.string.date_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable, context.getString(R.string.time), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable,context. getString(R.string.point_of_sale), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable,context. getString(R.string.trans_type), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable,context. getString(R.string.amount), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);
//
        for (int i = 0; i < PayCashier.size(); i++) {
                    insertCell(pdfPTable, String.valueOf(i + 1), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                    insertCell(pdfPTable, PayCashier.get(i).getUserName(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                    insertCell(pdfPTable, PayCashier.get(i).getTransDate(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                    insertCell(pdfPTable, PayCashier.get(i).getTime(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                    insertCell(pdfPTable, String.valueOf(PayCashier.get(i).getPosNo()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                    insertCell(pdfPTable, String.valueOf(PayCashier.get(i).getTransType()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                    insertCell(pdfPTable, String.valueOf(PayCashier.get(i).getValue()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                }

                insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
                insertCell(pdfPTableHeader, context.getString(R.string.cashier_in_out_report), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
                insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
                insertCell(pdfPTableHeader, context.getString(R.string.from_date) +cashierHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
                insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
                insertCell(pdfPTableHeader, context.getString(R.string.shift_name)+" : " +cashierHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
                insertCell(pdfPTableHeader, context.getString(R.string.to_date) +cashierHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
                insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
                insertCell(pdfPTableHeader, context.getString(R.string.cashier_no)+" : " +cashierHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
                insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale)+" : " +cashierHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
                insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
               String type="";
                if(cashierHeader.get(5).equals("-1")) {
                    type="All";
                }else if(cashierHeader.get(5).equals("0")){
                    type="In";
               }else {
                    type="Out";
                }

                insertCell(pdfPTableHeader, context.getString(R.string.cashier)+" : " +type, Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);

        try {
                    doc.add(pdfPTableHeader);
                    doc.add(pdfPTable);
                    Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                endDocPdf();


    }

    public void canselOrderReport(List <CancleOrder>canceledOrdersPdf,List<String> cancelHeader){
        float[] fromDat = {0.5f,1.5f,1.5f,1.5f,1.5f,1f,1.5f,2f};

        PdfPTable pdfPTable = new PdfPTable(fromDat);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("CancelOrderReport_"  + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, context.getString(R.string.number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.date_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.time), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.item_code), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.qty), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.total_price), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.void_reason), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
pdfPTable.setHeaderRows(1);
        for (int i = 0; i < canceledOrdersPdf.size(); i++) {
            insertCell(pdfPTable, String.valueOf(i + 1), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, canceledOrdersPdf.get(i).getTransDate(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, canceledOrdersPdf.get(i).getTime(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, canceledOrdersPdf.get(i).getItemCode(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, canceledOrdersPdf.get(i).getItemName(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(canceledOrdersPdf.get(i).getQty()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(canceledOrdersPdf.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, canceledOrdersPdf.get(i).getReason(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader,  context.getString(R.string.canceled_order_history), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) +cancelHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name)+" : " +cancelHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) +cancelHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no)+" : " +cancelHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) +" : "+cancelHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        String type="";
        if(cancelHeader.get(5).equals("-1")) {
            type="All";
        }else if(cancelHeader.get(5).equals("0")){
            type=context.getString(R.string.canceled);
        }else {
            type=context.getString(R.string.void_);
        }

        insertCell(pdfPTableHeader, context.getString(R.string.cashier)+" : " +type, Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);


        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();

    }

    public void Z_report(List <OrderTransactions> orderTransactionDataPdf,List<String> orderHeder,List <String> otherInfo,List <OrderTransactions>orderTransactionDataPdf2){

        PdfPTable pdfPTable = new PdfPTable(4);
        PdfPTable pdfPTableDetal = new PdfPTable(4);
        PdfPTable pdfPTableHeader = new PdfPTable(4);
        PdfPTable pdfPTableTax = new PdfPTable(3);

        createPDF("Z_Report_" + "_.pdf");

        pdfPTable.setWidthPercentage(100f);
        pdfPTableDetal.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableTax.setWidthPercentage(100f);

        pdfPTableHeader.setSpacingAfter(20);
        pdfPTable.setSpacingAfter(5);
        pdfPTableDetal.setSpacingAfter(15);

        insertCell(pdfPTable,  context.getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,  context.getString(R.string.total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,  context.getString(R.string.tax), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,  context.getString(R.string.net_total_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);

        for (int i = 0; i < orderTransactionDataPdf.size(); i++) {
            insertCell(pdfPTable, String.valueOf(orderTransactionDataPdf.get(i).getItemName()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(orderTransactionDataPdf.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(orderTransactionDataPdf.get(i).getTaxValue()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(orderTransactionDataPdf.get(i).getTotal() - (orderTransactionDataPdf.get(i).getTaxValue() + orderTransactionDataPdf.get(i).getDiscount())), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.z_report), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.date) + orderHeder.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.serial)+" : " +orderHeder.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 3, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale)+" : " +orderHeder.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);


        insertCell(pdfPTableDetal, context.getString(R.string.total_before_tax)+" : " + otherInfo.get(0), Element.ALIGN_RIGHT, 1, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.tax)+" : "  + otherInfo.get(1), Element.ALIGN_CENTER, 2, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.total_after_tax)+" : "  + otherInfo.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.service)+" : " + otherInfo.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.service_tax)+" : "  + otherInfo.get(4), Element.ALIGN_CENTER, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.total_tax)+" : "  + otherInfo.get(5), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.net_total_)+" : "  + otherInfo.get(6), Element.ALIGN_RIGHT, 3, arabicFont, BaseColor.WHITE);

        insertCell(pdfPTableTax, context.getString(R.string.tax_percent) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTableTax, context.getString(R.string.total) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTableTax, context.getString(R.string.tax) , ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTableTax.setHeaderRows(1);
        for (int i = 0; i < orderTransactionDataPdf2.size(); i++) {
            insertCell(pdfPTableTax, String.valueOf(orderTransactionDataPdf2.get(i).getTaxPerc()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTableTax, String.valueOf(orderTransactionDataPdf2.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTableTax, String.valueOf(orderTransactionDataPdf2.get(i).getTaxValue()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            doc.add(pdfPTableDetal);
            doc.add(pdfPTableTax);

            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();


    }

    public void MarketReport(List<OrderHeader>headerData,String fromDateT,String ToDateT){
        PdfPTable pdfPTable = new PdfPTable(6);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("MarketReport_"  + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, "Pos No", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Sale Before Tax", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Tax", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Net Sales", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Order Count", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Average", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < headerData.size(); i++) {
            insertCell(pdfPTable, String.valueOf(headerData.get(i).getPointOfSaleNumber()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerData.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerData.get(i).getTotalTax()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerData.get(i).getAmountDue()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerData.get(i).getTime()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerData.get(i).getAmountDue() / Integer.parseInt(headerData.get(i).getTime())), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "Market Report", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "From Date : " + fromDateT, Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "To Date   : " + ToDateT, Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);


        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();


    }

    public void AnnouncementForTheDay(List<Announcemet>AnnounPdf,List<String>AnnounHeader){
        PdfPTable pdfPTable = new PdfPTable(6);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("AnnouReport_"  + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, "Shift Name", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "User Name", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Pos No", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Date", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Message", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Is Show", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);

        for (int i = 0; i < AnnounPdf.size(); i++) {
            insertCell(pdfPTable, AnnounPdf.get(i).getShiftName(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, AnnounPdf.get(i).getUserName(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(AnnounPdf.get(i).getPosNo()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(AnnounPdf.get(i).getAnnouncementDate()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(AnnounPdf.get(i).getMessage()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(AnnounPdf.get(i).getIsShow()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "Announcement Report", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "From Date : " + AnnounHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "User Name : " + AnnounHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "To Date   : " +  AnnounHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "Pos No :" +  AnnounHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);


        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();

    }

    public void salesByHour(List <OrderHeader>headerList ,List <String>hourHeader){


        PdfPTable pdfPTable = new PdfPTable(6);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("salesByHours_"+ "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, "Hour", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "TotalGusts", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "No Of Trans", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "No of Sales", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Avg Per Trans", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Avg Per Gusts", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < headerList.size(); i++) {

            insertCell(pdfPTable, headerList.get(i).getTime(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerList.get(i).getTotalTax()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerList.get(i).getTotalDiscount()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerList.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerList.get(i).getAmountDue()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerList.get(i).getAllDiscount()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "Sales By Hour Report", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) +hourHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name)+" : "  +hourHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) +hourHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no)+" : "  +hourHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale)+" : "  +hourHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        String type="";
        if(hourHeader.get(5).equals("0")) {
            type=context.getString(R.string.qty);
        }else if(hourHeader.get(5).equals("1")){
            type=context.getString(R.string.money);
        }

        insertCell(pdfPTableHeader, context.getString(R.string.cashier)+" : "  +type, Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        try {
            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();


    }

    public void salesVolumeByItem(List<OrderTransactions>orderTransactionData,List<String>VolumeHeader){
        PdfPTable pdfPTable = new PdfPTable(5);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("salesVolumeItemReport_"  + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, "Item Code ", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Item Name ", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Qty", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Price", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Total", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);

        for (int i = 0; i < orderTransactionData.size(); i++) {
            if(!orderTransactionData.get(i).getTime().equals("*")) {
                insertCell(pdfPTable, orderTransactionData.get(i).getItemBarcode(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable, String.valueOf(orderTransactionData.get(i).getItemName()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable, String.valueOf(orderTransactionData.get(i).getQty()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable, String.valueOf(orderTransactionData.get(i).getPrice()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable, String.valueOf(orderTransactionData.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            }else {
                insertCell(pdfPTable, orderTransactionData.get(i).getItemCategory(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable, "", Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable, String.valueOf(orderTransactionData.get(i).getQty()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable,"", Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable, String.valueOf(orderTransactionData.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            }
        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "Sales Volume Item Report", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) +VolumeHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name)+" : "  +VolumeHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) +VolumeHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no)+" : "  +VolumeHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) +" : " +VolumeHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 3, arabicFont, BaseColor.WHITE);


        try {
            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();


    }
    public void TopSalesItemReport(List<OrderTransactions>transactionsPdf,List<String>salesHeader){

        PdfPTable pdfPTable = new PdfPTable(4);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("TopSalesItemReport_"  + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, "Item Code ", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Item Name ", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Qty", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Total", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);

        for (int i = 0; i < transactionsPdf.size(); i++) {

            insertCell(pdfPTable, transactionsPdf.get(i).getItemBarcode(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, transactionsPdf.get(i).getItemName(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(transactionsPdf.get(i).getQty()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(transactionsPdf.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "Top Sales Item Report", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) +salesHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name)+" : "  +salesHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) +salesHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no)+" : "  +salesHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale)+" : "  +salesHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier)+" : "  +salesHeader.get(5), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);

        try {
            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();

    }

    public void TopGroupSalesReport(){


    }
    public void TopFamilySalesReport(){


    }
    public void salesReportByCustomer(){


    }
    public void salesReportByCardType(List <PayMethod>OrderPayMDataPdf, List<String>cardHeader){
//
        PdfPTable pdfPTable = new PdfPTable(7);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("SalesByCardReport_"  + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, context.getString(R.string.number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.date_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.time), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.point_of_sale), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.voucher_no), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.net_total_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.customer_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);

        for (int i = 0; i < OrderPayMDataPdf.size(); i++) {
            insertCell(pdfPTable, String.valueOf(i + 1), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(OrderPayMDataPdf.get(i).getVoucherDate()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(OrderPayMDataPdf.get(i).getTime()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(OrderPayMDataPdf.get(i).getPointOfSaleNumber()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(OrderPayMDataPdf.get(i).getVoucherNumber()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(OrderPayMDataPdf.get(i).getPayValue()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(OrderPayMDataPdf.get(i).getUserName()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.sales_report_by_card_type), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) +cardHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name)+" : "  +cardHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) +cardHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.card_type) +cardHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) +" : " +cardHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 3, arabicFont, BaseColor.WHITE);
        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();

    }
    public void waiterReport(List <OrderHeader> headerDataMarket,List<String>waiterHeader){

        PdfPTable pdfPTable = new PdfPTable(7);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("WaiterReport_" + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, context.getString(R.string.waiter_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.discount), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.tax), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.net), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.service), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.service_tax), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);

        for (int i = 0; i < headerDataMarket.size(); i++) {
            insertCell(pdfPTable, String.valueOf(headerDataMarket.get(i).getWaiter()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerDataMarket.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerDataMarket.get(i).getTotalDiscount()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerDataMarket.get(i).getTotalTax()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerDataMarket.get(i).getAmountDue()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerDataMarket.get(i).getTotalService()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerDataMarket.get(i).getTotalServiceTax()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.waiter_report), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) +waiterHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name) +" : " +waiterHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) +waiterHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.waiter_name)+" : "  +waiterHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale)+" : "  +waiterHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 3, arabicFont, BaseColor.WHITE);

        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();

    }
    public void TableActionReport(List <TableActions>actionsPdf,List<String>tableHeader){

        PdfPTable pdfPTable = new PdfPTable(8);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("TablesActionReport_"  + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, "Date", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Time", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Table No", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Section", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Action", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "To Table", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "To Section", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "Cashier", ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);

        for (int i = 0; i < actionsPdf.size(); i++) {

            String type = "";
            switch (actionsPdf.get(i).getActionType()) {
                case 0:
                    type = "Move";
                    break;
                case 1:
                    type = "Merge";
                    break;
                case 2:
                    type = "Split";
                    break;
            }

            insertCell(pdfPTable, actionsPdf.get(i).getActionDate(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, actionsPdf.get(i).getActionTime(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(actionsPdf.get(i).getTableNo()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(actionsPdf.get(i).getSectionNo()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, type, Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(actionsPdf.get(i).getToTable()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(actionsPdf.get(i).getToSection()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(actionsPdf.get(i).getUserName()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "Table Action Report", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) +tableHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name)+" : "  +tableHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) +tableHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no) +" : " +tableHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale)+" : "  +tableHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        String type="";
        if(tableHeader.get(5).equals("-1")) {
            type="All";
        }else if(tableHeader.get(5).equals("0")){
            type="Move";
        }else if(tableHeader.get(5).equals("1")) {
            type="Marge";
        }else {
            type="Split";
        }

        insertCell(pdfPTableHeader, context.getString(R.string.trans_type) +type, Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);



        try {
            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();

    }
    public void ProfitLossReport(){


    }
    public void DetailSalesReport(){


    }
    public void simpleSalesTotalReport(List<OrderHeader>headerData,List<String>orderTotal,List<String>simpleHeader){

        PdfPTable pdfPTable = new PdfPTable(8);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("SimpleSalesReport_" + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, context.getString(R.string.date_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.pos), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.voucher_no), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.discount), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.tax), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.net), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.pay_method), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < headerData.size(); i++) {
            insertCell(pdfPTable, String.valueOf(headerData.get(i).getVoucherDate()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerData.get(i).getPointOfSaleNumber()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerData.get(i).getVoucherNumber()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerData.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerData.get(i).getAllDiscount()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerData.get(i).getTotalTax()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerData.get(i).getAmountDue()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerData.get(i).getTime()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        }
        insertCell(pdfPTable, context.getString(R.string.total), Element.ALIGN_LEFT, 3, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(orderTotal.get(0)), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(orderTotal.get(1)), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(orderTotal.get(2)), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, String.valueOf(orderTotal.get(3)), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, "", Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);


        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.simple_sales_total), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) +simpleHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name) +" : " +simpleHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) +simpleHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no) +" : " +simpleHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale)+" : "  +simpleHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        String type="";
        if(simpleHeader.get(5).equals("-1")) {
            type="All";
        }else if(simpleHeader.get(5).equals("0")){
            type="Take Away";
        }else if(simpleHeader.get(5).equals("1")) {
            type="Dine In";
        }
        insertCell(pdfPTableHeader, context.getString(R.string.trans_type)+" : " +type, Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);

        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();

    }
    public void soldQtyReport(List <OrderTransactions> transactionsPdf,List<String>soldHeader){
        PdfPTable pdfPTable = new PdfPTable(10);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("SoldQtyReport_"  + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, context.getString(R.string.family), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.category), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.item_code), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.qty), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.price), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.gross), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.discount), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.tax), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.net), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < transactionsPdf.size(); i++) {
            insertCell(pdfPTable, String.valueOf(transactionsPdf.get(i).getItemFamily()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(transactionsPdf.get(i).getItemCategory()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(transactionsPdf.get(i).getItemBarcode()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(transactionsPdf.get(i).getItemName()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(transactionsPdf.get(i).getQty()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(transactionsPdf.get(i).getPrice()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(transactionsPdf.get(i).getQty() * (transactionsPdf.get(i).getPrice())), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(transactionsPdf.get(i).getDiscount()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(transactionsPdf.get(i).getTaxValue()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(transactionsPdf.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.sold_qty_report), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) +soldHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name)+" : "  +soldHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) +soldHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no)+" : "  +soldHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) +" : " +soldHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.family_name) +" : " +soldHeader.get(5), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.category_name) +" : " +soldHeader.get(6), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);

        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();



    }

    public void userCountReport(List<OrderHeader>headerDataMarket,List<String>userHeader){
//
        PdfPTable pdfPTable = new PdfPTable(3);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("UserOrderCountReport_"  + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, context.getString(R.string.user_name_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.order_count), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.net_total_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);
        for (int i = 0; i < headerDataMarket.size(); i++) {
            insertCell(pdfPTable, String.valueOf(headerDataMarket.get(i).getUserName()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerDataMarket.get(i).getTime()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(headerDataMarket.get(i).getAmountDue()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.user_order_count), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) +userHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.user_name) +userHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) +userHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) +" : " +userHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);


        try {
            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();


        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();


    }









    void createPDF(String fileName) {
        doc = new Document();
        docWriter = null;
        Log.e("path45", "create" + "-->" + Environment.getExternalStorageDirectory().getPath());
        try {


            String directory_path = Environment.getExternalStorageDirectory().getPath() + "/ReportRos/";
            file = new File(directory_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String targetPdf = directory_path + fileName;
            File path = new File(targetPdf);

            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));
            doc.setPageSize(PageSize.A4);//size of page
            //open document
            doc.open();
            Paragraph paragraph = new Paragraph();
            paragraph.add("");
            doc.add(paragraph);

            Log.e("path44", "" + targetPdf);


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    void endDocPdf() {

        if (doc != null) {
            //close the document
            doc.close();
        }
        if (docWriter != null) {
            //close the writer
            docWriter.close();
        }
    }

    public void insertCell(PdfPTable table, String text, int align, int colspan, Font font, BaseColor border) {

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        cell.setBorderColor(border);
        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }

        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL); //for make arabic string from right to left ...

//        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        //add the call to the table
        table.addCell(cell);

    }




}
