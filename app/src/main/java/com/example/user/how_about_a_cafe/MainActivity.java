package com.example.user.how_about_a_cafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mFirebaseAuth.getCurrentUser();
    private TextView Uname;
    private CircleImageView Uiamge;
    private Context mContext;
    private List<ListItem> itemList = new ArrayList<>();
    private MyRecyclerViewAdapter adapter;
    private DatabaseReference myRef;
    final Context context = this;
    private String stUid;

    public static Activity _Main_Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("카페 어때");

        SharedPreferences preference = getSharedPreferences("a", MODE_PRIVATE);
        int firstviewshow = preference.getInt("First", 0);

        if (firstviewshow != 1) {
            Intent intent = new Intent(MainActivity.this, com.example.user.how_about_a_cafe.ViewPager.class);
            startActivity(intent);
        }

        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            stUid = sharedPreferences.getString("Uid", "");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            myRef = database.getReference("users");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        mContext = getApplicationContext();

        RecyclerView recyclerView = findViewById(R.id.main_recyclerview);
        recyclerView.setHasFixedSize(true);
        adapter = new MyRecyclerViewAdapter(itemList, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        addItem();



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View v = navigationView.getHeaderView(0);
        Uname = v.findViewById(R.id.Username);
        Uiamge = v.findViewById(R.id.UserImage);
    }

    private void addItem() {
        itemList.add(new ListItem(R.drawable.bback, "빽다방", "http://paikdabang.com/news/?cate=event"));
        itemList.add(new ListItem(R.drawable.yogerpresso, "요거프레소", "https://www.yogerpresso.co.kr/event/new_plan"));
        itemList.add(new ListItem(R.drawable.twosome, "투썸플레이스", "https://www.twosome.co.kr:7009/event/list.asp"));
        itemList.add(new ListItem(R.drawable.tomntoms, "탐앤탐스", "https://www.tomntoms.com/event/main.php"));
        itemList.add(new ListItem(R.drawable.starbucks, "스타벅스", "http://www.istarbucks.co.kr/whats_new/campaign_list.do"));
        itemList.add(new ListItem(R.drawable.ediya, "이디야 커피", "https://ediya.com/contents/event.html?tb_name=event"));
        itemList.add(new ListItem(R.drawable.angelinus, "엔제리너스", "http://www.angelinus.com/Event/Event_List.asp"));
        itemList.add(new ListItem(R.drawable.cafebene, "카페베네", "http://www.caffebene.co.kr/Content/Gnb/Community/Event.aspx?code=T3M1I1"));
        itemList.add(new ListItem(R.drawable.hollys, "할리스", "http://www.hollys.co.kr/news/notice/list.do"));
        itemList.add(new ListItem(R.drawable.gongcha, "공차", "http://www.gong-cha.co.kr/brand/board/event.php"));
        itemList.add(new ListItem(R.drawable.cafe_gate, "카페게이트", "http://cafegate.co.kr"));
        itemList.add(new ListItem(R.drawable.coffeebean, "커피빈", "http://www.coffeebeankorea.com/main/main.asp"));
        adapter.notifyDataSetChanged();
    }

    //현재 사용자 확인
    @Override
    public void onStart() {
        super.onStart();
        if (user == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        } else {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();

            myRef.child("users").child(stUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        String stPhoto = dataSnapshot.child("photo").getValue().toString();
                        String stname = dataSnapshot.child("name").getValue().toString();
                        System.out.println("LOGIN : " + stname);

                        Uname.setText(stname);
                        Glide.with(getApplicationContext()).load(stPhoto).into(Uiamge);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("종료")
                    .setMessage("앱을 종료하시겠습니까?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("취소", null).show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuitem) {
        // Handle navigation view item clicks here.
        int id = menuitem.getItemId();
        if (id == R.id.nav_account) {
            startActivity(new Intent(this, M_Account.class));
        } else if (id == R.id.nav_game) {
            Toast.makeText(this, "사다리 게임 아직 준비 중입니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=%EC%82%AC%EB%8B%A4%EB%A6%AC%EA%B2%8C%EC%9E%84"));
            startActivity(intent);
        } else if (id == R.id.nav_favortie) {
            startActivity(new Intent(this, Favorite.class));
        } else if (id == R.id.nav_location) {
            startActivity(new Intent(this, Google_map.class));
        } else if (id == R.id.nav_review) {
            if (user == null) {
                Toast.makeText(mContext, "로그인 후 이용해주세요", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, MyReview.class);
                startActivity(intent);
            }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}