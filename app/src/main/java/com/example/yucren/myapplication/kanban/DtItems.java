/**
  * Copyright 2018 bejson.com 
  */
package com.example.yucren.myapplication.kanban;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

/**
 * Auto-generated: 2018-12-26 15:18:54
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@SmartTable(name = "看板物料")
public class DtItems {
    public DtItems(long fitemid, String fitemcode, String fitemname, int ftransfer_batch) {
        this.fitemid = fitemid;
        this.fitemcode = fitemcode;
        this.fitemname = fitemname;
        this.ftransfer_batch = ftransfer_batch;
    }

    private long fitemid;
    @SmartColumn(id = 0,name = "料号")
    private String fitemcode;
    @SmartColumn(id = 1,name = "料品名称")
    private String fitemname;
    @SmartColumn(id = 2,name = "转移批量")
    private int ftransfer_batch;
    public void setFitemid(long fitemid) {
         this.fitemid = fitemid;
     }
     public long getFitemid() {
         return fitemid;
     }

    public void setFitemcode(String fitemcode) {
         this.fitemcode = fitemcode;
     }
     public String getFitemcode() {
         return fitemcode;
     }

    public void setFitemname(String fitemname) {
         this.fitemname = fitemname;
     }
     public String getFitemname() {
         return fitemname;
     }

    public void setFtransfer_batch(int ftransfer_batch) {
         this.ftransfer_batch = ftransfer_batch;
     }
     public int getFtransfer_batch() {
         return ftransfer_batch;
     }

}