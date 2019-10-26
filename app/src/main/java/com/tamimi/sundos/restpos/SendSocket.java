package com.tamimi.sundos.restpos;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sewoo.jpos.command.ESCPOS;
import com.sewoo.jpos.request.RequestQueue;
import com.tamimi.sundos.restpos.Models.KitchenScreen;
import com.tamimi.sundos.restpos.Models.OrderTransactions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SendSocket {

    Context context;
    JSONObject obj1;
    byte[] printIm;
    List<OrderTransactions> orderTransactions;
    DatabaseHandler db;
    PrintPic printPic;
    LinearLayout lin;
    ESCPOS escpos = new ESCPOS();
    RequestQueue requestQueue = RequestQueue.getInstance();
    char[] command = new char[]{27, 112, (byte) 48, (byte) 10, (byte) 50}; // for open cash drawer
    char[] ESC_m = new char[]{27, 109};//cut paper
    //                                                char[] LF = new char[]{10};//line feed
    char LF =10;//line feed
    char[] ESC_Clear = new char[]{0x1B , 0x40};//cLEAR
//    char ESC_Clear ='@';//cLEAR
//private final static char[] INIT_PRINTER = new char[]{0x1B, 0x40};

    public SendSocket(Context context, JSONObject obj, List<OrderTransactions> orderTransactions) {
        this.obj1 = obj;
        this.context = context;
        this.orderTransactions = orderTransactions;
        db = new DatabaseHandler(context);

    }

    public void sendMessage(int master, LinearLayout liner, List<Bitmap> bitmapList, List<String> IPprinter) {
        final Handler handler = new Handler();
        lin = liner;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Socket s = null;
                OutputStream out = null;
                PrintWriter output = null;

                if (master != 0) {

                    List<KitchenScreen> kitchenScreens = new ArrayList<>();
                    JSONArray obj3 = new JSONArray();
                    //this for get all kitchen ...
                    kitchenScreens = db.getAllKitchenScreen();
                    Log.e("size : ", "**" + orderTransactions.size());

                    if (kitchenScreens.size() != 0) {
                        if (Settings.kitchenType == 0) {
                            //this for_loop for filter and send all data to target kitchen has same kitchen no and have ip
                            for (int i = 0; i < kitchenScreens.size(); i++) {
                                if (!kitchenScreens.get(i).getKitchenIP().equals("")) {//&& isHostAvailable(kitchenScreens.get(i).getKitchenIP(), 9002,100)

                                    if (checkHosts(kitchenScreens.get(i).getKitchenIP())) {

                                        ///this for print in Kitchen screen
                                        // _________________________________________________________________________________________________________
                                        Log.e("kitchenType==0 ", "Kitchen screen");
                                        obj3 = getObjectForKitchenNo(orderTransactions, kitchenScreens.get(i).getKitchenNo());
                                        if (obj3.toString().length() > 2) {
                                            try {
                                                String ip = kitchenScreens.get(i).getKitchenIP();
                                                s = new Socket(ip.trim(), 9002);
                                                out = s.getOutputStream();
                                                output = new PrintWriter(out);
                                                output.println(obj3.toString());
                                                output.flush();
                                                Log.e("obj3 " + i + " sec " + kitchenScreens.get(i).getKitchenNo(), "obj3.toString().length()" + obj3.toString());
                                                output.close();
                                                out.close();
                                                s.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        // _________________________________________________________________________________________________________
//                                    }
                                    }
//                                else {
//                                    Toast.makeText(context, "Please Make Sure The Printer if Connect or not ", Toast.LENGTH_SHORT).show();
//                                }
                                }
                            }
                        } else {//this for print in Kitchen printer
                            Log.e("kitchenType==1 ", "printer");
//
                            for (int i = 0; i < bitmapList.size(); i++) {
                                if (!IPprinter.get(i).equals("")) {//&& isHostAvailable(kitchenScreens.get(i).getKitchenIP(), 9002,100)

                                    if (checkHosts(IPprinter.get(i))) {
                                        Log.e("bitmapList",""+bitmapList.get(i)+"     ==>"+bitmapList.size());
                                        if (bitmapList.get(i) != null) {
                                            try {

                                                String ip = IPprinter.get(i);
//                                    String ip = "192.168.2.10";
//                                                char[] command = new char[]{27, 112, (byte) 48, (byte) 10, (byte) 50}; // for open cash drawer
//                                                char[] ESC_m = new char[]{27, 109};//cut paper
////                                                char[] LF = new char[]{10};//line feed
//                                                char LF =10;//line feed


                                                PrintPic printPic = PrintPic.getInstance();
                                                printPic.init(bitmapList.get(i));
                                                byte[] bitmapdata = printPic.printDraw();
                                                s = new Socket(ip.trim(), 9100);
                                                out = s.getOutputStream();
                                                output = new PrintWriter(out);
                                                out.write(bitmapdata);
                                                out.flush();
                                                output.println(LF);
                                                output.flush();
                                                output.println(LF);
                                                output.flush();
                                                output.println(ESC_m);
                                                output.flush();

                                                output.close();
                                                out.close();
                                                s.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
//                                    else{
//                                        Toast.makeText(context, "Please Make Sure The Printer if Connect or not ", Toast.LENGTH_SHORT).show();
//                                    }
                                    }
                                }
                            }
                        }
                    } else {
//                        Toast.makeText(context, "Please Add kitchen/printer IP ", Toast.LENGTH_SHORT).show();
                    }
                    // _________________________________________________________________________________________________________
                }
//                else {//this for print cash printer
//                    try {
////                      String ip = kitchenScreens.get(i).getKitchenIP();
//                        String ip = "192.168.2.10";
//                        if (checkHosts("192.168.2.10")) {
////                            char[] command = new char[]{27, 112, (byte) 48, (byte) 10, (byte) 50}; // for open cash drawer
////                            char[] ESC_m = new char[]{27, 109};//cut paper
//
//                            s = new Socket(ip.trim(), 9100);
//                            out = s.getOutputStream();
//
//                            output = new PrintWriter(out);
//                            output.println(ESC_Clear);
//                            output.flush();
//
//                            out.write(convertToImage());
//                            out.flush();
//                            output.println();
//                            output.flush();
////                                        output.println(command);
////                                        output.flush();
//                            output.println(LF);
//                            output.flush();
//                            output.println(LF);
//                            output.flush();
//                            output.println(ESC_m);
//                            output.flush();
//
//                            output.close();
//                            out.close();
//                            s.close();
//
//                        } else {
//                            Toast.makeText(context, "Please Make Sure Your Printer ", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//
//                }

                //tis for read data send from server ...

//                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
//                    final String st = input.readLine();

//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
////                            String s = mTextViewReplyFromServer.getText().toString();
////                            if (st.trim().length() != 0)
////                                mTextViewReplyFromServer.setText(s + "\nFrom Server : " + st);
////                            if (st.trim().length() != 0)
////                                Toast.makeText(context, "From Server : successful Socket " + st, Toast.LENGTH_SHORT).show();
//                        }
//                    });


            }
        });

        thread.start();
    }


    //"""""""""""""""""""""""""""""""""""""""""""
//    else {///this for print in Kitchen printer
//        Log.e("kitchenType==1 ", "printer");
////                                        if (!printerValue.toString().equals("")) {
//        try {
//            String ip = kitchenScreens.get(i).getKitchenIP();
////                                    String ip = "192.168.2.10";
//            char[] command = new char[]{27, 112, (byte) 48, (byte) 10, (byte) 50}; // for open cash drawer
//            char[] ESC_m = new char[]{27, 109};//cut paper
//            s = new Socket(ip.trim(), 9100);
//            out = s.getOutputStream();
//            output = new PrintWriter(out);
//            out.write(convertToImage());
//            out.flush();
//            output.println(ESC_m);
//            output.flush();
//            Log.e("kitchenScreens " + i + " sec " + kitchenScreens.get(i).getKitchenNo(), "kitchenScreens = --> ");
//            output.close();
//            out.close();
//            s.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //}//


    //""""""""""""""""""""""""""""""""""""""""""""


    // this for get all object  send to target kitchen by kitchen no ...

    JSONArray getObjectForKitchenNo(List<OrderTransactions> orderTransactions, int KitchenNo) {

        JSONArray objNo = new JSONArray();

        for (int i = 0; i < orderTransactions.size(); i++) {
            if (orderTransactions.get(i).getScreenNo() == KitchenNo) {
                try {
                    JSONObject obj = new JSONObject();
                    Log.e("ISUPDATE  =1", "1234 ==>" + orderTransactions.get(i).getOrderKind() + "    " + orderTransactions.get(i).toString());

                    String Date = orderTransactions.get(i).getVoucherDate() + " " + orderTransactions.get(i).getTime();
                    Log.e("date ", " ==>" + Date);

                    obj.put("TRINDATE", Date);
                    obj.put("ITEMCODE", orderTransactions.get(i).getItemBarcode());
                    obj.put("ITEMNAME", orderTransactions.get(i).getItemName());
                    obj.put("QTY", orderTransactions.get(i).getQty());
                    obj.put("PRICE", orderTransactions.get(i).getPrice());
                    obj.put("NOTE", orderTransactions.get(i).getNote());
//                    obj.put("SCREENNO", orderTransactions.get(i).getScreenNo());
                    obj.put("POSNO", orderTransactions.get(i).getPosNo());
//                    obj.put("ORDERNO", orderTransactions.get(i).getVoucherNo());
//                    JSONObject jo = obj1.getJSONObject("ORDERHEADER");
                    String vhfNo = obj1.getString("ORDERNO");
                    Log.e("Vhf8uuu = = ", "" + vhfNo);
                    obj.put("ORDERNO", vhfNo);

                    obj.put("ORDERTYPE", orderTransactions.get(i).getOrderType());
                    obj.put("TABLENO", orderTransactions.get(i).getTableNo());
                    obj.put("SECTIONNO", orderTransactions.get(i).getSectionNo());
                    if (orderTransactions.get(i).getOrderKind() == 1) {
                        obj.put("ISUPDATE", 1);
                        Log.e("ISUPDATE  =1", "" + orderTransactions.get(i).getOrderKind());
                    } else if (orderTransactions.get(i).getOrderKind() == 0) {
                        obj.put("ISUPDATE", 0);
                        Log.e("ISUPDATE =0", "" + orderTransactions.get(i).getOrderKind());
                    }

                    obj.put("CASHNO", orderTransactions.get(i).getCashNo());
                    obj.put("ORDERTKKIND", obj1.get("ORDERTKKIND"));
                    obj.put("STGNO", "1");
                    Log.e("ORDERTKKIND master = = ", "" + obj1.get("ORDERTKKIND"));
                    objNo.put(obj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e("getobj " + KitchenNo, "" + objNo.toString());

        return objNo;
    }

    public boolean checkHosts(String subnet) {
        int timeout = 1000;
        boolean fa = false;
        try {
            if (InetAddress.getByName(subnet).isReachable(timeout)) {
                System.out.println(subnet + " is reachable");
                fa = true;

            }
        } catch (IOException e) {
            e.printStackTrace();
            fa = false;
        }
        Log.e("tesr3", "fa ==>" + fa);
        return fa;
    }

    byte[] convertToImage() {
//        String text = "اهلا وسهلا";
        // create a text paint
//        TextPaint tp = new TextPaint();
//        tp.setTextSize(40);
//        tp.setFakeBoldText(true);
        // configure text paint
        //... see on the link below how to configure TextPaint
        // based on the configuration, get size in pixels
//        int width = (int) tp.measureText(text) + 10;
//        int height = (int) tp.measureText(text);
        // create bitmap with proper size

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
//        lin.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        lin.layout(0, 0, lin.getMeasuredWidth(), lin.getMeasuredHeight());
//
//        Log.e("size of img ", "width=" + lin.getMeasuredWidth() + "      higth =" + lin.getHeight());
//
//        lin.setDrawingCacheEnabled(true);
//        lin.buildDrawingCache();
//        Bitmap bit = lin.getDrawingCache();

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        // create canvas to execute drawing
//        Canvas canvas = new Canvas(bitmap);
//        // draw on the bitmap
//        canvas.drawText(text, 0, height / 2, tp);
//                    Drawable d = ContextCompat.getDrawable(MainActivity.this, R.drawable.axe);
//                    Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();


        lin.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        lin.layout(0, 0, lin.getMeasuredWidth(), lin.getMeasuredHeight());

        Log.e("size of img ", "width=" + lin.getMeasuredWidth() + "      higth =" + lin.getHeight());

//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();

//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(lin.getWidth(), lin.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = lin.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        lin.draw(canvas);


        PrintPic printPic = PrintPic.getInstance();
        printPic.init(bitmap);
        byte[] bitmapdata = printPic.printDraw();

        return bitmapdata;
    }


}
