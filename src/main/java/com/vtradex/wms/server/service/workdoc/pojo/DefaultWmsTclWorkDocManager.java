package com.vtradex.wms.server.service.workdoc.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.client.utils.StringUtils;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.wms.server.enumeration.WmsSapFactoryCodeEnum;
import com.vtradex.wms.server.enumeration.WmsTclWorkDocType;
import com.vtradex.wms.server.model.component.LotInfo;
import com.vtradex.wms.server.model.dto.WmsInventoryDto;
import com.vtradex.wms.server.model.entity.base.WmsShippingLotTruck;
import com.vtradex.wms.server.model.entity.base.WmsShippingLotTruckBillType;
import com.vtradex.wms.server.model.entity.base.WmsSystemValues;
import com.vtradex.wms.server.model.entity.base.WmsSystemValuesType;
import com.vtradex.wms.server.model.entity.base.WmsWorker;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.item.WmsInventoryState;
import com.vtradex.wms.server.model.entity.item.WmsItemJITAtt;
import com.vtradex.wms.server.model.entity.item.WmsItemKey;
import com.vtradex.wms.server.model.entity.item.WmsItemUnPackingAtt;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.pickticket.WmsWaveDoc;
import com.vtradex.wms.server.model.entity.production.AssignmentStatus;
import com.vtradex.wms.server.model.entity.production.PickingStatus;
import com.vtradex.wms.server.model.entity.production.ProductionOrder;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetailPtDetail;
import com.vtradex.wms.server.model.entity.production.ShippingStatus;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderDetail;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;
import com.vtradex.wms.server.model.entity.workdoc.WmsWorkDoc;
import com.vtradex.wms.server.model.enums.WmsBillOfType;
import com.vtradex.wms.server.model.enums.WmsInventoryLogType;
import com.vtradex.wms.server.model.enums.WmsInventoryOperationStatus;
import com.vtradex.wms.server.model.enums.WmsInventoryStatus;
import com.vtradex.wms.server.model.enums.WmsLocationCode;
import com.vtradex.wms.server.model.enums.WmsLocationType;
import com.vtradex.wms.server.model.enums.WmsPickTicketStatus;
import com.vtradex.wms.server.model.enums.WmsPickticketGenType;
import com.vtradex.wms.server.model.enums.WmsTaskRelatedObjType;
import com.vtradex.wms.server.model.enums.WmsTaskStatus;
import com.vtradex.wms.server.model.enums.WmsTaskType;
import com.vtradex.wms.server.model.enums.WmsWaveDocStatus;
import com.vtradex.wms.server.model.enums.WmsWorkDocStatus;
import com.vtradex.wms.server.service.bol.WmsBolManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryLogManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.item.TclMessageManager;
import com.vtradex.wms.server.service.item.WmsItemManager;
import com.vtradex.wms.server.service.message.WmsCustomerManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.task.WmsTaskManager;
import com.vtradex.wms.server.service.workdoc.WmsTclWorkDocManager;
import com.vtradex.wms.server.service.workdoc.WmsTransactionalManager;
import com.vtradex.wms.server.utils.DateUtil;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.server.utils.WmsPackageUnitUtils;
import com.vtradex.wms.webservice.utils.CommonHelper;

/**
 * 
 * Tcl定制化作业单任务
 * 
 * @author <a href="zhen.lei@vtradex.com">Yogurt_lei</a>
 * 
 * @date 2017年7月19日 下午4:28:04
 */
public class DefaultWmsTclWorkDocManager extends DefaultWmsWorkDocManager implements WmsTclWorkDocManager {

    protected WmsInventoryLogManager wmsInventoryLogManager;
    protected TclMessageManager tclMessageManager;
    
    
    public DefaultWmsTclWorkDocManager(WorkflowManager workflowManager,
            WmsInventoryManager inventoryManager, WmsBolManager wmsBolManager,
            WmsTaskManager wmsTaskManager,WmsBussinessCodeManager wmsBussinessCodeManager,
            WmsRuleManager wmsRuleManager,WmsCustomerManager wmsCustomerManager,
            WmsTransactionalManager wmsTransactionalManager, WmsInventoryLogManager wmsInventoryLogManager,
            TclMessageManager tclMessageManager) {
        super(workflowManager, inventoryManager, wmsBolManager, wmsTaskManager,
                wmsBussinessCodeManager, wmsRuleManager, wmsCustomerManager,
                wmsTransactionalManager);
        this.wmsInventoryLogManager = wmsInventoryLogManager;
        this.tclMessageManager = tclMessageManager;
    }

    @Override
    public void saveQuickShippingWorkDoc(Long locationId, WmsWorkDoc workDoc) {
    	newWorkDoc(locationId, workDoc,null,null);
    }
    
    @Transactional
    public WmsWorkDoc newWorkDoc(Long locationId, WmsWorkDoc workDoc,String jitType,WmsWarehouse warehouse) {
    	if (workDoc.isNew()) {
    		if(null != workDoc.getUserField3() 
    				&& workDoc.getUserField3().equals(WmsItemJITAtt.JIT_DOWNLINE_SETTLE)){
    			throw new BusinessException("无法新建JIT下线出库单");
    		}
            workDoc.setType(WmsTclWorkDocType.QUICK_SHIPPING);
            if(null == warehouse){//页面新增JIT出库
            	warehouse = (WmsWarehouse)commonDao.findByQueryUniqueResult("FROM WmsWarehouse warehouse "
	                    + "WHERE warehouse.baseOrganization.id = :baseOrganizationId", 
	                    new String[] {"baseOrganizationId"}, 
	                    new Object[] {BaseOrganizationHolder.getThornBaseOrganization().getId()});
	            workDoc.setWarehouse(warehouse);
            }else{
            	workDoc.setWarehouse(warehouse);
            }
            //单号编码规则生成作业单号
            String workDocCode = wmsBussinessCodeManager.generateCodeByRule(warehouse,workDoc.getType());
            workDoc.setCode(workDocCode);
        }
        
        String locCode = (String) commonDao.findByQueryUniqueResult("SELECT loc.code FROM WmsLocation loc "
                + "WHERE loc.id=:locID", "locID", locationId);
        workDoc.setUserField1(locCode);
        if(null != jitType){//jitType有值说明是接口调用了此方法
        	workDoc.setUserField3(jitType);//线下结算
        }
        this.commonDao.store(workDoc);
        return workDoc;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void activeQuickShippingWorkDoc(WmsWorkDoc workDoc) {
    	List<WmsTask> tasks = commonDao.findByQuery("FROM WmsTask task WHERE task.workDoc.id=:workDocId", 
    			"workDocId", workDoc.getId());
    	for (WmsTask task : tasks) {
    		task.setStartTime(new Date());
    		task.setPickedQty(task.getPlanQty());
    		task.setStatus(WmsTaskStatus.IN_OPERATION);
//            task.setToLocation(task.getFromLocation()); // 出库等于入库n	
    		this.commonDao.store(task);
    		workDoc.addAllocateQty(task.getPlanQty(), task.getItem().getBuPrecision());
    	}
    	
    }
    
    /**
     * 配送单失效
     */
    @Override
    public void unActiveQuickShippingWorkDoc(WmsWorkDoc workDoc) {
    	List<WmsTask> tasks = commonDao.findByQuery("FROM WmsTask task WHERE task.workDoc.id=:workDocId", 
    			"workDocId", workDoc.getId());
    	for (WmsTask task : tasks) {
    		task.setStartTime(null);
    		task.setPickedQty(0D);
    		task.setStatus(WmsTaskStatus.READY_ALLOCATE);
//            task.setToLocation(task.getFromLocation()); // 出库等于入库n	
    		this.commonDao.store(task);
    		workDoc.setAllocateQty(0D);
    	}
    	
    }
    /**
     * 配送单删除
     */
    @Override
    public void deleteWorkDoc(WmsWorkDoc workDoc) {
        List<WmsTask> tasks = commonDao.findByQuery("FROM WmsTask task WHERE task.workDoc.id=:workDocId", 
                "workDocId", workDoc.getId());
        if(tasks.size() > 0){
        	throw new BusinessException("配送单有明细无法删除,请检查!!");
        }
        commonDao.delete(workDoc);
    }

    private List<WmsTask> sortTaskByInv(Long workDocId){
    	String hql = "FROM WmsInventory inv WHERE inv.task.workDoc.id =:workDocId" +
    			" order by inv.location.code desc,inv.itemKey.lotInfo.storageDate asc";
        List<WmsInventory> invs = commonDao.findByQuery(hql, "workDocId", workDocId);
        List<WmsTask> tasks = new ArrayList<WmsTask>();
        for(WmsInventory inv : invs){
        	if(!tasks.contains(inv.getTask())){
        		tasks.add(inv.getTask());
        	}
        }
        return tasks;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void shipQuickShippingWorkDoc(WmsWorkDoc workDoc) {
        List<WmsTask> tempTasks = commonDao.findByQuery("FROM WmsTask task WHERE task.workDoc.id=:workDocId", 
                "workDocId", workDoc.getId());
        List<Long> ids = new ArrayList<Long>();
        for (WmsTask task : tempTasks) {
        	ids.add(task.getId());
        }
        /**根据库存的存储日期将task排序,先进先出*/
        List<WmsTask> tasks = sortTaskByInv(workDoc.getId());
        
        for (int i = 0; i < tasks.size(); i++) {
        	this.detailShipQuickShippingTask(tasks.get(i));
        }
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void detailShipQuickShippingTask(WmsTask task) {
    	Double pickQty = task.getPickedQty();
    	
        WmsWorkDoc workDoc = task.getWorkDoc();
        String relatedBill = workDoc.getCode();
        
        if (workDoc.getPickedQty()==0) {
            workDoc.setStartTime(new Date());
        } 
        task.setEndTime(new Date());
        task.setStatus(WmsTaskStatus.FINISH);
        this.commonDao.store(task);
        List<WmsInventory> invs = commonDao.findByQuery("FROM WmsInventory inv WHERE inv.task.id=:taskId", 
                "taskId", task.getId());
        if(invs.isEmpty()) {
        	throw new BusinessException("未找到作业任务对应库存!");
        }
        for(WmsInventory inventory : invs) {
        	if(StringHelper.isNullOrEmpty(inventory.getItemKey().getLotInfo().getExtendPropC15())) {
        		throw new BusinessException("库存的扩展字段15应为拣货单明细ID，不能为空");
        	}
        	//拣货单明细ID
        	Long pickDetailID = Long.parseLong(inventory.getItemKey().getLotInfo().getExtendPropC15());
        	/**未发运数量*/
        	Double unShipQty = (Double) commonDao.findByQueryUniqueResult("SELECT "
        			+ "sum(pt.quantityBu-pt.shipQty) "
        	 		+ "FROM ProductionOrderDetailPtDetail pt LEFT JOIN pt.productionOrderDetail pod "
 	                + "WHERE pt.pickticketDetail.id=:ptID AND pod.planQuantityBu-pod.shippedQuantityBu>0 "
 	                + "ORDER BY pod.productionOrder.id, pod.id", "ptID", pickDetailID);
        	unShipQty = unShipQty == null ? 0d : unShipQty;
        	/**如果未发运数量<task数量*/
        	Double moveQty = 0D;//移位数量
//        	String splitValue = StringHelper.isNullOrEmpty(inventory.
//        			getItem().getUserFieldV3()) ? "" : inventory.getItem().getUserFieldV3();//拆包属性
        	/**不可拆包多余的分配数量做移位*/
//        	if(unShipQty < pickQty && splitValue.equals(WmsItemUnPackingAtt.WAREHOUSE_UNPACKING)){
        	if(DoubleUtils.sub(unShipQty, pickQty)< 0 ){//现在有超拣的业务,所以拣货数量可能大于分配数量
        		moveQty =DoubleUtils.sub(pickQty, unShipQty) ;
        		pickQty -= moveQty;//发运数量
        		newMoveInventory(inventory, CommonHelper.dealDoubleError(moveQty),workDoc.getCode());//多的数量做移位
        		if(pickQty <= 0){
        			commonDao.delete(inventory);
        			//return;
        		}
        	}
        	if(CommonHelper.dealDoubleError(pickQty)>0){
        		 //回写出库数量
            	Map<Long,Double> map = tclCustomShipping(pickDetailID, pickQty,inventory);
            	tclMessageManager.sendProductionShipInfo(Long.parseLong(inventory.getItemKey().getLotInfo().getExtendPropC15()), inventory, task.getPickedQty(),map);
                //生成发货库存日志
                wmsInventoryLogManager.addInventoryLog(WmsInventoryLogType.SHIPPING, 
                        relatedBill, null, task.getFromLocation(), 
                        task.getCompany(), inventory.getItemKey(), 
                        pickQty, -1*pickQty, inventory.getStatus(), "", "快捷发货");
                
                commonDao.delete(inventory);  
        	}
        	
            workDoc.pickedQty(pickQty, moveQty,task.getItem().getBuPrecision());
//            if (workDoc.getPickedQty().equals(workDoc.getAllocateQty())) {
//                workDoc.setEndTime(new Date());
//            }
            //配送单出库也要回写发运数量
            WmsPickTicketDetail detail = commonDao.load(WmsPickTicketDetail.class, pickDetailID);
            detail.shippedQty(task.getPickedQty());
            commonDao.store(detail);
            WmsPickTicket pick = commonDao.load(WmsPickTicket.class, detail.getPickTicket().getId());
            if(!WmsPickTicketStatus.FINISHED.equals(pick.getStatus())){
            	workflowManager.doWorkflow(detail.getPickTicket(),"wmsPickTicketProcess.ship");
            }
//            if(WmsTaskType.PICKING.equals(task.getType())) {//发货作业单才会写ptd的数量  配送单也会调用此方法，但是没有发货单不能刷数量，配送单的task的type是move
//	            WmsPickTicketDetail detail = commonDao.load(WmsPickTicketDetail.class, pickDetailID);
//	            detail.shippedQty(pickQty);
//	            commonDao.store(detail);
//	    		workflowManager.doWorkflow(detail.getPickTicket(),"wmsPickTicketProcess.ship");
//            }
        }
        //有部分配送单单头的拣货数量task完成了但是没回写，导致配送单的状态没改成完成，现在通过判断task是否全部完成来改配送单的状态
        String hql = "from WmsTask task where task.workDoc.id=:workDocId and task.status !=:status ";
        List<WmsTask> tasks = commonDao.findByQuery(hql, new String[]{"workDocId","status"}, 
        		new Object[]{workDoc.getId(),WmsTaskStatus.FINISH});
        if(tasks.isEmpty()){
        	workDoc.setStatus(WmsWorkDocStatus.FINISH);
        	workDoc.setEndTime(new Date());
        }
       
    }
    
    /**新建一条新库存,数量=移位数量,库位=XB库位*/
    private void newMoveInventory(WmsInventory inventory,Double moveQty,String code){
    	WmsInventory inv = EntityFactory.getEntity(WmsInventory.class);
		List<WmsLocation> locations = commonDao.findByQuery("from WmsLocation l where l.code=:code AND l.warehouse.id=:warehouseId ",
				new String[]{"code","warehouseId"},
				new Object[]{WmsLocationCode.XB,inventory.getWarehouse().getId()});
		if(locations.size() <= 0){
			throw new BusinessException("系统没有维护线边库位,请检查!");
		}
		inv.setPackQty(moveQty);
		inv.setQty(moveQty);
		inv.setWarehouse(inventory.getWarehouse());
		inv.setCompany(inventory.getCompany());
		inv.setItem(inventory.getItem());
		inv.setItemKey(inventory.getItemKey());
		inv.setPackageUnit(inventory.getPackageUnit());
//		inv.setTask(inventory.getTask());
		//线边仓的库存不应该带task，不然如果发生配送单做两次发运，线边库存会出错
		inv.setTask(null);
		inv.setPallet(inventory.getPallet());
		inv.setStatus(inventory.getStatus());
		inv.setLockStatus(inventory.getLockStatus());
		inv.setOperationStatus(WmsInventoryOperationStatus.NORMAL);
		inv.setRelatedBillType(inventory.getRelatedBillType());
		inv.setRelatedBillCode(inventory.getRelatedBillCode());
		inv.setPalletSeq(inventory.getPalletSeq());
		inv.setLocation(locations.get(0));
		commonDao.store(inv);
		/**生成移位日志*/
		wmsInventoryLogManager.addInventoryLog(WmsInventoryLogType.MOVE, 
				code, null, inventory.getLocation(), 
                inv.getCompany(), inventory.getItemKey(), 
                moveQty, -1*moveQty, inventory.getStatus(), "", "不可拆包货品移位");
		
		wmsInventoryLogManager.addInventoryLog(WmsInventoryLogType.MOVE, 
				code, null, inv.getLocation(), 
                inv.getCompany(), inventory.getItemKey(), 
                0d, moveQty, inventory.getStatus(), "", "不可拆包货品移位");
    }
    
    private Map<Long,Double> tclCustomShipping(Long pickTicketDetailId, Double pickedQty,WmsInventory inventory) {
    	WmsPickTicketDetail  pickTicketDetail = commonDao.load(WmsPickTicketDetail.class, pickTicketDetailId);
    	if(pickTicketDetail == null) {
    		throw new BusinessException("根据发货单明细ID"+pickTicketDetailId+"未找到记录");
    	}
    	Map<Long,Double> map = new HashMap<Long,Double>();//key-工单明细ID， value-数量
    	WmsPickTicket pt = pickTicketDetail.getPickTicket();
    	if(WmsPickticketGenType.SCLLD.equals(pt.getUserField3())) { //生产领料类型
	        List<Long> podptdIds =  commonDao.findByQuery("SELECT pt.id FROM ProductionOrderDetailPtDetail pt "
	                + "LEFT JOIN pt.productionOrderDetail pod "
	                + "WHERE pt.pickticketDetail.id=:ptID AND pod.pickedQuantityBu-pod.shippedQuantityBu>0"
	                + "ORDER BY pod.productionOrder.id, pod.id", 
	                    "ptID", pickTicketDetail.getId());
	        
	        for (Long podptdId : podptdIds) {
	        	ProductionOrderDetailPtDetail p = commonDao.load(ProductionOrderDetailPtDetail.class, podptdId);
	            ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, p.getProductionOrderDetail().getId());
	            if(!StringUtils.isEmpty(pod.getDeleteFlag())){
	            	throw new BusinessException("工单已关单不允许发料");
	            }
	            String dateStr = "";
	            Double unPickedQty =  pod.getUnShippedQty();;
				String hql = "from WmsSystemValues s where s.code=:code ";
				WmsSystemValues sv = (WmsSystemValues) commonDao.findByQueryUniqueResult(hql, "code", WmsSystemValuesType.CTDATE);
				if(sv==null){
					dateStr = "20180122";
				}else{
					dateStr = sv.getValue();
				}
				Date date = DateUtil.formatDate(dateStr);
				if(p.getCtDate().after(date)){
					unPickedQty = DoubleUtils.sub(p.getQuantityBu(), p.getShipQty());
				}
	            
	            if (pickedQty <= 0) {
	                break;
	            }
	            if(unPickedQty<= 0){
	            	continue;
	            }
	            Double truckQty =0d;
	            if (unPickedQty>pickedQty) {
	                System.out.println("作业单作业确认修改工单明细发运数量("+pod.getId()+")"+ pickedQty);
	                pod.addShippedQuantityBu(pickedQty);
	                p.addShipQty(pickedQty);
	                commonDao.store(p);
	                if(!map.containsKey(pod.getId())){
		            	map.put(pod.getId(), pickedQty);
		            }else{
		            	map.put(pod.getId(), map.get(pod.getId())+pickedQty);
		            }
	                truckQty=pickedQty;
	                pickedQty = 0D;
	               
	            } else {
	                System.out.println("作业单作业确认修改工单明细发运数量("+pod.getId()+")"+ unPickedQty);
	                pod.addShippedQuantityBu(unPickedQty);
	                p.addShipQty(unPickedQty);
	                commonDao.store(p);
	                if(!map.containsKey(pod.getId())){
		            	map.put(pod.getId(), unPickedQty);
		            }else{
		            	map.put(pod.getId(), map.get(pod.getId())+unPickedQty);
		            }
	                truckQty=unPickedQty;
	                pickedQty = DoubleUtils.sub(pickedQty, unPickedQty);
	            }
	            if(pod.getShippedQuantityBu() >= pod.getPlanQuantityBu()){
	            	pod.setShipStatus(ShippingStatus.SHIPPED);
	            }else{
	            	pod.setShipStatus(ShippingStatus.UNSHIPPED);
	            }
	            commonDao.store(pod);
	            //增加跟踪信息。
	            WmsShippingLotTruck t = EntityFactory.getEntity(WmsShippingLotTruck.class);
                t.setBillType(WmsShippingLotTruckBillType.SCLLD);
                t.setCompany(inventory.getCompany());
                t.setInventoryStatus(inventory.getStatus());
                t.setItem(inventory.getItem());
                t.setLineNo(pod.getLineNo()+"");
                t.setLocation(inventory.getLocation());
                t.setLotInfo(inventory.getItemKey().getLotInfo());
                t.setPackageUnit(inventory.getPackageUnit());
                t.setQty(truckQty);
                t.setRealteId(pod.getProductionOrder().getId());
                t.setRelateCode(pod.getProductionOrder().getCode());
                t.setSubRelateId(pod.getId());
                t.setWarehouse(inventory.getWarehouse());
                commonDao.store(t);
	            
	        }
	        
	        if(CommonHelper.dealDoubleError(pickedQty)>0d) {
	        	throw new BusinessException("PTDID:"+pickTicketDetailId+"发运数量剩余"+CommonHelper.dealDoubleError(pickedQty)+"未回写到工单");
	        }
    	}
    	else {
    		throw new BusinessException("拣货单生成方式UserField3错误");//先抛出异常  现在好像还没有其他的类型需要用到这个快捷发货功能的。
    	}
    	return map;
//    	else if(pt.getBillType().getCode().equals(WmsPickticketGenType.YLCKD)){ //预留出库单
//    		List<Long> podIds =  commonDao.findByQuery("SELECT pod.id FROM ReservedOrderDetailPtDetail pt "
//	                + "LEFT JOIN pt.reservedOrderDetail pod "
//	                + "WHERE pt.pickticketDetail.id=:ptID AND pod.pickedQuantityBu-pod.shippedQuantityBu>0"
//	                + "ORDER BY pod.reservedOrder.id, pod.id", 
//	                    "ptID", pickTicketDetail.getId());
//	        
//	        for (Long podId : podIds) {
//	        	WmsReservedOrderDetail pod = commonDao.load(WmsReservedOrderDetail.class, podId);
//	            Double unPickedQty = pod.getUnShippedQty();
//	
//	            if (pickedQty <= 0) {
//	                break;
//	            }
//	            
//	            if (unPickedQty>pickedQty) {
//	                System.out.println("作业单作业确认修改预留明细发运数量("+podId+")"+ pickedQty);
//	                pod.addShippedQuantityBu(pickedQty);
//	                pickedQty = 0D;
//	            } else {
//	                System.out.println("作业单作业确认修改预留明细发运数量("+podId+")"+ unPickedQty);
//	                pod.addShippedQuantityBu(unPickedQty);
//	                pickedQty = DoubleUtils.sub(pickedQty, unPickedQty);
//	            }
//	            
//	        }
//    		
//    	}
    	
    }
    
    private Map<Long,Double> tclCustomPicked(WmsPickTicketDetail pickTicketDetail, Double pickedQty,Boolean isHand) {
        Map<Long,Double> pros = new HashMap<Long, Double>();//记录处理的工单明细
    	WmsPickTicket pt = pickTicketDetail.getPickTicket();
    	if(pt.getBillType().getCode().equals(WmsPickticketGenType.SCLLD)) {
	        List<Long> podptdIds =  commonDao.findByQuery("SELECT pt.id FROM ProductionOrderDetailPtDetail pt "
	                + "LEFT JOIN pt.productionOrderDetail pod "
	                + "WHERE pt.pickticketDetail.id=:ptID AND pod.allocatedQuantityBu-pod.pickedQuantityBu>0 "
//	                + " AND pod.planQuantityBu-pod.shippedQuantityBu>0 "
	                + "ORDER BY pod.productionOrder.id, pod.id", 
	                    "ptID", pickTicketDetail.getId());
	        Long lastId = 0l;
	        for (Long podptdId : podptdIds) {
	        	ProductionOrderDetailPtDetail p = commonDao.load(ProductionOrderDetailPtDetail.class, podptdId);
	            ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, p.getProductionOrderDetail().getId());
	            Double unPickedQty = pod.getUnPickedQty();
	
	            String dateStr = "";
				String hql = "from WmsSystemValues s where s.code=:code ";
				WmsSystemValues sv = (WmsSystemValues) commonDao.findByQueryUniqueResult(hql, "code", WmsSystemValuesType.CTDATE);
				if(sv==null){
					dateStr = "20180122";
				}else{
					dateStr = sv.getValue();
				}
				Date date = DateUtil.formatDate(dateStr);
				if(p.getCtDate().after(date)){
					unPickedQty = DoubleUtils.sub(p.getLastAllocatedQty(), p.getPickQty());
				}
				
	            if (pickedQty <= 0) {
	                break;
	            }
	            
	            if (unPickedQty>pickedQty) {
	                System.out.println("作业单作业确认修改工单明细拣货数量("+pod.getId()+")"+ pickedQty);
	                pod.addPickedQuantityBu(pickedQty);
	                p.addPickQty(pickedQty);
	                commonDao.store(p);
	                if(isHand){
	                	pod.addHandQty(pickedQty);
	                }
	                pickedQty = 0D;
	            } else {
	                System.out.println("作业单作业确认修改工单明细拣货数量("+pod.getId()+")"+ unPickedQty);
	                pod.addPickedQuantityBu(unPickedQty);
	                p.addPickQty(unPickedQty);
	                commonDao.store(p);
	                if(isHand){
	                	pod.addHandQty(unPickedQty);
	                }
	                pickedQty = DoubleUtils.sub(pickedQty, unPickedQty);
	            }
	            if(pod.getPickedQuantityBu() >= pod.getPlanQuantityBu()){
	            	pod.setPickSataus(PickingStatus.PICKED);
	            }else{
	            	pod.setPickSataus(PickingStatus.UNPICKED);
	            }
	            commonDao.store(pod);
	            
	            
            	Double unPickedQtyFinal = pod.getUnPickedQty();//还剩多少数量未拣
            	if(p.getCtDate().after(date)){
            		unPickedQtyFinal = DoubleUtils.sub(p.getLastAllocatedQty(), p.getPickQty());
				}
	            Double allocateQty = unPickedQty - unPickedQtyFinal;//此次分配的数量
	            pros.put(pod.getId(),allocateQty);
	            lastId = podptdId;//最后一个工单明细ID
	        }
	        if(pickedQty > 0D){//如果拣货单超拣,剩余数量分到最后一个工单明细上
	        	ProductionOrderDetailPtDetail p = commonDao.load(ProductionOrderDetailPtDetail.class, lastId);
	        	ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, p.getProductionOrderDetail().getId());
	        	if(pod == null){
	        		throw new BusinessException("拣货数量未完全分配到工单明细上,剩余数量："+pickedQty+",请检查");
	        	}
	        	pod.addPickedQuantityBu(pickedQty);
	        	commonDao.store(pod);
	        	p.addPickQty(pickedQty);
	        	commonDao.store(p);
	        	if(pros.get(pod.getId()) != null){
	        		Double qty = pros.get(pod.getId());
	        		qty += pickedQty;
	        		pros.put(pod.getId(), qty);
	        	}
	        	pickedQty = 0D;
	        }
    	}
    	else if(pt.getBillType().getCode().equals(WmsPickticketGenType.YLCKD)){ //预留出库单
    		List<Long> podIds =  commonDao.findByQuery("SELECT pod.id FROM ReservedOrderDetailPtDetail pt "
	                + "LEFT JOIN pt.reservedOrderDetail pod "
	                + "WHERE pt.pickticketDetail.id=:ptID AND pod.allocatedQuantityBu-pod.pickedQuantityBu>0 "
	                + " AND pod.quantity-pod.shippedQuantityBu>0 "
	                + "ORDER BY pod.reservedOrder.id, pod.id", 
	                    "ptID", pickTicketDetail.getId());
	        Long lastId = 0L;
	        for (Long podId : podIds) {
	        	lastId = podId;
	        	WmsReservedOrderDetail pod = commonDao.load(WmsReservedOrderDetail.class, podId);
	            Double unPickedQty = pod.getUnPickedQty();
	
	            if (pickedQty <= 0) {
	                break;
	            }
	            
	            if (unPickedQty>pickedQty) {
	                System.out.println("作业单作业确认修改预留明细拣货数量("+podId+")"+ pickedQty);
	                pod.addPickedQuantityBu(pickedQty);
	                pickedQty = 0D;
	            } else {
	                System.out.println("作业单作业确认修改预留明细拣货数量("+podId+")"+ unPickedQty);
	                pod.addPickedQuantityBu(unPickedQty);
	                pickedQty = DoubleUtils.sub(pickedQty, unPickedQty);
	            }
	            
	        }
    		
    	}
        return pros;
    }
    
    /**
	 * 回写拣货单数据  重写  增加回写工单数量
	 * @Description:
	 * @Author:        <a href="yequan.song@vtradex.net">宋叶全</a>
	 * @CreateDate:    2015年12月15日:
	 * @arithMetic:
	 * @exception:
	 * @param x 此参数没有用,只是为了和底层方法区分开
	 */
	public Map<Long,Double> updatePickTicket(WmsTask task,Double unPickQty,int x){
		WmsPickTicketDetail detail = commonDao.load(WmsPickTicketDetail.class, task.getRelatedObjId());
		detail.pickedQty(unPickQty);
		//判断是否回写工单明细的交接数量，自管仓拣货到T1库位的需要回写，VMI拣货到FHC的不用写，因为后面VMI交接到自管仓的时候会写
		Boolean isHand = Boolean.FALSE;
		if(task.getToLocation().getType().equals(WmsLocationType.HANDOVER)) {
			isHand = Boolean.TRUE;
		}
		//拣货单回写后  回写工单
		Map<Long,Double> pros = tclCustomPicked(detail,unPickQty,isHand);
		
		//多拣时更新拣货单和明细分配数量
		Double overPickQty = DoubleUtils.sub(task.getPickedQty(),task.getPlanQty(), task.getItem().getBuPrecision());
		if(overPickQty.doubleValue()>0){
			detail.allocate(overPickQty);
		}
		commonDao.store(detail);
		
		//回写波次拣货单状态
		/*if(detail.getPickTicket().getWaveDoc() != null){
			updateWaveDoc(detail.getPickTicket().getWaveDoc().getId());
		}*/
		if(detail.getPickTicket().getWaveDoc() != null){
			WmsWaveDoc waveDoc = this.commonDao.load(WmsWaveDoc.class,detail.getPickTicket().getWaveDoc().getId());
			//调用波次作业流程
			workflowManager.doWorkflow(waveDoc, "wmsWaveDocProcess.working");
			//波次单作业完成时,更新作业完成时间
			if(WmsWaveDocStatus.FINISHED.equals(waveDoc.getStatus())){
				waveDoc.setFinishDate(new Date());
				this.commonDao.store(waveDoc);
			}
		}
		workflowManager.doWorkflow(detail.getPickTicket(), "wmsPickTicketProcess.working");
		
//		WmsBillType billType = commonDao.load(WmsBillType.class, detail.getPickTicket().getBillType().getId());
//		try {
//			Map<String, Object> value = wmsRuleManager.getRuleTableDetail(
//					BaseOrganizationHolder.getThornBaseOrganization().getId(),
//					"R101_生成BOL配置规则表", billType.getName());
//			if (value != null && "否".equals((String) value.get("是否生成BOL"))) {
////				return;
//				//提前生成的BOL更改拣货数量
//				wmsCustomerManager.ptConfirmCustomerMethod(detail,unPickQty,task);
//			}else{
//				//调用发货单生成
//		wmsBolManager.createBolByPickTicketTwo(detail,unPickQty,task);
        //根据规则表判断是否包装和生成bol明细
    	wmsBolManager.createBolByIsPacking(detail,unPickQty,task);
//			}
//		} catch (EcadBaseException e) {
//			throw new BusinessException(e.getMessage());
//		}
    	return pros;
	}
    
    /**
     * 重写作业确认方法 用于拣货信息回传SAP
     */
    public void singleWorkConfirm(WmsTask task,WmsTask newTask, Double pickedQty, Long workerId) {
		//记录作业单开始作业时间
		WmsWorkDoc workDoc = commonDao.load(WmsWorkDoc.class, task.getWorkDoc().getId());
		if(workDoc.getStartTime()==null){
			workDoc.setStartTime(new Date());
		}
		//记录任务开始作业时间
		if(task.getStartTime() == null){
			task.setStartTime(new Date());
		}
		//记录新任务开始作业时间
		if(newTask.getStartTime() == null){
			newTask.setStartTime(new Date());
		}
		WmsInventory inventory = null;
		WmsWorker worker = workerId == null ? null : commonDao.load(
				WmsWorker.class, workerId);
		task.setWorker(worker);
		/*拣货作业确认*/
		if(task.getType().equals(WmsTaskType.PICKING)){
			Double maxPickedQty = DoubleUtils.sub(task.getPlanQty(),task.getPickedQty(),task.getItem().getBuPrecision());
			if(maxPickedQty < pickedQty){
				WmsPickTicketDetail ptDetail = this.commonDao.load(WmsPickTicketDetail.class, task.getRelatedObjId());
//				Map<String, Object> value = wmsRuleManager.getRuleTableDetail("R101_多拣比例配置规则表", company.getName());
//				if (value != null) {
//					String overPickRateStr = (String) value.get("多拣比例");
//					try{
//					Double overPickRate = Double.valueOf(overPickRateStr);
//					//向上取整
//					pickTicketDetail.setOverpickRate(Math.ceil(overPickRate));
//					}catch(NumberFormatException e){
//						throw new BusinessException("the.overPickRate.is.not.numeric.type");	
//					}
//			}else{
//					pickTicketDetail.setOverpickRate(0D);
//			}
				/**下面的超拣逻辑有变动,tcl没有加超拣数量限制,用户拣多少就多少*/
//				if(ptDetail.getOverpickRate().intValue()<=0){
//					throw new BusinessException("pickedQty.greater.than.maxPickedQty");
//				}else if(ptDetail.getOverpickRate().intValue()>=1){
					//1即为允许多拣100%，2为允许多拣200%，以此类推
					//当前可拣数量=期待数量*(多拣比例+1)-已拣数量-其他已分配待拣数量(已分配数量-已拣数量-当前task未拣数量)
					//其他已分配数量
//					Double overExpectedQty= DoubleUtils.formatByPrecision(ptDetail.getExpectedQty()*(ptDetail.getOverpickRate()+1),task.getItem().getBuPrecision());
//					Double anotherAllocatedQty= DoubleUtils.formatByPrecision(ptDetail.getAllocatedQty()-ptDetail.getPickedQty()-maxPickedQty,task.getItem().getBuPrecision());
//					Double overPickedQty = DoubleUtils.formatByPrecision(overExpectedQty-ptDetail.getPickedQty()-anotherAllocatedQty, task.getItem().getBuPrecision());
//					if(pickedQty>overPickedQty){
//						throw new BusinessException("pickedQty.greater.than.overExpectedQty");
//					}
//				}
			}		
			task.setOperationType(WmsInventoryLogType.MOVE);
			inventoryManager.out(task,pickedQty);
			task.setInvRelatedBillCode(task.getRelatedBillCode());//在备货库位上记录相关单号
			task.setInvRelatedBillType(WmsBillOfType.PT);//在备货库位上记录相关单据类型
			WmsPickTicketDetail pickTicketDetail = commonDao.load(WmsPickTicketDetail.class, task.getRelatedObjId());
			WmsPickTicket pt = pickTicketDetail.getPickTicket();
			
			//如果是到T1库位  则将库存作业状态改成正常，不要用待出。且记录老单号  xuyan.xia 2017年08月02日17:53:55
			if(task.getToLocation().getType().equals(WmsLocationType.HANDOVER)) {
				task.setInvRelatedBillType(null);//在备货库位上记录相关单据类型
				String oldTaskType = task.getType(); 
				task.setType(WmsTaskType.KITTING_PICKING); //为了记录单号 临时用一下 后面还原成老type  库存需要修改批次
				WmsItemKey oldItemkey = task.getItemKey();
				LotInfo lotInfo = new LotInfo();
				BeanUtils.copyEntity(lotInfo, oldItemkey.getLotInfo());
				String poCode = "";
				if(!StringHelper.isNullOrEmpty(pickTicketDetail.getUserField1())){//有值代表是冰箱可拆包物料生成的拣配单明细
					poCode = pickTicketDetail.getUserField1();
				}else{
					poCode = pt.getRelatedBill1();
				}
				lotInfo.setExtendPropC6(pt.getUserField7());//产线
				lotInfo.setExtendPropC13(poCode); // 工单号/线体 // VMI交接单入库生成的asndetail需要
				lotInfo.setExtendPropC20(task.getWorkDoc().getCode()); //记录作业单号 用于配送单、VMI交接单,RF添加需要
				if (pt.getOrderDate() != null) { // 订单日期 如果是工单生成的 // 这个日期是工单的开始生产日期。
					lotInfo.setExtendPropC14(DateUtil.format(pt.getOrderDate(), "yyyyMMdd")); // 线体生产日期 // VMI交接单入库生成的asndetail需要
				}
				lotInfo.setExtendPropC15(pickTicketDetail.getId() + ""); // pickticketdetail_id // VMI交接单入库生成的asndetail需要
				
				WmsItemManager wmsItemManager = (WmsItemManager)applicationContext.getBean("wmsItemManager");
				
				WmsItemKey tmpik = new WmsItemKey();
				tmpik.setItem(oldItemkey.getItem());
				tmpik.setLotInfo(lotInfo);
				tmpik.setLotDisplay(lotInfo.stringValue());
				tmpik.setHashCode(tmpik.genHashCode());
				WmsItemKey newItemKey = wmsItemManager.getItemKey(tmpik);
				
//				WmsItemKey newItemKey = wmsItemManager.getItemKey(task.getWarehouse(), 
//						oldItemkey.getLotInfo().getAsnCustomerBill(), oldItemkey.getItem(), lotInfo);
				task.setItemKey(newItemKey);
				
				inventory = inventoryManager.in(task,pickedQty);
				task.setType(oldTaskType);
				task.setItemKey(oldItemkey);
				task.setInvRelatedBillType(WmsBillOfType.PT);//在备货库位上记录相关单据类型
				inventory.setRelatedBillType(WmsBillOfType.PT);//库存上记录
			}
			else {
				inventory = inventoryManager.in(task,pickedQty);
			}
			
			task.pickedQty(pickedQty);
			task.putawayQty(pickedQty);
			
			/**回写数量以及判断是否创建bol明细*/
			Map<Long,Double> pros = updatePickTicket(task,pickedQty,0);
			Set<Long> keys = pros.keySet();
			
			if(!StringHelper.isNullOrEmpty(pt.getUserField3()) 
					&& pt.getUserField3().equals(WmsPickticketGenType.SCLLD)
//					&& WmsItemUnPackingAtt.WAREHOUSE_UNPACKING.equals(pickTicketDetail.getItem().getUserFieldV3())
					&& inventory.getLocation().getCode().equals(WmsLocationCode.T1)
					&& StringHelper.in(pt.getUserField1(), new String[]{WmsSapFactoryCodeEnum.XYJ_NX,WmsSapFactoryCodeEnum.XYJ_WX})){
				
				Long lastId = 0L;
				Double totalQty = inventory.getQty();//总数量
				
				//生产领料单生成的拣货单,如果含有不可拆包物料的拣货明细,则查到此明细关联的工单,根据工单数量拆分库存
				if(WmsItemUnPackingAtt.WAREHOUSE_UNPACKING.equals(pickTicketDetail.getItem().getUserFieldV3())){
					for(Long id : keys){
						ProductionOrderDetail po = commonDao.load(ProductionOrderDetail.class, id);
						if(totalQty <= 0){
							break;
						}
						Double qty = pros.get(id);
						WmsInventory newInventory = EntityFactory.getEntity(WmsInventory.class);
						BeanUtils.copyEntity(newInventory, inventory);
						newInventory.setId(null);
						newInventory.setOperationStatus("NORMAL");
						//分配和取消分配时,新建库存需将原库存的托盘序号带到新库存上
						newInventory.setRelatedBillCode(po.getProductionOrder().getCode());//工单号
						newInventory.setQty(qty);//数量
						newInventory.setPackQty(DoubleUtils.div(qty, task.getPackageUnit().getConvertFigure()));//包装数量
						commonDao.store(newInventory);
						totalQty -= qty;
						lastId = newInventory.getId();//库存ID
					}
					if(totalQty > 0){//如果分配数量还有,则分到最后一个库存上
						WmsInventory inv = commonDao.load(WmsInventory.class, lastId);
						inv.addQty(totalQty);
						commonDao.store(inv);
						totalQty = 0D;
					}
					if(totalQty <= 0){
						inventory.setTask(null);
						inventory.setPallet(null);
						inventory.setCarton(null);
						inventory.setPalletSeq(0);
						inventory.setQty(0d);
						inventory.setPackQty(0D);
					}
					commonDao.store(inventory);
					
					
				}else{
					for(Long id : keys){//工单生成T1库位拣货单分配库存的时候，库存需要记录工单号
						ProductionOrderDetail po = commonDao.load(ProductionOrderDetail.class, id);
						inventory.setRelatedBillCode(po.getProductionOrder().getCode());//工单号
					}	
					commonDao.store(inventory);
				}
			}
			this.workflowManager.doWorkflow(task.getWorkDoc(), "wmsWorkDocProcess.confirmDecision");
		}/*上架作业确认*/
		if(task.getType().equals(WmsTaskType.PUTAWAY)){
			Double maxPickedQty = DoubleUtils.sub(task.getPlanQty(),task.getPickedQty(),task.getItem().getBuPrecision());
			if(maxPickedQty < pickedQty){
				throw new BusinessException("putawayQty.greater.than.unPutawayQty");
			}

			task.setOperationType(WmsInventoryLogType.MOVE);//记录日志类型
			newTask.setOperationType(WmsInventoryLogType.MOVE);//记录日志类型
			task.setInvRelatedBillCode(task.getRelatedBillCode());//在备货库位上记录相关单号
			inventoryManager.out(task,pickedQty);
			if(newTask!=null && newTask.getToLocation()!=task.getToLocation()){//上架作业确认时，修改目标库位，先扣减移出库位库存，再取消原task对应的库存
				inventoryManager.cancelPlanIn(task, pickedQty);//取消原task对应的库存
				inventory = inventoryManager.modifyToLocIn(newTask,pickedQty);//增加新目标库位库存
				//完成上架的任务状态更新为完成，移入库位更新为实际的移入库位
				newTask.pickedQty(pickedQty);
				newTask.putawayQty(pickedQty);
				this.commonDao.store(newTask);
			}else{
			    inventory= inventoryManager.in(task,pickedQty);
				task.pickedQty(pickedQty);
				task.putawayQty(pickedQty);
			}	
			updateWmsASN(task,pickedQty);
			//上架作业确认时，更新库存托盘顺序号
			inventoryManager.refreshInvPalletSeq(inventory);
			
			wmsCustomerManager.storeCustomerInf(newTask.getToLocation());
			
	    }/*移位作业确认*/
		else if(task.getType().equals(WmsTaskType.MOVE)){
			Double maxPickedQty = DoubleUtils.sub(task.getPlanQty(),task.getPickedQty(),task.getItem().getBuPrecision());
			if(maxPickedQty < pickedQty){
				throw new BusinessException("moveQty.greater.than.unMoveQty");
			}

			task.setOperationType(WmsInventoryLogType.MOVE);//记录日志类型
			newTask.setOperationType(WmsInventoryLogType.MOVE);//记录日志类型
			//待出的库存
			WmsInventory readyOutInv = inventoryManager.getWmsInventoryByTask(task, WmsInventoryOperationStatus.READY_OUT);
			if(readyOutInv == null){
				throw new BusinessException("未找到待出的库存，不能移位");
			}
			
			inventoryManager.out(task,pickedQty);
			
			Boolean isSendSAP = Boolean.FALSE; 
			String newInvStatus = task.getInventoryStatus();
			
			if(newTask!=null && newTask.getToLocation()!=task.getToLocation()){//上架作业确认时，修改目标库位，先扣减移出库位库存，再取消原task对应的库存
				inventoryManager.cancelPlanIn(task, pickedQty);//取消原task对应的库存
				if(newTask.getToLocation() != null && WmsLocationCode.HLC.equals(newTask.getToLocation().getCode()) && !WmsLocationCode.HLC.equals(newTask.getFromLocation().getCode())){
					newInvStatus = "不良品";//好料仓移到坏料仓
		        	isSendSAP = Boolean.TRUE;
		        }else if(newTask.getToLocation() != null && !WmsLocationCode.HLC.equals(newTask.getToLocation().getCode()) && WmsLocationCode.HLC.equals(newTask.getFromLocation().getCode())){
		        	newInvStatus = "合格" ;
		        	isSendSAP = Boolean.TRUE;
		        }
				newTask.setInventoryStatus(newInvStatus);//如果移到不良品仓，先改下状态记录库存日志用
				inventory = inventoryManager.modifyToLocIn(newTask,pickedQty);//增加新目标库位库存
				newTask.setInventoryStatus(readyOutInv.getStatus());//记录好了后，task状态再改回来
				
				//完成上架的任务状态更新为完成，移入库位更新为实际的移入库位
				newTask.pickedQty(pickedQty);
				newTask.putawayQty(pickedQty);
				this.commonDao.store(newTask);
			}else{
				if(task.getToLocation() != null && WmsLocationCode.HLC.equals(task.getToLocation().getCode()) && !WmsLocationCode.HLC.equals(task.getFromLocation().getCode())){
					newInvStatus = "不良品";//好料仓移到坏料仓
		        	isSendSAP = Boolean.TRUE;
		        }else if(task.getToLocation() != null && !WmsLocationCode.HLC.equals(task.getToLocation().getCode()) && WmsLocationCode.HLC.equals(task.getFromLocation().getCode())){
		        	newInvStatus = "合格" ;
		        	isSendSAP = Boolean.TRUE;
		        }
				task.setInventoryStatus(newInvStatus);//记录不良品库存日志用
			    inventory= inventoryManager.in(task,pickedQty);
			    task.setInventoryStatus(readyOutInv.getStatus());//日志记录好后，再改回来
				task.pickedQty(pickedQty);
				task.putawayQty(pickedQty);
			}
			if(isSendSAP){
	        	TclMessageManager tclMessageManager = (TclMessageManager)applicationContext.getBean("tclMessageManager");
	        	tclMessageManager.sendChangeStatusInfo(readyOutInv, pickedQty, newInvStatus);
	        }
			//移位作业确认时，更新库存托盘顺序号
			inventoryManager.refreshInvPalletSeq(inventory);
	    }/*补货作业确认*/ 
	    else if (task.getType().equals(WmsTaskType.REPLENISHMENT)){

			Double maxPickedQty = DoubleUtils.sub(task.getPlanQty(),task.getPickedQty(),task.getItem().getBuPrecision());
			if(maxPickedQty < pickedQty){
				throw new BusinessException("putawayQty.greater.than.unPutawayQty");
			}

			task.setOperationType(WmsInventoryLogType.MOVE);//记录日志类型
			newTask.setOperationType(WmsInventoryLogType.MOVE);//记录日志类型
			task.setInvRelatedBillCode(task.getRelatedBillCode());//在备货库位上记录相关单号
			inventoryManager.out(task,pickedQty);
			if(newTask!=null && newTask.getToLocation()!=task.getToLocation()){//上架作业确认时，修改目标库位，先扣减移出库位库存，再取消原task对应的库存
				inventoryManager.cancelPlanIn(task, pickedQty);//取消原task对应的库存
				inventory = inventoryManager.modifyToLocIn(newTask,pickedQty);//增加新目标库位库存
				//完成上架的任务状态更新为完成，移入库位更新为实际的移入库位
				newTask.pickedQty(pickedQty);
				newTask.putawayQty(pickedQty);
				this.commonDao.store(newTask);
			}else{
			    inventory= inventoryManager.in(task,pickedQty);
//			    inventoryManager.in(inventoryDto);
				task.pickedQty(pickedQty);
				task.putawayQty(pickedQty);
			}
			//上架作业确认时，更新库存托盘顺序号
			inventoryManager.refreshInvPalletSeq(inventory);
			
			wmsCustomerManager.storeCustomerInf(newTask.getToLocation());
			if (!WmsWorkDocStatus.FINISH.equals(task.getWorkDoc().getStatus())) {
				this.workflowManager.doWorkflow(task.getWorkDoc(), "wmsReplenishWorkDocProcess.doConfirm");
			}
	    }
		/*加工拣货作业确认*/
	    else if(task.getType().equals(WmsTaskType.KITTING_PICKING)){
			Double maxPickedQty = DoubleUtils.sub(task.getPlanQty(),task.getPickedQty(),task.getItem().getBuPrecision());
			if(maxPickedQty < pickedQty){
				throw new BusinessException("pickedQty.greater.than.maxPickedQty");
			}

			task.setOperationType(WmsInventoryLogType.MOVE);
			inventoryManager.out(task,pickedQty);
			task.setInvRelatedBillCode(task.getRelatedBillCode());//在加工库位上记录相关(加工单)单号
			inventory = inventoryManager.in(task,pickedQty);
			
			task.pickedQty(pickedQty);
			task.putawayQty(pickedQty);
			//更新加工单已拣货套数
			updateWmsKittingDoc(task);
			
			this.workflowManager.doWorkflow(task.getWorkDoc(), "wmsKittingWorkDocProcess.confirmDecision");
		}
		//记录任务任务完成时间
		if(WmsTaskStatus.FINISH.equals(task.getStatus())){
			task.setEndTime(new Date());
		}
		//记录新产生任务任务完成时间
		if(WmsTaskStatus.FINISH.equals(newTask.getStatus())){
			newTask.setEndTime(new Date());
		}
		//记录任务作业单完成时间
	  	if(WmsWorkDocStatus.FINISH.equals(workDoc.getStatus())){
	  		workDoc.setEndTime(new Date());
		}
		this.commonDao.store(workDoc);
		commonDao.store(task);
		commonDao.store(newTask);
		//客户化方法处理作业确认完后续处理(上传接口等)
		wmsCustomerManager.customerMethod(inventory, newTask);
		
		//如果task有groupTask则判断是否要修改TaskGroup的状态
		if(null!=task.getTaskGroup()){
			updateTaskGroupStatus(newTask.getTaskGroup().getId());
		}
		if(null!=newTask.getTaskGroup()){
			updateTaskGroupStatus(newTask.getTaskGroup().getId());
		}
	}

    @Override
	public void addDetail(ProductionOrderDetail orderDetail,List tableValues,Long workDocId) {
    	double quantity = Double.parseDouble(tableValues.get(0).toString());
    	dealProductOrderDetail(orderDetail, quantity, workDocId);
    }
    
	@Override
	public void dealProductOrderDetail(ProductionOrderDetail orderDetail,Double shippingQty,Long workDocId) {
		
		if(shippingQty > orderDetail.getPlanQuantityBu() - orderDetail.getAllocatedQuantityBu()){
			throw new BusinessException("分配数量不能大于计划数量减去已分配数量！！");
		}
		WmsWorkDoc workDoc = this.commonDao.load(WmsWorkDoc.class, workDocId);
		//查库存
		String hql = "FROM WmsInventory inventory WHERE (inventory.location.type=:type or inventory.location.type=:type2) "
				+ " AND inventory.location.countLock =:countLock "
				+ " AND inventory.status =:status "
				+ " AND inventory.operationStatus =:operationStatus "
				+ " AND inventory.item.id =:itemId "
				+ " AND inventory.itemKey.lotInfo.extendPropC10 =:factoryCode"
				+ " AND inventory.warehouse.id=:warehouseId "
				+ " AND inventory.qty > 0 order by inventory.itemKey.lotInfo.storageDate asc";
		
		@SuppressWarnings("unchecked")
		List<WmsInventory> invs = commonDao.findByQuery(hql,
				new String[]{"type","type2","countLock","status","operationStatus","itemId",
					"factoryCode","warehouseId"}, 
				new Object[]{WmsLocationType.STORAGE,WmsLocationType.STORAGE,Boolean.FALSE,"合格",
					WmsInventoryOperationStatus.NORMAL,orderDetail.getItem().getId(),orderDetail.getProductionOrder().getFactory().getCode(),workDoc.getWarehouse().getId()});
		if(invs.size() <= 0){
			throw new BusinessException("没有查到对应库存,请检查!!");
		}
		
		/**回写工单已分配数量*/
		orderDetail.addAllocatedQuantityBu(shippingQty);
		if(orderDetail.getAllocatedQuantityBu() >= orderDetail.getPlanQuantityBu()){
			orderDetail.setStatus(AssignmentStatus.ALLOCATED);
		}else{
			orderDetail.setStatus(AssignmentStatus.UNALLOCATED);
		}
		commonDao.store(orderDetail);
		
		Double qty = shippingQty; //剩余需要分配的数量
		for(WmsInventory inventory :invs){
			Double bcQty = 0D;
			if(qty<=0D) {
				break;
			}
			if(inventory.getQty()>=qty) {
				bcQty = qty;
				qty=0d;
			}
			else {
				bcQty = inventory.getQty();
				qty = qty- inventory.getQty();
			}
			 
			workDoc.refreshQuantity(bcQty,inventory.getItem().getBuPrecision());
			
			WmsTask task = EntityFactory.getEntity(WmsTask.class);
			task.setWarehouse(workDoc.getWarehouse());
			
			task.setWorkDoc(workDoc);
			
			task.setCompany(inventory.getCompany());
			task.setStatus(WmsTaskStatus.READY_ALLOCATE);
			task.setType(WmsTaskType.MOVE);
			task.setRelatedObjBillType(WmsTaskRelatedObjType.PT);
			task.setRelatedObjId(workDoc.getId());
			task.setRelatedBillCode(workDoc.getCode());
			task.setWorkArea(inventory.getLocation().getWorkArea());
			task.setFromLocation(inventory.getLocation());
			task.setPallet(inventory.getPallet());
			task.setCarton(inventory.getCarton());
			task.setItem(inventory.getItem());
			task.setItemKey(inventory.getItemKey());
			task.setInventoryStatus(inventory.getStatus());
			task.setPackageUnit(inventory.getPackageUnit());
			task.setPlanQty(bcQty);
			task.setProductionDetailId(orderDetail.getId());//工单明细ID
			task.setPlanPackQty(WmsPackageUnitUtils.getPackQty(task.getPackageUnit(), bcQty, task.getItem().getBuPrecision()));
			commonDao.store(task);
			
			//库存拆分
			Double oldPackQty=inventory.getPackQty();
			
			//对库存进行操作
			inventoryManager.planOut(task,bcQty,inventory.getId());
			inventory.setQty(oldPackQty-bcQty);
			inventory.setPackQty(WmsPackageUnitUtils.getPackQty(task.getPackageUnit(), inventory.getPackQty(), task.getItem().getBuPrecision()));
		}
		if(qty>0d) {
			throw new BusinessException("生产订单明细数量没有完全分配,库存不足,请检查!");
		}
		workflowManager.doWorkflow(workDoc, "wmsMoveDocProcess.allocate");
	}

	@Override
	public void shipJitOrder(WmsWorkDoc workDoc) {
		List<WmsTask> tasks = commonDao.findByQuery("FROM WmsTask task WHERE task.workDoc.id=:workDocId", 
                "workDocId", workDoc.getId());
        
        for (WmsTask task : tasks) {
            this.detailShipJITOrderTask(task);
        }
	}
	private void detailShipJITOrderTask(WmsTask task) {
        WmsWorkDoc workDoc = task.getWorkDoc();
        String relatedBill = workDoc.getCode();
        
        if (workDoc.getPickedQty()==0) {
            workDoc.setStartTime(new Date());
        } 
        
        task.setEndTime(new Date());
        task.setStatus(WmsTaskStatus.FINISH);
        this.commonDao.store(task);
        
 
        List<WmsInventory> invs = commonDao.findByQuery("FROM WmsInventory inv WHERE inv.task.id=:taskId", 
                "taskId", task.getId());
        
        if(invs.isEmpty()) {
        	throw new BusinessException("未找到作业任务对应库存!");
        }
        for(WmsInventory inventory : invs) {
        	 //回写出库数量
        	updateProductDetailQty(task.getProductionDetailId(), task.getPickedQty(),inventory);
        	tclMessageManager.shipJITDownLine(inventory, task, task.getPickedQty());//JIT下线出库回传SAP
            //生成发货库存日志
            wmsInventoryLogManager.addInventoryLog(WmsInventoryLogType.SHIPPING, 
                    relatedBill, null, task.getFromLocation(), 
                    task.getCompany(), inventory.getItemKey(), 
                    task.getPickedQty(), -1*task.getPickedQty(), inventory.getStatus(), "", "JIT下线出库");
            
            commonDao.delete(inventory);
            workDoc.pickedQty(task.getPickedQty(),0D, task.getItem().getBuPrecision());
            
            if (workDoc.getPickedQty().equals(workDoc.getAllocateQty())) {
                workDoc.setEndTime(new Date());
            }
        }
        
       
    }
	/**删除JIT出库单明细*/
	public void deleteMoveDocDetail(WmsTask task) {
		if(null != task.getProductionDetailId() && !"".equals(task.getProductionDetailId())){
			/**JIT出库单 回写工单数量*/
			Double planQty = task.getPlanQty();
			ProductionOrderDetail detail = commonDao.load(ProductionOrderDetail.class, task.getProductionDetailId());
			if(detail == null){
				throw new BusinessException("根据Task字段productionDetailId未找到对应工单明细,请检查字段值");
			}
			detail.setAllocatedQuantityBu(detail.getAllocatedQuantityBu() - planQty);
			commonDao.store(detail);
		}
		super.deleteMoveDocDetail(task);
	}
	/**发运回写工单数量*/
	private void updateProductDetailQty(Long pickTicketDetailId, Double pickedQty,WmsInventory inventory) {
    	
        ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, pickTicketDetailId);
        Double shipQty = pod.getShippedQuantityBu() == null ? 0D : pod.getShippedQuantityBu();//已发运数量
        Double unPickedQty = pod.getPlanQuantityBu() - shipQty;//未发运数量

        if (pickedQty <= 0) {
            return;
        }
        
        Double truckQty =0d;
        if (unPickedQty>pickedQty) {
            System.out.println("作业单作业确认修改工单明细发运数量("+pickTicketDetailId+")"+ pickedQty);
            pod.addShippedQuantityBu(pickedQty);//回写已发运数量
            pod.addPickedQuantityBu(pickedQty);//回写已拣货数量
            truckQty=pickedQty;
            pickedQty = 0D;
           
        } else {
            System.out.println("作业单作业确认修改工单明细发运数量("+pickTicketDetailId+")"+ unPickedQty);
            pod.addShippedQuantityBu(unPickedQty);
            pod.addPickedQuantityBu(pickedQty);//回写已拣货数量
            truckQty=unPickedQty;
            pickedQty = DoubleUtils.sub(pickedQty, unPickedQty);
        }
        if(CommonHelper.dealDoubleError(pickedQty)>0d){
        	throw new BusinessException("PTDID:"+pickTicketDetailId+"发运数量剩余"+CommonHelper.dealDoubleError(pickedQty)+"未回写到工单");
        }
        if(pod.getPickedQuantityBu() >= pod.getPlanQuantityBu()){
        	pod.setPickSataus(PickingStatus.PICKED);
        }else{
        	pod.setPickSataus(PickingStatus.UNPICKED);
        }
        if(pod.getShippedQuantityBu() >= pod.getPlanQuantityBu()){
        	pod.setShipStatus(ShippingStatus.SHIPPED);
        }else{
        	pod.setShipStatus(ShippingStatus.UNSHIPPED);
        }
        commonDao.store(pod);
        
        //增加跟踪信息。
        WmsShippingLotTruck t = EntityFactory.getEntity(WmsShippingLotTruck.class);
        t.setBillType(WmsShippingLotTruckBillType.SCLLD);
        t.setCompany(inventory.getCompany());
        t.setInventoryStatus(inventory.getStatus());
        t.setItem(inventory.getItem());
        t.setLineNo(pod.getLineNo()+"");
        t.setLocation(inventory.getLocation());
        t.setLotInfo(inventory.getItemKey().getLotInfo());
        t.setPackageUnit(inventory.getPackageUnit());
        t.setQty(truckQty);
        t.setRealteId(pod.getProductionOrder().getId());
        t.setRelateCode(pod.getProductionOrder().getCode());
        t.setSubRelateId(pod.getId());
        t.setWarehouse(inventory.getWarehouse());
        commonDao.store(t);
        
    }
	/**配送单添加明细方法重写*/
	public void addToMoveDocDetail(Long workDocId,WmsInventory inventory, List tableValues) {
//		double quantity = Double.parseDouble(tableValues.get(0).toString());
//		if(null != inventory.getItem().getUserFieldV3() && 
//				inventory.getItem().getUserFieldV3().equals(WmsItemUnPackingAtt.WAREHOUSE_UNPACKING)){
//			if(quantity % inventory.getItem().getUserFieldD1() != 0 || quantity - inventory.getQty() != 0){
//				throw new BusinessException("不可拆包货品的移位包装数量必须是整包包装数量或者等于库存数量");
//			}
//		}
		WmsWorkDoc workDoc = this.commonDao.load(WmsWorkDoc.class, workDocId);
		if(!WmsWorkDocStatus.READY_ALLOCATE.equals(workDoc.getStatus())){
			throw new BusinessException("单据状态不对，不能添加明细，请查看单据的状态");
		}
		super.addToMoveDocDetail(workDocId, inventory, tableValues);
	}
	
	
	/**修改保管员*/
	 public void modifyKeeper(WmsWorkDoc workDoc){
		 commonDao.store(workDoc);
	 }
	 
	 
	 /**
	  * 删除JIT
	  */
	 public void deleteJIT(WmsWorkDoc workDoc){
		 String Hql = "FROM WmsTask task WHERE task.workDoc.id =:id";
		 List<WmsTask> list = commonDao.findByQuery(Hql,"id",workDoc.getId());
		 if(list.size() > 0){
			 throw new BusinessException("JIT出库单已存在明细，不允许删除！！！");
		 }
		 commonDao.delete(workDoc);
	 }
}