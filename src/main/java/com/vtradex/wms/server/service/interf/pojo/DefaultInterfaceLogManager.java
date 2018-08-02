package com.vtradex.wms.server.service.interf.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.interfaceLog.InterfaceLog;
import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.model.message.TaskStatus;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.DateUtil;
import com.vtradex.wms.server.enumeration.WmsSapFactoryCodeEnum;
import com.vtradex.wms.server.model.entity.base.Wms2SapInterfaceLog;
import com.vtradex.wms.server.model.entity.base.Wms2SapInterfaceLogType;
import com.vtradex.wms.server.model.entity.base.WmsItemFactory;
import com.vtradex.wms.server.model.entity.base.WmsSapFactory;
import com.vtradex.wms.server.model.entity.base.WmsSystemValues;
import com.vtradex.wms.server.model.entity.base.WmsSystemValuesType;
import com.vtradex.wms.server.model.entity.email.EmailRecord;
import com.vtradex.wms.server.model.entity.email.EmailRecordType;
import com.vtradex.wms.server.model.entity.inventory.TclWmsInventoryLedger;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrder;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetailPtDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrderStatus;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrder;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderDetail;
import com.vtradex.wms.server.model.enums.WmsPickTicketStatus;
import com.vtradex.wms.server.service.emailrecord.EmailRecordManager;
import com.vtradex.wms.server.service.interf.InterfaceLogManager;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogFunction;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogStatus;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogTaskType;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogType;
import com.vtradex.wms.server.service.sap.SapDataDealManager;
import com.vtradex.wms.server.utils.DateUtils;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.webservice.client.sap.commcallback.ZRFCWMSMSGRETURNResponse;
import com.vtradex.wms.webservice.client.sap.delivery.DTRetMsg;
import com.vtradex.wms.webservice.model.InterfaceLogSys;
import com.vtradex.wms.webservice.model.TaskSubscriber;
import com.vtradex.wms.webservice.sap.base.SapCommonCallback;
import com.vtradex.wms.webservice.sap.model.SapCheckOrderInfo;
import com.vtradex.wms.webservice.sap.model.SapCheckOrderInfoArray;
import com.vtradex.wms.webservice.sap.model.SapPodEditType;
import com.vtradex.wms.webservice.sap.model.Wms2SapDeliveryOrder;
import com.vtradex.wms.webservice.sap.model.Wms2SapEInventoryArray;
import com.vtradex.wms.webservice.sap.model.Wms2SapInventoryLedgerArray;
import com.vtradex.wms.webservice.sap.model.Wms2SapItemAttr;
import com.vtradex.wms.webservice.sap.model.Wms2SapSupplierDocStatus;
import com.vtradex.wms.webservice.sap.model.Wms2SapSupplierDocType;
import com.vtradex.wms.webservice.sap.model.Wms2SapSupplierStatus;
import com.vtradex.wms.webservice.utils.CallSapWebService;
import com.vtradex.wms.webservice.utils.CommonHelper;
import com.vtradex.wms.webservice.utils.XMLHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;

public class DefaultInterfaceLogManager extends DefaultBaseManager implements InterfaceLogManager {
	
	protected SapDataDealManager sapDataDealManager;
	
	protected EmailRecordManager emailRecordManager;
	
	public DefaultInterfaceLogManager(SapDataDealManager sapDataDealManager,EmailRecordManager emailRecordManager) {
		this.sapDataDealManager = sapDataDealManager;
		this.emailRecordManager = emailRecordManager;
	}

	public InterfaceLog getInterfaceLog(Long id) {
		return commonDao.load(InterfaceLog.class, id);
	}

	public  void dealInterfaceLog(Long interfaceLogId) {
		
		InterfaceLog log = commonDao.load(InterfaceLog.class, interfaceLogId);
		if(!InterfaceLogFunction.ASYNC.equals(log.getFunction())) { //异步模式
			throw new BusinessException("非异步模式不能用异步方式执行!");
		}

		if(InterfaceLogType.BASIC_ITEM_SAP2WMS.equals(log.getType()) ) { //物料
			sapDataDealManager.dealSapItem(log);
			//WMS处理完成后  需要将处理结果返回给SAP
		}
		else if(InterfaceLogType.BASIC_SUPPLIER_SAP2WMS.equals(log.getType())) { //供应商
			//执行。
			sapDataDealManager.dealSapSupplier(log);
		    //WMS处理完成后  需要将处理结果返回给SAP
		}
		else if(InterfaceLogType.BASIC_WAREHOUSE_SAP2WMS.equals(log.getType())) { //SAP仓库
		    sapDataDealManager.dealSapWarehouse(log);
			//WMS处理完成后  需要将处理结果返回给SAP
		}
		else if(InterfaceLogType.BUSINESS_PO_SAP2WMS.equals(log.getType())) {//采购订单PO
			//执行。
			sapDataDealManager.dealSapPoOrder(log);
			//WMS处理完成后  需要将处理结果返回给SAP
			
		}
		else if(InterfaceLogType.BUSINESS_DELIVERYORDER_SAP2WMS.equals(log.getType())) {//采购交货单DeliveryOrder
			//执行。
			sapDataDealManager.dealSapDeliveryOrder(log);
			//WMS处理完成后  需要将处理结果返回给SAP
			
		}
		else if(InterfaceLogType.BUSINESS_ORDERCODE_SAP2WMS.equals(log.getType())) {//SAP根据WMS回传的交货单信息创建他们自己的交货单并把SAP交货单单号回传给WMS
			//执行。
			sapDataDealManager.dealSapDeliveryCode(log);
			//WMS处理完成后  需要将处理结果返回给SAP
		}
		else if(InterfaceLogType.BUSINESS_PRODUCTORDER_SAP2WMS.equals(log.getType())) {//生产订单ProductOrder
			//执行。
			sapDataDealManager.dealSapProductOrder(log);
			//WMS处理完成后  需要将处理结果返回给SAP
		}
		else if(InterfaceLogType.BUSINESS_RESERVEDDATA_SAP2WMS.equals(log.getType())) {//预留主数据
			//执行。
			sapDataDealManager.dealSapReservedData(log);
			//WMS处理完成后  需要将处理结果返回给SAP
		}
		else if(InterfaceLogType.BUSINESS_SALEOUTDELIVERY_SAP2WMS.equals(log.getType())) {//销售外向交货单
			//执行。
			sapDataDealManager.dealSapSaleOutDelivery(log);
			//WMS处理完成后  需要将处理结果返回给SAP
		}
		else if(InterfaceLogType.BUSINESS_POCHECKORDER_SAP2WMS.equals(log.getType())) {//标准对账单
			//执行。
			sapDataDealManager.dealSapCheckOrder(log);
			//WMS处理完成后  需要将处理结果返回给SAP
		}
		else if(InterfaceLogType.BUSINESS_POJSCHECKORDER_SAP2WMS.equals(log.getType())) {//寄售对账单
			//执行。
			sapDataDealManager.dealSapJSCheckOrder(log);
			//WMS处理完成后  需要将处理结果返回给SAP
		}
		else if(InterfaceLogType.BUSINESS_PRODUCTORDER_IN.equals(log.getType())) {//生产订单入库接口
			//执行。
			sapDataDealManager.dealSapProductOrderIn(log);
			//WMS处理完成后  需要将处理结果返回给SAP
		}
		else if(InterfaceLogType.BASIC_COSTCENTER_SAP2WMS.equals(log.getType())){// 成本中心主数据接口
			sapDataDealManager.dealSapCostCenter(log);
		}
	}
	 
    /**将wms的处理结果从interface的responseContent字段通过接口传到sap*/
	@SuppressWarnings("unused")
	public void sendResponseToSap(Long interfaceLogId) {
		InterfaceLog log = commonDao.load(InterfaceLog.class, interfaceLogId);
		String xml = log.getResponseContent();
		SapCommonCallback sapCommonCallback = (SapCommonCallback)XmlObjectConver.fromXML(new SapCommonCallback(), xml);
		ZRFCWMSMSGRETURNResponse response = null;
		String callErrorMsg = "";
		try {
			response = CallSapWebService.callCommCallBack(sapCommonCallback);
			updateInterfaceLogDeailInfo(log);
			if(StringHelper.isNullOrEmpty(log.getErrorLog())){
				log.setStatus(InterfaceLogStatus.STAT_FINISH);
				log.setErrorLog("");
			}else{
				log.setStatus(InterfaceLogStatus.STAT_FAIL);
			}
			sapDataDealManager.store(log);
		}
		catch(Exception e) {
			e.printStackTrace();
			updateInterfaceLogDeailInfo(log);
			log.setStatus(InterfaceLogStatus.STAT_FAIL);
			callErrorMsg=CommonHelper.getErrorMessageByException(e);
			callErrorMsg = StringHelper.substring(callErrorMsg, 100);
			log.setErrorLog(callErrorMsg);
			sapDataDealManager.store(log);
		}

		if(log.getStatus().equals(InterfaceLogStatus.STAT_FAIL)) {
			throw new BusinessException(callErrorMsg);//如果回调失败  则抛出异常，让thorn_task也失败。
		}
//		return response;
	}
	
	
	
	/**wms异步传输单据给sap
	 * 此方法不能加事务
	 * */
	@SuppressWarnings("unused")
	public void sendWms2SapInterfaceLog(Long wms2SapInterfaceLog) {
		Wms2SapInterfaceLog log = commonDao.load(Wms2SapInterfaceLog.class, wms2SapInterfaceLog);
		String xml = log.getRequestContent();
		String type = log.getType();
		String callErrorMsg = "";
		if(Wms2SapInterfaceLogType.DELIVERY_DOC.equals(type)) { //wms生成交货单回传
			
			Wms2SapDeliveryOrder wms2SapDeliveryOrder =   (Wms2SapDeliveryOrder)XmlObjectConver.fromXML(new Wms2SapDeliveryOrder(), xml);
			
			DTRetMsg response = null;
			
			try {
				response = CallSapWebService.callSapDelivery(wms2SapDeliveryOrder);
				
				log.setSendStatus(InterfaceLogStatus.STAT_FINISH);
				log.setReceiveTime(new Date());
				log.setRequestCnt(log.getRequestCnt()==null ? 1 : log.getRequestCnt()+1);
				if("S".equals(response.getRET())){
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FINISH);
					log.setErrorLog("");
				}else{
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
					log.setErrorLog(response.getMSG());
				}
				sapDataDealManager.store(log);
			}
			catch(Exception e) {
				e.printStackTrace();
				log.setSendStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveTime(new Date());
				callErrorMsg=CommonHelper.getErrorMessageByException(e);
				callErrorMsg = StringHelper.substring(callErrorMsg, 100);
				log.setErrorLog(callErrorMsg);
				sapDataDealManager.store(log);
			}
		}else if(Wms2SapInterfaceLogType.SURPPLIERBUSINESSBACK.equals(type)){//供应商反馈
			Wms2SapSupplierDocStatus wsds = (Wms2SapSupplierDocStatus)XmlObjectConver.fromXML(new Wms2SapSupplierDocStatus(),xml);
				
			com.vtradex.wms.webservice.client.sap.supplier.DTRetMsg response = null;
			try {
				response = CallSapWebService.callSapSupplier(wsds);
				log.setSendStatus(InterfaceLogStatus.STAT_FINISH);
				log.setReceiveTime(new Date());
				log.setRequestCnt(log.getRequestCnt()==null ? 1 : log.getRequestCnt()+1);
				if("S".equals(response.getRET())){
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FINISH);
					log.setErrorLog("");
					//实时接口一并修改处理状态和时间
					log.setDealStatus(InterfaceLogStatus.STAT_FINISH);
					log.setResponseTime(new Date());
				}else{
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
					log.setErrorLog(response.getMSG());
					//实时接口一并修改处理状态和时间
					log.setDealStatus(InterfaceLogStatus.STAT_FAIL);
					log.setResponseTime(new Date());
				}
				sapDataDealManager.store(log);
			}
			catch(Exception e) {
				e.printStackTrace();
				log.setSendStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveTime(new Date());
				//实时接口一并修改处理状态和时间
				log.setDealStatus(InterfaceLogStatus.STAT_FAIL);
				log.setResponseTime(new Date());
				
				callErrorMsg=CommonHelper.getErrorMessageByException(e);
				callErrorMsg = StringHelper.substring(callErrorMsg, 100);
				log.setErrorLog(callErrorMsg);
				sapDataDealManager.store(log);
			}
		}
		else if(Wms2SapInterfaceLogType.ITEMPROPERTY.equals(type)){//物料属性反馈
			Wms2SapItemAttr wia = (Wms2SapItemAttr) XmlObjectConver.fromXML(new Wms2SapItemAttr(),xml);
			
			com.vtradex.wms.webservice.client.sap.item.DTRetMsg response = null;
			
			try {
				response = CallSapWebService.callSapItem(wia);
				log.setSendStatus(InterfaceLogStatus.STAT_FINISH);
				log.setRequestCnt(log.getRequestCnt()==null ? 1 : log.getRequestCnt()+1);
				log.setReceiveTime(new Date());
				if("S".equals(response.getRET())){
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FINISH);
					log.setErrorLog("");
					//实时接口一并修改处理状态和时间
					log.setDealStatus(InterfaceLogStatus.STAT_FINISH);
					log.setResponseTime(new Date());
					
				}else{
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
					log.setErrorLog(response.getMSG());
					//实时接口一并修改处理状态和时间
					log.setDealStatus(InterfaceLogStatus.STAT_FAIL);
					log.setResponseTime(new Date());
				}
				sapDataDealManager.store(log);
			}
			catch(Exception e) {
				e.printStackTrace();
				log.setSendStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveTime(new Date());
				//实时接口一并修改处理状态和时间
				log.setDealStatus(InterfaceLogStatus.STAT_FAIL);
				log.setResponseTime(new Date());
				
				callErrorMsg=CommonHelper.getErrorMessageByException(e);
				callErrorMsg = StringHelper.substring(callErrorMsg, 100);
				log.setErrorLog(callErrorMsg);
				sapDataDealManager.store(log);
			}
		}
		else if(Wms2SapInterfaceLogType.DAYLYINVENTORY.equals(type)){//库存日结
			Wms2SapEInventoryArray wsias = (Wms2SapEInventoryArray) XmlObjectConver.fromXML(new Wms2SapEInventoryArray(), xml);
			com.vtradex.wms.webservice.client.sap.inv.DTRetMsg response = null;
			try{
				response = CallSapWebService.callSapInventory(wsias);
				log.setSendStatus(InterfaceLogStatus.STAT_FINISH);
				log.setReceiveTime(new Date());
				log.setRequestCnt(log.getRequestCnt()==null ? 1 : log.getRequestCnt()+1);
				if("S".equals(response.getRET())){
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FINISH);
					log.setErrorLog("");
					//实时接口一并修改处理状态和时间
					log.setDealStatus(InterfaceLogStatus.STAT_FINISH);
					log.setResponseTime(new Date());
				}else{
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
					log.setErrorLog(response.getMSG());
					//实时接口一并修改处理状态和时间
					log.setDealStatus(InterfaceLogStatus.STAT_FAIL);
					log.setResponseTime(new Date());
				}
				sapDataDealManager.store(log);
			}catch(Exception e) {
				e.printStackTrace();
				log.setSendStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveTime(new Date());
				//实时接口一并修改处理状态和时间
				log.setDealStatus(InterfaceLogStatus.STAT_FAIL);
				log.setResponseTime(new Date());
				
				callErrorMsg=CommonHelper.getErrorMessageByException(e);
				callErrorMsg = StringHelper.substring(callErrorMsg, 100);
				log.setErrorLog(callErrorMsg);
				sapDataDealManager.store(log);
			}
		}
		else if(Wms2SapInterfaceLogType.CANCELRECEIVEINFO.equals(type)){//取消收货
			Wms2SapInventoryLedgerArray ledgers = (Wms2SapInventoryLedgerArray) XmlObjectConver.fromXML(new Wms2SapInventoryLedgerArray(), xml);
			com.vtradex.wms.webservice.client.sap.cancelreceive.DTRetMsg response = null;
			try{
				response = CallSapWebService.callSapCancelReceive(ledgers);
				log.setSendStatus(InterfaceLogStatus.STAT_FINISH);
				log.setReceiveTime(new Date());
				log.setRequestCnt(log.getRequestCnt()==null ? 1 : log.getRequestCnt()+1);
				if("S".equals(response.getRET())){
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FINISH);
					log.setErrorLog("");
				}else{
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
					log.setErrorLog(response.getMSG());
				}
				sapDataDealManager.store(log);
			}catch(Exception e){
				e.printStackTrace();
				log.setSendStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveTime(new Date());
				callErrorMsg=CommonHelper.getErrorMessageByException(e);
				callErrorMsg = StringHelper.substring(callErrorMsg, 100);
				log.setErrorLog(callErrorMsg);
				sapDataDealManager.store(log);
			}
		}
		else if(Wms2SapInterfaceLogType.PICKCONFIRMINFO.equals(type)){ //采购退货
			Wms2SapInventoryLedgerArray ledgers = (Wms2SapInventoryLedgerArray) XmlObjectConver.fromXML(new Wms2SapInventoryLedgerArray(), xml);
			com.vtradex.wms.webservice.client.sap.returnback.DTRetMsg response = null;
			try{
				response = CallSapWebService.callSapReturn(ledgers);
				log.setSendStatus(InterfaceLogStatus.STAT_FINISH);
				log.setReceiveTime(new Date());
				log.setRequestCnt(log.getRequestCnt()==null ? 1 : log.getRequestCnt()+1);
				if("S".equals(response.getRET())){
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FINISH);
					log.setErrorLog("");
				}else{
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
					log.setErrorLog(response.getMSG());
				}
				sapDataDealManager.store(log);
			}catch(Exception e){
				e.printStackTrace();
				log.setSendStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveTime(new Date());
				callErrorMsg=CommonHelper.getErrorMessageByException(e);
				callErrorMsg = StringHelper.substring(callErrorMsg, 100);
				log.setErrorLog(callErrorMsg);
				sapDataDealManager.store(log);
			}
		}
		else if(Wms2SapInterfaceLogType.QCRECORDINFO.equals(type)){//质检
			Wms2SapInventoryLedgerArray ledgers = (Wms2SapInventoryLedgerArray) XmlObjectConver.fromXML(new Wms2SapInventoryLedgerArray(), xml);
			com.vtradex.wms.webservice.client.sap.quality.DTRetMsg response = null;
			try{
				response = CallSapWebService.callSapQuality(ledgers);
				log.setSendStatus(InterfaceLogStatus.STAT_FINISH);
				log.setReceiveTime(new Date());
				log.setRequestCnt(log.getRequestCnt()==null ? 1 : log.getRequestCnt()+1);
				if("S".equals(response.getRET())){
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FINISH);
					log.setErrorLog("");
				}else{
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
					log.setErrorLog(response.getMSG());
				}
				sapDataDealManager.store(log);
			}catch(Exception e){
				e.printStackTrace();
				log.setSendStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveTime(new Date());
				callErrorMsg=CommonHelper.getErrorMessageByException(e);
				callErrorMsg = StringHelper.substring(callErrorMsg, 100);
				log.setErrorLog(callErrorMsg);
				sapDataDealManager.store(log);
			}
		}
		else if(Wms2SapInterfaceLogType.RECEIVEINFO.equals(type)){//采购收货
			Wms2SapInventoryLedgerArray ledgers = (Wms2SapInventoryLedgerArray) XmlObjectConver.fromXML(new Wms2SapInventoryLedgerArray(), xml);
			com.vtradex.wms.webservice.client.sap.receiveinfo.DTRetMsg response = null;
			try{
				response = CallSapWebService.callSapReceive(ledgers);
				log.setSendStatus(InterfaceLogStatus.STAT_FINISH);
				log.setReceiveTime(new Date());
				log.setRequestCnt(log.getRequestCnt()==null ? 1 : log.getRequestCnt()+1);
				if("S".equals(response.getRET())){
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FINISH);
					log.setErrorLog("");
				}else{
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
					log.setErrorLog(response.getMSG());
				}
				sapDataDealManager.store(log);
			}catch(Exception e){
				e.printStackTrace();
				log.setSendStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveTime(new Date());
				callErrorMsg=CommonHelper.getErrorMessageByException(e);
				callErrorMsg = StringHelper.substring(callErrorMsg, 100);
				log.setErrorLog(callErrorMsg);
				sapDataDealManager.store(log);
			}
		}
		else if(Wms2SapInterfaceLogType.RESINFO.equals(type)){//预留发料
			Wms2SapInventoryLedgerArray ledgers = (Wms2SapInventoryLedgerArray) XmlObjectConver.fromXML(new Wms2SapInventoryLedgerArray(), xml);
			com.vtradex.wms.webservice.client.sap.resdata.DTRetMsg response = null;
			try{
				response = CallSapWebService.callSapResData(ledgers);
				log.setSendStatus(InterfaceLogStatus.STAT_FINISH);
				log.setReceiveTime(new Date());
				log.setRequestCnt(log.getRequestCnt()==null ? 1 : log.getRequestCnt()+1);
				if("S".equals(response.getRET())){
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FINISH);
					log.setErrorLog("");
				}else{
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
					log.setErrorLog(response.getMSG());
				}
				sapDataDealManager.store(log);
			}catch(Exception e){
				e.printStackTrace();
				log.setSendStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveTime(new Date());
				callErrorMsg=CommonHelper.getErrorMessageByException(e);
				callErrorMsg = StringHelper.substring(callErrorMsg, 100);
				log.setErrorLog(callErrorMsg);
				sapDataDealManager.store(log);
			}
		}
		else if(Wms2SapInterfaceLogType.PRODUCTIONINFO.equals(type) || Wms2SapInterfaceLogType.PRDRETURNINFO.equals(type)){//生产发料、退料
			Wms2SapInventoryLedgerArray ledgers = (Wms2SapInventoryLedgerArray) XmlObjectConver.fromXML(new Wms2SapInventoryLedgerArray(), xml);
			com.vtradex.wms.webservice.client.sap.prdmove.DTRetMsg response = null;
			try{
				response = CallSapWebService.callSapPrdMove(ledgers);
				log.setSendStatus(InterfaceLogStatus.STAT_FINISH);
				log.setReceiveTime(new Date());
				log.setRequestCnt(log.getRequestCnt()==null ? 1 : log.getRequestCnt()+1);
				if("S".equals(response.getRET())){
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FINISH);
					log.setErrorLog("");
				}else{
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
					log.setErrorLog(response.getMSG());
				}
				sapDataDealManager.store(log);
			}catch(Exception e){
				e.printStackTrace();
				log.setSendStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveTime(new Date());
				callErrorMsg=CommonHelper.getErrorMessageByException(e);
				callErrorMsg = StringHelper.substring(callErrorMsg, 100);
				log.setErrorLog(callErrorMsg);
				sapDataDealManager.store(log);
			}
		}
		else if(Wms2SapInterfaceLogType.OTHEROUTINFO.equals(type) || Wms2SapInterfaceLogType.OTHERININFO.equals(type) ){//其它出入库
			Wms2SapInventoryLedgerArray ledgers = (Wms2SapInventoryLedgerArray) XmlObjectConver.fromXML(new Wms2SapInventoryLedgerArray(), xml);
			com.vtradex.wms.webservice.client.sap.othership.DTRetMsg response = null;
			try{
				response = CallSapWebService.callSapOtherMove(ledgers);
				log.setSendStatus(InterfaceLogStatus.STAT_FINISH);
				log.setReceiveTime(new Date());
				log.setRequestCnt(log.getRequestCnt()==null ? 1 : log.getRequestCnt()+1);
				if("S".equals(response.getRET())){
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FINISH);
					log.setErrorLog("");
				}else{
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
					log.setErrorLog(response.getMSG());
				}
				sapDataDealManager.store(log);
			}catch(Exception e){
				e.printStackTrace();
				log.setSendStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveTime(new Date());
				callErrorMsg=CommonHelper.getErrorMessageByException(e);
				callErrorMsg = StringHelper.substring(callErrorMsg, 100);
				log.setErrorLog(callErrorMsg);
				sapDataDealManager.store(log);
			}
		}
		else if(Wms2SapInterfaceLogType.BFOUTORININFO.equals(type)){//报废出入库
			Wms2SapInventoryLedgerArray ledgers = (Wms2SapInventoryLedgerArray) XmlObjectConver.fromXML(new Wms2SapInventoryLedgerArray(), xml);
			com.vtradex.wms.webservice.client.sap.bfck.DTRetMsg response = null;
			try{
				response = CallSapWebService.callSapBFCK(ledgers);
				log.setSendStatus(InterfaceLogStatus.STAT_FINISH);
				log.setReceiveTime(new Date());
				log.setRequestCnt(log.getRequestCnt()==null ? 1 : log.getRequestCnt()+1);
				if("S".equals(response.getRET())){
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FINISH);
					log.setErrorLog("");
				}else{
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
					log.setErrorLog(response.getMSG());
				}
				sapDataDealManager.store(log);
			}catch(Exception e){
				e.printStackTrace();
				log.setSendStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveTime(new Date());
				callErrorMsg=CommonHelper.getErrorMessageByException(e);
				callErrorMsg = StringHelper.substring(callErrorMsg, 100);
				log.setErrorLog(callErrorMsg);
				sapDataDealManager.store(log);
			}
		}
		else if(Wms2SapInterfaceLogType.KNDBINFO.equals(type) || Wms2SapInterfaceLogType.DBCKINFO.equals(type)){//调拨出库
			Wms2SapInventoryLedgerArray ledgers = (Wms2SapInventoryLedgerArray) XmlObjectConver.fromXML(new Wms2SapInventoryLedgerArray(), xml);
			com.vtradex.wms.webservice.client.sap.invmove.DTRetMsg response = null;
			try{
				response = CallSapWebService.callSapInvMove(ledgers);
				log.setSendStatus(InterfaceLogStatus.STAT_FINISH);
				log.setReceiveTime(new Date());
				log.setRequestCnt(log.getRequestCnt()==null ? 1 : log.getRequestCnt()+1);
				if("S".equals(response.getRET())){
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FINISH);
					log.setErrorLog("");
				}else{
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
					log.setErrorLog(response.getMSG());
				}
				sapDataDealManager.store(log);
			}catch(Exception e){
				e.printStackTrace();
				log.setSendStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveTime(new Date());
				callErrorMsg=CommonHelper.getErrorMessageByException(e);
				callErrorMsg = StringHelper.substring(callErrorMsg, 100);
				log.setErrorLog(callErrorMsg);
				sapDataDealManager.store(log);
			}
		}
		else if(Wms2SapInterfaceLogType.XSJHDINFO.equals(type)){//销售交货单
			Wms2SapInventoryLedgerArray ledgers = (Wms2SapInventoryLedgerArray) XmlObjectConver.fromXML(new Wms2SapInventoryLedgerArray(), xml);
			com.vtradex.wms.webservice.client.sap.saleoutdelivery.DTRetMsg response = null;
			try{
				response = CallSapWebService.callSapOutDelivery(ledgers);
				log.setSendStatus(InterfaceLogStatus.STAT_FINISH);
				log.setReceiveTime(new Date());
				log.setRequestCnt(log.getRequestCnt()==null ? 1 : log.getRequestCnt()+1);
				if("S".equals(response.getRET())){
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FINISH);
					log.setErrorLog("");
				}else{
					log.setResponseContent(response.getMSG());
					log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
					log.setErrorLog(response.getMSG());
				}
				sapDataDealManager.store(log);
			}catch(Exception e){
				e.printStackTrace();
				log.setSendStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveTime(new Date());
				callErrorMsg=CommonHelper.getErrorMessageByException(e);
				callErrorMsg = StringHelper.substring(callErrorMsg, 100);
				log.setErrorLog(callErrorMsg);
				sapDataDealManager.store(log);
			}
		}else if(Wms2SapInterfaceLogType.CALLSAPPRODSTATUS.equals(type)){//工单拣配状态告知SAP
			SapCommonCallback sapCommonCallback = (SapCommonCallback)XmlObjectConver.fromXML(new SapCommonCallback(), xml);
			ZRFCWMSMSGRETURNResponse response = null;
			try {
				response = CallSapWebService.callCommCallBack(sapCommonCallback);
				log.setSendStatus(InterfaceLogStatus.STAT_FINISH);
				log.setReceiveTime(new Date());
				log.setRequestCnt(log.getRequestCnt()==null ? 1 : log.getRequestCnt()+1);
				
				log.setResponseContent("");
				log.setReceiveStatus(InterfaceLogStatus.STAT_FINISH);
				log.setErrorLog("");
				log.setDealStatus(InterfaceLogStatus.STAT_FINISH);
				log.setResponseTime(new Date());
				
				sapDataDealManager.store(log);
			}
			catch(Exception e) {
				e.printStackTrace();
				log.setSendStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveStatus(InterfaceLogStatus.STAT_FAIL);
				log.setReceiveTime(new Date());
				callErrorMsg=CommonHelper.getErrorMessageByException(e);
				callErrorMsg = StringHelper.substring(callErrorMsg, 100);
				log.setErrorLog(callErrorMsg);
				log.setDealStatus(InterfaceLogStatus.STAT_FAIL);
				log.setResponseTime(new Date());
				sapDataDealManager.store(log);
			}
		}
		if(log.getSendStatus().equals(InterfaceLogStatus.STAT_FAIL)) {
			throw new BusinessException(callErrorMsg);//如果回调失败  则抛出异常，让thorn_task也失败。
		}
	}
	/**发邮件*/
	public void sendEmail(Long emailRecordId) {
		
		try {
			emailRecordManager.sendEmail(emailRecordId);
			emailRecordManager.updateEmailRecordToSucess(emailRecordId);
//			InterfaceLogManager il = (InterfaceLogManager)applicationContext.getBean("interfaceLogManager");
//			il.createSupplier2SapInterfacelog(emailRecordId);
		}
		catch(Exception e) {
			e.printStackTrace();
			emailRecordManager.updateEmailRecordToFail(emailRecordId,CommonHelper.getErrorMessageByException(e));
			throw new BusinessException(e);
		}
		
		
	}
	

	private void updateInterfaceLogDeailInfo(InterfaceLog log) {
		log.setDealTime(new Date());
		
		//增加处理次数
		String cnt = log.getFileType(); // 这个是处理次数
		if(StringHelper.isNullOrEmpty(cnt)) {
			log.setFileType("0");
		}
		cnt = log.getFileType(); // 这个是处理次数
		Integer deal_cnt =0;
		try {
			deal_cnt = Integer.valueOf(cnt);
	 
		}
		catch(Exception e) {
			deal_cnt =0;
		}
		deal_cnt=deal_cnt+1;
		log.setFileType(deal_cnt+"");
	}
	
 
	
	public InterfaceLog createSapToWmsInterfaceLog(String taskType, String type, String requestXml,String request){
		return this.createInterfaceLog(taskType, type, InterfaceLogFunction.ASYNC, InterfaceLogSys.SAP_SYS, InterfaceLogSys.WMS_SYS, requestXml,request);
	}
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
    public InterfaceLog createInterfaceLog(String taskType, String type, String function, String fromSys, String toSys, String requestXml, String request) {
        try {
            InterfaceLog interfaceLog = EntityFactory.getEntity(InterfaceLog.class);
            interfaceLog.setType(type);
            interfaceLog.setFunction(function);
            interfaceLog.setFromSYS(fromSys);
            interfaceLog.setToSYS(toSys);
            
            interfaceLog.setRequest(StringHelper.substring(request, 100)); //关键信息 
            interfaceLog.setRequestContent(XMLHelper.prettyXML(requestXml)); //格式化
            interfaceLog.setRequestTime(new Date());
            commonDao.store(interfaceLog);
            
            if(InterfaceLogFunction.ASYNC.equals(function)) { //异步方式
            	createInterfaceLogTask(taskType, interfaceLog.getId(),TaskSubscriber.INTERFACELOG_DEAL); //保存执行报文任务
            }
            return interfaceLog;
        } catch (Exception e) {
            logger.error("", e);
            throw new BusinessException("保存报文失败");
        }
    }
    
    /**更新interfaceLog成成功*/
    public InterfaceLog updateInterfaceLogToSucess(Long id,String responseXml,String info) {
        return updateInterfaceLog(id,responseXml,InterfaceLogStatus.STAT_FINISH,info);
    }
    
    /**更新interfaceLog成失败*/
    public InterfaceLog updateInterfaceLogToFail(Long id,String responseXml,String info) {
        return updateInterfaceLog(id,responseXml,InterfaceLogStatus.STAT_FAIL,info);
    }
    private InterfaceLog updateInterfaceLog(Long id,String responseXml,String status,String info) {
        InterfaceLog log = commonDao.load(InterfaceLog.class, id);
        if(log != null) {
            log.setStatus(status);
            log.setResponseContent(responseXml);
            log.setResponseTime(new Date());
            log.setReference(StringHelper.substring(info, 255)); //关键信息
            log.setErrorLog(StringHelper.substring(info, 255));
        }
        return log;
    }
	
    /**
     * 创建报文执行任务
     * 
     * @param taskType {@link InterfaceLogTaskType}
     * @param interfaceLogId
     */
    private Task createInterfaceLogTask(String taskType, Long interfaceLogId,String subscriber) {
        try {
            Task task = new Task(taskType, subscriber, interfaceLogId);//sub不能为空
            commonDao.store(task);
            return task;
        } catch (Exception e) {
            logger.error("", e);
            throw new BusinessException("保存报文执行任务失败");
        }
    }

	@Override
	public Wms2SapInterfaceLog createWmsToSapInterfaceLog(String taskType,String type, String requestXml, Long refrenceId,String request) {
		return this.createInterfaceLog(taskType, type, InterfaceLogFunction.ASYNC, InterfaceLogSys.WMS_SYS, InterfaceLogSys.SAP_SYS, requestXml,refrenceId,request);
	}

	private Wms2SapInterfaceLog createInterfaceLog(String taskType,String type, String function, String fromSys, String toSys,String requestXml, Long refrenceId,String request) {
		try {
			Wms2SapInterfaceLog interfaceLog = EntityFactory.getEntity(Wms2SapInterfaceLog.class);
            interfaceLog.setType(type);
            interfaceLog.setFunction(function);
            interfaceLog.setFromSYS(fromSys);
            interfaceLog.setToSYS(toSys);
            interfaceLog.setRequest(request);
            interfaceLog.setDocId(refrenceId);//单据对象ID
            interfaceLog.setRequestContent(XMLHelper.prettyXML(requestXml)); //格式化
            interfaceLog.setRequestTime(new Date());
            commonDao.store(interfaceLog);
            
            if(InterfaceLogFunction.ASYNC.equals(function)) { //异步方式
            	createInterfaceLogTask(taskType, interfaceLog.getId(),TaskSubscriber.WMS2SAPINTERFACELOG_DEAL); //保存执行报文任务
            }
            return interfaceLog;
        } catch (Exception e) {
            logger.error("", e);
            throw new BusinessException("保存报文失败");
        }
	}
    //创建wms2sapDeliveryOrder
    @SuppressWarnings("unchecked")
	public void createWms2SapDeliveryOrder(Long deliveryOrderId){
    	WmsDeliveryOrder deliveryOrder = commonDao.load(WmsDeliveryOrder.class, deliveryOrderId);
    	String hql = "from WmsDeliveryOrderDetail detail where detail.deliveryOrder.id =:deliveryOrderId ";
    	List<WmsDeliveryOrderDetail> details = commonDao.findByQuery(hql, "deliveryOrderId", deliveryOrder.getId());
    	for(WmsDeliveryOrderDetail dod : details){
//    		PurchaseOrderDetail pod = commonDao.load(PurchaseOrderDetail.class, dod.getPurchaseOrderDetail().getId());
    		//创建交货单回传sap报文
            Wms2SapDeliveryOrder wms2SapDeliveryOrder = new Wms2SapDeliveryOrder();
            wms2SapDeliveryOrder.setLineNo("000010");
            wms2SapDeliveryOrder.setDoNo(deliveryOrder.getCode());
            wms2SapDeliveryOrder.setCount("1");
            wms2SapDeliveryOrder.setCommond(null);
            wms2SapDeliveryOrder.setDoLineNo(dod.getLineNo()+"");
            wms2SapDeliveryOrder.setPoNo(dod.getPoNo());
            wms2SapDeliveryOrder.setPoLineNo(dod.getPoDetailNo());
            wms2SapDeliveryOrder.setItemCode(dod.getItem().getCode());
            wms2SapDeliveryOrder.setQuantity(dod.getPlanQuantityBu()+"");
            wms2SapDeliveryOrder.setUnit(dod.getPackageUnit().getUnit());
            wms2SapDeliveryOrder.setFactoryCode(dod.getFactory().getCode());
            wms2SapDeliveryOrder.setLFDAT(DateUtils.getDateStr(deliveryOrder.getDeliveryDate(), "yyyyMMdd"));
            String xml = XmlObjectConver.toXML(wms2SapDeliveryOrder);
            createWms2SapInterfaceLog(deliveryOrder.getCode(),InterfaceLogTaskType.SEND_DELIVERY, Wms2SapInterfaceLogType.DELIVERY_DOC, InterfaceLogFunction.ASYNC, InterfaceLogSys.WMS_SYS,InterfaceLogSys.SAP_SYS, xml);
    	}
    }
    
    public Wms2SapInterfaceLog createWms2SapInterfaceLog(String code,String taskType, String type, String function, String fromSys, String toSys, String requestXml) {
        try {
        	Wms2SapInterfaceLog interfaceLog = EntityFactory.getEntity(Wms2SapInterfaceLog.class);
            interfaceLog.setType(type);
            interfaceLog.setFunction(function);
            interfaceLog.setFromSYS(fromSys);
            interfaceLog.setToSYS(toSys);
            interfaceLog.setRequestContent(XMLHelper.prettyXML(requestXml)); //格式化
            interfaceLog.setRequestTime(new Date());
            interfaceLog.setRequest(code);
            commonDao.store(interfaceLog);
            
            if(InterfaceLogFunction.ASYNC.equals(function)) { //异步方式
            	createInterfaceLogTask(taskType, interfaceLog.getId(),TaskSubscriber.WMS2SAPINTERFACELOG_DEAL); //保存执行报文任务
            }
            return interfaceLog;
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("", e);
            throw new BusinessException("保存报文失败");
        }
    }
    public void createSupplier2SapInterfacelog(Long emailRecordId){
    	EmailRecord email = commonDao.load(EmailRecord.class, emailRecordId);
    	String code = email.getRealateCode();
    	if(EmailRecordType.PO2SUPPLIER.equals(email.getEmailType())){
    		Wms2SapSupplierDocStatus wsds = new Wms2SapSupplierDocStatus();
        	wsds.setType(Wms2SapSupplierDocType.PO);
        	wsds.setOrderNo(code);
        	wsds.setStatus(Wms2SapSupplierStatus.SEND);
        	String xml = XmlObjectConver.toXML(wsds) ;
        	createWms2SapInterfaceLog(code, InterfaceLogTaskType.SEND_SURPPLIERBUSINESS, Wms2SapInterfaceLogType.SURPPLIERBUSINESSBACK, InterfaceLogFunction.ASYNC, InterfaceLogSys.WMS_SYS, InterfaceLogSys.SAP_SYS, xml);
    	}
    	if(EmailRecordType.DELIVERY2SUPPLIER.equals(email.getEmailType())){
    		Wms2SapSupplierDocStatus wsds = new Wms2SapSupplierDocStatus();
        	wsds.setType(Wms2SapSupplierDocType.DELIVERY);
        	wsds.setOrderNo(code);
        	wsds.setStatus(Wms2SapSupplierStatus.SEND);
        	String xml = XmlObjectConver.toXML(wsds) ;
        	createWms2SapInterfaceLog(code, InterfaceLogTaskType.SEND_SURPPLIERBUSINESS, Wms2SapInterfaceLogType.SURPPLIERBUSINESSBACK, InterfaceLogFunction.ASYNC, InterfaceLogSys.WMS_SYS, InterfaceLogSys.SAP_SYS, xml);
    	}
    	if(EmailRecordType.CHECKBZSUPPLIER.equals(email.getEmailType()) || EmailRecordType.CHECKJSSUPPLIER.equals(email.getEmailType())){
    		Wms2SapSupplierDocStatus wsds = new Wms2SapSupplierDocStatus();
        	wsds.setType(Wms2SapSupplierDocType.CHECKORDER);
        	wsds.setOrderNo(code);
        	wsds.setStatus(Wms2SapSupplierStatus.SEND);
        	String xml = XmlObjectConver.toXML(wsds) ;
        	createWms2SapInterfaceLog(code, InterfaceLogTaskType.SEND_SURPPLIERBUSINESS, Wms2SapInterfaceLogType.SURPPLIERBUSINESSBACK, InterfaceLogFunction.ASYNC, InterfaceLogSys.WMS_SYS, InterfaceLogSys.SAP_SYS, xml);
    	}
    }
    /**重新执行失败的接口日志*/
    public void resendFailInterfaceLog(InterfaceLog log) {
    	String hql = "FROM Task task WHERE task.status =:status AND task.messageId =:messageId ";
    	List<Task> tasks = commonDao.findByQuery(hql, new String[]{"status","messageId"}, new Object[]{TaskStatus.STAT_FAIL,log.getId()});
    	for(Task task : tasks){
    		if(TaskSubscriber.INTERFACELOG_RESPONSEINFO2SAP.equals(task.getSubscriber())){
    			commonDao.delete(task);
    		}
    		if(TaskSubscriber.INTERFACELOG_DEAL.equals(task.getSubscriber())){
    			task.setStatus(TaskStatus.STAT_READY);
    			commonDao.store(task);
    		}
    	}
    }
    
    /**重新执行失败的报文信息*/
    public void resendFailWms2SapInterfaceLog(Wms2SapInterfaceLog log) {
    	String hql = "FROM Task task WHERE 1=1 AND task.messageId =:messageId AND task.subscriber ='interfaceLogManager.sendWms2SapInterfaceLog' ";
    	List<Task> tasks = commonDao.findByQuery(hql, new String[]{"messageId"}, new Object[]{log.getId()});
    	for(Task task : tasks){
    		task.setStatus(TaskStatus.STAT_READY);
    		commonDao.store(task);
    	}
    }
    
    public InterfaceLog createFinishedInterfaceLog(String taskType, String type, String function, String fromSys, String toSys, String requestXml,String request){
    	 try {
             InterfaceLog interfaceLog = EntityFactory.getEntity(InterfaceLog.class);
             interfaceLog.setType(type);
             interfaceLog.setFunction(function);
             interfaceLog.setFromSYS(fromSys);
             interfaceLog.setToSYS(toSys);
             interfaceLog.setStatus(InterfaceLogStatus.STAT_FINISH);
             interfaceLog.setRequest(StringHelper.substring(request, 100)); //关键信息 
             interfaceLog.setRequestContent(XMLHelper.prettyXML(requestXml)); //格式化
             interfaceLog.setRequestTime(new Date());
             commonDao.store(interfaceLog);
             
             if(InterfaceLogFunction.ASYNC.equals(function)) { //异步方式
             	createInterfaceLogTask(taskType, interfaceLog.getId(),TaskSubscriber.INTERFACELOG_DEAL); //保存执行报文任务
             }
             return interfaceLog;
         } catch (Exception e) {
             logger.error("", e);
             throw new BusinessException("保存报文失败");
         }
    }
    /**
     * 实时接口校验工单会直接走修改逻辑，所以需要带事务
     * @param scois
     */
    public void checkProductionOrder(SapCheckOrderInfoArray scois){
    	//生产订单
    	SapCheckOrderInfo[] scoi = scois.getScoi();
    	Boolean flag = Boolean.FALSE;
		Map<String,SapCheckOrderInfo> sapMap = new HashMap<String, SapCheckOrderInfo>();//SAP传过来的工单明细信息--KEY：预留号+预留行项目+物料编码，--VALUE：工单报文
		Map<String,ProductionOrderDetail> wmsMap = new HashMap<String, ProductionOrderDetail>();//wms系统中工单明细信息
		List<ProductionOrderDetail> delList = new ArrayList<ProductionOrderDetail>();//记录需要删除的工单明细
		List<SapCheckOrderInfo> addList = new ArrayList<SapCheckOrderInfo>();//记录需要新增的工单明细信息
		List<SapCheckOrderInfo> editList = new ArrayList<SapCheckOrderInfo>();//记录需要修改的工单明细信息
		//查找工单是否存在
		String hql = "from ProductionOrder po where po.code=:code";
		List<ProductionOrder> pos = commonDao.findByQuery(hql, "code", scois.getEBELN());
		if(pos.isEmpty()){
			throw new BusinessException("根据生产订单号："+scois.getEBELN()+"WMS中未找到生产订单");
		}
		if(pos.size()>1){
			throw new BusinessException("根据生产订单号："+scois.getEBELN()+"WMS中找到了"+pos.size()+"条生产订单");
		}
		ProductionOrder po = pos.get(0);
		Boolean closePro = Boolean.TRUE;
		/**
		 * 把SAP传过来的报文工单明细信息和WMS工单明细信息分别放到map中，然后遍历判断SAP是增加一个物料还是删除一个物料
		 * 报文中有一个不带删除标识即认为不是关单
		 */
		for(SapCheckOrderInfo sco : scoi){
			String key = sco.getRSNUM()+"_"+sco.getRSPOS()+"_"+sco.getMATNR1();
			sapMap.put(key, sco);
			if(StringHelper.isNullOrEmpty(sco.getLOEKZ())){
				closePro = Boolean.FALSE;
			}
		}
		hql = "from ProductionOrderDetail pod where pod.productionOrder.id =:productionOrderId ";
		List<ProductionOrderDetail> pods = commonDao.findByQuery(hql, "productionOrderId", po.getId());
		for(ProductionOrderDetail proDetail : pods){
			WmsItem item = commonDao.load(WmsItem.class, proDetail.getItem().getId());
			String key = proDetail.getReservedOrder()+"_"+proDetail.getReservedProject()+"_"+item.getCode();
			wmsMap.put(key, proDetail);
		}
		/**
		 * 1、wms是否存在已分配但是未发的拣货单明细，有不允许关单/删除，否则可以关
		 * 2、wms存在未分配单拣货单明细，删除相应的对应关系及拣货单明细的计划数量相应的减少
		 */
		if(SapPodEditType.CLOSE.equals(scois.getSTATUS()) || SapPodEditType.DELETE.equals(scois.getSTATUS())){
			for(SapCheckOrderInfo sco : scoi){
				StringHelper.assertNullOrEmpty(sco.getLOEKZ(), "关闭或删除操作，LOEKZ字段不能为空");
				if(pods.size() != scoi.length){
					throw new BusinessException("SAP传的生产订单明细条数:"+scoi.length+"条与WMS生产订单明细条数"+pods.size()+"条数不匹配，不允许关单/删除");
				}
				//查找生产订单明细
				ProductionOrderDetail poDetail = this.getProductionOrderDetail(scois.getEBELN(), sco.getRSNUM(), sco.getRSPOS());
				//校验生产订单是否有已分配但是未发的拣货单，根据生产订单明细的数量来判断，不能根据拣货单来
				//因为如果有不可拆包的物料对应两个生产订单明细但是生成一个拣货单明细，这时候如果其中一个生产订单已拣完发运，拣完的是可以关单的
				for(ProductionOrderDetail pod : pods){
					flag = this.checkProDetail(pod);
				}
//				hql = "select pdptd.pickticketDetail from ProductionOrderDetailPtDetail pdptd " +
//				"where pdptd.productionOrderDetail.productionOrder.id =:productionOrderId";
//				List<WmsPickTicketDetail> checkPtds = commonDao.findByQuery(hql, new String[]{"productionOrderId"}, 
//						new Object[]{po.getId()});
//				for(WmsPickTicketDetail checkPtd :checkPtds){
//					if(checkPtd.getAllocatedQty()>0 && DoubleUtils.sub(checkPtd.getShippedQty(), checkPtd.getAllocatedQty())<0){
//						String podHql = "select p.productionOrderDetail from ProductionOrderDetailPtDetail p where p.pickticketDetail.id =:pickticketDetailId" +
//								" and p.productionOrderDetail.productionOrder.id =:productionOrderId ";
//						List<ProductionOrderDetail> unshipPods = commonDao.findByQuery(podHql, new String[]{"pickticketDetailId","productionOrderId"},
//								new Object[]{checkPtd.getId(),po.getId()});
//						String strs = "";
//						for(ProductionOrderDetail unshipPod : unshipPods){
//							strs+="_"+unshipPod.getReservedProject();
//						}
//						throw new BusinessException("WMS存在已分配但是未发运的拣货明细，不允许关单/删除，预留行项目号"+strs);
//					}
//				}
				this.updatePtdAndPodInfo2(poDetail.getId(), Double.valueOf(sco.getMENGE()),flag);
				poDetail.setDeleteFlag(sco.getLOEKZ());
				commonDao.store(poDetail);
				if(SapPodEditType.CLOSE.equals(scois.getSTATUS())){//SAP关单
					if(closePro){
						po.setStatus(ProductionOrderStatus.FINISHED);
						commonDao.store(po);
					}
				}
				if(SapPodEditType.DELETE.equals(scois.getSTATUS())){
					po.setStatus(ProductionOrderStatus.CLOSE);
					commonDao.store(po);
				}
			}
		}
		if(SapPodEditType.MODIFY.equals(scois.getSTATUS())){
			/**
			 * 1、改物料（SAP可能增加一个物料或减少一个物料或把A物料改成B物料）
			 * 2、改数量：改大可以直接改,改小：判断改后数量小于已分配数量，报错不允许改小
			 * 3、报文中有的物料，wms没有的做新增，报文中没有的，wms有的做删除
			 */
			Set<String> wmsKeys = wmsMap.keySet();
			for(String wmsKey : wmsKeys){
				if(!sapMap.containsKey(wmsKey)){
					delList.add(wmsMap.get(wmsKey));
				}
			}
			Set<String> sapKeys = sapMap.keySet();
			for(String sapKey : sapKeys){
				if(!wmsMap.containsKey(sapKey)){
					addList.add(sapMap.get(sapKey));
				}else{
					editList.add(sapMap.get(sapKey));
				}
			}
			WmsSapFactory factory = commonDao.load(WmsSapFactory.class, po.getFactory().getId());//工厂
			//wms需要删除的工单明细
			if(!delList.isEmpty()){
				for(ProductionOrderDetail proDetail : delList){
					if(proDetail.getAllocatedQuantityBu()>0){
						throw new BusinessException("生产订单明细已分配，不能删除生产明细，预留行项目_"+proDetail.getReservedProject());
					}
					flag = this.checkProDetail(proDetail);
//					List<WmsPickTicketDetail> ptds = this.getPickTicketDetail(proDetail.getId());
//					for(WmsPickTicketDetail checkPtdsPtd :ptds){
//						if(checkPtdsPtd.getAllocatedQty()>0){
//							throw new BusinessException("WMS存在已拣配的拣货单明细，不允许修改，预留行项目_"+proDetail.getReservedProject());
//						}
//					}
				}
				for(ProductionOrderDetail proDetail : delList){
					this.updatePtdAndPodInfo(proDetail.getId(), proDetail.getPlanQuantityBu(),flag);
					commonDao.delete(proDetail);
				}
			}
			//SAP新增工单明细
			if(!addList.isEmpty()){
				for(SapCheckOrderInfo sco : addList){
					po.setBeginDate(DateUtil.formatDate(sco.getGSTRP()));//开始日期
					po.setEndDate(DateUtil.formatDate(sco.getGLTRP()));//结束日期
					po.setPlanQuantity(Double.valueOf(sco.getGAMNG()));//计划成品数量
					po.setBeCreatPt(Boolean.FALSE);
					po.setSaleCode(sco.getXSDH());
					if(!"TP05".equals(po.getPtype())){
			        	StringHelper.assertNullOrEmpty(sco.getZPRO_LINE(), "ZPRO_LINE属性不能为空");
			        	po.setProductLine(sco.getZPRO_LINE());//生产线
			        	po.setLineDesc(sco.getZPRO_NAME());//生产线描述
			        }else{
			        	//冰箱,订单类型TP05的，SAP有产线就按SAP给的来，没有，产线就默认TP05
			        	if(StringHelper.in(factory.getCode(), new String[]{WmsSapFactoryCodeEnum.BX_NX,WmsSapFactoryCodeEnum.BX_WX})
			        			&& !StringHelper.isNullOrEmpty(sco.getZPRO_LINE())){
				        		po.setProductLine(sco.getZPRO_LINE());//生产线
					        	po.setLineDesc(sco.getZPRO_NAME());//生产线描述
			            }
			        	else{
			            	po.setProductLine("TP05");//生产线
			            	po.setLineDesc("研发样机");//产线描述
			            }
			        }
					ProductionOrderDetail detail  = EntityFactory.getEntity(ProductionOrderDetail.class);
					detail.setProductionOrder(po);
					detail.setLineNo(getMaxLineNo(po.getCode()));
					detail.setReservedOrder(sco.getRSNUM());
					detail.setReservedProject(sco.getRSPOS());
					List<WmsItem> items = commonDao.findByQuery("FROM WmsItem WHERE code=:code", "code", sco.getMATNR1());
			    	
			    	if(items.isEmpty()) {
			    		throw new BusinessException("明细物料【"+sco.getMATNR1()+"】不存在");
			    	}
			    	if(items.size()>1) {
			    		throw new BusinessException("根据编码"+sco.getMATNR1()+"找到了"+items.size()+"条物料");
			    	}
			    	WmsItem item = items.get(0); 
			        detail.setItem(item);
			        hql = "from WmsItemFactory itemf where itemf.item=:item and itemf.factory=:factory";
			    	List<WmsItemFactory> ifs = commonDao.findByQuery(hql, new String[]{"item","factory"}, new Object[]{item,factory});
					if (ifs.isEmpty()) {
						throw new BusinessException("物料编码【" + item.getCode() + "】在工厂"+factory.getName()+"下未绑定!");
					}

					if (ifs.size() > 1) {
						throw new BusinessException("物料编码【" + item.getCode() + "】在工厂"+factory.getName()+"下绑定了"+ifs.size()+"条!");
					}
			        if(!StringHelper.isNullOrEmpty(sco.getLOEKZ())){ //删除标识X--不做处理，系统记录这个标识，下发拣配的时候过滤有删除标识的明细
			        	detail.setDeleteFlag(sco.getLOEKZ());
			        }else{
			        	detail.setPlanQuantityBu(new Double(sco.getMENGE().trim()));//需求数量
			        	detail.setAllocatedQuantityBu(Double.valueOf(sco.getENMNG()==null ? "0": sco.getENMNG().toString()));//记录接口过来的发运数量
			        	detail.setPickedQuantityBu(Double.valueOf(sco.getENMNG()==null ? "0": sco.getENMNG().toString()));
			        	detail.setShippedQuantityBu(Double.valueOf(sco.getENMNG()==null ? "0": sco.getENMNG().toString())); 
			        	detail.setOldShippedQuantityBu(Double.valueOf(sco.getENMNG()==null ? "0": sco.getENMNG().toString()));
			        	detail.setXfQty(Double.valueOf(sco.getENMNG()==null ? "0": sco.getENMNG().toString()));//记录接口过来的发运数量
			        }
			        List<WmsPackageUnit> pkus = commonDao.findByQuery("FROM WmsPackageUnit "
			                + "WHERE item.id=:itemId AND unit=:code",  
			                new String[]{"itemId", "code"},  new Object[]{item.getId(), sco.getMEINS1()});
			        if (pkus.isEmpty()) {
			            throw new BusinessException("WMS物料编码【"+item.getCode()+"】的包装单位【"+sco.getMEINS1()+"】未维护");
			        }
			        if(pkus.size()>1){
			        	throw new BusinessException("根据物料ID"+item.getCode()+"和单位"+sco.getMEINS1()+"找到了"+pkus.size()+"条包装单位信息");
			        }
			        WmsPackageUnit pku = pkus.get(0);
			        detail.setPackageUnit(pku);
			        commonDao.store(detail);
			        po.setBeCreatPt(Boolean.FALSE);
			        if(closePro){
						po.setStatus(ProductionOrderStatus.FINISHED);
					}else{
						po.setStatus(ProductionOrderStatus.ACTIVE);
					}
			        commonDao.store(po);
				}
			}
			//修改工单明细
			if(!editList.isEmpty()){
				//改产线
				for(SapCheckOrderInfo sco : editList){
					if(!StringHelper.isNullOrEmpty(sco.getZPRO_LINE())){
						if(!po.getProductLine().equals(sco.getZPRO_LINE()) && (po.getBeCreatPt() || po.getBeDetailCreatePt()) ){
							throw new BusinessException("工单已下发,不允许修改产线");
						}
						if(!"TP05".equals(po.getPtype())){
				        	po.setProductLine(sco.getZPRO_LINE());//生产线
				        	po.setLineDesc(sco.getZPRO_NAME());//生产线描述
				        }else{
				        	//冰箱,订单类型TP05的，SAP有产线就按SAP给的来，没有，产线就默认TP05
				        	if(StringHelper.in(factory.getCode(), new String[]{WmsSapFactoryCodeEnum.BX_NX,WmsSapFactoryCodeEnum.BX_WX})
				        			&& !StringHelper.isNullOrEmpty(sco.getZPRO_LINE())){
					        		po.setProductLine(sco.getZPRO_LINE());//生产线
						        	po.setLineDesc(sco.getZPRO_NAME());//生产线描述
				            }
				        	else{
				            	po.setProductLine("TP05");//生产线
				            	po.setLineDesc("研发样机");//产线描述
				            }
				        }
					}
				}
					
				//改数量,改大可以随便改
				for(SapCheckOrderInfo sco : editList){
					ProductionOrderDetail poDetail = this.getProductionOrderDetail(scois.getEBELN(), sco.getRSNUM(), sco.getRSPOS());
					//数量改小,则改小后的数量不能小于已分配数量
					Double editQty = DoubleUtils.sub(Double.valueOf(sco.getMENGE()), poDetail.getXfQty()) ;
					//如果不是删除某一个明细或数量改小都可以直接让SAP过去
					if(!StringHelper.isNullOrEmpty(sco.getLOEKZ())){
						if(poDetail.getAllocatedQuantityBu()>0){
							throw new BusinessException("生产订单明细已分配，不能删除生产明细，预留行项目_"+poDetail.getReservedProject());
						}
						flag = this.checkProDetail(poDetail);
						this.updatePtdAndPodInfo(poDetail.getId(), poDetail.getPlanQuantityBu(),flag);
					}
					if(editQty<0){
						//修改后的数量如果大于下发数量 可以直接改，否则判断改后的数量如果大于分配数量可以改
						if(DoubleUtils.sub(Double.valueOf(sco.getMENGE()), poDetail.getAllocatedQuantityBu())>=0){
							this.updatePtdAndPodInfo(poDetail.getId(), -editQty,Boolean.FALSE);
							poDetail.subXfQty(-editQty);
						}else{
							throw new BusinessException("工单明细ID"+poDetail.getId()+"修改后的数量:"+sco.getMENGE()+"不能小于已分配数量"+poDetail.getAllocatedQuantityBu());
						}
					}
					po.setBeginDate(DateUtil.formatDate(sco.getGSTRP()));//开始日期
					po.setEndDate(DateUtil.formatDate(sco.getGLTRP()));//结束日期
					po.setPlanQuantity(Double.valueOf(sco.getGAMNG()));//计划成品数量
					po.setBeCreatPt(Boolean.FALSE);
					po.setSaleCode(sco.getXSDH());
					//工单明细有修改数量的就把所有状态置为空
					if(editQty != 0){
						poDetail.setStatus("");
						poDetail.setPickSataus("");
						poDetail.setShipStatus("");
					}
					poDetail.setPlanQuantityBu(Double.valueOf(sco.getMENGE()));//工单明细计划数量
					poDetail.setDealDlanQuantityBu(Double.valueOf(sco.getMENGE()));
					poDetail.setDeleteFlag(sco.getLOEKZ());
					poDetail.setBeCreatePt(Boolean.FALSE);
					if(closePro){
						po.setStatus(ProductionOrderStatus.FINISHED);
					}else{
						if(StringHelper.in(po.getStatus(), new String[]{ProductionOrderStatus.FINISHED,ProductionOrderStatus.CLOSE})){
							poDetail.setBeChangeXfQty(Boolean.TRUE);
						}
						po.setStatus(ProductionOrderStatus.ACTIVE);
					}
					commonDao.store(po);
					commonDao.store(poDetail);
				}
			}
		}
    }
    /**
	 * 通过ptd与pod对应关系表找到工单明细对应的拣货单明细
	 * @param proDetailId
	 * @return
	 */
	private List<WmsPickTicketDetail> getPickTicketDetail(Long proDetailId){
		String hql = "select pdptd.pickticketDetail from ProductionOrderDetailPtDetail pdptd " +
				"where pdptd.productionOrderDetail.id =:productionOrderDetailId";
		List<WmsPickTicketDetail> ptds = commonDao.findByQuery(hql, new String[]{"productionOrderDetailId"}, 
				new Object[]{proDetailId});
		return ptds;
	}
	/**
	 * 更新拣货单及对应关系表
	 * @param proDetailId
	 * @param qty
	 */
	private void updatePtdAndPodInfo(Long proDetailId,Double qty,Boolean flag){
		List<WmsPickTicketDetail> ptds = this.getPickTicketDetail(proDetailId);
		Double cancelQty = qty;
		for(WmsPickTicketDetail ptd :ptds){
			if(cancelQty<=0){
				break;
			}
			String hql = " from ProductionOrderDetailPtDetail p where p.productionOrderDetail.id=:productionOrderDetailId " +
					" and p.pickticketDetail.id=:pickticketDetailId ";
			ProductionOrderDetailPtDetail podpt = (ProductionOrderDetailPtDetail) commonDao.findByQueryUniqueResult(hql, new String[]{"productionOrderDetailId","pickticketDetailId"},
					new Object[]{proDetailId,ptd.getId()});
			WmsPickTicket pt = ptd.getPickTicket();
			if(flag && !StringHelper.in(pt.getStatus(), new String[]{WmsPickTicketStatus.CANCELED,WmsPickTicketStatus.OPEN,WmsPickTicketStatus.FINISHED,WmsPickTicketStatus.PART_SHIP})){
				throw new BusinessException("状态不对，请查看拣货单的状态"+pt.getCode());
			}
			if(WmsPickTicketStatus.OPEN.equals(pt.getStatus()) || WmsPickTicketStatus.CANCELED.equals(pt.getStatus())){
				if(DoubleUtils.sub(cancelQty, ptd.getExpectedQty())<=0){
					pt.removeQty(cancelQty);
					commonDao.store(pt);
					ptd.setExpectedPackQty(DoubleUtils.sub(ptd.getExpectedPackQty(), cancelQty));
					ptd.setExpectedQty(DoubleUtils.sub(ptd.getExpectedQty(), cancelQty));
					podpt.removeQuantityBu(cancelQty);
					cancelQty=0D;
				}else{
					cancelQty-=ptd.getExpectedQty();
					podpt.removeQuantityBu(ptd.getExpectedQty());
					pt.removeQty(ptd.getExpectedQty());
					commonDao.store(pt);
					ptd.setExpectedPackQty(0D);
					ptd.setExpectedQty(0D);
				}
				ptd.setPickTicket(pt);
				commonDao.store(ptd);
				commonDao.store(podpt);
				if(CommonHelper.dealDoubleError(podpt.getQuantityBu())<=0d){
					commonDao.delete(podpt);
				}
				if(CommonHelper.dealDoubleError(ptd.getExpectedQty())<=0d){
					//删除工单明细与拣货单明细对应关系
					hql = "delete from ProductionOrderDetailPtDetail p where p.pickticketDetail.id =:pickticketDetailId ";
					commonDao.executeByHql(hql, "pickticketDetailId", ptd.getId());
					//删除拣货明细批次属性要求
					hql = "delete from WmsPickTicketDetailRequire pdr where pdr.pickTicketDetail.id =:pickTicketDetailId ";
					commonDao.executeByHql(hql, "pickTicketDetailId",ptd.getId());
					//退拣的没删除BOLDetail,此时删除
					hql = "delete from WmsBolDetail d where d.pickTicketDetail.id=:pickTicketDetailId ";
					commonDao.executeByHql(hql, "pickTicketDetailId",ptd.getId());
					//删除拣货明细
					CommonHelper.log("delete ptdid:"+ptd.getId());
					hql = "delete from WmsPickTicketDetail p where p.id =:pId ";
					commonDao.executeByHql(hql, "pId",ptd.getId());
				}
				if(CommonHelper.dealDoubleError(pt.getQty())<=0d){
					//删除拣货单
					hql = "delete from WmsPickTicket pt where pt.id =:ptId ";
					commonDao.executeByHql(hql, "ptId",pt.getId());
				}
			}
		}
	}
	/**
	 * 工单关单专用
	 * @param proDetailId
	 * @param qty
	 * @param flag
	 */
	private void updatePtdAndPodInfo2(Long proDetailId,Double qty,Boolean flag){
		List<WmsPickTicketDetail> ptds = this.getPickTicketDetail(proDetailId);
		ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, proDetailId);
		Double cancelQty = qty;
		String dateStr = "";
		String hql = "from WmsSystemValues s where s.code=:code ";
		WmsSystemValues sv = (WmsSystemValues) commonDao.findByQueryUniqueResult(hql, "code", WmsSystemValuesType.CTDATE);
		if(sv==null){
			dateStr = "20180122";
		}else{
			dateStr = sv.getValue();
		}
		Date date = DateUtil.formatDate(dateStr);
		hql = " from ProductionOrderDetailPtDetail p where p.productionOrderDetail.id=:productionOrderDetailId ";
		List<ProductionOrderDetailPtDetail> podpts = commonDao.findByQuery(hql, 
				new String[]{"productionOrderDetailId"},new Object[]{proDetailId});
		for(ProductionOrderDetailPtDetail podpt :podpts){
			//历史数据的对应关系有问题，所以关单的时候数量用SAP接口传输过来的数量，
			//新数据关单时数量用工单的未分配数量
			if(podpt.getCtDate().after(date)){//新数据
				cancelQty = pod.getUnAllocateQty();
			}
		}
		for(WmsPickTicketDetail ptd :ptds){
			if(cancelQty<=0){
				break;
			}
			hql = " from ProductionOrderDetailPtDetail p where p.productionOrderDetail.id=:productionOrderDetailId " +
					" AND p.pickticketDetail.id=:pickticketDetailID ";
			ProductionOrderDetailPtDetail podpt = (ProductionOrderDetailPtDetail) commonDao.findByQueryUniqueResult(hql, 
					new String[]{"productionOrderDetailId","pickticketDetailID"}, new Object[]{proDetailId,ptd.getId()});
			WmsPickTicket pt = ptd.getPickTicket();
			if(flag && !StringHelper.in(pt.getStatus(), new String[]{WmsPickTicketStatus.CANCELED,WmsPickTicketStatus.OPEN,WmsPickTicketStatus.FINISHED,WmsPickTicketStatus.PART_SHIP})){
				throw new BusinessException("状态不对，请查看拣货单的状态"+pt.getCode());
			}
			if(WmsPickTicketStatus.OPEN.equals(pt.getStatus()) || WmsPickTicketStatus.CANCELED.equals(pt.getStatus())){
				if(DoubleUtils.sub(cancelQty, ptd.getExpectedQty())<=0){
					pt.removeQty(cancelQty);
					commonDao.store(pt);
					ptd.setExpectedPackQty(DoubleUtils.sub(ptd.getExpectedPackQty(), cancelQty));
					ptd.setExpectedQty(DoubleUtils.sub(ptd.getExpectedQty(), cancelQty));
					podpt.removeQuantityBu(cancelQty);
					cancelQty=0D;
				}else{
					cancelQty-=ptd.getExpectedQty();
					podpt.removeQuantityBu(ptd.getExpectedQty());
					pt.removeQty(ptd.getExpectedQty());
					commonDao.store(pt);
					ptd.setExpectedPackQty(0D);
					ptd.setExpectedQty(0D);
				}
				ptd.setPickTicket(pt);
				commonDao.store(ptd);
				commonDao.store(podpt);
				if(CommonHelper.dealDoubleError(podpt.getQuantityBu())<=0d){
					commonDao.delete(podpt);
				}
				if(CommonHelper.dealDoubleError(ptd.getExpectedQty())<=0d){
					//删除工单明细与拣货单明细对应关系
					hql = "delete from ProductionOrderDetailPtDetail p where p.pickticketDetail.id =:pickticketDetailId ";
					commonDao.executeByHql(hql, "pickticketDetailId", ptd.getId());
					//删除拣货明细批次属性要求
					hql = "delete from WmsPickTicketDetailRequire pdr where pdr.pickTicketDetail.id =:pickTicketDetailId ";
					commonDao.executeByHql(hql, "pickTicketDetailId",ptd.getId());
					//退拣的没删除BOLDetail,此时删除
					hql = "delete from WmsBolDetail d where d.pickTicketDetail.id=:pickTicketDetailId ";
					commonDao.executeByHql(hql, "pickTicketDetailId",ptd.getId());
					//删除拣货明细
					CommonHelper.log("delete ptdid:"+ptd.getId());
					hql = "delete from WmsPickTicketDetail p where p.id =:pId ";
					commonDao.executeByHql(hql, "pId",ptd.getId());
				}
				if(CommonHelper.dealDoubleError(pt.getQty())<=0d){
					
					//有些退拣退料会造成存在数量为0的明细  此时如果直接删除PT会报错删除不了，所以先删除明细相关信息
					String hql2="from WmsPickTicketDetail ptd where ptd.pickTicket.id=:id and ptd.expectedQty<=0.0000001";
					List ds = commonDao.findByQuery(hql2,new String[]{"id"},new Object[]{pt.getId()});
					if(!ds.isEmpty()) {
						//删除工单明细与拣货单明细对应关系
						hql = "delete from ProductionOrderDetailPtDetail p " +
								"where p.pickticketDetail.id in (select m.id from WmsPickTicketDetail m where m.pickTicket.id =:pickticketId) ";
						commonDao.executeByHql(hql, "pickticketId", pt.getId());
						//删除拣货明细批次属性要求
						hql = "delete from WmsPickTicketDetailRequire pdr " +
								"where pdr.pickTicketDetail.id in (select m.id from WmsPickTicketDetail m where m.pickTicket.id =:pickticketId) ";
						commonDao.executeByHql(hql, "pickticketId",pt.getId());
						//退拣的没删除BOLDetail,此时删除
						hql = "delete from WmsBolDetail d " +
								"where d.pickTicketDetail.id in (select m.id from WmsPickTicketDetail m where m.pickTicket.id =:pickticketId) ";
						commonDao.executeByHql(hql, "pickticketId",pt.getId());
						//删除拣货明细
						CommonHelper.log("having qty is 0 detail delete pt:"+pt.getId());
						hql = "delete from WmsPickTicketDetail p where p.pickTicket.id =:pId ";
						commonDao.executeByHql(hql, "pId",pt.getId());
					}
					
					//删除拣货单
					hql = "delete from WmsPickTicket pt where pt.id =:ptId ";
					commonDao.executeByHql(hql, "ptId",pt.getId());
				}
			}
		}
	}
	/**
	 * 查找生产订单明细
	 * @param poCode
	 * @param resCode
	 * @param resProject
	 * @return
	 */
	private ProductionOrderDetail getProductionOrderDetail (String poCode, String resCode,String resProject){
		String hql = "from ProductionOrderDetail pod where pod.productionOrder.code =:productionOrderCode " +
		"and pod.reservedOrder =:reservedOrder and pod.reservedProject=:reservedProject ";
		List<ProductionOrderDetail> podetails = commonDao.findByQuery(hql, new String[]{"productionOrderCode","reservedOrder","reservedProject"},
				new Object[]{poCode,resCode,resProject});
		if(podetails.isEmpty()){
			throw new BusinessException("根据生产订单号:"+poCode+"预留号:"+resCode+"预留行项目:"+resProject+"在WMS中未找到生产订单明细");
		}
		if(podetails.size()>1){
			throw new BusinessException("根据生产订单号:"+poCode+"预留号:"+resCode+"预留行项目:"+resProject+"在WMS中找到"+podetails.size()+"条生产订单明细");
		}
		return podetails.get(0);
	}
	/**
	 * 获取最大行号
	 * @param poCode
	 * @return
	 */
	private Integer getMaxLineNo(String poCode){
		String hql =  "select MAX(pod.lineNo) from ProductionOrderDetail pod where pod.productionOrder.code =:productionOrderCode ";
		Integer lineNo = (Integer) commonDao.findByQueryUniqueResult(hql, "productionOrderCode", poCode);
		return lineNo==null ? 10 : lineNo+10;
	}
	/**
	 * 校验生产订单明细是否可以修改
	 * @param pod
	 */
	private Boolean checkProDetail(ProductionOrderDetail pod){
		Double checkQty = pod.getAllocatedQuantityBu();
		ProductionOrder po = commonDao.load(ProductionOrder.class, pod.getProductionOrder().getId());
		if(DoubleUtils.sub(pod.getAllocatedQuantityBu(), pod.getPlanQuantityBu())>0){
			checkQty = pod.getPlanQuantityBu();
		}
		//考虑到不可拆包的物料多分的情况下,先判断工单明细是否有分配未发的,如果有再判断工单对应的拣货单是否有分配未发的,还有就报错
		if(pod.getAllocatedQuantityBu()>0 && DoubleUtils.sub(pod.getShippedQuantityBu(), checkQty)<0){
			String hql = "select pdptd.pickticketDetail from ProductionOrderDetailPtDetail pdptd " +
			"where pdptd.productionOrderDetail.productionOrder.id =:productionOrderId " +
			"and pdptd.pickticketDetail.pickTicket.status !='FINISHED' ";
			List<WmsPickTicketDetail> checkPtds = commonDao.findByQuery(hql, new String[]{"productionOrderId"}, 
					new Object[]{po.getId()});
			for(WmsPickTicketDetail checkPtd :checkPtds){
				if(checkPtd.getAllocatedQty()>0 && DoubleUtils.sub(checkPtd.getShippedQty(), checkPtd.getAllocatedQty())<0){
					throw new BusinessException("WMS存在已分配但是未发运的拣货明细，预留行项目号_"+pod.getReservedProject());
				}
			}
		}
		//有种情况，WMS发运后把台账报文传给SAP，但是SAP还没来得及处理，生产订单被操作员关单，这时如果SAP再处理发运的台账报文处理不了
		String hql = "from TclWmsInventoryLedger t where t.reservedCode=:reservedCode " +
				"and t.resProject=:resProject and t.sapCode is null";
		List<TclWmsInventoryLedger> ledgers = commonDao.findByQuery(hql,new String[]{"reservedCode","resProject"},
				new Object[]{pod.getReservedOrder(),pod.getReservedProject()}	);
		if(!ledgers.isEmpty()){
			throw new BusinessException("生产订单发料的台账记录SAP还没处理，预留行项目号_"+pod.getReservedProject());
		}
		return true;
	}
}