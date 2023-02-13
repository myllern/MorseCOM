package com.example.morescom;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class NormCrossCorr {

    private double[] template_dot;
    private double[] template_dash;
    private double[] template_intSymbolSpace;


    public NormCrossCorr(double[] template_dot, double[] template_dash, double[] template_intSymbolSpace) {
        this.template_dot = template_dot;
        this.template_dash = template_dash;
        this.template_intSymbolSpace = template_intSymbolSpace;

    }


    public ArrayList[] calculate(double[] data) {
        ArrayList[] arr = new ArrayList[2];
        arr[0] = (DoubleStream.of(getCoefficientOfFilter(data,template_dot)).boxed().collect(Collectors.toCollection(ArrayList::new)));
        arr[1] = (DoubleStream.of(getCoefficientOfFilter(data,template_dash)).boxed().collect(Collectors.toCollection(ArrayList::new)));
        arr[2] = (DoubleStream.of(getCoefficientOfFilter(data,template_intSymbolSpace)).boxed().collect(Collectors.toCollection(ArrayList::new)));

        return arr;
    }

    public double getCoefficientOfFilter(double[] data, double[] template) {
        double data_mean = mean(data);
        double data_sdv = standardDeviation(data, data_mean);
        double template_mean = mean(template);
        double template_std = standardDeviation(template, template_mean);
        double numerator = 0.0;
        for (int i = 0; i < data.length; i++) {
            numerator += (data[i] - data_mean) * (template[i] - template_mean);
        }
        return numerator / ((data_sdv) * template_std * data.length);
    }

    private static double mean(double[] data) {
        return Arrays.stream(data).sum() / data.length;
    }

    private static double standardDeviation(double[] data, double mean) {
        double sum = 0.0;
        for (double d : data) {
            sum += Math.pow(d - mean, 2);
        }
        return Math.sqrt(sum / (data.length - 1));
    }
}



