package com.example.hossein.tmusic.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class PictureUtils {
    public static Bitmap getScalledBitmap (String path , int destWidth , int destHeight){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path , options);

        int srcWidth = options.outWidth;
        int srcHeigth = options.outHeight;
        int sampleSize = 1 ;

        if(srcWidth > destWidth || srcHeigth > destHeight){
            float heghtScalled = srcHeigth / destHeight;
            float widthScalled = srcWidth / destWidth;

            sampleSize = Math.round(heghtScalled > widthScalled ? heghtScalled : widthScalled);
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize ;
        return BitmapFactory.decodeFile(path , options);
    }

    public static Bitmap getScalledBitmap (String path , Activity activity){

        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        return getScalledBitmap(path , point.x , point.y);
    }

}
