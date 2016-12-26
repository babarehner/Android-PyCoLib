package com.babarehner.android.pycolib;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.babarehner.android.pycolib.data.LibraryContract;

public class PythonistaActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final int EXISTING_PYTHONISTA_LOADER = 1;
    private Uri mCurrentPatronsUri;
    private Uri mCurrentPythonistaUri;

    private EditText mFNameET;
    private EditText mLNameET;
    private EditText mEMailET;
    private EditText mPhoneET;

    private boolean mBookChanged = false;

    // Touch listener check if changes made to a pythonista
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mBookChanged = false;
            return false;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pythonista);
    }

    // Options menu automatially called from onCreate I believe
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);

        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // define projection for Pythonistas Table
        String[] projection = {LibraryContract.LibraryEntry._IDP,
                LibraryContract.LibraryEntry.COL_F_NAME,
                LibraryContract.LibraryEntry.COL_L_NAME,
                LibraryContract.LibraryEntry.COL_EMAIL,
                LibraryContract.LibraryEntry.COL_PHONE};

        return new CursorLoader(this, mCurrentPythonistaUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        // move to the only row in the Cursor
        if (c.moveToFirst()){
            // get the index of each column and extract the data
            String fName = c.getString(c.getColumnIndex(LibraryContract.LibraryEntry.COL_F_NAME));
            String lName = c.getString(c.getColumnIndex(LibraryContract.LibraryEntry.COL_L_NAME));
            String eMail = c.getString(c.getColumnIndex(LibraryContract.LibraryEntry.COL_EMAIL));
            String phone = c.getString(c.getColumnIndex(LibraryContract.LibraryEntry.COL_PHONE));

            // update the text views
            mFNameET.setText(fName);
            mLNameET.setText(lName);
            mEMailET.setText(eMail);
            mPhoneET.setText(phone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // if invalid loader clear data from input field
        mFNameET.setText("");
        mLNameET.setText("");
        mEMailET.setText("");
        mPhoneET.setText("");

    }

    // Select from the options menu
    //@Override
    /*
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

    */
}
