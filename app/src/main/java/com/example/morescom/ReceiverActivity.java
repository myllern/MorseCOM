package com.example.morescom;

import android.graphics.Color;
import android.hardware.Camera;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;



import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class ReceiverActivity extends AppCompatActivity {

    GraphView graph;
    LineGraphSeries<DataPoint> seriesG, seriesB, seriesR;
    Camera mCamera;

    CameraPreview mPreview;
    ImageView convertedImageView;
    LinearLayout layoutForImage;
    FrameLayout preview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        convertedImageView = new ImageView(this);
        mCamera = getCameraInstance();
        layoutForImage = findViewById(R.id.ll);
        graph = findViewById(R.id.graph);
        mPreview = new CameraPreview(this, mCamera, convertedImageView, layoutForImage, graph);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        preview.setVisibility(View.INVISIBLE);

    }


    // This is connected to the lifecycle of the activity
    @Override
    protected void onPause() {
        super.onPause();
    }

    // This is connected to the lifecycle of the activity
    @Override
    protected void onResume() {
        super.onResume();
        /*
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera, convertedImageView,layoutForImage);
        preview.addView(mPreview);
        preview.setVisibility(View.INVISIBLE);
        */
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}

    /*
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

*/






