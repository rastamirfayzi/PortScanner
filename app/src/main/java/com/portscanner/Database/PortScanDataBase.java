package com.portscanner.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.portscanner.Model.PortBean;

import java.util.ArrayList;

/**
 * Created by administrator on 24/1/17.
 */

public class PortScanDataBase extends SQLiteOpenHelper {

    Context mContext;
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "portscandb";
    // Scan table name
    private static final String TABLE_SCANRECORD = "scanrecord";

    private static final String KEY_SCAN_NO = "scan_no";//0
    private static final String KEY_SCAN_DATE = "scan_date";//1
    private static final String KEY_SCAN_TIME = "scan_time";//2
    private static final String KEY_SCAN_HOST = "scan_host";//3
    private static final String KEY_SCAN_PORTS = "scan_ports";//4
    private static final String KEY_SCAN_STOP_STATUS = "scan_stop_status";//5
    private static final String KEY_SCAN_TIMEOUT = "scan_timeout";//6




    private static final String TABLE_SCANRECORDDETAIL = "scanrecorddetail";

    private static final String KEY_SCAN_DETAIL_NO = "scan_no_detail";//0
    private static final String KEY_PORT_NO = "port_number";//1
    private static final String KEY_PORT_STATUS = "port_status";//2
    private static final String KEY_PORT_NAME = "port_name";//3
    private static final String KEY_SCAN_FOREIGN_KEY = "scan_no";//4

    public PortScanDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_SCAN_TABLE = "CREATE TABLE " + TABLE_SCANRECORD + "(" + KEY_SCAN_NO + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_SCAN_DATE + " TEXT," +
                KEY_SCAN_TIME + " TEXT," +
                KEY_SCAN_HOST + " TEXT," +
                KEY_SCAN_PORTS + " TEXT," +
                KEY_SCAN_STOP_STATUS + " TEXT," +
                KEY_SCAN_TIMEOUT + " TEXT" + ")";

        db.execSQL(CREATE_SCAN_TABLE);

        String CREATE_SCAN_DETAIL_TABLE = "CREATE TABLE " +
                TABLE_SCANRECORDDETAIL + "(" + KEY_SCAN_DETAIL_NO + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_PORT_NO + " TEXT," +
                KEY_PORT_STATUS + " TEXT," +
                KEY_PORT_NAME+ " TEXT," +
                KEY_SCAN_FOREIGN_KEY+" Text REFERENCES "+TABLE_SCANRECORD+"("+KEY_SCAN_NO+"))";

        Log.e("CREATE_Script",CREATE_SCAN_DETAIL_TABLE);


        db.execSQL(CREATE_SCAN_DETAIL_TABLE);
        Log.e(getClass().getName(),"table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCANRECORD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCANRECORDDETAIL);
        // Create tables again
        onCreate(db);
    }

    public long insertScanRecord(PortBean portBean) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_SCAN_DATE, portBean.getDate());

        values.put(KEY_SCAN_TIME, portBean.getTime());

        values.put(KEY_SCAN_HOST, portBean.getHostName());

        values.put(KEY_SCAN_PORTS, portBean.getNo_of_ports());
        //1 =  complete 2=  manually stopped
        values.put(KEY_SCAN_STOP_STATUS, "1");

        values.put(KEY_SCAN_TIMEOUT, portBean.getTimeout());
        // Inserting Row
        long i= db.insert(TABLE_SCANRECORD, null, values);

        db.close();
        // Closing database connection

        return  i;
        //id of last inserted row in table

    }
    public long insertScanDetailRecord(PortBean portBean,String scan_no) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PORT_NO, portBean.getPortNo());
        values.put(KEY_PORT_STATUS, portBean.getPortStatus());
        values.put(KEY_SCAN_FOREIGN_KEY, scan_no);
        values.put(KEY_PORT_NAME,portBean.getPortName());
        long i= db.insert(TABLE_SCANRECORDDETAIL, null, values);
        db.close();
        // Closing database connection
        return  i;
        //id of last inserted row in table

    }
    public void updateScanRecord(String scan_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //1 =  complete 2=  manually stopped
        values.put(KEY_SCAN_STOP_STATUS, "2");
        db.update(TABLE_SCANRECORD, values, KEY_SCAN_NO + " = ?", new String[]{"" + scan_no});
        db.close();
    }
    public ArrayList<PortBean> getAllPortList(String Date) {
        Cursor cursor=null;
        SQLiteDatabase db=null;
        ArrayList<PortBean> messageList = new ArrayList<PortBean>();
        try {
           // String selectQuery = "SELECT * FROM " + TABLE_SCANRECORD +" WHERE "+ KEY_SCAN_DATE +"="+"'"+Date+"'";
            String selectQuery = "SELECT * FROM " + TABLE_SCANRECORD +" LIMIT 0,"+Date;
            //SELECT column FROM table LIMIT 0,5
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor!=null && cursor.getCount()>0){

                if (cursor.moveToFirst()) {
                    do {
                        PortBean menuBean = new PortBean();
                        menuBean.setScan_no(""+cursor.getInt(0));
                        menuBean.setDate(cursor.getString(1));
                        menuBean.setTime(cursor.getString(2));
                        menuBean.setHostName(cursor.getString(3));
                        menuBean.setNo_of_ports(cursor.getString(4));
                        menuBean.setScan_stop_status(cursor.getString(5));
                        menuBean.setOpenPorts(""+getPortsStatus(cursor.getString(0),"open"));
                        menuBean.setClosedPorts(""+getPortsStatus(cursor.getString(0),"close"));
                        // Adding item to list
                        messageList.add(menuBean);
                    } while (cursor.moveToNext());
                    cursor.close();
                    db.close();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {


        }
        // return cart item list
        return messageList;
    }
    public ArrayList<PortBean> getAllPortList(int Date) {
        Cursor cursor=null;
        SQLiteDatabase db=null;
        ArrayList<PortBean> messageList = new ArrayList<PortBean>();
        try {
            // String selectQuery = "SELECT * FROM " + TABLE_SCANRECORD +" WHERE "+ KEY_SCAN_DATE +"="+"'"+Date+"'";
            String selectQuery = "SELECT * FROM " + TABLE_SCANRECORD +" ORDER BY "+ KEY_SCAN_NO +" DESC LIMIT "+Date+","+"5";
            //ORDER BY id DESC
            //SELECT column FROM table LIMIT 0,5
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor!=null && cursor.getCount()>0){

                if (cursor.moveToFirst()) {
                    do {
                        PortBean menuBean = new PortBean();
                        menuBean.setScan_no(""+cursor.getInt(0));
                        menuBean.setDate(cursor.getString(1));
                        menuBean.setTime(cursor.getString(2));
                        menuBean.setHostName(cursor.getString(3));
                        menuBean.setNo_of_ports(cursor.getString(4));
                        menuBean.setScan_stop_status(cursor.getString(5));
                        menuBean.setOpenPorts(""+getPortsStatus(cursor.getString(0),"open"));
                        menuBean.setClosedPorts(""+getPortsStatus(cursor.getString(0),"close"));
                        // Adding item to list
                        messageList.add(menuBean);
                    } while (cursor.moveToNext());
                    cursor.close();
                    db.close();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {


        }
        // return cart item list
        return messageList;
    }
    public ArrayList<PortBean> getAllPortDetailList(String scan_no) {
        Cursor cursor=null;
        SQLiteDatabase db=null;
        ArrayList<PortBean> messageList = new ArrayList<PortBean>();
        try {
            String selectQuery = "SELECT * FROM " + TABLE_SCANRECORDDETAIL +" WHERE "+ KEY_SCAN_NO +"="+scan_no;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor!=null && cursor.getCount()>0){

                if (cursor.moveToFirst()) {
                    do {
                        PortBean portBean = new PortBean();
                        portBean.setPortNo(cursor.getString(1));
                        portBean.setPortStatus(cursor.getString(2));
                        portBean.setPortName(cursor.getString(3));
                        // Adding item to list
                        messageList.add(portBean);
                    } while (cursor.moveToNext());
                    cursor.close();
                    db.close();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {


        }

        // return cart item list
        return messageList;
    }
    public int getPortsStatus(String scan_no,String status) {
        SQLiteDatabase db = this.getReadableDatabase();
       // Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SCANRECORDDETAIL + " WHERE " + KEY_SCAN_NO+ "=? AND " + KEY_PORT_STATUS + "=?", new String[]{scan_no,status+""});
        String selectQuery = "SELECT * FROM " + TABLE_SCANRECORDDETAIL +" WHERE "+ KEY_SCAN_NO +"="+scan_no+" AND " + KEY_PORT_STATUS + "="+"'"+status+"'";
        db = this.getReadableDatabase();
        Cursor  cursor = db.rawQuery(selectQuery, null);
        int portcount=cursor.getCount();
        cursor.close();
        // return count
        return portcount;
    }

}
