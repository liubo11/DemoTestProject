package com.lb.demoproject.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.lb.demoproject.BaseActivity;
import com.lb.demoproject.R;

/**
 * Created by LiuBo on 2016-11-08.
 */

public class SplashActivity extends BaseActivity {
    //背景透明需要设置windowBackground与windowIsTranslucent属性
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_full_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, TestMainActivity.class);
                startActivity(intent);

                finish();
            }
        }, 3000);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
