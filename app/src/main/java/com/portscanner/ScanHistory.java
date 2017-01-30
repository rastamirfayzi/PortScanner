package com.portscanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.portscanner.Adapter.HistoryAdapter;
import com.portscanner.Adapter.PortAdapter;
import com.portscanner.Database.PortScanDataBase;
import com.portscanner.Model.PortBean;
import com.portscanner.Utility.ValidationConstant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by administrator on 24/1/17.
 */

public class ScanHistory extends AppCompatActivity {
    Context mContext;
    ImageView iv_back,iv_filter,iv_history;
    TextView tv_title,tv_view_more;
    ListView lv_history;
    PortScanDataBase portScanDataBase;
    ArrayList<PortBean> arrPortBean;
    HistoryAdapter historyAdapter;
    String strCurrentDate;
    int status_for_day=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_history);
        mContext=this;
        arrPortBean=new ArrayList<>();
        portScanDataBase=new PortScanDataBase(mContext);
        portScanDataBase.getWritableDatabase();
        initLayout();
    }
    /**
     * method to initlize layout
     */
    public void  initLayout(){
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_filter=(ImageView)findViewById(R.id.iv_filter);
        iv_history=(ImageView)findViewById(R.id.iv_history);
        tv_title=(TextView) findViewById(R.id.tv_title);
        lv_history=(ListView)findViewById(R.id.lv_history);
        iv_filter.setVisibility(View.GONE);
        iv_history.setVisibility(View.GONE);
        tv_title.setText(mContext.getResources().getString(R.string.History));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        strCurrentDate=ValidationConstant.getCurrentDate();
      /*  PortBean portBean=new PortBean();
        portBean.setItemType("date");
        portBean.setDay("Today");
        portBean.setDate(ValidationConstant.getCurrentDate());
        arrPortBean.add(portBean);*/
        lv_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showResults(position);
            }
        });
        LayoutInflater myinflater = getLayoutInflater();
        ViewGroup footer = (ViewGroup)myinflater.inflate(R.layout.history_footer, lv_history, false);
        lv_history.addFooterView(footer, null, false);
        tv_view_more = (TextView) footer.findViewById(R.id.tv_view_more);
        tv_view_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               // strCurrentDate=ValidationConstant.decreaseOneDay(strCurrentDate);

                setDataWithOffset();
                //setData(strCurrentDate);
            }
        });

       // setData(strCurrentDate);
        setDataWithOffset();

    }
    private void setDataWithOffset(){
        if (portScanDataBase!=null){
            ArrayList<PortBean>  arrPortBeanLocal=portScanDataBase.getAllPortList(status_for_day);
            if (arrPortBeanLocal!=null && arrPortBeanLocal.size()>0){
                status_for_day=status_for_day+5;
                arrPortBean.addAll(arrPortBeanLocal);
                if (arrPortBean!=null && arrPortBean.size()>0){
                    historyAdapter=new HistoryAdapter(arrPortBean,mContext);
                    lv_history.setAdapter(historyAdapter);
                }
            }else{
                    ValidationConstant.showToast(mContext, "No record found");
                    return;
            }
        }

    }
    private void setData(String strDate){
        if (portScanDataBase!=null){
           ArrayList<PortBean>  arrPortBeanLocal=portScanDataBase.getAllPortList(strDate);
            if (arrPortBeanLocal!=null && arrPortBeanLocal.size()>0){
                if (status_for_day>1) {
                    PortBean portBean = new PortBean();
                    portBean.setItemType("date");
                    if (status_for_day == 2) {
                        portBean.setDay("Yesterday");
                        portBean.setDate(strCurrentDate);
                    } else {
                        portBean.setDay(strCurrentDate);
                        portBean.setDate(strCurrentDate);
                    }
                    arrPortBean.add(portBean);
                }
            }else{
                if (status_for_day>1) {
                    ValidationConstant.showToast(mContext, "No record found");
                    return;
                }

            }

            arrPortBean.addAll(arrPortBeanLocal);
            if (arrPortBean!=null && arrPortBean.size()>0){
                historyAdapter=new HistoryAdapter(arrPortBean,mContext);
                lv_history.setAdapter(historyAdapter);
            }

        }

    }

    /**
     * method to result on result activity
     */
    public void showResults(int position) {
        Intent intent = new Intent(ScanHistory.this, ScanResult.class);
        ArrayList<PortBean> arrPortDetail=portScanDataBase.getAllPortDetailList(arrPortBean.get(position).getScan_no());
        if (arrPortBean.get(position).getScan_stop_status().equals("1")){
            intent.putExtra("scan_stop_status",mContext.getResources().getString(R.string.Complete_Scan));
        }else{
            intent.putExtra("scan_stop_status",mContext.getResources().getString(R.string.Manually_Stopped));
        }
        intent.putExtra("host_name", arrPortBean.get(position).getHostName());
        intent.putExtra("port_no", "");
        intent.putExtra("show_scan_status", true);
        if (arrPortDetail!=null && arrPortDetail.size()>0){
            intent.putExtra("port_list", arrPortDetail);
            //1 =  complete 2=  manually stopped
        }
        startActivity(intent);
    }
}
