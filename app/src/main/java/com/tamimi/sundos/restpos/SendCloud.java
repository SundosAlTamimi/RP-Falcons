package com.tamimi.sundos.restpos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

import static java.net.Proxy.Type.HTTP;

public class SendCloud {

    private Context context;
    private ProgressDialog progressDialog;
    private JSONObject obj;
    DatabaseHandler dbHandler;


    public SendCloud(Context context, JSONObject obj) {
        this.obj = obj;
        this.context = context;
        dbHandler = new DatabaseHandler(context);
    }

    public void startSending(String flag) {
//        Log.e("check",flag);

        if (flag.equals("kitchen"))
            new JSONTaskKitchen().execute();

        if (flag.equals("MaxSerial"))
            new JSONTaskMaxSerial().execute();

        if (flag.equals("Order"))
            new JSONTaskOrder().execute();

        if (flag.equals("FamilyCategory"))
            new JSONTaskFamilyCategory().execute();

        if (flag.equals("MenuRegistration"))
            new JSONTaskMenuRegistration().execute();

        if (flag.equals("Modifier"))
            new JSONTaskModifier().execute();

        if (flag.equals("ForceQuestion"))
            new JSONTaskForceQuestion().execute();

        if (flag.equals("ItemWithModifier"))
            new JSONItemWithModifier().execute();

        if (flag.equals("CategoryWithModifier"))
            new JSONCategoryWithModifier().execute();

        if (flag.equals("ItemWithFQ"))
            new JSONItemWithFQ().execute();

        if (flag.equals("Announcement"))
            new JSONAnnouncement().execute();

        if (flag.equals("authentication"))
            new JSONGetAuthentecationInformation().execute();
    }

    private class JSONTaskKitchen extends AsyncTask<String, String, String> {
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
//                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/RestSaveKitchenScreen";
                String link = Settings.URL + "RestSaveKitchenScreen";
//                String link = "http://10.0.0.16:8081/RestSaveKitchenScreen";
                String data = "compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compyear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "voucher=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link);
//                new SendDeviceDetails().execute("http://52.88.194.67:8080/IOTProjectServer/registerDevice", postData.toString());

                Log.e("data_kitchen ", "--> " + obj.toString());

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

                Log.e("tag KITCHEN", "" + stringBuffer.toString());

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
//            progressDialog.dismiss();
        }
    }

    private class JSONTaskOrder extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        String vhfNo, POSNO, orderKind;

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
//                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/RestSaveOrder";

                String link = Settings.URL + "RestSaveOrder";
//               String link = "http://10.0.0.16:8081/RestSaveOrder";
                String data = "compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compyear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "VOUCHER=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                try {
                    JSONObject jo = obj.getJSONObject("ORDERHEADER");
                    vhfNo = jo.getString("VHFNO");
                    POSNO = jo.getString("POSNO");
                    orderKind = jo.getString("ORDERKIND");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                URL url = new URL(link);
                Log.e("url con ", "" + url.toString());

                Log.e("data_order ", "--> " + obj.toString());
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

                Log.e("tag order", "" + stringBuffer.toString());

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
                Log.e("vhf Success___", "= " + vhfNo);

                dbHandler.updateOrderTablesIsPost(vhfNo, POSNO, orderKind);
                dbHandler.updateOrderTablesIsPost2(vhfNo, POSNO, orderKind);
                dbHandler.updateOrderTablesIsPost3(vhfNo, POSNO, orderKind);

            } else if (s != null && s.contains("ErrorCode : 6")) {

                dbHandler.updateOrderTablesIsPost(vhfNo,POSNO,orderKind);
                dbHandler.updateOrderTablesIsPost2(vhfNo,POSNO,orderKind);
                dbHandler.updateOrderTablesIsPost3(vhfNo,POSNO,orderKind);

            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag ORDER", "****Failed to export data");
                Log.e("vhf failed ___2", "= " + vhfNo + "POSNO = " + POSNO);
            }
//            progressDialog.dismiss();
        }
    }

    private class JSONTaskFamilyCategory extends AsyncTask<String, String, String> {
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
//                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/SaveGroup";
                String link = Settings.URL + "SaveGroup";

                String data = "Compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "CompYear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "POSNO=" + URLEncoder.encode("1", "UTF-8") + "&" +
                        "Group=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link);

                //http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/SaveGroup?Compno=736&CompYear=2019&POSNO=1&Group={%22SERIAL%22:1,%22ITYPE%22:1,%22NAME_CATEGORY_FAMILY%22:%22NEW%20FOOD%22}
                //http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/SaveGroup?Compno=736&CompYear=2019&POSNO=1&Group=%7B%22SERIAL%22%3A26%2C%22TYPE%22%3A2%2C%22NAME_CATEGORY_FAMILY%22%3A%22cat9+%22%7D
                Log.e("data_FamileC ", "--> " + obj.toString());

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

                Log.e("tag famil cat", "" + stringBuffer.toString());

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

            if (s != null && s.contains("Group Or Family Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

    private class JSONTaskMenuRegistration extends AsyncTask<String, String, String> {
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
//                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/SaveItemCard";
                String link = Settings.URL + "SaveItemCard";
                String data = "compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compyear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "ITEMCARD=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link);
                Log.e("data_menuReg ", "--> " + obj.toString());

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

                Log.e("tag MENUE", "" + stringBuffer.toString());
                Log.e("tag MENUE2", "" + obj.toString());

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
//Item Saved Successfully
            if (s != null && s.contains("Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
            progressDialog.dismiss();
        }
    }

    private class JSONTaskModifier extends AsyncTask<String, String, String> {
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
                String link = Settings.URL + "SaveModifier";
//                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/SaveModifier";


                String data = "CompNo=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compYear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "Modifier=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();

                Log.e("data_modifer ", "--> " + obj.toString());

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag MODIFIER", "" + stringBuffer.toString());

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

            if (s != null && s.contains("Modifier Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

    private class JSONTaskForceQuestion extends AsyncTask<String, String, String> {
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
                String link = Settings.URL + "SaveForceQuestion";
//                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/SaveForceQuestion";


                String data = "CompNo=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compYear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "FORCEQ=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");


                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();

                Log.e("data_ForceQ ", "--> " + obj.toString());
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag FORCE Q", "" + stringBuffer.toString());

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

            if (s != null && s.contains("FORCE_QUESTIONS Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

    private class JSONItemWithModifier extends AsyncTask<String, String, String> {
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
                String link = Settings.URL + "SaveItemWModifier";
//                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/SaveItemWModifier";


                String data = "CompNo=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compYear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "ITEMMODIF=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();

                Log.e("data_itemWithMo ", "--> " + obj.toString());

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag item with modif", "" + stringBuffer.toString());

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

            if (s != null && s.contains("Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

    private class JSONCategoryWithModifier extends AsyncTask<String, String, String> {
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
//                String link = "http://falconssoft.net/RestService/FSAppServiceDLL.dll/SaveCategWModifier";
                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/SaveCategWModifier";


                String data = "CompNo=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compYear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "CATEGMODIF=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();

                Log.e("data_Category_Modifir ", "--> " + obj.toString());

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag cat with modif", "" + stringBuffer.toString());

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

            if (s != null && s.contains("CATEGORY_WITH_MODIFIER Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

    private class JSONItemWithFQ extends AsyncTask<String, String, String> {
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
                String link = Settings.URL + "SaveItemWFQ";
//                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/SaveItemWFQ";


                String data = "CompNo=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compYear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "ITEMFQ=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();
                Log.e("data_itemwithfq ", "--> " + obj.toString());

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag Ann", "" + stringBuffer.toString());

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

            if (s != null && s.contains("ITEM_WITH_FQ Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

    private class JSONAnnouncement extends AsyncTask<String, String, String> {
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
                String link = Settings.URL + "SaveANNOUNCEMENT";
//                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/SaveANNOUNCEMENT";

                String data = "CompNo=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compYear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "ANNOUNC=" + URLEncoder.encode(obj.toString().trim(), "UTF-8");

                URL url = new URL(link);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();
                Log.e("data_Announcem ", "--> " + obj.toString());

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

            if (s != null && s.contains("ANNOUNCEMENT_TABLE Saved Successfully")) {
//                Toast.makeText(ExportJason.this , "Success" , Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

    private class JSONTaskMaxSerial extends AsyncTask<String, String, String> {
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
                String link = Settings.URL + "GetMaxVHFNo?";

                String data = "Compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "CompYear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "POSNO=" + URLEncoder.encode("1", "UTF-8") + "&" +
                        "CASHNO=" + URLEncoder.encode("" + Settings.cash_no, "UTF-8") + "&" +
                        "ORDERKIND=" + URLEncoder.encode("1", "UTF-8");

                URL url = new URL(link + data);
                Log.e("url max serial ", "" + url.toString());


                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
//                httpURLConnection.setRequestMethod("POST");
//
//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();

                Log.e("data_max_serial ", "--> " + obj.toString());

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag max serial", "" + stringBuffer.toString() + "ggg");

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

            Log.e("s.content", "" + s.toString());

            if (s != null && s.contains("MaxVHFNO")) {
                Log.e("tag", "****Success");
            } else {
                Log.e("tag maxserial .. ", "****Failed to export data");
            }
//            progressDialog.dismiss();
        }
    }

    private class JSONGetAuthentecationInformation extends AsyncTask<String, String, String> {
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
//            Log.e("check","open");

            try {
                String link = Settings.URL + "GetEmpInfoByUserNM?";

                String data = "compno=" + URLEncoder.encode("736", "UTF-8") + "&" +
                        "compyear=" + URLEncoder.encode("2019", "UTF-8") + "&" +
                        "USERNM=" + URLEncoder.encode(Settings.user_name, "UTF-8");

                URL url = new URL(link + data);
//                Log.e("url autentication", "" + url.toString());
                Log.e("data_Auth", "--> " + obj.toString());

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
//                httpURLConnection.setRequestMethod("POST");
//
//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("Authou serial", "" + stringBuffer.toString()+"ggg");

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
            String password="";
            int isActive=0;
//            Log.e("send cloud", " authentication " + s.toString());

            if (s != null ) {
                if( s.contains("OK")){
                Log.e("authentication", "** Success **");
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    password=jsonObject.getString("USER_PASSWORD");
                    isActive=Integer.parseInt(jsonObject.getString("ACTIVE"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Settings.checkUserFlag = 1;
            } else if (s.contains("No data found")){ // No data found
                Settings.checkUserFlag = 0;
                Log.e("authentication", "** no user found **");
            }}else { // no internet connection
                Settings.checkUserFlag = 3;
            }

            LogIn logIn = (LogIn) context;
            try {
                logIn.getAuthenticationResponse(obj.getString("username"),password,isActive);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            progressDialog.dismiss();
        }
    }


}


