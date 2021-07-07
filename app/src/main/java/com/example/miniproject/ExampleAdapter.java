package com.example.miniproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;



import java.util.ArrayList;


public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> mExampleItems;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mListener;
    }

    public interface OnItemClickListener{
        public void onDeleteClick(int position);
    }




    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public TextView placeView;
        public TextView itemView;
        public ImageView delete;

        public ExampleViewHolder( View view, final OnItemClickListener listener) {
            super(view);
            placeView = view.findViewById(R.id.place);
            itemView = view.findViewById(R.id.item);
            delete = view.findViewById(R.id.delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                    {
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleItems){
        mExampleItems = exampleItems;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.examplelayout,parent,false);
        ExampleViewHolder exampleViewHolder = new ExampleViewHolder(v,mListener);
        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleItem exampleItem = mExampleItems.get(position);
        holder.placeView.setText(exampleItem.getPlace());
        holder.itemView.setText(exampleItem.getItem());


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Delete Reminder");
                builder.setMessage("Delete...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseDatabase.getInstance().getReference().child("member")
                                .child(exampleItem.getItemId()).removeValue();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExampleItems.size();
    }
}
