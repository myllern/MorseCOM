package com.example.morescom;

public class MovingAvrageFilter {

    float [] datapoints;

    public MovingAvrageFilter(int nrOfDatapoints ){
        datapoints = new float[nrOfDatapoints];
        paddDataPoints();

    }

    public float avgFilter(float in){
        addNewDatapoint(in);
        return getAvg();
    }

    private void addNewDatapoint(float newData){
        for (int i = 0; i < datapoints.length - 1; i++) {
            datapoints[i] = datapoints[i + 1];
        }
        datapoints[datapoints.length-1] = newData;
    }
    private float getAvg(){
        float avg = 0;
        for (int i = 0; i < datapoints.length; i++) {
            avg =+ datapoints[i];
        }
        return avg / datapoints.length;
    }

    private void paddDataPoints(){
        for (int i = 0; i < datapoints.length; i++) {
            datapoints[i] = 0;
        }
    }


}
