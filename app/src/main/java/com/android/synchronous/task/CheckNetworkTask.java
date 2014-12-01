package com.android.synchronous.task;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.synchronous.util.NetworkUtil;

public class CheckNetworkTask {

    public static void check(final Context context){
        if(!NetworkUtil.isOnline(context)){
            AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
            builder.setTitle("Internet Disabled");
            builder.setMessage("Please enable and try again.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        }
    }
}