package com.babarehner.android.pycolib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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
    }
}
