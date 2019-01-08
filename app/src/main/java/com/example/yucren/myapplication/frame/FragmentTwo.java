package com.example.yucren.myapplication.frame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.yucren.myapplication.MainBottomActivity;
import com.example.yucren.myapplication.R;

/**
 * Created by yucren on 2018-12-30.
 */

public class FragmentTwo extends Fragment {
  public ListView gridView;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_two,container,false);
         gridView =(ListView)view.findViewById(R.id.gridView1);
          ImageButton button =view.findViewById(R.id.getDataBtn);
          button.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  try {
                      MainBottomActivity mainBottomActivity =(MainBottomActivity)getActivity();
                      MainBottomActivity.type ="pd";
                      mainBottomActivity.InitialScan();

                  } catch (Exception e) {
                      e.printStackTrace();
                  }


//                     EditTextPlus et = (EditTextPlus) ((GridLayout)gridView.getChildAt(i)).getChildAt(3);
//                     Toast.makeText(getActivity(),String.valueOf(et.getText()),Toast.LENGTH_LONG).show();
//                     et.setEnabled(false);
                 }
          });



//        List<Map<String,Object>> lists = new ArrayList<>();
//        for (int i =0;i<pds.length;i++)
//        {
//            Map<String,Object> map = new HashMap<>();
//            map.put("itemCode",pds[i].getItemCode());
//            map.put("itemName",pds[i].getItemName());
//            map.put("prcoess",pds[i].getItemMode());
//            map.put("pdinvCount",pds[i].getCount());
//            lists.add(map);
//
//        }
//        final SimpleAdapter adapter =new SimpleAdapter(getActivity(),lists,R.layout.items,new String[]
//                { "itemCode","itemName","prcoess","pdinvCount"},
//                new int[]{R.id.itemCode,R.id.itemName,
//                R.id.prcoess,R.id.pdinvCount
//
//        });

//        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                EditText editText =(EditText) ((GridLayout)view).getChildAt(3);
//                editText.setFocusable(true);
//                editText.setFocusableInTouchMode(true);
//                editText.requestFocus();
//
//
//                Map<String,Object> dd = (Map<String, Object>) parent.getItemAtPosition(position);
//                String name = (String) dd.get("itemCode");
//                String Code = (String) dd.get("itemName");
//            }
//        });

//        Button button =view.findViewById(R.id.getDataBtn);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//
//            }
//        });
        return view;



    }



    @Override
    public void onResume() {
        super.onResume();


    }
}
class Pd{
    private  String itemCode;
    private  String itemName;
    private String itemMode;
    private int Count;

    public Pd(String itemCode, String itemName, String itemMode, int count) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemMode = itemMode;
        Count = count;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemMode() {
        return itemMode;
    }

    public void setItemMode(String itemMode) {
        this.itemMode = itemMode;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}

