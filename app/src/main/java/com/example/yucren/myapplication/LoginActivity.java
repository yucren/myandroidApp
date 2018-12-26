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

import com.example.yucren.myapplication.kanban.Kanban;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Key;

import static android.widget.EditText.*;

public class LoginActivity extends AppCompatActivity {
    private EditText loginET;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

                IntentIntegrator in = new IntentIntegrator(LoginActivity.this);
                android.app.AlertDialog dialog = in.initiateScan();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            final String content = result.getContents();

            ;



            // handle scan result
        }
    }

    private void loadData(final String upcontent) {


        new Thread(new Runnable() {

            @Override
            public void run() {
                URL url;
                String result="";
                try {
                    String replace ="<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">";
                    url = new URL("http://10.10.13.44/SHJXMESWCFServer/MESService.svc/login?info=" + upcontent);
                    HttpURLConnection urlConnection  =(HttpURLConnection) url.openConnection();
                    InputStreamReader in =new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader bufferedReader =new BufferedReader(in);
                    String inputLine ="";
                    while ((inputLine=bufferedReader.readLine())!=null) {
                        result += inputLine +"\n";

                    }
                    result =result.replace(replace,"").replace("</string>","");
                    in.close();
                    urlConnection.disconnect();
                    Gson gson =new Gson();
                    Kanban kanban =     gson.fromJson(result,Kanban.class);
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
            }}).start();
    }
}
