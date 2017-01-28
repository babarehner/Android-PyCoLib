package com.babarehner.android.pycolib;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.babarehner.android.pycolib.data.LibraryContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class LibraryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // identifier for loader to run on separate thread
    private static final int BOOK_LOADER = 0;
    LibraryCursorAdapter mCursorAdapter;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        setTitle("Books");

        /** Changes the title bar text color. Need to hunt down a way to use findByID(T.id.color)
        * String title = "PyCoLib";
        * SpannableString s = new SpannableString(title);
        * s.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        * getSupportActionBar().setTitle(s);
        **/

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

        // set up an adapter to create a list for each row of Book data
        // There is no data now (data won't show up unit loader finishes) so null for Cursor
        mCursorAdapter = new LibraryCursorAdapter(this, null);
        libraryListView.setAdapter(mCursorAdapter);
        // the  item click listener
        libraryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                //create new intent to go to {@link BookActivity}
                Intent intent = new Intent(LibraryActivity.this, BookActivity.class);

                // Form the content URI by appending the "id: passed to this method into
                // the {@link LibraryEntry#CONTENT_URI
                Uri currentLibraryUri = ContentUris.withAppendedId(LibraryContract.LibraryEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentLibraryUri);

                // Start the {@link BookActivity in edit mode
                startActivity(intent);
            }
        });

        // Fire up the loader
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
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
                return true;
            case R.id.action_delete_all_data:
                deleteAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // inserts a row of test data into SQL table db
    private void insertTestDataBook() {

        BufferedReader br;

        //AssetManager am = getAssets();
        //final InputStream is = am.open("library.txt");

        try{
            final InputStream file = getAssets().open("library.txt");
            br = new BufferedReader(new InputStreamReader(file));
            String line = br.readLine();
            while(line != null){
                Log.d("Show Books", line);
                // -1 used to deal with empty values in csv file
                String[] stuff = line.split(",", -1);
                for (int i=0; i<3; i++){
                    Log.d( "stuff", stuff[i] +"\n");
                }
                ContentValues values = new ContentValues();
                values.put(LibraryContract.LibraryEntry.COL_TITLE, stuff[0]);
                values.put(LibraryContract.LibraryEntry.COL_AUTHOR, stuff[1]);
                values.put(LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED, stuff[2]);
                values.put(LibraryContract.LibraryEntry.COL_BORROWER, "not used");
                Uri uri = getContentResolver().insert(LibraryContract.LibraryEntry.CONTENT_URI, values);

                line = br.readLine();
            }


        } catch(IOException ioe) {
            ioe.printStackTrace();
        }

        /*
        ContentValues values = new ContentValues();
        values.put(LibraryContract.LibraryEntry.COL_TITLE, "Python Cookbook 2nd Edition");
        values.put(LibraryContract.LibraryEntry.COL_AUTHOR, "Alex Martelli & Others");
        values.put(LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED, "2005");
        values.put(LibraryContract.LibraryEntry.COL_BORROWER, "Mike Rehner");
        Uri uri = getContentResolver().insert(LibraryContract.LibraryEntry.CONTENT_URI, values);
        */
    }


    private void deleteAll() {
        int deleteRowCount = getContentResolver().delete(LibraryContract.LibraryEntry.CONTENT_URI, null, null);
        Log.v("LibraryActivity", "Number of Rows deleted: " + deleteRowCount);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Define a projections with the columns we are interested in
        // CursorAdapter requires column title of "_Id"
        String[] projection = {LibraryContract.LibraryEntry._ID,
                LibraryContract.LibraryEntry.COL_TITLE,
                LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED};

        // This loader will execute the ContentProvider's query  method on a background thread
        return new CursorLoader(this,                           // parent activity context
                LibraryContract.LibraryEntry.CONTENT_URI,       // ProviderContent URI
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // update(@link LibraryCursorAdapter) with this new cursor containing update Libary data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted- use null
        mCursorAdapter.swapCursor(null);
    }


}
