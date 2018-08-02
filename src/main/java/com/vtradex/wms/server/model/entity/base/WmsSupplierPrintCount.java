package com.vtradex.wms.server.model.entity.base;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;

/**
 * 供应商在TCL打印条码次数统计 
 * 作为TCL收费依据
 * @author fs
 * @date 2017-9-30 09:52:27
 */
public class WmsSupplierPrintCount extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3052869090741057587L;

	/**供应商*/
	private String supplier;
	/**供应商名称*/
	private String supplierName;
	
	/**客户端IP*/
	private String clientIp;
	
	/**打印张数*/
	private Integer printTimes;

	/**打印类型
	 * {@link WmsSupplierPrintCountType}}
	 * */
	private String printType;
	
	/**打印的asn detail*/
	private WmsASNDetail asnDetail;
	
	/**送货单号*/
	private String asnCode;
	
	/**asn明细行数*/
	private Integer lineCount;
	
	public WmsSupplierPrintCount(){}
	
	public WmsSupplierPrintCount(String supplier, String clientIp,
			Integer printTimes, String printType,String supplierName) {
		super();
		this.supplier = supplier;
		this.clientIp = clientIp;
		this.printTimes = printTimes;
		this.printType = printType;
		this.supplierName = supplierName;
	}

	
	public String getAsnCode() {
		return asnCode;
	}

	public void setAsnCode(String asnCode) {
		this.asnCode = asnCode;
	}

	public Integer getLineCount() {
		return lineCount;
	}

	public void setLineCount(Integer lineCount) {
		this.lineCount = lineCount;
	}

	public WmsASNDetail getAsnDetail() {
		return asnDetail;
	}

	public void setAsnDetail(WmsASNDetail asnDetail) {
		this.asnDetail = asnDetail;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public Integer getPrintTimes() {
		return printTimes;
	}

	public void setPrintTimes(Integer printTimes) {
		this.printTimes = printTimes;
	}

	public String getPrintType() {
		return printType;
	}

	public void setPrintType(String printType) {
		this.printType = printType;
	}
	
}
