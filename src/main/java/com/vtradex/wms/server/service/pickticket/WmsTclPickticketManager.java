package com.vtradex.wms.server.service.pickticket;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderDetail;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;

public interface WmsTclPickticketManager {
	
	/**
	 * 自动分配 整单
	 * @Description:
	 * @Author:        <a href="yequan.song@vtradex.net">宋叶全</a>
	 * @CreateDate:    2015年12月15日
	 * @param pickTicket:
	 * @arithMetic:
	 * @exception:
	 */
	public void autoAllocate(WmsPickTicket pickTicket);
	
	
	@Transactional
	void pickTicketconvertWarehouse(WmsPickTicket wmsPickTicket);
	
	
	
	
	/**
	 * 
	 * @Title: supplyWarehouseReplenish
	 * 
	 * @Description: 供货仓拉动补货
	 * 
	 * @return void    
	 *
	 * @throws 
	 *
	 * @author <a href="mailto:xu.feng@vtradex.com"/>冯旭/a>
	 *
	 * @date 2017年6月20日 10:08:56
	 */
	@Transactional
	Map<String,Object> tclSupplyWarehouseReplenish(WmsPickTicket wmsPickTicket);
	
	/**供货仓拉动补货*/
	/**无事务主方法*/
	void supplyWarehouseReplenishNoTransaction(WmsPickTicket wmsPickTicket);
	
	@Transactional
	void tclAfterSupplyWarehouseReplenish(WmsPickTicket wmsPickTicket,WmsPickTicket supplyPickTicket,Boolean firstLd);
	
	  
	/**
	 * 保存拣货单
	 * xiang.li 2017年8月7日11:16:51
	 */
	@Transactional
	void storePickTicket(WmsPickTicket pickTicket);
	
	
	
	
	/**
	 * 添加拣货单明细
	 * @Description:
	 * @Author:       <a href="yang.liu@vtradex.net">刘杨</a>
	 * @CreateDate:    2015年12月14日
	 * @param pickTicketId
	 * @param pickTicketDetail
	 * @param status:
	 * @arithMetic:
	 * @exception:
	 */
	@Transactional
	void addPickTicketDetail(Long pickTicketId,WmsPickTicketDetail pickTicketDetail);
	
	
	/**
	 * 移除拣货单明细
	 * @Description:
	 * @Author:        <a href="yang.liu@vtradex.net">刘杨</a>
	 * @CreateDate:    2015年12月14日
	 * @param pickTicketDetail:
	 * @arithMetic:
	 * @exception:
	 */
	@Transactional
	void removePickTicketDetail(WmsPickTicketDetail pickTicketDetail);
	
	
	/**添加拣货单明细明细*/
	@Transactional
	void addPickTicketDetail2(Long pickTicketId,WmsPickTicketDetail pickTicketDetail);
	
	/**
	 * 生产领料单退拣
	 * @param invId
	 * @param pod
	 * @param tableValues
	 */
	@Transactional
	void singleBackUp(Long invId,ProductionOrderDetail pod,List<String> tableValues);
	
	/**
	 * 预留出库单退拣
	 * @param invId
	 * @param pod
	 * @param tableValues
	 */
	@Transactional
	void singleYLBackUp(Long invId,WmsReservedOrderDetail rod,List<String> tableValues);

	/**
	 * 普通单据退拣
	 * @param invId
	 * @param pod
	 * @param tableValues
	 */
	@Transactional
	void normalSingleBackUp(WmsInventory inv,List<String> tableValues);
	
	/**
	 * 退拣生成拣货单
	 * @param productDetailId
	 * @param qty
	 * @param warehouse
	 */
	void createPickByTj(Long productDetailId,Double qty,WmsWarehouse warehouse,String source);
}
