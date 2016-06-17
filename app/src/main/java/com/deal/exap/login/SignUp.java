package com.deal.exap.login;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.WakeLocker;
import com.deal.exap.camera.CameraChooseDialogFragment;
import com.deal.exap.camera.CameraSelectInterface;
import com.deal.exap.camera.GallerySelectInterface;
import com.deal.exap.model.UserDTO;
import com.deal.exap.navigationdrawer.HomeActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonImageRequest;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.deal.exap.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.deal.exap.CommonUtilities.EXTRA_MESSAGE;
import static com.deal.exap.CommonUtilities.SENDER_ID;

public class SignUp extends BaseActivity {

    private static final String TAG = "SignUp";
    private int CAMERA_REQUEST = 1001;
    private int GALLERY_REQUEST = 1002;
    private ImageLoader image_loader;
    private CameraChooseDialogFragment dFragment;
    private ImageView ivProfile;
    private File f = null;
    private byte[] bitmapdata;
    private AsyncTask<Void, Void, Void> mRegisterTask;
    private Context mContext;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        mContext = SignUp.this;
        init();
        ((Button) findViewById(R.id.btn_signup)).setOnClickListener(signUpClick);
        ((ImageView) findViewById(R.id.img_add_photo)).setOnClickListener(addImageClick);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }


    private void init() {

        setHeader(getString(R.string.profile_header));
        setLeftClick();
        ivProfile = (ImageView) findViewById(R.id.iv_profile);
        setClick(R.id.edt_gender);
        //setClick(R.id.edt_dob);


        String pushRegistrationId = DealPreferences.getPushRegistrationId(SignUp.this);
        if (pushRegistrationId == null || pushRegistrationId.equalsIgnoreCase("")) {
            registrationPushNotification();
        }
    }

    View.OnClickListener signUpClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent i = new Intent(SignUp.this, HomeActivity.class);
//            startActivity(i);

            // CustomAlertDialog.getCustomAlert(SignUp.this).singleButtonAlertDialog(getString(R.string.uname_pwd_not_match), "", "");


            doSignUp();
        }
    };


    View.OnClickListener addImageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showAlertCamera();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_gender:
                showSexDialog();
                break;
            case R.id.edt_dob:
                //showCalendarDialog();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void showSexDialog() {
        final CharSequence[] items = {"Male", "Female"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Gender");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                setViewText(R.id.edt_gender, items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showAlertCamera() {
        try {
            if (dFragment == null) {
                dFragment = new CameraChooseDialogFragment();
            }
            dFragment.setCallBack(cameraSelectInterface, gallerySelectInterface);
            // Show DialogFragment
            FragmentManager fm = getSupportFragmentManager();
            dFragment.show(fm, "Dialog Fragment");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void showCalendarDialog() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        setViewText(R.id.edt_dob, (monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    CameraSelectInterface cameraSelectInterface = new CameraSelectInterface() {
        @Override
        public void startCamera() {
            clickPictureUsingCamera();
        }
    };

    GallerySelectInterface gallerySelectInterface = new GallerySelectInterface() {
        @Override
        public void startGallery() {
            selectImageFromGallery();
        }
    };


    /**
     * This method is used to click image using camera and set the clicked image
     * in round image view.
     */
    private void clickPictureUsingCamera() {
        try {
            Intent cameraIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selectImageFromGallery() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
        } catch (Exception e) {
            Log.i("info", e + "");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            byte[] hash = null;
            if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK
                    && null != data) {
                if (dFragment != null) {
                    dFragment.dismiss();
                    dFragment = null;
                }
                Uri selectedImage = data.getData();
                Log.d("DATA", data.toString());
                Log.d("uri ", selectedImage.toString());

                // setting image in image in profile pic.
//                image_loader
//                        .displayImage(selectedImage.toString(), img_profile);
//
//                Bitmap bitmap = ((BitmapDrawable) img_profile.getDrawable())
//                        .getBitmap();
                Bitmap bitmap = null;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(SignUp.this.getContentResolver(), selectedImage);
                    ivProfile.setImageBitmap(bitmap);

                    f = new File(this.getCacheDir(), "profile.png");
                    f.createNewFile();

//Convert bitmap to byte array
                    Bitmap bitmap1 = bitmap;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.PNG, 70 /*ignored for PNG*/, bos);
                    bitmapdata = bos.toByteArray();

//write the bytes in file
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                // Converting image's bitmap to byte array.
                // ByteArrayOutputStream bos = new ByteArrayOutputStream();
                //   bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
                //hash = bos.toByteArray();

                // converting image's byte array to Base64encoded string
                // imageStringBase64 = Base64.encodeToString(hash, Base64.NO_WRAP);

            } else if (requestCode == CAMERA_REQUEST
                    && resultCode == Activity.RESULT_OK) {
                if (dFragment != null) {
                    dFragment.dismiss();
                    dFragment = null;
                }
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ivProfile.setImageBitmap(photo);

                f = new File(this.getCacheDir(), "profile.png");
                f.createNewFile();

//Convert bitmap to byte array
                Bitmap bitmap1 = photo;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.PNG, 70 /*ignored for PNG*/, bos);
                bitmapdata = bos.toByteArray();

//write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();


                // Converting image's bitmap to byte array.
                //ByteArrayOutputStream bos = new ByteArrayOutputStream();
                //photo.compress(Bitmap.CompressFormat.JPEG, 30, bos);
                //hash = bos.toByteArray();

                // converting image's byte array to Base64encoded string
                // imageStringBase64 = Base64.encodeToString(hash, Base64.NO_WRAP);

            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }


    public void doSignUp() {
        Utils.hideKeyboard(SignUp.this);
        if (validateForm()) {
            if (Utils.isOnline(SignUp.this)) {
                String mobNumber = getIntent().getStringExtra("MOB_NUMBER");
                Map<String, String> params = new HashMap<>();
                params.put("action", Constant.DO_SIGNUP);
                params.put("email", getViewText(R.id.edt_email_id));
                params.put("password", getViewText(R.id.edt_password));
                params.put("device", "android");
                //  params.put("device_id", DealPreferences.getPushRegistrationId(SignUp.this));
                params.put("name", getViewText(R.id.edt_name));
                params.put("gender", getViewText(R.id.edt_gender).equals("Male") ? "M" : "F");
                //params.put("dob", getViewText(R.id.edt_dob));
                params.put("age", getViewText(R.id.edt_dob));
                params.put("confirm_password", getViewText(R.id.edt_confirm_password));
                params.put("mobile", mobNumber != null ? mobNumber : "");

                final ProgressDialog pdialog = Utils.createProgressDialog(SignUp.this, null, false);
                CustomJsonImageRequest postReq = new CustomJsonImageRequest(Request.Method.POST,
                        Constant.SERVICE_BASE_URL, params, f,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(TAG, "Response -> " + response.toString());
                                pdialog.dismiss();
                                try {
                                    if (response.getString("status").equalsIgnoreCase("2")) {
                                        Log.i("info", "" + response);
                                        finish();
                                        Intent intent = new Intent(SignUp.this, SplashScreen.class);
                                        startActivity(intent);
                                    } else if (response.getString("status").equalsIgnoreCase("1")) {
                                        UserDTO userDTO = new Gson().fromJson(response.getJSONObject("user").
                                                toString(), UserDTO.class);
                                        userDTO.setUserType(Constant.REGISTER);
                                        DealPreferences.putObjectIntoPref(mContext, userDTO, Constant.USER_INFO);

                                        DealPreferences.setIsShowSurveyAfterLogin(
                                                mContext, true);

                                        Intent intent = new Intent(mContext, HomeActivity.class);
                                        intent.putExtra("fragmentName", mContext.getString(R.string.interest_screen_title));
                                        startActivity(intent);
                                    } else {
                                        Utils.showDialog(mContext, getString(R.string.message), Utils.getWebServiceMessage(response));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pdialog.dismiss();
                        Utils.showExceptionDialog(SignUp.this);
                    }
                });
                pdialog.show();
                Log.i("info", postReq.toString());
                AppController.getInstance().getRequestQueue().add(postReq);
                postReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            } else {
                Utils.showNoNetworkDialog(SignUp.this);
            }
        }
    }

    public boolean validateForm() {


//        if(f==null)
//        {
//            Utils.showDialog(SignUp.this, "Message", "Please enter image");
//        }
//        else
        try {
            if (getViewText(R.id.edt_name).equals("")) {
                Utils.showDialog(SignUp.this, getString(R.string.message), getString(R.string.alert_please_enter_name));
                return false;
            } else if (getViewText(R.id.edt_dob).equals("")) {
                Utils.showDialog(SignUp.this, getString(R.string.message), getString(R.string.alert_please_enter_age));
                return false;
            } else if (Utils.isValidAge(getViewText(R.id.edt_dob)) == null ||
                    Utils.isValidAge(getViewText(R.id.edt_dob)).equalsIgnoreCase("")) {
                Utils.showDialog(SignUp.this, getString(R.string.message), getString(R.string.alert_please_enter_valid_age));
                return false;
            } else if (getViewText(R.id.edt_gender).equals("")) {
                Utils.showDialog(SignUp.this, getString(R.string.message), getString(R.string.alert_please_enter_gender));
                return false;
            } else if (getViewText(R.id.edt_email_id).equals("")) {
                Utils.showDialog(SignUp.this, getString(R.string.message), getString(R.string.alert_please_enter_email_id));
                return false;
            } else if (!Utils.isValidEmail(getViewText(R.id.edt_email_id))) {
                Utils.showDialog(SignUp.this, getString(R.string.message), getString(R.string.alert_please_valid_email_id));
                return false;
            } else if (getViewText(R.id.edt_password).equals("")) {
                Utils.showDialog(SignUp.this, getString(R.string.message), getString(R.string.please_enter_password));
                return false;
            } else if (getViewText(R.id.edt_confirm_password).equals("")) {
                Utils.showDialog(SignUp.this, getString(R.string.message), getString(R.string.alert_please_confirm_password));
                return false;
            } else if (!getViewText(R.id.edt_password).equals(getViewText(R.id.edt_confirm_password))) {
                Utils.showDialog(SignUp.this, getString(R.string.message), getString(R.string.alert_password_not_match));
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    // For Push notification
    private void registrationPushNotification() {
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(SignUp.this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(SignUp.this);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        final String regId = GCMRegistrar
                .getRegistrationId(SignUp.this);

        DealPreferences.setPushRegistrationId(SignUp.this, regId);
        Log.i("info", "RegId :" + regId);
        // Check if regid already presents
        if (regId.equals("")) {
            Log.i("info", "RegId :" + regId);
            // Registration is not present, register now with GCM
            GCMRegistrar.register(SignUp.this, SENDER_ID);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar
                    .isRegisteredOnServer(SignUp.this)) {
                // Skips registration.
                Log.i("info", "Already registered with GCM");
            } else {
                Log.i("info", "Not registered with GCM");
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }

    /**
     * Receiving push messages
     */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message depending upon your app
             * requirement For now i am just displaying it on the screen
             * */

            // Showing received message

            // Releasing wake lock
            WakeLocker.release();
        }
    };


    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(SignUp.this);
        } catch (Exception e) {
            Utils.ShowLog(TAG, "UnRegister Receiver Error " + e.getMessage());
        }
        super.onDestroy();
    }


}
