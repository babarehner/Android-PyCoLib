package com.babarehner.android.pycolib;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.babarehner.android.pycolib.data.LibraryContract;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final int EXISTING_BOOK_LOADER = 0;

    private Uri mCurrentLibraryUri = null;

    private EditText mTitleEditText;
    private EditText mAuthorEditText;
    private Spinner mPublishYearSpinner;
    private EditText mBorrowerEditText;

    private int mPublishYear;

    // Uri for the current book item
    private Uri mCurrentBookUri;

    // The first year possible for the publish year
    // used to get position in the  spinner
    public static final int START_YEAR = 1991;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        
        //Get intent and get data from intent
        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        // Idf the intent does not contain an single item Uri
        // FAB clicked
        if (mCurrentBookUri == null){
            // set page header to add book
            setTitle(getString(R.string.book_activity_title_add_book));
            // take out the delet menu
            invalidateOptionsMenu();
        } else {                            // individual item clicked
            setTitle(getString(R.string.book_activity_title_edit_book));
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, BookActivity.this);
        }

        mTitleEditText = (EditText) findViewById(R.id.edit_title);
        mAuthorEditText = (EditText) findViewById(R.id.edit_author);
        mPublishYearSpinner = (Spinner) findViewById(R.id.spinner_publish_year);
        setUpSpinner();
        mBorrowerEditText = (EditText) findViewById(R.id.edit_borrower);
    }

    private void setUpSpinner() {
        // Create adapter for spinner. Use string-array from resource file
        // Use default layout
        ArrayAdapter publishYearSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_publish_year_options, android.R.layout.simple_spinner_item);
        // Specify dropdown layout style- simple list view with 1 item per line
        publishYearSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // bind adapter to spinner
        mPublishYearSpinner.setAdapter(publishYearSpinnerAdapter);
        // Set the integer selected to the constant values

        mPublishYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                Log.v("BookActivity ",  selection);
                mPublishYear = Integer.parseInt(selection);  //The item at pos
            }
            // Because AdapterView is an abstract classe, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent){
                mPublishYear = 2016;
            }
        });

    }

    // Options menu automatially called from onCreate I believe
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);

        return true;
    }

    // Select from the options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                insertBook();
                finish();
                return true;
            case R.id.action_delete_all_data:
                // delete book in db
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertBook(){

        // read from EditText input fields
        String titleString = mTitleEditText.getText().toString().trim();
        String authorString = mAuthorEditText.getText().toString().trim();
        //String publishYearString = String.valueOf(mPublishYear);
        String borrowerString = mBorrowerEditText.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(LibraryContract.LibraryEntry.COL_TITLE, titleString);
        values.put(LibraryContract.LibraryEntry.COL_AUTHOR, authorString);
        values.put(LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED, mPublishYear);
        Log.v("BookActivity"," mPublishYear " + mPublishYear);
        values.put(LibraryContract.LibraryEntry.COL_BORROWER, borrowerString);

        Uri newUri = getContentResolver().insert(LibraryContract.LibraryEntry.CONTENT_URI, values);
        if (newUri == null) {
            Toast.makeText(this, getString(R.string.library_provider_insert_book_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.library_provider_insert_book_good),
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // define a projection that contains all columns in TBooks
        String[] projection = {LibraryContract.LibraryEntry._ID,
                LibraryContract.LibraryEntry.COL_TITLE,
                LibraryContract.LibraryEntry.COL_AUTHOR,
                LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED,
                LibraryContract.LibraryEntry.COL_BORROWER};

        // Start a new thread
        return new CursorLoader(this, mCurrentBookUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        // move to the only row in the Cursor
        if (c.moveToFirst()){
            // get the index of each column
            int titleColumnIndex=c.getColumnIndex(LibraryContract.LibraryEntry.COL_TITLE);
            int authorColumnIndex=c.getColumnIndex(LibraryContract.LibraryEntry.COL_AUTHOR);
            int publishYearColumnIndex=c.getColumnIndex(LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED);
            int borrowerColumnIndex=c.getColumnIndex(LibraryContract.LibraryEntry.COL_BORROWER);

            // use the index to pull the data out
            String title=c.getString(titleColumnIndex);
            String author=c.getString(authorColumnIndex);
            int publishYear = c.getInt(publishYearColumnIndex);
            String borrower = c.getString(borrowerColumnIndex);

            //Update the text views
            mTitleEditText.setText(title);
            mAuthorEditText.setText(author);
            mBorrowerEditText.setText(borrower);
            // Get the position of the publish date in the spinner
            mPublishYearSpinner.setSelection(publishYear - START_YEAR);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If invalid Loader clear data from input field
        mTitleEditText.setText("");
        mAuthorEditText.setText("");
        mPublishYearSpinner.setSelection(0);
        mBorrowerEditText.setText("");
    }
}








