package com.vtradex.wms.server.model.entity.order;

public interface WmsTransportOrderStatus {
	/** 打开 */
	public static final String OPEN = "OPEN";
	/** 在途 */
	public static final String ACTIVE = "ACTIVE";
	
	/** 部分收货 */
	public static final String RECEIVING = "RECEIVING";
 
	/** 完成 */
	public static final String RECEIVED = "RECEIVED";
}
