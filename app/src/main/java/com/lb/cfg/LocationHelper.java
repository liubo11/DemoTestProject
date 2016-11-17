package com.lb.cfg;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * Created by LiuBo on 2016-10-10.
 */

public class LocationHelper implements AMapLocationListener, Handler.Callback {
    private static LocationHelper sInstance;

    /**
     * Application中调用
     */
    public static LocationHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new LocationHelper(context);
        }
        return sInstance;
    }

    private static final String KEY_LOCATION_LAST_TIME = "location.last.updatetime";
    private static final String KEY_LOCATION_CITY = "location.city";
    private static final String KEY_LOCATION_TEMP = "location.temp";
    private static final String KEY_LOCATION_HUM = "location.hum";

    public static final int UNKNOWN_DATA = 0xFF;
    private String mCity;
    private int mTemperature = UNKNOWN_DATA;
    private int mHumidity = UNKNOWN_DATA;

    //所有数据都更新后的时间
    private int mUpdateTime;
    //最近一次存储的更新时间
    private int mLastUpdateTime;

    private Handler mMsgHandler;
    private LocationHelper(Context context) {
        mMsgHandler = new Handler(this);
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle metaData = appInfo.metaData;
            if (null != metaData) {
                String testP1 = metaData.getString(WEATHER_KEY,"");
                mWeatherKey = testP1;
            } else {
                Log.e("MyApplication", "metadata is null");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        if (mCity == null || mCity.length() == 0) {
            //没有获取到城市信息 那么调用方法
            updateLocation(context);
        } else {
            updateWeather(mCity, true);
        }
    }

    private AMapLocationClient mLocationClient;
    private void updateLocation(Context context) {
        if (mLocationClient == null) {
            //声明mLocationOption对象
            AMapLocationClientOption locationOption;
            mLocationClient = new AMapLocationClient(context);
            //初始化定位参数
            locationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为2000ms
            locationOption.setInterval(2000);
            locationOption.setOnceLocation(true);
            locationOption.setOnceLocationLatest(true);
            //设置定位参数
            mLocationClient.setLocationOption(locationOption);
        }
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mLocationClient.startLocation();
    }

    private static final int WHAT_WEATHER_SUCCESS = 0x1001;
    private static final String WEATHER_URL = "http://op.juhe.cn/onebox/weather/query";
    private static final String WEATHER_KEY = "com.galaxywind.weatherinfo.key";

    //最小更新周期 一个小时
    private static final int MIN_WEATHER_UPDATE_SPACE = 3600;
    private boolean isGettingWeatherInfo;
    private String mWeatherKey;

    private static String buildWeatherRequestUrl(@NonNull String city) throws UnsupportedEncodingException {
        return WEATHER_URL+"?cityname="+URLEncoder.encode(city, "UTF-8")+"&key="+WEATHER_KEY;
    }
    private boolean needUpdateWeather(boolean isSameCity) {
        if (isSameCity) {
            int compare;
            if (mUpdateTime == 0) {
                if (mLastUpdateTime == 0) {
                    return true;
                } else {
                    compare = mLastUpdateTime;
                }
            } else {
                compare = mUpdateTime;
            }
            int dt = (int) (System.currentTimeMillis() / 1000) - compare;
            return dt > MIN_WEATHER_UPDATE_SPACE || dt < 0;
        }
        return true;
    }

    /**
     * 更新城市天气：  目前 温度和湿度
     */
    public void updateWeather() {
        updateWeather(mCity, true);
    }
    private boolean isKeyValid() {
        return mWeatherKey != null;
    }
    private void updateWeather(final @NonNull String city, boolean isSameCity) {
        if (isGettingWeatherInfo || !needUpdateWeather(isSameCity) || !isKeyValid()) {
            return;
        }

        isGettingWeatherInfo = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(buildWeatherRequestUrl(city));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setConnectTimeout(3000);
                    connection.setRequestMethod("GET");
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream is = connection.getInputStream();
                        byte[] buffer = new byte[512];//大约8k的字符  用16k的buffer
                        byte[] container = new byte[1024 * 16];
                        final int max = 32;
                        int curLen = 0;
                        for (int i = 0; i < max; i++) {
                            int r = is.read(buffer);
                            if (r != -1) {
                                copyByteArray(container, curLen, buffer, 0, r);
                                curLen += r;
                            } else {
                                break;
                            }
                        }

                        String response = new String(container);
                        Log.d("debug", "http weather response="+response);
                        getWeatherInfo(response.trim());

                        is.close();
                        connection.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isGettingWeatherInfo = false;
            }
        }).start();
    }

    private static void copyByteArray(byte[] src, int start, byte[] dest, int ds, int dd) {
        for (int i = 0, j = ds; j < dd; i++, j++) {
            src[start+i] = dest[j];
        }
    }

    /**
     * 在子线程中调用
     * @param json json字符串
     */
    private void getWeatherInfo(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject resultJsn = jsonObject.getJSONObject("result");
            if (resultJsn.has("data")) {
                JSONObject dataObject = resultJsn.getJSONObject("data");
                if (dataObject.has("realtime")) {
                    JSONObject realObject = dataObject.getJSONObject("realtime");
                    if (realObject.has("weather")) {
                        JSONObject weatherObject = realObject.getJSONObject("weather");
                        String temp = weatherObject.getString("temperature");
                        String hum = weatherObject.getString("humidity");

                        Message msg = mMsgHandler.obtainMessage(WHAT_WEATHER_SUCCESS);
                        msg.arg1 = Integer.valueOf(temp);
                        msg.arg2 = Integer.valueOf(hum);
                        mMsgHandler.sendMessage(msg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        String city = aMapLocation.getCity();
        if (city != null && city.length() != 0) {
            updateWeather(city, city.equals(mCity));
            mCity = city;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == WHAT_WEATHER_SUCCESS) {
            mTemperature = msg.arg1;
            mHumidity = msg.arg2;
            mUpdateTime = (int) (System.currentTimeMillis() / 1000);
            if (mListener != null) {
                mListener.notifyChange(this);
            }
        }
        return false;
    }
    private LocationInfoChangeListener mListener;
    public void setLocationInfoChangeListener(LocationInfoChangeListener listener) {
        mListener = listener;
        if (mTemperature != UNKNOWN_DATA && mListener != null) {
            mListener.notifyChange(this);
        }
    }

    /**
     * 释放定位服务
     */
    public void release() {
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }

    public interface LocationInfoChangeListener {
        void notifyChange(LocationHelper locationHelper);
    }

    /**
     * 当前所在城市
     * @return city or ""
     */
    public String getCity() {
        return mCity;
    }

    /**
     * 当前城市的实时温度
     * @return 可能无效值 {@link #UNKNOWN_DATA}
     */
    public int getTemperature() {
        return mTemperature;
    }

    /**
     * 当前城市的实时湿度
     * @return 可能为无效值 {@link #UNKNOWN_DATA}
     */
    public int getHumidity() {
        return mHumidity;
    }
}
