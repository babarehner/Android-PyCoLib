package com.babarehner.android.pycolib;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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

    private Uri mCurrentBookUri;        // Uri for the current book item

    // The first year possible for the publish year
    public static final int START_YEAR = 1991;  // used to get position in the  spinner

    private boolean mBookChanged = false;   // When edit change made to a book row

    // Touch Listener to check if changes made to a book
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mBookChanged = true;
            return false;
        }
    };

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
            // take out the delete menu
            invalidateOptionsMenu();
        } else {                            // individual item clicked
            setTitle(getString(R.string.book_activity_title_edit_book));
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, BookActivity.this);
        }

        // Find all inpuut views to read from
        mTitleEditText = (EditText) findViewById(R.id.edit_title);
        mAuthorEditText = (EditText) findViewById(R.id.edit_author);
        mPublishYearSpinner = (Spinner) findViewById(R.id.spinner_publish_year);
        mBorrowerEditText = (EditText) findViewById(R.id.edit_borrower);

        //Setup TouchListener on all the input fields to see if user has touched
        // or modified a field.
        mTitleEditText.setOnTouchListener(mTouchListener);
        mAuthorEditText.setOnTouchListener(mTouchListener);
        mPublishYearSpinner.setOnTouchListener(mTouchListener);
        mBorrowerEditText.setOnTouchListener(mTouchListener);

        setUpSpinner();
    }

    private void setUpSpinner() {
        // TODO try and have spinner pull up publish year dynamically
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
                saveBook();
                finish();       // exit activity
                return true;
            case R.id.action_delete:
                // Alert Dialog for deleting one book
                showDeleteConfirmationDialog();
                return true;
            // this is the <- button on the header
            case android.R.id.home:
                // book has not changed
                if (!mBookChanged) {
                    NavUtils.navigateUpFromSameTask(BookActivity.this);
                    return true;
                }
                // set up dialog to warn user about unsaved changes
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                //user click discard. Navigate up to parent activity
                                NavUtils.navigateUpFromSameTask(BookActivity.this);
                            }
                        };
                // show user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // hide delete menu item when adding a new book
    @Override
    public boolean onPrepareOptionsMenu(Menu m) {
        super.onPrepareOptionsMenu(m);
        // if this is add a book, hide "delete" menu item
        if (mCurrentBookUri == null) {
            MenuItem menuItem = m.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }


    private void saveBook(){

        // read from EditText input fields
        String titleString = mTitleEditText.getText().toString().trim();
        String authorString = mAuthorEditText.getText().toString().trim();
        //String publishYearString = String.valueOf(mPublishYear);
        String borrowerString = mBorrowerEditText.getText().toString().trim();

        // if adding book and the title field is left blank do nothing
        if (mCurrentBookUri == null && TextUtils.isEmpty(titleString)) {
            Toast.makeText(this, getString(R.string.missing_book_title),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(LibraryContract.LibraryEntry.COL_TITLE, titleString);
        values.put(LibraryContract.LibraryEntry.COL_AUTHOR, authorString);
        values.put(LibraryContract.LibraryEntry.COL_YEAR_PUBLISHED, mPublishYear);
        //Log.v("BookActivity"," mPublishYear " + mPublishYear);
        values.put(LibraryContract.LibraryEntry.COL_BORROWER, borrowerString);

        if (mCurrentBookUri == null) {
            // a new book
            Uri newUri = getContentResolver().insert(LibraryContract.LibraryEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.library_provider_insert_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.library_provider_insert_book_good),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // existing book so update with content URI and pass in ContentValues
            int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);
            if (rowsAffected == 0) {
                // TODO Check db- Text Not Null does not seem to be working or entering
                // "" does not mean NOT Null- there must be an error message closer to the db!!!
                Toast.makeText(this, getString(R.string.edit_update_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.edit_update_book_success),
                        Toast.LENGTH_SHORT).show();
            }

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

    //
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener){
        // Create AlertDialogue.Builder amd set message and click listeners
        // for positive and negative buttons in dialogue.
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // user clicked the "keep eiditing" button. Dismiss dialog and keep editing
                if (dialog !=null) { dialog.dismiss();}
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Override the activity's normal back button. If book has changed create a
    // discard click listener that closed current activity.
    @Override
    public void onBackPressed() {
        if (!mBookChanged) {
            super.onBackPressed();
        }
            //otherwise if there are unsaved changes setup a dialog to warn the  user
            //handles the user confirming that changes should be made
            DialogInterface.OnClickListener discardButtonClickListener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            // user clicked "Discard" button, close the current activity
                            finish();
                        }
                    };

            // show dialog that there are unsaved changes
            showUnsavedChangesDialog(discardButtonClickListener);
    }


    private void showDeleteConfirmationDialog() {
        // Create and AlertDialog.Builder, set message and click
        // listeners for positive and negative buttons
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked delet so delete
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked cancel, dismiss dialog, continue editing
               if (dialog != null) {dialog.dismiss();}
            }
        });
        // Create and show dialog
        AlertDialog alertD = builder.create();
        alertD.show();
    }


    // delete book from db
    private void deleteBook(){
        if (mCurrentBookUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.delete_book_failure),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
      finish();
    }

}








