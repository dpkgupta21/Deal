package com.deal.exap.payment.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.deal.exap.R;
import com.deal.exap.misc.ImageActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * Created by YusataInfotech on 1/31/2016.
 */
public class SlidingImageAdapter extends PagerAdapter {

    private Context context;
    private List<String> imageList;
    private LayoutInflater inflater;
    private DisplayImageOptions options;


    public SlidingImageAdapter(Context context, List<String> imageList) {
        this.imageList = imageList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.slide_img)
                .showImageOnFail(R.drawable.slide_img)
                .showImageForEmptyUri(R.drawable.slide_img)
                .build();
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ImageActivity.class);
                i.putExtra("image", imageList.get(position));
                context.startActivity(i);
            }
        });

        ImageLoader.getInstance().displayImage(imageList.get(position), imageView,
                options);


        view.addView(imageLayout, 0);

        return imageLayout;
    }


}
