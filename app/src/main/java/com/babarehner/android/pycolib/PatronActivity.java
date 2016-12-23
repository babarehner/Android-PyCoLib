package com.babarehner.android.pycolib;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.babarehner.android.pycolib.data.LibraryContract;

public class PatronActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patron);

        // Create a floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_patron);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatronActivity.this, PythonistaActivity.class);
                startActivity(intent);
            }
        }) ;

    }

    // Create an  options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_library, menu);
        return true;
    }

    // Select from the options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_test_data:
                insertTestDataPythonista();
                return true;
            case R.id.action_delete_all_data:
                deleteAllPythonista();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

     // inserts a row of test data into SQL table db
    private void insertTestDataPythonista() {

        ContentValues values = new ContentValues();
        values.put(LibraryContract.LibraryEntry.COL_TITLE, "Python Cookbook 2nd Edition");
        values.put(LibraryContract.LibraryEntry.COL_AUTHOR, "Alex Martelli & Others");
        values.put(LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED, "2005");
        values.put(LibraryContract.LibraryEntry.COL_BORROWER, "Mike Rehner");

        //Log.v("LibraryActivity", "New Rows ID "+ newRowId);

        Uri uri = getContentResolver().insert(LibraryContract.LibraryEntry.CONTENT_URI, values);
    }


    private void deleteAllPythonista() {
        int deleteRowCount = getContentResolver().delete(LibraryContract.LibraryEntry.CONTENT_URI, null, null);
        Log.v("LibraryActivity", "Number of Rows deleted: " + deleteRowCount);
    }

}
