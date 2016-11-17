package com.lb.demoproject;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaButtonReceiver;
import android.view.View;

import com.lb.receiver.RemoteUtiles;

/**
 * Created by LiuBo on 2016-11-03.
 */

public class TestRemoteControlMusic extends BaseActivity {

    private RemoteUtiles remoteUtiles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = new View(this);
        setContentView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remoteUtiles = new RemoteUtiles(TestRemoteControlMusic.this);
                remoteUtiles.prepare();
                remoteUtiles.play(1000, 3 * 60 *60 * 1000, BitmapFactory.decodeResource(getResources(), R.drawable.p4));
            }
        });
    }



    private void prepare() {

    }

}
