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
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class TransmitterActivity extends AppCompatActivity {
    int unitTime = 300;
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
        flasher = new Flasher(cameraManager,cameraId, unitTime);


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
        flasher.transmit(encoder.encode(messageBox.getText().toString()));
    }
}


/*
dotF = [1 1 0 0 0 0 0 0];
dashF =[1 1 1 1 1 1 0 0];
dot_data = [15 15 10 10 10 10 10 10 ];
dash_data = [15 15 15 15  15 15 10 10];
coff_dot_fdot = max(xcorr(dot_data,dotF,10,'normalized'))
coff_dot_fdash =  max(xcorr(dot_data,dashF,10,'normalized'))
coff_dash_fdot  = max(xcorr(dash_data,dotF,10,'normalized'))
coff_dash_fdash = max(xcorr(dash_data,dashF,10,'normalized'))
 */