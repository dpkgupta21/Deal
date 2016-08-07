package com.deal.exap.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.deal.exap.model.UserDTO;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.SessionManager;
import com.deal.exap.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.UnsupportedEncodingException;
import java.util.Map;


public class CustomJsonRequest extends Request<JSONObject> {

    private Listener<JSONObject> listener;
    private Map<String, String> params;

    public CustomJsonRequest(String url, Map<String, String> params,
                             Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
        if (AppController.getAppContext() != null) {
            params.put("device_id", DealPreferences.getPushRegistrationId(AppController.getAppContext()));
            params.put("loginuser_id", Utils.getUserId(AppController.getAppContext()));
        }


        this.params = params;
    }

    public CustomJsonRequest(int method, String url, Map<String, String> params,
                             Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;

        if (AppController.getAppContext() != null) {
            params.put("device_id", DealPreferences.getPushRegistrationId(AppController.getAppContext()));
            params.put("loginuser_id", Utils.getUserId(AppController.getAppContext()));
        }

        this.params = params;
    }


    @Override
    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        return params;
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub


        try {
            if (response.has("another")) {
//                if (!response.getString("another").equals("1")) {
//                    listener.onResponse(response);
//                }
                UserDTO userDTO = DealPreferences.getObjectFromPref(AppController.getAppContext(), Constant.USER_INFO);
                userDTO = null;
                DealPreferences.putObjectIntoPref(AppController.getAppContext(),
                        userDTO, Constant.USER_INFO);
                SessionManager.logoutUser(AppController.getAppContext());

            } else {

                listener.onResponse(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}