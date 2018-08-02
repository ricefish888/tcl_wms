package com.vtradex.wms.server.service.order;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.entity.order.WmsCheckOrder;

public interface WmsCheckOrderManager extends BaseManager{
	
	/**
	 * 确认
	 */
	@Transactional
	void confirm(List<WmsCheckOrder> cos);
	
	/**
	 * 接收
	 */
	@Transactional
	void received(List<WmsCheckOrder> cos);

}
