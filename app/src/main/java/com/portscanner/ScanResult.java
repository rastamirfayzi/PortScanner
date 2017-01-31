package com.portscanner;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.portscanner.Adapter.PortAdapter;
import com.portscanner.Model.PortBean;
import com.portscanner.Utility.ValidationConstant;
import java.util.ArrayList;


public class ScanResult extends AppCompatActivity {
    Context mContext;
    ImageView iv_back,iv_filter,iv_history;
    TextView tv_title,tv_scan_status;
    TextView et_host_name;
    TextView et_port;
    ListView lv_report;
    PortAdapter portAdapter;
    ArrayList<PortBean> arrPortBean;
    String strHostName,strPort;
    String strFilter="all";
    ImageView iv_menu;
    LinearLayout ll_scan_status,ll_port;
    boolean show_scan_status;
    String  scan_stop_status;
    LinearLayout ll_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        mContext=this;
        arrPortBean = (ArrayList<PortBean>) getIntent().getSerializableExtra("port_list");
        strHostName =  getIntent().getStringExtra("host_name");
        strPort =  getIntent().getStringExtra("port_no");
        show_scan_status = getIntent().getBooleanExtra("show_scan_status",false);
        scan_stop_status = getIntent().getStringExtra("scan_stop_status");
        initLayout();
    }

    /**
     * method to initlize layout
     */
    public void  initLayout(){
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_filter=(ImageView)findViewById(R.id.iv_filter);
        iv_history = (ImageView) findViewById(R.id.iv_history);
        tv_title=(TextView) findViewById(R.id.tv_title);
        iv_menu=(ImageView)findViewById(R.id.iv_menu);
        et_host_name=(TextView) findViewById(R.id.et_host_name);
        tv_scan_status=(TextView)findViewById(R.id.tv_scan_status);
        et_port=(TextView) findViewById(R.id.et_port);
        lv_report=(ListView)findViewById(R.id.lv_report);
        ll_scan_status=(LinearLayout)findViewById(R.id.ll_scan_status);
        ll_back=(LinearLayout) findViewById(R.id.ll_back);

        ll_port=(LinearLayout)findViewById(R.id.ll_port);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_history.setVisibility(View.GONE);
        tv_title.setText(mContext.getResources().getString(R.string.Scanning_Result));
        if (show_scan_status){
            ll_scan_status.setVisibility(View.VISIBLE);
            ll_port.setVisibility(View.GONE);
            tv_scan_status.setText(scan_stop_status);
        }else{
            ll_scan_status.setVisibility(View.GONE);
            ll_port.setVisibility(View.VISIBLE);
        }
        et_host_name.setText(strHostName);
        et_port.setText(strPort);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int)(width-90),
                LinearLayout.LayoutParams.WRAP_CONTENT); // or set height to any fixed value you want

        et_host_name.setLayoutParams(lp);

        et_port.setLayoutParams(lp);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu(v);
            }
        });
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LayoutInflater myinflater = getLayoutInflater();
        ViewGroup myHeader = (ViewGroup)myinflater.inflate(R.layout.result_header, lv_report, false);
        lv_report.addHeaderView(myHeader, null, false);

        if (arrPortBean!=null && arrPortBean.size()>0){
            portAdapter=new PortAdapter(arrPortBean,mContext);
            lv_report.setAdapter(portAdapter);
        }


   }

    public PopupWindow pw;
    public View pview = null;
    /**
     * method to show filter window
     */
    public void popupMenu(View v) {
        if (pw != null && pw.isShowing()) {
            pw.dismiss();
            pview = null;
        } else {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            pview = inflater.inflate(R.layout.popup_option_menu, null);
            if (pview != null)
                pw = new PopupWindow(pview, LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
            pw.setOutsideTouchable(true);
            pw.setTouchable(true);
            pw.setFocusable(true);
            pw.setBackgroundDrawable(new BitmapDrawable());
            pw.showAsDropDown(v);
            pw.setTouchInterceptor(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        pw.dismiss();
                        return true;
                    }
                    return false;
                }
            });

            TextView tv_open = (TextView) pview.findViewById(R.id.tv_open);
            TextView tv_close = (TextView) pview.findViewById(R.id.tv_close);
            TextView tv_all = (TextView) pview.findViewById(R.id.tv_all);
            tv_open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strFilter="open";
                    filterList();
                    pw.dismiss();
                    pview = null;
                }
            });
            tv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strFilter="close";
                    filterList();
                    pw.dismiss();
                    pview = null;
                }
            });
            tv_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strFilter="all";
                    filterList();
                    pw.dismiss();
                    pview = null;
                }
            });


        }
    }
    /**
     * method to filter list
     */
    public void filterList(){

        if (arrPortBean!=null && arrPortBean.size()>0) {
            ArrayList<PortBean> arrFilterBean = new ArrayList<>();
            if (strFilter.equals("all")){
                arrFilterBean.addAll(arrPortBean);
            }else{
                for (int i = 0; i < arrPortBean.size(); i++) {
                    if (arrPortBean.get(i).getPortStatus().equals(strFilter)){
                        arrFilterBean.add(arrPortBean.get(i));
                    }
                }
            }
            if (arrFilterBean != null && arrFilterBean.size()>0) {
                portAdapter=new PortAdapter(arrFilterBean,mContext);
                lv_report.setAdapter(portAdapter);
            }else{
                portAdapter=new PortAdapter(arrFilterBean,mContext);
                lv_report.setAdapter(portAdapter);
                portAdapter.notifyDataSetChanged();
            }
        }else{
            ValidationConstant.showValidationDialog(mContext,mContext.getResources().getString(R.string.No_ports_found));
        }
    }
}
