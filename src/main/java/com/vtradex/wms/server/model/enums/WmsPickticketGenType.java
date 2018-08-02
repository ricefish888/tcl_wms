package com.vtradex.wms.server.model.enums;

/**
 * 生成拣货单的来源方式
 * @author fs
 * @date 2017-8-2 11:28:21
 */
public interface WmsPickticketGenType {
	public static String SCLLD = "SCLLD";/** 生产订单编码   (生产订单->拣货单)*/
	
	public static String YLCKD = "YLCKD";/** 预留出库单编码   (出库类型的预留单->拣货单)*/
	
	public static String XSJHD = "XSJHD"; /**销售交货单  (销售出库类型的交货单->拣货单)*/
	
}
