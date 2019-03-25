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
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
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
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

//    private final EditText badgeNumberInput;

//    public static final MediaType JSON
//            = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.badgeNumber);
        //populateAutoComplete();

//        RequestParams params = new RequestParams();
//        params.setUseJsonStreamer(true);
//        JSONObject student1 = new JSONObject();
//        try {
//            student1.put("stringValue", "tester");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        StringEntity stringEntity;
//        try {
//            stringEntity = new StringEntity(student1.toString());
//            AsyncHttpClient client = new AsyncHttpClient();
//            client.post(this,"http://192.168.1.253/PalletBuild/PalletBuild/CheckInUser", stringEntity, RequestParams.APPLICATION_JSON, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    Snackbar.make(findViewById(R.id.Login_Layout), new String(responseBody), Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                    Snackbar.make(findViewById(R.id.Login_Layout), new String(error.getMessage()), Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
//            });
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }



//        client.get("http://192.168.1.253/PalletBuild/PalletBuild/CheckAPIWorking", new AsyncHttpResponseHandler() {
//
//            @Override
//            public void onStart() {
//                Snackbar.make(findViewById(R.id.Login_Layout), "Hi", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                Snackbar.make(findViewById(R.id.Login_Layout), new String(responseBody), Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Snackbar.make(findViewById(R.id.Login_Layout), "Call Broke", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//
//            @Override
//            public void onRetry(int retryNo) {
//
//            }
//        });

//        mPasswordView = (EditText) findViewById(R.id.password);
//        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
//                    attemptLogin();
//                    return true;
//                }
//                return false;
//            }
//        });

//        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
//        mEmailSignInButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                attemptLogin();
//            }
//        });

        final EditText input1 = (EditText) findViewById(R.id.badgeNumber);

        input1.setShowSoftInputOnFocus(false);

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
                                RequestParams params = new RequestParams();
                                params.setUseJsonStreamer(true);
                                JSONObject student1 = new JSONObject();
                                try {
                                    student1.put("stringValue", input1.getText());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                StringEntity stringEntity;
                                try {
                                    stringEntity = new StringEntity(student1.toString());
                                    AsyncHttpClient client = new AsyncHttpClient();
                                    client.post(getBaseContext(),"http://192.168.1.253/PalletBuild/PalletBuild/CheckInUser", stringEntity, RequestParams.APPLICATION_JSON, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            String test = new String(responseBody);
                                            test = test.substring(1, test.length()-1);
                                            if(!test.equals("")){
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            }
                                            else{
                                                input1.setText("");
                                            }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                            Snackbar.make(findViewById(R.id.Login_Layout), new String(error.getMessage()), Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                        }
                                    });
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        handler.postDelayed(workRunnable, 1500 /*delay*/);
                    }
//
//
//                    private Timer timer=new Timer();
//                    private final long DELAY = 1000; // milliseconds

//                    @Override
//                    public void afterTextChanged(final Editable s) {
//                        if(timer != null){
//                            timer.cancel();
//                        }
//                        timer = new Timer();
//                        timer.schedule(
//                                new TimerTask() {
//                                    @Override
//                                    public void run() {
//                                        // TODO: do what you need here (refresh list)
//                                        RequestParams params = new RequestParams();
//                                        params.setUseJsonStreamer(true);
//                                        JSONObject student1 = new JSONObject();
//                                        try {
//                                            student1.put("stringValue", input1.getText());
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        StringEntity stringEntity;
//                                        try {
//                                            stringEntity = new StringEntity(student1.toString());
//                                            AsyncHttpClient client = new AsyncHttpClient();
//                                            client.post(getBaseContext(),"http://192.168.1.253/PalletBuild/PalletBuild/CheckInUser", stringEntity, RequestParams.APPLICATION_JSON, new AsyncHttpResponseHandler() {
//                                                @Override
//                                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                                                    String test = new String(responseBody);
//                                                    test = test.substring(1, test.length()-1);
//                                                    if(test != ""){
//                                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                                    }
//                                                    else{
//                                                        input1.setText("");
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                                                    Snackbar.make(findViewById(R.id.Login_Layout), new String(error.getMessage()), Snackbar.LENGTH_LONG)
//                                                            .setAction("Action", null).show();
//                                                }
//                                            });
//                                        } catch (UnsupportedEncodingException e) {
//                                            e.printStackTrace();
//                                        }
//                                        // you will probably need to use runOnUiThread(Runnable action) for some specific actions
//                                    }
//                                },
//                                DELAY
//                        );
//                    }
                }
        );

//        input1.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(input1.getText().length() > 6){
//                    new Handler().postDelayed(new Runnable(){
//                        public void run(){
//                            RequestParams params = new RequestParams();
//                            params.setUseJsonStreamer(true);
//                            JSONObject student1 = new JSONObject();
//                            try {
//                                student1.put("stringValue", input1.getText());
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            StringEntity stringEntity;
//                            try {
//                                stringEntity = new StringEntity(student1.toString());
//                                AsyncHttpClient client = new AsyncHttpClient();
//                                client.post(getBaseContext(),"http://192.168.1.253/PalletBuild/PalletBuild/CheckInUser", stringEntity, RequestParams.APPLICATION_JSON, new AsyncHttpResponseHandler() {
//                                    @Override
//                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                                        String test = new String(responseBody);
//                                        test = test.substring(1, test.length()-1);
//                                        if(test != ""){
//                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                        }
//                                        else{
//                                            input1.setText("");
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                                        Snackbar.make(findViewById(R.id.Login_Layout), new String(error.getMessage()), Snackbar.LENGTH_LONG)
//                                                .setAction("Action", null).show();
//                                    }
//                                });
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },1000);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//            }
//        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

//    String barcode="";
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //Log.ASSERT( "Key Down keyCode " + keyCode);
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return super.onKeyDown(keyCode, event);
//        } else if (keyCode == KeyEvent.KEYCODE_ENTER) {//if scanner doesn't return enter key code then make sure that any view must not have focus in window
//            //write your code to process the scanned barcode input
//            barcode = "";
//        } else {
//            Character input = (char) event.getUnicodeChar();
//            Log.i("Info from onKeyDown", "Scanner Input " + input);
//            if (Character.isDigit(input) || Character.isLetter(input)) {
//                barcode += input;//concat the characters
//            }
//        }
//        return true;
//    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent e) {
//
//
//        if(e.getAction()==KeyEvent.ACTION_DOWN){
//            Log.i("this tag","dispatchKeyEvent: "+e.toString());
//            char pressedKey = (char) e.getUnicodeChar();
//            barcode += pressedKey;
//        }
//        if (e.getAction()==KeyEvent.ACTION_DOWN && e.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//            Toast.makeText(getApplicationContext(),
//                    "barcode--->>>" + barcode, Toast.LENGTH_LONG)
//                    .show();
//
//            barcode="";
//        }
//
//        return super.dispatchKeyEvent(e);
//    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

