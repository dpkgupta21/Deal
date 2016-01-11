package com.deal.exap.partner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.model.ChatDTO;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends BaseActivity {

    private ListView lvChat;
    private ArrayList<ChatDTO> chatList;
    ChatListAdapter chatAdatper;
    private EditText etMessage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();

        getMessageList();
    }

    private void init() {

        lvChat = (ListView) findViewById(R.id.lv_chat);
        setHeader(getString(R.string.discussion_title));
        setLeftClick();
        setClick(R.id.iv_send);
        etMessage = (EditText) findViewById(R.id.et_message);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_send:
                sendMessage();
                break;
        }
    }

    public void sendMessage() {
        Utils.hideKeyboard(this);
        if(!getViewText(R.id.et_message).equals("")) {
            if (Utils.isOnline(this)) {
                Map<String, String> params = new HashMap<>();
                params.put("action", Constant.SEND_MESSAGE);
                params.put("lang", Utils.getSelectedLanguage(this));
                params.put("sender_id", Utils.getUserId(this));
                params.put("message", getViewText(R.id.et_message));
                params.put("receiver_id", ""+10);
                final ProgressDialog pdialog = Utils.createProgeessDialog(this, null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(Constant.TAG, "Resonse -> " + response.toString());
                                pdialog.dismiss();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        Type type = new TypeToken<ArrayList<ChatDTO>>(){}.getType();
                                        ArrayList<ChatDTO> lst = new Gson().fromJson(response.getJSONArray("messageList").toString(), type);
                                        if(lst!=null && lst.size()>0){
                                            //chatList.add(lst.get(0));
                                            //chatAdatper.notifyDataSetChanged();
                                            //chatAdatper.updateList(lst.subList(0, 1));
                                            etMessage.setText("");
                                            chatList.add(lst.get(0));
                                            chatAdatper.notifyDataSetChanged();
                                        }

                                    } else {
                                        Utils.showDialog(ChatActivity.this, "Error", Utils.getWebServiceMessage(response));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pdialog.dismiss();
                        Utils.showExceptionDialog(ChatActivity.this);
                    }
                });
                pdialog.show();
                AppController.getInstance().getRequestQueue().add(postReq);
            } else {
                Utils.showNoNetworkDialog(ChatActivity.this);
            }
        }
    }

    public void getMessageList() {
        if(Utils.isOnline(this)){
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.GET_MESSAGE);
            params.put("lang", Utils.getSelectedLanguage(this));
            params.put("user_id", Utils.getUserId(this));
            final ProgressDialog pdialog = Utils.createProgeessDialog(this, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<ChatDTO>>(){}.getType();
                                chatList = new Gson().fromJson(response.getJSONArray("conversions").toString(), type);
                                setChatList();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            pdialog.dismiss();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pdialog.dismiss();
                    Utils.showExceptionDialog(ChatActivity.this);
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            pdialog.show();
        }
        else{
            Utils.showNoNetworkDialog(ChatActivity.this);
        }
    }

    public void setChatList(){
        if(chatList!=null && chatList.size()>0){

            if(chatAdatper==null){
                chatAdatper = new ChatListAdapter(this);
                lvChat.setAdapter(new ChatListAdapter(this));
            }else{
                chatAdatper.notifyDataSetChanged();
            }

        }else{
            Utils.showDialog(this, "Message", "Empty Inbox");
        }
    }

    class ChatListAdapter extends BaseAdapter {

        private Context context;
        private String userId;
        private DisplayImageOptions options;
        public ChatListAdapter(Context context) {
            this.context = context;
            userId = Utils.getUserId(context);
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
            // TODO Auto-generated method stub
            return chatList.size();
        }
        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        /*View view = convertView;
        ViewHolder holder;
        try {
            if (view == null) {
                LayoutInflater li = (LayoutInflater) ((Activity) object)
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (View) li.inflate(R.layout.alert_row_layout,
                        parent, false);
                holder = new ViewHolder();
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;*/
            LayoutInflater li = (LayoutInflater) ((Activity) context)
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view;
            ChatDTO chatDTO = chatList.get(position);
            if(chatDTO.getUser_id().equals(userId))
                view = (View) li.inflate(R.layout.item_chat_right, parent,false);
            else
                view = (View) li.inflate(R.layout.item_chat_left, parent,false);

            ((TextView)view.findViewById(R.id.tv_msg)).setText(chatDTO.getMessage());
            ((TextView)view.findViewById(R.id.tv_date)).setText(chatDTO.getTimestamp());
            ImageView ivProfile = (ImageView) view.findViewById(R.id.iv_profile);
            ImageLoader.getInstance().displayImage(chatDTO.getImage(), ivProfile,
                    options);

            return view;
        }
    }
    public static class ViewHolder {
        TextView txt_title;
        TextView txt_desc;
        TextView txt_date_time;
        ImageView img;
    }
}
