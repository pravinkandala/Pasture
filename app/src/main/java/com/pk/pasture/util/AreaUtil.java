package com.pk.pasture.util;

import java.text.DecimalFormat;

public class AreaUtil {

    public static String getFormatedArea(Double area) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(area);

    }

}
