package com.assignment.saints.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ImageView logo = (ImageView) findViewById(R.id.logo);
        RotateAnimation aRotate = new RotateAnimation(0,720,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        aRotate.setStartOffset(0);
        aRotate.setDuration(3000);
        aRotate.setFillAfter(true);
        aRotate.setInterpolator(this, android.R.anim.decelerate_interpolator);
        aRotate.setRepeatCount(1);

        logo.startAnimation(aRotate);
        aRotate.setAnimationListener(new Animation.AnimationListener()
        {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(LoadingActivity.this,DisplayActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

}
