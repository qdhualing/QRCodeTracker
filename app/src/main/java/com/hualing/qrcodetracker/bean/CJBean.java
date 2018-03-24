package com.hualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class CJBean {

    private int id;
    private String cName;
    private int cOrder ;
    private String cGongXu;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public int getcOrder() {
        return cOrder;
    }

    public void setcOrder(int cOrder) {
        this.cOrder = cOrder;
    }

    public String getcGongXu() {
        return cGongXu;
    }

    public void setcGongXu(String cGongXu) {
        this.cGongXu = cGongXu;
    }
}
