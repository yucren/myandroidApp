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

/**
 * Created by yucren on 2018-12-30.
 */

public class FragmentThree extends Fragment {
    MainBottomActivity mainBottomActivity;
   public  SmartTable smartTable;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_three,container,false);

        Button button =(Button)view.findViewById(R.id.getinvBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    mainBottomActivity.loadInv("S00001");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
               WebView webView= (WebView) view.findViewById(R.id.webBrowser);
            webView.setWebChromeClient(new WebChromeClient(){
                @Override
                public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                    Toast.makeText(mainBottomActivity,message,Toast.LENGTH_LONG).show();
                    return  false;
                }
            });
            webView.getSettings().setJavaScriptEnabled(true);

                webView.loadUrl("file:///android_asset/index.html");

            }
        });
        smartTable =(SmartTable)view.findViewById(R.id.invtable);
        mainBottomActivity = (MainBottomActivity) getActivity();
        smartTable.getConfig().setShowXSequence(false);
        smartTable.getConfig().setShowYSequence(false);
        smartTable.getConfig().setFixedTitle(true);
        smartTable.getConfig().setShowColumnTitle(true);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
