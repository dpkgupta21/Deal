package com.deal.exap.misc;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.deal.exap.R;
import com.deal.exap.customviews.MyButtonViewSemi;
import com.deal.exap.customviews.MyTextViewReg16;
import com.deal.exap.login.NumberVerificationActivity;


public class CustomAlertDialog {

    private Object object;
    private Dialog dialog;

    public static CustomAlertDialog getCustomAlert(Object object) {
        return new CustomAlertDialog(object);
    }

    private CustomAlertDialog(Object object) {
        this.object = object;
    }


    public void singleButtonAlertDialog(final String alertMessage,
                                        final String positiveBtn, final String callBackMethod) {

        dialog = new Dialog((Context) object);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        ((MyTextViewReg16) dialog.findViewById(R.id.text_alert_msg)).setText(alertMessage);
        ((MyButtonViewSemi) dialog.findViewById(R.id.btn_alert_positive)).setOnClickListener(positiveButtonClick);

        dialog.show();
    }


    View.OnClickListener positiveButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (dialog != null) {
                    dialog.cancel();
                    dialog = null;
                }
            } catch (Exception e) {

            }


        }
    };

}
