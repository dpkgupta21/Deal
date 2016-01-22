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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.model.ChatDTO;
import com.deal.exap.model.MessageDTO;
import com.deal.exap.model.Partner;
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
import java.util.List;
import java.util.Map;

public class ChatActivity extends BaseActivity {

    private ListView lvChat;
    private List<MessageDTO> chatList;
    private ChatDTO chatDTO;
    ChatListAdapter chatAdatper;
    private EditText etMessage;
    private DisplayImageOptions options;
    private String receiverId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();

        // getMessageList();

        loadMessageList();


    }

    private void init() {
        receiverId = getIntent().getStringExtra("receiverID");
        lvChat = (ListView) findViewById(R.id.lv_chat);
        setHeader(getString(R.string.discussion_title));
        setLeftClick();
        setClick(R.id.iv_send);
        etMessage = (EditText) findViewById(R.id.et_message);
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
        if (!getViewText(R.id.et_message).equals("")) {
            if (Utils.isOnline(this)) {
                Map<String, String> params = new HashMap<>();
                params.put("action", Constant.SEND_MESSAGE);
                params.put("lang", Utils.getSelectedLanguage(this));
                params.put("sender_id", Utils.getUserId(this));
                params.put("message", getViewText(R.id.et_message));
                params.put("receiver_id", receiverId);
                final ProgressDialog pdialog = Utils.createProgeessDialog(this, null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(Constant.TAG, "Resonse -> " + response.toString());
                                pdialog.dismiss();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        ChatDTO chat = new Gson().fromJson(response.toString(),ChatDTO.class);
                                        if (chat != null) {
                                            //chatList.add(lst.get(0));
                                            //chatAdatper.notifyDataSetChanged();
                                            //chatAdatper.updateList(lst.subList(0, 1));
                                            etMessage.setText("");
                                            chatList.add(chat.getMessageList().get(chat.getMessageList().size()-1));
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
                postReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            } else {
                Utils.showNoNetworkDialog(ChatActivity.this);
            }
        }
    }


    public void loadMessageList() {
        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.LOAD_MESSAGE);
            params.put("receiver_id", receiverId);
            params.put("sender_id", Utils.getUserId(this));
            final ProgressDialog pdialog = Utils.createProgeessDialog(this, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                chatDTO = new Gson().fromJson(response.toString(), ChatDTO.class);
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
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            pdialog.show();
        } else {
            Utils.showNoNetworkDialog(ChatActivity.this);
        }
    }

    public void setChatList() {

        if(chatDTO!=null) {

            ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);
            ImageView img_company = (ImageView) findViewById(R.id.img_company);

            Partner partner = chatDTO.getPartner();

            ImageLoader.getInstance().displayImage(partner.getImage(), thumbnail,
                    options);
            ImageLoader.getInstance().displayImage(partner.getImage(), img_company,
                    options);
            if (Utils.isArebic(ChatActivity.this)) {
                setTextViewText(R.id.txt_title, partner.getName_ara());
                setTextViewText(R.id.txt_place_tag, partner.getAddress_ara());
            } else {
                setTextViewText(R.id.txt_title, partner.getName_eng());
                setTextViewText(R.id.txt_place_tag, partner.getAddress_eng());
            }

            if (partner.getIs_featured().equalsIgnoreCase("0")) {
                setViewVisibility(R.id.img_featured, View.GONE);
            } else {
                setViewVisibility(R.id.img_featured, View.VISIBLE);
            }

            chatList = chatDTO.getMessageList();
            if (chatList != null && chatList.size() > 0) {

                if (chatAdatper == null) {
                    chatAdatper = new ChatListAdapter(this);
                    lvChat.setAdapter(new ChatListAdapter(this));
                } else {
                    chatAdatper.notifyDataSetChanged();
                }

            } else {
                Utils.showDialog(this, "Message", "Empty Inbox");
            }
        }
        else
        {
            Utils.showDialog(this, "Message", "Empty Inbox");
        }
    }

    class ChatListAdapter extends BaseAdapter {

        private Context context;
        private String userId;
        private DisplayImageOptions option;

        public ChatListAdapter(Context context) {
            this.context = context;
            userId = Utils.getUserId(context);
            option = new DisplayImageOptions.Builder()
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
            LayoutInflater li = (LayoutInflater) ((Activity) context)
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view;
            MessageDTO chat= chatList.get(position);
            if (chat.getUser_id().equals(userId))
                view = (View) li.inflate(R.layout.item_chat_right, parent, false);
            else
                view = (View) li.inflate(R.layout.item_chat_left, parent, false);

            ((TextView) view.findViewById(R.id.tv_msg)).setText(chat.getMessage());
            ((TextView) view.findViewById(R.id.tv_date)).setText(chat.getTimestamp());
            ImageView ivProfile = (ImageView) view.findViewById(R.id.iv_profile);
            ImageLoader.getInstance().displayImage(chat.getImage(), ivProfile,
                    option);

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
