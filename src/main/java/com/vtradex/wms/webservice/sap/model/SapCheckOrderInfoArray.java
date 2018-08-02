package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XmlDataNote;

/**
 * 校验信息主数据数组
 * @author Administrator
 *						
							
 */
@XmlDataNote("SapCheckOrderInfoArray")
public class SapCheckOrderInfoArray {
	
	private SapCheckOrderInfo[] scoi;
	/**
	 * 订单号 采购订单号/交货单号/生产订单号
	 */
	private String EBELN;
	/**
	 * 类型 采购单/交货单
	 */
	private String TYPE;
	/**
	 * 工单修改类型
	 */
	private String STATUS;
	
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public SapCheckOrderInfo[] getScoi() {
		return scoi;
	}
	public void setScoi(SapCheckOrderInfo[] scoi) {
		this.scoi = scoi;
	}
	public String getEBELN() {
		return EBELN;
	}
	public void setEBELN(String eBELN) {
		EBELN = eBELN;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
}
