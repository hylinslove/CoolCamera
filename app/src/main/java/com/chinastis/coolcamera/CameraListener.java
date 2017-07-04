package com.chinastis.coolcamera;

/**
 * Created by MENG on 2017/6/29.
 */

public interface CameraListener {

    void pictureTaken(byte[] data);
    void autoFocusSuccess();

}
