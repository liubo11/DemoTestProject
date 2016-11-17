package com.lb.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

/**
 * Created by LiuBo on 2016-11-03.
 */

public class MediaButtonReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        KeyEvent ke = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

        System.out.println("action="+action);
        System.out.println("ke="+ke);
    }
}
