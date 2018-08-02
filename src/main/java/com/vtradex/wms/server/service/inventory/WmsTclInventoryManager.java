package com.vtradex.wms.server.service.inventory;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.wms.server.model.entity.base.WmsBarCodeRepPrintRecord;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;

/** 
* 
*/
public interface WmsTclInventoryManager extends WmsInventoryManager{
	@Transactional
	void backUp(WmsInventory inventory,Double backUpQty);
	
	
	/**
	 * 条码补打
	 */
	Map print(List<WmsInventory> inventorys);
}
