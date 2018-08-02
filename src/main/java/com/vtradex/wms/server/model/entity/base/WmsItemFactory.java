package com.vtradex.wms.server.model.entity.base;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.entity.item.WmsItem;
/***
 * 物料和工厂的映射关系
 * @author administrator
 *
 */
public class WmsItemFactory extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7758520685477204624L;

	@UniqueKey
	private WmsSapFactory factory;
	
	@UniqueKey
	private WmsItem item;

	public WmsSapFactory getFactory() {
		return factory;
	}

	public void setFactory(WmsSapFactory factory) {
		this.factory = factory;
	}

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}
	

}
