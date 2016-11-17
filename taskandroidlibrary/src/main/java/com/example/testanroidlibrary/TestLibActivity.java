package com.example.testanroidlibrary;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import cn.lb.custom.view.AnimCircleView;

/**
 * Created by Administrator on 2016-04-25.
 */
public class TestLibActivity  extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //AnimCircleView animCircleView = new AnimCircleView(this);

        Button btn = new Button(this);

        btn.setText(getString(R.string.btn_name));

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(TestLibActivity.this, "this is a test button", Toast.LENGTH_SHORT).show();
            }
        });

        setContentView(btn);
    }
}


