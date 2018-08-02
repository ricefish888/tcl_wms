package com.vtradex.wms.server.service.sap;

import java.io.File;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.entity.item.WmsMinPackageQty;

public interface WmsMinPackageManager extends BaseManager{

	/**保存最小包装单位*/
	@Transactional
	void storeMinPackage(WmsMinPackageQty min);
	
	
	/**
	 * 导入最小包装量
	 * @param file
	 */
	@Transactional
	void importWmsMinPackageQtyFile(File file);
}
