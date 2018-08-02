package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XmlDataNote;

/** 
* @ClassName: Sap销售外向交货单数组
* @Description: SapSaleOutDeliveryArray 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-13 上午9:51:23  
*/
@XmlDataNote("SapSaleOutDeliveryArray")
public class SapSaleOutDeliveryArray {
	private SapSaleOutDelivery[] ssods;
	
	private String ROWCNT;
	
	private String MESSAGEID;
	
	private String TYPE;

	public SapSaleOutDelivery[] getSsods() {
		return ssods;
	}

	public void setSsods(SapSaleOutDelivery[] ssods) {
		this.ssods = ssods;
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
