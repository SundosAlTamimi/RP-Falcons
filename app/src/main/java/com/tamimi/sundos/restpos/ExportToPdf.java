package com.tamimi.sundos.restpos;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
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
import java.util.List;

import static com.itextpdf.text.Element.ALIGN_CENTER;

public class ExportToPdf  {

    int pageNumber = 0;
    Document doc;
    File file;
    PdfWriter docWriter = null;
    //    PDFView pdfView;
    File pdfFileName;
//    PDFView pdfView;
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

    Font arabicFont = new Font(base, 11f);
    Font arabicFontHeader = new Font(base, 12f);
    Context context;

    public ExportToPdf(Context context) {

        this.context = context;
    }

    public void RecancelReport(List<BlindClose> blindClosePdf, List<String> headerData) {

        float[] fromDat = {1.5f, 1f, 1f, 1f, 1.5f, 1f, 1.10f, 1.5f, 1.5f, 1f};
        PdfPTable pdfPTable = new PdfPTable(fromDat);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("Re_CancellationReport__.pdf");
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
            insertCell(pdfPTable, String.valueOf((blindClosePdf.get(i).getTillOk() == 0 ? context.getString(R.string.no) : context.getString(R.string.yes))), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.re_cancellation_report), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) + headerData.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name) + " : " + headerData.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) + headerData.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no) + " : " + headerData.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) + " : " + headerData.get(4), Element.ALIGN_LEFT, 1, arabicFont, BaseColor.WHITE);

        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();
//

//        showPdfDialog("Re_CancellationReport__.pdf");
        showPdf(pdfFileName);

    }


    public void X_report(List<OrderTransactions> orderTransactionDataPdf, List<String> orderHeder, List<String> otherInfo, List<OrderTransactions> orderTransactionDataPdf2) {
        float[] fromDat = {1.5f, 1f, 1f, 1.5f};

        PdfPTable pdfPTable = new PdfPTable(4);
        PdfPTable pdfPTableDetal = new PdfPTable(fromDat);
        PdfPTable pdfPTableHeader = new PdfPTable(4);
        PdfPTable pdfPTableTax = new PdfPTable(3);

        createPDF("X_Report_" + "_.pdf");

        pdfPTable.setWidthPercentage(100f);
        pdfPTableDetal.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableTax.setWidthPercentage(100f);

        pdfPTableHeader.setSpacingAfter(20);
        pdfPTable.setSpacingAfter(5);
        pdfPTableDetal.setSpacingAfter(15);

        insertCell(pdfPTable, context.getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.tax), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.net_total_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);

        for (int i = 0; i < orderTransactionDataPdf.size(); i++) {
            insertCell(pdfPTable, String.valueOf(orderTransactionDataPdf.get(i).getItemName()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(orderTransactionDataPdf.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(orderTransactionDataPdf.get(i).getTaxValue()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(orderTransactionDataPdf.get(i).getTime()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.x_report), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name) + " : " + orderHeder.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) + orderHeder.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) + " : " + orderHeder.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) + orderHeder.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);

        insertCell(pdfPTableDetal, context.getString(R.string.total_before_tax) + " : " + otherInfo.get(0), Element.ALIGN_RIGHT, 1, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.tax) + " : " + otherInfo.get(1), Element.ALIGN_CENTER, 2, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.total_after_tax) + " : " + otherInfo.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.service) + " : " + otherInfo.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.service_tax) + " : " + otherInfo.get(4), Element.ALIGN_CENTER, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.total_tax) + " : " + otherInfo.get(5), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.net_total_) + " : " + otherInfo.get(6), Element.ALIGN_RIGHT, 3, arabicFont, BaseColor.WHITE);

        insertCell(pdfPTableTax, context.getString(R.string.tax_percent), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTableTax, context.getString(R.string.total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTableTax, context.getString(R.string.tax), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
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
        showPdf(pdfFileName);

    }


    public void cashierInOutReport(List<Pay> PayCashier, List<String> cashierHeader) {

        PdfPTable pdfPTable = new PdfPTable(7);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("CashierInOutReport_" + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, context.getString(R.string.number), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.cashier_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.date_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.time), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.point_of_sale), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.trans_type), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.amount), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
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
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) + cashierHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name) + " : " + cashierHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) + cashierHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no) + " : " + cashierHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) + " : " + cashierHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        String type = "";
        if (cashierHeader.get(5).equals("-1")) {
            type = context.getString(R.string.all);
        } else if (cashierHeader.get(5).equals("0")) {
            type = context.getString(R.string.cashier_in);
        } else {
            type =context.getString(R.string.cashier_out);
        }

        insertCell(pdfPTableHeader, context.getString(R.string.cashier) + " : " + type, Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);

        try {
            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();
        showPdf(pdfFileName);

    }

    public void canselOrderReport(List<CancleOrder> canceledOrdersPdf, List<String> cancelHeader) {
        float[] fromDat = {0.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1f, 1.5f, 2f};

        PdfPTable pdfPTable = new PdfPTable(fromDat);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("CancelOrderReport_" + "_.pdf");
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
        insertCell(pdfPTableHeader, context.getString(R.string.canceled_order_history), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) + cancelHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name) + " : " + cancelHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) + cancelHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no) + " : " + cancelHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) + " : " + cancelHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        String type = "";
        if (cancelHeader.get(5).equals("-1")) {
            type = context.getString(R.string.all);
        } else if (cancelHeader.get(5).equals("0")) {
            type = context.getString(R.string.canceled);
        } else {
            type = context.getString(R.string.void_);
        }

        insertCell(pdfPTableHeader, context.getString(R.string.cashier) + " : " + type, Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);


        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();

        showPdf(pdfFileName);


    }

    public void Z_report(List<OrderTransactions> orderTransactionDataPdf, List<String> orderHeder, List<String> otherInfo, List<OrderTransactions> orderTransactionDataPdf2) {
        float[] fromDat = {1.5f, 1f, 1f, 1.5f};
        PdfPTable pdfPTable = new PdfPTable(4);
        PdfPTable pdfPTableDetal = new PdfPTable(fromDat);
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

        insertCell(pdfPTable, context.getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.tax), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.net_total_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);

        for (int i = 0; i < orderTransactionDataPdf.size(); i++) {
            insertCell(pdfPTable, String.valueOf(orderTransactionDataPdf.get(i).getItemName()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(orderTransactionDataPdf.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(orderTransactionDataPdf.get(i).getTaxValue()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(orderTransactionDataPdf.get(i).getTime()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.z_report), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.date) + orderHeder.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.serial) + " : " + orderHeder.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
//        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 3, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) + " : " + orderHeder.get(2), Element.ALIGN_RIGHT, 4, arabicFont, BaseColor.WHITE);


        insertCell(pdfPTableDetal, context.getString(R.string.total_before_tax) + " : " + otherInfo.get(0), Element.ALIGN_RIGHT, 1, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.tax) + " : " + otherInfo.get(1), Element.ALIGN_CENTER, 2, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.total_after_tax) + " : " + otherInfo.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.service) + " : " + otherInfo.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.service_tax) + " : " + otherInfo.get(4), Element.ALIGN_CENTER, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.total_tax) + " : " + otherInfo.get(5), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableDetal, context.getString(R.string.net_total_) + " : " + otherInfo.get(6), Element.ALIGN_RIGHT, 3, arabicFont, BaseColor.WHITE);

        insertCell(pdfPTableTax, context.getString(R.string.tax_percent), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTableTax, context.getString(R.string.total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTableTax, context.getString(R.string.tax), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
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
        showPdf(pdfFileName);

    }

    public void MarketReport(List<OrderHeader> headerData, String fromDateT, String ToDateT) {
        PdfPTable pdfPTable = new PdfPTable(6);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("MarketReport_" + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, context.getString(R.string.point_of_sale), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.sale_before_tax), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.tax), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.net_salesM), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.order_count), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.average), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);

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
        insertCell(pdfPTableHeader, context.getString(R.string.market_report_), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) + fromDateT, Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) + ToDateT, Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);


        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();
        showPdf(pdfFileName);

    }

    public void AnnouncementForTheDay(List<Announcemet> AnnounPdf, List<String> AnnounHeader) {
        PdfPTable pdfPTable = new PdfPTable(6);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("AnnouReport_" + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, context.getString(R.string.shift_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.user_name_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.point_of_sale), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.date_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.message), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.show), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
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
        insertCell(pdfPTableHeader,  context.getString(R.string.announ_report), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader,  context.getString(R.string.from_date) + AnnounHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader,  context.getString(R.string.user_name)+" : "+ AnnounHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader,  context.getString(R.string.to_date) + AnnounHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader,  context.getString(R.string.point_of_sale)+" :" + AnnounHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);


        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();
        showPdf(pdfFileName);
    }

    public void salesByHour(List<OrderHeader> headerList, List<String> hourHeader) {


        PdfPTable pdfPTable = new PdfPTable(6);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("salesByHours_" + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, context.getString(R.string.hour), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.total_gusts), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.num_of_trans), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,  context.getString(R.string.sales_total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.avg_per_trans), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.avg_per_gusts), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
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
        insertCell(pdfPTableHeader,  context.getString(R.string.sales_by_hours), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) + hourHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name) + " : " + hourHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) + hourHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no) + " : " + hourHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) + " : " + hourHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        String type = "";
        if (hourHeader.get(5).equals("0")) {
            type = context.getString(R.string.qty);
        } else if (hourHeader.get(5).equals("1")) {
            type = context.getString(R.string.money);
        }

        insertCell(pdfPTableHeader, context.getString(R.string.cashier) + " : " + type, Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        try {
            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();
        showPdf(pdfFileName);

    }

    public void salesVolumeByItem(List<OrderTransactions> orderTransactionData, List<String> VolumeHeader) {
        PdfPTable pdfPTable = new PdfPTable(5);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("salesVolumeItemReport_" + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable,  context.getString(R.string.item_code), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,  context.getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,  context.getString(R.string.qty), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,  context.getString(R.string.price), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,  context.getString(R.string.total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);

        for (int i = 0; i < orderTransactionData.size(); i++) {
            if (!orderTransactionData.get(i).getTime().equals("*")) {
                insertCell(pdfPTable, orderTransactionData.get(i).getItemBarcode(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable, String.valueOf(orderTransactionData.get(i).getItemName()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable, String.valueOf(orderTransactionData.get(i).getQty()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable, String.valueOf(orderTransactionData.get(i).getPrice()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable, String.valueOf(orderTransactionData.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            } else {
                insertCell(pdfPTable, orderTransactionData.get(i).getItemCategory(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable, "", Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable, String.valueOf(orderTransactionData.get(i).getQty()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable, "", Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
                insertCell(pdfPTable, String.valueOf(orderTransactionData.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            }
        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader,  context.getString(R.string.sales_volume_by_item_type), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) + VolumeHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name) + " : " + VolumeHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) + VolumeHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no) + " : " + VolumeHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) + " : " + VolumeHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 3, arabicFont, BaseColor.WHITE);


        try {
            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();

        showPdf(pdfFileName);
    }

    public void TopSalesItemReport(List<OrderTransactions> transactionsPdf, List<String> salesHeader) {

        PdfPTable pdfPTable = new PdfPTable(4);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("TopSalesItemReport_" + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable,  context.getString(R.string.item_code), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,  context.getString(R.string.item_name), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,  context.getString(R.string.qty), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable,  context.getString(R.string.total), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);

        for (int i = 0; i < transactionsPdf.size(); i++) {

            insertCell(pdfPTable, transactionsPdf.get(i).getItemBarcode(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, transactionsPdf.get(i).getItemName(), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(transactionsPdf.get(i).getQty()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
            insertCell(pdfPTable, String.valueOf(transactionsPdf.get(i).getTotal()), Element.ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        }

        insertCell(pdfPTableHeader, "Falcon Soft ", Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.top_sales_item_report), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) + salesHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name) + " : " + salesHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) + salesHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no) + " : " + salesHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) + " : " + salesHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.order_by) + " : " + salesHeader.get(5), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);

        try {
            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();
        showPdf(pdfFileName);
    }

    public void TopGroupSalesReport() {


    }

    public void TopFamilySalesReport() {


    }

    public void salesReportByCustomer() {


    }

    public void salesReportByCardType(List<PayMethod> OrderPayMDataPdf, List<String> cardHeader) {
//
        PdfPTable pdfPTable = new PdfPTable(7);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("SalesByCardReport_" + "_.pdf");
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
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) + cardHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name) + " : " + cardHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) + cardHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.card_type) + cardHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) + " : " + cardHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 3, arabicFont, BaseColor.WHITE);
        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();
        showPdf(pdfFileName);
    }

    public void waiterReport(List<OrderHeader> headerDataMarket, List<String> waiterHeader) {

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
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) + waiterHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name) + " : " + waiterHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) + waiterHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.waiter_name) + " : " + waiterHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) + " : " + waiterHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 3, arabicFont, BaseColor.WHITE);

        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();
        showPdf(pdfFileName);
    }

    public void TableActionReport(List<TableActions> actionsPdf, List<String> tableHeader) {

        PdfPTable pdfPTable = new PdfPTable(8);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("TablesActionReport_" + "_.pdf");
        pdfPTable.setWidthPercentage(100f);
        pdfPTableHeader.setWidthPercentage(100f);
        pdfPTableHeader.setSpacingAfter(20);

        insertCell(pdfPTable, context.getString(R.string.date_), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.time), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.table_no), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.section), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.action), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.to_table), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.to_section), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        insertCell(pdfPTable, context.getString(R.string.cashier), ALIGN_CENTER, 1, arabicFont, BaseColor.BLACK);
        pdfPTable.setHeaderRows(1);

        for (int i = 0; i < actionsPdf.size(); i++) {

            String type = "";
            switch (actionsPdf.get(i).getActionType()) {
                case 0:
                    type = context.getString(R.string.move);
                    break;
                case 1:
                    type = context.getString(R.string.merge);
                    break;
                case 2:
                    type = context.getString(R.string.split);
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
        insertCell(pdfPTableHeader, context.getString(R.string.table_action_report), Element.ALIGN_CENTER, 4, arabicFontHeader, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 4, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) + tableHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name) + " : " + tableHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) + tableHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no) + " : " + tableHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) + " : " + tableHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        String type = "";
        if (tableHeader.get(5).equals("-1")) {
            type = context.getString(R.string.all);
        } else if (tableHeader.get(5).equals("0")) {
            type = context.getString(R.string.move);
        } else if (tableHeader.get(5).equals("1")) {
            type = context.getString(R.string.merge);
        } else {
            type = context.getString(R.string.split);
        }

        insertCell(pdfPTableHeader, context.getString(R.string.trans_type) + type, Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);


        try {
            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();
        showPdf(pdfFileName);
    }

    public void ProfitLossReport() {


    }

    public void DetailSalesReport() {


    }

    public void simpleSalesTotalReport(List<OrderHeader> headerData, List<String> orderTotal, List<String> simpleHeader) {

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
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) + simpleHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name) + " : " + simpleHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) + simpleHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no) + " : " + simpleHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) + " : " + simpleHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        String type = "";
        if (simpleHeader.get(5).equals("-1")) {
            type = context.getString(R.string.all);
        } else if (simpleHeader.get(5).equals("0")) {
            type = context.getString(R.string.take_away);
        } else if (simpleHeader.get(5).equals("1")) {
            type = context.getString(R.string.dine_in);
        }
        insertCell(pdfPTableHeader, context.getString(R.string.trans_type) + " : " + type, Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);

        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();
        showPdf(pdfFileName);
    }

    public void soldQtyReport(List<OrderTransactions> transactionsPdf, List<String> soldHeader) {
        PdfPTable pdfPTable = new PdfPTable(10);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("SoldQtyReport_" + "_.pdf");
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
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) + soldHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.shift_name) + " : " + soldHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) + soldHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.cashier_no) + " : " + soldHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) + " : " + soldHeader.get(4), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.family_name) + " : " + soldHeader.get(5), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.category_name) + " : " + soldHeader.get(6), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);

        try {

            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();
        showPdf(pdfFileName);

    }

    public void userCountReport(List<OrderHeader> headerDataMarket, List<String> userHeader) {
//
        PdfPTable pdfPTable = new PdfPTable(3);
        PdfPTable pdfPTableHeader = new PdfPTable(4);

        createPDF("UserOrderCountReport_" + "_.pdf");
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
        insertCell(pdfPTableHeader, context.getString(R.string.from_date) + userHeader.get(0), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.user_name) + userHeader.get(1), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.to_date) + userHeader.get(2), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, "", Element.ALIGN_LEFT, 2, arabicFont, BaseColor.WHITE);
        insertCell(pdfPTableHeader, context.getString(R.string.point_of_sale) + " : " + userHeader.get(3), Element.ALIGN_RIGHT, 1, arabicFont, BaseColor.WHITE);


        try {
            doc.add(pdfPTableHeader);
            doc.add(pdfPTable);
            Toast.makeText(context, context.getString(R.string.export_to_pdf), Toast.LENGTH_SHORT).show();


        } catch (DocumentException e) {
            e.printStackTrace();
        }
        endDocPdf();
        showPdf(pdfFileName);

    }


//    void showPdfDialog(String fileNamePdf) {
//
//        Dialog dialogPdf = new Dialog(context);
//        dialogPdf.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialogPdf.setCancelable(false);
//        dialogPdf.setContentView(R.layout.show_pdf_file);
//        dialogPdf.setCanceledOnTouchOutside(true);
//
//        pageNumber = doc.getPageNumber();
////        pdfView = (PDFView) dialogPdf.findViewById(R.id.pdfView);
////        displayFromAsset("/ReportRos/" + fileNamePdf);
//
//        dialogPdf.show();
//
//    }


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
            pdfFileName=path;

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

   void showPdf(File path){

       Intent intent = new Intent(Intent.ACTION_VIEW);
       intent.setDataAndType(Uri.fromFile(path), "application/pdf");
       context.startActivity(intent);
   }


//    private void displayFromAsset(String assetFileName) {
//        pdfFileName = assetFileName;
//
//        pdfView.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + assetFileName))
//                .defaultPage(pageNumber)
//                .enableSwipe(true)
//                .swipeHorizontal(false)
//                .onPageChange(this)
//                .enableAnnotationRendering(true)
//                .onLoad(this)
//                .scrollHandle(new DefaultScrollHandle(context))
//                .load();
//    }





}
