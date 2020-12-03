package com.ac.assignment_project_014.TicketMaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple Fragment subclass
 * Create an instance of this fragment
 */
public class EventDetailFragment extends Fragment {

    /**
     * if the device is tablet
     */
    private boolean isTablet;

    /**
     * if the event is saved
     */
    private boolean isSaved;

    /**
     * event id
     */
    private long id;

    /**
     * event name
     */
    private String eventName;

    /**
     * event date
     */
    private String eventDate;

    /**
     * min price of the event
     */
    private String eventMinPrice;

    /**
     * max price of the event
     */
    private String eventMaxPrice;

    /**
     * url of the event
     */
    private String eventURL;

    /**
     * image of the event
     */
    private String eventImage;

    /**
     * ProgressBar to show when loading
     */
    private ProgressBar progressBar;

    /**
     * image view of the event
     */
    private ImageView imgPromotion;

    private Bundle dataFromActivity;

    private AppCompatActivity parentActivity;

    /**
     * communicate with the saved event activity
     * reference： https://stackoverflow.com/questions/32346704/how-to-communicate-between-fragments
     */
    private CallBack myCallBack;

    //defining interface
    public interface CallBack {
        void removeEvent(long eventId);
    }

    public void setMyCallBack(CallBack myCallBack) {
        this.myCallBack = myCallBack;
    }



    /**
     * called when this fragment is loaded
     * @param inflater
     * @param container
     * @param savedInstanceState the current saved stated
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        isTablet = dataFromActivity.getBoolean(TicketMasterMainActivity.IS_TABLET);
        isSaved = dataFromActivity.getBoolean(TicketMasterMainActivity.IS_FAVORITE);
        id = dataFromActivity.getLong(TicketMasterMainActivity.EVENT_ID, -1L);
        eventName = dataFromActivity.getString(TicketMasterMainActivity.EVENT_NAME);
        eventDate = dataFromActivity.getString(TicketMasterMainActivity.EVENT_DATE);
        eventMinPrice = dataFromActivity.getString(TicketMasterMainActivity.EVENT_MIN_PRICE);
        eventMaxPrice = dataFromActivity.getString(TicketMasterMainActivity.EVENT_MAX_PRICE);
        eventURL = dataFromActivity.getString(TicketMasterMainActivity.EVENT_URL);
        eventImage = dataFromActivity.getString(TicketMasterMainActivity.EVENT_IMAGE);

        View view = inflater.inflate(R.layout.ticketmaster_fragment_event_details, container, false);

        progressBar = view.findViewById(R.id.processBar);
        imgPromotion = view.findViewById(R.id.fg_event_image);

        TextView fgEventName = view.findViewById(R.id.fg_event_name);
        fgEventName.setText(getString(R.string.ticketmaster_name) + eventName);

        TextView fgEventDate = view.findViewById(R.id.fg_event_date);
        fgEventDate.setText(getString(R.string.ticketmaster_date) + eventDate);

        TextView fgMinPrice = view.findViewById(R.id.fg_event_min_price);
        fgMinPrice.setText(getString(R.string.ticketmaster_min_price) + eventMinPrice);

        TextView fgMaxPrice = view.findViewById(R.id.fg_event_max_price);
        fgMaxPrice.setText(getString(R.string.ticketmaster_max_price) + eventMaxPrice);

        TextView fgURL = view.findViewById(R.id.fg_event_url);
        fgURL.setText(getString(R.string.ticketmaster_url) +eventURL);

        GetImage getImage = new GetImage();
        getImage.execute(eventImage);

        Button btn = view.findViewById(R.id.btn_add_remove);
        if(isSaved)
            btn.setText(getString(R.string.ticketmaster_event_remove_from_saved));
        else
            btn.setText(getString(R.string.ticketmaster_event_add_to_saved));

        btn.setOnClickListener(v -> {
            EventOpenHelper eventDB = new EventOpenHelper(parentActivity);
            SQLiteDatabase db = eventDB.getWritableDatabase();

            if (isSaved) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(parentActivity);
                alertDialog.setTitle(R.string.ticketmaster_confirm).setMessage(R.string.ticketmaster_remove_confirm
                ).setPositiveButton(R.string.ticketmaster_yes,(click, arg)->{
                    removeSavedEvent(db, btn);
                }).setNegativeButton(R.string.ticketmaster_no,(click, arg)->{
                }).create().show();
            } else {
                save(db, btn);
            }
        });

        return view;
    }


    /**
     *
     * @param context the current activity context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }


    /**
     * method to remove the saved event from database
     * @param db
     * @param button
     */
    private void removeSavedEvent(SQLiteDatabase db, Button button) {
        db.delete(TicketMasterMainActivity.EVENT_SAVED_TABLE, TicketMasterMainActivity.COL_ID + " = ?", new String[]{String.valueOf(id)});

        Snackbar snackbar = Snackbar.make(this.imgPromotion,
                R.string.ticketmaster_remove_success,
                Snackbar.LENGTH_LONG);
        button.setEnabled(false);
        snackbar.show();

        //update parent activity
        if (this.myCallBack != null) {
            myCallBack.removeEvent(id);
        }

    }

    /**
     * method to save event to the database
     * @param db
     * @param button
     */
    private void save(SQLiteDatabase db, Button button) {
        ContentValues newRowValue = new ContentValues();
        newRowValue.put(TicketMasterMainActivity.COL_NAME, eventName);
        newRowValue.put(TicketMasterMainActivity.COL_DATE, eventDate);
        newRowValue.put(TicketMasterMainActivity.COL_MINPRICE, eventMinPrice);
        newRowValue.put(TicketMasterMainActivity.COL_MAXPRICE, eventMaxPrice);
        newRowValue.put(TicketMasterMainActivity.COL_URL, eventURL);
        newRowValue.put(TicketMasterMainActivity.COL_IMAGE, eventImage);

        db.insert(TicketMasterMainActivity.EVENT_SAVED_TABLE, null, newRowValue);
        Snackbar snackbar = Snackbar.make(this.imgPromotion,
                R.string.ticketmaster_save_success,
                Snackbar.LENGTH_LONG);
        button.setEnabled(false);
        snackbar.show();
    }

    /**
     * a class used to get image from the server
     */
    private class GetImage extends AsyncTask<String, Integer, String> {

        /**
         * the image that will get from the server
         */
        private Bitmap image = null;

        /**
         * get image form the server in background
         * @param strings url
         * @return "done" if succeed
         */
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL imgUrl = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) imgUrl.openConnection();
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "done";
        }

        @Override
        public void onProgressUpdate(Integer...value){
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);

        }

        @Override
        public void onPostExecute(String fromDoInBackground)
        {
            imgPromotion.setImageBitmap(image);

        }
    }


}
