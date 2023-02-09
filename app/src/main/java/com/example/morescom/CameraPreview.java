package com.example.morescom;


import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.Color;
import android.graphics.ImageFormat;

import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static android.content.ContentValues.TAG;


public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    float[] rgbSum = new float[3];
    int counter;
    GraphView graph;
    LineGraphSeries<DataPoint> seriesG, seriesB, seriesR;

    private float flashThreshold = 1F;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int imageFormat;
    private boolean mProcessInProgress = false;
    private Bitmap mBitmap = null;
    private ImageView myCameraPreview;
    private int[] pixels = null;
    public int width = 640, height = 480;
    private Camera.Parameters params;

    private MovingAverageFilter MF;
    private float[] savedDataPoints;


    public CameraPreview(Context context, Camera camera, ImageView mCameraPreview, LinearLayout layout, GraphView graph) {
        super(context);
        this.graph = graph;
        mCamera = camera;
        params = mCamera.getParameters();
        imageFormat = params.getPreviewFormat();
        MF = new MovingAverageFilter(3);
        //Make sure that the preview size actually exists, and set it to our values
        for (Camera.Size previewSize : mCamera.getParameters().getSupportedPreviewSizes()) {
            if (previewSize.width == 640 && previewSize.height == 480) {
                params.setPreviewSize(previewSize.width, previewSize.height);
                break;
            }
        }
        mCamera.setParameters(params);
        myCameraPreview = mCameraPreview;
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        layout.addView(myCameraPreview);

        seriesR = new LineGraphSeries<>(new DataPoint[]{new DataPoint(0, 0),});
        seriesR.setColor(Color.RED);
        graph.addSeries(seriesR);

        seriesG = new LineGraphSeries<>(new DataPoint[]{new DataPoint(0, 0),});
        seriesG.setColor(Color.GREEN);
        graph.addSeries(seriesG);

        seriesB = new LineGraphSeries<>(new DataPoint[]{new DataPoint(0, 0),});
        seriesB.setColor(Color.BLUE);
        graph.addSeries(seriesB);

        Viewport vp = graph.getViewport();
        vp.setXAxisBoundsManual(true);
        vp.setMinX(0);
        vp.setMaxX(300);

        savedDataPoints = new float[5];


    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {

            mCamera.setPreviewDisplay(holder);
            mCamera.setPreviewCallback(this);
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Taken care of in our activity.
    }

    /* If the application is allowed to rotate, here is where you would change the camera preview
     * size and other formatting changes.*/
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null) {
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    /*This method is overridden from the camera class to do stuff on every frame that is taken
     * from the camera, in the form of the byte[] bytes array.
     *
     * */
    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        if (imageFormat == ImageFormat.NV21) {
            if (mProcessInProgress) {
                mCamera.addCallbackBuffer(bytes);
            }
            if (bytes == null) {
                return;
            }
            mCamera.addCallbackBuffer(bytes);
            /*
             * Here we rotate the byte array (because of some wierd feature in my phone at least)
             * if your picture is horizontal, delete the rotation of the byte array.
             * */
            bytes = rotateYUV420Degree90(bytes, width, height);

            if (mBitmap == null) {
                mBitmap = Bitmap.createBitmap(height, width, Bitmap.Config.ARGB_8888);
                myCameraPreview.setImageBitmap(mBitmap);
            }

            myCameraPreview.invalidate();
            mProcessInProgress = true;
            mCamera.addCallbackBuffer(bytes);
            // Start our background thread to process images
            new ProcessPreviewDataTask().execute(bytes);

        }
    }

    private class ProcessPreviewDataTask extends AsyncTask<byte[], Void, Boolean> {


        @Override
        protected Boolean doInBackground(byte[]... datas) {
            counter++;
            byte[] data = datas[0];
            // I use the tempWidth and tempHeight because of the rotation of the image, if your
            // picture is horizontal, use width and height instead.
            int tempWidth = 480;
            int tempHeight = 640;
            // Here we decode the image to a RGB array.
            pixels = decodeYUV420SP(data, tempWidth, tempHeight);
            mCamera.addCallbackBuffer(data);
            mProcessInProgress = false;


            return true;
        }

        @Override

        protected void onPostExecute(Boolean result) {
                counter++;
                myCameraPreview.invalidate();
                mBitmap.setPixels(pixels, 0, 480, 0, 0, 480, 640);
                myCameraPreview.setImageBitmap(scope(mBitmap, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2));
                float RGBsum = rgbSum[0] + rgbSum[1] + rgbSum[2];
                MF.pushValue(RGBsum);

                detectedFlash(RGBsum);


                seriesR.appendData(new DataPoint(counter, MF.getValue()), true, 250, false);
                //seriesG.appendData(new DataPoint(counter, ), true, 100, false);
                // seriesB.appendData(new DataPoint(counter, rgbSum[2]), true, 100, false);
                Log.d("ok", String.valueOf(MF.getValue()/2));
                rgbSum[0] = 0;
                rgbSum[1] = 0;
                rgbSum[2] = 0;


        }

    }

    public boolean detectedFlash(float in) {

        float avg = MF.getValue(); // Avg from datapoints
        //Log.d("avg",String.valueOf(in/avg));
        if (in / avg > 1.15) {
            //Log.d("avg", String.valueOf(in / avg));
            //Log.d("avg", "FLAAAAASH");
        }
        //Log.d("avg",String.valueOf(in/avg));


        return true;
    }


    public Bitmap scope(Bitmap bm, int centerX, int centerY) {
        int lineThickness = 5;
        int radius = 80; // radius of the circle
        for (int x = 0; x < bm.getWidth(); x++) {
            for (int y = 0; y < bm.getHeight(); y++) {
                int distance = (int) Math.sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY));
                if (distance < radius + lineThickness) {
                    float[] RGB = new float[3];
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        RGB = bm.getColor(x, y).getComponents();
                    }
                    rgbSum[0] += RGB[0];
                    rgbSum[1] += RGB[1];
                    rgbSum[2] += RGB[2];
                    if (distance > radius - lineThickness) {
                        bm.setPixel(x, y, Color.rgb(250, 0, 0));
                    }
                }
            }
        }
        return bm;
    }

    private byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }
        // Rotate the U and V color components
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i--;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i--;
            }
        }
        return yuv;
    }


    /* Decodes the image from the NV21 format into an RGB-array with integers.
     * Since the NV21 array is made out of bytes, and one pixel is made out of 1.5 bytes, this is
     * quite hard to understand. If you want more information on this you can read about it on
     * */
    public int[] decodeYUV420SP(byte[] yuv, int width, int height) {

        final int frameSize = width * height;
        int rgb[] = new int[width * height];
        final int ii = 0;
        final int ij = 0;
        final int di = +1;
        final int dj = +1;

        int a = 0;
        for (int i = 0, ci = ii; i < height; ++i, ci += di) {
            for (int j = 0, cj = ij; j < width; ++j, cj += dj) {
                int y = (0xff & ((int) yuv[ci * width + cj]));
                int v = (0xff & ((int) yuv[frameSize + (ci >> 1) * width + (cj & ~1) + 0]));
                int u = (0xff & ((int) yuv[frameSize + (ci >> 1) * width + (cj & ~1) + 1]));
                y = y < 16 ? 16 : y;

                int r = (int) (1.164f * (y - 16) + .596f * (v - 128));
                int g = (int) (1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                int b = (int) (1.164f * (y - 16) + 2.018f * (u - 128));


                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);
                rgb[a++] = 0xff000000 | (r << 16) | (g << 8) | b;


            }
        }

        return rgb;
    }


}
