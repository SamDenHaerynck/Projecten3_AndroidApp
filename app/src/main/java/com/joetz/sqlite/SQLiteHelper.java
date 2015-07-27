package com.joetz.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class is responsible for creating the table in the database.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    //Table Camps
    public static final String TABLE_CAMPS = "camps";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CAMPID = "campId";
    public static final String COLUMN_PERIOD = "period";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_PLACE = "place";
    public static final String COLUMN_TRANSPORT = "transport";
    public static final String COLUMN_MAXPARTICIPANTS = "maxParticipants";
    public static final String COLUMN_REGISTRATIONS = "registrations";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_STARPRICE1 = "starPrice1";
    public static final String COLUMN_STARPRICE2 = "starPrice2";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_EXTRAINFO = "extraInfo";
    public static final String COLUMN_ISDEDUCTIBLE = "isDeductible";
    public static final String COLUMN_PROMOTEXT = "promotext";
    public static final String COLUMN_ISFEATURED = "isFeatured";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_MINIMUMAGE = "minimumAge";
    public static final String COLUMN_MAXIMUMAGE = "maximumAge";

    private static final String DATABASE_NAME = "joetz.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "create table " + TABLE_CAMPS + "(" + COLUMN_ID
            + " integer primary key, " + COLUMN_CAMPID + " integer unique, " + COLUMN_PERIOD + " text, " + COLUMN_TITLE
            + " text, " + COLUMN_CITY + " text, " + COLUMN_PLACE + " text, " + COLUMN_TRANSPORT + " text, " + COLUMN_MAXPARTICIPANTS
            + " integer, " + COLUMN_REGISTRATIONS + " integer, " + COLUMN_PRICE + " numeric, " + COLUMN_STARPRICE1 + " numeric, " + COLUMN_STARPRICE2
            + " numeric, " + COLUMN_PHONE + " text, " + COLUMN_EMAIL + " text, " + COLUMN_EXTRAINFO
            + " text, " + COLUMN_ISDEDUCTIBLE + " numeric, " + COLUMN_PROMOTEXT + " TEXT, " + COLUMN_ISFEATURED + " integer, " + COLUMN_LOCATION + " text, " + COLUMN_MINIMUMAGE + " integer, " + COLUMN_MAXIMUMAGE + " integer);";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPS);
        onCreate(db);
    }
}
