package com.test.flavor;

import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.WallpaperManager;
import android.app.backup.BackupManager;
import android.app.usage.NetworkStatsManager;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothManager;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.PowerManager;
import android.service.notification.StatusBarNotification;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;

import com.lb.demoproject.MyApplication;

/**
 * Created by Administrator on 2016-04-26.
 */
public class Flavor1App extends MyApplication {


    /*目前Android中已有的系统级服务*/
    //private EntropyServer entropyServer;
    PowerManager mPowerManager;
    ActivityManager mActivityManager;
    TelecomManager mTelecomManager;
    PackageManager mPackageManager;
    AccountManager mAccountManager;
    ContentResolver mContentResolver;
    BatteryManager mBatteryManager;
    //HardwareService ring vibrare服务
    SensorManager mSensorManager;//传感器
    WindowManager mWindowManager;//PhoneWindowManager
    AlarmManager mAlarmManager;
    BluetoothManager mBluetoothManager;
    //StatusBarManager StatusBarNotification // 状态栏管理
    ClipboardManager mClipBoardManager;
    InputMethodManager mInputMethodManager;
    NetworkStatsManager mNetworkStatsManager;
    ConnectivityManager mConnectivityManager;
    AccessibilityManager mAccessibilityManager;
    NotificationManager mNotificationManager;
    //MountService 磁盘挂载监听服务
    //DeviceStorageMonitorService 磁盘不足监听服务
    LocationManager mLocationManager;
    SearchManager mSearchManager;
    WallpaperManager mWallpagerManager;
    AudioManager mAudioManager;
    //HeadsetObserver 耳机插拔监听
    BackupManager mBackupManager;
    AppWidgetManager mAppWidgetManager;

    //AudioFlinger
    MediaPlayer mMeiadPlayer;
    Camera mCamera;


    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("Flavor1App", "onCreate called");
    }
}
