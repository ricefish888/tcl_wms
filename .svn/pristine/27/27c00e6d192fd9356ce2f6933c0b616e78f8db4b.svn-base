package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XStreamCDATA;
import com.vtradex.wms.server.service.annotation.XmlDataNote;

/** 
* @ClassName:  生产入库订单对象
* @Description: SapProductOrderIn
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-18 上午10:30:37  
*/
@XmlDataNote("SapProductOrderIn")
public class SapProductOrderIn {
	private String LINENO; // CHAR(32) 行号
	
	private String   MBLNR;		//CHAR(10)	物料凭证号	必输	
	private String   AUFNR;		//CHAR(12)	生产订单号	必输	
	private String   MENGE;		//QUAN(17)	入库数量	必输	01为新增02为更改03为删除
	private String   MATNR;		//CHAR(18)	原材料编码	必输	JIT料的原材料编码
	@XStreamCDATA
	private String MENGE02;		//QUAN(17)	JIT建议反冲数量	必输	JIT料的反冲数量
	public String getLINENO() {
		return LINENO;
	}
	public void setLINENO(String lINENO) {
		LINENO = lINENO;
	}
	public String getMBLNR() {
		return MBLNR;
	}
	public void setMBLNR(String mBLNR) {
		MBLNR = mBLNR;
	}
	public String getAUFNR() {
		return AUFNR;
	}
	public void setAUFNR(String aUFNR) {
		AUFNR = aUFNR;
	}
	public String getMENGE() {
		return MENGE;
	}
	public void setMENGE(String mENGE) {
		MENGE = mENGE;
	}
	public String getMATNR() {
		return MATNR;
	}
	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}
	public String getMENGE02() {
		return MENGE02;
	}
	public void setMENGE02(String mENGE02) {
		MENGE02 = mENGE02;
	}
}
