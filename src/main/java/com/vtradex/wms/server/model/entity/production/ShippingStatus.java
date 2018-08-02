package com.vtradex.wms.server.model.entity.production;

/**
 * 发运状态
 * @author Administrator
 *
 */
public interface ShippingStatus {
	
	/**
	 * 部分发运
	 */
	String UNSHIPPED = "UNSHIPPED";
	/**
	 * 发运完成
	 */
	String SHIPPED = "SHIPPED";
}
