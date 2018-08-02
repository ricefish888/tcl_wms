package com.vtradex.wms.server.service.base;

import java.io.File;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.entity.base.WmsItemKeeper;

/**
 * 物料保管员
 */
public interface WmsItemKeeperManager extends BaseManager {
	
	
	//保存
	@Transactional
	void store(WmsItemKeeper itemKeeper);
	/**
	 * 导入物料保管员
	 * @param file
	 */
	@Transactional
	void importWmsItemKeeperFile(File file);
}
