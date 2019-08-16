package com.example.sy2chu.fotagsy2chu;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    Rating topFilter;
    GridLayout gridView;
    ScrollView scrollView;
    PopupWindow popup;
    boolean showImages = false;
    private StorageReference mStorageRef;

    Vector<RatedImage> images;
    Vector<ImageView> ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        this.images = new Vector<RatedImage>();
        this.ratings = new Vector<ImageView>();
        this.scrollView = (ScrollView) findViewById(R.id.scrollView);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.top_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setBackgroundColor(Color.parseColor("#ffffffff"));
        ImageView imgView = myToolbar.findViewById(R.id.action_filter);
        setListeners();
        this.gridView = (GridLayout) findViewById(R.id.gridview);

        if(savedInstanceState == null){
            topFilter = new Rating();
            imgView.setImageDrawable(topFilter);
            this.addAll();
        }else{
            topFilter = new Rating(savedInstanceState.getInt("currRating"));
            Log.i("TAG", "topfiler " + topFilter.getRatingNum());
            imgView.setImageDrawable(topFilter);
            //dont copy directly into this.images, create images individually from info
            Vector<RatedImage> tempImages = (Vector<RatedImage>)savedInstanceState.getSerializable("images");
            for(int i=0;i<tempImages.size();i++){
                RatedImage old = tempImages.elementAt(i);
                Bitmap img = old.getImg();
                int rate = old.getRating().getRatingNum();

                RatedImage imageView = new RatedImage(this);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(800, 600);
                GridLayout.LayoutParams gParams = new GridLayout.LayoutParams(params);
                gParams.setGravity(Gravity.CENTER_HORIZONTAL);
                imageView.setLayoutParams(gParams);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                imageView.setBitmap(img);
                imageView.setRating(rate);

                this.images.add(imageView);

                ImageView rating = imageView.getRatingImage();
                ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(800, 100);
                GridLayout.LayoutParams gParams2 = new GridLayout.LayoutParams(params2);
                gParams2.setGravity(Gravity.CENTER_HORIZONTAL);
                rating.setLayoutParams(gParams2);
                ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) rating.getLayoutParams();
                marginParams.setMargins(0,50,0,50);

                this.ratings.add(rating);
            }

            this.showImages = savedInstanceState.getBoolean("showimages");
            this.gridView.setRowCount((int)Math.ceil((double)(images.size()*2)/gridView.getColumnCount()));
            loadIntoGrid();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle inst) {
        super.onSaveInstanceState(inst);
        this.gridView.removeAllViews();
        inst.putSerializable("images", images);
        inst.putSerializable("ratings", ratings);
        //put the showimages too, if false, dont draw
        //maybe put popup too?
        inst.putBoolean("showimages", showImages);
        inst.putInt("currRating", topFilter.getRatingNum());
    }

    private void setListeners() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.top_toolbar);
        ImageButton clear = (ImageButton) myToolbar.findViewById(R.id.action_clear);
        ImageButton filter = (ImageButton) myToolbar.findViewById(R.id.action_filter);
        ImageButton load = (ImageButton) myToolbar.findViewById(R.id.action_load);

        clear.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_DOWN){
                    showImages = false;
                    clearAll();
                }
                return true;
            }
        });

        filter.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                if(ev.getAction() == MotionEvent.ACTION_DOWN){
                    float x = ev.getX();
                    x = x - 30;
                    double dx = x / 70;
                    dx = Math.ceil(dx);
                    if(dx > 5){
                        dx = 5;
                    }

                    Rating rate = new Rating( (int) dx);
                    topFilter = rate;
                    ((ImageButton)findViewById(R.id.action_filter)).setImageDrawable(rate);
                }
                loadIntoGrid();
                return true;
            }
        });

        load.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                if(ev.getAction() == MotionEvent.ACTION_DOWN){
                    images.removeAllElements();
                    ratings.removeAllElements();
                    gridView.removeAllViews();
                    addAll();
                    showImages = true;
                    if(images.size() > 0){
                        loadIntoGrid();
                    }
                }
                return true;
            }
        });
    }

    //add children to the vectors, add to actual grid later
    private void addChild(String url) {
        Downloader loader = new Downloader();

        RatedImage imageView = new RatedImage(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(800, 600);
        GridLayout.LayoutParams gParams = new GridLayout.LayoutParams(params);
        gParams.setGravity(Gravity.CENTER_HORIZONTAL);
        imageView.setLayoutParams(gParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        loader.bindImageView(imageView);
        loader.execute(url);

        ImageView rating = imageView.getRatingImage();
        ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(800, 100);
        GridLayout.LayoutParams gParams2 = new GridLayout.LayoutParams(params2);
        gParams2.setGravity(Gravity.CENTER_HORIZONTAL);
        rating.setLayoutParams(gParams2);
        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) rating.getLayoutParams();
        marginParams.setMargins(0,50,0,50);

        images.add(imageView);
        ratings.add(rating);
    }

    private ImageView getPlaceHolder(){
        Bitmap empty = Bitmap.createBitmap(800,600,Bitmap.Config.ARGB_8888);
        ImageView placeholder = new ImageView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(800, 600);
        GridLayout.LayoutParams gParams = new GridLayout.LayoutParams(params);
        gParams.setGravity(Gravity.CENTER_HORIZONTAL);
        placeholder.setLayoutParams(gParams);
        placeholder.setScaleType(ImageView.ScaleType.CENTER_CROP);
        placeholder.setImageBitmap(empty);
        return placeholder;
    }

    public void loadIntoGrid() {
        if(!showImages){return;}
        this.gridView.removeAllViews();
        int filter = topFilter.getRatingNum();
        int cols = this.gridView.getColumnCount();

        int index = 0;
        int count = 0;
        int pointer = 0;

        for(int i=0;i<images.size();){
            index = 0;
            while(index < cols && pointer < images.size()){
                if(images.elementAt(pointer).getRating().getRatingNum() >= filter){
                    gridView.addView(images.elementAt(pointer));
                    images.elementAt(pointer).bringToFront();
                    index += 1;
                    count += 1;
                }
                pointer += 1;
            }
            pointer = i;
            index = 0;
            if(count % cols != 0){
                gridView.addView(getPlaceHolder());
            }
            while(index < cols && pointer < images.size()){
                if(images.elementAt(pointer).getRating().getRatingNum() >= filter){
                    gridView.addView(ratings.elementAt(pointer));
                    ratings.elementAt(pointer).bringToFront();
                    index += 1;
                }
                pointer += 1;
            }
            i = pointer;
        }
    }

    private void addAll() {
        for (String url : URLs) {
            addChild(url);
        }
    }

    private void clearAll() {
        //this.images.removeAllElements();
        this.gridView.removeAllViews();
    }

    public void initPopup(Bitmap img) {
        ImageView imgview = new ImageView(this);
        Bitmap scaled = getScaledBitmap(img, getScalingAmount(img));
        imgview.setImageBitmap(scaled);
        imgview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popup.dismiss();
                return false;
            }
        });
        this.popup = new PopupWindow(imgview, GridLayout.LayoutParams.WRAP_CONTENT,
                GridLayout.LayoutParams.WRAP_CONTENT,
                true);
        Log.i("TAG", "popup set");
        if(Build.VERSION.SDK_INT>=21){
            this.popup.setElevation(30);
        }
        if (gridView == null) {
            Log.i("TAG", "gridview null");
        }
        popup.showAtLocation(gridView,Gravity.CENTER,0,0);
        Log.i("TAG", "popup shown");
        dimTheBackground(popup, 0.7f);
    }

    private float getScalingAmount(Bitmap bm) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;

        FrameLayout.LayoutParams layoutParams =
                (FrameLayout.LayoutParams)this.gridView.getLayoutParams();
        int leftMargin = layoutParams.leftMargin;
        int rightMargin = layoutParams.rightMargin;

        int screenWidth = displayWidth - (leftMargin + rightMargin);

        return ( (float) screenWidth / (float) bm.getWidth() );
    }

    private Bitmap getScaledBitmap(Bitmap bm, float scale) {
        int scaleY = (int) (bm.getHeight() * scale);
        int scaleX = (int) (bm.getWidth() * scale);

        return Bitmap.createScaledBitmap(bm, scaleX, scaleY, true);
    }

    private void dimTheBackground(PopupWindow pop, float amount) {
        View parent = pop.getContentView().getRootView();
        Context context = pop.getContentView().getContext();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) parent.getLayoutParams();
        p.flags = (p.flags | WindowManager.LayoutParams.FLAG_DIM_BEHIND );
        p.dimAmount = (amount);
        wm.updateViewLayout(parent, p);
    }

    private String[] URLs = {
        "https://firebasestorage.googleapis.com/v0/b/android-6163b.appspot.com/o/01-620x.jpg?alt=media&token=bcb306ee-1615-457c-8c00-cdac5de4c3ff",
        "https://firebasestorage.googleapis.com/v0/b/android-6163b.appspot.com/o/1462345582002.jpg?alt=media&token=afda4706-d07b-4c49-a9c1-e696262e4738",
        "https://firebasestorage.googleapis.com/v0/b/android-6163b.appspot.com/o/1494181505729.jpg?alt=media&token=bfc9cabb-086a-4b69-9d05-ffb24ce357fb",
        "https://firebasestorage.googleapis.com/v0/b/android-6163b.appspot.com/o/DmQEHy0U8AEQMXU.jpg?alt=media&token=435357c5-73fc-45a4-b1f6-7836d70fa897"
    };
}
