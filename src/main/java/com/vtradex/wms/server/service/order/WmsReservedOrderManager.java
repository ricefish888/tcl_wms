package com.vtradex.wms.server.service.order;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.model.entity.item.WmsCompany;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrder;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderDetail;
import com.vtradex.wms.server.model.entity.receiving.WmsASN;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;

public interface WmsReservedOrderManager extends BaseManager{
	
	//保存
	@Transactional
	void saveReservedOrder(WmsReservedOrder reservedOrder);
	
	
	//保存明细
	@Transactional
	void saveOrderDetail(Long id,WmsReservedOrderDetail wmsReservedOrderDetail);
	
	//保存明细
	@Transactional
	void deleteOrderDetail(WmsReservedOrderDetail wmsReservedOrderDetail);
	
	/**出库类型预留单生效,生成拣货单*/
	@Transactional
	void activeReserveOrder(WmsReservedOrder reservedOrder);
	
	/**入库类型预留单生效,生成拣货单*/
	@Transactional
	void createAsnByReservedOrder(WmsReservedOrder reservedOrder);
	
	/**
	 * 创建ASN公用方法
	 * @param company 货主
	 * @param billCode 单据类型编码
	 * @param warehouse 仓库
	 * @param date 订单日期
	 * @param orderCode 单号
	 * @param supplierCode 供应商编码
	 * @param supplierName 供应商名称
	 * @param supplier 供应商
	 * @param bgy 保管员
	 */
	@Transactional
	WmsASN createAsnByValues(List<Object[]> detailValues,WmsASN asn);
	
	/**
	 * 如果预留单生成了ASN或者拣货单,失效的时候需要删除对应的ASN或者拣货单
	 * @param reservedOrder
	 */
	@Transactional
	void unActiveOrder(WmsReservedOrder reservedOrder);
	
	/**
	 * 批量选中预留单明细生成拣货单,按照 工厂 分单--无事务方法
	 * fs
	 */
	void createPickNoTransaction(List<WmsReservedOrderDetail> details);
	
	/**
	 * 批量选中预留单明细生成拣货单,按照 工厂 分单 -- 有事务
	 * @param reservedOrderDetail
	 * @author fs 2017-8-30 09:47:19
	 */
	@Transactional
	List<WmsPickTicket> createPickByReserveOrderDetail(List<WmsReservedOrderDetail> details);
	
	/***
	 * 创建预留单
	 * @param map
	 * @param picks
	 */
	@Transactional
	void createPtBywasherFactory(Map<String,List<WmsReservedOrderDetail>> map,List<WmsPickTicket> picks);
	
	/**
	 * 取消拣配单
	 * @param detail
	 */
	@Transactional
	void cancelPick(WmsReservedOrderDetail detail);
	
	/**
	 * 根据预留明细生成收货单，预留单+保管员 分单 有事务
	 * @param reservedOrderDetails
	 * @param detailValues
	 */
	@Transactional
	void createAsnByRod(List<WmsReservedOrderDetail> reservedOrderDetails);
	
	/**
	 * 取消收货单
	 */
	@Transactional
	void cancelASN(WmsReservedOrderDetail detail);
	/**
	 * 出库取消
	 */
	@Transactional
	void cancelShip(WmsReservedOrderDetail detail,Double cancelQty);
}
