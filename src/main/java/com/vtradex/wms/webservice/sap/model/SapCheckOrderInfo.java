package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XmlDataNote;
/**
 * 校验信息主数据
 * @author Administrator
 *
 */
@XmlDataNote("SapCheckOrderInfo")
public class SapCheckOrderInfo {

	/**
	 * 行项目
	 */
	private String EBELP;
	/**
	 * 类型 标准/寄售
	 */
	private String PSTYP;
	/**
	 * 物料号
	 */
	private String MATNR;
	/**
	 * 工厂
	 */
	private String WERKS;
	/**
	 * 库位
	 */
	private String LGORT;
	/**
	 * 数量
	 */
	private String MENGE;
	/**
	 * 交货日期
	 */
	private String EINDT;
	
	/**
	 * 删除标识 有值代表删除 为空不删
	 */
	private String LOEKZ;
	/**
	 * 提货数
	 */
	private String ENMNG;

	/**
	 * 开始日期
	 */
	private String GSTRP;
	/**
	 * 结束日期
	 */
	private String GLTRP;
	/**
	 * 订单数量
	 */
	private String GAMNG;
	/**
	 * 预留号
	 */
	private String RSNUM;
	/**
	 * 预留行项目
	 */
	private String RSPOS;
	/**
	 * 原材料编码
	 */
	private String MATNR1;
	/**
	 * 需求数量
	 */
	private String BDMNG;
	/**
	 * 物料类型
	 */
	private String MTART;
	/**
	 * 产线
	 */
	private String ZPRO_LINE;
	/**
	 * 产线描述
	 */
	private String ZPRO_NAME;
	
	/**
	 * 原料单位
	 */
	private String MEINS1;
	
	/**
	 * 销售单号
	 */
	private String XSDH;
	
	public String getXSDH() {
		return XSDH;
	}
	public void setXSDH(String xSDH) {
		XSDH = xSDH;
	}
	public String getMEINS1() {
		return MEINS1;
	}
	public void setMEINS1(String mEINS1) {
		MEINS1 = mEINS1;
	}
	public String getENMNG() {
		return ENMNG;
	}
	public void setENMNG(String eNMNG) {
		ENMNG = eNMNG;
	}
	public String getLOEKZ() {
		return LOEKZ;
	}
	public void setLOEKZ(String lOEKZ) {
		LOEKZ = lOEKZ;
	}
	public String getEBELP() {
		return EBELP;
	}
	public void setEBELP(String eBELP) {
		EBELP = eBELP;
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
	public String getEINDT() {
		return EINDT;
	}
	public void setEINDT(String eINDT) {
		EINDT = eINDT;
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
	public String getRSNUM() {
		return RSNUM;
	}
	public void setRSNUM(String rSNUM) {
		RSNUM = rSNUM;
	}
	public String getRSPOS() {
		return RSPOS;
	}
	public void setRSPOS(String rSPOS) {
		RSPOS = rSPOS;
	}
	public String getMATNR1() {
		return MATNR1;
	}
	public void setMATNR1(String mATNR1) {
		MATNR1 = mATNR1;
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