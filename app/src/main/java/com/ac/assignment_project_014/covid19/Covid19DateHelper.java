package com.ac.assignment_project_014.covid19;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class Covid19DateHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "COVID19-DATA";
    private static int VERSION_NUM = 1;
    public static final String TABLE_NAME = "countrycase";
    public static final String KEY_ID = "id";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_TOTAL ="total";
    public static final String KEY_DAILY ="daily";
    public static final String KEY_QUERY_DATE ="date";
    public static final String KEY_DATA ="data";
    public static final String SQL_CREATE_TABLE =  "CREATE TABLE " + TABLE_NAME + " (" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_COUNTRY + " TEXT, " +
            KEY_TOTAL + " NUMERIC, " +
            KEY_DAILY + " NUMERIC, " +
            KEY_QUERY_DATE + " TEXT, " +
            KEY_DATA + " BLOB" +
            ")";
    private String DC_TAG = Covid19DateHelper.class.getSimpleName();
    public SQLiteDatabase database;

    public Covid19DateHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        Log.i(DC_TAG, "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i(DC_TAG,"Calling onUpgrade, oldVersion=" + oldVersion + "newVersion=" +newVersion);
    }

}
