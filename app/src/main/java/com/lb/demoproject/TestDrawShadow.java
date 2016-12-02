package com.lb.demoproject;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lb.widget.TestShadowView;

/**
 * Created by LiuBo on 2016-11-24.
 */

public class TestDrawShadow extends BaseActivity {

    private TestShadowView mShadowView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_shadow);

        int drawable = R.drawable.ic_arrow_anim;
        mShadowView = (TestShadowView) findViewById(R.id.shadow_view);
    }
}
