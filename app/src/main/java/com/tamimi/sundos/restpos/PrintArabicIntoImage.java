package com.tamimi.sundos.restpos;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextPaint;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class PrintArabicIntoImage  {

    public void sendSocketToNextStage() {
//        Log.e("level", "2");
//        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                try {
                    //Replace below IP with the IP of that device in which server socket open.
                    //If you change port then change the port number in the server side code also.


                    Socket s = new Socket("192.168.2.10", 9100);
                    OutputStream out = s.getOutputStream();

                    LinearLayout linearLayout;
                    String text = "اهلا وسهلا";
                    // create a text paint
                    TextPaint tp = new TextPaint();
                    tp.setTextSize(40);
                    tp.setFakeBoldText(true);
                    // configure text paint
                    //... see on the link below how to configure TextPaint
                    // based on the configuration, get size in pixels
                    int width = (int) tp.measureText(text) + 10;
                    int height = (int) tp.measureText(text);
                    // create bitmap with proper size
                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    // create canvas to execute drawing
                    Canvas canvas = new Canvas(bitmap);
                    // draw on the bitmap
                    canvas.drawText(text, 0, height / 2, tp);
//                    Drawable d = ContextCompat.getDrawable(MainActivity.this, R.drawable.axe);
//                    Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    PrintPic printPic = PrintPic.getInstance();
                    printPic.init(bitmap);
                    byte[] bitmapdata = printPic.printDraw();

//                PrintWriter output = new PrintWriter(out);
//                    imageView.buildDrawingCache();
//                    iv.setImageBitmap(tv1.getDrawingCache());
//                    final Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//                    Canvas canvas = new Canvas(bitmap);
//                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//                    paint.setColor(Color.BLACK);
//                    canvas.drawCircle(150, 150, 10, paint);
//
//                    MainActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            imageView.setImageBitmap(bitmap);
//
//                        }
//                    });
//
//                    imageView.draw(canvas);
//                    output.println( imageView.draw(canvas));
                    out.write(bitmapdata);
                    out.flush();
//                output.flush();
//                output.close();
                    out.close();
                    s.close();

                } catch (IOException e) {
                    e.printStackTrace();
                } //catch (JSONException e) {
//                    e.printStackTrace();
            }
        });
        thread.start();
    }

}
