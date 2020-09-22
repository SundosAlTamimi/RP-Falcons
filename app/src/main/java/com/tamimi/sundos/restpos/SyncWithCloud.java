package com.tamimi.sundos.restpos;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tamimi.sundos.restpos.Models.CategoryWithModifier;
import com.tamimi.sundos.restpos.Models.FamilyCategory;
import com.tamimi.sundos.restpos.Models.ForceQuestions;
import com.tamimi.sundos.restpos.Models.ItemWithFq;
import com.tamimi.sundos.restpos.Models.ItemWithModifier;
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

public class SyncWithCloud {
    private Context context;
    private ProgressDialog progressDialog;
    private DatabaseHandler mDHandler;
    public static String data1,data2,data3 ;

    public SyncWithCloud(Context context) {
        mDHandler = new DatabaseHandler(context);
        this.context = context;
    }

    public void startSyncing(String flag) {

        if (flag.equals("menuRegistration"))
            new SyncMenuRegistration().execute();

        if(flag.equals("sync")){

            new SyncModifier().execute();//ok
            new SyncForceQ().execute();//ok
            new SyncItem().execute();//ok
            new SyncCategory().execute();// not all ok
            new SyncShiftNo().execute();// not ok
            new SyncVoidReason().execute();//not ok
            new SyncJoinItemWithModifer().execute();//ok
            new SyncJoinItemWithFQ().execute();//OK
            new SyncJoinCategoryWithitem().execute();//OK

//            new SyncitemPic().execute();

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


    private class SyncModifier extends AsyncTask<String, String, String> {
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
        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
            try {
                String link = Settings.URL + "GetModifer";

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

            if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":0")) {
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
//                        obj.setModifierActive(finalObject.getInt("ACTIVE"));
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
        }
    }



    private class SyncForceQ extends AsyncTask<String, String, String> {
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
        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
            try {
                String link = Settings.URL + "GetForceQ";

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

                Log.e("tag", "Force Q -->" + stringBuffer.toString());

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

            if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":0")) {
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
//            progressDialog.dismiss();
        }
    }




    private class SyncItem extends AsyncTask<String, String, String> {
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
        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
            try {
//                String link = "http://Falconssoft.net/RestService/FSAppServiceDLL.dll/SyncGetItems";

//                String link = "http://10.0.0.16:8081/SyncGetItems";
                String link = Settings.URL + "SyncGetItems";

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

                Log.e("tag", "items -->" + stringBuffer.toString());

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

            if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":0")) {
                Log.e("tag item", "****Success");

                try {

                    JSONObject parentObject = new JSONObject(JsonResponse);
                    JSONArray parentArray = parentObject.getJSONArray("Data");

                    List<Items>items=new ArrayList<>();

                    for (int i = 0; i <parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);

//                        {"ErrorCode":0,"ErrorDescreption":0,"Data":[{"Id":1,"BarCodeNo":1,"CATEGORYID":1,
//                         "CATEGORYNAME":"Group 1","MENU_NAME":"Item1","UNITID":1,"UNITNAME":"Unit 1",
//                         "KITCHENID":1,"KITCHENNAME":"KITCHEN 1","FAMILYID":1,"FAMILY_NAME":"Family1",
//                         "PRICE":100,"TAX_TYPE":2,"TAX_PERCENT":0,"SECONDARY_NAME":null,"ITEM_BARCODE":"1",
//                         "STATUS":1,"ITEM_TYPE":1,"DESCRIPTION":"Test","INVENTORY_UNIT":null,"WASTAGE_PERCENT":0,
//                         "DISCOUNT_AVAILABLE":false,"POINT_AVAILABLE":false,"OPEN_PRICE":false,"KITCHEN_PRINTER_TO_USE":null,
//                         "USED":0,"SHOW_IN_MENU":false,"INDATE":"\/Date(1581373973037)\/"},{"Id":2,"BarCodeNo":2,"CATEGORYID":2,
//                         "CATEGORYNAME":"Group 2","MENU_NAME":"Item 2","UNITID":2,"UNITNAME":"Unit 2","KITCHENID":2,"KITCHENNAME":"KITCHEN 2",
//                         "FAMILYID":3,"FAMILY_NAME":"Family3","PRICE":50,"TAX_TYPE":1,"TAX_PERCENT":16,"SECONDARY_NAME":null,"ITEM_BARCODE":"2",
//                         "STATUS":1,"ITEM_TYPE":1,"DESCRIPTION":null,"INVENTORY_UNIT":null,"WASTAGE_PERCENT":0,"DISCOUNT_AVAILABLE":false,"POINT_AVAILABLE":false
//                         ,"OPEN_PRICE":false,"KITCHEN_PRINTER_TO_USE":null,"USED":0,"SHOW_IN_MENU":false,"INDATE":"\/Date(1581373993260)\/"}]}


                        Items obj = new Items();
                        obj.setMenuCategory(finalObject.getString("CATEGORYNAME"));
                        obj.setMenuName(finalObject.getString("MENU_NAME"));
                        obj.setFamilyName(finalObject.getString("FAMILY_NAME"));

                        obj.setPrice(finalObject.getDouble("PRICE"));
                        obj.setTaxType(finalObject.getInt("TAX_TYPE"));
                        obj.setTax(finalObject.getDouble("TAX_PERCENT"));
                        obj.setSecondaryName(finalObject.getString("SECONDARY_NAME"));

                        obj.setKitchenAlias(finalObject.getString("KITCHENNAME"));
                        try {
                            obj.setItemBarcode(finalObject.getInt("ITEM_BARCODE"));
                        }catch (Exception e){
                            obj.setItemBarcode(-10);
                        }
                        obj.setStatus(finalObject.getInt("STATUS"));
                        obj.setItemType(finalObject.getInt("ITEM_TYPE"));

                        obj.setDescription(finalObject.getString("DESCRIPTION"));
                        obj.setInventoryUnit(finalObject.getString("INVENTORY_UNIT"));
                        obj.setWastagePercent(finalObject.getInt("WASTAGE_PERCENT"));
                        if(finalObject.getBoolean("DISCOUNT_AVAILABLE")){
                            obj.setDiscountAvailable(1);
                        }else{
                            obj.setDiscountAvailable(0);
                        }

//                        obj.setPointAvailable(finalObject.getInt("POINT_AVAILABLE"));
                        if(finalObject.getBoolean("POINT_AVAILABLE")){
                            obj.setPointAvailable(1);
                        }else{
                            obj.setPointAvailable(0);
                        }

                        //   obj.setOpenPrice(finalObject.getInt("OPEN_PRICE"));

                        if(finalObject.getBoolean("OPEN_PRICE")){
                            obj.setOpenPrice(1);
                        }else{
                            obj.setOpenPrice(0);
                        }

                        obj.setKitchenPrinter(finalObject.getString("KITCHEN_PRINTER_TO_USE"));
                        obj.setUsed(finalObject.getInt("USED"));
//                        obj.setShowInMenu(finalObject.getInt("SHOW_IN_MENU"));

                        if(finalObject.getBoolean("SHOW_IN_MENU")){
                            obj.setShowInMenu(1);
                        }else{
                            obj.setShowInMenu(0);
                        }

//                        try {
//                            obj.setPic(finalObject.getString("ITEMPIC"));
//                        }catch(Exception e){
//                            obj.setPic("");
//                        }
//
                        items.add(obj);

                    }

 Log.e("tag item1", "item is update ");
                    mDHandler.deleteAllItems();
                    for (int i = 0; i < items.size(); i++) {
                        mDHandler.addItem(items.get(i));
//                        progressDialog.setMessage("ADDING "+items.get(i).getName()+"...");
                    }

           Log.e("tag item", "item is update ");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag_item_syn", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }


    private class SyncCategory extends AsyncTask<String, String, String> {
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
        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
            try {
                String link = Settings.URL + "GetFamCategUnit";

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

                Log.e("tag", "category_in -->" + stringBuffer.toString());

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

            if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":0")) {
                Log.e("tag_category_item", "****Success");

                try {

                    JSONObject parentObject = new JSONObject(JsonResponse);
                    JSONArray parentArray = parentObject.getJSONArray("Data");

                    List<FamilyCategory>items=new ArrayList<>();

                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);

                        //{"ErrorCode":0,"ErrorDescreption":0,"Data":[{"Id":1,"CATEGORYID":1
                        // ,"CATEGORY_NAME":"Group 1","PICPATH":null,"INDATE":"\/Date(1581373799627)\/",
                        // "iRowTable":0},{"Id":2,"CATEGORYID":2,"CATEGORY_NAME":"Group 2","PICPATH":null,
                        // "INDATE":"\/Date(1581373805330)\/","iRowTable":0},{"Id":3,"CATEGORYID":3,
                        // "CATEGORY_NAME":"cat5","PICPATH":null,"INDATE":"\/Date(1581339773163)\/",
                        // "iRowTable":0}]}


                        FamilyCategory obj = new FamilyCategory();
                        obj.setName(finalObject.getString("CATEGORY_NAME"));
                        obj.setType(2);
                        obj.setSerial(finalObject.getInt("CATEGORYID"));


//                        obj.setCatPic(finalObject.getString("PIC"));

//
                        items.add(obj);

                    }

//
                        mDHandler.deleteAllFamilyCategory();
                    for (int i = 0; i < items.size(); i++) {
                        mDHandler.addFamilyCategory(items.get(i));
//                        progressDialog.setMessage("ADDING ("+items.get(i).getName()+")...");
                    }
                    Log.e("tag_category_item", "****save_Success");

//

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag category", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }


    private class SyncShiftNo extends AsyncTask<String, String, String> {
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
        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
            try {
                String link = Settings.URL + "GetShifts";

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

                Log.e("tag", "shift -->" + stringBuffer.toString());

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

            if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":0")) {
                Log.e("tag shift", "****Success");

                try {

                    JSONObject parentObject = new JSONObject(JsonResponse);
                    JSONArray parentArray = parentObject.getJSONArray("Data");

                    List<Shift>items=new ArrayList<>();

                    for (int i = 0; i <parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);

                        //

                      //  SHIFT_NO":"1","SHIFT_NAME":"SHIFT A","FROM_TIME":"06:00","TO_TIME":"04:00"
                        Shift obj = new Shift();
                        obj.setShiftNo(finalObject.getInt("SHIFT_NO"));
                        obj.setShiftName(finalObject.getString("SHIFT_NAME"));
                        obj.setFromTime(finalObject.getString("FROM_TIME"));

                        obj.setToTime(finalObject.getString("TO_TIME"));


//
                        items.add(obj);

                    }

                    Log.e("tag shift", "shift is update ");
                    mDHandler.deleteAllShift();
                    for (int i = 0; i < items.size(); i++) {
                        mDHandler.addShift(items.get(i));
//                        progressDialog.setMessage("ADDING "+items.get(i).getName()+"...");
                    }

                    Log.e("tag shift", "shift is update ");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag_shift_no", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }


    private class SyncVoidReason extends AsyncTask<String, String, String> {
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
        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
            try {
                String link = Settings.URL + "GetVoidReason";

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

            if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":0")) {
                Log.e("tag void", "****Success");

                try {

                    JSONObject parentObject = new JSONObject(JsonResponse);
                    JSONArray parentArray = parentObject.getJSONArray("Data");

                    List<VoidResons>itemModifer=new ArrayList<>();

                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);

                        //SHIFT_NO":"1","SHIFT_NAME":"SHIFT A","USER_NUMBER":"1","USER_NAME":"ALAA","VOID_REASON":"خطأ مطبخ","SDATE":"04-08-2019","ACTIVEATED":"0"

                        VoidResons obj = new VoidResons();
                        obj.setShiftNo(finalObject.getInt("SHIFT_NO"));
                        obj.setShiftName(finalObject.getString("SHIFT_NAME"));
                        obj.setUserNo(finalObject.getInt("USER_NUMBER"));
                        obj.setUserName(finalObject.getString("USER_NAME"));
                        obj.setVoidReason(finalObject.getString("VOID_REASON"));
                        obj.setDate(finalObject.getString("SDATE"));
                        obj.setActiveated(finalObject.getInt("ACTIVEATED"));

//
                        itemModifer.add(obj);

                    }

//
                    mDHandler.deleteAllVoidReasons();
                    for (int i = 0; i < itemModifer.size(); i++) {
                        mDHandler.addVoidReason(itemModifer.get(i));
                    }

//

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag voidReson", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }


    private class SyncJoinItemWithModifer extends AsyncTask<String, String, String> {
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
        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
            try {
                String link = Settings.URL + "SyncGetItemWModifer";

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

                Log.e("tag", "itemModifer -->" + stringBuffer.toString());

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

            if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":0")) {
                Log.e("tag iM", "****Success");

                try {

                    JSONObject parentObject = new JSONObject(JsonResponse);
                    JSONArray parentArray = parentObject.getJSONArray("Data");

                    List<ItemWithModifier>itemModifer=new ArrayList<>();



                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);

                        //SHIFT_NO":"1","SHIFT_NAME":"SHIFT A","USER_NUMBER":"1","USER_NAME":"ALAA","VOID_REASON":"خطأ مطبخ","SDATE":"04-08-2019","ACTIVEATED":"0"

                        ItemWithModifier obj = new ItemWithModifier();
                        obj.setItemCode(finalObject.getInt("ITEM_CODE"));
                        obj.setModifierNo(finalObject.getInt("MODIFIER_NO"));
                        obj.setModifierText("-"+finalObject.getString("MODIFIER_TEXT"));


//
                        itemModifer.add(obj);

                    }

//();
                    mDHandler.deleteAllItemModifier();
                    for (int i = 0; i < itemModifer.size(); i++) {
                        mDHandler.addItemWithModifier(itemModifer.get(i));
                    }

                    Log.e("tag_iM", "****SAVE_Success");
//

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag ModiferItem", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

    private class SyncJoinItemWithFQ extends AsyncTask<String, String, String> {
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
        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
            try {
                String link = Settings.URL + "SyncGetItemWithFQ";

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

                Log.e("tag", "itemFq-->" + stringBuffer.toString());

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

            if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":0")) {
                Log.e("tag iFQ", "****Success");

                try {

                    JSONObject parentObject = new JSONObject(JsonResponse);
                    JSONArray parentArray = parentObject.getJSONArray("Data");

                    List<ItemWithFq>itemModifer=new ArrayList<>();



                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);

                        //SHIFT_NO":"1","SHIFT_NAME":"SHIFT A","USER_NUMBER":"1","USER_NAME":"ALAA","VOID_REASON":"خطأ مطبخ","SDATE":"04-08-2019","ACTIVEATED":"0"

                        ItemWithFq obj = new ItemWithFq();
                        obj.setItemCode(finalObject.getInt("ITEM_CODE"));
                        obj.setQuestionNo(finalObject.getInt("QUESTION_NO"));
                        obj.setQuestionText(finalObject.getString("QUESTION_TEXT"));


//
                        itemModifer.add(obj);

                    }

                    mDHandler.deleteAllItemFQ();
//();
                    for (int i = 0; i < itemModifer.size(); i++) {
                        mDHandler.addItemWithFQ(itemModifer.get(i));
                    }
                    Log.e("tag_iFQ", "****SAVE_Success");
//

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag iTEMfQ", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

    private class SyncJoinCategoryWithitem extends AsyncTask<String, String, String> {
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
        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
            try {
                String link = Settings.URL + "SyncGetCategWModf";

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

                Log.e("tag", "catMo-->" + stringBuffer.toString());

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

            if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":0")) {
                Log.e("tag_CWI", "****Success");

                try {

                    JSONObject parentObject = new JSONObject(JsonResponse);
                    JSONArray parentArray = parentObject.getJSONArray("Data");

                    List<CategoryWithModifier>itemModifer=new ArrayList<>();



                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);

                        //SHIFT_NO":"1","SHIFT_NAME":"SHIFT A","USER_NUMBER":"1","USER_NAME":"ALAA","VOID_REASON":"خطأ مطبخ","SDATE":"04-08-2019","ACTIVEATED":"0"

                        CategoryWithModifier obj = new CategoryWithModifier();
                        obj.setModifierName(finalObject.getString("MODIFIER_NAME"));
                        obj.setCategoryName(finalObject.getString("CATEGORY_NAME"));

                        itemModifer.add(obj);

                    }

                    mDHandler.deleteAllCategoryModefier();
//();
                    for (int i = 0; i < itemModifer.size(); i++) {
                        mDHandler.addCategoriesModifier(itemModifer.get(i));
                    }
                    Log.e("tag_CWI", "****SAVE_Success");
//

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag iTEMfQ", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }


    private class SyncitemPic extends AsyncTask<String, String, String> {
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
        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
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

            if (JsonResponse != null && JsonResponse.contains("\"ErrorCode\":0")) {
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
                        data1=finalObject.getString("ITEMPIC");
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag voidReson", "****Failed to export data");
//                progressDialog.dismiss();
            }

        }
    }




}
