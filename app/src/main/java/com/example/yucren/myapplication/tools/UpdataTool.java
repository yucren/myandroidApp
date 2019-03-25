package com.example.yucren.myapplication.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.yucren.myapplication.LoginActivity;
import com.example.yucren.myapplication.MainBottomActivity;
import com.example.yucren.myapplication.kanban.Kanban;
import com.example.yucren.myapplication.kanban.KanbanPD;
import com.example.yucren.myapplication.tools.version.Verson;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UpdataTool {


    public static  void getRemoteVersion (Activity context) {

         new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                String result = "";
                try {
                    String replace = "<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">";
                    url = new URL("http://yu539928505.imwork.net/androidApp/output.json");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(in);
                        String inputLine = "";
                        while ((inputLine = bufferedReader.readLine()) != null) {
                            result += inputLine + "\n";
                            result = result.replace(replace, "").replace("</string>", "");

                        }
                        in.close();
                    }
                    Gson gson = new Gson();
                    JsonParser jsonParser = new JsonParser();
                    List<Verson> versonList =new ArrayList<>();
                    JsonArray jsonArray = jsonParser.parse(result).getAsJsonArray();
                    for (JsonElement book : jsonArray) {
                        Verson verson =gson.fromJson(book,Verson.class);
                        versonList.add(verson);
                    }
                    urlConnection.disconnect();

                  int versionCheck = compareVersion(versonList.get(0).getApkInfo().getVersionName(),getVersionName(context));
                  if (versionCheck ==1)
                  {
                      context.runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                             new AlertDialog.Builder(context).setTitle("新版本提示").setMessage("检测到新版本：" + versonList.get(0).getApkInfo().getVersionName()).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialogInterface, int i) {
                                     Toast.makeText(context,"开始更新，请耐心等待",Toast.LENGTH_LONG).show();
                                     downApp(context,"http://yu539928505.imwork.net/androidApp/SHJXMES.apk");
                                 }
                             }).setCancelable(false).show();
                          }
                      });
                  }else if (versionCheck==0)
                  {
                      LoginActivity.isNew=true;

//                     context.runOnUiThread(new Runnable() {
//                                                                      @Override
//                                                                      public void run() {
//
//                                                                          Toast.makeText(context,"已经是最新版本",Toast.LENGTH_LONG).show();
//                                                                      }
//                                                                  }
//                      );
                                                              }





                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }



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
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"shjxmes.apk");
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
