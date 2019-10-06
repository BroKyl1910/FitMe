package com.kyle18003144.fitme;

import java.text.DecimalFormat;

public class UnitsHelper {

    static DecimalFormat df = new DecimalFormat("#");

    public static String convertToImperialLbs(double metric) {
        return df.format(metric * 2.2) + " lbs";
    }

    public static int convertToImperialWeight(double metric) {
        return (int) (metric * 2.2);
    }

    public static int convertToMetricWeight(double imperial) {
        return (int) (imperial * 0.45);
    }

    public static int convertToMetricHeight(double metric) {
        return (int) (metric * 2.5);
    }

    public static int convertToImperialHeight(double imperial) {
        return (int) (imperial * 0.4);
    }

    public static String convertToImperialHeightIn(double imperial) {
        return df.format(imperial * 0.4)+" inches";
    }

    public static String formatMetricWeight(double metric){
        return df.format(metric)+" kg";
    }
    public static String formatMetricHeight(double metric){
        return df.format(metric)+" cm";
    }
}
