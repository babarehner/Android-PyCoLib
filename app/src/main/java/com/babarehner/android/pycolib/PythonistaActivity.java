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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.babarehner.android.pycolib.data.LibraryContract;







public class PythonistaActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int EXISTING_PYTHONISTA_LOADER = 1;
    private Uri mCurrentPatronsUri;
    private Uri mCurrentPythonistaUri;

    private EditText mFNameET;
    private EditText mLNameET;
    private EditText mEMailET;
    private EditText mPhoneET;



    private boolean mPythonistaChanged = false;

    // Touch listener check if changes made to a pythonista
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mPythonistaChanged = true;
            return false;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pythonista);

        // get intent and get data from intent
        Intent intent = getIntent();
        mCurrentPythonistaUri = intent.getData();

        // if the  intent does not contain a single item Uri or the FAB is clicked
        if (mCurrentPythonistaUri == null){
            // Set page header to add a pythonista
            setTitle(getString(R.string.patron_activity_title_add_pythonista));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.patron_activity_title_edit_pythonista));
            getLoaderManager().initLoader(EXISTING_PYTHONISTA_LOADER, null, PythonistaActivity.this);
        }

        //Find all input views to read from
        mFNameET = (EditText) findViewById(R.id.edit_first_name);
        mLNameET = (EditText) findViewById(R.id.edit_last_name);
        mEMailET = (EditText) findViewById(R.id.edit_email);
        mPhoneET = (EditText) findViewById(R.id.edit_phone);

        mFNameET.setOnTouchListener(mTouchListener);
        mFNameET.setOnTouchListener(mTouchListener);
        mEMailET.setOnTouchListener(mTouchListener);
        mPhoneET.setOnTouchListener(mTouchListener);
    }

    // Options menu automatially called from onCreate I believe
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    // Select from the options menu
    //@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                savePythonista();
                finish();       // exit activity
                return true;
            case R.id.action_delete:
                // Alert Dialog for deleting one book
                showDeleteConfirmationDialog();
                return true;
            // this is the <- button on the header
            case android.R.id.home:
                // book has not changed
                if (!mPythonistaChanged) {
                    NavUtils.navigateUpFromSameTask(PythonistaActivity.this);
                    return true;
                }
                // set up dialog to warn user about unsaved changes
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                //user click discard. Navigate up to parent activity
                                NavUtils.navigateUpFromSameTask(PythonistaActivity.this);
                            }
                        };
                // show user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // hide delete menu item when adding a pythonista
    @Override
    public boolean onPrepareOptionsMenu(Menu m){
        super.onPrepareOptionsMenu(m);
        if (mCurrentPythonistaUri == null){
            MenuItem menuItem = m.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void savePythonista(){
        // read from the EditText input field
        String fNameStr = mFNameET.getText().toString().trim();
        String lNameStr = mLNameET.getText().toString().trim();
        String eMailStr = mEMailET.getText().toString().trim();
        String phoneStr = mPhoneET.getText().toString().trim();

        if (mCurrentPythonistaUri == null && (TextUtils.isEmpty(fNameStr) || TextUtils.isEmpty(lNameStr))) {
            Toast.makeText(this, getString(R.string.missing_first_last_name),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(LibraryContract.LibraryEntry.COL_F_NAME, fNameStr);
        values.put(LibraryContract.LibraryEntry.COL_L_NAME, lNameStr);
        values.put(LibraryContract.LibraryEntry.COL_EMAIL, eMailStr);
        values.put(LibraryContract.LibraryEntry.COL_PHONE, phoneStr);

        if (mCurrentPythonistaUri == null) {
            // a new book
            Uri newUri = getContentResolver().insert(LibraryContract.LibraryEntry.PYTHONISTA_URI, values);
            if (newUri == null){
                Toast.makeText(this, getString(R.string.pythonista_provider_insert_pythonista_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.pythonista_provider_insert_pythonista_good),
                        Toast.LENGTH_SHORT).show();
            }
        } else {   // now we are editing/updating the pythonista
            int rowsAffected = getContentResolver().update(mCurrentPythonistaUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.edit_update_pythonista_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.edit_update_pythonista_success),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDeleteConfirmationDialog() {
        // Create and AlertDialog.Builder, set message and click
        // listeners for positive and negative buttons
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_pythonista_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked delet so delete
                deletePythonista();
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
                if (dialog != null) {dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

    @Override
    public void onBackPressed(){
        if (mPythonistaChanged) {
            super.onBackPressed();
            return;
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

    // delete pythjonista from db
    private void deletePythonista(){
        if (mCurrentPythonistaUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentPythonistaUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.delete_pythonista_failure),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_pythonista_failure),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

}
