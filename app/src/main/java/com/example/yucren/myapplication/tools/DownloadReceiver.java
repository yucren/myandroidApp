package com.example.yucren.myapplication.tools;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;

public class DownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        Log.d("=====", "下载的IDonReceive: " + completeDownloadId);
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            DownloadManager.Query query = new DownloadManager.Query();
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            query.setFilterById(id);
            Cursor c = manager.query(query);
            if (c.moveToFirst()) {
                int fileUriId = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                String fileUri = c.getString(fileUriId);
                String fileName1 = c.getString(fileUriId);
                String fileName = fileName1.substring(fileName1.indexOf("shjxmes"));
                String dddd =context.getExternalFilesDir(null).getPath();
                File file = new File(Environment.getExternalStoragePublicDirectory("")+"/"+ Environment.DIRECTORY_DOWNLOADS, fileName);
                if (fileName != null) {
                    Intent intent_ins = new Intent(Intent.ACTION_VIEW);
                    //版本在7.0以上是不能直接通过uri访问的
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {

                        // 由于没有在Activity环境下启动Activity,设置下面的标签
                        //
                        //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                        Uri apkUri = FileProvider.getUriForFile(context, "com.example.yucren.myapplication.fileProvider", file);
                        //添加这一句表示对目标应用临时授权该Uri所代表的文件
                        intent_ins.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                      //  intent_ins.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent_ins.setDataAndType(apkUri, "application/vnd.android.package-archive");
                        Log.e("e","要安装的apk路径为=="+apkUri.getPath());
                    } else {
                        intent_ins.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent_ins.setDataAndType(Uri.fromFile(new File(fileUri)),
                                "application/vnd.android.package-archive");
                    }
                    context.startActivity(intent_ins);


                }
            }
        } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction())) {
            long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
//            manager.remove(ids);
//            Toast.makeText(context, "已经取消下载", Toast.LENGTH_SHORT).show();
        }

    }

    public static void installApp(Context pContext, File pFile) {
        if (null == pFile) return;
        if (!pFile.exists()) return;
        Intent _Intent = new Intent();
        _Intent.setAction(Intent.ACTION_VIEW);
        Uri _uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            _Intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            _uri = FileProvider.getUriForFile(pContext, pContext.getPackageName() + ".fileProvider", pFile);
        } else {
            _uri = Uri.fromFile(pFile);
            _Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        _Intent.setDataAndType(_uri, "application/vnd.android.package-archive");
        pContext.startActivity(_Intent);
    }

}
