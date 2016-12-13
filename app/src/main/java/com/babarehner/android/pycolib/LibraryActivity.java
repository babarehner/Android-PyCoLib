package com.babarehner.android.pycolib;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.babarehner.android.pycolib.data.LibraryContract;
import com.babarehner.android.pycolib.data.LibraryDbHelper;

import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.TBOOKS;


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

        ListView libraryListView = (ListView) findViewById(R.id.list);
        // have the libraryListView display  the empty view
        View emptyView = findViewById(R.id.empty_view);
        libraryListView.setEmptyView(emptyView);
    }


    @Override
    protected void onStart() {
        super.onStart();
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

        ContentValues values = new ContentValues();
        values.put(LibraryContract.LibraryEntry.COL_TITLE, "Python Cookbook 2nd Edition");
        values.put(LibraryContract.LibraryEntry.COL_AUTHOR, "Alex Martelli & Others");
        values.put(LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED, "2005");
        values.put(LibraryContract.LibraryEntry.COL_BORROWER, "Mike Rehner");
        //long newRowId = db.insert(TBOOKS, null, values);

        //Log.v("LibraryActivity", "New Rows ID "+ newRowId);

        Uri uri = getContentResolver().insert(LibraryContract.LibraryEntry.CONTENT_URI, values);
    }

    // Display db TBooks table for testing purposes.
    private void displayDBTBooks(){
        // Create or open a database to read from it
        //SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {LibraryContract.LibraryEntry._ID,
                LibraryContract.LibraryEntry.COL_TITLE,
                LibraryContract.LibraryEntry.COL_AUTHOR,
                LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED,
                LibraryContract.LibraryEntry.COL_BORROWER
        };

        //Cursor c = db.query(LibraryContract.LibraryEntry.TBOOKS, projection, null, null, null, null, null);
        Cursor c = getContentResolver().query(LibraryContract.LibraryEntry.CONTENT_URI, projection, null, null, null);

        // Set up the adapter and display the listview
        ListView booksListView = (ListView) findViewById(R.id.list);
        LibraryCursorAdapter adapter = new LibraryCursorAdapter(this, c);
        booksListView.setAdapter(adapter);

        //c.close();   adapter appears to take care of this closing
    }

    private void deleteAll() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int deleteRowCount = db.delete(TBOOKS, null, null);

        //Log.v("LibraryActivity", "Number of Rows deleted: " + deleteRowCount);
    }

}
