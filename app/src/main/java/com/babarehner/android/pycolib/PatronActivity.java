package com.babarehner.android.pycolib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class PatronActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patron);

        Toast.makeText(this, "Pythonistas NOT currenly implemented", Toast.LENGTH_LONG).show();
        /*
        // Create a floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatronActivity.this, PythonistaActivity.class);
                startActivity(intent);
            }
        }) ;
        */
    }
}
