package com.ac.assignment_project_014.audio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;


/**
 * This class is used to download Image from remote server .
 * @author Baoshan Li
 */
class DownloadImageHelper extends AsyncTask<String, Void, Bitmap> {
    /*
     * the image to download
     */
    ImageView bmImage;

    /**
     * the get the image
     */
    public DownloadImageHelper(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    /**
     * to download the image in background
     */
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bmp = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bmp = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bmp;
    }
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}