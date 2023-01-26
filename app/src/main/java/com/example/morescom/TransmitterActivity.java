package com.example.morescom;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TransmitterActivity extends AppCompatActivity {
    Button sendButton;
    EditText messageBox;
    CameraManager cameraManager;
    String cameraId;
    Encoder encoder;
    Flasher flasher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmitter);
        sendButton = findViewById(R.id.send_button);
        messageBox = (EditText) findViewById(R.id.transmitter_input_box);
        encoder = new Encoder();

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getMessageInput();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        flasher = new Flasher(cameraManager,cameraId);
    }

    public void getMessageInput() throws InterruptedException {
        if(messageBox.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter Data", Toast.LENGTH_SHORT).show();
        }else {

            if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    Toast.makeText(TransmitterActivity.this, "This device has flash", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(TransmitterActivity.this, "This device has no flash", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(TransmitterActivity.this, "This device has no camera", Toast.LENGTH_SHORT).show();
            }
        }
        List<String> encodedSentenceArray  = encoder.encode(messageBox.getText().toString());

        while(true){
            flashLightOn();
            Thread.sleep(1000);
            flashLightOff();
            flashLightOn();
            Thread.sleep(100);
            flashLightOff();
            Thread.sleep(1000);

        }





    }






    // bad practice
    private void flashLightOn(){
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try{
            assert cameraManager != null;
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            Toast.makeText(TransmitterActivity.this, "FlashLight is ON", Toast.LENGTH_SHORT).show();
        }
        catch(CameraAccessException e){
            Log.e("Camera Problem", "Cannot turn on camera flashlight");
        }
    }

    private void flashLightOff(){
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try{
            assert cameraManager != null;
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            Toast.makeText(TransmitterActivity.this, "FlashLight is OFF", Toast.LENGTH_SHORT).show();
        }
        catch(CameraAccessException e){
            Log.e("Camera Problem", "Cannot turn off camera flashlight");
        }
    }
}