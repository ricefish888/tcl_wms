package com.vtradex.wms.webservice.sap.base;

import com.vtradex.wms.server.service.annotation.XmlDataNote;
import com.vtradex.wms.webservice.model.SapInterfaceType;

/***
 * 组装自己的回调sap公共接口需要的对象  
 * 组装后会将对象转成xml存在interfacelog的responsexml中
 * 请求是取出来转成SAP需要的对象回调给sap
 * 
 * @author administrator
 *
 */
@XmlDataNote("SapCommonCallback")
public class SapCommonCallback {
	
	/**
	 * {@link SapInterfaceType}
	 * */
	private String itype; //接口类型
	
	private String messageId; 
	
	private SapCommonCallbackDetail[] sapCommonCallbackDetails;
	
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getItype() {
		return itype;
	}

	public void setItype(String itype) {
		this.itype = itype;
	}

	public SapCommonCallbackDetail[] getSapCommonCallbackDetails() {
		return sapCommonCallbackDetails;
	}

	public void setSapCommonCallbackDetails(SapCommonCallbackDetail[] sapCommonCallbackDetails) {
		this.sapCommonCallbackDetails = sapCommonCallbackDetails;
	}
	
	
	
	

}
