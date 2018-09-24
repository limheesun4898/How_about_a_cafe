package com.example.user.how_about_a_cafe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MenuList extends AppCompatActivity {
    public static DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private ExpandableListView listView;
    private ImageView image;
    private String url;
    private myGroup side;
    private myGroup drink;
    private boolean click = false;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        Intent intent = getIntent();
        Item item = new Item(intent.getStringExtra("cafe_name"));
        data = item.getCafe_name();

        final ArrayList<myGroup> DataList = new ArrayList<myGroup>();
        listView = (ExpandableListView) findViewById(R.id.expanded_menu);
//        image = (ImageView) findViewById(R.id.childImage);
        Button cal_btn = (Button) findViewById(R.id.cal_btn);

        cal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        firebaseDatabase.child(data).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                side = new myGroup("사이드 메뉴");
                firebaseDatabase.child(data).child("사이드 메뉴").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            side.child.add(snapshot.getKey());
                            side.childPrice.add(snapshot.getValue().toString() + "원");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                firebaseDatabase.child(data).child("사이드메뉴").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            side.child.add(snapshot.getKey());
                            side.childPrice.add(snapshot.getValue().toString() + "원");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                DataList.add(side);

                drink = new myGroup("음료");
                firebaseDatabase.child(data).child("음료").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            drink.child.add(snapshot.getKey());
                            drink.childPrice.add(snapshot.getValue().toString() + "원");

                            if (snapshot.getKey().equals("SIZE")) {
                                drink.child.remove("SIZE");
                                drink.childPrice.remove(snapshot.getValue() + "원");
                            }

                            if (snapshot.getKey().equals("SIZE2")) {
                                drink.child.remove("SIZE2");
                                drink.childPrice.remove(snapshot.getValue() + "원");
                            }


                            if (snapshot.getKey().equals("ICE")) {
                                drink.child.remove("ICE");
                                drink.childPrice.remove(snapshot.getValue() + "원");
                            }

                            if (snapshot.getKey().equals("ICE2")) {
                                drink.child.remove("ICE2");
                                drink.childPrice.remove(snapshot.getValue() + "원");
                            }

                            if (snapshot.getKey().equals("SIZE_CNT")) {
                                drink.child.remove("SIZE_CNT");
                                drink.childPrice.remove(snapshot.getValue() + "원");
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                DataList.add(drink);

                ExpandAdapter adapter = new ExpandAdapter(getApplicationContext(), R.layout.group_row, R.layout.child_row, DataList);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                CustomDialog customDialog = new CustomDialog(MenuList.this);
                customDialog.callFunction(DataList.get(i).child.get(i1), data, String.valueOf(i));
                return true;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_list_event:
                Toast.makeText(getApplicationContext(), "EVENT", Toast.LENGTH_LONG).show();
                return true;

            case R.id.menu_list_review:
                Toast.makeText(getApplicationContext(), "REVIEW", Toast.LENGTH_LONG).show();
                return true;

            case R.id.menu_list_favorite:
                Toast.makeText(getApplicationContext(), "favorite", Toast.LENGTH_LONG).show();
                if (click) {
                    item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                    click = false;
                } else {
                    item.setIcon(R.drawable.ic_favorite_black_24dp);
                    click = true;
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
