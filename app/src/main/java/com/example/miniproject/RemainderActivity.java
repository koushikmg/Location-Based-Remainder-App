package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class RemainderActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remainder);
        bt = (ImageButton) findViewById(R.id.imageButton2);
        bt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(RemainderActivity.this,AddItemActivity.class);
        startActivity(intent);
    }
}