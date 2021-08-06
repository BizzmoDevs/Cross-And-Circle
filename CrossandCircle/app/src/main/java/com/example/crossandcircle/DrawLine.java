package com.example.crossandcircle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawLine extends View {
    Paint paint = new Paint();
    int posX,posY,posX2,posY2,colorInt;

    private void init() {}
    public DrawLine(Context context) {
        super(context);
        init();
    }
    public DrawLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public DrawLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (colorInt == 1) {
            paint.setColor(Color.RED);
        }else paint.setColor(Color.BLACK);
        canvas.drawLine(posX, posY, posX2, posY2, paint);
    }


}