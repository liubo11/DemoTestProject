package com.lb.demoproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lb.widget.StarAnimView;

/**
 * Created by LiuBo on 2016-10-25.
 */

public class TestPadStarAnim extends BaseActivity {

    private StarAnimView starAnimView;
    private TextView mTextView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_pad_star_anim);

        mTextView = (TextView) findViewById(R.id.text);
        starAnimView = (StarAnimView) findViewById(R.id.star);

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && progress != 0) {
                    starAnimView.setDuration(progress * 1000);
                    mTextView.setText("速度： "+progress+" s / 圈");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
