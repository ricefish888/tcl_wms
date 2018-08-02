package com.vtradex.wms.server.model.entity.order;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderDetail;

/***
 * 到货交货单
 * @author administrator
 *
 */

public class WmsArrivalDelivery extends Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/***交货单明细*/
	private WmsDeliveryOrderDetail deliveryOrderDetail;
	
	/**交货日期*/
	private Date  deliveryDate;
	
	/**交货数量*/
	private Double quantity=0D;
	
	/**生成日期*/
	private Date genDate=new Date();

	public WmsDeliveryOrderDetail getDeliveryOrderDetail() {
		return deliveryOrderDetail;
	}

	public void setDeliveryOrderDetail(WmsDeliveryOrderDetail deliveryOrderDetail) {
		this.deliveryOrderDetail = deliveryOrderDetail;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Date getGenDate() {
		return genDate;
	}

	public void setGenDate(Date genDate) {
		this.genDate = genDate;
	}
	
	
	
}
