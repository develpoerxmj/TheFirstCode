package com.example.camerademo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_photo).setOnClickListener(this);
        findViewById(R.id.btn_camera).setOnClickListener(this);
        view = (ImageView) findViewById(R.id.iv_photo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_photo:
                takePhoto();
                break;
            case R.id.btn_camera:
                selectCamera();
                break;
        }
    }

    private void selectCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openCamera();
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 2){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }
        }
    }

    private void openCamera() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 10);
    }

    private Uri imageUri;

    private void takePhoto() {
        File image = new File(getExternalCacheDir(), System.currentTimeMillis()+".jpg");
        try {
            if (image.exists()){
                image.delete();
            }
            image.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(this, "com.example.camerademo.fileprovider", image);
        }else {
            imageUri = Uri.fromFile(image);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
//                try {
//                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                    view.setImageBitmap(bitmap);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                Glide.with(this).load(imageUri).into(view);
            }
        }else if (requestCode == 10 && resultCode == RESULT_OK){
            Log.i("------------------", data.getData().getAuthority()+"--------"+data.getData().getScheme());
            if (Build.VERSION.SDK_INT >= 19){
                //KitKat---Android4.4系统的代号
                //Android4.4开始，选取相册中的图片返回的不是真实的Uri，经过封装，需要解析
                handleImageOnKitKat(data);
            }else {
                //Android4.4以下返回的是真实的Uri
                handleImageBeforeKitKat(data);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)){
            //如果是document类型的Uri，则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的uri，直接获取路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        Log.i("------------", imagePath);
//        if (imagePath != null){
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            view.setImageBitmap(bitmap);
//        }else {
//            Toast.makeText(this, "file to get image", Toast.LENGTH_SHORT).show();
//        }
        Glide.with(this).load(imagePath).into(view);
    }

    private String getImagePath(Uri externalContentUri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(externalContentUri, null, selection, null, null);
        if (cursor != null){
            if (cursor.moveToNext()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }
}
