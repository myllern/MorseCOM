package com.example.morescom;

import java.math.*;
import java.util.Arrays;

public class NormCrossCorr {

    public NormCrossCorr(double[] a, double[] b, int maxlag) {
        double[] corr_coff = new double[2 * maxlag + 1];
        Arrays.fill(corr_coff, 0);

        double a_square = 0;
        double b_square = 0;


        for (int n = 0; n < a.length; n++) {
            a_square += a[n]*a[n];
        }
        for (int n = 0; n < b.length; n++) {
            b_square += b[n]*b[n];
        }

        for (int lag = b.length - 1, idx = maxlag - b.length + 1;
             lag > -a.length; lag--, idx++) {
            if (idx < 0)
                continue;

            if (idx >= corr_coff.length)
                break;

            int start = 0;

            if (lag < 0) {

                start = -lag;
            }
            int end = a.length - 1;

            if (end > b.length - lag - 1) {
                end = b.length - lag - 1;

            }
            for (int n = start; n <= end; n++) {

                corr_coff[idx] += a[n] * b[lag + n];
            }
            corr_coff[idx] = corr_coff[idx]/Math.sqrt(a_square*b_square);
            System.out.println(corr_coff[idx]);

        }

    }
}







