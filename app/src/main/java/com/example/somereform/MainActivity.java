package com.example.somereform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    /// we are going to use four fledge app,firebase and fireStore is used as backend.

    // we are designing self app allow user to keeep a joural,like self reflection
    // We all are way outer

    private Button getStartedButton;














    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getStartedButton=findViewById(R.id.startButton);

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

     startActivity(new Intent(MainActivity.this,LoginActivity.class));



            }



            //to listen wht to do when get started.
            //to listen call upon



        });













    }
}