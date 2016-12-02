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
     * Defines Table name and column names.
     */
    public static final class LibraryEntry implements BaseColumns {

        // Create Table books
        public final static String TBOOKS = "TBooks";

        // Primary key to be autoincremented. I believe this is a
        // required name for some Android APIs
        // Names for columns on TBooks table
        public static final String _ID = BaseColumns._ID;
        public static final String Col_TITLE= "Title";
        public static final String COL_AUTHOR= "Author";
        public static final String COL_YEAR_PUBLISHED = "YearPublished";
        public static final String COL_BORROWER = "Borrower";
        // not currently used- should be a boolean
        //public static final String COL_CHECKED_OUT = "CheckedOut";

        public static final int LOWEST_YEAR = 1991;     // When Python was born
        public static final int HIGHEST_YEAR = 2017;    // Current year + 1

    }
}