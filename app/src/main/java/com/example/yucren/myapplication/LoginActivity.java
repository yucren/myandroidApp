package com.example.yucren.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.table.TableData;
import com.example.yucren.myapplication.kanban.DtItems;
import com.example.yucren.myapplication.kanban.Kanban;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import static android.widget.EditText.*;

public class LoginActivity extends AppCompatActivity {
    public  static Kanban kanban =new Kanban();
    private EditText loginET;
    private Handler handler = new Handler();
    private String type;
    private   Button scanbtn;
    private  TextView curtv;
    private TextView nextv;
    private  EditText kanbano;
    private  SmartTable table;
    private  Button startBtn;
    private  Button submitBtn;
    private  Button waitBtn;
    private  Button recycleBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        curtv =(TextView)findViewById(R.id.cutv2);
        nextv =(TextView)findViewById(R.id.instock);
        scanbtn = (Button)findViewById(R.id.scanbtn);
        kanbano =(EditText)findViewById(R.id.kanbanno);
        startBtn=(Button)findViewById(R.id.startBtn);
        submitBtn=(Button)findViewById(R.id.submitBtn);
        waitBtn =(Button) findViewById(R.id.waitBtn);
        recycleBtn  = (Button)findViewById(R.id.recycleBtn);
        startBtn.setEnabled(false);
        submitBtn.setEnabled(false);
        waitBtn.setEnabled(false);
        recycleBtn.setEnabled(false);
        scanbtn.setEnabled(false);
        startBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
             type="startScan";
                loadData("", LoginActivity.this.type);
            }
        });
        submitBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                type="submit";
                loadData("", LoginActivity.this.type);
            }
        });
        waitBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                type="out";
                loadData("", LoginActivity.this.type);
            }
        });
        recycleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                type="recycle";
                loadData("", LoginActivity.this.type);
            }
        });

        IntentIntegrator in = new IntentIntegrator(LoginActivity.this);
        android.app.AlertDialog dialog = in.initiateScan();
        type="login";

        scanbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
           LoginActivity.this.type="scan";
           IntentIntegrator in = new IntentIntegrator(LoginActivity.this);
           android.app.AlertDialog dialog = in.initiateScan();
            }
        });
        table = (SmartTable)findViewById(R.id.scanTable);
//        ArrayList<DtItems> dtItems =  new ArrayList<DtItems>();
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowYSequence(false);
        table.getConfig().setFixedTitle(true);
        table.getConfig().setShowColumnTitle(true);
//     dtItems.add(new DtItems(110,"001001718","发动机goodluc1111111k",24));
//        table.setData(dtItems);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                 final String content = result.getContents();
                if (type=="login")
                {
                    login(content);
                    if (kanban.getLogin_user() != null && ! kanban.getLogin_user().equals(""))
                    {
                        final String loginUser = kanban.getLogin_user();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                LoginActivity.this.setTitle("看板扫描,扫描人员：" + kanban.getLogin_user()) ;
                                Toast.makeText(LoginActivity.this,"登陆成功，" + kanban.getLogin_user(),Toast.LENGTH_LONG).show();
                                scanbtn.setEnabled(true);
                            }
                        });
                    }
                    else {
                        LoginActivity.this.type="login";
                        IntentIntegrator in = new IntentIntegrator(LoginActivity.this);
                        android.app.AlertDialog dialog = in.initiateScan();
                    }
                }
                else
                {
                    kanban.setBoardNo(content);
                   kanbano.setText(content);
                    loadData(content, LoginActivity.this.type);
                }


            }
        }
        catch(Exception e)
            {
              Log.e("err", e.getMessage());
            }






            // handle scan result

    }
    private  void login (final String upcontent) throws InterruptedException {
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
                            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                } catch ( final  IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
                catch ( final Exception e)
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
       dd.start();
       dd.join();
    }
    private void loadData(final String upcontent,final String type) {


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

                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    curtv.setText(kanban.getStatusname());
                                    nextv.setText(kanban.getNext_statusname());
                                    table.setData(kanban.getDtItems());
                                    if (kanban.getStatusname().equals("开工")) {
                                        startBtn.setEnabled(true);
                                        recycleBtn.setEnabled(false);
                                        submitBtn.setEnabled(false);
                                        waitBtn.setEnabled(false);
                                    } else if (kanban.getStatusname().equals("出库")) {
                                        startBtn.setEnabled(false);
                                        submitBtn.setEnabled(false);
                                        recycleBtn.setEnabled(false);
                                        waitBtn.setEnabled(true);
                                    } else if (kanban.getStatusname().equals("待回收")) {
                                        startBtn.setEnabled(false);
                                        submitBtn.setEnabled(false);
                                        waitBtn.setEnabled(false);
                                        recycleBtn.setEnabled(true);
                                    } else if (kanban.getStatusname().equals("")) {
                                        startBtn.setEnabled(false);
                                        recycleBtn.setEnabled(false);
                                        waitBtn.setEnabled(false);
                                        waitBtn.setEnabled(false);

                                    } else {
                                       submitBtn.setEnabled(true);
                                        startBtn.setEnabled(false);
                                        recycleBtn.setEnabled(false);
                                        waitBtn.setEnabled(false);

                                    }

                                }
                            });





                    }



                            else{
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this,kanban.getErr().toString(),Toast.LENGTH_LONG).show();
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
                                Toast.makeText(LoginActivity.this, finalInputLine, Toast.LENGTH_LONG).show();

                            }
                        });
                    }


                }
                catch (SocketTimeoutException e)
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                       Toast.makeText(LoginActivity.this,"连接超时",Toast.LENGTH_LONG).show();
                        }
                    });
                }

                catch (final MalformedURLException e) {
                    handler.post( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            Log.v("v",e.getMessage());

                        }
                    });
                } catch ( final  IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            Log.v("v",e.getMessage());
                        }
                    });
                }
                catch (final Exception e)
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            Log.v("v",e.getMessage());
                        }
                    });
                }
            }}).start();
    }
}
