package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XStreamCDATA;
import com.vtradex.wms.server.service.annotation.XmlDataNote;

/**SAP采购交货单*/
@XmlDataNote("SapDeliveryOrder")
public class SapDeliveryOrder {
	private String LINENO; //CHAR(32) 行号
	
	private String VBELN; //	CHAR(10)	采购交货号	必输	
	private String ERNAM; //	CHAR(12)	创建者	必输	WMS自动生成的交货单，创建者为jobuser
	private String ERDAT; //	DATS(10)	创建日期	必输	  20170701这样的字样
	private String BLDAT; //	DATS(10)	凭证日期	必输	  20170701这样的字样
	private String LFART; //	DATS(10)	交货类型	必输	  默认取“EL”
	private String LIFNR; //	CHAR(10)	供应商编码	必输	
	private String TCODE; //	CHAR(10)	事物代码	必输 字段用于区分“手工创建”或“WMS生成”	
	@XStreamCDATA   //有特殊字符  需要CDATA
	private String POSNR; //	CHAR(05)	项目	必输	
	private String MATNR; //	CHAR(18)	物料号	必输	
	@XStreamCDATA   //有特殊字符  需要CDATA
	private String WERKS; //	CHAR(4)	工厂	必输	
	@XStreamCDATA   //有特殊字符  需要CDATA
	private String LGORT; //	CHAR(4)	库存地点	必输	标准Y004，寄售V001
	private String LFIMG; //	QUAN(17)	交货数量	必输	   可能3位小数
	private String MEINS; //	CHAR(3)	基本单位	必输	
	private String VGBEL; //	CHAR(10)	参考凭证	必输	 对应PO订单
	private String VGPOS; //	CHAR(04)	参考凭证行项目	必输	 对应PO订单行项目
	private String DABMG; //QUAN(17) 实际交货量，根据交货单及行项目，取实际交货量
	@XStreamCDATA   //有特殊字符  需要CDATA
	private String WBSTK; //CHAR(01) 货物移动状态	，A(没有处理) B(部分处理) C(完全地处理)
	private String LFDAT; //交货日期
	
	public String getLFDAT() {
		return LFDAT;
	}
	public void setLFDAT(String lFDAT) {
		LFDAT = lFDAT;
	}
	public String getLINENO() {
		return LINENO;
	}
	public void setLINENO(String lINENO) {
		LINENO = lINENO;
	}
	public String getVBELN() {
		return VBELN;
	}
	public void setVBELN(String vBELN) {
		VBELN = vBELN;
	}
	public String getERNAM() {
		return ERNAM;
	}
	public void setERNAM(String eRNAM) {
		ERNAM = eRNAM;
	}
	public String getERDAT() {
		return ERDAT;
	}
	public void setERDAT(String eRDAT) {
		ERDAT = eRDAT;
	}
	public String getBLDAT() {
		return BLDAT;
	}
	public void setBLDAT(String bLDAT) {
		BLDAT = bLDAT;
	}
	public String getLFART() {
		return LFART;
	}
	public void setLFART(String lFART) {
		LFART = lFART;
	}
	public String getLIFNR() {
		return LIFNR;
	}
	public void setLIFNR(String lIFNR) {
		LIFNR = lIFNR;
	}
	public String getTCODE() {
		return TCODE;
	}
	public void setTCODE(String tCODE) {
		TCODE = tCODE;
	}
	public String getPOSNR() {
		return POSNR;
	}
	public void setPOSNR(String pOSNR) {
		POSNR = pOSNR;
	}
	public String getMATNR() {
		return MATNR;
	}
	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}
	public String getVGBEL() {
		return VGBEL;
	}
	public void setVGBEL(String vGBEL) {
		VGBEL = vGBEL;
	}
	public String getVGPOS() {
		return VGPOS;
	}
	public void setVGPOS(String vGPOS) {
		VGPOS = vGPOS;
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
	public String getLFIMG() {
		return LFIMG;
	}
	public void setLFIMG(String lFIMG) {
		LFIMG = lFIMG;
	}
	public String getMEINS() {
		return MEINS;
	}
	public void setMEINS(String mEINS) {
		MEINS = mEINS;
	}
	public String getDABMG() {
		return DABMG;
	}
	public void setDABMG(String dABMG) {
		DABMG = dABMG;
	}
	public String getWBSTK() {
		return WBSTK;
	}
	public void setWBSTK(String wBSTK) {
		WBSTK = wBSTK;
	}	
}
