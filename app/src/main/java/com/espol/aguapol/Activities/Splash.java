package com.espol.aguapol.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import com.espol.aguapol.R;

import java.util.Timer;
import java.util.TimerTask;


public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Animation animation= AnimationUtils.loadAnimation(this,R.anim.desplazamiento_arriba);
        Animation animation2= AnimationUtils.loadAnimation(this,R.anim.desplazamiento_abajo);

        //imgSplash.setAnimation(animation2);

        ImageView txtSlpash=findViewById(R.id.imgSplash);
        txtSlpash.setAnimation(animation);

        TimerTask tarea= new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(Splash.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };

        Timer tiempo= new Timer();
        tiempo.schedule(tarea,2000);
    }
}