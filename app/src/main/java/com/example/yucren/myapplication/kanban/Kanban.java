/**
 * Copyright 2018 bejson.com
 */
package com.example.yucren.myapplication.kanban;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2018-12-28 9:39:50
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Kanban implements Serializable {

    private String Err;
    private String Login_user;
    private int Cur;
    private List<Dtprocess> Dtprocess;
    private List<DtItems> DtItems;
    private boolean Islast;
    private String Userno;
    private String Role;
    private String UserDpt;
    private String ScanUser;
    private String Duty;
    private String UserDptType;
    private String UserDptName;
    private int Scanid;
    private int Kbid;
    private String BoardNo;
    private int LineID;
    private String Statusname;
    private int Status;
    private int Next_status;
    private String Next_statusname;
    public void setErr(String Err) {
        this.Err = Err;
    }
    public String getErr() {
        return Err;
    }

    public void setLogin_user(String Login_user) {
        this.Login_user = Login_user;
    }
    public String getLogin_user() {
        return Login_user;
    }

    public void setCur(int Cur) {
        this.Cur = Cur;
    }
    public int getCur() {
        return Cur;
    }

    public void setDtprocess(List<Dtprocess> Dtprocess) {
        this.Dtprocess = Dtprocess;
    }
    public List<Dtprocess> getDtprocess() {
        return Dtprocess;
    }

    public void setDtItems(List<DtItems> DtItems) {
        this.DtItems = DtItems;
    }
    public List<DtItems> getDtItems() {
        return DtItems;
    }

    public void setIslast(boolean Islast) {
        this.Islast = Islast;
    }
    public boolean getIslast() {
        return Islast;
    }

    public void setUserno(String Userno) {
        this.Userno = Userno;
    }
    public String getUserno() {
        return Userno;
    }

    public void setRole(String Role) {
        this.Role = Role;
    }
    public String getRole() {
        return Role;
    }

    public void setUserDpt(String UserDpt) {
        this.UserDpt = UserDpt;
    }
    public String getUserDpt() {
        return UserDpt;
    }

    public void setScanUser(String ScanUser) {
        this.ScanUser = ScanUser;
    }
    public String getScanUser() {
        return ScanUser;
    }

    public void setDuty(String Duty) {
        this.Duty = Duty;
    }
    public String getDuty() {
        return Duty;
    }

    public void setUserDptType(String UserDptType) {
        this.UserDptType = UserDptType;
    }
    public String getUserDptType() {
        return UserDptType;
    }

    public void setUserDptName(String UserDptName) {
        this.UserDptName = UserDptName;
    }
    public String getUserDptName() {
        return UserDptName;
    }

    public void setScanid(int Scanid) {
        this.Scanid = Scanid;
    }
    public int getScanid() {
        return Scanid;
    }

    public void setKbid(int Kbid) {
        this.Kbid = Kbid;
    }
    public int getKbid() {
        return Kbid;
    }

    public void setBoardNo(String BoardNo) {
        this.BoardNo = BoardNo;
    }
    public String getBoardNo() {
        return BoardNo;
    }

    public void setLineID(int LineID) {
        this.LineID = LineID;
    }
    public int getLineID() {
        return LineID;
    }

    public void setStatusname(String Statusname) {
        this.Statusname = Statusname;
    }
    public String getStatusname() {
        return Statusname;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }
    public int getStatus() {
        return Status;
    }

    public void setNext_status(int Next_status) {
        this.Next_status = Next_status;
    }
    public int getNext_status() {
        return Next_status;
    }

    public void setNext_statusname(String Next_statusname) {
        this.Next_statusname = Next_statusname;
    }
    public String getNext_statusname() {
        return Next_statusname;
    }
    public Kanban initialKanban()
    {
        this.Err ="";
        this.DtItems=null;
        this.Dtprocess =null;
        return this ;

    }

}