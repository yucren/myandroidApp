package com.example.yucren.myapplication;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView logtv = (TextView) findViewById(R.id.login);
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        logtv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator in = new IntentIntegrator(LoginActivity.this);
                android.app.AlertDialog dialog = in.initiateScan();
            }
        });
        loginET = (EditText) findViewById(R.id.loginInfo);
        loginET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //  Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_LONG).show();
            }
        });
        loginET.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (KeyEvent.KEYCODE_ENTER == i && KeyEvent.ACTION_DOWN == keyEvent.getAction()) {
                    //  Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_LONG).show();
                    return true;
                } else {

                    return false;
                }

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_R) {

        }
        return super.onKeyDown(keyCode, event);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String content = result.getContents();
            loginET.setText(content);

            // handle scan result
        }
    }

    private void login(String content) {
        URL url;
        String result = "";
        try {
            String replace = "<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">";
            url = new URL("http://yu539928505.imwork.net/SHJXMESWCFServer/MESService.svc/login?info=" + content);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            String inputLine = "";
            while ((inputLine = bufferedReader.readLine()) != null) {
                result += inputLine + "\n";

            }
            result = result.replace(replace, "").replace("</string>", "");
            in.close();
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
