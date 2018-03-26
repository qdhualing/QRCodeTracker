package com.hualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des 半成品及以上组成成分bean
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class ComponentBean {

    private String name ;

    private String qrCodeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }
}
