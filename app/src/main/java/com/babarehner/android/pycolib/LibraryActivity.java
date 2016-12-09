package com.babarehner.android.pycolib;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;

import com.babarehner.android.pycolib.data.LibraryContract;
import com.babarehner.android.pycolib.data.LibraryDbHelper;

import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.COL_AUTHOR;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.COL_BORROWER;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.COL_TITLE;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.TBOOKS;
import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry._ID;


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

        // Create a floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LibraryActivity.this, BookActivity.class);
                startActivity(intent);
            }
        }) ;

        // Create an instance of Android's dbHelper which abstracts from SQLite
        mDbHelper = new LibraryDbHelper(this);
        displayDBTBooks();
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
                displayDBTBooks();
                return true;
            case R.id.action_delete_all_data:
                deleteAll();
                displayDBTBooks();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // inserts a row of test data into SQL table db
    private void insertTestDataBook() {
        //Create or open a database to write to it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LibraryContract.LibraryEntry.COL_TITLE, "Python Cookbook 2nd Edition");
        values.put(LibraryContract.LibraryEntry.COL_AUTHOR, "Alex Martelli & Others");
        values.put(LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED, "2005");
        values.put(LibraryContract.LibraryEntry.COL_BORROWER, "Mike Rehner");
        long newRowId = db.insert(TBOOKS, null, values);

        Log.v("LibraryActivity", "New Rows ID "+ newRowId);
    }

    // Display db TBooks table for testing purposes.
    private void displayDBTBooks(){
        // Create or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {LibraryContract.LibraryEntry._ID,
                LibraryContract.LibraryEntry.COL_TITLE,
                LibraryContract.LibraryEntry.COL_AUTHOR,
                LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED,
                LibraryContract.LibraryEntry.COL_BORROWER
        };

        Cursor c = db.query(LibraryContract.LibraryEntry.TBOOKS, projection, null, null, null, null, null);

        TextView displayView = (TextView) findViewById(R.id.text_view_library);

        try {
            displayView.setText(" Number of rows in Books table: " + c.getCount() + " books. \n\n");
            displayView.append(LibraryContract.LibraryEntry._ID + " - " +
                            LibraryContract.LibraryEntry.COL_TITLE + " - " +
                            LibraryContract.LibraryEntry.COL_AUTHOR + " - " +
                            LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED + " - " +
                            LibraryContract.LibraryEntry.COL_BORROWER + "\n\n");

            int idColIndex = c.getColumnIndex(_ID);
            int titleColIndex = c.getColumnIndex(COL_TITLE);
            int authorColIndex = c.getColumnIndex(COL_AUTHOR);
            int publishYearColIndex = c.getColumnIndex(COL_YEAR_PUBLISHED);
            int borrowerColIndex = c.getColumnIndex(COL_BORROWER);

            while (c.moveToNext()){
                int currentID = c.getInt(idColIndex);
                String currentTitle = c.getString(titleColIndex);
                String currentAuthor = c.getString(authorColIndex);
                int currentPublishYear = c.getInt(publishYearColIndex);
                String currentBorrower = c.getString(borrowerColIndex);

                displayView.append(currentID + " - " +
                        currentTitle + " - " +
                        currentAuthor + " - " +
                        currentPublishYear + " - " +
                        currentBorrower + "\n\n");
            }

        } finally {
            c.close();
        }
    }

    private void deleteAll() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int deleteRowCount = db.delete(TBOOKS, null, null);

        Log.v("LibraryActivity", "Number of Rows deleted: " + deleteRowCount);
    }

}
