package com.vtradex.wms.server.model.entity.base;

public interface Wms2SapInterfaceLogType {

	/**内向交货单*/
	String DELIVERY_DOC="DELIVERY_DOC";
	
	/**供应商业务反馈接口*/
	String SURPPLIERBUSINESSBACK="SURPPLIERBUSINESSBACK";
	
	/**库存日结反馈接口*/
	String DAYLYINVENTORY="DAYLYINVENTORY";
	
	/**物料属性反馈接口*/
	String ITEMPROPERTY="ITEMPROPERTY";
	
	/** 收货信息回传SAP*/
	String RECEIVEINFO="RECEIVEINFO";
	
	/**取消收货回传SAP*/
	String CANCELRECEIVEINFO="CANCELRECEIVEINFO";
	
	/**拣货确认回传SAP*/
	String PICKCONFIRMINFO ="PICKCONFIRMINFO";
	
	/**质检转合格回传SAP*/
	String QCRECORDINFO="QCRECORDINFO";
	
	/**生产订单发料回传SAP*/
	String PRODUCTIONINFO="PRODUCTIONINFO";
	
	/**生产订单退料回传SAP*/
	String PRDRETURNINFO="PRDRETURNINFO";
	
	/**预留发料回传SAP*/
	String RESINFO="RESINFO";
	
	/**其它出库单回传SAP*/
	String OTHEROUTINFO="OTHEROUTINFO";
	
	/**其它入库单回传SAP*/
	String OTHERININFO="OTHERININFO";
	
	/**报废出入库回传SAP*/
	String BFOUTORININFO="BFOUTORININFO";
	
	/**库内调拨--一个仓库两个工厂之间*/
	String KNDBINFO="KNDBINFO";
	
	/**调拨出库--两个仓库之间*/
	String DBCKINFO="DBCKINFO";
	
	/**销售交货单回传SAP*/
	String XSJHDINFO="XSJHDINFO";
	
	/**工单拣配状态告知SAP*/
	String CALLSAPPRODSTATUS = "CALLSAPPRODSTATUS";
}
