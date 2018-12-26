package com.example.yucren.myapplication;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

/**
 * Created by yucren on 2018-12-22.
 */
@SmartTable(name = "图书目录")
public class BookChapter {

    @SmartColumn(id = 0,name = "工序",autoMerge = true)
    private   String 工序;
    @SmartColumn(id = 1,autoMerge = true)
    private  String 品名;
    @SmartColumn(id = 2,name = "生产料品",autoMerge = true)
    private   String 生产料品;
    @SmartColumn(id = 3,name = "订单号",autoMerge = true)
    private   String 订单号;
}
