package com.vtradex.wms.server.model.entity.production;
/***
 * 交货单状态
 * @author administrator
 *
 */
public interface WmsDeliveryOrderStatus {
	
	//打开
	String OPEN="OPEN";
	
	//已取消
	String CLOSED="CLOSED";
	
	//已确定
	String CONFIRMED ="CONFIRMED";
	
	//部分收货
	String  PARTIALRECEIPT = "PARTIALRECEIPT";
	
	//收货完成
	String FINISH ="FINISH";
	
	//生效
	String ACTIVE ="ACTIVE";

}
