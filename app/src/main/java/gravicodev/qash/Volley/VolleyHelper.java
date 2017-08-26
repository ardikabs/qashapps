package gravicodev.qash.Volley;

import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import gravicodev.qash.Helper.ApiHelper;
import gravicodev.qash.Helper.VolleyCallback;

/**
 * Created by mfatihas on 7/28/2017.
 */

public class VolleyHelper {
    public static final String TAG = AppController.class
            .getSimpleName();

    public void getSaldo(final VolleyCallback callback, String accountNumber) throws JSONException {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String url = "http://finhacks.gravicodev.id/index.php";



        JSONObject requestJSON = new JSONObject();
        requestJSON.put("AccountNumber",accountNumber);
        requestJSON.put("method","getBalance");

        final String requestBody = requestJSON.toString();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //...
                        Boolean status = null;
                        try {
                            status = response.getBoolean("Status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status == true) {
                            try {
                                callback.onSuccess(response.getString("AvailableBalance"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            Log.d("VOLLEY","GAGAL");
                        }
                        Log.d("volley_log",""+status);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //...
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, tag_json_obj);
    }

    public void doTransfer(final VolleyCallback callback, String source,String dest,String amount, String desc) throws JSONException {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String url = "http://finhacks.gravicodev.id/index.php";

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("method","doTransfer");
        requestJSON.put("AccountNumber",source);
        Random rnd = new Random();
        int transactionID = 10000000 + rnd.nextInt(90000000);
        requestJSON.put("TransactionID",String.valueOf(transactionID));
        requestJSON.put("ReferenceID","12345678/PO/2017");
        Log.d("yoi",String.valueOf(transactionID));
        requestJSON.put("Amount",amount);
        requestJSON.put("BeneficiaryAccountNumber",dest);
        requestJSON.put("Remark1",desc);
        requestJSON.put("Remark2","QashApp");

        final String requestBody = requestJSON.toString();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //...
                        String status = null;
                        try {
                            status = response.getString("Status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("cok",status);
                        if(status.compareTo("Success") == 0) {
                            try {
                                callback.onSuccess(response.getString("TransactionID"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            Log.d("VOLLEY","GAGAL");
                        }
                        Log.d("volley_log",""+status);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //...
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, tag_json_obj);
    }

    public void getStatement(final VolleyCallback callback, String accountNumber) throws JSONException {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String url = "http://finhacks.gravicodev.id/index.php";



        JSONObject requestJSON = new JSONObject();
        requestJSON.put("AccountNumber",accountNumber);
        requestJSON.put("method","getStatement");

        final String requestBody = requestJSON.toString();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //...
                        Boolean status = null;
                        try {
                            status = response.getBoolean("Status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status == true) {
                            try {
                                callback.onSuccess(response.getString("Data"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            Log.d("VOLLEY","GAGAL");
                        }
                        Log.d("volley_log",""+status);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //...
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, tag_json_obj);
    }

    public void sendNotification(String AccountNumber, String title, String body) throws JSONException {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String url = "http://gravicodev.id:4747/sendNotification";

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("AccountNumber",AccountNumber);
        requestJSON.put("Title",title);
        requestJSON.put("Body",body);

        final String requestBody = requestJSON.toString();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //...
                        Boolean status = null;
                        try {
                            status = response.getBoolean("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("volley_log",""+status);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //...
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, tag_json_obj);
    }

    private static final String KEY = "qashcuteteam";

    public void login(final VolleyCallback callback, String userid, String passwd) throws JSONException {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String url = "http://gravicodev.id:4747/login";



        JSONObject requestJSON = new JSONObject();
        requestJSON.put("Userid",userid);
        try {
            requestJSON.put("Password",hmacSha256(passwd,KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestJSON.put("SecretKey",KEY);

        final String requestBody = requestJSON.toString();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //...
                        Boolean status = null;
                        try {
                            status = response.getBoolean("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status == true) {
                            try {
                                callback.onSuccess(response.getString("token"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            Log.d("VOLLEY","GAGAL");
                        }
                        Log.d("volley_log",""+status);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //...
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, tag_json_obj);
    }

    private static String hmacSha256(String value, String key)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {
        String type = "HmacSHA256";
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), type);
        Mac mac = Mac.getInstance(type);
        mac.init(secret);
        byte[] bytes = mac.doFinal(value.getBytes());
        return bytesToHex(bytes);
    }

    private final static char[] hexArray = "0123456789abcdef".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
