package com.lb.demoproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.lb.widget.TestStateView;

/**
 * Created by LiuBo on 2016-10-25.
 */

public class TestViewState extends BaseActivity {

    private TestStateView stateView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_view_state);

        stateView = (TestStateView) findViewById(R.id.test_view_state);

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateView.switchState();
            }
        });
    }
}
