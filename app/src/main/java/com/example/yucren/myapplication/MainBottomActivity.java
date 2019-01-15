package com.example.yucren.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.table.MapTableData;
import com.example.yucren.myapplication.frame.EditTextPlus;
import com.example.yucren.myapplication.frame.FragmentOne;
import com.example.yucren.myapplication.frame.FragmentThree;
import com.example.yucren.myapplication.frame.FragmentTwo;
import com.example.yucren.myapplication.frame.KanbanpdAdapter;
import com.example.yucren.myapplication.kanban.Kanban;
import com.example.yucren.myapplication.kanban.KanbanPD;
import com.example.yucren.myapplication.recevice.NetworkChangeReceiver;
import com.example.yucren.myapplication.tools.UpdataTool;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class MainBottomActivity extends BaseActivity {
    public static TreeSet<Float> treeSet1 = new TreeSet<>();
    public static TreeSet<Float> treeSet2 = new TreeSet<>();
    public static TreeSet<Float> treeSet3 = new TreeSet<>();
    public static TreeSet<Float> treeSet4 = new TreeSet<>();
    public static TreeSet<Float> treeSet5 = new TreeSet<>();
    public static TreeSet<Float> treeSet6 = new TreeSet<>();
    public  static boolean initStart=true;
    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;
    private FragmentThree fragmentThree;
    public  static Kanban kanban =new Kanban();
    public  static  String type;
    private  IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    public Handler handler = new Handler();
    private Handler handler1 =new Handler( ){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result =(String)msg.obj;
            Toast.makeText(MainBottomActivity.this,result,Toast.LENGTH_LONG).show();
        }
    };

    public  void  click(View v)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = "helloworld";
                Message message =new Message();
                message.obj = result;
                handler.sendMessage(message);
            }
        }).start();
    }
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

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bottom);
        intentFilter =new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver  =new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);
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
//        Toast.makeText(this,"你又回来了",Toast.LENGTH_LONG).show();
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_seetings)
        {

        }
        if (id == R.id.updateApp)
        {
            UpdataTool.getRemoteVersion(this);
        }
        if (id== R.id.exitMenu)
        {
            Intent intent = new Intent();
            intent.setAction("ExitApp");

            this.sendBroadcast(intent);
            super.finish();
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
        config.setDecodeBarCode(false);//是否扫描条形码 默认为true
        config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
        config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
        config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
        config.setFullScreenScan(true);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
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
                           loadpd(content);
                       }
                   }else if (type=="pd")
                   {
                       loadpd(content);
                   }

                   else {
                       kanban.setBoardNo(content);
                       fragmentOne.kanbano.setText(content);
                       loadData(content, MainBottomActivity.this.type);
                   }
               }
           }
           else  {
               loadpd("");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fragmentTwo.submitpdBtn.setEnabled(false);
                        Toast.makeText(MainBottomActivity.this,"扫描内容获取失败，请确认二维码是否正确,请重新扫描",Toast.LENGTH_LONG).show();

                    }
                });



           }
       }catch (Exception e)
       {
           Log.e("err",e.getMessage());
       }

   }
    public  void submitpd(List<KanbanPD> pdList)
    {
        new Thread(new Runnable() {

            @Override
            public void run() {
                URL url;
                String result="";
                try {
                    String replace ="<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">";
                    url=new URL("http://yu539928505.imwork.net/SHJXMESWCFServer/MESService.svc/submitpd");
                    HttpURLConnection urlConnection  =(HttpURLConnection) url.openConnection();
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
                    String param =gson.toJson(pdList);
                    out.write(param.getBytes());
                    out.flush();
                    out.close();
                    if (urlConnection.getResponseCode()== HttpURLConnection.HTTP_OK) {
                        InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(in);
                        String inputLine = "";
                        while ((inputLine = bufferedReader.readLine()) != null) {
                            result += inputLine;
                          //  result = result.replace(replace, "").replace("</string>", "");
                        }
                        in.close();
                        bufferedReader.close();
                        urlConnection.disconnect();
                        boolean d =result.equals("<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\"/>");
                        if (d)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((KanbanpdAdapter)(fragmentTwo.gridView.getAdapter())).clear();
                                    Toast.makeText(getApplicationContext(),"盘点成功" ,Toast.LENGTH_LONG).show();
                                    ((KanbanpdAdapter) fragmentTwo.gridView.getAdapter()).notifyDataSetChanged();
                                    fragmentTwo.submitpdBtn.setEnabled(false);

                                }
                            });
                        }
                        else {
                            final String finalResult = result;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"存在如下错误:" + finalResult,Toast.LENGTH_LONG).show();
                                }
                            });

                        }

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
    public  void loadpd( String kanbanNo) throws InterruptedException {

      new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String result ="";
                    String replace ="<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">";
                    URL url = new URL("http://yu539928505.imwork.net/SHJXMESWCFServer/MESService.svc/pd?no=" + kanbanNo);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(20000);
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("Charset", "UTF-8");
                    urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                    final Gson gson = new GsonBuilder().serializeNulls().create();
                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
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
                        JsonParser jsonParser = new JsonParser();
                        JsonArray jsonArray = jsonParser.parse(result).getAsJsonArray();
                        Gson gson2 = new GsonBuilder().enableComplexMapKeySerialization().create();
                        List<KanbanPD> maplist = new ArrayList<>();
                        for (JsonElement book : jsonArray) {
                            KanbanPD kanbanPD =gson.fromJson(book,KanbanPD.class);
                            maplist.add(kanbanPD);
                        }
                        Paint paint =new Paint();
                        paint.setTextSize(50);
                        treeSet1.clear();
                        treeSet2.clear();
                        treeSet3.clear();
                        treeSet4.clear();
                        treeSet5.clear();
                        treeSet6.clear();
                        for (KanbanPD pd:maplist) {
                            treeSet1.add(paint.measureText(pd.getFItemCode()));
                            treeSet2.add(paint.measureText(pd.getFItemName()));
                            treeSet3.add(paint.measureText(pd.getFModel()));
                            treeSet4.add(paint.measureText(pd.getFName()));
                        }
                        treeSet5.add(paint.measureText("盘点数量"));
                        treeSet6.add(paint.measureText("库存数量"));
                      runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                         fragmentTwo.kanbannoTv.setText("看板编号:" + kanbanNo);
                                         if (maplist.size()==1)
                                         {
                                             fragmentTwo.submitpdBtn.setEnabled(false);
                                         }
                                         else {
                                             fragmentTwo.submitpdBtn.setEnabled(true);
                                         }

                                        //    fragmentTwo.gridView.setVisibility(View.INVISIBLE);
                                            fragmentTwo.gridView.setAdapter(new KanbanpdAdapter(MainBottomActivity.this, maplist));
                                        }
                                    });
//                             new Thread(new Runnable() {
//                                  @Override
//                                  public void run() {
//                                      try {
//                                     //    Thread.sleep(100);
//                                       //  getCountClick();
//
//                                      // Thread.sleep(100);
//                                        //  getCountClick();
//                                          runOnUiThread(new Runnable() {
//                                              @Override
//                                              public void run() {
//                                                  fragmentTwo.gridView.setVisibility(View.VISIBLE);
//                                              }
//                                          });
//
//
//                                      } catch (Exception e) {
//                                          e.printStackTrace();
//                                      }
//
//                                  }
//                              }).start();
//
//
//
//                     }
//                      });


                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();







    }
   public  void loadInv(final String upcontent) throws InterruptedException {
         final List<Object> maplist =new ArrayList<>();
         new Thread(new Runnable() {
           @Override
          public void run() {
               try {
                   String result ="";
                   String replace ="<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">";
                   URL url = new URL("http://yu539928505.imwork.net/SHJXMESWCFServer/MESService.svc/getinv?no=" + upcontent);
                   HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                   urlConnection.setConnectTimeout(5000);
                   urlConnection.setReadTimeout(20000);
                   urlConnection.setUseCaches(false);
                   urlConnection.setRequestProperty("Charset", "UTF-8");
                   urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                   urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                   final Gson gson = new GsonBuilder().serializeNulls().create();
                   if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
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

                       JsonParser jsonParser = new JsonParser();
                       JsonArray jsonArray = jsonParser.parse(result).getAsJsonArray();
                       Gson gson2 = new GsonBuilder().enableComplexMapKeySerialization().create();
                       for (JsonElement book : jsonArray) {
                           KanbanPD kanbanPD =gson.fromJson(result,KanbanPD.class);
                           maplist.add(kanbanPD);
                       }
                       final MapTableData tableData = MapTableData.create("目录", maplist);
                       SmartTable smartTable =fragmentThree.smartTable;

                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               fragmentThree.smartTable.setTableData(tableData);
                           }
                       });

                   }
               } catch (ProtocolException e) {
                   e.printStackTrace();
               } catch (MalformedURLException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       });



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
                    else if (type=="inv")
                    {
                        url = new URL("http://yu539928505.imwork.net/SHJXMESWCFServer/MESService.svc/getinv");
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
                        if (type=="inv")
                        {
                            JsonParser jsonParser =new JsonParser();
                            JsonArray jsonArray = jsonParser.parse(result).getAsJsonArray();
                     Gson gson2 = new GsonBuilder().enableComplexMapKeySerialization().create();
                     Type type = new TypeToken<Map<String, String>>() {}.getType();
                            List<Object> maplist =new ArrayList<>();
                            for (JsonElement book  : jsonArray)
                            {

                                 Map<String, String> map2 = gson2.fromJson(book,type);
                                 maplist.add(map2);
                            }
                            MapTableData tableData =MapTableData.create("目录",maplist);
                            fragmentThree.smartTable .setTableData(tableData);
                            return;
                        }
                        kanban = gson.fromJson(result, Kanban.class);
                        if (kanban.getErr() == null || kanban.getErr() .equals("")) {

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
                                        fragmentOne.submitBtn.setEnabled(false);

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



    public void getCountClick() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                ListView listView = fragmentTwo.gridView;
                for (int i = 0; i < listView.getChildCount(); i++) {
                    LinearLayout  view1 = (LinearLayout) listView.getChildAt(i);
                    TextView itemCodeTv = (TextView)view1.findViewById(R.id.itemCode);
                    itemCodeTv.setWidth(Math.round(treeSet1.last()));
                    TextView itemNameTv =(TextView)view1.findViewById(R.id.itemName);
                    itemNameTv.setWidth(Math.round(treeSet2.last()));
                    TextView processTv =(TextView)view1.findViewById(R.id.fName);
                    processTv.setWidth(Math.round(treeSet3.last()));
                    TextView itemModel =(TextView)view1.findViewById(R.id.fModel);
                    itemModel.setWidth(Math.round(treeSet4.last()));
                    TextView fcout =(TextView)view1.findViewById(R.id.fcount);
                    fcout.setWidth(Math.round(treeSet5.last()));
                    EditTextPlus pdCount =(EditTextPlus) view1.findViewById(R.id.fdCount);
                    pdCount.setWidth(Math.round(treeSet6.last()));
                }
                listView.getLayoutParams().width=Math.round(treeSet1.last()+treeSet2.last()+treeSet3.last()+treeSet4.last()+treeSet5.last()+ treeSet6.last()+90);

            }
        });


    }


    public void submitClick(View view) {

        String submitpdData ="";
       List<KanbanPD> pdList =   ((KanbanpdAdapter)fragmentTwo.gridView.getAdapter()).pds;
       final List<KanbanPD> submitList=new ArrayList<>();
       int count =0;
       for (KanbanPD pd:pdList)
       {

           if (count==0)
           {
               submitpdData ="以下物料确认盘点\n";
               count +=1;
           }
           else if (pd.getFcount() != pd.getFDCount()){
               submitList.add(pd);
               submitpdData += count +":" + pd.getFItemCode() +"," + pd.getFItemName() + "," +pd.getFName() + ":盘点数量" + pd.getFDCount() +"\n" ;
               count +=1;
           }



       }
        if (submitList.size() !=0)
        {
            new  AlertDialog.Builder(this).setTitle("盘点确认").setMessage(submitpdData).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    submitpd(submitList);
                }


            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();

        }
        else {
            Toast.makeText(this,"没有盘点数量，请确认",Toast.LENGTH_LONG).show();
        }




    }
}

@SuppressLint("AppCompatCustomView")
class  TextViewEx extends  TextView
{

    public TextViewEx(Context context) {
        super(context);
    }

    public TextViewEx(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewEx(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TextViewEx(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(300,measureHeight(heightMeasureSpec));
    }
    private int measureWidth(int widthMeasureSpec) {
        int result = 0;
        int spaceMode = MeasureSpec.getMode(widthMeasureSpec);
        int spaceSize = MeasureSpec.getSize(widthMeasureSpec);

        if (spaceMode == MeasureSpec.EXACTLY){
            result = spaceSize;
        }else{
            result = 200;//设置宽度默认值
            if (spaceMode == MeasureSpec.AT_MOST){
                result = Math.min(result,spaceSize);
            }
        }
        return result;
    }

    /**
     * 获得实际宽度
     * @param heightMeasureSpec 带模式和值的高度spec对象
     * @return 实际测量值
     */
    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else {
            result = 50;//设置高度默认值
            if (specMode == MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return result;
    }
}

