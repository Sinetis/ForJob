package com.education.anatoly.testapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    Button getButton;
    ListView ordersListView;
    ProgressBar progressBar;

    String requestToken;
    String accessToken;

    String jsonOrders;

    String publicKey = "38cd79b5f2b2486d86f562e3c43034f8";
    String privateKey = "8e49ff607b1f46e1a5e8f6ad5d312a80";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getButton       = findViewById(R.id.getButton);
        ordersListView  = findViewById(R.id.ordersListView);

        progressBar     = findViewById(R.id.progressBar);
        //progressBar.

        getButton.setOnClickListener(new ClickListener());
        //String password = HashSHA1(requestToken + privateKey);
    }

    class ClickListener implements View.OnClickListener {
        public void onClick(View v) {
            new Request().execute();
        }
    }

    class Request extends AsyncTask<Void, Integer, String> {

        ArrayAdapter<String> adapter;

        @Override
        protected void onPreExecute() {
            getButton.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);

            ordersListView.setAdapter(adapter);
            ordersListView.setVisibility(View.VISIBLE);

            getButton.setEnabled(true);
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                requestToken = ApiHelper.GetRequestToken();

                accessToken = ApiHelper.GetAccessToken(
                        requestToken,
                        publicKey,
                        privateKey);

                jsonOrders = ApiHelper.GetOrderList(accessToken);

                JSONObject jObj = new JSONObject(jsonOrders);
                JSONArray jArr = jObj.getJSONArray("Result");

                List<String> labels = new ArrayList<>();
                for (int i = 0; i < jArr.length(); i++){
                    jObj = jArr.getJSONObject(i);
                    labels.add(
                            jObj.getString("Id") +
                            ": " +
                            jObj.getString("Title"));
                }

                adapter = new ArrayAdapter(
                        getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        labels);

            }
            catch (Exception e){
                ErrorDialog(e.toString());
            }

            return null;
        }
    }

//    boolean b = true;

//    void Authorization() {
//
//        AccountManager am = AccountManager.get(getApplicationContext());
//        Account account = new Account(AccountManager.KEY_ACCOUNT_NAME, AccountManager.KEY_ACCOUNT_TYPE);
//
//
//        am.addAccountExplicitly(account,"", null);
//        Account[] accs = am.getAccountsByType(AccountManager.KEY_ACCOUNT_TYPE);
//
//        am.getAuthToken(
//                accs[0],
//                "oauth_token",
//                null,
//                this,
//                new OnTokenAcquired(),
//                new Handler(new OnError())
//        );
//        while (b);
//    }
//
//    class OnError implements Handler.Callback{
//        @Override
//        public boolean handleMessage(Message msg) {
//            ErrorDialog(msg.toString());
//            b = false;
//            return false;
//        }
//    }
//
//    class OnTokenAcquired implements AccountManagerCallback<Bundle>{
//        @Override
//        public void run(AccountManagerFuture<Bundle> future) {
//            try {
//                Bundle bundle = future.getResult();
//                authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
//
//                Intent launch = (Intent) bundle.get(AccountManager.KEY_INTENT);
//
//                if (launch != null)
//                {
//                    startActivityForResult(launch, 0);
//                    return;
//                }
//            }
//            catch (Exception e){
//                ErrorDialog(e.toString());
//            }
//            finally {
//                b = false;
//            }
//        }
//    }

    void ErrorDialog(String text)
    {
//        AlertDialog ad = new AlertDialog.Builder(getApplicationContext())
//                .setMessage(text)
//                .setTitle("Error")
//                .setPositiveButton("OK", null)
//                .create();
//        ad.show();
        Toast.makeText(
                getApplicationContext(),
                text,
                Toast.LENGTH_LONG).show();

    }
}
