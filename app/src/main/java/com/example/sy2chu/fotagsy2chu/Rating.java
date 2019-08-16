package com.example.sy2chu.fotagsy2chu;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;

import java.util.Vector;

public class Rating extends Drawable {

    Vector<RectF> circles;
    private final Paint mBlackPaint;
    private final Paint mGrayPaint;
    private int rating = 1;

    public Rating() {
        this.setBounds(0, 0, 300, 60);
        circles = new Vector<RectF>();
        for(int i=0;i<5;i++){
            RectF circle = new RectF(i*70,0,(i+1)*70-10,60);
            circles.add(circle);
        }

        mBlackPaint = new Paint();
        mBlackPaint.setColor(Color.BLACK);

        mGrayPaint = new Paint();
        mGrayPaint.setColor(Color.GRAY);
    }

    public Rating(int r) {
        this.setBounds(0, 0, 300, 60);
        circles = new Vector<RectF>();
        for(int i=0;i<5;i++){
            RectF circle = new RectF(i*70,0,(i+1)*70-10,60);
            circles.add(circle);
        }

        mBlackPaint = new Paint();
        mBlackPaint.setColor(Color.BLACK);

        mGrayPaint = new Paint();
        mGrayPaint.setColor(Color.GRAY);

        this.rating = r;
    }

    public int getRatingNum() {
        return this.rating;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint white = new Paint();
        white.setStyle(Paint.Style.FILL);
        white.setColor(Color.WHITE);
        Rect rect = new Rect(-20,-30,350,30);
        canvas.drawRect(rect,white);

        for(int i=1;i<=rating;i++){
            canvas.drawOval(circles.get(i-1),mBlackPaint);
        }
        for(int i=rating;i<5;i++){
            canvas.drawOval(circles.get(i),mGrayPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        // This method is required
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // This method is required
    }

    @Override
    public int getOpacity() {
        // Must be PixelFormat.UNKNOWN, TRANSLUCENT, TRANSPARENT, or OPAQUE
        return PixelFormat.OPAQUE;
    }
}
