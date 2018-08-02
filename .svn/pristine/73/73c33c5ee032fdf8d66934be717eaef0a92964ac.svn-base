package com.vtradex.wms.server.service.sap;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.model.interfaceLog.InterfaceLog;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.entity.base.Wms2SapInterfaceLog;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogFunction;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogTaskType;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogType;
import com.vtradex.wms.webservice.model.InterfaceLogSys;
import com.vtradex.wms.webservice.sap.model.SapCheckOrderArray;
import com.vtradex.wms.webservice.sap.model.SapCostCenterArray;
import com.vtradex.wms.webservice.sap.model.SapDeliveryOrderArray;
import com.vtradex.wms.webservice.sap.model.SapItemArray;
import com.vtradex.wms.webservice.sap.model.SapJSCheckOrderArray;
import com.vtradex.wms.webservice.sap.model.SapPoArray;
import com.vtradex.wms.webservice.sap.model.SapProductOrderArray;
import com.vtradex.wms.webservice.sap.model.SapProductOrderInArray;
import com.vtradex.wms.webservice.sap.model.SapReservedDataArray;
import com.vtradex.wms.webservice.sap.model.SapReturnOrderCodeArray;
import com.vtradex.wms.webservice.sap.model.SapSaleOutDeliveryArray;
import com.vtradex.wms.webservice.sap.model.SapSupplierArray;
import com.vtradex.wms.webservice.sap.model.SapWarehouseArray;

/**SAP 接口数据处理的单条处理类*/
public interface SapRowDataDealManager extends BaseManager {
	
	@Transactional
	void storeInterfaceLog2(InterfaceLog interfaceLog,String responseXml);
	
	@Transactional
	void storeInterfaceLog(InterfaceLog interfaceLog,String responseXml,String errorInfo) ;
	
	/**处理物料*/
	@Transactional
	void dealSapItem(SapItemArray sapItemArray);

	/**处理sap仓库 */
	@Transactional
	void dealSapWarehouse(SapWarehouseArray sapWarehouseArray);
	
	/**处理sap供应商 */
	@Transactional
	void dealSapSupplier(SapSupplierArray sapSupplierArray);
	
	/**
     * 成本中心主数据
     * @param sccs
     */
    @Transactional
    void dealSapCostCenter(SapCostCenterArray sccs);
	
	/**处理sap采购订单  需要加事务*/
	@Transactional
	void dealSapPoOrders(SapPoArray poArray);
	/**处理采购交货单*/
	@Transactional
	void dealSapDeliveryOrder(SapDeliveryOrderArray spoas);

	/**处理生产订单*/
	@Transactional
	void dealSapProductOrder(SapProductOrderArray spoas);
	
	/**处理预留主数据*/
	@Transactional
	void dealSapReservedData(SapReservedDataArray datas);
	
	/**处理外向交货单*/
	@Transactional
	void dealSapSaleOutDelivery(SapSaleOutDeliveryArray datas);
	
	/**
     * 创建报文
     * 
     * @param taskType {@link InterfaceLogTaskType}
     * @param type {@link InterfaceLogType}
     * @param function {@link InterfaceLogFunction}
     * @param fromSys {@link InterfaceLogSys}
     * @param toSys {@link InterfaceLogSys}
     * @param requestXml
     */
    @Transactional
    public Wms2SapInterfaceLog createWms2SapInterfaceLog(String taskType, String type, String function, String fromSys, String toSys, String requestXml);
    
    /**
     * 处理sap返回的交货单 单号
     */
    @Transactional
    void dealSapReturnOrderCode(SapReturnOrderCodeArray srocs);
    
    /**
     * 处理sap标准对账单
     */
    @Transactional
    void dealSapCheckOrder(SapCheckOrderArray scoas);
    
    /**
     * 处理sap寄售对账单
     */
    @Transactional
    void dealSapJSCheckOrder(SapJSCheckOrderArray scoas);
    
    /**
     * 生产订单入库单
     */
    @Transactional
    void dealSapProductOrderIn(SapProductOrderInArray spoias);
    
}
