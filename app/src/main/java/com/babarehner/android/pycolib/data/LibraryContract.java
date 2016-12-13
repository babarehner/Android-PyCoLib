package com.babarehner.android.pycolib.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mike on 11/25/16.
 */

public final class LibraryContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private LibraryContract() { }

    /**
    * The 'Content Authority is a name for the entire content provide, similar to the
     * relationship beteween a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app which is guaranteed to be unique on the  device
     */
    public static final String CONTENT_AUTHORITY = "com.babarehner.android.pycolib";

    /**
     * Use CONTENT_AUTHOARITY to create the base of all URI's which apps will use to contact the
     * content provider. <.parse().changes String to Uri
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TBOOKS = "Books";
    public static final String PATH_TBORROWERS = "TBorrowers";
    public static final String PATH_TOANS ="TLoaned";

    // The MIME Type for a list of books
    public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
            + CONTENT_AUTHORITY +"/" + PATH_TBOOKS;
    // The MIME Type fo a single book
    public static final String CONTENT_BOOK_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH_TBOOKS;
    // The content URI  to access the TBooks data in the provider
    public static final Uri CONTENT_BOOKS_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TBOOKS);



    /**
     * Inner class that defines constant values for the library database table.
     * Defines Table name and column names.
     */
    public static final class LibraryEntry implements BaseColumns {

        // The MIME type of the {@link #CONTENT_URI} for a list of books
        public static  final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_TBOOKS;

        //The MIME type of the {@link #CONTENT_URI} for a single book
        public static final String CONENTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_TBOOKS;

        //The content uri to access the library data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TBOOKS);

        // Name of db table for books
        public final static String TBOOKS = "TBooks";


        // Primary key to be autoincremented. I believe this is a
        // required name for some Android APIs
        // Names for columns on TBooks table
        public static final String _ID = BaseColumns._ID;
        public static final String COL_TITLE= "Title";
        public static final String COL_AUTHOR= "Author";
        public static final String COL_YEAR_PUBLISHED = "YearPublished";
        public static final String COL_BORROWER = "Borrower";
        // not currently used- should be a boolean
        //public static final String COL_CHECKED_OUT = "CheckedOut";

        public static final int LOWEST_YEAR = 1991;     // When Python was born
        public static final int HIGHEST_YEAR = 2017;    // Current year + 1

        public static final String _IDB = BaseColumns._ID;  //Wonder if I can have two different
        public static final String TBORROWERS = "TBorrowers";
        public static final String COL_NAME = "Name";
        public static final String COL_PHONE = "Phone";
        public static final String COL_EMAIL = "EMail";

        public static final String TLOANED = "TLoaned";
        public static final String _IDL = BaseColumns._ID;
        public static final String COL_TITLE_ID = "Title";
        public static final String COL_NAME_ID = "Name";
        public static final String COL_LOAN_DATE = "LoanDate";
        public static final String  COL_RETURN_DATE = "ReturnDate";

    }
}