package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XmlDataNote;

/** 
* @ClassName: 采购交货单数组
* @Description: SapPurchaseOrderArray 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-5 下午2:12:21  
*/
@XmlDataNote("SapDeliveryOrderArray")
public class SapDeliveryOrderArray {
	private SapDeliveryOrder [] spoas;
	private String ROWCNT;
	private String MESSAGEID;
	private String TYPE; //	CHAR(02)	操作类型	必输	I为新增U为更改D为删除
	public SapDeliveryOrder[] getSpoas() {
		return spoas;
	}
	public void setSpoas(SapDeliveryOrder[] spoas) {
		this.spoas = spoas;
	}
	public String getROWCNT() {
		return ROWCNT;
	}
	public void setROWCNT(String rOWCNT) {
		ROWCNT = rOWCNT;
	}
	public String getMESSAGEID() {
		return MESSAGEID;
	}
	public void setMESSAGEID(String mESSAGEID) {
		MESSAGEID = mESSAGEID;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	
}
