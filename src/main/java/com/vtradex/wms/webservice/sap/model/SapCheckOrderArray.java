package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XmlDataNote;

/** 
* @ClassName: 采购对账单数组
* @Description: SapCheckOrderArray 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-14 下午5:22:04  
*/
@XmlDataNote("SapCheckOrderArray")
public class SapCheckOrderArray {
	private SapCheckOrder[] scos;
	
	private String ROWCNT;
	
	private String MESSAGEID;
	
	private String TYPE;

	public SapCheckOrder[] getScos() {
		return scos;
	}

	public void setScos(SapCheckOrder[] scos) {
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
