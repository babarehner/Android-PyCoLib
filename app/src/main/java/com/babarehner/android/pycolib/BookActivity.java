package com.babarehner.android.pycolib;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
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

import com.babarehner.android.pycolib.data.LibraryContract;
import com.babarehner.android.pycolib.data.LibraryDbHelper;

import static com.babarehner.android.pycolib.data.LibraryContract.LibraryEntry.TBOOKS;

public class BookActivity extends AppCompatActivity {

    private EditText mTitleEditText;
    private EditText mAuthorEditText;
    private Spinner mPublishYearSpinner;
    private int mPublishYear = 2016;
    private EditText mBorrowerEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

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
                mPublishYear = Integer.parseInt(selection);
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
        LibraryDbHelper mDbHelper = new LibraryDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // read from EditText input fields
        String titleString = mTitleEditText.getText().toString().trim();
        String authorString = mAuthorEditText.getText().toString().trim();
        String publishDateString = Integer.toString(mPublishYear);
        String borrowerString = mBorrowerEditText.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(LibraryContract.LibraryEntry.Col_TITLE, titleString);
        values.put(LibraryContract.LibraryEntry.COL_AUTHOR, authorString);
        values.put(LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED, publishDateString);
        values.put(LibraryContract.LibraryEntry.COL_BORROWER, borrowerString);

        long newRowId = db.insert(TBOOKS, null, values);

    }

}








