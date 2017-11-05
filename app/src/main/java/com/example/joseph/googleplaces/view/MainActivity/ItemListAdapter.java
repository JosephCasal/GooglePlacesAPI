package com.example.joseph.googleplaces.view.MainActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joseph.googleplaces.R;
import com.example.joseph.googleplaces.model.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joseph on 11/5/17.
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {

    private static final String TAG = "ItemListAd";
    List<Result> itemList = new ArrayList<>();
    Context context;

    public ItemListAdapter(List<Result> itemList) {
        this.itemList = itemList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Result item = itemList.get(position);
        holder.textView.setText(item.getName());
        Glide.with(context).load(item.getIcon()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivIcon);
            textView = itemView.findViewById(R.id.tvName);

        }
    }
}