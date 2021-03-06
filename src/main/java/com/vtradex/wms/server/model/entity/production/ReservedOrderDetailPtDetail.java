package com.vtradex.wms.server.model.entity.production;

import com.vtradex.engine.utils.DoubleUtil;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.utils.DoubleUtils;

/***
 * 预留单明细  拣货单明细  之间的关联关系
 * @author administrator
 *
 */
public class ReservedOrderDetailPtDetail extends Entity {
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 3640787607286433509L;

	/**预留单明细*/
	private WmsReservedOrderDetail reservedOrderDetail;
	
	/**拣配单明细*/
	private WmsPickTicketDetail pickticketDetail;
	
	/**关联 数量*/
	private Double quantityBu=0D;
	
	private WmsPackageUnit unit;


	public WmsReservedOrderDetail getReservedOrderDetail() {
		return reservedOrderDetail;
	}

	public void setReservedOrderDetail(WmsReservedOrderDetail reservedOrderDetail) {
		this.reservedOrderDetail = reservedOrderDetail;
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

	public void removeQuantityBu(Double qty){
		this.quantityBu = DoubleUtils.sub(this.quantityBu,qty);
	}
	public void addQuantity(Double qty){
		this.quantityBu = DoubleUtils.add(this.quantityBu,qty);
	}
	
	public ReservedOrderDetailPtDetail(){}
	public ReservedOrderDetailPtDetail(
			WmsReservedOrderDetail reservedOrderDetail,
			WmsPickTicketDetail pickticketDetail, Double quantityBu,
			WmsPackageUnit unit) {
		super();
		this.reservedOrderDetail = reservedOrderDetail;
		this.pickticketDetail = pickticketDetail;
		this.quantityBu = quantityBu;
		this.unit = unit;
	}

}
