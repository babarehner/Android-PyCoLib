package com.babarehner.android.pycolib.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry;

/**
 * Created by mike on 11/25/16.
 */

public class LibraryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = LibraryDbHelper.class.getSimpleName();

    public static final int DB_VERSION = 1;
    public static final String  DB_NAME = "PyLib.db";

    // Create the db
    public LibraryDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create the Tbooks table
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + LibraryEntry.TBOOKS
                + LibraryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LibraryEntry.COL_ISBN + " TEXT, "
                + LibraryEntry.Col_TITLE + " TEXT_NOT_NULL, "
                + LibraryEntry.COL_AUTHOR + " TEXT, "
                + LibraryEntry.COL_YEAR_PUBLISHED + " INTEGER, "
                + LibraryEntry.COL_CHECKED_OUT + " BOOLEAN;";

        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // TODO
    }
}
