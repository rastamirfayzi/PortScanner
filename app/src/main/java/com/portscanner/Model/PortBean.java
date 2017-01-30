package com.portscanner.Model;

import java.io.Serializable;

/**
 * Created by administrator on 17/1/17.
 */

public class PortBean implements Serializable {
    String portNo;
    String portName;
    String portStatus;
    String openPorts;
    String closedPorts;
    String no_of_ports;
    String date;
    String time;
    String hostName;
    String itemType;
    String timeout;
    String scan_no;
    String day;
    String scan_stop_status;

    public String getOpenPorts() {
        return openPorts;
    }

    public void setOpenPorts(String openPorts) {
        this.openPorts = openPorts;
    }

    public String getClosedPorts() {
        return closedPorts;
    }

    public void setClosedPorts(String closedPorts) {
        this.closedPorts = closedPorts;
    }



    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getPortNo() {
        return portNo;
    }

    public void setPortNo(String portNo) {
        this.portNo = portNo;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getPortStatus() {
        return portStatus;
    }

    public void setPortStatus(String portStatus) {
        this.portStatus = portStatus;
    }

    public String getNo_of_ports() {
        return no_of_ports;
    }

    public void setNo_of_ports(String no_of_ports) {
        this.no_of_ports = no_of_ports;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScan_no() {
        return scan_no;
    }

    public void setScan_no(String scan_no) {
        this.scan_no = scan_no;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getScan_stop_status() {
        return scan_stop_status;
    }

    public void setScan_stop_status(String scan_stop_status) {
        this.scan_stop_status = scan_stop_status;
    }

    @Override
    public String toString() {
        return "PortBean{" +
                "portNo='" + portNo + '\'' +
                ", portName='" + portName + '\'' +
                ", portStatus='" + portStatus + '\'' +
                ", openPorts='" + openPorts + '\'' +
                ", closedPorts='" + closedPorts + '\'' +
                ", no_of_ports='" + no_of_ports + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", hostName='" + hostName + '\'' +
                ", itemType='" + itemType + '\'' +
                ", timeout='" + timeout + '\'' +
                ", scan_no='" + scan_no + '\'' +
                ", day='" + day + '\'' +
                ", scan_stop_status='" + scan_stop_status + '\'' +
                '}';
    }
}
