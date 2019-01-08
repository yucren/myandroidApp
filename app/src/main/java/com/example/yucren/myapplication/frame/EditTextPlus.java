package com.example.yucren.myapplication.frame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class  EditTextPlus extends EditText
 {
     public int intId;


     public EditTextPlus(Context context) {
         super(context);
     }

     public EditTextPlus(Context context, AttributeSet attrs) {
         super(context, attrs);
     }

     public EditTextPlus(Context context, AttributeSet attrs, int defStyleAttr) {
         super(context, attrs, defStyleAttr);
     }

     @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
     public EditTextPlus(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
         super(context, attrs, defStyleAttr, defStyleRes);
     }
 }
