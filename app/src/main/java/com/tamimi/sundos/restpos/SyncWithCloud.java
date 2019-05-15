package com.tamimi.sundos.restpos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tamimi.sundos.restpos.Models.FamilyCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SyncWithCloud {
    private Context context;
    private ProgressDialog progressDialog;
    private DatabaseHandler mDHandler;

    public SyncWithCloud(Context context) {
        mDHandler = new DatabaseHandler(context);
        this.context = context;
    }

    public void startSyncing(String flag) {

        if (flag.equals("menuRegistration"))
            new SyncMenuRegistration().execute();

    }

    private class SyncMenuRegistration extends AsyncTask<String, String, String> {
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

//                String data = "compno=" + URLEncoder.encode("302", "UTF-8") + "&" +
//                        "compyear=" + URLEncoder.encode("2018", "UTF-8") + "&" +
//                        "voucher=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");
//
                URL url = new URL(link);

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
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);

            if (JsonResponse != null && JsonResponse.contains("Voucher Saved Successfully")) {
                Log.e("tag", "****Success");

                try {

                    JSONObject parentObject = new JSONObject(JsonResponse);
                    JSONArray parentArray = parentObject.getJSONArray("FAMILY_CATEGORY_TABLE");

                    List<FamilyCategory> familyCategoriesListCloud = new ArrayList<>();
                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);

                        FamilyCategory obj = new FamilyCategory();
                        obj.setSerial(finalObject.getInt("SERIAL"));
                        obj.setType(finalObject.getInt("TYPE"));
                        obj.setName(finalObject.getString("NAME_CATEGORY_FAMILY"));
//                        obj.setCatPic(finalObject.getInt("CATEGORY_PIC"));

                        familyCategoriesListCloud.add(obj);
                    }

                    List<FamilyCategory> familyCategoriesListLocal = mDHandler.getAllFamilyCategory();

                    List<FamilyCategory> resultList = unionFamilyCategory(familyCategoriesListCloud, familyCategoriesListLocal);

                    mDHandler.deleteAllFamilyCategory();
                    for (int i = 0; i < resultList.size(); i++) {
                        mDHandler.addFamilyCategory(resultList.get(i));
                    }

                    SendCloud sendCloud = new SendCloud(context, new JSONObject());
                    sendCloud.startSending("FamilyCategory");

//                    List<FamilyCategory> familyCategoriesListCloud = mDHandler.getAllFamilyCategory();
//                    familyCategoriesListCloud.remove(familyCategoriesListCloud.size()-1);
//                    familyCategoriesListCloud.remove(familyCategoriesListCloud.size()-1);
//                    for(int i = 0 ; i<familyCategoriesListCloud.size() ; i++){
//                        Log.e("list 1 --", familyCategoriesListCloud.get(i).getName());
//                    }
//
//                    List<FamilyCategory> familyCategoriesListLocal = mDHandler.getAllFamilyCategory();
//                    familyCategoriesListLocal.remove(0);
//                    familyCategoriesListLocal.remove(0);
//                    for(int i = 0 ; i<familyCategoriesListLocal.size() ; i++){
//                        Log.e("list 2 --", familyCategoriesListLocal.get(i).getName());
//                    }
//
//                    List<FamilyCategory> resultList = unionFamilyCategory(familyCategoriesListCloud, familyCategoriesListLocal);
//                    for(int i = 0 ; i<resultList.size() ; i++){
//                        Log.e("list 3 --", resultList.get(i).getName());
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag", "****Failed to export data");
            }
            progressDialog.dismiss();
        }
    }

    private List<FamilyCategory> unionFamilyCategory(List<FamilyCategory> familyCategoriesListCloud, List<FamilyCategory> familyCategoriesListLocal) {

        List<FamilyCategory> result = familyCategoriesListCloud;
        for (int i = 0; i < familyCategoriesListLocal.size(); i++) {
            boolean exists = false;
            for (int j = 0; j < familyCategoriesListCloud.size(); j++) {
                if (familyCategoriesListLocal.get(i).getSerial() == familyCategoriesListCloud.get(j).getSerial() &&
                        familyCategoriesListLocal.get(i).getType() == familyCategoriesListCloud.get(j).getType() &&
                        familyCategoriesListLocal.get(i).getName().equals(familyCategoriesListCloud.get(j).getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists)
                result.add(familyCategoriesListLocal.get(i));
        }
        return result;
    }
}
