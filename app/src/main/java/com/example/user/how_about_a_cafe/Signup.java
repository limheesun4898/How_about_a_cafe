package com.example.user.how_about_a_cafe;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Struct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mfirebaseAuth;
    DatabaseReference myRef;
    String Uname;
    String Uemail;

    private EditText loginsingup_name, loginsignup_email, loginsignup_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        loginsingup_name = findViewById(R.id.loginsignup_name);
        loginsignup_email = findViewById(R.id.loginsignup_email);
        loginsignup_password = findViewById(R.id.loginsignup_password);

        findViewById(R.id.Signup_loginsignupbutton).setOnClickListener(this);

        mfirebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

    }

    public void Singup(final String name, final String email, String password) {
        mfirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(Signup.this, "비밀번호가 간단해요..", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(Signup.this, "email 형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(Signup.this, "이미존재하는 email 입니다.", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(Signup.this, "다시 확인해주세요..", Toast.LENGTH_SHORT).show();
                            }
                        } else{
                            FirebaseUser user = mfirebaseAuth.getCurrentUser();
                            if (user != null) {
                                Uname = loginsingup_name.getText().toString();
                                Uemail = loginsignup_email.getText().toString();
                                Hashtable<String, String> profile = new Hashtable<String, String>();
                                profile.put("name", Uname);
                                profile.put("email", Uemail);
                                profile.put("photo", "https://firebasestorage.googleapis.com/v0/b/how-about-a-cafe.appspot.com/o/users%2Faccount.png?alt=media&token=49a1251d-650a-492d-8a22-bba9f4b7144e");
                                myRef.child(user.getUid()).setValue(profile);
                                System.out.println("UserUid : " + user.getUid());
                            }

                            Toast.makeText(Signup.this, "이메일 회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Signup.this, Login.class));
                            finish();
                        }

                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Signup_loginsignupbutton:
                if (loginsingup_name.getText().toString().isEmpty() || loginsignup_email.getText().toString().isEmpty() || loginsignup_password.getText().toString().isEmpty()){
                    Toast.makeText(this, "빈칸을 채워주세요 :)", Toast.LENGTH_SHORT).show();
                } else {
                    Singup(loginsingup_name.getText().toString(), loginsignup_email.getText().toString(), loginsignup_password.getText().toString());
                }
                break;
        }
    }

}
