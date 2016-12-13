package com.babarehner.android.pycolib;

import android.content.Context;
import android.database.Cursor;
import android.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babarehner.android.pycolib.data.LibraryContract;

/**
 * Created by mike on 12/12/16.
 */

public class LibraryCursorAdapter extends CursorAdapter {

    public LibraryCursorAdapter(Context context, Cursor c) {
        super(context, c /* flags*/);
    }

    /**
     * creates a new blank list item with no data
     * @param context   app context
     * @param c         cursor
     * @param parent    parent to which view is attached to
     * @return          the newly created list item view
     */
    @Override
    public View newView(Context context, Cursor c, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * Binds data to the empty lsit item
     * @param v
     * @param context
     * @param c
     */
    @Override
    public void bindView(View v, Context context, Cursor c) {
        // find the id of the views to modify
        TextView titleTextView = (TextView) v.findViewById(R.id.title);
        TextView publishYearTextView = (TextView) v.findViewById(R.id.publish_year);

        int titleColIndex = c.getColumnIndex(LibraryContract.LibraryEntry.COL_TITLE);
        int publishYearColIndex = c.getColumnIndex(LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED);

        String title = c.getString(titleColIndex);
        String publish_year = c.getString(publishYearColIndex);

        titleTextView.setText(title);
        publishYearTextView.setText(publish_year);
    }
}
