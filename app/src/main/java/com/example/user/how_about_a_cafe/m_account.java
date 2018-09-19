package com.example.user.how_about_a_cafe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.AppComponentFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class m_account extends AppCompatActivity {
    private FirebaseAuth mAuth;

    Bitmap bitmap;

    private TextView Username;
    private TextView Useremail;
    private CircleImageView UserImage;

    public m_account() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_account);

            mAuth = FirebaseAuth.getInstance();
            final FirebaseUser user = mAuth.getCurrentUser();


            Username = findViewById(R.id.Username);
            Useremail = findViewById(R.id.Useremail);
            UserImage = findViewById(R.id.UserImage);


            Thread mThread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(user.getPhotoUrl().toString());
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
                UserImage.setImageBitmap(bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Username.setText(mAuth.getCurrentUser().getDisplayName());
            Useremail.setText(mAuth.getCurrentUser().getEmail());
    }

}
//http://dailyddubby.blogspot.com/2018/04/104-firebase-google-login.html
