package com.chinastis.coolcamera;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by MENG on 2017/6/28.
 */

public class MyCameraView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private android.hardware.Camera mCamera;

    public MyCameraView(Context context,SurfaceView surfaceView ) {

        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setKeepScreenOn(true);

    }

    /**
     *SurfaceHolder回调
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Log.e("MENG","surface create");

        if(mCamera != null) {
            try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                if(mCamera != null) {
                    mCamera.release();
                    mCamera = null;
                }
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
    }


    public void initCamera(android.hardware.Camera camera) {
        mCamera = camera;
        mCamera.setDisplayOrientation(90);
        if(camera!= null) {
            Log.e("MENG","camera != null");
            mCamera.startPreview();
        }
    }
}
