package com.deal.exap.misc;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deal.exap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MapSupport {

    private GoogleMap map;

    public static void createMarker(GoogleMap map, double lat, double lng, String markerType, Context context, String discount) {

        try {
            LatLng latLng = new LatLng(lat, lng);
            if (markerType.equalsIgnoreCase("current")) {
                map.addMarker(new MarkerOptions()
                        .position(latLng).icon(
                                BitmapDescriptorFactory
                                        .fromResource(R.drawable.current_pointer)));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
            } else {


                View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.position_marker_layout, null);
                TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);
                numTxt.setText(discount);

                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, marker))));


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void drawPath(double startLat, double startLng, double endLat, double endLng, GoogleMap map) {
        StringBuilder urlString = new StringBuilder();
        map = map;
        try {
            urlString
                    .append("http://maps.googleapis.com/maps/api/directions/json");
            urlString.append("?origin=");
            urlString.append(startLat);
            urlString.append(",");
            urlString.append(startLng);
            urlString.append("&destination=");// to
            urlString.append(endLat);
            urlString.append(",");
            urlString.append(endLng);

            urlString.append("&sensor=false&mode=driving&alternatives=true");
            String str = urlString.toString();
            Log.i("info1", str);
            connectAsyncTask cat = new connectAsyncTask(str);
            cat.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        String url;

        connectAsyncTask(String urlPass) {
            url = urlPass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                drawPath1(result);
            }

        }
    }

    public void drawPath1(String result) {
        try {
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            for (int z = 0; z < list.size() - 1; z++) {
                LatLng src = list.get(z);
                LatLng dest = list.get(z + 1);
                map.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude),
                                new LatLng(dest.latitude, dest.longitude))
                        .width(5).color(Color.BLUE).geodesic(true));
                Log.i("info", "src :" + src.latitude + ", " + src.longitude);
                Log.i("info", "dest:" + dest.latitude + ", " + dest.longitude);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }


    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }


}
