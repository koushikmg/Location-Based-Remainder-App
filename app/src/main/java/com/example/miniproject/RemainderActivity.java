package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RemainderActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ExampleAdapter mAdapter;
    private ArrayList<ExampleItem> exampleItems ;
    ImageButton addItem;
    String userID;



    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    //Getting user id from firebase auth
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remainder);
        addItem = (ImageButton) findViewById(R.id.addItemButton);
        addItem.setOnClickListener(this);
        exampleItems = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        createItemList();

        createRecyclerView();

    }

    private void createRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(exampleItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick( int position) {
                removeItem(position);
            }
        });

    }

    public void removeItem(int position) {
        exampleItems.remove(position);
        mAdapter.notifyItemRemoved(position);

    }

    private void createItemList(){
       exampleItems.clear();

       databaseReference = FirebaseDatabase.getInstance().getReference(userID);

       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                   ExampleItem item = dataSnapshot.getValue(ExampleItem.class);
                   exampleItems.add(item);

               }

               mAdapter.notifyDataSetChanged();

           }

           @Override
           public void onCancelled(@NonNull  DatabaseError error) {

           }
       });


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(RemainderActivity.this,AddItemActivity.class);
        startActivity(intent);
    }
}