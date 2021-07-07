package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddItemActivity extends AppCompatActivity {
    private Button submit;
    private EditText location;
    private EditText itemName;


    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference reference;

    // creating a variable for
    // our object class
    ExampleItem exampleItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        submit = findViewById(R.id.submit);
        location = findViewById(R.id.location);
        itemName = findViewById(R.id.itemname);

        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        reference = firebaseDatabase.getInstance().getReference().child("member");

        exampleItem = new ExampleItem();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String place = location.getText().toString();
                String item = itemName.getText().toString();




                if (place == null || item == null || TextUtils.isEmpty(place) || TextUtils.isEmpty(item)) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(AddItemActivity.this, "Please add some data.", Toast.LENGTH_SHORT).show();
                } else {
                    // else call the method to add
                    // data to our database.

                    addDatatoFirebase(place, item);

                    Intent intent = new Intent(AddItemActivity.this,RemainderActivity.class);
                    startActivity(intent);
                }
            }

        });


    }


    private void addDatatoFirebase(String place, String item) {
        UUID uuid = UUID. randomUUID();
        exampleItem.setItem(item);
        exampleItem.setPlace(place);
        exampleItem.setItemId(uuid.toString());

        reference.child(uuid.toString()).setValue(exampleItem);

    }

}