package cn.lb.custom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016-05-27.
 */
public class AnimCircleView extends View {

    public AnimCircleView(Context context) {
        this(context, null);
    }

    public AnimCircleView (Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimCircleView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
    }

    private Paint mPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, 100, mPaint);
    }
}
