package com.babarehner.android.pycolib;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

public class PatronActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PYTHONISTA_LOADER = 1;
    PythonistaCursorAdapter mCursorAdapterPythonista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patron);

        setTitle("Pythonistas");

        // Create a floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_patron);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatronActivity.this, PythonistaActivity.class);
                startActivity(intent);
            }
        }) ;

        ListView pythonistaListView = (ListView) findViewById(R.id.list_pythonista);
        // have the patronListView display the empty view
        View emptyView = findViewById(R.id.patron_empty_view);
        pythonistaListView.setEmptyView(emptyView);

        mCursorAdapterPythonista = new PythonistaCursorAdapter(this, null);
        pythonistaListView.setAdapter(mCursorAdapterPythonista);

        pythonistaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PatronActivity.this, PythonistaActivity.class);
                Uri currentPythonistaUri = ContentUris.withAppendedId(
                        LibraryContract.LibraryEntry.PYTHONISTA_URI, id);
                intent.setData(currentPythonistaUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(PYTHONISTA_LOADER, null, this);

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
                insertTestDataPythonista();
                return true;
            case R.id.action_delete_all_data:
                deleteAllPythonista();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Checks if external storage is available for read
    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

     // inserts a row of test data into SQL table db
    private void insertTestDataPythonista() {

        BufferedReader br;

        //AssetManager am = getAssets();
        //final InputStream is = am.open("library.txt");
        // File pythonistas.txt is not uploaded to GitHub- user data held back
        try{
            final InputStream file = getAssets().open("pythonistas.txt");
            br = new BufferedReader(new InputStreamReader(file));
            String line = br.readLine();
            while(line != null){
                Log.d("Show Pythonistas", line);
                // -1 used to deal with empty values in csv file
                String[] stuff = line.split(",", -1);
                for (int i=0; i<4; i++){
                    Log.d( "stuff", stuff[i] +"\n");
                }
                ContentValues values = new ContentValues();
                values.put(LibraryContract.LibraryEntry.COL_L_NAME, stuff[0]);
                values.put(LibraryContract.LibraryEntry.COL_F_NAME, stuff[1]);
                values.put(LibraryContract.LibraryEntry.COL_PHONE, stuff[2]);
                values.put(LibraryContract.LibraryEntry.COL_EMAIL, stuff[3]);
                Uri uri = getContentResolver().insert(LibraryContract.LibraryEntry.PYTHONISTA_URI, values);

                line = br.readLine();
            }


        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        /*
        ContentValues values = new ContentValues();
        values.put(LibraryContract.LibraryEntry.COL_F_NAME, "Mike");
        values.put(LibraryContract.LibraryEntry.COL_L_NAME, "Rehner");
        values.put(LibraryContract.LibraryEntry.COL_EMAIL, "mrehner@e-wrench.net");
        values.put(LibraryContract.LibraryEntry.COL_PHONE, "614 477 6537");
        */
        //Log.v("LibraryActivity", "New Rows ID "+ newRowId);

        //Uri uri = getContentResolver().insert(LibraryContract.LibraryEntry.PYTHONISTA_URI, values);
    }


    private void deleteAllPythonista() {
        int deleteRowCount = getContentResolver().delete(LibraryContract.LibraryEntry.PYTHONISTA_URI, null, null);
        Log.v("PatronActivity", "Number of Rows deleted: " + deleteRowCount);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {LibraryContract.LibraryEntry._IDP,
                LibraryContract.LibraryEntry.COL_F_NAME,
                LibraryContract.LibraryEntry.COL_L_NAME};

        return new CursorLoader(this,
                LibraryContract.LibraryEntry.PYTHONISTA_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapterPythonista.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapterPythonista.swapCursor(null);
    }
}
