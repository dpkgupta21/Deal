package com.deal.exap.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;

import com.deal.exap.model.UserDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author admin
 *
 */
public class Utils {

	public static String phNum = "";

	public static String imgType[]={"smallthumb.jpg","small.jpg","medium.jpg","large.jpg"};

	public static AlertDialog showDialog(Context ctx, String title, String msg,
			String btn1, String btn2,
			OnClickListener listener1,
			OnClickListener listener2) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton(btn1, listener1);
		if (btn2 != null)
			builder.setNegativeButton(btn2, listener2);

		AlertDialog alert = builder.create();
		alert.show();
		return alert;

	}

	public static AlertDialog showDialog(Context ctx, String title, String msg,
			String btn1, String btn2, OnClickListener listener) {

		return showDialog(ctx, title, msg, btn1, btn2, listener,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						dialog.dismiss();
					}
				});

	}

	public static AlertDialog showDialog(Context ctx, String title, String msg,
			OnClickListener listener) {

		return showDialog(ctx, title, msg, "Ok", null, listener, null);
	}

	public static AlertDialog showDialog(Context ctx, String title, String msg) {

		return showDialog(ctx, title, msg,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						dialog.dismiss();
					}
				});

	}

	public static void showNoNetworkDialog(Context ctx) {

		showDialog(ctx, "No Network Connection",
				"Internet is not available. Please check your network connection.")
				.show();
	}

	public static void showExceptionDialog(Context ctx) {

		showDialog(ctx, "Error",
				"Some Error occured. Please try later.")
				.show();
	}

	public static void showFailDialog(final Activity ctx) {

		showDialog(ctx, "Error",
				"Failed to check the subscription.", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						ctx.finish();
					}
				})
				.show();
	}

	public static final boolean isOnline(Context context)
	{

		ConnectivityManager conMgr = (ConnectivityManager)context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conMgr.getActiveNetworkInfo() != null

				&& conMgr.getActiveNetworkInfo().isAvailable()

				&& conMgr.getActiveNetworkInfo().isConnected())
			return true;
		return false;
	}

	public static boolean deleteDir(File dir) {
	    if (dir != null && dir.isDirectory()) {
	       String[] children = dir.list();
	       Log.d("file", dir.list().length+dir.getAbsolutePath());
	       for (int i = 0; i < children.length; i++) {
	          boolean success = deleteDir(new File(dir, children[i]));
	          if (!success) {
	             return false;
	          }
	       }
	    }
	    // The directory is now empty so delete it
	    return dir.delete();
	}

	public static boolean isValidEmail(String email) {

		String emailExp = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,10}$";
		Pattern pattern = Pattern.compile(emailExp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean isNumeric(String number) {

		String numExp = "^[-+]?[0-9]*\\.?[0-9]+$";
		Pattern pattern = Pattern.compile(numExp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(number);
		return matcher.matches();
	}

	public static double getDoubleFromString(String number) {

		if(number==null)
			return 0.0;
		if(number.equals(""))
			return 0.0;
		String numExp = "^[-+]?[0-9]*\\.?[0-9]+$";
		Pattern pattern = Pattern.compile(numExp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(number);
		boolean match =matcher.matches();
		if(match)
			return Double.parseDouble(number);
		else
			return 0.0;
	}

	public static final void hideKeyboard(Activity ctx) {

		if (ctx.getCurrentFocus() != null) {
			InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(ctx.getCurrentFocus().getWindowToken(), 0);
		}
	}

	public static final void showKeyboard(Activity ctx) {

		if (ctx.getCurrentFocus() != null) {
			InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
		}
	}

	public static final void dialNumber(final Activity act, String number) {

		Intent call = new Intent(Intent.ACTION_DIAL);
		call.setData(Uri.parse("tel:" + number));
		act.startActivity(call);
	}

	public static final void makeCall(final Activity act, String number) {

		String num = number.replace(" ", "").replace("-", "");
		String digits = "0123456789";
		phNum = "";
		for (int i = 0; i < num.length(); i++) {
			if (digits.contains(num.charAt(i) + "")) {
				phNum += num.charAt(i);
			}
		}

		Log.d("phone", number + "=" + phNum);

		Utils.showDialog(act, "Call", "Call " + phNum, "Ok", "Cancel",
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Intent call = new Intent(Intent.ACTION_CALL);
						call.setData(Uri.parse("tel:" + phNum));
						act.startActivity(call);
					}
				}).show();
	}

	public static final void sendMessage(final Activity act, String phone) {

		String num = phone.replace(" ", "").replace("-", "");
		String digits = "0123456789";
		phNum = "";
		for (int i = 0; i < num.length(); i++) {
			if (digits.contains(num.charAt(i) + "")) {
				phNum += num.charAt(i);
			} else
				break;
		}

		Intent msg = new Intent(Intent.ACTION_SENDTO);
		msg.setData(Uri.parse("smsto:" + phone.trim()));
		act.startActivity(msg);

	}

	public static final void sendEmail(final Activity act, String email,
			String subject) {

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/html");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);

		act.startActivity(Intent.createChooser(intent, "Send Email"));
	}

	public static final void sendEmail(final Activity act, String email,
			String subject, String body) {

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/html");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, body);
		act.startActivity(Intent.createChooser(intent, "Send Email"));
	}

	public static void copyFile(File src, File dst) {

		try {
			if (!dst.exists())
				dst.createNewFile();
			FileInputStream in = new FileInputStream(src);
			FileOutputStream out = new FileOutputStream(dst);

			int size = (int) src.length();
			byte[] buf = new byte[size];
			in.read(buf);
			out.write(buf);
			out.flush();
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createNoMediaFile(File dir) {

		try {
			File f = new File(dir, ".nomedia");
			if (!f.exists())
				f.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	public static Typeface getTypeFace(Context ctx) {
		return Typeface.createFromAsset(ctx.getAssets(),
				"GOTHIC.TTF");
	}

	public static Typeface getBoldTypeFace(Context ctx) {
		return Typeface.createFromAsset(ctx.getAssets(),
				"GOTHICB.TTF");
	}

	public static String getRealPathFromURI(Uri contentUri, Activity act) {
		  String[] proj = { MediaStore.Images.Media.DATA };
		  Cursor cursor = act.managedQuery(contentUri, proj, null, null, null);

		  if (cursor == null)
		   return null;

		  int column_index = cursor
		    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		  cursor.moveToFirst();
		  return cursor.getString(column_index);
		 }

	public static final Bitmap getCompressedBm(byte b[], int w,
			int h)
	{

		try
		{
			final Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(b, 0, b.length, options);
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > h || width > w)
			{
				final int heightRatio = Math.round((float) height / (float) h);
		        final int widthRatio = Math.round((float) width / (float) w);

		        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

			}
			options.inSampleSize = inSampleSize;
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeByteArray(b, 0, b.length, options);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;

	}

	public static final Bitmap getCompressedBm(File file, int w, int h) {

		try {
			final Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file.getAbsolutePath(), options);
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > h || width > w) {
				final int heightRatio = Math.round((float) height / (float) h);
				final int widthRatio = Math.round((float) width / (float) w);

				inSampleSize = heightRatio < widthRatio ? heightRatio
						: widthRatio;

			}
			options.inSampleSize = inSampleSize;
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap getOrientationFixedImage(File f, int w, int h) {

		try {
			ExifInterface exif = new ExifInterface(f.getPath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			int angle = 0;

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				angle = 90;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				angle = 180;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				angle = 270;
			}

			Matrix mat = new Matrix();
			mat.postRotate(angle);

			Bitmap bmp = Utils.getCompressedBm(f, w, h);
			Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
					bmp.getHeight(), mat, true);
			return correctBmp;
		} catch (IOException e) {
			Log.w("TAG", "-- Error in setting image");
		} catch (OutOfMemoryError oom) {
			Log.w("TAG", "-- OOM Error in setting image");
		}
		return Utils.getCompressedBm(f, w, h);
	}

	public static Bitmap downLoadImage(String urlStr) {

		if (!URLUtil.isValidUrl(urlStr))
			return null;
		InputStream is = null;
		Bitmap bmp = null;
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			conn.connect();
			is = conn.getInputStream();

			Options opt = new Options();
			opt.inScaled = true;
			opt.inDither = true;

			opt.inTempStorage = new byte[16000];

			bmp = BitmapFactory.decodeStream(is, null, opt);

			if (is != null)
				is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bmp;
	}


	 public static void CopyStream(InputStream is, OutputStream os)
	    {
	        final int buffer_size=1024;
	        try
	        {
	            byte[] bytes=new byte[buffer_size];
	            for(;;)
	            {
	              int count=is.read(bytes, 0, buffer_size);
	              if(count==-1)
	                  break;
	              os.write(bytes, 0, count);
	            }
	        }
	        catch(Exception ex){}
	    }

	 public static int resolveBitmapOrientation(File bitmapFile) throws IOException {
	        ExifInterface exif = null;
	        exif = new ExifInterface(bitmapFile.getAbsolutePath());

	        return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
	    }

	 public static Bitmap applyOrientation(Bitmap bitmap, int orientation) {
	        int rotate = 0;
	        switch (orientation) {
	            case ExifInterface.ORIENTATION_ROTATE_270:
	                rotate = 270;
	                break;
	            case ExifInterface.ORIENTATION_ROTATE_180:
	                rotate = 180;
	                break;
	            case ExifInterface.ORIENTATION_ROTATE_90:
	                rotate = 0;
	                break;
	            default:
	                return bitmap;
	        }

	        int w = bitmap.getWidth();
	        int h = bitmap.getHeight();
	        Matrix mtx = new Matrix();
	        mtx.postRotate(rotate);
	        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	    }

	 public static void writeBitmapToFile(String tempphoto, Bitmap bmp){
		  FileOutputStream fOut;
		    try {
		        fOut = new FileOutputStream(tempphoto);
		        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		        fOut.flush();
		        fOut.close();

		    } catch (FileNotFoundException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
	 }

	 public static String convertStringToFormat(String inputDate){
		 Date date;
			SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy hh:mmaa");
		    try {
				date = format.parse(inputDate);
				format = new SimpleDateFormat("dd MMMMM yyyy");
				return format.format(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
	 }

	public static boolean validateEditText(EditText et, int requestCode, String msg){
		if(et.getText().toString().trim().equals("")){
			et.setError("Required Field");
			return false;
		}
		else{
			if(requestCode==101){
				if(et.getText().toString().trim().equals("")){
					et.setError(msg);
					return false;
				}else
					return true;
			}
			else if(requestCode==102){
				if(!Utils.isNumeric(et.getText().toString().trim())){
					et.setError(msg);
					return false;
				}else
					return true;
			}else if(requestCode==103){
				if(!Utils.isValidEmail(et.getText().toString().trim())){
					et.setError(msg);
					return false;
				}else
					return true;
			}
		}
		return true;
	}
	public static boolean validatePassword(EditText password, EditText confirmPassword){
		boolean flag = true;
		if(password.getText().toString().trim().equals("")){
			password.setError("Required Field");
			flag = false;
		}
		if(confirmPassword.getText().toString().trim().equals("")){
			confirmPassword.setError("Required Field");
			flag = false;
		}
		if(!password.getText().toString().equals(confirmPassword.getText().toString())){
			confirmPassword.setError("Password not match");
			flag =false;
		}
		return flag;
	}


	/**
	 * This method converts device specific pixels to density independent pixels.
	 *
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;
	}


	public static String getCurrentDateTime(){
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy h:mm:ss a");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getCurrentDate(){
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static Calendar getDateFromString(String strDate){
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy h:mm:ss a");
		Calendar c = Calendar.getInstance();
		try {
			Date d = dateFormat.parse(strDate);
			c.setTime(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return c;
		}
		return c;
	}

	public static boolean isFromDateGreater(String fromDate, String toDate){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        	Date date1 = sdf.parse(fromDate);
        	Date date2 = sdf.parse(toDate);
        	if(date2.after(date1)){
        		return true;
        	}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	public static String getTime(int hr,int min) {
	    Time tme = new Time(hr,min,0);//seconds by default set to zero
	    Format formatter;
	    formatter = new SimpleDateFormat("h:mm:ss a");
	    return formatter.format(tme);
	}

	public static String checkDigit(int number) {
		return number <= 9 ? "0" + number : String.valueOf(number);
	}

	public static void vibrateMe(Context context){
		Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 500 milliseconds
		v.vibrate(100);
	}

	public static double roundTo2Decimal(double number){
		return Math.round(number * 100.0) / 100.0;
	}

	public static String roundTo2Decimal2(String number){
		try{
			double val = Double.parseDouble(number);
			DecimalFormat df=new DecimalFormat("0.0");

			return df.format(val);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "0.0";
	}

	public static String getDateFFromString(String strDate){
		DateFormat fromFormat = new SimpleDateFormat("MM-dd-yyyy");
		DateFormat toFormat = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			Date d = fromFormat.parse(strDate);
			return toFormat.format(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public static String addDayInDate(String strDate, int day){
		DateFormat fromFormat = new SimpleDateFormat("MM-dd-yyyy");
		DateFormat toFormat = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime( fromFormat.parse(strDate));
			cal.add(Calendar.DAY_OF_MONTH, day);
			return toFormat.format(cal.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public static String addDayInDate(Date date, int day){
		DateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_MONTH, day);
			return toFormat.format(cal.getTime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public static String getStringFromPref(Context context, String key){
		return context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE).getString(key, null);
	}

	public static void putStringIntoPref(Context context, String key, String value){
		context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE).edit().putString(key, value).commit();
	}

	public static void putIntIntoPref(Context context, String key, int value){
		context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
	}

	public static void putBooleanIntoPref(Context context, String key, boolean value){
		context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
	}

	public static void putLongIntoPref(Context context, String key, long value){
		context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
	}

	public static int getIntFromPref(Context context, String key){
		return context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE).getInt(key, 0);
	}

	public static boolean getBooleanFromPref(Context context, String key, boolean defValue){
		return context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE).getBoolean(key, defValue);
	}

	public static long getLongFromPref(Context context, String key){
		return context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE).getLong(key, 0);
	}

	public static boolean isRequiredEditText(EditText et){
		if(et.getText().toString().trim().equals("")){
			return false;
		}
		return true;
	}
	public static boolean isRequiredEditText(Button et){
		if(et.getText().toString().trim().equals("")){
			return false;
		}
		return true;
	}

	public static long getDateTimeStamp(String date){
		DateFormat fromFormat = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date d = fromFormat.parse(date);
			return d.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static String getDateStringFromTimestamp(long timestamp){
		 Timestamp stamp = new Timestamp(timestamp);
		  Date d = new Date(stamp.getTime());
		  DateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
		  return toFormat.format(d);
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int density) {
		int cw=bitmap.getWidth();
		int ch=bitmap.getHeight();
	    Bitmap output = Bitmap.createBitmap(cw,ch, Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    paint.setStyle(Style.FILL);
	    Rect rect = new Rect(0, 0, cw, ch);
	    RectF rectF = new RectF(rect);
	    final float roundPx = 10*density;

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);

	    return output;
	  }

	/*public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int density) {
		int cw=bitmap.getWidth();
		int ch=bitmap.getHeight();
	    Bitmap output = Bitmap.createBitmap(cw,ch, Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    paint.setStyle(Style.FILL);
	    Rect rect = new Rect(0, 0, cw, ch);
	    RectF rectF = new RectF(rect);
	    final float roundPx = 10*density;

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	   
	    return output;
	  }*/
	
	/* public static Intent getOpenFacebookIntent(Context context) {

	     try {
	      context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
	      return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/258056787682575"));
	     } catch (Exception e) {
	      return new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.facebook_page)));
	     }
	  }
	 */



	 public static String getGMT(int hours,int mins){
		 Log.d("time", " "+hours+" : "+mins);
		 String gmtTime="";


			 Calendar cal = Calendar.getInstance();
				 cal.set(Calendar.HOUR_OF_DAY, hours);
				 cal.set(Calendar.MINUTE,mins);


			 Log.d("time value ", " "+ cal.get(Calendar.AM_PM)+"  cal day "+cal.get(Calendar.HOUR_OF_DAY)+" date "+cal.get(Calendar.MINUTE));


			TimeZone timezone = TimeZone.getTimeZone("GMT");
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

			Log.d("time current ", " "+gmtTime+" Date "+cal.get(Calendar.DATE) +"Cal time "+cal.get(Calendar.HOUR_OF_DAY));
			df.setTimeZone(timezone);
			gmtTime = df.format(new Date(cal.getTimeInMillis()));

		 Log.d("time", " "+gmtTime);
		 return gmtTime;
	 }

	 public static long getGMTFromTimeStamp(long timestamp){

		Calendar gmtCal = Calendar.getInstance();
		gmtCal.setTimeInMillis(timestamp);
		gmtCal.setTimeZone(TimeZone.getTimeZone("GMT"));
		return gmtCal.getTimeInMillis();
	 }

	 public static String convertGMTToDefault(String time){
		 String gmtTime="";
		 String t[] = time.split(":");
		 if(t.length>1){
			 Calendar cal = Calendar.getInstance();
			 cal.setTimeZone(TimeZone.getTimeZone("GMT"));
			 cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t[0]));
			 cal.set(Calendar.MINUTE, Integer.parseInt(t[1]));

			TimeZone timezone = TimeZone.getDefault();
			SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
			df.setTimeZone(timezone);
			// df.setTimeZone(TimeZone.getDefault());

			gmtTime = df.format(new Date(cal.getTimeInMillis()));
		 }
		 return gmtTime;
	 }

	 public static String convertGMTToDefault24(String time){
		 String gmtTime="";
		 String t[] = time.split(":");
		 if(t.length>1){
			 Calendar cal = Calendar.getInstance();
			 cal.setTimeZone(TimeZone.getTimeZone("GMT"));
			 cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t[0]));
			 cal.set(Calendar.MINUTE, Integer.parseInt(t[1]));
			 /*if(t[2].equalsIgnoreCase("am")){
				 cal.set(Calendar.AM_PM, Calendar.AM);
			 }else{
				 cal.set(Calendar.AM_PM, Calendar.PM);
			 }*/

			TimeZone timezone = TimeZone.getDefault();
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			df.setTimeZone(timezone);
			// df.setTimeZone(TimeZone.getDefault());

			gmtTime = df.format(new Date(cal.getTimeInMillis()));
		 }
		 return gmtTime;
	 }

	 public static long convertDateToGMT(String datestr){
			try{
				DateFormat fromFormat = new SimpleDateFormat("MM/dd/yyyy");
				Date d = fromFormat.parse(datestr);

				TimeZone gmtTime = TimeZone.getTimeZone("GMT");
				fromFormat.setTimeZone(gmtTime);
				System.out.println("DateTime in GMT : " + fromFormat.format(d));
				return d.getTime();
			}catch(Exception e){
				e.printStackTrace();
			}
			return 0;
		}

		public static String convertGMTToDate(long timestamp){
			try{
				Date d = new Date(timestamp);
				DateFormat fromFormat = new SimpleDateFormat("MM/dd/yyyy");
				String datestr = fromFormat.format(d);
				final DateFormat formatter = DateFormat.getDateTimeInstance();
				formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
				/*final Date timezone = formatter.parse(datestr);
				formatter.setTimeZone(TimeZone.getDefault());
				System.out.println(formatter.format(timezone));*/

				return datestr;
			}catch(Exception e){
				e.printStackTrace();
			}
			return "";
		}
		
	/*public static double distance(double lat1, double lon1, double lat2,
			double lon2, char unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == 'K') {
			dist = dist * 1.609344;
		} else if (unit == 'N') {
			dist = dist * 0.8684;
		}
		return (dist);
	}*/


	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int radius = Math.min(h / 2, w / 2);
		Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Config.ARGB_8888);

		Paint p = new Paint();
		p.setAntiAlias(true);

		Canvas c = new Canvas(output);
		c.drawARGB(0, 0, 0, 0);
		p.setStyle(Style.FILL);

		c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

		p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		c.drawBitmap(bitmap, 4, 4, p);
		/*p.setXfermode(null);                                                
		p.setStyle(Style.STROKE);                                           
		p.setColor(Color.WHITE);                                            
		p.setStrokeWidth(3);                                                
		c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);                */

		return output;
	}


	public static void gotoURL(String url, Context context){
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		context.startActivity(i);
	}

	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

	    // Usually this should be a field rather than a method variable so
	    // that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

	public static void openWebUrl(Context context, String url){
		Intent i = new Intent(Intent.ACTION_VIEW);
    	i.setData(Uri.parse(url));
    	context.startActivity(i);
	}

	/**
	 * Method use to get String date in user readable format
	 * @param c context of activity.
	 * @return string date in dd-MMM-yyyy format.
	 */
	public static String getUserDateFormat(Calendar c){
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String formattedDate = sdf.format(c.getTime());
		return formattedDate;
	}

	public static Calendar getCalendarForUserDate(String date){//2009-09-22
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			return c;
		}catch(Exception e){
			e.printStackTrace();
			return Calendar.getInstance();
		}
	}

	public static String convertDBtoUserDateFormat(String date){//2009-09-22
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(date);

			SimpleDateFormat to = new SimpleDateFormat("MM/dd/yyyy");
			return to.format(d);
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}


	/**
	 * Method use to get orientation fixed bitmap.
	 * @param f file object of any image.
	 * @return
	 */
	public static Bitmap getOrientationFixedImage(File f) {

		try {
			ExifInterface exif = new ExifInterface(f.getPath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			int angle = 0;

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				angle = 90;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				angle = 180;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				angle = 270;
			}

			Matrix mat = new Matrix();
			mat.postRotate(angle);

			Bitmap bmp = Utils.getCompressedBm(f);
			Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
					bmp.getHeight(), mat, true);
			return correctBmp;
		} catch (IOException e) {
			Log.w("TAG", "-- Error in setting image");
		} catch (OutOfMemoryError oom) {
			Log.w("TAG", "-- OOM Error in setting image");
		}
		return Utils.getCompressedBm(f);
	}

	/**
	 * Method use to get commpressed bitmap of any image.
	 * @param file image file which you want to commpress.
	 * @return
	 */
	public static final Bitmap getCompressedBm(File file) {

		try {
			final Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file.getAbsolutePath(), options);
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 2;

			long maxMB = 1024 * 1024 * 2;

			if (file.length() > maxMB) {
				final int heightRatio = Math.round((float) file.length()
						/ maxMB);

				inSampleSize = heightRatio;

			}
			Log.d("Sample Size======================", inSampleSize + "");
			options.inSampleSize = inSampleSize;
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



 	/**
 	 * Method use to get file extention of any file name.
 	 * @param fileName
 	 * @return
 	 */
 	public static String getFileExtention(String fileName){
 		return fileName.substring(fileName.lastIndexOf("."));
 	}


 	public static boolean getWebServiceStatus(JSONObject json){
		try {
			return json.getBoolean("status");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

 	public static String getWebServiceMessage(JSONObject json){

		try {
			return json.getString("message");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "Error";
 	}

 	public static String getResponseInString(String json, String key){
 		try{
 			JSONObject jsonObj = new JSONObject(json);
 			return jsonObj.getJSONObject("response").getString(key);
 		}catch(Exception e){
 			e.printStackTrace();
 			return "error";
 		}
 	}

 	public static String getResponseInArray(String json, String key){
 		try{
			JSONObject jsonObj = new JSONObject(json);
			return jsonObj.getJSONObject(key).getJSONArray("response").toString();
 		}catch(Exception e){
 			e.printStackTrace();
 			return "error";
 		}
 	}

 	public static String getResponseInObject(String json, String key){
 		try{
 			JSONObject jsonObj = new JSONObject(json);
 			return jsonObj.getJSONObject(key).getJSONObject("response").getJSONObject("data").toString();
 		}catch(Exception e){
 			e.printStackTrace();
 			return "error";
 		}
 	}

	public static String getResponseInObject1(String json, String key){
		try{
			JSONObject jsonObj = new JSONObject(json);
			return jsonObj.getJSONObject(key).getJSONObject("response").toString();
		}catch(Exception e){
			e.printStackTrace();
			return "error";
		}
	}

 	/**
     * Method use to play video from web url into video intent
     * @param url string url of video file
     * @param activity
     */
    public static void playVideo(String url, FragmentActivity activity){
    	Uri myUri = Uri.parse(url);
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setDataAndType(myUri, "video/mp4");
    	activity.startActivity(intent);
    }

	/**
	 * This genric method use to put object into preference<br>
	 * How to use<br>
	 * Bean bean = new Bean();<br>
	 * putObjectIntoPref(context,bean,key)
	 * @param context Context of an application
	 * @param e your genric object
	 * @param key String key which is associate with object
	 */
	public static <E> void putObjectIntoPref(Context context, E e, String key) {
		Editor editor = context.getSharedPreferences(Constant.PREF_FILE,
				Context.MODE_PRIVATE).edit();
		try {
			editor.putString(key, ObjectSerializer.serialize(e));
		} catch (IOException exc) {
			exc.printStackTrace();
		}

		editor.commit();

	}

	public static <E> void removeObjectIntoPref(Context context, String key) {
		Editor editor = context.getSharedPreferences(Constant.PREF_FILE,
				Context.MODE_PRIVATE).edit();
		editor.remove(key);
		editor.commit();

	}

	/**
	 * This method is use to get your object from preference.<br>
	 * How to use<br>
	 * Bean bean = getObjectFromPref(context,key);
	 * @param context
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E> E getObjectFromPref(Context context, String key){
		try{
			SharedPreferences pref = context.getSharedPreferences(Constant.PREF_FILE,Context.MODE_PRIVATE);
			return (E) ObjectSerializer.deserialize(pref.getString(key, null));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Method use to get byte array from file.
	 * @param file
	 * @return
	 */
	public static String getByteArrayFromFile(File file) {
		try{
			DataInputStream is = new DataInputStream(new FileInputStream(file));
			byte[] bytes = new byte[(int) file.length()];
			is.readFully(bytes);
			is.close();
			return Base64.encodeBytes(bytes);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
/*		int size = (int) file.length();
		byte[] bytes = new byte[size];
		try {
			BufferedInputStream buf = new BufferedInputStream(
					new FileInputStream(file));
			buf.read(bytes, 0, bytes.length);
			buf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/

	}

	public int getRandomNo(int Low, int High){
		Random r = new Random();
		int R = r.nextInt(High-Low) + Low;
		return R;
	}

	public static String getTwoDfigiString(double number){

	    DecimalFormat df = new DecimalFormat("#.00");
	    String angleFormated = df.format(number);
	    System.out.println(angleFormated); //output 20.30
	    return angleFormated;

	}
	
	/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::                                                                         :*/
	/*::  This routine calculates the distance between two points (given the     :*/
	/*::  latitude/longitude of those points). It is being used to calculate     :*/
	/*::  the distance between two locations using GeoDataSource (TM) prodducts  :*/
	/*::                                                                         :*/
	/*::  Definitions:                                                           :*/
	/*::    South latitudes are negative, east longitudes are positive           :*/
	/*::                                                                         :*/
	/*::  Passed to function:                                                    :*/
	/*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
	/*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
	/*::    unit = the unit you desire for results                               :*/
	/*::           where: 'M' is statute miles (default)                         :*/
	/*::                  'K' is kilometers                                      :*/
	/*::                  'N' is nautical miles                                  :*/
	/*::  Worldwide cities and other features databases with latitude longitude  :*/
	/*::  are available at http://www.geodatasource.com                          :*/
	/*::                                                                         :*/
	/*::  For enquiries, please contact sales@geodatasource.com                  :*/
	/*::                                                                         :*/
	/*::  Official Web site: http://www.geodatasource.com                        :*/
	/*::                                                                         :*/
	/*::           GeoDataSource.com (C) All Rights Reserved 2015                :*/
	/*::                                                                         :*/
	/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

	public static double distance(double lat1, double lon1, double lat2, double lon2) {
	  double theta = lon1 - lon2;
	  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	  dist = Math.acos(dist);
	  dist = rad2deg(dist);
	  dist = dist * 60 * 1.1515;

	  return (dist);
	}


	public static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	public static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	public static String get2DecimalValue(String value){
		if(value == null)
			return "0.00";
		if(value.equals(""))
			return "0.00";
		try {
			return String.format("%.2f", Double.parseDouble(value));
		}catch (Exception e) {
			return "0.00";
		}
	}

	public static void ShowLog(String tag,String response)
	{
		Log.i(tag,response);
	}


	public static String getSelectedLanguage(Context context){

		if (DealPreferences.getAPP_LANG(context).contains(Constant.LANG_ARABIC_CODE)){
			return "era";
		}
		else{
			return "eng";
		}
	}

	public static String getUserId(Context context){
		UserDTO userDTO = Utils.getObjectFromPref(context, Constant.USER_INFO);
		if(userDTO!=null)
			return userDTO.getId();
		else
			return "0";
	}

	public static String getUserName(Context context){
		UserDTO userDTO = Utils.getObjectFromPref(context, Constant.USER_INFO);
		if(userDTO!=null)
			return userDTO.getName();
		else
			return "";
	}


	public static String getUserImage(Context context){
		UserDTO userDTO = Utils.getObjectFromPref(context, Constant.USER_INFO);
		if(userDTO!=null)
			return userDTO.getImage();
		else
			return "";
	}

	public static ProgressDialog createProgeessDialog(Context context, String message, boolean isCancelable){
		ProgressDialog pdialog = new ProgressDialog(context);
		if (message == null)
			pdialog.setMessage("Loading....");
		else
			pdialog.setMessage(message);
		pdialog.setIndeterminate(true);
		pdialog.setCancelable(isCancelable);
		return pdialog;
	}

	public static String loadJSONFromAsset(Context context) {
		String json = null;
		try {
			InputStream is = context.getAssets().open("countrycode.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
	}


	public static ArrayList<String> getYears() {
		ArrayList<String> years = new ArrayList<>();
		int thisYear = Calendar.getInstance().get(Calendar.YEAR);
		years.add("YYYY");
		for (int i = thisYear; i <= 2050; i++) {
			years.add(Integer.toString(i));
		}
		return years;
	}

	public static ArrayList<String> getMonths() {
		ArrayList<String> months = new ArrayList<>();
		months.add("MM");
		months.add("01");
		months.add("02");
		months.add("03");
		months.add("04");
		months.add("05");
		months.add("06");
		months.add("07");
		months.add("08");
		months.add("09");
		months.add("10");
		months.add("11");
		months.add("12");
		return months;
	}

	public static boolean isArebic(Context context){
		if (DealPreferences.getAPP_LANG(context).contains(Constant.LANG_ARABIC_CODE)){
			return true;
		}
		else
			return false;
	}
}
