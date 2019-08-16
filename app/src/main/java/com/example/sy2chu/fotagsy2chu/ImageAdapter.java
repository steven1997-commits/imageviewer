package com.example.sy2chu.fotagsy2chu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return URLs.length;
    } //set to 5 for now, CHANGE LATER!!!!!!!!!!!!!!!!!!!!!!!

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        /*ImageView imageView;

        imageView.setImageResource(mThumbIds[position]);
        return imageView;*/

        RatedImage imageView = null;
        Downloader loader = new Downloader();


        imageView = new RatedImage(mContext);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(800, 650));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);


        loader.bindImageView(imageView);
        loader.execute(URLs[position]);

        return imageView;
    }

    // references to our images
    private String[] URLs = {
           "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/bunny.jpg",
           "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/chinchilla.jpg",
            "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/doggo.jpg",
            "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/hamster.jpg",
            "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/husky.jpg",
            "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/kitten.png",
            "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/loris.jpg",
            "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/puppy.jpg",
            "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/redpanda.jpg",
            "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/sleepy.png"
    };
}
