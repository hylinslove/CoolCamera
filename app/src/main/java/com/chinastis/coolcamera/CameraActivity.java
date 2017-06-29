package com.chinastis.coolcamera;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

public class CameraActivity extends AppCompatActivity  {

    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camrea);

        mCamera = Camera.open();

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView_camera);

        MyCameraView cameraView = new MyCameraView(this,surfaceView);

        cameraView.initCamera(mCamera);

    }

    public void capture(View view) {

        mCamera.takePicture(
                new Camera.ShutterCallback() {
                    @Override
                    public void onShutter() {
                        Log.e("MENG","shutter");
                    }
                },
                new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        Log.e("MENG","raw");
                    }
                },
                new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        Log.e("MENG","jpeg");
                        mCamera.stopPreview();
                    }
                });

    }
}
