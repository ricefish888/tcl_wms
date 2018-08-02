package com.vtradex.wms.server.model.entity.base;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;

/**
 * 物料保管员
 * 
 * @author <a href="mailto:ming.chen@tech.vtradex.com">陈明</a>
 * @since Dec 10, 2015
 */
public class WmsItemKeeper extends Entity {

	 
	/**
	 * 
	 */
	private static final long serialVersionUID = -7269145033278691760L;
	/** 物料*/
	private WmsItem item;
	/** 仓库*/
	private WmsWarehouse warehouse;
	/** 工厂 可为空  为空则为所有*/
	private WmsSapFactory factory;
	/** 保管员*/
	private ThornUser keeper;
	/** 状态*/
	private String status;
	
	public WmsItem getItem() {
		return item;
	}
	public void setItem(WmsItem item) {
		this.item = item;
	}
	public WmsWarehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}
	
	public WmsSapFactory getFactory() {
		return factory;
	}
	public void setFactory(WmsSapFactory factory) {
		this.factory = factory;
	}
	public ThornUser getKeeper() {
		return keeper;
	}
	public void setKeeper(ThornUser keeper) {
		this.keeper = keeper;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	 
     
}
