package com.deal.exap.payment;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.deal.exap.R;
import com.deal.exap.customviews.MyEditTextViewReg;
import com.deal.exap.login.BaseActivity;
import com.deal.exap.utility.DealPreferences;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BuyPaymentDialogActivity extends BaseActivity implements PWTransactionListener {

    private ArrayList<String> months;
    private ArrayList<String> years;
    private double transactionPrice;
    private TextView txtMonth;
    private TextView txtYear;
    private PWProviderBinder _binder;
    // For development
    private static final String APPLICATIONIDENTIFIER = "Hyperpay.6085WorldOfSS.mcommerce";//"gate2play.WorldofSS.mcommerce.test";
    private static final String PROFILETOKEN = "44a2f1d0f1a711e5a7dc11fc67275b56"; //"930e6e9744154563afc4718ab0352b9a";

    // For test
//    private static final String APPLICATIONIDENTIFIER = "payworks.sandbox";
//    private static final String PROFILETOKEN = "20d5a0d5ce1d4501a4826a8b7e159d19";

    private ServiceConnection _serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            _binder = (PWProviderBinder) service;
            // we have a connection to the service
            try {
                _binder.initializeProvider(PWConnect.PWProviderMode.LIVE,
                        APPLICATIONIDENTIFIER, PROFILETOKEN);
                _binder.addTransactionListener(BuyPaymentDialogActivity.this);
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
        setContentView(R.layout.dialog_payment);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        transactionPrice = getIntent().getDoubleExtra("BUY_PRICE", 0.0);

        months = Utils.getMonths();
        years = Utils.getYears();
        txtMonth = (TextView) findViewById(R.id.txt_month);
        txtYear = (TextView) findViewById(R.id.txt_year);

        txtMonth.setText(months.get(0));
        txtYear.setText(years.get(0));
        Button btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_pay.setText("Pay " + transactionPrice);
        //RadioGroup radio_grp = (RadioGroup) findViewById(R.id.radio_grp);
        final RadioButton radio_btn_visa = (RadioButton) findViewById(R.id.radio_btn_visa);
        final RadioButton radio_btn_master_card = (RadioButton) findViewById(R.id.radio_btn_master_card);

        radio_btn_visa.setChecked(true);
        txtMonth.setOnClickListener(monthDialog);
        txtYear.setOnClickListener(yearDialog);


        ((ImageView) findViewById(R.id.iv_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    String cardHolderName = ((EditText) findViewById(R.id.edt_card_number)).
                            getText().toString().trim();
                    String cardNumber = ((EditText) findViewById(R.id.edt_card_number)).
                            getText().toString().trim();//"4005550000000001";
                    String cvv = ((EditText) findViewById(R.id.edt_cvv)).
                            getText().toString().trim();//123
                    String month = txtMonth.
                            getText().toString().trim();//05
                    String year = txtYear.
                            getText().toString().trim();//"2017";
                    PWCreditCardType creditCardType = null;
                    //CheckBox chkRememberMe = (CheckBox) findViewById(R.id.chk_remember_me);
                    if (radio_btn_master_card.isChecked()) {
                        creditCardType = PWCreditCardType.MASTERCARD;
                    } else if (radio_btn_visa.isChecked()) {
                        creditCardType = PWCreditCardType.VISA;
                    }

                    callTransaction(cardHolderName, cardNumber, month, year, cvv,
                            transactionPrice, creditCardType);
                }
            }

        });

    }

    View.OnClickListener monthDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openMonthDialog();

        }
    };

    View.OnClickListener yearDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openYearDialog();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this,
                com.mobile.connect.service.PWConnectService.class));
        bindService(new Intent(this,
                        com.mobile.connect.service.PWConnectService.class),
                _serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void callTransaction(String cardHolderName,
                                 String cardNumber,
                                 String month,
                                 String year,
                                 String cvv,
                                 double transactionPrice,
                                 PWCreditCardType creditCardType) {
        PWPaymentParams paymentParams = null;
        try {
            paymentParams = _binder
                    .getPaymentParamsFactory()
                    .createCreditCardPaymentParams(transactionPrice, PWCurrency.SAUDI_ARABIA_RIYAL,
                            "A test charge",
                            cardHolderName,
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_month:
                openMonthDialog();
                break;
            case R.id.txt_year:
                openYearDialog();
                break;

        }
    }


    public void openMonthDialog() {
        final Dialog dialog = new Dialog(BuyPaymentDialogActivity.this);
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
                txtMonth.setText(months.get(position));
                dialog.dismiss();

            }
        });


    }

    public void openYearDialog() {
        final Dialog dialog = new Dialog(BuyPaymentDialogActivity.this);
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
                txtYear.setText(years.get(position));
                dialog.dismiss();
            }
        });


    }


    public boolean validateForm() {

        if (getViewText(R.id.edt_card_number).equals("")) {
            Utils.showDialog(BuyPaymentDialogActivity.this, "Message", "Please enter card number");
            return false;
        } else if (getViewText(R.id.edt_card_holder_name).equals("")) {
            Utils.showDialog(BuyPaymentDialogActivity.this, "Message", "Please enter cardholder name.");
            return false;
        } else if (getViewText(R.id.txt_month).equals("")) {
            Utils.showDialog(BuyPaymentDialogActivity.this, "Message", "Please enter month");
            return false;
        } else if (getViewText(R.id.txt_year).equals("")) {
            Utils.showDialog(BuyPaymentDialogActivity.this, "Message", "Please enter year");
            return false;
        } else if (getViewText(R.id.edt_cvv).equals("")) {
            Utils.showDialog(BuyPaymentDialogActivity.this, "Message", "Please enter cvv");
            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        setStatusText("Charged " + transactionPrice + "!");
        String processUniqueIdentifier = transaction.getProcessorUniqueIdentifier();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("processUniqeIdentifier", processUniqueIdentifier);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
