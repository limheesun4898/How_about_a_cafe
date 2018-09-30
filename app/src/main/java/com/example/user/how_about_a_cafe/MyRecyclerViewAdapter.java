package com.example.user.how_about_a_cafe;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{
    List<ListItem> itemList;
    Context context;
    View view;
    public MyRecyclerViewAdapter(List<ListItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
    public MyRecyclerViewAdapter(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        int width = parent.getResources().getDisplayMetrics().widthPixels / 2;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(width, width));
        MyRecyclerViewAdapter.ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.ViewHolder holder, final int position) {
        final ListItem item = itemList.get(position);
        MyRecyclerViewAdapter.ViewHolder viewHolder = (MyRecyclerViewAdapter.ViewHolder)holder;

        Glide.with(context).load(item.getImage()).into(holder.image);
        holder.imagetext.setText(item.getTitle());
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MenuList.class);
                intent.putExtra("img", item.image);
                intent.putExtra("cafe_name", item.title);
                intent.putExtra("url",item.url);
                ((AppCompatActivity) context).startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView imagetext;
        public ViewHolder(@NonNull View view) {
            super(view);
            image = view.findViewById(R.id.recyclerview_image);
            imagetext = view.findViewById(R.id.recyclerview_text);
        }

    }

}
