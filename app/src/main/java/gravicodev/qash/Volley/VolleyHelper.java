package gravicodev.qash.Volley;

import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.AuthFailureError;
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

    public int getToken(){
        int saldo = 0;

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String url = "https://sandbox.bca.co.id/api/oauth/token";

        StringRequest postRequest=new StringRequest(Request.Method.POST,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                String access_token = null;
                try {
                    obj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    access_token = obj.getString("access_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ApiHelper.access_token = access_token;
                Log.d("volley_log",ApiHelper.access_token);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("error:",volleyError.toString());

                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("grant_type", "client_credentials");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Basic YWI3ZjA4ZWQtY2VhYS00ZDgzLTllMDktZjZiMDc2MGE0Y2U5OjNjZmEwMjU2LTAzYmYtNDcxZC04ZWRiLTFhYjc3OTZkOGQzZA==");
                return headers;
            }


        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(postRequest, tag_json_obj);

        return saldo;
    }

    public int getSaldo(String corporateID, String accountNumber){
        int saldo = 0;

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String url = "https://sandbox.bca.co.id/banking/v2/corporates/BCAAPI2016/accounts/0201245680";



        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //...
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
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Basic YWI3ZjA4ZWQtY2VhYS00ZDgzLTllMDktZjZiMDc2MGE0Y2U5OjNjZmEwMjU2LTAzYmYtNDcxZC04ZWRiLTFhYjc3OTZkOGQzZA==");
                return headers;
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, tag_json_obj);

        return saldo;
    }

    public int doTransfer(){
        int saldo = 0;

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String url = "https://sandbox.bca.co.id/api/oauth/token";

        StringRequest postRequest=new StringRequest(Request.Method.POST,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                String access_token = null;
                try {
                    obj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    access_token = obj.getString("access_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("volley_log",access_token);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("error:",volleyError.toString());

                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("grant_type", "client_credentials");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Basic YWI3ZjA4ZWQtY2VhYS00ZDgzLTllMDktZjZiMDc2MGE0Y2U5OjNjZmEwMjU2LTAzYmYtNDcxZC04ZWRiLTFhYjc3OTZkOGQzZA==");
                return headers;
            }


        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(postRequest, tag_json_obj);

        return saldo;
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
