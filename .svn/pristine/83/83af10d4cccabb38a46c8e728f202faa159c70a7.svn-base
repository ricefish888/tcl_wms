package com.vtradex.wms.server.service.item;

import java.io.File;
import java.util.Date;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.entity.base.WmsAccountCloseDay;
import com.vtradex.wms.server.model.entity.bol.WmsBol;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.inventory.WmsStorageDaily;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;

public interface TclMessageManager extends BaseManager{
	
	/**生成发送给sap的单号以及报文*/
	@Transactional
	void genSendToSapLog();
	/** 
	* @Title: WMS-SAP创建物料属性日志
	* @Description:createItemLog 
	* @return void   
	* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
	* @date 2017-7-18 上午10:52:30  
	* @throws 
	*/
	@Transactional
	void createItemLog(WmsItem item);

	
	/**这个无参的创建日结方法供接口调用*/
	@Transactional
	void createDailyInventory();
	
	/**
	 * 系统上线后  根据某天的库存重新初始化日期
	 * 由于之前的库存日志不准 ， 系统上线后在某天重新初始化日结 则将当前的库存作为今天的期末库存。 以后算日结不能比这一天早。
	 * */
	@Transactional
	void initDailyInventory();
	
	/** 
	* @Title: 创建库存日结
	* @Description: createDaylyInventory2
	* @return void   
	* @throws 
	*/
	@Transactional
	public void createDailyInventory2(Date date) ;
	
	/**
	 * 收货信息写到中间表回传sap
	 */
	@Transactional
	void sendReceiveInfo2SAP(WmsASNDetail detail,Double receiveQty);
	/**
	 * 取消收货信息写到中间表回传sap
	 */
	@Transactional
	void sendCancelReceiveInfo2SAP(WmsASNDetail asnDetail,Double cancleQty);
	
	/**
	 * 质检转合格接口--质检确认回传SAP
	 * @param detail
	 * @param QcQty
	 */
	@Transactional
	void sendQcRecordInfo2SAP(WmsASNDetail detail,String qcStatus ,Double QcQty);
	
	/**
	 * 发料-发运时回传SAP
	 * @param detail
	 * @param inventory
	 * @param pickQty
	 */
	@Transactional
	void sendProductionShipInfo(Long detailId,WmsInventory inventory,Double pickQty,Map<Long,Double> map);
	
	/**
	 * 库存调拨--一个仓库两个工厂之间的调拨
	 */
	@Transactional
	void sendChangeTypeInfo(WmsInventory inventory,Double newFactoryQty,String newFactoryCode,String wareHouseCode);
	
	/**
	 * 生产订单入库JIT下线出库
	 * @param inventory
	 * @param task
	 * @param qty
	 */
	@Transactional
	void shipJITDownLine(WmsInventory inventory,WmsTask task,Double qty);
	
	/**
	 * 销售出库单传SAP
	 */
	@Transactional
	void sendOutDeliveryShipInfo(WmsBol bol);
	
	/**
	 * 保存账单关闭日
	 */
	@Transactional
	void storeWmsAccountCloseDay(WmsAccountCloseDay closeDay);
	
	/**生产订单导入拣配*/
	void importProductionFile(File file);
	 
	/**
	 * 库存调拨--好料仓坏料仓间的调拨
	*/
	@Transactional
	void sendChangeStatusInfo(WmsInventory inventory,Double changQty,String newInvStatus);
	/**
	 * 生产订单导入--定时任务调用
	 */
	void importProductionOrderByTask(Long warehouseId);
	
	@Transactional
	public void insertWarehouseAutoImportPro(Long warehouseId);
	@Transactional
	public void insertAllWarehouseAutoImportPro();
}
