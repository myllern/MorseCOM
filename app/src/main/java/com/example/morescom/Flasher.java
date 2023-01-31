package com.example.morescom;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class  Flasher{
    private CameraManager CM;
    private String cID;

    private int dotDealy;
    private int dashDelay = dotDealy * 3;
    private int wordEndDelay = dotDealy * 6;

    public Flasher(CameraManager CM, String cID, int unitTime){

        this.dotDealy = unitTime;
        this.CM = CM;
        this.cID = cID;
    }
    public void transmit(List<String> BlinkArray) throws InterruptedException {

        for (String word : BlinkArray
        ) {
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == '-') {
                    flashLightOn();
                    Thread.sleep(dashDelay);
                    flashLightOff();
                    Thread.sleep(dotDealy);
                }
                if (word.charAt(i) == '.') {
                    flashLightOn();
                    Thread.sleep(dotDealy);
                    flashLightOff();
                    Thread.sleep(dotDealy);
                }
            }
            Thread.sleep(wordEndDelay);
        }
    }

    private void flashLightOn(){
        try{
            assert CM != null;
            String cameraId = CM.getCameraIdList()[0];
            CM.setTorchMode(cameraId, true);
        }
        catch(CameraAccessException e){
            Log.e("Camera Problem", "Cannot turn on camera flashlight");
        }
    }

    private void flashLightOff(){
        try{
            assert CM != null;
            String cameraId = CM.getCameraIdList()[0];
            CM.setTorchMode(cameraId, false);
        }
        catch(CameraAccessException e){
            Log.e("Camera Problem", "Cannot turn off camera flashlight");
        }
    }





}