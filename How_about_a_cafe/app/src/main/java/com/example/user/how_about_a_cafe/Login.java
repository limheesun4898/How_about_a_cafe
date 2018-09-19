package com.example.user.how_about_a_cafe;

import android.content.Intent;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener, onAuthStateChanged {
    private FirebaseAuth mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 1000;
    private CallbackManager mCallbackManager;
    private FirebaseAuth.AuthStateListener mAuthLiestener;

    EditText Login_email,Login_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.GoogleLoginbtn).setOnClickListener(this);
        findViewById(R.id.Signupbtn_Login).setOnClickListener(this);
        findViewById(R.id.LoginBtn_Login).setOnClickListener(this);

        Login_email = findViewById(R.id.Login_email);
        Login_password = findViewById(R.id.Login_password);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthLiestener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){

                }
                else{

                }
            }
        };

        GoogleSignInOptions gso = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
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
        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                //구글 로그인 성공해서 파베에 인증
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(Login.this, "인증 실패", Toast.LENGTH_SHORT).show();
                        }else{
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
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                        } else {
                            Toast.makeText(Login.this, "facebook 로그인 성공", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        }

                    }
                });
    }
    //이메일 로그인
    @Override
    public void clickSignIn(EditText Login_email, EditText Login_password) {
        mFirebaseAuth.signInWithEmailAndPassword(this.Login_email.getText().toString(), this.Login_password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Login.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "로그인 완료", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        }
                        // ...
                    }
                });
    }

    //계정 데이터 가져옴
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        user.getEmail();
        user.getUid();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthLiestener);
    }
    @Override
    public void onStop(){
        super.onStop();
        if(mAuthLiestener !=null)
            mFirebaseAuth.removeAuthStateListener(mAuthLiestener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.GoogleLoginbtn:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent,RC_SIGN_IN);
                break;
            case R.id.Signupbtn_Login:
                Intent intent = new Intent(this, Signup.class);
                startActivity(intent);
                break;
            case R.id.LoginBtn_Login:
                clickSignIn(Login_email,Login_password);
                break;
        }
    }
}
