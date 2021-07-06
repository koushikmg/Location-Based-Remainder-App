package com.example.miniproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> mExampleItems;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public TextView placeView;
        public TextView itemView;

        public ExampleViewHolder( View view) {
            super(view);
            placeView = view.findViewById(R.id.place);
            itemView = view.findViewById(R.id.item);
        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleItems){
        mExampleItems = exampleItems;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.examplelayout,parent,false);
        ExampleViewHolder exampleViewHolder = new ExampleViewHolder(v);
        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(ExampleAdapter.ExampleViewHolder holder, int position) {
        ExampleItem exampleItem = mExampleItems.get(position);
        holder.placeView.setText(exampleItem.getPlace());
        holder.itemView.setText(exampleItem.getItem());
    }

    @Override
    public int getItemCount() {
        return mExampleItems.size();
    }
}
