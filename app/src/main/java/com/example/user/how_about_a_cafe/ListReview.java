package com.example.user.how_about_a_cafe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListReview extends AppCompatActivity {
    public static ArrayList<ReviewItem> mItems = new ArrayList<>();
    public static DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private Spinner list_review_spinner;
    private String cafe_name;
    private List<String> data;
    private SpinnerAdapter spinnerAdapter;
    private String menu;
    private RecyclerView.Adapter recycleradapter;
    private RecyclerView recyclerView;
    private TextView null_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_review);

        Intent intent = getIntent();
        cafe_name = intent.getStringExtra("cafe_name");

        list_review_spinner = (Spinner) findViewById(R.id.list_review_spinner);
        recyclerView = (RecyclerView) findViewById(R.id.list_review_recyclerview);
        null_text = (TextView) findViewById(R.id.list_review_null_text);

        Toolbar mytoolbar = findViewById(R.id.list_review_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recycleradapter = new ListReviewRecyclerAdapter(mItems, ListReview.this);
        recyclerView.setAdapter(recycleradapter);

        mItems.clear();

        data = new ArrayList<>();
        firebaseDatabase.child(cafe_name).child("사이드 메뉴").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    data.add(snapshot.getKey());

                }

                spinnerAdapter = new SpinnerAdapter(ListReview.this, data);
                list_review_spinner.setAdapter(spinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        firebaseDatabase.child(cafe_name).child("사이드메뉴").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    data.add(snapshot.getKey());

                }
                spinnerAdapter = new SpinnerAdapter(ListReview.this, data);
                list_review_spinner.setAdapter(spinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        firebaseDatabase.child(cafe_name).child("음료").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    data.add(snapshot.getKey());

                    if (snapshot.getKey().equals("SIZE")) {
                        data.remove("SIZE");
                        data.remove(snapshot.getValue() + "원");
                    }

                    if (snapshot.getKey().equals("SIZE2")) {
                        data.remove("SIZE2");
                        data.remove(snapshot.getValue() + "원");
                    }


                    if (snapshot.getKey().equals("ICE")) {
                        data.remove("ICE");
                        data.remove(snapshot.getValue() + "원");
                    }

                    if (snapshot.getKey().equals("ICE2")) {
                        data.remove("ICE2");
                        data.remove(snapshot.getValue() + "원");
                    }

                    if (snapshot.getKey().equals("SIZE_CNT")) {
                        data.remove("SIZE_CNT");
                        data.remove(snapshot.getValue() + "원");
                    }

                }
                spinnerAdapter = new SpinnerAdapter(ListReview.this, data);
                list_review_spinner.setAdapter(spinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        list_review_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, View view, int i, long l) {
                menu = spinnerAdapter.getItem(i).toString();

                if (firebaseDatabase.child("Review").getKey() != null) {
                    firebaseDatabase.child("Review").child(menu).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ReviewItem item = dataSnapshot.getValue(ReviewItem.class);

                            if (item == null) {
                                null_text.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                            else {
                                recyclerView.setVisibility(View.VISIBLE);
                                null_text.setVisibility(View.GONE);
                                if (item.isIsimage()){
                                    ReviewItem a = new ReviewItem(item.getReview(), item.getRating(),item.getUrl(), item.isIsimage());
                                    mItems.add(a);
                                    recycleradapter.notifyDataSetChanged();
                                }

                                else{
                                    ReviewItem a = new ReviewItem(item.getReview(), item.getRating(), item.isIsimage());
                                    mItems.add(a);
                                    recycleradapter.notifyDataSetChanged();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_review, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.write_review_btn:
                Intent intent = new Intent(ListReview.this, WriteReview.class);
                intent.putExtra("cafe_name", cafe_name);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}
