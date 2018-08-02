package com.vtradex.wms.server.model.entity.order;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderDetail;
import com.vtradex.wms.server.utils.DoubleUtils;

/***
 * 配货单  由供应商新建
 * @author administrator
 *
 */
public class WmsTransportOrderDetail extends VersionalEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2348842170482610735L;

	private WmsTransportOrder transportOrder;
	
	private Integer lineNo;
	
	/**物料*/
	private WmsItem item;
	
	private WmsPackageUnit packageUnit;
	
	/**收货数量*/
	private Double receiveQty =0D;
	
	/**原始配货数量*/
	private Double oldQuantity =0D;
	
	/**配货数量 每次收完货后 会回写修改此数量*/
	private Double quantity =0D;
	
	/**状态
	 * {@link WmsTransportOrderDetailStatus}
	 */
	private String status = WmsTransportOrderDetailStatus.ACTIVE;
	
	/**交货单明细*/
	private WmsDeliveryOrderDetail deliveryOrderDetail;
	
	/**描述*/
	private String remark;

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getReceiveQty() {
		return receiveQty;
	}

	public void setReceiveQty(Double receiveQty) {
		this.receiveQty = receiveQty;
	}

	public Double getOldQuantity() {
		return oldQuantity;
	}

	public void setOldQuantity(Double oldQuantity) {
		this.oldQuantity = oldQuantity;
	}

	public WmsTransportOrder getTransportOrder() {
		return transportOrder;
	}

	public void setTransportOrder(WmsTransportOrder transportOrder) {
		this.transportOrder = transportOrder;
	}

	public Integer getLineNo() {
		return lineNo;
	}

	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public WmsPackageUnit getPackageUnit() {
		return packageUnit;
	}

	public void setPackageUnit(WmsPackageUnit packageUnit) {
		this.packageUnit = packageUnit;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public WmsDeliveryOrderDetail getDeliveryOrderDetail() {
		return deliveryOrderDetail;
	}

	public void setDeliveryOrderDetail(WmsDeliveryOrderDetail deliveryOrderDetail) {
		this.deliveryOrderDetail = deliveryOrderDetail;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
	 * 配货数量-收货数量
	 */
	public void subQuantity(Double qty){
		this.quantity = DoubleUtils.sub(this.quantity, qty);
	}
	
 

}
