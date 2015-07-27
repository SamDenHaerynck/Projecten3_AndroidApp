package com.joetz.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.joetz.domain.Camp;
import com.joetz.fragments.main.CampsOverviewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for storing data into the SQLite database.
 */
public class CampsDataSource {
    // Database fields
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = { SQLiteHelper.COLUMN_ID,SQLiteHelper.COLUMN_CAMPID,
            SQLiteHelper.COLUMN_PERIOD, SQLiteHelper.COLUMN_TITLE, SQLiteHelper.COLUMN_CITY, SQLiteHelper.COLUMN_PLACE, SQLiteHelper.COLUMN_TRANSPORT,
            SQLiteHelper.COLUMN_MAXPARTICIPANTS, SQLiteHelper.COLUMN_REGISTRATIONS, SQLiteHelper.COLUMN_PRICE, SQLiteHelper.COLUMN_STARPRICE1, SQLiteHelper.COLUMN_STARPRICE2,
            SQLiteHelper.COLUMN_PHONE, SQLiteHelper.COLUMN_EMAIL, SQLiteHelper.COLUMN_EXTRAINFO, SQLiteHelper.COLUMN_ISDEDUCTIBLE,
            SQLiteHelper.COLUMN_PROMOTEXT, SQLiteHelper.COLUMN_ISFEATURED, SQLiteHelper.COLUMN_LOCATION, SQLiteHelper.COLUMN_MINIMUMAGE, SQLiteHelper.COLUMN_MAXIMUMAGE};

    public CampsDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        Log.w("close", "closing the datasource");
        dbHelper.close();
    }

    /**
     * This method puts the camps into the database.
     * @param camps
     */
    public void insertCamps(List<Camp> camps) {
        dbHelper.onUpgrade(database, 1, 1);
        for (Camp camp : camps) {
            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_CAMPID, camp.getId());
            values.put(SQLiteHelper.COLUMN_TITLE, camp.getTitle());
            values.put(SQLiteHelper.COLUMN_CITY, camp.getCity());
            values.put(SQLiteHelper.COLUMN_PERIOD, camp.getPeriod());
            values.put(SQLiteHelper.COLUMN_PROMOTEXT, camp.getPromotext());
            values.put(SQLiteHelper.COLUMN_ISDEDUCTIBLE, camp.getIsDeductible());
            values.put(SQLiteHelper.COLUMN_PLACE, camp.getPlace());
            values.put(SQLiteHelper.COLUMN_EXTRAINFO, camp.getExtraInfo());
            values.put(SQLiteHelper.COLUMN_MAXPARTICIPANTS, camp.getMaxParticipants());
            values.put(SQLiteHelper.COLUMN_REGISTRATIONS, camp.getRegistrations());
            values.put(SQLiteHelper.COLUMN_PRICE, camp.getPrice());
            values.put(SQLiteHelper.COLUMN_STARPRICE1, camp.getStarPrice1());
            values.put(SQLiteHelper.COLUMN_STARPRICE2, camp.getStarPrice2());
            values.put(SQLiteHelper.COLUMN_TRANSPORT, camp.getTransport());
            values.put(SQLiteHelper.COLUMN_ISFEATURED, camp.getIsFeatured());
            values.put(SQLiteHelper.COLUMN_LOCATION, camp.getLocation());
            values.put(SQLiteHelper.COLUMN_MINIMUMAGE, camp.getMinimumAge());
            values.put(SQLiteHelper.COLUMN_MAXIMUMAGE, camp.getMaximumAge());
            long insertId = database.insert(SQLiteHelper.TABLE_CAMPS, null, values);
            Cursor cursor = database.query(SQLiteHelper.TABLE_CAMPS, allColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
            cursor.moveToFirst();
            cursor.close();
        }
    }

    /**
     * Returns all camps without filtering.
     * @return
     */
    public List<Camp> getAllCamps() {
        return cursorToList(database.query(SQLiteHelper.TABLE_CAMPS, allColumns, null, null, null, null, null));
    }

    /**
     * Returns all camps that are featured.
     * @return
     */
    public List<Camp> getFeaturedCamps() {
        return cursorToList(database.query(SQLiteHelper.TABLE_CAMPS, null, "isFeatured = '1'", null, null, null, null));
    }

    protected String getConstraintsTitleSeason(String title, String season) {
        String constraints = "";
        if(title != null) constraints = "title LIKE '%" + title + "%'";
        if(!season.equals("") && !season.equals(CampsOverviewFragment.seasons[0])) {
            if(!constraints.equals("")) constraints += " AND ";
            constraints += " period LIKE '" + season + "'";
        }
        return constraints;
    }

    /**
     * This method return a list of camps when the age parameter is not specified.
     * @param title
     * @param season
     * @return
     */
    public List<Camp> filterCamps(String title, String season) {
        return cursorToList(database.query(SQLiteHelper.TABLE_CAMPS, null, getConstraintsTitleSeason(title, season), null, null, null, null));
    }

    /**
     * This method returns a list of camps based on certain filter values
     * @param title
     * @param season
     * @param age
     * @return
     */
    public List<Camp> filterCamps(String title, String season, int age) {
        String constraints = getConstraintsTitleSeason(title, season);
        if(constraints != null && !constraints.equals("") && age != -2) constraints += " AND ";
        if(age != -2) constraints += age + " BETWEEN minimumAge AND maximumAge";
        return cursorToList(database.query(SQLiteHelper.TABLE_CAMPS, null, constraints, null, null, null, null));
    }

    /**
     * This method turns a Cursor object into a List<Camp> object
     * @param cursor
     * @return
     */
    private List<Camp> cursorToList(Cursor cursor) {
        List<Camp> camps = new ArrayList();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Camp camp = cursorToCamp(cursor);
            camps.add(camp);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return camps;
    }

    /**
     * This method turns a Cursor object into a Camp object.
     * @param cursor
     * @return
     */
    private Camp cursorToCamp(Cursor cursor) {
        Camp camp = new Camp();
        camp.setId((int) cursor.getLong(1));
        camp.setPeriod(cursor.getString(2));
        camp.setTitle(cursor.getString(3));
        camp.setCity(cursor.getString(4));
        camp.setPlace(cursor.getString(5));
        camp.setTransport(cursor.getString(6));
        camp.setMaxParticipants(cursor.getInt(7));
        camp.setRegistrations(cursor.getInt(8));
        camp.setPrice(cursor.getDouble(9));
        camp.setStarPrice1(cursor.getDouble(10));
        camp.setStarPrice2(cursor.getDouble(11));
        camp.setExtraInfo(cursor.getString(14));
        camp.setIsDeductible(cursor.getInt(15));
        camp.setPromotext(cursor.getString(16));
        camp.setIsFeatured(cursor.getInt(17));
        camp.setLocation(cursor.getString(18));
        camp.setMinimumAge(cursor.getInt(19));
        camp.setMaximumAge(cursor.getInt(20));
        return camp;
    }
}
