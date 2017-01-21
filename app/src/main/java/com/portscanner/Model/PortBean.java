package com.portscanner.Model;

import java.io.Serializable;

/**
 * Created by administrator on 17/1/17.
 */

public class PortBean implements Serializable {
    String portNo;
    String portName;
    String portStatus;

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
}
