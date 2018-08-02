package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XStreamCDATA;
import com.vtradex.wms.server.service.annotation.XmlDataNote;

/** 
* @ClassName: 采购对账单
* @Description: SapCheckOrder 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-14 下午5:23:10  
*/
@XmlDataNote("SapCheckOrder")
public class SapCheckOrder {
	
	private String LINENO; // CHAR(32) 行号
	
	private String PSTYP;		//	CHAR(01)	项目类别	必输	默认为0；
	@XStreamCDATA
	private String LIFNR;		//	CHAR(10)	供应商	必输	参考ZMM015对应输出字段LIFNR
	private String NAME1;		//	CHAR(35)	供应商描述	必输	参考ZMM015对应输出字段NAME1
	@XStreamCDATA
	private String BUDAT;		//	DATS(10)	过帐日期	必输	参考ZMM015对应输出字段BUDAT
	@XStreamCDATA
	private String EBELN;		//	CHAR(10)	采购凭证	必输	参考ZMM015对应输出字段EBELN
	@XStreamCDATA
	private String EBELP;		//	CHAR(05)	项目	必输	参考ZMM015对应输出字段EBELP
	private String WERKS;		//	CHAR(04)	工厂	必输	参考ZMM015对应输出字段WERKS
	private String BLDAT;		//	DATS(10)	凭证日期	必输	参考ZMM015对应输出字段BLDAT
	private String EKGRP;		//	CHAR(03)	采购组	必输	参考ZMM015对应输出字段EKGRP
	private String MATNR;		//	CHAR(18)	物料号	必输	参考ZMM015对应输出字段MATNR
	private String MAKTX;		//	CHAR(40)	物料描述	必输	参考ZMM015对应输出字段MAKTX
	@XStreamCDATA
	private String MBLNR;		//	CHAR(10)	入库凭证	必输	参考ZMM015对应输出字段MBLNR
	private String ZEILE;		//	CHAR(04)	入库凭证行	必输	参考ZMM015对应输出字段ZEILE
	private String MENGE;		//	QUAN(17)	入库数量	必输	参考ZMM015对应输出字段MENGE
	private String BPRME;		//	CHAR(03)	入库单位	必输	参考ZMM015对应输出字段BPRME
	private String LGORT;		//	CHAR(04)	库位	必输	参考ZMM015对应输出字段LGORT
	private String NETPR1;//		CURR(14)	应执行单价	必输	参考ZMM015对应输出字段NETPR1；
	private String PEIN2;//	CHAR(6)	价格单位	必输	参考ZMM015对应输出字段PEIN2；
	@XStreamCDATA
	private String DATAB;//	DATS(10)	价格有效从	必输	参考ZMM015对应输出字段DATAB；
	private String DATBI;//	DATS(10)	价格有效到	必输	参考ZMM015对应输出字段DATBI；
	private String NETWR1;//		CURR(16)	应执行净额	必输	参考ZMM015对应输出字段NETWR1；
	private String BTAXA;//	CURR(16)	应执行税额	必输	参考ZMM015对应输出字段BTAXA；
	@XStreamCDATA
	private String ETAXA;//	CURR(16)	应执行含税金额	必输	参考ZMM015对应输出字段ETAXA；
	private String BPMNG1;//		QUAN(17)	过账发票数量	选输	参考ZMM015对应输出字段 BPMNG1；
	@XStreamCDATA
	private String DMBTR1;//		CURR(16)	过账发票金额	选输	参考ZMM015对应输出字段DMBTR1；
	@XStreamCDATA
	private String BPMNG2;//		QUAN(17)	预制发票数量	选输	参考ZMM015对应输出字段 BPMNG2；
	@XStreamCDATA
	private String DMBTR2;//		CURR(16)	预制发票金额	选输	参考ZMM015对应输出字段 DMBTR2；
	@XStreamCDATA
	private String BPMNG3;//		QUAN(17)	发票数量总计	选输	参考ZMM015对应输出字段 BPMNG3；
	@XStreamCDATA
	private String DMBTR3;//		CURR(16)	发票金额总计	选输	参考ZMM015对应输出字段 DMBTR3；
	private String XBLNR;//CHAR(10)	送货单号	必输	参考ZMM015对应输出字段 XBLNR；
	public String getLINENO() {
		return LINENO;
	}
	public void setLINENO(String lINENO) {
		LINENO = lINENO;
	}
	public String getPSTYP() {
		return PSTYP;
	}
	public void setPSTYP(String pSTYP) {
		PSTYP = pSTYP;
	}
	public String getLIFNR() {
		return LIFNR;
	}
	public void setLIFNR(String lIFNR) {
		LIFNR = lIFNR;
	}
	public String getNAME1() {
		return NAME1;
	}
	public void setNAME1(String nAME1) {
		NAME1 = nAME1;
	}
	public String getBUDAT() {
		return BUDAT;
	}
	public void setBUDAT(String bUDAT) {
		BUDAT = bUDAT;
	}
	public String getEBELN() {
		return EBELN;
	}
	public void setEBELN(String eBELN) {
		EBELN = eBELN;
	}
	public String getEBELP() {
		return EBELP;
	}
	public void setEBELP(String eBELP) {
		EBELP = eBELP;
	}
	public String getWERKS() {
		return WERKS;
	}
	public void setWERKS(String wERKS) {
		WERKS = wERKS;
	}
	public String getBLDAT() {
		return BLDAT;
	}
	public void setBLDAT(String bLDAT) {
		BLDAT = bLDAT;
	}
	public String getEKGRP() {
		return EKGRP;
	}
	public void setEKGRP(String eKGRP) {
		EKGRP = eKGRP;
	}
	public String getMATNR() {
		return MATNR;
	}
	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}
	public String getMAKTX() {
		return MAKTX;
	}
	public void setMAKTX(String mAKTX) {
		MAKTX = mAKTX;
	}
	public String getMBLNR() {
		return MBLNR;
	}
	public void setMBLNR(String mBLNR) {
		MBLNR = mBLNR;
	}
	public String getZEILE() {
		return ZEILE;
	}
	public void setZEILE(String zEILE) {
		ZEILE = zEILE;
	}
	public String getMENGE() {
		return MENGE;
	}
	public void setMENGE(String mENGE) {
		MENGE = mENGE;
	}
	public String getBPRME() {
		return BPRME;
	}
	public void setBPRME(String bPRME) {
		BPRME = bPRME;
	}
	public String getLGORT() {
		return LGORT;
	}
	public void setLGORT(String lGORT) {
		LGORT = lGORT;
	}
	public String getNETPR1() {
		return NETPR1;
	}
	public void setNETPR1(String nETPR1) {
		NETPR1 = nETPR1;
	}
	public String getPEIN2() {
		return PEIN2;
	}
	public void setPEIN2(String pEIN2) {
		PEIN2 = pEIN2;
	}
	public String getDATAB() {
		return DATAB;
	}
	public void setDATAB(String dATAB) {
		DATAB = dATAB;
	}
	public String getDATBI() {
		return DATBI;
	}
	public void setDATBI(String dATBI) {
		DATBI = dATBI;
	}
	public String getNETWR1() {
		return NETWR1;
	}
	public void setNETWR1(String nETWR1) {
		NETWR1 = nETWR1;
	}
	public String getBTAXA() {
		return BTAXA;
	}
	public void setBTAXA(String bTAXA) {
		BTAXA = bTAXA;
	}
	public String getETAXA() {
		return ETAXA;
	}
	public void setETAXA(String eTAXA) {
		ETAXA = eTAXA;
	}
	public String getBPMNG1() {
		return BPMNG1;
	}
	public void setBPMNG1(String bPMNG1) {
		BPMNG1 = bPMNG1;
	}
	public String getDMBTR1() {
		return DMBTR1;
	}
	public void setDMBTR1(String dMBTR1) {
		DMBTR1 = dMBTR1;
	}
	public String getBPMNG2() {
		return BPMNG2;
	}
	public void setBPMNG2(String bPMNG2) {
		BPMNG2 = bPMNG2;
	}
	public String getDMBTR2() {
		return DMBTR2;
	}
	public void setDMBTR2(String dMBTR2) {
		DMBTR2 = dMBTR2;
	}
	public String getBPMNG3() {
		return BPMNG3;
	}
	public void setBPMNG3(String bPMNG3) {
		BPMNG3 = bPMNG3;
	}
	public String getDMBTR3() {
		return DMBTR3;
	}
	public void setDMBTR3(String dMBTR3) {
		DMBTR3 = dMBTR3;
	}
	public String getXBLNR() {
		return XBLNR;
	}
	public void setXBLNR(String xBLNR) {
		XBLNR = xBLNR;
	}

}
