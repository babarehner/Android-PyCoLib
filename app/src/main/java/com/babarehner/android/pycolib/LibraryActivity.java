package com.babarehner.android.pycolib;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;

public class LibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // Changes the title bar text color. Need to hunt down a way to use findByID(T.id.color)
        String title = "PyCoLib";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
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
                // insertBook;
                // displayData:
                return true;
            case R.id.action_delete_all_data:
                // deleteData
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
