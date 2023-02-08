package com.example.javaquizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MechanicsActivity extends AppCompatActivity {

    Button button_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanics);

        button_main = findViewById(R.id.button_main);

        button_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MechanicsActivity.this,MainActivity.class);
                startActivity(intent);
                MechanicsActivity.super.onBackPressed();
            }
        });
    }
}