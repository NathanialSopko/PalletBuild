package com.multisorb.scancheck;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private void ResetActivity(){
        final EditText input1 = (EditText) findViewById(R.id.badgeNumber);

        input1.setText("");

        input1.requestFocus();
    }

    @Override
    public void onBackPressed() {
        ResetActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText input1 = (EditText) findViewById(R.id.badgeNumber);

        input1.setShowSoftInputOnFocus(false);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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
                                    RequestParams params = new RequestParams();
                                    params.setUseJsonStreamer(true);
                                    JSONObject student1 = new JSONObject();
                                    try {
                                        student1.put("stringValue", input1.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    StringEntity stringEntity;
                                    try {
                                        stringEntity = new StringEntity(student1.toString());
                                        AsyncHttpClient client = new AsyncHttpClient();
                                        client.post(getBaseContext(),"http://10.38.0.69/PalletBuild/PalletBuild/CheckInUser", stringEntity, RequestParams.APPLICATION_JSON, new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                String testString = new String(responseBody, StandardCharsets.UTF_8);

                                                testString = testString.substring(1, testString.length()-1);
                                                testString = testString.replace("\\\"", "\"");
                                                //testString = testString.replace("'", "\'");

                                                Gson gson = new GsonBuilder().create();
                                                ReturnBadgeModel object = new ReturnBadgeModel();
                                                try{

                                                    object = gson.fromJson(testString, ReturnBadgeModel.class);

                                                }
                                                catch(JsonSyntaxException e){
                                                    ResetActivity();
                                                }

                                                if(object.Column1 == 0){
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    Bundle b = new Bundle();
                                                    b.putString("Label", object.EmpName); //Your id
                                                    b.putString("Badge_ID", object.BadgeNo); //Your id
                                                    intent.putExtras(b); //Put your id to your next Intent
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else{
                                                    Snackbar.make(findViewById(R.id.Login_Layout), object.EmpName, Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                    ResetActivity();
                                                }
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                                Snackbar.make(findViewById(R.id.Login_Layout), new String(error.getMessage()), Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                                ResetActivity();
                                            }
                                        });
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    } catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                        handler.postDelayed(workRunnable, 750 /*delay*/);
                    }
                }
        );

    }
}

