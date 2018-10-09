package com.example.user.how_about_a_cafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoView;

public class Onclick_ImageActivity extends FragmentActivity {
    private ImageButton close;
    private PhotoView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onclick_image);

        close = (ImageButton) findViewById(R.id.onclick_image_close_button);
        image = (PhotoView) findViewById(R.id.onclick_image_photoView);

        Intent intent = getIntent();
        String data = intent.getStringExtra("date");

        Glide.with(this).load(data).into(image);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
