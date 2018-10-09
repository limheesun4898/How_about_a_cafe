package com.example.user.how_about_a_cafe;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.how_about_a_cafe.ListReview.mItems;

public class MyReview extends AppCompatActivity {
    public static ArrayList<MyReviewItem> mItems = new ArrayList<>();
    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public static DatabaseReference databaseReference = firebaseDatabase.getReference();
    public static StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
    private RecyclerView.Adapter recycleradapter;
    private RecyclerView recyclerView;
    private TextView null_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);

        recyclerView = (RecyclerView) findViewById(R.id.my_review_recyclerview);
        null_text = (TextView) findViewById(R.id.my_review_null_text);
        Toolbar mytoolbar = findViewById(R.id.my_review_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recycleradapter = new MyReviewAdapter(mItems, MyReview.this);
        recyclerView.setAdapter(recycleradapter);
        mItems.clear();
        if (uid != null) {
            if (databaseReference.child("UserReview").getKey() != null) {
                databaseReference.child("UserReview").child(uid).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        MyReviewItem item = dataSnapshot.getValue(MyReviewItem.class);
                        if (item.isIsimage()) {
                            MyReviewItem a = new MyReviewItem(item.getReview(), item.getRating(), item.getUrl(), item.getFormatDate(), item.getName(), item.getProfile_image(), item.getMenu(), item.getCafe_name(), item.getSelection(), item.isIsimage());
                            mItems.add(a);
                            recycleradapter.notifyDataSetChanged();

                        } else {
                            MyReviewItem a = new MyReviewItem(item.getReview(), item.getRating(), item.getFormatDate(), item.getName(), item.getProfile_image(), item.getMenu(), item.getCafe_name(), item.getSelection(), item.isIsimage());
                            mItems.add(a);
                            recycleradapter.notifyDataSetChanged();
                        }
                        addData();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        finish();
                        startActivity(getIntent());
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

    }



    public void addData() {
        if (mItems.size() == 0) {
            null_text.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            null_text.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
