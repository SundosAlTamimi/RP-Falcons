package com.tamimi.sundos.restpos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SendCloud {

    private Context context;
    private ProgressDialog progressDialog;
    private JSONObject obj;

    public SendCloud(Context context, JSONObject obj) {
        this.obj = obj;
        this.context = context;
    }

    public void startSending(String flag) {
        if (flag.equals("kitchen"))
            new JSONTaskKitchen().execute();

        if (flag.equals("Server"))
            new JSONTaskServer().execute();

        if (flag.equals("FamilyCategory"))
            new JSONTaskServer().execute();
    }

    private class JSONTaskKitchen extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/RestSaveKitchenScreen?";

                String data = "compno=" + URLEncoder.encode("302", "UTF-8") + "&" +
                        "compyear=" + URLEncoder.encode("2018", "UTF-8") + "&" +
                        "voucher=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link + data);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

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

            if (s != null && s.contains("Voucher Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
//
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
            progressDialog.dismiss();
        }
    }

    private class JSONTaskServer extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/RestSaveOrder?";

                String data = "compno=" + URLEncoder.encode("302", "UTF-8") + "&" +
                        "compyear=" + URLEncoder.encode("2018", "UTF-8") + "&" +
                        "voucher=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link + data);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

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

            if (s != null && s.contains("Voucher Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
            progressDialog.dismiss();
        }
    }

}


