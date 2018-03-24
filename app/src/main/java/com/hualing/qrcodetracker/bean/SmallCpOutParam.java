package com.hualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class SmallCpOutParam {
    private String qrCodeId ;

    private String remark;

    private String cpCode;
    private String cpName;
    private int sortId;
    private String ylpc;
    private String scpc;
    private float dwzl;
    private String fhDate;
    private String fhr;
    private String outDh;

    public String getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCpCode() {
        return cpCode;
    }

    public void setCpCode(String cpCode) {
        this.cpCode = cpCode;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public String getYlpc() {
        return ylpc;
    }

    public void setYlpc(String ylpc) {
        this.ylpc = ylpc;
    }

    public String getScpc() {
        return scpc;
    }

    public void setScpc(String scpc) {
        this.scpc = scpc;
    }

    public float getDwzl() {
        return dwzl;
    }

    public void setDwzl(float dwzl) {
        this.dwzl = dwzl;
    }

    public String getFhDate() {
        return fhDate;
    }

    public void setFhDate(String fhDate) {
        this.fhDate = fhDate;
    }

    public String getFhr() {
        return fhr;
    }

    public void setFhr(String fhr) {
        this.fhr = fhr;
    }

    public String getOutDh() {
        return outDh;
    }

    public void setOutDh(String outDh) {
        this.outDh = outDh;
    }
}
