package com.tamimi.sundos.restpos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tamimi.sundos.restpos.Models.FamilyCategory;
import com.tamimi.sundos.restpos.Models.ForceQuestions;
import com.tamimi.sundos.restpos.Models.Items;
import com.tamimi.sundos.restpos.Models.Modifier;
import com.tamimi.sundos.restpos.Models.Shift;
import com.tamimi.sundos.restpos.Models.VoidResons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SyncWithCloud2 {
    private Context context;
    private ProgressDialog progressDialog;
    private DatabaseHandler mDHandler;
    public static String data1,data2,data3 ;
    String okGo="f";

    public SyncWithCloud2(Context context) {
        mDHandler = new DatabaseHandler(context);
        this.context = context;
    }

    public void startSyncing(String flag) {

        if (flag.equals("menuRegistration"))
            new SyncMenuRegistration().execute();

        if(flag.equals("sync")){

//            new SyncModifier().execute();
            new SyncForceQ().execute();
//            new SyncItem().execute();
//            new SyncCategory().execute();
//            new SyncShiftNo().execute();
//            new SyncVoidReason().execute();
//            new  SyncitemPic().execute();

        }





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
//            progressDialog.dismiss();
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


//    private class SyncModifier extends AsyncTask<String, String, String> {
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            progressDialog = new ProgressDialog(context);
////            progressDialog.setCancelable(false);
////            progressDialog.setMessage("Loading...");
////            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////            progressDialog.setProgress(0);
////            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
//            try {
//                String link = "http://Falconssoft.net/RestService/FSAppServiceDLL.dll/GetModifer";
//
//                String data = "compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
//                        "compyear=" + URLEncoder.encode("2019", "UTF-8") ;
////
//                URL url = new URL(link);
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setRequestMethod("POST");
//
//
//
//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();
//
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//                StringBuffer stringBuffer = new StringBuffer();
//
//                while ((JsonResponse = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(JsonResponse + "\n");
//                }
//
//                bufferedReader.close();
//                inputStream.close();
//                httpURLConnection.disconnect();
//
//                Log.e("tag", "modifier -->" + stringBuffer.toString());
//
//                return stringBuffer.toString();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e("tag", "Error closing stream", e);
//                    }
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String JsonResponse) {
//            super.onPostExecute(JsonResponse);
//
//            if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":\"0\"")) {
//                Log.e("tag Modifier", "****Success");
//
//                try {
//
//                    JSONObject parentObject = new JSONObject(JsonResponse);
//                    JSONArray parentArray = parentObject.getJSONArray("Data");
//
//                    List<Modifier>itemModifer=new ArrayList<>();
//
//                    for (int i = 0; i < parentArray.length(); i++) {
//                        JSONObject finalObject = parentArray.getJSONObject(i);
//
//                        Modifier obj = new Modifier();
//                        obj.setModifierNumber(finalObject.getInt("MODIFIER_NO"));
//                        obj.setModifierName(finalObject.getString("MODIFIER_NAME"));
//                        obj.setModifierActive(finalObject.getInt("ACTIVE"));
////
//                        itemModifer.add(obj);
//
//                    }
//
////
//                    mDHandler.deleteAllModifier();
//                    for (int i = 0; i < itemModifer.size(); i++) {
//                        mDHandler.addModifierItem(itemModifer.get(i));
//                    }
//
////
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                Log.e("tag Modifier", "****Failed to export data");
//            }
////            progressDialog.dismiss();
//        }
//    }

    private class SyncForceQ extends AsyncTask<String, String, String> {
//        private String JsonResponse = null;
//        private HttpURLConnection urlConnection = null;
//        private BufferedReader reader = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_LIGHT);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019

            SyncForceQ1();
            SyncModifier1();
            SyncitemPic1();

            return okGo;
        }

        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);

//            if(okGo != "f"){
//            progressDialog.dismiss();}
            if (progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }

//        }
    }


    public void SyncModifier1(){
Log.e("syM","be");
        String JsonResponse = null;
         HttpURLConnection urlConnection = null;
         BufferedReader reader = null;
        try {
            String link = "http://Falconssoft.net/RestService/FSAppServiceDLL.dll/GetModifer";

            String data = "compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
                    "compyear=" + URLEncoder.encode("2019", "UTF-8") ;
//
            URL url = new URL(link);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");



            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(data);
            wr.flush();
            wr.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();

            while ((JsonResponse = bufferedReader.readLine()) != null) {
                stringBuffer.append(JsonResponse + "\n");
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            Log.e("tag", "modifier -->" + stringBuffer.toString());

            JsonResponse= stringBuffer.toString();
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

        if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":\"0\"")) {
            Log.e("tag Modifier", "****Success");

            try {

                JSONObject parentObject = new JSONObject(JsonResponse);
                JSONArray parentArray = parentObject.getJSONArray("Data");

                List<Modifier>itemModifer=new ArrayList<>();

                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    Modifier obj = new Modifier();
                    obj.setModifierNumber(finalObject.getInt("MODIFIER_NO"));
                    obj.setModifierName(finalObject.getString("MODIFIER_NAME"));
                    obj.setModifierActive(finalObject.getInt("ACTIVE"));
//
                    itemModifer.add(obj);

                }

//
                mDHandler.deleteAllModifier();
                for (int i = 0; i < itemModifer.size(); i++) {
                    mDHandler.addModifierItem(itemModifer.get(i));
                }

//

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("tag Modifier", "****Failed to export data");
        }
//            progressDialog.dismiss();
        Log.e("syM","end");



    }


    public void SyncForceQ1(){
        Log.e("syF","be");
         String JsonResponse = null;
         HttpURLConnection urlConnection = null;
         BufferedReader reader = null;

        try {
            String link = "http://Falconssoft.net/RestService/FSAppServiceDLL.dll/GetForceQ";

            String data = "compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
                    "compyear=" + URLEncoder.encode("2019", "UTF-8") ;
//
            URL url = new URL(link);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");



            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(data);
            wr.flush();
            wr.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();

            while ((JsonResponse = bufferedReader.readLine()) != null) {
                stringBuffer.append(JsonResponse + "\n");
            }

            JsonResponse= stringBuffer.toString();

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            Log.e("tag", "Force Q -->" + stringBuffer.toString());

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

        if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":\"0\"")) {
            Log.e("tag fotceQ", "****Success");

            try {

                JSONObject parentObject = new JSONObject(JsonResponse);
                JSONArray parentArray = parentObject.getJSONArray("Data");

                List<ForceQuestions>itemForceQuestions=new ArrayList<>();
                //"QUESTION_NO":"2","QUESTION_TEXT":"Question No 2","MULTIPLE_ANSWER":"0","ANSWER":"No Sugar"}

                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    ForceQuestions obj = new ForceQuestions();
                    obj.setQuestionNo(finalObject.getInt("QUESTION_NO"));
                    obj.setQuestionText(finalObject.getString("QUESTION_TEXT"));
                    obj.setMultipleAnswer(finalObject.getInt("MULTIPLE_ANSWER"));
                    obj.setAnswer(finalObject.getString("ANSWER"));
//
                    itemForceQuestions.add(obj);

                }

//
                mDHandler.deleteAllForceQ();
                for (int i = 0; i < itemForceQuestions.size(); i++) {
                    mDHandler.addForceQuestion(itemForceQuestions.get(i));
                }

//

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("tag fotceQ", "****Failed to export data");
        }
        Log.e("syf","end");
    }



//    private class SyncItem extends AsyncTask<String, String, String> {
//        private String JsonResponse = null;
//        private HttpURLConnection urlConnection = null;
//        private BufferedReader reader = null;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            progressDialog = new ProgressDialog(context);
////            progressDialog.setCancelable(false);
////            progressDialog.setMessage("Loading...");
////            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////            progressDialog.setProgress(0);
////            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
//            try {
//                String link = "http://Falconssoft.net/RestService/FSAppServiceDLL.dll/SyncGetItems";
//
//                String data = "compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
//                        "compyear=" + URLEncoder.encode("2019", "UTF-8") ;
////
//                URL url = new URL(link);
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setRequestMethod("POST");
//
//
//
//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();
//
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//                StringBuffer stringBuffer = new StringBuffer();
//
//                while ((JsonResponse = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(JsonResponse + "\n");
//                }
//
//                bufferedReader.close();
//                inputStream.close();
//                httpURLConnection.disconnect();
//
//                Log.e("tag", "items -->" + stringBuffer.toString());
//
//                return stringBuffer.toString();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e("tag", "Error closing stream", e);
//                    }
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String JsonResponse) {
//            super.onPostExecute(JsonResponse);
//
//            if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":\"0\"")) {
//                Log.e("tag item", "****Success");
//
//                try {
//
//                    JSONObject parentObject = new JSONObject(JsonResponse);
//                    JSONArray parentArray = parentObject.getJSONArray("Data");
//
//                    List<Items>items=new ArrayList<>();
//
//                    for (int i = 0; i <parentArray.length(); i++) {
//                        JSONObject finalObject = parentArray.getJSONObject(i);
//
//                        //
//
//
//                        Items obj = new Items();
//                        obj.setMenuCategory(finalObject.getString("MENU_CATEGORY"));
//                        obj.setMenuName(finalObject.getString("MENU_NAME"));
//                        obj.setFamilyName(finalObject.getString("FAMILY_NAME"));
//
//                        obj.setPrice(finalObject.getDouble("PRICE"));
//                        obj.setTaxType(finalObject.getInt("TAX_TYPE"));
//                        obj.setTax(finalObject.getDouble("TAX_PERCENT"));
//                        obj.setSecondaryName(finalObject.getString("SECONDARY_NAME"));
//
//                        obj.setKitchenAlias(finalObject.getString("KITCHEN_NAME"));
//                        try {
//                            obj.setItemBarcode(finalObject.getInt("ITEM_BARCODE"));
//                        }catch (Exception e){
//                            obj.setItemBarcode(-10);
//                        }
//                        obj.setStatus(finalObject.getInt("STATUS"));
//                        obj.setItemType(finalObject.getInt("ITEM_TYPE"));
//
//                        obj.setDescription(finalObject.getString("DESCRIPTION"));
//                        obj.setInventoryUnit(finalObject.getString("INVENTORY_UNIT"));
//                        obj.setWastagePercent(finalObject.getInt("WASTAGE_PERCENT"));
//                        obj.setDiscountAvailable(finalObject.getInt("DISCOUNT_AVAILABLE"));
//
//
//                        obj.setPointAvailable(finalObject.getInt("POINT_AVAILABLE"));
//                        obj.setOpenPrice(finalObject.getInt("OPEN_PRICE"));
//                        obj.setKitchenPrinter(finalObject.getString("KITCHEN_PRINTER_TO_USE"));
//                        obj.setUsed(finalObject.getInt("USED"));
//                        obj.setShowInMenu(finalObject.getInt("SHOW_IN_MENU"));
//
////
//                        items.add(obj);
//
//                    }
//
// Log.e("tag item1", "item is update ");
//                    mDHandler.deleteAllItems();
//                    for (int i = 0; i < items.size(); i++) {
//                        mDHandler.addItem(items.get(i));
////                        progressDialog.setMessage("ADDING "+items.get(i).getName()+"...");
//                    }
//
//           Log.e("tag item", "item is update ");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                Log.e("tag item", "****Failed to export data");
//            }
////            progressDialog.dismiss();
//        }
//    }
//
//
//    private class SyncCategory extends AsyncTask<String, String, String> {
//        private String JsonResponse = null;
//        private HttpURLConnection urlConnection = null;
//        private BufferedReader reader = null;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            progressDialog = new ProgressDialog(context);
////            progressDialog.setCancelable(false);
////            progressDialog.setMessage("Loading...");
////            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////            progressDialog.setProgress(0);
////            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
//            try {
//                String link = "http://Falconssoft.net/RestService/FSAppServiceDLL.dll/GetFamCategUnit";
//
//                String data = "compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
//                        "compyear=" + URLEncoder.encode("2019", "UTF-8") ;
////
//                URL url = new URL(link);
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setRequestMethod("POST");
//
//
//
//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();
//
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//                StringBuffer stringBuffer = new StringBuffer();
//
//                while ((JsonResponse = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(JsonResponse + "\n");
//                }
//
//                bufferedReader.close();
//                inputStream.close();
//                httpURLConnection.disconnect();
//
//                Log.e("tag", "category -->" + stringBuffer.toString());
//
//                return stringBuffer.toString();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e("tag", "Error closing stream", e);
//                    }
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String JsonResponse) {
//            super.onPostExecute(JsonResponse);
//
//            if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":\"0\"")) {
//                Log.e("tag category", "****Success");
//
//                try {
//
//                    JSONObject parentObject = new JSONObject(JsonResponse);
//                    JSONArray parentArray = parentObject.getJSONArray("Data");
//
//                    List<FamilyCategory>items=new ArrayList<>();
//
//                    for (int i = 0; i < parentArray.length(); i++) {
//                        JSONObject finalObject = parentArray.getJSONObject(i);
//
//                        //
//
//
//                        FamilyCategory obj = new FamilyCategory();
//                        obj.setName(finalObject.getString("NAME_CATEGORY_FAMILY"));
//                        obj.setType(finalObject.getInt("ITYPE"));
//                        obj.setSerial(finalObject.getInt("SERIAL"));
//
////                        obj.setCatPic(finalObject.getDouble("PRICE"));
//
////
//                        items.add(obj);
//
//                    }
//
////
//                        mDHandler.deleteAllFamilyCategory();
//                    for (int i = 0; i < items.size(); i++) {
//                        mDHandler.addFamilyCategory(items.get(i));
////                        progressDialog.setMessage("ADDING ("+items.get(i).getName()+")...");
//                    }
//
////
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                Log.e("tag category", "****Failed to export data");
//            }
////            progressDialog.dismiss();
//        }
//    }
//
//
//    private class SyncShiftNo extends AsyncTask<String, String, String> {
//        private String JsonResponse = null;
//        private HttpURLConnection urlConnection = null;
//        private BufferedReader reader = null;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            progressDialog = new ProgressDialog(context);
////            progressDialog.setCancelable(false);
////            progressDialog.setMessage("Loading...");
////            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////            progressDialog.setProgress(0);
////            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
//            try {
//                String link = "http://Falconssoft.net/RestService/FSAppServiceDLL.dll/GetShifts";
//
//                String data = "compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
//                        "compyear=" + URLEncoder.encode("2019", "UTF-8") ;
////
//                URL url = new URL(link);
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setRequestMethod("POST");
//
//
//
//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();
//
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//                StringBuffer stringBuffer = new StringBuffer();
//
//                while ((JsonResponse = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(JsonResponse + "\n");
//                }
//
//                bufferedReader.close();
//                inputStream.close();
//                httpURLConnection.disconnect();
//
//                Log.e("tag", "shift -->" + stringBuffer.toString());
//
//                return stringBuffer.toString();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e("tag", "Error closing stream", e);
//                    }
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String JsonResponse) {
//            super.onPostExecute(JsonResponse);
//
//            if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":\"0\"")) {
//                Log.e("tag shift", "****Success");
//
//                try {
//
//                    JSONObject parentObject = new JSONObject(JsonResponse);
//                    JSONArray parentArray = parentObject.getJSONArray("Data");
//
//                    List<Shift>items=new ArrayList<>();
//
//                    for (int i = 0; i <parentArray.length(); i++) {
//                        JSONObject finalObject = parentArray.getJSONObject(i);
//
//                        //
//
//                      //  SHIFT_NO":"1","SHIFT_NAME":"SHIFT A","FROM_TIME":"06:00","TO_TIME":"04:00"
//                        Shift obj = new Shift();
//                        obj.setShiftNo(finalObject.getInt("SHIFT_NO"));
//                        obj.setShiftName(finalObject.getString("SHIFT_NAME"));
//                        obj.setFromTime(finalObject.getString("FROM_TIME"));
//
//                        obj.setToTime(finalObject.getString("TO_TIME"));
//
//
////
//                        items.add(obj);
//
//                    }
//
//                    Log.e("tag shift", "shift is update ");
//                    mDHandler.deleteAllShift();
//                    for (int i = 0; i < items.size(); i++) {
//                        mDHandler.addShift(items.get(i));
////                        progressDialog.setMessage("ADDING "+items.get(i).getName()+"...");
//                    }
//
//                    Log.e("tag shift", "shift is update ");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                Log.e("tag item", "****Failed to export data");
//            }
////            progressDialog.dismiss();
//        }
//    }
//
//
//    private class SyncVoidReason extends AsyncTask<String, String, String> {
//        private String JsonResponse = null;
//        private HttpURLConnection urlConnection = null;
//        private BufferedReader reader = null;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            progressDialog = new ProgressDialog(context);
////            progressDialog.setCancelable(false);
////            progressDialog.setMessage("Loading...");
////            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////            progressDialog.setProgress(0);
////            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
//            try {
//                String link = "http://Falconssoft.net/RestService/FSAppServiceDLL.dll/GetVoidReason";
//
//                String data = "compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
//                        "compyear=" + URLEncoder.encode("2019", "UTF-8") ;
////
//                URL url = new URL(link);
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setRequestMethod("POST");
//
//
//
//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();
//
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//                StringBuffer stringBuffer = new StringBuffer();
//
//                while ((JsonResponse = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(JsonResponse + "\n");
//                }
//
//                bufferedReader.close();
//                inputStream.close();
//                httpURLConnection.disconnect();
//
//                Log.e("tag", "voidReson -->" + stringBuffer.toString());
//
//                return stringBuffer.toString();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e("tag", "Error closing stream", e);
//                    }
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String JsonResponse) {
//            super.onPostExecute(JsonResponse);
//
//            if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":\"0\"")) {
//                Log.e("tag void", "****Success");
//
//                try {
//
//                    JSONObject parentObject = new JSONObject(JsonResponse);
//                    JSONArray parentArray = parentObject.getJSONArray("Data");
//
//                    List<VoidResons>itemModifer=new ArrayList<>();
//
//                    for (int i = 0; i < parentArray.length(); i++) {
//                        JSONObject finalObject = parentArray.getJSONObject(i);
//
//                        //SHIFT_NO":"1","SHIFT_NAME":"SHIFT A","USER_NUMBER":"1","USER_NAME":"ALAA","VOID_REASON":"خطأ مطبخ","SDATE":"04-08-2019","ACTIVEATED":"0"
//
//                        VoidResons obj = new VoidResons();
//                        obj.setShiftNo(finalObject.getInt("SHIFT_NO"));
//                        obj.setShiftName(finalObject.getString("SHIFT_NAME"));
//                        obj.setUserNo(finalObject.getInt("USER_NUMBER"));
//                        obj.setUserName(finalObject.getString("USER_NAME"));
//                        obj.setVoidReason(finalObject.getString("VOID_REASON"));
//                        obj.setDate(finalObject.getString("SDATE"));
//                        obj.setActiveated(finalObject.getInt("ACTIVEATED"));
//
////
//                        itemModifer.add(obj);
//
//                    }
//
////
//                    mDHandler.deleteAllVoidReasons();
//                    for (int i = 0; i < itemModifer.size(); i++) {
//                        mDHandler.addVoidReason(itemModifer.get(i));
//                    }
//
////
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                Log.e("tag voidReson", "****Failed to export data");
//            }
////            progressDialog.dismiss();
//        }
//    }
//
//
//    private class SyncitemPic extends AsyncTask<String, String, String> {
//        private String JsonResponse = null;
//        private HttpURLConnection urlConnection = null;
//        private BufferedReader reader = null;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            progressDialog = new ProgressDialog(context);
////            progressDialog.setCancelable(false);
////            progressDialog.setMessage("Loading...");
////            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////            progressDialog.setProgress(0);
////            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
//            try {
////                String link = "http://Falconssoft.net/RestService/FSAppServiceDLL.dll/GetVoidReason";
////http://10.0.0.16:8081/GetItemsPic?CompNo=736&compYear=2019
//
//                String link = "http://10.0.0.16:8081/GetItemsPic";
//                String data = "compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
//                        "compyear=" + URLEncoder.encode("2019", "UTF-8") ;
////
//                URL url = new URL(link);
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setRequestMethod("POST");
//
//
//
//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();
//
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//                StringBuffer stringBuffer = new StringBuffer();
//
//                while ((JsonResponse = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(JsonResponse + "\n");
//                }
//
//                bufferedReader.close();
//                inputStream.close();
//                httpURLConnection.disconnect();
//
//                Log.e("tag", "voidReson -->" + stringBuffer.toString());
//
//                return stringBuffer.toString();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e("tag", "Error closing stream", e);
//                    }
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String JsonResponse) {
//            super.onPostExecute(JsonResponse);
//
//            if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":\"0\"")) {
//                Log.e("tag void", "****Success");
//
//                try {
//
//                    JSONObject parentObject = new JSONObject(JsonResponse);
//                    JSONArray parentArray = parentObject.getJSONArray("Data");
//
//                    List<VoidResons>itemModifer=new ArrayList<>();
//
////                    for (int i = 0; i < parentArray.length(); i++) {
////                        JSONObject finalObject = parentArray.getJSONObject(i);
//
//                        //SHIFT_NO":"1","SHIFT_NAME":"SHIFT A","USER_NUMBER":"1","USER_NAME":"ALAA","VOID_REASON":"خطأ مطبخ","SDATE":"04-08-2019","ACTIVEATED":"0"
//
////                        VoidResons obj = new VoidResons();
////                        obj.setShiftNo(finalObject.getInt("SHIFT_NO"));
////                        obj.setShiftName(finalObject.getString("SHIFT_NAME"));
////                        obj.setUserNo(finalObject.getInt("USER_NUMBER"));
////                        obj.setUserName(finalObject.getString("USER_NAME"));
////                        obj.setVoidReason(finalObject.getString("VOID_REASON"));
////                        obj.setDate(finalObject.getString("SDATE"));
////                        obj.setActiveated(finalObject.getInt("ACTIVEATED"));
//
////
////                        itemModifer.add(obj);
//                    JSONObject finalObject = parentArray.getJSONObject(0);
//                        data1=finalObject.getString("ITEMPIC");
//                        Log.e("bitmap in side ",""+data1);
//
//                    JSONObject finalObject1 = parentArray.getJSONObject(1);
//                        data2=finalObject1.getString("ITEMPIC");
//
//
//                    JSONObject finalObject2 = parentArray.getJSONObject(2);
//                        data3=finalObject2.getString("ITEMPIC");
//
//
////                    }
//
////
////                    mDHandler.deleteAllVoidReasons();
////                    for (int i = 0; i < itemModifer.size(); i++) {
////                        mDHandler.addVoidReason(itemModifer.get(i));
////                    }
//
////                    progressDialog.dismiss();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                Log.e("tag voidReson", "****Failed to export data");
////                progressDialog.dismiss();
//            }
//
//        }
//    }

public void SyncitemPic1(){

     String JsonResponse = null;
     HttpURLConnection urlConnection = null;
     BufferedReader reader = null;

    try {
//                String link = "http://Falconssoft.net/RestService/FSAppServiceDLL.dll/GetVoidReason";
//http://10.0.0.16:8081/GetItemsPic?CompNo=736&compYear=2019

        String link = "http://10.0.0.16:8081/GetItemsPic";
        String data = "compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
                "compyear=" + URLEncoder.encode("2019", "UTF-8") ;
//
        URL url = new URL(link);

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod("POST");



        DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
        wr.writeBytes(data);
        wr.flush();
        wr.close();

        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuffer stringBuffer = new StringBuffer();

        while ((JsonResponse = bufferedReader.readLine()) != null) {
            stringBuffer.append(JsonResponse + "\n");
        }

        bufferedReader.close();
        inputStream.close();
        httpURLConnection.disconnect();

        Log.e("tag", "voidReson -->" + stringBuffer.toString());

     JsonResponse=stringBuffer.toString();
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

    if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":\"0\"")) {
        Log.e("tag void", "****Success");

        try {

            JSONObject parentObject = new JSONObject(JsonResponse);
            JSONArray parentArray = parentObject.getJSONArray("Data");

            List<VoidResons>itemModifer=new ArrayList<>();

//                    for (int i = 0; i < parentArray.length(); i++) {
//                        JSONObject finalObject = parentArray.getJSONObject(i);

            //SHIFT_NO":"1","SHIFT_NAME":"SHIFT A","USER_NUMBER":"1","USER_NAME":"ALAA","VOID_REASON":"خطأ مطبخ","SDATE":"04-08-2019","ACTIVEATED":"0"

//                        VoidResons obj = new VoidResons();
//                        obj.setShiftNo(finalObject.getInt("SHIFT_NO"));
//                        obj.setShiftName(finalObject.getString("SHIFT_NAME"));
//                        obj.setUserNo(finalObject.getInt("USER_NUMBER"));
//                        obj.setUserName(finalObject.getString("USER_NAME"));
//                        obj.setVoidReason(finalObject.getString("VOID_REASON"));
//                        obj.setDate(finalObject.getString("SDATE"));
//                        obj.setActiveated(finalObject.getInt("ACTIVEATED"));

//
//                        itemModifer.add(obj);
            JSONObject finalObject = parentArray.getJSONObject(0);
//            data1=finalObject.getString("ITEMPIC");
//            data1= "iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAAEH5aXCAAAACXBIWXMAAAsTAAALEwEAmpwYAAA8GUlEQVR42u29B5wcZf0\\/\\/p6Z7X33er9ceu8khBogIE1Q6UKkYwFsXxEVBQuioogFEQuIyJeiKE2pgUASIIWQnrtcyd3lyu7dbe+7szu\\/9zN3G44QUPnr95uv\\/zz3mtvd2Zl5nk9\\/f562kqZp+GeL9IFvSvKNEQWYICEcjsLtdjuTyeTvnE7n+nA4\\/AOv16tfHAwGUVZW9s6bEkPDmq+yGv6oiqRTQVUoBEd5GUJ89fl863nT0v03FXhTV6qg3fbKXsyoL8fZMz2ojCfwB38fNqhluHOyF5u2bcFJixbprdNvCgZj2rpIDs8M+WHx1sI\\/GED76nXYWzYDWPMYrrjlG0jwAbVD+\\/CVC04ZvQlaSjv7j20o91iwPu3EMZufxN+e+AtSxSLME4\\/AnGPn4pwzz8YlkxTEQvE9YzUFNVOZD3O+9QdUPvUzKOd9B7nNa7H39JVIrG+D1vYiymctxY6bVsDj8Uj7WS5uPOWmu\\/Grz1+KC848F729O\\/Ghk47BWn8K6V1vYsPG9Zg5Y\\/rbNJXKddddp913332wWmywOxz46lduwi9++TO8\\/NIqeL0+\\/YZ3Cff111\\/HtGnTeIF3kPKpZlMQiUTOpNyelmX5X6AR\\/\\/ab\\/vkbErwhEhtEndMLDWZEo1FQQT+bSCSWu1yuq\\/h5WDDiXTfUusrujYQzl3ndVvRSa+vtRgSjQ6jwVpaUdJTFeaHVmZgmxw04oSsK604\\/fvzRqZglJXHKXjueaCpAdtiRSYR5c9moIAOxqPZqtICBnAlHOIz4QZeKKeoAntjRjTnZJC698kwcbclBZnMlCuuvuzTLaclsHr+NKWh88B489\\/p2bN\\/4V5g\\/dCUmnX4ZevcN4IstCr5+3rJRLrGN2oDTjc8ftxzdfW0448KrkZ91MX736kaoHZth\\/thH8MRlizA3l9nPVu2RRx7BvMUnYtatL8LzxOeQmXocZhYH4W9vQ1wyo71tKwn3vq2MZKNWV1+vK2Iul4Mim3DPPfdg1uypus3zeKcirl69GkuXLkU6nT6GivgqaRMKGaIilolrSk7jf0A1PojG\\/rPlf66S5NgHobsVrkqYtDy\\/MUHTpB+RuV8QYvCMMVcwXRSh+JIk6Z\\/FKz\\/\\/iafPLT245LkPWkmtw619+fUIH2rEZ6pMcJZZgKIZI3TMJq0Al8uNx\\/cNoTOSwcyqWpxQbYCCIpKREJ8gwy4q52s0GJTeUUkkFIbD4+7xJ9RGgzGHN9tDOGZqLc7YGMOlPiNeoCE\\/0yFjutqOC+fVoj2iwWUsotVfRF2DGUbZhq9PscLDh4\\/EIrDmJNjdJqyPZVFttu9X3pNf2tn73Kx6L\\/4S7MfOYTMM5NGaPSEMx1XcvfIIHK9JePKFVbjsmuuQzuZgN+dx3jd+iz\\/d\\/mm0NMzA1rc2on9wDyJZM\\/4ypOKaujyrLMDn8VyvV0K+fpUKfauobU17FLu9Try05i08uo0XNtbAHJeQoZVXIoEVC2rx8iudCHXuQ8rLtqeGYa9fBvTugGa1Yv7iKjx5SiM8Djd0LkUiv39HIBEu3Ov2YP32t3D6Vgem3nUpIuEElKUr0OOshXnmqRhOy1D8AXhe+B2qVUaopmtQbPbgmmsuwCP0HCdffQPu\\/+xZCMdUXSEYGqR3qfDw8LBmNplw\\/bU34JmXnkE8OAhNsaDCXYe0GkcyHofNrmDBh87CgqZm\\/OyXv0NGy8GlxhBJZhEODqNQKAjNquHj\\/O9pJ8JXWCyWCXRlXX19fWinF+rt7dW\\/q66uxpw583DWWWfBbDbDwSioa2YkIl6Eb1n7v2eMhyv5\\/2klwkGaixoS0RxG5CJqTTJjoxFFnovFYjAajbDZbEcy\\/F2squpEq9XaqyjKw4QhLwmDK4U\\/AVNofCWk8Q9XoolKTDROu90ugjPy+bzAOzAYDPvd\\/lglNlaS\\/ocqCSoaJrosWnAkDKNJoWt3IZfOY7ggoz0ewzHVXuTo9lPFBFzuKhhJCb0EhEsac+3S+1YSimZOd3ssT39xcxqfaMxhqpUu3eqFmksjkUxin5rFaykFZo8NjpSKpdVO1EsKQvRvRUORgcvJoKAgEQrd6vX5bnpHJap4V9SO0IrS+tU9PTi2woPZr0VwfLkVMSmHhbIZl892QQ6mkLACFlnB5zYEUV7bgLMbZSy1yTCreQQ0BXI6hiqXDhe\\/wuN7+ysJB0OIGGStku45G0lgx0gC05p82Dqk4cmIir5oHB3twzhtmhUdqgv1xizaUmYsccrIVjkQHRjC3UvrEQxloJnS8Dl9jCU57EsqUgOhZ0mFOyLB+MQ3RiLoZWuqh+OomzsRF77Sg3leGQt4U8jmwIsEh4uqHdg1MIg2KkFFVoO25WWcc82XUVetYO9IH+aUVeKkCivaVDuOLVNEwrDf1WvdyQLM4Rj8OQvekrN4eGcc1bVudPsZ9\\/2t+OYJy\\/C73z+CJ196Fp2btkM2x6Bc\\/APM2vwSujPbcM03HsJ5RzRhVajIHGsI1832wsEKUUpkqIqam6z66qYo6h0qapUcVplr8djGTizfvgYnLJ+Je\\/\\/4OHrfeBNZoxmenB\\/TPns\\/1n7uI4h+8k7Uv3Efcjfcji9OLofdlMdnWpwUMe0mEhXqLe2HwLrKaVFc8kI7Pj59Jk79ww6YiExyNiMMKQlFaxK\\/OW4mvvfyBuzpC0IeyQGVNcxFy1GTHEFH2wC0lsn4zjmN+MoUp65VJfi\\/n13ihKwa4bEU8cMntuLZuy5H1dQl6Njejr2hKBauvBnp9qfRbPPhwZlXwWGJQts5hFjfHhiHqcJUa\\/WlW3Hza35c36QKJpVs5e3wK1hmYO5s48WG3w7inOGNeOWH16KsuhbJK38PKTWAIasPmRHaTnQYyr3nI5Ij0Fi2Etrpn0bdhoex3NGNr97zO1Sk46IOwSo9BI93kEF6EV+M\\/mfmotNx5x\\/uw7KWKiy56LOILfgY3P\\/9JaRSIUjMUbKzV+DGs4\\/BurVr8OqGjbCli1Dzw1iy4kw8eP9vx1v7u5PREts+d9PX0frWDuzZtQUZtipvlGHRnKipq4E\\/ncbRC6bjmCNPpNsx4svf+j4MSgZqOIRLL78cP7r99v1sOqirp4elUyzesWrVqs\\/f8KUvoae3h47QSMBgIxVJAS7ojV3o7+\\/Dgvnz4Keqnnb6CvzoRz8SCUrp4av5fjk9Nd6zElFefPFF4Wn3dnZ2Nvf19aOjo013+QKZ1NU14IQTTsCyZcv0SktaRJefZCU6dBEV\\/kOVTJ48Gc3NzRhz9b+lq7\\/8IK5eIPnzKGBNeOKKigr83Ur+rZHxcCWHVCX\\/E+UwIYda+c8jJDnupECSgYQf9oIPNq8BvYSuk1wSUhkJNqsRo7dIIiQI5+nih8\\/zuIpOtE7g5VIpOdNSEU6VIF3PiEUZw9Hi3x947naPx7NvfONK4L1UhKcX7S31cnxwQtKSYrWZXyWaWyaaksvm9MaKUjDIsBEyOthYTdL4YAXpZBr5bASqLIgrkBArA6ggxKjfE2dDcwV1f73jG8gMvY\\/PXkxC\\/P8SQhxeo+\\/Pr2wNbmZ0\\/\\/jCJtQSARtyORSKCgyypCcVomRRRC6nYiCZQFwtoiuUQ4xEVTisqMwynjK7MVGCDpMZBUrJWiigwk1kZni7C3ssturvhbQPaPAyfv\\/6P01IKDl88VDHyAMz5zSgwGfLshO3bO3Han8W0\\/MJXL24ERPdNniYjuVlAdXyKKQTeGVXABOaqniPEY1Ok54DqvzOmDHg3tZ+9HhqicgDqPMkEZG8qAgN4OZFsyFrOURjKR3XSsyktKLoHiWTRPOKko55glHRmYb7SMjl70nIb5gOiJImJ1bMmaWZLQlMMFjQG1aRZgpXac2So8yuXE5c\\/ddd2OusQEujF90dfcjYajAcj6BGymMu4e0VU6yYUl2B1u5ONLLh7hRTQEcCbmsZXuhPQrEB+0ZSeDOSIQcJNOwBXFlTDTUTx8T6GkyyF9AWLCBOIOLUiqhx2WAsZOEqr9jf8Fy+0NTav69X2Oqc5qa3CUmlUkL\\/Gq0GS0\\/aKGFLexdy1dVoJvZOEJXWlfsQD8bRU2bCsQYnFj61ASfPnYYzKpndxfJYG\\/OjVaqiSoTgD9IpyBoGEjZUmzJosmnoURzoymYRSQ4x66hAIMJc15DBV5llGJihbO8aQMFmx2S7B8ZiENWFHC6ZVyMGDSgQBXFLGm6TBesHsvjJ2gH8\\/sIZyEfSV5gUw70ml\\/Ed7lcoa8EfGoG1aNBVN800Z\\/VwCo93DsPK1KhFC6OxuRGVkgW7t+\\/B6kpy25hHndmJ6VYVyVwB91F9ri5TcVxzNXa27sbTf12H11Y9jPbhJGxGE5zpFFKRNGKpMGY9sAE7rr8WSnEffI21MNQfAUO5Get\\/8T2Emcyn6TC2U0V\\/tWkvjmosh3EgguWzyjGfyUpeNtGjeUWjma9Kif2E5HK5n\\/D99WnqY2csh589twmLj1qA0xucCEeDCMg+PNfahxGC4H2qjI370rAUhnH3kibc+toQjjf24rtnnYjNgQHsfGkndrTuwquvvsq0qw1XXnc5fnv7Q0hHh2D20R1nU6j86SZo934NUVcjzj73eOx64SkMrt+NU89agb+t2YDrH3gAsxwF1GZlpL1u\\/PmNHbjhqBlwMG2AVIAmG2GkB2R5lm7\\/1PHZ1R\\/5\\/pzxXRRi3PGSNZ16qqzIMaxs8GJtPI9Qahg\\/2GDGyOAAOUcv5rCj2leFIVWhnicgZdLQxHtKiwgd1W7amLsMO4MRSIM0\\/Lgfzp3rMHN2I3YFDMzSYpAnTURuymy4VRNCxTBOigSxq\\/UFpI67EL+fPQGGsihOrfLub9pYEi283BaC\\/Pn7CSHCn8yXPaLvRbg+UQxSmrrnxeZ0CI+8FYQ6MoQ\\/rW+H2elFuZneiGLvDwRQ4CMKTgvMFbUUdB1UOglf9+s4yeDH1g2r0VzfhKNcafQsuwQ\\/2F7HazR6sxwq4sMId3QhY\\/FBM9PmEUI+xzQnbUNu6Wz49rXhzPnNTLYl\\/PTYGpjV4js8lQiujDenMDN5\\/kCI8iv66qtKvrwABeVep959LCm0m5mLseTID6P3Tz+Eb8ESVDrt6NnTCpPTBldgBJkjz0CA155jiCKrSEzEP4m7N3Qha6tCV8aEnL0F5lgnsj\\/\\/NIxD7VBmnoaMx84GVUHOSzBm+Yw5J2LRRz+KLbdejymB1YgsOA9fvO1mXD\\/ZicjYwEmpUBLdfJnwDvdbeiWkuJkX36ITIsmjqkKT+s73f4ZH4EXKOBeXLp4Noz0ElYGuYzCMZ3eNICn8P2j8rjymFgehDexGYMvrSA\\/6kZ1zFCLl8+Cy2VCx+lYcf\\/wpcFs8WPXKCwhMuRCGxcdAHdyBzF9\\/hZy\\/F3Zjkigii\\/MuvBQrzrsIpy2cBJUhwOV4G\\/6QuW+wzUeK9yJXfD\\/QqI31s+iqtmXLFlx4wYep+1Zkc0mql4cNc8BF2DGY0VBfdGHQbQD6t2GY0OWKC67U73t59csI9QSRVxiRq8oxoaEZJocRFrMZDY2NePqhe5EomlCsmoajPnIpPA4n\\/nDz1XCa81i5ciW++c1v6l2Noi3j7FcAsNhBA+LBisiw+ZAgr\\/EVGZh+8qOf4ks3fQkeTxmcRhvmLV2KQDSJ8y44B+1benD2GbMxGLbixz\\/4CqwWpw74xEBEjl6vrTOIydMnM1v\\/C1accoauIgKndW5bg4RmQjKTh89mxolnnYuv3Hgj5kxr0SN6qX3ienpWC7mfPVhb\\/y4hogtAvK5Zs8bR29vb1tHRUTswMAC\\/3693zMbjcf0Q3QEC6WYyGb1jpFgs6BLRyABV1SAzqCl8Vj6fJnNMWLRoIW762k0488On7VdtMUgo6hP3iaO8vFxA51nCCY05pHd0OXwgQtauXas3UnC5srISDQ0NwmvcTCK+xkuNCiOogQ7BarXtvz+dSUHNqzrsF88QAPMgMH688f6G7fnCyMhIXNRb6ovf\\/7wPSsj\\/pXKYkEOtHCbkUCuHCTnUymFCDrVymJBDrRwm5FArB+1pNGhxJEaK8LisaE1k0FTmgq2YQ1EzELmq+gzLsflf++8R6TBht4Dfk\\/jdQp6anUqlmvjq4zlRT1R0UvO6nUS\\/m4iGd4r7RU+keJaYMFKaQCWKQLoCKYvzevuSSX12p+gkL537VxNyLA8B5U8+EHYLmC6uE7D9gAxPzzdEbzvLm\\/zuNjbusf8NQhbzvgdJyOTxD\\/uAhIyXiEhQruDxl383IefJivwIm8EbJV0iejYni25KCWUuLzRZDCkU+WQDoqEgz+ahShaIvjSHw0ommN8mhIQym9Xf22w2lMZWxOdsNvsNEvLtfykhE8tcE5RiviMZjcmayhTWYGXDxWi4S88gBWGiM284nEbcYIKLHwagQDMVYJcltDBvJ31IkvCslObVJFsxwe306pNCRRFpskiZ9UZRgiITHBvKX8Hjxf\\/PhGwZ9N+7sS97mdnnxJF1DtRI2dHFFQV1vyqpGH3GUCIJP9PbZNIAv82KDM97SHidTYJTTHDQCjDIRhTYKNHh4nKK8RG921Nv5FhHuk6I6LQYl9a+SUIWfWBCzC7LoMNQrN6qWvC5R3bg+KMnY7KcxEIpg2qHHc4yG6Si6KoxkOMFROJJZGQVnoyEsF2GyWwlKUas2jdEvTJhMJqHXUmhyVELsyGPIz0miJEVSSogQx3LRoNUUyNk6qHFoEGxClUTIpORIfFqLieTEO2fIqSvP5drrrAaFaoHFQSrBqK4ryuO2qYqnEB7mVFmwgS7oldSlGXIuQj8SQXpTBb+gowpHgu8Ngu\\/k2DIFZBg437eGsNLqSLmWkLI2iUkYh5c1aDgSJeNhFAiidzosJ0kw2Gz0zXz+bRFIfBEKoG8mhcOgXSY8u9JSGkkT3xKq\\/GgyeD0kQnYnYygiRyozUSxJm\\/DLW0ZRF1FlBeKOJrnM0YZf93aidOmNqPJqOLK+TX4772DOLOqDMZ0Fgly1lBI026M+LN\\/ACnrRPwlUUQurWGxQ4FszuCqMiucck6fXNXbugfTp05G61ASsXgAdRXlukq67A6YjPslIb0nIWLESiI3WkPpB7972pKL8rkkEukcEqqNXiSDKmMRDrcbf9sbxq\\/6ZIS0GJrpzcJpM3YNpeGW4zCOxPDdM6ZgrrEAzaJgqG8QLY56pGxFBPNZjGg2DGVy6EjEEQpreJacmmZzY747hfMmNuKBp9\\/ADWcvpZso4Na\\/rMclx8+hbRlgzqdgoyobTOZSu3u39\\/Q0iaaL0ap3ECLmspstlqq8QfKnZQMSwRQclFPIrmJ6rhJP5ftRPQAsmF+Ba98YQNbow7lUi4f2ZdDqH8SxE5vwwM4EbDGauD2CRVYZixo9eKurG3GlDAumNsBSzGNj2AC7oYBkjx+TXRICXjt8Lh+8DiOmBNpx4cLZtDtVH1xNxVTkDWmYChLsVhtVjYRoCQxrdvSHox\\/zWhx\\/bnIY30mI8BjGohRSjQZvXyyGkUgUA3YnTtfc2OqNY37GiueoZseU83M6j9s68jitXMLEohFl9U78dG0\\/NMa7kZQRjUoM24YdMNJ2FtbShVoM2CRGeeUMg2QcUXMDcskMlk6w4SKXAdva+6iCwBETmxEZ9mNahRXmSByn1tugFOn9hJ3aTHAqRZz9651YeXQNTqorg1WWJaPzAEJYnMlUNpan6AtUsQKJf2rXEJqqK7AlFsf5zdV4fPs+FJx0i3T+uwYs2M0HX9BiR5Aq0mS0481EAv2UJOyV6BkKwr9xDY6d3oiy2lokcik8vLEHRacMn5GtjiTIvQzyW9Zg2uyZsPtaUH7kXFRThadXuRBksAwqBShJE86dXo5KBt0CbeX+TX5cvcgHp8XJWCSfQmN5\\/kBCfhlLx6\\/RchKkvApZyiPvqMDXd\\/XCmbIhbk\\/jZLcLg\\/RC84i1nu2JYbWWxUU1Puwkd4\\/zSHhJc2DvhlX48bLZSGQzWLtuMx774xPY1boeKZjhyKVhSDCORLpw0t0P46ltMaTv\\/R4srgzKGuciX9OI73\\/jCzi1oRadXgvkSAH39KexfXcbPjyhEQqB6Sfm2WHms20eNx2mYS+ddcuBhKRGVFg3bGvHwpkTYc\\/FcfOLL+Prp56Fn\\/cOY4LThhyD2pOburGjv4iZdRJFXga724h5VA+rJYOGrhGcsWAqtrS14o0Nm\\/HnJ5\\/C+jfewOQF87F3625Ichp0yMgrFtTe8gACd38XxlgPnOYKNM2vROerz6Hpwzfgl7d\\/AZ54GCP0dEHJiRf6gzT4As6dWYMJuWFkFSODtUsfj5fGOpLHD4ZqqVwGBZMLD6xtRaek4JwlE1EXGcYrMQI4WzW2RTNoo7Q6\\/EG0F6rwRV8aCVsWO7cF8ftzZyCvqdi1rQdvrt+C4MgInnj8cVhtZn08sX9nPwryEOwuO066\\/Rd4dl03Jg5sRxcx2ZUfX4Gn7vwl9u5tw9zayai84jJcfOLHsKCZsWUwj7cyGdSoGh2KG4ZiCiqDpiKAJqM\\/IdI7CSEA1ExWgjfFhrTBgttfacUrA2kcX1OJxhkOTGNQ2xTNYlveAXeFE6\\/0jmDnvjSmb1qDy+b4oLlMCPnDWNPWhljnIKKRINR0AoHgMJmUhLMYg628GYbZxyNSPR3qH+9HsbAZvkt+gz6zD4vRB+e+PYh+5FRsCXixsiyBhfOmQaLNhgMD+OSCiXDbizrINBSNeoAUCLmiouKdhIg5syV8U+r235bUcM3jm3DV3BY23oC5VgW9wTTuDhex\\/vVhDBHd5pgY+XwewokyjCSyMKoxFAjGC0wttAJ1ucqH6ZUe7Nk3gHSO2Ckcg7X9TcxyqnDUVeCF1\\/bCNqkWualzUCRaMMYqUam1Irh+L6Z78rBdfBEuSO3AVUvm0Qu+HQDHzdD\\/+4TE6ZLv2eYHplTBP6Dgw44BXPQoPRPFK4lxbo8XqtUMibHDKJmg0ssUigkoTgMuY+NnT67GPW9uRWYghQhjQaKvD0rGjLSNLbIz1hud0LIB2OvmQ+rvpBPrhTJipasFlJoKYrg8zjt+EhbzmZ9a1kTbNv99QjKZTIdY+KCMDsLrxVjII2Zw4JnhIJ5oL2DkqbvQYTgBNqcfDnMlRkIhJKm\\/KtXUWl6GHN2usWIi5FgHWjrWoznfB5kouCnpR9XkZnwtfBRiLnofBjFDuA9zPXasp2eTqxtopAFynHCfeCvdMAOmWic8m17CirOPxxG1RlzT7KZavS0SkbOIFILMf9ck5pVMZu4vQWnxZ3OXEedkiGxNuOutXtzxi18D3dsxMz+ItMuJIxYfiVeeX41goB9h3uecdzqGZpyEH0wo0FbeQOO85Vi\\/oQuTytx40jITgTIP5i+pQeujq2EgEo5n6Y6ZymhDu\\/U5JxY6mIQlAc\\/plyD90tMo\\/u1+zH96C2ZZ2vDTI+YhQ2dUKmKtH9v5ZzL+YwcSIqK7RmL2f7YysluMkj6p5RuPvYzbPnMp5s9ajnh6NyZVNaC3r5dZIxMtGqSLUT91zZ2Y2\\/ocpvosGFEN8Jy1Eve\\/uh2qzYt+00T6fQXeJ76D5CsP0cNZUHb+VxAabCdy9hBYxpF3V6I47wQ0mBNI\\/e7biA134NyntuOGpbWYqKWRU9+epDY2jCcSosjBCHmAhFw8ioKZFBHQEXahSHWb+F9PI3L0Iga1CI74w00YHE4jPNQLs8OBrJgJfv4dkFx5DHa1IV+1ADATWCZVFDL09bEw4Q9QF9iL+KM3wOhk+jyLsWWwiEDdMTDYc8CsEzF7ZgPmO3J47JYbMacsgkjWi9se\\/TNOrVGQYq6TV7XxhAzIslxXsud3TBgYG03df3WOUB1qhv7aidp5LRg5\\/0lc2+KAUmaH2xVHPBrD+p443iBWKpqnUQXDmOSRUZVoQ2TXWwi3bqfDyEE+\\/lyEDRPg2PkwFpXTWykutLe1w+CxYfjoG1CsLId142MYevlx2AwpxEb6ccEnrsJgOItf\\/PLHqDcXIDO+GZS30TtTYxuJSJeyyIN10M3j8ZbutYidChSnyMhOPP50JCfMxyWXr8RixoxJU1pgYjb43Ja9uO4nf0Zq4hE4It+Bngd\\/TAPXkKKPF+sWikxPE7YyYNIRuHheFe79+R2YNWcRvvSl\\/8LN3\\/wm+nIayhIjkAsKomIiZzaEHIPF6R+\\/Fk0Eod\\/5znehqkxxHU7mI8ZSG7\\/A48cHzUcOKDfyuE0M6IthZeGWp0yuR3CIOYdCd2vMMQ1ugMfK6Or0MUUl7HaWQU3sxeBAFyqc9Tjt9NPw2muvMX+UMdg7CCtxmt\\/fg5\\/8\\/C4MDQ\\/jh9\\/\\/PqprqhH3E4j6GuGaugwtC45DZT6Ax+66BSctPxZ19XW44447dG0Zl6u\\/wOPkAxv8fl2m3yUhXxG9G0IPV15zDWZNnk04sh1v7dmK1DA5l0kgmUrrY+tZA4nTXLAaHUjlYqgl4h0KBBiGDdAUDT6LhOXnfBx9u\\/bg1FNP1XsVv\\/Wtb2EgnIZMNFwoJOEmCC0QAiXSGVTXN+GVF59DRWWlToi4noSswWiHIP4ZQkTf0vn01Q8LQsR8ECu5ItaTlMl2TJ4xFTkGwQVHLsWiuUdh95tP4+zzLtfnjgQDfn2Or6hcodPo6exFOFGAypD\\/6euuR2NjA666\\/ArUNTB7DHVjJKMQcWcxcfpMXP+5L+ITl1wEsyQ8lGF\\/cGZbfkqpfPa92vq+hAhpkAAPHzbS1dWlPPHEE7q6iFkQIt7IY5WYmIKazBb9etEHnM\\/lmW2ax1YZOpCgxwmHQ1ix4mTceOOX8elPf4bnrfo0kGnTJ+GKK67QF9AKexT9Vwaj8cDuoHnUjq3ju1Q\\/CCF6hZs2bfpSZ2fnD8RK3v7+fgwNDekzh4QdiUPEH9HvpM\\/tKs33KhTHPKEGhX68UMzp551OD758w5dxxplnYO7cWbrqqGNElNojOudIyO\\/49jLxWdTxLyFk27ZtusHx87fZ6JsEEaJi0X87QsgeCvEIhqg+o0FLQB0RfUXl1dW1mDRpElpaJvB1sg4txLPF88bNE9OfJ5hAoh7ifReVFg39ywkRohZ9tBMmTBDweUEsFvsVOb6QqbPeQeNyut9xfyxW6qQeHXYodWLrEGisN180XEiNz+7isz\\/Lc08LyYrI\\/W8nRKyMEjOExs3uES38OJHoBXxdIDRDnHyf3niVjW+lNMUKqvv5XXdpfEQQ9y8n5P9SOUzIoVYOE3KolcOEHGrlMCGHWvmPIeQ\\/pRwWyCFWDgvkECuHBXKIlcMCOcTKYYEcYuWwQA6xclggh1h5l0CS73Gh2P+hICX0zU0tWRsUgwVOm4SCWUF7MAmzwYhqtxUOLYOiJiOTFXN4jfqcy0JRTM4x6FP7SvWNbUHy7gZJUmWhUJhkNpvnOhyOGbx+Jk+LTbqqeOyfUFwafCr1Xo6nQ7wX6yTl0TkqpdNpHgF+9vO+Dta9k\\/VsNxgMO3lebF9SEPeJe0Sf9Pv1ZoqZFqIXVPS4Hmxepei7Fp35pW5gMbRysOsOJYG42dCjyZSz+X45j4kH1lfaU\\/G9yvg9Y0rdyuMFIrqMBWMP\\/K6kDKXJ3+PLmECCvG8dBfJXjI7u7f1PE4ixqBXOgFa8irp8AiTFLPanEYMLgqn710TzyPNfTp\\/zo8HCG10OsSFfUR\\/pH+WYgtK00kQihXwurW85LCsSVDFZSExwH71QF4gs9t0QtxbGMUPSOY\\/42PhB0ajsf35JeMJKBBPH84bnd\\/C4j2\\/\\/QIEM\\/d8SCJST1Lz81Vw+v1zfmkFYhtgpiYcixtyKo0uxxcBiaVMJUcQgkUEbY6kqIZdVoSoyVNkARVNh08QqAQU5WYFiMUAfVRLPEzRIFkGMvg+h4G8+m+PzBKuLKMil+R4S8kVNb4vFYhV7VEIZNxekZEmCJ6Uh7IMVMQRHoXXzuIN0\\/Aaj7vDQEUjbSFLsmrrSajV\\/NyvJdVVWA2wkOxoOwCKWXdAaNLosTTahIFSUDRVElTZNKpUCmVfUrzAiQ4aIjV1iORXDyTiKBTG+7kVPJIaU0YqhdFIfq6yurECTw4pjPCZYLRa2gzGAdacScVpQTt+ppkhrE8wXexBRRlDENHWXe9R6DO9mutB+Yb2lbRnGC0xYthipO2AtvfCdd\\/O4mUfkf0UgBiEQqzS3aM7fm1XVBQmK4M6XduFvg0XMaKzGXJ8ZteYiaoxZTKstQygYhj+UxLxJ9bDkUvqOJAcKRBuVijAo5DIZxBMxyLSEjES3RZck5wzIWk1ImKzY0taF5ro6+GQj4rkstlJgxXwRVlqeWMlBR49KrxsKrciqjM5E9FrNqKACiUngPn0TD1Gj8V20jgokToEY9N0d3i0QOwVieQ9OIUqBfIECufd\\/RCDDsTgcBdfHNZPpHpNFs9uFyrHRmmRGllrfNRLD\\/dti2Et\\/3x0MYMmRM2AYCmBCfBhXHDETZlVDIk90ZMjA5RxtpCSGfVWTPmcUUh6yloVKwoMpGXmTHVIxAwelpBToviR+h4y+l5qBDJOLCtVTQozt8I1tILeD2v1GMAbV4MTISBohJYXJriqY2dTBeAyBTBBNZR7MrS5Hk8WIemGtwoqRQ05sl6EZkA9nKIw4rZpCoHUXdVujhak5uO0WyFYbSlPAJP27sZli\\/EsnE7pQWX5F\\/n6GAlH\\/5QKxUAPzSuJsI8z\\/HUxqVmMmT6krSBoKDMSqHoTlJP18TkbKDGyNZ7BuRMOGiIIE21shJtFnE3CR2eVKEUc1VaHZbkM6nsK+SASGugoMD4ZQx7gyud5Dt0I3Q3fS3R3A1AoX2vwDeHRrB86cPwtHN9dBjaaJ3vJ6jMmwDaohB0uK7SjSiowO7CVj142ksGU4hiHJB1mNw0sk63PY4LbYIIUk9LWHIdVGsKDciZMqylGVzcAkZ2k0FIBJKIhJ92gmsYGSOrYfCi1bTCBL0QqNBA1GxrNcVoALIQxNbC8Nk92qT6Ap8Zbu+ee0lutLTuCfEsg9Y1u2lYpoRLXHWX3qzJaXIsno9FgqhmZfHbVTxu7eYWQZHHd29SIeDePMY4+CJZ+BOxGiq2CDairwfE8Yj3eGsYeuYcRmhSxmvaVSqKQ5ZwMBtEyYgBDjalc8jiAllw3lcNRUH4Y7O2BIJLGoxoqrj56ElnI3uujOtGQKTrF0MJmGQ6zZ4nNMjE2WvAj2GQoyByOBgUSmR4i+hggOMokiVvO5fxtJIEp3YzIbkEj2ob7BBaffhFx0GEtbyrGASlOVKcDrtmAgHEIfraulygenmkALFaZIixwmenzk+Y2YNrEKXpcZm9avx5kfOoWxS6MFqzqI8NhcMBvfZREizpzD48l\\/SiAHlrhWXLmqc\\/B+IwPssTOaENJScKsyPEYaOK1kJFtEbziCCe5qJEdCSLuNMLoccKcVVJFAoToxWsRz+8J4IiYzSFtQR42e7SHRkSD8UcLfSjeTPAO2dvahqdqBZG8\\/GaaiYKtBXVUZphopsf5WVNgLaOJnr0kmg8rgNoklcnQvqSLMtLbXBwbxel8\\/LL5qBIYSCFFz8wIgBNO0DA0TKVSnCPRUssb5s\\/G3rfsQNHp4vQVWUxoVagZT+czZNR7s7e5HBYHCwvpaojuVzM4xtikYzqjojeUw1eaFQhSomTRCboIsIkCxzkChJ7G47ZDNllFXVhBTk3NiF0Uqj4MAKPzovU8\\/fYFRkccgIXDrxRe9t0CCI0H9VQQzh81+m9lmu1HkBwIcvrF1px6cZk2agFYyv729CycvWgQllYREM8\\/TTaRSefgTA2SQS1+iM3NCFaoLosFmvDySxS3b++Esc+CjlXbUeaxQ4kXsCaVQS+TSpaWxrm+ACWEFEsxVhvI5dPf5oSVUVJjMWDytEdlcnswKw+O002UwgOcLtLoUUsjCbSygweeA3WFh7HGhO6ohQBe6L92PaJwxIm9iDDNiboOPSqXRAtOYUV5AgLRINgcKFjvvMyFO95Tn38eqnGhxmxCgkH02I8oo9LadnWiikFzVInRmYCwo+iFimSRWLjPOmTw2WoiA+EXyxIiXO+PoHAjCQ4Wa00jX6HBsJ7xfpkhitjngsUj\\/kIV8K5fMfD2Tyeoz6QsiuNGhCgiZp2k+09HHSgK4ePERmOmy4qGtW9Enp3HJ3MXw0Dj3MobsJDLc0t+DRRkTTmuZhLXhGB4f5vk++vP6MtjSMSypcfOVscBiQp1bQjWZuaojq99vc2ThdJsRSFkxTMSVo+tIKy4EMwaYFCO89Pc1cgJ1jDEBoX29tLi4CrenHBYqp43Xy8UsuotWhDPMghwuhKNET7RyRU1SIMOYEtmFiRMmQrH7kDc6qUwaYnBAs7jRkI8iMRJAdYVTr6PCrsAsFpxR6c6f04ByxtCiQIUEYmIZoSQXqbhRAjwDDOkUDCYJD28O4vm+PE5ZMQcLGaGbzXnYrB6RjL5FA1mqm4\\/h7wvkTOHrwkyusuksAyV0aYulpqqwQlZslLzojebxzOA+9JGZFrqoFm8ltg7twxFVJnykqRybdo9gPYUnub1wRpMMbglCTyeiBQfuHB4hKrKh1qJiJn3+arFdKAPmpXNaMBIYQaDIGCUzPvljaHEZUJHPIsvgr5LBRcaQdCwKhTlKgcJMENml+sOIBPuRjIrFNREkC1l9WZRUoOPIWKnFCZgEAKFL8U2cixXXfgV\\/fasfqUA7Cv3tkLIhVFaLLeQrkSCMzgoms61nHLsc5yyehQLjiUoXlKTmR8jDfqK1fsb\\/LQMptA1G6NZAsFGBORUWxtAcaqqrsLt\\/iGgxiyUzGlBF+O\\/Mp3TltjoJWmSxRBS\\/Jf+vNEnK+wqknsczPGYlkxFkSYBktjHJ0iCLSaBENlapgPXpOLIGM5oIJ7d3RdDJZ\\/rptkyMKx5C1X5\\/kAwoIhGNwGm3w+kpw5ubu9BDDZ4ztQUtFT78becW1DNYLq5m\\/MkkUF5hQBVh77auMBomtCBO7Uz07sKK2Y2YWtOMZCiBQX8APf0D6N7XjwH\\/EFa9\\/DJGmOOINZmFQgoymW5jvqHRvxs1g75lXURhvGObHYMBLDz6TNi\\/fgdWb9wMw4M\\/RbK7FW6fnZk7XRoFXV5eQ4BSwGB\\/iGDcieK8hSj\\/0NG45mPH4hLmUM4UcxSFbi0rciMDYmRfG112W4J5E8OFTNcYZNLqs2r48NQaTDcRJgsQQpvIErWJuOFxOfXF4GNdNceQ12vfTyBf5PFD8WaYIuz1x9FDv24kmrK7GRQZ6Jz0\\/Y4I8FDHW6iu9+KS6mYkYgXc1dXJDMEOr\\/DtghfMrHf4h9EVyWE4p8Dk86IYyMLlIwKzJ1ApfkGEgXgfEZIDtVhaw2ernXBEQzhl9jQsamlCinlPNhCHnxa1k9Y3yOvDBBF9bNMLL76AvKoiT2R3zjlnY8fmduzZ3gMtnSNIiMFgVwk\\/GStyRFJaBL4TPo65n7oTr29cB3fvVlQks4TEacT39eLcY5Yh2t+N3R27YKzxon1kENHOffApzG4IRFZ+9lrULZmO5slTUYhkMbnZhALdn5N\\/UYE5yPAuxqShKF0trbeBvD9q9kR46KZMmnD7ElGgUU8ufWOrJcfKr3lc\\/Z4CCYfDD\\/HlAnHeaaWW0WwJJZCiWb3ZH8Pze4bRnS6iJzaCqZqMFXU1sNSakXQYEAnF4MuSeJsb\\/kIRG9s6Ya6pYnxg0khnS5eLXZEiWkMFGFIMqLINLRYNUxrMeO5PD+OcSc34wsWniTSAbqsfajiKrp5B7AgH0RuMIBMIktF5hEMhrFu3Vt8yMxTwo2H6ZFTNn4HXnn8J\\/t3tYg2A3rFocBoZ88SE+omYe+xxqD73ajz52lbIr62GtXUN4FYQl6ow6ezzoM2cDZvHg4VeBVvv+Cla\\/3gvvIurcdpHL0Qm64XfVIdu0uu0F\\/GFc45HLJ3G1PpKmJIaUoTKQ0R04UQKBVrmqS1VmFAmNr3QRjN9kTyLxJHJskh44xRYachAluU3fT7fovcTiHBXHxJdcDITHBejlawW9JRGE7CPKCfAuBJN2fFoJIo\\/bduB4+VKLJ9eC0+LDcN7e3DmhFq6NVoXkdIuNrgjEEE\\/Y0jeyLjCQFo0FuENAX3M4EkFEcA+1BJ9xKu86B5IMllUYS6rQVpI0CygrcIgXNQ7FYvMYUSvrkLoazSKvqaCjm4MRGsVjiKcpgyTthhzBitCMeEiKJBiAMGwCWpgD6Rdz8HcLVZDNMPUWMRA1fnwRzJEp2nIknguk2AiQ\\/eUCdByZchaZULjDDXcBovq0NFZJB3E6TV0zXRbYbMJLZPr0ExLlOJBOCmcs2cRhTkkMcCyv2NV0RRdLmKn0XgiPrbppu6yOrxe7+T3E8gv+PIpcX7050tsBx1TEH1D\\/QULHu+I4sWte9BQ60EZ845MugAjffIZVTbUED0ME0oOhYt4tMOPRzoHkB12oZiggJlhm0T3h9VKoEBtNoofP2LQZpKWZ0BXeMjMemVahEkVAZUIRvQ\\/kfCsnNc3Km9pqEQ1\\/XGKCtOeFz\\/8xgCfZjv1Ja85KKEAFLpMhMjwWBgFPkdhm0xkooFxUSF90UyEDoDw21dBl1pFj2AjChPr4\\/IwKEnkOuLMxJmdSxG4WG+uax+yJhvsdW54mGuoDOQtNeX41MQWFOJdWDihHNOZhymKjIOV0hqGcTxdRYGc9H4C+QhfHuV5g1g7M34lx9tFPCiMTESlKypDq5rFuliAuD8Di+aCZqjA8Z4C3tjwBG7dQFPwHkcmqbAmQ0RodGlej54oyQ4HJFqhJhbPCgIKeb1jUfhXA63BYTXrmyqILW\\/yTBSdzGdm1tox30Wr6N+H\\/l2tyBE6F+hO9\\/UMYWTreqT8PTB4y+BY\\/CHsk1sYdBkD3FYKsYCiWPPKV+ETBXK0ig7Jjh1IFIyQKydBH+\\/KpmAuhGkNUSRdNhiYPzkKNgrEgriVzpA5FAjNjXTNcnAPPjSPtB49C5t39eG4MiMuaDHRNfM7swM4CIIVvBQjpaVFkRTKV+mybntPgfCziRf9nm\\/PF4ulSt3I7xgihehspKa5nWS+SIxUvYOxL6XisT0DeG0ohdzWN9HZ3snY0gi0tmNqcxaVTXWoN1jhJvqqq2\\/E5s1voa97H31qcqxX1UzNciFFhsVcXqhVjQhYyzFi9EILDsLX8xrmEJV50mHmBLRGkQlX1sFZ7oU71oeR7kEcu+I0bIcJD+2NoluM+jprIVtysIhNjWi9SoxIjKChGBtClhaUc\\/loFcysmYlrGbabyFKL08fn+HSJ1sy8QR8EU5mnzJ0LeVI1vAQUySdvhzNmQPPK65AmYrz6pInYs3cXrlk6BxPp1tOZzEHHU0SXvlDy0Y2VMES+zuZ1Q++HskQR49fbKJDKzHs8OEettDtpumKPXj1wMXmkXIRL6BhJ4olfP4IHt6xHdvJ8mFY9BalvLfI1s2EomHDckqlomDgF9\\/zmNzoet1iZWdPKCpI6is5CObotJ+xnfxF9vqmYMPQivrp8ArzOBtzynW+haeosRFQJSYKCtn0jmLtwIYzMP1xT52JL3oM9xQoy0gPJoKLOnsMc4wBef+A+WIkKi7EI8t4myItPQ8Rai2In45hEyIyQ6CgjoqKbzJoZA13wqGHEmcFHXRVwLl2CmhoXQqv+hPjaZ2AZ3MYU0ILJn7wV8875BFpo9POZUx1V5YDXJJYUZt5zgKu0tzF5fxGPh\\/7R33OeT4G8QYGYDvrgokLEQZMWgRcF\\/We4NdmAJDH4mb\\/bhE6bEeF1f2XjKlHpMGPfXd9mgpeCtbaesnNhgBl8NZOncHhEXwwnsn9Dke4skkb5gsVQT7sGO1s3Q11zPypSDLaGWqTMMjJMMovMaXzzlsA2ZS6GKGCjo0Lvr1LT1OyCGCagNRDtmLPM1Hu3wb7uPsQCHbAZR\\/OBMC0FYnOPo09DP8FBkUIwpegFspK+3X2R8NzY0IB8ZTVsZdVYOH0icv3D2PzH+1Gf7cMES5pxw4FHnn8dE069GOd\\/7vOY4ZVwZKUVlYxxyWQG2XwR7yGPkkC+wbffPvC79+1cTKfT05PJ5BuUoOvA72Q23GYx6z2nIqQUaCViWeSdv\\/41Hnx2L5KLPoq2jAl2+t0kNcfLyyo9UXizMdQy7\\/D7e3U3ZbDYAMLkvMEJxZhG1FmNjsQ8uFJB2A0RBIw2uib6\\/\\/QIvSKtEnn4aEluNQerqJPZeiSVRTuZqhJ6il\\/9UJiwuRjULd0bEN74DNzZoJ61m+0WZJmFN02aChPrbd3ZBfecq9Evutcn10OZ3MjwItGtmphHGOEKbEPHWxvQsXkjY0aQLi3I7zM45vglsMpl6ItouOS6z+Kk5Qvh0XIoM42OmSSYqWez+YMKRAxbOxyOz1PJ7zwYzw8WQ97+cvSJYl\\/xF3h+2TtcFqWQTKdQzCRhkEYHaCxWN\\/7ry1\\/DX375fZiWfBL9Kz5P5u3Bt2fZUVnp1MdMWqjdRqIlMf5tJsLa2TuM3YE0brvrXgwZ6iB7qmCW40g4y2FzWdASfAtY9zJyYm+S3Ah9vEA9rJcWJXoRxDCQySAW7pbBNvsYDLtnICV79BX8VXufQ2W+Gxv39uHjn7gU29\\/YDv\\/eflx08YX405MPoWrqJOyY+wm9\\/dKe7ci0boGVrk\\/KhMg4arhxdM+tXJ4wmgloKlmA29OIT37qBkQSfdjWtgufuPIafPjkYwkQEvqa3TRVxudm5m+Q3gWGxG\\/eFQqFE1VVXT\\/+vABP\\/5CFHFDEqs4\\/QO9QHxXc+LFjEaQETN60aRNu+urX0MFgLTl8FKcLc+fNRTMDX53ThKr6CfCUV8HrMMHFGBRNZ9A9ksKXv\\/19ujIPDJ5mxInCyp2EwW3rkG7fSGRWECAWpsJoQBQERIlUxOicGEoNj1BYzJoTigVKZQPcZRXIJ8KYWGbDmSctx8Zt23DGmWdi165dmDVrFrbys\\/htkocfeUTvAMyJPR9pJXnGpbRK8qw+ePmchqkzmPlvQn6kF+nIIGiCcHt9aGhhXGuowvy5s3HJykvg85VhdAcJbfza+gP5J2asXP73mPxBZi4KiHajuE\\/MTxJBXwQlMWlNMKuzsxMnn7ECScGwDBvIREkkQ6rC\\/EERK8DZUNFlTddjYCImiV5kkT+Jpe1F8RuRQMxMoGA2wkP0oxAv9DPOGClsLxl15LIjddg4bdo0DA4O4tlnn4XNQbQnW5AkEmMUgq\\/Mi3g6h0A4jsmz5mMWk7y5c+boS4qF4oj7kqkktm\\/dhmQoDFWjNoshWeYXsNLZ0WWJbTkkukOXkRYo5REd6EZe\\/LAHnUFNTQ2qqyoxj6jrpptu0ifljd\\/j4wCBvMbjozwC\\/whzP\\/BUUmLoGxKJxK0UhKG0CY+YAGAkZO3uG8K1n\\/08k8QUTDYLFi5YiJrKKvT09aGQSqCmogIKtTzATF38ILrIttNkUJb5gCrmdCV7idjyTAnMyGUMCAz0UIOzYrrI\\/pkgok7BCDH7Q8wcCidiiJK5Wn5UyIuPXIpqwuznn3kWy486Tt+68owzTsdHzvqIriBnnX22vg9Cku0RPcB51qtvRZNLoUBwIKna6OJ00mXk8xtamnH0scvxkY+eg6VHLoFTjH7SQoWHKLWnJBDhKSj4J9jWK+g9gv8MXz+wQIRFiHsZpBbw9W4eR4yHb6LsaW\\/Hzh3bse711\\/GnR\\/+IaDgCBzPrHHG6vhBeMegIS1HETkUmggQH\\/XSW8SGDsnIfHE43rzHyXBzDw0NQ8wWxLaHuXiwUhPg5d9FrKgaqRM0xsS1nhhk7M2Ev3d7MmdOxdetWXH31NfjeD76H2po6fJLvX1j1Avbu3YtEPIFkYkTX5hyzfafLjUaiq\\/nz52P+vHmYPW8+Jk+eQlTkQJzXalS4ApPX0WlAtoOxRTD\\/Rirqb4SA3utnN\\/7dAtEtY+xHTk7ZvXv3tzs6Oha3tbUhEAjoGiSmbYp4I1yc2FFAXC\\/Ol3bkUPUe27weFPXuHzF7UJH1HTdGN7oQ2boyLkiOxqySAujTbQqj4\\/+iPeKzQFNiF80FCxbg6KOPpoB8WLXqRWzcuEl3KXPpbsT+EkcuW4ympiZ97wnhisT9pami4n1J88WzRX0HmZc1gtHfuL6Lh76VlJjTdUgIRAignVYxlkw2kMnXRiKRy\\/i5QjRSbNUrBCPei2tK00kFA0SvwGhMGmVG6Sh1MYj6xFH6sZh3TmKTdKGJMt59CMZVVlagoaFRjzmzZ8\\/G9OnT9W0lKirKcaBFl+4X9Yh2je+hKNXJ7zQ+90m6pR\\/x9JqD8eaQE4hgrCBA+FLz6OaCYm\\/yCjbwLBIlstNlZLJ57De\\/xpg96g7ECFvJpeXzOR29lKablpgippCazRbd1Qi3JfbAsVodpe1edGaIZ5a2Myv9crK4tyQs8Tp+A7qSMPZvCJxMlr5vZRsf4zMeZDt2izrH7ft80HLIC0Q0TvzupZjBV9rIRNxDtCSmDc7G6NZLJ2J00y4xYqkz2W63vWf9\\/+LZ72JdxDZeu45Ce5n3vUG3Fi1dK1CdqKtE73+yQN7z+eOWI5TzqMVo\\/5rYfbiah3CBXjJJ9CDYWLeF9Ql1FqaU4+c06xG\\/cRXh+5GxzrseHuKXXgfJ5AEKpHBgnQdbH\\/J\\/RiCHy7+nHBbIIVYOC+QQK4cFcoiVwwI5xMphgRxi5bBADrFyWCCHWDkskEOs\\/D8+dm05nIZzDAAAAABJRU5ErkJggg==";
            Settings.datatest=finalObject.getString("ITEMPIC");
            Log.e("bitmap in side ",""+data1);

            JSONObject finalObject1 = parentArray.getJSONObject(1);
            data2=finalObject1.getString("ITEMPIC");


            JSONObject finalObject2 = parentArray.getJSONObject(2);
            data3=finalObject2.getString("ITEMPIC");


//                    }

//
//                    mDHandler.deleteAllVoidReasons();
//                    for (int i = 0; i < itemModifer.size(); i++) {
//                        mDHandler.addVoidReason(itemModifer.get(i));
//                    }

//                    progressDialog.dismiss();
            okGo="T";
//            progressDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    } else {
        Log.e("tag voidReson", "****Failed to export data");
//        okGo="T";
//        data1= "iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAAEH5aXCAAAACXBIWXMAAAsTAAALEwEAmpwYAAA8GUlEQVR42u29B5wcZf0\\/\\/p6Z7X33er9ceu8khBogIE1Q6UKkYwFsXxEVBQuioogFEQuIyJeiKE2pgUASIIWQnrtcyd3lyu7dbe+7szu\\/9zN3G44QUPnr95uv\\/zz3mtvd2Zl5nk9\\/f562kqZp+GeL9IFvSvKNEQWYICEcjsLtdjuTyeTvnE7n+nA4\\/AOv16tfHAwGUVZW9s6bEkPDmq+yGv6oiqRTQVUoBEd5GUJ89fl863nT0v03FXhTV6qg3fbKXsyoL8fZMz2ojCfwB38fNqhluHOyF5u2bcFJixbprdNvCgZj2rpIDs8M+WHx1sI\\/GED76nXYWzYDWPMYrrjlG0jwAbVD+\\/CVC04ZvQlaSjv7j20o91iwPu3EMZufxN+e+AtSxSLME4\\/AnGPn4pwzz8YlkxTEQvE9YzUFNVOZD3O+9QdUPvUzKOd9B7nNa7H39JVIrG+D1vYiymctxY6bVsDj8Uj7WS5uPOWmu\\/Grz1+KC848F729O\\/Ghk47BWn8K6V1vYsPG9Zg5Y\\/rbNJXKddddp913332wWmywOxz46lduwi9++TO8\\/NIqeL0+\\/YZ3Cff111\\/HtGnTeIF3kPKpZlMQiUTOpNyelmX5X6AR\\/\\/ab\\/vkbErwhEhtEndMLDWZEo1FQQT+bSCSWu1yuq\\/h5WDDiXTfUusrujYQzl3ndVvRSa+vtRgSjQ6jwVpaUdJTFeaHVmZgmxw04oSsK604\\/fvzRqZglJXHKXjueaCpAdtiRSYR5c9moIAOxqPZqtICBnAlHOIz4QZeKKeoAntjRjTnZJC698kwcbclBZnMlCuuvuzTLaclsHr+NKWh88B489\\/p2bN\\/4V5g\\/dCUmnX4ZevcN4IstCr5+3rJRLrGN2oDTjc8ftxzdfW0448KrkZ91MX736kaoHZth\\/thH8MRlizA3l9nPVu2RRx7BvMUnYtatL8LzxOeQmXocZhYH4W9vQ1wyo71tKwn3vq2MZKNWV1+vK2Iul4Mim3DPPfdg1uypus3zeKcirl69GkuXLkU6nT6GivgqaRMKGaIilolrSk7jf0A1PojG\\/rPlf66S5NgHobsVrkqYtDy\\/MUHTpB+RuV8QYvCMMVcwXRSh+JIk6Z\\/FKz\\/\\/iafPLT245LkPWkmtw619+fUIH2rEZ6pMcJZZgKIZI3TMJq0Al8uNx\\/cNoTOSwcyqWpxQbYCCIpKREJ8gwy4q52s0GJTeUUkkFIbD4+7xJ9RGgzGHN9tDOGZqLc7YGMOlPiNeoCE\\/0yFjutqOC+fVoj2iwWUsotVfRF2DGUbZhq9PscLDh4\\/EIrDmJNjdJqyPZVFttu9X3pNf2tn73Kx6L\\/4S7MfOYTMM5NGaPSEMx1XcvfIIHK9JePKFVbjsmuuQzuZgN+dx3jd+iz\\/d\\/mm0NMzA1rc2on9wDyJZM\\/4ypOKaujyrLMDn8VyvV0K+fpUKfauobU17FLu9Try05i08uo0XNtbAHJeQoZVXIoEVC2rx8iudCHXuQ8rLtqeGYa9fBvTugGa1Yv7iKjx5SiM8Djd0LkUiv39HIBEu3Ov2YP32t3D6Vgem3nUpIuEElKUr0OOshXnmqRhOy1D8AXhe+B2qVUaopmtQbPbgmmsuwCP0HCdffQPu\\/+xZCMdUXSEYGqR3qfDw8LBmNplw\\/bU34JmXnkE8OAhNsaDCXYe0GkcyHofNrmDBh87CgqZm\\/OyXv0NGy8GlxhBJZhEODqNQKAjNquHj\\/O9pJ8JXWCyWCXRlXX19fWinF+rt7dW\\/q66uxpw583DWWWfBbDbDwSioa2YkIl6Eb1n7v2eMhyv5\\/2klwkGaixoS0RxG5CJqTTJjoxFFnovFYjAajbDZbEcy\\/F2squpEq9XaqyjKw4QhLwmDK4U\\/AVNofCWk8Q9XoolKTDROu90ugjPy+bzAOzAYDPvd\\/lglNlaS\\/ocqCSoaJrosWnAkDKNJoWt3IZfOY7ggoz0ewzHVXuTo9lPFBFzuKhhJCb0EhEsac+3S+1YSimZOd3ssT39xcxqfaMxhqpUu3eqFmksjkUxin5rFaykFZo8NjpSKpdVO1EsKQvRvRUORgcvJoKAgEQrd6vX5bnpHJap4V9SO0IrS+tU9PTi2woPZr0VwfLkVMSmHhbIZl892QQ6mkLACFlnB5zYEUV7bgLMbZSy1yTCreQQ0BXI6hiqXDhe\\/wuN7+ysJB0OIGGStku45G0lgx0gC05p82Dqk4cmIir5oHB3twzhtmhUdqgv1xizaUmYsccrIVjkQHRjC3UvrEQxloJnS8Dl9jCU57EsqUgOhZ0mFOyLB+MQ3RiLoZWuqh+OomzsRF77Sg3leGQt4U8jmwIsEh4uqHdg1MIg2KkFFVoO25WWcc82XUVetYO9IH+aUVeKkCivaVDuOLVNEwrDf1WvdyQLM4Rj8OQvekrN4eGcc1bVudPsZ9\\/2t+OYJy\\/C73z+CJ196Fp2btkM2x6Bc\\/APM2vwSujPbcM03HsJ5RzRhVajIHGsI1832wsEKUUpkqIqam6z66qYo6h0qapUcVplr8djGTizfvgYnLJ+Je\\/\\/4OHrfeBNZoxmenB\\/TPns\\/1n7uI4h+8k7Uv3Efcjfcji9OLofdlMdnWpwUMe0mEhXqLe2HwLrKaVFc8kI7Pj59Jk79ww6YiExyNiMMKQlFaxK\\/OW4mvvfyBuzpC0IeyQGVNcxFy1GTHEFH2wC0lsn4zjmN+MoUp65VJfi\\/n13ihKwa4bEU8cMntuLZuy5H1dQl6Njejr2hKBauvBnp9qfRbPPhwZlXwWGJQts5hFjfHhiHqcJUa\\/WlW3Hza35c36QKJpVs5e3wK1hmYO5s48WG3w7inOGNeOWH16KsuhbJK38PKTWAIasPmRHaTnQYyr3nI5Ij0Fi2Etrpn0bdhoex3NGNr97zO1Sk46IOwSo9BI93kEF6EV+M\\/mfmotNx5x\\/uw7KWKiy56LOILfgY3P\\/9JaRSIUjMUbKzV+DGs4\\/BurVr8OqGjbCli1Dzw1iy4kw8eP9vx1v7u5PREts+d9PX0frWDuzZtQUZtipvlGHRnKipq4E\\/ncbRC6bjmCNPpNsx4svf+j4MSgZqOIRLL78cP7r99v1sOqirp4elUyzesWrVqs\\/f8KUvoae3h47QSMBgIxVJAS7ojV3o7+\\/Dgvnz4Keqnnb6CvzoRz8SCUrp4av5fjk9Nd6zElFefPFF4Wn3dnZ2Nvf19aOjo013+QKZ1NU14IQTTsCyZcv0SktaRJefZCU6dBEV\\/kOVTJ48Gc3NzRhz9b+lq7\\/8IK5eIPnzKGBNeOKKigr83Ur+rZHxcCWHVCX\\/E+UwIYda+c8jJDnupECSgYQf9oIPNq8BvYSuk1wSUhkJNqsRo7dIIiQI5+nih8\\/zuIpOtE7g5VIpOdNSEU6VIF3PiEUZw9Hi3x947naPx7NvfONK4L1UhKcX7S31cnxwQtKSYrWZXyWaWyaaksvm9MaKUjDIsBEyOthYTdL4YAXpZBr5bASqLIgrkBArA6ggxKjfE2dDcwV1f73jG8gMvY\\/PXkxC\\/P8SQhxeo+\\/Pr2wNbmZ0\\/\\/jCJtQSARtyORSKCgyypCcVomRRRC6nYiCZQFwtoiuUQ4xEVTisqMwynjK7MVGCDpMZBUrJWiigwk1kZni7C3ssturvhbQPaPAyfv\\/6P01IKDl88VDHyAMz5zSgwGfLshO3bO3Han8W0\\/MJXL24ERPdNniYjuVlAdXyKKQTeGVXABOaqniPEY1Ok54DqvzOmDHg3tZ+9HhqicgDqPMkEZG8qAgN4OZFsyFrOURjKR3XSsyktKLoHiWTRPOKko55glHRmYb7SMjl70nIb5gOiJImJ1bMmaWZLQlMMFjQG1aRZgpXac2So8yuXE5c\\/ddd2OusQEujF90dfcjYajAcj6BGymMu4e0VU6yYUl2B1u5ONLLh7hRTQEcCbmsZXuhPQrEB+0ZSeDOSIQcJNOwBXFlTDTUTx8T6GkyyF9AWLCBOIOLUiqhx2WAsZOEqr9jf8Fy+0NTav69X2Oqc5qa3CUmlUkL\\/Gq0GS0\\/aKGFLexdy1dVoJvZOEJXWlfsQD8bRU2bCsQYnFj61ASfPnYYzKpndxfJYG\\/OjVaqiSoTgD9IpyBoGEjZUmzJosmnoURzoymYRSQ4x66hAIMJc15DBV5llGJihbO8aQMFmx2S7B8ZiENWFHC6ZVyMGDSgQBXFLGm6TBesHsvjJ2gH8\\/sIZyEfSV5gUw70ml\\/Ed7lcoa8EfGoG1aNBVN800Z\\/VwCo93DsPK1KhFC6OxuRGVkgW7t+\\/B6kpy25hHndmJ6VYVyVwB91F9ri5TcVxzNXa27sbTf12H11Y9jPbhJGxGE5zpFFKRNGKpMGY9sAE7rr8WSnEffI21MNQfAUO5Get\\/8T2Emcyn6TC2U0V\\/tWkvjmosh3EgguWzyjGfyUpeNtGjeUWjma9Kif2E5HK5n\\/D99WnqY2csh589twmLj1qA0xucCEeDCMg+PNfahxGC4H2qjI370rAUhnH3kibc+toQjjf24rtnnYjNgQHsfGkndrTuwquvvsq0qw1XXnc5fnv7Q0hHh2D20R1nU6j86SZo934NUVcjzj73eOx64SkMrt+NU89agb+t2YDrH3gAsxwF1GZlpL1u\\/PmNHbjhqBlwMG2AVIAmG2GkB2R5lm7\\/1PHZ1R\\/5\\/pzxXRRi3PGSNZ16qqzIMaxs8GJtPI9Qahg\\/2GDGyOAAOUcv5rCj2leFIVWhnicgZdLQxHtKiwgd1W7amLsMO4MRSIM0\\/Lgfzp3rMHN2I3YFDMzSYpAnTURuymy4VRNCxTBOigSxq\\/UFpI67EL+fPQGGsihOrfLub9pYEi283BaC\\/Pn7CSHCn8yXPaLvRbg+UQxSmrrnxeZ0CI+8FYQ6MoQ\\/rW+H2elFuZneiGLvDwRQ4CMKTgvMFbUUdB1UOglf9+s4yeDH1g2r0VzfhKNcafQsuwQ\\/2F7HazR6sxwq4sMId3QhY\\/FBM9PmEUI+xzQnbUNu6Wz49rXhzPnNTLYl\\/PTYGpjV4js8lQiujDenMDN5\\/kCI8iv66qtKvrwABeVep959LCm0m5mLseTID6P3Tz+Eb8ESVDrt6NnTCpPTBldgBJkjz0CA155jiCKrSEzEP4m7N3Qha6tCV8aEnL0F5lgnsj\\/\\/NIxD7VBmnoaMx84GVUHOSzBm+Yw5J2LRRz+KLbdejymB1YgsOA9fvO1mXD\\/ZicjYwEmpUBLdfJnwDvdbeiWkuJkX36ITIsmjqkKT+s73f4ZH4EXKOBeXLp4Noz0ElYGuYzCMZ3eNICn8P2j8rjymFgehDexGYMvrSA\\/6kZ1zFCLl8+Cy2VCx+lYcf\\/wpcFs8WPXKCwhMuRCGxcdAHdyBzF9\\/hZy\\/F3Zjkigii\\/MuvBQrzrsIpy2cBJUhwOV4G\\/6QuW+wzUeK9yJXfD\\/QqI31s+iqtmXLFlx4wYep+1Zkc0mql4cNc8BF2DGY0VBfdGHQbQD6t2GY0OWKC67U73t59csI9QSRVxiRq8oxoaEZJocRFrMZDY2NePqhe5EomlCsmoajPnIpPA4n\\/nDz1XCa81i5ciW++c1v6l2Noi3j7FcAsNhBA+LBisiw+ZAgr\\/EVGZh+8qOf4ks3fQkeTxmcRhvmLV2KQDSJ8y44B+1benD2GbMxGLbixz\\/4CqwWpw74xEBEjl6vrTOIydMnM1v\\/C1accoauIgKndW5bg4RmQjKTh89mxolnnYuv3Hgj5kxr0SN6qX3ienpWC7mfPVhb\\/y4hogtAvK5Zs8bR29vb1tHRUTswMAC\\/3693zMbjcf0Q3QEC6WYyGb1jpFgs6BLRyABV1SAzqCl8Vj6fJnNMWLRoIW762k0488On7VdtMUgo6hP3iaO8vFxA51nCCY05pHd0OXwgQtauXas3UnC5srISDQ0NwmvcTCK+xkuNCiOogQ7BarXtvz+dSUHNqzrsF88QAPMgMH688f6G7fnCyMhIXNRb6ovf\\/7wPSsj\\/pXKYkEOtHCbkUCuHCTnUymFCDrVymJBDrRwm5FArB+1pNGhxJEaK8LisaE1k0FTmgq2YQ1EzELmq+gzLsflf++8R6TBht4Dfk\\/jdQp6anUqlmvjq4zlRT1R0UvO6nUS\\/m4iGd4r7RU+keJaYMFKaQCWKQLoCKYvzevuSSX12p+gkL537VxNyLA8B5U8+EHYLmC6uE7D9gAxPzzdEbzvLm\\/zuNjbusf8NQhbzvgdJyOTxD\\/uAhIyXiEhQruDxl383IefJivwIm8EbJV0iejYni25KCWUuLzRZDCkU+WQDoqEgz+ahShaIvjSHw0ommN8mhIQym9Xf22w2lMZWxOdsNvsNEvLtfykhE8tcE5RiviMZjcmayhTWYGXDxWi4S88gBWGiM284nEbcYIKLHwagQDMVYJcltDBvJ31IkvCslObVJFsxwe306pNCRRFpskiZ9UZRgiITHBvKX8Hjxf\\/PhGwZ9N+7sS97mdnnxJF1DtRI2dHFFQV1vyqpGH3GUCIJP9PbZNIAv82KDM97SHidTYJTTHDQCjDIRhTYKNHh4nKK8RG921Nv5FhHuk6I6LQYl9a+SUIWfWBCzC7LoMNQrN6qWvC5R3bg+KMnY7KcxEIpg2qHHc4yG6Si6KoxkOMFROJJZGQVnoyEsF2GyWwlKUas2jdEvTJhMJqHXUmhyVELsyGPIz0miJEVSSogQx3LRoNUUyNk6qHFoEGxClUTIpORIfFqLieTEO2fIqSvP5drrrAaFaoHFQSrBqK4ryuO2qYqnEB7mVFmwgS7oldSlGXIuQj8SQXpTBb+gowpHgu8Ngu\\/k2DIFZBg437eGsNLqSLmWkLI2iUkYh5c1aDgSJeNhFAiidzosJ0kw2Gz0zXz+bRFIfBEKoG8mhcOgXSY8u9JSGkkT3xKq\\/GgyeD0kQnYnYygiRyozUSxJm\\/DLW0ZRF1FlBeKOJrnM0YZf93aidOmNqPJqOLK+TX4772DOLOqDMZ0Fgly1lBI026M+LN\\/ACnrRPwlUUQurWGxQ4FszuCqMiucck6fXNXbugfTp05G61ASsXgAdRXlukq67A6YjPslIb0nIWLESiI3WkPpB7972pKL8rkkEukcEqqNXiSDKmMRDrcbf9sbxq\\/6ZIS0GJrpzcJpM3YNpeGW4zCOxPDdM6ZgrrEAzaJgqG8QLY56pGxFBPNZjGg2DGVy6EjEEQpreJacmmZzY747hfMmNuKBp9\\/ADWcvpZso4Na\\/rMclx8+hbRlgzqdgoyobTOZSu3u39\\/Q0iaaL0ap3ECLmspstlqq8QfKnZQMSwRQclFPIrmJ6rhJP5ftRPQAsmF+Ba98YQNbow7lUi4f2ZdDqH8SxE5vwwM4EbDGauD2CRVYZixo9eKurG3GlDAumNsBSzGNj2AC7oYBkjx+TXRICXjt8Lh+8DiOmBNpx4cLZtDtVH1xNxVTkDWmYChLsVhtVjYRoCQxrdvSHox\\/zWhx\\/bnIY30mI8BjGohRSjQZvXyyGkUgUA3YnTtfc2OqNY37GiueoZseU83M6j9s68jitXMLEohFl9U78dG0\\/NMa7kZQRjUoM24YdMNJ2FtbShVoM2CRGeeUMg2QcUXMDcskMlk6w4SKXAdva+6iCwBETmxEZ9mNahRXmSByn1tugFOn9hJ3aTHAqRZz9651YeXQNTqorg1WWJaPzAEJYnMlUNpan6AtUsQKJf2rXEJqqK7AlFsf5zdV4fPs+FJx0i3T+uwYs2M0HX9BiR5Aq0mS0481EAv2UJOyV6BkKwr9xDY6d3oiy2lokcik8vLEHRacMn5GtjiTIvQzyW9Zg2uyZsPtaUH7kXFRThadXuRBksAwqBShJE86dXo5KBt0CbeX+TX5cvcgHp8XJWCSfQmN5\\/kBCfhlLx6\\/RchKkvApZyiPvqMDXd\\/XCmbIhbk\\/jZLcLg\\/RC84i1nu2JYbWWxUU1Puwkd4\\/zSHhJc2DvhlX48bLZSGQzWLtuMx774xPY1boeKZjhyKVhSDCORLpw0t0P46ltMaTv\\/R4srgzKGuciX9OI73\\/jCzi1oRadXgvkSAH39KexfXcbPjyhEQqB6Sfm2WHms20eNx2mYS+ddcuBhKRGVFg3bGvHwpkTYc\\/FcfOLL+Prp56Fn\\/cOY4LThhyD2pOburGjv4iZdRJFXga724h5VA+rJYOGrhGcsWAqtrS14o0Nm\\/HnJ5\\/C+jfewOQF87F3625Ichp0yMgrFtTe8gACd38XxlgPnOYKNM2vROerz6Hpwzfgl7d\\/AZ54GCP0dEHJiRf6gzT4As6dWYMJuWFkFSODtUsfj5fGOpLHD4ZqqVwGBZMLD6xtRaek4JwlE1EXGcYrMQI4WzW2RTNoo7Q6\\/EG0F6rwRV8aCVsWO7cF8ftzZyCvqdi1rQdvrt+C4MgInnj8cVhtZn08sX9nPwryEOwuO066\\/Rd4dl03Jg5sRxcx2ZUfX4Gn7vwl9u5tw9zayai84jJcfOLHsKCZsWUwj7cyGdSoGh2KG4ZiCiqDpiKAJqM\\/IdI7CSEA1ExWgjfFhrTBgttfacUrA2kcX1OJxhkOTGNQ2xTNYlveAXeFE6\\/0jmDnvjSmb1qDy+b4oLlMCPnDWNPWhljnIKKRINR0AoHgMJmUhLMYg628GYbZxyNSPR3qH+9HsbAZvkt+gz6zD4vRB+e+PYh+5FRsCXixsiyBhfOmQaLNhgMD+OSCiXDbizrINBSNeoAUCLmiouKdhIg5syV8U+r235bUcM3jm3DV3BY23oC5VgW9wTTuDhex\\/vVhDBHd5pgY+XwewokyjCSyMKoxFAjGC0wttAJ1ucqH6ZUe7Nk3gHSO2Ckcg7X9TcxyqnDUVeCF1\\/bCNqkWualzUCRaMMYqUam1Irh+L6Z78rBdfBEuSO3AVUvm0Qu+HQDHzdD\\/+4TE6ZLv2eYHplTBP6Dgw44BXPQoPRPFK4lxbo8XqtUMibHDKJmg0ssUigkoTgMuY+NnT67GPW9uRWYghQhjQaKvD0rGjLSNLbIz1hud0LIB2OvmQ+rvpBPrhTJipasFlJoKYrg8zjt+EhbzmZ9a1kTbNv99QjKZTIdY+KCMDsLrxVjII2Zw4JnhIJ5oL2DkqbvQYTgBNqcfDnMlRkIhJKm\\/KtXUWl6GHN2usWIi5FgHWjrWoznfB5kouCnpR9XkZnwtfBRiLnofBjFDuA9zPXasp2eTqxtopAFynHCfeCvdMAOmWic8m17CirOPxxG1RlzT7KZavS0SkbOIFILMf9ck5pVMZu4vQWnxZ3OXEedkiGxNuOutXtzxi18D3dsxMz+ItMuJIxYfiVeeX41goB9h3uecdzqGZpyEH0wo0FbeQOO85Vi\\/oQuTytx40jITgTIP5i+pQeujq2EgEo5n6Y6ZymhDu\\/U5JxY6mIQlAc\\/plyD90tMo\\/u1+zH96C2ZZ2vDTI+YhQ2dUKmKtH9v5ZzL+YwcSIqK7RmL2f7YysluMkj6p5RuPvYzbPnMp5s9ajnh6NyZVNaC3r5dZIxMtGqSLUT91zZ2Y2\\/ocpvosGFEN8Jy1Eve\\/uh2qzYt+00T6fQXeJ76D5CsP0cNZUHb+VxAabCdy9hBYxpF3V6I47wQ0mBNI\\/e7biA134NyntuOGpbWYqKWRU9+epDY2jCcSosjBCHmAhFw8ioKZFBHQEXahSHWb+F9PI3L0Iga1CI74w00YHE4jPNQLs8OBrJgJfv4dkFx5DHa1IV+1ADATWCZVFDL09bEw4Q9QF9iL+KM3wOhk+jyLsWWwiEDdMTDYc8CsEzF7ZgPmO3J47JYbMacsgkjWi9se\\/TNOrVGQYq6TV7XxhAzIslxXsud3TBgYG03df3WOUB1qhv7aidp5LRg5\\/0lc2+KAUmaH2xVHPBrD+p443iBWKpqnUQXDmOSRUZVoQ2TXWwi3bqfDyEE+\\/lyEDRPg2PkwFpXTWykutLe1w+CxYfjoG1CsLId142MYevlx2AwpxEb6ccEnrsJgOItf\\/PLHqDcXIDO+GZS30TtTYxuJSJeyyIN10M3j8ZbutYidChSnyMhOPP50JCfMxyWXr8RixoxJU1pgYjb43Ja9uO4nf0Zq4hE4It+Bngd\\/TAPXkKKPF+sWikxPE7YyYNIRuHheFe79+R2YNWcRvvSl\\/8LN3\\/wm+nIayhIjkAsKomIiZzaEHIPF6R+\\/Fk0Eod\\/5znehqkxxHU7mI8ZSG7\\/A48cHzUcOKDfyuE0M6IthZeGWp0yuR3CIOYdCd2vMMQ1ugMfK6Or0MUUl7HaWQU3sxeBAFyqc9Tjt9NPw2muvMX+UMdg7CCtxmt\\/fg5\\/8\\/C4MDQ\\/jh9\\/\\/PqprqhH3E4j6GuGaugwtC45DZT6Ax+66BSctPxZ19XW44447dG0Zl6u\\/wOPkAxv8fl2m3yUhXxG9G0IPV15zDWZNnk04sh1v7dmK1DA5l0kgmUrrY+tZA4nTXLAaHUjlYqgl4h0KBBiGDdAUDT6LhOXnfBx9u\\/bg1FNP1XsVv\\/Wtb2EgnIZMNFwoJOEmCC0QAiXSGVTXN+GVF59DRWWlToi4noSswWiHIP4ZQkTf0vn01Q8LQsR8ECu5ItaTlMl2TJ4xFTkGwQVHLsWiuUdh95tP4+zzLtfnjgQDfn2Or6hcodPo6exFOFGAypD\\/6euuR2NjA666\\/ArUNTB7DHVjJKMQcWcxcfpMXP+5L+ITl1wEsyQ8lGF\\/cGZbfkqpfPa92vq+hAhpkAAPHzbS1dWlPPHEE7q6iFkQIt7IY5WYmIKazBb9etEHnM\\/lmW2ax1YZOpCgxwmHQ1ix4mTceOOX8elPf4bnrfo0kGnTJ+GKK67QF9AKexT9Vwaj8cDuoHnUjq3ju1Q\\/CCF6hZs2bfpSZ2fnD8RK3v7+fgwNDekzh4QdiUPEH9HvpM\\/tKs33KhTHPKEGhX68UMzp551OD758w5dxxplnYO7cWbrqqGNElNojOudIyO\\/49jLxWdTxLyFk27ZtusHx87fZ6JsEEaJi0X87QsgeCvEIhqg+o0FLQB0RfUXl1dW1mDRpElpaJvB1sg4txLPF88bNE9OfJ5hAoh7ifReVFg39ywkRohZ9tBMmTBDweUEsFvsVOb6QqbPeQeNyut9xfyxW6qQeHXYodWLrEGisN180XEiNz+7isz\\/Lc08LyYrI\\/W8nRKyMEjOExs3uES38OJHoBXxdIDRDnHyf3niVjW+lNMUKqvv5XXdpfEQQ9y8n5P9SOUzIoVYOE3KolcOEHGrlMCGHWvmPIeQ\\/pRwWyCFWDgvkECuHBXKIlcMCOcTKYYEcYuWwQA6xclggh1h5l0CS73Gh2P+hICX0zU0tWRsUgwVOm4SCWUF7MAmzwYhqtxUOLYOiJiOTFXN4jfqcy0JRTM4x6FP7SvWNbUHy7gZJUmWhUJhkNpvnOhyOGbx+Jk+LTbqqeOyfUFwafCr1Xo6nQ7wX6yTl0TkqpdNpHgF+9vO+Dta9k\\/VsNxgMO3lebF9SEPeJe0Sf9Pv1ZoqZFqIXVPS4Hmxepei7Fp35pW5gMbRysOsOJYG42dCjyZSz+X45j4kH1lfaU\\/G9yvg9Y0rdyuMFIrqMBWMP\\/K6kDKXJ3+PLmECCvG8dBfJXjI7u7f1PE4ixqBXOgFa8irp8AiTFLPanEYMLgqn710TzyPNfTp\\/zo8HCG10OsSFfUR\\/pH+WYgtK00kQihXwurW85LCsSVDFZSExwH71QF4gs9t0QtxbGMUPSOY\\/42PhB0ajsf35JeMJKBBPH84bnd\\/C4j2\\/\\/QIEM\\/d8SCJST1Lz81Vw+v1zfmkFYhtgpiYcixtyKo0uxxcBiaVMJUcQgkUEbY6kqIZdVoSoyVNkARVNh08QqAQU5WYFiMUAfVRLPEzRIFkGMvg+h4G8+m+PzBKuLKMil+R4S8kVNb4vFYhV7VEIZNxekZEmCJ6Uh7IMVMQRHoXXzuIN0\\/Aaj7vDQEUjbSFLsmrrSajV\\/NyvJdVVWA2wkOxoOwCKWXdAaNLosTTahIFSUDRVElTZNKpUCmVfUrzAiQ4aIjV1iORXDyTiKBTG+7kVPJIaU0YqhdFIfq6yurECTw4pjPCZYLRa2gzGAdacScVpQTt+ppkhrE8wXexBRRlDENHWXe9R6DO9mutB+Yb2lbRnGC0xYthipO2AtvfCdd\\/O4mUfkf0UgBiEQqzS3aM7fm1XVBQmK4M6XduFvg0XMaKzGXJ8ZteYiaoxZTKstQygYhj+UxLxJ9bDkUvqOJAcKRBuVijAo5DIZxBMxyLSEjES3RZck5wzIWk1ImKzY0taF5ro6+GQj4rkstlJgxXwRVlqeWMlBR49KrxsKrciqjM5E9FrNqKACiUngPn0TD1Gj8V20jgokToEY9N0d3i0QOwVieQ9OIUqBfIECufd\\/RCDDsTgcBdfHNZPpHpNFs9uFyrHRmmRGllrfNRLD\\/dti2Et\\/3x0MYMmRM2AYCmBCfBhXHDETZlVDIk90ZMjA5RxtpCSGfVWTPmcUUh6yloVKwoMpGXmTHVIxAwelpBToviR+h4y+l5qBDJOLCtVTQozt8I1tILeD2v1GMAbV4MTISBohJYXJriqY2dTBeAyBTBBNZR7MrS5Hk8WIemGtwoqRQ05sl6EZkA9nKIw4rZpCoHUXdVujhak5uO0WyFYbSlPAJP27sZli\\/EsnE7pQWX5F\\/n6GAlH\\/5QKxUAPzSuJsI8z\\/HUxqVmMmT6krSBoKDMSqHoTlJP18TkbKDGyNZ7BuRMOGiIIE21shJtFnE3CR2eVKEUc1VaHZbkM6nsK+SASGugoMD4ZQx7gyud5Dt0I3Q3fS3R3A1AoX2vwDeHRrB86cPwtHN9dBjaaJ3vJ6jMmwDaohB0uK7SjSiowO7CVj142ksGU4hiHJB1mNw0sk63PY4LbYIIUk9LWHIdVGsKDciZMqylGVzcAkZ2k0FIBJKIhJ92gmsYGSOrYfCi1bTCBL0QqNBA1GxrNcVoALIQxNbC8Nk92qT6Ap8Zbu+ee0lutLTuCfEsg9Y1u2lYpoRLXHWX3qzJaXIsno9FgqhmZfHbVTxu7eYWQZHHd29SIeDePMY4+CJZ+BOxGiq2CDairwfE8Yj3eGsYeuYcRmhSxmvaVSqKQ5ZwMBtEyYgBDjalc8jiAllw3lcNRUH4Y7O2BIJLGoxoqrj56ElnI3uujOtGQKTrF0MJmGQ6zZ4nNMjE2WvAj2GQoyByOBgUSmR4i+hggOMokiVvO5fxtJIEp3YzIbkEj2ob7BBaffhFx0GEtbyrGASlOVKcDrtmAgHEIfraulygenmkALFaZIixwmenzk+Y2YNrEKXpcZm9avx5kfOoWxS6MFqzqI8NhcMBvfZREizpzD48l\\/SiAHlrhWXLmqc\\/B+IwPssTOaENJScKsyPEYaOK1kJFtEbziCCe5qJEdCSLuNMLoccKcVVJFAoToxWsRz+8J4IiYzSFtQR42e7SHRkSD8UcLfSjeTPAO2dvahqdqBZG8\\/GaaiYKtBXVUZphopsf5WVNgLaOJnr0kmg8rgNoklcnQvqSLMtLbXBwbxel8\\/LL5qBIYSCFFz8wIgBNO0DA0TKVSnCPRUssb5s\\/G3rfsQNHp4vQVWUxoVagZT+czZNR7s7e5HBYHCwvpaojuVzM4xtikYzqjojeUw1eaFQhSomTRCboIsIkCxzkChJ7G47ZDNllFXVhBTk3NiF0Uqj4MAKPzovU8\\/fYFRkccgIXDrxRe9t0CCI0H9VQQzh81+m9lmu1HkBwIcvrF1px6cZk2agFYyv729CycvWgQllYREM8\\/TTaRSefgTA2SQS1+iM3NCFaoLosFmvDySxS3b++Esc+CjlXbUeaxQ4kXsCaVQS+TSpaWxrm+ACWEFEsxVhvI5dPf5oSVUVJjMWDytEdlcnswKw+O002UwgOcLtLoUUsjCbSygweeA3WFh7HGhO6ohQBe6L92PaJwxIm9iDDNiboOPSqXRAtOYUV5AgLRINgcKFjvvMyFO95Tn38eqnGhxmxCgkH02I8oo9LadnWiikFzVInRmYCwo+iFimSRWLjPOmTw2WoiA+EXyxIiXO+PoHAjCQ4Wa00jX6HBsJ7xfpkhitjngsUj\\/kIV8K5fMfD2Tyeoz6QsiuNGhCgiZp2k+09HHSgK4ePERmOmy4qGtW9Enp3HJ3MXw0Dj3MobsJDLc0t+DRRkTTmuZhLXhGB4f5vk++vP6MtjSMSypcfOVscBiQp1bQjWZuaojq99vc2ThdJsRSFkxTMSVo+tIKy4EMwaYFCO89Pc1cgJ1jDEBoX29tLi4CrenHBYqp43Xy8UsuotWhDPMghwuhKNET7RyRU1SIMOYEtmFiRMmQrH7kDc6qUwaYnBAs7jRkI8iMRJAdYVTr6PCrsAsFpxR6c6f04ByxtCiQIUEYmIZoSQXqbhRAjwDDOkUDCYJD28O4vm+PE5ZMQcLGaGbzXnYrB6RjL5FA1mqm4\\/h7wvkTOHrwkyusuksAyV0aYulpqqwQlZslLzojebxzOA+9JGZFrqoFm8ltg7twxFVJnykqRybdo9gPYUnub1wRpMMbglCTyeiBQfuHB4hKrKh1qJiJn3+arFdKAPmpXNaMBIYQaDIGCUzPvljaHEZUJHPIsvgr5LBRcaQdCwKhTlKgcJMENml+sOIBPuRjIrFNREkC1l9WZRUoOPIWKnFCZgEAKFL8U2cixXXfgV\\/fasfqUA7Cv3tkLIhVFaLLeQrkSCMzgoms61nHLsc5yyehQLjiUoXlKTmR8jDfqK1fsb\\/LQMptA1G6NZAsFGBORUWxtAcaqqrsLt\\/iGgxiyUzGlBF+O\\/Mp3TltjoJWmSxRBS\\/Jf+vNEnK+wqknsczPGYlkxFkSYBktjHJ0iCLSaBENlapgPXpOLIGM5oIJ7d3RdDJZ\\/rptkyMKx5C1X5\\/kAwoIhGNwGm3w+kpw5ubu9BDDZ4ztQUtFT78becW1DNYLq5m\\/MkkUF5hQBVh77auMBomtCBO7Uz07sKK2Y2YWtOMZCiBQX8APf0D6N7XjwH\\/EFa9\\/DJGmOOINZmFQgoymW5jvqHRvxs1g75lXURhvGObHYMBLDz6TNi\\/fgdWb9wMw4M\\/RbK7FW6fnZk7XRoFXV5eQ4BSwGB\\/iGDcieK8hSj\\/0NG45mPH4hLmUM4UcxSFbi0rciMDYmRfG112W4J5E8OFTNcYZNLqs2r48NQaTDcRJgsQQpvIErWJuOFxOfXF4GNdNceQ12vfTyBf5PFD8WaYIuz1x9FDv24kmrK7GRQZ6Jz0\\/Y4I8FDHW6iu9+KS6mYkYgXc1dXJDMEOr\\/DtghfMrHf4h9EVyWE4p8Dk86IYyMLlIwKzJ1ApfkGEgXgfEZIDtVhaw2ernXBEQzhl9jQsamlCinlPNhCHnxa1k9Y3yOvDBBF9bNMLL76AvKoiT2R3zjlnY8fmduzZ3gMtnSNIiMFgVwk\\/GStyRFJaBL4TPo65n7oTr29cB3fvVlQks4TEacT39eLcY5Yh2t+N3R27YKzxon1kENHOffApzG4IRFZ+9lrULZmO5slTUYhkMbnZhALdn5N\\/UYE5yPAuxqShKF0trbeBvD9q9kR46KZMmnD7ElGgUU8ufWOrJcfKr3lc\\/Z4CCYfDD\\/HlAnHeaaWW0WwJJZCiWb3ZH8Pze4bRnS6iJzaCqZqMFXU1sNSakXQYEAnF4MuSeJsb\\/kIRG9s6Ya6pYnxg0khnS5eLXZEiWkMFGFIMqLINLRYNUxrMeO5PD+OcSc34wsWniTSAbqsfajiKrp5B7AgH0RuMIBMIktF5hEMhrFu3Vt8yMxTwo2H6ZFTNn4HXnn8J\\/t3tYg2A3rFocBoZ88SE+omYe+xxqD73ajz52lbIr62GtXUN4FYQl6ow6ezzoM2cDZvHg4VeBVvv+Cla\\/3gvvIurcdpHL0Qm64XfVIdu0uu0F\\/GFc45HLJ3G1PpKmJIaUoTKQ0R04UQKBVrmqS1VmFAmNr3QRjN9kTyLxJHJskh44xRYachAluU3fT7fovcTiHBXHxJdcDITHBejlawW9JRGE7CPKCfAuBJN2fFoJIo\\/bduB4+VKLJ9eC0+LDcN7e3DmhFq6NVoXkdIuNrgjEEE\\/Y0jeyLjCQFo0FuENAX3M4EkFEcA+1BJ9xKu86B5IMllUYS6rQVpI0CygrcIgXNQ7FYvMYUSvrkLoazSKvqaCjm4MRGsVjiKcpgyTthhzBitCMeEiKJBiAMGwCWpgD6Rdz8HcLVZDNMPUWMRA1fnwRzJEp2nIknguk2AiQ\\/eUCdByZchaZULjDDXcBovq0NFZJB3E6TV0zXRbYbMJLZPr0ExLlOJBOCmcs2cRhTkkMcCyv2NV0RRdLmKn0XgiPrbppu6yOrxe7+T3E8gv+PIpcX7050tsBx1TEH1D\\/QULHu+I4sWte9BQ60EZ845MugAjffIZVTbUED0ME0oOhYt4tMOPRzoHkB12oZiggJlhm0T3h9VKoEBtNoofP2LQZpKWZ0BXeMjMemVahEkVAZUIRvQ\\/kfCsnNc3Km9pqEQ1\\/XGKCtOeFz\\/8xgCfZjv1Ja85KKEAFLpMhMjwWBgFPkdhm0xkooFxUSF90UyEDoDw21dBl1pFj2AjChPr4\\/IwKEnkOuLMxJmdSxG4WG+uax+yJhvsdW54mGuoDOQtNeX41MQWFOJdWDihHNOZhymKjIOV0hqGcTxdRYGc9H4C+QhfHuV5g1g7M34lx9tFPCiMTESlKypDq5rFuliAuD8Di+aCZqjA8Z4C3tjwBG7dQFPwHkcmqbAmQ0RodGlej54oyQ4HJFqhJhbPCgIKeb1jUfhXA63BYTXrmyqILW\\/yTBSdzGdm1tox30Wr6N+H\\/l2tyBE6F+hO9\\/UMYWTreqT8PTB4y+BY\\/CHsk1sYdBkD3FYKsYCiWPPKV+ETBXK0ig7Jjh1IFIyQKydBH+\\/KpmAuhGkNUSRdNhiYPzkKNgrEgriVzpA5FAjNjXTNcnAPPjSPtB49C5t39eG4MiMuaDHRNfM7swM4CIIVvBQjpaVFkRTKV+mybntPgfCziRf9nm\\/PF4ulSt3I7xgihehspKa5nWS+SIxUvYOxL6XisT0DeG0ohdzWN9HZ3snY0gi0tmNqcxaVTXWoN1jhJvqqq2\\/E5s1voa97H31qcqxX1UzNciFFhsVcXqhVjQhYyzFi9EILDsLX8xrmEJV50mHmBLRGkQlX1sFZ7oU71oeR7kEcu+I0bIcJD+2NoluM+jprIVtysIhNjWi9SoxIjKChGBtClhaUc\\/loFcysmYlrGbabyFKL08fn+HSJ1sy8QR8EU5mnzJ0LeVI1vAQUySdvhzNmQPPK65AmYrz6pInYs3cXrlk6BxPp1tOZzEHHU0SXvlDy0Y2VMES+zuZ1Q++HskQR49fbKJDKzHs8OEettDtpumKPXj1wMXmkXIRL6BhJ4olfP4IHt6xHdvJ8mFY9BalvLfI1s2EomHDckqlomDgF9\\/zmNzoet1iZWdPKCpI6is5CObotJ+xnfxF9vqmYMPQivrp8ArzOBtzynW+haeosRFQJSYKCtn0jmLtwIYzMP1xT52JL3oM9xQoy0gPJoKLOnsMc4wBef+A+WIkKi7EI8t4myItPQ8Rai2In45hEyIyQ6CgjoqKbzJoZA13wqGHEmcFHXRVwLl2CmhoXQqv+hPjaZ2AZ3MYU0ILJn7wV8875BFpo9POZUx1V5YDXJJYUZt5zgKu0tzF5fxGPh\\/7R33OeT4G8QYGYDvrgokLEQZMWgRcF\\/We4NdmAJDH4mb\\/bhE6bEeF1f2XjKlHpMGPfXd9mgpeCtbaesnNhgBl8NZOncHhEXwwnsn9Dke4skkb5gsVQT7sGO1s3Q11zPypSDLaGWqTMMjJMMovMaXzzlsA2ZS6GKGCjo0Lvr1LT1OyCGCagNRDtmLPM1Hu3wb7uPsQCHbAZR\\/OBMC0FYnOPo09DP8FBkUIwpegFspK+3X2R8NzY0IB8ZTVsZdVYOH0icv3D2PzH+1Gf7cMES5pxw4FHnn8dE069GOd\\/7vOY4ZVwZKUVlYxxyWQG2XwR7yGPkkC+wbffPvC79+1cTKfT05PJ5BuUoOvA72Q23GYx6z2nIqQUaCViWeSdv\\/41Hnx2L5KLPoq2jAl2+t0kNcfLyyo9UXizMdQy7\\/D7e3U3ZbDYAMLkvMEJxZhG1FmNjsQ8uFJB2A0RBIw2uib6\\/\\/QIvSKtEnn4aEluNQerqJPZeiSVRTuZqhJ6il\\/9UJiwuRjULd0bEN74DNzZoJ61m+0WZJmFN02aChPrbd3ZBfecq9Evutcn10OZ3MjwItGtmphHGOEKbEPHWxvQsXkjY0aQLi3I7zM45vglsMpl6ItouOS6z+Kk5Qvh0XIoM42OmSSYqWez+YMKRAxbOxyOz1PJ7zwYzw8WQ97+cvSJYl\\/xF3h+2TtcFqWQTKdQzCRhkEYHaCxWN\\/7ry1\\/DX375fZiWfBL9Kz5P5u3Bt2fZUVnp1MdMWqjdRqIlMf5tJsLa2TuM3YE0brvrXgwZ6iB7qmCW40g4y2FzWdASfAtY9zJyYm+S3Ah9vEA9rJcWJXoRxDCQySAW7pbBNvsYDLtnICV79BX8VXufQ2W+Gxv39uHjn7gU29\\/YDv\\/eflx08YX405MPoWrqJOyY+wm9\\/dKe7ci0boGVrk\\/KhMg4arhxdM+tXJ4wmgloKlmA29OIT37qBkQSfdjWtgufuPIafPjkYwkQEvqa3TRVxudm5m+Q3gWGxG\\/eFQqFE1VVXT\\/+vABP\\/5CFHFDEqs4\\/QO9QHxXc+LFjEaQETN60aRNu+urX0MFgLTl8FKcLc+fNRTMDX53ThKr6CfCUV8HrMMHFGBRNZ9A9ksKXv\\/19ujIPDJ5mxInCyp2EwW3rkG7fSGRWECAWpsJoQBQERIlUxOicGEoNj1BYzJoTigVKZQPcZRXIJ8KYWGbDmSctx8Zt23DGmWdi165dmDVrFrbys\\/htkocfeUTvAMyJPR9pJXnGpbRK8qw+ePmchqkzmPlvQn6kF+nIIGiCcHt9aGhhXGuowvy5s3HJykvg85VhdAcJbfza+gP5J2asXP73mPxBZi4KiHajuE\\/MTxJBXwQlMWlNMKuzsxMnn7ECScGwDBvIREkkQ6rC\\/EERK8DZUNFlTddjYCImiV5kkT+Jpe1F8RuRQMxMoGA2wkP0oxAv9DPOGClsLxl15LIjddg4bdo0DA4O4tlnn4XNQbQnW5AkEmMUgq\\/Mi3g6h0A4jsmz5mMWk7y5c+boS4qF4oj7kqkktm\\/dhmQoDFWjNoshWeYXsNLZ0WWJbTkkukOXkRYo5REd6EZe\\/LAHnUFNTQ2qqyoxj6jrpptu0ifljd\\/j4wCBvMbjozwC\\/whzP\\/BUUmLoGxKJxK0UhKG0CY+YAGAkZO3uG8K1n\\/08k8QUTDYLFi5YiJrKKvT09aGQSqCmogIKtTzATF38ILrIttNkUJb5gCrmdCV7idjyTAnMyGUMCAz0UIOzYrrI\\/pkgok7BCDH7Q8wcCidiiJK5Wn5UyIuPXIpqwuznn3kWy486Tt+68owzTsdHzvqIriBnnX22vg9Cku0RPcB51qtvRZNLoUBwIKna6OJ00mXk8xtamnH0scvxkY+eg6VHLoFTjH7SQoWHKLWnJBDhKSj4J9jWK+g9gv8MXz+wQIRFiHsZpBbw9W4eR4yHb6LsaW\\/Hzh3bse711\\/GnR\\/+IaDgCBzPrHHG6vhBeMegIS1HETkUmggQH\\/XSW8SGDsnIfHE43rzHyXBzDw0NQ8wWxLaHuXiwUhPg5d9FrKgaqRM0xsS1nhhk7M2Ev3d7MmdOxdetWXH31NfjeD76H2po6fJLvX1j1Avbu3YtEPIFkYkTX5hyzfafLjUaiq\\/nz52P+vHmYPW8+Jk+eQlTkQJzXalS4ApPX0WlAtoOxRTD\\/Rirqb4SA3utnN\\/7dAtEtY+xHTk7ZvXv3tzs6Oha3tbUhEAjoGiSmbYp4I1yc2FFAXC\\/Ol3bkUPUe27weFPXuHzF7UJH1HTdGN7oQ2boyLkiOxqySAujTbQqj4\\/+iPeKzQFNiF80FCxbg6KOPpoB8WLXqRWzcuEl3KXPpbsT+EkcuW4ympiZ97wnhisT9pami4n1J88WzRX0HmZc1gtHfuL6Lh76VlJjTdUgIRAignVYxlkw2kMnXRiKRy\\/i5QjRSbNUrBCPei2tK00kFA0SvwGhMGmVG6Sh1MYj6xFH6sZh3TmKTdKGJMt59CMZVVlagoaFRjzmzZ8\\/G9OnT9W0lKirKcaBFl+4X9Yh2je+hKNXJ7zQ+90m6pR\\/x9JqD8eaQE4hgrCBA+FLz6OaCYm\\/yCjbwLBIlstNlZLJ57De\\/xpg96g7ECFvJpeXzOR29lKablpgippCazRbd1Qi3JfbAsVodpe1edGaIZ5a2Myv9crK4tyQs8Tp+A7qSMPZvCJxMlr5vZRsf4zMeZDt2izrH7ft80HLIC0Q0TvzupZjBV9rIRNxDtCSmDc7G6NZLJ2J00y4xYqkz2W63vWf9\\/+LZ72JdxDZeu45Ce5n3vUG3Fi1dK1CdqKtE73+yQN7z+eOWI5TzqMVo\\/5rYfbiah3CBXjJJ9CDYWLeF9Ql1FqaU4+c06xG\\/cRXh+5GxzrseHuKXXgfJ5AEKpHBgnQdbH\\/J\\/RiCHy7+nHBbIIVYOC+QQK4cFcoiVwwI5xMphgRxi5bBADrFyWCCHWDkskEOs\\/D8+dm05nIZzDAAAAABJRU5ErkJggg==";

//        progressDialog.dismiss();
    }

    Log.e("itembic", "*** end ***");


}


}}
