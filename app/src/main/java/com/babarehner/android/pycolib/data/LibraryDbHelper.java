package com.babarehner.android.pycolib.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry;

import java.util.ArrayList;
import java.util.List;

import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.COL_F_NAME;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.COL_L_NAME;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.COL_TITLE;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.TBOOKS;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.TPYTHONISTAS;

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
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + TBOOKS
                + "("
                + LibraryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LibraryEntry.COL_TITLE + " TEXT_NOT_NULL, "
                + LibraryEntry.COL_AUTHOR + " TEXT, "
                + LibraryEntry.COL_YEAR_PUBLISHED + " INTEGER, "
                + LibraryEntry.COL_BORROWER + " TEXT);";
                // + LibraryEntry.COL_CHECKED_OUT + " BOOLEAN;";

        String SQL_CREATE_PYTHONISTAS_TABLE = "CREATE TABLE " + TPYTHONISTAS
                + "("
                + LibraryEntry._IDP + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_F_NAME + " TEXT_NOT_NULL, "
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

    // get data for name spinner
    public List<String > getNames(){
        List<String> names = new ArrayList<String>();
        String fNameQuery = "SELECT " + COL_F_NAME + "," + COL_L_NAME + " FROM " + TPYTHONISTAS + " ;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(fNameQuery, null);
        String name;
        if (c.moveToFirst()) {
            do {
                name = c.getString(1) + ", " + c.getString(0);
                names.add(name);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return names;
    }

    // get data for book spinner
    public List<String > getBooks(){
        List<String> titles = new ArrayList<String>();
        String fNameQuery = "SELECT " + COL_TITLE + " FROM " + TBOOKS + " ;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(fNameQuery, null);
        if (c.moveToFirst()) {
            do {
                titles.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return titles;
    }


}
