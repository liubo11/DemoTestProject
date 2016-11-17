package com.lb.demoproject;

import android.os.Bundle;
import com.custom.WheelView;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016-05-25.
 */
public class TestCustomTopBarActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_top_bar);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
       // setSupportActionBar(toolbar);


    }
}

