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

    String[] nameID, bookID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        setTitle("Check Out Book");

        s = (Spinner) findViewById(R.id.name_co_spinner);
        LibraryDbHelper db = new LibraryDbHelper(getApplicationContext());
        List<String> names_list = db.getNames();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, names_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(dataAdapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //Log.v("name ", (String) parent.getItemAtPosition(pos));
                String name = (String) parent.getItemAtPosition(pos);
                // nameID[0] returns the primary key ID of a pythonista
                nameID = name.split(Pattern.quote("."));
                Log.v("nameID = ", nameID[0]);
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
                //Log.d("book", (String) parent.getItemAtPosition(pos));
                String book = (String) parent.getItemAtPosition(pos);
                bookID = book.split(Pattern.quote("."));
                Log.v("bookID = ", bookID[0]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Date Listener
        addListenerButton();
        setCurrentDateOnView();

    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }


    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void setCurrentDateOnView() {
        tvDisplayDate = (TextView) findViewById(R.id.tvCODate);
        // dpResult = (DatePicker) findViewById(R.id.dpResult);

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
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            StringBuilder sb = new StringBuilder().append(year)
                    .append("-").append(month + 1).append("-").append(day);

            // set selected date into textview
            tvDisplayDate.setText(sb);

            // After clicking on date, this is sent to update the Tloaned Table
            LibraryDbHelper db3 = new LibraryDbHelper(getApplicationContext());
            db3.checkOutBook(Integer.parseInt(bookID[0]), Integer.parseInt(nameID[0]), sb.toString());
            // dpResult.init(year, month, day, null);
        }
    };
    /*
    } public DbBackend(Context context) {
        super(context);
    }

    public class DbBackend extends DbObject {

        public DbBackend(Context context) {
            super(context);
        }


    public String[] getAllSpinnerContent(){

        String query = "Select * from content";
        Cursor cursor = this.getDbConnection().rawQuery(query, null);
        ArrayList<String> spinnerContent = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do{
                String word = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                spinnerContent.add(word);
            }while(cursor.moveToNext());
        }
        cursor.close();

        String[] allSpinner = new String[spinnerContent.size()];
        allSpinner = spinnerContent.toArray(allSpinner);

        return allSpinner;
    }


        public class MainActivity extends ActionBarActivity {

            private ArrayAdapter<String> listAdapter;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                Spinner spinner = (Spinner) findViewById(R.id.spinner);

                DbBackend dbBackend = new DbBackend(MainActivity.this);
                String[] spinnerLists = dbBackend.getAllSpinnerContent();

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item, spinnerLists);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        return;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.action_settings) {
                    return true;
                }

                return super.onOptionsItemSelected(item);
            }
        }
        */
}
