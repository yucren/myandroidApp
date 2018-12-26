package com.example.yucren.myapplication;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.bin.david.form.core.*;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.IBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;
import com.bin.david.form.data.table.PageTableData;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String BS_PACKAGE = "com.google.zxing.client.android";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button =(Button)findViewById(R.id.button1);
        Button addRowBtn =(Button)findViewById(R.id.addRow);
        Button scanbtn =(Button)findViewById(R.id.scanbtn);
        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator in = new IntentIntegrator(MainActivity.this);
         android.app.AlertDialog dialog = in.initiateScan();





        }
        });
        addRowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartTable table =(SmartTable)findViewById(R.id.table);
              List<BookChapter>  bookChapters = table.getTableData().getT();
              bookChapters.remove(0);
               table.setData(bookChapters);
               



            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartTable table =(SmartTable)findViewById(R.id.table);
//                List<BookChapter> bookChapters =new ArrayList<>();
//                bookChapters.add(new BookChapter("9","helloworld","50"));
//                bookChapters.add(new BookChapter("9","helloworld","50"));
//                bookChapters.add(new BookChapter("9","helloworld","50"));
//                bookChapters.add(new BookChapter("9","helloworld","50"));
//                bookChapters.add(new BookChapter("9","helloworld","50"));
//                bookChapters.add(new BookChapter("9","helloworld","50"));
//                bookChapters.add(new BookChapter("9","helloworld","50"));
//                bookChapters.add(new BookChapter("9","helloworld","50"));
//                bookChapters.add(new BookChapter("9","helloworld","50"));
//                bookChapters.add(new BookChapter("9","helloworld","50"));
//                bookChapters.add(new BookChapter("9","helloworld","50"));
//                bookChapters.add(new BookChapter("9","helloworld","50"));
//                bookChapters.add(new BookChapter("9","helloworld","50"));
//                bookChapters.add(new BookChapter("9","helloworld","50"));
//                bookChapters.add(new BookChapter("9","helloworld","50"));
//                bookChapters.add(new BookChapter("9","helloworld","50"));


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                     send();
                    }
                }).start();

            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
   IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
   if (scanResult != null) {
   IntentResult result =   IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);
   String content =result.getContents();
   TextView scanTV =(TextView)findViewById(R.id.scanResult);
   scanTV.setText(content);

     // handle scan result
   }
   // else continue with any other code you need in the method

 }
    private  void send()
    {
       // final TextView textView =(TextView)findViewById(R.id.textView1);
        URL url;
        String result="";
        try {
            String replace ="<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">";
            url = new URL("http://yu539928505.imwork.net/MESEXWCF/MESService.svc/PickListInfor?SN=>LSW0065EEJ0001268<");
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
           JsonParser jsonParser =new JsonParser();
            JsonArray jsonArray = jsonParser.parse(result).getAsJsonArray();
           ArrayList<BookChapter> bookChapters =new ArrayList<>();
            Gson gson =new Gson();
//            Gson gson2 = new GsonBuilder().enableComplexMapKeySerialization().create();
//            Type type = new TypeToken<Map<String, String>>() {}.getType();
//            List<Object> maplist =new ArrayList<>();
            for (JsonElement book  : jsonArray)
            {
             BookChapter chapter =     gson.fromJson(book,BookChapter.class);
             bookChapters.add((chapter));
            //    Map<String, String> map2 = gson2.fromJson(book,type);
            //    maplist.add(map2);
            }
            SmartTable table =(SmartTable)findViewById(R.id.table);
            table.setData(bookChapters);
//            MapTableData tableData =MapTableData.create("目录",maplist);
//            tableData.getColumns().get(11).setColumnName("俞程仁");
//            tableData.getColumns().get(11).setAutoMerge(true);
//            tableData.getColumns().get(11).setMaxMergeCount(10);
//            tableData.getColumns().get(11).setTextAlign(Paint.Align.LEFT);
//            tableData.getColumns().get(1).setReverseSort(true);
//            table.setTableData(tableData);
//
////            PageTableData<BookChapter> bookChapterPageTableData=   table.setData(bookChapters);
////            bookChapterPageTableData.setCurrentPage(2);
//            table.getConfig().setShowXSequence(false);
//            table.getConfig().setShowYSequence(false);
//            table.setZoom(true);
//            table.getConfig().setContentStyle(new FontStyle(20, Color.BLACK));
//            //设置全局背景颜色
////            table.getConfig().setContentBackground(new IBackgroundFormat() {
////                @Override
////                public void drawBackground(Canvas canvas, Rect rect, Paint paint) {
////                    paint.setColor(Color.YELLOW);
////                    canvas.drawRect(rect,paint);
////                }
////            });
            table.getConfig().setContentCellBackgroundFormat(new ICellBackgroundFormat<CellInfo>() {
                @Override
                public void drawBackground(Canvas canvas, Rect rect, CellInfo cellInfo, Paint paint) {
                               if (cellInfo.row % 2 ==0)
                               {
                                   paint.setColor(Color.YELLOW);
                                   canvas.drawRect(rect,paint);
                               }
                }

                @Override
                public int getTextColor(CellInfo cellInfo) {
                    return 0;
                }
            });
//
//           // Toast.makeText(MainActivity.this,"共有多少行" + table.getTableData().getColumns().size(),Toast.LENGTH_LONG).show();
//            //textView.setText(result);

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        catch ( Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
