package com.deal.exap.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class WebServiceCaller extends AsyncTask<Object, Void, String> {

	private Context context;
	public ProgressDialog pdialog;
	private WebServiceListener callback;
	private String url;
	private String msg;
	private int requestCode, resultCode;
	private boolean showLoading;
	private boolean isCancelable = true;
	private static final String USER_AGENT = "Mozilla/5.0";
	private HashMap<String, String> params;
	/**
	 * @param context
	 *            Context of activity.
	 * @param callback
	 *            instance of WebServiceListener listner
	 * @param url
	 *            Webservice url on which you are requesting
	 * @param params
	 *            json request parameter.
	 * @param requestCode
	 */
	public WebServiceCaller(Context context, WebServiceListener callback,
			String url, HashMap<String, String> params, int requestCode) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.callback = callback;
		this.url = url;
		this.requestCode = requestCode;
		this.params = params;
		this.showLoading = true;
	}

	/**
	 * @param context
	 *            Context of activity.
	 * @param callback
	 *            instance of WebServiceListener listner
	 * @param url
	 *            Webservice url on which you are requesting
	 * @param params
	 *            json request parameter.
	 * @param requestCode
	 * @param isCancleable
	 * @param showLoading
	 *            true if you want to show loading dialog else false
	 */
	public WebServiceCaller(Context context, WebServiceListener callback,
			String url, HashMap<String, String> params, int requestCode, boolean isCancleable,
			boolean showLoading) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.callback = callback;
		this.url = url;
		this.requestCode = requestCode;
		this.params = params;
		this.showLoading = true;
		this.isCancelable = isCancleable;
		this.showLoading = showLoading;
	}

	/**
	 * @param context
	 *            Context of activity.
	 * @param callback
	 *            instance of WebServiceListener listner
	 * @param url
	 *            Webservice url on which you are requesting
	 * @param params
	 *            json request parameter.
	 * @param requestCode
	 * @param showLoading
	 *            if true then progress bar will appear.
	 */
	public WebServiceCaller(Context context, WebServiceListener callback,
			String url, HashMap<String, String> params, int requestCode, boolean showLoading) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.callback = callback;
		this.url = url;
		this.requestCode = requestCode;
		this.params = params;
		this.showLoading = showLoading;
	}

	/**
	 * @param context
	 *            Context of activity.
	 * @param callback
	 *            instance of WebServiceListener listner
	 * @param url
	 *            Webservice url on which you are requesting
	 * @param params
	 *            json request parameter.
	 * @param requestCode
	 * @param showLoading
	 *            if true then progress bar will appear.
	 * @param msg
	 *            message to show in loading bar.
	 */
	public WebServiceCaller(Context context, WebServiceListener callback,
			String url, HashMap<String, String> params, int requestCode, boolean showLoading,
			String msg) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.callback = callback;
		this.url = url;
		this.requestCode = requestCode;
		this.params = params;
		this.showLoading = showLoading;
		this.msg = msg;
	}


	@Override
	protected void onPreExecute() {
		if (showLoading) {
			pdialog = new ProgressDialog(context);
			if (msg == null)
				pdialog.setMessage("Loading....");
			else
				pdialog.setMessage(msg);
			pdialog.setIndeterminate(true);
			pdialog.setCancelable(false);
			pdialog.show();
		}
	}

	@Override
	protected String doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		return CallPostService(url, params);
	}

	@Override
	protected void onPostExecute(String result) {
		if (pdialog != null && pdialog.isShowing())
			pdialog.dismiss();
		callback.onTaskComplete(result, requestCode, resultCode);
	}

	public static String JSONTokener(String in) {
		// consume an optional byte order mark (BOM) if it exists
		if (in != null && in.startsWith("\ufeff")) {
			in = in.substring(1);
		}
		return in;
	}

	public String CallPostService(String url, HashMap<String, String> param) {
		String result = "";
		try {
			String parameters = urlEncodeUTF8(param);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);

			// For POST only - START
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			if(param!=null)
				os.write(parameters.getBytes());
			os.flush();
			os.close();
			// For POST only - END

			int responseCode = con.getResponseCode();
			System.out.println("POST Response Code :: " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) { //success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				result = response.toString();
				System.out.println("Response :: " + result);
				in.close();

				// print result
				System.out.println(response.toString());
			} else {
				System.out.println("POST request not worked");
				resultCode = Constant.RESULT_EXCEPTION;
				return result;
			}
		}catch (Exception e){
			e.printStackTrace();
			Log.d("http resp error", e.toString());
			resultCode = Constant.RESULT_EXCEPTION;
			return e.toString();
		}
		resultCode = Constant.RESULT_OK;
		return result;
	}

	public String CallPostService(String url, String param) {
		String result = "";
		try {
			String parameters = urlEncodeUTF8(param);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencode");

			// For POST only - START
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			if(param!=null)
				os.write(parameters.getBytes());
			os.flush();
			os.close();
			// For POST only - END

			int responseCode = con.getResponseCode();
			System.out.println("POST Response Code :: " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) { //success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				result = response.toString();
				System.out.println("Response :: " + result);
				in.close();

				// print result
				System.out.println(response.toString());
			} else {
				System.out.println("POST request not worked");
				resultCode = Constant.RESULT_EXCEPTION;
				return result;
			}
		}catch (Exception e){
			e.printStackTrace();
			Log.d("http resp error", e.toString());
			resultCode = Constant.RESULT_EXCEPTION;
			return e.toString();
		}
		resultCode = Constant.RESULT_OK;
		return result;
	}

	static String urlEncodeUTF8(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException(e);
		}
	}
	static String urlEncodeUTF8(Map<?,?> map) {
		StringBuilder sb = new StringBuilder();
		if(map!=null &&  map.size()>0) {
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append(String.format("%s=%s",
						urlEncodeUTF8(entry.getKey().toString()),
						urlEncodeUTF8(entry.getValue().toString())
				));
			}
		}
		return sb.toString();
	}
}
