package com.lb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-08-26.
 */
public class TestShortCutKey extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean ifValue = false;

        List<String> names = new ArrayList<>();
        Object myName = "123";
        String rName = myName instanceof String ? ((String) myName) : null;


    }
}
