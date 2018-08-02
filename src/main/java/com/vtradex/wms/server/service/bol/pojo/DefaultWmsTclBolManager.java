package com.vtradex.wms.server.service.bol.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.wms.server.enumeration.WarehouseEnumeration;
import com.vtradex.wms.server.model.component.LotInfo;
import com.vtradex.wms.server.model.entity.base.WmsCustomer;
import com.vtradex.wms.server.model.entity.base.WmsFactoryXmlb;
import com.vtradex.wms.server.model.entity.base.WmsItemKeeper;
import com.vtradex.wms.server.model.entity.base.WmsSapFactory;
import com.vtradex.wms.server.model.entity.base.WmsShippingLotTruck;
import com.vtradex.wms.server.model.entity.base.WmsShippingLotTruckBillType;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.model.entity.base.WmsSystemValues;
import com.vtradex.wms.server.model.entity.base.WmsSystemValuesType;
import com.vtradex.wms.server.model.entity.bol.WmsBol;
import com.vtradex.wms.server.model.entity.bol.WmsBolDetail;
import com.vtradex.wms.server.model.entity.bol.WmsBolStatus;
import com.vtradex.wms.server.model.entity.bol.WmsBolType;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.item.WmsBillType;
import com.vtradex.wms.server.model.entity.item.WmsItemUnPackingAtt;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrder;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetailPtDetail;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrder;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderDetail;
import com.vtradex.wms.server.model.entity.receiving.WmsASN;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.entity.receiving.WmsASNStatus;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;
import com.vtradex.wms.server.model.enums.BaseStatus;
import com.vtradex.wms.server.model.enums.WmsBillOfType;
import com.vtradex.wms.server.model.enums.WmsInventoryLogType;
import com.vtradex.wms.server.model.enums.WmsLocationType;
import com.vtradex.wms.server.model.enums.WmsPickTicketStatus;
import com.vtradex.wms.server.model.enums.WmsPickticketBillTypeCode;
import com.vtradex.wms.server.model.enums.WmsPickticketGenType;
import com.vtradex.wms.server.service.bol.WmsTclBolManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.item.TclMessageManager;
import com.vtradex.wms.server.service.message.WmsCustomerManager;
import com.vtradex.wms.server.service.receiving.WmsTclASNManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.utils.BeanUtils;
import com.vtradex.wms.server.utils.DateUtil;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.webservice.utils.Arith;
import com.vtradex.wms.webservice.utils.CommonHelper;

/**
 * 
 * Tcl 定制化Bol业务
 * 
 * @author <a href="zhen.lei@vtradex.com">Yogurt_lei</a>
 * 
 * @date 2017年7月21日 下午12:31:32
 */
public class DefaultWmsTclBolManager extends DefaultWmsBolManager implements WmsTclBolManager {
    
	protected WmsBussinessCodeManager wmsBussinessCodeManager;
	protected WmsTclASNManager wmsTclAsnManager;
	protected TclMessageManager tclMessageManager;
   
	public DefaultWmsTclBolManager(WorkflowManager workflowManager,
            WmsInventoryManager inventoryManager,
            WmsBussinessCodeManager wmsBussinessCodeManager,
            WmsRuleManager wmsRuleManager, WmsCustomerManager wmsCustomerManager,WmsTclASNManager wmsTclAsnManager,TclMessageManager tclMessageManager) {
        super(workflowManager, inventoryManager, wmsBussinessCodeManager,
                wmsRuleManager, wmsCustomerManager);
        this.wmsBussinessCodeManager = wmsBussinessCodeManager;
        this.wmsTclAsnManager = wmsTclAsnManager;
        this.tclMessageManager = tclMessageManager;
    }
	
	
	@SuppressWarnings("unchecked")
	private WmsASN genASNHead(WmsPickTicket wmsPickTicket,WmsWarehouse warehouse) {
		Map<String, Object> map = null;
        try {
            map = wmsRuleManager.getRuleTableDetail("R101_库间调拨入库单据类型规则表", wmsPickTicket.getCompany().getName(),wmsPickTicket.getBillType().getName());
            if(map.isEmpty()){
            	throw new BusinessException("货主:"+wmsPickTicket.getCompany().getName()+"单据类型:"+wmsPickTicket.getBillType().getName()+"R101_库间调拨入库单据类型规则表未找到数据"); 
            }
        } catch(Exception e) {
            throw new BusinessException("warehouse.allocation.rule.none"); 
        }
            
        WmsASN asn =  EntityFactory.getEntity(WmsASN.class);

        String billTypeName = map.get("入库单据类型").toString();
        String find_bill_type_hql = "FROM WmsBillType billType WHERE billType.name=:billTypeName AND billType.masterGroup.id = :masterGroupId";
        List<WmsBillType> wmsBillTypeList = this.commonDao.findByQuery(find_bill_type_hql, new String[]{"billTypeName", "masterGroupId"}, new Object[]{billTypeName, wmsPickTicket.getCompany().getMasterGroup().getId()});
        
        WmsBillType wmsBillType = null;
        if(wmsBillTypeList.size() > 1) {
            throw new BusinessException("please.maintain.the.redundant.bill.type");
        }
        
        if(wmsBillTypeList.size() == 0) {
            throw new BusinessException("please.maintain.the.storage.bill.type");
        }

        wmsBillType = wmsBillTypeList.get(0);
        if(!WmsBillOfType.ASN.equals(wmsBillType.getType())) {
            throw new BusinessException("the.bill.type.must.be.asn");
        }
        
        asn.setBillType(wmsBillType);
        
//        if(null==wmsPickTicket.getCustomer()){
//        	throw new BusinessException("请维护拣货单的收货人!!!");
//        }
        
        String asnCode = wmsBussinessCodeManager.generateCodeByRule(warehouse,asn.getBillType().getCode());
        asn.setCode(asnCode);
        
        WmsWarehouse wh = (WmsWarehouse)commonDao.findByQueryUniqueResult("FROM WmsWarehouse warehouse WHERE warehouse.baseOrganization.id = :baseOrganizationId", 
        		new String[] {"baseOrganizationId"}, new Object[] {BaseOrganizationHolder.getThornBaseOrganization().getId()});

        String locationType = WmsLocationType.RECEIVE;
        if("补货入库单".equals(billTypeName)) { //增加补货入库ASN默认收货库位设置为T-1库位(HANDOVER)
        	locationType = WmsLocationType.HANDOVER;
        }
        
        List<WmsLocation> locs = commonDao.findByQuery("FROM WmsLocation loc WHERE loc.status = 'ENABLED' "
                + "AND loc.type = :locType AND loc.warehouse = :warehouse", 
                new String[]{"locType", "warehouse"}, 
                new Object[]{locationType, warehouse});
        if (locs!=null && locs.size()>0) {
            asn.setReceiveLocation(locs.get(0));
        } else {
            throw new BusinessException("库间调拨未维护【"+locationType+"】类型的库位!");
        }
       
        asn.setStatus(WmsASNStatus.ACTIVE);
        asn.setRelatedBill1(wmsPickTicket.getCode());
        asn.setWarehouse(warehouse);
        asn.setCompany(wmsPickTicket.getCompany());
        asn.setFromCode(wh.getCode());
        asn.setFromName(wh.getName());
        asn.setFromContact(wh.getContact());
        
        this.commonDao.store(asn);
        return asn;
	}
 
	private void genASNDetail(WmsPickTicketDetail pickTicketDetail, WmsASN asn,
			WmsInventory inventory,WmsBolDetail bolDetail,int lineNo,Double pickQty,String proCode) {

        Set<WmsASNDetail> details = asn.getDetails();
        if(details == null) {
        	details = new HashSet<WmsASNDetail>();
        }
        //根据BOL明细生成Asn明细
        WmsASNDetail wmsASNDetail =EntityFactory.getEntity(WmsASNDetail.class);
        wmsASNDetail.setAsn(asn);
        wmsASNDetail.setLineNo(lineNo);
        wmsASNDetail.setItem(bolDetail.getPickTicketDetail().getItem());
        wmsASNDetail.setExpectedPackQty(pickQty);
        wmsASNDetail.setExpectedQty(pickQty);
        wmsASNDetail.setPackageUnit(bolDetail.getPickTicketDetail().getPackageUnit());
        wmsASNDetail.setUserField5(asn.getUserField5());//项目类型
        wmsASNDetail.setInventoryStatus(inventory.getStatus());//库存状态
        
      
        
        LotInfo lotInfo = inventory.getItemKey().getLotInfo();  //批次信息
        LotInfo newLotInfo = new LotInfo();
        BeanUtils.copyEntity(newLotInfo, lotInfo);
        
        
        WmsPickTicket pt = pickTicketDetail.getPickTicket();
        
      //如果是调拨出库，lotInfo需要修改工厂。
        if(WmsBolType.DB.equals(bolDetail.getBol().getType())) {
        	
        	String factorycode=pt.getUserField2();
        	WmsSapFactory factory = null;
        	try{
        		factory = (WmsSapFactory)commonDao.findByQueryUniqueResult("FROM WmsSapFactory factory where factory.code=:fc", "fc",factorycode);
        	}catch(Exception e){
        		throw new BusinessException("根据工厂编码"+factorycode+"找到了多条数据!");
        	}
	        
			if(null==factory){
				throw new BusinessException("工厂"+factorycode+"未找到");
			}
			newLotInfo.setExtendPropC10(factory.getCode()); //工厂编码
			newLotInfo.setExtendPropC11(factory.getName()); //工厂名称
			newLotInfo.setExtendPropC8(asn.getUserField5());//标准 VMI仓到自管仓库存类型要改成标准
			asn.setUserField7(factory.getCode());
        }
        else {
        	if(proCode == null){
        		newLotInfo.setExtendPropC13(pt.getRelatedBill1()); //工单号/线体   VMI交接单入库生成的asndetail需要
        	}else{
        		newLotInfo.setExtendPropC13(proCode); //工单号/线体   VMI交接单入库生成的asndetail需要
        	}
	        /*String hql = "FROM WmsTask task WHERE task.relatedObjId =:relatedObjId ";
	        List<WmsTask> tasks = commonDao.findByQuery(hql, "relatedObjId", pickTicketDetail.getId());*/
	        newLotInfo.setExtendPropC20(bolDetail.getBol().getCode()); //RF创建配送单需要
	        if(pt.getOrderDate() != null) { //订单日期  如果是工单生成的  这个日期是工单的开始生产日期。
	        	newLotInfo.setExtendPropC14(DateUtil.format(pt.getOrderDate() , "yyyyMMdd")); //线体生产日期  VMI交接单入库生成的asndetail需要
	        }
	        newLotInfo.setExtendPropC15(pickTicketDetail.getId()+""); //pickticketdetail_id    VMI交接单入库生成的asndetail需要
        }
        newLotInfo.setExtendPropC6(pt.getUserField7());//线体
        newLotInfo.setExtendPropC7(asn.getCode());//ASN单号
        
        wmsASNDetail.setLotInfo(newLotInfo);
        this.commonDao.store(wmsASNDetail);
        details.add(wmsASNDetail);
        
        WmsSupplier su = null;
        if(!StringHelper.isNullOrEmpty(inventory.getItemKey().getLotInfo().getSupplierCode()) 
        		&& "XN".equals(inventory.getItemKey().getLotInfo().getSupplierCode())){
        	su = pt.getSupplier();
        }else{
        	su = (WmsSupplier)commonDao.findByQueryUniqueResult("FROM WmsSupplier su WHERE su.code=:sc", "sc",inventory.getItemKey().getLotInfo().getSupplierCode());
        }
        if(null!=su){
    		asn.setSupplier(su);
    	}
        asn.setDetails(details);
        asn.refreshQtyBU();
        asn.setOrderDate(new Date());
        this.commonDao.store(asn);
        wmsTclAsnManager.refreshDetail(asn);//刷新标签数量
	}
	/**
	 * 找保管员 先按  物料+仓库+工厂  找不到就按  物料+仓库
	 * @param itemCode
	 * @param warehouseCode
	 * @param factoryCode
	 * @return
	 */
	private ThornUser getThornUser(String itemCode,String warehouseCode,String factoryCode){
        WmsItemKeeper itemKeeper = null;
        String baseWmsItemKeeperHql = "FROM WmsItemKeeper WHERE item.code=:itemCode AND warehouse.code=:warehouseCode";
        List<WmsItemKeeper> wmsItemKeepers = commonDao.findByQuery(baseWmsItemKeeperHql + " AND factory.code=:factoryCode", 
                new String[]{"itemCode", "warehouseCode", "factoryCode"}, 
                new Object[]{itemCode, warehouseCode, factoryCode});
        if (wmsItemKeepers!=null && wmsItemKeepers.size()>0) {
            itemKeeper = wmsItemKeepers.get(0);
        } else {
            wmsItemKeepers = commonDao.findByQuery(baseWmsItemKeeperHql + " AND factory IS NULL", 
                    new String[]{"itemCode", "warehouseCode"},
                    new Object[]{itemCode, warehouseCode});
            if (wmsItemKeepers!=null && wmsItemKeepers.size()>0) {
                itemKeeper = wmsItemKeepers.get(0);
            }
        }
        if(itemKeeper==null){
        	return null;
        }else{
        	return itemKeeper.getKeeper();
        }
	}
	
    @SuppressWarnings("unchecked")
	public void shipBol(WmsBol bol) {
//    	String inventoryHql = "SELECT inventory.id FROM WmsInventory inventory WHERE inventory.location.type =:localtionType AND inventory.operationStatus='READY_OUT' "
//				+ "AND inventory.relatedBillCode =:relatedBillCode AND inventory.item.id =:itemId AND inventory.packQty > 0 AND inventory.qty > 0";
		
		String hql = "SELECT bolDetail.id FROM WmsBolDetail bolDetail WHERE bolDetail.bol.id =:bolId "
				+ "AND ((bolDetail.bol.status =:status and bolDetail.bol.masterBol.id is null) "
				+ "or (bolDetail.bol.status=:status1 and bolDetail.bol.masterBol.id is not null))";
		List<Long> bolDetailIds = commonDao.findByQuery(hql, new String[]{"bolId","status","status1"},
				new Object[]{bol.getId(),WmsBolStatus.ACTIVE,WmsBolStatus.LOADING});
		
		
		
		WmsCustomer customer = null;
		if(bol.getCustomer()!=null) {
			customer = commonDao.load(WmsCustomer.class, bol.getCustomer().getId());
		}
		Boolean gen_asn = false;//是否需要生成asn
		Map<String, WmsASN> asnMap = new HashMap<String, WmsASN>();//key=ASN,value=相关单号
		
		/**销售出库单不生成ASN*/
		Map<ThornUser,List<Long>> keeperMap = new HashMap<ThornUser, List<Long>>();
		if(null!=customer && null!=customer.getWarehouse() 
				&& null != bol.getType() && !bol.getType().equals(WmsBolType.XSCK)){
			gen_asn = true;
			
			//生成ASN时，按照保管员分单，相同保管员的生成一个收货单
			for(Long bolDetailId : bolDetailIds){
				WmsBolDetail bolDetail = commonDao.load(WmsBolDetail.class, bolDetailId);
				ThornUser keeper = getThornUser(bolDetail.getItemKey().getItem().getCode(), customer.getWarehouse().getCode(), bolDetail.getItemKey().getLotInfo().getExtendPropC10());
				if(!keeperMap.containsKey(keeper)){
					List<Long> bdIds = new ArrayList<Long>();
					bdIds.add(bolDetailId);
					keeperMap.put(keeper, bdIds);
				}else{
					keeperMap.get(keeper).add(bolDetailId);
				}
			}
		}
		if(!keeperMap.isEmpty()){
			Set<ThornUser> keys = keeperMap.keySet();
			for(ThornUser keeper : keys){
				this.shipWmsBol(keeperMap.get(keeper), gen_asn, keeper, customer, bol);
			}
		}else{
			this.shipWmsBol(bolDetailIds, gen_asn, null, customer, bol);
		}
//		for(Long bolDetailId : bolDetailIds){}
		
//		bol.setStatus(WmsBolStatus.SHIPPED);
		bol.setActualDeliveryTime(new Date());
		commonDao.store(bol);
	}
    private void shipWmsBol(List<Long> bolDetailIds,Boolean gen_asn,ThornUser keeper,WmsCustomer customer,WmsBol bol){
    	WmsASN asn = null;
    	Map<String, WmsASN> asnMap = new HashMap<String, WmsASN>();//key=ASN,value=相关单号
    	int lineNo =0;
    	String billType = "";
    	for(Long bolDetailId : bolDetailIds){
    		String inventoryHql = "SELECT inventory.id FROM WmsInventory inventory WHERE inventory.location.type =:localtionType AND inventory.operationStatus='READY_OUT' "
    			+ "AND inventory.relatedBillCode =:relatedBillCode AND inventory.item.id =:itemId AND inventory.itemKey.lotInfo.lot=:lot " +
    			" AND inventory.packQty > 0 AND inventory.qty > 0 AND inventory.itemKey.lotInfo.supplierCode=:supplierCode" +
    			" AND inventory.itemKey.lotInfo.extendPropC10 =:extendPropC10";
        	
			WmsBolDetail bolDetail = commonDao.load(WmsBolDetail.class, bolDetailId);
			Double unShipQty = bolDetail.getPickedQty();
			Double dealQty =unShipQty;

			WmsPickTicketDetail pickTicketDetail = commonDao.load(WmsPickTicketDetail.class, bolDetail.getPickTicketDetail().getId());
			WmsPickTicket pickTicket = this.commonDao.load(WmsPickTicket.class,pickTicketDetail.getPickTicket().getId());
			billType = pickTicket.getBillType().getCode();
			//是否允许短缺发运等于否时
			if(pickTicket.getAllowShortShip()!=null&&!pickTicket.getAllowShortShip()){
				//拣货数量大于等于期待数量时允许发运确认，如果小于则不允许发运确认
				if(pickTicket.getPickQty()<pickTicket.getQty()){
					throw new BusinessException("this.pt.can.not.allowShortShip",new String[]{pickTicket.getCode()});
				}
			}
//			Double unShipQty = DoubleUtils.sub(bolDetail.getPlanQty(), bolDetail.getPickedQty(),pickTicketDetail.getItem().getBuPrecision());
			List<Long> inventoryIds = commonDao.findByQuery(inventoryHql, new String[]{"localtionType","relatedBillCode","itemId","lot","supplierCode","extendPropC10"}, 
					new Object[]{WmsLocationType.SHIP,pickTicketDetail.getPickTicket().getCode(),pickTicketDetail.getItem().getId(),
					bolDetail.getItemKey().getLotInfo().getLot(),bolDetail.getItemKey().getLotInfo().getSupplierCode(),bolDetail.getItemKey().getLotInfo().getExtendPropC10()});
		    if(inventoryIds.isEmpty()||inventoryIds.size()<=0){
		    	throw new BusinessException("没有库存无法交接"+bolDetailId);
		    }
//		    String hql ="";
		    List<Long> proptdIds = new ArrayList<Long>();
		    String split = pickTicketDetail.getItem().getUserFieldV3();//拆包属性
		    if(WmsPickticketGenType.SCLLD.equals(billType)
		    		&& !StringHelper.isNullOrEmpty(split)
					&& WmsItemUnPackingAtt.WAREHOUSE_UNPACKING.equals(split)){//生产领料拣货单判断拣货明细对应几个工单,根据工单拆分生成asn
				String hql = "select p.id from ProductionOrderDetailPtDetail p "
						+ "left join p.productionOrderDetail prod where p.pickticketDetail.id=:id "
						+ "and prod.allocatedQuantityBu-prod.handQty > 0 "
						+ "ORDER BY prod.productionOrder.id, prod.id";
				proptdIds = commonDao.findByQuery(hql,"id",pickTicketDetail.getId());
		    }	
			
		    for(Long inventoryId : inventoryIds){
				if(dealQty <= 0){
					break;
				}
				WmsInventory inventory = commonDao.load(WmsInventory.class, inventoryId);
				Double qty = inventory.getQty();
//				Double invQty = inventory.getQty();
				if(dealQty <  inventory.getQty()){
					qty = dealQty;
					dealQty = 0D;
//					invQty = DoubleUtils.sub(inventory.getQty(),unShipQty,pickTicketDetail.getItem().getBuPrecision());
				}else{
					qty = inventory.getQty();
					dealQty = DoubleUtils.sub(dealQty, inventory.getQty(),pickTicketDetail.getItem().getBuPrecision());
//					invQty = 0D;
				}
//				Double packQty = WmsPackageUnitUtils.getPackQty(inventory.getPackageUnit(), qty, inventory.getItem().getBuPrecision());
//				WmsCustomer customer = commonDao.load(WmsCustomer.class, bol.getCustomer().getId());
//				if(null!=customer&&null!=customer.getWarehouse()){
//					lineNo+=1;
//					warehouseAllocation(pickTicket,inventory,customer.getWarehouse(),bolDetail,lineNo);
//				}
				
				if(gen_asn) {
					lineNo+=1;
					if(WmsPickticketGenType.SCLLD.equals(billType)
							&& !StringHelper.isNullOrEmpty(split)
							&& WmsItemUnPackingAtt.WAREHOUSE_UNPACKING.equals(split)){
						
						Double shipQty =CommonHelper.dealDoubleError(qty);//交接数量
						for(Long proptdId : proptdIds){
							ProductionOrderDetailPtDetail p = commonDao.load(ProductionOrderDetailPtDetail.class, proptdId);
							if(shipQty <= 0){
								break;
							}
							Integer line = 1;//行号 ;
							ProductionOrderDetail pro = commonDao.load(ProductionOrderDetail.class, p.getProductionOrderDetail().getId());
							Double restQty = pro.getRestHandQty()-pro.getOldShippedQuantityBu();//可交接数量
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
								if(DoubleUtils.sub(p.getQuantityBu(), p.getLastAllocatedQty())<0){
									restQty = p.getLastAllocatedQty();
								}else{
									restQty = p.getQuantityBu();
								}
								
							}
							WmsASN wmsASN = null;
							String prCode = pro.getProductionOrder().getCode();//工单号
							if(asnMap.get(prCode) != null){//同一张工单的不同物料新建asn时放一起
								wmsASN = asnMap.get(prCode);
								line = wmsASN.getDetails().size() ;
								line = line == null ? 1 : (line+1);
							}
							
							if(DoubleUtils.sub(restQty, shipQty) >= 0){
								wmsASN = createAsn(wmsASN, pickTicket, customer, bol, pickTicketDetail, inventory, bolDetail, line,shipQty,prCode);
								wmsASN.setCustomerBill(prCode);//记录工单号
								wmsASN.setKeeper(keeper);//保管员
								pro.addHandQty(shipQty);//添加已交接数量
								shipQty = 0d;
							}
							else if(DoubleUtils.sub(restQty,shipQty) < 0){
								wmsASN = createAsn(wmsASN, pickTicket, customer, bol, pickTicketDetail, inventory, bolDetail, line,restQty,prCode);
								wmsASN.setCustomerBill(prCode);//记录工单号
								wmsASN.setKeeper(keeper);//保管员
								pro.addHandQty(restQty);//添加已交接数量
								shipQty =DoubleUtils.sub(shipQty, restQty);
							}
							commonDao.store(wmsASN);
							asnMap.put(prCode, wmsASN);
							
							commonDao.store(pro);
						}
						shipQty = CommonHelper.dealDoubleError(shipQty);
						if(shipQty>0){
							throw new BusinessException("交接出错，数量剩余"+shipQty+"未完全交接"+bolDetail.getId());
						}
					}else{
						asn = createAsn(asn, pickTicket, customer, bol, pickTicketDetail, inventory, bolDetail, lineNo,qty,null);
						asn.setKeeper(keeper);
						commonDao.store(asn);
						asnMap.put("-", asn);
					}
				}
				//因为交接单和出库单调用的都是此方法，只有生成ASN的才需要做交接，其余的发料都是直接就出库的
				if(WmsPickticketGenType.SCLLD.equals(billType)){
					inventoryManager.out(inventory, qty,WmsInventoryLogType.VMI_OUT,bol.getCode());
				}else{
					inventoryManager.out(inventory, qty,WmsInventoryLogType.SHIPPING,bol.getCode());
				}
				if(!WmsBolType.VMI.equals(bol.getType())){
					pickTicketDetail.shippedQty(qty);
				}
				
				Map<Long,Double> map = new HashMap<Long, Double>();
				if(!gen_asn) { //不是vmi交接出库  则需要回写预留单或其他单据 的发运数据
					
					map = tclCustomShipped(pickTicketDetail,qty,inventory);
					
				}
				
				commonDao.store(pickTicketDetail);
				WmsPickTicket pick = commonDao.load(WmsPickTicket.class, pickTicketDetail.getPickTicket().getId());
				if(!WmsPickTicketStatus.FINISHED.equals(pick.getStatus())){
					workflowManager.doWorkflow(pick,"wmsPickTicketProcess.ship");
				}
				if(!WmsPickticketBillTypeCode.XSJHD.equals(billType)){
					if(WmsPickticketBillTypeCode.TGYSCK.equals(billType) 
							&& BaseStatus.DISABLED.equals(pickTicket.getSupplier().getStatus())){
						throw new BusinessException("供应商已失效不能出库");
					}
					tclMessageManager.sendProductionShipInfo(pickTicketDetail.getId(), inventory, qty,map);// 销售交货单不放在库存下循环生成台账
				}
				commonDao.store(bolDetail);
			}
		    if(dealQty>0d) {
				throw new BusinessException("交接出错，数量剩余"+dealQty+"未完全匹配"+bolDetail.getId());
			}
		}
    	Iterator it = asnMap.values().iterator();
		//如果创建了ASN,将ASN自动收货,不创建上架单
		while(it.hasNext()){
			WmsASN wmsASN = (WmsASN) it.next();
			if(WmsPickticketGenType.SCLLD.equals(billType)){
				wmsTclAsnManager.receiveAll(wmsASN.getId(),-1L);/**整单收货,保管员传-1是为了标识这个asn是VMI入库*/
			}else{
				wmsTclAsnManager.receiveAll(wmsASN.getId(),0L);
			}
		}
    	if(WmsPickticketBillTypeCode.XSJHD.equals(billType)){
			tclMessageManager.sendOutDeliveryShipInfo(bol);
		}
    }
    
    private Map<Long,Double> tclCustomShipped(WmsPickTicketDetail pickTicketDetail, Double pickedQty,WmsInventory inventory) {
        
    	WmsPickTicket pt = pickTicketDetail.getPickTicket();
//    	if(pt.getBillType().getCode().equals(WmsBillTypeCode.SCLLD)) {
//	        List<Long> podIds =  commonDao.findByQuery("SELECT pod.id FROM ProductionOrderDetailPtDetail pt "
//	                + "LEFT JOIN pt.productionOrderDetail pod "
//	                + "WHERE pt.pickticketDetail.id=:ptID AND pod.allocatedQuantityBu-pod.pickedQuantityBu>0"
//	                + "ORDER BY pod.productionOrder.id, pod.id", 
//	                    "ptID", pickTicketDetail.getId());
//	        
//	        for (Long podId : podIds) {
//	            ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, podId);
//	            Double unPickedQty = pod.getUnPickedQty();
//	
//	            if (pickedQty <= 0) {
//	                break;
//	            }
//	            
//	            if (unPickedQty>pickedQty) {
//	                System.out.println("作业单作业确认修改工单明细拣货数量("+podId+")"+ pickedQty);
//	                pod.addPickedQuantityBu(pickedQty);
//	                pickedQty = 0D;
//	            } else {
//	                System.out.println("作业单作业确认修改工单明细拣货数量("+podId+")"+ unPickedQty);
//	                pod.addPickedQuantityBu(unPickedQty);
//	                pickedQty = DoubleUtils.sub(pickedQty, unPickedQty);
//	            }
//	            
//	        }
//    	}
//    	else 
    	Map<Long,Double> map = new HashMap<Long, Double>();//key-预留明细ID,value-数量
    	if(WmsPickticketGenType.YLCKD.equals(pt.getUserField3())){ //预留出库单
    		List<Long> podIds =  commonDao.findByQuery("SELECT pod.id FROM ReservedOrderDetailPtDetail pt "
	                + "LEFT JOIN pt.reservedOrderDetail pod "
	                + "WHERE pt.pickticketDetail.id=:ptID AND pod.pickedQuantityBu-pod.shippedQuantityBu>0"
	                + "ORDER BY pod.reservedOrder.id, pod.id", 
	                    "ptID", pickTicketDetail.getId());
	        
	        for (Long podId : podIds) {
	        	WmsReservedOrderDetail pod = commonDao.load(WmsReservedOrderDetail.class, podId);
	            Double unPickedQty = pod.getUnShippedQty();
	
	            if (pickedQty <= 0) {
	                break;
	            }
	            
	            Double truckQty = 0d;
	            if (unPickedQty>pickedQty) {
	                System.out.println("作业单作业确认修改预留明细发运数量("+podId+")"+ pickedQty);
	                pod.addShippedQuantityBu(pickedQty);
	                if(!map.containsKey(podId)){
	                	map.put(podId, pickedQty);
	                }else{
	                	map.put(podId, map.get(podId)+pickedQty);
	                }
	                truckQty =pickedQty;
	                pickedQty = 0D;
	            } else {
	                System.out.println("作业单作业确认修改预留明细发运数量("+podId+")"+ unPickedQty);
	                pod.addShippedQuantityBu(unPickedQty);
	                if(!map.containsKey(podId)){
	                	map.put(podId, unPickedQty);
	                }else{
	                	map.put(podId, map.get(podId)+unPickedQty);
	                }
	                truckQty =unPickedQty;
	                pickedQty = DoubleUtils.sub(pickedQty, unPickedQty);
	            }
	            
	          //增加跟踪信息。
	            WmsShippingLotTruck t = EntityFactory.getEntity(WmsShippingLotTruck.class);
                t.setBillType(WmsShippingLotTruckBillType.YLCKD);
                t.setCompany(inventory.getCompany());
                t.setInventoryStatus(inventory.getStatus());
                t.setItem(inventory.getItem());
                t.setLineNo(pod.getLineNo()+"");
                t.setLocation(inventory.getLocation());
                t.setLotInfo(inventory.getItemKey().getLotInfo());
                t.setPackageUnit(inventory.getPackageUnit());
                t.setQty(truckQty);
                t.setRealteId(pod.getReservedOrder().getId());
                t.setRelateCode(pod.getReservedOrder().getCode());
                t.setSubRelateId(pod.getId());
                t.setWarehouse(inventory.getWarehouse());
                commonDao.store(t);
	            
	        }
	        if(CommonHelper.dealDoubleError(pickedQty)>0d) {
	        	throw new BusinessException("PTDID:"+pickTicketDetail.getId()+"发运数量剩余"+CommonHelper.dealDoubleError(pickedQty)+"未回写到预留单");
	        }
    	}
    	else if(WmsPickticketGenType.XSJHD.equals(pt.getUserField3())){ //销售交货单  发货类型
    		List<Long> podIds =  commonDao.findByQuery("SELECT pod.id FROM DeliveryOrderDetailPtDetail pt "
	                + "LEFT JOIN pt.deliveryOrderDetail pod "
	                + "WHERE pt.pickticketDetail.id=:ptID AND pod.planQuantityBu-pod.delivedQuantityBu>0"
	                + "ORDER BY pod.deliveryOrder.id, pod.id", 
	                    "ptID", pickTicketDetail.getId());
	        
	        for (Long podId : podIds) {
	        	WmsDeliveryOrderDetail pod = commonDao.load(WmsDeliveryOrderDetail.class, podId);
	            Double unPickedQty = pod.getPlanQuantityBu()-pod.getDelivedQuantityBu();
	
	            if (pickedQty <= 0) {
	                break;
	            }
	            
	            Double truckQty = 0d;
	            if (unPickedQty>pickedQty) {
	                System.out.println("作业单作业确认修改销售交货单已交货数量("+podId+")"+ pickedQty);
	                pod.addDelivedQuantityBu(pickedQty);
	                pod.subTheDeliveryQuantityBu(pickedQty);
	                truckQty =pickedQty;
	                pickedQty = 0D;
	            } else {
	                System.out.println("作业单作业确认修改销售交货单已交货数量("+podId+")"+ unPickedQty);
	                pod.addDelivedQuantityBu(unPickedQty);
	                pod.subTheDeliveryQuantityBu(unPickedQty);
	                truckQty =unPickedQty;
	                pickedQty = DoubleUtils.sub(pickedQty, unPickedQty);
	            }
	            
	          //增加跟踪信息。
	            WmsShippingLotTruck t = EntityFactory.getEntity(WmsShippingLotTruck.class);
                t.setBillType(WmsShippingLotTruckBillType.XSJHD);
                t.setCompany(inventory.getCompany());
                t.setInventoryStatus(inventory.getStatus());
                t.setItem(inventory.getItem());
                t.setLineNo(pod.getLineNo()+"");
                t.setLocation(inventory.getLocation());
                t.setLotInfo(inventory.getItemKey().getLotInfo());
                t.setPackageUnit(inventory.getPackageUnit());
                t.setQty(truckQty);
                t.setRealteId(pod.getDeliveryOrder().getId());
                t.setRelateCode(pod.getDeliveryOrder().getCode());
                t.setSubRelateId(pod.getId());
                t.setWarehouse(inventory.getWarehouse());
                commonDao.store(t);
	            
	        }
	        if(CommonHelper.dealDoubleError(pickedQty)>0d) {
	        	throw new BusinessException("PTDID:"+pickTicketDetail.getId()+"发运数量剩余"+CommonHelper.dealDoubleError(pickedQty)+"未回写到销售交货单");
	        }
    	}
        return map;
    }
 
    /**
     * 原有流程增加发货单发运后生成的补货入库ASN默认收货库位设置为T-1库位(HANDOVER)
     */
    @SuppressWarnings("unchecked")
    public void warehouseAllocation(WmsPickTicket wmsPickTicket,WmsInventory inventory,WmsWarehouse warehouse,WmsBolDetail bolDetail,int lineNo) {

        Map<String, Object> map = null;
        try {
            map = wmsRuleManager.getRuleTableDetail("R101_库间调拨入库单据类型规则表", wmsPickTicket.getCompany().getName(),wmsPickTicket.getBillType().getName());
        } catch(Exception e) {
            throw new BusinessException("warehouse.allocation.rule.none"); 
        }
            
        WmsASN asn = new WmsASN();

        String billTypeName = map.get("入库单据类型").toString();
        String find_bill_type_hql = "FROM WmsBillType billType WHERE billType.name=:billTypeName AND billType.masterGroup.id = :masterGroupId";
        List<WmsBillType> wmsBillTypeList = this.commonDao.findByQuery(find_bill_type_hql, new String[]{"billTypeName", "masterGroupId"}, new Object[]{billTypeName, wmsPickTicket.getCompany().getMasterGroup().getId()});
        
        WmsBillType wmsBillType = null;
        if(wmsBillTypeList.size() > 1) {
            throw new BusinessException("please.maintain.the.redundant.bill.type");
        }
        
        if(wmsBillTypeList.size() == 0) {
            throw new BusinessException("please.maintain.the.storage.bill.type");
        }

        wmsBillType = wmsBillTypeList.get(0);
        if(!WmsBillOfType.ASN.equals(wmsBillType.getType())) {
            throw new BusinessException("the.bill.type.must.be.asn");
        }
        
        asn.setBillType(wmsBillType);
        
//        if(null==wmsPickTicket.getCustomer()){
//        	throw new BusinessException("请维护拣货单的收货人!!!");
//        }
        
        String asnCode = wmsBussinessCodeManager.generateCodeByRule(warehouse,asn.getBillType().getCode());
        asn.setCode(asnCode);
        
        WmsWarehouse wh = (WmsWarehouse)commonDao.findByQueryUniqueResult("FROM WmsWarehouse warehouse WHERE warehouse.baseOrganization.id = :baseOrganizationId", new String[] {"baseOrganizationId"}, new Object[] {BaseOrganizationHolder.getThornBaseOrganization().getId()});

        //增加补货入库ASN默认收货库位设置为T-1库位(HANDOVER)
        List<WmsLocation> locs = commonDao.findByQuery("FROM WmsLocation loc WHERE loc.status = 'ENABLED' "
                + "AND loc.type = :locType AND loc.warehouse = :warehouse", 
                new String[]{"locType", "warehouse"}, 
                new Object[]{"HANDOVER", warehouse});
        if (locs!=null && locs.size()>0) {
            asn.setReceiveLocation(locs.get(0));
        } else {
            throw new BusinessException("库间调拨未维护【HANDOVER】类型的库位!");
        }
       
        asn.setStatus(WmsASNStatus.ACTIVE);
        asn.setRelatedBill1(wmsPickTicket.getCode());
        asn.setWarehouse(warehouse);
        asn.setCompany(wmsPickTicket.getCompany());
        asn.setFromCode(wh.getCode());
        asn.setFromName(wh.getName());
        asn.setFromContact(wh.getContact());
        
        this.commonDao.store(asn);
        
        
        Set<WmsASNDetail> details = new HashSet<WmsASNDetail>();
        //根据BOL明细生成Asn明细
        WmsASNDetail wmsASNDetail =EntityFactory.getEntity(WmsASNDetail.class);
        wmsASNDetail.setAsn(asn);
        wmsASNDetail.setLineNo(lineNo);
        wmsASNDetail.setItem(bolDetail.getPickTicketDetail().getItem());
        wmsASNDetail.setExpectedPackQty(bolDetail.getPickTicketDetail().getExpectedPackQty());
        wmsASNDetail.setExpectedQty(bolDetail.getPickTicketDetail().getExpectedQty());
        wmsASNDetail.setPackageUnit(bolDetail.getPickTicketDetail().getPackageUnit());
        wmsASNDetail.setUserField5(WmsFactoryXmlb.BZ);//项目类型
        wmsASNDetail.setInventoryStatus(inventory.getStatus());//库存状态
        wmsASNDetail.setLotInfo(inventory.getItemKey().getLotInfo());//批次信息
        this.commonDao.store(wmsASNDetail);
        details.add(wmsASNDetail);
        
        WmsSupplier su = null;
        if(!StringHelper.isNullOrEmpty(inventory.getItemKey().getLotInfo().getSupplierCode())){
        	su = (WmsSupplier)commonDao.findByQueryUniqueResult("FROM WmsSupplier su WHERE su.code=:sc", "sc",inventory.getItemKey().getLotInfo().getSupplierCode());
        	if(null!=su){
        		asn.setSupplier(su);
        	}
        }
        asn.setDetails(details);
        asn.refreshQtyBU();
        asn.setOrderDate(new Date());
        this.commonDao.store(asn);
        wmsTclAsnManager.refreshDetail(asn);//刷新标签数量
    }
 
    @SuppressWarnings("rawtypes")
    public void addBOLDetail(Long bolId,WmsBolDetail detail,List values) {
		try {
			detail=commonDao.load(WmsBolDetail.class, detail.getId());
			double inputQty = new Double(values.get(0).toString().trim());//输入数量
			double pickedC = detail.getPickedQty();//已拣货数量
			WmsBol bol = this.commonDao.load(WmsBol.class, bolId);
			if(!WmsBolStatus.OPEN.equals(bol.getStatus())){
				throw new BusinessException("单据状态不是打开,不能添加明细");
			}
			if(inputQty>pickedC){
				throw new BusinessException("添加数量不能大于已拣货数量!!!");
			}
			if(bol.getDetails().size()>=200){
				throw new BusinessException("当前单据明细数据量过大，请新建一个单据添加");
			}
			//查询ptdeatil是否被加入过
			String hql = "select detail FROM WmsBolDetail detail "
					+ "where detail.workDoc=:workDoc "
					+ "and detail.itemKey.id=:itemKeyId "
					+ "and detail.pickTicketDetail.id=:pickTicketDetailId "
					+ "and detail.isPacking = :isPacking "
					+ "and detail.bol.id=:dbi ";
			
			WmsBolDetail deta = (WmsBolDetail)commonDao.findByQueryUniqueResult(hql, new String[]{"workDoc","itemKeyId","pickTicketDetailId","isPacking","dbi",}, 
					new Object[]{detail.getWorkDoc(),detail.getItemKey().getId(),detail.getPickTicketDetail().getId(),detail.getIsPacking(),bolId});
			
			if(null!=deta){//已经被加入过
				deta.setPickedQty(DoubleUtils.add(deta.getPickedQty(),inputQty));
				deta.setPlanQty(DoubleUtils.add(deta.getPlanQty(), inputQty));
				commonDao.store(deta);
				detail.setPickedQty(DoubleUtils.sub(detail.getPickedQty(), inputQty));
				commonDao.store(detail);
				
				if(detail.getPickedQty()<=0d) {
					commonDao.delete(detail);
				}
				bol.refreshBOLQty();
				refreshBOlWeightAndVolume(bol);
				
			}else{//没有被加入过
				
				if(detail.getPickedQty()==inputQty) {
					detail.setBol(bol);
					bol.addBOLDetail(detail);
					this.commonDao.store(detail);
					
					bol.refreshBOLQty();
					refreshBOlWeightAndVolume(bol);
				}
				else {
					
					WmsBolDetail newBolDetail = EntityFactory.getEntity(WmsBolDetail.class);
					BeanUtils.copyEntity(newBolDetail, detail);
					newBolDetail.setId(null);
					newBolDetail.setPlanQty(inputQty);
					newBolDetail.setPickedQty(inputQty);
					newBolDetail.setItemKey(detail.getItemKey());
					newBolDetail.setBol(bol);
					bol.addBOLDetail(newBolDetail);
					commonDao.store(newBolDetail);
					
					detail.setPlanQty(DoubleUtils.sub(detail.getPlanQty(), inputQty));
					detail.setPickedQty(Arith.sub(detail.getPickedQty(), inputQty));
					commonDao.store(detail);
					
					
					bol.refreshBOLQty();
					refreshBOlWeightAndVolume(bol);
				}
			}
			//bolDetail按ptd来生成的，所以一个bolDetail肯定对应一个ptd
			WmsPickTicketDetail ptDetail = this.commonDao.load(WmsPickTicketDetail.class, detail.getPickTicketDetail().getId());
			ptDetail.addPlanedShipQty(inputQty);
			commonDao.store(ptDetail);
			
			//记录已加入BOL数量到拣货单明细
//			hql = "from WmsTask task where task.workDoc.id=:workDocId and task.itemKey.id=:itemKeyId ";
//			List<WmsTask> tasks = commonDao.findByQuery(hql, new String[]{"workDocId","itemKeyId"}, 
//					new Object[]{detail.getWorkDoc().getId(),detail.getItemKey().getId()});
//			Double qty = inputQty;
//			for(WmsTask task : tasks){
//				WmsPickTicketDetail ptDetail = this.commonDao.load(WmsPickTicketDetail.class, task.getRelatedObjId());
//				if(qty<=0){
//					break;
//				}
//				if(DoubleUtils.sub(ptDetail.getPlanedShipQty(), ptDetail.getShippedQty())>=0
//						&& DoubleUtils.sub(ptDetail.getPickedQty(), ptDetail.getPlanedShipQty())>0){
//					if(ptDetail.getPickedQty()<qty){
//						ptDetail.addPlanedShipQty(ptDetail.getPickedQty());
//						qty=DoubleUtils.sub(qty, ptDetail.getPickedQty());
//					}else{
//						ptDetail.addPlanedShipQty(qty);
//						qty=0D;
//					}
//					this.commonDao.store(ptDetail);
//				}
//			}
		} catch (BusinessException be) {
			logger.error("save.wmsBolDetail.list.error", be);
			throw new BusinessException(be.getMessage());
		}
	}
    
	public void deleteBol(WmsBol bol) {
		// TODO Auto-generated method stub
		
		String hql="select bolDetail.id from WmsBolDetail bolDetail where bolDetail.bol.id=:bolId";
		List<Long> ids=commonDao.findByQuery(hql,"bolId",bol.getId());
		//必须先删除明细  不然会出现页面没刷新的时候，一个人交接了另外一个人又删除了 造成重复添加  xuyan.xia 2017年11月01日17:36:34
		if(!ids.isEmpty()) {
			throw new BusinessException("请先删除明细！");
		}
//		for(Long detailId:ids){
//			WmsBolDetail bolDetail=commonDao.load(WmsBolDetail.class, detailId);
//			this.deleteBOLDetail(bolDetail);
//		}
	}
    
	public void deleteBOLDetail(WmsBolDetail bolDetail) {
    	
    	//查询未添加的是否有
		String hql = "select detail FROM WmsBolDetail detail "
				+ "where 1=1 "
				+ "and detail.itemKey.id=:itemKeyId "
				+ " and detail.pickTicketDetail.id=:pickTicketDetailId "
				+ "and detail.workDoc.id=:workDocId "
				+ "and detail.isPacking = :isPacking "
				+ "and detail.bol  is null ";
		
		WmsBolDetail deta = (WmsBolDetail)commonDao.findByQueryUniqueResult(hql, new String[]{"itemKeyId","pickTicketDetailId","workDocId","isPacking"}, 
				new Object[]{bolDetail.getItemKey().getId(),bolDetail.getPickTicketDetail().getId(),
				bolDetail.getWorkDoc().getId(),bolDetail.getIsPacking()});
		
		WmsBol bol = load(WmsBol.class,bolDetail.getBol().getId());
		if(!WmsBolStatus.OPEN.equals(bol.getStatus())){
			throw new BusinessException("不是打开状态的单据，不允许删除明细");
		}
		if(bol.getPtId()!=null && bol.getPtId()>0L) {
			/**删除bol恢复拣货单原始状态*/
			WmsPickTicket pickTicket = commonDao.load(WmsPickTicket.class,bol.getPtId());
			pickTicket.setStatus(bol.getPickStatus());
			commonDao.store(pickTicket);
		}
		
		
		if(null!=deta){//有未被添加的
			deta.setPlanQty(DoubleUtils.add(deta.getPlanQty(), bolDetail.getPickedQty()));
			deta.setPickedQty(Arith.add(deta.getPickedQty(),bolDetail.getPickedQty()));
			commonDao.store(deta);
			bol.removeDetails(bolDetail);
			
			bol.refreshBOLQty();
			
			refreshBOlWeightAndVolume(bol);
			commonDao.delete(bolDetail);
			
		}else{//没有未被添加的
			
			bol.removeDetails(bolDetail);
			bol.refreshBOLQty();
			this.refreshBOlWeightAndVolume(bol);
			bolDetail.setBol(null);
			this.commonDao.store(bolDetail);
			 
		}
		WmsPickTicketDetail ptDetail = this.commonDao.load(WmsPickTicketDetail.class, bolDetail.getPickTicketDetail().getId());
		ptDetail.movePlanedShipQty(bolDetail.getPickedQty());
		this.commonDao.store(ptDetail);
//		//移出已加入BOL数量到拣货单明细
//		hql = "from WmsTask task where task.workDoc.id=:workDocId and task.itemKey.id=:itemKeyId ";
//		List<WmsTask> tasks = commonDao.findByQuery(hql, new String[]{"workDocId","itemKeyId"}, 
//				new Object[]{bolDetail.getWorkDoc().getId(),bolDetail.getItemKey().getId()});
//		for(WmsTask task : tasks){
//			WmsPickTicketDetail ptDetail = this.commonDao.load(WmsPickTicketDetail.class, task.getRelatedObjId());
//			if(DoubleUtils.sub(ptDetail.getPlanedShipQty(),ptDetail.getShippedQty())>0){
//				ptDetail.movePlanedShipQty(bolDetail.getPickedQty());
//				this.commonDao.store(ptDetail);
//			}
//		}
	}
	
	public void storeBOLDB(WmsBol bol) {
		if(null != bol.getType() && bol.getType().equals(WmsBolType.XSCK)){
			String hql = "select sum(d.deliveryOrderDetail.delivedQuantityBu) from "
					+ "DeliveryOrderDetailPtDetail d where d.pickticketDetail.pickTicket.id=:id";
			Double deliverQty = (Double) commonDao.findByQueryUniqueResult(hql, "id", bol.getPtId());
			if(null != deliverQty && deliverQty > 0){
				throw new BusinessException("此拣货单对应的交货单已经交货了");
			}
		}
		
		this.storeBOL(bol);
		//生成明细。
		
		Long ptid = bol.getPtId();
		
		if(ptid == null || ptid==0L) {
			throw new BusinessException("拣货单不能为空");
		}
		WmsPickTicket wmsPickTicket = commonDao.load(WmsPickTicket.class, ptid);
		if(null != bol.getType() && bol.getType().equals(WmsBolType.XSCK)){
			bol.setPickStatus(wmsPickTicket.getStatus());//拣货单原始状态
			wmsPickTicket.setStatus(WmsPickTicketStatus.PICK_FINISHED);
		}
 
		bol.setExpectedDeliveryTime(new Date());
		bol.setCustomer(wmsPickTicket.getCustomer());
		bol.setContact(wmsPickTicket.getContact());
		bol.setShipToName(wmsPickTicket.getShipToName());
		
		//如果类型为销售出库单
		if(bol.getType().equals(WmsBolType.XSCK)){
			String hql ="From WmsPickTicket w WHERE w.id =:id";
			WmsPickTicket pt = (WmsPickTicket) commonDao.findByQueryUniqueResult(hql, "id", bol.getPtId());
			
			String Hql = "FROM  WmsDeliveryOrder w WHERE w.code =:code";
			WmsDeliveryOrder order = (WmsDeliveryOrder) commonDao.findByQueryUniqueResult(Hql, "code", pt.getRelatedBill1());
            if(order != null){
            	WmsCustomer customer = order.getCustomer();
            	bol.setCustomer(customer);
            }
		}
		
//		this.storeBOL(bol);
		commonDao.store(bol);
		
		//生成bol时过滤掉需要打包的BOL明细
		String getUsableBolDetail = " from WmsBolDetail detail where detail.bol.id is null and detail.pickTicketDetail.id = :id "
				                  + " and detail.isPacking ='N'";
		for(WmsPickTicketDetail wmsPickTicketDetail : wmsPickTicket.getDetails()){
			List<WmsBolDetail> wmsBolDetails = this.commonDao.findByQuery(getUsableBolDetail, "id", wmsPickTicketDetail.getId());
			if(!wmsBolDetails .isEmpty()&&wmsBolDetails.size()>0){
				for(WmsBolDetail wmsBolDetail : wmsBolDetails){
					this.addBOLDetail(bol.getId(), wmsBolDetail);
				}
			}
		}
		if(!bol.getDetails().isEmpty()){
			this.workflowManager.doWorkflow(bol, "wmsBOLProcess.active");
		}
		
	}
	
	 public void storeBOLVMI(WmsBol bol){
		super.storeBOL(bol);
		
		if(bol.getWarehouse() != null && !bol.getWarehouse().getCode().equals(WarehouseEnumeration.VMI)){
			throw new BusinessException("非VMI库不能新建交接单!!");
		}
		if(StringHelper.isNullOrEmpty(bol.getPurchaseOrderCode())){
			throw new BusinessException("工单号不能为空，请选择工单！！！");
		}
	}
	
	 /**VMI交接单发运在自管仓生成ASN*/
	 private WmsASN createAsn(WmsASN asn,WmsPickTicket pickTicket,WmsCustomer customer,
			 WmsBol bol,WmsPickTicketDetail pickTicketDetail,WmsInventory inventory,
			 WmsBolDetail bolDetail,Integer lineNo,Double pickQty,String productCode){
		 if(asn==null) {
				asn = this.genASNHead(pickTicket, customer.getWarehouse());
				asn.setCustomerBill(bol.getCode());
				asn.setUserField7(inventory.getItemKey().getLotInfo().getExtendPropC10());//工厂编码
				if(customer.getWarehouse().getCode().equals(WarehouseEnumeration.VMI) ){
					asn.setUserField5(WmsFactoryXmlb.JS);//项目类别=寄售
				}else{
					asn.setUserField5(WmsFactoryXmlb.BZ);//项目类别=标准
				}
				commonDao.store(asn);
		 }
		this.genASNDetail(pickTicketDetail,asn,inventory,bolDetail,lineNo,pickQty,productCode);
		return asn;
	 }
	 
	 public WmsBolDetail createWmsBolDetail(WmsPickTicketDetail detail, Double qty,
				WmsTask task,Boolean isPacking){
		    //查询是有已有满足条件的bol明细,没有则新建,有则累计数量
			String hql = "select bolDetail.id FROM WmsBolDetail bolDetail WHERE bolDetail.workDoc.id =:workDocId "
					   + " and bolDetail.pickTicketDetail.item.id=:itemId "
					   + " and bolDetail.pickTicketDetail.id=:pickTicketDetailId "
					   + " and bolDetail.itemKey.id=:itemKeyId "
					   + " and bolDetail.isPacking = :isPacking "
					   + " and bolDetail.bol is null ";
			List<Long> bolDetailIds = commonDao.findByQuery(hql,new String[]{"workDocId","itemId","pickTicketDetailId","itemKeyId","isPacking"}
			, new Object[]{task.getWorkDoc().getId(),task.getItem().getId(),detail.getId(),task.getItemKey().getId(),isPacking});
			WmsBolDetail bolDetail;
			if(!bolDetailIds.isEmpty()&&bolDetailIds.size()>0){
					bolDetail = this.commonDao.load(WmsBolDetail.class, bolDetailIds.get(0));
					bolDetail.setPlanQty(bolDetail.getPlanQty()+qty);
					bolDetail.setPickedQty(bolDetail.getPickedQty()+qty);
					commonDao.store(bolDetail);
			}else{
				bolDetail = new WmsBolDetail();
				bolDetail.setPickTicketDetail(detail);
				bolDetail.setPlanQty(qty);
				bolDetail.setPickedQty(qty);
				bolDetail.setItemKey(task.getItemKey());
				bolDetail.setIsPacking(isPacking);
				bolDetail.setWorkDoc(task.getWorkDoc());
				commonDao.store(bolDetail);
	       }
		   return bolDetail;
		}
}