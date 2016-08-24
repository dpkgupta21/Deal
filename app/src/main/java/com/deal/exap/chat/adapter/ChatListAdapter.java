package com.deal.exap.chat.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.model.MessageDTO;
import com.deal.exap.utility.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * Created by DeepakGupta on 1/23/16.
 */

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<MessageDTO> chatList;
    private String userId;
    private DisplayImageOptions option;
    private static final int TYPE_RIGHT_USER = 0;
    private static final int TYPE_LEFT_USER = 1;

    public ChatListAdapter(Activity mActivity, List<MessageDTO> chatList) {
        this.mActivity = mActivity;
        this.chatList = chatList;
        userId = Utils.getUserId(mActivity);
        option = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.default_img)
                .showImageOnFail(R.drawable.default_img)
                .showImageForEmptyUri(R.drawable.default_img)
                .build();
    }

    public void setChatList(List<MessageDTO> chatList) {
        this.chatList = chatList;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return chatList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return chatList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return Long.parseLong(chatList.get(arg0).getUser_id());
    }

    @Override
    public int getItemViewType(int position) {
        int type= chatList.get(position).getUser_id().equals(userId) ? TYPE_RIGHT_USER : TYPE_LEFT_USER;
        return type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater li = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int type = getItemViewType(position);
        MessageDTO chat = chatList.get(position);

        switch (type) {
            case TYPE_RIGHT_USER:
                ViewHolder mHolder = null;

                if (convertView == null) {

                    mHolder = new ViewHolder();
                    convertView = li.inflate(R.layout.item_chat_right, null);
                    mHolder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
                    mHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                    mHolder.iv_profile = (ImageView) convertView.findViewById(R.id.iv_profile);
                    convertView.setTag(mHolder);
                } else {
                    mHolder = (ViewHolder) convertView.getTag();
                }
                mHolder.tv_msg.setText(chat.getMessage());
                mHolder.tv_date.setText(chat.getTimestamp());
                ImageLoader.getInstance().displayImage(chat.getImage(), mHolder.iv_profile,
                        option);

                return convertView;
            case TYPE_LEFT_USER:
                ViewHolderLeft mHolderLeft = null;

                if (convertView == null) {

                    mHolderLeft = new ViewHolderLeft();
                    convertView = li.inflate(R.layout.item_chat_left, null);
                    mHolderLeft.tv_msg_left = (TextView) convertView.findViewById(R.id.tv_msg_left);
                    mHolderLeft.tv_date_left = (TextView) convertView.findViewById(R.id.tv_date_left);
                    mHolderLeft.iv_profile_left = (ImageView) convertView.findViewById(R.id.iv_profile_left);
                    convertView.setTag(mHolderLeft);


                } else {
                    mHolderLeft = (ViewHolderLeft) convertView.getTag();
                }
                mHolderLeft.tv_msg_left.setText(chat.getMessage());
                mHolderLeft.tv_date_left.setText(chat.getTimestamp());
                ImageLoader.getInstance().displayImage(chat.getImage(), mHolderLeft.iv_profile_left,
                        option);
                return convertView;
        }

        return null;
    }


    public static class ViewHolder {
        public TextView tv_msg;
        public TextView tv_date;
        public ImageView iv_profile;
    }

    public static class ViewHolderLeft {
        public TextView tv_msg_left;
        public TextView tv_date_left;
        public ImageView iv_profile_left;
    }
}
