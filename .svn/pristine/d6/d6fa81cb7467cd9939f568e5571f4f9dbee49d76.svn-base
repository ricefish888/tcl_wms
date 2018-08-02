package com.vtradex.wms.server.service.production.pojo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.jdbc.core.JdbcTemplate;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.entity.base.MidSurpplierUser;
import com.vtradex.wms.server.model.entity.base.Wms2SapInterfaceLogType;
import com.vtradex.wms.server.model.entity.base.WmsFactoryWarehouse;
import com.vtradex.wms.server.model.entity.base.WmsFactoryXmlb;
import com.vtradex.wms.server.model.entity.base.WmsSapFactory;
import com.vtradex.wms.server.model.entity.base.WmsWarning;
import com.vtradex.wms.server.model.entity.base.WmsWarningStatus;
import com.vtradex.wms.server.model.entity.base.WmsWarningType;
import com.vtradex.wms.server.model.entity.email.EmailRecordType;
import com.vtradex.wms.server.model.entity.inventory.WmsInventoryTrend;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemUnPackingAtt;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.order.ConfirmStatus;
import com.vtradex.wms.server.model.entity.order.ProductionOrderMeetInfoStatus;
import com.vtradex.wms.server.model.entity.order.PurchaseOrderDetail;
import com.vtradex.wms.server.model.entity.order.WmsArrivalDelivery;
import com.vtradex.wms.server.model.entity.order.WmsProductionOrderMeetInfo;
import com.vtradex.wms.server.model.entity.order.WmsProductionOrderMeetInfoType;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetailRequire;
import com.vtradex.wms.server.model.entity.production.DailyDeliverOrderDetail;
import com.vtradex.wms.server.model.entity.production.DeliveryOrderDetailPtDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrder;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrderStatus;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrder;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderBillType;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderCreatedType;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderStatus;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderType;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.enums.BaseStatus;
import com.vtradex.wms.server.model.enums.WmsInventoryOperationStatus;
import com.vtradex.wms.server.model.enums.WmsLocationType;
import com.vtradex.wms.server.model.enums.WmsLotCategoryType;
import com.vtradex.wms.server.model.enums.WmsPickticketGenType;
import com.vtradex.wms.server.model.enums.WmsSOQueryRequireType;
import com.vtradex.wms.server.service.emailrecord.EmailRecordManager;
import com.vtradex.wms.server.service.interf.InterfaceLogManager;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogTaskType;
import com.vtradex.wms.server.service.production.ProductionOrderManager;
import com.vtradex.wms.server.service.production.WmsDeliveryOrderManager;
import com.vtradex.wms.server.service.sap.SapRowDataDealManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.utils.DateUtil;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.webservice.sap.model.SapDeliveryOrder;
import com.vtradex.wms.webservice.sap.model.SapDeliveryOrderArray;
import com.vtradex.wms.webservice.sap.model.SapPo;
import com.vtradex.wms.webservice.sap.model.SapPoArray;
import com.vtradex.wms.webservice.sap.model.SapProductOrder;
import com.vtradex.wms.webservice.sap.model.SapProductOrderArray;
import com.vtradex.wms.webservice.sap.model.SapReservedData;
import com.vtradex.wms.webservice.sap.model.SapReservedDataArray;
import com.vtradex.wms.webservice.sap.model.Wms2SapSupplierDocStatus;
import com.vtradex.wms.webservice.sap.model.Wms2SapSupplierDocType;
import com.vtradex.wms.webservice.sap.model.Wms2SapSupplierStatus;
import com.vtradex.wms.webservice.utils.CommonHelper;
import com.vtradex.wms.webservice.utils.EmailHelper;
import com.vtradex.wms.webservice.utils.ExcelHelper;
import com.vtradex.wms.webservice.utils.WebServiceHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;

public class DefaultWmsDeliveryOrderManager extends DefaultBaseManager implements WmsDeliveryOrderManager {
    
	
    public static final String MEET_DAY="DAY"; //每天的齐套计算
    public static final String MEET_MON="MON";//计算每月的齐套信息
    
    
	 private WmsBussinessCodeManager wmsBussinessCodeManager;
	 private WorkflowManager workflowManager;
	 private InterfaceLogManager interfaceLogManager;
	 private SapRowDataDealManager sapRowDataDealManager;
	 private ProductionOrderManager productionOrderManager;
	 private JdbcTemplate jdbcTemplate;
    
	 public DefaultWmsDeliveryOrderManager(WmsBussinessCodeManager wmsBussinessCodeManager, WorkflowManager workflowManager ,InterfaceLogManager interfaceLogManager,
			SapRowDataDealManager sapRowDataDealManager,ProductionOrderManager productionOrderManager,JdbcTemplate jdbcTemplate) {
        this.wmsBussinessCodeManager = wmsBussinessCodeManager;
        this.workflowManager = workflowManager;
        this.interfaceLogManager = interfaceLogManager;
    	this.sapRowDataDealManager=sapRowDataDealManager;
    	this.productionOrderManager = productionOrderManager;
    	this.jdbcTemplate=jdbcTemplate;
	 }
	
	@Override
    public void addPoDetail(Long doId, Long podId, WmsDeliveryOrderDetail dod) {
	    
	    if (dod.getTheDeliveryQuantityBu()>dod.getPlanQuantityBu()) {
	        throw new BusinessException("本次交货数量不能大于计划交货数量!");
	    }
	    PurchaseOrderDetail pod = commonDao.load(PurchaseOrderDetail.class, podId);
	    if (dod.getPlanQuantityBu()>pod.getExpectedQty()) {
	        throw new BusinessException("计划交货数量不能大于采购单期待数量!");
	    }
	    if (pod.getExpectedQty()-dod.getDelivedQuantityBu()-dod.getPlanQuantityBu()<0) {
	        throw new BusinessException("该采购单可交货数量不足!【计划数量-已交货数量-分配数量】< 0!");
	    }
	    
	    WmsDeliveryOrder deliveryOrder = commonDao.load(WmsDeliveryOrder.class, doId);
        pod.setAllotQty(dod.getPlanQuantityBu()); //采购明细分配数量=计划交货数量
        pod.getPurchaseOrder().refreshAllotQty();
        
        if (dod.isNew()) {
            dod.setFactory(pod.getPurchaseOrder().getSapFactory());
            dod.setKcdd(pod.getReceivedLoc());
            dod.setPurchaseOrderDetail(pod);
            dod.setDeliveryOrder(deliveryOrder);
            deliveryOrder.addDetail(dod);
        }
        commonDao.store(dod);
    }

    @Override
    public void storeWmsDeliveryOrder(WmsDeliveryOrder wmsDeliveryOrder) {
        if (wmsDeliveryOrder.isNew()) {
            WmsWarehouse wh = (WmsWarehouse)commonDao.findByQueryUniqueResult("FROM WmsWarehouse warehouse WHERE warehouse.baseOrganization.id = :baseOrganizationId", 
                    new String[] {"baseOrganizationId"}, new Object[] {BaseOrganizationHolder.getThornBaseOrganization().getId()});
            wmsDeliveryOrder.setWarehouse(wh);
            String code = wmsBussinessCodeManager.generateCodeByRule(wh, "DL");
            wmsDeliveryOrder.setCode(code);
            wmsDeliveryOrder.setCreatedType(WmsDeliveryOrderCreatedType.HAND);
            wmsDeliveryOrder.setBillTypeName(WmsDeliveryOrderBillType.CGBILLTYPE);
            
            workflowManager.doWorkflow(wmsDeliveryOrder, "wmsDeliveryOrderProcess.new");
        }
    }
    
    public String getMaxLineNo(Map<String, Object> param) {

        Integer lineNo = (Integer) commonDao.findByQueryUniqueResult("SELECT MAX(detail.lineNo) "
                + "FROM WmsDeliveryOrderDetail detail WHERE detail.deliveryOrder.id = :deliveryOrderId",
                        new String[] { "deliveryOrderId" },
                        new Object[] { (Long) param.get("deliveryOrder.id") });
        if (lineNo == null || lineNo.intValue() == 0) {
            lineNo = 1;
        } else {
            lineNo += 1;
        }

        return lineNo.toString();
    }

    public void removeDetails(List<WmsDeliveryOrderDetail> details) {
        WmsDeliveryOrder deliveryOrder = commonDao.load(WmsDeliveryOrder.class, details.get(0).getDeliveryOrder().getId());
        for (WmsDeliveryOrderDetail detail : details) {
            deliveryOrder.removeDetail(detail);
            commonDao.delete(detail);
        }
        
        this.commonDao.store(deliveryOrder);
    }
    
    @Override
    public void confirm(List<WmsDeliveryOrder> dos) {
        for (WmsDeliveryOrder po : dos) {
        	//判断采购订单确认状态
        	this.judgePurchaseOrder(po);
        	
            po.setConfirmTime(new Date());
            po.setConfirmor(UserHolder.getUser().getName());
            po.setConfirmStatus(ConfirmStatus.CONFIRM);
            this.commonDao.store(po);
            if(!StringHelper.isNullOrEmpty(po.getSapCode())){//判断 SAP是否回传给我们SAP单号，有就直接触发反馈给供应商，没有就等SAP把SAP单号回传给我们的时候触发
            	createWms2SapInterfacelog(po,po.getConfirmStatus());
        	}
        }
        
    }
    /**
	 * 创建供应商反馈报文
	 * @param po
	 * @param status
	 */
    public void createWms2SapInterfacelog(WmsDeliveryOrder po,String status) {
    	Wms2SapSupplierDocStatus wsds = new Wms2SapSupplierDocStatus();
    	wsds.setType(Wms2SapSupplierDocType.DELIVERY);
    	wsds.setOrderNo(po.getSapCode());
    	if(ConfirmStatus.CONFIRM.equals(status)){
    		wsds.setStatus(Wms2SapSupplierStatus.CONFIRMED);
    	}else if(ConfirmStatus.RECEIVED.equals(status)){
    		wsds.setStatus(Wms2SapSupplierStatus.VIEWED);
    	}else{
    		wsds.setStatus(Wms2SapSupplierStatus.OPENED);
    	}
    	String xml = XmlObjectConver.toXML(wsds) ;
    	interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_SURPPLIERBUSINESS, Wms2SapInterfaceLogType.SURPPLIERBUSINESSBACK, xml, po.getId(),po.getCode());
	
	}


	@Override
    public void received(List<WmsDeliveryOrder> dos) { 
        for (WmsDeliveryOrder po : dos) {
        	//判断采购订单确认状态
        	this.judgePurchaseOrder(po);
        	
            po.setReceiveTime(new Date());
            po.setReceiver(UserHolder.getUser().getName());
            po.setConfirmStatus(ConfirmStatus.RECEIVED);
            this.commonDao.store(po);
            if(!StringHelper.isNullOrEmpty(po.getSapCode())){//判断 SAP是否回传给我们SAP单号，有就直接触发反馈给供应商，没有就等SAP把SAP单号回传给我们的时候触发
            	createWms2SapInterfacelog(po,po.getConfirmStatus());
        	}
            
        }
        
    }
	
	//判断采购订单是否确认
	public void judgePurchaseOrder(WmsDeliveryOrder order) {
		if (order.getBillTypeName().equals(WmsDeliveryOrderBillType.CGBILLTYPE)) {
			String hql = "SELECT w.purchaseOrderDetail FROM WmsDeliveryOrderDetail w WHERE w.deliveryOrder.id =:Id ";
			// 查找采购订单明细
			List<PurchaseOrderDetail> details = commonDao.findByQuery(hql,"Id", order.getId());
			if (details.isEmpty() || details.size() == 0) {
				throw new BusinessException("根据交货单编码【"+order.getCode()+"】未找到明细！！！");
			}
			for (PurchaseOrderDetail detail : details) {
				if (detail == null) {
					throw new BusinessException("未找到采购订单明细！！！");
				}
				if (detail.getPurchaseOrder() == null) {
					throw new BusinessException("未找到采购订单！！！");
				}
				if (!detail.getPurchaseOrder().getConfirmStatus()
						.equals(ConfirmStatus.CONFIRM)) {
					throw new BusinessException("采购订单未确认，交货单不允许接收或确认！！！！");
				}
			}
		}
	}
	
    
	public void test(){
		//TODO --暂时先不生成交货单
		if(1==1){
			return;
		}
		calculationWmsDeliveryOrder(DateUtil.formatStrToDateYMD("2016-06-01"));
	}
	
	
	
	/**
	 * @author haibin.deng	
	 * @Descriptioin 根据日期查询出未取消且有发货计划的生产订单，算出总库存数量，此总库存数量即为总需求数量。
	 */
//	public void calculationWmsDeliveryOrder(Date startDate){
//		List<Object[]> productionOrderLists = calculationProductionOrderRequirement(startDate); //取生产订单
//		List<Object[]> reservedOrderLists = calculationWmsReservedOrderRequirement();//预留单数量
//		List<Object[]> deliveryOrders = findDeliveryOrders();//取销售交货单-> 暂时不用销售交货单
//		List<Object[]> totalProductionOrderLists = unitListUtils(productionOrderLists, reservedOrderLists,deliveryOrders);
//		StringBuffer sb = new StringBuffer("");
//		Boolean EXCEPTION_FLAG= Boolean.FALSE;
//		for (Object[] listValue  : totalProductionOrderLists) {
//			String itemCode = listValue[0].toString();
//			Date deliverDate =  DateUtil.addDayToDate(DateUtil.formatStrToDateYMD(listValue[3].toString()), -1);//交货日期=工单开始日期-1
//			Double productionCount = Double.valueOf(listValue[1].toString());
//			WmsSapFactory sapFactory = this.commonDao.load(WmsSapFactory.class, Long.valueOf(listValue[2].toString()));
//			List<WmsItem> items = commonDao.findByQuery("from WmsItem where code=:code","code",itemCode);
//			WmsItem item = items.get(0);
//			
//			//取可用库存
//			Double itemInventoryCount = countWmsItemInventoryByFactory(item.getId(), sapFactory.getCode());
//			
//			//取未交货的交货单  在途库存
//			Double deliveryOrderDetailCount =  findWmsDeliveryOrderDetailUnfinishedTaskByItemAndFactory(itemCode, sapFactory.getId());
//			
//			Double demandQuantity = itemInventoryCount + deliveryOrderDetailCount;
//			if (DoubleUtils.compareByPrecision(demandQuantity, productionCount, 3) >= 1) {
//				sb.append(itemCode).append(",");
//				continue;
//			}
//			
//			//取采购订单明细
//			List<PurchaseOrderDetail> purchaseOrderDetailList = findPurchaseOrder(itemCode,sapFactory.getId());
//			if (purchaseOrderDetailList.isEmpty()) {
////		 		String code = wmsBussinessCodeManager.generateCodeByRule(warehouse, WmsWarningType.GEN_DELIVERY);
////		 		wa.setCode(code);
//				String msg = "物料编码"+itemCode+"仓库"+sapFactory.getName()
//						+"("+sapFactory.getCode()+")未找到的可用的采购订单，无法生成交货单";
//				genWarning(WmsWarningType.GEN_DELIVERY, msg);
//				
//				EXCEPTION_FLAG = Boolean.TRUE;
//				continue; 
//			}
//			
//			//需要生成交货单的物料数量
//			Double tmpDemandQuantity = productionCount - demandQuantity;
//			
//			if(!StringHelper.isNullOrEmpty(item.getUserFieldV3()) 
//					&& item.getUserFieldV3().equals(WmsItemUnPackingAtt.WAREHOUSE_UNPACKING)){//判断物料是否可拆包
//				Double minQty = items.get(0).getUserFieldD1() == null ? 0 : items.get(0).getUserFieldD1();
//				if(minQty > 0){
//					Double pack = Math.ceil(tmpDemandQuantity/minQty);//包数--向上取整
//					tmpDemandQuantity = pack * minQty;//需要交货的交货数量必须是最小包装量的整数倍
//				}
//			}
//			
//			for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetailList) {
//				if (tmpDemandQuantity.doubleValue() <= 0D) {
//					break;
//				}
//				Double tmpPodQty = purchaseOrderDetail.getExpectedQty() - purchaseOrderDetail.getAllotQty();
//				
//				WmsFactoryWarehouse fFactoryWarehouse = findWmsFactoryWarehouse(sapFactory.getId(),purchaseOrderDetail.getPstyp());
//				if (null == fFactoryWarehouse) {
//					throw new BusinessException("factory.warehouse.not.found.by.factory", new String[]{sapFactory.getName()});
//				}
//				WmsWarehouse warehouse = fFactoryWarehouse.getWarehouse();
//				
//				
//				WmsDeliveryOrderDetail deliveryOrderDetail = creatWmsDeliveryOrder(deliverDate, purchaseOrderDetail,warehouse);
//				if (tmpDemandQuantity.doubleValue() > tmpPodQty.doubleValue()){
//					deliveryOrderDetail.setPlanQuantityBu(tmpPodQty.doubleValue());
////			 		deliveryOrderDetail.setTheDeliveryQuantityBu(tmpPodQty.doubleValue());
//					purchaseOrderDetail.addAllotQty(tmpPodQty.doubleValue());
//					tmpDemandQuantity -= tmpPodQty.doubleValue();
//				}else {
//					deliveryOrderDetail.setPlanQuantityBu(tmpDemandQuantity);
////			 		deliveryOrderDetail.setTheDeliveryQuantityBu(tmpDemandQuantity);
//					purchaseOrderDetail.addAllotQty(tmpDemandQuantity);
//					tmpDemandQuantity = 0D;
//				}
//				this.commonDao.store(purchaseOrderDetail);
//				purchaseOrderDetail.getPurchaseOrder().refreshAllotQty();
//				this.commonDao.store(deliveryOrderDetail);
//				this.commonDao.store(deliveryOrderDetail.getDeliveryOrder());
//				produceEmailRecord(deliveryOrderDetail.getDeliveryOrder(), deliveryOrderDetail);//发送邮件
//				
//				WmsDeliveryOrder wmsDeliveryOrder = deliveryOrderDetail.getDeliveryOrder();
//				workflowManager.doWorkflow(wmsDeliveryOrder, "wmsDeliveryOrderProcess.active");
//				this.activeDeliveryOrder(wmsDeliveryOrder);
//			}
//		}
//		
//		if(EXCEPTION_FLAG) {
//			LocalizedMessage.addMessage("操作成功，但生成了预警，请查看预警");
//		}
//		else {
//			LocalizedMessage.addMessage("操作成功");
//		}
//	}
	public void calculationWmsDeliveryOrder(Date startDate){
		//TODO --暂时先不生成交货单
		if(1==1){
			return;
		}
//		WmsWarehouse wh = findCurrentWarehouse();//当前仓库
		
		Map<String,Map<String,Map<String,Object>>> all = calculationProductionOrderRequirement(startDate); //取生产订单和预留单
//		List<Object[]> reservedOrderLists = calculationWmsReservedOrderRequirement();//预留单数量
		List<Object[]> deliveryOrders = findDeliveryOrders();//取销售交货单-> 暂时不用销售交货单
//		List<Object[]> totalProductionOrderLists = unitListUtils(productionOrderLists, reservedOrderLists,deliveryOrders);
		StringBuffer sb = new StringBuffer("");
		Boolean EXCEPTION_FLAG= Boolean.FALSE;
		Set<String> key1s = all.keySet(); //工厂+物料
		
		for(String key1 : key1s) {
			String[] values = key1.split(CommonHelper.VTRADEX_SPLIT_STR);
			Map<String,Map<String,Object>> key1info = all.get(key1);
			Long itemId = Long.valueOf(values[0].toString());//物料id
			
			Long facId = Long.valueOf(values[1]);//工厂ID
			WmsSapFactory factory = this.commonDao.load(WmsSapFactory.class, facId);
			
			WmsItem item = commonDao.load(WmsItem.class, itemId);
			String itemCode = item.getCode();
		 	
			//取可用库存
			Double itemInventoryCount = countWmsItemInventoryByFactory(itemId, factory.getCode());
			
			//取未交货的交货单  在途库存
//			Double deliveryOrderDetailCount =  findDeliverOrderNum(itemId, facId); //取的是所有交货日期的在途库存 实际可能在途库存的交货日期在需要的交货日期之后，就错误了
//			//认为的总库存
//			Double demandQuantity = itemInventoryCount + deliveryOrderDetailCount;
//			
			Set<String> key2s = key1info.keySet(); //交货日期
			TreeSet<String> keyes_sort = new TreeSet<String>();
			for(String key2 :  key2s) { //按照小到大排序
				keyes_sort.add(key2);
			}
			Double productionCount =0d; //处理的每一天的累计总计划
			for(String key2 :  keyes_sort) { //key2为交货日期
				Map<String,Object> key2info = key1info.get(key2); // GD_SL/YL_SL
				Double gdsl =null;
				Double ylsl = null;
				if(key2info.get("GD_SL")!=null) {
					gdsl = (Double) key2info.get("GD_SL"); // 此物料 此工厂 在此交货日期下的工单数量
				}
				
				if(key2info.get("YL_SL")!=null) {
					ylsl = (Double) key2info.get("YL_SL");// 此物料 此工厂 在此交货日期下的预留数量
				}
				gdsl = StringHelper.replaceNullToZero(gdsl);
				ylsl = StringHelper.replaceNullToZero(ylsl);
				
				Date deliverDate = DateUtil.formatStrToDateYMD(key2);////交货日期  取工单的时候已经处理了
//				Date deliverDate =  DateUtil.addDayToDate(DateUtil.formatStrToDateYMD(key2), -1);//交货日期=工单开始日期-1
//				Date tomorrow = DateUtil.addDayToDate(DateUtil.getTodayBegin(), 1);
//				if(deliverDate.before(tomorrow)) {//如果交货日期<明天  则设置成明天。
//					deliverDate =tomorrow;
//				}
				Double lsqty = gdsl + ylsl; //本交货日期 工单加预留的总数量；
				
				productionCount = productionCount+lsqty;
				
//				String deliverDateStr = DateUtil.format(deliverDate, "yyyy-MM-dd");
				
				//取未交货的交货单  在途库存  交货日期<=本次交货日期
				Double deliveryOrderDetailCount =  findDeliverOrderNum(itemId, facId, key2); //取的是所有交货日期的在途库存 实际可能在途库存的交货日期在需要的交货日期之后，就错误了 所以需要加上日期去查
//				//认为的总库存
				Double demandQuantity = itemInventoryCount + deliveryOrderDetailCount;
				
				
				if (DoubleUtils.compareByPrecision(demandQuantity, productionCount, 3) >= 0) {  //总库存>=总需求  直接处理下一条
					sb.append(itemCode).append(",");
					continue;
				}
				//取采购订单明细
				List<PurchaseOrderDetail> purchaseOrderDetailList = findPurchaseOrder(itemId,facId);
			 	if (purchaseOrderDetailList.isEmpty()) {
			 		String msg = "物料编码"+itemCode+"仓库"+factory.getName()
			 				+"("+factory.getCode()+")未找到的可用的采购订单，无法生成交货单";
			 		genWarning(WmsWarningType.GEN_DELIVERY, msg);
			 		
			 		EXCEPTION_FLAG = Boolean.TRUE;
			 		continue; 
			 	}
			 	//需要生成交货单的物料数量
				
				productionCount = CommonHelper.dealDoubleError(productionCount);
				demandQuantity = CommonHelper.dealDoubleError(demandQuantity);
				Double tmpDemandQuantity = DoubleUtils.sub(productionCount , demandQuantity);  //需要交货数量= 总计划-总库存
			 	
			 	if(!StringHelper.isNullOrEmpty(item.getUserFieldV3()) 
			 			&& item.getUserFieldV3().equals(WmsItemUnPackingAtt.WAREHOUSE_UNPACKING)){//判断物料是否可拆包
			 		Double minQty = item.getUserFieldD1() == null ? 0 : item.getUserFieldD1();
			 		if(minQty > 0){
			 			Double pack = Math.ceil(tmpDemandQuantity/minQty);//包数--向上取整
				 		tmpDemandQuantity = pack * minQty;//需要交货的交货数量必须是最小包装量的整数倍
			 		}
			 	}
//			 	WmsDeliveryOrder deliveryOrder = createDeliverOrder(deliverDate, purchaseOrderDetail, warehouse);
			 	
			 	for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetailList) {
			 		if (tmpDemandQuantity.doubleValue() <= 0D) {
						break;
					}
			 		Double tmpPodQty = purchaseOrderDetail.getExpectedQty() - purchaseOrderDetail.getAllotQty(); //本张采购订单可生成的交货单最大数量
			 		
					WmsFactoryWarehouse fFactoryWarehouse = findWmsFactoryWarehouse(facId,purchaseOrderDetail.getPstyp());
					if (null == fFactoryWarehouse) {
						throw new BusinessException("factory.warehouse.not.found.by.factory", new String[]{factory.getName()});
					}
					WmsWarehouse warehouse = fFactoryWarehouse.getWarehouse();
			 		
					WmsDeliveryOrderDetail deliveryOrderDetail = creatWmsDeliveryOrder(deliverDate, purchaseOrderDetail,warehouse);
				 	if (tmpDemandQuantity.doubleValue() > tmpPodQty.doubleValue()){
				 		deliveryOrderDetail.setPlanQuantityBu(tmpPodQty.doubleValue());
				 		deliveryOrderDetail.setTheDeliveryQuantityBu(tmpPodQty.doubleValue());//TODO:
				 		purchaseOrderDetail.addAllotQty(tmpPodQty.doubleValue());
				 		tmpDemandQuantity =DoubleUtils.sub(tmpDemandQuantity,  tmpPodQty.doubleValue());
				 	}else {
				 		deliveryOrderDetail.setPlanQuantityBu(tmpDemandQuantity);
				 		deliveryOrderDetail.setTheDeliveryQuantityBu(tmpDemandQuantity);//TODO:
				 		purchaseOrderDetail.addAllotQty(tmpDemandQuantity);
				 		tmpDemandQuantity = 0D;
				 	}
			 		this.commonDao.store(purchaseOrderDetail);
			 		purchaseOrderDetail.getPurchaseOrder().refreshAllotQty();
			 		this.commonDao.store(deliveryOrderDetail);
			 		this.commonDao.store(deliveryOrderDetail.getDeliveryOrder());
			 		produceEmailRecord(deliveryOrderDetail.getDeliveryOrder(), deliveryOrderDetail);//发送邮件
				
			 		WmsDeliveryOrder wmsDeliveryOrder = deliveryOrderDetail.getDeliveryOrder();
			 		workflowManager.doWorkflow(wmsDeliveryOrder, "wmsDeliveryOrderProcess.active");
			 		this.activeDeliveryOrder(wmsDeliveryOrder);
			 	}
			}	
		}
		if(EXCEPTION_FLAG) {
			LocalizedMessage.addMessage("操作成功，但生成了预警，请查看预警");
		}
		else {
			LocalizedMessage.addMessage("操作成功");
		}
	}
	
	private List<Object[]> unitListUtils(List<Object[]> productionOrderLists, 
				List<Object[]> reservedOrderLists,List<Object[]> deliveryOrders){
		List<Object[]> returnValueList = new ArrayList<Object[]>();
		productionOrderLists.addAll(reservedOrderLists);
		productionOrderLists.addAll(deliveryOrders);
		for (Object[] objects : productionOrderLists) {
			addRequestNumberList(returnValueList, objects);
		}
		return returnValueList;
	}
	
	private void addRequestNumberList(List<Object[]> returnValueList , Object[] desObj){
		Boolean flag = Boolean.FALSE;
		//tmpObj[0]=物料编码	 tmpObj[1]=数量	tmpObj[2]=工厂	tmpObj[3]=交货日期
		for (Object[] tmpObj : returnValueList) {
			int days = DateUtil.getMargin(tmpObj[3].toString(), desObj[3].toString());//日期相差的天数
			if (String.valueOf(tmpObj[0].toString()).equals(String.valueOf(desObj[0].toString())) && 
					Long.valueOf(tmpObj[2].toString()) == Long.valueOf(desObj[2].toString())
					&& days == 0) {//内容相同
				Double tmpNumber = Double.valueOf(tmpObj[1].toString()) + Double.valueOf(desObj[1].toString());
				tmpObj[1] = tmpNumber;
				flag =  Boolean.TRUE;
			}
		}
		if (flag == Boolean.FALSE) {
			returnValueList.add(desObj);
		}
	}
	
	private WmsDeliveryOrderDetail creatWmsDeliveryOrder(Date startDate,PurchaseOrderDetail purchaseOrderDetail,WmsWarehouse warehouse){
		
		WmsDeliveryOrder deliveryOrder = EntityFactory.getEntity(WmsDeliveryOrder.class);
		deliveryOrder.setWarehouse(warehouse);
		String code = wmsBussinessCodeManager.generateCodeByRule(warehouse, "DL");
		deliveryOrder.setCode(code);
		deliveryOrder.setDeliveryDate(startDate);
//		deliveryOrder.setDeliveryDate(startDate);
		deliveryOrder.setCreatedType(WmsDeliveryOrderCreatedType.SYSTEM);
		
		deliveryOrder.setBillTypeName(WmsDeliveryOrderBillType.CGBILLTYPE);
		deliveryOrder.setProject("000010");
		
		deliveryOrder.setSupplier(purchaseOrderDetail.getPurchaseOrder().getSupplier());
		workflowManager.doWorkflow(deliveryOrder, "wmsDeliveryOrderProcess.new");
		
		WmsDeliveryOrderDetail deliveryOrderDetail = EntityFactory.getEntity(WmsDeliveryOrderDetail.class);
		deliveryOrderDetail.setDeliveryOrder(deliveryOrder);
		deliveryOrderDetail.setPurchaseOrderDetail(purchaseOrderDetail);
		deliveryOrderDetail.setPoNo(purchaseOrderDetail.getPurchaseOrder().getCode());
		deliveryOrderDetail.setPoDetailNo(purchaseOrderDetail.getEbelp());
		deliveryOrderDetail.setLineNo(purchaseOrderDetail.getLineNo());
		deliveryOrderDetail.setItem(purchaseOrderDetail.getItem());
		deliveryOrderDetail.setPosnr("000010");
		deliveryOrderDetail.setPackageUnit(purchaseOrderDetail.getPackageUnit());
		deliveryOrderDetail.setFactory((purchaseOrderDetail.getPurchaseOrder().getSapFactory()));
		
		return deliveryOrderDetail;
	}
//	private WmsDeliveryOrder createDeliverOrder(Date startDate,PurchaseOrderDetail purchaseOrderDetail,WmsWarehouse warehouse){
//		WmsDeliveryOrder deliveryOrder = EntityFactory.getEntity(WmsDeliveryOrder.class);
//		deliveryOrder.setWarehouse(warehouse);
//		String code = wmsBussinessCodeManager.generateCodeByRule(warehouse, "DL");
//		deliveryOrder.setCode(code);
//		deliveryOrder.setDeliveryDate(startDate);
////		deliveryOrder.setDeliveryDate(startDate);
//		deliveryOrder.setDeliveryDate(new Date());
//		deliveryOrder.setCreatedType(WmsDeliveryOrderCreatedType.SYSTEM);
//		
//		deliveryOrder.setBillTypeName(WmsDeliveryOrderBillType.CGBILLTYPE);
//		deliveryOrder.setProject("000010");
//		
//	 	deliveryOrder.setSupplier(purchaseOrderDetail.getPurchaseOrder().getSupplier());
//	 	workflowManager.doWorkflow(deliveryOrder, "wmsDeliveryOrderProcess.new");
//	 	return deliveryOrder;
//	}
//	
//	private WmsDeliveryOrderDetail creatWmsDeliveryOrder(WmsDeliveryOrder deliveryOrder,PurchaseOrderDetail purchaseOrderDetail){
//
//	 	WmsDeliveryOrderDetail deliveryOrderDetail = EntityFactory.getEntity(WmsDeliveryOrderDetail.class);
//	 	deliveryOrderDetail.setDeliveryOrder(deliveryOrder);
//	 	deliveryOrderDetail.setPurchaseOrderDetail(purchaseOrderDetail);
//	 	deliveryOrderDetail.setPoNo(purchaseOrderDetail.getPurchaseOrder().getCode());
//	 	deliveryOrderDetail.setPoDetailNo(purchaseOrderDetail.getEbelp());
//	 	deliveryOrderDetail.setLineNo(purchaseOrderDetail.getLineNo());
//	 	deliveryOrderDetail.setItem(purchaseOrderDetail.getItem());
//	 	deliveryOrderDetail.setPosnr("000010");
//	 	deliveryOrderDetail.setPackageUnit(purchaseOrderDetail.getPackageUnit());
//	 	deliveryOrderDetail.setFactory((purchaseOrderDetail.getPurchaseOrder().getSapFactory()));
//	 	
//	 	return deliveryOrderDetail;
//	}
	
	
	/**
	 * 
	 * @param srcMap
	 * @param addMap
	 */
	public void mapApendUtils(Map<String,Double> srcMap , Map<String,Double> addMap){

		for (Map.Entry<String,Double> addEntiry : addMap.entrySet()) {
			if (srcMap.containsKey(addEntiry.getKey())) {
				srcMap.put(addEntiry.getKey(),DoubleUtils.add(srcMap.get(addEntiry.getKey()), addEntiry.getValue()));
			} else {
				srcMap.put(addEntiry.getKey(), addEntiry.getValue());
			}
		}
	}
	
	public void produceEmailRecord(WmsDeliveryOrder deliveryOrder,WmsDeliveryOrderDetail deliveryOrderDetail){
//		EmailRecord emailRecord = EntityFactory.getEntity(EmailRecord.class);
//		emailRecord.setReceiver(deliveryOrder.getSupplier().getContact().getContactName());
//		emailRecord.setEmailBox(deliveryOrder.getSupplier().getContact().getEmail());
//		emailRecord.setTheme("零件配送");
//		StringBuffer sb = new StringBuffer("请于: ").append(DateUtil.formatDateYMDToStr(deliveryOrder.getDeliveryDate())).
//				append("配送货品编码: ").append(deliveryOrderDetail.getItem().getCode()).append("到")
//				 .append(deliveryOrderDetail.getFactory().getName());
//		emailRecord.setContent(sb.toString());
//		emailRecord.setStatus(InterfaceLogStatus.STAT_READY);
//		this.commonDao.store(emailRecord);
//		
		
		String subject =EmailHelper.getTheme(EmailRecordType.DELIVERY2SUPPLIER, deliveryOrder.getCode());
		List<String> infos = new ArrayList<String>();
		infos.add(deliveryOrder.getCode());
		String content = EmailHelper.getEmailContent(EmailRecordType.DELIVERY2SUPPLIER, infos);
		MidSurpplierUser msu = (MidSurpplierUser)commonDao.findByQueryUniqueResult("FROM MidSurpplierUser msu where msu.sid=:ms", "ms", deliveryOrder.getSupplier().getId());
		if(null!=msu){
			ThornUser user = commonDao.load(ThornUser.class, msu.getUid());
			//每个邮箱发邮件
			String emails = user.getEmail();
			if(null == emails){
				return;
			}
			String [] ems = emails.split("\\|");
			for (String em : ems) {
				EmailRecordManager emailRecordManager = (EmailRecordManager)applicationContext.getBean("emailRecordManager");
				emailRecordManager.storeEmailRecordWaitSend(user.getLoginName(),em,subject,content,EmailHelper.getEmailCc(),EmailRecordType.DELIVERY2SUPPLIER, deliveryOrder.getCode());
			}
		}
		
	}
	
	
	public WmsItem findWmsItem(String code){
		String hql = " FROM WmsItem item WHERE item.code =:code AND item.status =:status";
		return (WmsItem) this.commonDao.findByQueryUniqueResult(hql, new String[]{"code","status"}, new Object[]{code,BaseStatus.ENABLED});
	}
	
	@SuppressWarnings("unchecked")
	private List<PurchaseOrderDetail> findPurchaseOrder(Long itemId, Long factoryId){
		String hql = " select pod FROM PurchaseOrderDetail pod WHERE pod.item.id =:id "
				+ " AND pod.allotQty < pod.expectedQty "
				+ " AND pod.purchaseOrder.sapFactory.id =:factoryId "
				+ " AND (pod.userField1 not like 'W%' or pod.userField1 is null)"//库位不等于w开头
//				+ " ORDER BY pod.purchaseOrder.creatDate ASC ,(pod.expectedQty - pod.allotQty) DESC";
				+ " ORDER BY pod.receivedDate ASC ,(pod.expectedQty - pod.allotQty) DESC"; //根据交货日期排序
		return  this.commonDao.findByQuery(hql, new String[]{"id","factoryId"}, new Object[]{itemId,factoryId});
	}
	
	
	/**
	 * @Descriptioin 库存转换 
	 * @param listValue
	 * @return
	 */
	private Map<String,Map<Long,Double>> convertListToMap(List<Object[]> listValue){
		
		Map<String,Map<Long,Double>> value = new HashMap<String, Map<Long,Double>>();
		if (null == listValue || listValue.isEmpty()) {
			return value;
		}
		
		for (Object[] list : listValue) {
			
			if (value.containsKey(list[0])) {
				Map<Long,Double> tmpFactoryMap = value.get(list[0]);
				 if (tmpFactoryMap.containsKey(list[2])) {
					 tmpFactoryMap.put(Long.valueOf(list[2].toString()), DoubleUtils.add(tmpFactoryMap.get(list[2]), 
							 Double.valueOf(list[1].toString())));
				 } else {
					 tmpFactoryMap.put(Long.valueOf(list[2].toString()), Double.valueOf(list[1].toString()));
				 }
			} else {
				Map<Long,Double> tmpMap = new HashMap<Long, Double>();
				 tmpMap.put(Long.valueOf(list[2].toString()), Double.valueOf(list[1].toString()));
				 value.put(list[0].toString(), tmpMap);
			}
		}
		
		return value;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,Map<String,Map<String,Object>>> calculationProductionOrderRequirement(Date startDate){
		String hql = "SELECT pod.item.id, SUM(pod.planQuantityBu-pod.allocatedQuantityBu),"
				+ "pod.productionOrder.factory.id,pod.productionOrder.beginDate "
				+ "  FROM ProductionOrderDetail pod WHERE 1 = 1 "
				+ " AND pod.productionOrder.status =:status "
				+ " AND pod.productionOrder.updateInfo.createdTime >=:createdTime "
				+ " AND (pod.planQuantityBu- pod.allocatedQuantityBu) > 0 "
				+ " AND pod.productionOrder.beginDate <= trunc(sysdate+8) "
				+ " GROUP BY pod.item.id, pod.productionOrder.factory.id,pod.productionOrder.beginDate";
		
		List<Object[]> gd = commonDao.findByQuery(hql, new String[]{"status","createdTime"}, new Object[]{
				ProductionOrderStatus.ACTIVE,DateUtil.formatStrToDateYMD(DateUtil.formatDateYMDToStr(startDate))});
		
		hql = "SELECT rod.item.id,SUM(rod.quantity-rod.allocatedQuantityBu), "
				+ "rod.factory.id,rod.reservedOrder.jzrq FROM WmsReservedOrderDetail rod WHERE 1 = 1 "
				+ " AND rod.reservedOrder.status in(:status) "
				+ " AND rod.reservedOrder.ydlx in (:ydlx)"
				+ " AND rod.quantity >0 "
				+ " AND rod.reservedOrder.jzrq <= trunc(sysdate+8) "
				+ " GROUP BY rod.item.id, rod.factory.id,rod.reservedOrder.jzrq ";
		List<Object[]> yld = commonDao.findByQuery(hql, new String[]{"status","ydlx"}, new Object[]{
				Arrays.asList(ProductionOrderStatus.OPEN,ProductionOrderStatus.ACTIVE),
				Arrays.asList(WmsReservedOrderType.Z01,WmsReservedOrderType.Z03,WmsReservedOrderType.Z311)});
		
		Map<String,Map<String,Map<String,Object>>> result = new HashMap<String,Map<String,Map<String,Object>>>() ;
		for(Object[] obj : gd) {
			 String itemId = obj[0].toString(); //物料id
			 Double qty = (Double)obj[1]; //数量
			 Long fac_id = (Long)obj[2];//工厂id
			 Date planDate = (Date)obj[3];//计划日期
			 
			 //交货日期=计划日期-1天  但是最小的交货日期等于明天
			 Date deliverD = DateUtil.addDayToDate(planDate, -1);;//交货日期
					 
			 Date tomorrow = DateUtil.addDayToDate(DateUtil.getTodayBegin(), 1);
			 if(deliverD.before(tomorrow)) {//如果交货日期<明天  则设置成明天。
				deliverD =tomorrow;
			 }
			 String deliverDate = DateUtil.formatDateYMDToStr(deliverD);
			 
			 String key1 = itemId+CommonHelper.VTRADEX_SPLIT_STR+fac_id;  //工厂 + 物料
			 if(result.get(key1)==null) {
				 result.put(key1, new HashMap<String,Map<String,Object>>());
			 }
			 Map<String,Map<String,Object>> info2 = result.get(key1);
			 
			 String key2 = deliverDate;// 交货日期
			 if(info2.get(key2)==null) {
				 info2.put(key2, new HashMap<String,Object>());
			 }
			 Map<String,Object> info3 = info2.get(key2);
			 String key3 = "GD_SL";
			 if(!info3.containsKey(key3)) {
				 info3.put(key3, 0D);
			 }
			 Double old = (Double)info3.get(key3);
			 info3.put(key3,old+qty);
			 
		 }
		
		for(Object[] obj : yld) {
			 String itemId = obj[0].toString(); //物料id
			 Double qty = (Double)obj[1];
			 Long fac_id = (Long)obj[2];
			 Date planDate = (Date)obj[3];//计划日期
			 
			//交货日期=计划日期-1天  但是最小的交货日期等于明天
			 Date deliverD = DateUtil.addDayToDate(planDate, -1);;//交货日期
					 
			 Date tomorrow = DateUtil.addDayToDate(DateUtil.getTodayBegin(), 1);
			 if(deliverD.before(tomorrow)) {//如果交货日期<明天  则设置成明天。
				deliverD =tomorrow;
			 }
			 String deliverDate = DateUtil.formatDateYMDToStr(deliverD);
			 
			 String key1 = itemId+CommonHelper.VTRADEX_SPLIT_STR+fac_id;  //工厂 + 物料
			 if(result.get(key1)==null) {
				 result.put(key1, new HashMap<String,Map<String,Object>>());
			 }
			 Map<String,Map<String,Object>> info2 = result.get(key1);
			 
			 String key2 = deliverDate;// 交货日期
			 if(info2.get(key2)==null) {
				 info2.put(key2, new HashMap<String,Object>());
			 }
			 Map<String,Object> info3 = info2.get(key2);
			 String key3 = "YL_SL";
			 if(!info3.containsKey(key3)) {
				 info3.put(key3, 0D);
			 }
			 Double old = (Double)info3.get(key3);
			 info3.put(key3,old+qty);
			
		 }
		
		return result;
	}
	
	private List<Object[]> calculationWmsReservedOrderRequirement(){
		String hql = "SELECT rod.item.code,SUM(rod.quantity-rod.allocatedQuantityBu), "
						+ "rod.factory.id,rod.reservedOrder.jzrq FROM WmsReservedOrderDetail rod WHERE 1 = 1 "
						+ " AND rod.reservedOrder.status in(:status) "
						+ " AND rod.reservedOrder.ydlx in (:ydlx)"
						+ " AND rod.quantity >0 "
						+ " GROUP BY rod.item.code , rod.factory.id,rod.reservedOrder.jzrq ";
		return this.commonDao.findByQuery(hql, new String[]{"status","ydlx"}, new Object[]{
				Arrays.asList(ProductionOrderStatus.OPEN,ProductionOrderStatus.ACTIVE),
				Arrays.asList(WmsReservedOrderType.Z01,WmsReservedOrderType.Z03,WmsReservedOrderType.Z311)
		});
	}
	/**查销售交货单*/
	private List<Object[]> findDeliveryOrders(){
		String hql = "select d.item.code,sum(d.planQuantityBu-d.delivedQuantityBu),d.factory.id "
				+ "from WmsDeliveryOrderDetail d where d.deliveryOrder.billTypeName = :billTypeName "
				+ "AND (d.planQuantityBu-d.delivedQuantityBu) > 0 "
				+ "AND d.deliveryOrder.status <> :status and 1=2"//and 1=2暂时不用销售交货单
				+ "group by d.item.code,d.factory.id ";
		return this.commonDao.findByQuery(hql,
				new String[]{"billTypeName","status"},
				new Object[]{WmsDeliveryOrderBillType.XSBILLTYPE,WmsDeliveryOrderStatus.FINISH});
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> countWmsItemInventory(){
		String hql = "SELECT inventory.item.code, SUM(inventory.qty) FROM WmsInventory inventory WHERE (inventory.location.type=:type or inventory.location.type=:type2) "
				+ " AND inventory.location.countLock =:countLock "
				+ " AND inventory.status =:status "
				+ " AND inventory.operationStatus =:operationStatus "
				+ " AND inventory.qty > 0 "
				+ " GROUP BY inventory.item.code ";
		return this.commonDao.findByQuery(hql, new String[]{"type","type2","countLock","status","operationStatus"}, new Object[]{WmsLocationType.RECEIVE,
				WmsLocationType.STORAGE,Boolean.FALSE,"不良品",WmsInventoryOperationStatus.NORMAL});
	}
	/***查询可用库存*/
	public List<Map<String,Object>>  countWmsInventoryGroupByItemIdFactoryCode() {
		
		String sql = "select i.id as itemid, "  
					+ " ik.extend_propc10 as factorycode," 
					+ " round(nvl(sum(inv.qty),0),3) as qty " 
					+ " from wms_inventory inv inner join wms_location loc on inv.location_id = loc.id " 
					+ " inner join wms_item i on i.id=inv.item_id " 
					+ " inner join wms_item_key ik on ik.id = inv.item_key_id " 
					+ " where 1=1 and inv.qty>0  and inv.status<>? " 
					+ " and ( " 
					+ "       (loc.type = 'RECEIVE' and  inv.operation_status='NORMAL') "  // -- 发货仓 待出
					+ "    or (loc.type = 'STORAGE' and  inv.operation_status in ('NORMAL','READY_IN') ) "  // --X111 待出
					+ "    or (loc.code = 'XBC' and  inv.operation_status='NORMAL') "  
					+ " ) "  //
					+ " GROUP BY i.id,ik.extend_propc10  ";
		List<Map<String,Object>> countValue = jdbcTemplate.queryForList(sql, new Object[]{ "不良品" });
		return countValue;
	}
	
	private Double countWmsItemInventoryByFactory(Long itemId ,String factoryCode){
		String hql = "SELECT SUM(inventory.qty) FROM WmsInventory inventory "
				+ " WHERE " + findInventory() //存货位的正常和待入+收货位的正常库存  状态不等于不良品
				+ " AND inventory.location.countLock =:countLock "  
				+ " AND inventory.status <> :status "
				+ " AND inventory.item.id =:itemId "
				+ " AND inventory.itemKey.lotInfo.extendPropC10 =:factoryCode "
//				+ " AND inventory.lockStatus = :lockStatus "//收货仓的现在是锁定状态 所以不能控制  xuyan.xia 2017-11-01 18:24:21
				+ " AND inventory.qty > 0 ";
		Double countValue = (Double) this.commonDao.findByQueryUniqueResult(hql, 
				new String[]{"countLock","status","itemId","factoryCode"
//						,"lockStatus"
						}, 
				new Object[]{Boolean.FALSE,"不良品",itemId,factoryCode
//						,Boolean.FALSE
						});
		if (null == countValue) {
			return 0D;
		} else {
			return countValue;
		}
	}
	/**查询标准和寄售库存*/
	private List countInventoryByXmlb(Long itemId ,String factoryCode){
		String sql = "select t.iid as itemid,t.gc as faccode, nvl(sum(t.bz),0) as bzkc,nvl(sum(t.js),0) as jskc from ( "
				+ "select "
				+ "(case when k.extend_propc8='0' then inv.qty else 0 end) as bz,"
				+ "(case when k.extend_propc8='2' then inv.qty else 0 end) as js ,"
				+ " item.id as iid, "
				+ " k.extend_propc10 as gc "
				+ "from  wms_inventory inv "
				+ "left join wms_item_key k on inv.item_key_id=k.id "
				+ "left join wms_location loc on inv.location_id=loc.id "
				+ "left join wms_item item on item.id = inv.item_id "
				+ "where "
				+ "inv.status<>'不良品' "
				+ "and "
				+ "( "//inventoryCondition()注释在这个方法里
				+ "(loc.type='RECEIVE' and inv.operation_status='NORMAL') "
				+ "OR "
				+ "(loc.type='STORAGE' and inv.operation_status in ('NORMAL','READY_IN')) "
				+ "or "
				+ "(loc.type='HANDOVER') "
				+ "or "
				+ "(loc.type='SHIP') "
				+ ") "
	//			+ "and k.extend_propc10=? "
				+ "and inv.lock_status='N' "
				+ "and loc.code<>? "
	//			+ "and item.id=? "
				+ "and inv.qty>0 ) t "
				+ "  group by t.iid,t.gc ";
		List countValue = jdbcTemplate.queryForList(sql, new Object[]{"ZDC"});
		return countValue;
//		String sql = "select nvl(sum(t.bz),0) as bzkc,nvl(sum(t.js),0) as jskc from ( "
//				+ "select "
//				+ "(case when k.extend_propc8='0' then inv.qty else 0 end) as bz,"
//				+ "(case when k.extend_propc8='2' then inv.qty else 0 end) as js "
//				+ "from  wms_inventory inv "
//				+ "left join wms_item_key k on inv.item_key_id=k.id "
//				+ "left join wms_location loc on inv.location_id=loc.id "
//				+ "left join wms_item item on item.id = inv.item_id "
//				+ "where "
//				+ "inv.status<>'不良品' "
//				+ "and "
//				+ "( "//inventoryCondition()注释在这个方法里
//				+ "(loc.type='RECEIVE' and inv.operation_status='NORMAL') "
//				+ "OR "
//				+ "(loc.type='STORAGE' and inv.operation_status in ('NORMAL','READY_IN')) "
//				+ "or "
//				+ "(loc.type='HANDOVER') "
//				+ "or "
//				+ "(loc.type='SHIP') "
//				+ ") "
//				+ "and k.extend_propc10=? "
//				+ "and inv.lock_status='N' "
//				+ "and loc.code<>? "
//				+ "and item.id=? "
//				+ "and inv.qty>0 ) t ";
//		List countValue = jdbcTemplate.queryForList(sql, new Object[]{factoryCode,"ZDC",itemId});
//		return countValue;
	}
	
	public List<Object[]> findWmsDeliveryOrderDetailUnfinishedTask(){
		String hql = "SELECT dod.item.code, SUM(dod.planQuantityBu - dod.delivedQuantityBu)  FROM  WmsDeliveryOrderDetail dod WHERE 1 =1 "
				+ " AND (dod.planQuantityBu - dod.delivedQuantityBu) >0 "
				+ " GROUP BY dod.item.code ";
		return this.commonDao.findByQuery(hql);
	}
	
	private Double findDeliverOrderNum(Long itemId ,Long factoryId,String deliveryDate){
		String hql = "SELECT SUM(dod.planQuantityBu - dod.delivedQuantityBu)  FROM  WmsDeliveryOrderDetail dod WHERE dod.item.id =:itemId "
				+ " AND dod.factory.id =:factoryId AND (dod.planQuantityBu - dod.delivedQuantityBu) >0 "
				+ " AND dod.deliveryOrder.billTypeName=:billTypeName "
				+ " AND (dod.kcdd not like 'W%' or dod.kcdd is null)"//库位不等于w开头
				+ " AND to_char(dod.deliveryOrder.deliveryDate,'yyyy-mm-dd')<=:deliveryDate ";//w
		Double returnValue = (Double)this.commonDao.findByQueryUniqueResult(hql, 
				new String[]{"itemId","factoryId","billTypeName","deliveryDate"}, 
				new Object[]{itemId,factoryId,WmsDeliveryOrderBillType.CGBILLTYPE,deliveryDate});
		if (null == returnValue) {
			return 0D;
		} else {
			return returnValue;
		}
	}
	/**查交货数量和*/
	private Double findDeliveryQtyByItemAndFactory(Long itemId ,Long factoryId){
		String hql = "SELECT SUM(dod.theDeliveryQuantityBu)  FROM  WmsDeliveryOrderDetail dod "
				+ " WHERE dod.item.id =:itemId AND dod.factory.id =:factoryId"
				+ " AND (dod.planQuantityBu - dod.delivedQuantityBu) >0 ";
		Double deliverQty = (Double)this.commonDao.findByQueryUniqueResult(hql, 
				new String[]{"itemId","factoryId"}, 
				new Object[]{itemId,factoryId});
		if (null == deliverQty) {
			return 0D;
		} else {
			return deliverQty;
		}
	}
	
	private List<WmsDeliveryOrderDetail> findDeOrderDetailByItemAndFac(Long itemId ,Long factoryId){
		String hql = " FROM  WmsDeliveryOrderDetail dod WHERE dod.item.id =:itemId "
				+ " AND dod.factory.id =:factoryId"
				+ " AND (dod.planQuantityBu - dod.delivedQuantityBu) >0 "
				+ " AND dod.deliveryOrder.billTypeName=:billTypeName "
				+ " AND (dod.kcdd not like 'W%' or dod.kcdd is null)"
				+ " ORDER BY dod.deliveryOrder.id ASC,dod.id ASC ";
		return this.commonDao.findByQuery(hql, 
				new String[]{"itemId","factoryId","billTypeName"},
				new Object[]{itemId,factoryId,WmsDeliveryOrderBillType.CGBILLTYPE});
		
	}
	
	private WmsFactoryWarehouse findWmsFactoryWarehouse(Long factoryId,String xmlb){
		String hql = "  FROM WmsFactoryWarehouse fw WHERE fw.factory.id =:factoryId "
						+ " AND fw.type =:type";
		return  (WmsFactoryWarehouse) this.commonDao.findByQueryUniqueResult(hql, new String[]{"factoryId","type"}, new Object[]{factoryId,xmlb});
		
	}

	
	public void activeDeliveryOrder(WmsDeliveryOrder wmsDeliveryOrder){
		
		/**单据名称=销售交货单的交货单 生效 生成拣货单*/
		if(null != wmsDeliveryOrder.getBillTypeName() 
				&& wmsDeliveryOrder.getBillTypeName().equals(WmsDeliveryOrderBillType.XSBILLTYPE)){
			Long facId = null;
			for(WmsDeliveryOrderDetail detail : wmsDeliveryOrder.getDetails()){
				if(null != facId && detail.getFactory().getId() != facId){
					throw new BusinessException("此交货单明细工厂不一致,请检查!!");
				}else{
					facId = detail.getFactory().getId();
				}
				if(StringHelper.isNullOrEmpty(detail.getKcdd())){
					throw new BusinessException("销售交货单库存地点不能为空");
				}
			}
			WmsFactoryWarehouse fFactoryWarehouse = findWmsFactoryWarehouse(facId,WmsFactoryXmlb.BZ);
			
			/**创建拣货单*/
			createPtByDeliveryOrder(wmsDeliveryOrder, fFactoryWarehouse, facId);
		}else{
			if(WmsDeliveryOrderCreatedType.SYSTEM.equals(wmsDeliveryOrder.getCreatedType())){
				interfaceLogManager.createWms2SapDeliveryOrder(wmsDeliveryOrder.getId());
			}
		}
	}
	/**创建拣货单*/	
	private void createPtByDeliveryOrder(WmsDeliveryOrder wmsDeliveryOrder,
			WmsFactoryWarehouse fFactoryWarehouse,Long facId){
		WmsPickTicket pickTicket = productionOrderManager.createWmsPickTicketByProductionOrder(fFactoryWarehouse.getWarehouse(), wmsDeliveryOrder.getDeliveryDate(),WmsPickticketGenType.XSJHD);
		WmsSapFactory fac = commonDao.load(WmsSapFactory.class, facId);
		pickTicket.setUserField1(fac.getCode()); //工厂编码
		pickTicket.setUserField2(fac.getName());//工厂名称
		
		for (WmsDeliveryOrderDetail d : wmsDeliveryOrder.getDetails()) {
			if(d.getPlanQuantityBu()<=0){
				break;
			}
			WmsPickTicketDetail pickTicketDetail = productionOrderManager.creatWmsPickTicketDetail(pickTicket, d.getItem(), d.getPackageUnit(),d.getPlanQuantityBu());
			pickTicketDetail.setExpectedPackQty(d.getPlanQuantityBu());
			/**保存拣货单明细和交货单明细对应表*/
			DeliveryOrderDetailPtDetail dod = 
					new DeliveryOrderDetailPtDetail(d, pickTicketDetail, d.getPlanQuantityBu(), d.getPackageUnit());
			commonDao.store(dod);
			addLotInfo(pickTicketDetail);
		}
		pickTicket.setRelatedBill1(wmsDeliveryOrder.getCode());
		pickTicket.setUserField3(WmsPickticketGenType.XSJHD);//来源
		
		pickTicket.setAllowShortShip(Boolean.TRUE);//允许短缺发运
		this.commonDao.store(pickTicket);
	}
	/**添加项目类别=标准的批次信息*/
	private void addLotInfo(WmsPickTicketDetail pickTicketDetail){
		WmsPickTicketDetailRequire ptdr3 = EntityFactory.getEntity(WmsPickTicketDetailRequire.class);
		ptdr3.setPickTicketDetail(pickTicketDetail);
		ptdr3.setLotKey("EXTEND_PROPC8"); //批次属性
		ptdr3.setLotValue1(WmsFactoryXmlb.BZ); //项目类别
		ptdr3.setQueryRequire(WmsSOQueryRequireType.E); //等于
		ptdr3.setLevel(WmsLotCategoryType.FORCEMATCHED);//等级
		ptdr3.setAllowModified(false); //不允许修改
		commonDao.store(ptdr3);
	}
    public void importProductFile(File file) {
		
		//解析.xls文件
		List<SapProductOrder> orders = readProductFromExcel(file);
		
		SapProductOrderArray spoa = new SapProductOrderArray();
		spoa.setSpos(orders.toArray(new SapProductOrder[]{}));
		spoa.setTYPE("I");
		spoa.setMESSAGEID(new Date().toLocaleString());
		spoa.setROWCNT(orders.size()+"");
		
		sapRowDataDealManager.dealSapProductOrder(spoa);
	}
	
	//解析.xls
	private List<SapProductOrder> readProductFromExcel(File file){
		
		List<SapProductOrder> orders=new ArrayList<SapProductOrder>();
		
		List<Map<String, Object>> infos=ExcelHelper.parseExcel2List(file);
		for (Map<String, Object> rowDataMap : infos) {
			
			
			String AUFNR = (String)rowDataMap.get("生产订单号");
			if(StringHelper.isNullOrEmpty(AUFNR)){
				throw new BusinessException("行号"+rowDataMap.get("EXCEL行号")+"生产订单号不能为空");
			}
			AUFNR = CommonHelper.addCharBeforeStr(AUFNR, 12, "0");
			
			String WERKS = (String)rowDataMap.get("工厂");
			if(StringHelper.isNullOrEmpty(WERKS)){
				throw new BusinessException("行号"+rowDataMap.get("EXCEL行号")+"工厂不能为空");
			}
			String MATNR = (String)rowDataMap.get("成品编码");
			
			String AUART = (String)rowDataMap.get("订单类型");
			String GLTRP = (String)rowDataMap.get("基本完成日期");
			if(StringHelper.isNullOrEmpty(GLTRP)){
				throw new BusinessException("行号"+rowDataMap.get("EXCEL行号")+"基本完成日期不能为空");
			}
			if(GLTRP.length() - 8 != 0){
    			throw new BusinessException("行号"+rowDataMap.get("EXCEL行号")+"基本完成日期【"+GLTRP+"】格式有误,请检查");
    		}
			//判断字符串 是否是数字
			try{
			   int s = Integer.valueOf(GLTRP);
			}catch(Exception e){
				throw new BusinessException("行号"+rowDataMap.get("EXCEL行号")+"基本完成日期【"+GLTRP+"】格式有误,请检查");
			}
			
			String GSTRP = (String)rowDataMap.get("基本开始日期");
			if(StringHelper.isNullOrEmpty(GSTRP)){
				throw new BusinessException("行号"+rowDataMap.get("EXCEL行号")+"基本开始日期不能为空");
			}
			if(GSTRP.length() - 8 != 0){
    			throw new BusinessException("行号"+rowDataMap.get("EXCEL行号")+"基本开始日期【"+GSTRP+"】格式有误,请检查");
    		}
			//判断字符串 是否是数字
			try {
				int s = Integer.valueOf(GSTRP);
			} catch (Exception e) {
				throw new BusinessException("行号" + rowDataMap.get("EXCEL行号")+ "基本完成日期【" + GSTRP + "】格式有误,请检查");
			}
				
			String GAMNG = (String)rowDataMap.get("计划数量");
			if(StringHelper.isNullOrEmpty(GAMNG)){
				throw new BusinessException("行号"+rowDataMap.get("EXCEL行号")+"计划数量不能为空");
			}
			String MAKTX = (String)rowDataMap.get("备注");
			String POSNR = (String)rowDataMap.get("组件项目号");
			if(StringHelper.isNullOrEmpty(POSNR)){
				throw new BusinessException("行号"+rowDataMap.get("EXCEL行号")+"组件项目号不能为空");
			}
			
			String MATNR1 = (String)rowDataMap.get("物料编码");
			if(StringHelper.isNullOrEmpty(MATNR1)){
				throw new BusinessException("行号"+rowDataMap.get("EXCEL行号")+"物料编码不能为空");
			}
			
			String BDMNG = (String)rowDataMap.get("需求数量");
			if(StringHelper.isNullOrEmpty(BDMNG)){
				throw new BusinessException("行号"+rowDataMap.get("EXCEL行号")+"需求数量不能为空");
			}
			
			String MAKTX1 = (String)rowDataMap.get("物料描述");
			String MTART = (String)rowDataMap.get("物料类型");
			String XLOEK = (String)rowDataMap.get("删除标识");
			String KZEAR = (String)rowDataMap.get("最后的发货");
			String ENMNG = (String)rowDataMap.get("提货数量");
			String RSNUM = (String)rowDataMap.get("预留号");
			String BWART = (String)rowDataMap.get("移动类型");
			String MEINS = (String)rowDataMap.get("成品单位");
			String MEINS1 = (String)rowDataMap.get("原料单位");
			if(StringHelper.isNullOrEmpty(MEINS1)){
				throw new BusinessException("行号"+rowDataMap.get("EXCEL行号")+"原料单位不能为空");
			}
			String ZPRO_LINE = (String)rowDataMap.get("产线");
			String ZPRO_NAME = (String)rowDataMap.get("产线描述");
			
			String RSPOS = (String)rowDataMap.get("预留行项目号");
			if(StringHelper.isNullOrEmpty(RSPOS)){
				throw new BusinessException("行号"+rowDataMap.get("EXCEL行号")+"预留行项目号不能为空");
			}
			SapProductOrder order = new SapProductOrder();
			
			
			order.setAUFNR(AUFNR);
			order.setWERKS(WERKS);
			order.setMATNR(MATNR);
			order.setAUART(AUART);
			order.setGLTRP(GLTRP);
			order.setGSTRP(GSTRP);
			order.setGAMNG(GAMNG);
			order.setMAKTX(MAKTX);
			order.setPOSNR(POSNR);
			order.setMATNR1(MATNR1);
			order.setBDMNG(BDMNG);
			order.setMAKTX1(MAKTX1);
			order.setMTART(MTART);
			order.setXLOEK(XLOEK);
			order.setKZEAR(KZEAR);
			order.setENMNG(ENMNG);
			order.setRSNUM(RSNUM);
			order.setBWART(BWART);
			order.setMEINS(MEINS);
			order.setMEINS1(MEINS1);
			order.setZPRO_LINE(ZPRO_LINE);
			order.setZPRO_NAME(ZPRO_NAME);
			order.setRSPOS(RSPOS);
			orders.add(order);

		}
		
		return orders;
		
	}
	
	/**生成交货单 + 工单齐套性校验  两个事务*/
    public void importOrderHandleTime(File file) {
    	CommonHelper.log("开始导入齐套性校验");
    	CommonHelper.log("开始生成交货单");
    	
    	WmsWarehouse wh = findCurrentWarehouse();//当前仓库
    	
    	//齐套验证前先生成交货单
    	WmsDeliveryOrderManager deliveryOrderManager = (WmsDeliveryOrderManager)applicationContext.getBean("wmsDeliveryOrderManager");
    	deliveryOrderManager.test();
    	
    	//齐套性验证   有事务
    	deliveryOrderManager.gdqt(file);
    }
    
    /**工单齐套性验证*/
    public void gdqt(File file){
    	CommonHelper.log("开始解析文件并组装");
		//解析.xls文件
    	List<Long> productionOrderList = readOrderHandleTimeExcel(file);
    	if (productionOrderList.isEmpty()) {
    		throw new BusinessException("导入的生产单查询为空!");
    	}
    	CommonHelper.log("开始齐套性校验逻辑处理");
    	
    	
    	/**当前仓库*/
		WmsWarehouse wh = (WmsWarehouse)commonDao.findByQueryUniqueResult("FROM WmsWarehouse warehouse WHERE warehouse.baseOrganization.id = :baseOrganizationId", 
				new String[] {"baseOrganizationId"}, new Object[] {BaseOrganizationHolder.getThornBaseOrganization().getId()});
		//将工单齐套表数据全部标识为不是最新数据
    	
    	dealOrderHandleTime(productionOrderList,wh,this.MEET_DAY);
    }
    
    
    //定时任务的月度齐套
    public void gdqt_mon() {
    	CommonHelper.log("定时任务的月度齐套-洗衣机");
    	//先算洗衣机  再算冰箱
    	/**洗衣机仓库*/
		WmsWarehouse wh = commonDao.load(WmsWarehouse.class, 2L);
		String sql = "SELECT DISTINCT P.id as id FROM PRODUCTION_ORDER P "
				+ "INNER JOIN PRODUCTION_ORDER_DETAIL PD ON PD.PRODUCTION_ORDER_ID=P.ID "
				+ "INNER JOIN WMS_SAP_FACTORY SF ON SF.ID = P.FACTORY_ID "
				+ "WHERE 1=1 AND P.STATUS IN ('OPEN','ACTIVE') AND TRUNC(P.BEGIN_DATE)<=TRUNC(SYSDATE)+1 "
				+ "AND SF.CODE IN ('2000','2100') ";
		
		List<Map<String,Object>> gdinfos = jdbcTemplate.queryForList(sql);
		List<Long> productionOrderList = new ArrayList<Long>();
		for(Map<String,Object> m : gdinfos) {
			Long poid = Long.valueOf((m.get("id"))+"");//工单id
			productionOrderList.add(poid);
		}
		//将工单齐套表数据全部标识为不是最新数据
    	dealOrderHandleTime(productionOrderList,wh,this.MEET_MON);
    	
    	CommonHelper.log("定时任务的月度齐套-冰箱");
    	/**冰箱仓库*/
		wh = commonDao.load(WmsWarehouse.class, 3L);
		sql = "SELECT DISTINCT P.id as id FROM PRODUCTION_ORDER P "
				+ "INNER JOIN PRODUCTION_ORDER_DETAIL PD ON PD.PRODUCTION_ORDER_ID=P.ID "
				+ "INNER JOIN WMS_SAP_FACTORY SF ON SF.ID = P.FACTORY_ID "
				+ "WHERE 1=1 AND P.STATUS IN ('OPEN','ACTIVE') AND TRUNC(P.BEGIN_DATE)<=TRUNC(SYSDATE)+1 "
				+ "AND SF.CODE IN ('1000','1100') ";
		
		gdinfos = jdbcTemplate.queryForList(sql);
		productionOrderList = new ArrayList<Long>();
		for(Map<String,Object> m : gdinfos) {
			Long poid = Long.valueOf((m.get("id"))+"");//工单id
			productionOrderList.add(poid);
		}
		//将工单齐套表数据全部标识为不是最新数据
    	dealOrderHandleTime(productionOrderList,wh,this.MEET_MON);
    	CommonHelper.log("定时任务的月度齐套-完成");
    }
    
    
    
	
    private List<Long> readOrderHandleTimeExcel(File file){
    	WmsWarehouse wh = findCurrentWarehouse();//当前仓库
    	//如果当前仓库是洗衣机,但是工厂不是洗衣机工厂,则不处理
		
    	List<Long> productionOrderList = new ArrayList<Long>();
		List<Map<String, Object>> infos=ExcelHelper.parseExcel2List(file);
		for (Map<String, Object> rowDataMap : infos) {
			String code = (String)rowDataMap.get("生产订单号");
			code = CommonHelper.addCharBeforeStr(code, 12,"0");
			  ProductionOrder productionOrder = findProductionOrderByCode(code);
			  if (null == productionOrder) {
				  throw new BusinessException("find.ProductionOrder.is.empty.by.code",new String[]{code});
			  }
			  String facCode = productionOrder.getFactory().getCode();
			  /**校验工厂是不是当前仓库下的,不是抛异常*/
			 productionOrderManager.validateFactory(facCode,code,Boolean.TRUE,wh.getId());
			  
			  for(ProductionOrderDetail detail :productionOrder.getDetails()) {
				  WmsItem item  = detail.getItem();
				  if(StringHelper.isNullOrEmpty(item.getUserFieldV1()) || StringHelper.isNullOrEmpty(item.getUserFieldV2())){
					  //没有交接属性生成预警
						String msg = "物料编码"+item.getCode()+"的JIT属性或交接属性不完整";
				 		genWarning(WmsWarningType.ITEM_ATT, msg);
				  }
			  }
			  
			  if(StringHelper.in(productionOrder.getStatus(), new String[]{ProductionOrderStatus.OPEN,ProductionOrderStatus.ACTIVE})){
				  productionOrderList.add(productionOrder.getId());
			  }
		}
		return productionOrderList;
    }
    /**
     * 齐套性校验的时候 先删除没有配货的每日送货数量，重新算齐套生成
     * */
    private void deleteDailyDelivery() {
    	String hql = "from DailyDeliverOrderDetail d where d.deliverQty-d.loadQty>0 and d.isAutoCreate='Y' ";
    	List<DailyDeliverOrderDetail> ds = commonDao.findByQuery(hql);
    	for(DailyDeliverOrderDetail d : ds) {
    		Double cancelQty = d.getDeliverQty()-d.getLoadQty();
    		if(d.getLoadQty()<=0d) {
    			d.getOrderDetail().addTheDeliveryQuantityBu(-1*cancelQty);
    			commonDao.delete(d);
    		}
    		else {
    			d.getOrderDetail().addTheDeliveryQuantityBu(-1*cancelQty);
    			d.setDeliverQty(d.getLoadQty());
    			commonDao.store(d);
    		}
    	}
    }
    

    
    /**工单齐套*/
    private void dealOrderHandleTime(List<Long> productionOrderList,WmsWarehouse wh, String type){
    	String type_gd=WmsProductionOrderMeetInfoType.GD;
    	String type_yld=WmsProductionOrderMeetInfoType.YLD;
    	if(MEET_MON.equals(type)) {
    		type_gd=WmsProductionOrderMeetInfoType.MON_GD;
    	}
//		/**删除配货数量*/
//		deleteDailyDelivery();
		
		/**标记这一批数据,返回这批数据的标识,后面根据标识来查询,避免用in*/
		String unicode = markBatchData(productionOrderList);
		
		if(MEET_DAY.equals(type)) {
			String hql = "update WmsProductionOrderMeetInfo set isNewFlag='N' where warehouse.id=:id and isNewFlag='Y' and type in ('GD','YLD')";
			commonDao.executeByHql(hql, "id", wh.getId());
		}
		else {//月度
			String hql = "delete from WmsProductionOrderMeetInfo where trunc(checkDate)=trunc(sysdate) and type=:type and warehouse.id=:id";
			commonDao.executeByHql(hql, new String[]{"type","id"}, new Object[]{WmsProductionOrderMeetInfoType.MON_GD,wh.getId()});
		}
		
		//获取库存并组装
		List<Map<String,Object>> allInvs = countWmsInventoryGroupByItemIdFactoryCode(); //查询库存
        Map<String,Double> dealInvs = new HashMap<String,Double>(); //组装后的库存
        for(Map<String,Object> map : allInvs) {
        	Long itemid = Long.valueOf(map.get("itemid")+"");
        	String faccode = (String)map.get("factorycode");
        	Double qty = Double.valueOf(map.get("qty")+"");
        	String key = itemid + CommonHelper.VTRADEX_SPLIT_STR + faccode;
        	if(!dealInvs.containsKey(key)) {
        		dealInvs.put(key, 0D);
        	}
        	dealInvs.put(key, DoubleUtils.add(dealInvs.get(key), qty));
        }
        
	        
		//  获取预留和工单的总计划数量   //key1  工厂+物料  key2 交货日期  key3 GD_SL/YL_SL
		Map<String,Map<String,Map<String,Object>>> sumPlanInfo = getGdYlZjh(unicode,wh.getId());
		Set<String> key1s = sumPlanInfo.keySet(); //工厂+物料
		
		for(String key1 : key1s) {
		 
			String[] values = key1.split(CommonHelper.VTRADEX_SPLIT_STR);
			Map<String,Map<String,Object>> key1info = sumPlanInfo.get(key1);
			Long itemId = Long.valueOf(values[0].toString());//物料id
			
			Long facId = Long.valueOf(values[1]);//工厂ID
			WmsSapFactory factory = this.commonDao.load(WmsSapFactory.class, facId);
			//总库存
//			Double inv = countWmsItemInventoryByFactory(itemId, factory.getCode()); 
			
			
			//总库存
        	Double inv = 0d;
        	String invKey = itemId + CommonHelper.VTRADEX_SPLIT_STR + factory.getCode();
        	if(dealInvs.get(invKey)!=null) {
        		inv = dealInvs.get(invKey);
        	}
        	
			//在途库存    根据物料+工厂 取已经安排交货的交货单  sum(交货单明细的本次交货数量)   
//			Double deliveryQty = findDeliveryQtyByItemAndFactory(itemId, facId);
			//任务的总可用库存
//			Double totalInventory = inv + deliveryQty;
			Double totalInventory = inv ;
			//临时对象
			Double tmpItemCountNumber = totalInventory ;
			
			//临时对象 总库存
//			Double tmpTotalInventory = totalInventory ;
			
			//校验工单齐套性  注意排序
			List<ProductionOrderDetail> productionOrderDetailList = findProductionOrderDetailByPoId(unicode,itemId,factory.getId());
			
			for (ProductionOrderDetail productionOrderDetail : productionOrderDetailList) {
				String reservedOrder = productionOrderDetail.getReservedOrder();//预留单号
				String reservedProject = productionOrderDetail.getReservedProject();//预留行项目号
				String productLine = productionOrderDetail.getProductionOrder().getProductLine();//线体号
				Double shipQty = productionOrderDetail.getShippedQuantityBu();//发运数量
				String status = ProductionOrderMeetInfoStatus.COMPLETE;
				String lineDesc = productionOrderDetail.getProductionOrder().getLineDesc(); //生产线描述
				Date xqrq = productionOrderDetail.getProductionOrder().getBeginDate(); //需求日期  取工单的开始日期
				Double planQty = DoubleUtils.sub(productionOrderDetail.getPlanQuantityBu(), productionOrderDetail.getAllocatedQuantityBu());
				Double invQty = productionOrderDetail.getPlanQuantityBu();//库存满足量
				if(planQty <= 0){//分配数>计划数
					createWmsProductionOrderMeetInfo(productionOrderDetail,Boolean.TRUE,type_gd,wh,reservedOrder,reservedProject,productLine,shipQty,invQty,0D,status,lineDesc,xqrq);
				}
				else if (tmpItemCountNumber.doubleValue() >= planQty) {
					createWmsProductionOrderMeetInfo(productionOrderDetail,Boolean.TRUE,type_gd,wh,reservedOrder,reservedProject,productLine,shipQty,invQty,0D,status,lineDesc,xqrq);
					tmpItemCountNumber -= planQty;
				} 
				else {
					if(tmpItemCountNumber <= 0 && productionOrderDetail.getAllocatedQuantityBu() <= 0d){
						tmpItemCountNumber = 0D;
						status = ProductionOrderMeetInfoStatus.SHORT;//缺料
					}else{
						status = ProductionOrderMeetInfoStatus.PART_COMPLETE;//部分满足
					}
					Double shortQty = planQty - tmpItemCountNumber;//缺料量
					invQty =( productionOrderDetail.getAllocatedQuantityBu() -productionOrderDetail.getShippedQuantityBu() )+ tmpItemCountNumber;//库存满足量=分配量+库存
					createWmsProductionOrderMeetInfo(productionOrderDetail,Boolean.FALSE,type_gd,wh,reservedOrder,reservedProject,productLine,shipQty,invQty,shortQty,status,lineDesc,xqrq);
					tmpItemCountNumber = 0D;
				}
				tmpItemCountNumber = tmpItemCountNumber < 0D ? 0D : tmpItemCountNumber;
			}
			
			if(MEET_MON.equals(type)) {
				continue; //月度的齐套信息不需要计算预留的
			}
			
			//校验预留齐套性 注意排序
			List<WmsReservedOrderDetail> rodList = findReservedOrderDetail(itemId,facId);

			for (WmsReservedOrderDetail rod : rodList) {
				String reservedOrder = rod.getReservedOrder().getCode();//预留单号
				String reservedProject = rod.getProject();//预留行项目号
				String productLine = null;//线体号
				Double shipQty = rod.getShippedQuantityBu();//发运数量
				String status = ProductionOrderMeetInfoStatus.COMPLETE;
				String lineDesc = null; //生产线描述
				Date xqrq = rod.getRequestDate(); //需求日期
				
				Double qty = DoubleUtils.sub(rod.getQuantity(), rod.getAllocatedQuantityBu());
				Double invQty = rod.getQuantity();//库存满足量
				if(qty <= 0){//分配数>计划数
					createWmsProductionOrderMeetInfo(rod,Boolean.TRUE,type_yld,wh,reservedOrder,reservedProject,productLine,shipQty,invQty,0d,status,lineDesc,xqrq);
				}
				else if (tmpItemCountNumber.doubleValue() >= qty) {
					 createWmsProductionOrderMeetInfo(rod,Boolean.TRUE,type_yld,wh,reservedOrder,reservedProject,productLine,shipQty,invQty,0d,status,lineDesc,xqrq);
					 tmpItemCountNumber -= qty;
				} 
				else {
					if(tmpItemCountNumber <= 0 && rod.getAllocatedQuantityBu() <= 0d){
						status = ProductionOrderMeetInfoStatus.SHORT;//缺料
					}else{
						status = ProductionOrderMeetInfoStatus.PART_COMPLETE;//部分满足
					}
					Double shortQty = qty - tmpItemCountNumber;//缺料量
					invQty = rod.getAllocatedQuantityBu() + tmpItemCountNumber;//库存满足量=分配量+库存
					
					createWmsProductionOrderMeetInfo(rod,Boolean.FALSE,type_yld,wh,reservedOrder,reservedProject,productLine,shipQty,invQty,shortQty,status,lineDesc,xqrq);
					tmpItemCountNumber = 0D;
				}
				tmpItemCountNumber = tmpItemCountNumber < 0D ? 0D : tmpItemCountNumber;
			}
		}
		//如果是晚上计算的月度齐套率  则删除无效数据
//		if(MEET_MON.equals(type)) {
//			//删除无效数据。
//			String sql3 = "DELETE FROM wms_po_meetinfo a  where a.type = 'MON_GD' AND TRUNC(A.CHECK_DATE)=TRUNC(SYSDATE) "
//					+ "AND "
//					+ "( "
//					+ "  ( "
//					+ "    TRUNC(A.XQRQ)=TRUNC(SYSDATE)+1  AND "
//					+ "   A.PRODUCT_LINE not IN  ('01','02','03','07','08','10','24','25','11','12','13', '15','16','17','18','19','20','21','22','23') "
//					+ "  ) or  "
//					+ "  ( TRUNC(A.XQRQ)=TRUNC(SYSDATE) AND A.PRODUCT_LINE not IN  ('06','09') ) "
//					+ "  or  TRUNC(A.XQRQ)<TRUNC(SYSDATE)   or  TRUNC(A.XQRQ)>TRUNC(SYSDATE)+1 "
//					+ ")";
//			this.jdbcTemplate.update(sql3);
//		}
		
	}
	/**生成预警*/
	private void genWarning(String type,String info) {
		WmsWarning wa = EntityFactory.getEntity(WmsWarning.class);
 		wa.setType(type);
 		wa.setStatus(WmsWarningStatus.OPEN);
 		wa.setWarningInfo(info);
 		WebServiceHelper.println(wa.getWarningInfo());
 		commonDao.store(wa);
		
	}
	
	private List<WmsReservedOrderDetail> findReservedOrderDetail(Long itemId,Long facId){
		 //当前日期加两天
		 String date = DateUtil.formatDateYMDToStr(DateUtil.addDayToDate(new Date(), 2));
		 String hql = "FROM WmsReservedOrderDetail wrd WHERE wrd.reservedOrder.jzrq <= "
		 			+ "to_date(:date, 'yyyy-mm-dd') AND wrd.item.id=:itemId "
		 			+ "AND wrd.factory.id=:facId AND wrd.deleteFlag is null "
		 			+ " AND wrd.reservedOrder.ydlx in ('"+WmsReservedOrderType.Z01+"','"+WmsReservedOrderType.Z03+"','"+WmsReservedOrderType.Z311+"') " //发货类型预留
		 			+ "ORDER BY wrd.reservedOrder.jzrq ASC";
		 List<WmsReservedOrderDetail> yl= this.commonDao.findByQuery(hql,
				 new String[]{"date","itemId","facId"},
				 new Object[]{date,itemId,facId} );
		 return yl;
	}
	//key1  工厂+物料  key2 交货日期  key3 GD_SL/YL_SL
	private Map<String,Map<String,Map<String,Object>>> getGdYlZjh(String unicode,Long warehouseId){
		String hql = "SELECT pod.item.id,SUM(pod.planQuantityBu-pod.allocatedQuantityBu),"
				+ "pod.productionOrder.factory.id,pod.productionOrder.beginDate FROM ProductionOrderDetail pod "
				+ " WHERE pod.productionOrder.batchMark = :batchMark "
			      + " GROUP BY pod.item.id,pod.productionOrder.factory.id,pod.productionOrder.beginDate";
		 List<Object[]> gd= this.commonDao.findByQuery(hql,"batchMark",unicode);
		 
		 //当前日期加两天
		 String date = DateUtil.formatDateYMDToStr(DateUtil.addDayToDate(new Date(), 2));
		 hql = "SELECT wrd.item.id,SUM(wrd.quantity-wrd.allocatedQuantityBu),wrd.factory.id,wrd.reservedOrder.jzrq "
		 		+ "FROM WmsReservedOrderDetail wrd WHERE wrd.reservedOrder.jzrq <= "
		 		+ "to_date(:date, 'yyyy-mm-dd') "
			      + " GROUP BY wrd.item.id,wrd.factory.id,wrd.reservedOrder.jzrq";
		 List<Object[]> yl= this.commonDao.findByQuery(hql,"date",date );
		 
		 Map<String,Map<String,Map<String,Object>>> result = new HashMap<String,Map<String,Map<String,Object>>>() ;
		 
		 for(Object[] obj : gd) {
			 String itemId = obj[0].toString(); //物料id
			 Double qty = (Double)obj[1]; //数量
			 Long fac_id = (Long)obj[2];//工厂id
			 String deliverDate = DateUtil.formatDateYMDToStr((Date)obj[3]);//交货日期
			 
			 String key1 = itemId+CommonHelper.VTRADEX_SPLIT_STR+fac_id;  //工厂 + 物料
			 if(result.get(key1)==null) {
				 result.put(key1, new HashMap<String,Map<String,Object>>());
			 }
			 Map<String,Map<String,Object>> info2 = result.get(key1);
			 
			 String key2 = deliverDate;// 交货日期
			 if(info2.get(key2)==null) {
				 info2.put(key2, new HashMap<String,Object>());
			 }
			 Map<String,Object> info3 = info2.get(key2);
			 String key3 = "GD_SL";
			 info3.put(key3, qty);
			 
		 }
		 for(Object[] obj : yl) {
			 String itemId = obj[0].toString(); //物料id
			 Double qty = (Double)obj[1];
			 Long fac_id = (Long)obj[2];
			 WmsSapFactory fac = commonDao.load(WmsSapFactory.class, fac_id);
			 /**校验工厂和仓库是否对应   不对应返回true*/
			 Boolean flag = productionOrderManager.validateFactory(fac.getCode(), "",Boolean.FALSE,warehouseId);
			 if(flag){
				 continue;
			 }
			 String deliverDate = DateUtil.formatDateYMDToStr((Date)obj[3]);//交货日期
			 
			 String key1 = itemId+CommonHelper.VTRADEX_SPLIT_STR+fac_id;  //工厂 + 物料
			 if(result.get(key1)==null) {
				 result.put(key1, new HashMap<String,Map<String,Object>>());
			 }
			 Map<String,Map<String,Object>> info2 = result.get(key1);
			 
			 String key2 = deliverDate;// 交货日期
			 if(info2.get(key2)==null) {
				 info2.put(key2, new HashMap<String,Object>());
			 }
			 Map<String,Object> info3 = info2.get(key2);
			 String key3 = "YL_SL";
			 info3.put(key3, qty);
			
		 }
		 
		 return result;
	}
	
	private List<ProductionOrderDetail> findProductionOrderDetailByPoId(String unicode,Long itemId,Long factoryId){
		String hql = " FROM ProductionOrderDetail pod WHERE pod.item.id =:itemId "
						+ " AND pod.productionOrder.factory.id =:factoryId "
						+ " AND  pod.productionOrder.batchMark = :batchMark and pod.deleteFlag is null " //deleteFlag为删除的  不用考虑
						+ " ORDER BY pod.productionOrder.beginDate ASC ";
		return this.commonDao.findByQuery(hql, new String[]{"itemId","factoryId","batchMark"}, new Object[]{itemId,factoryId,unicode});
	}
	
	public void createWmsArrivalDelivery(WmsDeliveryOrderDetail deliveryOrderDetail,Double quantity,Date  deliveryDate){
		WmsArrivalDelivery arrivalDelivery = EntityFactory.getEntity(WmsArrivalDelivery.class);
		arrivalDelivery.setDeliveryOrderDetail(deliveryOrderDetail);
		if (null == deliveryDate) {
			arrivalDelivery.setDeliveryDate(new Date());
		} else {
			/**交货日期为空  交货日期=当前-1天*/
			arrivalDelivery.setDeliveryDate(DateUtil.addDayToDate(new Date(), -1));
		}
		arrivalDelivery.setQuantity(quantity);
		this.commonDao.store(arrivalDelivery);
	}
	
	/**
	 * 预留 创建 齐套
	 * @param shipQty	发运数量
	 * @param planQty	库存满足量
	 * @param shortQty  缺料数量
	 */
	private void createWmsProductionOrderMeetInfo(WmsReservedOrderDetail rod,Boolean beMeet,String orderType,WmsWarehouse wh,
			String reservedOrder,String reservedProject,String productLine,Double shipQty,
			Double invQty,Double shortQty,String status,String lineDesc,Date xqrq) {
		createWmsProductionOrderMeetInfo(rod.getId(),rod.getReservedOrder().getCode(),
				 rod.getReservedOrder().getJzrq(),rod.getReservedOrder().getJzrq(),rod.getItem(),
				 rod.getQuantity(),rod.getUnit(),rod.getFactory(),beMeet,orderType,
				 wh,reservedOrder,reservedProject,productLine,shipQty,invQty,shortQty,status,lineDesc,xqrq);
	}
	/**
	 * 工单 创建 齐套
	 * @param shipQty	发运数量
	 * @param planQty	库存满足量
	 * @param shortQty  缺料数量
	 */
	private void createWmsProductionOrderMeetInfo(ProductionOrderDetail productionOrderDetail,Boolean beMeet,String orderType,WmsWarehouse wh,
			String reservedOrder,String reservedProject,String productLine,Double shipQty,Double invQty,Double shortQty,String status,String lineDesc,Date xqrq) {
		createWmsProductionOrderMeetInfo(productionOrderDetail.getId(),productionOrderDetail.getProductionOrder().getCode(),
				 productionOrderDetail.getProductionOrder().getBeginDate(),productionOrderDetail.getProductionOrder().getEndDate(), 
				 productionOrderDetail.getItem(),productionOrderDetail.getPlanQuantityBu(),productionOrderDetail.getPackageUnit(), 
				 productionOrderDetail.getProductionOrder().getFactory(),beMeet,orderType,wh,reservedOrder,reservedProject,
				 productLine,shipQty,invQty,shortQty,status,lineDesc,xqrq);
	}
	/**创建齐套信息 公用方法*/
	private void createWmsProductionOrderMeetInfo(Long detailId,String code,Date beginDate,Date endDate,
			WmsItem item,Double qty,WmsPackageUnit unit,WmsSapFactory fac, Boolean beMeet,
			String orderType,WmsWarehouse wh,String reservedOrder,String reservedProject,
			String productLine,Double shipQty,Double invQty,Double shortQty,String status,String lineDesc,Date xqrq){
		
		if(WmsProductionOrderMeetInfoType.MON_GD.equals(orderType)) { //月度齐套  很多情况不需要插入
			if(xqrq==null) {
				return ;
			}
			//只需要保留需求日期为今天 产线为06 09 的
			//和需求日期为明天 产线为'01','02','03','07','08','10','24','25','11','12','13', '15','16','17','18','19','20','21','22','23'的，其他全部不需要保存
			String xqrq_str = DateUtil.format(xqrq, "yyyyMMdd");
			String today_str = DateUtil.format(new Date(), "yyyyMMdd");
			String tomorrow_str = DateUtil.format(DateUtil.getTomorrowBegin(),"yyyyMMdd");
			if(xqrq_str.equals(today_str)) {
				if(!StringHelper.in(productLine, new String[]{"06","09"})) {
					return ;
				}
			}
			else if(xqrq_str.equals(tomorrow_str)) {
				if(!StringHelper.in(productLine, 
						new String[]{"01","02","03","07","08","10","24","25","11","12","13", "15","16","17","18","19","20","21","22","23"})) {
					return ;
				}
			}
			else {
				return ;
			}
		}
		
		WmsProductionOrderMeetInfo pom = EntityFactory.getEntity(WmsProductionOrderMeetInfo.class);
		pom.setBeMeet(beMeet);
		pom.setCheckDate(new Date());
		pom.setType(orderType);
		pom.setDetailId(detailId);
		pom.setOrderCode(code);
		pom.setBeginDate(beginDate);
		pom.setEndDate(endDate);
		pom.setItem(item);
		pom.setPlanQty(qty);
		pom.setPackageUnit(unit);
		pom.setFactory(fac);
		pom.setWarehouse(wh);
		pom.setIsNewFlag(Boolean.TRUE);
		pom.setReservedOrder(reservedOrder);
		pom.setReservedProject(reservedProject);
		pom.setProductLine(productLine);
		pom.setShipQty(shipQty);
		pom.setInvQty(invQty);//库存满足量
		pom.setShortQty(shortQty);//缺料量
		pom.setStatus(status);
		pom.setLineDesc(lineDesc);//生产线描述
		pom.setXqrq(xqrq);
		this.commonDao.store(pom);
	}
	
	
	public ProductionOrder findProductionOrderByCode(String ProductionOrderCode){
		String hql = " FROM ProductionOrder po WHERE po.code =:code ";
					//	+ " AND po.status in(:status)"; 
		return (ProductionOrder) this.commonDao.findByQueryUniqueResult(hql, new String[]{"code"}, new Object[]{ProductionOrderCode});
	}
	
	
	/**
	 * 批量导入PO
	 */
    public void importPurchaseOrderFile(File file){
    	
    	List<SapPo> sapPo = readPurchaseOrderFromExcel(file);
    
    	SapPoArray spa = new SapPoArray();
    	spa.setSapPos(sapPo.toArray(new SapPo[]{}));
    	spa.setTYPE("01");
    	spa.setROWCNT(sapPo.size()+"");
    	spa.setMESSAGEID(new Date().toLocaleString());
    	sapRowDataDealManager.dealSapPoOrders(spa);
		
	}
	
	
	private List<SapPo> readPurchaseOrderFromExcel(File file){
		
		List<SapPo> sapPo = new ArrayList<SapPo>();
		List<Map<String,Object>> infos = ExcelHelper.parseExcel2List(file);
		for(Map<String,Object> info : infos){
			
			
			String EBELN = (String) info.get("采购订单号");
			if (StringHelper.isNullOrEmpty(EBELN)) {
               throw new BusinessException("行号"+info.get("EXCEL行号")+"采购订单号不能为空");
			}
			String BSART = (String) info.get("采购订单类型");
			if (StringHelper.isNullOrEmpty(BSART)) {
				throw new BusinessException("行号"+info.get("EXCEL行号")+"采购订单类型不能为空");
			}
			String AEDAT = (String) info.get("订单创建日期");
			if (StringHelper.isNullOrEmpty(AEDAT)) {
				throw new BusinessException("行号"+info.get("EXCEL行号")+"订单创建日期不能为空");
			}
			if(AEDAT.length() - 8 != 0){
    			throw new BusinessException("行号"+info.get("EXCEL行号")+"订单创建日期【"+AEDAT+"】格式有误,请检查");
    		}
			//判断字符串 是否是数字
			try {
				int s = Integer.valueOf(AEDAT);
			} catch (Exception e) {
				throw new BusinessException("行号" + info.get("EXCEL行号")+ "订单创建日期【" + AEDAT + "】格式有误,请检查");
			}
			
			String ERNAM = (String) info.get("订单创建人");
			if (StringHelper.isNullOrEmpty(ERNAM)) {
				throw new BusinessException("行号"+info.get("EXCEL行号")+"订单创建人不能为空");
			}
			String LIFNR = (String) info.get("供应商编码");
			
			if (StringHelper.isNullOrEmpty(LIFNR)) {
				throw new BusinessException("行号"+info.get("EXCEL行号")+"供应商编码不能为空");
			}
			
			String EKORG = (String) info.get("采购组织");
			if (StringHelper.isNullOrEmpty(EKORG)) {
				throw new BusinessException("行号"+info.get("EXCEL行号")+"采购组织不能为空");
			}
			String EKGRP = (String) info.get("采购组");
			if (StringHelper.isNullOrEmpty(EKGRP)) {
				throw new BusinessException("行号"+info.get("EXCEL行号")+"采购组不能为空");
			}
			String EBELP = (String) info.get("项目");
			if (StringHelper.isNullOrEmpty(EBELP)) {
				throw new BusinessException("行号"+info.get("EXCEL行号")+"项目不能为空");
			}
			EBELP = CommonHelper.addCharBeforeStr(EBELP, 5, "0");
			
			String LOEKZ = (String) info.get("删除标识");
            
			String PSTYP = (String) info.get("项目类别");
			if (StringHelper.isNullOrEmpty(PSTYP)) {
				throw new BusinessException("行号"+info.get("EXCEL行号")+"项目类别不能为空");
			}
			String MATNR = (String) info.get("物料号");
			if (StringHelper.isNullOrEmpty(MATNR)) {
				throw new BusinessException("行号"+info.get("EXCEL行号")+"物料号不能为空");
			}
			String TXZ01 = (String) info.get("物料描述");
			if (StringHelper.isNullOrEmpty(TXZ01)) {
				throw new BusinessException("行号"+info.get("EXCEL行号")+"物料描述不能为空");
			}
			String WERKS = (String) info.get("工厂");
			if (StringHelper.isNullOrEmpty(WERKS)) {
				throw new BusinessException("行号"+info.get("EXCEL行号")+"工厂不能为空");
			}
			
			String LGORT = (String) info.get("库存地点");
			if (StringHelper.isNullOrEmpty(LGORT)) {
				throw new BusinessException("行号"+info.get("EXCEL行号")+"库存地点不能为空");
			}
			
			String MENGE = (String) info.get("订单数量");
			if (StringHelper.isNullOrEmpty(MENGE)) {
				throw new BusinessException("行号"+info.get("EXCEL行号")+"订单数量不能为空");
			}
			String MEINS = (String) info.get("订单单位");
			if (StringHelper.isNullOrEmpty(MEINS)) {
				throw new BusinessException("行号"+info.get("EXCEL行号")+"订单单位不能为空");
			}
			String RETPO = (String) info.get("退货标识");
			String INSMK = (String) info.get("状态");
			if(!StringHelper.isNullOrEmpty(INSMK)){
				if(!("X".equals(INSMK))){
					throw new BusinessException("行号"+info.get("EXCEL行号")+"状态【"+INSMK+"】格式有误,请检查");
				}
			}
			
			String EINDT = (String) info.get("交货日期");
			if (StringHelper.isNullOrEmpty(EINDT)) {
				throw new BusinessException("行号"+info.get("EXCEL行号")+"交货日期不能为空");
			}
			if(EINDT.length() - 8 != 0){
    			throw new BusinessException("行号"+info.get("EXCEL行号")+"交货日期【"+EINDT+"】格式有误,请检查");
    		}
			
//			String LINENO = (String) info.get("已交货数量");//只是导入期初采购订单临时使用下，后面此处代码要去掉
//			if (StringHelper.isNullOrEmpty(LINENO)) {
//				throw new BusinessException("行号"+info.get("EXCEL行号")+"已交货数量不能为空");
//			}
			
			//判断字符串 是否是数字
			try {
				int s = Integer.valueOf(EINDT);
			} catch (Exception e) {
				throw new BusinessException("行号" + info.get("EXCEL行号")+ "交货日期【" + EINDT + "】格式有误,请检查");
			}
			
	
			SapPo sap = new SapPo();
			
//			sap.setLINENO(LINENO);
			sap.setEBELN(EBELN);
			sap.setBSART(BSART);
			sap.setAEDAT(AEDAT);//订单创建日期
			sap.setERNAM(ERNAM);
			sap.setLIFNR(LIFNR);
			sap.setEKORG(EKORG);
			sap.setEKGRP(EKGRP);
			sap.setEBELP(EBELP);
			sap.setLOEKZ(LOEKZ);
			
			if("标准".equals(PSTYP) || "0".equals(PSTYP)){
				sap.setPSTYP("0");//项目类别
			}else if("寄售".equals(PSTYP) || "2".equals(PSTYP)){
				sap.setPSTYP("2");
			}else{
				throw new BusinessException("行号"+info.get("EXCEL行号")+"项目类别【"+PSTYP+"】不合法,请检查");
			}

			sap.setMATNR(MATNR);
			sap.setTXZ01(TXZ01);
			sap.setWERKS(WERKS);
			sap.setLGORT(LGORT);
			sap.setMENGE(MENGE);
			sap.setMEINS(MEINS);
			sap.setRETPO(RETPO);
			sap.setINSMK(INSMK);
			sap.setEINDT(EINDT);
			sapPo.add(sap);
		}
		return sapPo;
	}
	
	
	//交货单批量导入
	public void importWmsDeliveryOrderFile(File file){
		
        List<SapDeliveryOrder> deliveryOrders = readWmsDeliveryOrderFromExcel(file);
        SapDeliveryOrderArray doa = new SapDeliveryOrderArray();
        doa.setSpoas(deliveryOrders.toArray(new SapDeliveryOrder[]{}));
        doa.setTYPE("I");
        doa.setROWCNT(deliveryOrders.size()+"");
        doa.setMESSAGEID(new Date().toLocaleString());
    	sapRowDataDealManager.dealSapDeliveryOrder(doa);
	}
	
	
	
	
	private List<SapDeliveryOrder> readWmsDeliveryOrderFromExcel(File file){
		
		List<SapDeliveryOrder>  deliveryOrders = new ArrayList<SapDeliveryOrder>();
		
		List<Map<String,Object>> infos = ExcelHelper.parseExcel2List(file);
		
		for(Map<String,Object> info : infos){
			
			String VBELN = (String) info.get("采购交货号");
			if(StringHelper.isNullOrEmpty(VBELN)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"采购交货号不能为空");
			}
			VBELN = CommonHelper.addCharBeforeStr(VBELN, 10, "0");
			String ERNAM = (String) info.get("创建者");
			if(StringHelper.isNullOrEmpty(ERNAM)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"创建者不能为空");
			}
			String ERDAT = (String) info.get("创建日期");
			if(StringHelper.isNullOrEmpty(ERDAT)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"创建日期不能为空");
			}
			if(ERDAT.length() - 8 != 0){
    			throw new BusinessException("行号"+info.get("EXCEL行号")+"创建日期【"+ERDAT+"】格式有误,请检查");
    		}
			//判断字符串 是否是数字
			try {
				int s = Integer.valueOf(ERDAT);
			} catch (Exception e) {
				throw new BusinessException("行号" + info.get("EXCEL行号")+ "创建日期【" + ERDAT + "】格式有误,请检查");
			}
			
			String BLDAT = (String) info.get("凭证日期");
			if(StringHelper.isNullOrEmpty(BLDAT)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"凭证日期不能为空");
			}
			if(BLDAT.length() - 8 != 0){
    			throw new BusinessException("行号"+info.get("EXCEL行号")+"凭证日期【"+BLDAT+"】格式有误,请检查");
    		}
			//判断字符串 是否是数字
			try {
				int s = Integer.valueOf(BLDAT);
			} catch (Exception e) {
				throw new BusinessException("行号" + info.get("EXCEL行号")+ "创建日期【" + BLDAT + "】格式有误,请检查");
			}
			
			
			String LFART = (String) info.get("交货类型");
			if(StringHelper.isNullOrEmpty(LFART)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"交货类型不能为空");
			}
			
			String LIFNR = (String) info.get("供应商编码");
			if(StringHelper.isNullOrEmpty(LIFNR)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"供应商编码不能为空");
			}
			
			String TCODE = (String) info.get("事物代码");
			
			String POSNR = (String) info.get("项目");
			if(StringHelper.isNullOrEmpty(POSNR)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"项目不能为空");
			}
			
			POSNR = CommonHelper.addCharBeforeStr(POSNR,6,"0");
			String MATNR = (String) info.get("物料号");
			if(StringHelper.isNullOrEmpty(MATNR)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"物料号不能为空");
			}
			
			String WERKS = (String) info.get("工厂");
			if(StringHelper.isNullOrEmpty(WERKS)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"工厂不能为空");
			}
			String LGORT = (String) info.get("库存地点");
			if(StringHelper.isNullOrEmpty(LGORT)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"库存地点不能为空");
			}

			String LFIMG = (String) info.get("交货数量");
			if(StringHelper.isNullOrEmpty(LFIMG)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"交货数量不能为空");
			}
			String MEINS = (String) info.get("基本单位");
			if(StringHelper.isNullOrEmpty(MEINS)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"基本单位不能为空");
			}
			String VGBEL = (String) info.get("参考凭证");
			if(StringHelper.isNullOrEmpty(VGBEL)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"参考凭证不能为空");
			}
			String VGPOS = (String) info.get("参考凭证行项目");
			if(StringHelper.isNullOrEmpty(VGPOS)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"参考凭证行项目不能为空");
			}
			VGPOS = CommonHelper.addCharBeforeStr(VGPOS,5,"0");

			
			String DABMG = (String) info.get("实际交货量");
			String WBSTK = (String) info.get("货物移动状态");
			
			String LFDAT = (String) info.get("交货日期");
			if(StringHelper.isNullOrEmpty(LFDAT)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"交货日期不能为空");
			}
			if(LFDAT.length() - 8 != 0){
    			throw new BusinessException("行号"+info.get("EXCEL行号")+"交货日期【"+LFDAT+"】格式有误,请检查");
    		}
			//判断字符串 是否是数字
			try {
				int s = Integer.valueOf(LFDAT);
			} catch (Exception e) {
				throw new BusinessException("行号" + info.get("EXCEL行号")+ "交货日期【"+LFDAT+"】格式有误,请检查");
			}
			
			
			SapDeliveryOrder deliveryOrder = new SapDeliveryOrder();
			
			
			deliveryOrder.setVBELN(VBELN);
			deliveryOrder.setERNAM(ERNAM);
			deliveryOrder.setERDAT(ERDAT);
			deliveryOrder.setBLDAT(BLDAT);
			deliveryOrder.setLFART(LFART);
			deliveryOrder.setLIFNR(LIFNR);
			deliveryOrder.setTCODE(TCODE);
			deliveryOrder.setPOSNR(POSNR);
			deliveryOrder.setMATNR(MATNR);
			deliveryOrder.setWERKS(WERKS);
			deliveryOrder.setLGORT(LGORT);
			deliveryOrder.setLFIMG(LFIMG);
			deliveryOrder.setMEINS(MEINS);
			deliveryOrder.setVGBEL(VGBEL);
			deliveryOrder.setVGPOS(VGPOS);
			deliveryOrder.setDABMG(DABMG);
			deliveryOrder.setWBSTK(WBSTK);
			deliveryOrder.setLFDAT(LFDAT);
			
			deliveryOrders.add(deliveryOrder);;
		
		}
		return deliveryOrders;		
	}

	@Override
	public void unActiveDeliveryOrder(WmsDeliveryOrder deliveryOrder) {
		//销售交货单生效会生成拣货单,所以生效要删除对应的拣货单
		if(deliveryOrder.getBillTypeName().equals(WmsDeliveryOrderBillType.XSBILLTYPE)){
			List<Long> deliverDetailId = new ArrayList<Long>();//交货单明细ID
			List<Long> middleTableId = new ArrayList<Long>();//交货单明细与拣货单明细管理表
			List<Long> pdtId = new ArrayList<Long>();//拣货单明细ID
			List<Long> pickIds = new ArrayList<Long>();//拣货单ID
			
			
			for(WmsDeliveryOrderDetail detail : deliveryOrder.getDetails()){
				deliverDetailId.add(detail.getId());
			}
			String hql = "select d.id,d.pickticketDetail.id,d.pickticketDetail.pickTicket.id  "
					+ "from DeliveryOrderDetailPtDetail d "
					+ "where d.deliveryOrderDetail.id in (:ids)";
			List<Object[]> idValues = commonDao.findByQuery(hql,"ids",deliverDetailId);
			
			for(Object[] obj : idValues){
				middleTableId.add((Long)obj[0]);
				pdtId.add((Long)obj[1]);
				pickIds.add((Long)obj[2]);
			}
			
			hql = "from WmsPickTicket p where status<>'OPEN' AND status<>'CLOSED' and p.id in (:ids)";
			List<WmsPickTicket> pickTickets = commonDao.findByQuery(hql,"ids",pickIds);//已经作业的拣货单
			if(pickTickets.size() > 0){
				throw new BusinessException("拣货单已经作业,交货单无法失效");
			}
			
			hql = "from WmsPickTicket p where status='OPEN' and p.id in (:ids)";
			pickTickets = commonDao.findByQuery(hql,"ids",pickIds);//打开或者关闭的拣货单
			
			//删除交货单明细和拣货单明细对应关系表的数据
			hql = "delete from DeliveryOrderDetailPtDetail w where w.id in (:ids)";
			commonDao.executeByHql(hql, "ids", middleTableId);
			
			//删除bol明细,拣货单明细以及批次信息
			deleteRelatePickData(pdtId,null,"交货单");
			
			//删除拣货单
			commonDao.deleteAll(pickTickets);
			
			
			/**由于仓单拉动可能生成新的拣货单,所以和老的拣货单失去了关联,这里在找老拣货单*/
			hql = "select w.id from WmsPickTicket w where w.userField3=:userField3 "
					+ "and w.relatedBill1=:relatedBill1 and w.allocateQty=0";
			
			List<Long> pickTicketIds = commonDao.findByQuery(hql,
					new String[]{"userField3","relatedBill1"},
					new Object[]{WmsPickticketGenType.XSJHD,deliveryOrder.getCode()});
			if(pickTicketIds.size() > 0){
				hql = "select w.id from WmsPickTicketDetail w where w.pickTicket.id in (:ids)";
				List<Long> detailIds = commonDao.findByQuery(hql, "ids", pickTicketIds);
				
				if(detailIds.size() > 0){
					//删除拣货单明细、bol明细、批次信息
					deleteRelatePickData(detailIds,pickTicketIds,"交货单");
				}
				
			}
		}
	}
	
	/**删除拣货明细以及关联的数据  例如bol明细和批次信息   拣货明细ID,拣货单ID,单据类型*/
	public void deleteRelatePickData(List<Long> pdtId,List<Long> pickIds,String type){
		if(pdtId.size() > 0){
			//删除拣货单明细的批次信息
			String hql = "delete from WmsPickTicketDetailRequire w where w.pickTicketDetail.id in (:ids)";
			commonDao.executeByHql(hql, "ids", pdtId);
			//删除BOL明细
			hql = "delete from WmsBolDetail w where w.pickTicketDetail.id in (:ids)";
			commonDao.executeByHql(hql, "ids", pdtId);
			
			//删除拣货单明细
			hql = "delete from WmsPickTicketDetail w where w.id in (:ids)";
			commonDao.executeByHql(hql, "ids", pdtId);
			
			if(null != pickIds && pickIds.size() > 0){
				//删除拣货单
				hql = "delete from WmsPickTicket w where w.id in (:id)";
				commonDao.executeByHql(hql, "id", pickIds);
				pickIds.clear();
			}
		}else if(null != pickIds && pickIds.size() > 0){
			//删除拣货单
			String hql = "delete from WmsPickTicket w where w.id in (:id)";
			commonDao.executeByHql(hql, "id", pickIds);
		}else{//如果拣货明细ID为空,写入日志,方便检查
			logger.error("========="+type+"失效->拣货单明细ID为空===========");
		}	
	}
	
	/**预留单导入*/
	public void importReservedOrderFile(File file){
		List<SapReservedData> srds = this.readReservedOrderFromExcel(file);
		
		SapReservedDataArray srda = new SapReservedDataArray();
		srda.setDatas(srds.toArray(new SapReservedData[]{}));
		srda.setTYPE("I");
		srda.setROWCNT(srds.size()+"");
		srda.setMESSAGEID(new Date().toLocaleString());
		sapRowDataDealManager.dealSapReservedData(srda);
	}
	
	private List<SapReservedData> readReservedOrderFromExcel(File file){
		List<SapReservedData> srds = new ArrayList<SapReservedData>();
		List<Map<String,Object>> infos = ExcelHelper.parseExcel2List(file);
		for(Map<String,Object> info : infos){
			String LINENO = (String) info.get("行号");
			String RSNUM = (String) info.get("预留号");
			if(StringHelper.isNullOrEmpty(RSNUM)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"预留号不能为空");
			}
			RSNUM = CommonHelper.addCharBeforeStr(RSNUM, 10, "0");
			
			String RSDAT = (String) info.get("基准日期");
			if(StringHelper.isNullOrEmpty(RSNUM)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"基准日期不能为空");
			}
			
			if(RSDAT.length() - 8 != 0){
    			throw new BusinessException("行号"+info.get("EXCEL行号")+"基准日期【"+RSDAT+"】格式有误,请检查");
    		}
			
			String USNAM = (String) info.get("用户名称");
//			if(StringHelper.isNullOrEmpty(USNAM)){
//				throw new BusinessException("行号"+info.get("EXCEL行号")+"用户名称不能为空");
//			}
			
			String BWART = (String) info.get("移动类型");
			if(StringHelper.isNullOrEmpty(BWART)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"移动类型不能为空");
			}
			String KOSTL = (String) info.get("成本中心");
			String KTEXT = (String) info.get("成本中心描述");
			String UMWRK = (String) info.get("收货工厂");
			String UMLGO = (String) info.get("收货库存地点");
			String RSPOS = (String) info.get("项目");
			if(StringHelper.isNullOrEmpty(RSPOS)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"项目不能为空");
			}
			RSPOS = CommonHelper.addCharBeforeStr(RSPOS,4,"0");
			String XLOEK = (String) info.get("删除标识");
			String KZEAR = (String) info.get("最后发货");
			String MATNR = (String) info.get("物料号");
			if(StringHelper.isNullOrEmpty(MATNR)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"物料号不能为空");
			}
			String WERKS = (String) info.get("工厂");
			if(StringHelper.isNullOrEmpty(WERKS)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"工厂不能为空");
			}
			String LGORT = (String) info.get("发出库位");
			String BDTER = (String) info.get("需求日期");
			if(StringHelper.isNullOrEmpty(BDTER)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"需求日期不能为空");
			}
			if(BDTER.length() - 8 != 0){
    			throw new BusinessException("行号"+info.get("EXCEL行号")+"需求日期【"+BDTER+"】格式有误,请检查");
    		}
			
			String BDMNG = (String) info.get("需求量");
			if(StringHelper.isNullOrEmpty(BDMNG)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"需求量不能为空");
			}
			String MEINS = (String) info.get("基本计量单位");
			if(StringHelper.isNullOrEmpty(MEINS)){
				throw new BusinessException("行号"+info.get("EXCEL行号")+"基本计量单位不能为空");
			}
			String SHKZG = (String) info.get("借贷标识");
			String ENMNG = (String) info.get("提货数");
			String SGTXT = (String) info.get("行项目文本");
			
			SapReservedData srd = new SapReservedData();
			srd.setLINENO(LINENO);
			srd.setRSNUM(RSNUM);
			srd.setRSDAT(RSDAT);
			srd.setUSNAM(USNAM);
			srd.setBWART(BWART);
			srd.setKOSTL(KOSTL);
			srd.setKTEXT(KTEXT);
			srd.setUMWRK(UMWRK);
			srd.setUMLGO(UMLGO);
			srd.setRSPOS(RSPOS);
			srd.setXLOEK(XLOEK);
			srd.setKZEAR(KZEAR);
			srd.setMATNR(MATNR);
			srd.setWERKS(WERKS);
			srd.setLGORT(LGORT);
			srd.setBDTER(BDTER);
			srd.setBDMNG(BDMNG);
			srd.setMEINS(MEINS);
			srd.setSHKZG(SHKZG);
			srd.setENMNG(ENMNG);
			srd.setSGTXT(SGTXT);
			
			srds.add(srd);
		}
		
		return srds;
		
	}

	@Override
	public void addDeliverOrderQty(DailyDeliverOrderDetail ddod,Double deliverQty) {
		WmsDeliveryOrderDetail deliveryOrderDetail = ddod.getOrderDetail();
		Double planQty = deliveryOrderDetail.getPlanQuantityBu();//交货单明细的计划数
		if(planQty - deliverQty - deliveryOrderDetail.getDelivedQuantityBu() 
				- deliveryOrderDetail.getTheDeliveryQuantityBu() < 0){//用户填写的数量>计划数
			throw new BusinessException("交货数量不能大于交货单明细的计划数");
		}
		ddod.setDeliverQty(deliverQty);
		ddod.setIsAutoCreate(Boolean.FALSE);//非系统生成
		ddod.setLoadQty(0D);
		commonDao.store(ddod);
		deliveryOrderDetail.addTheDeliveryQuantityBu(deliverQty);//本次交货数量
		commonDao.store(deliveryOrderDetail);
		
	}

	@Override
	public void deleteDailyOrder(DailyDeliverOrderDetail ddod) {
		
		WmsDeliveryOrderDetail deliveryOrderDetail = ddod.getOrderDetail();
		if(deliveryOrderDetail.getDelivedQuantityBu() > 0){
			throw new BusinessException("交货单"+
					deliveryOrderDetail.getDeliveryOrder().getCode()+"已经开始交货,无法删除每日交货明细");
		}
		
		deliveryOrderDetail.subTheDeliveryQuantityBu(ddod.getDeliverQty());//本次交货数量
		commonDao.store(deliveryOrderDetail);
	}
	
	/**
	 * 查库存条件的公用方法
	 * 存货位的正常和待入+收货位的正常库存  状态不等于不良品
	 * @return
	 */
	private String findInventory(){
		String str = 
				" ("
						+ " (inventory.location.type='RECEIVE' and inventory.operationStatus='NORMAL') "
						+ " 	or "
						+ " (inventory.location.type='STORAGE' and inventory.operationStatus in ('NORMAL','READY_IN'))"
						+ " 	or "
						+ " (inventory.location.code='XBC' and inventory.operationStatus ='NORMAL') "
						
						+ ") ";
		return str;
	}

	/**
	 * 查仓库所有的当前实物库存
	 * 	收货库位=正常
	 * 	存货库位=正常+待入
	 * 	调拨库位=正常+待入+待出 	拣货单分配到调拨库位,会生成待入库存,
	 * 						拣货作业完成后库存变正常,加入配送单分配后会生成待出库存,所以要统计调拨位所有状态的库存
	 * 	发货库位=待出+待入+正常	拣货单分配到备货库位,会生成待入库存,作业完成后产生待出库存
	 * @return
	 */
	private String inventoryCondition(){
		String str = 
				" ("
				+ " (inventory.location.type='RECEIVE' and inventory.operationStatus='NORMAL') "
				+ " 	or "
				+ " (inventory.location.type='STORAGE' and inventory.operationStatus in ('NORMAL','READY_IN')) "
				+ "		or "
				+ " inventory.location.type='HANDOVER' "
				+ " 	or "
				+ "	inventory.location.type='SHIP'"
				+ ") ";
		return str;
	}
	/**
	 * 查找当前仓库
	 * @return
	 */
	private WmsWarehouse findCurrentWarehouse(){
		WmsWarehouse wh = (WmsWarehouse)commonDao.findByQueryUniqueResult("FROM WmsWarehouse warehouse WHERE warehouse.baseOrganization.id = :baseOrganizationId", 
				new String[] {"baseOrganizationId"}, new Object[] {BaseOrganizationHolder.getThornBaseOrganization().getId()});
		return wh;
	}
	
	private Map<String,Object> getInventoryTrendMap(Long facId,Long itemId) {
		Map<String,Object> submap =new HashMap<String,Object>();
		submap.put("GC", facId); //工厂ID
		submap.put("WL", itemId); //物料ID
		submap.put("JSKC", 0D); //寄售库存数据量
		submap.put("BZKC", 0D); //标准库存数量
		for(int i=1;i<=15;i++) {
			submap.put("GDT"+i, 0D); //工单t1----t15
			submap.put("YLT"+i, 0D); //预留t1---t15
		}
		return submap;
	}

	@Override
	public void refreshInventoryTrend() {
		
		//删除当天历史数据
		Date date = new Date();
		CommonHelper.log("开始删除历史数据");
		String deleteHql = "delete from WmsInventoryTrend w "
					+ "where to_char(needDate,'yyyy-mm-dd')=:date";
		commonDao.executeByHql(deleteHql, "date", DateUtil.format(date, "yyyy-MM-dd"));
		
		Date t_1 = DateUtil.addDayToDate(date, 1);
		/**
		 * 计算当前实时寄售和标准库存
		 * 
		 * t-1工单需求 为  明天（含）之前的所有工单的计划 及   需求日期<=sysdate+1
		 * t-2工单需求 为  明天（不含）之后 后天（含）之前的所有工单的计划 及   需求日期>sysdate+1 且 需求日期<=sysdate +2   也就是 =trunc(sysdate)+2
		 * t-3工单需求 为  后天（不含）之后 大后天（含）之前的所有工单的计划 及   需求日期>sysdate+2 且 需求日期<=sysdate +3   也就是 =trunc(sysdate)+3
		 * 以此类推
		 * 预留也一样
		 * 
		 * **/
		
		CommonHelper.log("开始取T-1工单数据");
		//key为 工厂ID+物料id  第二个key为string
		Map<String,Map<String,Object>> result = new HashMap<String,Map<String,Object>>();
//		//取T-1工单情况 T-1包含以前
//		String hql = "select d.item.id,d.productionOrder.factory.id,sum(d.planQuantityBu-d.shippedQuantityBu) "
//				+ "from ProductionOrderDetail d where d.productionOrder.beginDate<=:date "
//				+ "group by d.item.id,d.productionOrder.factory.id";
//		
//		
//		List<Object[]> objs = commonDao.findByQuery(hql,"date",t_1);
//		for(Object[] obj : objs){
//			Long itemId = (Long)obj[0];
//			Long facId = (Long)obj[1];
//			
//			Double gdQty = StringHelper.replaceNullToZero((Double)obj[2]);//工单需求数量
//			String key = facId + CommonHelper.VTRADEX_SPLIT_STR + itemId;
//			if(!result.containsKey(key)) {
//				Map<String,Object> submap =new HashMap<String,Object>();
//				submap.put("GC", facId); //工厂ID
//				submap.put("WL", itemId); //物料ID
//				submap.put("GD", 0D); //工单数量
//				submap.put("YL", 0D); //预留数量
//				submap.put("JSKC", 0D); //寄售库存数据量
//				submap.put("BZKC", 0D); //标准库存数量
//				result.put(key, submap);
//			}
//			Map map = result.get(key);
//			map.put("GD", gdQty);
//		}
		
		String sql = "select d.item_id as itemid,"
				+ " o.factory_id as factoryid, "
				+ " round(nvl(sum(case when trunc(o.begin_date)<=trunc(sysdate)+1 then d.plan_quantity_bu-d.shipped_quantity_bu else 0 end ),0),3) as t1, "
				+ " round(nvl(sum(case when trunc(o.begin_date)=trunc(sysdate)+2 then d.plan_quantity_bu-d.shipped_quantity_bu else 0 end ),0),3) as t2, "
				+ " round(nvl(sum(case when trunc(o.begin_date)=trunc(sysdate)+3 then d.plan_quantity_bu-d.shipped_quantity_bu else 0 end ),0),3) as t3, "
				+ " round(nvl(sum(case when trunc(o.begin_date)=trunc(sysdate)+4 then d.plan_quantity_bu-d.shipped_quantity_bu else 0 end ),0),3) as t4, "
				+ " round(nvl(sum(case when trunc(o.begin_date)=trunc(sysdate)+5 then d.plan_quantity_bu-d.shipped_quantity_bu else 0 end ),0),3) as t5, "
				+ " round(nvl(sum(case when trunc(o.begin_date)=trunc(sysdate)+6 then d.plan_quantity_bu-d.shipped_quantity_bu else 0 end ),0),3) as t6, "
				+ " round(nvl(sum(case when trunc(o.begin_date)=trunc(sysdate)+7 then d.plan_quantity_bu-d.shipped_quantity_bu else 0 end ),0),3) as t7, "
				+ " round(nvl(sum(case when trunc(o.begin_date)=trunc(sysdate)+8 then d.plan_quantity_bu-d.shipped_quantity_bu else 0 end ),0),3) as t8, "
				+ " round(nvl(sum(case when trunc(o.begin_date)=trunc(sysdate)+9 then d.plan_quantity_bu-d.shipped_quantity_bu else 0 end ),0),3) as t9, "
				+ " round(nvl(sum(case when trunc(o.begin_date)=trunc(sysdate)+10 then d.plan_quantity_bu-d.shipped_quantity_bu else 0 end ),0),3) as t10, "
				+ " round(nvl(sum(case when trunc(o.begin_date)=trunc(sysdate)+11 then d.plan_quantity_bu-d.shipped_quantity_bu else 0 end ),0),3) as t11, "
				+ " round(nvl(sum(case when trunc(o.begin_date)=trunc(sysdate)+12 then d.plan_quantity_bu-d.shipped_quantity_bu else 0 end ),0),3) as t12, "
				+ " round(nvl(sum(case when trunc(o.begin_date)=trunc(sysdate)+13 then d.plan_quantity_bu-d.shipped_quantity_bu else 0 end ),0),3) as t13, "
				+ " round(nvl(sum(case when trunc(o.begin_date)=trunc(sysdate)+14 then d.plan_quantity_bu-d.shipped_quantity_bu else 0 end ),0),3) as t14, "
				+ " round(nvl(sum(case when trunc(o.begin_date)=trunc(sysdate)+15 then d.plan_quantity_bu-d.shipped_quantity_bu else 0 end ),0),3) as t15 "
				+ " from production_order_detail d  "
				+ " inner join production_order o on o.id = d.production_order_id "
				+ " where trunc(o.begin_date)<=trunc(sysdate)+15 "
				+ " and d.plan_quantity_bu-d.shipped_quantity_bu>0 "
				+ " and o.status='ACTIVE' "
				+ " group by d.item_id,o.factory_id ";
		List<Map<String,Object>> gdinfos = jdbcTemplate.queryForList(sql);
		for(Map<String,Object> m : gdinfos) {
			Long itemId = Long.valueOf((m.get("itemid"))+"");//物料ID
			Long facId = Long.valueOf((m.get("factoryid"))+"");//工厂ID
			
			Double t1 = Double.valueOf((m.get("t1"))+"");
			Double t2 = Double.valueOf((m.get("t2"))+"");
			Double t3 = Double.valueOf((m.get("t3"))+"");
			Double t4 = Double.valueOf((m.get("t4"))+"");
			Double t5 = Double.valueOf((m.get("t5"))+"");
			Double t6 = Double.valueOf((m.get("t6"))+"");
			Double t7 = Double.valueOf((m.get("t7"))+"");
			Double t8 = Double.valueOf((m.get("t8"))+"");
			Double t9 = Double.valueOf((m.get("t9"))+"");
			Double t10 = Double.valueOf((m.get("t10"))+"");
			Double t11 = Double.valueOf((m.get("t11"))+"");
			Double t12 = Double.valueOf((m.get("t12"))+"");
			Double t13 = Double.valueOf((m.get("t13"))+"");
			Double t14 = Double.valueOf((m.get("t14"))+"");
			Double t15 = Double.valueOf((m.get("t15"))+"");
			
			String key = facId + CommonHelper.VTRADEX_SPLIT_STR + itemId;
			if(!result.containsKey(key)) {
				result.put(key, getInventoryTrendMap(facId,itemId));
			}
			Map map = result.get(key);
			map.put("GDT1", t1);
			map.put("GDT2", t2);
			map.put("GDT3", t3);
			map.put("GDT4", t4);
			map.put("GDT5", t5);
			map.put("GDT6", t6);
			map.put("GDT7", t7);
			map.put("GDT8", t8);
			map.put("GDT9", t9);
			map.put("GDT10", t10);
			map.put("GDT11", t11);
			map.put("GDT12", t12);
			map.put("GDT13", t13);
			map.put("GDT14", t14);
			map.put("GDT15", t15);
		}
		CommonHelper.log("开始取T-1预留数据");
		
		sql = "select d.item_id as itemid, "
			+ " d.factory_id as factoryid, "
			+ " round(nvl(sum(case when trunc(d.request_date)<=trunc(sysdate)+1 then d.quantity-d.shipped_quantity_bu else 0 end ),0),3) as t1, "
			+ " round(nvl(sum(case when trunc(d.request_date)=trunc(sysdate)+2 then d.quantity-d.shipped_quantity_bu else 0 end ),0),3) as t2, "
			+ " round(nvl(sum(case when trunc(d.request_date)=trunc(sysdate)+3 then d.quantity-d.shipped_quantity_bu else 0 end ),0),3) as t3, "
			+ " round(nvl(sum(case when trunc(d.request_date)=trunc(sysdate)+4 then d.quantity-d.shipped_quantity_bu else 0 end ),0),3) as t4, "
			+ " round(nvl(sum(case when trunc(d.request_date)=trunc(sysdate)+5 then d.quantity-d.shipped_quantity_bu else 0 end ),0),3) as t5, "
			+ " round(nvl(sum(case when trunc(d.request_date)=trunc(sysdate)+6 then d.quantity-d.shipped_quantity_bu else 0 end ),0),3) as t6, "
			+ " round(nvl(sum(case when trunc(d.request_date)=trunc(sysdate)+7 then d.quantity-d.shipped_quantity_bu else 0 end ),0),3) as t7, "
			+ " round(nvl(sum(case when trunc(d.request_date)=trunc(sysdate)+8 then d.quantity-d.shipped_quantity_bu else 0 end ),0),3) as t8, "
			+ " round(nvl(sum(case when trunc(d.request_date)=trunc(sysdate)+9 then d.quantity-d.shipped_quantity_bu else 0 end ),0),3) as t9, "
			+ " round(nvl(sum(case when trunc(d.request_date)=trunc(sysdate)+10 then d.quantity-d.shipped_quantity_bu else 0 end ),0),3) as t10, "
			+ " round(nvl(sum(case when trunc(d.request_date)=trunc(sysdate)+11 then d.quantity-d.shipped_quantity_bu else 0 end ),0),3) as t11, "
			+ " round(nvl(sum(case when trunc(d.request_date)=trunc(sysdate)+12 then d.quantity-d.shipped_quantity_bu else 0 end ),0),3) as t12, "
			+ " round(nvl(sum(case when trunc(d.request_date)=trunc(sysdate)+13 then d.quantity-d.shipped_quantity_bu else 0 end ),0),3) as t13, "
			+ " round(nvl(sum(case when trunc(d.request_date)=trunc(sysdate)+14 then d.quantity-d.shipped_quantity_bu else 0 end ),0),3) as t14, "
			+ " round(nvl(sum(case when trunc(d.request_date)=trunc(sysdate)+15 then d.quantity-d.shipped_quantity_bu else 0 end ),0),3) as t15 "
			+ " from reserved_order_detail d inner join reserved_order o on o.id =d.reserved_order_id "
			+ " where o.ydlx in ('"+WmsReservedOrderType.Z01+"','"+WmsReservedOrderType.Z03+"','"+WmsReservedOrderType.Z311+"') " //发货类型预留
			+ " and trunc(d.request_date) <=trunc(sysdate)+15 "
			+ " and d.quantity-d.shipped_quantity_bu>0 "
			+ " group by d.item_id,d.factory_id ";

		List<Map<String,Object>> ylinfos = jdbcTemplate.queryForList(sql);
		for(Map<String,Object> m : ylinfos) {
			Long itemId = Long.valueOf((m.get("itemid"))+"");//物料ID
			Long facId = Long.valueOf((m.get("factoryid"))+"");//工厂ID
			
			Double t1 = Double.valueOf((m.get("t1"))+"");
			Double t2 = Double.valueOf((m.get("t2"))+"");
			Double t3 = Double.valueOf((m.get("t3"))+"");
			Double t4 = Double.valueOf((m.get("t4"))+"");
			Double t5 = Double.valueOf((m.get("t5"))+"");
			Double t6 = Double.valueOf((m.get("t6"))+"");
			Double t7 = Double.valueOf((m.get("t7"))+"");
			Double t8 = Double.valueOf((m.get("t8"))+"");
			Double t9 = Double.valueOf((m.get("t9"))+"");
			Double t10 = Double.valueOf((m.get("t10"))+"");
			Double t11 = Double.valueOf((m.get("t11"))+"");
			Double t12 = Double.valueOf((m.get("t12"))+"");
			Double t13 = Double.valueOf((m.get("t13"))+"");
			Double t14 = Double.valueOf((m.get("t14"))+"");
			Double t15 = Double.valueOf((m.get("t15"))+"");
			
			String key = facId + CommonHelper.VTRADEX_SPLIT_STR + itemId;
			if(!result.containsKey(key)) {
				result.put(key, getInventoryTrendMap(facId,itemId));
			}
			Map map = result.get(key);
			map.put("YLT1", t1);
			map.put("YLT2", t2);
			map.put("YLT3", t3);
			map.put("YLT4", t4);
			map.put("YLT5", t5);
			map.put("YLT6", t6);
			map.put("YLT7", t7);
			map.put("YLT8", t8);
			map.put("YLT9", t9);
			map.put("YLT10", t10);
			map.put("YLT11", t11);
			map.put("YLT12", t12);
			map.put("YLT13", t13);
			map.put("YLT14", t14);
			map.put("YLT15", t15);
		}
		
//		//取T-1预留情况 T-1包含以前
//		String hql = "select w.item.id,w.factory.id,sum(w.quantity-w.shippedQuantityBu) from WmsReservedOrderDetail w "
//				+ "where w.reservedOrder.jzrq<=:date "
//				+ "group by w.item.id,w.factory.id";
//		List<Object[]> ylobjs = commonDao.findByQuery(hql,"date",t_1);
//		for(Object[] obj : ylobjs){
//			Long itemId = (Long)obj[0];
//			Long facId = (Long)obj[1];
//			Double ylQty = StringHelper.replaceNullToZero((Double)obj[2]);//预留需求数量
//			
//			String key = facId + CommonHelper.VTRADEX_SPLIT_STR + itemId;
//			if(!result.containsKey(key)) {
//				Map<String,Object> submap =new HashMap<String,Object>();
//				submap.put("GC", facId); //工厂ID
//				submap.put("WL", itemId); //物料ID
//				submap.put("GD", 0D); //工单数量
//				submap.put("YL", 0D); //预留数量
//				submap.put("JSKC", 0D); //寄售库存数据量
//				submap.put("BZKC", 0D); //标准库存数量
//				result.put(key, submap);
//			}
//			Map map = result.get(key);
//			map.put("YL", ylQty);
//		}
		
		CommonHelper.log("开始取当前库存数据");
		//取当前库存情况
			//标准/寄售 	库存数量
		List<Map<String,Object>> infos = countInventoryByXmlb(null, null);
		
		String hql2 = "from WmsSapFactory";
		List<WmsSapFactory> fs = commonDao.findByQuery(hql2);
		Map<String,Long> fs_id = new HashMap<String,Long>();
		for(WmsSapFactory s : fs) {
			fs_id.put(s.getCode(), s.getId());
		}
		for(Map<String,Object> m : infos) {
			Long itemId = Long.valueOf((m.get("itemid"))+"");//物料ID
			String facCode = (m.get("faccode"))+"";//工厂code
			
			Long facId = (Long)fs_id.get(facCode);
			
			Double bzQty = Double.valueOf((m.get("bzkc"))+"");//标准库存
			Double jsQty = Double.valueOf((m.get("jskc"))+"");//寄售库存
			
			String key = facId + CommonHelper.VTRADEX_SPLIT_STR + itemId;
			if(!result.containsKey(key)) {
//				Map<String,Object> submap =new HashMap<String,Object>();
//				submap.put("GC", facId); //工厂ID
//				submap.put("WL", itemId); //物料ID
//				submap.put("GD", 0D); //工单数量
//				submap.put("YL", 0D); //预留数量
//				submap.put("JSKC", 0D); //寄售库存数据量
//				submap.put("BZKC", 0D); //标准库存数量
//				result.put(key, submap);
				result.put(key, getInventoryTrendMap(facId, itemId));
			}
			Map map = result.get(key);
			map.put("JSKC", jsQty);
			map.put("BZKC", bzQty);
			
		}
		
		CommonHelper.log("开始组装插入数据");
		Set<String> keys = result.keySet();
		for(String key : keys) {
			
			Map<String,Object> submap = result.get(key);
			
			Long facId = (Long)submap.get("GC");
			Long itemId = (Long)submap.get("WL");
			
//			Double bzQty  = (Double)submap.get("BZKC");
//			Double jsQty  = (Double)submap.get("JSKC");
//			Double gdQty  = (Double)submap.get("GD");
//			Double ylQty  = (Double)submap.get("YL");
			//组装
			WmsSapFactory s = commonDao.load(WmsSapFactory.class, facId);
			
//			WmsInventoryTrend wit = new WmsInventoryTrend(date, s, 
//					commonDao.load(WmsItem.class, itemId), bzQty, jsQty, gdQty, ylQty);
			
			WmsInventoryTrend wit = EntityFactory.getEntity(WmsInventoryTrend.class);
			wit.setNeedDate(date);
			wit.setFactory(s);
			wit.setItem(commonDao.load(WmsItem.class, itemId));
			wit.setInvBZQty((Double)submap.get("BZKC"));
			wit.setInvJSQty((Double)submap.get("JSKC"));
			
			wit.setT1ProQty((Double)submap.get("GDT1"));
			wit.setT2ProQty((Double)submap.get("GDT2"));
			wit.setT3ProQty((Double)submap.get("GDT3"));
			wit.setT4ProQty((Double)submap.get("GDT4"));
			wit.setT5ProQty((Double)submap.get("GDT5"));
			wit.setT6ProQty((Double)submap.get("GDT6"));
			wit.setT7ProQty((Double)submap.get("GDT7"));
			wit.setT8ProQty((Double)submap.get("GDT8"));
			wit.setT9ProQty((Double)submap.get("GDT9"));
			wit.setT10ProQty((Double)submap.get("GDT10"));
			wit.setT11ProQty((Double)submap.get("GDT11"));
			wit.setT12ProQty((Double)submap.get("GDT12"));
			wit.setT13ProQty((Double)submap.get("GDT13"));
			wit.setT14ProQty((Double)submap.get("GDT14"));
			wit.setT15ProQty((Double)submap.get("GDT15"));
			
			wit.setT1ResQty((Double)submap.get("YLT1"));
			wit.setT2ResQty((Double)submap.get("YLT2"));
			wit.setT3ResQty((Double)submap.get("YLT3"));
			wit.setT4ResQty((Double)submap.get("YLT4"));
			wit.setT5ResQty((Double)submap.get("YLT5"));
			wit.setT6ResQty((Double)submap.get("YLT6"));
			wit.setT7ResQty((Double)submap.get("YLT7"));
			wit.setT8ResQty((Double)submap.get("YLT8"));
			wit.setT9ResQty((Double)submap.get("YLT9"));
			wit.setT10ResQty((Double)submap.get("YLT10"));
			wit.setT11ResQty((Double)submap.get("YLT11"));
			wit.setT12ResQty((Double)submap.get("YLT12"));
			wit.setT13ResQty((Double)submap.get("YLT13"));
			wit.setT14ResQty((Double)submap.get("YLT14"));
			wit.setT15ResQty((Double)submap.get("YLT15"));
			
			
			commonDao.store(wit);
			
 
		}
		
		
		
		
		
//		String hql = "select d.item.id,d.productionOrder.factory.id,sum(d.planQuantityBu-d.shippedQuantityBu) "
//				+ "from ProductionOrderDetail d where d.productionOrder.beginDate<=:date "
//				+ "group by d.item.id,d.productionOrder.factory.id";
//		List<Object[]> objs = commonDao.findByQuery(hql,"date",t_1);
//		for(Object[] obj : objs){
//			Long itemId = (Long)obj[0];
//			Long facId = (Long)obj[1];
//			Double gdQty = StringHelper.replaceNullToZero((Double)obj[2]);//工单需求数量
//			
//			WmsSapFactory factory = commonDao.load(WmsSapFactory.class, facId);
//			WmsItem item = commonDao.load(WmsItem.class, itemId);
//			//标准/寄售 	库存数量
//			List<Map<String,Double>> invQtys = countInventoryByXmlb(itemId, factory.getCode());
//			Double bzQty = Double.valueOf((invQtys.get(0).get("bzkc"))+"");//标准库存
//			Double jsQty = Double.valueOf((invQtys.get(0).get("jskc"))+"");//寄售库存
//			hql = "select sum(w.quantity-w.shippedQuantityBu) from WmsReservedOrderDetail w "
//					+ "where w.item.id=:id and w.factory.id=:facId and w.reservedOrder.jzrq<=:date";
//			Double ylQty = (Double)commonDao.findByQueryUniqueResult(hql,
//					new String[]{"id","facId","date"},new Object[]{itemId,facId,t_1});
//			ylQty = StringHelper.replaceNullToZero(ylQty);
//			
//			WmsInventoryTrend wit = new WmsInventoryTrend(date, factory, 
//									item, bzQty, jsQty, gdQty, ylQty);
//			commonDao.store(wit);
//		}
	}
	
	private String markBatchData(List<Long> productionList){
		String unicode = StringHelper.getUUID();//标识
		
		String hql = "update ProductionOrder p set p.batchMark=:batchMark where id in (:ids)";
		String[] params =  new String[]{"batchMark","ids"};
		
		int maxSize = productionList.size();
		
		int bachSize=950; //处理oracle 的in大于等于1000报错的问题
		
		if(maxSize < bachSize){
			commonDao.executeByHql(hql,params,new Object[]{unicode,productionList});
		}
		else{//数据超过bachSize,则拆分更新
			List<Long> batchData = new ArrayList<Long>();//900条数据更新一次
			for(Long id : productionList){
				if(batchData.size()>=bachSize) {
					commonDao.executeByHql(hql,params,new Object[]{unicode,batchData});
					batchData.clear();//清空list,准备处理下一批数据
				}
				else {
					batchData.add(id);
				}
			}
			if(batchData.size() > 0){//如果还有数据,继续更新
				commonDao.executeByHql(hql,params,new Object[]{unicode,batchData});
			}
		}
		return unicode;
	}
}
