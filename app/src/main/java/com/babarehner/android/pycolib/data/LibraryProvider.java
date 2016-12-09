package com.babarehner.android.pycolib.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
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

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_TBOOKS, BOOKS);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_TBOOKS + "/", BOOK_ID);
    }


    @Override
    public boolean onCreate() {

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[]projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        return null;
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
