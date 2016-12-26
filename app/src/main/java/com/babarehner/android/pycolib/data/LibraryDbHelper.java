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

    // SQL statement to create the Tbooks table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + LibraryEntry.TBOOKS
                + "("
                + LibraryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LibraryEntry.COL_TITLE + " TEXT_NOT_NULL, "
                + LibraryEntry.COL_AUTHOR + " TEXT, "
                + LibraryEntry.COL_YEAR_PUBLISHED + " INTEGER, "
                + LibraryEntry.COL_BORROWER + " TEXT);";
                // + LibraryEntry.COL_CHECKED_OUT + " BOOLEAN;";

        String SQL_CREATE_PYTHONISTAS_TABLE = "CREATE TABLE " + LibraryEntry.TPYTHONISTAS
                + "("
                + LibraryEntry._IDP + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LibraryEntry.COL_F_NAME + " TEXT_NOT_NULL, "
                + LibraryEntry.COL_L_NAME + " TEXT_NOT_NULL, "
                + LibraryEntry.COL_PHONE + " TEXT, "
                + LibraryEntry.COL_EMAIL + " TEXT);";

        String SQL_CREATE_LOAN_TABLE = "CREATE TABLE " + LibraryEntry.TLOANED
                + "("
                + LibraryEntry._IDL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LibraryEntry.COL_TITLE_ID + " LONG NOT NULL, "
                + LibraryEntry.COL_NAME_ID + " LONG NOT NULL, "
                + LibraryEntry.COL_LOAN_DATE + " DATE NOT NULL, "
                + LibraryEntry.COL_RETURN_DATE + " DATE);";


        db.execSQL(SQL_CREATE_BOOKS_TABLE);
        db.execSQL(SQL_CREATE_PYTHONISTAS_TABLE);
        db.execSQL(SQL_CREATE_LOAN_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // TODO
    }
}
