package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XStreamCDATA;
import com.vtradex.wms.server.service.annotation.XmlDataNote;

/** SAP接口 采购订单 */
@XmlDataNote("SapPo")
public class SapPo  {

	private String LINENO; //CHAR(32) 行号
	
	private String EBELN; //	CHAR(10)	采购订单号	必输	

	private String BSART; //	CHAR(04)	采购订单类型	必输	
	private String AEDAT; //	DATS(10)	订单创建日期	必输	  20170701这样的字样
	private String ERNAM; //	CHAR(12)	订单创建人	必输	
	private String LIFNR; //	CHAR(10)	供应商编码	必输	
	private String EKORG; //	CHAR(04)	采购组织	必输	
	private String EKGRP; //	CHAR(03)	采购组	必输	
	@XStreamCDATA   //有特殊字符  需要CDATA
	private String EBELP; //	CHAR(05)	项目	必输	
	private String LOEKZ; //	CHAR(01)	删除标识	选输	X为删除；
	private String PSTYP; //	CHAR(01)	项目类别	必输	0为标准2为寄售
	private String MATNR; //	CHAR(18)	物料号	必输	
	@XStreamCDATA   //有特殊字符  需要CDATA
	private String TXZ01; //	CHAR(40)	物料描述	必输	
	private String WERKS; //	CHAR(4)	工厂	必输	
	private String LGORT; //	CHAR(4)	库存地点	必输	标准Y004，寄售V001
	private String MENGE; //	QUAN(17)	订单数量	必输	   可能3位小数
	private String MEINS; //	CHAR(3)	订单单位	必输	
	private String RETPO; //	CHAR(1)	退货标识	选输	X为退货
	@XStreamCDATA   //有特殊字符  需要CDATA
	private String INSMK; //    CHAR(1) 空为合格   X为待检
	private String EINDT; // 交货日期
	
	public String getEINDT() {
		return EINDT;
	}
	public void setEINDT(String eINDT) {
		EINDT = eINDT;
	}
	public String getLINENO() {
		return LINENO;
	}
	public void setLINENO(String lINENO) {
		LINENO = lINENO;
	}
	public String getEBELN() {
		return EBELN;
	}
	public void setEBELN(String eBELN) {
		EBELN = eBELN;
	}
	
	public String getBSART() {
		return BSART;
	}
	public void setBSART(String bSART) {
		BSART = bSART;
	}
	public String getAEDAT() {
		return AEDAT;
	}
	public void setAEDAT(String aEDAT) {
		AEDAT = aEDAT;
	}
	public String getERNAM() {
		return ERNAM;
	}
	public void setERNAM(String eRNAM) {
		ERNAM = eRNAM;
	}
	public String getLIFNR() {
		return LIFNR;
	}
	public void setLIFNR(String lIFNR) {
		LIFNR = lIFNR;
	}
	public String getEKORG() {
		return EKORG;
	}
	public void setEKORG(String eKORG) {
		EKORG = eKORG;
	}
	public String getEKGRP() {
		return EKGRP;
	}
	public void setEKGRP(String eKGRP) {
		EKGRP = eKGRP;
	}
	public String getEBELP() {
		return EBELP;
	}
	public void setEBELP(String eBELP) {
		EBELP = eBELP;
	}
	public String getLOEKZ() {
		return LOEKZ;
	}
	public void setLOEKZ(String lOEKZ) {
		LOEKZ = lOEKZ;
	}
	public String getPSTYP() {
		return PSTYP;
	}
	public void setPSTYP(String pSTYP) {
		PSTYP = pSTYP;
	}
	public String getMATNR() {
		return MATNR;
	}
	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}
	public String getTXZ01() {
		return TXZ01;
	}
	public void setTXZ01(String tXZ01) {
		TXZ01 = tXZ01;
	}
	public String getWERKS() {
		return WERKS;
	}
	public void setWERKS(String wERKS) {
		WERKS = wERKS;
	}
	public String getLGORT() {
		return LGORT;
	}
	public void setLGORT(String lGORT) {
		LGORT = lGORT;
	}
	public String getMENGE() {
		return MENGE;
	}
	public void setMENGE(String mENGE) {
		MENGE = mENGE;
	}
	public String getMEINS() {
		return MEINS;
	}
	public void setMEINS(String mEINS) {
		MEINS = mEINS;
	}
	public String getRETPO() {
		return RETPO;
	}
	public void setRETPO(String rETPO) {
		RETPO = rETPO;
	}
	public String getINSMK() {
		return INSMK;
	}
	public void setINSMK(String iNSMK) {
		INSMK = iNSMK;
	}
 
}
