package com.deal.exap;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.login.OrgSplashActivity;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.android.gcm.GCMBaseIntentService;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.deal.exap.CommonUtilities.SENDER_ID;
import static com.deal.exap.CommonUtilities.displayMessage;


public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        DealPreferences.setPushRegistrationId(context, registrationId);
        displayMessage(context, "Your device registred with GCM");
        // Log.d("NAME", MainActivity.name);
        // ServerUtilities.register(context, MainActivity.name,
        //	MainActivity.email, registrationId);
    }

    /**
     * Method called on device un registred
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        //ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     */
    @Override
    protected void onMessage(Context context, Intent intent) {

        String message = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
        try {
            String url = intent.getExtras().getString(CommonUtilities.EXTRA_URL);

            if (url != null && !url.equalsIgnoreCase("")) {
                sendURL(context, url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Received message :" + message);


        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    private void sendURL(Context mContext, String url) {
        try {
            new Thread(new UrlSendHandler(mContext, url)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class UrlSendHandler implements Runnable {
        private Context mContext;
        private String url;


        public UrlSendHandler(Context mContext, String url) {
            this.mContext = mContext;
            this.url = url;
        }

        @Override
        public void run() {
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //Toast.makeText(mContext, "Successful", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        //Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
    }

    /**
     * Method called on receiving a deleted message
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on Error
     */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context,
                getString(R.string.gcm_recoverable_error, errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void generateNotification(Context context, String message) {
        int icon = R.mipmap.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // Notification notification = new Notification(icon, message, when);

        String title = message;

        Intent notificationIntent = new Intent(context, OrgSplashActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        //notification.setLatestEventInfo(context, title, message, intent);
        //notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        //  notification.defaults |= Notification.DEFAULT_SOUND;

        // Vibrate if vibrate is enabled
        //  notification.defaults |= Notification.DEFAULT_VIBRATE;


        Notification notification = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            notification = new Notification();
            notification.icon = R.mipmap.ic_launcher;
            try {
                Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo",
                        Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                deprecatedMethod.invoke(notification, context, title, message, intent);
            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                Log.w(TAG, "Method not found", e);
            }
        } else {
            // Use new API
            Notification.Builder builder = new Notification.Builder(context)
                    .setContentIntent(intent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title).setContentText(message);
            notification = builder.build();
        }

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        //notification.defaults |= Notification.DEFAULT_SOUND;
        notification.sound = Uri.parse("android.resource://"
                + context.getPackageName() + "/" + R.raw.notification);
        // Vibrate if vibrate is enabled
        notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;

        notificationManager.notify(0, notification);

    }

}