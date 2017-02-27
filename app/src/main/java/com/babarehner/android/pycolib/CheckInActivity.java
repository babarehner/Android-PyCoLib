package com.babarehner.android.pycolib;

import android.app.DatePickerDialog;
import android.app.Dialog;
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

import com.babarehner.android.pycolib.data.LibraryDbHelper;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class CheckInActivity extends AppCompatActivity {

    Spinner b;

    // Shows date for verification purpose only
    private TextView tvDisplayDate;
    private Button btnCheckInDate;

    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 999;

    String[] loanedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        setTitle("Check In Book");


        b = (Spinner) findViewById(R.id.book_ci_spinner);
        LibraryDbHelper db = new LibraryDbHelper(getApplicationContext());
        List<String> titles_list = db.getCheckOutBooks();

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, titles_list);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.setAdapter(dataAdapter2);

        b.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // pull title and then titleID out of Spinner
                String loaned = (String) parent.getItemAtPosition(pos);
                loanedID = loaned.split(Pattern.quote("."));
                Log.v("loanedID= ", loanedID[0]);
                setPythonistaOnView();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {  // required for spinners
            }
        });



        addListenerButton();
        setCurrentDateOnView();

    }


    public void setPythonistaOnView(){
        TextView tvPythonista = (TextView) findViewById(R.id.pythonist_return);

        // SELECT P.FirstName, P.LastName, P._id,  L._id, L.Name From TPythonistas P LEFT OUTER JOIN (Select TLoaned.Name, TLoaned._id FROM TLoaned WHERE TLoaned._id = 6) L WHERE L.Name = P._id;
        LibraryDbHelper db = new LibraryDbHelper(getApplicationContext());

        String s = db.getPythonistaName(Integer.parseInt(loanedID[0]));
        Log.v("String s= ", s);

        tvPythonista.setText( s);
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

            StringBuilder sb = new StringBuilder().append(year)
                    .append("-").append(month+1).append("-").append(day);

            // set selected date into textview
            tvDisplayDate.setText(sb);

            // After clicking on date, this is sent to update the Tloaned Table
            LibraryDbHelper dbh = new LibraryDbHelper(getApplicationContext());
            dbh.updateReturnDate(Integer.parseInt(loanedID[0]), sb.toString());
        }
    };
}
