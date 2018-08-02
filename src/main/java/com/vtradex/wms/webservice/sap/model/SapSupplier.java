package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XStreamCDATA;
import com.vtradex.wms.server.service.annotation.XmlDataNote;

/** SAP接口 供应商对象 */
@XmlDataNote("SapSupplier")
public class SapSupplier  {
	
	private String LINENO; //CHAR(32) 行号

	private String LIFNR; //	CHAR(10)	供应商编码
	
	@XStreamCDATA   //有特殊字符  需要CDATA
	private String NAME1; //	CHAR(35)	供应商描述
	private String KTOKK; //	CHAR(04)	帐户组
	private String SORTL; //	CHAR(10)	检索项
	
	@XStreamCDATA   //有特殊字符  需要CDATA
	private String STRAS; //	CHAR(35)	供应商地址
	@XStreamCDATA
	private String PSTLZ; //	CHAR(10)	邮政编码
	private String ERDAT; //	DATS(10)	创建日期
	@XStreamCDATA
	private String TELF1; //	CHAR(16)	电话1
	@XStreamCDATA
	private String TELF2; //	CHAR(16)	电话2
	@XStreamCDATA
	private String SMTP_ADDR1; //	CHAR(20)	邮箱地址
	@XStreamCDATA
	private String SMTP_ADDR2; //	CHAR(20)	邮箱地址
	@XStreamCDATA
	private String SMTP_ADDR3; //	CHAR(20)	邮箱地址
	@XStreamCDATA
	private String LOEVM; //	CHAR(01)	一般数据删除
	public String getLINENO() {
		return LINENO;
	}
	public void setLINENO(String lINENO) {
		LINENO = lINENO;
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
	public String getKTOKK() {
		return KTOKK;
	}
	public void setKTOKK(String kTOKK) {
		KTOKK = kTOKK;
	}
	public String getSORTL() {
		return SORTL;
	}
	public void setSORTL(String sORTL) {
		SORTL = sORTL;
	}
	public String getSTRAS() {
		return STRAS;
	}
	public void setSTRAS(String sTRAS) {
		STRAS = sTRAS;
	}
	public String getPSTLZ() {
		return PSTLZ;
	}
	public void setPSTLZ(String pSTLZ) {
		PSTLZ = pSTLZ;
	}
	public String getERDAT() {
		return ERDAT;
	}
	public void setERDAT(String eRDAT) {
		ERDAT = eRDAT;
	}
	public String getTELF1() {
		return TELF1;
	}
	public void setTELF1(String tELF1) {
		TELF1 = tELF1;
	}
	public String getTELF2() {
		return TELF2;
	}
	public void setTELF2(String tELF2) {
		TELF2 = tELF2;
	}
	public String getSMTP_ADDR1() {
		return SMTP_ADDR1;
	}
	public void setSMTP_ADDR1(String sMTP_ADDR1) {
		SMTP_ADDR1 = sMTP_ADDR1;
	}
	public String getSMTP_ADDR2() {
		return SMTP_ADDR2;
	}
	public void setSMTP_ADDR2(String sMTP_ADDR2) {
		SMTP_ADDR2 = sMTP_ADDR2;
	}
	public String getSMTP_ADDR3() {
		return SMTP_ADDR3;
	}
	public void setSMTP_ADDR3(String sMTP_ADDR3) {
		SMTP_ADDR3 = sMTP_ADDR3;
	}
	public String getLOEVM() {
		return LOEVM;
	}
	public void setLOEVM(String lOEVM) {
		LOEVM = lOEVM;
	}

}
