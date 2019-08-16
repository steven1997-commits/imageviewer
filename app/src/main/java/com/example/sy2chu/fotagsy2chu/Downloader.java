package com.example.sy2chu.fotagsy2chu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

public class Downloader extends AsyncTask<String, Void, Bitmap> {

    RatedImage image;

    Downloader() {

    }

    @Override
    protected Bitmap doInBackground(String... URLs) {
        String theURL = URLs[0];
        Bitmap bitmap = null;

        try{
            InputStream input = new java.net.URL(theURL).openStream();
            bitmap = BitmapFactory.decodeStream(input);
        }catch (Exception e){
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap res) {
        image.setBitmap(res);
    }

    public void bindImageView(RatedImage view) {
        this.image = view;
    }
}
