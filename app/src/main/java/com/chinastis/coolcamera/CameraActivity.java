package com.chinastis.coolcamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

public class CameraActivity extends AppCompatActivity implements CameraListener {

    MyCameraView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camrea);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView_camera);

        cameraView = new MyCameraView(this,surfaceView);

        cameraView.initCamera();

    }

    public void capture(View view) {
        cameraView.capture();

    }

    @Override
    public void pictureTaken(byte[] data) {

    }

    @Override
    public void autoFocusSuccess() {

    }
}
