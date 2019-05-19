package com.tamimi.sundos.restpos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tamimi.sundos.restpos.BackOffice.MenuRegistration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ReceiveCloud {

    private Context context;
    private int groupType;
    private ProgressDialog progressDialog;
    private JSONObject obj;


    public int maxGroupSerial = -1;

    public ReceiveCloud(Context context, int groupType) {
        this.context = context;
        this.groupType = groupType;
    }

    public void startReceiving(String flag) {

        if (flag.equals("MaxGroupSerial"))
            new JSONTaskMaxGroupSerial().execute();

    }

    private class JSONTaskMaxGroupSerial extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setCancelable(false);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setProgress(0);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/GetMaxGroupSerial?";

                String data = "Compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "CompYear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "POSNO=" + URLEncoder.encode("1", "UTF-8");

                URL url = new URL(link + data);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
//                httpURLConnection.setRequestMethod("POST");

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null && s.contains("MaxSerial")) {
                try {
                    JSONObject obj = new JSONObject(s);
                    maxGroupSerial = Integer.parseInt(obj.getString("MaxSerial"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("e ", e.getMessage());
                }
            } else {
                Log.e("tag", "****Failed to fitch MaxSerial data");
            }

            MenuRegistration menuRegistration = new MenuRegistration();
            if (groupType == 2)
                menuRegistration.storeCategory(maxGroupSerial + 1);
            else
                menuRegistration.storeFamily(maxGroupSerial + 1);

//            progressDialog.dismiss();
        }
    }


}
