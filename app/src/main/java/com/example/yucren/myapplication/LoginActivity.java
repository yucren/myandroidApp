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
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import static android.widget.EditText.*;

public class LoginActivity extends AppCompatActivity {
    public  static Kanban kanban =null;
    private EditText loginET;
    private Handler handler = new Handler();
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        IntentIntegrator in = new IntentIntegrator(LoginActivity.this);
        android.app.AlertDialog dialog = in.initiateScan();
        type="login";

        Button scanbtn = (Button)findViewById(R.id.scanbtn);
        scanbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
           LoginActivity.this.type="scan";
           IntentIntegrator in = new IntentIntegrator(LoginActivity.this);
           android.app.AlertDialog dialog = in.initiateScan();
            }
        });
        SmartTable table = (SmartTable)findViewById(R.id.scanTable);
        ArrayList<DtItems> dtItems =  new ArrayList<DtItems>();
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowYSequence(false);
        table.getConfig().setFixedTitle(true);
        table.getConfig().setShowColumnTitle(true);
//     dtItems.add(new DtItems(110,"001001718","发动机goodluc1111111k",24));
        table.setData(dtItems);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                final String content = result.getContents();
                kanban.setBoardNo(content);
                loadData(content, LoginActivity.this.type);
            }
        }
        catch(Exception e)
            {
                e.printStackTrace();
            }


           // login(content);



            // handle scan result

    }
    private  void login (final String upcontent)
    {
        new Thread(new Runnable() {
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
                    final String loginUser = kanban.getLogin_user();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,loginUser,Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
                        url = new URL("http://yu539928505.imwork.net/StartScan");
                    }
                    else if (type =="scan")
                    {
                        url = new URL("http://yu539928505.imwork.net/Scan");
                    }
                    else if (type =="submit")
                    {
                        url = new URL("http://yu539928505.imwork.net/Submit");
                    }
                    else if (type=="recycle")
                    {
                        url = new URL("http://yu539928505.imwork.net/recycle");
                    }
                    else {
                        url=new URL("");
                    }
                    HttpURLConnection urlConnection  =(HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    DataOutputStream out =new DataOutputStream(urlConnection.getOutputStream());
                    Gson gson =new Gson();
                    String param =gson.toJson(new Kanban(),Kanban.class);
                    out.writeBytes(param);
                    out.flush();
                    out.close();
                    if (urlConnection.getResponseCode()== HttpURLConnection.HTTP_OK)
                    {
                        InputStreamReader in =new InputStreamReader(urlConnection.getInputStream());
                        BufferedReader bufferedReader =new BufferedReader(in);
                        String inputLine ="";
                        while ((inputLine=bufferedReader.readLine())!=null) {
                            result += inputLine +"\n";
                            result =result.replace(replace,"").replace("</string>","");
                            in.close();
                        }
                    }
                    urlConnection.disconnect();
                    Kanban kanban =     gson.fromJson(result,Kanban.class);
                    final String loginUser = kanban.getLogin_user();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,loginUser,Toast.LENGTH_LONG).show();
                        }
                    });

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

                catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }}).start();
    }
}
