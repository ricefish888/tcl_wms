package com.vtradex.wms.server.model.entity.item;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.model.entity.item.WmsItem;

public class WmsMinPackageQty extends Entity{

	private static final long serialVersionUID = 2890912073223348088L;
	/**供应商*/
	private WmsSupplier supplier;
	/**物料*/
	private WmsItem item;
	/**最小包装量*/
	private Double minUnit=0D;
	
	public WmsSupplier getSupplier() {
		return supplier;
	}
	public void setSupplier(WmsSupplier supplier) {
		this.supplier = supplier;
	}
	public WmsItem getItem() {
		return item;
	}
	public void setItem(WmsItem item) {
		this.item = item;
	}
	public Double getMinUnit() {
		return minUnit;
	}
	public void setMinUnit(Double minUnit) {
		this.minUnit = minUnit;
	}

}
