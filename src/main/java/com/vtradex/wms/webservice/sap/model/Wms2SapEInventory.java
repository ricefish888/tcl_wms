package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XStreamCDATA;
import com.vtradex.wms.server.service.annotation.XmlDataNote;

/** 
* @ClassName: 每日库存清单wms-sap
* @Description: Wms2SapEInventory 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-19 下午12:29:37  
*/
@XmlDataNote("Wms2SapEInventory")
public class Wms2SapEInventory {
	
	private String BUKRS="0530";	//		CHAR(4)	公司代码	必输	默认为0530
	
	private String WERKS;	//	CHAR(4)	工厂	必输	
	@XStreamCDATA   //有特殊字符  需要CDATA
	private String LGORT;	//	CHAR(4)	库存地点	必输	
	
	private String MATNR;	//	CHAR(18)	物料号 	必输	
	@XStreamCDATA   //有特殊字符  需要CDATA
	private String MAKTX;	//	CHAR(40)	物料描述	必输	
	
	private String ERDAT;	//	DAT(10)	库存日期	必输	
	
	private String LIFNR;	//	CHAR(10)	供应商编码	选输	
	/**
	 * {@link WmsFactoryXmlb}
	 * */
	private String SOBKZ;	//	CHAR(01)	库存类型	必输	F为标准；K为寄售；
	private String MENGE1;	//		QUAN(17)	非限制数量	必输	
	private String MENGE2;	//		QUAN(17)	质检库存	必输	
	private String BMENG;	//	QUAN(17)	WMS期初数量	必输	
	private String IMENG;	//	QUAN(17)	WMS入库数量	必输	
	private String OMENG;	//	QUAN(17)	WMS出库数量	必输	
	private String MENGE;	//	QUAN(17)	WMS期末数量	必输	非限制数量+在途数量
	public String getBUKRS() {
		return BUKRS;
	}
	public void setBUKRS(String bUKRS) {
		BUKRS = bUKRS;
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
	public String getERDAT() {
		return ERDAT;
	}
	public void setERDAT(String eRDAT) {
		ERDAT = eRDAT;
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
	public String getMENGE1() {
		return MENGE1;
	}
	public void setMENGE1(String mENGE1) {
		MENGE1 = mENGE1;
	}
	public String getMENGE2() {
		return MENGE2;
	}
	public void setMENGE2(String mENGE2) {
		MENGE2 = mENGE2;
	}
	public String getBMENG() {
		return BMENG;
	}
	public void setBMENG(String bMENG) {
		BMENG = bMENG;
	}
	public String getIMENG() {
		return IMENG;
	}
	public void setIMENG(String iMENG) {
		IMENG = iMENG;
	}
	public String getOMENG() {
		return OMENG;
	}
	public void setOMENG(String oMENG) {
		OMENG = oMENG;
	}
	public String getMENGE() {
		return MENGE;
	}
	public void setMENGE(String mENGE) {
		MENGE = mENGE;
	}
}
