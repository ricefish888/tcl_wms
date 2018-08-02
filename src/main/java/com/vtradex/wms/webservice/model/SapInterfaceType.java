package com.vtradex.wms.webservice.model;

public interface SapInterfaceType {
	
	
	String SAP_COMMONCALLBACK_ITYPE_ITEM="MAT"; //物料
	String SAP_COMMONCALLBACK_ITYPE_SUPPLIER="VEN"; //供应商
	String SAP_COMMONCALLBACK_ITYPE_WAREHOUSE="WAR"; //仓库
	
	String SAP_COMMONCALLBACK_ITEYP_PO="PO";// PO采购订单
	String SAP_COMMONCALLBACK_ITEYP_PUO="IND";// PO内向交货单
	String SAP_COMMONCALLBACK_ITEYP_PRO="PRD";// 生产订单
	String SAP_COMMONCALLBACK_ITEYP_REDATA="RES";// 预留单
	String SAP_COMMONCALLBACK_ITEYP_SOD="OUD";// 外向交货单
	
	String SAP_COMMONCALLBACK_ITEYP_CHECKORDER = "CKD";//sap标准对账单
	String SAP_COMMONCALLBACK_ITEYP_JSCHECKORDER = "CKD";//sap寄售对账单
	String SAP_COMMONCALLBACK_ITEYP_PROIN = "PCV";//生产订单入库单
	
	String SAP_COMMONCALLBACK_ITEYP_COST = "CSK";// 成本中心
	
	String SAP_COMMONCALLBACK_ITEYP_LEDGER = "UDO";//库存台账
	
	//单号回传对应类型
	String SAP_COMMONCALLBACK_ITEYP_DOCODE="IND";// SAP采购交货单单号回传
	/**采购入库*/
	String SAP_CODE_RETURN_PO = "GR";
	/**质检*/
	String SAP_CODE_RETURN_ZJ = "ISP";
	/**采购入库取消*/
	String SAP_CODE_RETURN_CANCELIN = "UDO";
	/**采购退货*/
	String SAP_CODE_RETURN_CGTH = "PCV";
	/**预留发料*/
	String SAP_CODE_RETURN_YL = "IS";
	/**库存调拨*/
	String SAP_CODE_RETURN_DB = "STS";
	/**报废出库*/
	String SAP_CODE_RETURN_BF = "RIS";
	/**其它出入库*/
	String SAP_CODE_RETURN_OT = "OTH";
	/**生产发料*/
	String SAP_CODE_RETURN_PRO ="OIS";
	/**销售交货单出库*/
	String SAP_CODE_RETURN_OUTDO = "ODP";
	
	/**工单拣配标志*/
	String SAP_CODE_PROD_BECREATPT ="JPD";
}
