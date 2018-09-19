package com.example.user.how_about_a_cafe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup extends AppCompatActivity {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;


    private FirebaseAuth mfirebaseAuth;
    private DatabaseReference mDatabase;

    private Uri mImageCaptureUri;

    private String absoultePath;
    private CircleImageView profileImage;
    private ImageButton btn_UploaPicture;
    final Context context = this;

    String Uname;
    String Uemail;
    String pw;
    String  UphotoUrl;
    private EditText loginsingup_name,loginsignup_email,loginsignup_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        loginsingup_name = findViewById(R.id.loginsignup_name);
        loginsignup_email = findViewById(R.id.loginsignup_email);
        loginsignup_password = findViewById(R.id.loginsignup_password);
        Button Signup_loginsignupbutton = findViewById(R.id.Signup_loginsignupbutton);
        profileImage = findViewById(R.id.profileImage);
        btn_UploaPicture = findViewById(R.id.btn_UploaPicture);

        mfirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


//        /*권한*/
//        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.N){
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
//        }

            btn_UploaPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakePhotoAction();
                    }
                };
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbumAction();
                    }
                };
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };
                new AlertDialog.Builder(context)
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("사진 촬영", cameraListener)
                        .setNegativeButton("앨범 선택", albumListener)
                        .setNeutralButton("취소", cancelListener)
                        .show();
            }
        });

        Signup_loginsignupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Singup(loginsingup_name.getText().toString(), loginsignup_email.getText().toString(),  loginsignup_password.getText().toString());

            }
        });

    }
        // 카메라 촬영 후 이미지 가져오기
    public void doTakePhotoAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //파일의 경로 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    //갤러리에서 이미지 가져오기
    public void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            return;
        }
        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                mImageCaptureUri = data.getData();
            }
            case PICK_FROM_CAMERA: {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");
                //CROP 이미지
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);

                startActivityForResult(intent, CROP_FROM_CAMERA);
                break;
            }
            case CROP_FROM_CAMERA: {
                if (resultCode != RESULT_OK) {
                    return;
                }
                final Bundle extras = data.getExtras();

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/SmartWheel/" + System.currentTimeMillis() + ".jpg";

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    //이미지에 Crop된 Bitmap을 보여줌
                    profileImage.setImageBitmap(photo);
                    UphotoUrl = new String(String.valueOf(photo));


                    storeCropImage(photo, filePath);
                    absoultePath = filePath;
                    break;
                }

                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) {
                    f.delete();
                }
            }
        }
    }

    //Bitmap을 저장
    private void storeCropImage(Bitmap bitmap, String filePath) {
        String dirpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel";
        File directory_SmartWheel = new File(dirpath);

        if (!directory_SmartWheel.exists())
            directory_SmartWheel.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(copyFile)));

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Singup(String name, String email, String password) {
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
                        } else {
                            //이메일 로그인 프로필 DB 저장
                            Uname = loginsingup_name.getText().toString();
                            Uemail = loginsignup_email.getText().toString();

                            User_Data userData = new User_Data(Uname, Uemail, UphotoUrl);
                            mDatabase.child("Email_login").child(Uname).setValue(userData);

                            Toast.makeText(context, "이메일 회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Signup.this, Login.class));
                            finish();
                        }
                    }
                });
    }


}
//http://abc1211.tistory.com/470
//http://jeongchul.tistory.com/287

