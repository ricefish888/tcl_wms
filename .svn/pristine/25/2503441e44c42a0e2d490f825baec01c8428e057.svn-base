package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XStreamCDATA;
import com.vtradex.wms.server.service.annotation.XmlDataNote;

/** 
* @ClassName: Sap预留主数据
* @Description: SapReservedData 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-12 上午9:56:07  
*/
@XmlDataNote("SapReservedData")
public class SapReservedData {
	
	private String LINENO; //CHAR(32) 行号

	private String RSNUM; //CHAR(10) 预留号 必输
	private String TYPE; //CHAR(02) 操作类型 01为新增02为更改 必输
	private String RSDAT; //DATS (10) 基准日期 必输
	private String USNAM; //CHAR(12) 用户名称 必输
	private String BWART; //CHAR(03) 移动类型 必输
	@XStreamCDATA
	private String KOSTL; //CHAR(10) 成本中心 选输 Z01/Z02/Z03/Z04必输
	@XStreamCDATA
	private String KTEXT; //CHAR(40) 成本中心描述 选输 取值时，控制CSKT-SPRAS = ‘ZH’, CSKT-DATBI >= SY_DATUM
	@XStreamCDATA
	private String UMWRK; //CHAR(10) 收货工厂 选输
	@XStreamCDATA
	private String UMLGO; //	CHAR(04)	收货库存地点	选输	
	@XStreamCDATA
	private String RSPOS; //	CHAR(04)	项目	必输	
	@XStreamCDATA
	private String XLOEK;//	CHAR(01)	项目已删除	选输	删除标识X
	@XStreamCDATA
	private String KZEAR;//	CHAR(01)	最后发货	选输	最后发货X
	@XStreamCDATA
	private String MATNR;//	CHAR(18)	物料号	必输	
	@XStreamCDATA
	private String WERKS;//	CHAR(04)	工厂	必输	
	private String LGORT;//	CHAR(04)	发出库位	选输	
	@XStreamCDATA
	private String BDTER;//	DATS (10)	需求日期	必输	
	private String BDMNG;//	QUAN(17)	需求量	必输	
	@XStreamCDATA
	private String MEINS;//	UNIT (03)	基本计量单位	必输	
	@XStreamCDATA
	private String SHKZG;//	CHAR(01)	借/贷标识	必输	
	private String ENMNG;//	QUAN(17)	提货数	选输	
	@XStreamCDATA
	private String SGTXT; // CHAR(50) 行项目文本
	
	public String getSGTXT() {
		return SGTXT;
	}
	public void setSGTXT(String sGTXT) {
		SGTXT = sGTXT;
	}
	public String getLINENO() {
		return LINENO;
	}
	public void setLINENO(String lINENO) {
		LINENO = lINENO;
	}
	public String getRSNUM() {
		return RSNUM;
	}
	public void setRSNUM(String rSNUM) {
		RSNUM = rSNUM;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getRSDAT() {
		return RSDAT;
	}
	public void setRSDAT(String rSDAT) {
		RSDAT = rSDAT;
	}
	public String getUSNAM() {
		return USNAM;
	}
	public void setUSNAM(String uSNAM) {
		USNAM = uSNAM;
	}
	public String getBWART() {
		return BWART;
	}
	public void setBWART(String bWART) {
		BWART = bWART;
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
	public String getUMWRK() {
		return UMWRK;
	}
	public void setUMWRK(String uMWRK) {
		UMWRK = uMWRK;
	}
	public String getUMLGO() {
		return UMLGO;
	}
	public void setUMLGO(String uMLGO) {
		UMLGO = uMLGO;
	}
	public String getRSPOS() {
		return RSPOS;
	}
	public void setRSPOS(String rSPOS) {
		RSPOS = rSPOS;
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
	public String getBDTER() {
		return BDTER;
	}
	public void setBDTER(String bDTER) {
		BDTER = bDTER;
	}
	public String getBDMNG() {
		return BDMNG;
	}
	public void setBDMNG(String bDMNG) {
		BDMNG = bDMNG;
	}
	public String getMEINS() {
		return MEINS;
	}
	public void setMEINS(String mEINS) {
		MEINS = mEINS;
	}
	public String getSHKZG() {
		return SHKZG;
	}
	public void setSHKZG(String sHKZG) {
		SHKZG = sHKZG;
	}
	public String getENMNG() {
		return ENMNG;
	}
	public void setENMNG(String eNMNG) {
		ENMNG = eNMNG;
	}
}
