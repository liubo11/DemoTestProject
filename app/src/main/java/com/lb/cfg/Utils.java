package com.lb.cfg;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import java.util.Random;

/**
 * Created by Administrator on 2016-08-01.
 */
public class Utils {

    public static Drawable getRemoteDrawable(Context localContext) {
        try {
            String remotePackage = "com.gwcd.airplug";
            String resIdString = "banner_air_con_a";
            Context context = localContext.createPackageContext(remotePackage, Context.CONTEXT_IGNORE_SECURITY);
            Resources remoteResources = context.getResources();
            Drawable remoteDrawable = remoteResources.getDrawable(
                    remoteResources.getIdentifier(resIdString, "drawable", remotePackage));
            return remoteDrawable;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
    private static Random random = new Random();
    public static int getRandomColor() {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.rgb(r,g,b);
    }
    public static int getAlphaRandomColor(int alpha) {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.argb(alpha,r,g,b);
    }

    public static int getRandom(int n) {
        return random.nextInt(n);
    }
}
