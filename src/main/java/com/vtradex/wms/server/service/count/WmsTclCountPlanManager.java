package com.vtradex.wms.server.service.count;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.entity.count.WmsCountPlan;

/**
 * 盘点
 * @author fs
 * @date 2017-8-25 11:03:44
 */
public interface WmsTclCountPlanManager extends BaseManager {

	/**
	 * 盘点计划关闭
	 * @param plan
	 */
	@Transactional
	void close(WmsCountPlan plan);
	
}
