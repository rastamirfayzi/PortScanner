package com.portscanner;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.portscanner.Model.PortBean;
import com.portscanner.Utility.ValidationConstant;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TestDashboard extends AppCompatActivity {
    public static int VALUE_4 = 4;
    Context mContext;
    ImageView iv_back, iv_filter;
    TextView tv_start_scan, tv_title, tv_seek_progress;
    EditText et_host_name;
    EditText et_port;
    String strHostName, strPort;
    SeekBar seekBar;
    LinearLayout ll_start_scan;
    List<String> arrPortList;
    ArrayList<PortBean> arrPortBean;
    /**
     * method to show scan dialog
     */
    ProgressBar progressBar;
    TextView tv_percent;
    TextView tv_counter;
    Dialog dialogScan;
    int seekProgress = 5;
    boolean isAllValidPort = true;
    int taskCounter = 0;
    boolean shouldStop = false;
    private Connection connection;
    int countporttoScan=0;
    int PORTLIMIT=1000;
    int MAX_PORT_VALUE=65536;
    int MIN_PORT_VALUE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mContext = this;
        initLayout();
    }

    /**
     * method to initlize layout
     */
    public void initLayout() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_filter = (ImageView) findViewById(R.id.iv_filter);
        tv_start_scan = (TextView) findViewById(R.id.tv_start_scan);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_host_name = (EditText) findViewById(R.id.et_host_name);
        tv_seek_progress = (TextView) findViewById(R.id.tv_seek_progress);
        et_port = (EditText) findViewById(R.id.et_port);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setProgress(5);
        tv_seek_progress.setText("5");
        ll_start_scan = (LinearLayout) findViewById(R.id.ll_start_scan);
        iv_back.setVisibility(View.GONE);
        iv_filter.setVisibility(View.GONE);
        tv_title.setText(mContext.getResources().getString(R.string.Dashboard));
        ValidationConstant.hideKeyBoard(mContext, et_host_name);


        ll_start_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidationConstant.hideKeyBoard(mContext, et_host_name);


                strHostName = et_host_name.getText().toString();
                strPort = et_port.getText().toString();
                if (ValidationConstant.isStringNullOrBlank(strHostName)) {
                    ValidationConstant.showValidationDialog(mContext, mContext.getString(R.string.Please_enter_host_name));
                    return;
                }/*else if (!ValidationConstant.isvalidIPAddress(strHostName)) {
                    ValidationConstant.showValidationDialog(mContext, mContext.getString(R.string.Please_enter_valid_host));
                    return;
                }*/ else if (ValidationConstant.isStringNullOrBlank(strPort)) {
                    ValidationConstant.showValidationDialog(mContext, mContext.getString(R.string.Please_enter_port));
                    return;
                } else if (!ValidationConstant.isValidPortValidation(strPort)) {
                    ValidationConstant.showValidationDialog(mContext, mContext.getString(R.string.Please_enter_valid_port));
                    return;
                }
                isAllValidPort = true;
                arrPortList = new ArrayList<String>();
                if (strPort.contains(",")) {
                    List<String> listPorts = Arrays.asList(strPort.split(","));
                    for (int i = 0; i < listPorts.size(); i++) {
                        if (listPorts.get(i).contains("-")) {

                            List<String> listPortsDash = Arrays.asList(listPorts.get(i).split("-"));

                            if (Integer.parseInt(listPortsDash.get(1)) < Integer.parseInt(listPortsDash.get(0))) {
                                ValidationConstant.showValidationDialog(mContext, mContext.getString(R.string.Please_enter_valid_port_range));
                                return;
                            }

                            int startPortRange = Integer.parseInt(listPortsDash.get(0));
                            int stopPortRange = Integer.parseInt(listPortsDash.get(1));

                            if ((stopPortRange-startPortRange)>PORTLIMIT){
                                ValidationConstant.showValidationDialog(mContext, mContext.getString(R.string.Maximum_ports_allowed_are_thousand_only));
                                return;
                            }

                            if (startPortRange < MIN_PORT_VALUE || startPortRange > MAX_PORT_VALUE || stopPortRange < MIN_PORT_VALUE || stopPortRange > MAX_PORT_VALUE) {
                                ValidationConstant.showValidationDialog(mContext, mContext.getString(R.string.Port_range_must_be_between_0_65536));
                                return;
                            } else {
                                for (int j = startPortRange; j <= stopPortRange; j++) {
                                    if (!arrPortList.contains("" + j)) {
                                        if (j < MIN_PORT_VALUE || j > MAX_PORT_VALUE) {
                                            isAllValidPort = false;
                                            break;
                                        } else {
                                            arrPortList.add("" + j);
                                        }
                                    }
                                }
                            }

                        } else {
                            if (!arrPortList.contains(listPorts.get(i))) {
                                if (Integer.parseInt(listPorts.get(i)) < MIN_PORT_VALUE || Integer.parseInt(listPorts.get(i)) > MAX_PORT_VALUE) {
                                    isAllValidPort = false;
                                    break;
                                } else {
                                    arrPortList.add(listPorts.get(i));
                                }
                            }

                        }
                    }

                    if (arrPortList.size()>PORTLIMIT){
                        ValidationConstant.showValidationDialog(mContext, mContext.getString(R.string.Maximum_ports_allowed_are_thousand_only));
                        return;
                    }
                } else {

                    if (strPort.contains("-")) {
                        List<String> listPortsDash = Arrays.asList(strPort.split("-"));
                        if (Integer.parseInt(listPortsDash.get(1)) < Integer.parseInt(listPortsDash.get(0))) {
                            ValidationConstant.showValidationDialog(mContext, mContext.getString(R.string.Please_enter_valid_port_range));
                            return;
                        }
                        int startPortRange = Integer.parseInt(listPortsDash.get(0));
                        int stopPortRange = Integer.parseInt(listPortsDash.get(1));

                        if ((stopPortRange-startPortRange)>PORTLIMIT){
                            ValidationConstant.showValidationDialog(mContext, mContext.getString(R.string.Maximum_ports_allowed_are_thousand_only));
                            return;
                        }

                        if (startPortRange < MIN_PORT_VALUE || startPortRange > MAX_PORT_VALUE || stopPortRange < MIN_PORT_VALUE || stopPortRange > MAX_PORT_VALUE) {
                            ValidationConstant.showValidationDialog(mContext, mContext.getString(R.string.Port_range_must_be_between_0_65536));
                            return;
                        } else {
                            for (int j = startPortRange; j <= stopPortRange; j++) {
                                if (!arrPortList.contains("" + j)) {
                                    if (j < MIN_PORT_VALUE || j > MAX_PORT_VALUE) {
                                        isAllValidPort = false;
                                        break;
                                    } else {
                                        arrPortList.add("" + j);
                                    }

                                }
                            }
                        }

                    } else {
                        if (!arrPortList.contains(strPort)) {
                            if (Integer.parseInt(strPort) < MIN_PORT_VALUE || Integer.parseInt(strPort) > MAX_PORT_VALUE) {
                                isAllValidPort = false;
                            } else {
                                arrPortList.add(strPort);
                            }
                        }
                    }

                }




                if (isAllValidPort) {
                    if (arrPortList != null && arrPortList.size() > 0) {
                        if (ValidationConstant.isNetworkAvailable(mContext)) {

                            if (strHostName.contains("http") || strHostName.contains("https")) {
                                GetHostName getHostName = new GetHostName();
                                getHostName.execute();
                            } else {
                                 showScanDialog();
                                shouldStop = false;
                                countporttoScan=0;
                                if (arrPortList.size() <= 3) {
                                    taskCounter = VALUE_4;
                                    connection = new Connection(arrPortList);
                                    connection.execute(arrPortList);
                                } else {
                                    if (arrPortList.size() >= 4) {
                                        taskCounter = 1;
                                        VALUE_4 = 4;

                                        int partitionCount = arrPortList.size() / 4;
                                        List<String> list1 = arrPortList.subList(0 * partitionCount, 1 * partitionCount);
                                        connection = new Connection(list1);
                                        connection.execute(list1);

                                        List<String> list2 = arrPortList.subList(1 * partitionCount, 2 * partitionCount);
                                        connection = new Connection(list2);
                                        connection.execute(list2);

                                        List<String> list3 = arrPortList.subList(2 * partitionCount,3 * partitionCount);
                                        connection = new Connection(list3);
                                        connection.execute(list3);

                                        List<String> list4 = arrPortList.subList(3 * partitionCount,arrPortList.size());
                                        connection = new Connection(list4);
                                        connection.execute(list4);
                                        Log.e(getClass().getName(),"lsits..." +list1+" "+list2+" "+list3+" "+list4+" ");
                                    }
                                }

                            }

                        } else {
                            ValidationConstant.showValidationDialog(mContext, ValidationConstant.internetMessage);

                        }

                    } else {

                    }
                } else {
                    ValidationConstant.showValidationDialog(mContext, mContext.getString(R.string.Port_range_must_be_between_0_65536));
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 5) {
                    seekBar.setProgress(5);
                    tv_seek_progress.setText("" + 5);
                    seekProgress = 5;
                } else {
                    tv_seek_progress.setText("" + progress);
                    seekProgress = progress;
                }


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * method to show scan dialog
     */
    public void showScanDialog() {

        try {
            if (dialogScan != null && dialogScan.isShowing()) {
                return;
            }

            dialogScan = new Dialog(mContext, R.style.DialogCustomTheme);
            dialogScan.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogScan.setContentView(R.layout.popup_scan);
            dialogScan.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogScan.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

            dialogScan.setCancelable(false);
            TextView tv_ports = (TextView) dialogScan
                    .findViewById(R.id.tv_ports);
            TextView tv_host = (TextView) dialogScan
                    .findViewById(R.id.tv_host);
            tv_ports.setText(strPort);
            tv_host.setText(strHostName + ": ");

            tv_percent = (TextView) dialogScan.findViewById(R.id.tv_percent);
            tv_counter = (TextView) dialogScan.findViewById(R.id.tv_counter);
            final TextView tv_stop_scan = (TextView) dialogScan.findViewById(R.id.tv_stop_scan);
            progressBar = (ProgressBar) dialogScan.findViewById(R.id.progressBar);
            tv_stop_scan.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {

                        if (connection != null)
                           shouldStop = true;
                        dialogScan.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });

            dialogScan.show();


        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    /**
     * method to result on result activity
     */
    public void showResults() {
        Intent intent = new Intent(TestDashboard.this, ScanResult.class);
        intent.putExtra("port_list", arrPortBean);
        intent.putExtra("host_name", strHostName);
        intent.putExtra("port_no", strPort);
        startActivity(intent);
    }

    synchronized public void addBean(PortBean portBean) {
        arrPortBean.add(portBean);
    }

    /**
     * class to convert http and https url to domain name
     */
    private class GetHostName extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            String strHostAddress;
            try {
                URL url = new URL(strHostName);
                Log.e(getClass().getName(), "sona.................." + url.getHost());
                strHostAddress = url.getHost();
                strHostName = strHostAddress;
            } catch (MalformedURLException e) {
                Log.e(getClass().getName(), "sona.................." + e.toString());
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return strHostName;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            showScanDialog();
            shouldStop = false;
            countporttoScan=0;
            if (arrPortList.size() <= 3) {
                taskCounter = VALUE_4;
                connection = new Connection(arrPortList);
                connection.execute(arrPortList);
            } else {
                if (arrPortList.size() >= 4) {
                    taskCounter = 1;
                    VALUE_4 = 4;

                    int partitionCount = arrPortList.size() / 4;
                    List<String> list1 = arrPortList.subList(0 * partitionCount, 1 * partitionCount);
                    connection = new Connection(list1);
                    connection.execute(list1);

                    List<String> list2 = arrPortList.subList(1 * partitionCount, 2 * partitionCount);
                    connection = new Connection(list2);
                    connection.execute(list2);

                    List<String> list3 = arrPortList.subList(2 * partitionCount,3 * partitionCount);
                    connection = new Connection(list3);
                    connection.execute(list3);

                    List<String> list4 = arrPortList.subList(3 * partitionCount,arrPortList.size());
                    connection = new Connection(list4);
                    connection.execute(list4);
                    Log.e(getClass().getName(),"lsits..." +list1+" "+list2+" "+list3+" "+list4+" ");
                }
            }
        }
    }

    /**
     * method to connect with port
     */

    private class Connection extends AsyncTask<List<String>, Integer, Void> {
        int totalNoPorts = 0;


        List<String> arrLocalPort;
        public Connection ( List<String> arrLocalPort1){
            arrLocalPort=arrLocalPort1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            arrPortBean = new ArrayList<>();
            totalNoPorts = arrPortList.size();
            progressBar.setMax(100);
            tv_percent.setText("0%");
            progressBar.setProgress(0);
            tv_counter.setText(0 + "/" + totalNoPorts);

        }

        @Override
        protected Void doInBackground(List<String>... params) {

              arrLocalPort   = params[0];

            for (int i = 0; i < arrLocalPort.size(); i++) {

                if (shouldStop)
                    break;

                try{
                    Socket ServerSok = new Socket(strHostName,i);
                    System.out.println("Port in use: " + i );
                    ServerSok.close();
                    continue;
                }catch (Exception e) {

                }

                Log.e(getClass().getName(), "Port........not in use" + i);

              /*  int counter = i;
                counter++;*/
                countporttoScan=countporttoScan+1;
                publishProgress(countporttoScan);
                continue;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            Log.e(getClass().getName(),"values...................."+taskCounter+" "+VALUE_4);
            if (taskCounter == VALUE_4) {
                if (dialogScan != null && dialogScan.isShowing()) {
                    dialogScan.dismiss();
                    showResults();
                }
            } else {
                taskCounter++;
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            double progressValue = (double) values[0] / totalNoPorts * 100;
            tv_percent.setText((int) progressValue + "%");
            progressBar.setProgress((int) progressValue);
            tv_counter.setText(values[0] + "/" + totalNoPorts);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }
    }


}


