package com.chinastis.coolcamera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;

/**
 * Created by MENG on 2017/6/28.
 */

public class MyCameraView extends SurfaceView implements SurfaceHolder.Callback ,View.OnTouchListener{

    enum FlashMode{
        ON ,OFF,AUTO
    }

    private SurfaceHolder mHolder;

    private android.hardware.Camera mCamera;

    private CameraListener listener;

    private float startPoint;

    private boolean isZooming = false;

    public MyCameraView(Context context) {
        super(context);

        mHolder = this.getHolder();
        setOnTouchListener(this);
        mHolder.addCallback(this);
        mHolder.setKeepScreenOn(true);

    }

    public MyCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = this.getHolder();
        setOnTouchListener(this);
        mHolder.addCallback(this);
        mHolder.setKeepScreenOn(true);

    }

    public MyCameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void initCamera() {
        mCamera = Camera.open();
    }

    public void setListener(CameraListener listener) {
        this.listener = listener;
    }


    /**
     *SurfaceHolder回调
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if(mCamera != null) {
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.setDisplayOrientation(90);
                mCamera.startPreview();
            } catch (IOException e) {
                if(mCamera != null) {
                    mCamera.release();
                    mCamera = null;
                }
                e.printStackTrace();
            }
        }
    }

    /**
     * 拍照
     */
    public void capture() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                mCamera.stopPreview();
                if(listener != null) {
                    listener.pictureTaken(data);
                }
            }
        });
    }

    /**
     * 开始预览
     */
    public void startPreview(){
        if(mCamera!=null) {
            mCamera.startPreview();
        }
    }

    /**
     * 实现相机自动聚焦
     */
    private void focus() {
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(listener != null) {
                    listener.autoFocusSuccess();
                }
                mCamera.cancelAutoFocus();
            }
        });
    }

    /**
     * 相机预览界面缩放
     */
    private void setZoom(int scale) {
        Camera.Parameters parameters = mCamera.getParameters();

        if(parameters.isZoomSupported()){
            parameters.setZoom(scale);
            mCamera.setParameters(parameters);
        } else {
            Log.e("MENG","not support");
        }
    }

    /**
     * 设置相机闪光灯模式
     * @param mode 闪光灯模式
     */
    public void setFlashMode(FlashMode mode){
        Camera.Parameters parameters = mCamera.getParameters();

        switch (mode) {
            case ON:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                break;
            case OFF:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                break;
            case AUTO:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                break;
        }
        mCamera.setParameters(parameters);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    /**
     * 屏幕触摸回调函数
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if(event.getPointerCount() > 1 ){
                    startPoint = distance(event);
                }

                break;

            case MotionEvent.ACTION_MOVE:

                if(event.getPointerCount() > 1 ){
                    float endPoint = distance(event);

                    int scale = (int)((endPoint - startPoint)/10);
                    int zoom = scale+mCamera.getParameters().getZoom();
                    if (zoom<mCamera.getParameters().getMaxZoom() && zoom>0) {
                        if(isZooming) {
                            setZoom(zoom);
                        }
                    }

                    startPoint = endPoint;
                    isZooming =true;

                    return true;
                }

                break;

            case MotionEvent.ACTION_UP:
                isZooming = false;
                if(event.getPointerCount()<2) {
                    focus();
                }
                break;
        }

        return true;
    }

    /**
     * 计算两个手指间的距离
     */
    private float distance(MotionEvent event) {

        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);

        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
