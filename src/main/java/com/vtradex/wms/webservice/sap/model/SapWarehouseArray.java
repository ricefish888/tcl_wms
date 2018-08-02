package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XmlDataNote;
 
/***
 * SAP接口  仓库主数据  数组
 * @author administrator
 *
 */
@XmlDataNote("SapWarehouseArray")
public class SapWarehouseArray {
	
	private SapWarehouse[] sapWarehouses;
	
	private String ROWCNT;
	
	private String MESSAGEID;
	
	private String type;//I新增 U修改

	public SapWarehouse[] getSapWarehouses() {
		return sapWarehouses;
	}

	public void setSapWarehouses(SapWarehouse[] sapWarehouses) {
		this.sapWarehouses = sapWarehouses;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
