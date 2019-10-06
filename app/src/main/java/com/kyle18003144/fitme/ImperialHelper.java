package com.kyle18003144.fitme;

import java.text.DecimalFormat;

public class ImperialHelper {

    static  DecimalFormat df = new DecimalFormat("#");

    public static String convertToImperialLbs(double metric){
        return df.format(metric * 2.2)+" lbs";
    }

    public static String convertToMetricKgs(double imperial){
        return df.format(imperial * 0.45)+" kg";
    }

    public static int convertToImperial(double metric){
        return (int) (metric * 2.2);
    }

    public static int convertToMetric(double imperial){
        return (int) (imperial * 0.45);
    }
}
