package com.example.yucren.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.yucren.myapplication.kanban.Kanban;
import com.example.yucren.myapplication.tools.UpdataTool;
import com.google.gson.Gson;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends BaseActivity {
    public static final String TAG_EXIT = "exit";
    public Kanban kanban = new Kanban();
    public Handler handler = new Handler();
    public static String address="";
    public  static  boolean isNew;
    private SharedPreferences config;
    private int REQUEST_CODE_SCAN = 111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UpdataTool.getRemoteVersion(this);
        config =getSharedPreferences("loginConfig", MODE_PRIVATE);
        String data =config.getString("loginInfo","");
        isNew =true;
        if (!data.equals(""))
        {
            try {
                login(data);
                Intent intent = new Intent(getApplicationContext(), MainBottomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("kanban", kanban);
                intent.putExtras(bundle);
                startActivity(intent);
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if (intent != null) {
//            boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
//            if (isExit) {
//                this.finish();
//            }
//        }
//
//    }

    private void InitialScan() {
        int i =ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (i!=PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE },1);
            return;
        }
        Intent intent = new Intent(LoginActivity.this, CaptureActivity.class);
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);//是否播放扫描声音 默认为true
        config.setShake(true);//是否震动  默认为true
        config.setDecodeBarCode(false);//是否扫描条形码 默认为true
        config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
        config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
        config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1)
        {
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                InitialScan();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        super.onActivityResult(requestCode,resultCode,intent);
        try {
            if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
                if (intent != null) {
                    final String content = intent.getStringExtra(Constant.CODED_CONTENT);
                    login(content);
                    if (kanban.getLogin_user() != null && !kanban.getLogin_user().equals("")) {
                        final String loginUser = kanban.getLogin_user();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登陆成功，" + kanban.getLogin_user(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainBottomActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("kanban", kanban);
                                intent.putExtras(bundle);
                                 config.edit().putString("loginInfo",content).apply();

                                startActivity(intent);

                            }
                        });
                    } else {
                        InitialScan();
                    }
                }
            } else {
              //  login("001001718,BEFFA6C0D5363F6B44C0AF4029E17DB3,俞程仁,信息部");
                if (kanban.getLogin_user() != null && !kanban.getLogin_user().equals("")) {
                    final String loginUser = kanban.getLogin_user();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "登陆成功，" + kanban.getLogin_user(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainBottomActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("kanban", kanban);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            //  InitialScan();
                        }
                    });

                }
            }

        }
        catch (Exception e)
        {
            Log.e("err",e.getMessage());
        }


    }

    private void login(final String upcontent) throws InterruptedException {
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        Thread dd = new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                String result = "";
                try {
                    String replace = "<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">";
                    url = new URL("http://yu539928505.imwork.net/SHJXMESWCFServer/MESService.svc/login?info=" + upcontent);
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
                    urlConnection.disconnect();
                    kanban = gson.fromJson(result, Kanban.class);

                } catch (final MalformedURLException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        dd.start();
        dd.join();
    }

    public void loginApp(View view) {
        if (LoginActivity.isNew)
        {
            InitialScan();
        }
        else {
            Toast.makeText(this,"请升级到最新版本",Toast.LENGTH_LONG).show();
        }

      // String value =getData(5555);
     //  Toast.makeText(this,value,Toast.LENGTH_LONG).show();
//     new Thread(new Runnable() {
//         @Override
//         public void run() {
//             try {
//              final String value =   getRemoteInfo(5555);
//              runOnUiThread(new Runnable() {
//                  @Override
//                  public void run() {
//                      Toast.makeText(LoginActivity.this,value,Toast.LENGTH_LONG).show();
//                  }
//              });
//             } catch (final Exception e) {
//                 runOnUiThread(new Runnable() {
//                     @Override
//                     public void run() {
//                         Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
//                     }
//                 });
//             }
//         }
//     }).start();

                                           }


    public String getRemoteInfo(int value) throws Exception{
        String result ="";
        String WSDL_URI = "http://192.168.1.6/WcfService1/Service1.svc?wsdl";//wsdl 的uri
        String URL = "http://192.168.1.6/WcfService1/Service1.svc";
        String SOAP_ACTION = "http://tempuri.org/IService1/GetData";
        String namespace = "http://tempuri.org/";//namespace
        String methodName = "GetData";//要调用的方法名称

        SoapObject request = new SoapObject(namespace, methodName);
        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
        request.addProperty("value", 5555);


        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.bodyOut = request;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
        httpTransportSE.call("http://tempuri.org/IService1/GetData", envelope);//调用

        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        // 获取返回的结果
        result = object.getProperty(0).toString();
        Log.d("debug",result);
        return result;

    }

    public  void  ConnWebSocketServer()
    {

    }


}
