package com.lb.demoproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.galaxywind.clib.CLib;
import com.lb.widget.OverProgressBar;

import java.util.Random;

/**
 * Created by LiuBo on 2016-09-14.
 */
public class TestOverProgressBarActivity extends BaseActivity {
    private OverProgressBar opb;

    private ImageView container;
    private Button btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_progress_bar);
        container = (ImageView) findViewById(R.id.container);
        container.setImageResource(R.drawable.back);

        btn = (Button) findViewById(R.id.btn);
        opb = (OverProgressBar) findViewById(R.id.opb);
        opb.setOnProgressChangeListener(new OverProgressBar.OnProgressChangeListener() {
            @Override
            public void progressChangeListener(int progress, boolean isFinished) {
                seekBar.setProgress(progress);
                System.out.println("progressChangeListener:progress="+progress);
            }
        });

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    opb.setProgress(progress);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back);
                    CLib.ClBlurBitmap(bitmap, progress);
                    container.setImageBitmap(bitmap);
                    btn.setText(""+progress);
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
    SeekBar seekBar;
    private Random random = new Random();
    private int getRandomInt(int size) {
        return random.nextInt(size);
    }
    public void onClick1(View v) {
        opb.setProgress(getRandomInt(100));
    }
}
