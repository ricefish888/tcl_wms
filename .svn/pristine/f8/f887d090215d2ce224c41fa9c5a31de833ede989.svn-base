package com.vtradex.wms.server.service.production;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.entity.production.DailyDeliverOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrder;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderDetail;

/**
 *交货单
 *xiang.li 
 *2017-7-6 14:50:20
 */
public interface WmsDeliveryOrderManager extends BaseManager {
	
    @Transactional
    void addPoDetail(Long doId, Long podId, WmsDeliveryOrderDetail dod);
    
    /**
     * 确认
     */
    @Transactional
    void confirm(List<WmsDeliveryOrder> dos);
    
    /**
     * 接收
     */
    @Transactional
    void received(List<WmsDeliveryOrder> dos);
	
	/**
	 * 
	 * 新建 修改 保存交货单 (测试用)
	 *
	 * @author Yogurt_lei
	 *
	 * @date 2017年7月7日 下午1:55:42
	 */
	@Transactional
	void storeWmsDeliveryOrder(WmsDeliveryOrder wmsDeliveryOrder);

	/**
	 *
	 * 删除明细
	 * 
	 * @author Yogurt_lei
	 *
	 * @date 2017年7月7日 下午4:25:37
	 */
	@Transactional
	void removeDetails(List<WmsDeliveryOrderDetail> details);
	
	/**
	 * 
	 * 获取最大行号
	 *
	 * @author Yogurt_lei
	 *
	 * @date 2017年7月7日 下午4:10:57
	 */
	String getMaxLineNo(Map<String, Object> param);
	
	@Transactional
	void calculationWmsDeliveryOrder(Date startDate);
	
	/**生成交货单*/
	@Transactional
	void test();
	/**
	 * 激活交货单
	 */
	@Transactional
	void activeDeliveryOrder(WmsDeliveryOrder wmsDeliveryOrder);
	
	/**无事务方法  齐套性验证 + 生成交货单*/
	void importOrderHandleTime(File file);
	
	/**工单齐套性验证  有事务*/
	@Transactional
	void gdqt(File file);
	
	
    //定时任务的月度齐套
	@Transactional
    void gdqt_mon();
	
	/**根据物料ID和工厂编码分组查询实时库存*/
	@Transactional
	public List<Map<String,Object>>  countWmsInventoryGroupByItemIdFactoryCode() ;
	
//	/**根据物料id和工厂编码获取库存，JIT齐套需要用到*/
//	@Transactional
//	Double countWmsItemInventoryByFactory(Long itemId ,String factoryCode);

	/**
	 * 生产订单导入
	 * @param file
	 * @throws BusinessException
	 */
	void importProductFile(File file);
	

	/**
	 * PO批量导入
	 * @param file
	 */
	void importPurchaseOrderFile(File file);
	
	
	/**
	 * 交货单批量导入
	 * @param file
	 */
	void importWmsDeliveryOrderFile(File file);
	
	/**
	 * 交货单失效  如果是销售交货单 失效需要删除对应的拣货单
	 * @param deliveryOrder
	 * @author fs
	 */
	@Transactional
	void unActiveDeliveryOrder(WmsDeliveryOrder deliveryOrder);
	
	/**
	 * 预留单、交货单、工单失效->删除拣货单明细以及关联的数据
	 * @param pdtId 拣货单明细ID
	 * @author fs
	 */
	@Transactional
	void deleteRelatePickData(List<Long> pdtId,List<Long> pickIds,String type);
	
	/**
	 * 预留单批量导入
	 */
	void importReservedOrderFile(File file);
	/**
	 * 创建供应商反馈报文
	 * @param po
	 * @param status
	 */
	@Transactional
	void createWms2SapInterfacelog(WmsDeliveryOrder po,String status);
	
	
	/**
	 * 判断采购订单是否确认(公用方法)
	 */
	void judgePurchaseOrder(WmsDeliveryOrder order);
	
	/**
	 * 新增交货明细的数量
	 */
	@Transactional
	void addDeliverOrderQty(DailyDeliverOrderDetail ddod,Double deliverQty);
	
	/**
	 * 删除每日交货明细
	 * @param ddod
	 */
	@Transactional
	void deleteDailyOrder(DailyDeliverOrderDetail ddod);

	/**
	 * 库存趋势表数据插入
	 */
	@Transactional
	void refreshInventoryTrend();
}
