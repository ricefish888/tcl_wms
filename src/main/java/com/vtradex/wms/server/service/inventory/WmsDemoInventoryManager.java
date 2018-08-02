package com.vtradex.wms.server.service.inventory;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
public interface WmsDemoInventoryManager extends BaseManager{
	/**
	 * 库存初始化导入数据
	 * */
	@Transactional
	void initInventoryByImport(Map<String,String> map);
}
