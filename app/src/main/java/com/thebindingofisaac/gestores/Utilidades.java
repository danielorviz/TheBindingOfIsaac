package com.thebindingofisaac.gestores;

/**
 * Created by Dani on 13/11/2017.
 */
public class Utilidades {

    public static double proximoACero(double a, double b) {
        if (Math.pow(a,2) <  Math.pow(b,2))
            return a;
        else
            return b;

    }
}
