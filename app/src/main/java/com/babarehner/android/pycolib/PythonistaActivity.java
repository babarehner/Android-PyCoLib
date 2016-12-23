package com.babarehner.android.pycolib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

public class PythonistaActivity extends AppCompatActivity {

    @Override
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
