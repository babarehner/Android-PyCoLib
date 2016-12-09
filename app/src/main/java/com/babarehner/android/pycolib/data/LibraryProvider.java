package com.babarehner.android.pycolib.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static com.babarehner.android.pycolib.data.LibraryContract.PATH_TBOOKS;
import static com.babarehner.android.pycolib.data.LibraryContract.CONTENT_AUTHORITY;


/**
 * Created by mike on 12/6/16.
 */

public class LibraryProvider extends ContentProvider {

    public static final String LOG_TAG = LibraryProvider.class.getSimpleName();

    private static final int BOOKS = 100;
    private static final int BOOK_ID = 101;
    private static final int BORROWER = 200;
    private static final int BORROWER_ID = 201;
    private static final int LOANED = 300;
    private static final int LOANED_ID = 301;

    private LibraryDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_TBOOKS, BOOKS);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_TBOOKS + "/#", BOOK_ID);
    }


    @Override
    public boolean onCreate() {
        // why is this line need- I know it initialized mDbHelper
        mDbHelper = new LibraryDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[]projection, String selection, String[] selectionArgs,
                        String sortOrder) {



        //Create or open a database to write to it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor c;

        int match = sUriMatcher.match(uri);
        switch (match){
            case BOOKS:
                c = db.query(LibraryContract.LibraryEntry.TBOOKS, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ID:
                selection = LibraryContract.LibraryEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                c = db.query(LibraryContract.LibraryEntry.TBOOKS, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannnot query unknonw URI: " + uri);
        }
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return uri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri){
        return null;
    }
}
