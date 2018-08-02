package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XStreamCDATA;
import com.vtradex.wms.server.service.annotation.XmlDataNote;

/**
 * @ClassName: Sap销售外向交货单
 * @Description: SapSaleOutDelivery
 * @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>
 * @date 2017-7-13 上午9:52:43
 */
@XmlDataNote("SapSaleOutDelivery")
public class SapSaleOutDelivery {

	private String LINENO; // CHAR(32) 行号

	private String VBELN; // CHAR(10) 销售外向交货单号 必输
	private String TYPE; // CHAR(02) 操作类型 必输 01为新增02为更改03为删除
	private String ERNAM; // CHAR(12) 创建者 必输
	private String ERDAT; // DATS(10) 创建日期 必输
	private String BLDAT; // DATS(10) 凭证日期 必输
	private String LFART; // CHAR(04) 交货类型 必输 默认取“NCCU”
	private String KUNNR; // CHAR(10) 客户 必输
	@XStreamCDATA
	private String POSNR; // CHAR(04) 项 必输 存在一个交货单对应多个行项目的情况；
	@XStreamCDATA
	private String MATNR; // CHAR(18) 物料号 必输
	@XStreamCDATA
	private String WERKS; // CHAR(04) 工厂 必输
	@XStreamCDATA
	private String LGORT; // CHAR(04) 库存地点 必输
	@XStreamCDATA
	private String LFIMG; // QUAN(17) 交货数量 必输
	@XStreamCDATA
	private String MEINS; // CHAR(03) 基本单位 必输
	@XStreamCDATA
	private String VGBEL; // CHAR(10) 参考凭证 必输 对应
	@XStreamCDATA
	private String VGPOS; // CHAR(04) 参考凭证行项目 必输 对应销售订单行项目
	
	private String NAME1; //客户名称
	
	private String WADAT; //交货日期

	public String getWADAT() {
		return WADAT;
	}

	public void setWADAT(String wADAT) {
		WADAT = wADAT;
	}

	public String getNAME1() {
		return NAME1;
	}

	public void setNAME1(String nAME1) {
		NAME1 = nAME1;
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

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
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

	public String getKUNNR() {
		return KUNNR;
	}

	public void setKUNNR(String kUNNR) {
		KUNNR = kUNNR;
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
}
