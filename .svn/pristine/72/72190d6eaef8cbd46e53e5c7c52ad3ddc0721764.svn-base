package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XStreamCDATA;
import com.vtradex.wms.server.service.annotation.XmlDataNote;

/** 
* @ClassName: WMS请求SAP交货单DTO
* @Description: WmsDTODeliveryOrder 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-11 下午3:38:45  
*/
@XmlDataNote("Wms2SapDeliveryOrder")
public class Wms2SapDeliveryOrder {
	private String lineNo; //CHAR(32) 行号
	
	private String doNo; //	CHAR(10)	WMS交货单号 必输
	private String count; //	CHAR(02)	数量 数量  明细总条数
	private String commond; //	为空即可
	@XStreamCDATA   //有特殊字符  需要CDATA
	private String doLineNo; //	行项目
	private String poNo; //	采购订单
	private String poLineNo; //	采购订单行
	private String itemCode; //	物料
	private String quantity; //	数量
	private String unit; //	单位
	private String factoryCode; //工厂
	private String LFDAT; //交货日期
	public String getLineNo() {
		return lineNo;
	}
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	public String getDoNo() {
		return doNo;
	}
	public void setDoNo(String doNo) {
		this.doNo = doNo;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getCommond() {
		return commond;
	}
	public void setCommond(String commond) {
		this.commond = commond;
	}
	public String getDoLineNo() {
		return doLineNo;
	}
	public void setDoLineNo(String doLineNo) {
		this.doLineNo = doLineNo;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getPoLineNo() {
		return poLineNo;
	}
	public void setPoLineNo(String poLineNo) {
		this.poLineNo = poLineNo;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getFactoryCode() {
		return factoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}
	public String getLFDAT() {
		return LFDAT;
	}
	public void setLFDAT(String lFDAT) {
		LFDAT = lFDAT;
	}
	
} 
