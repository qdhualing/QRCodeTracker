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

public class WlOutVerifyResult {

    private String outDh;
    private String lhDw;
    private String lhRq;
    private String lhR;
    private String lhFzr;
    private String fhFzr;
    private String remark;

    private List<WLOutShowBean> beans;

    public String getOutDh() {
        return outDh;
    }

    public void setOutDh(String outDh) {
        this.outDh = outDh;
    }

    public String getLhDw() {
        return lhDw;
    }

    public void setLhDw(String lhDw) {
        this.lhDw = lhDw;
    }

    public String getLhRq() {
        return lhRq;
    }

    public void setLhRq(String lhRq) {
        this.lhRq = lhRq;
    }

    public String getLhR() {
        return lhR;
    }

    public void setLhR(String lhR) {
        this.lhR = lhR;
    }

    public String getLhFzr() {
        return lhFzr;
    }

    public void setLhFzr(String lhFzr) {
        this.lhFzr = lhFzr;
    }

    public String getFhFzr() {
        return fhFzr;
    }

    public void setFhFzr(String fhFzr) {
        this.fhFzr = fhFzr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<WLOutShowBean> getBeans() {
        return beans;
    }

    public void setBeans(List<WLOutShowBean> beans) {
        this.beans = beans;
    }
}
