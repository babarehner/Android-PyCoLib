package com.babarehner.android.pycolib.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static com.babarehner.android.pycolib.data.LibraryContract.CONTENT_AUTHORITY;
import static com.babarehner.android.pycolib.data.LibraryContract.PATH_TBOOKS;


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

        // Notify if the data at this URI changes. Then we need to update cursor
        // Listener attached is automatically notified with uri
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBook(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

     //Insert a book into the books table with the given content values. Return the new conntent URI
     //for that specific row inthe  database
    private Uri insertBook(Uri uri, ContentValues values){
        // Check that the  name is not null
        String name = values.getAsString(LibraryContract.LibraryEntry.COL_TITLE);
        if (name == null){
            throw new IllegalArgumentException("Title required to insert book!");
        }

        //TODO in insertBook check data before entering in db
        String author = values.getAsString(LibraryContract.LibraryEntry.COL_AUTHOR);
        Integer pubYear = values.getAsInteger(LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED);
        String borrower = values.getAsString(LibraryContract.LibraryEntry.COL_BORROWER);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // insert a book into the TBooks Table
        long id = db.insert(LibraryContract.LibraryEntry.TBOOKS, null, values);
        Log.v(LOG_TAG, "Book not entered " + values);
        if (id == -1){          // if the insertion failed
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the books content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri,id);
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updateBook(uri, values, selection, selectionArgs);
            case BOOK_ID:
                selection = LibraryContract.LibraryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for: " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs){

        // if there are no values to update, quit!
        if (values.size() == 0){return 0;}

        // check that the Title value is not empty
        if (values.containsKey(LibraryContract.LibraryEntry.COL_TITLE)){
            String title = values.getAsString(LibraryContract.LibraryEntry.COL_TITLE);
            // Not entirelly sure that it will get to this if statement if the title column is empty
            if (title == null) {
                throw new IllegalArgumentException("Book requires a title");
            }
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        int rowsUpdated = db.update(LibraryContract.LibraryEntry.TBOOKS, values, selection, selectionArgs);
        if (rowsUpdated != 0){
            // Notify all listeners that the data has changed
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // returns the # of db rows affected by theupdate statement
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch(match){
            case BOOKS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = db.delete(LibraryContract.LibraryEntry.TBOOKS, selection, selectionArgs);
                break;
            case BOOK_ID:
                // Delete a single row fiven by the ID in the URI
                selection = LibraryContract.LibraryEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(LibraryContract.LibraryEntry.TBOOKS, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for: " + uri);
        }

        if (rowsDeleted != 0){
            // Notify all listeners that the date at the given Uri has changed
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri){

        final int match = sUriMatcher.match(uri);
        switch (match){
            case BOOKS:
                return LibraryContract.LibraryEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return LibraryContract.LibraryEntry.CONENTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unkown UrI: " + uri + "with match: " + match);
        }
    }
}
