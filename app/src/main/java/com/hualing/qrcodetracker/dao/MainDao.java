package com.hualing.qrcodetracker.dao;

import com.hualing.qrcodetracker.aframework.yoni.ActionRequest;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.bean.DataInputParams;
import com.hualing.qrcodetracker.bean.DataResult;
import com.hualing.qrcodetracker.bean.GetNeedInputedDataParams;
import com.hualing.qrcodetracker.bean.LoginParams;
import com.hualing.qrcodetracker.bean.LoginResult;
import com.hualing.qrcodetracker.bean.MaterialInParams;
import com.hualing.qrcodetracker.bean.MaterialOutParams;
import com.hualing.qrcodetracker.bean.ProductInParams;
import com.hualing.qrcodetracker.bean.ProductOutParams;
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

    @ActionRequest(func = GlobalData.Service.GET_INPUTED_DATA)
    ActionResult<DataResult> getNeedInputedData(GetNeedInputedDataParams params);

    @ActionRequest(func = GlobalData.Service.COMMIT_INPUTED_DATA)
    ActionResult<ActionResult> commitInputedData(DataInputParams params);

    @ActionRequest(func = GlobalData.Service.MATERAIL_IN)
    ActionResult<ActionResult> materialIn(MaterialInParams params);

    @ActionRequest(func = GlobalData.Service.MATERAIL_OUT)
    ActionResult<ActionResult> materialOut(MaterialOutParams params);

    @ActionRequest(func = GlobalData.Service.PRODUCT_IN)
    ActionResult<ActionResult> productIn(ProductInParams params);

    @ActionRequest(func = GlobalData.Service.PRODUCT_OUT)
    ActionResult<ActionResult> productOut(ProductOutParams params);




}
