package com.babarehner.android.pycolib;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.babarehner.android.pycolib.data.LibraryContract;
import com.babarehner.android.pycolib.data.LibraryDbHelper;


public class LibraryActivity extends AppCompatActivity {


    private LibraryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // Changes the title bar text color. Need to hunt down a way to use findByID(T.id.color)
        String title = "PyCoLib";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LibraryActivity.this, BookActivity.class);
                startActivity(intent);
            }
        }) ;

        // Create an instance of Androids db helper cwhich creates
        // a SQLLite db extended in data/LibraryDbHelper
        mDbHelper = new LibraryDbHelper(this);
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
                insertTestDataBook();
                // displayData:
                return true;
            case R.id.action_delete_all_data:
                // deleteData
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // inserts a row of test data into SQL table db
    private void insertTestDataBook() {
        //Create or open a database to write to it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LibraryContract.LibraryEntry.Col_TITLE, "Python Cookbook 2nd Edition");
        values.put(LibraryContract.LibraryEntry.COL_AUTHOR, "Alex Martelli & Others");
        values.put(LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED, "2005");
        values.put(LibraryContract.LibraryEntry.COL_BORROWER, "Mike Rehner");
        long newRowId = db.insert(LibraryContract.LibraryEntry.TBOOKS, null, values);

        Log.v("LibraryActivity", "New Rows ID "+ newRowId);
    }

    // Display db TBooks table for testing purposes.
    private void displayDBTbooks(){
        // Create or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

    }

}
