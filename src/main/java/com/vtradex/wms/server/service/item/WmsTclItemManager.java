package com.vtradex.wms.server.service.item;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.wms.server.model.entity.item.WmsItem;

public interface WmsTclItemManager extends WmsItemManager {
	
	

	/**
	 * 新建/修改
	 */
	@Transactional
	void storeWmsItem(WmsItem item ,Long companyId);
	
	
	
	/**
	 * 导入物料
	 * @param map
	 */
	@Transactional
	void initWmsItemByImport(Map<String,String> map);
	/**
	 * 导入物料属性
	 * @param map
	 */
	@Transactional
	void initWmsItemAttrByImport(Map<String,String> map);
	/**
	 * 导入物料上架分配
	 * @param map
	 */
	@Transactional
	void initWmsItemUpByImport(Map<String,String> map);
	
	/**
	 * 修改是否可录入编码属性
	 */
	@Transactional
	void editWmsItemScan(WmsItem item,String userFieldV10);
}