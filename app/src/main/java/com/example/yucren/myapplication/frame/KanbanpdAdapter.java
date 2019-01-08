package com.example.yucren.myapplication.frame;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yucren.myapplication.MainBottomActivity;
import com.example.yucren.myapplication.R;
import com.example.yucren.myapplication.kanban.KanbanPD;

import java.util.List;

public class  KanbanpdAdapter extends BaseAdapter {
    public List<KanbanPD> pds;
    private LayoutInflater inflater;
    public KanbanpdAdapter(Context context, List<KanbanPD>  lists)
    {
    pds =lists;
    inflater =LayoutInflater.from(context);
    }
   public void  clear(){

        pds.clear();

   }

    @Override
    public int getCount() {
        return pds.size();
    }

    @Override
    public Object getItem(int position) {
        return pds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.items,null);
        TextView itemCodeTv = (TextView)view.findViewById(R.id.itemCode);
        TextView itemNameTv =(TextView)view.findViewById(R.id.itemName);
        TextView processTv =(TextView)view.findViewById(R.id.fName);
        TextView itemModel =(TextView)view.findViewById(R.id.fModel);
        TextView fcout =(TextView)view.findViewById(R.id.fcount);
        final EditTextPlus pdCount =(EditTextPlus) view.findViewById(R.id.fdCount);
        pdCount.setSaveEnabled(false);
        KanbanPD pd = pds.get(position);
        itemCodeTv.getLayoutParams().width=Math.round(MainBottomActivity.treeSet1.last());
         itemNameTv.getLayoutParams().width=Math.round(MainBottomActivity.treeSet2.last());
         itemModel.getLayoutParams().width=Math.round(MainBottomActivity.treeSet3.last());
        processTv.getLayoutParams().width=Math.round(MainBottomActivity.treeSet4.last());
        fcout.getLayoutParams().width=Math.round(MainBottomActivity.treeSet5.last());
        pdCount.getLayoutParams().width=Math.round(MainBottomActivity.treeSet6.last());

        if ( position ==0)
        {
            itemCodeTv.setText("物料代码");
            itemNameTv.setText("物料名称");
            itemModel.setText("规格型号");
            processTv.setText(("扫描点"));
            pdCount.setText("盘点数量");
            fcout.setText("库存数量");
            pdCount.setEnabled(false);
            pdCount.setTextColor(Color.BLACK);

            pdCount.intId=position;
        }
        else {
            itemCodeTv.setText(pd.getFItemCode());
            itemNameTv.setText(pd.getFItemName());
            itemModel.setText(pd.getFModel());
            processTv.setText(pd.getFName());
            fcout.setText(String.valueOf(pd.getFcount()));
            pdCount.setText(  String.valueOf(pd.getFDCount()));
            pdCount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (! String.valueOf(s).equals("") && ! String.valueOf(s).equals("-"))
                        {
                            pds.get(position).setFDCount(Integer.parseInt((String.valueOf(s))));
                        }
                        else
                        {
                            pds.get(position).setFDCount(0);
                        }
                    }catch (Exception ex)
                    {
                        Toast.makeText(inflater.getContext(),"发生错误：" + ex.getMessage(),Toast.LENGTH_LONG).show();
                    }




                }
            });
            pdCount.intId=position;
        }

        return view;
    }
}
