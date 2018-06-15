package com.example.administrator.aman;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.gcm.GcmNetworkManager;

public class MainActivity extends AppCompatActivity {
    private GcmNetworkManager mGcmNetworkManager;
    private static final String TAG = "MainActivity";
    private static final int RC_PLAY_SERVICES = 123;
    private BroadcastReceiver mReceiver;



    public static final String TASK_TAG_PERIODIC = "periodic_task";
private static  int SPLASH_TIME_OUT=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(MainActivity.this,Sign.class);

                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);



        // Check that Google Play Services is available, since we need it to use GcmNetworkManager
        // but the API does not use GoogleApiClient, which would normally perform the check
        // automatically.


    }


}
