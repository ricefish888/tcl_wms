package com.vtradex.wms.server.service.inventory.pojo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.wms.server.model.component.LotInfo;
import com.vtradex.wms.server.model.dto.WmsInventoryDto;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.item.WmsBillType;
import com.vtradex.wms.server.model.entity.item.WmsCompany;
import com.vtradex.wms.server.model.entity.item.WmsInventoryState;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemJITAtt;
import com.vtradex.wms.server.model.entity.item.WmsItemKey;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.order.PurchaseOrder;
import com.vtradex.wms.server.model.entity.order.PurchaseOrderDetail;
import com.vtradex.wms.server.model.entity.order.PurchaseOrderDetailStatus;
import com.vtradex.wms.server.model.entity.order.WmsTransportOrder;
import com.vtradex.wms.server.model.entity.order.WmsTransportOrderDetail;
import com.vtradex.wms.server.model.entity.order.WmsTransportOrderStatus;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrder;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderDetailStatus;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderStatus;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderDetail;
import com.vtradex.wms.server.model.entity.receiving.WmsASN;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.entity.receiving.WmsASNStatus;
import com.vtradex.wms.server.model.entity.receiving.WmsJITDownLineRecord;
import com.vtradex.wms.server.model.entity.receiving.WmsPo;
import com.vtradex.wms.server.model.entity.receiving.WmsPoDetail;
import com.vtradex.wms.server.model.entity.receiving.WmsReceivedRecord;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;
import com.vtradex.wms.server.model.entity.workdoc.WmsWorkDoc;
import com.vtradex.wms.server.model.enums.BaseStatus;
import com.vtradex.wms.server.model.enums.WmsAsnGenType;
import com.vtradex.wms.server.model.enums.WmsInOutLockType;
import com.vtradex.wms.server.model.enums.WmsInventoryLogType;
import com.vtradex.wms.server.model.enums.WmsInventoryOperationStatus;
import com.vtradex.wms.server.model.enums.WmsLocationCode;
import com.vtradex.wms.server.model.enums.WmsLocationType;
import com.vtradex.wms.server.model.enums.WmsPickTicketStatus;
import com.vtradex.wms.server.model.enums.WmsTaskStatus;
import com.vtradex.wms.server.model.enums.WmsWorkDocStatus;
import com.vtradex.wms.server.service.inventory.WmsInventoryLogManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManageManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.item.TclMessageManager;
import com.vtradex.wms.server.service.item.WmsItemManager;
import com.vtradex.wms.server.service.pickticket.WmsPickticketManager;
import com.vtradex.wms.server.service.receiving.WmsASNManager;
import com.vtradex.wms.server.service.task.WmsTaskManager;
import com.vtradex.wms.server.service.workdoc.WmsWorkDocManager;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.WmsPackageUnitUtils;

/**
 * 库存管理
 * 
 * @author <a href="ming.chen@tech.vtradex.com">陈明</a>
 * @since 2016-01-11
 */
public class DefaultWmsInventoryManageManager extends DefaultBaseManager implements WmsInventoryManageManager {
    private WorkflowManager workflowManager;
    protected WmsInventoryManager inventoryManager;
    private WmsItemManager wmsItemManager;
    private WmsPickticketManager wmsPickticketManager;
    private WmsASNManager wmsASNManager;
    private WmsInventoryLogManager wmsInventoryLogManager;
    private WmsTaskManager wmsTaskManager;
    private WmsWorkDocManager wmsWorkDocManager;
    private TclMessageManager tclMessageManager;
    
    public TclMessageManager getTclMessageManager(){
		if(tclMessageManager == null){
			tclMessageManager = (TclMessageManager)applicationContext.getBean("tclMessageManager");
		}
		return tclMessageManager;
	}
    
    public DefaultWmsInventoryManageManager(WorkflowManager workflowManager,WmsInventoryManager inventoryManager,WmsItemManager wmsItemManager,
            WmsPickticketManager wmsPickticketManager,WmsASNManager wmsASNManager,WmsInventoryLogManager wmsInventoryLogManager,WmsTaskManager wmsTaskManager,WmsWorkDocManager wmsWorkDocManager) {
        super();
        this.workflowManager = workflowManager;
        this.inventoryManager = inventoryManager;
        this.wmsItemManager = wmsItemManager;
        this.wmsPickticketManager = wmsPickticketManager;
        this.wmsASNManager = wmsASNManager;
        this.wmsInventoryLogManager = wmsInventoryLogManager;
        this.wmsTaskManager = wmsTaskManager;
        this.wmsWorkDocManager = wmsWorkDocManager;
    }
    /**
     * 取消收货
     * @Description:
     * @Author:        <a href="ming.chen@tech.vtradex.com">陈明</a>
     * @CreateDate:    2016-01-11
     */
    public void cancelReceipt(WmsInventory inventory,Long asnId,Double packNum,String lineNoStr) {
        if(inventory.getLocation()==null||(!inventory.getLocation().getType().equals(WmsLocationType.RECEIVE))){
            throw new BusinessException("收货库位的库存才能取消收货");
        }else{
        	if(!"待检".equals(inventory.getStatus())){
        		throw new BusinessException("不是待检的库存,不能取消收货");
        	}
            WmsASN asn=new WmsASN();
            if(packNum!=null&&packNum>0){
                if(packNum>inventory.getPackQty()){
                    throw new BusinessException("取消收货数量不能大于库存包装数量");
                }
            }else{
                throw new BusinessException("取消收货数量不正确");
            } 
            //判断库存相关字段是否为空，为空则判断ASN单号是否为空，空则报错“请指定ASN取消收货”；
            if(StringUtils.isEmpty(inventory.getRelatedBillCode())){
                if(asnId==null || asnId==0){
                    throw new BusinessException("请指定ASN取消收货");
                }else{
                    asn=commonDao.load(WmsASN.class, asnId);
                }
            }else{
                String hql = "FROM WmsASN asn WHERE asn.code=:code ";
                asn=(WmsASN)commonDao.findByQueryUniqueResult(hql, "code", inventory.getRelatedBillCode());
                if(asn==null){
                    throw new BusinessException("请指定ASN取消收货");
                }
            }
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(asn.getStartReceivedDate());
            int startReceivedFlag = calendar.get(Calendar.MONTH);
            calendar.setTime(asn.getOrderDate());
            int orderDateFlag = calendar.get(Calendar.MONTH);
            
            if (startReceivedFlag!=orderDateFlag) {
                throw new BusinessException("跨月ASN不允许取消收货");
            }
            
            //根据ASN单号和itemId找到对应的ASN明细，找不到则报错“找不到ASN对应的明细”；找到多条明细则报错“ASN存在相同货品明细。”
            String dhql ="";
            List<WmsASNDetail> detailList = new ArrayList<WmsASNDetail>();
            if(!StringUtils.isEmpty(lineNoStr)){
                 Integer lineNo = 0;
                 try{
                     lineNo = Integer.parseInt(lineNoStr);
                 }catch(Exception e){
                     throw new BusinessException("asnDetail.lineNo.must.be.number");
                 }
                 dhql = "FROM WmsASNDetail detail WHERE detail.asn=:asn AND detail.item=:item AND detail.packageUnit=:packageUnit "
                        + " and detail.lineNo =:lineNo";    
                 detailList=commonDao.findByQuery(dhql, new String[] {"asn","item","packageUnit","lineNo"}, 
                         new Object[] {asn,inventory.getItem(),inventory.getPackageUnit(),lineNo});
            }else{
                 dhql = "FROM WmsASNDetail detail WHERE detail.asn=:asn AND detail.item=:item AND detail.packageUnit=:packageUnit";
                 detailList=commonDao.findByQuery(dhql, new String[] {"asn","item","packageUnit"}, new Object[] {asn,inventory.getItem(),inventory.getPackageUnit()});
            }

            if(detailList==null||detailList.size()<=0){
                throw new BusinessException("找不到ASN对应的明细");
            }else if(detailList.size()>1){
                throw new BusinessException("ASN存在相同货品明细");
            }else{
                for (WmsASNDetail detail : detailList) {
                    WmsTransportOrderDetail tod = detail.getTransportOrderDetail();
                    if (tod!=null) {// 若该asn由配货单创建而来 对相应单据进行回写
                        WmsDeliveryOrderDetail dod = tod.getDeliveryOrderDetail();
                        PurchaseOrderDetail pod = dod.getPurchaseOrderDetail();
                        PurchaseOrder po = pod.getPurchaseOrder();
                        
                        pod.subReceivedQty(packNum);
                        pod.addAllotQty(packNum);
                        po.refreshAllotQty();
                        po.refreshReceiveQty();
                        
                        dod.subDelivedQuantityBu(packNum);
                        dod.addTheDeliveryQuantityBu(packNum);
                        dod.setQuantity(dod.getQuantity()+packNum);
                        if(pod.getReceivedQty()>=pod.getExpectedQty()){//实收数量大于等于计划数量  状态改完成
                        	pod.setStatus(PurchaseOrderDetailStatus.FINISH);
                        }else{
                        	pod.setStatus(PurchaseOrderDetailStatus.UNFINISH);
                        }
                        
                        if(dod.getDelivedQuantityBu()>=dod.getPlanQuantityBu()){
                        	dod.setStatus(WmsDeliveryOrderDetailStatus.FINISH);
                        }else{
                        	dod.setStatus(WmsDeliveryOrderDetailStatus.UNFINISH);
                        }
                        commonDao.store(pod);
                        commonDao.store(dod);
                    }
                    //预留取消收货回写已发运数量
                    WmsBillType billType = commonDao.load(WmsBillType.class, asn.getBillType().getId());
                    if(WmsAsnGenType.YLRKD.equals(billType.getCode())){
                    	WmsReservedOrderDetail rod = commonDao.load(WmsReservedOrderDetail.class, Long.valueOf(detail.getUserField1()));
                		if(rod == null){
                			throw new BusinessException("跟据预留收货单"+asn.getCode()+"未找到对应的预留明细，不能取消收货");
                		}
                		rod.setShippedQuantityBu(DoubleUtils.sub(rod.getShippedQuantityBu(), packNum));
            			commonDao.store(rod);
                    }
                }
                
                WmsASNDetail asnDetail=detailList.get(0);
                //判断该库存是否绑定task，绑定则根据数量修改task和workdoc，如task计划数量为0，则删除，workdoc计划数量为0，则删除；
                if(inventory.getTask()!=null){
                    WmsTask task=inventory.getTask();
                    WmsWorkDoc workDoc=inventory.getTask().getWorkDoc();
                    Double num = WmsPackageUnitUtils.getQtyBU(task.getPackageUnit(), packNum, task.getItem().getBuPrecision());
                    if(task.getStatus().equals(WmsTaskStatus.READY_ALLOCATE)&&(workDoc.getStatus().equals(WmsWorkDocStatus.READY_ALLOCATE))){
                        task.planQty(-num,-packNum);
                        commonDao.store(task);
                        if(task.getPlanQty()==0){
                            commonDao.delete(task);
                        }
                        if(workDoc.getQty()==0){
                            commonDao.delete(workDoc);
                        }
                    }else{
                        throw new BusinessException("存在已分配的任务，无法取消收货");
                    }
                    
                }
                WmsItemKey itemKey = this.commonDao.load(WmsItemKey.class, inventory.getItemKey().getId());
                WmsLocation location = this.commonDao.load(WmsLocation.class, inventory.getLocation().getId());
                String pallet = inventory.getPallet();
                String carton = inventory.getCarton();
                String inventoryStatus =  inventory.getStatus();
                WmsPackageUnit packageUnit = this.commonDao.load(WmsPackageUnit.class, inventory.getPackageUnit().getId());
                //扣除库存数量，如果库存数量为0，则接触绑定task
                inventoryManager.out(inventory, WmsPackageUnitUtils.getQtyBU(inventory.getPackageUnit(), packNum, inventory.getItem().getBuPrecision()),WmsInventoryLogType.CANCEL_RECEIVING,"取消收货");
                //根据库存取消收货数量修改ASN明细收货数量和ASN收货数量；
                Double detailNum = WmsPackageUnitUtils.getQtyBU(asnDetail.getPackageUnit(), packNum, asnDetail.getItem().getBuPrecision());
                //取消收货数量不能大于已收货数量
                Double receiveNum = asnDetail.getReceivedQty();
                if(detailNum>receiveNum){
                    throw new BusinessException("detailNum.can.not.more.than.receiveNum");
                }
                asnDetail.cancelReceive(detailNum,asnDetail.getItem().getBuPrecision());
                if(asnId!=null && asnId!=0){
                    //取消收货时,如果ASN对应收货库位为存货库位【即快捷收货ASN单】,则不更新上架数量
                    WmsLocation loc = this.commonDao.load(WmsLocation.class, asn.getReceiveLocation().getId());
                    if(!WmsLocationType.STORAGE.equals(loc.getType())){
                        asnDetail.cancelMoveQty(detailNum,asnDetail.getItem().getBuPrecision());
                    }
                }
                /*判断asn明细是否关联了PO,是则刷新po和po明细的收货数量*/
                if (null != asnDetail.getPoDetail()) {
                    WmsPoDetail poDetail = this.commonDao.load(WmsPoDetail.class, asnDetail.getPoDetail().getId());
                    poDetail.receive(-detailNum);
                    this.commonDao.store(poDetail);
                }
                commonDao.store(asnDetail);
                //取消收货时删除收货记录表信息
                String hql =" FROM WmsReceivedRecord rr WHERE rr.asnDetail.id = :asnDetailId " 
                          + " and rr.itemKey.id =:itemKeyId and rr.locationId =:locationId and rr.packageUnit =:packageUnit";
                if (StringUtils.isNotEmpty(inventoryStatus)) {
                    hql += " and rr.inventoryStatus = '" + inventoryStatus + "'";
                } else {
                    hql += " and (rr.inventoryStatus is null or rr.inventoryStatus = '') ";
                }
                if (StringUtils.isNotEmpty(pallet)) {
                    hql += " and rr.pallet = '" + pallet + "'";
                } else {
                    hql += " and (rr.pallet is null or rr.pallet = '') ";
                }
                if (StringUtils.isNotEmpty(carton)) {
                    hql += " and rr.carton = '" + carton + "'";
                } else {
                    hql += " and (rr.carton is null or rr.carton = '') ";
                }
                List<WmsReceivedRecord> records = commonDao.findByQuery(hql, new String[]{"asnDetailId","itemKeyId","locationId","packageUnit"}, 
                        new Object[]{asnDetail.getId(),itemKey.getId(),location.getId(),packageUnit.getUnit()});
                Double tempPackNum = packNum;
                for(WmsReceivedRecord record:records){
                    if(tempPackNum.intValue() > 0){
                        if(tempPackNum>=record.getReceivedPackQty()){
                            tempPackNum = tempPackNum - record.getReceivedPackQty();
                            commonDao.delete(record);
                        }else{
                            record.subPackQty(tempPackNum);
                            tempPackNum = 0D;
                        }
                    }
                }
                if(null != asnDetail.getPoDetail()) {
                    WmsPo po = this.commonDao.load(WmsPo.class, asnDetail.getPoDetail().getPo().getId());
                    workflowManager.doWorkflow(po,"wmsPoProcess.singleCancelReceive");
                }
                workflowManager.doWorkflow(asnDetail.getAsn(),"wmsASNProcess.singleCanel");
                
                TclMessageManager tclMessageManager = getTclMessageManager();
                tclMessageManager.sendCancelReceiveInfo2SAP(asnDetail, packNum);
                //取消收货时如果是采购入库的JIT料 需要把关系表数据清除
//                if(WmsAsnGenType.ZCRKD.equals(asnDetail.getAsn().getBillType().getCode()) && WmsItemJITAtt.JIT_DOWNLINE_SETTLE.equals(asnDetail.getItem().getUserFieldV2())){
//                	hql = "FROM WmsJITDownLineRecord r WHERE r.asnCode =:asnCode AND r.detail.id =:detailId ";
//                    List<WmsJITDownLineRecord> jitRecords = commonDao.findByQuery(hql, new String[]{"asnCode","detailId"}, new Object[]{asnDetail.getAsn().getCode(),asnDetail.getId()});
//                    for(WmsJITDownLineRecord r : jitRecords){
//                    	commonDao.delete(r);
//                    }//ZCCKD
//                }
            }
           
            /**回写交货单以及配货单的状态*/
            writeStatusBack(detailList);
        }
    }
    
    /**
     * 取消收货时回写交货单以及配货单状态 fs
     * @param tod
     * @param dod
     */
    @SuppressWarnings("unchecked")
	private void writeStatusBack(List<WmsASNDetail> details){
    	WmsASNDetail asnDetail = details.get(0);
    	WmsTransportOrderDetail tod = asnDetail.getTransportOrderDetail();
    	if(tod == null){//手工新建的ASN没有配货单
    		return;
    	}
    	WmsDeliveryOrderDetail dod = tod.getDeliveryOrderDetail();
    	/**回写配货单状态*/
    	WmsTransportOrder order = commonDao.load(WmsTransportOrder.class,tod.getTransportOrder().getId());//配货单
    	String status = WmsTransportOrderStatus.RECEIVED;//收货完成
    	
    	String hql = "from WmsTransportOrderDetail d where d.transportOrder.id=:id";
    	List<WmsTransportOrderDetail> tranDetails = commonDao.findByQuery(hql,"id",tod.getTransportOrder().getId());
    	Double sumTranQty = 0D;//配货数量之和
    	List<Long> ids = new ArrayList<Long>();//
    	for(WmsTransportOrderDetail d : tranDetails){
    		sumTranQty += d.getQuantity();
    		ids.add(d.getId());
    	}
    	
    	/**配货单明细关联的ASN明细的收货数量之和*/
    	hql = "select sum(d.receivedQty) from WmsASNDetail d where d.transportOrderDetail.id in (:ids)";
    	Double receiveQty = (Double) commonDao.findByQueryUniqueResult(hql, "ids", ids);
    	receiveQty = receiveQty == null ? 0D : receiveQty;
    	if(sumTranQty - receiveQty > 0 && receiveQty > 0){//配货数量>收货数量&&收货数量>0 收货中
    		status = WmsTransportOrderStatus.RECEIVING;//
    	}else if(sumTranQty - receiveQty == 0){
    		status = WmsTransportOrderStatus.RECEIVED;//配货数量==收货数量  收货完成
    	}else if(receiveQty == 0){//收货数量=0  生效
    		status = WmsTransportOrderStatus.ACTIVE;
    	}
    	order.setStatus(status);
    	commonDao.store(order);
    	
    	WmsDeliveryOrder deliveryOrder = commonDao.load(WmsDeliveryOrder.class,dod.getDeliveryOrder().getId());
    	
    	hql = "select sum(d.planQuantityBu),sum(d.delivedQuantityBu) from WmsDeliveryOrderDetail d where d.deliveryOrder.id=:id";
    	List<Object[]> values = commonDao.findByQuery(hql,"id",deliveryOrder.getId());
    	Double planQty = values.get(0)[0] == null ? 0d : (Double) values.get(0)[0];//计划数量
    	Double deliverQty = values.get(0)[1] == null ? 0d : (Double) values.get(0)[1];//已交货数量
    	if(planQty - deliverQty > 0 && deliverQty > 0){//计划数量>已交货数量 && 已交货数量>0  部分收货
    		status = WmsDeliveryOrderStatus.PARTIALRECEIPT;
    	}else if(planQty - deliverQty == 0){//计划数量 == 交货数量  完成
    		status = WmsDeliveryOrderStatus.FINISH;
    	}else if(deliverQty == 0){//已交货数量==0  生效
    		status = WmsDeliveryOrderStatus.ACTIVE;
    	}
		deliveryOrder.setStatus(status);
		commonDao.store(deliveryOrder);
    }
    /**
     * 转换包装
     * @Description:
     * @Author:        <a href="ming.chen@tech.vtradex.com">陈明</a>
     * @CreateDate:    2016-01-27
     */
    public void conversionPackage(WmsInventory inventory,Long targetPackageUnitId,Double conversionPackNum) {
        WmsPackageUnit targetPackageUnit=commonDao.load(WmsPackageUnit.class, targetPackageUnitId);
        //如果(需要转换的目标数量转换成基本包装单位的数量)大于(原包装库存数量)，则提示错误；
        Double conversionNum = WmsPackageUnitUtils.getQtyBU(targetPackageUnit, conversionPackNum, targetPackageUnit.getItem().getBuPrecision());
        if(conversionNum>inventory.getQty()){
            throw new BusinessException("转换失败:转换目标数量大于原包装库存数量!");
        }
        //Double conversionNum = WmsPackageUnitUtils.getQtyBU(inventory.getPackageUnit(), conversionPackNum, inventory.getItem().getBuPrecision());
        //查询是否存在相同货品、库位、批次、包装、库存状态的库存，存在则更新数量，不存在则新建库存；
        inventoryManager.changeWmsInventory(inventory, targetPackageUnit, conversionPackNum);
        //原来单位的包装小数
        Double decPackNum=0D;
        Double oldSubPackNum = WmsPackageUnitUtils.getPackQty(inventory.getPackageUnit(), inventory.getQty()-conversionNum, inventory.getItem().getBuPrecision());
        Double oldPackNum = WmsPackageUnitUtils.getPackQty(inventory.getPackageUnit(), conversionNum, inventory.getItem().getBuPrecision());
        
        int intOldSubPackNum = oldSubPackNum.intValue();
        if(oldSubPackNum>intOldSubPackNum){
            DecimalFormat df =new DecimalFormat("#.000");  
            decPackNum=Double.valueOf(df.format(oldSubPackNum-intOldSubPackNum));
        }
        //原来单位的包装小数转为最小单位的数，再转为最小单位的数。
        Double decToNum=0D;
        decToNum = WmsPackageUnitUtils.getQtyBU(inventory.getPackageUnit(),decPackNum,inventory.getItem().getBuPrecision());
        //查询是否存在相同货品、库位、批次、最小包装单位、库存状态的库存，存在则更新数量，不存在则新建库存；
        String hql = "FROM WmsPackageUnit unit WHERE unit.item=:item AND unit.convertFigure=1 AND unit.status='ENABLED'";
        WmsPackageUnit unit=(WmsPackageUnit)commonDao.findByQueryUniqueResult(hql, "item", inventory.getItem());
        if(unit!=null){
            //查询是否存在相同货品、库位、批次、包装、库存状态的库存，存在则更新数量，不存在则新建库存；
            inventoryManager.changeWmsInventory(inventory, unit, decToNum);
        }
        //扣减转换后的原库存数量；
        if(inventory.getPackQty()>oldPackNum){
            inventory.subPackQty(oldPackNum+decPackNum);
            commonDao.store(inventory);
        }else{
            commonDao.delete(inventory);
        }       
    }

    /**
     * 库存保存
     * @Description:
     * @Author:        <a href="yequan.song@vtradex.net">宋叶全</a>
     * @CreateDate:    2016年2月4日
     * @param inventory:
     * @arithMetic:
     * @exception:
     */
    public void storeInventory(WmsInventory inventory,String descript) {
        // TODO Auto-generated method stub
        String warehouseHql = "FROM WmsWarehouse warehouse WHERE warehouse.baseOrganization.id = :baseOrganizationId";
        WmsWarehouse warehouse = (WmsWarehouse)commonDao.findByQueryUniqueResult(warehouseHql, 
                new String[] {"baseOrganizationId"}, new Object[] {BaseOrganizationHolder.getThornBaseOrganization().getId()});
        
        String minPackageUnitHql = "FROM WmsPackageUnit packageUnit WHERE packageUnit.item.id =:itemId AND packageUnit.convertFigure =1 ";
        WmsPackageUnit packageUnit = (WmsPackageUnit)commonDao.findByQueryUniqueResult(minPackageUnitHql, 
                new String[] {"itemId"}, new Object[] {inventory.getItem().getId()});
        
        String countLocationHql = "FROM WmsLocation location WHERE location.type =:countType AND location.status =:countStatus AND location.warehouse.id =:warehouseId";
        List<WmsLocation> countLocations = commonDao.findByQuery(countLocationHql, new String[]{"countType","countStatus","warehouseId"}, new Object[]{WmsLocationType.COUNT,BaseStatus.ENABLED,warehouse.getId()});
        if(countLocations == null || countLocations.size() <=0){
            throw new BusinessException("can.not.find.the.count.location");
        }
        
        WmsItemKey itemKey = wmsItemManager.getItemKeyNVerify(warehouse,"", inventory.getItem(), null);
        Double packNum = WmsPackageUnitUtils.getPackQty(packageUnit, inventory.getQty(), inventory.getItem().getBuPrecision());
        //盘点库位扣减
        WmsInventoryDto countInventoryDto = new WmsInventoryDto();

        countInventoryDto.setWarehouse(warehouse);
        countInventoryDto.setCompany(inventory.getCompany());
        countInventoryDto.setLocation(countLocations.get(0));
        countInventoryDto.setItem(inventory.getItem());
        countInventoryDto.setItemKey(itemKey);
        countInventoryDto.setPackageUnit(packageUnit);
        countInventoryDto.setQty(-inventory.getQty());
        countInventoryDto.setPackQty(-packNum);
        countInventoryDto.setStatus(inventory.getStatus());
        countInventoryDto.setType(WmsInventoryLogType.QTY_CHANGE);
        countInventoryDto.setDescript(descript);
        countInventoryDto.setPallet(inventory.getPallet());
        countInventoryDto.setCarton(inventory.getCarton());
        
        inventoryManager.in(countInventoryDto);
        
        
        //存货库位新增
        WmsInventoryDto inventoryDto = new WmsInventoryDto();

        inventoryDto.setWarehouse(warehouse);
        inventoryDto.setCompany(inventory.getCompany());
        inventoryDto.setLocation(inventory.getLocation());
        inventoryDto.setItem(inventory.getItem());
        inventoryDto.setItemKey(itemKey);
        inventoryDto.setPackageUnit(packageUnit);
        inventoryDto.setQty(inventory.getQty());
        inventoryDto.setPackQty(packNum);
        inventoryDto.setStatus(inventory.getStatus());
        inventoryDto.setType(WmsInventoryLogType.QTY_CHANGE);
        inventoryDto.setDescript(descript);
        inventoryDto.setPallet(inventory.getPallet());
        inventoryDto.setCarton(inventory.getCarton());

        inventoryManager.in(inventoryDto);
        //刷新库满度
//      inventoryManager.refreshLocationUseRate(inventory.getLocation(), 1);
    }

    /**
     * 库存数量调整
     * @Description:
     * @Author:        <a href="yequan.song@vtradex.net">宋叶全</a>
     * @CreateDate:    2016年2月4日
     * @param inventory:
     * @arithMetic:
     * @exception:
     */
    public void storeInventoryQtyAdjust(WmsInventory inventory,Double changePackQty,String descript) {
        String countLocationHql = "FROM WmsLocation location WHERE location.type =:countType AND location.status =:countStatus AND location.warehouse.id =:warehouseId";
        List<WmsLocation> countLocations = commonDao.findByQuery(countLocationHql, new String[]{"countType","countStatus","warehouseId"}, new Object[]{WmsLocationType.COUNT,BaseStatus.ENABLED,inventory.getWarehouse().getId()});
        if(countLocations == null || countLocations.size() <=0){
            throw new BusinessException("can.not.find.the.count.location");
        }
        
        WmsWarehouse warehouse = inventory.getWarehouse();
        WmsCompany company = inventory.getCompany();
        WmsLocation location = inventory.getLocation();
        WmsItem item = inventory.getItem();
        WmsItemKey itemKey = inventory.getItemKey();
        WmsPackageUnit unit = inventory.getPackageUnit();
        String state = inventory.getStatus();
        
        Double discPackQty = DoubleUtils.sub(changePackQty,inventory.getPackQty(),item.getBuPrecision());
        if(discPackQty.doubleValue() == 0){
            throw new BusinessException("inventory.number.is.not.modified");
        }
        
//      Double pickedPackQty = WmsPackageUnitUtils.getPackQty(inventory.getPackageUnit(), discQty, inventory.getItem().getBuPrecision());
        Double discQty = WmsPackageUnitUtils.getQtyBU(unit, discPackQty, item.getBuPrecision());
        //盘点库位扣减
        WmsInventoryDto countInventoryDto = new WmsInventoryDto();

        countInventoryDto.setWarehouse(warehouse);
        countInventoryDto.setCompany(company);
        countInventoryDto.setLocation(countLocations.get(0));
        countInventoryDto.setItem(item);
        countInventoryDto.setItemKey(itemKey);
        countInventoryDto.setPackageUnit(unit);
        countInventoryDto.setQty(-discQty);
        countInventoryDto.setPackQty(-discPackQty);
        countInventoryDto.setStatus(state);
        countInventoryDto.setType(WmsInventoryLogType.QTY_CHANGE);
        countInventoryDto.setDescript(descript);
        countInventoryDto.setPallet(inventory.getPallet());
        countInventoryDto.setCarton(inventory.getCarton());
                
        inventoryManager.out(countInventoryDto);
        
        //存货库位新增
        WmsInventoryDto inventoryDto = new WmsInventoryDto();

        inventoryDto.setWarehouse(warehouse);
        inventoryDto.setCompany(company);
        inventoryDto.setLocation(location);
        inventoryDto.setItem(item);
        inventoryDto.setItemKey(itemKey);
        inventoryDto.setPackageUnit(unit);
        inventoryDto.setQty(discQty);
        inventoryDto.setPackQty(discPackQty);
        inventoryDto.setStatus(state);
        inventoryDto.setType(WmsInventoryLogType.QTY_CHANGE);
        inventoryDto.setDescript(descript);
        inventoryDto.setPallet(countInventoryDto.getPallet());
        inventoryDto.setCarton(countInventoryDto.getCarton());

        inventoryManager.in(inventoryDto);
        //刷新库满度
//      inventoryManager.refreshLocationUseRate(inventory.getLocation(), 1);
        
    }

    /**
     * 库存批次调整
     * @Description:
     * @Author:        <a href="yequan.song@vtradex.net">宋叶全</a>
     * @CreateDate:    2016年2月4日
     * @param inventory:
     * @arithMetic:
     * @exception:
     */
    public void storeInventoryLotInfoAdjust(WmsInventory inventory,LotInfo lotinfo,String descript) {
//      if(inventory.getItemKey().getLotInfo().stringValue().equals(lotinfo.stringValue())){
//          throw new BusinessException("批次没有修改。");
//      }
        
        WmsWarehouse warehouse = inventory.getWarehouse();
        WmsCompany company = inventory.getCompany();
        WmsLocation location = inventory.getLocation();
        WmsItem item = inventory.getItem();
        WmsPackageUnit unit = inventory.getPackageUnit();
        Double packQty = inventory.getPackQty();
        String state = inventory.getStatus();
        Double qty = WmsPackageUnitUtils.getQtyBU(unit, packQty, item.getBuPrecision());
        
        WmsItemKey itemKey = wmsItemManager.getItemKeyByLotInfoModify(inventory.getWarehouse(), 
                lotinfo.getAsnCustomerBill(), inventory.getItem(), lotinfo);
        if(inventory.getItemKey().getLotInfo()!=null){
        if(inventory.getItemKey().getLotInfo().stringValue().equals(itemKey.getLotInfo().stringValue())){
            throw new BusinessException("no.lotInfo.modify");
        }
        }
        //库位扣减
        WmsInventoryDto countInventoryDto = new WmsInventoryDto();

        countInventoryDto.setWarehouse(warehouse);
        countInventoryDto.setCompany(company);
        countInventoryDto.setLocation(location);
        countInventoryDto.setItem(item);
        countInventoryDto.setItemKey(inventory.getItemKey());
        countInventoryDto.setPackageUnit(unit);
        countInventoryDto.setQty(qty);
        countInventoryDto.setPackQty(packQty);
        countInventoryDto.setStatus(state);
        countInventoryDto.setType(WmsInventoryLogType.ITEM_KEY_CHANGE);
        countInventoryDto.setDescript(descript);
        countInventoryDto.setPallet(inventory.getPallet());
        countInventoryDto.setCarton(inventory.getCarton());
        
        inventoryManager.out(countInventoryDto);
        
        //存货库位新增
        WmsInventoryDto inventoryDto = new WmsInventoryDto();

        inventoryDto.setWarehouse(warehouse);
        inventoryDto.setCompany(company);
        inventoryDto.setLocation(location);
        inventoryDto.setItem(item);
        inventoryDto.setItemKey(itemKey);
        inventoryDto.setPackageUnit(unit);
        inventoryDto.setQty(qty);
        inventoryDto.setPackQty(packQty);
        inventoryDto.setStatus(state);
        inventoryDto.setType(WmsInventoryLogType.ITEM_KEY_CHANGE);
        inventoryDto.setDescript(descript);
        inventoryDto.setPallet(countInventoryDto.getPallet());
        inventoryDto.setCarton(countInventoryDto.getCarton());

        inventoryManager.in(inventoryDto);
    }

    /**
     * 库存状态调整
     * @Description:
     * @Author:        <a href="yequan.song@vtradex.net">宋叶全</a>
     * @CreateDate:    2016年2月4日
     * @param inventory:
     * @arithMetic:
     * @exception:
     */
    @SuppressWarnings("unchecked")
    public void storeInventoryStateAdjust(WmsInventory inventory, Long inventoryStateId, String descript, Boolean beLocation, Boolean beLot, Boolean beProductDate) {
        
        if (!WmsInventoryOperationStatus.NORMAL.equals(inventory.getOperationStatus())) {
            throw new BusinessException("inventory.status.is.normal");
        }
        
        if (!beLot && !beLocation && !beProductDate) {
            WmsInventoryState inventoryState = commonDao.load(WmsInventoryState.class, inventoryStateId);
            String changgeStatus= inventoryState.getName();
            if(inventory.getStatus().equals(changgeStatus)){
                throw new BusinessException("inventory.status.is.not.modified");
            }
            WmsWarehouse warehouse = inventory.getWarehouse();
            WmsCompany company = inventory.getCompany();
            WmsLocation location = inventory.getLocation();
            WmsItem item = inventory.getItem();
            WmsItemKey itemKey = inventory.getItemKey();
            WmsPackageUnit unit = inventory.getPackageUnit();
            Double packQty = inventory.getPackQty();
            Double qty = WmsPackageUnitUtils.getQtyBU(unit, packQty, item.getBuPrecision());
            
            this.storeSingleInventorySateAdjust(inventory, changgeStatus, warehouse, company, location, item, itemKey, unit, packQty, qty, descript);
        } else {
            List<String> paramsNameList = new ArrayList<String>();
            paramsNameList.add("companyId");
            paramsNameList.add("warehouseId");
            paramsNameList.add("itemId");
            
            List<Object> paramsValueList = new ArrayList<Object>();
            paramsValueList.add(inventory.getCompany().getId());
            paramsValueList.add(inventory.getWarehouse().getId());
            paramsValueList.add(inventory.getItem().getId());
            
            StringBuffer hql = new StringBuffer("from WmsInventory inv "
                    + " where inv.company.id = :companyId and inv.warehouse.id = :warehouseId "
                    + " and inv.item.id = :itemId and inv.operationStatus = 'NORMAL'");
            if (beLocation) {
                hql.append(" and inv.location.id = :locId ");
                paramsNameList.add("locId");
                paramsValueList.add(inventory.getLocation().getId());
            }
            if (beLot) {
                hql.append(" and inv.itemKey.lotInfo.lot = :lot");
                paramsNameList.add("lot");
                paramsValueList.add(inventory.getItemKey().getLotInfo().getLot());
            }
            if (beProductDate) {
                hql.append(" and inv.itemKey.lotInfo.productDate = :productDate");
                paramsNameList.add("productDate");
                paramsValueList.add(inventory.getItemKey().getLotInfo().getProductDate());
            }
            
            List<WmsInventory> invList = commonDao.findByQuery(hql.toString(), paramsNameList.toArray(new String[]{}), paramsValueList.toArray());
            for (WmsInventory destInv : invList) {
                WmsInventoryState inventoryState = commonDao.load(WmsInventoryState.class, inventoryStateId);
                String changgeStatus = inventoryState.getName();
                if(inventory.getStatus().equals(changgeStatus)){
                    throw new BusinessException("inventory.status.is.not.modified");
                }
                WmsWarehouse warehouse = destInv.getWarehouse();
                WmsCompany company = destInv.getCompany();
                WmsLocation location = destInv.getLocation();
                WmsItem item = destInv.getItem();
                WmsItemKey itemKey = destInv.getItemKey();
                WmsPackageUnit unit = destInv.getPackageUnit();
                Double packQty = destInv.getPackQty();
                Double qty = WmsPackageUnitUtils.getQtyBU(unit, packQty, item.getBuPrecision());
                
                this.storeSingleInventorySateAdjust(destInv, changgeStatus, warehouse, company, location, item, itemKey, unit, packQty, qty, descript);
            }
        }
    }
    
    private void storeSingleInventorySateAdjust(WmsInventory inventory, String changgeStatus, WmsWarehouse warehouse, WmsCompany company, 
            WmsLocation location, WmsItem item, WmsItemKey itemKey, WmsPackageUnit unit, Double packQty, Double qty, String descript) {     
        
        //库位扣减
//      WmsInventoryDto countInventoryDto = new WmsInventoryDto();
//
//      countInventoryDto.setWarehouse(warehouse);
//      countInventoryDto.setCompany(company);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ,m                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
//      countInventoryDto.setLocation(location);
//      countInventoryDto.setItem(item);
//      countInventoryDto.setItemKey(itemKey);
//      countInventoryDto.setPackageUnit(unit);
//      countInventoryDto.setQty(qty);
//      countInventoryDto.setPackQty(packQty);
//      countInventoryDto.setStatus(inventory.getStatus());
//      countInventoryDto.setType(WmsInventoryLogType.STATUS_CHANGE);
//      countInventoryDto.setDescript(descript);
//      countInventoryDto.setPallet(inventory.getPallet());
//      countInventoryDto.setCarton(inventory.getCarton());
        
//      inventoryManager.outout(countInventoryDto);
        String pallet = inventory.getPallet();
        String carton = inventory.getCarton();
        //状态调整库存扣减
        inventoryManager.out(inventory, qty, WmsInventoryLogType.STATUS_CHANGE,descript);
        
        //存货库位库存新增
        WmsInventoryDto inventoryDto = new WmsInventoryDto();

        inventoryDto.setWarehouse(warehouse);
        inventoryDto.setCompany(company);
        inventoryDto.setLocation(location);
        inventoryDto.setItem(item);
        
        
        inventoryDto.setItemKey(itemKey);
        inventoryDto.setPackageUnit(unit);
        inventoryDto.setQty(qty);
        inventoryDto.setPackQty(packQty);
        inventoryDto.setStatus(changgeStatus);
        inventoryDto.setType(WmsInventoryLogType.STATUS_CHANGE);
        inventoryDto.setDescript(descript);
        inventoryDto.setPallet(pallet);
        inventoryDto.setCarton(carton);
        

        inventoryManager.in(inventoryDto);
        
    }
    
    /**
     * 
     * @Title: storeInventoryMoveByPallet
     * 
     * @Description: 按托移位
     *
     * @return void    
     *
     * @throws 
     *
     * @author huangchao
     *
     * @date 2016年4月13日 下午12:00:53
     */
    @Override
    public void storeInventoryMoveByPallet(WmsInventory inventory, Long locationId, String descript) {
        
        WmsLocation wmslocation = commonDao.load(WmsLocation.class, locationId);
        if(wmslocation.getCountLock()||inventory.getLocation().getCountLock()){
            throw new BusinessException("wmsLocation.has.been.countLocked");
        }
        //检查移出库位出锁
        verifyLocationInOut(BaseStatus.LOCK_OUT,inventory.getLocation().getId());
        //检查移入库位入锁
        verifyLocationInOut(BaseStatus.LOCK_IN,wmslocation.getId());
        String hql = "from WmsInventory wsn where wsn.pallet = :pallet and wsn.location = :location and wsn.qty >0 and wsn.location.countLock = false";
        List<WmsInventory> wsns = this.commonDao.findByQuery(hql, new String[]{"pallet","location"}, new Object[]{inventory.getPallet(),inventory.getLocation()});
        
        Boolean flag = false;
        for(WmsInventory wsn:wsns){
            if(!WmsInventoryOperationStatus.NORMAL.equals(wsn.getOperationStatus())){
                flag = true;
                break;
            }
        }
        
        WmsLocation toLocation = commonDao.load(WmsLocation.class, locationId);
        
        if(flag){
            throw new BusinessException("wmsinventory.has.been.alloted.can.not.add.by.pallet");
        }else{
            for(WmsInventory wsn:wsns){
                
                WmsWarehouse warehouse = wsn.getWarehouse();
                WmsCompany company = wsn.getCompany();
                WmsLocation location = wsn.getLocation();
                WmsItem item = wsn.getItem();
                WmsItemKey itemKey = wsn.getItemKey();
                WmsPackageUnit unit = wsn.getPackageUnit();
                Double packQty = wsn.getPackQty();
                Double qty = wsn.getQty();
                String inventoryStatus =  wsn.getStatus();
                String pallet = wsn.getPallet();
                String carton = wsn.getCarton();
                Integer palletSeq = wsn.getPalletSeq();
                Boolean lockStatus = wsn.getLockStatus();
                
                //库位扣减
                WmsInventoryDto countInventoryDto = new WmsInventoryDto();

                countInventoryDto.setWarehouse(warehouse);
                countInventoryDto.setCompany(company);
                countInventoryDto.setLocation(location);
                countInventoryDto.setItem(item);
                countInventoryDto.setItemKey(itemKey);
            
                countInventoryDto.setPackageUnit(unit);
                countInventoryDto.setQty(wsn.getQty());
                countInventoryDto.setPackQty(packQty);
                countInventoryDto.setStatus(wsn.getStatus());
                countInventoryDto.setType(WmsInventoryLogType.MOVE);
                countInventoryDto.setDescript(descript);
                countInventoryDto.setPallet(wsn.getPallet());
                countInventoryDto.setCarton(wsn.getCarton());
                countInventoryDto.setPalletSeq(wsn.getPalletSeq());
                countInventoryDto.setLockStatus(lockStatus);
                
//              inventoryManager.out(countInventoryDto);
                inventoryManager.out(wsn,qty,WmsInventoryLogType.MOVE,"");
                //刷新库满度
//              inventoryManager.refreshLocationUseRate(location, -1);
                //存货库位新增
                WmsInventoryDto inventoryDto = new WmsInventoryDto();

                inventoryDto.setWarehouse(warehouse);
                inventoryDto.setCompany(company);
                inventoryDto.setLocation(toLocation);
                inventoryDto.setItem(item);
                inventoryDto.setItemKey(itemKey);
                inventoryDto.setPackageUnit(unit);
                inventoryDto.setQty(qty);
                inventoryDto.setPackQty(packQty);
                inventoryDto.setStatus(inventoryStatus);
                inventoryDto.setType(WmsInventoryLogType.MOVE);
                inventoryDto.setDescript(descript);
                inventoryDto.setPallet(pallet);
                inventoryDto.setCarton(carton);
                inventoryDto.setPalletSeq(palletSeq);
                inventoryDto.setLockStatus(lockStatus);

                inventoryManager.in(inventoryDto);
                //刷新库满度
//              inventoryManager.refreshLocationUseRate(toLocation, 1);
            }
        }
        
    }

    public void storeInventoryCargoTransfer(WmsInventory inventory,Long companyId,Double transferQty){
        //转移时，扣减原库位货主库存，如果数量为0，则删除该库存；  单位为包装单位
        Double surplusQty = inventory.getPackQty() - transferQty;
        if(surplusQty < 0){
            throw new BusinessException("transferQty.has.over");
        }else{
            //转移时，生成新货主对应库存，除货主和库存数量不一致外，生成的库存记录与原库存记录数据一致；
            WmsCompany newCompany = this.commonDao.load(WmsCompany.class, companyId);
            WmsCompany oldCompany = this.commonDao.load(WmsCompany.class, inventory.getCompany().getId());
            WmsItem oldItem = this.commonDao.load(WmsItem.class,inventory.getItem().getId());
            WmsPackageUnit oldPackageUnit = commonDao.load(WmsPackageUnit.class, inventory.getPackageUnit().getId());
            String hql = "from WmsItem item where item.code =:code and item.masterGroup.id =:masterGroupId";
            List<WmsItem> items = this.commonDao.findByQuery(hql,new String[]{"code","masterGroupId"},
                    new Object[]{inventory.getItem().getCode(),newCompany.getMasterGroup().getId()});
            if(items.isEmpty()||items.size()<=0){
                throw new BusinessException("this.newCompany.don't.have.this.item");
            }
            String hql2 = "FROM WmsPackageUnit packageUnit WHERE packageUnit.unit = :unit AND packageUnit.item.id = :itemId";
            
            List<WmsPackageUnit> units = commonDao.findByQuery(hql2, 
                    new String[]{"unit","itemId"}, 
                    new Object[]{inventory.getPackageUnit().getUnit(),items.get(0).getId()});
            if(units.isEmpty()||units.size()<=0){
                throw new BusinessException("this.newCompany.don't.have.this.item.unit");
            }
            //新建itemKey
            WmsWarehouse warehouse = this.commonDao.load(WmsWarehouse.class, inventory.getWarehouse().getId());
            WmsItemKey oldItemKey = this.commonDao.load(WmsItemKey.class, inventory.getItemKey().getId());
            WmsItemKey newItemKey = wmsItemManager.getItemKey(warehouse,
                    oldItemKey.getLotInfo().getSoiCode() , items.get(0), oldItemKey.getLotInfo());
            WmsInventory newInventory = EntityFactory.getEntity(WmsInventory.class);
            BeanUtils.copyEntity(newInventory, inventory);
            newInventory.setId(null);
            newInventory.setCompany(newCompany);
            newInventory.setItem(items.get(0));
            newInventory.setItemKey(newItemKey);
            newInventory.setPackageUnit(units.get(0));
            newInventory.setPackQty(transferQty);
            newInventory.setQty(transferQty * units.get(0).getConvertFigure());
            this.commonDao.store(newInventory);
            
            autoPickFinish(inventory, oldCompany, transferQty);
            autoAsnFinish(newInventory, newCompany, transferQty);
            //转移时，扣减原库位货主库存，如果数量为0，则删除该库存；
            if(surplusQty == 0D){
                this.commonDao.delete(inventory);
            }else{
                inventory.setPackQty(surplusQty);
                inventory.setQty(surplusQty * oldPackageUnit.getConvertFigure());
            }
        }
        
    }
    
    public void autoPickFinish(WmsInventory inventory,WmsCompany oldCompany,Double transferQty){
        //转移完成后，系统自动生成原货主对应拣货单(WmsPickTicket)、拣货单明细(WmsPickTicketDetail)，状态为完成，
        //单据类型为货主转移出库单(单据类型为货主转移出库单)；
        WmsPickTicket wmsPickTicket = EntityFactory.getEntity(WmsPickTicket.class);
        WmsPickTicketDetail wmsPickTicketDetail = EntityFactory.getEntity(WmsPickTicketDetail.class);
        String getBillType = "from WmsBillType billType where billType.code = :code and billType.masterGroup.id = :masterGroupId";
        WmsBillType wmsBillTypeOut = (WmsBillType) this.commonDao.findByQueryUniqueResult(getBillType, new String[]{"code","masterGroupId"}, new Object[]{"TRANSFER_OUT",oldCompany.getMasterGroup().getId()});
        if(wmsBillTypeOut == null){
            throw new BusinessException("TRANSFER_OUT.billType.not.exist");
        }
        
        wmsPickTicket.setCompany(inventory.getCompany());
        wmsPickTicket.setBillType(wmsBillTypeOut);
        wmsPickTicket.setOrderDate(new Date());
        wmsPickticketManager.storePickTicket(wmsPickTicket);
        
        wmsPickTicketDetail.setLineNo(1);
        wmsPickTicketDetail.setItem(inventory.getItem());
        wmsPickTicketDetail.setPackageUnit(inventory.getPackageUnit());
        wmsPickTicketDetail.setExpectedPackQty(transferQty);
        wmsPickTicketDetail.setInventoryStatus(inventory.getStatus());
        wmsPickticketManager.addPickTicketDetail(wmsPickTicket.getId(), wmsPickTicketDetail);
        
        wmsPickTicketDetail.allocate(wmsPickTicketDetail.getExpectedQty());
        wmsPickTicketDetail.pickedQty(wmsPickTicketDetail.getExpectedQty());
        wmsPickTicketDetail.shippedQty(wmsPickTicketDetail.getExpectedQty());
        wmsPickTicket.setStatus(WmsPickTicketStatus.FINISHED);
        this.commonDao.store(wmsPickTicketDetail);
        this.commonDao.store(wmsPickTicket);
        
        //一个原货主的从目标数量到0的库存日志
        wmsInventoryLogManager.addInventoryLog(WmsInventoryLogType.QTY_CHANGE, wmsPickTicket.getCode(), wmsPickTicket.getBillType(), inventory.getLocation(), oldCompany, inventory.getItemKey(), inventory.getPackQty(), -transferQty, inventory.getStatus(), StringUtils.isEmpty(inventory.getPallet())?"":inventory.getPallet(),"货权转移");
    }
    
    public void autoAsnFinish(WmsInventory inventory,WmsCompany newCompany,Double transferQty){
        //新货主ASN和ASNDetail完成；，
        WmsASN wmsAsn = EntityFactory.getEntity(WmsASN.class);
        WmsASNDetail wmsAsndetail = EntityFactory.getEntity(WmsASNDetail.class);
        
        //货主转移入库单(单据类型为货主转移入库单)
        String getBillType = "from WmsBillType billType where billType.code = :code and billType.masterGroup.id = :masterGroupId";
        WmsBillType wmsBillTypeIn = (WmsBillType) this.commonDao.findByQueryUniqueResult(getBillType, new String[]{"code","masterGroupId"}, new Object[]{"TRANSFER_IN",newCompany.getMasterGroup().getId()});
        if(wmsBillTypeIn == null){
            throw new BusinessException("TRANSFER_IN.billType.not.exist");
        }
        wmsAsn.setCompany(newCompany);
        wmsAsn.setBillType(wmsBillTypeIn);
        wmsAsn.setOrderDate(new Date());
        wmsASNManager.storeASN(wmsAsn);
        
        wmsAsndetail.setLineNo(1);
        wmsAsndetail.setItem(inventory.getItem());
        wmsAsndetail.setPackageUnit(inventory.getPackageUnit());
        wmsASNManager.addDetail(wmsAsn.getId(), wmsAsndetail, transferQty);
        
        wmsAsndetail.receive(wmsAsndetail.getExpectedQty());
        wmsAsndetail.addMovedQty(wmsAsndetail.getExpectedQty());
        wmsAsn.setReceiveLocation(inventory.getLocation());
        wmsAsn.setStatus(WmsASNStatus.RECEIVED);
        this.commonDao.store(wmsAsndetail);
        this.commonDao.store(wmsAsn);
        
        //一个新货主的从0到目标数量的库存日志；
        wmsInventoryLogManager.addInventoryLog(WmsInventoryLogType.QTY_CHANGE, wmsAsn.getCode(), wmsAsn.getBillType(), inventory.getLocation(), newCompany, inventory.getItemKey(), 0D, transferQty, inventory.getStatus(),StringUtils.isEmpty(inventory.getPallet())?"":inventory.getPallet(), "货权转移");
    }
    @Override
    public void refreshLotInfo(WmsTask oldTask, LotInfo newLotInfo, Double inQty) {
        WmsInventoryDto wmsInventoryDto = inventoryManager.getWmsInventoryDto(oldTask, inQty, oldTask.getFromLocation());
        List<WmsInventory> inventorys= inventoryManager.getWmsInventory(wmsInventoryDto, newLotInfo, WmsInventoryOperationStatus.NORMAL);
        Double oldQty = inQty;
        for (WmsInventory wmsInventory : inventorys) {
            //TODO  循环处理库存  存在根据数量和原task生成新的task；(可能存在多条)，扣减库存，并生成原task对应目标库位库存
             
            if(oldQty > wmsInventory.getQty()){
                inventoryManager.out(wmsInventory, wmsInventory.getQty(), WmsInventoryLogType.MOVE,"移位"); 
                wmsInventoryDto.setRelatedObjBillType("PT");
                inventoryManager.in(wmsInventoryDto);
                //TODO 处理库存如 IN 方法  
                WmsTask newTask = wmsTaskManager.createWmsTask(oldTask, oldTask.getFromLocation(), wmsInventory.getQty(), WmsTaskStatus.FINISH);
                this.wmsWorkDocManager.updatePickTicket(newTask,wmsInventory.getQty());
                this.workflowManager.doWorkflow(newTask.getWorkDoc(), "wmsWorkDocProcess.confirmDecision");
                oldQty = DoubleUtils.sub(oldQty, wmsInventory.getQty());
                
            }else{
                inventoryManager.out(wmsInventory, oldQty, WmsInventoryLogType.MOVE,"移位"); 
                wmsTaskManager.createWmsTask(oldTask, oldTask.getFromLocation(), oldQty, WmsTaskStatus.FINISH);
                oldQty = 0d;
                break;
            }
            
        } 
        if(oldQty > 0){
            throw new BusinessException("库存数量不足");
        }
    }
    /* (non-Javadoc)
     * @see com.vtradex.wms.server.service.inventory.WmsInventoryManageManager#verifyLocation(java.lang.String, java.lang.Long)
     */
    @Override
    public void verifyLocationInOut(String inOutLockType, Long locationId) {
        // TODO Auto-generated method stub
        WmsLocation location = this.commonDao.load(WmsLocation.class, locationId);
        if(BaseStatus.LOCK_IN.equals(inOutLockType)){
            if(WmsInOutLockType.INLOCKED.equals(location.getInOutLock())||WmsInOutLockType.LOCKED.equals(location.getInOutLock())){
                throw new BusinessException("this.location.has.been.locked");
            }
        }else if(BaseStatus.LOCK_OUT.equals(inOutLockType)){
            if(WmsInOutLockType.OUTLOCKED.equals(location.getInOutLock())||WmsInOutLockType.LOCKED.equals(location.getInOutLock())){
                throw new BusinessException("this.location.has.been.locked");
            }
        }
    }
    /* (non-Javadoc)
     * @see com.vtradex.wms.server.service.inventory.WmsInventoryManageManager#moveInventory(com.vtradex.wms.server.model.entity.inventory.WmsInventory, java.lang.Long, java.lang.String)
     */
    @Override
    public void moveInventory(WmsInventory inventory, Long toLocationId,Double packQty,String descript) {
        // TODO Auto-generated method stub
        WmsWarehouse warehouse = inventory.getWarehouse();
        WmsCompany company = inventory.getCompany();
        WmsItem item = inventory.getItem();
        WmsItemKey itemKey = inventory.getItemKey();
        WmsPackageUnit unit = inventory.getPackageUnit();
        Double qty = WmsPackageUnitUtils.getQtyBU(unit, packQty, item.getBuPrecision());
        WmsLocation toLocation = this.commonDao.load(WmsLocation.class, toLocationId);
        WmsInventoryDto countInventoryDto = new WmsInventoryDto();
        //移出库位减少
//      countInventoryDto.setWarehouse(warehouse);
//      countInventoryDto.setCompany(company);
//      countInventoryDto.setLocation(inventory.getLocation());
//      countInventoryDto.setItem(item);
//      countInventoryDto.setItemKey(itemKey);
//      countInventoryDto.setPackageUnit(unit);
//      countInventoryDto.setQty(qty);
//      countInventoryDto.setPackQty(packQty);
//      countInventoryDto.setStatus(inventory.getStatus());
//      countInventoryDto.setType(WmsInventoryLogType.MOVE);
//      countInventoryDto.setDescript(descript);
//      countInventoryDto.setPallet(inventory.getPallet());
//      countInventoryDto.setCarton(inventory.getCarton());
//      countInventoryDto.setPalletSeq(inventory.getPalletSeq());
        
        Boolean isSendSAP = Boolean.FALSE; 
        
        //移入库位新增
        WmsInventoryDto inventoryDto = new WmsInventoryDto();

        inventoryDto.setWarehouse(warehouse);
        inventoryDto.setCompany(company);
        inventoryDto.setLocation(toLocation);
        inventoryDto.setItem(item);
        inventoryDto.setItemKey(itemKey);
        inventoryDto.setPackageUnit(unit);
        inventoryDto.setQty(qty);
        inventoryDto.setPackQty(packQty);
        if(WmsLocationCode.HLC.equals(toLocation.getCode()) && !WmsLocationCode.HLC.equals(inventory.getLocation().getCode())){
        	inventoryDto.setStatus("不良品");
        	isSendSAP = Boolean.TRUE;
        }else if(!WmsLocationCode.HLC.equals(toLocation.getCode()) && WmsLocationCode.HLC.equals(inventory.getLocation().getCode())){
        	inventoryDto.setStatus("合格");
        	isSendSAP = Boolean.TRUE;
        }else{
        	inventoryDto.setStatus(inventory.getStatus());
        }
        inventoryDto.setType(WmsInventoryLogType.MOVE);
        inventoryDto.setDescript(descript);
        inventoryDto.setPallet(inventory.getPallet());
        inventoryDto.setCarton(inventory.getCarton());
        inventoryDto.setPalletSeq(inventory.getPalletSeq());
        
        //移出库位库存扣减
        inventoryManager.out(inventory,qty,WmsInventoryLogType.MOVE,descript);
        //移出库位库存新增
        inventoryManager.in(inventoryDto);
        if(isSendSAP){
        	TclMessageManager tclMessageManager = this.getTclMessageManager();
        	tclMessageManager.sendChangeStatusInfo(inventory, qty, inventoryDto.getStatus());
        }
    }
    /* (non-Javadoc)
     * @see com.vtradex.wms.server.service.inventory.WmsInventoryManageManager#storeInventoryStateAdjustRf(com.vtradex.wms.server.model.entity.inventory.WmsInventory, java.lang.Long, java.lang.Double, java.lang.String)
     */
    @Override
    public void inventoryStateChange(WmsInventory inventory,String changgeStatus, Double packQty, String descript) {
        // TODO Auto-generated method stub
        WmsWarehouse warehouse = inventory.getWarehouse();
        WmsCompany company = inventory.getCompany();
        WmsLocation location = inventory.getLocation();
        WmsItem item = inventory.getItem();
        WmsItemKey itemKey = inventory.getItemKey();
        WmsPackageUnit unit = inventory.getPackageUnit();
//      Double packQty = inventory.getPackQty();
        Double qty = WmsPackageUnitUtils.getQtyBU(unit, packQty, item.getBuPrecision());
        
        //如果库存调整时,更新数量和库存状态
        this.storeSingleInventorySateAdjust(inventory, changgeStatus, warehouse, company, location, item, itemKey, unit, packQty, qty, descript);
    }
    
    
}
