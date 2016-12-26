package com.babarehner.android.pycolib;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.babarehner.android.pycolib.data.LibraryContract;


/**
 * Created by mike on 12/23/16.
 */

public class PythonistaCursorAdapter extends CursorAdapter {


    public PythonistaCursorAdapter(Context context, Cursor c) {
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
        return LayoutInflater.from(context).inflate(R.layout.list_pythonista, parent, false);
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
        TextView firstNameTV = (TextView) v.findViewById(R.id.first_name);
        TextView lastNameTV = (TextView) v.findViewById(R.id.last_name);

        int fNameColIndex = c.getColumnIndex(LibraryContract.LibraryEntry.COL_F_NAME);
        int lNameColIndex = c.getColumnIndex(LibraryContract.LibraryEntry.COL_L_NAME);

        String firstName = c.getString(fNameColIndex);
        String lastName = c.getString(lNameColIndex);

        firstNameTV.setText(firstName);
        lastNameTV.setText(lastName + ", ");
    }
}

