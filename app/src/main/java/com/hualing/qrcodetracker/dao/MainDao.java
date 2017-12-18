package com.hualing.qrcodetracker.dao;

import com.hualing.qrcodetracker.aframework.yoni.ActionRequest;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.bean.LoginParams;
import com.hualing.qrcodetracker.bean.LoginResult;
import com.hualing.qrcodetracker.global.GlobalData;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public interface MainDao {

    @ActionRequest(func = GlobalData.Service.LOGIN)
    ActionResult<LoginResult> login(LoginParams params);


}
