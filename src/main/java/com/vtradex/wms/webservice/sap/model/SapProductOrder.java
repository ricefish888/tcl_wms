package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XStreamCDATA;
import com.vtradex.wms.server.service.annotation.XmlDataNote;

/** 
* @ClassName: Sap 生产订单
* @Description: SapProductOrders 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-7 下午3:13:55  
*/
@XmlDataNote("SapProductOrders")
public class SapProductOrder {
	
	private String LINENO; //CHAR(32) 行号
	
	private String AUFNR;//CHAR(12) 生产订单号
	private String WERKS;//CHAR(04) 工厂
	private String MATNR;//CHAR(18) 物料编码
	private String AUART;//CHAR(04) 订单类型
	private String GLTRP;//DATS（8） 基本完成日期
	private String GSTRP;//DATS（8） 基本开始日期
	private String GAMNG;//QUANDATS(103) 计划数量
	@XStreamCDATA
	private String MAKTX;//物料描述

	private String POSNR;//CHAR(04) 组件项目号
	private String MATNR1;//CHAR(18) 物料编码
	private String BDMNG;//QUAN(13) 需求数量
	@XStreamCDATA
	private String MAKTX1;//物料描述
	@XStreamCDATA
	private String MTART;//CHAR(04) 物料类型
	
	private String XLOEK;//CHAR(01)删除标识
	private String KZEAR;//CHAR(01)最后的发货
	private String ENMNG;//QUAN(13)提货数量
	private String RSNUM;//CHAR(10)预留号
	
	private String BWART;//  移动类型
	private String MEINS;//  成品单位
	private String MEINS1;// 原料单位
	
	private String ZPRO_LINE;//产线
	private String ZPRO_NAME;//产线描述
	
	private String RSPOS; //预留行项目号
	
	private String XSDH;//销售单号
	
	public String getXSDH() {
		return XSDH;
	}
	public void setXSDH(String xSDH) {
		XSDH = xSDH;
	}
	public String getRSPOS() {
		return RSPOS;
	}
	public void setRSPOS(String rSPOS) {
		RSPOS = rSPOS;
	}
	public String getLINENO() {
		return LINENO;
	}
	public void setLINENO(String lINENO) {
		LINENO = lINENO;
	}
	public String getAUFNR() {
		return AUFNR;
	}
	public void setAUFNR(String aUFNR) {
		AUFNR = aUFNR;
	}
	public String getWERKS() {
		return WERKS;
	}
	public void setWERKS(String wERKS) {
		WERKS = wERKS;
	}
	public String getMATNR() {
		return MATNR;
	}
	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}
	public String getGLTRP() {
		return GLTRP;
	}
	public void setGLTRP(String gLTRP) {
		GLTRP = gLTRP;
	}
	public String getGSTRP() {
		return GSTRP;
	}
	public void setGSTRP(String gSTRP) {
		GSTRP = gSTRP;
	}
	public String getGAMNG() {
		return GAMNG;
	}
	public void setGAMNG(String gAMNG) {
		GAMNG = gAMNG;
	}
	public String getMAKTX() {
		return MAKTX;
	}
	public void setMAKTX(String mAKTX) {
		MAKTX = mAKTX;
	}
	public String getPOSNR() {
		return POSNR;
	}
	public void setPOSNR(String pOSNR) {
		POSNR = pOSNR;
	}
	public String getBDMNG() {
		return BDMNG;
	}
	public void setBDMNG(String bDMNG) {
		BDMNG = bDMNG;
	}
	public String getMTART() {
		return MTART;
	}
	public void setMTART(String mTART) {
		MTART = mTART;
	}
	public String getMATNR1() {
		return MATNR1;
	}
	public void setMATNR1(String mATNR1) {
		MATNR1 = mATNR1;
	}
	public String getMAKTX1() {
		return MAKTX1;
	}
	public void setMAKTX1(String mAKTX1) {
		MAKTX1 = mAKTX1;
	}
	public String getXLOEK() {
		return XLOEK;
	}
	public void setXLOEK(String xLOEK) {
		XLOEK = xLOEK;
	}
	public String getKZEAR() {
		return KZEAR;
	}
	public void setKZEAR(String kZEAR) {
		KZEAR = kZEAR;
	}
	public String getENMNG() {
		return ENMNG;
	}
	public void setENMNG(String eNMNG) {
		ENMNG = eNMNG;
	}
	public String getRSNUM() {
		return RSNUM;
	}
	public void setRSNUM(String rSNUM) {
		RSNUM = rSNUM;
	}
	public String getAUART() {
		return AUART;
	}
	public void setAUART(String aUART) {
		AUART = aUART;
	}
	public String getBWART() {
		return BWART;
	}
	public void setBWART(String bWART) {
		BWART = bWART;
	}
	public String getMEINS() {
		return MEINS;
	}
	public void setMEINS(String mEINS) {
		MEINS = mEINS;
	}
	public String getMEINS1() {
		return MEINS1;
	}
	public void setMEINS1(String mEINS1) {
		MEINS1 = mEINS1;
	}
	public String getZPRO_LINE() {
		return ZPRO_LINE;
	}
	public void setZPRO_LINE(String zPRO_LINE) {
		ZPRO_LINE = zPRO_LINE;
	}
	public String getZPRO_NAME() {
		return ZPRO_NAME;
	}
	public void setZPRO_NAME(String zPRO_NAME) {
		ZPRO_NAME = zPRO_NAME;
	}
	
}
