package com.vtradex.wms.server.service.model.interfaceLog;

/**
 * 接口类型定义
 */
public interface InterfaceLogType {
	
	 /*********************************************SAP给WMS的*****************************/

    /**基础数据-物料*/
    String BASIC_ITEM_SAP2WMS = "BASIC_ITEM_SAP2WMS";
    
    /**基础数据-供应商*/
    String BASIC_SUPPLIER_SAP2WMS = "BASIC_SUPPLIER_SAP2WMS";
    
    /**基础数据-仓库*/
    String BASIC_WAREHOUSE_SAP2WMS = "BASIC_WAREHOUSE_SAP2WMS";
    
    /**基础数据-成本中心*/
    String BASIC_COSTCENTER_SAP2WMS = "BASIC_COSTCENTER_SAP2WMS";
    
    
    /**业务数据-采购订单*/
    String BUSINESS_PO_SAP2WMS="BUSINESS_PO_SAP2WMS";
   
    /**业务数据-采购交货单*/
    String BUSINESS_DELIVERYORDER_SAP2WMS="BUSINESS_DELIVERYORDER_SAP2WMS";
    
    /**业务数据-SAP生成的交货单号*/
    String BUSINESS_ORDERCODE_SAP2WMS="BUSINESS_ORDERCODE_SAP2WMS";
    
    /**业务数据-生产订单(工单)*/
    String BUSINESS_PRODUCTORDER_SAP2WMS="BUSINESS_PRODUCTORDER_SAP2WMS";
 
    /**业务数据-预留主数据*/
    String BUSINESS_RESERVEDDATA_SAP2WMS="BUSINESS_RESERVEDDATA_SAP2WMS";
    
    /**业务数据-Sap销售外向交货单*/
    String BUSINESS_SALEOUTDELIVERY_SAP2WMS="BUSINESS_SALEOUTDELIVERY_SAP2WMS";
   
    /**业务数据-Sap采购对账单标准*/
    String BUSINESS_POCHECKORDER_SAP2WMS="BUSINESS_POCHECKORDER_SAP2WMS";
   
    /**业务数据-Sap采购对账单寄售*/
    String BUSINESS_POJSCHECKORDER_SAP2WMS="BUSINESS_POJSCHECKORDER_SAP2WMS";
    
    /**业务数据-Sap生产订单入库*/
    String BUSINESS_PRODUCTORDER_IN="BUSINESS_PRODUCTORDER_IN";
	 /*********************************************WMS给SAP的*****************************/
    /**业务数据-采购订单*/
    String BUSINESS_PO_WMS2SAP="BUSINESS_PO_WMS2SAP";
    
    /**校验订单信息*/
	String CHECKORDERINFO ="CHECKORDERINFO";
}
