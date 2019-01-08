package com.example.yucren.myapplication.frame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.example.yucren.myapplication.MainBottomActivity;
import com.example.yucren.myapplication.R;

/**
 * Created by yucren on 2018-12-30.
 */

public class FragmentOne extends Fragment {
    public EditText loginET;
    public Button scanbtn;
    public TextView curtv;
    public TextView nextv;
    public  EditText kanbano;
    public SmartTable table;
    public  Button startBtn;
    public  Button submitBtn;
    public  Button waitBtn;
    public  Button recycleBtn;

    View view;
    MainBottomActivity mainBottomActivity;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_one,container,false);
        curtv =(TextView)view.findViewById(R.id.cutv2);
        nextv =(TextView)view.findViewById(R.id.instock);
        scanbtn = (Button)view.findViewById(R.id.scanbtn);
        kanbano =(EditText)view.findViewById(R.id.kanbanno);
        startBtn=(Button)view.findViewById(R.id.startBtn);
        submitBtn=(Button)view.findViewById(R.id.submitBtn);
        waitBtn =(Button) view.findViewById(R.id.waitBtn);
        recycleBtn  = (Button)view.findViewById(R.id.recycleBtn);


        startBtn.setEnabled(false);
        submitBtn.setEnabled(false);
        waitBtn.setEnabled(false);
        recycleBtn.setEnabled(false);
        scanbtn.setEnabled(false);
        return  view;

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        //当设置的高度比原来默认的高度要小时，调用setHeight();是不生效的
        super.onResume();
        DisplayMetrics metrics =new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;    //宽像素
        int height =metrics.heightPixels;  //高像素
        float density =metrics.density;  //屏幕密度
        int densityDpi =metrics.densityDpi;
        float deviceDP =  width /density;
        int value = (int)((deviceDP -15-20)/4 * density);
        startBtn.getLayoutParams().width =value;
        submitBtn.getLayoutParams().width =value;
        waitBtn.getLayoutParams().width =value;
        recycleBtn.getLayoutParams().width =value;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mainBottomActivity.kanban.getLogin_user() != null && ! mainBottomActivity.kanban.getLogin_user().equals("")) {
            scanbtn.setEnabled(true);
        }

        mainBottomActivity =(MainBottomActivity)getActivity();
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainBottomActivity.type="startScan";
                mainBottomActivity.loadData("", mainBottomActivity.type);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainBottomActivity.type="submit";
                mainBottomActivity.loadData("", mainBottomActivity.type);
            }
        });
        waitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainBottomActivity.type="out";
                mainBottomActivity.loadData("", mainBottomActivity.type);
            }
        });
        recycleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainBottomActivity.type="recycle";
                mainBottomActivity.loadData("", mainBottomActivity.type);
            }
        });
        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainBottomActivity.type="scan";
                mainBottomActivity.InitialScan();
            }
        });
        table = (SmartTable)view.findViewById(R.id.scanTable);
//        ArrayList<DtItems> dtItems =  new ArrayList<DtItems>();
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowYSequence(false);
        table.getConfig().setFixedTitle(true);
        table.getConfig().setShowColumnTitle(true);

        }
    }

