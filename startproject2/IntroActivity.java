package com.example.startproject2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ImageView logo = (ImageView) findViewById(R.id.imageView2);
        TextView introwrite = (TextView)findViewById(R.id.textView5);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.logoanim);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.introwriting);
        logo.startAnimation(animation1);
        introwrite.startAnimation(animation2);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class );
                startActivity(intent);
                finish();
            }
        },2000);


    }
}
