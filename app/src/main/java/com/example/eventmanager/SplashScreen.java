package com.example.eventmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar splashProgressBar;
    private int procress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        splashProgressBar=findViewById(R.id.splashProgressBarId);

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();

                Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        thread.start();
    }

    private void doWork() {

        for(procress=20;procress<=100;procress=procress+20){
            try{
                Thread.sleep(1000);
                splashProgressBar.setProgress(procress);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }
}
