package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XStreamCDATA;
import com.vtradex.wms.server.service.annotation.XmlDataNote;

@XmlDataNote("Wms2SapInventoryLedger")
public class Wms2SapInventoryLedger {
	
	private String TYPE;//移动类型
	private String FRBNR;//	CHAR(16)	提货单
	private String BLDAT;//	DATS(10)	凭证日期
	private String BUDAT;//	DATS(10)	过帐日期
	private String ZEILE;//	CHAR(04)	行项目
	private String BWART;//	CHAR(04)	移动类型
	private String MATNR;//	CHAR(10)	物料编码
	private String WERKS;//	CHAR(04)	工厂
	private String LGORT;//	CHAR(04)	库位
	private String LIFNR;//	CHAR(10)	供应商编码
	private String SOBKZ;//	CHAR(01)	特殊库存
	private String MENGE;//	QUAN(17)	数量
	private String EBELN;//	CHAR(10)	采购订单
	private String EBELP;//	CHAR(05)	采购订单行项目
	private String INSMK;//	CHAR(01)	库存类型
	private String VBELN_IM;//	CHAR(10)	交货单
	private String VBELP_IM;//CHAR(06)	交货单行项目
	private String MEINS; //基本单位
	@XStreamCDATA //有特殊字符  需要CDATA
	private String SGTXT;//	CHAR(50)	行文本
	
	private String XBLNR; //参照  --取消收货
	
	private String EKGRP; //采购组  --发货
	private String RETPO; //退货标识--发货
	
	private String UMLGO; //收货库位--质检转合格
	
	private String AUFNR; //生产订单号
	
	private String LFBNR; //SAP生成的凭证号回传给WMS
	
	private String LFPOS; //取消的老入库单的行号
	
	private String RSNUM; //预留单号
	
	private String RSPOS; //预留行项目
	
	private String KOSTL; //成本中心
	
	private String UMWRK; //收货工厂
	/**交货单创建方式 W-WMS创建，S-SAP创建*/
	private String TYPE_IM;
	/**利润中心*/
	private String PRCTR;
	
	public String getPRCTR() {
		return PRCTR;
	}
	public void setPRCTR(String pRCTR) {
		PRCTR = pRCTR;
	}
	public String getTYPE_IM() {
		return TYPE_IM;
	}
	public void setTYPE_IM(String tYPE_IM) {
		TYPE_IM = tYPE_IM;
	}
	public String getLFPOS() {
		return LFPOS;
	}
	public void setLFPOS(String lFPOS) {
		LFPOS = lFPOS;
	}
	public String getUMWRK() {
		return UMWRK;
	}
	public void setUMWRK(String uMWRK) {
		UMWRK = uMWRK;
	}
	public String getKOSTL() {
		return KOSTL;
	}
	public void setKOSTL(String kOSTL) {
		KOSTL = kOSTL;
	}
	public String getRSPOS() {
		return RSPOS;
	}
	public void setRSPOS(String rSPOS) {
		RSPOS = rSPOS;
	}
	public String getRSNUM() {
		return RSNUM;
	}
	public void setRSNUM(String rSNUM) {
		RSNUM = rSNUM;
	}
	public String getLFBNR() {
		return LFBNR;
	}
	public void setLFBNR(String lFBNR) {
		LFBNR = lFBNR;
	}
	public String getMEINS() {
		return MEINS;
	}
	public void setMEINS(String mEINS) {
		MEINS = mEINS;
	}
	public String getAUFNR() {
		return AUFNR;
	}
	public void setAUFNR(String aUFNR) {
		AUFNR = aUFNR;
	}
	public String getUMLGO() {
		return UMLGO;
	}
	public void setUMLGO(String uMLGO) {
		UMLGO = uMLGO;
	}
	public String getXBLNR() {
		return XBLNR;
	}
	public void setXBLNR(String xBLNR) {
		XBLNR = xBLNR;
	}
	public String getRETPO() {
		return RETPO;
	}
	public void setRETPO(String rETPO) {
		RETPO = rETPO;
	}
	public String getEKGRP() {
		return EKGRP;
	}
	public void setEKGRP(String eKGRP) {
		EKGRP = eKGRP;
	}
	public String getType() {
		return TYPE;
	}
	public void setType(String type) {
		this.TYPE = type;
	}
	public String getFRBNR() {
		return FRBNR;
	}
	public void setFRBNR(String fRBNR) {
		FRBNR = fRBNR;
	}
	public String getBLDAT() {
		return BLDAT;
	}
	public void setBLDAT(String bLDAT) {
		BLDAT = bLDAT;
	}
	public String getBUDAT() {
		return BUDAT;
	}
	public void setBUDAT(String bUDAT) {
		BUDAT = bUDAT;
	}
	public String getZEILE() {
		return ZEILE;
	}
	public void setZEILE(String zEILE) {
		ZEILE = zEILE;
	}
	public String getBWART() {
		return BWART;
	}
	public void setBWART(String bWART) {
		BWART = bWART;
	}
	public String getMATNR() {
		return MATNR;
	}
	public void setMATNR(String mATNR) {
		MATNR = mATNR;
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
	public String getLIFNR() {
		return LIFNR;
	}
	public void setLIFNR(String lIFNR) {
		LIFNR = lIFNR;
	}
	public String getSOBKZ() {
		return SOBKZ;
	}
	public void setSOBKZ(String sOBKZ) {
		SOBKZ = sOBKZ;
	}
	public String getMENGE() {
		return MENGE;
	}
	public void setMENGE(String mENGE) {
		MENGE = mENGE;
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
	public String getINSMK() {
		return INSMK;
	}
	public void setINSMK(String iNSMK) {
		INSMK = iNSMK;
	}
	public String getVBELN_IM() {
		return VBELN_IM;
	}
	public void setVBELN_IM(String vBELN_IM) {
		VBELN_IM = vBELN_IM;
	}
	public String getVBELP_IM() {
		return VBELP_IM;
	}
	public void setVBELP_IM(String vBELP_IM) {
		VBELP_IM = vBELP_IM;
	}
	public String getSGTXT() {
		return SGTXT;
	}
	public void setSGTXT(String sGTXT) {
		SGTXT = sGTXT;
	}
	
}
