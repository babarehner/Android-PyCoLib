package com.babarehner.android.pycolib.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.babarehner.android.pycolib.QueryCaddy;
import com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry;

import java.util.ArrayList;
import java.util.List;

import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.COL_F_NAME;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.COL_LOAN_DATE;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.COL_L_NAME;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.COL_NAME_ID;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.COL_RETURN_DATE;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.COL_TITLE_ID;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.TBOOKS;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.TLOANED;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.TPYTHONISTAS;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry._IDB;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry._IDL;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry._IDP;

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
                + _IDB + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LibraryEntry.COL_TITLE + " TEXT_NOT_NULL, "
                + LibraryEntry.COL_AUTHOR + " TEXT, "
                + LibraryEntry.COL_YEAR_PUBLISHED + " INTEGER, "
                + LibraryEntry.COL_BORROWER + " TEXT);";
                // + LibraryEntry.COL_CHECKED_OUT + " BOOLEAN;";

        String SQL_CREATE_PYTHONISTAS_TABLE = "CREATE TABLE " + TPYTHONISTAS
                + "("
                + _IDP + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_F_NAME + " TEXT_NOT_NULL, "
                + LibraryEntry.COL_L_NAME + " TEXT_NOT_NULL, "
                + LibraryEntry.COL_PHONE + " TEXT, "
                + LibraryEntry.COL_EMAIL + " TEXT);";

        // Table generates a default loan_date from inside SQLite
        String SQL_CREATE_LOAN_TABLE = "CREATE TABLE " + TLOANED
                + "("
                + _IDL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE_ID + " INTEGER NOT NULL, "
                + COL_NAME_ID + " IMTEGER NOT NULL, "
                + COL_LOAN_DATE + " DATE NOT NULL default current_date, "
                + COL_RETURN_DATE + " DATE);";


        db.execSQL(SQL_CREATE_BOOKS_TABLE);
        db.execSQL(SQL_CREATE_PYTHONISTAS_TABLE);
        db.execSQL(SQL_CREATE_LOAN_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // TODO
    }

    // get data for name spinner
    public List<String > getNames(){
        List<String> names = new ArrayList<>();
        String fNameQuery = "SELECT " + _IDP + "," + COL_F_NAME + "," + COL_L_NAME + " FROM " + TPYTHONISTAS + " ;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(fNameQuery, null);
        String name;
        if (c.moveToFirst()) {
            do {
                // put together NameID#, period, Lastname, Firstname
                name = c.getString(0) + ". " + c.getString(2) + ", " + c.getString(1);
                names.add(name);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return names;
    }

    // get data for book spinner
    public List<String > getBooks(){
        List<String> titles = new ArrayList<>();

        String strSQL = "SELECT B._id, B.Title FROM TBooks B WHERE NOT B._id IN (Select TLoaned.TitleID " +
                " FROM TLoaned WHERE TLoaned.ReturnDate IS  NULL);";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(strSQL, null);
        try {
            if (c.moveToFirst()) {
                do {
                    titles.add(c.getString(0) + ". " + c.getString(1));
                } while (c.moveToNext());
            }
        }
        finally {
            c.close();
            db.close();
        }
        return titles;
    }


    public void checkOutBook(int bookID, int pythonistaID, String date){

        ContentValues values = new ContentValues();
        values.put(COL_TITLE_ID, bookID);
        values.put(COL_NAME_ID, pythonistaID);
        values.put(COL_LOAN_DATE, date);

        //values.put(LibraryContract.LibraryEntry.COL_BORROWER, borrowerString);

        String checkOut = "Insert into TLoaned values (null," + bookID + ", "
                + pythonistaID  + ", " + date + ");";
        SQLiteDatabase db = this.getWritableDatabase();
        long rowId = db.insert(TLOANED, null, values );
        Log.v("rowID", Long.toString(rowId));

    }

    public List<String> getCheckOutBooks(){
        List<String> titles = new ArrayList<>();
        String gCOBooks = "Select L._id, B.Title, L.TitleID, L.LoanDate, L.ReturnDate, B._id " +
                "FROM TLoaned L LEFT OUTER JOIN (SELECT TBooks._id, TBooks.Title FROM TBooks) B " +
                "ON L.TitleID = B._id WHERE (L.LoanDate IS NOT NULL AND L.ReturnDate IS NULL);";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(gCOBooks, null);
        if (c.moveToFirst()) {
            do {
                titles.add(c.getString(0) + ". " + c.getString(1));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return titles;
    }

    public String getPythonistaName(int loanedRowID){
        List<String> pythonistaName = new ArrayList<>();
        String fName = "SELECT P.FirstName, P.LastName, P._id,  L._id, L.Name From TPythonistas P " +
                " LEFT OUTER JOIN (Select TLoaned.Name, TLoaned._id FROM TLoaned WHERE TLoaned._id = " +
                loanedRowID + " ) L WHERE L.Name = P._id;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(fName, null);
        if (c.moveToFirst()){
            do {
                pythonistaName.add(c.getString(0) + " " + c.getString(1));
            } while (c.moveToNext());
        }
        c.close();
        db.close();

        // Correct getter syntax to get a string value out of a Generic ArrayList
        return pythonistaName.get(0);
    }

    public void updateReturnDate(int id, String date){
        String strSQL = "UPDATE TLoaned SET ReturnDate = '" + date + "' where _id = '" + id + "' ;";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSQL);
        db.close();
    }

    public QueryCaddy getFiller(int loanedRowID){
        QueryCaddy filler = new QueryCaddy("1", "2", "3", "4", "5");
        Log.v(" after null", filler.getName());
        // Returns 1 row with _id, FirstName, LastName, email, phone, book title
        String strSQL = "Select P.FirstName, P.Phone, P.EMail, B.Title, T.LoanDate FROM TPythonistas P, TBooks B, TLoaned T WHERE " +
               "T._id = '" + loanedRowID + "' AND T.Name = P._id AND T.TitleID  = B._id;";
        Log.v("strSQL", strSQL);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(strSQL, null);
        if (c.moveToFirst()) {
            do {
                filler = new QueryCaddy(c.getString(0), c.getString(1), c.getString(2), c.getString(3),c.getString(4));
            } while (c.moveToNext());
        }
        c.close();
        db.close();

        return filler;
    }


}
