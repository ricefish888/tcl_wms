package com.vtradex.wms.server.model.entity.production;

import java.util.Date;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.utils.DoubleUtils;

/**
 * 交货单明细按交货日期拆开
 * @author fs
 * @date 2017-8-25 21:38:43
 */
public class DailyDeliverOrderDetail extends VersionalEntity{

	private static final long serialVersionUID = -4213375327032603972L;

	/**交货单明细*/
	private WmsDeliveryOrderDetail orderDetail;
	
	/**交货日期*/
	private Date deliverDate;
	
	/**本次交货数量*/
	private Double deliverQty;
	
	/**配货数量*/
	private Double loadQty;
	
	/**系统自动生成或者手工新建 标识*/
	private Boolean isAutoCreate;

	
	
	public Boolean getIsAutoCreate() {
		return isAutoCreate;
	}

	public void setIsAutoCreate(Boolean isAutoCreate) {
		this.isAutoCreate = isAutoCreate;
	}

	public void addDeliverQty(Double qty) {
	    this.deliverQty = DoubleUtils.add(this.deliverQty, qty);
	}
	
	public WmsDeliveryOrderDetail getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(WmsDeliveryOrderDetail orderDetail) {
		this.orderDetail = orderDetail;
	}

	public Date getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	public Double getDeliverQty() {
		return deliverQty;
	}

	public void setDeliverQty(Double deliverQty) {
		this.deliverQty = deliverQty;
	}

	public Double getLoadQty() {
		return loadQty;
	}

	public void setLoadQty(Double loadQty) {
		this.loadQty = loadQty;
	}
	public DailyDeliverOrderDetail(){}
	public DailyDeliverOrderDetail(WmsDeliveryOrderDetail orderDetail,
			Date deliverDate, Double deliverQty, Double loadQty,Boolean isAutoCreate) {
		super();
		this.orderDetail = orderDetail;
		this.deliverDate = deliverDate;
		this.deliverQty = deliverQty;
		this.loadQty = loadQty;
		this.isAutoCreate = isAutoCreate;
	}

	
}
