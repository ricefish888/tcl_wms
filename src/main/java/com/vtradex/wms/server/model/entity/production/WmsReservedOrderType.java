package com.vtradex.wms.server.model.entity.production;

public interface WmsReservedOrderType {
	
	
	//Z01 Z03 Z311为出库预留申请
	String Z01 = "Z01";
	
	String Z03 = "Z03";
	
	String Z311="311";
	
	//Z02 Z04为入库预留申请
	String Z02="Z02";
	
	String Z04="Z04";
	//用于预留退料时Z311对应的入库移动类型
	String Z312 ="312";
	
}
