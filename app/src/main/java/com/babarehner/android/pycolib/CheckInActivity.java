package com.babarehner.android.pycolib;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

    private LibraryDbHelper dbHelp;

    private QueryCaddy qc;
    private String pyName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        setTitle("Check In Book");


        b = (Spinner) findViewById(R.id.book_ci_spinner);
        // Is context class based or function/method based???
        dbHelp = new LibraryDbHelper(getApplicationContext());
        List<String> titles_list = dbHelp.getCheckOutBooks();

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, titles_list);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.setAdapter(dataAdapter2);

        b.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE); // Sets selected text color blue
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


    public void sendEmail(View v){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        LibraryDbHelper dbh = new LibraryDbHelper(getApplicationContext());
        qc = dbh.getFiller(Integer.parseInt(loanedID[0]));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {qc.getEmail()});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Python Book " + qc.getBookTitle());
        String subject = qc.getName() + "\n\n" + "Hope you are doing well. Just to let you know that " +
                "other Pythonistas may want to borrow " + "'" + qc.getBookTitle() +
                "' from the Python library on " + qc.getLoanDate() + "." +
                "We are still meeting every Friday at Panera. If I'm not there, please give the book " +
                "to another Pythonista such as Travis or Jim." +
                "\n\n Cheers, \n\n Mike";
        intent.putExtra(Intent.EXTRA_TEXT, subject);
        intent.setData(Uri.parse("mailto:"));   //Only e-mail should handle this
        // check if intent can be handled, let user decide e-mail client
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    public void sendText(View v){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        LibraryDbHelper dbh = new LibraryDbHelper(getApplicationContext());
        qc = dbh.getFiller(Integer.parseInt(loanedID[0]));
        String message = "Hope you are doing well. Please return '" + qc.getBookTitle() + "' borrowed from the Python library on " +
                qc.getLoanDate() + ". Please help share this title with other pythonistas- Mike";
        intent.putExtra("sms_body", message);
        intent.setData(Uri.parse("smsto:" + qc.getPhoneNo()));    // only sms apps respond
        // Check if intent can be handled
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    public void setPythonistaOnView(){
        TextView tvPythonista = (TextView) findViewById(R.id.pythonist_return);

        // SELECT P.FirstName, P.LastName, P._id,  L._id, L.Name From TPythonistas P LEFT OUTER JOIN (Select TLoaned.Name, TLoaned._id FROM TLoaned WHERE TLoaned._id = 6) L WHERE L.Name = P._id;
        //LibraryDbHelper db = new LibraryDbHelper(getApplicationContext());

        pyName = dbHelp.getPythonistaName(Integer.parseInt(loanedID[0]));
        Log.v("String s= ", pyName);

        tvPythonista.setText( pyName);
    }


    public void setCurrentDateOnView() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

//        tvDisplayDate.setText(new StringBuilder()
//                // Month starts with 0, add 1)
//                .append(month + 1).append("-").append(day).append("-")
//                .append(year).append(" "));
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
            // tvDisplayDate.setText(sb);
            showCheckInConfirmationDialog(sb.toString());
        }
    };


    private void showCheckInConfirmationDialog(final String s){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        String msg = (pyName + " wishes to check in '" + loanedID[1] + "' on "+ s);
        b.setMessage(msg);
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Log.v("Before db call: ", bookID[0] + bookID[1] + nameID[1]);
                LibraryDbHelper dbh = new LibraryDbHelper(getApplicationContext());
                // update TLoaned Table
                dbh.updateReturnDate(Integer.parseInt(loanedID[0]), s);;
                // needed full name of class when dealing with call backs
                Toast t = Toast.makeText(CheckInActivity.this, (pyName + " checked in '" + loanedID[1] + "'/ on " + s), Toast.LENGTH_LONG);
                t.show();
                Intent intent = new Intent(CheckInActivity.this, MainActivity.class);  // Go back to main menu
                startActivity(intent);
            }
        });

        b.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked cancel, try again
                if (dialog !=null){
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alert = b.create();
        alert.show();
    }

}
