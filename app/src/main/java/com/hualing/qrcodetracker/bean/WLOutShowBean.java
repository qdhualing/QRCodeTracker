package com.hualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class WLOutShowBean {

    //二维码编号
    private String qRCodeID;
    //物料编码
    private String wLCode;
    //货物名称
    private String productName;
    //类别
    private int sortID;
    //原料批次
    private String yLPC;
    //数量
    private float shl ;
    //单位重量
    private float dWZL;
    //单位
    private String dW ;
    //规格
    private String gG;
    //出库单号
    private String outDh;
    //来料时间（入库时间）
    private String time;
    //标志位
    private int bz;

    public String getqRCodeID() {
        return qRCodeID;
    }

    public void setqRCodeID(String qRCodeID) {
        this.qRCodeID = qRCodeID;
    }

    public String getwLCode() {
        return wLCode;
    }

    public void setwLCode(String wLCode) {
        this.wLCode = wLCode;
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

    public String getdW() {
        return dW;
    }

    public void setdW(String dW) {
        this.dW = dW;
    }

    public String getgG() {
        return gG;
    }

    public void setgG(String gG) {
        this.gG = gG;
    }

    public String getOutDh() {
        return outDh;
    }

    public void setOutDh(String outDh) {
        this.outDh = outDh;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getBz() {
        return bz;
    }

    public void setBz(int bz) {
        this.bz = bz;
    }
}
