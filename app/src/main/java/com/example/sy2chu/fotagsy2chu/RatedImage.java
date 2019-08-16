package com.example.sy2chu.fotagsy2chu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class RatedImage extends android.support.v7.widget.AppCompatImageView {
    Rating rating = new Rating(1);
    Bitmap img = null;
    Context context;
    ImageView ratingView = null;

    RatedImage(Context c){
        super(c);
        this.context = c;
        this.ratingView = new ImageView(context);
        this.setClickable(true);

        this.bringToFront();
        this.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_DOWN){
                    float x = ev.getX();
                    float y = ev.getY();
                    Log.i("TAG", "Picture touched! x = " + x + " y = " + y);
                }
                if (ev.getAction() == MotionEvent.ACTION_UP) {
                    float x = ev.getX();
                    float y = ev.getY();
                    Log.i("TAG", "Action up! x = " + x + " y = " + y);
                    MainActivity main = (MainActivity) context;
                    main.initPopup(img);
                }
                return true;
            }
        });

        ratingView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                if(ev.getAction() == MotionEvent.ACTION_BUTTON_PRESS){
                    float x = ev.getX();
                    float y = ev.getY();
                    Log.i("TAG", "Rating pressed! x = " + x + " y = " + y);
                }
                if (ev.getAction() == MotionEvent.ACTION_UP) {
                    float x = ev.getX();

                    float unit = 340/5;
                    int rate = (int) Math.ceil(x / unit);
                    Log.i("TAG", "Action up! rate = " + rate);
                    if (rate > 5) {
                        rate = 5;
                    }
                    rating = new Rating(rate);
                    ImageView temp = getRatingImage();
                    MainActivity main = (MainActivity) context;
                    main.loadIntoGrid();
                }
                return true;
            }
        });
    }

    public void setBitmap(Bitmap bm) {
        this.img = bm;
        this.setImageBitmap(img);
    }

    public Rating getRating() {
        return this.rating;
    }

    public ImageView getRatingImage() {
        ratingView.setImageDrawable(rating);
        return ratingView;
    }

    public Bitmap getImg() {
        return this.img;
    }

    public void setRating(int rating) {
        this.rating = new Rating(rating);
        ratingView.setImageDrawable(this.rating);
    }
}
