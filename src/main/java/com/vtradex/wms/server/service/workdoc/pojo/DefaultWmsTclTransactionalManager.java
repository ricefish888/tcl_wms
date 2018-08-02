package com.vtradex.wms.server.service.workdoc.pojo;

import java.util.List;
import java.util.Map;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.wms.server.enumeration.WarehouseEnumeration;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.item.WmsBillType;
import com.vtradex.wms.server.model.entity.item.WmsItemUnPackingAtt;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.pickticket.WmsWaveDoc;
import com.vtradex.wms.server.model.entity.production.AssignmentStatus;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetailPtDetail;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderDetail;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;
import com.vtradex.wms.server.model.enums.WmsBillOfType;
import com.vtradex.wms.server.model.enums.WmsLocationCode;
import com.vtradex.wms.server.model.enums.WmsPickTicketStatus;
import com.vtradex.wms.server.model.enums.WmsPickticketBillTypeCode;
import com.vtradex.wms.server.model.enums.WmsPickticketGenType;
import com.vtradex.wms.server.model.enums.WmsTaskRelatedObjType;
import com.vtradex.wms.server.model.enums.WmsTaskStatus;
import com.vtradex.wms.server.model.enums.WmsTaskType;
import com.vtradex.wms.server.model.enums.WmsWaveDocStatus;
import com.vtradex.wms.server.service.bol.WmsBolManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.message.WmsCustomerManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.task.WmsTaskManager;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.webservice.utils.CommonHelper;

/**
 * 
 * tcl定制化拣货单业务 
 * 分配与取消分配修改回写相应单据
 * 
 * @author <a href="zhen.lei@vtradex.com">Yogurt_lei</a>
 * 
 * @date 2017年7月18日 下午4:33:23
 */
public class DefaultWmsTclTransactionalManager extends DefaultWmsTransactionalManager {

    public DefaultWmsTclTransactionalManager(WorkflowManager workflowManager,
            WmsInventoryManager inventoryManager, WmsBolManager wmsBolManager,
            WmsTaskManager wmsTaskManager,
            WmsBussinessCodeManager wmsBussinessCodeManager,
            WmsRuleManager wmsRuleManager, WmsCustomerManager wmsCustomerManager) {
        super(workflowManager, inventoryManager, wmsBolManager, wmsTaskManager,
                wmsBussinessCodeManager, wmsRuleManager, wmsCustomerManager);
    }

    @Override
    public void doAutoAllocateResult(WmsPickTicketDetail pickTicketDetail,
            Map<Long, Double> allocateInfo,WmsLocation shipLocation) {
        pickTicketDetail = this.commonDao.load(WmsPickTicketDetail.class,
                pickTicketDetail.getId());
        WmsPickTicket pickTicket = this.commonDao.load(WmsPickTicket.class, pickTicketDetail.getPickTicket().getId());
        //分配
        Double unAllocateQty = pickTicketDetail.getUnAllocateQty();
        String splitValue = pickTicketDetail.getItem().getUserFieldV3() == null 
        						? "" : pickTicketDetail.getItem().getUserFieldV3();//拆包属性
        Long oldshipLocationId= shipLocation.getId();
        for (Long srcInventoryId : allocateInfo.keySet())
        {
            double allocateQuantityBU =DoubleUtils.format3Fraction(allocateInfo.get(srcInventoryId));
            if (allocateQuantityBU == 0)
            {
                continue;
            }

            if(unAllocateQty <= 0){
                break;
            }
            
            Long inventoryId = srcInventoryId;
            WmsInventory inventory = commonDao.load(WmsInventory.class, inventoryId);
            if(!pickTicket.getUserField1().equals(inventory.getItemKey().getLotInfo().getExtendPropC10())){
            	throw new BusinessException("必须分配工厂:"+pickTicket.getUserField1()+"下的库存"+pickTicket.getId()+":"+inventory.getId());
            }
            if(inventory.getLocation().getCode().equals(WmsLocationCode.XB)){
            	WmsBillType billType = commonDao.load(WmsBillType.class, pickTicket.getBillType().getId());
            	if(!WmsPickticketBillTypeCode.SCLLD.equals(billType.getCode())){
            		throw new BusinessException("不是生产领料单，不允许分配XBC库存");
            	}
            	/**线边库位的库存出库先放入XBS库位*/
            	shipLocation = (WmsLocation) commonDao.findByQueryUniqueResult("from "
            			+ "WmsLocation w where w.code=:code AND w.warehouse.id =:warehouseId",
            			new String[]{"code","warehouseId"},
            			new Object[]{WmsLocationCode.XBS,inventory.getWarehouse().getId()});
            	if(shipLocation == null){
            		throw new BusinessException("系统未维护"+WmsLocationCode.XBS+"库位");
            	}
            }
            else {
            	shipLocation = commonDao.load(WmsLocation.class, oldshipLocationId);
            	
            }
            Double qty = allocateQuantityBU;
            
            Double allocateQty = unAllocateQty;
            Double allocatePackQty = 0D;
            Double allocatePackWeight = 0D;
            Double allocatePackVolume = 0D;
            
            if(unAllocateQty > qty){
            	allocateQty = qty;
            	unAllocateQty = DoubleUtils.sub(unAllocateQty, allocateQty);
            	qty = 0D;
            }else if(unAllocateQty <= qty && !splitValue.equals(WmsItemUnPackingAtt.WAREHOUSE_UNPACKING)){
            	allocateQty = unAllocateQty;
            	unAllocateQty = 0D;
            	qty =  DoubleUtils.sub(qty, unAllocateQty);
            }else if(unAllocateQty <= qty && splitValue.equals(WmsItemUnPackingAtt.WAREHOUSE_UNPACKING)){//不可拆包
            	allocateQty = qty;
            	unAllocateQty = 0D;
            	qty =  DoubleUtils.sub(qty, unAllocateQty);
            }
            
            
            //备货库位取值使用客户化方法 
            WmsLocation newShipLocation = wmsCustomerManager.setTaskToLocation(shipLocation, pickTicketDetail, inventory);
            Double allocatePointQty = allocateQty % inventory.getPackageUnit().getConvertFigure();
            Double allocateTempQty  = allocateQty;
            //当分配整箱库存,存在小数位时,将task拆分
            if(allocatePointQty>0){
                //先建立小数位对应的task
                Double allocatePackPointQty = DoubleUtils.div(allocatePointQty,inventory.getPackageUnit().getConvertFigure(),inventory.getItem().getBuPrecision());
                Double allocatePackPointWeight = DoubleUtils.mul(allocatePointQty, inventory.getPackageUnit().getWeight(),inventory.getItem().getBuPrecision());
                Double allocatePackPointVolume =  DoubleUtils.mul(allocatePointQty, inventory.getPackageUnit().getVolume(),inventory.getItem().getBuPrecision());
    
                WmsTask task = new WmsTask();
                task.setWarehouse(inventory.getWarehouse());
                task.setCompany(inventory.getCompany());
                task.setStatus(WmsTaskStatus.ALLOCATED);
                task.setType(WmsTaskType.PICKING);
                task.setRelatedObjBillType(WmsTaskRelatedObjType.PT);
                task.setRelatedObjId(pickTicketDetail.getId());
                task.setRelatedBillCode(pickTicket.getCode());
                task.setWorkArea(inventory.getLocation().getWorkArea());
                task.setFromLocation(inventory.getLocation());
                task.setToLocation(newShipLocation);
                task.setPallet(inventory.getPallet());
                task.setCarton(inventory.getCarton());
                task.setItem(inventory.getItem());
                task.setItemKey(inventory.getItemKey());
                task.setInventoryStatus(inventory.getStatus());
                task.setPackageUnit(inventory.getPackageUnit());
                task.setPlanPackQty(allocatePackPointQty);
                task.setPlanQty(allocatePointQty);
                task.setPlanWeight(allocatePackPointWeight);
                task.setPlanVolume(allocatePackPointVolume);
                commonDao.store(task);
                inventoryManager.planOut(task,inventoryId);
                //planIn时，增加拣货单号和类型
                task.setInvRelatedBillCode(pickTicket.getCode());
                task.setInvRelatedBillType(WmsBillOfType.PT);
                task.setLockStatus(inventory.getLockStatus());
                inventoryManager.planIn(task);
                
                //计算小数位之外的剩余需分配数量
                allocateTempQty = DoubleUtils.sub(allocateQty, allocatePointQty,inventory.getItem().getBuPrecision());
                
            }
            //存在整数位的数量>0,再新建对应的task
            if(allocateTempQty.intValue()>0){   
                allocatePackQty = DoubleUtils.div(allocateTempQty,inventory.getPackageUnit().getConvertFigure(),inventory.getItem().getBuPrecision());
                allocatePackWeight = DoubleUtils.mul(allocateTempQty, inventory.getPackageUnit().getWeight(),inventory.getItem().getBuPrecision());
                allocatePackVolume =  DoubleUtils.mul(allocateTempQty, inventory.getPackageUnit().getVolume(),inventory.getItem().getBuPrecision());

                WmsTask task = new WmsTask();
                task.setWarehouse(inventory.getWarehouse());
                task.setCompany(inventory.getCompany());
                task.setStatus(WmsTaskStatus.ALLOCATED);
                task.setType(WmsTaskType.PICKING);
                task.setRelatedObjBillType(WmsTaskRelatedObjType.PT);
                task.setRelatedObjId(pickTicketDetail.getId());
                task.setRelatedBillCode(pickTicket.getCode());
                task.setWorkArea(inventory.getLocation().getWorkArea());
                task.setFromLocation(inventory.getLocation());
                task.setToLocation(newShipLocation);
                task.setPallet(inventory.getPallet());
                task.setCarton(inventory.getCarton());
                task.setItem(inventory.getItem());
                task.setItemKey(inventory.getItemKey());
                task.setInventoryStatus(inventory.getStatus());
                task.setPackageUnit(inventory.getPackageUnit());
                task.setPlanPackQty(allocatePackQty);
                task.setPlanQty(allocateTempQty);
                task.setPlanWeight(allocatePackWeight);
                task.setPlanVolume(allocatePackVolume);
                commonDao.store(task);
                inventoryManager.planOut(task,inventoryId);
                //planIn时，增加拣货单号和类型
                task.setInvRelatedBillCode(pickTicket.getCode());
                task.setInvRelatedBillType(WmsBillOfType.PT);
                task.setLockStatus(inventory.getLockStatus());
                inventoryManager.planIn(task);
            }
                pickTicketDetail.allocate(allocateQty);
                this.tclCustomAllocate(pickTicketDetail, allocateQty); // tcl分配业务 回写工单
            
        }
        
        commonDao.store(pickTicketDetail);
        workflowManager.doWorkflow(pickTicket, "wmsPickTicketProcess.allocate");
        //拣货单分配后调用客户化方法
        wmsCustomerManager.ptAllocateCustomerMethod(pickTicket);
        //普通拣货单
        if(pickTicket.getWaveDoc() == null){
            //全部分配才创建作业单
            if(pickTicket.defineStatus().equals(WmsPickTicketStatus.ALLOCATED)){
//              WmsPickTicket pickTicket = commonDao.load(WmsPickTicket.class,pickTicketDetail.getPickTicket().getId());
                //生成作业单
//              WmsPickticketManager wmsPickticketManager = (WmsPickticketManager) SpringContextHolder.getBean("wmsPickticketManager");
//              wmsPickticketManager.createWorkDoc(pickTicket);
                try {
                    //调用消息生成拣货作业单
                    this.workflowManager.sendMessage(pickTicket, "wmsPickTicketProcess.createWorkDocByMessage");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
//                  throw new BusinessException(e.getCause().getLocalizedMessage());
                    //生成作业单异常不影响拣货单分配
                    LocalizedMessage.addLocalizedMessage("作业单生成报错:"+e.getCause().getLocalizedMessage());
                }
            }
        }else{
            WmsWaveDoc waveDoc = this.commonDao.load(WmsWaveDoc.class, pickTicket.getWaveDoc().getId());
            //作业中状态的波次单不需要进行状态变化【针对短拣登记的波次单】
            if(!WmsWaveDocStatus.WORKING.equals(waveDoc.getStatus())){
                workflowManager.doWorkflow(waveDoc,"wmsWaveDocProcess.allocate");
            }
        }
     
    }
    
    /**
     * 
     * 拣货单分配时修改工单明细分配数量  生产订单id小到大+生产订单明细id小到大
     * 
     * @param pickTicketDetail 拣货单明细
     * @param allocateQty 分配数量
     *
     * @author Yogurt_lei
     *
     * @date 2017年7月18日 下午4:47:57
     */
    private void tclCustomAllocate(WmsPickTicketDetail pickTicketDetail, Double allocateQty) {
    	
    	WmsPickTicket pt = pickTicketDetail.getPickTicket();
        
    	if(pt.getBillType().getCode().equals(WmsPickticketGenType.SCLLD)) {
	        List<ProductionOrderDetailPtDetail> podptds =  commonDao.findByQuery("SELECT pt FROM ProductionOrderDetailPtDetail pt "
	                + "LEFT JOIN pt.productionOrderDetail pod "
	                + "WHERE pt.pickticketDetail.id=:ptID AND pod.planQuantityBu-pod.allocatedQuantityBu>0 " 
	                + " AND pod.planQuantityBu-pod.shippedQuantityBu>0 "
	                + "ORDER BY pod.productionOrder.id, pod.id", 
	                    "ptID", pickTicketDetail.getId());
	        Long lastid = 0L;
	        for (ProductionOrderDetailPtDetail podptd : podptds) {
	        	Long podId = podptd.getProductionOrderDetail().getId();
	        	lastid = podptd.getId();
	            ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, podId);
	            Double unAllocateQty = pod.getUnAllocateQty();
	            if (allocateQty <= 0D) {
	                break;
	            }
	            //分配的库存可能存在多条所以分配数量要累加，不能直接覆盖
	            if (unAllocateQty>allocateQty) {
	                CommonHelper.log("拣货单分配修改工单明细("+podId+")"+ allocateQty);
	                pod.addAllocatedQuantityBu(allocateQty);
//	                podptd.setLastAllocatedQty(allocateQty);
	                podptd.setLastAllocatedQty(DoubleUtils.add(podptd.getLastAllocatedQty(), allocateQty));
	                commonDao.store(podptd);
	                allocateQty = 0D;
	            } else {
	                CommonHelper.log("拣货单分配修改工单明细("+podId+")"+ unAllocateQty);
	                pod.addAllocatedQuantityBu(unAllocateQty);
//	                podptd.setLastAllocatedQty(unAllocateQty);
	                podptd.setLastAllocatedQty(DoubleUtils.add(podptd.getLastAllocatedQty(), unAllocateQty));
	                commonDao.store(podptd);
	                allocateQty = DoubleUtils.sub(allocateQty, unAllocateQty);
	            }
	            if(pod.getAllocatedQuantityBu() >= pod.getPlanQuantityBu()){
	            	pod.setStatus(AssignmentStatus.ALLOCATED);
	            }else{
	            	pod.setStatus(AssignmentStatus.UNALLOCATED);
	            }
	            commonDao.store(pod);
	        }
	         
	        
	        
	        //如果明细全部循环完毕还有剩余的分配数量没分到  说明此物料是不可拆包物料 多分了，直接剩余部分全部分配到最后一个明细上。 //TODO：test
	        allocateQty=CommonHelper.dealDoubleError(allocateQty);
	        if(allocateQty>0D) {
	        	if(lastid ==0L) { //上面没进入for 所有lastid还是0  则直接查询
	    	        List<Long> tmp_podIds =  commonDao.findByQuery("SELECT pt.id FROM ProductionOrderDetailPtDetail pt "
	    	                + "LEFT JOIN pt.productionOrderDetail pod "
	    	                + "WHERE pt.pickticketDetail.id=:ptID "
//	    	                + "AND pod.planQuantityBu-pod.allocatedQuantityBu>0 "
	    	                + "ORDER BY pod.productionOrder.id, pod.id", 
	    	                    "ptID", pickTicketDetail.getId());
	    	        for(Long id : tmp_podIds) {
	    	        	lastid = id;
	    	        }
	        	}
	        	ProductionOrderDetailPtDetail p = commonDao.load(ProductionOrderDetailPtDetail.class, lastid);
	        	 ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, p.getProductionOrderDetail().getId());
	        	 if(pod == null){
	        		 throw new BusinessException("根据拣货单明细ID:"+pickTicketDetail.getId()+"物料："+pickTicketDetail.getItem().getId()+"未找到对应的生产订单明细");
	        	 }
	        	 CommonHelper.log("拣货单分配修改工单明细("+pod.getId()+")"+ allocateQty);
	             pod.addAllocatedQuantityBu(allocateQty);
	             p.setLastAllocatedQty(DoubleUtils.add(p.getLastAllocatedQty(), allocateQty));
	             commonDao.store(p);
	             allocateQty = 0D;
	        }
	        
    	}
    	else if(pt.getBillType().getCode().equals(WmsPickticketGenType.YLCKD)){ //预留出库单
    		List<Long> podIds =  commonDao.findByQuery("SELECT pod.id FROM ReservedOrderDetailPtDetail pt "
	                + "LEFT JOIN pt.reservedOrderDetail pod "
	                + "WHERE pt.pickticketDetail.id=:ptID AND pod.quantity-pod.allocatedQuantityBu>0 "
	                + " AND pod.quantity-pod.shippedQuantityBu>0 "
	                + "ORDER BY pod.reservedOrder.id, pod.id", 
	                    "ptID", pickTicketDetail.getId());
    		Long lastid = 0L;
	        for (Long podId : podIds) {
	        	lastid = podId;
	        	WmsReservedOrderDetail pod = commonDao.load(WmsReservedOrderDetail.class, podId);
	            Double unAllocateQty = pod.getUnAllocateQty();
	            if (allocateQty <= 0) {
	                break;
	            }
	            if (unAllocateQty>allocateQty) {
	                CommonHelper.log("拣货单分配修改预留明细("+podId+")"+ allocateQty);
	                pod.addAllocatedQuantityBu(allocateQty);
	                allocateQty = 0D;
	            } else {
	                CommonHelper.log("拣货单分配修改预留明细("+podId+")"+ unAllocateQty);
	                pod.addAllocatedQuantityBu(unAllocateQty);
	                allocateQty = DoubleUtils.sub(allocateQty, unAllocateQty);
	            }
	        }
	        WmsReservedOrderDetail rod = commonDao.load(WmsReservedOrderDetail.class, lastid);
	        if(rod==null){
	        	throw new BusinessException("根据拣货单明细ID:"+pickTicketDetail.getId()+"未找到对应的预留单明细");
	        }
    	}
        
    }
}
