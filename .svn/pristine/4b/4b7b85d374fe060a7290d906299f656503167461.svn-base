package com.vtradex.wms.server.service.order;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.entity.order.PurchaseOrder;
import com.vtradex.wms.server.model.entity.order.PurchaseOrderDetail;

public interface WmsPurchaseOrderManager extends BaseManager{
	
	@Transactional
	void saveOrUpdate(PurchaseOrder purchaseOrder);
	
	@SuppressWarnings("rawtypes")
	String getMaxLineNoByPurchaseOrderDetail(Map param);

	
	/**
	 * 新增PurchaseOrderDetail
	 * @param asnId
	 * @param detail
	 * @param expectedQuantity
	 */
	@Transactional
	void addDetail(Long id, PurchaseOrderDetail purchaseOrderDetail, double expectedPackQty);
	
	
	/**
	 * @param details
	 */
	@Transactional
	void removeDetails(PurchaseOrderDetail purchaseOrderDetail);
	
	/**
	 * 确认
	 */
	@Transactional
	void confirm(List<PurchaseOrder> pos);
	
	/**
	 * 接收
	 */
	@Transactional
	void received(List<PurchaseOrder> pos);
	
	/**
	 * 生效
	 */
	void isActivePurchase(PurchaseOrder purchaseOrder);
}
