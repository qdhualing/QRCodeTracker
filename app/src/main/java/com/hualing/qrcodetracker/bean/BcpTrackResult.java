package com.hualing.qrcodetracker.bean;

import java.util.List;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class BcpTrackResult {

    private String productName	;
    private String sortName	;
    private String bcpCode	;
    private String ylpc	;
    private String scpc	;
    private String scTime	;
    private String dw	;
    private float dwzl	;
    private String cheJian	;
    private String gx	;
    private String gg	;
    private String czy	;
    private String zjy	;
    private String jyzt	;

    //组成成分
    private List<ComponentBean> componentBeans ;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getBcpCode() {
        return bcpCode;
    }

    public void setBcpCode(String bcpCode) {
        this.bcpCode = bcpCode;
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

    public String getScTime() {
        return scTime;
    }

    public void setScTime(String scTime) {
        this.scTime = scTime;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    public float getDwzl() {
        return dwzl;
    }

    public void setDwzl(float dwzl) {
        this.dwzl = dwzl;
    }

    public String getCheJian() {
        return cheJian;
    }

    public void setCheJian(String cheJian) {
        this.cheJian = cheJian;
    }

    public String getGx() {
        return gx;
    }

    public void setGx(String gx) {
        this.gx = gx;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    public String getCzy() {
        return czy;
    }

    public void setCzy(String czy) {
        this.czy = czy;
    }

    public String getZjy() {
        return zjy;
    }

    public void setZjy(String zjy) {
        this.zjy = zjy;
    }

    public String getJyzt() {
        return jyzt;
    }

    public void setJyzt(String jyzt) {
        this.jyzt = jyzt;
    }

    public List<ComponentBean> getComponentBeans() {
        return componentBeans;
    }

    public void setComponentBeans(List<ComponentBean> componentBeans) {
        this.componentBeans = componentBeans;
    }
}
