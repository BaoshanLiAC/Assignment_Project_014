package com.ac.assignment_project_014.TicketMaster;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
 * A simple {@link Fragment} subclass
 * creat an instance of this fragment
 */
public class EventDetailFragment extends Fragment {

    private boolean isTablet;
    private boolean isSaved;
    private long id;
    private String eventName;
    private String eventDate;
    private String eventMinPrice;
    private String eventMaxPrice;
    private String eventURL;
    private String eventImage;

    private ProgressBar progressBar;
    private ImageView imgPromotion;
    private Bundle dataFromActivity;
    private AppCompatActivity parentActivity;

    /**
     * no-arg constructor
     */
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
        dataFromActivity = getArguments();
       // if (getArguments() != null) {
            isTablet = dataFromActivity.getBoolean(TicketMasterMainActivity.IS_TABLET);
            isSaved = dataFromActivity.getBoolean(TicketMasterMainActivity.IS_FAVORITE);
            id = dataFromActivity.getLong(TicketMasterMainActivity.EVENT_ID, -1L);
            eventName = dataFromActivity.getString(TicketMasterMainActivity.EVENT_NAME);
            eventDate = dataFromActivity.getString(TicketMasterMainActivity.EVENT_DATE);
            eventMinPrice = dataFromActivity.getString(TicketMasterMainActivity.EVENT_MIN_PRICE);
            eventMaxPrice = dataFromActivity.getString(TicketMasterMainActivity.EVENT_MAX_PRICE);
            eventURL = dataFromActivity.getString(TicketMasterMainActivity.EVENT_URL);
            eventImage = dataFromActivity.getString(TicketMasterMainActivity.EVENT_IMAGE);
        //}
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ticketmaster_fragment_event_details, container, false);

        progressBar = view.findViewById(R.id.processBar);
        imgPromotion = view.findViewById(R.id.fg_event_image);

        TextView fgEventName = view.findViewById(R.id.fg_event_name);
        fgEventName.setText(R.string.ticketmaster_name + eventName);

        TextView fgEventDate = view.findViewById(R.id.fg_event_date);
        fgEventDate.setText(R.string.ticketmaster_date + eventDate);

        TextView fgMinPrice = view.findViewById(R.id.fg_event_min_price);
        fgMinPrice.setText(R.string.ticketmaster_min_price + eventMinPrice);

        TextView fgMaxPrice = view.findViewById(R.id.fg_event_max_price);
        fgMaxPrice.setText(R.string.ticketmaster_max_price + eventMaxPrice);

        TextView fgURL = view.findViewById(R.id.fg_event_url);
        fgURL.setText(R.string.ticketmaster_url+eventURL);

        GetImage getImage = new GetImage();
        getImage.execute(eventImage);

        Button btn = view.findViewById(R.id.btn_add_remove);
        if(isSaved)
            btn.setText(R.string.ticketmaster_event_remove_from_fav);
        else
            btn.setText(R.string.ticketmaster_event_add_to_fav);

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



    private void removeSavedEvent(SQLiteDatabase db, Button button) {
        int count = db.delete(TicketMasterMainActivity.EVENT_SAVED_TABLE, TicketMasterMainActivity.COL_ID + " = ?", new String[]{String.valueOf(id)});
        if (count > 0) {
            Snackbar snackbar = Snackbar.make(this.imgPromotion,
                    R.string.ticketmaster_remove_success,
                    Snackbar.LENGTH_LONG);
            button.setEnabled(false);
            snackbar.show();

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

    private void save(SQLiteDatabase db, Button button) {
        ContentValues newRowValue = new ContentValues();
        newRowValue.put(TicketMasterMainActivity.COL_NAME, eventName);
        newRowValue.put(TicketMasterMainActivity.COL_DATE, eventDate);
        newRowValue.put(TicketMasterMainActivity.COL_MINPRICE, eventMinPrice);
        newRowValue.put(TicketMasterMainActivity.COL_MAXPRICE, eventMaxPrice);
        newRowValue.put(TicketMasterMainActivity.COL_URL, eventURL);
        newRowValue.put(TicketMasterMainActivity.COL_IMAGE, eventImage);

        long newId = db.insert(TicketMasterMainActivity.EVENT_SAVED_TABLE, null, newRowValue);
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

    /**
     * a class used to get image from the server
     */
    class GetImage extends AsyncTask<String, Integer, String> {

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
