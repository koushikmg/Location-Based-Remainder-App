package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddItemActivity extends AppCompatActivity {
    Button submit;
    EditText location;
    EditText itemName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        submit = findViewById(R.id.submit);
        location = findViewById(R.id.location);
        itemName = findViewById(R.id.itemname);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItemActivity.this,RemainderActivity.class);
                intent.putExtra("location",location.getText().toString());
                intent.putExtra("item",itemName.getText().toString());
                intent.putExtra("flag",true);
                startActivity(intent);
            }
        });
    }

}