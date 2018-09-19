package com.example.user.how_about_a_cafe;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends Activity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GridView grid;
    String[] web = {
            "스타벅스", "이디야 커피", "설빙", "요거프레소", "베스킨라빈스", "엔젤리너스",
            "투썸플레이스", "카페베네", "할리스 커피", "나뚜루 팝", "네스카페",
            "탐엔탐스", "공차", "빽다방", "던킨 도너츠"

    };

    int[] imageId = {
            R.drawable.starbucks, R.drawable.ediya, R.drawable.sulbing, R.drawable.yogerpresso,
            R.drawable.baskinrobbins, R.drawable.angelinus, R.drawable.twosome,
            R.drawable.cafebene, R.drawable.hollys, R.drawable.natuurpop, R.drawable.nescafe,
            R.drawable.tomntoms, R.drawable.gongcha, R.drawable.bback, R.drawable.dunkin
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();

        GridAdapter adapter = new GridAdapter(MainActivity.this, web, imageId);
        grid = (GridView) findViewById(R.id.gridview);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "You Clicked at " + web[+position], Toast.LENGTH_SHORT).show();

            }
        });

    }


    //현재 사용자 확인
    @Override
    public void onStart() {
        super.onStart();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }
    }

}
