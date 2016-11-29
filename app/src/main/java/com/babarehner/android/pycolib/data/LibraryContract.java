package com.babarehner.android.pycolib.data;

import android.provider.BaseColumns;

/**
 * Created by mike on 11/25/16.
 */

public final class LibraryContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private LibraryContract() { }

    /**
     * Inner class that defines constant values for the library database table.
     * Each entry in the table represents a single book.
     */
    public static final class LibraryEntry implements BaseColumns {

        // Table books
        public final static String TBOOKS = "books";

        // Primary key to be autoincremented. I believe this is a
        // required name for some Android APIs
        public final static String _ID = BaseColumns._ID;
        public final static String COL_ISBN ="ISBN";
        public final static String Col_TITLE= "Title";
        public static final String COL_AUTHOR= "Author";
        public static final String COL_YEAR_PUBLISHED = "YearPublished";
        public static boolean COL_CHECKED_OUT = false;

        public static final int LOWEST_YEAR = 1991;
        public static final int HIGHEST_YEAR = 2020;

    }
}