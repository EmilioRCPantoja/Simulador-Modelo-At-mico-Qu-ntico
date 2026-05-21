package com.mygdx.utils;

import com.badlogic.gdx.math.Vector3;

public class Distancia {

    public static double getdistancia(Vector3 pos1, Vector3 pos2){
        double r = 0.0;

        double xd = (double) ((pos1.x - pos2.x) * (pos1.x - pos2.x));

        double yd = (double) ((pos1.y - pos2.y) * (pos1.y - pos2.y));

        double zd = (double) ((pos1.z - pos2.z) * (pos1.z - pos2.z));

        r = Math.sqrt(xd + yd + zd);

        return r;
    }

    public static double getDistancia(double x, double y, double z){
        return Math.sqrt((x*x) + (y*y) + (z*z));
    }
}
