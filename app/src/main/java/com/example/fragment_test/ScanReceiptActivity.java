package com.example.fragment_test;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ScanReceiptActivity extends AppCompatActivity {

    ProcessCameraProvider cameraProvider;
    PreviewView mCameraView;
    ImageCapture imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_receipt);
        Button captureBnt = findViewById(R.id.captureBnt);

        mCameraView = findViewById(R.id.camara);
        ProcessCameraProvider.getInstance(this).addListener(new Runnable() {
            @Override
            public void run() {
                // 获取具体的相机过程提供者
                try {
                    cameraProvider = ProcessCameraProvider.getInstance(ScanReceiptActivity.this).get();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // 创建相机预览窗口
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(mCameraView.getSurfaceProvider());

                // 获取用于拍照的实例
                imageCapture = new ImageCapture.Builder().build();

                // 指定用于预览的相机，默认为后置相机，如果需要前置相机预览请使用CameraSelector.DEFAULT_FRONT_CAMERA
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                try {
                    // 绑定提供者之前先进行解绑，不能重复绑定
                    cameraProvider.unbindAll();

                    // 绑定提供者,将相机的生命周期进行绑定，因为camerax具有生命周期感知力，所以消除打开和关闭相机的任务
                    cameraProvider.bindToLifecycle(ScanReceiptActivity.this, cameraSelector, preview, imageCapture);
                } catch (Exception e) {
                    Log.e(TAG, "相机绑定异常 " + e.getMessage());
                }
            }
        }, ContextCompat.getMainExecutor(this));

        captureBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
    }

    private void takePhoto() {
        // 檢查是否有可用的相機拍攝器
        ImageCapture imageCapture = this.imageCapture;
        if (imageCapture == null) {
            return;
        }

        // 定義拍攝照片的名稱
        String name = new SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
                .format(System.currentTimeMillis());

        // 使用MediaStore操作照片文件
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image");
        }

        // 指定輸出參數
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
                .build();

        // 開始拍攝照片
        imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onError(@NonNull ImageCaptureException exc) {
                        // 拍攝失敗
                        String msg = "Photo capture failed: " + exc.getMessage();
                        Log.e(TAG, msg, exc);
                        Toast.makeText(ScanReceiptActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults output) {
                        // 拍攝成功，saveUri是圖片的uri地址
                        String msg = "Photo capture succeeded: " + output.getSavedUri();
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("picture_uri", output.getSavedUri().toString());
                        intent.putExtra("result", bundle);
                        setResult(Activity.RESULT_OK, intent);

                        //關閉此activity
                        finish();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, msg);
                    }
                });
    }
}