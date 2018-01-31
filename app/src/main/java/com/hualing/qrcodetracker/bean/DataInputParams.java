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

public class DataInputParams {

    private List<DataBean> needToInputDataList ;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<DataBean> getNeedToInputDataList() {
        return needToInputDataList;
    }

    public void setNeedToInputDataList(List<DataBean> needToInputDataList) {
        this.needToInputDataList = needToInputDataList;
    }

}
