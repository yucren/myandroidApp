package com.example.yucren.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.example.yucren.myapplication.kanban.Kanban;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class MainBottomActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public  static Kanban kanban =null;
    private EditText loginET;
    private Handler handler = new Handler();
    private String type;
    private  TextView curtv;
    private TextView nextv;
    private  EditText kanbano;
    private SmartTable table;
    private Button startBtn;
    private  Button submitBtn;
    private  Button waitBtn;
    private  Button recycleBtn;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        curtv =(TextView)findViewById(R.id.cutv2);
        nextv =(TextView)findViewById(R.id.instock);
        kanbano =(EditText)findViewById(R.id.kanbanno);
        startBtn=(Button)findViewById(R.id.startBtn);
        submitBtn=(Button)findViewById(R.id.submitBtn);
        waitBtn =(Button) findViewById(R.id.waitBtn);
        recycleBtn  = (Button)findViewById(R.id.recycleBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="startScan";
                loadData("", MainBottomActivity.this.type);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="submit";
                loadData("", MainBottomActivity.this.type);
            }
        });
        waitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="recycle";
                loadData("", MainBottomActivity.this.type);
            }
        });
        recycleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="startScan";
                loadData("", MainBottomActivity.this.type);
            }
        });

        IntentIntegrator in = new IntentIntegrator(MainBottomActivity.this);
        android.app.AlertDialog dialog = in.initiateScan();
        type="login";
        Button scanbtn = (Button)findViewById(R.id.scanbtn);
        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainBottomActivity.this.type="scan";
                IntentIntegrator in = new IntentIntegrator(MainBottomActivity.this);
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
                    login("001001718,BEFFA6C0D5363F6B44C0AF4029E17DB3,俞程仁,信息部");
                }
                else
                {
                    kanban.setBoardNo(content);
                    kanbano.setText(content);
                    loadData(content, MainBottomActivity.this.type);
                }


            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }






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
                            Toast.makeText(MainBottomActivity.this,loginUser,Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final MalformedURLException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainBottomActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                } catch ( final IOException e) {
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

                            MainBottomActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    curtv.setText(kanban.getStatusname());
                                    nextv.setText(kanban.getNext_statusname());
                                    table.setData(kanban.getDtItems());


                                    if (kanban.getStatusname().equals("开工")) {
                                        recycleBtn.setEnabled(false);
                                        submitBtn.setEnabled(false);
                                        waitBtn.setEnabled(false);
                                    } else if (kanban.getStatusname().equals("待回收")) {
                                        startBtn.setEnabled(false);
                                        submitBtn.setEnabled(false);
                                        recycleBtn.setEnabled(false);
                                    } else if (kanban.getStatusname().equals("回收")) {
                                        startBtn.setEnabled(false);
                                        submitBtn.setEnabled(false);
                                        waitBtn.setEnabled(false);
                                    } else if (kanban.getStatusname().equals("")) {
                                        startBtn.setEnabled(false);
                                        recycleBtn.setEnabled(false);
                                        waitBtn.setEnabled(false);
                                        waitBtn.setEnabled(false);

                                    } else {

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