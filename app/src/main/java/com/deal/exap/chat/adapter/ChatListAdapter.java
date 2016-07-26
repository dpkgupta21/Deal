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

    public void setChatList(List<MessageDTO> chatList){
        this.chatList = chatList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView=convertView;
        ViewHolder mHolder;

        MessageDTO chat = chatList.get(position);
        if(mView==null) {

            LayoutInflater li = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (chat.getUser_id().equals(userId))
                mView = (View) li.inflate(R.layout.item_chat_right, parent, false);
            else
                mView = (View) li.inflate(R.layout.item_chat_left, parent, false);

            mHolder=new ViewHolder();

            mHolder.tv_msg=(TextView)mView.findViewById(R.id.tv_msg);
            mHolder.tv_date=(TextView)mView.findViewById(R.id.tv_date);
            mHolder.iv_profile=(ImageView)mView.findViewById(R.id.iv_profile);

            mView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) mView.getTag();
        }

        mHolder.tv_msg.setText(chat.getMessage());
        mHolder.tv_date.setText(chat.getTimestamp());
        ImageLoader.getInstance().displayImage(chat.getImage(), mHolder.iv_profile,
                option);

        return mView;
    }


    public static class ViewHolder {
        TextView tv_msg;
        TextView tv_date;
        ImageView iv_profile;
    }
}
