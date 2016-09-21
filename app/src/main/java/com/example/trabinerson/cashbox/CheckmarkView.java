/*
 * Copyright (c) 2016 PayPal, Inc.
 *
 * All rights reserved.
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */

package com.example.trabinerson.cashbox;

/**
 * Created by asaf on 9/21/16.
 */

import android.animation.TimeAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;



public class CheckmarkView extends View implements TimeAnimator.TimeListener {

    private float t = 0.0f;
    private float duration = 2500.0f;
    private Vector2 longlineStart;
    private Vector2 longlineEnd;
    private Vector2 shortlineStart;
    private Vector2 shortlineEnd;
    private float startAngle;
    private float endAngle;
    private Paint paint;
    private Vector2 center;
    private float radius;
    private TimeAnimator mTimeAnimator;
    private Bitmap clearBitmap;

    public CheckmarkView(Context context) {
        super(context);
        mTimeAnimator = new TimeAnimator();
    }

    public CheckmarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTimeAnimator = new TimeAnimator();
    }

    public CheckmarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTimeAnimator = new TimeAnimator();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int diameter = Math.min(w, h);
        radius = 0.5f * diameter / 2.0f;
        startAngle = (float)Math.toRadians(320);
        endAngle = (float)Math.toRadians(5);
        float checkmarkVertexOffset = 0.3f * radius;
        center = new Vector2(w / 2.0f, h / 2.0f);
        longlineStart = new Vector2(center.x, center.y + checkmarkVertexOffset);
        longlineEnd = new Vector2(center.x + ((float)Math.cos(startAngle) * radius), center.y + ((float)Math.sin(startAngle) * radius));
        Vector2 longlineVector = new Vector2(longlineEnd.x - longlineStart.x, longlineEnd.y - longlineStart.y);
        Vector2 shortlineVector = new Vector2(longlineVector.y * 0.4f, -longlineVector.x * 0.4f);
        shortlineStart = new Vector2(longlineStart.x + shortlineVector.x, longlineStart.y + shortlineVector.y);
        shortlineEnd = longlineStart;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //paint.setColor(0xFF00CF92);
        paint.setColor(0xFFFFFFFF);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);

        clearBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        clearBitmap.eraseColor(Color.TRANSPARENT);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void startAnimation() {
        mTimeAnimator.end();
        t = 0.0f;
        mTimeAnimator.setTimeListener(this);
        mTimeAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(clearBitmap, 0.0f, 0.0f, null);
        paint.setStrokeWidth(3.0f);
        paint.setAlpha(255);

        float strokeT = partitionAnimation(0.0f, 0.3f, t);
        float checkmarkT = partitionAnimation(0.0f, 0.5f, strokeT);
        checkmarkT = (float)Math.cos((checkmarkT + 1.0f)*Math.PI) * 0.5f + 0.5f;
        float shortlineT = partitionAnimation(0.0f, 0.4f, checkmarkT);
        float longlineT = partitionAnimation(0.4f, 1.0f, checkmarkT);
        float circleT = partitionAnimation(0.5f, 1.0f, strokeT);
        circleT *= circleT;
        float flourishT = partitionAnimation(0.3f, 1.0f, t);

        if (shortlineT > 0) {
            canvas.drawLine(shortlineStart.x,
                    shortlineStart.y,
                    lerp(shortlineStart.x, shortlineEnd.x, shortlineT),
                    lerp(shortlineStart.y, shortlineEnd.y, shortlineT), paint);
        }

        if (longlineT > 0) {
            canvas.drawLine(longlineStart.x, longlineStart.y,
                    lerp(longlineStart.x, longlineEnd.x, longlineT),
                    lerp(longlineStart.y, longlineEnd.y, longlineT), paint);
        }

        if (circleT > 0) {
            canvas.drawArc(center.x - radius, center.y - radius, center.x + radius, center.y + radius, (float)Math.toDegrees(startAngle), (float)Math.toDegrees(lerp(startAngle, endAngle, circleT) - startAngle), false, paint);
        }

        if (flourishT > 0) {
            flourishT = 1.0f - flourishT;
            flourishT = 1.0f - (float)Math.pow(flourishT, 5.0f);
            paint.setStrokeWidth(lerp(3.0f, 17.0f, flourishT));
            paint.setAlpha((int)Math.floor(lerp(200.0f, 0.0f, flourishT)));
            float rad = lerp(radius, radius * 1.8f, flourishT);
            canvas.drawArc(center.x - rad, center.y - rad, center.x + rad, center.y + rad, 0, 360.0f, false, paint);
        }
    }

    private float partitionAnimation(float start, float end, float t) {
        t = Math.max(start, Math.min(t, end));
        return (t-start) / (end-start);
    }

    private float lerp(float start, float end, float t) {
        return (1-t)*start + t*end;
    }

    @Override
    public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
        t += deltaTime / duration;
        t = Math.min(1.0f, t);
        postInvalidate();
        if (t == 1.0f) {
            mTimeAnimator.end();
        }
    }

    public class Vector2 {
        public float x;
        public float y;

        public Vector2(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
