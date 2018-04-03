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

public class BcpInVerifyResult {

    private String inDh;
    private String jhDw;
    private String shRq;
    private String jhR;
    private String jhFzr;
    private String shFzr;
    private String remark;

    private List<BcpInShowBean> beans;

    public String getInDh() {
        return inDh;
    }

    public void setInDh(String inDh) {
        this.inDh = inDh;
    }

    public String getJhDw() {
        return jhDw;
    }

    public void setJhDw(String jhDw) {
        this.jhDw = jhDw;
    }

    public String getShRq() {
        return shRq;
    }

    public void setShRq(String shRq) {
        this.shRq = shRq;
    }

    public String getJhR() {
        return jhR;
    }

    public void setJhR(String jhR) {
        this.jhR = jhR;
    }

    public String getJhFzr() {
        return jhFzr;
    }

    public void setJhFzr(String jhFzr) {
        this.jhFzr = jhFzr;
    }

    public String getShFzr() {
        return shFzr;
    }

    public void setShFzr(String shFzr) {
        this.shFzr = shFzr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<BcpInShowBean> getBeans() {
        return beans;
    }

    public void setBeans(List<BcpInShowBean> beans) {
        this.beans = beans;
    }
}
