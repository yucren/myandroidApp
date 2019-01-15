package com.example.yucren.myapplication.recevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
       Log.i("myreceiver","收到广播");
        Toast.makeText(context,"赶紧开始扫描吧",Toast.LENGTH_LONG);
    }
}
