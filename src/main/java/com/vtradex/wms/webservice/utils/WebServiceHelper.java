package com.vtradex.wms.webservice.utils;

import java.util.List;
import java.util.Set;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.webservice.model.SapInterfaceStatus;
import com.vtradex.wms.webservice.sap.base.SapCommonCallback;
import com.vtradex.wms.webservice.sap.base.SapCommonCallbackDetail;
import com.vtradex.wms.webservice.sap.base.StandardResponseResult;
import com.vtradex.wms.webservice.sap.model.SapResponse;

public class WebServiceHelper {
	
	public static void println(Object obj) {
		System.out.println(obj);
	}
	public static String setToString(Set<String> infos) {
		StringBuffer sb = new StringBuffer("");
		
		for(String s : infos) {
			sb.append(s+"|");
		}
		String result = sb.toString();
		
		if(!StringHelper.isNullOrEmpty(result)) {
			if(result.endsWith("|")) {
				result = StringHelper.deleteLastChar(result);
			}
		}
		return result;
	}
	public static SapResponse getSapSucessResponse() {
		return getSapResponse(StandardResponseResult.SUCCESS);
	}
	public static SapResponse getSapFailResponse() {
		return getSapResponse(StandardResponseResult.ERROR);
	}
	private static SapResponse getSapResponse(String result) {
		SapResponse s = new SapResponse();
		s.setRETURN_RESULT(result);
		return s;
	}
	
	/***组装一个失败的SapCommonCallbackDetail*/
	public static SapCommonCallbackDetail getSapCommonCallbackDetailError(String lineNo,String errorInfo) {
		return getSapCommonCallbackDetail(lineNo,SapInterfaceStatus.SAP_COMMONCALLBACK_FLG_FAIL,errorInfo);
	}
	/***组装一个成功的SapCommonCallbackDetail*/
	public static SapCommonCallbackDetail getSapCommonCallbackDetailSucess(String lineNo) {
		return getSapCommonCallbackDetail(lineNo,SapInterfaceStatus.SAP_COMMONCALLBACK_FLG_SUCESS,"处理成功");
	}
	
	private static SapCommonCallbackDetail getSapCommonCallbackDetail(String lineNo, String flag,String errorInfo) {
		SapCommonCallbackDetail d = new SapCommonCallbackDetail();
		d.setLineNo(lineNo); //0标识头项目出问题
		d.setFlag(flag);
		d.setRemark(StringHelper.substring(errorInfo, 200)); //异常信息
		return d;
	}
	
	/***组装一个返回给sap的callback*/
	public static SapCommonCallback getCommonCallbackResponse(String messageId,String itype,List<SapCommonCallbackDetail> details) {
		SapCommonCallback commCallback = new SapCommonCallback();
		try {
		commCallback.setMessageId(messageId);
		commCallback.setItype(itype);
		SapCommonCallbackDetail[] ds =  details.toArray(new SapCommonCallbackDetail[]{});
		commCallback.setSapCommonCallbackDetails(ds);
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
		
		return commCallback;
	}
	
	/**获取公共回传接口的头项目就错误的报文*/
	public static SapCommonCallback getCommonCallbackHeadErrorResponse(String itype,String messageId,String errorInfo) {
		SapCommonCallback commCallback = new SapCommonCallback();
		commCallback.setItype(itype);
		commCallback.setMessageId(messageId);
		SapCommonCallbackDetail d = getSapCommonCallbackDetailError("0",errorInfo);
	 
		SapCommonCallbackDetail[] ds = new SapCommonCallbackDetail[1];
		ds[0]=d;
		commCallback.setSapCommonCallbackDetails(ds);
		return commCallback;
	}

}
