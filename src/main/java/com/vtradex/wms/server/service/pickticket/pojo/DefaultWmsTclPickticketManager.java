package com.vtradex.wms.server.service.pickticket.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.vtradex.engine.exception.EcadBaseException;
import com.vtradex.rf.common.exception.RfBusinessException;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.security.ThornBaseOrganization;
import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.wms.server.enumeration.WarehouseEnumeration;
import com.vtradex.wms.server.enumeration.WmsSapFactoryCodeEnum;
import com.vtradex.wms.server.model.entity.base.WmsCustomer;
import com.vtradex.wms.server.model.entity.base.WmsFactoryWarehouse;
import com.vtradex.wms.server.model.entity.base.WmsFactoryXmlb;
import com.vtradex.wms.server.model.entity.base.WmsItemFactory;
import com.vtradex.wms.server.model.entity.base.WmsItemKeeper;
import com.vtradex.wms.server.model.entity.base.WmsSapWarehouseStatus;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.model.entity.base.WmsSystemValues;
import com.vtradex.wms.server.model.entity.base.WmsSystemValuesType;
import com.vtradex.wms.server.model.entity.bol.WmsBol;
import com.vtradex.wms.server.model.entity.bol.WmsBolDetail;
import com.vtradex.wms.server.model.entity.bol.WmsBolStatus;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.item.WmsBillType;
import com.vtradex.wms.server.model.entity.item.WmsCompany;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemKey;
import com.vtradex.wms.server.model.entity.item.WmsItemUnPackingAtt;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.item.WmsWarehouseCompany;
import com.vtradex.wms.server.model.entity.kitting.WmsKittingDoc;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetailRequire;
import com.vtradex.wms.server.model.entity.pickticket.WmsWaveDoc;
import com.vtradex.wms.server.model.entity.production.AssignmentStatus;
import com.vtradex.wms.server.model.entity.production.DeliveryOrderDetailPtDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrder;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetailPtDetail;
import com.vtradex.wms.server.model.entity.production.ReservedOrderDetailPtDetail;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrder;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderDetail;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;
import com.vtradex.wms.server.model.entity.workdoc.WmsWorkDoc;
import com.vtradex.wms.server.model.enums.BaseStatus;
import com.vtradex.wms.server.model.enums.WmsBillOfType;
import com.vtradex.wms.server.model.enums.WmsLocationCode;
import com.vtradex.wms.server.model.enums.WmsLocationType;
import com.vtradex.wms.server.model.enums.WmsLotCategoryType;
import com.vtradex.wms.server.model.enums.WmsPickSource;
import com.vtradex.wms.server.model.enums.WmsPickTicketStatus;
import com.vtradex.wms.server.model.enums.WmsPickticketBillTypeCode;
import com.vtradex.wms.server.model.enums.WmsPickticketGenType;
import com.vtradex.wms.server.model.enums.WmsSOQueryRequireType;
import com.vtradex.wms.server.model.enums.WmsTaskStatus;
import com.vtradex.wms.server.model.enums.WmsWaveDocStatus;
import com.vtradex.wms.server.model.enums.WmsWorkDocStatus;
import com.vtradex.wms.server.model.enums.WmsWorkDocType;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.message.WmsCustomerManager;
import com.vtradex.wms.server.service.order.WmsReservedOrderManager;
import com.vtradex.wms.server.service.pickticket.WmsPickticketManager;
import com.vtradex.wms.server.service.pickticket.WmsTclPickticketManager;
import com.vtradex.wms.server.service.production.ProductionOrderManager;
import com.vtradex.wms.server.service.replenish.WmsMoveDocReplenishmentManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.workdoc.WmsTclWorkDocManager;
import com.vtradex.wms.server.service.workdoc.WmsTransactionalManager;
import com.vtradex.wms.server.utils.BeanUtils;
import com.vtradex.wms.server.utils.DateUtil;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.PackageUtils;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.server.utils.WmsPackageUnitUtils;
import com.vtradex.wms.webservice.utils.CommonHelper;

/**
 * 
 * tcl定制化拣货单业务 
 * 分配与取消分配修改回写相应单据
 * 
 * @author <a href="zhen.lei@vtradex.com">Yogurt_lei</a>
 * 
 * @date 2017年7月18日 下午4:03:32
 */
public class DefaultWmsTclPickticketManager extends DefaultWmsPickticketManager implements WmsTclPickticketManager{
    private WorkflowManager workflowManager;
    private WmsRuleManager wmsRuleManager;
    private WmsTransactionalManager wmsTransactionalManager;
    private WmsBussinessCodeManager wmsBussinessCodeManager;
    
    public DefaultWmsTclPickticketManager(
            WorkflowManager workflowManager,
            WmsInventoryManager inventoryManager,
            WmsBussinessCodeManager wmsBussinessCodeManager,
            WmsRuleManager wmsRuleManager,
            WmsCustomerManager wmsCustomerManager,
            WmsTransactionalManager wmsTransactionalManager,
            WmsMoveDocReplenishmentManager wmsMoveDocReplenishmentManager) {
        super(workflowManager, inventoryManager, wmsBussinessCodeManager,
                wmsRuleManager, wmsCustomerManager, wmsTransactionalManager,
                wmsMoveDocReplenishmentManager);
        this.workflowManager = workflowManager;
        this.wmsRuleManager = wmsRuleManager;
        this.wmsTransactionalManager = wmsTransactionalManager;
        this.wmsBussinessCodeManager = wmsBussinessCodeManager;
    }
    
    @Override
    public void manualCancelAllocate(Map<Long, Double> cancelInfo) {
        for (Long key : cancelInfo.keySet()) {
            WmsTask task = commonDao.load(WmsTask.class, key);
            if(task.getStatus().equals(WmsTaskStatus.IN_OPERATION)||task.getStatus().equals(WmsTaskStatus.FINISH)){
                throw new BusinessException("task.have.already.started");
            }
            Double qty = cancelInfo.get(key);
            qty = CommonHelper.dealDoubleError(qty);
            if(task.getUnmovedQuantityBU() < qty){
                throw new BusinessException("qty.is.greater.than.unmovedQuantityBU");
            }
            
            Double packQty = WmsPackageUnitUtils.getPackQty(task.getPackageUnit(), qty, task.getItem().getBuPrecision());
            
            inventoryManager.cancelPlanIn(task, qty);
            task.setInvRelatedBillType(WmsBillOfType.PT);
            inventoryManager.cancelPlanOut(task, qty);
            
            Long pickTicketDetailId = task.getRelatedObjId();
            
            task.planQty(-qty,-packQty);//扣除计划移位包装数量和移位数量
            if(task.getPlanQty().doubleValue() <= 0){
                boolean deleteWorkDoc = false;
                WmsWorkDoc workDoc = null;
                if(task.getWorkDoc()!=null && task.getWorkDoc().getQty().doubleValue() <= 0){
                    deleteWorkDoc = true;
                    workDoc = task.getWorkDoc();
                }else if (task.getWorkDoc()!=null && task.getWorkDoc().getQty().doubleValue() > 0){
                    workDoc = this.commonDao.load(WmsWorkDoc.class, task.getWorkDoc().getId());
                    if(workDoc.getQty().doubleValue() <= workDoc.getPickedQty().doubleValue()){
                        workDoc.setStatus(WmsWorkDocStatus.FINISH);
                        this.commonDao.store(workDoc);
                    }
                }
                commonDao.delete(task);
                if(deleteWorkDoc){
                    commonDao.delete(workDoc);
                }
            }
            
            WmsPickTicketDetail detail = commonDao.load(WmsPickTicketDetail.class, pickTicketDetailId);
            detail.allocate(-qty);
            this.tclCustomCancelAllocate(detail, qty);
            // 取消分配 回写tcl单据
            commonDao.store(detail);
            
            workflowManager.doWorkflow(detail.getPickTicket(),"wmsPickTicketProcess.cancelSingle");
            if(detail.getPickTicket().getWaveDoc()!=null){
                //如果波次单状态为生效/作业中/完成,则不允许进行取消分配
                WmsWaveDoc waveDoc = this.commonDao.load(WmsWaveDoc.class, detail.getPickTicket().getWaveDoc().getId());
                if(!WmsWaveDocStatus.OPEN.equals(waveDoc.getStatus())&&!WmsWaveDocStatus.PARTALLOCATED.equals(waveDoc.getStatus())
                        &&!WmsWaveDocStatus.ALLOCATED.equals(waveDoc.getStatus())){
                    throw new BusinessException("this.wavedoc.is.already.worked.can.not.be.cancelAllocate");
                }
                workflowManager.doWorkflow(detail.getPickTicket().getWaveDoc(),"wmsWaveDocProcess.cancelAllocate");
            }
        }
    }

    /*
    * 拣货单取消分配时修改工单明细分配数量  生产订单id小到大+生产订单明细id小到大
    * 
    * @param pickTicketDetail 拣货单明细
    * @param allocateQty 分配数量
    *
    * @author Yogurt_lei
    *
    * @date 2017年7月18日 下午4:47:57
    */
    private void tclCustomCancelAllocate(WmsPickTicketDetail pickTicketDetail, Double cancelAllocateQty) {
        WmsPickTicket pt = pickTicketDetail.getPickTicket();
	   	if(pt.getBillType().getCode().equals(WmsPickticketGenType.SCLLD)) {
		   	 List<Long> podptdIds =  commonDao.findByQuery("SELECT pt.id FROM ProductionOrderDetailPtDetail pt "
		             + "LEFT JOIN pt.productionOrderDetail pod "
		             + "WHERE pt.pickticketDetail.id=:ptID AND pod.allocatedQuantityBu-pod.pickedQuantityBu>0"
		             + "ORDER BY pod.productionOrder.id, pod.id", 
		                 "ptID", pickTicketDetail.getId());
		     
		     for (Long podptdId : podptdIds) {
		    	 ProductionOrderDetailPtDetail p = commonDao.load(ProductionOrderDetailPtDetail.class, podptdId);
		         ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, p.getProductionOrderDetail().getId());
		         Double allocateQty = pod.getAllocatedQuantityBu()-pod.getPickedQuantityBu();//可取消分配的数量
		         cancelAllocateQty = CommonHelper.dealDoubleError(cancelAllocateQty);
		         allocateQty =  CommonHelper.dealDoubleError(allocateQty);
		         if (cancelAllocateQty <= 0) {
		             break;
		         }
		         
		         if(allocateQty > cancelAllocateQty){
		             CommonHelper.log("拣货单取消分配修改工单明细("+pod.getId()+")："+ cancelAllocateQty);
		             pod.subAllocatedQuantityBu(cancelAllocateQty);
		             p.setLastAllocatedQty(DoubleUtils.sub(p.getLastAllocatedQty(), cancelAllocateQty));
		             cancelAllocateQty = 0D;
		         }else{
		             CommonHelper.log("拣货单取消分配修改工单明细("+pod.getId()+")："+ allocateQty);
		             pod.subAllocatedQuantityBu(allocateQty);
		             p.setLastAllocatedQty(DoubleUtils.sub(p.getLastAllocatedQty(), allocateQty));
		             cancelAllocateQty = DoubleUtils.sub(cancelAllocateQty, allocateQty);
		         }
		         pod.defineStatus();//更新分配状态
		         commonDao.store(pod);
		         commonDao.store(p);
		     }
		     if(CommonHelper.dealDoubleError(cancelAllocateQty)>0d) {
		    	 throw new BusinessException("剩余"+CommonHelper.dealDoubleError(cancelAllocateQty)+"的取消数量无法找到对应的工单");
		     }
	   	}
	   	else if(pt.getBillType().getCode().equals(WmsPickticketGenType.YLCKD)){ //预留出库单
	   		List<Long> podIds =  commonDao.findByQuery("SELECT pod.id FROM ReservedOrderDetailPtDetail pt "
		             + "LEFT JOIN pt.reservedOrderDetail pod "
		             + "WHERE pt.pickticketDetail.id=:ptID AND pod.allocatedQuantityBu-pod.pickedQuantityBu>0"
		             + "ORDER BY pod.reservedOrder.id, pod.id DESC", 
		                 "ptID", pickTicketDetail.getId());
		     
		     for (Long podId : podIds) {
		    	 WmsReservedOrderDetail pod = commonDao.load(WmsReservedOrderDetail.class, podId);
		         Double allocateQty = pod.getAllocatedQuantityBu()-pod.getPickedQuantityBu();//可取消分配数量
		         
		         if (cancelAllocateQty <= 0) {
		             break;
		         }
		         
		         if(allocateQty > cancelAllocateQty){
		             CommonHelper.log("拣货单取消分配修改预留明细("+podId+")："+ cancelAllocateQty);
		             pod.subAllocatedQuantityBu(cancelAllocateQty);
		             cancelAllocateQty = 0D;
		         }else{
		             CommonHelper.log("拣货单取消分配修改预留明细("+podId+")："+ allocateQty);
		             pod.subAllocatedQuantityBu(allocateQty);
		             cancelAllocateQty = DoubleUtils.sub(cancelAllocateQty, allocateQty);
		         }
		         
		     }
		        
	   	}
       
   }
    
    /**
     * @author haibin.deng
     * @Description 拣货单转换仓库 根据生产单明细 ProductionOrderDetailPtDetail
     */
    public void pickTicketconvertWarehouse(WmsPickTicket wmsPickTicket){
    	
    	if (wmsPickTicket.getDetails().isEmpty()) {
    		throw new BusinessException("WmsPickTicketDetail.can.not.be.empty");
    	}
    	ProductionOrderDetailPtDetail podd = null;//生产计划拣货单关系表
    	ReservedOrderDetailPtDetail rodd = null;//预留单拣货单关系表
    	String billCode = wmsPickTicket.getBillType().getCode();
    	for (WmsPickTicketDetail pickTicketDetail : wmsPickTicket.getDetails()) {
    		if(billCode.equals(WmsPickticketGenType.SCLLD)){//生产领料单
    			podd = findProductionOrderDetailPtDetailByPTDetail(pickTicketDetail);
        		if (null != podd) {
        			break;
        		}
    		}else if(billCode.equals(WmsPickticketGenType.YLCKD)){//预留出库单
    			rodd = findReservedOrderDetailPtDetailByPTDetail(pickTicketDetail);
    			if (null != rodd) {
        			break;
        		}
    		}
    		
		}
    	if (null == podd && billCode.equals(WmsPickticketGenType.SCLLD)) {
    		throw new BusinessException("find.productionOrderDetailPtDetail.by.pickTicketDetail.is.empty");
    	}else if(null == rodd && billCode.equals(WmsPickticketGenType.YLCKD)){
    		throw new BusinessException("find.ReservedOrderDetailPtDetail.by.pickTicketDetail.is.empty");
    	}
    	WmsFactoryWarehouse factoryWarehouse = null;
    	String factoryName = null;
    	WmsLocation location = null;
    	WmsWarehouse convertWarehouse = null;
    	if(billCode.equals(WmsPickticketGenType.SCLLD)){
    		
    		ProductionOrder productionOrder = podd.getProductionOrderDetail().getProductionOrder();
    		factoryWarehouse = findWmsFactoryWarehouse(productionOrder.getFactory().getId());
    		factoryName = productionOrder.getFactory().getName();
    		convertWarehouse = factoryWarehouse.getWarehouse();
    		location = findLocation(convertWarehouse.getId());
    		
    	}else if(billCode.equals(WmsPickticketGenType.YLCKD)){
    		
    		WmsReservedOrder reservedOrder = rodd.getReservedOrderDetail().getReservedOrder();
    		factoryWarehouse = findWmsFactoryWarehouse(reservedOrder.getFactory().getId());
    		factoryName = reservedOrder.getFactory().getName();
    		convertWarehouse = factoryWarehouse.getWarehouse();
    		location = findLocationByWareHouse(convertWarehouse.getId());
    	}
    	
    	if (null == factoryWarehouse) {
    		throw new BusinessException("find.WmsFactoryWarehouse.by.factory.to.warehouse.is.empty",new String[]{factoryName});
    	}
    	wmsPickTicket.setWarehouse(convertWarehouse);
    	wmsPickTicket.setShipLocation(location);
    	this.commonDao.store(wmsPickTicket);
    	LocalizedMessage.addMessage("单号: "+wmsPickTicket.getCode()+" 已转到仓库: "+convertWarehouse.getName());
    }
    
	public WmsFactoryWarehouse findWmsFactoryWarehouse(Long factoryId){
		String hql = "  FROM WmsFactoryWarehouse fw WHERE fw.factory.id =:factoryId "
						+ " AND fw.type =:type";
		return  (WmsFactoryWarehouse) this.commonDao.findByQueryUniqueResult(hql, new String[]{"factoryId","type"}, new Object[]{factoryId,WmsFactoryXmlb.BZ});
	}
    
    @SuppressWarnings("unchecked")
	public ProductionOrderDetailPtDetail findProductionOrderDetailPtDetailByPTDetail(WmsPickTicketDetail PTDetail){
    	String hql = " FROM ProductionOrderDetailPtDetail podd WHERE podd.pickticketDetail.id =:pickticketDetailId";
    	List<ProductionOrderDetailPtDetail> poddList = this.commonDao.findByQuery(hql, "pickticketDetailId", PTDetail.getId());
    	if (poddList.isEmpty()) {
    		throw new BusinessException("not.found.ProductionOrderDetailPtDetail.by.WmsPickTicketDetail");
    	} else {
    		return poddList.get(0);
    	}
    	
    }
    
    public ReservedOrderDetailPtDetail findReservedOrderDetailPtDetailByPTDetail(WmsPickTicketDetail PTDetail){
    	String hql = " FROM ReservedOrderDetailPtDetail rodd WHERE rodd.pickticketDetail.id =:pickticketDetailId";
    	List<ReservedOrderDetailPtDetail> roddList = this.commonDao.findByQuery(hql, "pickticketDetailId", PTDetail.getId());
    	if (roddList.isEmpty()) {
    		throw new BusinessException("not.found.ReservedOrderDetailPtDetail.by.WmsPickTicketDetail");
    	} else {
    		return roddList.get(0);
    	}
    }
    /**
	 * 自动分配 整单
	 * @Description:
	 * @Author:        <a href="yequan.song@vtradex.net">宋叶全</a>
	 * @CreateDate:    2015年12月15日
	 * @param pickTicket:
	 * @arithMetic:
	 * @exception:
	 */
	public void autoAllocate(WmsPickTicket pickTicket){
	    StringBuffer buf = new StringBuffer();
		String hql = "FROM WmsPickTicketDetail detail WHERE detail.pickTicket.id =:pickTicketId and detail.expectedQty-detail.allocatedQty>0 ";
		List<WmsPickTicketDetail> detaillist = commonDao.findByQuery(hql, "pickTicketId", pickTicket.getId());
		if(detaillist.isEmpty()){
			System.out.println("11111");
		}
		for(WmsPickTicketDetail detail : detaillist){
		    try{
		    	this.autoAllocate(detail);	    	
			} catch (BusinessException be) {
				LocalizedMessage.setMessage(null);
				logger.error("", be);
				buf.append(be.getMessage() + "\n");
			} catch (Exception e) {
				logger.error("", e);
				buf.append(e.getMessage() + "\n");
			}
		}
		if(buf.length() > 0){
			LocalizedMessage.addLocalizedMessage(buf.toString());
		}
		
		
		pickTicket = commonDao.load(WmsPickTicket.class, pickTicket.getId());
		WmsWarehouse w = commonDao.load(WmsWarehouse.class, pickTicket.getWarehouse().getId());
		//调用供货仓拉动补货
		if(!"VMI".equals(w.getCode())) {
			//供货仓拉动补货
			if( StringHelper.in(pickTicket.getStatus(), new String[]{WmsPickTicketStatus.OPEN,WmsPickTicketStatus.PARTALLOCATED}) && pickTicket.getQty()>0) {
				 if(!StringHelper.isNullOrEmpty(pickTicket.getUserField3()) && pickTicket.getUserField3().equals(WmsPickticketGenType.XSJHD)){
					 return;
				 }
//				WmsPickticketManager ptm = (WmsPickticketManager)applicationContext.getBean("wmsPickticketManager");
//				ptm.supplyWarehouseReplenish(pickTicket); //需要事务
				WmsTclPickticketManager ptm = (WmsTclPickticketManager)applicationContext.getBean("wmsTclPickticketManager");
				ptm.supplyWarehouseReplenishNoTransaction(pickTicket); //无事务
			}
			
		}
	}
    @SuppressWarnings("unchecked")
	@Override
    public void autoAllocate(WmsPickTicketDetail pickTicketDetail) {

        pickTicketDetail = this.commonDao.load(WmsPickTicketDetail.class,
                pickTicketDetail.getId());
        WmsPickTicket pickTicket = this.commonDao.load(WmsPickTicket.class, pickTicketDetail.getPickTicket().getId());
        //校验
        if (pickTicketDetail.getUnAllocateQty() <= 0){
            return;
        }
        
        WmsLocation shipLocation = null; 
        if(pickTicket.getShipLocation() == null){
            throw new BusinessException("pickTicket.shipLocation.can.not.be.null", new String[]{pickTicket.getCode()});
        }else{
            shipLocation = commonDao.load(WmsLocation.class, pickTicket.getShipLocation().getId());
        }

        WmsWarehouse warehouse = this.commonDao.load(WmsWarehouse.class, pickTicket.getWarehouse().getId());
        ThornBaseOrganization baserOrganization = this.commonDao.load(ThornBaseOrganization.class, warehouse.getBaseOrganization().getId());
        WmsCompany company = this.commonDao.load(WmsCompany.class,pickTicket.getCompany().getId());
        WmsBillType billType = this.commonDao.load(WmsBillType.class, pickTicket.getBillType().getId());
        WmsItem item = this.commonDao.load(WmsItem.class, pickTicketDetail.getItem().getId());
        WmsPackageUnit unit = this.commonDao.load(WmsPackageUnit.class, pickTicketDetail.getPackageUnit().getId());
        Map<Long, Double> allocateInfo = new LinkedHashMap<Long, Double>();
        Map<String, Object> problem = new HashMap<String, Object>();
        
        problem.put("组织", baserOrganization.getId());
        problem.put("模型", baserOrganization.getBusinessModel().getId());
        problem.put("仓库ID", warehouse.getId());
        problem.put("货主ID", company.getId());
        problem.put("货主名称", company.getName());
        problem.put("单据类型", billType.getName());
        problem.put("拣货组", item.getAllocationGroup());
        problem.put("包装级别", unit.getUnitLevel());
        problem.put("库存状态", pickTicketDetail.getInventoryStatus());
        problem.put("待拣选数量", pickTicketDetail.getUnAllocateQty());
        problem.put("数量", pickTicketDetail.getExpectedQty());
        problem.put("货品ID", item.getId());
        problem.put("是否越库", pickTicket.getAllowCross()?"是":"否");
        problem.put("拣货单号", pickTicket.getCode());  //增加捡货单号，查询是否有对应生产单的生产线
        problem.put("相关单号", pickTicket.getRelatedBill1());  //增加相关单号，如果是工单需要从退拣仓先出
        problem.put("产线", pickTicket.getUserField7() ==null ? "-" : pickTicket.getUserField7());//产线
        
        List<WmsPickTicketDetailRequire> reqs = commonDao.findByQuery(" FROM WmsPickTicketDetailRequire r "
                + "WHERE r.pickTicketDetail.id =:ptdId ", "ptdId", pickTicketDetail.getId());
        
        List<Map<String,Object>> lotList = new ArrayList<Map<String,Object>>();
        for(WmsPickTicketDetailRequire req:reqs){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("要求1", req.getLotValue1());
            map.put("要求2", req.getLotValue2());
            map.put("要求3", req.getLotValue3());
            map.put("要求4", req.getLotValue4());
            map.put("要求5", req.getLotValue5());
            map.put("批次属性", req.getLotKey());
            map.put("指定级别", req.getLevel());
            map.put("查询要求", req.getQueryRequire());
            lotList.add(map);
        }
        
        problem.put("拣货批次要求列表", lotList);
        String splitValue = item.getUserFieldV3() == null ? "" : item.getUserFieldV3();
        if(!billType.getCode().equals(WmsPickticketGenType.SCLLD)){//如果不是生产领料单,不可拆包物料和其它物料的分配逻辑一样
        	splitValue = "";
        }
        problem.put("拆包属性",splitValue);
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        int flag = 0;
        //根据收货人组查询规则表调用拣货分配规则
        if(pickTicket.getCustomer()!=null){     
            WmsCustomer customer = this.commonDao.load(WmsCustomer.class, pickTicket.getCustomer().getId());
            String customerGroup = customer.getCustomerGroup();
            if(!StringUtils.isEmpty(customerGroup)){
                Map<String, Object> value = new HashMap<String, Object>();
                try{
                 value = wmsRuleManager.getRuleTableDetail(
                        "R101_收货人组_拣货规则配置规则表", customerGroup);
                }catch(Exception e){
                    throw new BusinessException(e.getMessage());
                }
                if(value!=null&&!"".equals(value)){
                    String mainRule =  "R101_"+value.get("拣货分配规则").toString();
                    result = wmsRuleManager.execute(baserOrganization, value.get("拣货分配规则").toString(), mainRule, problem);
                    flag = 1;
                }
            }
        }
       if(flag==0){
           result = wmsRuleManager.execute(baserOrganization, "拣货分配规则", "R101_拣货分配规则", problem);
       }
        
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("返回列表");
        for(Map<String, Object> obj : resultList){
        	Long inventoryId = (Long)obj.get("库存ID");
            
            Double qty = Double.valueOf(String.valueOf(obj.get("分配数量")));
            if (allocateInfo.get(inventoryId)==null) {
                allocateInfo.put(inventoryId, qty);
            } else {
                allocateInfo.put(inventoryId, allocateInfo.get(inventoryId)+qty);
            }
        }
        //分配库存
        wmsTransactionalManager.doAutoAllocateResult(pickTicketDetail, allocateInfo, shipLocation);
    }
    
    public WmsRuleManager getWmsRuleManager() {
        return wmsRuleManager;
    }

    public void setWmsRuleManager(WmsRuleManager wmsRuleManager) {
        this.wmsRuleManager = wmsRuleManager;
    }

    public WmsTransactionalManager getWmsTransactionalManager() {
        return wmsTransactionalManager;
    }

    public void setWmsTransactionalManager(
            WmsTransactionalManager wmsTransactionalManager) {
        this.wmsTransactionalManager = wmsTransactionalManager;
    }

	/**供货仓拉动补货*/
	/**无事务主方法*/
	public void supplyWarehouseReplenishNoTransaction(WmsPickTicket wmsPickTicket) {
		WmsBillType bill = commonDao.load(WmsBillType.class, wmsPickTicket.getBillType().getId());
		if((!StringHelper.isNullOrEmpty(wmsPickTicket.getUserField3()) 
				&& wmsPickTicket.getUserField3().equals(WmsPickticketGenType.XSJHD))
				 || bill.getCode().equals(WmsPickticketBillTypeCode.TGYSCK)){
			return ;
		}
		WmsTclPickticketManager ptm = (WmsTclPickticketManager)applicationContext.getBean("wmsTclPickticketManager");
		Map<String,Object> result = null;
		//ptm.tclSupplyWarehouseReplenish 这个方法如果执行失败 会造成错误数据 自管仓都有一张部分分配的PT，这是错误的，所以重试3次，
		//如果3次都失败，系统直接return掉，但是肯定会产生错误数据。
		int sleep=15;
		if(wmsPickTicket.getWarehouse().getId().equals(2L)){//洗衣机
			sleep = 10;
		}else if(wmsPickTicket.getWarehouse().getId().equals(3L)){//冰箱
			sleep =20;
		}
		try{
			result = ptm.tclSupplyWarehouseReplenish(wmsPickTicket); //有事务
		}catch(Exception e){
			try {
				
				Thread.sleep(sleep*1000L); //睡眠15秒 重试
			}
			catch(Exception e1 ){
			}
			try{
				result = ptm.tclSupplyWarehouseReplenish(wmsPickTicket); //有事务
			}catch(Exception e2){
				try {
					Thread.sleep(sleep*2*1000L); //睡眠30秒 重试
				}
				catch(Exception e3 ){
				}
				try{
					result = ptm.tclSupplyWarehouseReplenish(wmsPickTicket); //有事务
				}catch(Exception e3){
					CommonHelper.log("拆拣货单异常，3次处理失败，ptid:"+wmsPickTicket.getId());
					e3.printStackTrace();
					return;
//					throw new BusinessException(e3);
				}
			}
		}
		
		
		wmsPickTicket = (WmsPickTicket)result.get("wmsPickTicket");
		Long supplyPickTicketId = (Long)result.get("supplyPickTicketId");
		Boolean firstLd = (Boolean)result.get("firstLd");
		WmsPickTicket supplyPickTicket = commonDao.load(WmsPickTicket.class, supplyPickTicketId);
		this.autoAllocate(supplyPickTicket); //无事务
		
		//ptm.tclAfterSupplyWarehouseReplenish 这个方法如果执行失败 会造成错误数据 VMI和自管仓都有一张打开的PT，ptd与工单明细的关系在VMI下，这是错误的，所以重试3次，
		//如果3次都失败，系统会打印日志，但是肯定会出现异常数据。
		try {
			ptm.tclAfterSupplyWarehouseReplenish(wmsPickTicket,supplyPickTicket,firstLd); //有事务
		}
		catch(Exception e ) {
			try {
				Thread.sleep(sleep*1000L); //睡眠15秒 重试
			}
			catch(Exception e1 ){
			}
			try {
				ptm.tclAfterSupplyWarehouseReplenish(wmsPickTicket,supplyPickTicket,firstLd); //有事务
			}
			catch(Exception e2 ) {
				try {
					Thread.sleep(sleep*2*1000L);//睡眠30秒 重试
				}
				catch(Exception e1 ){
				}
				try {
					ptm.tclAfterSupplyWarehouseReplenish(wmsPickTicket,supplyPickTicket,firstLd); //有事务
				}
				catch(Exception e3 ) {
					CommonHelper.log("拉动异常，3次处理失败，ptid:"+wmsPickTicket.getId());
					e.printStackTrace();
				}
			}
		}		
		
	}
    
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
	public Map<String,Object> tclSupplyWarehouseReplenish(WmsPickTicket wmsPickTicket) {
//		WmsWarehouse wh = (WmsWarehouse)commonDao.findByQueryUniqueResult("FROM WmsWarehouse warehouse WHERE warehouse.baseOrganization.id = :baseOrganizationId", 
//				new String[] {"baseOrganizationId"}, new Object[] {BaseOrganizationHolder.getThornBaseOrganization().getId()});
		WmsWarehouse wh = commonDao.load(WmsWarehouse.class, wmsPickTicket.getWarehouse().getId());
		
		//供货仓
		WmsWarehouse supplyWarehouse = null;
		if(wh.getSupplyWarehouse() != null && wh.getSupplyWarehouse().getId() != null) {
			supplyWarehouse = commonDao.load(WmsWarehouse.class, wh.getSupplyWarehouse().getId());
		}
		//供货仓是否维护 or 供货仓是否有效状态 or 单据为可执行
		if(supplyWarehouse == null) {
			throw new BusinessException("this.supply.warehouse.is.not.maintain");
		} else if(BaseStatus.DISABLED.equals(supplyWarehouse.getStatus())) {
			throw new BusinessException("this.supply.warehouse.is.disabled");
		} else if(!wmsPickTicket.getIsExecutable()) {
			throw new BusinessException("this.pickticket.is.enexecutable");
		}
		
		WmsPickTicket supplyPickTicket = null; // 供货仓生成的PT  TCL的此单在VMI下。
		WmsPickTicket currentNewPickTicket = null; //第一次仓单拉动新生成的备份的原始关闭的单据。
		//有一个拣货明细的计划数量>已分配数量，生成新拣货单 
		Boolean createPt = Boolean.FALSE;
		for(WmsPickTicketDetail detail : wmsPickTicket.getDetails()){
			if(detail.getExpectedQty()>detail.getAllocatedQty()){
				createPt = Boolean.TRUE;
			}
		}
		if(createPt){
			//创建供货补货拣货单
			supplyPickTicket = createNewSupplyPickTicket(supplyWarehouse, wmsPickTicket,Boolean.TRUE);
		}
//		if(StringHelper.in(wmsPickTicket.getStatus(), new String[]{WmsPickTicketStatus.OPEN,WmsPickTicketStatus.PARTALLOCATED})) {
			
		Boolean firstLd=false; //第一次仓单拉动标识
		if(wmsPickTicket.getOriginalId()==null || wmsPickTicket.getOriginalId().equals(0L)) {
			firstLd = true;
		}
		
		
		//原拣货单已分配数量==0  则直接关闭作为母单 同时不生成currentNewPickTicket
		if(wmsPickTicket.getAllocateQty() == 0D && WmsPickTicketStatus.OPEN.equals(wmsPickTicket.getStatus()) && firstLd) {
			wmsPickTicket.setIsExecutable(Boolean.FALSE);
			wmsPickTicket.setStatus(WmsPickTicketStatus.CLOSED);
		}
		
		
		
		//原拣货单已分配数量>0
		if(wmsPickTicket.getAllocateQty() > 0) {
			
			if(firstLd) {//没有原始ID，说明是第一次做仓库单拉动，需要生成原始的PT备份。
				//当前仓库生成新的拣货单
				currentNewPickTicket = createCarrenPickTicket(wmsPickTicket, wh);
			}
			
			//删除分配数量为0的明细
			deleteCurrentPickTicketDetail(wmsPickTicket);
		}
		if(firstLd) {//第一次仓单拉动
			//设置原始单据id
			if(currentNewPickTicket != null) {
				if(supplyPickTicket != null){
					supplyPickTicket.setOriginalId(currentNewPickTicket.getId());
				}
				wmsPickTicket.setOriginalId(currentNewPickTicket.getId());
			} else {
				if(supplyPickTicket != null){
					supplyPickTicket.setOriginalId(wmsPickTicket.getId());
				}
			}
		}
		else {//不是第一次  不生成currentNewPickTicket
			if(supplyPickTicket != null){
				supplyPickTicket.setOriginalId(wmsPickTicket.getOriginalId());
			}
		}
		
		//不为关闭状态，则重新定义状态
		if(!WmsPickTicketStatus.CLOSED.equals(wmsPickTicket.getStatus())) {
			wmsPickTicket.setStatus(wmsPickTicket.defineStatus());
		}
		this.commonDao.store(wmsPickTicket);
		if(supplyPickTicket != null){
			this.commonDao.store(supplyPickTicket);
		}
		
		/**如果已分配，那么自动生成作业单*/
		if(wmsPickTicket.getStatus().equals(WmsPickTicketStatus.ALLOCATED)){
			createWorkDoc(wmsPickTicket);
		}
		
//		CommonHelper.log(wmsPickTicket.getCode());//老单子  被拆分出来的 部分数量。
//		CommonHelper.log(currentNewPickTicket.getCode());//新生成的被关闭的单子
//		CommonHelper.log(supplyPickTicket.getCode());//新生成的 在另外仓库的 部分数量
//		
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("wmsPickTicket", wmsPickTicket);
		result.put("supplyPickTicketId",supplyPickTicket.getId());
		result.put("firstLd", firstLd);
		
		return result;
		
	
		
		
	}
	public void tclAfterSupplyWarehouseReplenish(WmsPickTicket wmsPickTicket,WmsPickTicket supplyPickTicket,Boolean firstLd) {
		wmsPickTicket = commonDao.load(WmsPickTicket.class, wmsPickTicket.getId());
		supplyPickTicket = commonDao.load(WmsPickTicket.class, supplyPickTicket.getId());
		
		/** 生成的VMI拣货单做自动分配,如果没有分配到库存,则删除VMI拣货单及明细,
		 * 	如果是部分分配,则把未分配到的库存转回到原仓库,并新建一个打开状态的拣货单
		 */
//		autoAllocate(supplyPickTicket);//对VMI的单子做自动分配动作
		WmsWarehouse wh =wmsPickTicket.getWarehouse();
		WmsLocation loc = findLocation(wh.getId());//T1库位
		
		WmsSupplier supplier = wmsPickTicket.getSupplier();//供应商
		supplyPickTicket.setSupplier(supplier);//供应商
		
		if(supplyPickTicket.getStatus().equals(WmsPickTicketStatus.ALLOCATED)
				&& wmsPickTicket.getStatus().equals(WmsPickTicketStatus.OPEN)){
			/**自管仓原始拣货单如果状态为打开,则删除*/
			deletePickRelate(wmsPickTicket);
		}
		
		if(!firstLd) {
			if(supplyPickTicket.getStatus().equals(WmsPickTicketStatus.OPEN)){//VMI没有库存则直接转回原始仓库
				
				//vmi的单子转到自管仓  自管仓的单子删除  因为自管仓的所有明细相关信息已经全部到VMI了
				
				supplyPickTicket.setWarehouse(wmsPickTicket.getWarehouse());
				supplyPickTicket.setShipLocation(wmsPickTicket.getShipLocation());
				String oldCode = wmsPickTicket.getCode();
				commonDao.store(supplyPickTicket);
				
				String hql2 = "from WmsPickTicketDetail a where a.pickTicket.id=:ptid";
				List<WmsPickTicketDetail> dss = commonDao.findByQuery(hql2,new String[]{"ptid"},new Object[]{supplyPickTicket.getId()});
				dealPtdRequire(dss,WmsFactoryXmlb.BZ);//如果拣货单明细的批次属性有项目类别,那么VMI仓设为寄售,其它设为标准
				
				if(wmsPickTicket.getStatus().equals(WmsPickTicketStatus.OPEN)){//如果原始单据为打开  则删除
					/**自管仓原始拣货单如果状态为打开,则删除*/
					deletePickRelate(wmsPickTicket);
					supplyPickTicket.setCode(oldCode);
					
				}
				
				commonDao.store(supplyPickTicket);
			}
			else if(supplyPickTicket.getStatus().equals(WmsPickTicketStatus.PARTALLOCATED)){
				WmsPickTicket pick = createNewSupplyPickTicket(wh,supplyPickTicket,Boolean.TRUE);//创建拣货单->冰箱/洗衣机 状态打开   VMI库存不够的部分  转回自管仓
				pick.setOriginalId(wmsPickTicket.getOriginalId());//全部关联到老单据
				//处理一下单头的关键字段。
				pick.setWarehouse(wmsPickTicket.getWarehouse());
				
				///////////////////////////////////////////////////////////////////////////////////
				//删除分配数量为0的明细
				deleteCurrentPickTicketDetail(supplyPickTicket);
				
				String hql2 = "from WmsPickTicketDetail a where a.pickTicket.id=:ptid";
				List<WmsPickTicketDetail> dss = commonDao.findByQuery(hql2,new String[]{"ptid"},new Object[]{supplyPickTicket.getId()});
				dealPtdRequire(dss,WmsFactoryXmlb.JS);//如果拣货单明细的批次属性有项目类别,那么VMI仓设为寄售,其它设为标准
				
				pick.refreshPickTicketQty();//刷新单头总数量
				WmsBillType bill = commonDao.load(WmsBillType.class, pick.getBillType().getId());
				if(WmsPickticketBillTypeCode.SCLLD.equals(bill.getCode())){
					pick.setShipLocation(loc);//T1库位
				}
				commonDao.store(pick);
				
				String hql = "select nvl(sum(d.expectedQty),0),nvl(sum(d.shippedQty),0) from WmsPickTicketDetail d where d.pickTicket.id=:ptid";
				Object ds = commonDao.findByQueryUniqueResult(hql,new String[]{"ptid"},new Object[]{supplyPickTicket.getId()});
				Object[] qinfo = (Object[])ds;
				
				Double qty = (Double)qinfo[0];
				Double qty2= (Double)qinfo[1];
				supplyPickTicket.setQty(qty);
				supplyPickTicket.setShipQty(qty2);
				
				
				supplyPickTicket.setStatus(supplyPickTicket.defineStatus());//VMI拣货单的未分配数量被拆走后,原始单子为已分配状态
				commonDao.store(supplyPickTicket);
				
				/**如果拣货单已分配,则自动创建作业单*/
				if(supplyPickTicket.getStatus().equals(WmsPickTicketStatus.ALLOCATED)){
					createWorkDoc(supplyPickTicket);
				}
				
				if(wmsPickTicket.getStatus().equals(WmsPickTicketStatus.OPEN)){//如果原始单据为打开  则删除
					/**自管仓原始拣货单如果状态为打开,则删除*/
					deletePickRelate(wmsPickTicket);
					
				}
			}
		}
		else {
			if(supplyPickTicket.getStatus().equals(WmsPickTicketStatus.OPEN)){//VMI没有库存则直接转回原始仓库
				
				supplyPickTicket.setWarehouse(wh);
				supplyPickTicket.setShipLocation(wmsPickTicket.getShipLocation());
				commonDao.store(supplyPickTicket);
				String hql2 = "from WmsPickTicketDetail a where a.pickTicket.id=:ptid";
				List<WmsPickTicketDetail> dss = commonDao.findByQuery(hql2,new String[]{"ptid"},new Object[]{supplyPickTicket.getId()});
				dealPtdRequire(dss,WmsFactoryXmlb.BZ);//如果拣货单明细的批次属性有项目类别,那么VMI仓设为寄售,其它设为标准
			}
			else if(supplyPickTicket.getStatus().equals(WmsPickTicketStatus.PARTALLOCATED)){
				WmsPickTicket pick = createNewSupplyPickTicket(wh,supplyPickTicket,Boolean.TRUE);//创建拣货单->冰箱/洗衣机 状态打开   VMI库存不够的部分  转回自管仓
				pick.setOriginalId(wmsPickTicket.getOriginalId());//全部关联到老单据
				
				//处理一下单头的关键字段。
				pick.setWarehouse(wmsPickTicket.getWarehouse());
				
				
				//删除分配数量为0的明细
				deleteCurrentPickTicketDetail(supplyPickTicket);
				
				String hql2 = "from WmsPickTicketDetail a where a.pickTicket.id=:ptid";
				List<WmsPickTicketDetail> dss = commonDao.findByQuery(hql2,new String[]{"ptid"},new Object[]{supplyPickTicket.getId()});
				dealPtdRequire(dss,WmsFactoryXmlb.JS);//如果拣货单明细的批次属性有项目类别,那么VMI仓设为寄售,其它设为标准
				
				pick.refreshPickTicketQty();//刷新单头总数量
				WmsBillType bill = commonDao.load(WmsBillType.class, pick.getBillType().getId());
				if(WmsPickticketBillTypeCode.SCLLD.equals(bill.getCode())){
					pick.setShipLocation(loc);//T1库位
				}
				commonDao.store(pick);
				
				String hql = "select nvl(sum(d.expectedQty),0),nvl(sum(d.shippedQty),0) from WmsPickTicketDetail d where d.pickTicket.id=:ptid";
				Object ds = commonDao.findByQueryUniqueResult(hql,new String[]{"ptid"},new Object[]{supplyPickTicket.getId()});
				Object[] qinfo = (Object[])ds;
				
				Double qty = (Double)qinfo[0];
				Double qty2= (Double)qinfo[1];
				supplyPickTicket.setQty(qty);
				supplyPickTicket.setShipQty(qty2);
				
				supplyPickTicket.setStatus(supplyPickTicket.defineStatus());//VMI拣货单的未分配数量被拆走后,原始单子为已分配状态
				commonDao.store(supplyPickTicket);
				
				/**如果拣货单已分配,则自动创建作业单*/
				if(supplyPickTicket.getStatus().equals(WmsPickTicketStatus.ALLOCATED)){
					createWorkDoc(supplyPickTicket);
				}
			}
		}
		 
	}
	
	/**删除拣货单以及相关的数据*/
	private void deletePickRelate(WmsPickTicket wmsPickTicket){
		//删除拣货单明细的批次信息
		String hql = "delete from WmsPickTicketDetailRequire w where w.id in (select id from WmsPickTicketDetailRequire q  where q.pickTicketDetail.pickTicket.id=:id)";
		commonDao.executeByHql(hql, "id", wmsPickTicket.getId());
	 
		//删除拣货单明细与工单明细对应关系
		hql = "delete from ProductionOrderDetailPtDetail p where p.pickticketDetail.id in(select w.id from WmsPickTicketDetail w where w.pickTicket.id=:id)";
		commonDao.executeByHql(hql, "id", wmsPickTicket.getId());
		
		//删除拣货单明细
		hql = "delete from WmsPickTicketDetail w where w.pickTicket.id=:id";
		commonDao.executeByHql(hql, "id", wmsPickTicket.getId());
		
		//删除拣货单明细
		hql = "delete from WmsPickTicket w where w.id=:id";
		commonDao.executeByHql(hql, "id", wmsPickTicket.getId());
	}
	
	
	/**找到拣货明细的批次信息  如果批次信息有设置项目类别  那么转回冰箱或洗衣库的时候 要设为"标准"*/
	@SuppressWarnings("unchecked")
	private void dealPtdRequire(List<WmsPickTicketDetail> details,String xmlb){
		for(WmsPickTicketDetail ptd : details){
			List<WmsPickTicketDetailRequire> list = commonDao.findByQuery("from "
					+ "WmsPickTicketDetailRequire w where w.pickTicketDetail.id="+ptd.getId()+" and lotKey='EXTEND_PROPC8'");
			for(WmsPickTicketDetailRequire w : list){
				w.setLotValue1(xmlb);
				commonDao.store(w);
			}
		}
	}
	
	/**
	 * 创建供货仓补货拣货单
	 * @param createNewSupplyPickTicket
	 * @param wmsPickTicket
	 * @param isVmi=true VMI, false = 自管仓
	 * @return
	 */
	private WmsPickTicket createNewSupplyPickTicket(WmsWarehouse supplyWarehouse, 
			WmsPickTicket wmsPickTicket,Boolean isVmi) {
		//新建拣货单
		WmsPickTicket newPickTicket = new WmsPickTicket();
		//设置仓库为供货仓
		newPickTicket.setWarehouse(supplyWarehouse);
		WmsBillType wmsBillType = commonDao.load(WmsBillType.class, wmsPickTicket.getBillType().getId());
		String billCode = wmsBillType.getCode();//单据类型编码
		//供货仓拣货单号
		String newPickTicketCode = wmsBussinessCodeManager.generateCodeByRule(supplyWarehouse,billCode);
		newPickTicket.setCode(newPickTicketCode);
		//原拣货单货主是否绑定供货仓仓库
		WmsWarehouseCompany wc = (WmsWarehouseCompany)commonDao.findByQueryUniqueResult("FROM WmsWarehouseCompany wareCompany WHERE wareCompany.warehouse.id=:warehouseId AND wareCompany.company.id=:companyId", 
				new String[] {"warehouseId", "companyId"}, new Object[] {supplyWarehouse.getId(), wmsPickTicket.getCompany().getId()});
		if(wc == null && isVmi) {
			throw new BusinessException("this.old.pickticket.company.not.have.supplyWarehouse");
		}
		
		WmsLocation location = findLocationByWareHouse(supplyWarehouse.getId());
		
		if(!WarehouseEnumeration.VMI.equals(supplyWarehouse.getCode()) && billCode.equals(WmsPickticketBillTypeCode.SCLLD)){
			location = findLocation(supplyWarehouse.getId());
		}else{
			location = findLocationByWareHouse(supplyWarehouse.getId());
		}
		
		newPickTicket.setCompany(wc.getCompany());
		newPickTicket.setBillType(wmsBillType);
		//是否原始单据号为否
		newPickTicket.setIsOriginal(Boolean.FALSE);
		//原始单据id
		newPickTicket.setOriginalId(wmsPickTicket.getId());
		//锁单
		newPickTicket.setIsHold(wmsPickTicket.getIsHold());
		newPickTicket.setStatus(WmsPickTicketStatus.OPEN);
		newPickTicket.setRelatedBill1(wmsPickTicket.getRelatedBill1());
		newPickTicket.setRelatedBill2(wmsPickTicket.getRelatedBill2());
		newPickTicket.setRelatedBill3(wmsPickTicket.getRelatedBill3());
		newPickTicket.setCustomer(wmsPickTicket.getCustomer());
		newPickTicket.setShipToName(wmsPickTicket.getShipToName());
		newPickTicket.setContact(wmsPickTicket.getContact());
		newPickTicket.setOrderDate(wmsPickTicket.getOrderDate());
		newPickTicket.setPriority(wmsPickTicket.getPriority());
		newPickTicket.setIntendShipDate(wmsPickTicket.getIntendShipDate());
		newPickTicket.setExpectedPickFinishDate(wmsPickTicket.getExpectedPickFinishDate());
		newPickTicket.setFinshDate(wmsPickTicket.getFinshDate());
		newPickTicket.setDescription(wmsPickTicket.getDescription());
		newPickTicket.setShipDock(wmsPickTicket.getShipDock());
		newPickTicket.setShipLocation(location);//备货库位
		newPickTicket.setCarrier(wmsPickTicket.getCarrier());
		newPickTicket.setRequireArriveDate(wmsPickTicket.getRequireArriveDate());
		newPickTicket.setAllowShortShip(wmsPickTicket.getAllowShortShip());
		newPickTicket.setUserField1(wmsPickTicket.getUserField1());
		newPickTicket.setUserField2(wmsPickTicket.getUserField2());
		newPickTicket.setUserField3(wmsPickTicket.getUserField3());
		newPickTicket.setUserField4(wmsPickTicket.getUserField4());
		newPickTicket.setUserField5(wmsPickTicket.getUserField5());
		newPickTicket.setUserField6(wmsPickTicket.getUserField6());
		newPickTicket.setUserField7(wmsPickTicket.getUserField7());
		newPickTicket.setUserField8(wmsPickTicket.getUserField8());
		newPickTicket.setUserField9(wmsPickTicket.getUserField9());
		newPickTicket.setUserField10(wmsPickTicket.getUserField10());
		newPickTicket.setUserField11(wmsPickTicket.getUserField11());
		newPickTicket.setUserField12(wmsPickTicket.getUserField12());
		newPickTicket.setUserField13(wmsPickTicket.getUserField13());
		newPickTicket.setUserField14(wmsPickTicket.getUserField14());
		newPickTicket.setUserField15(wmsPickTicket.getUserField15());
		newPickTicket.setUserField16(wmsPickTicket.getUserField16());
		newPickTicket.setUserField17(wmsPickTicket.getUserField17());
		newPickTicket.setUserField18(wmsPickTicket.getUserField18());
		newPickTicket.setUserField19(wmsPickTicket.getUserField19());
		newPickTicket.setUserField20(wmsPickTicket.getUserField20());
		newPickTicket.setUserField21(wmsPickTicket.getUserField21());
		newPickTicket.setUserField22(wmsPickTicket.getUserField22());
		newPickTicket.setUserField23(wmsPickTicket.getUserField23());
		newPickTicket.setUserField24(wmsPickTicket.getUserField24());
		newPickTicket.setAllowCross(wmsPickTicket.getAllowCross());
		newPickTicket.setRoute(wmsPickTicket.getRoute());
		newPickTicket.setAllowLotSwap(wmsPickTicket.getAllowLotSwap());
		newPickTicket.setWaitReplenish(wmsPickTicket.getWaitReplenish());
		
		newPickTicket.setSupplier(wmsPickTicket.getSupplier());//供应商
		
		//计划数量=原拣货单计划数量-已分配数量
		newPickTicket.setQty(wmsPickTicket.getQty() - wmsPickTicket.getAllocateQty());
		//保存
		commonDao.store(newPickTicket);
		 
		//拣货明细清单
		Set<WmsPickTicketDetail> detailSets = wmsPickTicket.getDetails();
		//排序
		List<WmsPickTicketDetail> details = new ArrayList<WmsPickTicketDetail>(detailSets);
		Collections.sort(details, new Comparator() {
	        @Override
	        public int compare(Object o1, Object o2) {
	        	WmsPickTicketDetail d1 = (WmsPickTicketDetail)o1;
	        	WmsPickTicketDetail d2 = (WmsPickTicketDetail)o2;
	            if(d1.getId() > d2.getId()) {
	                return 1;
	            } else if(d1.getId() == d2.getId()) {
	                return 0;
	            } else {
	                return -1;
	            }
	        }       
		});
		//行号
		Integer lineNo = 1;
		for (WmsPickTicketDetail wmsPickTicketDetail : details) {
			//新期待数量：原拣货单明细.计划数量-已分配数量>0
			Double expectedQty = DoubleUtils.sub(wmsPickTicketDetail.getExpectedQty(), wmsPickTicketDetail.getAllocatedQty());
			
			if(CommonHelper.dealDoubleError(expectedQty) > 0D) {
				
				WmsItem wmsItem = this.commonDao.load(WmsItem.class, wmsPickTicketDetail.getItem().getId());
				WmsPackageUnit wmsPackageUnit = this.commonDao.load(WmsPackageUnit.class, wmsPickTicketDetail.getPackageUnit().getId());
				WmsPickTicketDetail newWmsPickTicketDetail = new WmsPickTicketDetail();
				newWmsPickTicketDetail.setPickTicket(newPickTicket);
				newWmsPickTicketDetail.setLineNo(lineNo);
				newWmsPickTicketDetail.setItem(wmsItem);
				newWmsPickTicketDetail.setInventoryStatus(wmsPickTicketDetail.getInventoryStatus());
				newWmsPickTicketDetail.setPackageUnit(wmsPackageUnit);
				//件装量
				Double convertFigure = wmsPackageUnit.getConvertFigure();
				//期待包装数量： 新期待数量/折算系数(取货品基本单位精度)
				Double expectedPackQty = DoubleUtils.round(expectedQty / convertFigure, wmsItem.getBuPrecision());
				newWmsPickTicketDetail.setExpectedPackQty(expectedPackQty);
				newWmsPickTicketDetail.setExpectedQty(expectedQty);
				newWmsPickTicketDetail.setAllocatedQty(0D);
				newWmsPickTicketDetail.setPickedQty(0D);
				newWmsPickTicketDetail.setPlanedShipQty(0D);
				newWmsPickTicketDetail.setShippedQty(0D);
				newWmsPickTicketDetail.setFullpackAllocation(wmsPickTicketDetail.getFullpackAllocation());
				newWmsPickTicketDetail.setOverpickRate(wmsPickTicketDetail.getOverpickRate());
				newWmsPickTicketDetail.setUserField1(wmsPickTicketDetail.getUserField1());
				newWmsPickTicketDetail.setUserField2(wmsPickTicketDetail.getUserField2());
				newWmsPickTicketDetail.setUserField3(wmsPickTicketDetail.getUserField3());
				newWmsPickTicketDetail.setUserField4(wmsPickTicketDetail.getUserField4());
				newWmsPickTicketDetail.setUserField5(wmsPickTicketDetail.getUserField5());
				newWmsPickTicketDetail.setUserField6(wmsPickTicketDetail.getUserField6());
				newWmsPickTicketDetail.setUserField7(wmsPickTicketDetail.getUserField7());
				newWmsPickTicketDetail.setUserField8(wmsPickTicketDetail.getUserField8());
				newWmsPickTicketDetail.setUserField9(wmsPickTicketDetail.getUserField9());
				newWmsPickTicketDetail.setUserField10(wmsPickTicketDetail.getUserField10());
				newWmsPickTicketDetail.setUserField11(wmsPickTicketDetail.getUserField11());
				newWmsPickTicketDetail.setUserField12(wmsPickTicketDetail.getUserField12());
				newWmsPickTicketDetail.setUserField13(wmsPickTicketDetail.getUserField13());
				newWmsPickTicketDetail.setUserField14(wmsPickTicketDetail.getUserField14());
				newWmsPickTicketDetail.setUserField15(wmsPickTicketDetail.getUserField15());
				newWmsPickTicketDetail.setUserField16(wmsPickTicketDetail.getUserField16());
				newWmsPickTicketDetail.setUserField17(wmsPickTicketDetail.getUserField17());
				newWmsPickTicketDetail.setUserField18(wmsPickTicketDetail.getUserField18());
				newWmsPickTicketDetail.setUserField19(wmsPickTicketDetail.getUserField19());
				lineNo += 1;
				this.commonDao.store(newWmsPickTicketDetail);
				newPickTicket.addPickTicketDetail(newWmsPickTicketDetail);
				
				/**带入批次信息*/
				newRequire(wmsPickTicketDetail.getId(), newWmsPickTicketDetail);
				
				//newWmsPickTicketDetail 为新仓库生成的单子 wmsPickTicketDetail为原始明细
				//从老的 上面转移 expectedQty 到 新的上面  如果为0 则删除
				reBindDetails(expectedQty, billCode, newWmsPickTicketDetail, wmsPickTicketDetail);
			}
		}
		return newPickTicket;
	}
	
	/**
	 * 重新绑定拣货明细数据,例如:拣货明细与交货明细,拣货明细与工单明细.. fs
	 * @param expectedQty
	 * @param billCode
	 * @param newWmsPickTicketDetail  新生成的拣货明细
	 * @param wmsPickTicketDetail	旧的拣货明细
	 */
	private void reBindDetails(Double expectedQty,String billCode,
			WmsPickTicketDetail newWmsPickTicketDetail,WmsPickTicketDetail wmsPickTicketDetail){
		Double dealQty = expectedQty;
		Double allocatedQty = CommonHelper.dealDoubleError(wmsPickTicketDetail.getAllocatedQty());
		Double newDealQty = CommonHelper.dealDoubleError(allocatedQty);
		if(null != billCode && billCode.equals(WmsPickticketGenType.SCLLD)){//生产领料单
			String hql = " select p from ProductionOrderDetailPtDetail p " +
					"LEFT JOIN p.productionOrderDetail pod  " +
					"where p.pickticketDetail=:oldptd " +
					"ORDER BY pod.productionOrder.id, pod.id";
			List<ProductionOrderDetailPtDetail> ps = commonDao.findByQuery(hql,new String[]{"oldptd"},new Object[]{wmsPickTicketDetail}) ;
			for(ProductionOrderDetailPtDetail p : ps) {
				dealQty = CommonHelper.dealDoubleError(dealQty);
				if(dealQty<=0) {
					break;
				}
//				Double ptd_allo=wmsPickTicketDetail.getAllocatedQty();
//				double thisCan = DoubleUtils.sub(p.getQuantityBu(), ptd_allo); //本条可以给新单的
//				
//				p.setQuantityBu(ptd_allo);
//				commonDao.store(p);
//				ProductionOrderDetailPtDetail newp = new ProductionOrderDetailPtDetail();
//				newp.setUnit(p.getUnit());
//				newp.setProductionOrderDetail(p.getProductionOrderDetail());
//				newp.setPickticketDetail(newWmsPickTicketDetail);
//				newp.setQuantityBu(thisCan);
//				commonDao.store(newp);
//				if(ptd_allo==0d) {
//					commonDao.delete(p);
//				}
				String dateStr = "";
				hql = "from WmsSystemValues s where s.code=:code ";
				WmsSystemValues sv = (WmsSystemValues) commonDao.findByQueryUniqueResult(hql, "code", WmsSystemValuesType.CTDATE);
				if(sv==null){
					dateStr = "20180122";
				}else{
					dateStr = sv.getValue();
				}
				Date date = DateUtil.formatDate(dateStr);
				//更新新逻辑后，因为之前的对应关系老数据中已经有数量为负数的了，走新逻辑会导致拣货单部分分配，所以更新前的数据还是走老逻辑，新数据走新逻辑
				if(p.getCtDate().before(date)) {//老逻辑
					Double ptd_allo=wmsPickTicketDetail.getAllocatedQty();
					double thisCan = DoubleUtils.sub(p.getQuantityBu(), ptd_allo); //本条可以给新单的
					
					p.setQuantityBu(ptd_allo);
					commonDao.store(p);
					ProductionOrderDetailPtDetail newp = new ProductionOrderDetailPtDetail();
					newp.setUnit(p.getUnit());
					newp.setProductionOrderDetail(p.getProductionOrderDetail());
					newp.setPickticketDetail(newWmsPickTicketDetail);
					newp.setQuantityBu(thisCan);
					newp.setCtDate(p.getCtDate());
					commonDao.store(newp);
					if(ptd_allo==0d) {
						commonDao.delete(p);
					}
				}else{
					//上面的逻辑不可拆包料数量会扣减成负数，下面重写
					//工单明细有分配数量的，需要把对应关系拆开，没有分配数量的直接把关系移到新拣货单明细上
					if(CommonHelper.dealDoubleError(p.getLastAllocatedQty())>0D){
						if(DoubleUtils.sub(p.getQuantityBu(), p.getLastAllocatedQty())<=0){
							newDealQty = DoubleUtils.sub(newDealQty,p.getLastAllocatedQty());
						}else{
							ProductionOrderDetailPtDetail newp = new ProductionOrderDetailPtDetail();
							newp.setUnit(p.getUnit());
							newp.setProductionOrderDetail(p.getProductionOrderDetail());
							newp.setPickticketDetail(newWmsPickTicketDetail);
							newp.setQuantityBu(DoubleUtils.sub(p.getQuantityBu(), p.getLastAllocatedQty()));
							newp.setCtDate(p.getCtDate());
							commonDao.store(newp);
							p.setQuantityBu(p.getLastAllocatedQty());
							commonDao.store(p);
							newDealQty = DoubleUtils.sub(newDealQty,p.getLastAllocatedQty());
							if(CommonHelper.dealDoubleError(p.getQuantityBu())==0D) {
								commonDao.delete(p);
							}
							if(CommonHelper.dealDoubleError(newp.getQuantityBu())==0D){
								commonDao.delete(newp);
							}
						}
					}else{
						ProductionOrderDetailPtDetail newp = new ProductionOrderDetailPtDetail();
						newp.setUnit(p.getUnit());
						newp.setProductionOrderDetail(p.getProductionOrderDetail());
						newp.setPickticketDetail(newWmsPickTicketDetail);
						newp.setQuantityBu(p.getQuantityBu());
						newp.setCtDate(p.getCtDate());
						commonDao.store(newp);
						commonDao.delete(p);
					}
				}
			}
		}else if(null != billCode && billCode.equals(WmsPickticketGenType.YLCKD)){
			String hql = "from ReservedOrderDetailPtDetail p where p.pickticketDetail=:oldptd order by p.reservedOrderDetail.id asc,p.quantityBu asc";
			List<ReservedOrderDetailPtDetail> ps = commonDao.findByQuery(hql,new String[]{"oldptd"},new Object[]{wmsPickTicketDetail}) ;
			for(ReservedOrderDetailPtDetail p : ps) {
				
				
				
				dealQty = CommonHelper.dealDoubleError(dealQty);
				if(dealQty<=0) {
					break;
				}
//				if(wmsPickTicketDetail.getAllocatedQty() == 0D){
//					p.setPickticketDetail(newWmsPickTicketDetail);
//					dealQty=dealQty-p.getQuantityBu();
//				}
//				Double thisCan = DoubleUtils.sub(p.getQuantityBu(), wmsPickTicketDetail.getAllocatedQty());
//				
				Double ptd_allo=wmsPickTicketDetail.getAllocatedQty();
				double thisCan = DoubleUtils.sub(p.getQuantityBu(), ptd_allo); //本条可以给新单的
				
				p.setQuantityBu(ptd_allo);
				commonDao.store(p);
				ReservedOrderDetailPtDetail newp = new ReservedOrderDetailPtDetail();
				newp.setUnit(p.getUnit());
				newp.setReservedOrderDetail(p.getReservedOrderDetail());
				newp.setPickticketDetail(newWmsPickTicketDetail);
				newp.setQuantityBu(thisCan);
				commonDao.store(newp);
				if(ptd_allo==0d) {
					commonDao.delete(p);
				}
				
				
//				if(dealQty<=0) {
//					break;
//				}
//				
//				if(p.getQuantityBu()<=dealQty) {
//					p.setPickticketDetail(newWmsPickTicketDetail);
//					dealQty=DoubleUtils.sub(dealQty,p.getQuantityBu());
//				}
//				else if(p.getQuantityBu()>dealQty) {
//					p.setQuantityBu(DoubleUtils.sub(p.getQuantityBu(),dealQty));
//					commonDao.store(p);
//					ReservedOrderDetailPtDetail newp = new ReservedOrderDetailPtDetail();
//					newp.setUnit(p.getUnit());;
//					newp.setReservedOrderDetail(p.getReservedOrderDetail());
//					newp.setPickticketDetail(newWmsPickTicketDetail);
//					newp.setQuantityBu(dealQty);
//					commonDao.store(newp);
//				}
			}
		}
		else if(null != billCode && billCode.equals(WmsPickticketGenType.XSJHD)){ //销售交货单
			String hql = "from DeliveryOrderDetailPtDetail p where p.pickticketDetail=:oldptd order by p.deliveryOrderDetail.id asc,p.quantityBu asc";
			List<DeliveryOrderDetailPtDetail> ps = commonDao.findByQuery(hql,new String[]{"oldptd"},new Object[]{wmsPickTicketDetail}) ;
			for(DeliveryOrderDetailPtDetail p : ps) {
				if(dealQty<=0) {
					break;
				}
				
				if(p.getQuantityBu()<=dealQty) {
					p.setPickticketDetail(newWmsPickTicketDetail);
					dealQty=DoubleUtils.sub(dealQty,p.getQuantityBu());
				}
				else if(p.getQuantityBu()>dealQty) {
					p.setQuantityBu(DoubleUtils.sub(p.getQuantityBu(),dealQty));
					commonDao.store(p);
					DeliveryOrderDetailPtDetail newp = new DeliveryOrderDetailPtDetail();
					newp.setUnit(p.getUnit());
					newp.setDeliveryOrderDetail(p.getDeliveryOrderDetail());
					newp.setPickticketDetail(newWmsPickTicketDetail);
					newp.setQuantityBu(dealQty);
					commonDao.store(newp);
				}
			}
		}
	}
	
	/**转换仓库 获取目的仓库的库位 fs*/
	public WmsLocation findLocationByWareHouse(Long wareHouseId){
		String hql = "from WmsLocation w where w.type=:type "
				+ "and status='ENABLED' and w.warehouse.id="+wareHouseId;
		List<WmsLocation> locations = commonDao.findByQuery(hql,"type",WmsLocationType.SHIP);
		if(locations.size() <= 0){
			throw new BusinessException("目的仓库没有维护备货库位,请检查!!");
		}
		return locations.get(0);
	}
	/**转换仓库 从VMI->冰箱或洗衣机  找到T1库位 fs*/
	public WmsLocation findLocation(Long wareHouseId){
		List<WmsLocation> locations = commonDao.findByQuery("from WmsLocation w "
				+ "where w.code='"+WmsLocationCode.T1+"' and status='ENABLED' and w.warehouse.id="+wareHouseId);
		if(locations.size() <= 0){
			throw new BusinessException("目的仓库没有维护"+WmsLocationCode.T1+"库位,请检查!!");
		}
		return locations.get(0);
	}
	/**批次信息带到新的拣货明细上 fs*/
	public void newRequire(Long oldId,WmsPickTicketDetail newDetail){
		String hql = "from WmsPickTicketDetailRequire r where r.pickTicketDetail.id=:id";
		List<WmsPickTicketDetailRequire> pdr = commonDao.findByQuery(hql,"id",oldId);
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,newDetail.getPickTicket().getWarehouse().getId());
		for(WmsPickTicketDetailRequire w : pdr){
			WmsPickTicketDetailRequire newRequire = new WmsPickTicketDetailRequire();
			newRequire.setPickTicketDetail(newDetail);
			newRequire.setLotKey(w.getLotKey());
			newRequire.setLotValue1(w.getLotValue1());
			
			if(!StringHelper.isNullOrEmpty(w.getLotKey())
					&& w.getLotKey().equals("EXTEND_PROPC8")){//批次属性里面包含标准寄售
				
				if(!warehouse.getCode().equals(WarehouseEnumeration.VMI)){//VMI改为2,冰箱洗衣机为0
					newRequire.setLotValue1("0");
				}else{
					newRequire.setLotValue1("2");
				}
			}
			newRequire.setLotValue2(w.getLotValue2());
			newRequire.setLotValue3(w.getLotValue3());
			newRequire.setLotValue4(w.getLotValue4());
			newRequire.setLotValue5(w.getLotValue5());
			newRequire.setQueryRequire(w.getQueryRequire());
			newRequire.setLevel(w.getLevel());
			newRequire.setAllowModified(w.getAllowModified());
			commonDao.store(newRequire);
		}
	}
	/**
	 * 当前仓库生成拣货单，作为原始数据保存
	 * @param wmsPickTicket
	 * @param wh
	 * @param supplyWarehouse
	 * @param isAllocate true=全部分配 false=未完全分配
	 * @return
	 */
	private WmsPickTicket createCarrenPickTicket(WmsPickTicket wmsPickTicket, WmsWarehouse wh) {
		//当前仓库新拣货单
		WmsPickTicket currentNewPickTicket = new WmsPickTicket();
		currentNewPickTicket.setWarehouse(wh);
		WmsBillType wmsBillType = this.commonDao.load(WmsBillType.class, wmsPickTicket.getBillType().getId());
		WmsCompany wmsCompany = this.commonDao.load(WmsCompany.class, wmsPickTicket.getCompany().getId());
		String currentNewPickTicketCode = wmsBussinessCodeManager.generateCodeByRule(wh, wmsBillType.getCode());
		currentNewPickTicket.setCode(currentNewPickTicketCode);
		currentNewPickTicket.setCompany(wmsCompany);
		currentNewPickTicket.setBillType(wmsBillType);
		//不可执行
		currentNewPickTicket.setIsExecutable(Boolean.FALSE);
		//是否原始单据号为是
		currentNewPickTicket.setIsOriginal(Boolean.TRUE);
		//锁单
		currentNewPickTicket.setIsHold(wmsPickTicket.getIsHold());
		//打开状态
		currentNewPickTicket.setStatus(WmsPickTicketStatus.CLOSED);
		currentNewPickTicket.setRelatedBill1(wmsPickTicket.getRelatedBill1());
		currentNewPickTicket.setRelatedBill2(wmsPickTicket.getRelatedBill2());
		currentNewPickTicket.setRelatedBill3(wmsPickTicket.getRelatedBill3());
		currentNewPickTicket.setCustomer(wmsPickTicket.getCustomer());
		currentNewPickTicket.setShipToName(wmsPickTicket.getShipToName());
		currentNewPickTicket.setContact(wmsPickTicket.getContact());
		currentNewPickTicket.setOrderDate(wmsPickTicket.getOrderDate());
		currentNewPickTicket.setPriority(wmsPickTicket.getPriority());
		currentNewPickTicket.setIntendShipDate(wmsPickTicket.getIntendShipDate());
		currentNewPickTicket.setExpectedPickFinishDate(wmsPickTicket.getExpectedPickFinishDate());
		currentNewPickTicket.setFinshDate(wmsPickTicket.getFinshDate());
		currentNewPickTicket.setDescription(wmsPickTicket.getDescription());
		currentNewPickTicket.setShipDock(wmsPickTicket.getShipDock());
		currentNewPickTicket.setShipLocation(wmsPickTicket.getShipLocation());
		currentNewPickTicket.setCarrier(wmsPickTicket.getCarrier());
		currentNewPickTicket.setRequireArriveDate(wmsPickTicket.getRequireArriveDate());
		currentNewPickTicket.setAllowShortShip(wmsPickTicket.getAllowShortShip());
		currentNewPickTicket.setUserField1(wmsPickTicket.getUserField1());
		currentNewPickTicket.setUserField2(wmsPickTicket.getUserField2());
		currentNewPickTicket.setUserField3(wmsPickTicket.getUserField3());
		currentNewPickTicket.setUserField4(wmsPickTicket.getUserField4());
		currentNewPickTicket.setUserField5(wmsPickTicket.getUserField5());
		currentNewPickTicket.setUserField6(wmsPickTicket.getUserField6());
		currentNewPickTicket.setUserField7(wmsPickTicket.getUserField7());
		currentNewPickTicket.setUserField8(wmsPickTicket.getUserField8());
		currentNewPickTicket.setUserField9(wmsPickTicket.getUserField9());
		currentNewPickTicket.setUserField10(wmsPickTicket.getUserField10());
		currentNewPickTicket.setUserField11(wmsPickTicket.getUserField11());
		currentNewPickTicket.setUserField12(wmsPickTicket.getUserField12());
		currentNewPickTicket.setUserField13(wmsPickTicket.getUserField13());
		currentNewPickTicket.setUserField14(wmsPickTicket.getUserField14());
		currentNewPickTicket.setUserField15(wmsPickTicket.getUserField15());
		currentNewPickTicket.setUserField16(wmsPickTicket.getUserField16());
		currentNewPickTicket.setUserField17(wmsPickTicket.getUserField17());
		currentNewPickTicket.setUserField18(wmsPickTicket.getUserField18());
		currentNewPickTicket.setUserField19(wmsPickTicket.getUserField19());
		currentNewPickTicket.setUserField20(wmsPickTicket.getUserField20());
		currentNewPickTicket.setUserField21(wmsPickTicket.getUserField21());
		currentNewPickTicket.setUserField22(wmsPickTicket.getUserField22());
		currentNewPickTicket.setUserField23(wmsPickTicket.getUserField23());
		currentNewPickTicket.setUserField24(wmsPickTicket.getUserField24());
		currentNewPickTicket.setAllowCross(wmsPickTicket.getAllowCross());
		currentNewPickTicket.setRoute(wmsPickTicket.getRoute());
		currentNewPickTicket.setAllowLotSwap(wmsPickTicket.getAllowLotSwap());
		currentNewPickTicket.setWaitReplenish(wmsPickTicket.getWaitReplenish());
		
		currentNewPickTicket.setSupplier(wmsPickTicket.getSupplier());//供应商
		//计划数量
		currentNewPickTicket.setQty(wmsPickTicket.getQty());
		currentNewPickTicket.setAllocateQty(0D);
		//保存原拣货单
		commonDao.store(currentNewPickTicket);
		
		//拣货明细清单
		Set<WmsPickTicketDetail> detailSets = wmsPickTicket.getDetails();
		//排序
		List<WmsPickTicketDetail> details = new ArrayList<WmsPickTicketDetail>(detailSets);
		Collections.sort(details, new Comparator() {
	        @Override
	        public int compare(Object o1, Object o2) {
	        	WmsPickTicketDetail d1 = (WmsPickTicketDetail)o1;
	        	WmsPickTicketDetail d2 = (WmsPickTicketDetail)o2;
	            if(d1.getId() > d2.getId()) {
	                return 1;
	            } else if(d1.getId() == d2.getId()) {
	                return 0;
	            } else {
	                return -1;
	            }
	        }       
		});
		for (WmsPickTicketDetail wmsPickTicketDetail : details) {
			WmsPickTicketDetail currentWmsPickTicketDetail = new WmsPickTicketDetail();
			WmsItem wmsItem = this.commonDao.load(WmsItem.class, wmsPickTicketDetail.getItem().getId());
			WmsPackageUnit wmsPackageUnit = this.commonDao.load(WmsPackageUnit.class, wmsPickTicketDetail.getPackageUnit().getId());
			currentWmsPickTicketDetail.setPickTicket(currentNewPickTicket);
			currentWmsPickTicketDetail.setLineNo(wmsPickTicketDetail.getLineNo());
			currentWmsPickTicketDetail.setItem(wmsItem);
			currentWmsPickTicketDetail.setInventoryStatus(wmsPickTicketDetail.getInventoryStatus());
			currentWmsPickTicketDetail.setPackageUnit(wmsPackageUnit);
			currentWmsPickTicketDetail.setExpectedPackQty(wmsPickTicketDetail.getExpectedPackQty());
			currentWmsPickTicketDetail.setExpectedQty(wmsPickTicketDetail.getExpectedQty());
			currentWmsPickTicketDetail.setAllocatedQty(0D);
			currentWmsPickTicketDetail.setPickedQty(0D);
			currentWmsPickTicketDetail.setPlanedShipQty(0D);
			currentWmsPickTicketDetail.setShippedQty(0D);
			currentWmsPickTicketDetail.setFullpackAllocation(wmsPickTicketDetail.getFullpackAllocation());
			currentWmsPickTicketDetail.setOverpickRate(wmsPickTicketDetail.getOverpickRate());
			currentWmsPickTicketDetail.setUserField1(wmsPickTicketDetail.getUserField1());
			currentWmsPickTicketDetail.setUserField2(wmsPickTicketDetail.getUserField2());
			currentWmsPickTicketDetail.setUserField3(wmsPickTicketDetail.getUserField3());
			currentWmsPickTicketDetail.setUserField4(wmsPickTicketDetail.getUserField4());
			currentWmsPickTicketDetail.setUserField5(wmsPickTicketDetail.getUserField5());
			currentWmsPickTicketDetail.setUserField6(wmsPickTicketDetail.getUserField6());
			currentWmsPickTicketDetail.setUserField7(wmsPickTicketDetail.getUserField7());
			currentWmsPickTicketDetail.setUserField8(wmsPickTicketDetail.getUserField8());
			currentWmsPickTicketDetail.setUserField9(wmsPickTicketDetail.getUserField9());
			currentWmsPickTicketDetail.setUserField10(wmsPickTicketDetail.getUserField10());
			currentWmsPickTicketDetail.setUserField11(wmsPickTicketDetail.getUserField11());
			currentWmsPickTicketDetail.setUserField12(wmsPickTicketDetail.getUserField12());
			currentWmsPickTicketDetail.setUserField13(wmsPickTicketDetail.getUserField13());
			currentWmsPickTicketDetail.setUserField14(wmsPickTicketDetail.getUserField14());
			currentWmsPickTicketDetail.setUserField15(wmsPickTicketDetail.getUserField15());
			currentWmsPickTicketDetail.setUserField16(wmsPickTicketDetail.getUserField16());
			currentWmsPickTicketDetail.setUserField17(wmsPickTicketDetail.getUserField17());
			currentWmsPickTicketDetail.setUserField18(wmsPickTicketDetail.getUserField18());
			currentWmsPickTicketDetail.setUserField19(wmsPickTicketDetail.getUserField19());
			commonDao.store(currentWmsPickTicketDetail);
			
			/**带入批次信息*/
			newRequire(wmsPickTicketDetail.getId(), currentWmsPickTicketDetail);
		}
		return currentNewPickTicket;
	}
	
	/**
	 * 创建发货单明细 fs
	 * @param pick
	 * @param wmsPickTicketDetail
	 * @return
	 */
	private WmsPickTicketDetail createPtd(WmsPickTicket pick,
			WmsPickTicketDetail wmsPickTicketDetail,Double planQty,Integer lineNo){
		WmsPickTicketDetail currentWmsPickTicketDetail = new WmsPickTicketDetail();
		WmsItem wmsItem = this.commonDao.load(WmsItem.class, wmsPickTicketDetail.getItem().getId());
		WmsPackageUnit wmsPackageUnit = this.commonDao.load(WmsPackageUnit.class, wmsPickTicketDetail.getPackageUnit().getId());
		currentWmsPickTicketDetail.setPickTicket(pick);
		currentWmsPickTicketDetail.setLineNo(lineNo);
		currentWmsPickTicketDetail.setItem(wmsItem);
		currentWmsPickTicketDetail.setInventoryStatus(wmsPickTicketDetail.getInventoryStatus());
		currentWmsPickTicketDetail.setPackageUnit(wmsPackageUnit);
		currentWmsPickTicketDetail.setExpectedPackQty(planQty);
		currentWmsPickTicketDetail.setExpectedQty(planQty);
		currentWmsPickTicketDetail.setAllocatedQty(0D);
		currentWmsPickTicketDetail.setPickedQty(0D);
		currentWmsPickTicketDetail.setPlanedShipQty(0D);
		currentWmsPickTicketDetail.setShippedQty(0D);
		currentWmsPickTicketDetail.setFullpackAllocation(wmsPickTicketDetail.getFullpackAllocation());
		currentWmsPickTicketDetail.setOverpickRate(wmsPickTicketDetail.getOverpickRate());
		currentWmsPickTicketDetail.setUserField1(wmsPickTicketDetail.getUserField1());
		currentWmsPickTicketDetail.setUserField2(wmsPickTicketDetail.getUserField2());
		currentWmsPickTicketDetail.setUserField3(wmsPickTicketDetail.getUserField3());
		currentWmsPickTicketDetail.setUserField4(wmsPickTicketDetail.getUserField4());
		currentWmsPickTicketDetail.setUserField5(wmsPickTicketDetail.getUserField5());
		currentWmsPickTicketDetail.setUserField6(wmsPickTicketDetail.getUserField6());
		currentWmsPickTicketDetail.setUserField7(wmsPickTicketDetail.getUserField7());
		currentWmsPickTicketDetail.setUserField8(wmsPickTicketDetail.getUserField8());
		currentWmsPickTicketDetail.setUserField9(wmsPickTicketDetail.getUserField9());
		currentWmsPickTicketDetail.setUserField10(wmsPickTicketDetail.getUserField10());
		currentWmsPickTicketDetail.setUserField11(wmsPickTicketDetail.getUserField11());
		currentWmsPickTicketDetail.setUserField12(wmsPickTicketDetail.getUserField12());
		currentWmsPickTicketDetail.setUserField13(wmsPickTicketDetail.getUserField13());
		currentWmsPickTicketDetail.setUserField14(wmsPickTicketDetail.getUserField14());
		currentWmsPickTicketDetail.setUserField15(wmsPickTicketDetail.getUserField15());
		currentWmsPickTicketDetail.setUserField16(wmsPickTicketDetail.getUserField16());
		currentWmsPickTicketDetail.setUserField17(wmsPickTicketDetail.getUserField17());
		currentWmsPickTicketDetail.setUserField18(wmsPickTicketDetail.getUserField18());
		currentWmsPickTicketDetail.setUserField19(wmsPickTicketDetail.getUserField19());
		commonDao.store(currentWmsPickTicketDetail);
		pick.addPickTicketDetail(currentWmsPickTicketDetail);
		
		/**带入批次信息*/
		newRequire(wmsPickTicketDetail.getId(), currentWmsPickTicketDetail);
		return currentWmsPickTicketDetail;
	}
	
	/**
	 * 删除分配数量为0的拣货单明细
	 * @param wmsPickTicket
	 */
	private void deleteCurrentPickTicketDetail(WmsPickTicket wmsPickTicket) {
		//拣货明细清单
		Set<WmsPickTicketDetail> detailSets = wmsPickTicket.getDetails();
		//排序
		List<WmsPickTicketDetail> details = new ArrayList<WmsPickTicketDetail>(detailSets);
		Collections.sort(details, new Comparator() {
	        @Override
	        public int compare(Object o1, Object o2) {
	        	WmsPickTicketDetail d1 = (WmsPickTicketDetail)o1;
	        	WmsPickTicketDetail d2 = (WmsPickTicketDetail)o2;
	            if(d1.getId() > d2.getId()) {
	                return 1;
	            } else if(d1.getId() == d2.getId()) {
	                return 0;
	            } else {
	                return -1;
	            }
	        }       
		});
		Double qty = 0D;//拣货单总数量
		for (WmsPickTicketDetail wmsPickTicketDetail : details) {
			
			if(CommonHelper.dealDoubleError(wmsPickTicketDetail.getAllocatedQty()) == 0D) {
				String hql = "delete from WmsPickTicketDetailRequire where pickTicketDetail=:pickTicketDetail";
				commonDao.executeByHql(hql, "pickTicketDetail", wmsPickTicketDetail);
				if(CommonHelper.dealDoubleError(wmsPickTicketDetail.getExpectedQty())==0d) {
					hql ="delete from ProductionOrderDetailPtDetail p where p.pickticketDetail.id =:pickticketDetailId ";
					commonDao.executeByHql(hql, "pickticketDetailId", wmsPickTicketDetail.getId());
					String resHql = "delete from ReservedOrderDetailPtDetail r where r.pickticketDetail.id=:pickticketDetailId ";
					commonDao.executeByHql(resHql, "pickticketDetailId", wmsPickTicketDetail.getId());
				}
				//删除
				this.commonDao.delete(wmsPickTicketDetail);
				wmsPickTicket.getDetails().remove(wmsPickTicketDetail);
			} else {
				WmsItem wmsItem = this.commonDao.load(WmsItem.class, wmsPickTicketDetail.getItem().getId());
				wmsPickTicketDetail.setItem(wmsItem);
				//单位
				WmsPackageUnit wmsPackageUnit = this.commonDao.load(WmsPackageUnit.class, wmsPickTicketDetail.getPackageUnit().getId());
				//件装量
				Double convertFigure = wmsPackageUnit.getConvertFigure();
				/**不可拆包料的分配数量可能大于计划数量,计划数量<分配数量,则将计划数量设为分配数量*/
				if(wmsPickTicketDetail.getAllocatedQty() < wmsPickTicketDetail.getExpectedQty()){
					wmsPickTicketDetail.setExpectedQty(wmsPickTicketDetail.getAllocatedQty());
					//期待包装数量： 新期待数量/折算系数(取货品基本单位精度)
					wmsPickTicketDetail.setExpectedPackQty(PackageUtils.convertPackQuantity(wmsPickTicketDetail.getExpectedQty(), convertFigure, wmsItem.getBuPrecision()));
				}
				qty += wmsPickTicketDetail.getExpectedQty();
				this.commonDao.store(wmsPickTicketDetail);
			}
		}
		//是否原始单据为否
		wmsPickTicket.setIsOriginal(Boolean.FALSE);
		//设置数量为分配数量
		wmsPickTicket.setQty(qty);
		commonDao.store(wmsPickTicket);
	}
	
	
	public void storePickTicket(WmsPickTicket pickTicket) {
		String oldBillCode = "";
		if(!pickTicket.isNew()){
			WmsPickTicket old = (WmsPickTicket) pickTicket.getOldEntity();
			WmsBillType billType = commonDao.load(WmsBillType.class,old.getBillType().getId());
			oldBillCode = billType.getCode();//旧的单据类型
			
			 //拣货单类型为预留出库单或销售交货单等,不允许修改
			if(null != pickTicket.getUserField3() && 
					(pickTicket.getUserField3().equals(WmsPickticketGenType.SCLLD)
				    || pickTicket.getUserField3().equals(WmsPickticketGenType.YLCKD) 
					|| pickTicket.getUserField3().equals(WmsPickticketGenType.XSJHD))){
				throw new BusinessException("拣货单类型为预留出库单或销售交货单等,不允许修改");
			}
		}
		String newBillCode = pickTicket.getBillType().getCode();//新的单据类型
		pickTicket.setAllowShortShip(Boolean.TRUE);//允许短缺发运
		super.storePickTicket(pickTicket);

		if(StringUtils.isEmpty(pickTicket.getUserField1())){
			throw new BusinessException("出库工厂属性必填,不能为空");
		}
		
       
		
		//调拨出库单
		if(WmsPickticketBillTypeCode.DBCKD.equals(newBillCode)){
			if(StringUtils.isEmpty(pickTicket.getUserField2())){
				throw new BusinessException("调拨出库单：入库工厂属性必填,不能为空");
			}
			 
			//收货人必须是仓库。
			if(pickTicket.getCustomer()==null || pickTicket.getCustomer().getWarehouse()==null) {
				throw new BusinessException("调拨出库单：收货人不能为空且必须是仓库");
			}
			if(pickTicket.getSupplier() != null && "XN".equals(pickTicket.getSupplier().getCode())){
				throw new BusinessException("调拨出库单：不能选择虚拟供应商");
			}
			String wareHql = "FROM WmsWarehouse house where house.baseOrganization.id=:hbi and house.status='ENABLED'";
			WmsWarehouse warehouse = (WmsWarehouse)commonDao.findByQueryUniqueResult(wareHql, "hbi",BaseOrganizationHolder.getThornBaseOrganization().getId());
			if(null==warehouse){
				throw new RfBusinessException("没有找到对应仓库!!");
			}
			if(!"VMI".equals(pickTicket.getWarehouse().getCode()) && !"VMI".equals(pickTicket.getCustomer().getWarehouse().getCode())){
				throw new BusinessException("自管仓调拨出库收货人必须是VMI仓库");
			}
			
			
			if(pickTicket.getCustomer().getWarehouse().getId().equals(warehouse.getId())) {//选择的收货人是当前仓库
				if(!"VMI".equals(pickTicket.getCustomer().getWarehouse().getCode())) { //必须是vmi
					throw new BusinessException("收货人选择错误");
				}
				
				if(StringHelper.in(pickTicket.getUserField1(), new String[]{"2000","2100"})  && 
						StringHelper.in(pickTicket.getUserField2(), new String[]{"2000","2100"}) )  {
					throw new BusinessException("调拨出库单：出库工厂与入库工厂不能同属于一工厂");
				 
				}
				if(StringHelper.in(pickTicket.getUserField2(), new String[]{"1000","1100"})  && 
						StringHelper.in(pickTicket.getUserField1(), new String[]{"1000","1100"}) )  {
					throw new BusinessException("调拨出库单：出库工厂与入库工厂不能同属于一工厂");
				}
			}
			else {//选择的收货人不是当前仓库 则是跨仓库  跨仓库不控制工厂
			}
			
			//收货人必须有入库工厂
			String hql = "from WmsFactoryWarehouse w where w.factory.code=:factoryCode and w.warehouse=:warehouse";
			List<WmsFactoryWarehouse> fs = commonDao.findByQuery(hql,new String[]{"factoryCode","warehouse"},new Object[]{pickTicket.getUserField2(),pickTicket.getCustomer().getWarehouse()});
			if(fs.isEmpty()) {
				throw new BusinessException("收货人"+pickTicket.getCustomer().getName()+"下无选择的入库工厂");
			}
			
		}
		if(WmsPickticketBillTypeCode.TGYSCK.equals(newBillCode)) { //退供应商出库单
			if(pickTicket.getSupplier()==null) {
				throw new BusinessException("退供应商出库单：供应商不能为空");
			}
			if("XN".equals(pickTicket.getSupplier().getCode())){
				throw new BusinessException("退供应商出库单：不能选择虚拟供应商");
			}
		}
		if(WmsPickticketBillTypeCode.BFCKD.equals(newBillCode) || WmsPickticketBillTypeCode.PKCKD.equals(newBillCode)){//报废出库单
			if(StringHelper.isNullOrEmpty(pickTicket.getUserField4())){
				throw new BusinessException("报废出库单/盘亏出库单：成本中心不能为空");
			}
		}
		/**报废出库单和其它出库单 不能需要加上项目类别=标准的 批次属性,其它的不用*/
		if(oldBillCode.equals(WmsPickticketBillTypeCode.BFCKD) 
				|| oldBillCode.equals(WmsPickticketBillTypeCode.QTCKD) 
				|| oldBillCode.equals(WmsPickticketBillTypeCode.XSJHD) 
				&& !newBillCode.equals(WmsPickticketBillTypeCode.BFCKD) 
				&& !newBillCode.equals(WmsPickticketBillTypeCode.QTCKD)
				&& !newBillCode.equals(WmsPickticketBillTypeCode.XSJHD)
				&& pickTicket.getDetails().size() > 0){
			Set<WmsPickTicketDetail> details = pickTicket.getDetails();
			for(WmsPickTicketDetail detail : details){
				/**其它出库 和 报废出库 改为其它单据类型时，删除明细项目类别=标准的批次信息*/
				String hql = "delete from WmsPickTicketDetailRequire where pickTicketDetail.id =:id and lotKey='EXTEND_PROPC8' and lotValue1='0'";
				int updateLine = commonDao.executeByHql(hql, "id", detail.getId());
			}
		}else if(newBillCode.equals(WmsPickticketBillTypeCode.BFCKD) 
				|| newBillCode.equals(WmsPickticketBillTypeCode.QTCKD) 
				|| newBillCode.equals(WmsPickticketBillTypeCode.XSJHD) 
				&& !oldBillCode.equals(WmsPickticketBillTypeCode.BFCKD) 
				&& !oldBillCode.equals(WmsPickticketBillTypeCode.QTCKD)
				&& !oldBillCode.equals(WmsPickticketBillTypeCode.XSJHD)
				&& pickTicket.getDetails().size() > 0){
			Set<WmsPickTicketDetail> details = pickTicket.getDetails();
			for(WmsPickTicketDetail detail : details){
				Boolean isExist = findPtXmlb(detail);
				if(isExist){
					/**添加一条项目类别=标准的批次信息*/
					addLotInfo(detail);
				}
			}
		}
		
		//失效的供应商不能下单
		if(pickTicket.getSupplier()!=null && BaseStatus.DISABLED.equals(pickTicket.getSupplier().getStatus())){
			throw new BusinessException("供应商已失效不能下单");
		}
		
	}
	
	/**查拣货单明细有没有项目类别=标准的批次信息*/
	private Boolean findPtXmlb(WmsPickTicketDetail detail){
		String hql = "select id from WmsPickTicketDetailRequire "
				+ "where pickTicketDetail.id =:id and lotKey='EXTEND_PROPC8' and lotValue1='0'";
		Long id = (Long) commonDao.findByQueryUniqueResult(hql,"id",detail.getId());
		return id == null ? Boolean.TRUE : Boolean.FALSE;
	}
	
	/**
	 * 添加拣货单明细
	 * @Description:
	 * @Author:        <a href="yang.liu@vtradex.net">刘杨</a>
	 * @CreateDate:    2015年12月14日
	 * @param pickTicketId
	 * @param pickTicketDetail
	 * @param status:
	 * @arithMetic:
	 * @exception:
	 */
	public void addPickTicketDetail(Long pickTicketId,
			WmsPickTicketDetail pickTicketDetail) {
		WmsPickTicket pickTicket = commonDao.load(WmsPickTicket.class, pickTicketId);
		Boolean isNew = Boolean.FALSE;//新建还是修改
		if(pickTicketDetail.isNew()){
			isNew = Boolean.TRUE;
		}else{
			//判断拣货单类型，如果为预留出库单等,不允许修改
			if(null != pickTicket.getUserField3() 
				&& (pickTicket.getUserField3().equals(WmsPickticketGenType.SCLLD)
				|| pickTicket.getUserField3().equals(WmsPickticketGenType.YLCKD))){
				throw new BusinessException("拣货单类型为预留出库单等,不允许修改");
			}
			//单据类型为销售交货单,修改拣货单的计划数量不能大于原交货单计划数量
			if(null != pickTicket.getUserField3() && pickTicket.getUserField3().equals(WmsPickticketGenType.XSJHD)){
				String hql = "select d.deliveryOrderDetail FROM DeliveryOrderDetailPtDetail d WHERE d.pickticketDetail.id =:Id";
				List<WmsDeliveryOrderDetail>  details = commonDao.findByQuery(hql, "Id", pickTicketDetail.getId());
				if(details.isEmpty()){
					throw new BusinessException("未查找到交货单明细！！！");
				}
				Double quantity = 0D;
				for(WmsDeliveryOrderDetail detail : details){
					quantity += detail.getPlanQuantityBu();
				}
				if(pickTicketDetail.getExpectedPackQty() > quantity){
					throw new BusinessException("修改数量不能大于原交货单计划数量！！！");
				}
			}
		}
		
		super.addPickTicketDetail(pickTicketId, pickTicketDetail);
		
			  
		
		
		
		String billCode = pickTicketDetail.getPickTicket().getBillType().getCode();
		if(WmsPickticketBillTypeCode.DBCKD.equals(billCode)){
			String hql = "FROM WmsItemFactory w WHERE w.factory.code =:code AND w.item.id =:id";
			String hql1 = "FROM WmsItemFactory w WHERE w.factory.code =:code AND w.item.id =:id";
			
			List<WmsItemFactory>  list = commonDao.findByQuery(hql, new String[]{"code","id"}, new Object[]{pickTicket.getUserField1(),pickTicketDetail.getItem().getId()});
			List<WmsItemFactory>  list1 = commonDao.findByQuery(hql, new String[]{"code","id"}, new Object[]{pickTicket.getUserField2(),pickTicketDetail.getItem().getId()});
		    if(list.size() == 0 || list1.size() == 0){
		    	throw new BusinessException(pickTicketDetail.getItem().getName()+"在工厂编码为"+pickTicket.getUserField1()+"与"+pickTicket.getUserField2()+"中没有同时存在");
		    }
		
		}
		
		String itemFactoryHql = "FROM WmsItemFactory w WHERE w.factory.code =:code AND w.item.id =:id";
		List<WmsItemFactory>  itemFactory = commonDao.findByQuery(itemFactoryHql, new String[]{"code","id"}, new Object[]{pickTicket.getUserField1(),pickTicketDetail.getItem().getId()});
		if(itemFactory.size() == 0){
			throw new BusinessException("物料在出库工厂编码为"+pickTicket.getUserField1()+"中不存在");
		}
		if(isNew){//修改不重复添加
			//增加
			WmsPickTicketDetailRequire ptdr = EntityFactory.getEntity(WmsPickTicketDetailRequire.class);
			ptdr.setPickTicketDetail(pickTicketDetail);
			ptdr.setLotKey("EXTEND_PROPC10"); //批次属性
			if(StringHelper.isNullOrEmpty(pickTicketDetail.getPickTicket().getUserField1())) {
				throw new BusinessException("拣配单工厂编码不能为空");
			}
			ptdr.setLotValue1(pickTicketDetail.getPickTicket().getUserField1()); //工厂编码
			ptdr.setQueryRequire(WmsSOQueryRequireType.E); //等于
			ptdr.setLevel(WmsLotCategoryType.FORCEMATCHED);//等级
			ptdr.setAllowModified(false); //不允许修改
			
			commonDao.store(ptdr);
		}
		
		if(WmsPickticketBillTypeCode.TGYSCK.equals(billCode)) {//退供应商出库
			if(pickTicketDetail.getPickTicket().getSupplier()==null) {
				throw new BusinessException("退供应商出库单供应商不能为空");
			}
			
			WmsPickTicketDetailRequire ptdr2 = EntityFactory.getEntity(WmsPickTicketDetailRequire.class);
			ptdr2.setPickTicketDetail(pickTicketDetail);
			ptdr2.setLotKey("SUPPLIER_CODE"); //批次属性
			ptdr2.setLotValue1(pickTicketDetail.getPickTicket().getSupplier().getCode()); //供应商编码
			ptdr2.setQueryRequire(WmsSOQueryRequireType.E); //等于
			ptdr2.setLevel(WmsLotCategoryType.FORCEMATCHED);//等级
			ptdr2.setAllowModified(false); //不允许修改
			commonDao.store(ptdr2);
			
		}else if(billCode.equals(WmsPickticketBillTypeCode.BFCKD) 
				|| billCode.equals(WmsPickticketBillTypeCode.QTCKD) 
				|| billCode.equals(WmsPickticketBillTypeCode.XSJHD)
				&& isNew){
			addLotInfo(pickTicketDetail);//添加批次信息
		}
	}
	
	/**如果是其它出库单或者是报废出库单  需要加一条项目类别=标准的批次信息   修改拣货单也会调此方法 fs*/
	private void addLotInfo(WmsPickTicketDetail pickTicketDetail){
		WmsWarehouse w = commonDao.load(WmsWarehouse.class, pickTicketDetail.getPickTicket().getWarehouse().getId());
		
		WmsPickTicketDetailRequire ptdr3 = EntityFactory.getEntity(WmsPickTicketDetailRequire.class);
		ptdr3.setPickTicketDetail(pickTicketDetail);
		ptdr3.setLotKey("EXTEND_PROPC8"); //批次属性
		if(w.getCode().equals("VMI")){//VMI仓库的项目类别为寄售
			ptdr3.setLotValue1(WmsFactoryXmlb.JS); //项目类别
		}else{
			ptdr3.setLotValue1(WmsFactoryXmlb.BZ); //项目类别
		}
		
		ptdr3.setQueryRequire(WmsSOQueryRequireType.E); //等于
		ptdr3.setLevel(WmsLotCategoryType.FORCEMATCHED);//等级
		ptdr3.setAllowModified(Boolean.TRUE); //不允许修改
		commonDao.store(ptdr3);
	}
	
	public void createWorkDoc(Object obj) {
//		super.createWorkDoc(obj);
		
		List<WmsTask> wmsTasks = null;
		
		WmsWarehouse warehouse = null;
		String billCode = "";
		WmsWorkDoc workDoc = new WmsWorkDoc();
		Map<String,List<WmsTask>> keeperMap = new HashMap<String, List<WmsTask>>();
		Boolean splitFlag = Boolean.FALSE;//是否拆分拣货单标识
		Boolean flag = Boolean.FALSE;//标记拣货单是否有未创建作业单的task
		//普通拣货单
		if(obj instanceof WmsPickTicket){
			WmsPickTicket pickTicket = (WmsPickTicket)obj;
			WmsBillType billType = commonDao.load(WmsBillType.class, pickTicket.getBillType().getId());
//			WmsPickTicket newOpenPickTicket = null;//部分分配的拣货单拆出来的新的拣货单
			if(pickTicket.getStatus().equals(WmsPickTicketStatus.PARTALLOCATED)){//如果是部分分配的拣货单,生成作业单的时候自动把未分配的明细拆出来
				//销售出库单不能拆单，因为一个销售出库单只能传SAP一次
				if(!WmsPickticketBillTypeCode.XSJHD.equals(billType.getCode())){
//					throw new BusinessException("部分分配的销售出库单不能创建作业单");
					/**拆分拣货单*/
					createNewSupplyPickTicket(pickTicket.getWarehouse(), pickTicket,Boolean.FALSE);
					splitFlag = Boolean.TRUE;
				}
				
			}
			if(WmsPickticketBillTypeCode.XSJHD.equals(billType.getCode()) 
					&& WmsPickTicketStatus.PART_SHIP.equals(pickTicket.getStatus())){
				throw new BusinessException("已经发了一部分的销售出库单，不能再次拣配");
			}
			
			Long keeperId = 0L; //保管员ID
			for(WmsPickTicketDetail pickDetail : pickTicket.getDetails()){
				Double unAllocateQty = pickDetail.getUnAllocateQty();//未分配数量
				/**如果拣货明细未完全分配,则拆分出来生成新的拣货单*/
				if(unAllocateQty > 0){
					if(!WmsPickticketBillTypeCode.XSJHD.equals(billType.getCode())){
						pickDetail.setExpectedQty(DoubleUtils.sub(pickDetail.getExpectedQty(), unAllocateQty));
						pickDetail.setExpectedPackQty(DoubleUtils.sub(pickDetail.getExpectedPackQty(), unAllocateQty));
						commonDao.store(pickDetail);
					}
				}
				
				if(pickDetail.getAllocatedQty() <= 0D){
					continue;
				}
				String hql1 = "FROM WmsItemKeeper wik WHERE wik.item.id =:itemId AND wik.warehouse.id =:warehouseId AND wik.status =:status ";
				String hql2 = " AND wik.factory.code =:factoryCode ";
				String hql3 = " AND wik.factory IS NULL ";
				
				
				String hql = "FROM WmsTask t WHERE t.relatedObjId =:relatedObjId AND t.workDoc IS NULL";
				wmsTasks = commonDao.findByQuery(hql, "relatedObjId", pickDetail.getId());
				
				
				for(WmsTask t : wmsTasks) {
					flag = Boolean.TRUE;
					Long warehouseId = pickTicket.getWarehouse().getId();
					if(WmsFactoryXmlb.JS.equals(t.getItemKey().getLotInfo().getExtendPropC8())) { //vmi
						warehouseId = 4L;//vmi仓库id
					}
					
					
					
					//先根据仓库+工厂+物料找对应的保管员   如果是线边仓的库存  则看是标准还是寄售  如果是寄售 则给vmi的保管员  2017-11-06 19:29:36
					List<WmsItemKeeper> keepers = commonDao.findByQuery(hql1+hql2, new String[]{"itemId","warehouseId","status","factoryCode"},
							new Object[]{pickDetail.getItem().getId(),warehouseId,WmsSapWarehouseStatus.ENABLED,pickTicket.getUserField1()});
					if(keepers.isEmpty()){
						//上面找不到就根据 仓库+物料找对应的保管员
						keepers = commonDao.findByQuery(hql1+hql3, new String[]{"itemId","warehouseId","status"}, 
								new Object[]{pickDetail.getItem().getId(),warehouseId,WmsSapWarehouseStatus.ENABLED});
						if(!keepers.isEmpty()){
							keeperId = keepers.get(0).getKeeper().getId();
						}else{
							keeperId = 0L;
						}
					}else{
						keeperId = keepers.get(0).getKeeper().getId();
					}
					String key = keeperId.toString();
					if(!keeperMap.containsKey(key)){
						keeperMap.put(key, new ArrayList<WmsTask>());
					}
					keeperMap.get(key).add(t);
					
//					
//					if(!keeperMap.containsKey(key)){
//						keeperMap.put(key, wmsTasks);
//					}else{
//						for(WmsTask task : wmsTasks){
//							keeperMap.get(key).add(task);
//						}
//					}
//					
				}
				
				
				
				
			}
			/**如果生成了新的拣货单,新的拣货单刷新数量以及状态---旧的拣货单也刷新下状态,没问题的话应该是已分配*/
			if(splitFlag){
				pickTicket.refreshAllQty();
				pickTicket.defineStatus();
			}
			
			Set<String> keys  = keeperMap.keySet();
			for(String key : keys){
				WmsWorkDoc keeperWorkDoc = new WmsWorkDoc();
				ThornUser keeper = commonDao.load(ThornUser.class, Long.valueOf(key));
				if(keeper != null){
					keeperWorkDoc.setKeeper(keeper);
				}else{
					keeperWorkDoc.setKeeper(null);
				}
				String relateCode = pickTicket.getRelatedBill1();//工单号或线体
				String hql = "from ProductionOrder where code=:code";
				ProductionOrder po = (ProductionOrder) commonDao.findByQueryUniqueResult(hql,"code",relateCode);
				keeperWorkDoc.setProductLine(relateCode);//产线
				if(null != po){
					keeperWorkDoc.setProductLine(po.getProductLine());//产线
				}
				keeperWorkDoc.setProductOrderCode(relateCode);//工单号
				keeperWorkDoc.setUserField5(pickTicket.getUserField6());//产线描述
				
				if(!StringHelper.isNullOrEmpty(relateCode)){
					List<ProductionOrder> pos = commonDao.findByQuery("from ProductionOrder where code=:code", "code", relateCode);
					if(pos.size() > 0){
						keeperWorkDoc.setProductOrderCode(pos.get(0).getCode());//工单号
						keeperWorkDoc.setProductLine(pos.get(0).getProductLine());//产线
					}
				}
				keeperWorkDoc.setUserField4(pickTicket.getUserField5());//备注
				keeperWorkDoc.setType(WmsWorkDocType.PICKING);
				//单号编码规则生成作业单号
				String workDocCode = wmsBussinessCodeManager.generateCodeByRule(pickTicket.getWarehouse(),WmsWorkDocType.PICKING);
				keeperWorkDoc.setCode(workDocCode);
				keeperWorkDoc.setWarehouse(pickTicket.getWarehouse());
				keeperWorkDoc.setRelatedBillCode(pickTicket.getCode());
				keeperWorkDoc.setStatus(WmsWorkDocStatus.ENABLED);
				keeperWorkDoc.setBillTypeCode(billType.getCode());
				keeperWorkDoc.setBillTypeName(billType.getName());
		        commonDao.store(keeperWorkDoc);
				this.bandTaskByWorkDoc(keeperWorkDoc.getId(),keeperMap.get(key));//将task跟workdoc绑定,并生成任务组
				if(StringHelper.in(keeperWorkDoc.getWarehouse().getCode(), 
						new String[]{WarehouseEnumeration.XYJ,WarehouseEnumeration.BX}) ){
					for(WmsTask task : keeperMap.get(key)){
						if(WmsFactoryXmlb.JS.equals(task.getItemKey().getLotInfo().getExtendPropC8())){
							keeperWorkDoc.setUserField4("VMI");
							commonDao.store(keeperWorkDoc);
							break;
						}
					}
				}
				
			}
			
		}
		
		//波次拣货单
		if(obj instanceof WmsWaveDoc){
			String hql = "SELECT DISTINCT(t) FROM WmsTask t,WmsPickTicketDetail pickTicketDetail "
					+ "WHERE pickTicketDetail.pickTicket.waveDoc.id =:waveDocId "
					+ "AND t.relatedBillCode =pickTicketDetail.pickTicket.code AND t.workDoc IS NULL";
			WmsWaveDoc waveDoc = (WmsWaveDoc)obj;
			wmsTasks = commonDao.findByQuery(hql, "waveDocId", waveDoc.getId());
			warehouse = waveDoc.getWarehouse();
			billCode = waveDoc.getCode();
			workDoc.setType(WmsWorkDocType.WAVE_PICKING);
		}
		
		//波次拣货单根据拣货单分组为task的二次分拣序号赋予1~n的值；
		if(obj instanceof WmsWaveDoc){
			WmsWaveDoc waveDoc = (WmsWaveDoc)obj;
			String hql = "SELECT t FROM WmsPickTicket t WHERE t.waveDoc.id = :waveDocId";
			List<WmsPickTicket> pickTickets = commonDao.findByQuery(hql, "waveDocId", waveDoc.getId());
			
			String hql2 = "FROM WmsTask t WHERE t.relatedObjId IN "
					+ " (SELECT pickTicketDetail.id FROM WmsPickTicketDetail pickTicketDetail "
					+ " WHERE pickTicketDetail.pickTicket.id = :pickTicketID)"
					+ " AND t.status in (:stlist)";
			List<String> stlist = new ArrayList<String>();
			stlist.add(WmsTaskStatus.ENABLED);
			stlist.add(WmsTaskStatus.ALLOCATED);
			
			for (int i = 0; i < pickTickets.size(); i++) {
				WmsPickTicket wmsPickTicket = pickTickets.get(i);
				List<WmsTask> tasks2 = commonDao.findByQuery(hql2, 
						new String[]{"pickTicketID", "stlist"}, new Object[]{wmsPickTicket.getId(), stlist});
				for (WmsTask wmsTask : tasks2) {
					wmsTask.setSortDisplaygroupSeq(Integer.toString(i+1));
					commonDao.store(wmsTask);
				}
			}
		};
		
		
		//加工作业单
		if(obj instanceof WmsKittingDoc){
			//task.setRelatedObjId(kittingDoc.getId());
			//task.setRelatedBillCode(kittingDoc.getCode());
			String hql = "FROM WmsTask t WHERE t.relatedBillCode =:relatedBillCode AND t.workDoc IS NULL";
			WmsKittingDoc kittingDoc = (WmsKittingDoc)obj;
			wmsTasks = commonDao.findByQuery(hql, "relatedBillCode", kittingDoc.getCode());
			warehouse = kittingDoc.getWarehouse();
			billCode = kittingDoc.getCode();
			workDoc.setType(WmsWorkDocType.KITTING_PICKING);
			
			if(wmsTasks.size() <= 0){
				throw new BusinessException("kitting.workDoc.has.been.created",new String[]{billCode});
			}
		}
				
		if(!flag){
			throw new BusinessException("pickTicket.workDoc.has.been.created",new String[]{billCode});
		}
		if(obj instanceof WmsKittingDoc || obj instanceof WmsWaveDoc){
			//单号编码规则生成作业单号
			String workDocCode = wmsBussinessCodeManager.generateCodeByRule(warehouse,workDoc.getType());
			workDoc.setCode(workDocCode);
			workDoc.setWarehouse(warehouse);
			workDoc.setRelatedBillCode(billCode);
			workDoc.setStatus(WmsWorkDocStatus.ENABLED);
	        commonDao.store(workDoc);
			this.bandTaskByWorkDoc(workDoc.getId(),wmsTasks);//将task跟workdoc绑定,并生成任务组
		}
		
		if(obj instanceof WmsPickTicket){
			WmsPickTicket pickTicket = (WmsPickTicket)obj;
			String hql = "select id from WmsWorkDoc w where "
					+ "w.relatedBillCode=:relatedBillCode and w.status=:status";
			
			List<Long> workDocIds = commonDao.findByQuery(hql,
					new String[]{"relatedBillCode","status"},
					new Object[]{pickTicket.getCode(),WmsWorkDocStatus.ENABLED});
			for(Long workDocId : workDocIds){
				hql = "from WmsTask w where w.workDoc.id=:workDocID";
				List<WmsTask> tasks = commonDao.findByQuery(hql,"workDocID",workDocId);
				List<WmsTask> readyTasks = new ArrayList<WmsTask>();//有线边发货库位的作业单直接生成配送单
				for(WmsTask task : tasks){
					/**XB库位自动作业确认*/
					if(null != task.getFromLocation().getCode() 
							&& task.getFromLocation().getCode().equals(WmsLocationCode.XB)){
						
						WmsItem item = commonDao.load(WmsItem.class, task.getItem().getId());
						Double qty = DoubleUtils.sub(task.getPlanQty(),task.getPutawayQty(),item.getBuPrecision()); 
						
						WmsTclWorkDocManager tclWorkDocManager = (WmsTclWorkDocManager) applicationContext.getBean("wmsTclWorkDocManager");
						Long workId = task.getWorker() == null ? null : task.getWorker().getId();
						tclWorkDocManager.singleWorkConfirm(task, task, qty, workId);
						WmsBillType billType = commonDao.load(WmsBillType.class, pickTicket.getBillType().getId());
						//只有生产领料单分配到线边库位的库存才做配送单自动发运
						if(WmsPickticketBillTypeCode.SCLLD.equals(billType.getCode())){
							readyTasks.add(task);
						}
					}
				}
				//暂时先不用待完善,(循环task去判断移出库位)
				//线边仓的作业单直接生成配送单出库
				WmsWorkDoc doc = commonDao.load(WmsWorkDoc.class, workDocId);
				//非空代表有线边发货库位的库存，需要直接生成配送单
				if(!readyTasks.isEmpty()){
					WmsTclWorkDocManager tclWorkDocManager = (WmsTclWorkDocManager) applicationContext.getBean("wmsTclWorkDocManager");
					//创建配送单
					WmsWorkDoc xbfhWorkDoc = EntityFactory.getEntity(WmsWorkDoc.class);
					xbfhWorkDoc.setUserField2(com.vtradex.wms.server.model.entity.production.WmsWorkDocType.T_1_AREA);
					xbfhWorkDoc.setUserField4(doc.getUserField4()==null ? "系统交接_"+doc.getWarehouse().getCode() : "系统交接_"+doc.getUserField4());
					xbfhWorkDoc.setStatus(WmsWorkDocStatus.READY_ALLOCATE);
					xbfhWorkDoc.setRelatedBillCode(doc.getCode());
					
					String locHql = "FROM WmsLocation loc WHERE loc.code=:code AND loc.status=:status AND loc.warehouse.id =:warehouseId";
					List<WmsLocation> locs = commonDao.findByQuery(locHql, new String[]{"code","status","warehouseId"}, 
							new Object[]{WmsLocationCode.XBS,BaseStatus.ENABLED,doc.getWarehouse().getId()});
					if(locs.isEmpty()){
						throw new RfBusinessException("未维护XBFH调拨库位信息，请先维护");
					}
					tclWorkDocManager.newWorkDoc(locs.get(0).getId(), xbfhWorkDoc, null, doc.getWarehouse());
					commonDao.store(xbfhWorkDoc);
					//添加配送单明细
					String invHql = "FROM WmsInventory inv WHERE inv.itemKey.lotInfo.extendPropC20 =:workDocCode " +
							" AND inv.qty>0 AND inv.operationStatus= 'NORMAL' AND inv.location.countLock = 'N' " +
							" AND inv.warehouse.baseOrganization.id =:baseOrganizationId AND inv.location.type =:type " +
							" AND inv.location.code=:locCode ";
					List<WmsInventory> invs = commonDao.findByQuery(invHql, new String[]{"workDocCode","baseOrganizationId","type","locCode"}, 
							new Object[]{doc.getCode(),doc.getWarehouse().getBaseOrganization().getId(),WmsLocationType.HANDOVER,WmsLocationCode.XBS});
					if(!invs.isEmpty()){
						for(WmsInventory inv : invs){
							List<Double> listValues = new ArrayList<Double>();
							listValues.add(inv.getQty());
							tclWorkDocManager.addToMoveDocDetail(xbfhWorkDoc.getId(), inv, listValues);
						}
					}
					//生效配送单
					xbfhWorkDoc.setStatus(WmsWorkDocStatus.ENABLED);
					tclWorkDocManager.activeQuickShippingWorkDoc(xbfhWorkDoc);
					//记录交接人交接时间
					List<WmsTask> xbfhTasks = commonDao.findByQuery("FROM WmsTask task WHERE task.workDoc.id=:workDocId", 
			    			"workDocId", xbfhWorkDoc.getId());
					for(WmsTask xbfhTask : xbfhTasks){
						xbfhTask.setJjUserLoginName("xtjj");
						xbfhTask.setJjTime(new Date());
						commonDao.store(xbfhTask);
					}
					//发运
					//判断系统属性值如果是1代表线边的自动发运否则就不自动发
					hql = "from WmsSystemValues s where s.code=:code ";
					WmsSystemValues sv = (WmsSystemValues) commonDao.findByQueryUniqueResult(hql, "code", WmsSystemValuesType.XBSHIP);
					if(sv != null && "1".equals(sv.getValue())){
						tclWorkDocManager.shipQuickShippingWorkDoc(xbfhWorkDoc);
					}
				}
			}
		}
	}
	
	
	//删除明细
	public void removePickTicketDetail(WmsPickTicketDetail pickTicketDetail) {
		
		WmsPickTicket pickTicket = load(WmsPickTicket.class,pickTicketDetail.getPickTicket().getId());
		
		 //拣货单类型为预留出库单或销售交货单等,不允许删除
		if(null != pickTicket.getUserField3()
			&& (pickTicket.getUserField3().equals(WmsPickticketGenType.SCLLD)
			|| pickTicket.getUserField3().equals(WmsPickticketGenType.YLCKD) 
//				|| pickTicket.getUserField3().equals(WmsPickticketGenType.XSJHD)
				)
			){
			throw new BusinessException("拣货单类型为预留出库单或生产领料单，不允许删除");
		}
		if(null != pickTicket.getUserField3() && pickTicket.getUserField3().equals(WmsPickticketGenType.XSJHD)) { //删除关系
			String hql = "delete from DeliveryOrderDetailPtDetail ptd where ptd.pickticketDetail.id=:id";
			commonDao.executeByHql(hql, new String[]{"id"}, new Object[]{pickTicketDetail.getId()});
		}
		
		List<WmsPickTicketDetailRequire> detailRequires = this.commonDao.findByQuery("from WmsPickTicketDetailRequire detailRequire where detailRequire.pickTicketDetail.id = :pickTicketDetailId","pickTicketDetailId",pickTicketDetail.getId());
		if(!detailRequires.isEmpty()&&detailRequires.size()>0){
			this.commonDao.deleteAll(detailRequires);
		}
		pickTicket.removeDetails(pickTicketDetail);
		pickTicket.refreshPickTicketQty();
		
		
	}
	
	
	/**拣货单添加明细*/
	public void addPickTicketDetail2(Long pickTicketId,WmsPickTicketDetail pickTicketDetail){
		WmsPickTicket pickTicket = commonDao.load(WmsPickTicket.class, pickTicketId);
		if (pickTicketDetail.isNew()) {
			if (null != pickTicket.getUserField3()
					&& (pickTicket.getUserField3().equals(WmsPickticketGenType.SCLLD)
							|| pickTicket.getUserField3().equals(WmsPickticketGenType.YLCKD) 
							|| pickTicket.getUserField3().equals(WmsPickticketGenType.XSJHD))) {
				throw new BusinessException("拣货单类型为预留出库单、销售交货单等,不允许新建");
			}
		}
		this.addPickTicketDetail(pickTicketId, pickTicketDetail);
	}
	
	/**
	 * 手工退拣
	 * 拣货单单
	 */
	public void manualBackUp(Long inventoryId,Double backUpQty,Integer lineNo,Long podId) {
		// TODO Auto-generated method stub
		String pickTickeDetailHql ="";
		Double originalBackUpQty = backUpQty;//原始的退拣数量
		WmsInventory inventory = commonDao.load(WmsInventory.class, inventoryId);
		List<Long> pickTicketDetailIds = new ArrayList<Long>();
		if (lineNo == null || lineNo.intValue() == 0) {
			pickTickeDetailHql = "SELECT detail.id FROM WmsPickTicketDetail detail WHERE detail.pickTicket.code =:pickTicketCode AND detail.item.id =:itemId AND detail.expectedQty>0";	
			pickTicketDetailIds = commonDao.findByQuery(pickTickeDetailHql, new String[]{"pickTicketCode","itemId"}, new Object[]{inventory.getRelatedBillCode(),inventory.getItem().getId()});
		}else{
			pickTickeDetailHql = "SELECT detail.id FROM WmsPickTicketDetail detail WHERE detail.pickTicket.code =:pickTicketCode AND detail.item.id =:itemId "
					+ " and detail.lineNo =:lineNo AND detail.expectedQty>0";	
			pickTicketDetailIds = commonDao.findByQuery(pickTickeDetailHql, new String[]{"pickTicketCode","itemId","lineNo"}, 
					new Object[]{inventory.getRelatedBillCode(),inventory.getItem().getId(),lineNo});
		}
		
		if(pickTicketDetailIds.size() <= 0 ){
			/**下架到T1库位的库存的关联单号是工单号,所以可能查不到库存*/
			if(inventory.getLocation().getType().equals(WmsLocationType.HANDOVER)){
				inventoryManager.backUp(inventory,backUpQty);		
			}else{
				throw new BusinessException("inventory.did.not.find.details");
			}
		}else{
			String hql = "select w.pickticketDetail.id from ProductionOrderDetailPtDetail w "
				+ "where w.productionOrderDetail.id=:id and w.pickticketDetail.pickedQty>0 " 
				+ "AND w.pickticketDetail.id in(:pickticketDetailId) "
				+ "order by w.productionOrderDetail.id";
			List<Long> ptdIds = commonDao.findByQuery(hql,new String[]{"id","pickticketDetailId"},new Object[]{podId,pickTicketDetailIds});
			for(Long pickTicketDetailId : ptdIds){
				if(backUpQty <= 0){
					break;
				}
				WmsPickTicketDetail detail = commonDao.load(WmsPickTicketDetail.class,pickTicketDetailId);
				//来源为SCLLD、YLCKD的在这里不回写数量
				String source = StringHelper.replaceNullOrEmptyToStr(detail.getPickTicket().getUserField3(), "-");
	//			if(source.equals(WmsPickticketGenType.SCLLD) && !WarehouseEnumeration.XYJ.equals(inventory.getWarehouse().getCode())){
	//				inventoryManager.backUp(inventory,backUpQty);
	//				break;
	//			}
				WmsWaveDoc wavedoc = null;
				if(detail.getPickTicket().getWaveDoc() != null){
					wavedoc = commonDao.load(WmsWaveDoc.class, detail.getPickTicket().getWaveDoc().getId());
					if(!wavedoc.getStatus().equals(WmsWaveDocStatus.FINISHED)){
						throw new BusinessException("has.joined.the.state.wave.picking.orders.for.job.not.to.pick.back");
					}else{
						wavedoc.removeAllocateQty(detail.getPickTicket().getQty()-backUpQty);
						wavedoc.removeQty(detail.getPickTicket().getQty());
					}
				}
	//				//退拣时，处理BOL数量
	//				this.backUpDealBol(inventory,pickTicketDetailId,backUpQty); //BOL处理数量不对 放到后面处理  xuyan.xia
				//处理拣货单明细拣货数量
				Double bakQty = backUpQty;
				if(detail.getPickedQty() > backUpQty){
					bakQty = backUpQty;
					backUpQty = 0D;
				}else{
					bakQty = detail.getPickedQty();
					backUpQty = DoubleUtils.sub(backUpQty, detail.getPickedQty(),inventory.getItem().getBuPrecision());
				}
	//			if(!inventory.getWarehouse().getCode().equals(WarehouseEnumeration.BX)){
					/**退拣时，处理BOL数量*/
					this.backUpDealBol(inventory,pickTicketDetailId,bakQty);
	//			}
				
	//			Double backUpPackQty = WmsPackageUnitUtils.getPackQty(inventory.getPackageUnit(), bakQty, inventory.getItem().getBuPrecision());
				//退拣时退拣最小单位数量
				inventoryManager.backUp(inventory,bakQty);		
				
				/**预留出库单、生产领料单  单独处理,不在这里回写数量*/
				if(!StringHelper.in(source, new String[]{WmsPickticketGenType.SCLLD,WmsPickticketGenType.YLCKD})){
					//更新拣货单明细拣货数量
					detail.pickedQty(-bakQty);
					//如果该拣货单存在超拣比例,则退拣时超出拣货数量的部分不扣分配数量
					if(detail.getOverpickRate().intValue()>0){
						Double allocatedQty = detail.getAllocatedQty();
						Double pickedQty = detail.getPickedQty();
						Double tempQty = DoubleUtils.sub(pickedQty,allocatedQty,inventory.getItem().getBuPrecision());
						if(tempQty.doubleValue()<=0){
							detail.allocate(-bakQty);
						}else{
							//只需要扣除实际分配数量
							Double tempQty1 = DoubleUtils.sub(bakQty,tempQty,inventory.getItem().getBuPrecision());
							if(tempQty1.doubleValue()>0){
							   detail.allocate(-tempQty1);
							}
						}
					}else{
						detail.allocate(-bakQty);
					}
				}
	//			detail.allocate(-bakQty);
				detail.getPickTicket().setWaveDoc(null);  //去除与波次单绑定关系
	//			Double bolQty = detail.getPlanedShipQty();//已加入bol数量
	//			if(bolQty >= originalBackUpQty){//如果已加入bol数量大于等于退拣数量,那么bol数量 -= 退拣数量
	//				detail.setPlanedShipQty(DoubleUtils.sub(bolQty,originalBackUpQty));
	//			}else if(bolQty > 0 && bolQty < originalBackUpQty){//如果bol数量小于退拣数量,则置0
	//				detail.setPlanedShipQty(0D);
	//			}
				commonDao.store(detail);
				
				/**退拣不改变拣货单状态*/
	//			workflowManager.doWorkflow(detail.getPickTicket(),"wmsPickTicketProcess.backUp");
				if(wavedoc != null){
	//				workflowManager.doWorkflow(wavedoc,"wmsWaveDocProcess.backUp");
				}
			}
		}
	}
	
	/**
	 * 手工退拣
	 * 拣货单单
	 */
	public void manualBackUp(Long inventoryId,Double backUpQty,Integer lineNo) {
		// TODO Auto-generated method stub
		String pickTickeDetailHql ="";
		Double originalBackUpQty = backUpQty;//原始的退拣数量
		WmsInventory inventory = commonDao.load(WmsInventory.class, inventoryId);
		List<Long> pickTicketDetailIds = new ArrayList<Long>();
		if (lineNo == null || lineNo.intValue() == 0) {
			pickTickeDetailHql = "SELECT detail.id FROM WmsPickTicketDetail detail WHERE detail.pickTicket.code =:pickTicketCode AND detail.item.id =:itemId AND detail.expectedQty>0";	
			pickTicketDetailIds = commonDao.findByQuery(pickTickeDetailHql, new String[]{"pickTicketCode","itemId"}, new Object[]{inventory.getRelatedBillCode(),inventory.getItem().getId()});
		}else{
			pickTickeDetailHql = "SELECT detail.id FROM WmsPickTicketDetail detail WHERE detail.pickTicket.code =:pickTicketCode AND detail.item.id =:itemId "
					+ " and detail.lineNo =:lineNo AND detail.expectedQty>0";	
			pickTicketDetailIds = commonDao.findByQuery(pickTickeDetailHql, new String[]{"pickTicketCode","itemId","lineNo"}, 
					new Object[]{inventory.getRelatedBillCode(),inventory.getItem().getId(),lineNo});
		}
		
		if(pickTicketDetailIds.size() <= 0 ){
			/**下架到T1库位的库存的关联单号是工单号,所以可能查不到库存*/
			if(inventory.getLocation().getType().equals(WmsLocationType.HANDOVER)){
				inventoryManager.backUp(inventory,backUpQty);		
			}else{
				throw new BusinessException("inventory.did.not.find.details");
			}
		}
		
		for(Long pickTicketDetailId : pickTicketDetailIds){
			if(backUpQty <= 0){
				break;
			}
			WmsPickTicketDetail detail = commonDao.load(WmsPickTicketDetail.class,pickTicketDetailId);
			//来源为SCLLD、YLCKD的在这里不回写数量
			String source = StringHelper.replaceNullOrEmptyToStr(detail.getPickTicket().getUserField3(), "-");
//			if(source.equals(WmsPickticketGenType.SCLLD) && !WarehouseEnumeration.XYJ.equals(inventory.getWarehouse().getCode())){
//				inventoryManager.backUp(inventory,backUpQty);
//				break;
//			}
			WmsWaveDoc wavedoc = null;
			if(detail.getPickTicket().getWaveDoc() != null){
				wavedoc = commonDao.load(WmsWaveDoc.class, detail.getPickTicket().getWaveDoc().getId());
				if(!wavedoc.getStatus().equals(WmsWaveDocStatus.FINISHED)){
					throw new BusinessException("has.joined.the.state.wave.picking.orders.for.job.not.to.pick.back");
				}else{
					wavedoc.removeAllocateQty(detail.getPickTicket().getQty()-backUpQty);
					wavedoc.removeQty(detail.getPickTicket().getQty());
				}
			}
//				//退拣时，处理BOL数量
//				this.backUpDealBol(inventory,pickTicketDetailId,backUpQty); //BOL处理数量不对 放到后面处理  xuyan.xia
			//处理拣货单明细拣货数量
			Double bakQty = backUpQty;
			if(detail.getPickedQty() > backUpQty){
				bakQty = backUpQty;
				backUpQty = 0D;
			}else{
				bakQty = detail.getPickedQty();
				backUpQty = DoubleUtils.sub(backUpQty, detail.getPickedQty(),inventory.getItem().getBuPrecision());
			}
//			if(!inventory.getWarehouse().getCode().equals(WarehouseEnumeration.BX)){
				/**退拣时，处理BOL数量*/
				this.backUpDealBol(inventory,pickTicketDetailId,bakQty);
//			}
			
//			Double backUpPackQty = WmsPackageUnitUtils.getPackQty(inventory.getPackageUnit(), bakQty, inventory.getItem().getBuPrecision());
			//退拣时退拣最小单位数量
			inventoryManager.backUp(inventory,bakQty);		
			
			/**预留出库单、生产领料单  单独处理,不在这里回写数量*/
			if(!StringHelper.in(source, new String[]{WmsPickticketGenType.SCLLD,WmsPickticketGenType.YLCKD})){
				//更新拣货单明细拣货数量
				detail.pickedQty(-bakQty);
				//如果该拣货单存在超拣比例,则退拣时超出拣货数量的部分不扣分配数量
				if(detail.getOverpickRate().intValue()>0){
					Double allocatedQty = detail.getAllocatedQty();
					Double pickedQty = detail.getPickedQty();
					Double tempQty = DoubleUtils.sub(pickedQty,allocatedQty,inventory.getItem().getBuPrecision());
					if(tempQty.doubleValue()<=0){
						detail.allocate(-bakQty);
					}else{
						//只需要扣除实际分配数量
						Double tempQty1 = DoubleUtils.sub(bakQty,tempQty,inventory.getItem().getBuPrecision());
						if(tempQty1.doubleValue()>0){
						   detail.allocate(-tempQty1);
						}
					}
				}else{
					detail.allocate(-bakQty);
				}
			}
//			detail.allocate(-bakQty);
			detail.getPickTicket().setWaveDoc(null);  //去除与波次单绑定关系
//			Double bolQty = detail.getPlanedShipQty();//已加入bol数量
//			if(bolQty >= originalBackUpQty){//如果已加入bol数量大于等于退拣数量,那么bol数量 -= 退拣数量
//				detail.setPlanedShipQty(DoubleUtils.sub(bolQty,originalBackUpQty));
//			}else if(bolQty > 0 && bolQty < originalBackUpQty){//如果bol数量小于退拣数量,则置0
//				detail.setPlanedShipQty(0D);
//			}
			commonDao.store(detail);
			
			/**退拣不改变拣货单状态*/
//			workflowManager.doWorkflow(detail.getPickTicket(),"wmsPickTicketProcess.backUp");
			if(wavedoc != null){
//				workflowManager.doWorkflow(wavedoc,"wmsWaveDocProcess.backUp");
			}
		}
	}

	public void backUpDealBol(WmsInventory inventory,Long pickTicketDetailId,Double backUpQty) {
		/**
		 * 根据拣货单明细找到对应的BOL删除数量
		 */
		WmsPickTicketDetail ptDetail = this.commonDao.load(WmsPickTicketDetail.class, pickTicketDetailId);
		WmsPickTicket pickTicket = this.commonDao.load(WmsPickTicket.class,ptDetail.getPickTicket().getId() );
		WmsWarehouse warehouse = this.commonDao.load(WmsWarehouse.class, inventory.getWarehouse().getId());
		WmsItemKey itemKey = this.commonDao.load(WmsItemKey.class, inventory.getItemKey().getId());
		WmsCompany company = this.commonDao.load(WmsCompany.class, inventory.getCompany().getId());
		WmsBillType billType = commonDao.load(WmsBillType.class, ptDetail.getPickTicket().getBillType().getId());
		//退拣扣除bol明细跟bol数量时，查询拣货流程配置规则表
		//如客户化拣货流程配置规则表,需调用客户化方法
		String billTypeName ="";
		if(pickTicket.getWaveDoc()!=null){
			billTypeName = "波次拣货单";
		}else{
			billTypeName = billType.getName();
		}
		Map<String, Object> value = wmsRuleManager.getRuleTableDetail("R101_拣货流程配置规则表", company.getName(),billTypeName);
		String bolDetailHql ="";
		List<Long> bolDetailIds = new ArrayList<Long>();
		Double bolBakQty = backUpQty;
		if (value != null && "是".equals((String) value.get("是否需要容器拣货"))
				&&"是".equals((String) value.get("是否需要包装"))) {
			throw new BusinessException("the.rule.table.create.error");
		}else if (value==null||(value != null &&"否".equals((String) value.get("是否需要包装")))) {
			//如果未配置拣货流程规则表||(数据不为空&&是否需要包装==否)【意味着是正常拣货流程/容器拣货流程】；
			//则根据库存拣货单明细、货品、库存的itemKey查询生效bol明细，循环bol明细，bol明细中如果存在bol，过滤掉bol状态是已装车/发运/并已绑定装车单。
			//反之，则扣除bol明细数量。
			//如循环结束后退拣数量>0,则提示异常“退拣数量不足,请检查拣货单对应bol是否已装车或已发运!”
			bolDetailHql = " select bolDetail.id from WmsBolDetail bolDetail where bolDetail.pickTicketDetail.id = :pickTicketDetailId "
					            + " and bolDetail.itemKey.lotInfo.extendPropC10 =:extendPropC10 and isPacking = 'N'"
					            + " AND bolDetail.itemKey.lotInfo.supplierCode=:supplierCode AND bolDetail.itemKey.lotInfo.lot=:lot"
					            + " AND bolDetail.itemKey.item.id =:itemId";
			bolDetailIds = commonDao.findByQuery(bolDetailHql,new String[]{"pickTicketDetailId","extendPropC10","supplierCode","lot","itemId"},
					new Object[]{pickTicketDetailId,itemKey.getLotInfo().getExtendPropC10(),itemKey.getLotInfo().getSupplierCode(),
					itemKey.getLotInfo().getLot(),itemKey.getItem().getId()});
			for(Long bolDetailId : bolDetailIds){
				if(bolBakQty <=0){
					break;
				}
				WmsBolDetail bolDetail = this.commonDao.load(WmsBolDetail.class, bolDetailId);
				WmsBol bol = new WmsBol();
				if(bolDetail.getBol()!=null){
			    	bol= this.commonDao.load(WmsBol.class, bolDetail.getBol().getId());
			    	//过滤掉bol状态是已装车/发运/并已绑定装车单
			    	if(WmsBolStatus.SHIPPED.equals(bol.getStatus())||
			    			WmsBolStatus.LOADING.equals(bol.getStatus())||
			    			bol.getMasterBol()!=null){
			    		continue;
			    	}
				}
				//调用公共方法扣除bol明细数量
				bolBakQty = this.subBolDetailQty(bolDetail, bolBakQty);
			}
			//如果退拣数量>0,则提示异常“退拣数量不足,请检查拣货单对应bol是否已装车或已发运!”
			if(bolBakQty>0){
				throw new BusinessException("bolBakQty.is.not.enough.please.check.this.bol.isLoaded.or.isShiped");
			}
		}else if(value != null && "是".equals((String) value.get("是否需要包装"))){
			//如果数据不为空&&是否需要包装==是【意味着是包装流程】；
		    //则根据库存拣货单明细、货品、库存的itemKey查询生效bol明细【默认查询条件是否需要包装【是】/计划数量>已包装数量】，循环明细，只扣除计划数量-包装数量的bol明细数量，如果bol明细数量<=0,则删除该明细。
		    //如循环结束后退拣数量>0，则提示异常“退拣数量不足，请检查拣货单对应bol是否已打包!”
			bolDetailHql = " select bolDetail.id from WmsBolDetail bolDetail where bolDetail.pickTicketDetail.id = :pickTicketDetailId "
		                 + " and bolDetail.itemKey.id =:itemkeyId and isPacking = 'Y' and bolDetail.planQty - bolDetail.packedQty >0 ";
            bolDetailIds = commonDao.findByQuery(bolDetailHql,new String[]{"pickTicketDetailId","itemkeyId"},
		               new Object[]{pickTicketDetailId,itemKey.getId()});
            for(Long bolDetailId : bolDetailIds){
				if(bolBakQty <=0){
					break;
				}
				WmsBolDetail bolDetail = this.commonDao.load(WmsBolDetail.class, bolDetailId);
				//调用公共方法扣除bol明细数量
				bolBakQty = this.subBolDetailQty(bolDetail, bolBakQty);
            }
			//如果退拣数量>0,则提示异常“退拣数量不足，请检查拣货单对应bol是否已打包!”
			if(bolBakQty>0){
				throw new BusinessException("bolBakQty.is.not.enough.please.check.this.bol.has.finished.packing");
			}
		}
	}
	/**
	 * 生产领料单退拣
	 */
	@Override
	public void singleBackUp(Long invId, ProductionOrderDetail pod,List<String> tableValues) {
		Double backUpQty;
		try {
			backUpQty = Double.valueOf(tableValues.get(0));//退拣数量
		} catch (NumberFormatException nfe) {
			throw new BusinessException("backUpQty.can.only.be.a.numeric.type");
		}
		if(backUpQty <= 0){
			throw new BusinessException("退拣数量必须大于0");
		}
		
		WmsInventory inv = commonDao.load(WmsInventory.class, invId);
//		super.singleBackUp(inv, tableValues);
		if (DoubleUtils.sub(backUpQty.doubleValue(), inv.getQty().doubleValue()) >0 ) {
			throw new BusinessException("退拣数量不能大于库存数量");
		}
//		Map<Long,Double> backUpMap = new HashMap<Long,Double>();
		
//		backUpMap.put(inventory.getId(), backUpQty);
		Integer lineNo = 0;
		this.manualBackUp(inv.getId(), backUpQty, lineNo,pod.getId());
		
		Double pickQty = pod.getPickedQuantityBu()-pod.getOldShippedQuantityBu();//已拣数量 (要去掉接口过来的原拣货数量)
		if(backUpQty - pickQty > 0){
			throw new BusinessException("退拣数量不能大于已拣数量!");
		}
		
		//扣除拣货单的分配以及拣货数量
		writeBackPickTick(pod.getId(),backUpQty,"");
		
		WmsWarehouse wh = inv.getWarehouse();
		//如果VMI做退拣,生成拣货单的仓库必须是自管仓
		if(wh.getCode().equals(WarehouseEnumeration.VMI)){
			WmsFactoryWarehouse w = findWmsFactoryWarehouse(pod.getProductionOrder().getFactory().getId());
			wh = w.getWarehouse();
		}
		
		/**生成拣货单*/
		this.createPickByTj(pod.getId(), backUpQty, wh,null);
	}
	
	/**预留出库单的退拣*/
	public void singleYLBackUp(Long invId, WmsReservedOrderDetail rod,List<String> tableValues) {
		Double backUpQty = Double.valueOf(tableValues.get(0));//退拣数量
		if(backUpQty <= 0){
			throw new BusinessException("退拣数量必须大于0");
		}
		
		WmsInventory inv = commonDao.load(WmsInventory.class, invId);
		super.singleBackUp(inv, tableValues);
		
		Double pickQty = rod.getPickedQuantityBu();//已拣数量
		if(backUpQty - pickQty > 0){
			throw new BusinessException("退拣数量不能大于已拣数量!");
		}
		
		dealPtAndRodBackQty(rod.getId(),backUpQty);
		rod.subPickedQuantityBu(backUpQty);//已拣数量-退拣数量
		rod.subAllocatedQuantityBu(backUpQty);//分配数量-退拣数量
		if(rod.getAllocatedQuantityBu() < 0D){//如果拣货单超拣,那么做退拣可能造成预留单明细的分配数量为负数,
			rod.setAllocatedQuantityBu(0d);
		}
		commonDao.store(rod);
		
		WmsWarehouse wh = inv.getWarehouse();
		//如果VMI做退拣,生成拣货单的仓库必须是自管仓
		if(wh.getCode().equals(WarehouseEnumeration.VMI)){
			WmsFactoryWarehouse w = findWmsFactoryWarehouse(rod.getFactory().getId());
			wh = w.getWarehouse();
		}
		
		/**预留退拣生成拣货单*/
		createYLPickByTj(rod.getId(), backUpQty, wh);
	}

	@Override
	public void normalSingleBackUp(WmsInventory inv, List<String> tableValues) {
		String hql = "select userField3 from WmsPickTicket w where w.code=:code";
		String billType = (String) commonDao.findByQueryUniqueResult(hql,"code",inv.getRelatedBillCode());
		billType = StringHelper.replaceNullOrEmptyToStr(billType, "-") ;
		//销售出库单只有在发运的时候，才回写已交货数量，所以销售交货单退拣时是不用回写任何数量的，用普通的推荐按钮
		if(billType.equals(WmsPickticketGenType.SCLLD) 
				|| billType.equals(WmsPickticketGenType.YLCKD)){
			throw new BusinessException("生产领料单、预留出库单类型的拣货单应该用对应按钮做退拣!");
		}
		if(inv.getLocation().getType().equals("HANDOVER")){
			throw new BusinessException("调拨库位的退拣请使用T-1区退拣按钮!!");
		}
		singleBackUp(inv, tableValues);
	}	
	
	/**
	 * 根据退拣的物料生成拣货单以及明细
	 * @param productDetailId
	 * @param qty
	 * @param warehouse
	 */
	public void createPickByTj(Long productDetailId,Double qty,WmsWarehouse warehouse,String source){
		
		ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, productDetailId);
		Double cancelQty = pod.getAllocatedQuantityBu();
		/**
		 * 问题描述：不可拆包料存在超分配的情况，退料时如果按照计划数量来退，就存在实际已经退完了，但是工单的分配数量还是挂着一部分数量，
		 * 这会导致后面关单时，按照工单明细的未分配数量来扣除拣货单明细的数量时，拣货单明细的数量未扣完，所以拣货单明细也就不会被删除，会继续发货，但是其实工单是已经关单的了，不应该再次发货
		 * 修改：退料时判断计划数量和分配数量谁大谁小，用小的数量来扣除退料数量然后再赋值给分配数量
		 */
		Double dealQty = 0D;
		if(DoubleUtils.sub(pod.getPlanQuantityBu(), pod.getAllocatedQuantityBu())>0){
			dealQty = DoubleUtils.sub(pod.getAllocatedQuantityBu(),qty);
		}else{
			dealQty = DoubleUtils.sub(pod.getPlanQuantityBu(),qty);
		}
		dealQty = CommonHelper.dealDoubleError(dealQty);
		//来源是RF代表是生产退料
		if(WmsPickSource.RF.equals(source)){
			//退料的数量如果和工单的计划数量一致，代表这个工单全部都要退回，此时把分配数量和拣货数量及发运数量直接改成0
			if(DoubleUtils.sub(qty, pod.getPlanQuantityBu())==0D){
				//生产订单退料需要把对应关系解除,存在不可拆包的物流多分配的情况，所以工单全部退料的话，数量按分配数量来
				this.writeBackPickTick(pod.getId(), cancelQty,source);
				pod.setAllocatedQuantityBu(0D);
				pod.setPickedQuantityBu(0D);
				pod.setShippedQuantityBu(0D);
				pod.setHandQty(0D);
			}else{
				//工单不是全部都退，数量按实际退料的数量来
				this.writeBackPickTick(pod.getId(), qty,source);
				pod.setAllocatedQuantityBu(dealQty);
				pod.setPickedQuantityBu(dealQty);
				pod.subShippedQuantityBu(qty);
			}
		}else{
			pod.subPickedQuantityBu(qty);//已拣数量-退拣数量
			///**考虑物料超拣情况,如果拣货数量扣完退拣数量比分配数量小,这时候分配数量和拣货数量一致,否则不用更新分配数量*/
//			if(pod.getPickedQuantityBu() - pod.getAllocatedQuantityBu() < 0){
//				pod.setAllocatedQuantityBu(pod.getPickedQuantityBu());
//			}
			/**
			 * 20171124改成分配数量和拣货数量同步修改，如果工单计划10个分配10个拣货2个，这时候做退拣2个，分配数量就会变成0，实际分配数量应该是8
			 */
			pod.subAllocatedQuantityBu(qty);
			if(pod.getAllocatedQuantityBu() < 0D){//可能存在超拣情况
				pod.setAllocatedQuantityBu(0D);
			}
		}
		//交接数量:是VMI交接出库时生成交接入库时的数量依据，也要相应的减少，否则会出现退拣后交接数量没减少，导致下次系统交接出的时候，相应交接入库的数量会少一部分未交接过去
		pod.setHandQty(DoubleUtils.sub(pod.getHandQty(), qty));
		if(pod.getHandQty()<0){
			pod.setHandQty(0D);
		}
		pod.defineStatus();//更新工单明细的三个状态
		commonDao.store(pod);
		
		ProductionOrder po = commonDao.load(ProductionOrder.class,pod.getProductionOrder().getId());
		String relateBill = null;//相关单号,冰箱库则为产线,洗衣机为工单号
		if(StringHelper.in(pod.getProductionOrder().getFactory().getCode(),
				new String[]{WmsSapFactoryCodeEnum.BX_NX,WmsSapFactoryCodeEnum.BX_WX})){//冰箱库则相关单号为产线
			relateBill = po.getProductLine();
		}else{
			relateBill = po.getCode();
		}
		
		
		ProductionOrderManager productionOrderManager = (ProductionOrderManager)applicationContext.getBean("productionOrderManager");
		
		WmsPickTicket pickTicket = findTjPick(relateBill,source,pod.getProductionOrder().getBeginDate(),po.getFactory().getCode());//找退拣类型状态为打开的拣货单
		WmsPickTicketDetail pickTicketDetail = null;
		if(null == pickTicket){
			/**根据物料的退拣数量生成一个拣货单*/
			pickTicket = productionOrderManager.createWmsPickTicketByProductionOrder(warehouse, pod.getProductionOrder().getBeginDate(),WmsPickticketGenType.SCLLD);
			if("RF".equals(source)){
				pickTicket.setUserField5("退料");
			}else{
				pickTicket.setUserField5("退拣");
			}
			pickTicket.setUserField6(po.getLineDesc());
			pickTicket.setUserField7(po.getProductLine());
			pickTicket.setUserField1(pod.getProductionOrder().getFactory().getCode());//工厂编码
			pickTicket.setRelatedBill1(relateBill);
		}else{
			//判断此拣货单有没有此物料的明细,有的话直接加上退拣数量,没有的话新增一条拣货明细
			//如果是可拆包料，相同物料也不合并，因为可能发生两个工单明细合并生成一个拣货单明细，工单明细之前就是部分分配的状态，此时工单明细
			//的未分配数量如果大于合并后的拣货单明细计划数量，回写分配的时候只会回写到一个工单上，但实际是两个工单明细
			if(WmsItemUnPackingAtt.WAREHOUSE_UNPACKING.equals(pod.getItem().getUserFieldV3())){
				pickTicketDetail = findPickDetailByItem(pod.getItem().getId(),pickTicket.getId());
			}
		}
		//对应关系中的计划数量不能大于工单的计划数量，否则会导致工单无法发运
		Double podptdqty = qty;
		if(DoubleUtils.sub(pod.getPlanQuantityBu(), qty)<0D){
			podptdqty = pod.getPlanQuantityBu();
		}
		if(pickTicketDetail == null){//没找到就增
			pickTicketDetail = productionOrderManager.creatWmsPickTicketDetail(pickTicket,pod.getItem(), pod.getPackageUnit(), qty);
			productionOrderManager.storeProductionOrderDetailPtDetail(pod, pickTicketDetail,podptdqty);//创建一条关系数据
		}else{
			Double packQty = DoubleUtils.add(pickTicketDetail.getExpectedPackQty(),qty * pickTicketDetail.getPackageUnit().getConvertFigure());
			pickTicketDetail.setExpectedPackQty(packQty);
			pickTicketDetail.setExpectedQty(DoubleUtils.add(pickTicketDetail.getExpectedQty(),qty));
			commonDao.store(pickTicketDetail);
			
			String hql = "from ProductionOrderDetailPtDetail p where p.productionOrderDetail.id=:pod and p.pickticketDetail.id=:ptd";
			List<ProductionOrderDetailPtDetail> podAndPtds = commonDao.findByQuery(hql,new String[]{"pod","ptd"},new Object[]{pod.getId(),pickTicketDetail.getId()});
			if(podAndPtds.size() > 1){
				throw new BusinessException("根据拣货单明细["+pickTicketDetail.getId()+"]和工单明细["+pod.getId()+"]找到多条关系表数据,请检查!");
			}else if(podAndPtds.size() <= 0){
				productionOrderManager.storeProductionOrderDetailPtDetail(pod, pickTicketDetail,podptdqty);//创建一条关系数据
			}else if(podAndPtds.size() == 1){
				podAndPtds.get(0).addQuantityBu(qty);
				commonDao.store(podAndPtds.get(0));
			}
		}
		pickTicket.refreshPickTicketQty();
		commonDao.store(pickTicket);
	}
	
	/**
	 * 预留出库单退拣生成拣货单
	 * @param rodId
	 * @param qty  退拣数量
	 * @param warehouse
	 */
	public void createYLPickByTj(Long rodId,Double qty,WmsWarehouse warehouse){
		
		WmsReservedOrderDetail reservedOrderDetail = commonDao.load(WmsReservedOrderDetail.class,rodId);
		WmsReservedOrder reservedOrder = commonDao.load(WmsReservedOrder.class, reservedOrderDetail.getReservedOrder().getId());
		
		String relateBill = reservedOrderDetail.getGroupNo();//拣货单相关单号=分组号
		
		ProductionOrderManager productionOrderManager = (ProductionOrderManager)applicationContext.getBean("productionOrderManager");
		
		WmsPickTicket pickTicket = findTjPick(relateBill,"",reservedOrder.getJzrq(),reservedOrderDetail.getFactory().getCode());//找退拣类型状态为打开的拣货单,预留退拣肯定是页面操作的
		WmsPickTicketDetail pickTicketDetail = null;
		if(null == pickTicket){
			/**根据物料的退拣数量生成一个拣货单*/
			pickTicket = productionOrderManager.createWmsPickTicketByProductionOrder(warehouse,reservedOrder.getJzrq(),WmsPickticketGenType.YLCKD);
			pickTicket.setUserField5("退拣");
			pickTicket.setUserField1(reservedOrderDetail.getFactory().getCode());//工厂编码
			pickTicket.setRelatedBill1(relateBill);
		}else{
			//判断此拣货单有没有此物料的明细,有的话直接加上退拣数量,没有的话新增一条拣货明细
			pickTicketDetail = findPickDetailByItem(reservedOrderDetail.getItem().getId(),pickTicket.getId());
		}
		
		if(pickTicketDetail == null){//没找到就增
			pickTicketDetail = productionOrderManager.creatWmsPickTicketDetail(pickTicket,reservedOrderDetail.getItem(), reservedOrderDetail.getUnit(), qty);
			storeReserveOrderPtDetail(reservedOrderDetail, pickTicketDetail,qty);//创建一条关系数据
		}else{
			Double packQty = DoubleUtils.add(pickTicketDetail.getExpectedPackQty(),qty * pickTicketDetail.getPackageUnit().getConvertFigure());
			pickTicketDetail.setExpectedPackQty(packQty);
			pickTicketDetail.setExpectedQty(DoubleUtils.add(pickTicketDetail.getExpectedQty(),qty));
			commonDao.store(pickTicketDetail);
			
			String hql = "from ReservedOrderDetailPtDetail r where r.reservedOrderDetail.id=:rod and r.pickticketDetail.id=:ptd";
			List<ReservedOrderDetailPtDetail> rodAndPtds = commonDao.findByQuery(hql,new String[]{"rod","ptd"},new Object[]{reservedOrderDetail.getId(),pickTicketDetail.getId()});
			
			if(rodAndPtds.size() > 1){
				throw new BusinessException("根据拣货单明细["+pickTicketDetail.getId()+"]和工单明细["+reservedOrderDetail.getId()+"]找到多条关系表数据,请检查!");
			}else if(rodAndPtds.size() <= 0){
				storeReserveOrderPtDetail(reservedOrderDetail, pickTicketDetail,qty);//创建一条关系数据
			}else if(rodAndPtds.size() == 1){
				rodAndPtds.get(0).addQuantity(qty);
				commonDao.store(rodAndPtds.get(0));
			}
		}
		pickTicket.refreshPickTicketQty();
		commonDao.store(pickTicket);
	}
	
	/**
	 * 根据相关单号和订单日期来找退拣类型的拣货单,如果找到了,就把退拣的数量和物料加到这个拣货单下面
	 * @return
	 */
	private WmsPickTicket findTjPick(String poCode,String source,Date orderDate,String factory){
		String hql = "from WmsPickTicket w where w.userField5=:userField5 "
				+ "and w.status='OPEN' and w.relatedBill1=:relatedBill1 " 
				+ "and to_char(w.orderDate,'yyyy-mm-dd')=:orderDate and w.userField1=:userField1 ";
		List<WmsPickTicket> pickTickets = new ArrayList<WmsPickTicket>();
		if(StringHelper.isNullOrEmpty(source)){//来源字段未空表示是页面上操作的退拣，否则就是PDA操作的生产退料
			pickTickets = commonDao.findByQuery(hql,new String[]{"userField5","relatedBill1","orderDate","userField1"},
					new Object[]{"退拣",poCode,DateUtil.formatDateYMDToStr(orderDate),factory});
		}else{
			pickTickets = commonDao.findByQuery(hql,new String[]{"userField5","relatedBill1","orderDate","userField1"},
					new Object[]{"退料",poCode,DateUtil.formatDateYMDToStr(orderDate),factory});
		}
		if(pickTickets.size() > 0){
			return pickTickets.get(0);
		}
		return null;
	}
	
	/**
	 * 根据物料找拣货单明细
	 * @param itemId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private WmsPickTicketDetail findPickDetailByItem(Long itemId,Long pickId){
		String hql = "from WmsPickTicketDetail w where w.item.id=:itemId and w.pickTicket.id=:pId";
		
		List<WmsPickTicketDetail> pickTicketDetail = commonDao.findByQuery(hql,
									new String[]{"itemId","pId"},new Object[]{itemId,pickId});
		if(pickTicketDetail.size() > 0){
			return pickTicketDetail.get(0);
		}
		return null;
	}
	
	/**
	 * 根据工单明细id找到对应拣货单明细,将拣货单明细的拣货和分配数量相应扣除退拣数量 fs
	 * source 有值代表是生产退料
	 * @param podId			工单明细ID
	 * @param backUpQty		退拣数量
	 */
	private void writeBackPickTick(Long podId,Double backUpQty,String source){
		List<ProductionOrderDetailPtDetail> relateDatas = new ArrayList<ProductionOrderDetailPtDetail>();//记录关联数量为0的关系表数据
		List<WmsPickTicket> pickTickets = new ArrayList<WmsPickTicket>();//记录明细对应的拣货单,最后刷新单头数量
		String hql = "select w.pickticketDetail from ProductionOrderDetailPtDetail w "
				+ "where w.productionOrderDetail.id=:id and w.pickticketDetail.pickedQty>0 "
				+ "order by w.productionOrderDetail.id";
		List<WmsPickTicketDetail> list = commonDao.findByQuery(hql,"id",podId);
		for(WmsPickTicketDetail p : list){
			if(backUpQty <= 0){
				break;
			}
			hql = "from ProductionOrderDetailPtDetail w "
					+ "where w.productionOrderDetail.id=:id and w.pickticketDetail.id=:pId ";
			List<ProductionOrderDetailPtDetail> podpds = commonDao.findByQuery(hql,
					new String[]{"id","pId"},new Object[]{podId,p.getId()});
			Double tempBackupQty = backUpQty;//这个数量给关系表用
			for(ProductionOrderDetailPtDetail podpd : podpds){//扣掉关系表的关联数量
				if(tempBackupQty <= 0){
					break;
				}
				if(tempBackupQty - podpd.getQuantityBu() >= 0){
					//工单明细的分配数量-退拣数量,如果等于0,那么删掉关系表数据,如果!=0,则不删
					Double qty = DoubleUtils.sub(podpd.getProductionOrderDetail().getAllocatedQuantityBu(), tempBackupQty);
					if(qty == 0D){//工单明细与拣货明细关系表的数量如果扣完了,则删除此关联关系
						relateDatas.add(podpd);
					}
					tempBackupQty = DoubleUtils.sub(tempBackupQty,podpd.getQuantityBu());
				}else{
					podpd.removeQuantityBu(tempBackupQty);
					if(WmsPickSource.RF.equals(source)){//退料相应的发运数量也有扣减
						podpd.setShipQty(DoubleUtils.sub(podpd.getShipQty(), tempBackupQty));
					}
					tempBackupQty = 0D;
				}
			}
			
			if(p.getPickedQty() - backUpQty >= 0){//拣货数量>退拣数量
				p.setAllocatedQty(DoubleUtils.sub(p.getAllocatedQty(),backUpQty));
				p.setPickedQty(DoubleUtils.sub(p.getPickedQty(),backUpQty));
				p.setExpectedPackQty(DoubleUtils.sub(p.getExpectedPackQty(),backUpQty*p.getPackageUnit().getConvertFigure()));
				p.setExpectedQty(DoubleUtils.sub(p.getExpectedQty(),backUpQty));
				//生产退料需要减少相应的发运数量
				if(WmsPickSource.RF.equals(source)){
					p.setShippedQty(DoubleUtils.sub(p.getShippedQty(), backUpQty));
				}
				backUpQty = 0D;
			}else{//拣货数量<退拣数量
				p.setAllocatedQty(0D);
				p.setPickedQty(0D);
				p.setExpectedPackQty(0d);
				p.setExpectedQty(0d);
				if(WmsPickSource.RF.equals(source)){
					p.setShippedQty(0D);
				}
				backUpQty = backUpQty - p.getAllocatedQty();
			}
			if(p.getExpectedPackQty() < 0D || p.getExpectedQty() < 0D){//超拣情况会存在拣货数量>计划数量,所以计划数量可能会扣成负数
				p.setExpectedPackQty(0D);
				p.setExpectedQty(0D);
				if(p.getAllocatedQty() > 0D){//可能出现    计划数量<退拣数量<拣货分配数量
					p.setExpectedPackQty(p.getAllocatedQty());
					p.setExpectedQty(p.getAllocatedQty());
				}
			}
			commonDao.store(p);
			if(!pickTickets.contains(p.getPickTicket())){
				pickTickets.add(p.getPickTicket());
			}
		}
		for(WmsPickTicket pick : pickTickets){
			pick.refreshAllQty();
			pick.refreshShippedQty();
			commonDao.store(pick);
		}
		if(relateDatas.size() > 0){
			/**删除关联数量=0的关系表数据*/
			commonDao.deleteAll(relateDatas);
		}
 	}
	
	/**添加预留单明细和拣货单明细对应关系*/
	private void storeReserveOrderPtDetail(WmsReservedOrderDetail reservedOrderDetail,WmsPickTicketDetail pickTicketDetail,Double quantityBu){
		ReservedOrderDetailPtDetail odp = new ReservedOrderDetailPtDetail
						(reservedOrderDetail, pickTicketDetail, quantityBu, reservedOrderDetail.getUnit());
		
		this.commonDao.store(odp);
	}
	
	/**
	 * 根据预留单明细id找到对应拣货单明细,将拣货单明细的拣货和分配数量相应扣除退拣数量 fs
	 * @param rodId			工单明细ID
	 * @param backUpQty		退拣数量
	 */
	private void dealPtAndRodBackQty(Long rodId,Double backUpQty){
		List<ReservedOrderDetailPtDetail> relateDatas = new ArrayList<ReservedOrderDetailPtDetail>();//记录关联数量为0的关系表数据
		List<WmsPickTicket> pickTickets = new ArrayList<WmsPickTicket>();//记录明细对应的拣货单,最后刷新单头数量
		String hql = "select w.pickticketDetail from ReservedOrderDetailPtDetail w "
				+ "where w.reservedOrderDetail.id=:id and w.pickticketDetail.pickedQty>0 "
				+ "order by w.reservedOrderDetail.id";
		List<WmsPickTicketDetail> list = commonDao.findByQuery(hql,"id",rodId);
		for(WmsPickTicketDetail p : list){
			if(backUpQty <= 0){
				break;
			}
			hql = "from ReservedOrderDetailPtDetail w "
					+ "where w.reservedOrderDetail.id=:id and w.pickticketDetail.id=:pId";
			List<ReservedOrderDetailPtDetail> rodpds = commonDao.findByQuery(hql,
					new String[]{"id","pId"},new Object[]{rodId,p.getId()});
			Double tempBackupQty = backUpQty;//这个数量给关系表用
			for(ReservedOrderDetailPtDetail rodpd : rodpds){//扣掉关系表的关联数量
				if(tempBackupQty <= 0){
					break;
				}
				if(tempBackupQty - rodpd.getQuantityBu() >= 0){
					//工单明细的分配数量-退拣数量,如果等于0,那么删掉关系表数据,如果!=0,则不删
					Double qty = DoubleUtils.sub(rodpd.getReservedOrderDetail().getAllocatedQuantityBu(), tempBackupQty);
					if(qty == 0D){//工单明细与拣货明细关系表的数量如果扣完了,则删除此关联关系
						relateDatas.add(rodpd);
					}
					tempBackupQty = DoubleUtils.sub(tempBackupQty,rodpd.getQuantityBu());
				}else{
					rodpd.removeQuantityBu(tempBackupQty);
					tempBackupQty = 0D;
				}
			}
			
			if(p.getPickedQty() - backUpQty >= 0){//拣货数量>退拣数量
				p.setAllocatedQty(DoubleUtils.sub(p.getAllocatedQty(),backUpQty));
				p.setPickedQty(DoubleUtils.sub(p.getPickedQty(),backUpQty));
				p.setExpectedPackQty(DoubleUtils.sub(p.getExpectedPackQty(),backUpQty*p.getPackageUnit().getConvertFigure()));
				p.setExpectedQty(DoubleUtils.sub(p.getExpectedQty(),backUpQty));
				backUpQty = 0D;
			}else{//拣货数量<退拣数量
				p.setAllocatedQty(0D);
				p.setPickedQty(0D);
				p.setExpectedPackQty(0d);
				p.setExpectedQty(0d);
				backUpQty = backUpQty - p.getAllocatedQty();
			}
			if(p.getExpectedPackQty() < 0D || p.getExpectedQty() < 0D){//超拣情况会存在拣货数量>计划数量,所以计划数量可能会扣成负数
				p.setExpectedPackQty(0D);
				p.setExpectedQty(0D);
			}
			commonDao.store(p);
			if(!pickTickets.contains(p.getPickTicket())){
				pickTickets.add(p.getPickTicket());
			}
		}
		for(WmsPickTicket pick : pickTickets){
			pick.refreshAllQty();
			commonDao.store(pick);
		}
		if(relateDatas.size() > 0){
			/**删除关联数量=0的关系表数据*/
			commonDao.deleteAll(relateDatas);
		}
 	}
}
