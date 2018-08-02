package com.vtradex.wms.server.model.entity.production;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.webservice.utils.CommonHelper;

/***
 * 生产订单明细  拣货单明细  之间的关联关系
 * @author administrator
 *
 */
public class ProductionOrderDetailPtDetail extends Entity {
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 3640787607286433509L;

	/**生产订单明细*/
	private ProductionOrderDetail productionOrderDetail;
	
	/**拣配单明细*/
	private WmsPickTicketDetail pickticketDetail;
	
	/**关联 数量*/
	private Double quantityBu=0D;
	
	private WmsPackageUnit unit;
	/**最后一次分配数量*/
	private Double lastAllocatedQty =0D;
	
	/**创建时间(拆对应关系时，拆出来的新对应关系要和老对应关系 时间保持一致)*/
	private Date ctDate;
	
	/**发运数量*/
	private Double shipQty =0D;
	
	/**拣货数量*/
	private Double pickQty =0D;
	
	public Double getPickQty() {
		return pickQty;
	}

	public void setPickQty(Double pickQty) {
		this.pickQty = pickQty;
	}

	public Double getShipQty() {
		return shipQty;
	}

	public void setShipQty(Double shipQty) {
		this.shipQty = shipQty;
	}

	public Double getLastAllocatedQty() {
		return lastAllocatedQty;
	}

	public void setLastAllocatedQty(Double lastAllocatedQty) {
		this.lastAllocatedQty = lastAllocatedQty;
	}

	public Date getCtDate() {
		return ctDate;
	}

	public void setCtDate(Date ctDate) {
		this.ctDate = ctDate;
	}

	public ProductionOrderDetail getProductionOrderDetail() {
		return productionOrderDetail;
	}

	public void setProductionOrderDetail(ProductionOrderDetail productionOrderDetail) {
		this.productionOrderDetail = productionOrderDetail;
	}

	public WmsPickTicketDetail getPickticketDetail() {
		return pickticketDetail;
	}

	public void setPickticketDetail(WmsPickTicketDetail pickticketDetail) {
		this.pickticketDetail = pickticketDetail;
	}

	public Double getQuantityBu() {
		return CommonHelper.dealDoubleError(quantityBu);
	}

	public void setQuantityBu(Double quantityBu) {
		this.quantityBu = CommonHelper.dealDoubleError(quantityBu) ;
	}

	public WmsPackageUnit getUnit() {
		return unit;
	}

	public void setUnit(WmsPackageUnit unit) {
		this.unit = unit;
	}
	public void addQuantityBu(Double qty){
		this.quantityBu = DoubleUtils.add(this.quantityBu, qty);
		this.quantityBu = CommonHelper.dealDoubleError(this.quantityBu) ;
	}
	
	public void removeQuantityBu(Double qty){
		this.quantityBu = DoubleUtils.sub(this.quantityBu, qty);
		this.quantityBu = CommonHelper.dealDoubleError(this.quantityBu) ;
	}
	
	public void addShipQty(Double qty){
		this.shipQty = DoubleUtils.add(this.shipQty, qty);
		this.shipQty = CommonHelper.dealDoubleError(this.shipQty) ;
	}
	
	public void addPickQty(Double qty){
		this.pickQty = DoubleUtils.add(this.pickQty, qty);
		this.pickQty = CommonHelper.dealDoubleError(this.pickQty) ;
	}
}
