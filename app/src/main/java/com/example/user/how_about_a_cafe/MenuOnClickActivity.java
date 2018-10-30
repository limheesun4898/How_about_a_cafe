package com.example.user.how_about_a_cafe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuOnClickActivity extends AppCompatActivity {
    public static ArrayList<ReviewItem> mItems = new ArrayList<>();
    public static DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid;
    private RatingBar ratingBar;
    private TextView rating_ave;
    private String cafe_name;
    private List<String> data;
    private String menu;
    private RecyclerView.Adapter recycleradapter;
    private RecyclerView recyclerView;
    private TextView null_text;
    private int price;
    private int total;
    private int total_2;
    private int ice = 0;
    private boolean ice_cnt = false;
    private int size = 0;
    private int size_cnt = 1;
    private int iMenu_cnt = 1;
    private int iPerson_cnt = 1;
    private int total_3;
    private int SIZECNT = 0;
    private String imageurl;
    private String category;
    private float average = 0;
    private int review_cnt;
    private float sum = 0;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_on_click);

        Intent intent = getIntent();
        menu = intent.getStringExtra("menu_name");
        cafe_name = intent.getStringExtra("cafe_name");
        imageurl = intent.getStringExtra("imageurl");
        category = intent.getStringExtra("category");

        recyclerView = (RecyclerView) findViewById(R.id.list_review_recyclerview);
        null_text = (TextView) findViewById(R.id.list_review_null_text);
        ratingBar = (RatingBar) findViewById(R.id.rating_average);
        rating_ave = (TextView) findViewById(R.id.rating_average_text);
        back = (ImageButton) findViewById(R.id.backspace);

        if (user != null)
            uid = user.getUid();

        Toolbar mytoolbar = findViewById(R.id.menu_on_click_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recycleradapter = new ListReviewRecyclerAdapter(mItems, MenuOnClickActivity.this);
        recyclerView.setAdapter(recycleradapter);

        data = new ArrayList<>();
        mItems.clear();

        if (firebaseDatabase.child("Review").getKey() != null) {
            firebaseDatabase.child("Review").child(cafe_name).child(menu).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    ReviewItem item = dataSnapshot.getValue(ReviewItem.class);

                    if (item.getReview() != null) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            review_cnt += 1;
                            sum += Float.parseFloat(item.getRating());
                        }

                        average = sum / review_cnt;
                        String num = String.format("%.1f", average);
                        ratingBar.setRating(average);
                        rating_ave.setText(num);

                    }

                    if (item.isIsimage()) {
                        ReviewItem a = new ReviewItem(item.getReview(), item.getRating(), item.getUrl(), item.getFormatDate(), item.getName(), item.getProfile_image(), item.getMenu(), item.getCafe_name(), item.getSelection(), item.isIsimage());
                        mItems.add(a);
                        recycleradapter.notifyDataSetChanged();

                    } else {
                        ReviewItem a = new ReviewItem(item.getReview(), item.getRating(), item.getFormatDate(), item.getName(), item.getProfile_image(), item.getMenu(), item.getCafe_name(), item.getSelection(), item.isIsimage());
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

            calData();

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void calData() {
        final ImageView menu_image = (ImageView) findViewById(R.id.menu_on_click_image);
        final TextView menu_name = (TextView) findViewById(R.id.menu_on_click_name);
        final TextView menu_price = (TextView) findViewById(R.id.menu_on_click_price);
        final Button person_minus = (Button) findViewById(R.id.menu_on_click_person_minus);
        final Button person_plus = (Button) findViewById(R.id.menu_on_click_person_plus);
        final TextView person_cnt = (TextView) findViewById(R.id.menu_on_click_person_cnt);
        final Button menu_minus = (Button) findViewById(R.id.menu_on_click_menu_minus);
        final TextView menu_cnt = (TextView) findViewById(R.id.menu_on_click_menu_cnt);
        final Button menu_plus = (Button) findViewById(R.id.menu_on_click_menu_plus);
        final Button hot_btn = (Button) findViewById(R.id.menu_on_click_hot);
        final Button ice_btn = (Button) findViewById(R.id.menu_on_click_ice);
        final Button size1 = (Button) findViewById(R.id.menu_on_click_size1);
        final Button size2 = (Button) findViewById(R.id.menu_on_click_size2);
        final Button size3 = (Button) findViewById(R.id.menu_on_click_size3);
        final Button ok_btn = (Button) findViewById(R.id.menu_on_click_ok);

        Glide.with(MenuOnClickActivity.this).load(imageurl).into(menu_image);
        menu_name.setText(menu);
        hot_btn.setEnabled(false);
        size1.setEnabled(false);
        if (iMenu_cnt == 1)
            menu_minus.setEnabled(false);
        else
            menu_minus.setEnabled(true);
        if (iPerson_cnt == 1)
            person_minus.setEnabled(false);
        else
            person_minus.setEnabled(true);
        if (ice_cnt) {
            ice_btn.setBackgroundColor(getResources().getColor(R.color.colorGray));
            hot_btn.setBackgroundColor(getResources().getColor(R.color.colorHot));
        }
        else {
            hot_btn.setBackgroundColor(getResources().getColor(R.color.colorGray));
            ice_btn.setBackgroundColor(getResources().getColor(R.color.colorIce));
        }

        firebaseDatabase.child(cafe_name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (category.equals("0")) {
                    firebaseDatabase.child(cafe_name).child("사이드메뉴").child(menu).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null)
                                return;
                            else {
                                String s_price = dataSnapshot.getValue().toString();
                                menu_price.setText(s_price);
                                price = Integer.parseInt(s_price);
                                total = price;
                                total_2 = price;
                                total_3 = price;
                                hot_btn.setVisibility(View.GONE);
                                ice_btn.setVisibility(View.GONE);
                                size1.setVisibility(View.GONE);
                                size2.setVisibility(View.GONE);
                                size3.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                } else {
                    firebaseDatabase.child(cafe_name).child("음료").child(menu).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String s_price = dataSnapshot.getValue().toString();
                            menu_price.setText(s_price);
                            price = Integer.parseInt(s_price);
                            total = price;
                            total_2 = price;
                            total_3 = price;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                firebaseDatabase.child(cafe_name).child("음료").child("ICE").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            ice = 0;
                            ice_btn.setVisibility(View.GONE);
                            hot_btn.setVisibility(View.GONE);
                        } else
                            ice = Integer.parseInt(dataSnapshot.getValue().toString());

//                        if (ice == 0) {
//                            hot_btn.setBackgroundColor(Color.GRAY);
//                            ice_btn.setBackgroundColor(Color.GRAY);
//                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                firebaseDatabase.child(cafe_name).child("음료").child("SIZE").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null){
                            size = 0;
                            size1.setVisibility(View.GONE);
                            size2.setVisibility(View.GONE);
                            size3.setVisibility(View.GONE);

                        }
                        else
                            size = Integer.parseInt(dataSnapshot.getValue().toString());
//                        if (size == 0) {
//                            size1.setEnabled(false);
//                            size2.setEnabled(false);
//                            size3.setEnabled(false);
//                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                firebaseDatabase.child(cafe_name).child("음료").child("SIZE_CNT").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null)
                            SIZECNT = 0;
                        else
                            SIZECNT = Integer.parseInt(dataSnapshot.getValue().toString());

                        if (SIZECNT == 2) {
                            size3.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        hot_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                hot_btn.setEnabled(false);
                ice_btn.setEnabled(true);
                ice_cnt = false;
                menu_price.setText(String.valueOf(price));
                total = Integer.parseInt(menu_price.getText().toString());
                total_2 = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());
                if (ice_cnt) {
                    ice_btn.setBackgroundColor(getResources().getColor(R.color.colorGray));
                    hot_btn.setBackgroundColor(getResources().getColor(R.color.colorHot));
                }
                else {
                    hot_btn.setBackgroundColor(getResources().getColor(R.color.colorGray));
                    ice_btn.setBackgroundColor(getResources().getColor(R.color.colorIce));
                }
            }
        });

        ice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hot_btn.setEnabled(true);
                ice_btn.setEnabled(false);
                ice_cnt = true;
                menu_price.setText(String.valueOf(price + ice));
                total = Integer.parseInt(menu_price.getText().toString());
                total_2 = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());
                if (ice_cnt) {
                    ice_btn.setBackgroundColor(getResources().getColor(R.color.colorGray));
                    hot_btn.setBackgroundColor(getResources().getColor(R.color.colorHot));
                }
                else {
                    hot_btn.setBackgroundColor(getResources().getColor(R.color.colorGray));
                    ice_btn.setBackgroundColor(getResources().getColor(R.color.colorIce));
                }
            }
        });

        size1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size1.setEnabled(false);
                size2.setEnabled(true);
                size3.setEnabled(true);
                size_cnt = 1;

                if (iMenu_cnt > 1) {
                    iMenu_cnt = 1;
                    menu_cnt.setText(String.valueOf(iMenu_cnt));
                    menu_minus.setEnabled(false);
                }

                if (iPerson_cnt > 1) {
                    iPerson_cnt = 1;
                    person_cnt.setText(String.valueOf(iPerson_cnt) + " / N");
                    person_minus.setEnabled(false);
                }

                if (ice_cnt)
                    menu_price.setText(String.valueOf(price + ice));
                else
                    menu_price.setText(String.valueOf(price));

                total = Integer.parseInt(menu_price.getText().toString());
                total_2 = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());

            }
        });

        size2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size1.setEnabled(true);
                size2.setEnabled(false);
                size3.setEnabled(true);
                size_cnt = 2;
                if (iMenu_cnt > 1) {
                    iMenu_cnt = 1;
                    menu_cnt.setText(String.valueOf(iMenu_cnt));
                    menu_minus.setEnabled(false);
                }

                if (iPerson_cnt > 1) {
                    iPerson_cnt = 1;
                    person_cnt.setText(String.valueOf(iPerson_cnt) + " / N");
                    person_minus.setEnabled(false);
                }

                if (ice_cnt)
                    menu_price.setText(String.valueOf(price + ice + size));
                else
                    menu_price.setText(String.valueOf(price + size));

                total = Integer.parseInt(menu_price.getText().toString());
                total_2 = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());
            }
        });

        size3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size1.setEnabled(true);
                size2.setEnabled(true);
                size3.setEnabled(false);
                size_cnt = 3;
                if (iMenu_cnt > 1) {
                    iMenu_cnt = 1;
                    menu_cnt.setText(String.valueOf(iMenu_cnt));
                    menu_minus.setEnabled(false);
                }

                if (iPerson_cnt > 1) {
                    iPerson_cnt = 1;
                    person_cnt.setText(String.valueOf(iPerson_cnt) + " / N");
                    person_minus.setEnabled(false);
                }

                if (ice_cnt)
                    menu_price.setText(String.valueOf(price + ice + size * 2));
                else
                    menu_price.setText(String.valueOf(price + size * 2));

                total = Integer.parseInt(menu_price.getText().toString());
                total_2 = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());
            }
        });

        person_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iPerson_cnt -= 1;
                if (iPerson_cnt == 1)
                    person_minus.setEnabled(false);
                person_plus.setEnabled(true);
                person_cnt.setText(String.valueOf(iPerson_cnt) + " / N");
                menu_price.setText(String.valueOf(Math.round(total_2 / iPerson_cnt)));
                total = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());
            }
        });

        person_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iPerson_cnt > 10)
                    person_plus.setEnabled(false);
                person_minus.setEnabled(true);
                iPerson_cnt += 1;
                person_cnt.setText(String.valueOf(iPerson_cnt) + " / N");
                menu_price.setText(String.valueOf(Math.round(total_2 / iPerson_cnt)));
                total = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());
            }
        });

        menu_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iMenu_cnt -= 1;
                if (iMenu_cnt == 1)
                    menu_minus.setEnabled(false);
                menu_plus.setEnabled(true);
                menu_cnt.setText(String.valueOf(iMenu_cnt));
                menu_price.setText(String.valueOf(total * iMenu_cnt));
                total_2 = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());

            }
        });

        menu_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iMenu_cnt += 1;
                if (iMenu_cnt > 10)
                    menu_plus.setEnabled(false);
                menu_minus.setEnabled(true);
                menu_cnt.setText(String.valueOf(iMenu_cnt));
                menu_price.setText(String.valueOf(total * iMenu_cnt));
                total_2 = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());
            }
        });

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    if (category.equals("0")) {
                        Cal_Menu_Item item = new Cal_Menu_Item(menu, " ", " ", String.valueOf(total_3), String.valueOf(iMenu_cnt), "side");
                        FirebaseDatabase.getInstance().getReference().child("user_menu").child(uid).child(cafe_name).child(menu).setValue(item);
                        finish();
                    }
                    else {
                        Cal_Menu_Item item = new Cal_Menu_Item(menu, ice_cnt, String.valueOf(size_cnt), String.valueOf(total_3), String.valueOf(iMenu_cnt), "drink");
                        FirebaseDatabase.getInstance().getReference().child("user_menu").child(uid).child(cafe_name).child(menu).setValue(item);
                        finish();
                    }

                } else {
                    Toast.makeText(MenuOnClickActivity.this, "로그인 후 이용해주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });
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
                if (user != null) {
                    Intent intent = new Intent(MenuOnClickActivity.this, WriteReview.class);
                    intent.putExtra("cafe_name", cafe_name);
                    intent.putExtra("menu", menu);
                    startActivity(intent);
                } else
                    Toast.makeText(this, "로그인 후 이용해주세요", Toast.LENGTH_SHORT).show();


        }
        return super.onOptionsItemSelected(item);
    }
}
