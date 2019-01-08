package com.example.yucren.myapplication.tools;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

public class UpdataTool {
    /**
     * 获取版本事情
     * @param context 上正文
     * @return 返回版本号
     * @throws PackageManager.NameNotFoundException
     */
    public  static String getVersionName(Context context) throws PackageManager.NameNotFoundException
    {
        PackageManager packageManager =context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        String verson =packageInfo.versionName;
        return  verson;
    }
    public static int compareVersion (String version1,String version2)
    {
        if (version1.equals(version2))
        {
            return  0;
        }
        String[] version1Array =version1.split("\\.");
        String[] version2Array =version2.split("\\.");
        int index =0;
        int minLen =Math.min(version1Array.length,version2.length());
        int diff =0;
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }


    }
    public  static  void downApp(Context context,String url){
        DownloadManager downloadManager =(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri =Uri.parse(url);
        DownloadManager.Request request =new DownloadManager.Request(uri);
        request.setDestinationInExternalFilesDir( context,Environment.DIRECTORY_DOWNLOADS,"shjxmes.apk");
        request.setDescription("上海机械MESApp下载");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("application/vnd.android.package-archive");
        // 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        request.setVisibleInDownloadsUi(true);
        long reference =downloadManager.enqueue(request);
        SharedPreferences sharedPreferences = context.getSharedPreferences("downloadcomplete",Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong("reference",reference);
    }


}
