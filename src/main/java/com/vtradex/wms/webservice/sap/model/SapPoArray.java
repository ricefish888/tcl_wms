package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XmlDataNote;
 
/***
 * SAP接口  采购订单  数组
 * @author administrator
 *
 */
@XmlDataNote("SapPoArray")
public class SapPoArray {
	
	private SapPo[] sapPos;
	
	private String TYPE; //	CHAR(02)	操作类型	必输	01为新增02为更改
	
	private String ROWCNT;
	
	private String MESSAGEID;

	public SapPo[] getSapPos() {
		return sapPos;
	}

	public void setSapPos(SapPo[] sapPos) {
		this.sapPos = sapPos;
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
