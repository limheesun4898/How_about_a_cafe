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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup extends AppCompatActivity implements View.OnClickListener{

    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;

    String mCurrentPhotoPath;
    Uri imageUri, photoURI, albumURI;

    private FirebaseAuth mfirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private CircleImageView profileImage;
    final Context context = this;

    String Uname;
    String Uemail;
    String image;

    private EditText loginsingup_name,loginsignup_email,loginsignup_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        loginsingup_name = findViewById(R.id.loginsignup_name);
        loginsignup_email = findViewById(R.id.loginsignup_email);
        loginsignup_password = findViewById(R.id.loginsignup_password);
        findViewById(R.id.Signup_loginsignupbutton).setOnClickListener(this);
        profileImage = findViewById(R.id.profileImage);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new RoundedCorners(15));

        mfirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    public void Singup( final String email, String password) {
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
                            if (profileImage == null){
                                profileImage.setImageResource(R.drawable.account);

                            }else{

                            }
                            //이메일 로그인 프로필 DB 저장
                            Uname = loginsingup_name.getText().toString();
                            Uemail = loginsignup_email.getText().toString();

                            User_Data email_user_data = new User_Data(Uname, Uemail);
                            mDatabase.child("Email_login").child(Uname).setValue(email_user_data);

                            Toast.makeText(context, "이메일 회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Signup.this, Login.class));
                            finish();
                        }
                    }
                });
    }
    public void iv_view(View v){
        checkPermission();
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                captureCamera();
            }
        };
        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getAlbum();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Signup_loginsignupbutton:
                Singup( loginsignup_email.getText().toString(),  loginsignup_password.getText().toString());
                break;
        }
    }

    private void captureCamera() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = creatImageFile();
                } catch (IOException ex) {
                }
                if (photoFile != null) {
                    Uri providerURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName(), photoFile);
                    imageUri = providerURI;

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } else {
            Toast.makeText(this, "저장공간이 접근 불가능한 기기입니다.", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public File creatImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "gyeom");

        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }

        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }
    private void getAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
    }

    private void galleryAddpic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    public void cropImage() {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setDataAndType(photoURI, "image/*");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("output", albumURI);
        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(15));
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        cropSingleImage(imageUri);
                        Glide.with(this).load(imageUri).apply(requestOptions).into(profileImage);

                    } catch (Exception e) {
                        Toast.makeText(this, "오류발생", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "사진찍기를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_TAKE_ALBUM:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getData() != null) {
                        try {
                            File albumFile = null;
                            albumFile = creatImageFile();
                            photoURI = data.getData();
                            albumURI = Uri.fromFile(albumFile);
                            cropImage();
                        } catch (IOException e) {

                        }
                    }
                }
                break;

            case REQUEST_IMAGE_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    galleryAddpic();
                    Glide.with(this).load(albumURI).apply(requestOptions).into(profileImage);
                }
                break;
        }
    }

    public String getPaht(Uri uri){
        String[] pro = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,pro,null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(index);
    }

    public void cropSingleImage(Uri photoUriPath) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        cropIntent.setDataAndType(photoUriPath, "image/*");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("output", photoUriPath);

        List list = getPackageManager().queryIntentActivities(cropIntent, 0);

        Intent i = new Intent(cropIntent);
        ResolveInfo res = (ResolveInfo) list.get(0);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        grantUriPermission(res.activityInfo.packageName, photoUriPath,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

        startActivityForResult(i, REQUEST_IMAGE_CROP);

    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 처음 호출시엔 if 안의 부분은 false로 리턴, else 의 요청으로 넘어감
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA))) {
                new android.support.v7.app.AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CAMERA:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] < 0) {
                        Toast.makeText(this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                break;
        }
    }



}
//http://abc1211.tistory.com/470
//http://jeongchul.tistory.com/287
//http://g-y-e-o-m.tistory.com/m/48  카메라 갤러리