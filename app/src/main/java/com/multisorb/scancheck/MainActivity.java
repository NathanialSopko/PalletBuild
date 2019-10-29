package com.multisorb.scancheck;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends AppCompatActivity {

    private String badgeID;

    private String palletID;

    private Snackbar CheckSnackBar;

    @Override
    public void onBackPressed() {
        final EditText input1 = (EditText) findViewById(R.id.plain_text_input);
        final EditText input2 = (EditText) findViewById(R.id.plain_text_input2);
        input2.setEnabled(true);

        if(palletID != ""){
            ResetContainerScan();
        }
        else{
            ResetActivity();
        }
    }

    private void ResetActivity(){
        palletID = "";

        final EditText input1 = (EditText) findViewById(R.id.plain_text_input);
        final EditText input2 = (EditText) findViewById(R.id.plain_text_input2);
        final TextView textView  = (TextView)findViewById(R.id.totalScanned);

        input1.setText("");
        input2.setText("");
        textView.setText("");

        input1.setFocusable(true);
        input1.requestFocus();

        DismissSnackBar();
    }

    private void ResetContainerScan(){
        final EditText input2 = (EditText) findViewById(R.id.plain_text_input2);

        View overallDiv = findViewById(R.id.overallDiv);
        overallDiv.setBackgroundColor(Color.parseColor("#ffffff"));

        input2.setText("");

        input2.requestFocus();

        DismissSnackBar();
    }

    private void DismissSnackBar(){
        if(CheckSnackBar != null){
            if(CheckSnackBar.isShown()){
                CheckSnackBar.dismiss();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.palletID = "";

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        String value = ""; // or other values
        if(b != null){
            value = b.getString("Label");
            setTitle(value);

            badgeID = b.getString("Badge_ID");
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final EditText input1 = findViewById(R.id.plain_text_input);
        final EditText input2 = findViewById(R.id.plain_text_input2);


        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            input1.setShowSoftInputOnFocus(false);
            input2.setShowSoftInputOnFocus(false);
        //}

        input1.addTextChangedListener(
                new TextWatcher() {
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                    Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
                    Runnable workRunnable;
                    @Override public void afterTextChanged(Editable s) {
                        handler.removeCallbacks(workRunnable);
                        workRunnable = new Runnable() {
                            @Override
                            public void run() {
                                if(!input1.getText().toString().equals("")){
                                    try {
                                        GetPalletCountFirst();
                                    }
                                    catch (Exception e){

                                    }
                                }
                            }
                        };
                        handler.postDelayed(workRunnable, 750 /*delay*/);
                    }
                }
        );


        input2.addTextChangedListener(
                new TextWatcher() {
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                    Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
                    Runnable workRunnable;
                    @Override public void afterTextChanged(Editable s) {
                        handler.removeCallbacks(workRunnable);
                        workRunnable = new Runnable() {
                            @Override
                            public void run() {
                                if(!input2.getText().toString().equals("")){
                                    if(input2.getText().toString().equals(palletID)){
                                        ResetActivity();
                                    }
                                    else{
                                        Check();
                                    }
                                }
                            }
                        };
                        handler.postDelayed(workRunnable, 750 /*delay*/);
                    }
                }
        );
    }

    /** GetPalletCountFirst is assuming that there is no palletID already stored **/
    public void GetPalletCountFirst(){
        final EditText input1 = (EditText) findViewById(R.id.plain_text_input);
        final EditText input2 = (EditText) findViewById(R.id.plain_text_input2);
        final TextView textView  = (TextView)findViewById(R.id.totalScanned);

        String text1 = input1.getText().toString();

        RequestParams params = new RequestParams();
        params.setUseJsonStreamer(true);
        JSONObject student1 = new JSONObject();
        try {
            student1.put("Pallet_ID", text1);
            student1.put("isIndia", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity stringEntity;
        try {
            stringEntity = new StringEntity(student1.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(getBaseContext(),"http://10.38.0.69/PalletBuild/PalletBuild/GetTotalScanCount", stringEntity, RequestParams.APPLICATION_JSON, new AsyncHttpResponseHandler() {
            //client.post(getBaseContext(),"http://10.38.57.50/PalletBuild/PalletBuild/GetTotalScanCount", stringEntity, RequestParams.APPLICATION_JSON, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String testString = new String(responseBody, StandardCharsets.UTF_8);

                    testString = testString.substring(1, testString.length()-1);
                    testString = testString.replace("\\\"", "\"");

                    Gson gson = new GsonBuilder().create();
                    GetPalletCountReturner object = new GetPalletCountReturner();
                    try{

                        object = gson.fromJson(testString, GetPalletCountReturner.class);

                    }
                    catch(JsonSyntaxException e){
                        ResetActivity();
                    }

                    if(object.Error == 0){
                        //display the count here
                        textView.setText(Integer.toString(object.Count));
                        palletID = input1.getText().toString();
                        input1.setFocusable(false);
                        input2.requestFocus();
                    }
                    else{
                        //do something as a failure

                        Snackbar.make(getCurrentFocus(), "Invalid Pallet ID", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        ResetActivity();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Snackbar.make(findViewById(R.id.main_layout), new String(error.getMessage()), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    ResetActivity();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void GetPalletCount(){
        final EditText input1 = (EditText) findViewById(R.id.plain_text_input);
        final EditText input2 = (EditText) findViewById(R.id.plain_text_input2);
        final TextView textView  = (TextView)findViewById(R.id.totalScanned);

        String text1 = input1.getText().toString();

        RequestParams params = new RequestParams();
        params.setUseJsonStreamer(true);
        JSONObject student1 = new JSONObject();
        try {
            student1.put("Pallet_ID", text1);
            student1.put("isIndia", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity stringEntity;
        try {
            stringEntity = new StringEntity(student1.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(getBaseContext(),"http://10.38.0.69/PalletBuild/PalletBuild/GetTotalScanCount", stringEntity, RequestParams.APPLICATION_JSON, new AsyncHttpResponseHandler() {
                //client.post(getBaseContext(),"http://10.38.57.50/PalletBuild/PalletBuild/GetTotalScanCount", stringEntity, RequestParams.APPLICATION_JSON, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String testString = new String(responseBody, StandardCharsets.UTF_8);

                    testString = testString.substring(1, testString.length()-1);
                    testString = testString.replace("\\\"", "\"");

                    Gson gson = new GsonBuilder().create();
                    GetPalletCountReturner object = new GetPalletCountReturner();
                    try{

                        object = gson.fromJson(testString, GetPalletCountReturner.class);

                    }
                    catch(JsonSyntaxException e){
                        ResetActivity();
                    }

                    if(object.Error == 0){
                        //display the count here
                        textView.setText(Integer.toString(object.Count));
                        palletID = input1.getText().toString();
                    }
                    else{
                        //do something as a failure

                        Snackbar.make(getCurrentFocus(), "Invalid Pallet ID", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        ResetActivity();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Snackbar.make(findViewById(R.id.main_layout), new String(error.getMessage()), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    ResetActivity();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void Check(){
        EditText input1 = (EditText) findViewById(R.id.plain_text_input);
        EditText input2 = (EditText) findViewById(R.id.plain_text_input2);

        String text1 = input1.getText().toString();
        String text2 = input2.getText().toString();

        RequestParams params = new RequestParams();
        params.setUseJsonStreamer(true);
        JSONObject student1 = new JSONObject();
        try {
            student1.put("Badge_ID", badgeID);
            student1.put("Pallet_ID", text1);
            student1.put("CONT_ID", text2);
            student1.put("isIndia", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity stringEntity;
        try {
            stringEntity = new StringEntity(student1.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(getBaseContext(),"http://10.38.0.69/PalletBuild/PalletBuild/CheckPalletData", stringEntity, RequestParams.APPLICATION_JSON, new AsyncHttpResponseHandler() {
            //client.post(getBaseContext(),"http://10.38.57.50/PalletBuild/PalletBuild/CheckPalletData", stringEntity, RequestParams.APPLICATION_JSON, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String testString = new String(responseBody, StandardCharsets.UTF_8);

                    testString = testString.substring(1, testString.length()-1);
                    testString = testString.replace("\\\"", "\"");

                    Gson gson = new GsonBuilder().create();
                    GetPalletDataReturner object = new GetPalletDataReturner();
                    try{

                        object = gson.fromJson(testString, GetPalletDataReturner.class);

                    }
                    catch(JsonSyntaxException e){
                        ResetActivity();
                    }

                    if(Integer.parseInt(object.errno) == 0){
                        MediaPlayer ring= MediaPlayer.create(MainActivity.this,R.raw.quiteimpressed);
                        ring.start();

                        View overallDiv = findViewById(R.id.overallDiv);
                        overallDiv.setBackgroundColor(0xFF00FF00);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                View overallDiv = findViewById(R.id.overallDiv);
                                overallDiv.setBackgroundColor(0xffffffff);
                            }
                        }, 3000);


                        DismissSnackBar();
                        CheckSnackBar = Snackbar.make(getCurrentFocus(), object.errtxt, Snackbar.LENGTH_LONG)
                                .setAction("Action", null);
                        CheckSnackBar.show();

                        GetPalletCount();

                        ResetContainerScan();
                    }
                    else{
                        //this is a bad scan... stop here and make them click back.
                        final EditText input2 = (EditText) findViewById(R.id.plain_text_input2);
                        final EditText input1 = (EditText) findViewById(R.id.plain_text_input);
                        //input2.setText("");
//                        input2.clearFocus();
//                        input1.clearFocus();
//                        getWindow().getDecorView().clearFocus();
                        input2.setEnabled(false);

                        MediaPlayer ring= MediaPlayer.create(MainActivity.this,R.raw.buzz);
                        ring.start();

                        View tempDiv = findViewById(R.id.overallDiv);
                        tempDiv.setBackgroundColor(0xFFFF0000);

                        DismissSnackBar();
                        CheckSnackBar = Snackbar.make(getCurrentFocus(), object.errtxt, Snackbar.LENGTH_INDEFINITE)
                                .setAction("Action", null);
                        CheckSnackBar.show();

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Snackbar.make(findViewById(R.id.main_layout), new String(error.getMessage()), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    ResetActivity();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
