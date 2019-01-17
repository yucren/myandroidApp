package com.example.yucren.myapplication.frame;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.example.yucren.myapplication.MainBottomActivity;
import com.example.yucren.myapplication.R;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.StringCallback;

import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by yucren on 2018-12-30.
 */

public class FragmentThree extends Fragment {
    MainBottomActivity mainBottomActivity;
   public  SmartTable smartTable;
   public  Future<WebSocket> webSocketFuture;
   public  WebSocket webSocket;
   public Button sendBtn;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_three, container, false);
        mainBottomActivity = (MainBottomActivity)inflater.getContext();
        Button button = (Button) view.findViewById(R.id.getinvBtn);
        sendBtn =(Button)view.findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

           Socket socket = (Socket) webSocket.getSocket();

                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String s) {
                        mainBottomActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mainBottomActivity,s,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                webSocket.send("a string");
                webSocket.send(new byte[10]);


            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



             webSocketFuture = AsyncHttpClient.getDefaultInstance().websocket("ws://192.168.60.3/WebSocketsSample/ws.ashx?name=yuchengren", "80", new AsyncHttpClient.WebSocketConnectCallback() {


                 @Override
                    public void onCompleted(Exception ex, WebSocket webSocket) {
                        if (ex != null) {
                            ex.printStackTrace();
                            return;
                        }
                        FragmentThree.this.webSocket = webSocket;
                        webSocket.send("a string");
                        webSocket.send(new byte[10]);
                       
                        webSocket.setStringCallback(new WebSocket.StringCallback() {
                            @Override
                            public void onStringAvailable(String s) {
                                mainBottomActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mainBottomActivity,s,Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        });
                        webSocket.setDataCallback(new DataCallback() {
                            public void onDataAvailable(DataEmitter emitter, ByteBufferList byteBufferList) {
                                System.out.println("I got some bytes!");
                                // note that this data has been read
                                byteBufferList.recycle();
                            }
                        });
                    }
                });




//                try {
//                    mainBottomActivity.loadInv("S00001");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//               WebView webView= (WebView) view.findViewById(R.id.webBrowser);
//            webView.setWebChromeClient(new WebChromeClient(){
//                @Override
//                public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//
//
//                    return  false;
//                }
//            });
//            webView.getSettings().setJavaScriptEnabled(true);
//
//                webView.loadUrl("file:///android_asset/index.html");
//
//            }
           // }

//        smartTable =(SmartTable)view.findViewById(R.id.invtable);
//        mainBottomActivity = (MainBottomActivity) getActivity();
//        smartTable.getConfig().setShowXSequence(false);
//        smartTable.getConfig().setShowYSequence(false);
//        smartTable.getConfig().setFixedTitle(true);
//        smartTable.getConfig().setShowColumnTitle(true);

        }}
        );
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }
}
