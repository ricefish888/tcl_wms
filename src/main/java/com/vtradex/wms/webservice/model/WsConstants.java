package com.vtradex.wms.webservice.model;

/**
 * WebService常量定义.

 */
public interface WsConstants {
	
	String CHARSET = ";charset=UTF-8";
	
	/**WMS搭建SAP接口服务的命名空间*/
	String NS_SAP_BASIC = "http://sap.server.edi.vtradex.com/";
	
	/**访问sap服务的用户名 测试*/
//	String SAP_WS_USERNAME="WMSDATA";
	
	/**访问sap服务的密码 测试*/
//	String SAP_WS_PASSWORD="wms2sap";
	
	/**访问sap服务映射的域名 测式*/
//	String SAP_WS_YM = "pidev01.tcl.com";
	
	/**访问sap服务映射的域名 正式*/
	String SAP_WS_YM = "piprd01.tcl.com";
	
	/**访问sap服务的用户名 正式*/
	String SAP_WS_USERNAME="WMS-PI";
	
	/**访问sap服务的密码 正式*/
	String SAP_WS_PASSWORD="wms2sap";
	
	/**sap公共回传接口的服务地址，此地址从wsdl中手工提取出来的
I_TYP:物料 MAT 供应商:VEN 仓库:WAR
MSG_ID：SAP给你的MESSAGE NO
LINE_NO:SAP给你的行号
FLG: S：成功 E:失败
MSG_TXT:消息内容
	 * 
	 * */
	String SAP_WS_ADDRESS_COMMONCALLBACK="http://"+SAP_WS_YM+":50000/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_WMS_SENDER&receiverParty=&receiverService=&interface=ZRFC_WMS_MSG_RETURN&interfaceNamespace=urn%3Asap-com%3Adocument%3Asap%3Arfc%3Afunctions";
		
	/***
	 * SAP交货单接口 此地址从wsdl中手工提取出来的
	 */
	String SAP_WS_ADDRESS_DELIVERY="http://"+SAP_WS_YM+":50000/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_WMS_SENDER&receiverParty=&receiverService=&interface=SI_WMS_INBOUND_DLV_OB&interfaceNamespace=http%3A%2F%2Fwms.tcl.com%2Finbound_delivery";

	/**
	 * WMS库存传sap
	 */
	String SAP_WS_ADDRESS_INVENTORY="http://"+SAP_WS_YM+":50000/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_WMS_SENDER&receiverParty=&receiverService=&interface=SI_Wms_Stock_OB&interfaceNamespace=http%3A%2F%2Fwms.tcl.com%2Fwms_stock";
	
	/**
	 * wms物料属性回传sap
	 */
	String SAP_WS_ADDRESS_ITEM="http://"+SAP_WS_YM+":50000/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_WMS_SENDER&receiverParty=&receiverService=&interface=SI_Wms_Mat_Attr_OB&interfaceNamespace=http%3A%2F%2Fwms.tcl.com%2Fmaterial_attr";
	
	/**
	 * wms供应商确认回传sap
	 */
	String SAP_WS_ADDRESS_SUPPLIER="http://"+SAP_WS_YM+":50000/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_WMS_SENDER&receiverParty=&receiverService=&interface=SI_WMS_Confirm_OB&interfaceNamespace=http%3A%2F%2Fwms.tcl.com%2Fvendor_confirm";
	
	/**
	 * 取消收货回传SAP
	 */
	String SAP_WS_ADDRESS_CANCELRECEIVE="http://"+SAP_WS_YM+":50000/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_WMS_SENDER&receiverParty=&receiverService=&interface=SI_Wms_Receipt_Undo_OB&interfaceNamespace=https%3A%2F%2Fwms.tcl.com%2FReceipt_Undo";
	
	/**
	 * 采购退货出库回传SAP
	 */
	String SAP_WS_ADDRESS_RETURN="http://"+SAP_WS_YM+":50000/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_WMS_SENDER&receiverParty=&receiverService=&interface=SI_Wms_Receipt_Return_OB&interfaceNamespace=http%3A%2F%2Fwms.tcl.com%2FReceipt_Return";
	
	/**
	 * 质检转合格回传SAP
	 */
	String SAP_WS_ADDRESS_QUALITY="http://"+SAP_WS_YM+":50000/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_WMS_SENDER&receiverParty=&receiverService=&interface=SI_Wms_Inspect_OB&interfaceNamespace=http%3A%2F%2Fwms.tcl.com%2FInspect_Order";
	
	/**
	 * 采购收货回传sap
	 */
	String SAP_WS_ADDRESS_RECEIVE="http://"+SAP_WS_YM+":50000/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_WMS_SENDER&receiverParty=&receiverService=&interface=SI_Wms_Ind_Post_OB&interfaceNamespace=http%3A%2F%2Fwms.tcl.com%2Findelivery_Post";
	
	/**
	 * 预留发料回传SAP
	 */
	String SAP_WS_ADDRESS_RES="http://"+SAP_WS_YM+":50000/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_WMS_SENDER&receiverParty=&receiverService=&interface=SI_Wms_Res_Issue_OB&interfaceNamespace=http%3A%2F%2Fwms.tcl.com%2FRes_Issue";
	
	/**
	 * 生产发料、退料回传SAP
	 */
	String SAP_WS_ADDRESS_PRD="http://"+SAP_WS_YM+":50000/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_WMS_SENDER&receiverParty=&receiverService=&interface=SI_Wms_Prd_Move_OB&interfaceNamespace=https%3A%2F%2Fwms.tcl.com%2Fprd_move";
	
	/**
	 * 外向交货单回传SAP
	 */
	String SAP_WS_ADDRESS_OUTDELIVERY ="http://"+SAP_WS_YM+":50000/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_WMS_SENDER&receiverParty=&receiverService=&interface=SI_Outdelivery_Post_OB&interfaceNamespace=http%3A%2F%2Fwms.tcl.com%2FOutbound_Post";
	
	/**
	 * 报废出库回传SAP
	 */
	String SAP_WS_ADDRESS_BFCK ="http://"+SAP_WS_YM+":50000/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_WMS_SENDER&receiverParty=&receiverService=&interface=SI_Wms_Scrap_Out_OB&interfaceNamespace=http%3A%2F%2Fwms.tcl.com%2FScrap_Out";
	
	/**
	 * 其他发料回传SAP
	 */
	String SAP_WS_ADDRESS_OTHER="http://"+SAP_WS_YM+":50000/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_WMS_SENDER&receiverParty=&receiverService=&interface=SI_Wms_Other_Move_OB&interfaceNamespace=http%3A%2F%2Fwms.tcl.com%2Fother_move";
	
	/**
	 * 调拨回传SAP
	 */
	String SAP_WS_ADDRESS_DBCK="http://"+SAP_WS_YM+":50000/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_WMS_SENDER&receiverParty=&receiverService=&interface=SI_Wms_Stock_Move_OB&interfaceNamespace=https%3A%2F%2Fwms.tcl.com%2Fstock_move";
	
	//	public static final String NS_ERP_BUSINESS = "http://api.anli.business.erp.vtradex.com";
//	public static final String NS_GENCODE = "http://api.anli.gencode.vtradex.com";
	
//
//	
//	//基础数据接口路径
//	public static final String VtradexBasicService_endpointInterface = "com.vtradex.webservice.server.erp.VtradexBasicService";
//	String basic_serviceName = "VtradexBasicService";
//	String basic_portName = "VtradexBasicServicePort";
//	
//	//业务数据接口路径
//	String VtradexBusinessService_endpointInterface = "com.vtradex.webservice.server.erp.VtradexBusinessService";
//	String business_serviceName = "VtradexBusinessService";
//	String business_portName = "VtradexBusinessServicePort";
//	
//	//置码系统接口路径
//	String VtradexGenCodeService_endpointInterface = "com.vtradex.webservice.server.gencode.VtradexGenCodeService";
//	String gen_code_serviceName = "VtradexGenCodeService";
//	String gen_code_portName = "VtradexGenCodeServicePort";
//	
	
	
}
