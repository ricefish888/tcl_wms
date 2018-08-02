package com.vtradex.wms.server.model.entity.base;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;

/***
 * 条码补打记录
 *
 */
public class WmsBarCodeRepPrintRecord extends Entity {
 

	private static final long serialVersionUID = -7079072358899741019L;

	/**仓库*/
	private WmsWarehouse warehouse;
	
	/**物料*/
	private WmsItem item;
	
	/**批号*/
	private String lotkey;
	
	/**条码*/
	private String barcode;
//	
//	private WmsASNDetail wmsASNDetail;
	private String factoryCode;
	private String asnCode;
	private String supplierCode;
	private String supplierName;
	private String xmlb;
	private Date storeaDate;
	
	
	
	/**最小包装量*/
	private Double minPackageQty;
	
	/**登记时间*/
	private Date insertTime = new Date();

	/**登记人*/
	private String rfUser;
	
	/**是否打印*/
	private Boolean printFlag= Boolean.FALSE;
	
	/**打印时间*/
	private Date printTime;
	

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getFactoryCode() {
		return factoryCode;
	}

	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}

	public String getAsnCode() {
		return asnCode;
	}

	public void setAsnCode(String asnCode) {
		this.asnCode = asnCode;
	}

	 
	 
	public String getXmlb() {
		return xmlb;
	}

	public void setXmlb(String xmlb) {
		this.xmlb = xmlb;
	}

	public Date getStoreaDate() {
		return storeaDate;
	}

	public void setStoreaDate(Date storeaDate) {
		this.storeaDate = storeaDate;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public String getLotkey() {
		return lotkey;
	}

	public void setLotkey(String lotkey) {
		this.lotkey = lotkey;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public String getRfUser() {
		return rfUser;
	}

	public void setRfUser(String rfUser) {
		this.rfUser = rfUser;
	}

	public Boolean getPrintFlag() {
		return printFlag;
	}

	public void setPrintFlag(Boolean printFlag) {
		this.printFlag = printFlag;
	}

	public Date getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}
//
//	public WmsASNDetail getWmsASNDetail() {
//		return wmsASNDetail;
//	}
//
//	public void setWmsASNDetail(WmsASNDetail wmsASNDetail) {
//		this.wmsASNDetail = wmsASNDetail;
//	}

	public Double getMinPackageQty() {
		return minPackageQty;
	}

	public void setMinPackageQty(Double minPackageQty) {
		this.minPackageQty = minPackageQty;
	}
	
	
	
}
