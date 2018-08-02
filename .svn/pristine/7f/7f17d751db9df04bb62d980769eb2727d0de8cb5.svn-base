package com.vtradex.wms.server.model.entity.base;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;

/***
 * 工厂  交货地(项目类别)  wms仓库映射关系
 * @author administrator
 *
 */
public class WmsFactoryWarehouse extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2759478463909508688L;

	private WmsSapFactory  factory;
	
	/**项目类别   采购订单接口的pstyt字段 0为标准2为寄售
	 * {@link WmsFactoryXmlb}
	 * */
	private String  type; 
	
	private WmsWarehouse warehouse;

	public WmsSapFactory getFactory() {
		return factory;
	}

	public void setFactory(WmsSapFactory factory) {
		this.factory = factory;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}


}
