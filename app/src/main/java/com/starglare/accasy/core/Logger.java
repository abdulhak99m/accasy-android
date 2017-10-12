package com.starglare.accasy.core;

/**
 * Created by MuhammadAmin on 7/31/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.starglare.accasy.models.ReportModel;

public class Logger extends SQLiteOpenHelper {

    static Context context;
    private static Logger instance = null;
    SQLiteDatabase db;
    public int lastInsertId;



    public static class ReportEntry implements BaseColumns {
        public static final String TABLE_NAME = "report";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_SUB_CATEGORY = "subcategory";
        public static final String COLUMN_NAME_COORDINATES = "coordinates";
        public static final String COLUMN_NAME_COMMENT = "comment";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phone_number";
        public static final String COLUMN_NAME_POSTED = "posted";
        public static final String COLUMN_NAME_TIME_STAMP = "time_stamp";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " +  TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_CATEGORY + " TEXT," +
                        COLUMN_NAME_COORDINATES + " TEXT,"+
                        COLUMN_NAME_SUB_CATEGORY + " TEXT,"+
                        COLUMN_NAME_COMMENT + " TEXT,"+
                        COLUMN_NAME_IMAGE + " BLOB,"+
                        COLUMN_NAME_PHONE_NUMBER + " TEXT,"+
                        COLUMN_NAME_POSTED + " INTEGER,"+
                        COLUMN_NAME_TIME_STAMP + " LONG);";
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }




    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "app.db"; //context.getString(R.string.db_name);
    public static final int MESSAGE_ENTRY = 1;


    private Logger(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static Logger getInstance(Context context) {
        if(instance == null) {
            instance = new Logger(context);
            return instance;
        }

        return instance;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ReportEntry.SQL_CREATE_ENTRIES);
        Log.i("Logger:","tables created");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(ReportEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
        Log.i("Logger","Database upgraded");
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    /***************************INSERT*********************************/

    public long insertReport( ReportModel reportModel) {
        // Gets the data repository in write mode
        db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ReportEntry.COLUMN_NAME_CATEGORY, reportModel.getCategory());
        values.put(ReportEntry.COLUMN_NAME_SUB_CATEGORY, reportModel.getSubCategory());
        values.put(ReportEntry.COLUMN_NAME_COORDINATES, reportModel.getCoordinates());
        values.put(ReportEntry.COLUMN_NAME_COMMENT, reportModel.getComment());
        values.put(ReportEntry.COLUMN_NAME_IMAGE, reportModel.getImage());
        values.put(ReportEntry.COLUMN_NAME_PHONE_NUMBER, reportModel.getPhoneNumber());
        values.put(ReportEntry.COLUMN_NAME_POSTED, 0);
        values.put(ReportEntry.COLUMN_NAME_TIME_STAMP, reportModel.getTime());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ReportEntry.TABLE_NAME, null, values);
        Log.i("Logger","Report insert successfull");
        lastInsertId = (int)newRowId;
        return newRowId;
    }

    /**************************RETRIEVE******************************/
    public Cursor selectAllReports() {
        db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ReportEntry._ID,
                ReportEntry.COLUMN_NAME_CATEGORY,
                ReportEntry.COLUMN_NAME_SUB_CATEGORY,
                ReportEntry.COLUMN_NAME_COORDINATES,
                ReportEntry.COLUMN_NAME_COMMENT,
                ReportEntry.COLUMN_NAME_IMAGE,
                ReportEntry.COLUMN_NAME_PHONE_NUMBER,
                ReportEntry.COLUMN_NAME_POSTED,
                ReportEntry.COLUMN_NAME_TIME_STAMP
        };


        // How you want the results sorted in the resulting Cursor
        String sortOrder =   ReportEntry._ID + " DESC";

        Cursor cursor = db.query(
                ReportEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return cursor;
    }

    public Cursor selectReportNotSentToServer() {
        db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ReportEntry._ID,
                ReportEntry.COLUMN_NAME_CATEGORY,
                ReportEntry.COLUMN_NAME_SUB_CATEGORY,
                ReportEntry.COLUMN_NAME_COORDINATES,
                ReportEntry.COLUMN_NAME_COMMENT,
                ReportEntry.COLUMN_NAME_IMAGE,
                ReportEntry.COLUMN_NAME_PHONE_NUMBER,
                ReportEntry.COLUMN_NAME_POSTED,
                ReportEntry.COLUMN_NAME_TIME_STAMP
        };


        // How you want the results sorted in the resulting Cursor
        String sortOrder =   ReportEntry._ID + " DESC";
        String where = ReportEntry.COLUMN_NAME_POSTED + " = ?";
        String[] values = {"0"};

        Cursor cursor = db.query(
                ReportEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                where,                                // The columns for the WHERE clause
                values,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return cursor;

    }


    /**************************UPDATE******************************/

    public int updateReport(String id) {
        db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReportEntry.COLUMN_NAME_POSTED, 1);

        String selection = ReportEntry._ID + " = ?" ;
        String[] selectionArgs = { id };

        // Insert the new row, returning the primary key value of the new row
        int rowsAffected = db.update(ReportEntry.TABLE_NAME, values, selection, selectionArgs);
        return rowsAffected;
    }

    /**************************DELETE******************************/
    public void delete(String id) {
        db = getWritableDatabase();
        // Define 'where' part of query.
        String selection =  ReportEntry._ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { id };
        // Issue SQL statement.
        db.delete( ReportEntry .TABLE_NAME, selection, selectionArgs);
    }

}
