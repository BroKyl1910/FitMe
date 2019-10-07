package com.kyle18003144.fitme;

import java.text.DecimalFormat;

public class UnitsHelper {

    static DecimalFormat df = new DecimalFormat("#");

    //Used only for displaying imperial weights
    public static String convertToImperialLbs(double metric) {
        return df.format(metric * 2.20462) + " lbs";
    }
    public static String convertToImperialHeightIn(double metric) {
        return df.format(metric * 0.393701) + " inches";
    }
    public static String formatMetricWeight(double metric) {
        return df.format(metric) + " kg";
    }
    public static String formatMetricHeight(double metric) {
        return df.format(metric) + " cm";
    }


    //Used only for conversions
    public static int convertToImperialWeight(double metric) {
        return (int) Math.round(metric * 2.20462);
    }
    public static int convertToMetricWeight(double imperial) {
        return (int) Math.round(imperial * 0.453592);
    }
    public static int convertToMetricHeight(double imperial) {
        return (int) Math.round(imperial * 2.54);
    }
    public static int convertToImperialHeight(double metric) {
        return (int) Math.round(metric * 0.393701);
    }




}
