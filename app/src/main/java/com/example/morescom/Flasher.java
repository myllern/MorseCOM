package com.example.morescom;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class  Flasher{
    CameraManager CM;
    String cID;
    int dashDelay = 100;
    int dotDealy = 20;

    public Flasher(CameraManager CM, String cID){
        this.CM = CM;
        this.cID = cID;
    }

    public void blink(ArrayList<String> BlinkArray) throws InterruptedException {

        for (String word : BlinkArray
        ) {
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == '-') {
                    Thread.sleep(dashDelay);
                } else {
                    Thread.sleep(dotDealy);
                }

            }
        }
    }


    // bad practice
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




}