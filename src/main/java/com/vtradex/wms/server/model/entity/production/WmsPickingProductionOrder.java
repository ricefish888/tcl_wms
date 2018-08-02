package com.vtradex.wms.server.model.entity.production;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;

/**
 * 工单导入记录表
 * @author Administrator
 *
 */
public class WmsPickingProductionOrder extends VersionalEntity {

	private static final long serialVersionUID = 1263869413680214712L;
	/**
	 * 仓库
	 */
	private WmsWarehouse warehouse;
	/**
	 * 工单号
	 */
	private String prodOrderCode;
	
	public WmsWarehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}
	public String getProdOrderCode() {
		return prodOrderCode;
	}
	public void setProdOrderCode(String prodOrderCode) {
		this.prodOrderCode = prodOrderCode;
	}
}
