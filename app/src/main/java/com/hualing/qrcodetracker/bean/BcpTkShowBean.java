package com.hualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class BcpTkShowBean {

    //二维码编号
    private String qRCodeID;
    //物料编码
    private String bcpCode;
    //货物名称
    private String productName;
    //类别
    private int sortID;
    //原料批次
    private String yLPC;
    private String sCPC;
    private String scTime;
    //数量
    private float shl ;
    //单位重量
    private float dWZL;
    //出库单号
    private String backDh;
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getqRCodeID() {
        return qRCodeID;
    }

    public void setqRCodeID(String qRCodeID) {
        this.qRCodeID = qRCodeID;
    }

    public String getBcpCode() {
        return bcpCode;
    }

    public void setBcpCode(String bcpCode) {
        this.bcpCode = bcpCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getScTime() {
        return scTime;
    }

    public void setScTime(String scTime) {
        this.scTime = scTime;
    }

    public float getShl() {
        return shl;
    }

    public void setShl(float shl) {
        this.shl = shl;
    }

    public float getdWZL() {
        return dWZL;
    }

    public void setdWZL(float dWZL) {
        this.dWZL = dWZL;
    }

    public String getBackDh() {
        return backDh;
    }

    public void setBackDh(String backDh) {
        this.backDh = backDh;
    }
}
