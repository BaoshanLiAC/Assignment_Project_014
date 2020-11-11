package com.ac.assignment_project_014.TicketMaster;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ac.assignment_project_014.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EventDetailFragment extends Fragment {

    public static final String KEY_IS_TABLET = "IS_TABLET";
    public static final String KEY_IS_FAVORITE = "IS_FAV";
    public static final String KEY_EVENT_ID = "EVENT_ID";
    public static final String KEY_EVENT_NAME = "EVENT_NAME";
    public static final String KEY_EVENT_DATE = "EVENT_DATE";
    public static final String KEY_EVENT_MIN_PRICE = "EVENT_MINPRICE";
    public static final String KEY_EVENT_MAX_PRICE = "EVENT_MAXPRICE";
    public static final String KEY_EVENT_URL = "EVENT_URL";
    public static final String KEY_EVENT_IMAGE = "EVENT_IMAGE";

    private boolean isTablet;
    private boolean isFav;
    private long id;
    private String eventName;
    private String eventDate;
    private String eventMinPrice;
    private String eventMaxPrice;
    private String eventURL;
    private String eventImage;

    private ProgressBar progressBar;
    private ImageView imgPromotion;

    private AppCompatActivity parentActivity;

    public EventDetailFragment() {

    }

    /**
     * used to communicate with other activity
     * reference to： https://developer.android.com/training/basics/fragments/communicating.html
     */
    private OnRemoveFavoriteEventListener callback;

    public void setCallback(OnRemoveFavoriteEventListener callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isTablet = getArguments().getBoolean(KEY_IS_TABLET);
            isFav = getArguments().getBoolean(KEY_IS_FAVORITE);
            id = getArguments().getLong(KEY_EVENT_ID, -1L);
            eventName = getArguments().getString(KEY_EVENT_NAME);
            eventDate = getArguments().getString(KEY_EVENT_DATE);
            eventMinPrice = getArguments().getString(KEY_EVENT_MIN_PRICE);
            eventMaxPrice = getArguments().getString(KEY_EVENT_MAX_PRICE);
            eventURL = getArguments().getString(KEY_EVENT_URL);
            eventImage = getArguments().getString(KEY_EVENT_IMAGE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ticketmaster_fragment_event_details, container, false);

        progressBar = view.findViewById(R.id.processBar);
        imgPromotion = view.findViewById(R.id.fg_event_image);

        TextView fgEventName = view.findViewById(R.id.fg_event_name);
        fgEventName.setText(String.format(getString(R.string.ticketmaster_name), eventName));

        TextView fgEventDate = view.findViewById(R.id.fg_event_date);
        fgEventDate.setText(String.format(getString(R.string.ticketmaster_date), eventDate));

        TextView fgMinPrice = view.findViewById(R.id.fg_event_min_price);
        fgMinPrice.setText(String.format(getString(R.string.ticketmaster_min_price), eventMinPrice));

        TextView fgMaxPrice = view.findViewById(R.id.fg_event_max_price);
        fgMaxPrice.setText(String.format(getString(R.string.ticketmaster_max_price), eventMaxPrice));

        TextView fgURL = view.findViewById(R.id.fg_event_url);
        fgURL.setText(String.format(getString(R.string.ticketmaster_url), eventURL));


        Button btnAddRemove = view.findViewById(R.id.btn_add_remove);
        btnAddRemove.setText(isFav ? R.string.ticketmaster_event_remove_from_fav : R.string.ticketmaster_event_add_to_fav);
        btnAddRemove.setOnClickListener(v -> {
            EventOpenHelper eventDB = new EventOpenHelper(parentActivity);
            SQLiteDatabase db = eventDB.getWritableDatabase();

            if (isFav) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(parentActivity);
                alertDialog.setTitle(R.string.ticketmaster_confirm).setMessage(R.string.ticketmaster_remove_confirm
                ).setPositiveButton(R.string.ticketmaster_yes,(click, arg)->{
                    removeFavoriteEvent(db, btnAddRemove);
                }).setNegativeButton(R.string.ticketmaster_no,(click, arg)->{
                }).create().show();
            } else {
                saveToFavorite(db, btnAddRemove);
            }
        });

        progressBar.setVisibility(View.VISIBLE);

        GetImage getImage = new GetImage();
        getImage.execute(eventImage);

        return view;
    }

    private void saveToFavorite(SQLiteDatabase db, Button button) {
        ContentValues newRowValue = new ContentValues();
        newRowValue.put(Event.COL_NAME, eventName);
        newRowValue.put(Event.COL_DATE, eventDate);
        newRowValue.put(Event.COL_MINPRICE, eventMinPrice);
        newRowValue.put(Event.COL_MAXPRICE, eventMaxPrice);
        newRowValue.put(Event.COL_URL, eventURL);
        newRowValue.put(Event.COL_IMAGE, eventImage);

        long newId = db.insert(Event.TABLE_NAME_FAVORITE, null, newRowValue);
        Snackbar snackbar;
        if (newId >= 0) {
            snackbar = Snackbar.make(this.imgPromotion,
                    R.string.ticketmaster_save_success,
                    Snackbar.LENGTH_LONG);
            if (!isTablet) {
                snackbar.setAction(R.string.ticketmaster_go_to_fav, v -> {
                    Intent intent = new Intent(parentActivity, TicketMasterSavedEventsActivity.class);
                    parentActivity.startActivity(intent);
                });
            }

            button.setEnabled(false);
        } else {
            snackbar = Snackbar.make(this.imgPromotion,
                    R.string.ticketmaster_save_fail,
                    Snackbar.LENGTH_LONG);
        }
        snackbar.show();
    }

    private void removeFavoriteEvent(SQLiteDatabase db, Button button) {
        int count = db.delete(Event.TABLE_NAME_FAVORITE, Event.COL_ID + " = ?", new String[]{String.valueOf(id)});
        if (count > 0) {
            Snackbar snackbar = Snackbar.make(this.imgPromotion,
                    R.string.ticketmaster_remove_success,
                    Snackbar.LENGTH_LONG);
            if (!isTablet) {
                snackbar.setAction(R.string.ticketmaster_go_to_fav, (btn) -> {
                    parentActivity.finish();
                });
            }
            snackbar.show();
            button.setEnabled(false);

            // update parent activity if it is tablet
            if (this.callback != null) {
                callback.removeEvent(id);
            }
        } else {
            Snackbar snackbar = Snackbar.make(this.imgPromotion,
                    R.string.ticketmaster_remove_fail,
                    Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public interface OnRemoveFavoriteEventListener {
        void removeEvent(long eventId);
    }

    /**
     * get MD5 of a string
     * reference to: https://stackoverflow.com/questions/13152736/how-to-generate-an-md5-checksum-for-a-file-in-android
     */
    public static String getMD5EncryptedString(String encTarget){
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");

            mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
            String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
            while ( md5.length() < 32 ) {
                md5 = "0"+md5;
            }
            return md5;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
        } // Encryption algorithm
        return "";
    }


    class GetImage extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            String localFile = getMD5EncryptedString(url) + ".jpg";

            try {
                if (fileExistance(localFile)) {
                    return loadFromLocalStorage(localFile);
                } else {
                    return loadImageFromRemote(url, localFile);
                }
            } catch(IOException ioe) {
                Log.e("DSS", "Failed to load image.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                imgPromotion.setImageBitmap(bitmap);
            }
            progressBar.setVisibility(View.INVISIBLE);
        }

        public boolean fileExistance(String fname) {
            Log.i("DSS", "Image file to find: " + fname);
            File file = parentActivity.getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        private Bitmap loadImageFromRemote(String sUrl, String imagefile) throws IOException {
            Bitmap bm = null;

            URL url = new URL(sUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                bm = BitmapFactory.decodeStream(connection.getInputStream());
                saveToLocalStorage(imagefile, bm);
            }
            return bm;
        }

        private void saveToLocalStorage(String imagefile, Bitmap image) throws IOException {
            FileOutputStream outputStream = parentActivity.openFileOutput(imagefile, Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            outputStream.flush();
            outputStream.close();
        }

        private Bitmap loadFromLocalStorage(String imagefile) throws IOException {
            Log.i("WF", "Found in local storage, use it directly");
            try(FileInputStream fis =  parentActivity.openFileInput(imagefile)) {
                return BitmapFactory.decodeStream(fis);
            } catch (FileNotFoundException e) {
                Log.e("DSS", "File not found: " + imagefile);
            }
            return null;
        }
    }


}
