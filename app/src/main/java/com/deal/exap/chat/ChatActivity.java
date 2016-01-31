package com.deal.exap.chat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deal.exap.R;
import com.deal.exap.chat.adapter.ChatListAdapter;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.model.ChatDTO;
import com.deal.exap.model.MessageDTO;
import com.deal.exap.model.Partner;
import com.deal.exap.utility.Constant;
import com.deal.exap.utility.Utils;
import com.deal.exap.volley.AppController;
import com.deal.exap.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONObject;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends BaseActivity {

    //private ListView lvChat;
    private Context mContext;
    private List<MessageDTO> chatList;
    private ChatDTO chatDTO;
    private ChatListAdapter chatAdatper;
    private EditText etMessage;
    private DisplayImageOptions options;
    private String receiverId;
    private ListView lvChat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        lvChat = (ListView) findViewById(R.id.lv_chat);
        mContext = ChatActivity.this;
        init();

        // getMessageList();

        loadMessageList();


    }

    private void init() {
        receiverId = getIntent().getStringExtra("receiverID");
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
                params.put("lang", Utils.getSelectedLanguage(mContext));
                params.put("sender_id", Utils.getUserId(mContext));
                params.put("message", getViewText(R.id.et_message));
                params.put("receiver_id", receiverId);
                final ProgressDialog pdialog = Utils.createProgressDialog(mContext,
                        null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(Constant.TAG, "Response -> " + response.toString());
                                pdialog.dismiss();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        ChatDTO chat = new Gson().fromJson(response.toString(), ChatDTO.class);
                                        if (chat != null) {
                                            etMessage.setText("");

                                            chatList.clear();
                                            chatAdatper.setChatList(chat.getMessageList());
                                            chatAdatper.notifyDataSetChanged();
                                        }
                                    } else {
                                        Utils.showDialog(mContext, "Message", Utils.getWebServiceMessage(response));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pdialog.dismiss();
                        Utils.showExceptionDialog(mContext);
                    }
                });
                pdialog.show();
                AppController.getInstance().getRequestQueue().add(postReq);
                postReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            } else {
                Utils.showNoNetworkDialog(mContext);
            }
        }
    }


    public void loadMessageList() {
        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constant.LOAD_MESSAGE);
            params.put("receiver_id", receiverId);
            params.put("sender_id", Utils.getUserId(mContext));
            final ProgressDialog pdialog = Utils.createProgressDialog(mContext,
                    null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                Utils.ShowLog(Constant.TAG, "got some response = " + response.toString());
                                chatDTO = new Gson().fromJson(response.toString(), ChatDTO.class);
                                if (chatDTO.isStatus()) {
                                    lvChat.setVisibility(View.VISIBLE);
                                    setChatList();
                                } else {
                                    lvChat.setVisibility(View.GONE);
                                    String msg = response.getString("message");
                                    TextView txt_blank = (TextView) findViewById(R.id.txt_blank);
                                    txt_blank.setVisibility(View.VISIBLE);
                                    txt_blank.setText(msg);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            pdialog.dismiss();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pdialog.dismiss();
                    Utils.showExceptionDialog(mContext);
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

    private void setChatList() {

        if (chatDTO != null) {

            ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);
            ImageView img_company = (ImageView) findViewById(R.id.img_company);

            Partner partner = chatDTO.getPartner();

            ImageLoader.getInstance().displayImage(partner.getImage(), thumbnail,
                    options);
            ImageLoader.getInstance().displayImage(partner.getImage(), img_company,
                    options);
            if (Utils.isArabic(mContext)) {
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
                    chatAdatper = new ChatListAdapter((Activity) mContext, chatList);

                    lvChat.setAdapter(chatAdatper);

                } else {
                    chatAdatper.setChatList(chatList);
                    chatAdatper.notifyDataSetChanged();

                }
            } else {

                Utils.showDialog(this, "Message", "Empty Inbox");

            }
        } else {

            Utils.showDialog(this, "Message", "Empty Inbox");

        }
    }
}
