package com.example.user.how_about_a_cafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    public static DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private StorageReference mStorge = FirebaseStorage.getInstance().getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
    private EditText editReview;
    private String name;
    private String profile;
    private TextView textNum;
    private Spinner spinner;
    private RatingBar ratingBar;
    private SpinnerAdapter spinnerAdapter;
    private float rating;
    private String cafe_name;
    private List<String> list_data;
    private String menu;
    private ImageView photo_select;
    private boolean text_null = true;
    static Uri downloadUrl;
    static boolean isimage = false;
    final int GALLERY_INTENT = 100;
    private String text;
    private int selection;
    private String data;

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfDate = new SimpleDateFormat("YYYY-MM-dd (HH:mm:ss)");
    String formatDate = sdfDate.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        textNum = (TextView) findViewById(R.id.edit_review_textNum);
        editReview = (EditText) findViewById(R.id.edit_review_edit);
        spinner = (Spinner) findViewById(R.id.edit_spinner);
        ratingBar = (RatingBar) findViewById(R.id.edit_ratingBar);
        photo_select = (ImageView) findViewById(R.id.edit_review_photo_select);

        Toolbar mytoolbar = findViewById(R.id.edit_review_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        data = intent.getStringExtra("date");

        Review();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = v;     //v가 별 개수
            }
        });

        firebaseDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("name").getValue().toString();
                profile = dataSnapshot.child("photo").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        firebaseDatabase.child("UserReview").child(uid).child(data).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MyReviewItem item = dataSnapshot.getValue(MyReviewItem.class);

                if (item.isIsimage())
                    downloadUrl = Uri.parse(item.getUrl());
                else
                    downloadUrl = null;

                formatDate = item.getFormatDate();
                cafe_name = item.getCafe_name();
                menu = item.getMenu();
                rating = Float.parseFloat(item.getRating());
                text = item.getReview();
                selection = Integer.parseInt(item.getSelection());
                isimage = item.isIsimage();

                setData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        list_data = new ArrayList<>();
        firebaseDatabase.child("스타벅스").child("사이드메뉴").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    list_data.add(snapshot.getKey());

                }
                spinnerAdapter = new SpinnerAdapter(EditActivity.this, list_data);
                spinner.setAdapter(spinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        firebaseDatabase.child("스타벅스").child("음료").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    list_data.add(snapshot.getKey());

                    if (snapshot.getKey().equals("SIZE")) {
                        list_data.remove("SIZE");
                        list_data.remove(snapshot.getValue() + "원");
                    }

                    if (snapshot.getKey().equals("SIZE2")) {
                        list_data.remove("SIZE2");
                        list_data.remove(snapshot.getValue() + "원");
                    }


                    if (snapshot.getKey().equals("ICE")) {
                        list_data.remove("ICE");
                        list_data.remove(snapshot.getValue() + "원");
                    }

                    if (snapshot.getKey().equals("ICE2")) {
                        list_data.remove("ICE2");
                        list_data.remove(snapshot.getValue() + "원");
                    }

                    if (snapshot.getKey().equals("SIZE_CNT")) {
                        list_data.remove("SIZE_CNT");
                        list_data.remove(snapshot.getValue() + "원");
                    }

                }
                spinnerAdapter = new SpinnerAdapter(EditActivity.this, list_data);
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

        photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pto();
            }
        });
    }

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
                if (charSequence.length() > 0)
                    text_null = false;
                else
                    text_null = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case GALLERY_INTENT:
                setList_image(data);
                break;

            default:
                break;

        }
    }

    public void setList_image(Intent data) {
        Uri file = data.getData();
        StorageReference filePath = mStorge.child("images/" + formatDate);

        filePath.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadUrl = taskSnapshot.getDownloadUrl();
                isimage = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditActivity.this, "예외 발생" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pto() {
        final int GALLERY_INTENT = 100;

        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, GALLERY_INTENT);
    }

    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            finish();
        }
    };

    public void setData() {
        editReview.setText(text);
        ratingBar.setRating(rating);
        spinner.setSelection(selection);
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
            case R.id.write_review_send:    //리뷰 파이어베이스에 저장해주는 부붑
                if (rating == 0.0 || text_null)
                    Toast.makeText(this, "별점과 리뷰을 작성해주세요", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(EditActivity.this, "리뷰가 수정되었습니다", Toast.LENGTH_SHORT).show();
                    ReviewItem a = new ReviewItem(editReview.getText().toString(), String.valueOf(rating), String.valueOf(downloadUrl), formatDate, name, profile, menu, cafe_name, String.valueOf(selection), isimage);
                    MyReviewItem b = new MyReviewItem(editReview.getText().toString(), String.valueOf(rating), String.valueOf(downloadUrl), formatDate, name, profile, menu, cafe_name, String.valueOf(selection), isimage);     //작성한 리뷰와 별점이 파이어베이스에 올라감
                    FirebaseDatabase.getInstance().getReference().child("UserReview").child(uid).child(data).setValue(b);//formatData는 작성한 시간
                    FirebaseDatabase.getInstance().getReference().child("Review").child(cafe_name).child(menu).child(data).setValue(a);
                    Intent intent = new Intent(EditActivity.this, MyReview.class);
                    startActivity(intent);
                    finish();
                    return true;


                }
        }
        return super.onOptionsItemSelected(item);
    }
}
