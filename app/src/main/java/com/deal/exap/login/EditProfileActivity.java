package com.deal.exap.login;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.camera.CameraChooseDialogFragment;
import com.deal.exap.camera.CameraSelectInterface;
import com.deal.exap.camera.GallerySelectInterface;
import com.deal.exap.gps.GPSTracker;
import com.deal.exap.locationselection.LocationSelectionActivity;
import com.deal.exap.model.UserDTO;
import com.deal.exap.navigationdrawer.HomeActivity;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonImageRequest;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends BaseActivity {

    private static final String TAG = "EditProfile";

    private UserDTO userDTO;
    private DisplayImageOptions options;
    private int CAMERA_REQUEST = 1001;
    private int GALLERY_REQUEST = 1002;
    private int LOCATION_RESULT_REQUEST = 1003;
    private CameraChooseDialogFragment dFragment;
    private File f;
    private byte[] bitmapdata;
    private ImageView profile;
    //private String currentAddress;
    private GPSTracker gpsTracker;

    private Activity mActivity;

    //private ImageLoader imageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mActivity = EditProfileActivity.this;

        init();
        gpsTracker = new GPSTracker(mActivity);
//        try {
//            currentAddress = Utils.getAddress(gpsTracker.getLatitude(), gpsTracker.getLongitude(),
//                    mActivity);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (getIntent().getStringExtra("address") != null) {
//            setEditText(R.id.txt_location, getIntent().getStringExtra("address"));
//        }
    }


    private void init() {
        userDTO = DealPreferences.getObjectFromPref(this, Constant.USER_INFO);
        setHeader(getString(R.string.edit_profile_header));
        setLeftClick();

        //setClick(R.id.et_age);
        setClick(R.id.et_sex);
        setClick(R.id.txt_image_change);
        setClick(R.id.btn_edit_profile);
        setClick(R.id.txt_location);
        if (userDTO.getName() != null && !userDTO.getName().equalsIgnoreCase(""))
            setTextViewText(R.id.txt_name, userDTO.getName());
        else
            setTextViewText(R.id.txt_name, "");


        if (userDTO.getEmail() != null && !userDTO.getEmail().equalsIgnoreCase(""))
            setTextViewText(R.id.edt_email_id, userDTO.getEmail());
        else
            setTextViewText(R.id.edt_email_id, "");


        if (userDTO.getAge() != null && !userDTO.getAge().equalsIgnoreCase(""))
            setTextViewText(R.id.et_age, userDTO.getAge());
        else
            setTextViewText(R.id.et_age, "");
        if (userDTO.getGender() != null && !userDTO.getGender().equalsIgnoreCase("")) {
            if (userDTO.getGender().equalsIgnoreCase("M"))
                setTextViewText(R.id.et_sex, "Male");
            else
                setTextViewText(R.id.et_sex, "Female");
        } else {
            setTextViewText(R.id.et_sex, "");
        }


        if (userDTO.getLocation() != null && !userDTO.getLocation().equalsIgnoreCase(""))
            setTextViewText(R.id.txt_location, userDTO.getLocation());
        else
            setTextViewText(R.id.txt_location, "");


        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(false)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.avtar)
                .showImageOnFail(R.drawable.avtar)
                .showImageForEmptyUri(R.drawable.avtar)
                .build();

        profile = (ImageView) findViewById(R.id.profile);

        ImageLoader.getInstance().displayImage(userDTO.getImage(), profile,
                options);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.et_sex:
                showSexDialog();
                break;
            case R.id.et_age:
                //showCalendarDialog();
                break;

            case R.id.btn_edit_profile:
                doEditProfile();

                break;

            case R.id.txt_image_change:
                showAlertCamera();
                break;

            case R.id.txt_location:
                openLocationActivity();
                break;
        }
    }

    /**
     * this method opens the LocalSelectionActivity to change the location.
     *
     * @see LocationSelectionActivity
     */
    private void openLocationActivity() {
        try {
//            if (TextUtils.isEmpty(currentAddress)) {
//                currentAddress = Utils.getAddress(gpsTracker.getLatitude(), gpsTracker.getLongitude(),
//                        mActivity);
//                Toast.makeText(mActivity, "Getting current address..", Toast.LENGTH_SHORT).show();
//            } else {
            Intent intent = new Intent(mActivity, LocationSelectionActivity.class);
            //intent.putExtra("currentAddress", currentAddress);
            startActivityForResult(intent, LOCATION_RESULT_REQUEST);
            //}
        } catch (Exception e) {
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
                        setViewText(R.id.et_age, (monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    public void showSexDialog() {
        final CharSequence[] items = {"Male", "Female"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Gender");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                setViewText(R.id.et_sex, items[item].toString());
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


            if (requestCode == LOCATION_RESULT_REQUEST && null != data) {
                String val = data.getStringExtra("address");
                setViewText(R.id.txt_location, val);
            } else if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK
                    && null != data) {
                if (dFragment != null) {
                    dFragment.dismiss();
                    dFragment = null;
                }
                Uri selectedImage = data.getData();
                Log.d("DATA", data.toString());
                Log.d("uri ", selectedImage.toString());
                Bitmap bitmap = null;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    profile.setImageBitmap(bitmap);

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


            } else if (requestCode == CAMERA_REQUEST
                    && resultCode == Activity.RESULT_OK) {
                if (dFragment != null) {
                    dFragment.dismiss();
                    dFragment = null;
                }
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                profile.setImageBitmap(photo);

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
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doEditProfile() {
        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.EDIT_PROFILE);
            params.put("user_id", Utils.getUserId(this));
            params.put("name", getViewText(R.id.txt_name));
            params.put("email", getViewText(R.id.edt_email_id));
            params.put("gender", getViewText(R.id.et_sex).equals("Male") ? "M" : "F");
            params.put("age", getViewText(R.id.et_age));
            params.put("location", getViewText(R.id.txt_location));
            params.put("lat", "" + gpsTracker.getLatitude());
            params.put("lng", "" + gpsTracker.getLongitude());

            final ProgressDialog pdialog = Utils.createProgressDialog(this, null, false);
            CustomJsonImageRequest postReq = new CustomJsonImageRequest(Request.Method.POST,
                    Constant.SERVICE_BASE_URL, params, f,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.ShowLog(TAG, "Response -> " + response.toString());
                            pdialog.dismiss();
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    Log.i("info", "" + response);

                                    UserDTO userDTO = new Gson().fromJson(response.getJSONObject("user").
                                            toString(), UserDTO.class);
                                    userDTO.setUserType(Constant.REGISTER);
                                    DealPreferences.putObjectIntoPref(EditProfileActivity.this,
                                            userDTO, Constant.USER_INFO);

                                    // delete cache image
                                    File imageFile = ImageLoader.getInstance().getDiscCache().
                                            get(userDTO.getImage());
                                    if (imageFile.exists()) {
                                        imageFile.delete();
                                    }
                                    MemoryCacheUtils.removeFromCache(userDTO.getImage(),
                                            ImageLoader.getInstance().getMemoryCache());


                                    ImageLoader.getInstance().displayImage(userDTO.getImage(), profile,
                                            options);
                                    Intent intent = new Intent(EditProfileActivity.this,
                                            HomeActivity.class);
                                    intent.putExtra("fragmentName",
                                            EditProfileActivity.this.getString(R.string.setting_screen_title));
                                    startActivity(intent);

                                } else {
                                    Utils.showDialog(EditProfileActivity.this, "Error",
                                            Utils.getWebServiceMessage(response));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pdialog.dismiss();
                    Utils.showExceptionDialog(EditProfileActivity.this);
                }
            });

            pdialog.show();
            Log.i("info", postReq.toString());
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        } else {
            Utils.showNoNetworkDialog(this);

        }
    }


}


