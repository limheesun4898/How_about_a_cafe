package com.example.user.how_about_a_cafe;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

interface onAuthStateChanged {
    //이메일 로그인
    void clickSignIn(EditText Login_email, EditText Login_password);

    //이메일 로그인
    void clickSignIn(String email, String Login_password);

    void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth);

}