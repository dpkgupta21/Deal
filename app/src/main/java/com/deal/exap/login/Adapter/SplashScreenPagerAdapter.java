package com.deal.exap.login.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.deal.exap.R;

import java.io.IOException;
import java.io.InputStream;

public class SplashScreenPagerAdapter extends PagerAdapter {

    private Activity _activity;
    private String[] imageNameArray;
    private Bitmap bitmap;

    public SplashScreenPagerAdapter(Activity _activity, String[] imageNameArray) {
        this._activity = _activity;
        this.imageNameArray = imageNameArray;
    }

    @Override
    public int getCount() {
        return imageNameArray.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.splash_screen_view_pager_item, container, false);

//        TextView txt_description = (TextView) view.findViewById(R.id.txt_description);
//        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        ImageView image_item = (ImageView) view.findViewById(R.id.image_item);

        String imageFileName = imageNameArray[position];
        try {
            InputStream is = _activity.getAssets().open(imageFileName + ".png");

            bitmap = BitmapFactory.decodeStream(is);
            image_item  .setImageBitmap(bitmap);
            int imgResId = _activity.getResources().getIdentifier(imageFileName,
                    "drawable", "com.deal.exap");
            image_item.setImageResource(imgResId);

            image_item.setDrawingCacheEnabled(true);
            image_item.buildDrawingCache(true);
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //txt_description.setText(mPicturesList.get(pictureIdsList.get(position)));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);

//        View view=(RelativeLayout) object;
//        ImageView image_item = (ImageView) view.findViewById(R.id.image_item);
//
//        BitmapDrawable bd = (BitmapDrawable)image_item.getDrawable();
//        Bitmap bm = bd.getBitmap();
//        bm.recycle();

    }



}
