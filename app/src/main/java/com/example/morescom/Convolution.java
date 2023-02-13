package com.example.morescom;


/**
 * This class is used for noise removal using WMA (weighted moving average by convolution method).
 * I have tested it and it is working absolutly fine out put of this class is tested against the
 * matlab WMA filter. This implementation is quite simple. To check how to use this class I have
 * add the main method as an example.
 * @author Muhammad Muaaz
 *@version 1.0
 */

public class Convolution {

    private double[] in;
    private double[] kernal;
    private double[] out;
    public Convolution(double[] _in,double[]_kernal) {
        setIn(_in);
        setKernal(_kernal);
    }
    private void setIn(double[] _in)throws IllegalArgumentException {

        // check the size of the datavector...
        if(_in.length <= 3) {
            throw new IllegalArgumentException("Data length can't be zero or smaller than zero");
        }

        this.in = _in;
        //Our denoised singal is of same length as of our input raw signal...
        this.out = new double [_in.length];
    }
    private void setKernal(double[] _kernal)throws IllegalArgumentException {

        //Check length of Kernel vector if its greater than zero; or Length is not an odd number.
        if(_kernal.length <= 0 || (_kernal.length%2) == 0) {
            throw new IllegalArgumentException("kernal length can't be zero or smaller than zero");
        }

        this.kernal = _kernal;
    }

    /**
     * This Method has results same as the results of the MATLAB'S conv(h,v) funciton.
     * To impliment WMA filter we use this convulation method.
     * To perform convulation we start with the kernel size (which must be of odd length),
     * From here we find out the number of zeros that we must append before and after the
     * data vector. After that we pick number of samples from that data vector equal to the
     * number of samples in the kernel vector, and multiply them (you can call it dot product).
     * The Length of denoised signal is same as the length of the raw signal

     * @return double [] out vector; which contains the denoised signal.
     * @version 1.0
     * @author Muhammad Muaaz
     *
     */
    @SuppressWarnings("unused")
    public double[] colvoltion1D() {

        int kernalSize = kernal.length;
        int zerosToAppend = (int) Math.ceil(kernalSize/2);

        // Make a new dataVector by appending zeros.
        double[] dataVec = new double [kernalSize-1+in.length];

        // add data in dataVec by compansating the zeropadding
        for(int i = 0; i< in.length;i++)
        {
            //Add data inbetween the padded zeros...
            dataVec[i+(int) Math.ceil(kernalSize/2)] = in[i];
        }
        // convolution begins here...
        int end = 0;
        while (end < in.length) {
            double sum = 0.0;
            for (int i = 0; i <kernalSize; i++)
            {
                sum += kernal[i]*dataVec[end+i];
            }
            out[end]= sum;
            end = end+1;
        }
        return out;
    }

}