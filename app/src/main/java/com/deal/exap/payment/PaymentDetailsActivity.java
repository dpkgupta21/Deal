package com.deal.exap.payment;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.customviews.MyEditTextViewReg;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.utility.Utils;
import com.mobile.connect.PWConnect;
import com.mobile.connect.exception.PWError;
import com.mobile.connect.exception.PWException;
import com.mobile.connect.exception.PWProviderNotInitializedException;
import com.mobile.connect.listener.PWTransactionListener;
import com.mobile.connect.payment.PWCurrency;
import com.mobile.connect.payment.PWPaymentParams;
import com.mobile.connect.payment.credit.PWCreditCardType;
import com.mobile.connect.provider.PWTransaction;
import com.mobile.connect.service.PWProviderBinder;

import java.util.ArrayList;

public class PaymentDetailsActivity extends BaseActivity implements PWTransactionListener {

    private ArrayList<String> months;
    private ArrayList<String> years;
    private double price;
    private PWProviderBinder _binder;
    private double transactionPrice;
    private String dealName;


    // For production
    private static final String APPLICATIONIDENTIFIER = "Hyperpay.6085WorldOfSS.mcommerce";//"gate2play.WorldofSS.mcommerce.test";
    private static final String PROFILETOKEN = "44a2f1d0f1a711e5a7dc11fc67275b56"; //"930e6e9744154563afc4718ab0352b9a";

//    private static final String APPLICATIONIDENTIFIER = "gate2play.WorldofSS.mcommerce.test";
//      private static final String PROFILETOKEN = "930e6e9744154563afc4718ab0352b9a";

    private ServiceConnection _serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            _binder = (PWProviderBinder) service;
            // we have a connection to the service
            try {
                _binder.initializeProvider(PWConnect.PWProviderMode.LIVE,
                        APPLICATIONIDENTIFIER, PROFILETOKEN);
                _binder.addTransactionListener(PaymentDetailsActivity.this);
            } catch (PWException ee) {
                setStatusText("Error initializing the provider.");
                // error initializing the provider
                ee.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            _binder = null;
        }
    };

    private void setStatusText(final String string) {
        runOnUiThread(new Runnable() {
            public void run() {
                ((TextView) findViewById(R.id.txt_status)).setText(string);
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        transactionPrice = Double.parseDouble(getIntent().getStringExtra("BUY_PRICE"));
        dealName = getIntent().getStringExtra("dealName");

        EditText edt_card_number = (EditText) findViewById(R.id.edt_card_number);
        edt_card_number.addTextChangedListener(new FourDigitCardFormatWatcher());

        setViewText(R.id.txt_order_name, dealName);
        setViewText(R.id.txt_order_quantity, "Quantity 1");
        setViewText(R.id.txt_order_total_val, transactionPrice + " SAR");

        startService(new Intent(this,
                com.mobile.connect.service.PWConnectService.class));
        bindService(new Intent(this,
                        com.mobile.connect.service.PWConnectService.class),
                _serviceConnection, Context.BIND_AUTO_CREATE);


        init();


    }

    private void callTransaction() {
        PWPaymentParams paymentParams = null;
        try {
            final RadioButton radio_btn_visa = (RadioButton) findViewById(R.id.radio_btn_visa);
            final RadioButton radio_btn_master_card = (RadioButton)
                    findViewById(R.id.radio_btn_master_card);

            PWCreditCardType creditCardType = null;

            String holder = ((MyEditTextViewReg) findViewById(R.id.edt_card_holder_name)).
                    getText().toString().trim();
            String cardNumber = ((MyEditTextViewReg) findViewById(R.id.edt_card_number)).
                    getText().toString().trim();//"4005550000000001";
            String cvv = ((MyEditTextViewReg) findViewById(R.id.edt_card_cvv)).
                    getText().toString().trim();//123
            String month = ((TextView) findViewById(R.id.txt_month)).
                    getText().toString().trim();//05
            String year = ((TextView) findViewById(R.id.txt_year)).
                    getText().toString().trim();//"2017";

            if (radio_btn_master_card.isChecked()) {
                creditCardType = PWCreditCardType.MASTERCARD;
            } else if (radio_btn_visa.isChecked()) {
                creditCardType = PWCreditCardType.VISA;
            }
            paymentParams = _binder
                    .getPaymentParamsFactory()
                    .createCreditCardPaymentParams(transactionPrice,
                            PWCurrency.SAUDI_ARABIA_RIYAL, "A test charge", holder,
                            creditCardType, cardNumber, year, month, cvv);

        } catch (PWProviderNotInitializedException e) {
            setStatusText("Error: Provider not initialized!");
            e.printStackTrace();
            return;
        } catch (PWException e) {
            setStatusText("Error: Invalid Parameters!");
            e.printStackTrace();
            return;
        }

        setStatusText("Preparing...");

        try {
            _binder.createAndRegisterDebitTransaction(paymentParams);
        } catch (PWException e) {
            setStatusText("Error: Could not contact Gateway!");
            e.printStackTrace();
        }
    }

    private void init() {
        setHeader("Payment");
        setLeftClick();

        months = Utils.getMonths();
        years = Utils.getYears();
//        setClick(R.id.txt_month);
//        setClick(R.id.txt_year);
        setViewText(R.id.txt_month, months.get(0));
        setViewText(R.id.txt_year, years.get(0));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.txt_month:
                //openMonthDialog();
                break;
            case R.id.txt_year:
                //openYearDialog();
                break;
            case R.id.btn_save:
                if (validateForm()) {
                    callTransaction();
                }
                break;
        }
    }


    public void openMonthDialog() {
        final Dialog dialog = new Dialog(PaymentDetailsActivity.this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_country_code);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ListView listView = (ListView) dialog.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, months);
        listView.setAdapter(adapter);
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setViewText(R.id.txt_month, months.get(position));
                dialog.dismiss();
            }
        });


    }

    public void openYearDialog() {
        final Dialog dialog = new Dialog(PaymentDetailsActivity.this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_country_code);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ListView listView = (ListView) dialog.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, years);
        listView.setAdapter(adapter);
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setViewText(R.id.txt_year, years.get(position));
                dialog.dismiss();
            }
        });


    }

    public boolean validateForm() {

        if (getViewText(R.id.edt_card_holder_name).equals("")) {
            Utils.showDialog(PaymentDetailsActivity.this, "Message", "Please enter cardholder name.");
            return false;
        } else if (getViewText(R.id.txt_month).equals("")) {
            Utils.showDialog(PaymentDetailsActivity.this, "Message", "Please enter month");
            return false;
        } else if (getViewText(R.id.txt_year).equals("")) {
            Utils.showDialog(PaymentDetailsActivity.this, "Message", "Please enter year");
            return false;
        } else if (getViewText(R.id.edt_card_number).equals("")) {
            Utils.showDialog(PaymentDetailsActivity.this, "Message", "Please enter card number");
            return false;
        } else if (!isCheckboxChecked(R.id.chk_terms)) {
            Utils.showDialog(PaymentDetailsActivity.this, "Message", "Please select terms & conditions");
            return false;
        }

        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(_serviceConnection);
        stopService(new Intent(this,
                com.mobile.connect.service.PWConnectService.class));
    }


    @Override
    public void creationAndRegistrationFailed(PWTransaction transaction, PWError error) {
        setStatusText("Error contacting the gateway.");
        //Log.e("com.payworks.customtokenization.TokenizationActivity", error.getErrorMessage());
    }

    @Override
    public void creationAndRegistrationSucceeded(PWTransaction transaction) {
        setStatusText("Processing...");
        try {
            _binder.debitTransaction(transaction);
        } catch (PWException e) {
            setStatusText("Invalid Transaction.");
            e.printStackTrace();
        }
    }

    @Override
    public void transactionFailed(PWTransaction arg0, PWError error) {
        setStatusText("Error contacting the gateway.");
        // Log.e("com.payworks.customtokenization.TokenizationActivity", error.getErrorMessage());
    }

    @Override
    public void transactionSucceeded(PWTransaction transaction) {
        // our debit succeeded
        setStatusText("Charged " + transactionPrice);

        String processUniqueIdentifier = transaction.getProcessorUniqueIdentifier();


        Intent resultIntent = new Intent();
        resultIntent.putExtra("processUniqeIdentifier", processUniqueIdentifier);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public static class FourDigitCardFormatWatcher implements TextWatcher {

        // Change this to what you want... ' ', '-' etc..
        final char space = ' ';

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Remove spacing char
            if (s.length() > 0 && (s.length() % 5) == 0) {
                final char c = s.charAt(s.length() - 1);
                if (space == c) {
                    s.delete(s.length() - 1, s.length());
                }
            }
            // Insert char where needed.
            if (s.length() > 0 && (s.length() % 5) == 0) {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a space we insert a space
                if (Character.isDigit(c) && TextUtils.split(s.toString(),
                        String.valueOf(space)).length <= 3) {
                    s.insert(s.length() - 1, String.valueOf(space));
                }
            }
        }


    }

}
