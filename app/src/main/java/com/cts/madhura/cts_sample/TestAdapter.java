package com.cts.madhura.cts_sample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.Collections;
import java.util.List;

/**
 * We bind data to views and recycle views when the user scrolls through RecyclerView.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.RowHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<Test> data= Collections.emptyList();

    /* Intialise context and data sent from DisplayActivity */
    public TestAdapter(Context context, List<Test> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test, parent, false);
        RowHolder rowHolder = new RowHolder(view);
        return rowHolder;
    }

    /* Bind data */
    @Override
    public void onBindViewHolder(RowHolder rowHolder, int position) {

        //Get current position of item in recyclerview to bind data and assign values from list
        RowHolder holder = (RowHolder) rowHolder;
        Test current=data.get(position);
        holder.title.setText(current.title);
        holder.description.setText(current.description);

        //load image into imageview using glide
        Glide.with(context).load(current.imageHref).into(holder.img);
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class RowHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView img;
        TextView description;

        // create constructor to get widget reference
        public RowHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.title);
            img= (ImageView) itemView.findViewById(R.id.imgRef);
            description = (TextView) itemView.findViewById(R.id.testDescription);
        }
    }
}
