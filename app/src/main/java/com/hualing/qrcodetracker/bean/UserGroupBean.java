package com.hualing.qrcodetracker.bean;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class UserGroupBean {

    private int groupID;
    private int isAdmin;
    private String groupName;
    private String groupDesc;
    private int groupStatus;
    private String groupMenu;
    private String groupSort;
    private int groupOrder;
    private int groupCode;
    private String authMobile;
    private String authComputer;

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public int getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(int groupStatus) {
        this.groupStatus = groupStatus;
    }

    public String getGroupMenu() {
        return groupMenu;
    }

    public void setGroupMenu(String groupMenu) {
        this.groupMenu = groupMenu;
    }

    public String getGroupSort() {
        return groupSort;
    }

    public void setGroupSort(String groupSort) {
        this.groupSort = groupSort;
    }

    public int getGroupOrder() {
        return groupOrder;
    }

    public void setGroupOrder(int groupOrder) {
        this.groupOrder = groupOrder;
    }

    public int getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(int groupCode) {
        this.groupCode = groupCode;
    }

    public String getAuthMobile() {
        return authMobile;
    }

    public void setAuthMobile(String authMobile) {
        this.authMobile = authMobile;
    }

    public String getAuthComputer() {
        return authComputer;
    }

    public void setAuthComputer(String authComputer) {
        this.authComputer = authComputer;
    }
}
