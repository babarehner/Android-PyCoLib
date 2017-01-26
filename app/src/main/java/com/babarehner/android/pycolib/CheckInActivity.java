package com.babarehner.android.pycolib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.babarehner.android.pycolib.data.LibraryDbHelper;

import java.util.List;

public class CheckInActivity extends AppCompatActivity {

    Spinner s, b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        setTitle("Check In Book");

        s = (Spinner) findViewById(R.id.name_ci_spinner);
        LibraryDbHelper db = new LibraryDbHelper(getApplicationContext());
        List<String> names_list = db.getNames();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, names_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(dataAdapter);

        b = (Spinner) findViewById(R.id.book_ci_spinner);
        LibraryDbHelper db2 = new LibraryDbHelper(getApplicationContext());
        List<String> titles_list = db2.getBooks();

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, titles_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.setAdapter(dataAdapter2);

    }
}
