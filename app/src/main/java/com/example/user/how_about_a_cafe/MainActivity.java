package com.example.user.how_about_a_cafe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthLiestener;

    private TextView Uname;
    private TextView Uemail;
    private CircleImageView Uiamge;
    Bitmap bitmap;

    private GridView grid;
    String[] web = {
            "스타벅스", "이디야 커피", "요거프레소", "엔제리너스",
            "투썸플레이스", "카페베네", "할리스",
            "탐엔탐스", "공차", "빽다방", "카페게이트", "커피빈"
    };
    int[] imageId = {
            R.drawable.starbucks, R.drawable.ediya, R.drawable.yogerpresso,
            R.drawable.angelinus, R.drawable.twosome,
            R.drawable.cafebene, R.drawable.hollys,
            R.drawable.tomntoms, R.drawable.gongcha, R.drawable.bback, R.drawable.cafe_gate, R.drawable.coffeebean
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("카페 어때?");

        GridAdapter adapter = new GridAdapter(MainActivity.this, web, imageId);
        grid = findViewById(R.id.gridview);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, MenuList.class);
                intent.putExtra("cafe_name", web[+position]);
                startActivity(intent);

            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mAuthLiestener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
//                    UserAccount();
                } else {

                }
            }
        };

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        Uname = view.findViewById(R.id.Username);
        Uemail = view.findViewById(R.id.Useremail);
        Uiamge = view.findViewById(R.id.UserImage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }

    private void UserAccount() {
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = null;
                    url = new URL(mFirebaseAuth.getCurrentUser().getPhotoUrl().toString());
                    if (url == null) {
                        return;
                    }
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException ee) {
                    ee.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();

        try {
            mThread.join();
            Uiamge.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Uname.setText(mFirebaseAuth.getCurrentUser().getDisplayName());
        Uemail.setText(mFirebaseAuth.getCurrentUser().getEmail());

    }

    //현재 사용자 확인
    @Override
    public void onStart() {
        super.onStart();
        if (mFirebaseUser == null) {
//            startActivity(new Intent(MainActivity.this, Login.class));
//            finish();
        } else {
//            UserAccount();
            Uname.setText(mFirebaseAuth.getCurrentUser().getDisplayName());
            Uemail.setText(mFirebaseAuth.getCurrentUser().getEmail());
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuitem) {
        // Handle navigation view item clicks here.
        int id = menuitem.getItemId();
        if (id == R.id.nav_account) {
            startActivity(new Intent(this, m_account.class));
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
