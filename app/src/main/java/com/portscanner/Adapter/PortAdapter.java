package com.portscanner.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.portscanner.Model.PortBean;
import com.portscanner.R;

import java.util.ArrayList;

/**
 * Created by administrator on 17/1/17.
 */

public class PortAdapter extends BaseAdapter {

    ViewHolder mHolder;
    Context mContext;
    ArrayList<PortBean> mList;
    LayoutInflater inflator;
    View view;

    public PortAdapter(ArrayList<PortBean> list, Context ctx) {
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
            view = mInflater.inflate(R.layout.list_item_port_result, null);
            mHolder = new ViewHolder();

            mHolder.tv_port_no = (TextView) view.findViewById(R.id.tv_port_no);
            mHolder.tv_port_name = (TextView) view.findViewById(R.id.tv_port_name);
            mHolder.tv_port_status = (TextView) view.findViewById(R.id.tv_port_status);

            view.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        mHolder.tv_port_no.setText(mList.get(position).getPortNo());
        mHolder.tv_port_name.setText(mList.get(position).getPortName());
        mHolder.tv_port_status.setText(mList.get(position).getPortStatus());
        return view;
    }

    public class ViewHolder {

        public TextView tv_port_no,tv_port_name,tv_port_status;
    }

}
