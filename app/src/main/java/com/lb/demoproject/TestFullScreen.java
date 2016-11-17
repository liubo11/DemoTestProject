package com.lb.demoproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by LiuBo on 2016-11-08.
 */

public class TestFullScreen extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_full_screen);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                v.setSystemUiVisibility(0);
            }
        });


    }
}
