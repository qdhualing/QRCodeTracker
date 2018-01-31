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

public class DataResult {


    private List<DataBean> needToInputDataList ;

    public List<DataBean> getNeedToInputDataList() {
        return needToInputDataList;
    }

    public void setNeedToInputDataList(List<DataBean> needToInputDataList) {
        this.needToInputDataList = needToInputDataList;
    }
}
