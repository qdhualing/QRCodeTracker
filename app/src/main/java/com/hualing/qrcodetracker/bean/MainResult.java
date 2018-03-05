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

public class MainResult {

    private String departmentName;

    private List<Module2> canUseFunctionList;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }


    public List<Module2> getCanUseFunctionList() {
        return canUseFunctionList;
    }

    public void setCanUseFunctionList(List<Module2> canUseFunctionList) {
        this.canUseFunctionList = canUseFunctionList;
    }
}
