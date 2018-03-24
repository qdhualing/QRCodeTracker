package com.hualing.qrcodetracker.model;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class LocalShowBean {

    private int ID;
    private String QRCodeID;
    private String ProductName;
    private boolean flag ;

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getQRCodeID() {
        return QRCodeID;
    }

    public void setQRCodeID(String QRCodeID) {
        this.QRCodeID = QRCodeID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

}
