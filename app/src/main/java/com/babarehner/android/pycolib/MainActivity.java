package com.babarehner.android.pycolib;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.babarehner.android.pycolib.data.LibraryContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);

        TextView library = (TextView) findViewById(R.id.library_page);
        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent libraryIntent = new Intent(MainActivity.this, LibraryActivity.class);
                startActivity(libraryIntent);
            }
        });

        TextView patron = (TextView) findViewById(R.id.patron_page);
        patron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent patronIntent = new Intent(MainActivity.this, PatronActivity.class);
                startActivity(patronIntent);
            }
        });

        TextView checkOut = (TextView) findViewById(R.id.checkout_page);
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkOutIntent = new Intent(MainActivity.this, CheckOutActivity.class);
                startActivity(checkOutIntent);
            }
        });

        TextView checkIn = (TextView) findViewById(R.id.checkin_page);
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkInIntent = new Intent(MainActivity.this, CheckInActivity.class);
                startActivity(checkInIntent);
            }
        });
    }

    // Create an  options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin_test, menu);
        return true;
    }

    // Select from the options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_test_books:
                insertTestDataBook();
                return true;
            case R.id.action_insert_test_pythonistas:
                insertTestDataPythonista();
                return true;
            case R.id.action_delete_all_data:
                deleteAll();
                return true;
            case R.id.action_backup_database:
                backupDB();
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

    }


    // Checks if external storage is available for read
    // Not currently used- concept is to protect pythonista data instead of embedding
    // it in the app as it currently is.
    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        // returns boolean true or false
        return Environment.MEDIA_MOUNTED.equals(state);
        /*
         if (Environment.MEDIA_MOUNTED.equals(state)) {
         return true;
         }
         return false;
         */
    }


    // inserts a row of test data into SQL table db
    private void insertTestDataPythonista() {

        BufferedReader br;

        //AssetManager am = getAssets();
        //final InputStream is = am.open("library.txt");
        // File pythonistas.txt is not uploaded to GitHub- using .gitignore
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
    }


    private void deleteAll() {
        int deleteRowCountBooks = getContentResolver().delete(LibraryContract.LibraryEntry.CONTENT_URI, null, null);
        Log.v("LibraryActivity", "Number of Rows deleted: " + deleteRowCountBooks);
        int deleteRowCountPythonistas = getContentResolver().delete(LibraryContract.LibraryEntry.PYTHONISTA_URI, null, null);
        Log.v("PatronActivity", "Number of Rows deleted: " + deleteRowCountPythonistas);
    }

    private void backupDB(){
        Toast.makeText(this, "Backup of database is not yet implemented",
                Toast.LENGTH_SHORT).show();
    }

}
