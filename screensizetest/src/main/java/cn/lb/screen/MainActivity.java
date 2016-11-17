package cn.lb.screen;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class MainActivity extends Activity implements Serializable{
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics dm = getResources().getDisplayMetrics();

                String str = dm.toString();
                Log.d("Screen","dm="+str);
                File f = new File(Environment.getExternalStorageDirectory().getPath(),"test_debug");
                //File f = new File(getFilesDir().getPath()+"/test_debug");
                Log.d("path","file="+f.toString());
                if (!f.exists()) {
                    Log.d("path","create path="+f.mkdir());
                    File debugf = new File(f, "debug.txt");
                    try {
                        Log.d("path","debugf="+debugf.toString());
                        debugf.createNewFile();
                        FileOutputStream fos = new FileOutputStream(debugf);
                        fos.write(str.getBytes());
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                mHandler.postDelayed(this, 500);
            }
        }, 500);
    }
}
