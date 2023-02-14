package com.example.morescom;


public class NormCrossCorrSingleValue {
    double corr_coff = 0;
    double startSeq_square;
    double inputData_square = 0;
    float[] startSeq;

    public NormCrossCorrSingleValue(float[] startSeq) {

        this.startSeq = startSeq;

        for (int n = 0; n < startSeq.length; n++) {
            this.startSeq_square += startSeq[n] * startSeq[n];
        }
    }

    public double calcCoff( float [] inputData) {

        corr_coff= 0;
        inputData_square = 0;

        for (int n = 0; n < inputData.length; n++) {
            inputData_square += inputData[n] * inputData[n];
        }


        for (int n = 0; n < inputData.length; n++) {
            corr_coff += inputData[n] * startSeq[n];
        }
        corr_coff = corr_coff / Math.sqrt(inputData_square * startSeq_square);
        return corr_coff;

    }
}









