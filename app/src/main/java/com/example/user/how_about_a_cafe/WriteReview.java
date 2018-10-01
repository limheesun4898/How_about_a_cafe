package com.example.user.how_about_a_cafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//리뷰는 무조건 별점이 있어야하고 별점 없이는 리뷰를 작성할 수 없도록 만듬. 그래서 별점이 없는 리뷰는 없음.
public class WriteReview extends AppCompatActivity {
    public static DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private EditText editReview;
    private TextView textNum;
    private Spinner spinner;
    private RatingBar ratingBar;
    private SpinnerAdapter spinnerAdapter;
    private float rating;
    private String cafe_name;
    private List<String> data;
    private String menu;

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfDate = new SimpleDateFormat("YYYY-MM-dd (HH:mm:ss)");
    String formatDate = sdfDate.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        Intent intent = getIntent();
        cafe_name = intent.getStringExtra("cafe_name");

        textNum = (TextView) findViewById(R.id.write_review_textNum);
        editReview = (EditText) findViewById(R.id.write_review_edit);
        spinner = (Spinner) findViewById(R.id.spinner);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar1);

        Toolbar mytoolbar = findViewById(R.id.write_review_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Review();       //리뷰쓸 때 글자 수 세주는 함수

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = v;     //v가 별 개수
            }
        });

        data = new ArrayList<>();
        firebaseDatabase.child(cafe_name).child("사이드 메뉴").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    data.add(snapshot.getKey());

                }

                spinnerAdapter = new SpinnerAdapter(WriteReview.this, data);
                spinner.setAdapter(spinnerAdapter);
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
                spinnerAdapter = new SpinnerAdapter(WriteReview.this, data);
                spinner.setAdapter(spinnerAdapter);
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
                spinnerAdapter = new SpinnerAdapter(WriteReview.this, data);
                spinner.setAdapter(spinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                menu = spinnerAdapter.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            finish();
        }
    };

    public void onBackPressed() {
        String getEdit2 = editReview.getText().toString();

        if (getEdit2.getBytes().length <= 0)
            finish();
        else
            alt();      //작성한 내용이 있는데 종료시키려고 할 때 뜨는 다이얼로그
    }

    private void Review() {
        editReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textNum.setText(charSequence.length() + " /200");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void alt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("작성중인 내용을 저장하지 않고 나가시겠습니까?");
        builder.setPositiveButton("확인", dialogListener);
        builder.setNegativeButton("취소", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.write_review, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.write_review_send:        //리뷰 파이어베이스에 저장해주는 부붑
                if (rating == 0.0)
                    Toast.makeText(this, "별점을 지정해주세요", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(WriteReview.this, "리뷰가 작성되었습니다", Toast.LENGTH_SHORT).show();

                    ReviewItem a = new ReviewItem(editReview.getText().toString(), String.valueOf(rating));     //작성한 리뷰와 별점이 파이어베이스에 올라감
                    FirebaseDatabase.getInstance().getReference().child("Review").child(menu).child(formatDate).setValue(a); //formatData는 작성한 시간
                    finish();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
