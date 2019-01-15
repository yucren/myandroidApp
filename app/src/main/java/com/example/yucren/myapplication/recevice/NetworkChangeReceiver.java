package com.example.yucren.myapplication.recevice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context,"网络状态发生变化" + getNetworkType(context) ,Toast.LENGTH_LONG).show();
    }
    private  static  final  int NETTYPE_WIFI=0x01;
    private  static  final  int NETTYPE_CMWAP=0x02;
    private  static  final  int NETTYPE_CMNET=0x03;
    public String getNetworkType(Context context){
        ConnectivityManager connectivityManager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        int netType =0;
        String netTYPEZN="无网络";
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo ==null)
        {
            return  netTYPEZN;
        }
        int nType = networkInfo.getType();
        if (nType==ConnectivityManager.TYPE_MOBILE)
        {
            String extrainfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extrainfo))
            {
                if ( extrainfo.toLowerCase().equals("cmnet"))
                {
                    netType = NETTYPE_CMNET;
                    netTYPEZN="手机网络CMMNET";
                }
                else {
                    netType=NETTYPE_CMWAP;
                    netTYPEZN="手机网络CMMWAP";
                }
            }
        }else  if (nType==ConnectivityManager.TYPE_WIFI)
        {
            netType =NETTYPE_WIFI;
            netTYPEZN="WIFI";
        }
     return  netTYPEZN;
    }

}
