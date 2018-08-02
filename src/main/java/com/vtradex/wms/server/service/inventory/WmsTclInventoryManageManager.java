package com.vtradex.wms.server.service.inventory;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;

/** 
* @ClassName: TCL库存管理定制
* @Description: WmsTclInventoryManager 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-8-7 上午9:36:02  
*/
public interface WmsTclInventoryManageManager extends WmsInventoryManageManager{
	/**内外销调拨*/
	@Transactional
	void changeType(WmsInventory inventory,Double newFactoryQty,String newFactoryCode);
	
	/**正残品调拨*/
	@Transactional
	void changeStatus(WmsInventory inventory,Double newFactoryQty,Long invstatusid,Long newLocId);
	
	/**移位 */
	@Transactional
	void transposing(WmsInventory inventory,Double newFactoryQty,Long newLocId);
	
	/**
	 * 库存初始化导入数据
	 * */
	@Transactional
	void initInventoryByImport(Map<String,String> map);
	
	/**VMI与自管仓库间调拨*/
	@Transactional
	void changeHouse(WmsInventory inventory,Double qty,Long locationId);
	
	
	/**修改供应商*/
	@Transactional
	void modifySupplier(WmsInventory inventory,Long supplierId);
	
	
	/**修改线体*/
	@Transactional
	void modifyXT(WmsInventory inventory,String xt,Double qty);
	
	/**自管仓间调拨*/
	@Transactional
	void changeHouseInZG(WmsInventory inventory,Double newFactoryQty,String newFactoryCode);
}
