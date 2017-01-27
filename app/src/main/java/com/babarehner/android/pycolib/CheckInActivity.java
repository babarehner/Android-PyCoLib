package com.babarehner.android.pycolib;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.babarehner.android.pycolib.data.LibraryDbHelper;

import java.util.Calendar;
import java.util.List;

public class CheckInActivity extends AppCompatActivity {

    Spinner s, b;

    // Shows date for verification purpose only
    private TextView tvDisplayDate;
    private Button btnCheckInDate;

    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 999;

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


        addListenerButton();
        setCurrentDateOnView();

    }


    public void setCurrentDateOnView() {
        tvDisplayDate = (TextView) findViewById(R.id.tvCIDate);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        tvDisplayDate.setText(new StringBuilder()
                // Month starts with 0, add 1)
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));
    }


    public void addListenerButton() {
        btnCheckInDate = (Button) findViewById(R.id.button_return);
        btnCheckInDate.setOnClickListener(new View.OnClickListener() {
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
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            tvDisplayDate.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));
        }
    };
}
