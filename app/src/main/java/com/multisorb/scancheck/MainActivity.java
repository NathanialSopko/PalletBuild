package com.multisorb.scancheck;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        final EditText input1 = (EditText) findViewById(R.id.plain_text_input);
        final EditText input2 = (EditText) findViewById(R.id.plain_text_input2);



        input1.setShowSoftInputOnFocus(false);
        input2.setShowSoftInputOnFocus(false);

        input1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                new Handler().postDelayed(new Runnable(){
                    public void run(){
                        input2.requestFocus();
                    }
                },1000);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        input2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                new Handler().postDelayed(new Runnable(){
                    public void run(){
                        fab.performClick();
                    }
                },1000);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText input1 = (EditText) findViewById(R.id.plain_text_input);
                EditText input2 = (EditText) findViewById(R.id.plain_text_input2);

                String text1 = input1.getText().toString();
                String text2 = input2.getText().toString();


                if(text1.equals(text2)){
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

                    Snackbar.make(view, "Text was the same", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    //MainActivity.this.recreate();
                }
                else{

                    MediaPlayer ring= MediaPlayer.create(MainActivity.this,R.raw.consequence);
                    ring.start();

                    View overallDiv = findViewById(R.id.overallDiv);
                    overallDiv.setBackgroundColor(Color.parseColor("#ff0000"));
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            View overallDiv = findViewById(R.id.overallDiv);
                            overallDiv.setBackgroundColor(0xffffffff);
                        }
                    }, 3000);

                    Snackbar.make(view, "Text was different", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    //MainActivity.this.recreate();

                }


            }
        });
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
