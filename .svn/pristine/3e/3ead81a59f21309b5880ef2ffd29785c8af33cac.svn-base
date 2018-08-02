package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XStreamCDATA;
import com.vtradex.wms.server.service.annotation.XmlDataNote;

/**
 * 成本中心主数据对象
 * @author Administrator
 *
 */
@XmlDataNote("SapCostCenter")
public class SapCostCenter {

	private String KOSTL;//	CHAR(10)	成本中心
	private String KTEXT;//	CHAR(20)	成本中心名称
	@XStreamCDATA
	private String DATBI;//	DATS(10)	有效截止至
	
	private String BKZKP;// CHAR(10)    冻结标识
	
	public String getBKZKP() {
		return BKZKP;
	}
	public void setBKZKP(String bKZKP) {
		BKZKP = bKZKP;
	}
	public String getKOSTL() {
		return KOSTL;
	}
	public void setKOSTL(String kOSTL) {
		KOSTL = kOSTL;
	}
	public String getKTEXT() {
		return KTEXT;
	}
	public void setKTEXT(String kTEXT) {
		KTEXT = kTEXT;
	}
	public String getDATBI() {
		return DATBI;
	}
	public void setDATBI(String dATBI) {
		DATBI = dATBI;
	}
	
}
