package com.multisorb.scancheck;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
