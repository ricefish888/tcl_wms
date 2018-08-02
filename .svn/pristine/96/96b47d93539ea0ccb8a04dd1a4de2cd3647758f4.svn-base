package com.vtradex.wms.server.model.entity.base;

import com.vtradex.thorn.server.model.Entity;
/**
 * 洗衣机仓库不可拆分物料零头量 
 */
public class WmsProductionOrderDetailExtend extends Entity {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 4185757997357768914L;
	//源ID     				
	private Long srcId;
	//目标ID
	private Long descId;
	//零头量
	private Double oddQty = 0D;
	//已分配量
	public Double allocatedQuantityBU = 0D;
	//已拣货量
	public Double picQuantityBU = 0D;
	//已发运量
	public Double shipQuantityBU = 0D;
	
	public WmsProductionOrderDetailExtend() {
		super();
	}
	public WmsProductionOrderDetailExtend(Long srcId, Long descId, Double oddQty) {
		super();
		this.srcId = srcId;
		this.descId = descId;
		this.oddQty = oddQty;
	}
	public Long getSrcId() {
		return srcId;
	}
	public void setSrcId(Long srcId) {
		this.srcId = srcId;
	}
	public Long getDescId() {
		return descId;
	}
	public void setDescId(Long descId) {
		this.descId = descId;
	}
	public Double getOddQty() {
		return oddQty;
	}
	public void setOddQty(Double oddQty) {
		this.oddQty = oddQty;
	}
	public Double getAllocatedQuantityBU() {
		return allocatedQuantityBU;
	}
	public void setAllocatedQuantityBU(Double allocatedQuantityBU) {
		this.allocatedQuantityBU = allocatedQuantityBU;
	}
	public Double getPicQuantityBU() {
		return picQuantityBU;
	}
	public void setPicQuantityBU(Double picQuantityBU) {
		this.picQuantityBU = picQuantityBU;
	}
	public Double getShipQuantityBU() {
		return shipQuantityBU;
	}
	public void setShipQuantityBU(Double shipQuantityBU) {
		this.shipQuantityBU = shipQuantityBU;
	}
}
