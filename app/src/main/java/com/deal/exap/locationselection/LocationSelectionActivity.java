package com.deal.exap.locationselection;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.deal.exap.R;
import com.deal.exap.customviews.CustomProgressDialog;
import com.deal.exap.gps.GPSTracker;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.misc.PlaceJSONParser;
import com.deal.exap.utility.DealPreferences;
import com.deal.exap.utility.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class LocationSelectionActivity extends BaseActivity implements
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Activity mActivity;
    private AutoCompleteTextView edtSearch;
    private PlacesTask placesTask;
    private ParserTask parserTask;
    private MarkerOptions marketOptions;
    private Marker marker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);
        mActivity = LocationSelectionActivity.this;

        CustomProgressDialog.showProgDialog(mActivity, null);
        setHeader("");
        setLeftClick();
        setClick(R.id.btn_current_loc);

        edtSearch = (AutoCompleteTextView) findViewById(R.id.edt_location_box);

        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    LatLng latLng = Utils.getLocationFromAddress(edtSearch.getText().toString(),
                            mActivity);
                    animateCamera(latLng.latitude, latLng.longitude);
                    return true;
                }
                return false;
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        setUpMap();

    }


    @Override
    protected void onStart() {
        super.onStart();


        edtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edtSearch.getRight() -
                            edtSearch.getCompoundDrawables()[DRAWABLE_RIGHT].
                                    getBounds().width())) {
                        String edtAddressVal = edtSearch.getText().toString();
                        if (edtAddressVal != null && !edtAddressVal.equalsIgnoreCase("")) {
                            LatLng latLng = Utils.getLocationFromAddress(edtSearch.getText().toString(),
                                    mActivity);
                            if (latLng != null) {
                                try {
                                    mMap.setMyLocationEnabled(false);
                                } catch (SecurityException e) {
                                    e.printStackTrace();
                                }

                                mMap.clear();

                                marketOptions = new MarkerOptions().position(new LatLng(latLng.latitude,
                                        latLng.longitude));
                                marker = mMap.addMarker(marketOptions);
                                marker.showInfoWindow();
                                animateCamera(latLng.latitude, latLng.longitude);
                            }
                        }
                        return true;
                    }

                }
                return false;
            }
        });
    }

    private void setUpMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_current_loc:
                CustomProgressDialog.showProgDialog(mActivity, null);
                GPSTracker gpsTracker = new GPSTracker(mActivity);
                double lat = DealPreferences.getLatitude(mActivity);
                double lng = DealPreferences.getLongitude(mActivity);
                try {
                    mMap.clear();
                    marketOptions = null;
                    marker = null;
                    mMap.setMyLocationEnabled(true);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                animateCamera(lat, lng);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        String val = edtSearch.getText().toString();
        //Intent intent = new Intent(mActivity, EditProfileActivity.class);
        intent.putExtra("address", val);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //startActivity(intent);
        //mActivity.finish();
        setResult(1003, intent);
        finish();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double lat = DealPreferences.getLatitude(mActivity);
        double lng = DealPreferences.getLongitude(mActivity);


        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        animateCamera(lat, lng);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                try {
                    mMap.setMyLocationEnabled(false);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }

                mMap.clear();
                marketOptions = new MarkerOptions().position(new LatLng(latLng.latitude,
                        latLng.longitude));
                marker = mMap.addMarker(marketOptions);
                animateCamera(latLng.latitude, latLng.longitude);


            }
        });
    }

    /**
     * this method animate the camera on map according to the given lat and lng.
     *
     * @param lat is latitude of location.
     * @param lng is longitude of location.
     */
    private void animateCamera(final double lat, final double lng) {

        LatLng latLng = new LatLng(lat, lng);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14f).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        retrieveAddress(lat, lng);
        CustomProgressDialog.hideProgressDialog();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location_selection_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.choose_location:
                onBackPressed();
                break;

        }
        return true;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker != null) {
            if (marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
            } else {
                marker.showInfoWindow();
            }
        }
        return false;
    }

    private void retrieveAddress(final double lat, final double lng) {
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                String address = Utils.getAddress(lat, lng,
                        mActivity);
                if (address != null) {
                    edtSearch.setText(address);
                    if (marketOptions != null) {
                        marketOptions = marketOptions.title(address).snippet("");
                        marker = mMap.addMarker(marketOptions);
                    }
                }

            }
        };
        handler.post(r);

    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyCnFCfiC8PBKUFdUfHpAfSSlqh24F8wHEQ";

            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + sensor + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                // Fetching the data from we service
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            edtSearch.setAdapter(adapter);
        }
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            // Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


}
