package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XmlDataNote;

/** 
* @ClassName:  采购对账单寄售数组
* @Description: SapJSCheckOrderArray
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-14 下午5:44:44  
*/
@XmlDataNote("SapJSCheckOrderArray")
public class SapJSCheckOrderArray {
	
	private SapJSCheckOrder[] scos;
	
	private String ROWCNT;
	
	private String MESSAGEID;
	
	private String TYPE;

	public SapJSCheckOrder[] getScos() {
		return scos;
	}

	public void setScos(SapJSCheckOrder[] scos) {
		this.scos = scos;
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
