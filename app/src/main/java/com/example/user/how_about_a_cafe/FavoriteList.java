package com.example.user.how_about_a_cafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.thekhaeng.recyclerviewmargin.LinearLayoutMargin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoriteList extends AppCompatActivity {

    private List<ListItem> itemList;
    private MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("즐겨찾기");

        RecyclerView recyclerView = findViewById(R.id.main_recyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        LinearLayoutMargin layoutMargin = new LinearLayoutMargin(20);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(layoutMargin);

        setupEmpty();

        setItem(recyclerView);
    }

    private void setupEmpty() {

    }

    private void setItem(RecyclerView recyclerView) {
        SharedPref sharedPref = new SharedPref();
        itemList = sharedPref.getFavorites(this);

        if (itemList == null) {
            itemList = new ArrayList<>();
        }

        adapter = new MyRecyclerViewAdapter(itemList, this);
        Collections.reverse(itemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String output = data.getStringExtra("refresh");
                if (output.equals("true")) {
                    recreate();
                }
            }
        }
    }
}
