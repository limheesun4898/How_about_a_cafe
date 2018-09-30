package com.example.user.how_about_a_cafe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.recyclerviewmargin.LinearLayoutMargin;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private FirebaseAuth mFirebaseAuth;
    FirebaseUser user;
    private TextView Uname;
    private TextView Uemail;
    private CircleImageView Uiamge;
    Bitmap bitmap;
    Context mContext;
    private List<ListItem> itemList = new ArrayList<>();
    private MyRecyclerViewAdapter adapter;
    DatabaseReference myRef;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("카페 어때?");

        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        mContext = getApplicationContext();

        RecyclerView recyclerView = findViewById(R.id.main_recyclerview);
        recyclerView.setHasFixedSize(true);
        adapter = new MyRecyclerViewAdapter(itemList, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        LinearLayoutMargin layoutMargin = new LinearLayoutMargin(20);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(layoutMargin);
        recyclerView.setAdapter(adapter);
        addItem();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View v = navigationView.getHeaderView(0);
        Uname = v.findViewById(R.id.Username);
        Uemail = v.findViewById(R.id.Useremail);
        Uiamge = v.findViewById(R.id.UserImage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }

    private void addItem() {
        itemList.add(new ListItem(R.drawable.starbucks, "스타벅스", "http://www.istarbucks.co.kr/whats_new/campaign_list.do"));
        itemList.add(new ListItem(R.drawable.ediya, "이디야 커피", "https://ediya.com/contents/event.html?tb_name=event"));
        itemList.add(new ListItem(R.drawable.yogerpresso, "요거프레소", "https://www.yogerpresso.co.kr/event/new_plan"));
        itemList.add(new ListItem(R.drawable.angelinus, "엔젤리너스", "http://www.angelinus.com/Event/Event_List.asp"));
        itemList.add(new ListItem(R.drawable.twosome, "투썸플레이스", "https://www.twosome.co.kr:7009/event/list.asp"));
        itemList.add(new ListItem(R.drawable.cafebene, "카페베네", "http://www.caffebene.co.kr/Content/Gnb/Community/Event.aspx?code=T3M1I1"));
        itemList.add(new ListItem(R.drawable.hollys, "할리스 커피", "http://www.hollys.co.kr/news/notice/list.do"));
        itemList.add(new ListItem(R.drawable.tomntoms, "탐엔탐스", "https://www.tomntoms.com/event/main.php"));
        itemList.add(new ListItem(R.drawable.gongcha, "공차", "http://www.gong-cha.co.kr/brand/board/event.php"));
        itemList.add(new ListItem(R.drawable.bback, "빽다방", "http://paikdabang.com/news/?cate=event"));
        itemList.add(new ListItem(R.drawable.cafe_gate, "카페 게이트", "http://cafegate.co.kr"));
        itemList.add(new ListItem(R.drawable.coffeebean, "커피빈", "http://www.coffeebeankorea.com/main/main.asp"));
        adapter.notifyDataSetChanged();
    }

//    private void UserAccount() {
//        Thread mThread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    URL url = null;
//                    url = new URL(mFirebaseAuth.getCurrentUser().getPhotoUrl().toString());
//                    if (url == null) {
//                        return;
//                    }
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setDoInput(true);
//                    conn.connect();
//
//                    InputStream is = conn.getInputStream();
//                    bitmap = BitmapFactory.decodeStream(is);
//
//                } catch (MalformedURLException ee) {
//                    ee.printStackTrace();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        mThread.start();
//
//        try {
//            mThread.join();
//            Uiamge.setImageBitmap(bitmap);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Uname.setText(mFirebaseAuth.getCurrentUser().getDisplayName());
//        Uemail.setText(mFirebaseAuth.getCurrentUser().getEmail());
//
//    }

    //현재 사용자 확인
    @Override
    public void onStart() {
        super.onStart();
        if (user == null) {

        } else {
            //UserAccount();
//            Uiamge.setImageURI(user.getPhotoUrl());
//            Uname.setText(user.getDisplayName());
//            Uemail.setText(user.getEmail());
            Hashtable<String, String> profile = new Hashtable<String, String>();
            profile.put("email", user.getEmail());
            profile.put("photo", "");
            myRef.child(user.getUid()).setValue(profile);

            myRef.child("users").child(String.valueOf(Uname)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue().toString();
                    String stphoto = dataSnapshot.child("photo").getValue().toString();
                    if (TextUtils.isEmpty(stphoto)){
                        Uiamge.setImageResource(R.drawable.account);
                    }else{


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
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorit_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.favorite) {
            startActivity(new Intent(getApplicationContext(), FavoriteList.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuitem) {
        // Handle navigation view item clicks here.
        int id = menuitem.getItemId();
        if (id == R.id.nav_account) {
            startActivity(new Intent(this, M_Account.class));
        } else if (id == R.id.nav_game) {
            Toast.makeText(this, "사다리게임", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_location) {
            startActivity(new Intent(this, Google_map.class));
        } else if (id == R.id.nav_review) {
            Toast.makeText(this, "리뷰 보기", Toast.LENGTH_SHORT).show();
            mFirebaseAuth.signOut();
            startActivity(new Intent(this, Login.class));
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
