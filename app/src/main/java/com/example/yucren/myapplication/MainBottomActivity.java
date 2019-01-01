package com.example.yucren.myapplication;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yucren.myapplication.frame.FragmentOne;
import com.example.yucren.myapplication.frame.FragmentThree;
import com.example.yucren.myapplication.frame.FragmentTwo;
import com.example.yucren.myapplication.kanban.Kanban;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class MainBottomActivity extends BaseActivity {
    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;
    private FragmentThree fragmentThree;
    public  static Kanban kanban =new Kanban();
    public  static  String type;

    public Handler handler = new Handler();
    private int REQUEST_CODE_SCAN = 111;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showNav(R.id.navigation_home);
                    return true;
                case R.id.navigation_dashboard:
                    showNav(R.id.navigation_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    showNav(R.id.navigation_notifications);
                    return true;



            }
            return false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode ==KeyEvent.KEYCODE_BACK && event.getRepeatCount() !=0)
        {
            new AlertDialog.Builder(this).setMessage("长按退出扫描程序").setTitle("退出提示").setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myExit();
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();


            return true;

        }
        else  if (keyCode ==KeyEvent.KEYCODE_BACK && event.getRepeatCount() ==0)
            {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);

                return  true;
            }
            else {
            return super.onKeyDown(keyCode, event);
        }

    }
    protected void myExit() {
        Intent intent = new Intent();
        intent.setAction("ExitApp");

        this.sendBroadcast(intent);
        super.finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bottom);
        init();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        kanban = (Kanban) bundle.getSerializable("kanban");
        ActionBar supportActionBar =getSupportActionBar();
        supportActionBar.setTitle("MES手机客户端");
        supportActionBar.setSubtitle("登陆用户：" +kanban.getLogin_user());

        setTitle(kanban.getLogin_user());
        Toast.makeText(this,"你又回来了",Toast.LENGTH_LONG).show();
//        type="login";
//        AlertDialog.Builder builder =new AlertDialog.Builder(this);
//        builder.setTitle("提示");
//        builder.setMessage("扫描前请先登陆");
//        builder.setIcon(R.drawable.ic_launcher_background);
//        builder.setCancelable(false);
//        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//                InitialScan();
//
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                MainBottomActivity.this.finish();
//            }
//        });
//        Dialog dialog =builder.create();
//        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_seetings)
        {

        }
        if (id==R.id.user)
        {
        AlertDialog.Builder  builder = new AlertDialog.Builder(this).setTitle("登陆用户信息").setIcon(R.mipmap.ic_launcher_round);
            LayoutInflater inflater =getLayoutInflater();
            View layout = View.inflate(this,R.layout.dialog_user,null);
            builder.setView(layout);
            TextView userName =(TextView)layout.findViewById(R.id.userName);
            TextView userDepart =(TextView)layout.findViewById(R.id.departName);
            TextView userRole =(TextView)layout.findViewById(R.id.roleName);
            userName.setText(kanban.getLogin_user());
            userDepart.setText(kanban.getUserDptName());
            userRole.setText(kanban.getRole());
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar,menu);
        return  true;
    }

    private void init(){
        fragmentOne=new FragmentOne();
        fragmentTwo=new FragmentTwo();
        fragmentThree=new FragmentThree();
        FragmentTransaction beginTransaction=getSupportFragmentManager().beginTransaction();
        beginTransaction.add(R.id.content,fragmentOne).add(R.id.content,fragmentTwo).add(R.id.content,fragmentThree);//开启一个事务将fragment动态加载到组件
        beginTransaction.hide(fragmentOne).hide(fragmentTwo).hide(fragmentThree);//隐藏fragment
       // beginTransaction.addToBackStack(null);//返回到上一个显示的fragment
        beginTransaction.commit();//每一个事务最后操作必须是commit（），否则看不见效果
        showNav(R.id.navigation_home);
    }

    private void showNav(int navid){
        FragmentTransaction beginTransaction=getSupportFragmentManager().beginTransaction();
        switch (navid){
            case R.id.navigation_home:
                beginTransaction.hide(fragmentTwo).hide(fragmentThree);
                beginTransaction.show(fragmentOne);
                //beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                break;
            case R.id.navigation_dashboard:
                beginTransaction.hide(fragmentOne).hide(fragmentThree);
                beginTransaction.show(fragmentTwo);
               // beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                break;
            case R.id.navigation_notifications:
                beginTransaction.hide(fragmentTwo).hide(fragmentOne);
                beginTransaction.show(fragmentThree);
               // beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                break;
        }
    }

    public   void InitialScan()
    {
        Intent intent = new Intent(this, CaptureActivity.class);
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);//是否播放扫描声音 默认为true
        config.setShake(true);//是否震动  默认为true
        config.setDecodeBarCode(true);//是否扫描条形码 默认为true
        config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
        config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
        config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }
    private  void login (final String upcontent) throws InterruptedException {
        try{

        Thread dd =  new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                String result="";
                try {
                    String replace ="<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">";
                    url = new URL("http://yu539928505.imwork.net/SHJXMESWCFServer/MESService.svc/login?info=" + upcontent);
                    HttpURLConnection urlConnection  =(HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    if (urlConnection.getResponseCode()== HttpURLConnection.HTTP_OK)
                    {
                        InputStreamReader in =new InputStreamReader(urlConnection.getInputStream());
                        BufferedReader bufferedReader =new BufferedReader(in);
                        String inputLine ="";
                        while ((inputLine=bufferedReader.readLine())!=null) {
                            result += inputLine +"\n";
                            result =result.replace(replace,"").replace("</string>","");

                        }
                        in.close();
                    }
                    Gson gson =new Gson();
                    urlConnection.disconnect();
                    kanban =     gson.fromJson(result,Kanban.class);

                } catch (final MalformedURLException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainBottomActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                } catch ( final  IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainBottomActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
                catch ( final Exception e)
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainBottomActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        dd.start();
        dd.join();
    }catch (Exception e)
        {
            final String err = e.getMessage();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainBottomActivity.this,"发生错误" + err,Toast.LENGTH_LONG);
                }
            });
        }

    }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
       super.onActivityResult(requestCode,resultCode,intent);
       try {
           if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
               if (intent != null) {
                   final String content = intent.getStringExtra(Constant.CODED_CONTENT);
                   if (type == "login") {
                       login(content);
                       if (kanban.getLogin_user() != null && !kanban.getLogin_user().equals("")) {
                           final String loginUser = kanban.getLogin_user();
                           handler.post(new Runnable() {
                               @Override
                               public void run() {
                                   MainBottomActivity.this.setTitle("看板扫描,扫描人员：" + kanban.getLogin_user());
                                   Toast.makeText(MainBottomActivity.this, "登陆成功，" + kanban.getLogin_user(), Toast.LENGTH_LONG).show();
                                   fragmentOne.scanbtn.setEnabled(true);

                               }
                           });
                       } else {
                           MainBottomActivity.this.type = "login";
                           InitialScan();
                       }
                   } else {
                       kanban.setBoardNo(content);
                       fragmentOne.kanbano.setText(content);
                       loadData(content, MainBottomActivity.this.type);
                   }
               }
           }
           else  {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainBottomActivity.this,"扫描内容获取失败，请确认二维码是否正确,请重新扫描",Toast.LENGTH_LONG).show();

                    }
                });



           }
       }catch (Exception e)
       {
           Log.e("err",e.getMessage());
       }

   }
    public void loadData(final String upcontent, final String type) {


        new Thread(new Runnable() {

            @Override
            public void run() {
                URL url;
                String result="";
                try {
                    String replace ="<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">";
                    if (type =="login")
                    {
                        url = new URL("http://yu539928505.imwork.net/SHJXMESWCFServer/MESService.svc/login?info=" + upcontent);
                    }
                    else  if (type =="startScan")
                    {
                        url = new URL("http://yu539928505.imwork.net/SHJXMESWCFServer/MES.svc/StartScan");

                    }
                    else if (type =="scan")
                    {
                        kanban =kanban.initialKanban();
                        url = new URL("http://yu539928505.imwork.net/SHJXMESWCFServer/MES.svc/Scan");

                    }
                    else if (type =="submit")
                    {
                        url = new URL("http://yu539928505.imwork.net/SHJXMESWCFServer/MES.svc/Submit");
                    }
                    else if (type =="out")
                    {
                        url = new URL("http://yu539928505.imwork.net/SHJXMESWCFServer/MES.svc/out");
                    }
                    else if (type=="recycle")
                    {
                        url = new URL("http://yu539928505.imwork.net/SHJXMESWCFServer/MES.svc/recycle");
                    }
                    else {
                        url=new URL("");
                    }
                    final HttpURLConnection urlConnection  =(HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("Charset","UTF-8");
                    urlConnection.setRequestProperty("Accept-Charset","UTF-8");
                    urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
                    DataOutputStream out =new DataOutputStream(urlConnection.getOutputStream());
                    final Gson gson =new GsonBuilder().serializeNulls().create();
                    String param =gson.toJson(kanban,Kanban.class);
                    out.write(param.getBytes());
                    out.flush();
                    out.close();
                    if (urlConnection.getResponseCode()== HttpURLConnection.HTTP_OK) {
                        InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(in);
                        String inputLine = "";
                        while ((inputLine = bufferedReader.readLine()) != null) {
                            result += inputLine + "\n";
                            result = result.replace(replace, "").replace("</string>", "");
                        }
                        in.close();
                        bufferedReader.close();
                        urlConnection.disconnect();
                        kanban = gson.fromJson(result, Kanban.class);
                        if (kanban.getErr() == null || kanban.getErr() == "") {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fragmentOne.curtv.setText(kanban.getStatusname());
                                    fragmentOne.nextv.setText(kanban.getNext_statusname());
                                    fragmentOne.table.setData(kanban.getDtItems());
                                    if (kanban.getStatusname().equals("开工")) {
                                        fragmentOne.startBtn.setEnabled(true);
                                        fragmentOne.recycleBtn.setEnabled(false);
                                        fragmentOne.submitBtn.setEnabled(false);
                                        fragmentOne.waitBtn.setEnabled(false);
                                    } else if (kanban.getStatusname().equals("出库")) {
                                        fragmentOne.startBtn.setEnabled(false);
                                        fragmentOne.submitBtn.setEnabled(false);
                                        fragmentOne.recycleBtn.setEnabled(false);
                                        fragmentOne.waitBtn.setEnabled(true);
                                    } else if (kanban.getStatusname().equals("待回收")) {
                                        fragmentOne.startBtn.setEnabled(false);
                                        fragmentOne.submitBtn.setEnabled(false);
                                        fragmentOne.waitBtn.setEnabled(false);
                                        fragmentOne.recycleBtn.setEnabled(true);
                                    } else if (kanban.getStatusname().equals("")) {
                                        fragmentOne.startBtn.setEnabled(false);
                                        fragmentOne.recycleBtn.setEnabled(false);
                                        fragmentOne.waitBtn.setEnabled(false);
                                        fragmentOne.waitBtn.setEnabled(false);

                                    } else {
                                        fragmentOne.submitBtn.setEnabled(true);
                                        fragmentOne.startBtn.setEnabled(false);
                                        fragmentOne.recycleBtn.setEnabled(false);
                                        fragmentOne.waitBtn.setEnabled(false);

                                    }

                                }
                            });





                        }



                        else{
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainBottomActivity.this,kanban.getErr().toString(),Toast.LENGTH_LONG).show();
                                }
                            });

                        }
//                         if (type !="scan" && type !="login")
//                         {
//                             kanban=null;
//                         }

                    }
                    else {
                        InputStreamReader in = new InputStreamReader(urlConnection.getErrorStream());
                        BufferedReader bufferedReader = new BufferedReader(in);
                        String inputLine = "";
                        String err="";
                        while (( inputLine = bufferedReader.readLine()) != null) {
                            err += inputLine + "\n";
                        }
                        in.close();
                        bufferedReader.close();
                        urlConnection.disconnect();
                        final String finalInputLine = err;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainBottomActivity.this, finalInputLine, Toast.LENGTH_LONG).show();

                            }
                        });
                    }


                }
                catch (SocketTimeoutException e)
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainBottomActivity.this,"连接超时",Toast.LENGTH_LONG).show();
                        }
                    });
                }

                catch (final MalformedURLException e) {
                    handler.post( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainBottomActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            Log.v("v",e.getMessage());

                        }
                    });
                } catch ( final  IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainBottomActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            Log.v("v",e.getMessage());
                        }
                    });
                }
                catch (final Exception e)
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainBottomActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            Log.v("v",e.getMessage());
                        }
                    });
                }
            }}).start();
    }
    }

