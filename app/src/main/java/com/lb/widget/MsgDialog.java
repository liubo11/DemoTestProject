package com.lb.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.lb.demoproject.R;

import java.util.zip.Inflater;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by LiuBo on 2016-10-28.
 */

public class MsgDialog {

    public static void show(Context context, String msg) {
        final MaterialDialog dialog = new MaterialDialog(context);
        dialog.setTitle("警告")
                .setCanceledOnTouchOutside(true)
                .setMessage(msg)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
        dialog.show();


    }

    public static void showAlertDialog(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.Pad_Dialog_Light);
        builder.setView(LayoutInflater.from(context).inflate(R.layout.test_dialog_layout, null));
        builder.create().show();
    }
}
