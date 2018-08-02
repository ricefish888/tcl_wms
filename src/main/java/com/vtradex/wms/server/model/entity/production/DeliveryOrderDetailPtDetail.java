package com.vtradex.wms.server.model.entity.production;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;

/***
 * 交货单明细  拣货单明细  之间的关联关系
 * @author fs
 *
 */
public class DeliveryOrderDetailPtDetail extends Entity {
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 3640787607286433509L;

	/**交货单明细*/
	private WmsDeliveryOrderDetail deliveryOrderDetail;
	
	/**拣配单明细*/
	private WmsPickTicketDetail pickticketDetail;
	
	/**关联 数量*/
	private Double quantityBu=0D;
	
	private WmsPackageUnit unit;


	public WmsDeliveryOrderDetail getDeliveryOrderDetail() {
		return deliveryOrderDetail;
	}

	public void setDeliveryOrderDetail(WmsDeliveryOrderDetail deliveryOrderDetail) {
		this.deliveryOrderDetail = deliveryOrderDetail;
	}

	public WmsPickTicketDetail getPickticketDetail() {
		return pickticketDetail;
	}

	public void setPickticketDetail(WmsPickTicketDetail pickticketDetail) {
		this.pickticketDetail = pickticketDetail;
	}

	public Double getQuantityBu() {
		return quantityBu;
	}

	public void setQuantityBu(Double quantityBu) {
		this.quantityBu = quantityBu;
	}

	public WmsPackageUnit getUnit() {
		return unit;
	}

	public void setUnit(WmsPackageUnit unit) {
		this.unit = unit;
	}

	public DeliveryOrderDetailPtDetail(){}
	public DeliveryOrderDetailPtDetail(
			WmsDeliveryOrderDetail deliveryOrderDetail,
			WmsPickTicketDetail pickticketDetail, Double quantityBu,
			WmsPackageUnit unit) {
		super();
		this.deliveryOrderDetail = deliveryOrderDetail;
		this.pickticketDetail = pickticketDetail;
		this.quantityBu = quantityBu;
		this.unit = unit;
	}

}
