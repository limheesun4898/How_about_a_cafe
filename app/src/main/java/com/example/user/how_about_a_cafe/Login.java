package com.example.user.how_about_a_cafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.service.autofill.UserData;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Hashtable;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, onAuthStateChanged {
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 1000;
    private CallbackManager mCallbackManager;
    private FirebaseAuth.AuthStateListener mAuthLiestener;
    FirebaseUser user;

    EditText Login_email, Login_password;
    DatabaseReference myRef;
    String name, email, photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.GoogleLoginbtn).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        mAuthLiestener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Uid", user.getUid());
                    editor.apply();
                    System.out.println("loginuid : " + user.getUid());
                } else {
                    // User is signed out
                }
                // ...
            }
        };
        GoogleSignInOptions gso = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //facebook로그인
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton facebook_login = findViewById(R.id.facekook_login);
        facebook_login.setReadPermissions("email", "public_profile");
        facebook_login.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    //구글로그인 {
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                //구글 로그인 성공해서 파베에 인증
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(Login.this, "인증 실패", Toast.LENGTH_SHORT).show();
                        } else {

                            //구글 로그인 프로필 DB 저장
                            FirebaseUser user = mAuth.getCurrentUser();
                            name = user.getDisplayName();
                            email = user.getEmail();
                            photoUrl = "https://firebasestorage.googleapis.com/v0/b/how-about-a-cafe.appspot.com/o/users%2Faccount.png?alt=media&token=187d46ea-019f-487f-97b6-3a2305272630";
                            //DB에 데이터 저장
                            Hashtable<String, String> profile = new Hashtable<String, String>();
                            profile.put("name", name);
                            profile.put("email", email);
                            profile.put("photo", photoUrl);
                            myRef.child(user.getUid()).setValue(profile);

                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    // }
    //facebook
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                        } else {
                            // 프로필 DB 저장
                            FirebaseUser user = mAuth.getCurrentUser();
                            name = user.getDisplayName();
                            photoUrl = "https://firebasestorage.googleapis.com/v0/b/how-about-a-cafe.appspot.com/o/users%2Faccount.png?alt=media&token=187d46ea-019f-487f-97b6-3a2305272630";
                            //DB에 데이터 저장
                            Hashtable<String, String> profile = new Hashtable<String, String>();
                            profile.put("name", name);
                            profile.put("email", "페북 로그인 하였습니다.");
                            profile.put("photo", photoUrl);
                            myRef.child(user.getUid()).setValue(profile);

                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        }

                    }
                });
    }


    @Override
    public void clickSignIn(EditText Login_email, EditText Login_password) {

    }

    @Override
    public void clickSignIn(String email, String Login_password) {

    }

    //계정 데이터 가져옴
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        user.getEmail();
        user.getUid();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthLiestener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthLiestener != null)
            mAuth.removeAuthStateListener(mAuthLiestener);
        else {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.GoogleLoginbtn:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
