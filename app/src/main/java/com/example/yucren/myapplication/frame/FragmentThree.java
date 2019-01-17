package com.example.yucren.myapplication.frame;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.StringCallback;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
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
   private  AsyncHttpClient.WebSocketConnectCallback webSocketConnectCallback;
    View view;
    private String address = "ws://yuchengren.oicp.io:38379/WebSocketsSample/ws.ashx?name=yuchengren";
    private URI uri;
    private static final String TAG = "JavaWebSocket";
    private WebSocketClient mWebSocketClient =null;

    public void initSockect() {
        try {
            uri = new URI(address);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (null == mWebSocketClient) {
            mWebSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    mWebSocketClient.send("hello yuchengren");
                    mainBottomActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mainBottomActivity,"open",Toast.LENGTH_LONG).show();
                        }
                    });
                }
                @Override
                public void onMessage(String s) {
                    mainBottomActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mainBottomActivity,s,Toast.LENGTH_LONG).show();
                        }
                    });
                }
                @Override
                public void onClose(int i, String s, boolean b) {
                    Log.i(TAG, "onClose: ");
                }
                @Override
                public void onError(Exception e) {
                    Log.i(TAG, "onError: ");
                }
            };
            mWebSocketClient.connect();

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_three, container, false);
        mainBottomActivity = (MainBottomActivity)inflater.getContext();
        Button button = (Button) view.findViewById(R.id.getinvBtn);
        sendBtn =(Button)view.findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//
//                webSocket.send("a string");
//                webSocket.send(new byte[10]);

                initSockect();
           //     mWebSocketClient.send("hello yuchengren");


            }
        });

        webSocketConnectCallback = new AsyncHttpClient.WebSocketConnectCallback() {


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
                webSocket.setClosedCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception ex) {
                        mainBottomActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mainBottomActivity,"helloworld",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




             webSocketFuture = AsyncHttpClient.getDefaultInstance().websocket("ws://yuchengren.oicp.io:38379/WebSocketsSample/ws.ashx?name=yuchengren", "80", webSocketConnectCallback);




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
