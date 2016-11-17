package com.lb.demoproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by LiuBo on 2016-10-13.
 */

public class TestDrawText extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(new TestTextV(this));

    }

    private class TestTextV extends ImageView {
        private TextPaint mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        public TestTextV(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mPaint.setColor(Color.RED);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setTextSize(50);

            canvas.drawLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2, mPaint);
            canvas.drawLine(canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight(), mPaint);
            canvas.save();
            //canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2 + 25);

            Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
            int baseline = (int) (canvas.getHeight() / 2 - (fontMetrics.bottom + fontMetrics.top) / 2);



            canvas.drawText("123ABCabc壹贰叁", canvas.getWidth() / 2, baseline, mPaint);

            canvas.restore();
        }
    }
}
