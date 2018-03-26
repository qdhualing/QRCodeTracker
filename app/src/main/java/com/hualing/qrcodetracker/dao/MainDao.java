package com.hualing.qrcodetracker.dao;

import com.hualing.qrcodetracker.aframework.yoni.ActionRequest;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.bean.BCPCKDResult;
import com.hualing.qrcodetracker.bean.BCPINParam;
import com.hualing.qrcodetracker.bean.BCPRKDResult;
import com.hualing.qrcodetracker.bean.BCPTKDResult;
import com.hualing.qrcodetracker.bean.BCPTKGetShowDataParam;
import com.hualing.qrcodetracker.bean.BCPTKParam;
import com.hualing.qrcodetracker.bean.BCPTKShowDataResult;
import com.hualing.qrcodetracker.bean.BcpThrowGetShowDataParam;
import com.hualing.qrcodetracker.bean.BcpThrowParam;
import com.hualing.qrcodetracker.bean.BcpThrowShowDataResult;
import com.hualing.qrcodetracker.bean.BcpTrackResult;
import com.hualing.qrcodetracker.bean.BigCpOutGetDataParam;
import com.hualing.qrcodetracker.bean.BigCpOutGetDataResult;
import com.hualing.qrcodetracker.bean.BigCpOutParam;
import com.hualing.qrcodetracker.bean.BigCpResult;
import com.hualing.qrcodetracker.bean.CJResult;
import com.hualing.qrcodetracker.bean.BigCPINParam;
import com.hualing.qrcodetracker.bean.CreateBCPCKDParam;
import com.hualing.qrcodetracker.bean.CreateBCPRKDParam;
import com.hualing.qrcodetracker.bean.CreateBCPTKDParam;
import com.hualing.qrcodetracker.bean.CreateWLCKDParam;
import com.hualing.qrcodetracker.bean.CreateWLRKDParam;
import com.hualing.qrcodetracker.bean.CreateWLTKDParam;
import com.hualing.qrcodetracker.bean.DataInputParams;
import com.hualing.qrcodetracker.bean.DataResult;
import com.hualing.qrcodetracker.bean.GXResult;
import com.hualing.qrcodetracker.bean.GetGXParam;
import com.hualing.qrcodetracker.bean.GetNeedInputedDataParams;
import com.hualing.qrcodetracker.bean.GetSXYLParam;
import com.hualing.qrcodetracker.bean.HlSortResult;
import com.hualing.qrcodetracker.bean.LoginParams;
import com.hualing.qrcodetracker.bean.LoginResult;
import com.hualing.qrcodetracker.bean.MainParams;
import com.hualing.qrcodetracker.bean.MainResult;
import com.hualing.qrcodetracker.bean.MaterialInParams;
import com.hualing.qrcodetracker.bean.MaterialOutParams;
import com.hualing.qrcodetracker.bean.PdtSortResult;
import com.hualing.qrcodetracker.bean.ProductInParams;
import com.hualing.qrcodetracker.bean.ProductOutParams;
import com.hualing.qrcodetracker.bean.SXYLResult;
import com.hualing.qrcodetracker.bean.SmallCPINParam;
import com.hualing.qrcodetracker.bean.SmallCpOutGetDataParam;
import com.hualing.qrcodetracker.bean.SmallCpOutGetDataResult;
import com.hualing.qrcodetracker.bean.SmallCpOutParam;
import com.hualing.qrcodetracker.bean.UserGroupResult;
import com.hualing.qrcodetracker.bean.WLCKDResult;
import com.hualing.qrcodetracker.bean.WLINParam;
import com.hualing.qrcodetracker.bean.WLOutGetShowDataParam;
import com.hualing.qrcodetracker.bean.WLOutParam;
import com.hualing.qrcodetracker.bean.WLOutShowDataResult;
import com.hualing.qrcodetracker.bean.WLRKDResult;
import com.hualing.qrcodetracker.bean.WLTKDResult;
import com.hualing.qrcodetracker.bean.WLTKGetShowDataParam;
import com.hualing.qrcodetracker.bean.WLTKParam;
import com.hualing.qrcodetracker.bean.WLTKShowDataResult;
import com.hualing.qrcodetracker.bean.WLThrowGetShowDataParam;
import com.hualing.qrcodetracker.bean.WLThrowParam;
import com.hualing.qrcodetracker.bean.WLThrowShowDataResult;
import com.hualing.qrcodetracker.bean.WlTrackParam;
import com.hualing.qrcodetracker.bean.WlTrackResult;
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

    @ActionRequest(func = GlobalData.Service.GET_MAIN_DATA)
    ActionResult<MainResult> getMainData(MainParams params);

    @ActionRequest(func = GlobalData.Service.COMMIT_MATERIALIN_INPUTED_DATA)
    ActionResult<ActionResult> commitMaterialInInputedData(WLINParam params);

    @ActionRequest(func = GlobalData.Service.CREATE_WL_RKD)
    ActionResult<WLRKDResult> createWL_RKD(CreateWLRKDParam params);

    @ActionRequest(func = GlobalData.Service.CREATE_WL_CKD)
    ActionResult<WLCKDResult> createWL_CKD(CreateWLCKDParam params);

    @ActionRequest(func = GlobalData.Service.GET_PDT_SORT)
    ActionResult<PdtSortResult> getPdtSort();

    @ActionRequest(func = GlobalData.Service.GET_HL_SORT)
    ActionResult<HlSortResult> getHlSort();

    @ActionRequest(func = GlobalData.Service.GET_WL_OUT_SHOW_DATA)
    ActionResult<WLOutShowDataResult> getWlOutShowData(WLOutGetShowDataParam getParam);

    @ActionRequest(func = GlobalData.Service.WL_OUT)
    ActionResult<ActionResult> wlOut(WLOutParam params);

    @ActionRequest(func = GlobalData.Service.GET_DEPARTMENT_DATA)
    ActionResult<UserGroupResult> getDepartmentData();

    @ActionRequest(func = GlobalData.Service.CREATE_WL_TKD)
    ActionResult<WLTKDResult> createWL_TKD(CreateWLTKDParam params);

    @ActionRequest(func = GlobalData.Service.GET_WL_TK_SHOW_DATA)
    ActionResult<WLTKShowDataResult> getWlTKShowData(WLTKGetShowDataParam getParam);

    @ActionRequest(func = GlobalData.Service.WL_TK)
    ActionResult<ActionResult> wlTK(WLTKParam params);

    @ActionRequest(func = GlobalData.Service.GET_WL_THROW_SHOW_DATA)
    ActionResult<WLThrowShowDataResult> getWlThrowShowData(WLThrowGetShowDataParam getParam);

    @ActionRequest(func = GlobalData.Service.WL_THROW)
    ActionResult<ActionResult> wlThrow(WLThrowParam params);

    @ActionRequest(func = GlobalData.Service.GET_GX)
    ActionResult<GXResult> getGX(GetGXParam param);

    @ActionRequest(func = GlobalData.Service.GET_CJ)
    ActionResult<CJResult> getCJData();

    @ActionRequest(func = GlobalData.Service.CREATE_BCP_RKD)
    ActionResult<BCPRKDResult> createBCP_RKD(CreateBCPRKDParam params);

    @ActionRequest(func = GlobalData.Service.BCP_IN)
    ActionResult<ActionResult> commitBCPInInputedData(BCPINParam params);

    @ActionRequest(func = GlobalData.Service.GET_TLYL)
    ActionResult<SXYLResult> getSXYL(GetSXYLParam param);

    @ActionRequest(func = GlobalData.Service.GET_BCP_THROW_SHOW_DATA)
    ActionResult<BcpThrowShowDataResult> getBcpThrowShowData(BcpThrowGetShowDataParam getParam);

    @ActionRequest(func = GlobalData.Service.BCP_THROW)
    ActionResult<ActionResult> bcpThrow(BcpThrowParam params);

    @ActionRequest(func = GlobalData.Service.CREATE_BCP_TKD)
    ActionResult<BCPTKDResult> createBCP_TKD(CreateBCPTKDParam params);

    @ActionRequest(func = GlobalData.Service.GET_BCP_TK_SHOW_DATA)
    ActionResult<BCPTKShowDataResult> getBCPTKShowData(BCPTKGetShowDataParam getParam);

    @ActionRequest(func = GlobalData.Service.BCP_TK)
    ActionResult<ActionResult> bcpTK(BCPTKParam param);

    @ActionRequest(func = GlobalData.Service.CREATE_BCP_CKD)
    ActionResult<BCPCKDResult> createBCP_CKD(CreateBCPCKDParam params);

    @ActionRequest(func = GlobalData.Service.GET_LEI_BIE)
    ActionResult<PdtSortResult> getLeiBie();

    @ActionRequest(func = GlobalData.Service.BIG_CP_IN)
    ActionResult<ActionResult> commitBigCPInInputedData(BigCPINParam params);

    @ActionRequest(func = GlobalData.Service.SMALL_CP_IN)
    ActionResult<ActionResult> commitSmallCPInInputedData(SmallCPINParam params);

    @ActionRequest(func = GlobalData.Service.GET_BIG_CP_DATA)
    ActionResult<BigCpResult> getBigCpData();

    @ActionRequest(func = GlobalData.Service.BIG_CP_OUT)
    ActionResult<ActionResult> bigCpOut(BigCpOutParam params);

    @ActionRequest(func = GlobalData.Service.GET_BIG_CP_OUT_DATA)
    ActionResult<BigCpOutGetDataResult> getBigCpOutData(BigCpOutGetDataParam bigCpOutGetDataParam);

    @ActionRequest(func = GlobalData.Service.GET_SMALL_CP_OUT_DATA)
    ActionResult<SmallCpOutGetDataResult> getSmallCpOutData(SmallCpOutGetDataParam smallCpOutGetDataParam);

    @ActionRequest(func = GlobalData.Service.SMALL_CP_OUT)
    ActionResult<ActionResult> smallCpOut(SmallCpOutParam params);

    @ActionRequest(func = GlobalData.Service.WL_TRACK)
    ActionResult<WlTrackResult> getWlTrackShowData(WlTrackParam param);

    @ActionRequest(func = GlobalData.Service.BCP_TRACK)
    ActionResult<BcpTrackResult> getBcpTrackShowData(WlTrackParam param);
}
