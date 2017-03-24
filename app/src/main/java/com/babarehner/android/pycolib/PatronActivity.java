package com.babarehner.android.pycolib;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.babarehner.android.pycolib.data.LibraryContract;

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
        getMenuInflater().inflate(R.menu.menu_patron, menu);
        return true;
    }

    // Select from the options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_pythonista:
                Intent intent = new Intent(PatronActivity.this, PythonistaActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
