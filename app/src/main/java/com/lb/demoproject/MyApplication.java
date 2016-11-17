package com.lb.demoproject;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lb.cfg.Config;
import com.lb.cfg.LocationHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Administrator on 2016-04-25.
 */
public class MyApplication extends Application implements AMapLocationListener{

    @Override
    public void onCreate() {
        super.onCreate();


        //LocationHelper.getInstance(this);



        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle metaData = appInfo.metaData;
            if (null != metaData) {
                String testP1 = metaData.getString("com.galaxywind.weatherinfo.key","none");

                Log.d("MyApplication", "testp1="+testP1);
            } else {
                Log.e("MyApplication", "metadata is null");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Log.d("MyApplication", "start app");


        boolean isSupportTab = getResources().getBoolean(R.bool.is_support_show_tab);
        int maxUserNum = getResources().getInteger(R.integer.max_user_num);

        new Config(this);

        Log.d("MyApplication", "isSupportTab="+isSupportTab+", maxUserNum="+maxUserNum);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                int type = amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                double lat = amapLocation.getLatitude();//获取纬度
                double lon = amapLocation.getLongitude();//获取经度
                float acc = amapLocation.getAccuracy();//获取精度信息
                String city = amapLocation.getCity();

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());

                String time = df.format(date);//定位时间

                Log.d("demo-debug", "时间："+time+"，纬度："+lat+", 经度："+lon+", 精度信息："+acc+"，city："+city);

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }
}
