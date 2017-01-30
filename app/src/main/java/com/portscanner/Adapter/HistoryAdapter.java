package com.portscanner.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.portscanner.Model.PortBean;
import com.portscanner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 17/1/17.
 */

public class HistoryAdapter extends BaseAdapter {

    ViewHolder mHolder;
    Context mContext;
    ArrayList<PortBean> mList;
    LayoutInflater inflator;
    View view;

    public HistoryAdapter(ArrayList<PortBean> list, Context ctx) {
        this.mList = list;
        this.mContext = ctx;
        inflator = LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        view = convertView;
        LayoutInflater mInflater = (LayoutInflater)
                mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = mInflater.inflate(R.layout.list_item_port_history, null);
            mHolder = new ViewHolder();

            mHolder.tv_no_of_ports = (TextView) view.findViewById(R.id.tv_no_of_ports);
            mHolder.ll_detail = (LinearLayout) view.findViewById(R.id.ll_detail);
            mHolder.tv_day = (TextView) view.findViewById(R.id.tv_day);
            mHolder.tv_host_name = (TextView) view.findViewById(R.id.tv_host_name);
            mHolder.tv_open_ports = (TextView) view.findViewById(R.id.tv_open_ports);
            mHolder.tv_closed_ports = (TextView) view.findViewById(R.id.tv_closed_ports);
            mHolder.tv_date_time = (TextView) view.findViewById(R.id.tv_date);

            view.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) view.getTag();
        }
      /*  if (mList.get(position).getItemType()!=null){
            mHolder.ll_detail.setVisibility(View.GONE);
            mHolder.tv_day.setVisibility(View.VISIBLE);
            mHolder.tv_day.setText(mList.get(position).getDay());

        }else{
            mHolder.ll_detail.setVisibility(View.VISIBLE);
            mHolder.tv_day.setVisibility(View.GONE);

        }*/
        mHolder.tv_host_name.setText(mList.get(position).getHostName());
        mHolder.tv_date_time.setText(mList.get(position).getTime());
        mHolder.tv_open_ports.setText(mList.get(position).getOpenPorts());
        mHolder.tv_closed_ports.setText(mList.get(position).getClosedPorts());
        mHolder.tv_no_of_ports.setText(mList.get(position).getNo_of_ports());
     /*   if (mList.get(position).getItemType().equals("date")){

        }else{

        }*/


        return view;
    }

    public class ViewHolder {

        public TextView tv_day,tv_host_name,tv_no_of_ports,tv_open_ports,tv_closed_ports,tv_date_time;
        LinearLayout ll_detail;
    }

}
