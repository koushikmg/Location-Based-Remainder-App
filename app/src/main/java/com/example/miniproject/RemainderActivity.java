package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class RemainderActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<ExampleItem> exampleItems ;
    ImageButton addItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remainder);
        addItem = (ImageButton) findViewById(R.id.addItemButton);
        addItem.setOnClickListener(this);

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
    }

    private void createItemList(){
        exampleItems = new ArrayList<>();
        exampleItems.add(new ExampleItem("Jaynagar","Jacket"));
        exampleItems.add(new ExampleItem("Jaynagar1","Jacket1"));
        exampleItems.add(new ExampleItem("Jaynagar2","Jacket2"));
        exampleItems.add(new ExampleItem("Jaynagar3","Jacket3"));
        exampleItems.add(new ExampleItem("Jaynagar4","Jacket4"));
        exampleItems.add(new ExampleItem("Jaynagar5","Jacket5"));
        exampleItems.add(new ExampleItem("Jaynagar6","Jacket6"));
        exampleItems.add(new ExampleItem("Jaynagar7","Jacket7"));
        exampleItems.add(new ExampleItem("Jaynagar8","Jacket8"));
        exampleItems.add(new ExampleItem("Jaynagar9","Jacket9"));
        exampleItems.add(new ExampleItem("Jaynagar10","Jacket10"));
        exampleItems.add(new ExampleItem("Jaynagar11","Jacket11"));
        exampleItems.add(new ExampleItem("Jaynagar12","Jacket12"));
        exampleItems.add(new ExampleItem("Jaynagar13","Jacket13"));
        exampleItems.add(new ExampleItem("Jaynagar14","Jacket14"));

        Intent intent = getIntent();
        String newplace = intent.getStringExtra("location");
        String newitemname = intent.getStringExtra("item");
        Boolean addNewItem = intent.getBooleanExtra("flag",false);
        if(addNewItem){
            exampleItems.add(new ExampleItem(newplace,newitemname));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(RemainderActivity.this,AddItemActivity.class);
        startActivity(intent);
    }
}