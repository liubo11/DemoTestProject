package com.lb.receiver;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.*;

/**
 * Created by LiuBo on 2016-11-03.
 */

public class RemoteUtiles {

    public RemoteUtiles(Context context) {
        this.mContext = context;
    }

    private ComponentName mComponentName;
    private Context mContext;
    private PendingIntent mPendingIntent;
    private Handler mHandler;

    private MediaSessionCompat mMediaSession;

    public void prepare() {
        if (mComponentName != null) return;
        //这里同样要指明相应的MediaBottonReceiver，用来接收处理线控信息
        //Android5.0之前的版本线控信息直接通过BroadcastReceiver处理
        mComponentName = new ComponentName(mContext.getPackageName(), MediaButtonReceiver.class.getName());
        mContext.getPackageManager().setComponentEnabledSetting(mComponentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setComponent(mComponentName);
        mPendingIntent = PendingIntent.getBroadcast(mContext, 0, mediaButtonIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //由于非线程安全，这里要把所有的事件都放到主线程中处理，使用这个handler保证都处于主线程
        mHandler = new Handler(Looper.getMainLooper());

        mMediaSession = new MediaSessionCompat(mContext, "tag", mComponentName, null);
        //指明支持的按键信息类型
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(mPendingIntent);

        //这里指定可以接收的来自锁屏页面的按键信息
        PlaybackStateCompat state = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_FAST_FORWARD | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS | PlaybackStateCompat.ACTION_STOP).build();
        mMediaSession.setPlaybackState(state);

        //在Android5.0及以后的版本中线控信息在这里处理
        mMediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public boolean onMediaButtonEvent(Intent intent) {
                //通过Callback返回按键信息，为复用MediaButtonReceiver，直接调用它的onReceive()方法
                MediaButtonReceiver mMediaButtonReceiver = new MediaButtonReceiver();
                mMediaButtonReceiver.onReceive(mContext, intent);
                return true;
            }
        }, mHandler);    //把mHandler当做参数传入，保证按键事件处理在主线程



        //把MediaSession置为active，这样才能开始接收各种信息
        if (!mMediaSession.isActive()) {
            mMediaSession.setActive(true);
        }

    }

    public void play(int playTime, int duration, Bitmap cover) {
        //同步当前的播放状态和播放时间
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder();
        stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, playTime, 1.0f);
        mMediaSession.setPlaybackState(stateBuilder.build());

        //同步歌曲信息
        MediaMetadataCompat.Builder md = new MediaMetadataCompat.Builder();
        md.putString(MediaMetadataCompat.METADATA_KEY_TITLE, "123");
        md.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "ldh");
        md.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "abc");
        md.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration);
        md.putBitmap(MediaMetadataCompat.METADATA_KEY_ART, cover);
        mMediaSession.setMetadata(md.build());
    }
}
