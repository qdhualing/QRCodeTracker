package com.hualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class CpOutShowBean {

    //二维码编号
    private String qRCodeID;
    //物料编码
    private String cpCode;
    //货物名称
    private String cpName;
    //类别
    private int sortID;
    //原料批次
    private String yLPC;
    private String sCPC;
    //单位重量
    private float dWZL;
    //出库单号
    private String outDh;
    //来料时间（入库时间）
    private String fHDate;

    public String getqRCodeID() {
        return qRCodeID;
    }

    public void setqRCodeID(String qRCodeID) {
        this.qRCodeID = qRCodeID;
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

    public int getSortID() {
        return sortID;
    }

    public void setSortID(int sortID) {
        this.sortID = sortID;
    }

    public String getyLPC() {
        return yLPC;
    }

    public void setyLPC(String yLPC) {
        this.yLPC = yLPC;
    }

    public String getsCPC() {
        return sCPC;
    }

    public void setsCPC(String sCPC) {
        this.sCPC = sCPC;
    }

    public float getdWZL() {
        return dWZL;
    }

    public void setdWZL(float dWZL) {
        this.dWZL = dWZL;
    }

    public String getOutDh() {
        return outDh;
    }

    public void setOutDh(String outDh) {
        this.outDh = outDh;
    }

    public String getfHDate() {
        return fHDate;
    }

    public void setfHDate(String fHDate) {
        this.fHDate = fHDate;
    }
}
