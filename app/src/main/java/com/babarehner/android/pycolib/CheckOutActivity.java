package com.babarehner.android.pycolib;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.babarehner.android.pycolib.data.LibraryDbHelper;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class CheckOutActivity extends AppCompatActivity {

    Spinner s, b;

    private TextView tvDisplayDate;
    // Shows calendar when datepicker in activity_check_out_xml
    // private DatePicker dpResult;
    private Button btnCheckOutDate;

    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 999;

    private String[] nameID, bookID;

    public String book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        setTitle("Check Out Book");

        s = (Spinner) findViewById(R.id.name_co_spinner);
        LibraryDbHelper db = new LibraryDbHelper(getApplicationContext());
        List<String> names_list = db.getNames();

        // Default view- works fine
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, names_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(dataAdapter);

//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
//               R.layout.spinner_item, names_list);
//        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
//        s.setAdapter(dataAdapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE); // Sets selected text color blue
                //Log.v("name ", (String) parent.getItemAtPosition(pos));
                String name = (String) parent.getItemAtPosition(pos);
                // nameID[0] returns the primary key ID of a pythonista
                nameID = name.split(Pattern.quote("."));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        b = (Spinner) findViewById(R.id.book_co_spinner);
        LibraryDbHelper db2 = new LibraryDbHelper(getApplicationContext());
        List<String> titles_list = db2.getBooks();

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, titles_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.setAdapter(dataAdapter2);

        b.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // line below has been listed with a fatal exception (Null Pointer) error at times
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE); // Sets selected text color blue
                //Log.d("book", (String) parent.getItemAtPosition(pos));
                book = (String) parent.getItemAtPosition(pos);
                //bookID = book.split(Pattern.quote("."));
                bookID = book.split("\\.");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        addListenerButton();
        setCurrentDateOnView();
    }


    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void setCurrentDateOnView() {
        //tvDisplayDate = (TextView) findViewById(R.id.tvCODate);
        // dpResult = (DatePicker) findViewById(R.id.dpResult);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

//        tvDisplayDate.setText(new StringBuilder()
//            // Month starts with 0, add 1)
//            .append(month + 1).append("-").append(day).append("-")
//            .append(year).append(" "));
    }

    public void addListenerButton() {
        btnCheckOutDate = (Button) findViewById(R.id.button_borrow);
        btnCheckOutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener datePickerListener =
            new DatePickerDialog.OnDateSetListener(){

        // when dialog box is closed, below method will be called
        public void onDateSet(DatePicker v, int selectedYear, int selectedMonth, int selectedDay) {
            // if statement below uses validation to keep onDateSet from firing twice!!!!!!
            if (v.isShown()) {
                year = selectedYear;
                month = selectedMonth;
                day = selectedDay;

                StringBuilder sb = new StringBuilder().append(year)
                        .append("-").append(month + 1).append("-").append(day);

                // set selected date into textview not shown to make ui simpler
                // tvDisplayDate.setText(sb);

                // After clicking on date, this is sent to update the Tloaned Table
                Log.v("Before db call: ", bookID[0] + bookID[1] + nameID[1]);
                LibraryDbHelper db3 = new LibraryDbHelper(getApplicationContext());
                db3.checkOutBook(Integer.parseInt(bookID[0]), Integer.parseInt(nameID[0]), sb.toString());
                // needed full name of class when dealing with call backs
                Toast t = Toast.makeText(CheckOutActivity.this, (nameID[1] + " checked out '" + bookID[1] + "'/ on " + sb.toString()), Toast.LENGTH_LONG);
                t.show();
            }
        }
    };

}
