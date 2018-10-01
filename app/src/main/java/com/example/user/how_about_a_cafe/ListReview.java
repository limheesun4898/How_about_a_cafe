package com.example.user.how_about_a_cafe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ListReview extends AppCompatActivity {
    private String cafe_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_review);

        Intent intent = getIntent();
        cafe_name = intent.getStringExtra("cafe_name");

        Toolbar mytoolbar = findViewById(R.id.list_review_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
