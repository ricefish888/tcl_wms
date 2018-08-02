package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XStreamCDATA;
import com.vtradex.wms.server.service.annotation.XmlDataNote;

/** SAP接口 工厂下的仓库 */
@XmlDataNote("SapWarehouse")
public class SapWarehouse  {
	
	private String LINENO; //CHAR(32) 行号
	private String LGORT; //	CHAR(04)	仓库号
	private String WERKS; //	CHAR(04)	工厂
	
	@XStreamCDATA   //有特殊字符  需要CDATA
	private String LGOBE; //	CHAR(16)	仓库描述
	private String DISKZ; //	CHAR(01)	MRP标识
	public String getLINENO() {
		return LINENO;
	}
	public void setLINENO(String lINENO) {
		LINENO = lINENO;
	}
	public String getLGORT() {
		return LGORT;
	}
	public void setLGORT(String lGORT) {
		LGORT = lGORT;
	}
	public String getWERKS() {
		return WERKS;
	}
	public void setWERKS(String wERKS) {
		WERKS = wERKS;
	}
	public String getLGOBE() {
		return LGOBE;
	}
	public void setLGOBE(String lGOBE) {
		LGOBE = lGOBE;
	}
	public String getDISKZ() {
		return DISKZ;
	}
	public void setDISKZ(String dISKZ) {
		DISKZ = dISKZ;
	}
	
	
	

	
	 

}
