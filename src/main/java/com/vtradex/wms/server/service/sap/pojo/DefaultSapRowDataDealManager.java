package com.vtradex.wms.server.service.sap.pojo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.UpdateInfo;
import com.vtradex.thorn.server.model.interfaceLog.InterfaceLog;
import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.DateUtil;
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.thorn.server.web.security.BussinessModelHolder;
import com.vtradex.wms.server.enumeration.WmsSapFactoryCodeEnum;
import com.vtradex.wms.server.model.component.Contact;
import com.vtradex.wms.server.model.entity.base.MidSurpplierUser;
import com.vtradex.wms.server.model.entity.base.Wms2SapInterfaceLog;
import com.vtradex.wms.server.model.entity.base.WmsCostCenter;
import com.vtradex.wms.server.model.entity.base.WmsCustomer;
import com.vtradex.wms.server.model.entity.base.WmsFactoryWarehouse;
import com.vtradex.wms.server.model.entity.base.WmsFactoryXmlb;
import com.vtradex.wms.server.model.entity.base.WmsItemFactory;
import com.vtradex.wms.server.model.entity.base.WmsSapFactory;
import com.vtradex.wms.server.model.entity.base.WmsSapWarehouse;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.model.entity.email.EmailRecordType;
import com.vtradex.wms.server.model.entity.inventory.TclWmsInventoryLedger;
import com.vtradex.wms.server.model.entity.item.UnitLevel;
import com.vtradex.wms.server.model.entity.item.WmsBillType;
import com.vtradex.wms.server.model.entity.item.WmsCompany;
import com.vtradex.wms.server.model.entity.item.WmsInventoryState;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemJITAtt;
import com.vtradex.wms.server.model.entity.item.WmsItemScanCode;
import com.vtradex.wms.server.model.entity.item.WmsLotRule;
import com.vtradex.wms.server.model.entity.item.WmsMasterGroup;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.order.ConfirmStatus;
import com.vtradex.wms.server.model.entity.order.PurchaseOrder;
import com.vtradex.wms.server.model.entity.order.PurchaseOrderDetail;
import com.vtradex.wms.server.model.entity.order.PurchaseOrderStatus;
import com.vtradex.wms.server.model.entity.order.WmsCheckOrder;
import com.vtradex.wms.server.model.entity.order.WmsCheckOrderStatus;
import com.vtradex.wms.server.model.entity.order.WmsTransportOrderDetail;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrder;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrderStatus;
import com.vtradex.wms.server.model.entity.production.ReservedOrderDetailPtDetail;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrder;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderBillType;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderCreatedType;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderStatus;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrder;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderType;
import com.vtradex.wms.server.model.entity.receiving.WmsASN;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.entity.receiving.WmsASNStatus;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;
import com.vtradex.wms.server.model.entity.workdoc.WmsWorkDoc;
import com.vtradex.wms.server.model.enums.BaseStatus;
import com.vtradex.wms.server.model.enums.WmsAsnGenType;
import com.vtradex.wms.server.model.enums.WmsLocationCode;
import com.vtradex.wms.server.model.enums.WmsPickTicketStatus;
import com.vtradex.wms.server.model.enums.WmsPickticketGenType;
import com.vtradex.wms.server.model.enums.WmsWorkDocStatus;
import com.vtradex.wms.server.service.emailrecord.EmailRecordManager;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogFunction;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogStatus;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogTaskType;
import com.vtradex.wms.server.service.order.WmsPurchaseOrderManager;
import com.vtradex.wms.server.service.production.ProductionOrderManager;
import com.vtradex.wms.server.service.production.WmsDeliveryOrderManager;
import com.vtradex.wms.server.service.receiving.WmsTclASNManager;
import com.vtradex.wms.server.service.sap.SapRowDataDealManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.supplier.WmsSupplierManager;
import com.vtradex.wms.server.service.workdoc.WmsTclWorkDocManager;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.GlobalParamUtils;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.server.utils.WmsPackageUnitUtils;
import com.vtradex.wms.webservice.model.SapInterfaceType;
import com.vtradex.wms.webservice.model.TaskSubscriber;
import com.vtradex.wms.webservice.sap.model.SapCheckOrder;
import com.vtradex.wms.webservice.sap.model.SapCheckOrderArray;
import com.vtradex.wms.webservice.sap.model.SapCostCenter;
import com.vtradex.wms.webservice.sap.model.SapCostCenterArray;
import com.vtradex.wms.webservice.sap.model.SapDeliveryOrder;
import com.vtradex.wms.webservice.sap.model.SapDeliveryOrderArray;
import com.vtradex.wms.webservice.sap.model.SapItem;
import com.vtradex.wms.webservice.sap.model.SapItemArray;
import com.vtradex.wms.webservice.sap.model.SapJSCheckOrder;
import com.vtradex.wms.webservice.sap.model.SapJSCheckOrderArray;
import com.vtradex.wms.webservice.sap.model.SapPo;
import com.vtradex.wms.webservice.sap.model.SapPoArray;
import com.vtradex.wms.webservice.sap.model.SapProductOrder;
import com.vtradex.wms.webservice.sap.model.SapProductOrderArray;
import com.vtradex.wms.webservice.sap.model.SapProductOrderIn;
import com.vtradex.wms.webservice.sap.model.SapProductOrderInArray;
import com.vtradex.wms.webservice.sap.model.SapReservedData;
import com.vtradex.wms.webservice.sap.model.SapReservedDataArray;
import com.vtradex.wms.webservice.sap.model.SapReturnOrderCode;
import com.vtradex.wms.webservice.sap.model.SapReturnOrderCodeArray;
import com.vtradex.wms.webservice.sap.model.SapSaleOutDelivery;
import com.vtradex.wms.webservice.sap.model.SapSaleOutDeliveryArray;
import com.vtradex.wms.webservice.sap.model.SapSupplier;
import com.vtradex.wms.webservice.sap.model.SapSupplierArray;
import com.vtradex.wms.webservice.sap.model.SapWarehouse;
import com.vtradex.wms.webservice.sap.model.SapWarehouseArray;
import com.vtradex.wms.webservice.utils.Arith;
import com.vtradex.wms.webservice.utils.CommonHelper;
import com.vtradex.wms.webservice.utils.EmailHelper;
import com.vtradex.wms.webservice.utils.ExcelHelper;
import com.vtradex.wms.webservice.utils.WebServiceHelper;
import com.vtradex.wms.webservice.utils.XMLHelper;

/**SAP 接口数据处理类*/
public class DefaultSapRowDataDealManager extends DefaultBaseManager implements SapRowDataDealManager {
	
	private WmsSupplierManager supplierManager;
	private WorkflowManager workflowManager;
	private ProductionOrderManager productionOrderManager;
	private WmsBussinessCodeManager wmsBussinessCodeManager;
	
	public DefaultSapRowDataDealManager(WmsSupplierManager supplierManager,WorkflowManager workflowManager,
			ProductionOrderManager productionOrderManager,WmsBussinessCodeManager wmsBussinessCodeManager) {
		this.supplierManager = supplierManager;
		this.workflowManager = workflowManager;
		this.productionOrderManager = productionOrderManager;
		this.wmsBussinessCodeManager = wmsBussinessCodeManager;
	}
    
    public void storeInterfaceLog2(InterfaceLog interfaceLog,String responseXml) {
		this.storeInterfaceLog(interfaceLog, responseXml,"");
	}
    
    public void storeInterfaceLog(InterfaceLog interfaceLog,String responseXml,String errorInfo) {
		interfaceLog.setResponseContent(responseXml);
		interfaceLog.setResponse("");
		interfaceLog.setErrorLog(StringHelper.substring(errorInfo, 255));
		commonDao.store(interfaceLog);
	}
    
	/**处理物料*/
    public void dealSapItem(SapItemArray sapItemArray) {

        WebServiceHelper.println("开始处理sap单条物料信息");
//        String type = sapItemArray.getTYPE();

        SapItem[] items = sapItemArray.getSapItems();
        for (SapItem sapItem : items) {
        	
        	StringHelper.assertNullOrEmpty(sapItem.getMEINS(), "MEINS属性不能为空");
        	StringHelper.assertNullOrEmpty(sapItem.getEKGRP(), "EKGRP属性不能为空");
        	
        	updateWmsItem(sapItem);
		}
        System.out.println("sap单条物料信息处理完成");
    }
    
    /**根据默认获取批次规则*/
    private WmsLotRule getDefaultLotRule() {
    	List<WmsLotRule> lotRules = commonDao.findByQuery("FROM WmsLotRule WHERE name LIKE :name", "name", "%默认%");
    	if(lotRules.isEmpty()) {
    		throw new BusinessException("未找到默认批次规则");
    	}
    	if(lotRules.size()>1) {
    		throw new BusinessException("根据默认找到了"+lotRules.size()+"条批次规则");
    	}
    	return lotRules.get(0);
    }
    /**根据编码获取物料*/
    private WmsItem getItemByCode(String code) {
    	List<WmsItem> items = commonDao.findByQuery("FROM WmsItem WHERE code=:code", "code", code);
    	
    	if(items.isEmpty()) {
    		return null;
    	}
    	if(items.size()>1) {
    		throw new BusinessException("根据编码"+code+"找到了"+items.size()+"条物料");
    	}
    	return items.get(0);
    }
    /**根据编码获取SAP工厂*/
    private WmsSapFactory getSapFactoryByCode(String code){
		  List<WmsSapFactory> factorys = (List<WmsSapFactory>) commonDao.findByQuery("FROM WmsSapFactory WHERE code=:code", 
	              "code", code);
	      if (factorys.isEmpty()) {
	          throw new BusinessException("未维护编码【"+code+"】的SAP工厂信息!");
	      }
          
	      	if(factorys.size()>1) {
	      		throw new BusinessException("根据编码"+code+"找到了"+factorys.size()+"条SAP工厂信息");
	      	}
	      	return factorys.get(0);
    }
    /**判断物料是否能在工厂下使用*/
    private void checkItemFactory(WmsItem item , WmsSapFactory factory) {
    	String hql = "from WmsItemFactory itemf where itemf.item=:item and itemf.factory=:factory";
    	List<WmsItemFactory> ifs = commonDao.findByQuery(hql, new String[]{"item","factory"}, new Object[]{item,factory});
		if (ifs.isEmpty()) {
			throw new BusinessException("物料编码【" + item.getCode() + "】在工厂"+factory.getName()+"下未绑定!");
		}

		if (ifs.size() > 1) {
			throw new BusinessException("物料编码【" + item.getCode() + "】在工厂"+factory.getName()+"下绑定了"+ifs.size()+"条!");
		}
    }
//    /**判断物料是否能在工厂下使用*/
//    private void checkItemFactory(String itemCode , String factoryCode) {
//    	WmsItem item =getItemByCode(itemCode);
//    	WmsSapFactory factory = getSapFactoryByCode(factoryCode);
//    	checkItemFactory(item, factory);
//    }
    /**根据编码获取供应商*/
    private WmsSupplier getSupplierByCode(String code) {
    	List<WmsSupplier> sups = commonDao.findByQuery("FROM WmsSupplier surpplier where surpplier.code=:sc", "sc", code);
    	if(sups.isEmpty()) {
    		return null;
    	}
    	if(sups.size()>1) {
      		throw new BusinessException("根据编码"+code+"找到了"+sups.size()+"条供应商信息");
      	}
    	return sups.get(0);
    }
    /**根据采购单号和行项目获取采购交货单明细*/	
    private WmsDeliveryOrderDetail getDeliveryOrderDetail(String poNo,String poDetailNo,String deliveryOrderCode){
    	String hql = "FROM WmsDeliveryOrderDetail dod WHERE dod.poNo =:poNo AND dod.poDetailNo =:poDetailNo AND dod.deliveryOrder.sapCode=:sapCode ";
        List<WmsDeliveryOrderDetail> details = commonDao.findByQuery(hql, new String[]{"poNo","poDetailNo","sapCode"}, new Object[]{poNo,poDetailNo,deliveryOrderCode});
        if(details.isEmpty()){
        	return null;
        }
        if(details.size()>1){
        	throw new BusinessException("根据采购单号"+poNo+"和采购单行项目"+poDetailNo+"找到了"+details.size()+"条交货单明细");
        }
        return details.get(0);
    }
    /**根据SAP交货单号*/
    private WmsDeliveryOrder getDeliveryOrderBySapCode(String code){
    	List<WmsDeliveryOrder> deliveryOrders = commonDao.findByQuery("FROM WmsDeliveryOrder "
                + "WHERE sapCode=:sapCode ", new String[]{"sapCode"}, new Object[]{code});
    	if(deliveryOrders.isEmpty()){
        	return null;
        }
        if(deliveryOrders.size()>1){
        	throw new BusinessException("根据SAP交货单号"+code+"找到了"+deliveryOrders.size()+"条交货单");
        }
        return deliveryOrders.get(0);
    }
    /**根据采购单号和行项目获取采购明细*/
    private PurchaseOrderDetail getPurchaseOrderDetail(String poCode,String ebelp){
    	String podHql = "FROM PurchaseOrderDetail pod where pod.purchaseOrder.code=:pdc and pod.ebelp=:pe";
        List<PurchaseOrderDetail> pords = commonDao.findByQuery(podHql, new String[]{"pdc","pe"}, new Object[]{poCode,ebelp});
        if(pords.isEmpty()){
        	throw new BusinessException("根据采购单"+poCode+"和行项目"+ebelp+"未找到采购订单明细");
        }
        if(pords.size()>1){
        	throw new BusinessException("根据采购单号"+poCode+"和行项目"+ebelp+"找到了"+pords.size()+"条采购单明细");
        }
        return pords.get(0);
    }
    /**根据物料和单位获取包装*/
    private WmsPackageUnit getWmsPackageUnitByItem(Long itemId,String unit){
    	List<WmsPackageUnit> pkus = commonDao.findByQuery("FROM WmsPackageUnit "
                + "WHERE item.id=:itemId AND unit=:code",  
                new String[]{"itemId", "code"},  new Object[]{itemId, unit});
        if (pkus.isEmpty()) {
            throw new BusinessException("WMS物料ID【"+itemId+"】的包装单位【"+unit+"】未维护");
        }
        if(pkus.size()>1){
        	throw new BusinessException("根据物料ID"+itemId+"和单位"+unit+"找到了"+pkus.size()+"条包装单位信息");
        }
        return pkus.get(0);
    }
    /**获取默认货主*/
    private WmsCompany getDefaultWmsCompany(){
    	List<WmsCompany> companys = commonDao.findByQuery("FROM WmsCompany  WHERE name LIKE :name", "name", "%默认%");
    	if (companys.isEmpty()) {
            throw new BusinessException("未找到默认货主");
        }
        if(companys.size()>1){
        	throw new BusinessException("找到多个默认货主");
        }
        return companys.get(0);
    }
    /**根据供应商ID获取供应商用户关系*/
    private MidSurpplierUser getMidSurpplierUserBySupplierId(Long supplierId){
    	List<MidSurpplierUser> msus = commonDao.findByQuery("FROM MidSurpplierUser msu where msu.sid=:ms", "ms", supplierId);
		if(msus.isEmpty()){
			return null;
		}
		if(msus.size()>1){
			throw new BusinessException("根据供应商ID"+supplierId+"找到多条供应商用户关系");
		}
    	return msus.get(0);
    }
    /**根据编码获取客户*/
    private WmsCustomer getWmsCustomerByCode(String code){
    	String hql = "FROM WmsCustomer cus WHERE cus.code =:code AND cus.status =:status ";
        List<WmsCustomer> customers = commonDao.findByQuery(hql, new String[]{"code","status"}, new Object[]{code,BaseStatus.ENABLED});
        if(customers.isEmpty()){
			return null;
		}
		if(customers.size()>1){
			throw new BusinessException("根据客户编码"+code+"找到多条客户数据");
		}
		return customers.get(0);
    }
    /**获取对账单*/
    private WmsCheckOrder getWmsCheckOrder(String supplierCode,String code){
    	String hql = "FROM WmsCheckOrder o WHERE o.supplier.code =:supplierCode AND o.code=:code ";
		List<WmsCheckOrder> orders = commonDao.findByQuery(hql, new String[]{"supplierCode","code"}, new Object[]{supplierCode,code});
		if(orders.isEmpty()){
			return null;
		}
		if(orders.size()>1){
			throw new BusinessException("根据供应商编码"+supplierCode+"和对账单编码"+code+"找到多条对账单数据");
		}
		return orders.get(0);
    }
    /**获取库存状态*/
    private WmsInventoryState getStateByStatus(String status){
    	String hql = "FROM WmsInventoryState invState WHERE invState.name =:name ";
    	List<WmsInventoryState> states = commonDao.findByQuery(hql, "name", status);
    	if(states.isEmpty()){
    		throw new BusinessException("根据名称"+status+"未找到库存状态数据");
    	}
    	if(states.size()>1){
    		throw new BusinessException("根据名称"+status+"未找到多条库存状态数据");
    	}
		return states.get(0);
    	
    }
    /**处理物料信息*/
	private void updateWmsItem(SapItem sapItem) {
		String code = sapItem.getMATNR();
        WmsItem item = getItemByCode(code);
        WmsLotRule lotRule = getDefaultLotRule();
        
        if (null==item) {
            item = EntityFactory.getEntity(WmsItem.class);
            item.setCode(code);
        }
        try {
            item.setBarCode(sapItem.getMATNR());
            item.setName(sapItem.getMAKTX());
            item.setLotRule(lotRule);
            item.setCountLotRule(lotRule);
            item.setBaseUnit(sapItem.getMEINS());
            item.setDescription(sapItem.getWGBEZ());
            item.setUserFieldV4(sapItem.getEKGRP());//采购组
            item.setUserFieldV6(sapItem.getMTART());//物料类型
            item.setUserFieldV7(sapItem.getMMSTA());//跨工厂物料状态
            item.setUserFieldV8(sapItem.getMATKL());//物料组
            item.setUserFieldV9(sapItem.getEKNAM());//采购组描述
            if(StringHelper.isNullOrEmpty(item.getAllocationGroup())){
            	item.setAllocationGroup("普通");
            }
            if(StringHelper.isNullOrEmpty(item.getPutawayGroup())){
            	item.setPutawayGroup("普通");
            }
            if(sapItem.getMATKL().startsWith("2106") || sapItem.getMATKL().startsWith("3106")
            		|| sapItem.getMATKL().startsWith("3206") || sapItem.getMATKL().startsWith("3306")){
            	item.setUserFieldV10(WmsItemScanCode.SCANCODE_YES);
            }else{
            	item.setUserFieldV10(WmsItemScanCode.SCANCODE_NO);
            }
            if (item.isNew()) {
                item.setMasterGroup(lotRule.getMasterGroup());
                WmsPackageUnit packageUnit = EntityFactory.getEntity(WmsPackageUnit.class);
        		//设置数字1为默认的拆箱级别
        		packageUnit.setConvertFigure(1D);
        		packageUnit.setUnit(item.getBaseUnit());
        		packageUnit.setUnitLevel(UnitLevel.A);
        		packageUnit.setWeight(item.getWeight());
        		packageUnit.setVolume(item.getVolume());
        		packageUnit.setDescription(sapItem.getMSEHT());
        		item.addPackageUnit(packageUnit);
//        		workflowManager.doWorkflow(item, "itemProcess.new");
            }else{
            	String hql = "from WmsPackageUnit u where u.item.id=:itemId ";
            	WmsPackageUnit u = (WmsPackageUnit) commonDao.findByQueryUniqueResult(hql, "itemId", item.getId());
            	if(u==null){
            		throw new BusinessException("物料"+item.getCode()+"未维护包装单位");
            	}
            	u.setUnit(item.getBaseUnit());
            	u.setWeight(item.getWeight());
            	u.setVolume(item.getVolume());
            	u.setDescription(sapItem.getMSEHT());
            	commonDao.store(u);
            }
            item.setBuPrecision(5);
            commonDao.store(item);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException("WMS系统生成物料信息异常!");
        }
        
        WmsSapFactory factory =getSapFactoryByCode(sapItem.getWERKS()); 
        List<WmsItemFactory> itemFactorys = ( List<WmsItemFactory>) commonDao.findByQuery("FROM WmsItemFactory "
                + "WHERE item.id=:itemId AND factory.id=:factoryId", 
                new String[]{"itemId", "factoryId"}, new Object[]{item.getId(), factory.getId()});
        
        
        if(null != sapItem.getLVORM1() && "X".equals(sapItem.getLVORM1())){//工厂删除标识 只删除对应的工厂和物料绑定关系
        	for(WmsItemFactory wmsItemFactory : itemFactorys){
        		commonDao.delete(wmsItemFactory);
        	}
        }
        
        if(null != sapItem.getLVORM() && "X".equals(sapItem.getLVORM())){//集团删除标识  把物料失效 并把该物料所有的对应关系都删掉
        	item.setStatus(BaseStatus.DISABLED);
        	List<WmsItemFactory> wmsItemFactorys = ( List<WmsItemFactory>) commonDao.findByQuery("FROM WmsItemFactory "
                    + "WHERE item.id=:itemId ", 
                    new String[]{"itemId" }, new Object[]{item.getId()});
        	for(WmsItemFactory wmsItemFactory : wmsItemFactorys){
        		commonDao.delete(wmsItemFactory);
        	}
        }
        
        if(sapItem.getLVORM() == null && sapItem.getLVORM1() == null){
        	if (itemFactorys.isEmpty()) {
            	WmsItemFactory itemFactory = EntityFactory.getEntity(WmsItemFactory.class);
                itemFactory.setItem(item);
                itemFactory.setFactory(factory);
                commonDao.store(itemFactory);
            }
        }
	}

	
	/**处理仓库*/
    public void dealSapWarehouse(SapWarehouseArray sapWarehouseArray) {

        System.out.println("开始处理sap单条仓库信息");
        SapWarehouse[] sws = sapWarehouseArray.getSapWarehouses();
//        String type = sapWarehouseArray.getType();//类型
        for(SapWarehouse warehouse : sws) {
        	StringHelper.assertNullOrEmpty(warehouse.getWERKS(), "WERKS属性不能为空");
        	updateWmsWarehouse(warehouse);
        }
    }
    /**更新仓库信息*/
	private void updateWmsWarehouse(SapWarehouse warehouse) {
		WmsSapWarehouse house = (WmsSapWarehouse)commonDao.findByQueryUniqueResult("FROM WmsSapWarehouse house where house.code=:hc", "hc", warehouse.getLGORT());;
    	if(house==null){
    		//新增
    		house = EntityFactory.getEntity(WmsSapWarehouse.class);
    	}
        WmsSapFactory factory = this.getSapFactoryByCode(warehouse.getWERKS());
        house.setSapFactory(factory);
        house.setCode(warehouse.getLGORT());
        house.setName(warehouse.getLGOBE());
        commonDao.store(house);
	}

	/**处理供应商*/
    public void dealSapSupplier(SapSupplierArray sapSupplierArray) {
        System.out.println("开始处理sap单条供应商信息");
        SapSupplier[] suppliers = sapSupplierArray.getSapSuppliers();
   
    	for(SapSupplier sapSupplier : suppliers) {
    		StringHelper.assertNullOrEmpty(sapSupplier.getLIFNR(), "LIFNR属性不能为空");
    		StringHelper.assertNullOrEmpty(sapSupplier.getSMTP_ADDR1(), "SMTP_ADDR1属性不能为空");
    		
    		WmsSupplier supplier = this.getSupplierByCode(sapSupplier.getLIFNR());
    		if(null!=sapSupplier.getLOEVM()){ //删除
    			if(supplier!=null) {
	    			 try {
		   				  supplierManager.unActiveSurpplier(supplier);
		   			} catch (Exception e) {
		   				throw new BusinessException("供应商删除失败!!编码为:"+supplier.getCode());
		   			}
    			}
    		}
    		else {//新增修改
	    		if(supplier ==null){//新增
	    			supplier = EntityFactory.getEntity(WmsSupplier.class);
	    		}
	    		try {
	    			updateWmsSupplier(supplier,sapSupplier);
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    			throw new BusinessException("供应商处理失败!编码为:"+supplier.getCode()+StringHelper.substring(CommonHelper.getErrorMessageByException(e), 255));
	    		}
    		}
    	}
       
    }
    /**处理供应商*/
	private void updateWmsSupplier(WmsSupplier wmsSupplier,SapSupplier sapSupplier) {
			  wmsSupplier.setCode(sapSupplier.getLIFNR());
			  wmsSupplier.setName(sapSupplier.getNAME1());
			  wmsSupplier.setStatus(BaseStatus.ENABLED);
			  
			  Contact contact = new Contact();
			  contact.setTelephone(StringHelper.isNullOrEmpty(sapSupplier.getTELF1()) ?"":sapSupplier.getTELF1());
			  contact.setMobile(StringHelper.isNullOrEmpty(sapSupplier.getTELF2()) ?"":sapSupplier.getTELF2());
			  contact.setPostCode(sapSupplier.getPSTLZ());
			  
			  wmsSupplier.setContact(contact);
			  wmsSupplier.setAccountGroup(sapSupplier.getKTOKK());
			  wmsSupplier.setSearchStr(sapSupplier.getSORTL());
			  wmsSupplier.setEmail1(sapSupplier.getSMTP_ADDR1());
			  wmsSupplier.setEmail2(sapSupplier.getSMTP_ADDR2());
			  wmsSupplier.setEmail3(sapSupplier.getSMTP_ADDR3());
			  supplierManager.storeSurpplier(wmsSupplier);
	}
	/**处理成本中心*/
	public void dealSapCostCenter(SapCostCenterArray sccs){
		System.out.println("开始成本中心主数据................");
//		String type = sccs.getTYPE();
		for(SapCostCenter scc : sccs.getSccs()){
			StringHelper.assertNullOrEmpty(scc.getKOSTL(), "KOSTL属性不能为空");
			StringHelper.assertNullOrEmpty(scc.getDATBI(), "DATBI属性不能为空");
			//格式判断
			try {
				DateUtil.getDate(scc.getDATBI(),"yyyyMMdd");
			}
			catch(Exception e) {
				throw new BusinessException(scc.getDATBI()+"转换成日期yyyyMMdd失败");
			}
			
			List<WmsCostCenter> costs = commonDao.findByQuery("FROM WmsCostCenter cost WHERE cost.code =:code ", "code", scc.getKOSTL());
			WmsCostCenter cost = null;
	    	if(costs.isEmpty()) {
	    		cost = EntityFactory.getEntity(WmsCostCenter.class);
	    	}
	    	if(costs.size()>1) {
	      		throw new BusinessException("根据编码"+scc.getKOSTL()+"找到了"+costs.size()+"条成本中心");
	      	}
	    	if(costs.size()==1) {
	    		cost = costs.get(0);
	    	}
	    	saveOrUpdateCostCenter(scc, cost);
		}
	}
	/**处理成本中心*/
	private void saveOrUpdateCostCenter(SapCostCenter scc,WmsCostCenter cost){
		if(DateUtil.formatDate(scc.getDATBI()).after(new Date())){
			cost.setCode(scc.getKOSTL());
			cost.setName(scc.getKTEXT());
			if(scc.getBKZKP()==null){
				cost.setFreezeFlag("-");
			}else{
				cost.setFreezeFlag(scc.getBKZKP());
			}
			cost.setXxpirationDate(DateUtil.formatDate(scc.getDATBI()));
			commonDao.store(cost);
		}
	}
	
	/**采购交货单信息*/
	public void dealSapDeliveryOrder(SapDeliveryOrderArray spoas) {
		Map<String,WmsSupplier> supplierMap = new HashMap<String, WmsSupplier>();
        System.out.println("开始处理sap单条采购交货单信息");
        String type = spoas.getTYPE();
        int i = 0;
        for(SapDeliveryOrder order : spoas.getSpoas()){
        	CommonHelper.log("开始处理Sap采购交货单:明细"+i);
        	StringHelper.assertNullOrEmpty(order.getPOSNR(), "POSNR属性不能为空");
        	StringHelper.assertNullOrEmpty(order.getLFDAT(), "LFDAT属性不能为空");
        	WmsDeliveryOrder deliveryOrder = this.getDeliveryOrderBySapCode(order.getVBELN());
        	i++;
        	try {
        		if(deliveryOrder ==null){
        			if("D".equals(type)){
                		return;
                	}
            		deliveryOrder = EntityFactory.getEntity(WmsDeliveryOrder.class);
            	}
        		//类型为D 代表SAP删除交货单，WMS系统只是把相应的数量改成0
                saveOrUpdateDeliveryOrder(order, deliveryOrder, type,i);
                //一个交货单发一个邮件
                //20171218已改，改成定时任务早晚12点触发，一个供应商发一封
//                String key = order.getVBELN();
//                if(!supplierMap.containsKey(key)){
//                	WmsSupplier supplier = this.getSupplierByCode(order.getLIFNR());
//                    if (supplier==null) {
//                        throw new BusinessException("WMS供应商【"+order.getLIFNR()+"】未维护");
//                    }
//                	supplierMap.put(key, supplier);
//                	createEmailRecord(supplier, order);//生成邮件
//                }
               /* else if ("D".equals(type)) { //删除
                	try {
                		List<WmsDeliveryOrderDetail> details = commonDao.findByQuery(" FROM WmsDeliveryOrderDetail d WHERE d.deliveryOrder.sapCode=:sapCode AND d.deliveryOrder.project=:project ",  new String[]{"sapCode","project"}, new Object[]{order.getVBELN(),order.getPOSNR()});
                		for(WmsDeliveryOrderDetail detail : details){
                			if(detail.getDelivedQuantityBu() > 0){
                				throw new BusinessException("已有交货数量，不允许删除");
                			}
                			commonDao.delete(detail);
                		}
                		if(deliveryOrder !=null ){
                			commonDao.delete(deliveryOrder);
                		}
					} catch (Exception e) {
						e.printStackTrace();
						throw new BusinessException("交货单删除失败!!");
					}
                }*/
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new BusinessException("类型:"+type + ",sap单条采购交货单处理失败。"+StringHelper.substring(CommonHelper.getErrorMessageByException(ex), 255));
            }
        }
    }
	
	private void saveOrUpdateDeliveryOrder(SapDeliveryOrder order, WmsDeliveryOrder deliveryOrder, String type,int i) {
		
        deliveryOrder.setSapCode(order.getVBELN());
        if(deliveryOrder.getCode()==null){
        	deliveryOrder.setCode(order.getVBELN());
        }
        deliveryOrder.setStatus(WmsDeliveryOrderStatus.OPEN);
        //供应商
        WmsSupplier supplier = this.getSupplierByCode(order.getLIFNR());
        if (supplier==null) {
            throw new BusinessException("WMS供应商【"+order.getLIFNR()+"】未维护");
        }
//        createEmailRecord(supplier, order);//生成邮件
        deliveryOrder.setSupplier(supplier);
        if(StringHelper.isNullOrEmpty(order.getLFDAT())){
        	throw new BusinessException("交货日期字段不能为空");
        }
        deliveryOrder.setDeliveryDate(DateUtil.formatDate(order.getLFDAT()));
        deliveryOrder.setCreatedType(WmsDeliveryOrderCreatedType.HAND);
        deliveryOrder.setProject(order.getPOSNR());
        WmsDeliveryOrderDetail detail= this.getDeliveryOrderDetail(order.getVGBEL(),order.getVGPOS(),order.getVBELN());
        if (detail == null) {
            UpdateInfo updateInfo = new UpdateInfo();
            updateInfo.setCreatedTime(new Date());
            updateInfo.setCreator(order.getERNAM());
            deliveryOrder.setUpdateInfo(updateInfo);
            deliveryOrder.setBillTypeName(WmsDeliveryOrderBillType.CGBILLTYPE);
            deliveryOrder.setConfirmStatus(ConfirmStatus.OPEN);
            detail = EntityFactory.getEntity(WmsDeliveryOrderDetail.class);
            detail.setLineNo(i); // 明细行号 当前单明细
            detail.setPosnr(order.getPOSNR());
            updateDoDetail(detail,deliveryOrder,order);
        } else {
            deliveryOrder.getUpdateInfo().setLastOperator(order.getERNAM());
            deliveryOrder.getUpdateInfo().setUpdateTime(new Date());
            if(ConfirmStatus.CONFIRM.equals(deliveryOrder.getConfirmStatus())||ConfirmStatus.RECEIVED.equals(deliveryOrder.getConfirmStatus())){
            	deliveryOrder.setConfirmStatus(ConfirmStatus.OPEN);
            }
            if("D".equals(type)){ //类型为D 代表SAP删除交货单，WMS系统只是把相应的数量改成0
            	String hql ="FROM WmsTransportOrderDetail t WHERE t.deliveryOrderDetail.deliveryOrder.code =:dCode ";
            	List<WmsTransportOrderDetail> wtods = commonDao.findByQuery(hql, "dCode", order.getVBELN());
            	if(!wtods.isEmpty()){
            		throw new BusinessException("已加入配货单此交货单不能删除");
            	}
            	//先回写采购单明细分配数量 再把交货单数量改成0
            	PurchaseOrderDetail poDetail = commonDao.load(PurchaseOrderDetail.class, detail.getPurchaseOrderDetail().getId());
            	poDetail.subAllotQty(detail.getTheDeliveryQuantityBu());
            	poDetail.getPurchaseOrder().refreshAllotQty();
            	commonDao.store(poDetail);
            	
            	detail.setPlanQuantityBu(0D);
            	detail.setDelivedQuantityBu(0D);
            	detail.setTheDeliveryQuantityBu(0D);
            	detail.setQuantity(0D);
            	commonDao.store(detail);
            }else{
            	
            
            //如果是收货完成
//            if(WmsDeliveryOrderStatus.FINISH.equals(deliveryOrder.getStatus())){
//            	throw new BusinessException("交货单已收货完成修改失败!!!");
//            }else{
//            	if(detail.getDelivedQuantityBu() > 0){
//        			throw new BusinessException("已交货不允许修改");
//        		} 先注释掉 不做控制允许SAP修改
        		//交货单数量增加
        		Double adjustQty = Arith.sub(Double.parseDouble(order.getLFIMG()),detail.getPlanQuantityBu());
        		if(adjustQty > 0){
        			//获取PO明细单数量
        			PurchaseOrderDetail pod = detail.getPurchaseOrderDetail();
        			String hql = "SELECT SUM(dod.planQuantityBu) FROM WmsDeliveryOrderDetail dod WHERE dod.purchaseOrderDetail.id =:purchaseOrderDetailId  ";
        			Double allDodQty = (Double) commonDao.findByQueryUniqueResult(hql, new String[]{"purchaseOrderDetailId"}, new Object[]{pod.getId()});
        			
        			if(Arith.sub(allDodQty+adjustQty,pod.getExpectedQty())>0){
        				//增加数量大于PO单的收货数量
        				throw new BusinessException("修改数量大于采购订单计划数量请先修改采购订单数量!!!"+pod.getPurchaseOrder().getCode());
        			}else{
        				//增加数量小于PO单的收货数量
        				updateDoDetail(detail,deliveryOrder,order);
        			}
        		}else{
        			Double unDeliveryQuantityBu = Arith.sub(detail.getTheDeliveryQuantityBu(),detail.getQuantity());//未配货数量
        			if(unDeliveryQuantityBu<-adjustQty){
        				String hql ="FROM WmsTransportOrderDetail t WHERE t.deliveryOrderDetail.deliveryOrder.code =:dCode ";
                    	List<WmsTransportOrderDetail> wtods = commonDao.findByQuery(hql, "dCode", order.getVBELN());
                    	if(!wtods.isEmpty()){
                    		throw new BusinessException("已加入配货单不允许修改");
                    	}
        			}
        			//交货单数量减少
        			updateDoDetail(detail,deliveryOrder,order);
        		}
            	}
//            }
        }
        WmsDeliveryOrderManager doManager = (WmsDeliveryOrderManager) applicationContext.getBean("wmsDeliveryOrderManager");
        deliveryOrder.setStatus(WmsDeliveryOrderStatus.ACTIVE);
        doManager.activeDeliveryOrder(deliveryOrder);
    }
	
	private void updateDoDetail(WmsDeliveryOrderDetail detail,WmsDeliveryOrder deliveryOrder,SapDeliveryOrder order) {
		detail.setDeliveryOrder(deliveryOrder);
        WmsItem item = this.getItemByCode(order.getMATNR());
        if (item==null) {
            throw new BusinessException("WMS物料【"+order.getMATNR()+"】未维护");
        }
        detail.setItem(item);
        WmsSapFactory factory = this.getSapFactoryByCode(order.getWERKS());
        detail.setFactory(factory);
        
        this.checkItemFactory(item, factory);
        
        detail.setKcdd(order.getLGORT());
        detail.setPlanQuantityBu(Double.parseDouble(StringHelper.isNullOrEmpty(order.getLFIMG())?"0":order.getLFIMG()));
        if(detail.isNew()){
        	detail.setDelivedQuantityBu(Double.parseDouble(StringHelper.isNullOrEmpty(order.getDABMG())?"0":order.getDABMG()));//已交货数量
//        	detail.setTheDeliveryQuantityBu(DoubleUtils.sub(detail.getPlanQuantityBu(), detail.getDelivedQuantityBu()));//本次交货数量=计划数量-已交货
        }
        detail.setTheDeliveryQuantityBu(DoubleUtils.sub(detail.getPlanQuantityBu(), detail.getDelivedQuantityBu()));//本次交货数量=计划数量-已交货
        detail.setPoNo(order.getVGBEL());
        detail.setPoDetailNo(order.getVGPOS());
        WmsPackageUnit pku = this.getWmsPackageUnitByItem(item.getId(), order.getMEINS());
        detail.setPackageUnit(pku);
        detail.setExtend1(order.getWBSTK());//货物移动状态
        detail.setPosnr(order.getPOSNR());//行项目
         
        //保存采购订单明细
        PurchaseOrderDetail pord = this.getPurchaseOrderDetail(order.getVGBEL(),order.getVGPOS());
        detail.setPurchaseOrderDetail(pord);
        
        //仓库
        String pstype = detail.getPurchaseOrderDetail().getPstyp();
        WmsWarehouse house = null;
        if(null!=pstype){
        	String houseHql = "SELECT wfw.warehouse FROM WmsFactoryWarehouse wfw where wfw.factory=:wf and wfw.type=:wt";
        	house = (WmsWarehouse)commonDao.findByQueryUniqueResult(houseHql, new String[]{"wf","wt"}, new Object[]{factory,pstype});
        }
        if(null!=house){
        	deliveryOrder.setWarehouse(house);
        	commonDao.store(deliveryOrder);
        	commonDao.store(detail);
        }
        //先保存下交货单及交货单明细 以确保查交货单的本次交货数量能查到
        String qtyHql = "SELECT SUM(dod.planQuantityBu) FROM WmsDeliveryOrderDetail dod WHERE dod.purchaseOrderDetail.id=:purchaseOrderDetailId ";
        Double planQty = (Double) commonDao.findByQueryUniqueResult(qtyHql, "purchaseOrderDetailId", pord.getId());
        if(DoubleUtils.sub(pord.getExpectedQty(), planQty)<0){
        	throw new BusinessException("交货单明细数量不能大于采购订单明细数量");
        }
        String hql = "SELECT SUM(dod.theDeliveryQuantityBu) FROM WmsDeliveryOrderDetail dod WHERE dod.purchaseOrderDetail.id=:purchaseOrderDetailId ";
        Double allotQty = (Double) commonDao.findByQueryUniqueResult(hql, "purchaseOrderDetailId", pord.getId());
        pord.setAllotQty(allotQty==null ? 0D : allotQty);//写采购单明细的分配数量
        pord.getPurchaseOrder().refreshAllotQty();
        commonDao.store(pord);
	}

	@SuppressWarnings("unchecked")
	public void dealSapPoOrders(SapPoArray poArray) {
		System.out.println("开始处理Sap采购订单");
		int i=0;
		Map<String,WmsSupplier> supplierMap = new HashMap<String, WmsSupplier>();
		for(SapPo po : poArray.getSapPos()) {
			System.err.println("开始处理Sap采购订单:明细"+i);
			StringHelper.assertNullOrEmpty(po.getEINDT(), "EINDT属性不能为空");
			StringHelper.assertNullOrEmpty(po.getLGORT(), "LGORT属性不能为空");
			StringHelper.assertNullOrEmpty(po.getEBELN(), "EBELN属性不能为空");
			StringHelper.assertNullOrEmpty(po.getAEDAT(), "AEDAT属性不能为空");
			i++;
			String dateStr = "20170901";
			Date date = DateUtil.formatDate(dateStr);
			Date poDate = DateUtil.formatDate(po.getAEDAT());
//			if(poDate.before(date)){
//				throw new BusinessException("17年9月1号之前的采购订单不允许修改");
//			}
			PurchaseOrder order = null;
			List<PurchaseOrder> orders = commonDao.findByQuery("FROM PurchaseOrder order where order.code=:oc", "oc", po.getEBELN());
			if(orders.size()>1){
				throw new BusinessException("根据采购单号"+po.getEBELN()+"找到多条记录");
			}
			if(orders.size()==1){
				order = orders.get(0);
			}
			updateSapPoOrder(po,order,i);
			//一个采购订单发一个邮件
			//20171218已改，改成定时任务早晚12点触发，一个供应商发一封
//			String key = po.getEBELN();
//			if(!supplierMap.containsKey(key)){
//				WmsSupplier supplier = this.getSupplierByCode(po.getLIFNR()); 
//		        if (supplier==null) {
//		            throw new BusinessException("WMS供应商【"+po.getLIFNR()+"】不存在");
//		        }
//				supplierMap.put(key, supplier);
//				createEmailRecord(supplier,po);//生成邮件
//			}
			if(!StringHelper.isNullOrEmpty(po.getLOEKZ())){//X为删除的标识
				//删除采购订单明细
				String detailHql = "FROM PurchaseOrderDetail detail WHERE detail.purchaseOrder.code=:code AND detail.ebelp =:ebelp ";
				String checkHql = "FROM WmsDeliveryOrderDetail wd WHERE wd.purchaseOrderDetail.id=:wpId";
				List<WmsDeliveryOrderDetail> wdods = null;
				PurchaseOrderDetail detail = null;
				
				List<PurchaseOrderDetail> details = commonDao.findByQuery(detailHql, new String[]{"code","ebelp"}, new Object[]{po.getEBELN(),po.getEBELP()});
				
				if(details.isEmpty()){
					throw new BusinessException("根据采购单号"+po.getEBELN()+"和行项目"+po.getEBELP()+"未找到采购单明细，删除失败");
				}
				if(details.size()>1){
					throw new BusinessException("根据采购单号"+po.getEBELN()+"和行项目:"+po.getEBELP()+"找到了 "+details.size()+"条记录,删除失败");
				}
				detail = details.get(0);
				wdods = commonDao.findByQuery(checkHql, "wpId", detail.getId());
				
				for(WmsDeliveryOrderDetail dod : wdods){
					if(dod.getPlanQuantityBu()>0){
						throw new BusinessException("WMS交货单已有交货数量，不能删除采购单");
					}
				}
				detail.setExpectedQty(0D);
				detail.setExpectedPackQty(0D);
				detail.setAllotQty(0D);
				detail.setReceivedQty(0D);
				detail.getPurchaseOrder().refreshQtyBU();
				detail.getPurchaseOrder().refreshAllotQty();
				detail.getPurchaseOrder().refreshReceiveQty();
				commonDao.store(detail);
			}
		}
//		if(poArray.getSapPos() != null && poArray.getSapPos().length>0){
//			WmsSupplier supplier = this.getSupplierByCode(poArray.getSapPos()[0].getLIFNR()); 
//			if (supplier==null) {
//	            throw new BusinessException("WMS供应商【"+poArray.getSapPos()[0].getLIFNR()+"】不存在");
//	        }
//			createEmailRecord(supplier,poArray.getSapPos()[0]);//生成邮件
//		}
	}
	private void updateSapPoOrder(SapPo po,PurchaseOrder order,int row) {
        if(order == null){
			//新增
        	order = EntityFactory.getEntity(PurchaseOrder.class);
        	order.setStatus(PurchaseOrderStatus.OPEN);
        	order.setConfirmStatus(ConfirmStatus.OPEN);
        	UpdateInfo updateInfo = new UpdateInfo();
            updateInfo.setCreatedTime(new Date());//创建时间
            updateInfo.setCreator(po.getERNAM());//订单创建人
            order.setUpdateInfo(updateInfo);
        }else{
        	//修改
    		order.getUpdateInfo().setLastOperator(po.getERNAM());//订单创建人
    		order.getUpdateInfo().setUpdateTime(new Date());//更新时间
    		if(order.getConfirmStatus().equals(ConfirmStatus.CONFIRM)||order.getConfirmStatus().equals(ConfirmStatus.RECEIVED)){
    			order.setConfirmStatus(ConfirmStatus.OPEN);
    		}
        }
        order.setCode(po.getEBELN());//采购订单号
		order.setBsart(po.getBSART());//采购订单类型
		order.setCreatDate(DateUtil.formatDate(po.getAEDAT()));//订单创建日期
		WmsSupplier supplier = this.getSupplierByCode(po.getLIFNR()); 
        if (supplier==null) {
            throw new BusinessException("WMS供应商【"+po.getLIFNR()+"】不存在");
        }
		WmsCompany company = this.getDefaultWmsCompany();
		order.setCompany(company);
//        createEmailRecord(supplier,po);//生成邮件
        order.setSupplier(supplier);//WMS供应商
        order.setQty(new Double(po.getMENGE()));//订单数量
        order.setEkorg(po.getEKORG());//采购组织
        order.setEkgrp(po.getEKGRP());//采购组
        order.setUserField3(po.getLOEKZ()==null?"":po.getLOEKZ());//删除标识
        order.setUserField4(po.getRETPO()==null?"":po.getRETPO());//退货标识 
        commonDao.store(order);
        //明细数量
        PurchaseOrderDetail detail = null;
        String hql = "FROM PurchaseOrderDetail detail WHERE detail.purchaseOrder.code =:code AND detail.ebelp =:ebelp ";
        List<PurchaseOrderDetail> details  = commonDao.findByQuery(hql, 
        		new String[]{"code","ebelp"}, new Object[]{order.getCode(),po.getEBELP()});
        if(details.size()>1){
        	throw new BusinessException("根据采购单号"+order.getCode()+"和行项目"+po.getEBELP()+"找到多条明细");
        }
        if(details.size()==1){
        	detail = details.get(0);
        }
        if(detail == null){
        	//新建明细
        	detail = EntityFactory.getEntity(PurchaseOrderDetail.class);
        	updatePurchaseOrderDetail(po,order,detail,row);
        }else{
//        	updatePurchaseOrderDetail(po,order,detail,row);
        	//修改明细
        	if(Arith.sub(detail.getExpectedPackQty(),Double.valueOf(po.getMENGE()))<=0){ //明细数量 小于修改后的数量  说明是增加了采购数量 则直接修改。
        		//增加期待数量
        		updatePurchaseOrderDetail(po,order,detail,row);
        	}else{
        		//减少期待数量
        		double d1 = Double.valueOf(po.getMENGE()); //修改数量
        		//计算已分配数量
        		double d2 = DoubleUtils.add(detail.getAllotQty(), detail.getReceivedQty());//采购单已分配的数量
        		if(Arith.sub(d1, d2)>=0){//修改后数量大于等于已分配数量  则直接修改。
        			updatePurchaseOrderDetail(po,order,detail,row);
        		}else{
        			throw new BusinessException("采购订单号为【"+order.getCode()+"】行项目号为:"+po.getEBELP()+"修改失败!修改后数量"+d1+"小于已分配数量"+d2);
        		}
        	} 
        }
        
        WmsPurchaseOrderManager wmsPurchaseOrderManager = (WmsPurchaseOrderManager) applicationContext.getBean("wmsPurchaseOrderManager");
        order.setStatus(PurchaseOrderStatus.ACTIVE);
        wmsPurchaseOrderManager.isActivePurchase(order);
	}
	private void updatePurchaseOrderDetail(SapPo po,PurchaseOrder order,PurchaseOrderDetail detail, int row) {
		if(detail.isNew()){
			detail.setPurchaseOrder(order);
			detail.setLineNo(Integer.valueOf(po.getEBELP())); // 明细行号
		}
    	WmsItem item = this.getItemByCode(po.getMATNR());
    	if (item==null) {
    		throw new BusinessException("物料【"+po.getMATNR()+"】不存在");
    	}
    	item.setDescription(po.getTXZ01());
    	detail.setItem(item);
    	WmsSapFactory factory = this.getSapFactoryByCode(po.getWERKS()); 
    	
    	this.checkItemFactory(item, factory);
    	
    	order.setSapFactory(factory);//单头的工厂
    	detail.setFactory(factory);//明细的工厂
    	WmsPackageUnit pku = this.getWmsPackageUnitByItem(item.getId(), po.getMEINS()); 
    	detail.setPackageUnit(pku);//包装单位
    	detail.setUserField1(po.getLGORT());//库存地点
    	detail.setEbelp(po.getEBELP());//项目
    	detail.setPstyp(po.getPSTYP());//项目类别
    	detail.setReceivedDate(DateUtil.formatDate(po.getEINDT()));
    	detail.setExpectedQty(Double.valueOf(po.getMENGE()));//期待数量
    	detail.setExpectedPackQty(Double.valueOf(po.getMENGE()));//期待包装数量
    	
//    	detail.setAllotQty(Double.valueOf(po.getLINENO()));//分配数量和实际收货数量只在导入的时候写入，接口过来的不改，更新时这两个代码不能更新到EDI上
//    	detail.setReceivedQty(Double.valueOf(po.getLINENO()));
    	
    	detail.setInventoryStatus(StringHelper.isNullOrEmpty(po.getINSMK()) ?"合格":"X".equals(po.getINSMK())?"待检":"");
    	commonDao.store(detail);
    	order.addDetail(detail);
    	order.refreshQtyBU();
	}

	private void createEmailRecord(WmsSupplier supplier,Object obj) {
		if(obj instanceof SapPo){
			SapPo po = (SapPo)obj;
			String subject =EmailHelper.getTheme(EmailRecordType.PO2SUPPLIER, po.getEBELN());
			List<String> infos = new ArrayList<String>();
			infos.add(po.getEBELN());
			String content = EmailHelper.getEmailContent(EmailRecordType.PO2SUPPLIER, infos);
			
			MidSurpplierUser msu = this.getMidSurpplierUserBySupplierId(supplier.getId());
			if(null!=msu){
				ThornUser user = commonDao.load(ThornUser.class, msu.getUid());
				//每个邮箱发邮件
				String emails = user.getEmail();
				if(null!=emails){
					String [] ems = emails.split("\\|");
					for (String em : ems) {
						//异步发邮件
						EmailRecordManager emailRecordManager = (EmailRecordManager)applicationContext.getBean("emailRecordManager");
						emailRecordManager.storeEmailRecordWaitSend(user.getLoginName(),em,subject,content,EmailHelper.getEmailCc(),EmailRecordType.PO2SUPPLIER,po.getEBELN());
					}
				}
			}
		}
		if(obj instanceof SapDeliveryOrder){
			SapDeliveryOrder sdo = (SapDeliveryOrder)obj;
			String subject =EmailHelper.getTheme(EmailRecordType.DELIVERY2SUPPLIER, sdo.getVBELN());
			List<String> infos = new ArrayList<String>();
			infos.add(sdo.getVBELN());
			String content = EmailHelper.getEmailContent(EmailRecordType.DELIVERY2SUPPLIER, infos);
			MidSurpplierUser msu = this.getMidSurpplierUserBySupplierId(supplier.getId());
			if(null!=msu){
				ThornUser user = commonDao.load(ThornUser.class, msu.getUid());
				//每个邮箱发邮件
				String emails = user.getEmail();
				String [] ems = emails.split("\\|");
				for (String em : ems) {
					//异步发邮件
					EmailRecordManager emailRecordManager = (EmailRecordManager)applicationContext.getBean("emailRecordManager");
					emailRecordManager.storeEmailRecordWaitSend(user.getLoginName(),em,subject,content,EmailHelper.getEmailCc(),EmailRecordType.DELIVERY2SUPPLIER,sdo.getVBELN());
				}
			}
		}
		if(obj instanceof SapCheckOrder){
			SapCheckOrder sco = (SapCheckOrder) obj;
			String subject =EmailHelper.getTheme(EmailRecordType.CHECKBZSUPPLIER, sco.getLIFNR()+StringHelper.substring(sco.getBUDAT(), 6));
			List<String> infos = new ArrayList<String>();
			infos.add(sco.getLIFNR()+StringHelper.substring(sco.getBUDAT(), 6));
			String content = EmailHelper.getEmailContent(EmailRecordType.CHECKBZSUPPLIER, infos);
			MidSurpplierUser msu = this.getMidSurpplierUserBySupplierId(supplier.getId());
			if(null!=msu){
				ThornUser user = commonDao.load(ThornUser.class, msu.getUid());
				//每个邮箱发邮件
				String emails = user.getEmail();
				String [] ems = emails.split("\\|");
				for (String em : ems) {
					//异步发邮件
					EmailRecordManager emailRecordManager = (EmailRecordManager)applicationContext.getBean("emailRecordManager");
					emailRecordManager.storeEmailRecordWaitSend(user.getLoginName(),em,subject,content,EmailHelper.getEmailCc(),EmailRecordType.CHECKBZSUPPLIER,sco.getLIFNR()+StringHelper.substring(sco.getBUDAT(), 6));
				}
			}
		}
		if(obj instanceof SapJSCheckOrder){
			SapJSCheckOrder sco = (SapJSCheckOrder) obj;
			String subject =EmailHelper.getTheme(EmailRecordType.CHECKJSSUPPLIER, sco.getLIFNR()+StringHelper.substring(sco.getBUDAT(), 6));
			List<String> infos = new ArrayList<String>();
			infos.add(sco.getLIFNR()+StringHelper.substring(sco.getBUDAT(), 6));
			String content = EmailHelper.getEmailContent(EmailRecordType.CHECKJSSUPPLIER, infos);
			MidSurpplierUser msu = this.getMidSurpplierUserBySupplierId(supplier.getId());
			if(null!=msu){
				ThornUser user = commonDao.load(ThornUser.class, msu.getUid());
				//每个邮箱发邮件
				String emails = user.getEmail();
				String [] ems = emails.split("\\|");
				for (String em : ems) {
					//异步发邮件
					EmailRecordManager emailRecordManager = (EmailRecordManager)applicationContext.getBean("emailRecordManager");
					emailRecordManager.storeEmailRecordWaitSend(user.getLoginName(),em,subject,content,EmailHelper.getEmailCc(),EmailRecordType.CHECKJSSUPPLIER,sco.getLIFNR()+StringHelper.substring(sco.getBUDAT(), 6));
				}
			}
		}
	}
	@Override
	public void dealSapProductOrder(SapProductOrderArray spoas) {
		
		System.out.println("开始处理 Sap生产订单.........");
		String type = spoas.getTYPE();
		int i = 0;
		List<String> orderCodeList = new ArrayList<String>();
		//关单标识，如果SAP要关单则所有的明细都会带删除标识,有一个不带就认为不是关单
		Boolean closePro = Boolean.TRUE;
		for(SapProductOrder spo : spoas.getSpos()){
			if(StringHelper.isNullOrEmpty(spo.getXLOEK())){
				closePro = Boolean.FALSE;
				break;
			}
		}
		for(SapProductOrder spo : spoas.getSpos()){
			StringHelper.assertNullOrEmpty(spo.getMEINS1(), "MEINS1属性不能为空");
//			StringHelper.assertNullOrEmpty(spo.getZPRO_LINE(), "ZPRO_LINE属性不能为空");
			StringHelper.assertNullOrEmpty(spo.getRSNUM(), "RSNUM属性不能为空");
			StringHelper.assertNullOrEmpty(spo.getRSPOS(), "RSPOS属性不能为空");
			i+=10;
			String spoCode = spo.getAUFNR();//生产订单号
			ProductionOrder order = null;
			try {
				order = (ProductionOrder)commonDao.findByQueryUniqueResult("FROM ProductionOrder order where order.code=:oc", "oc", spoCode);
			} catch (Exception e) {
				throw new BusinessException("根据生产订单号"+spoCode+"查询到多条生产订单数据!!");
			}
			boolean flag =order==null?true:false;
			if(!"D".equals(type)){
				if(order !=null && (order.getBeCreatPt() || order.getBeDetailCreatePt())){
//					throw new BusinessException("工单"+order.getCode()+"已经开始拣配，无法修改");
					//如果SAP发送修改指令，且WMS已经开始拣配，则认为是修改工单开始时间。
			        order.setBeginDate(DateUtil.formatDate(spo.getGSTRP()));//基本开始日期
			        order.setEndDate(DateUtil.formatDate(spo.getGLTRP()));//基本完成日期
			        
			        if(!StringHelper.isNullOrEmpty(order.getBeginHour())) {
			        	 Calendar calendar = Calendar.getInstance();
			             calendar.setTime( DateUtil.getDate(DateUtil.format(order.getBeginDate(), "yyyy-MM-dd"), "yyyy-MM-dd"));
			             calendar.add(Calendar.HOUR_OF_DAY, Integer.valueOf(order.getBeginHour()+""));
			             order.setBeginTime(calendar.getTime());
			           
			        }
			        if(!StringHelper.isNullOrEmpty(order.getEndHour())) {
			        	 Calendar calendar = Calendar.getInstance();
			             calendar.setTime( DateUtil.getDate(DateUtil.format(order.getEndDate(), "yyyy-MM-dd"), "yyyy-MM-dd"));
			             calendar.add(Calendar.HOUR_OF_DAY, Integer.valueOf(order.getEndHour()+""));
			             order.setEndTime(calendar.getTime());
			        }
			        commonDao.store(order);
			        if(closePro){
			        	for(ProductionOrderDetail detail : order.getDetails()){
			        		detail.setDeleteFlag(spo.getXLOEK());
			        		commonDao.store(detail);
			        	}
			        	order.setStatus(ProductionOrderStatus.FINISHED);
						commonDao.store(order);
			        }
				}
				else {
				   //新增
					updateProductOrder(flag,spo,order,i,orderCodeList);
				}
			}
			else if("D".equals(type)){
				if(order == null){
					throw new BusinessException("工单"+spoCode+"在系统中不存在");
				}
				if(order.getBeCreatPt()) {
	    			throw new BusinessException("工单"+order.getCode()+"已经开始拣配，无法修改");
	    		}
				//关闭生产订单工单
				order.setStatus(ProductionOrderStatus.CLOSE);
				commonDao.store(order);
			}
		}
	}
	private void updateProductOrder(boolean flag, SapProductOrder spo,ProductionOrder order,int i,List<String> orderCodeList) {
		if(order == null){
			order = EntityFactory.getEntity(ProductionOrder.class);
		}
		order.setCode(spo.getAUFNR());// 生产订单号
		WmsSapFactory factory = (WmsSapFactory) commonDao.findByQueryUniqueResult("FROM WmsSapFactory WHERE code=:code", "code", spo.getWERKS());
        if (factory==null) {
            throw new BusinessException("SAP工厂【"+spo.getWERKS()+"】不存在");
        }
        order.setSaleCode(spo.getXSDH());//销售单号
        order.setFactory(factory);//工厂
        order.setCpItemCode(spo.getMATNR());//单头物料
        order.setPlanQuantity(new Double(spo.getGAMNG().trim()));//计划数量
        order.setPtype(spo.getAUART());//订单类型
        order.setBeginDate(DateUtil.formatDate(spo.getGSTRP()));//基本开始日期
        order.setEndDate(DateUtil.formatDate(spo.getGLTRP()));//基本完成日期
        order.setRemark(spo.getMAKTX());//单头物料描述
        if(!"TP05".equals(spo.getAUART())){
        	StringHelper.assertNullOrEmpty(spo.getZPRO_LINE(), "ZPRO_LINE属性不能为空");
        	order.setProductLine(spo.getZPRO_LINE());
        	order.setLineDesc(spo.getZPRO_NAME());//产线描述
        }else{
        	//冰箱,订单类型TP05的，SAP有产线就按SAP给的来，没有，产线就默认TP05
        	if(StringHelper.in(spo.getWERKS(), new String[]{WmsSapFactoryCodeEnum.BX_NX,WmsSapFactoryCodeEnum.BX_WX})
        			&& !StringHelper.isNullOrEmpty(spo.getZPRO_LINE())){
            		order.setProductLine(spo.getZPRO_LINE());
                	order.setLineDesc(spo.getZPRO_NAME());//产线描述
            }
        	else{
            	order.setProductLine("TP05");//生产线
            	order.setLineDesc("研发样机");//产线描述
            }
        }
        
        if(flag){
			//新增
        	order.setStatus(ProductionOrderStatus.ACTIVE); //生产订单过来直接生效
        	UpdateInfo updateInfo = new UpdateInfo();
            updateInfo.setCreatedTime(new Date());//创建时间
            order.setUpdateInfo(updateInfo);
        }else{
        	//修改
    		order.getUpdateInfo().setUpdateTime(new Date());//更新时间
        }
        //删除老的生产订单所有明细行，生成新的明细行
        if(!orderCodeList.contains(order.getCode())){
        	if(!order.isNew()){
            	String hql = " DELETE FROM ProductionOrderDetail pod WHERE pod.productionOrder.id =:id ";
                commonDao.executeByHql(hql, "id", order.getId());
    		}
        	orderCodeList.add(order.getCode());
        }
        
        
        commonDao.store(order);
//        String hql = "FROM ProductionOrderDetail detail WHERE detail.productionOrder.code =:code AND detail.reservedOrder =:reservedOrder AND detail.reservedProject =:reservedProject ";
        ProductionOrderDetail detail  = EntityFactory.getEntity(ProductionOrderDetail.class);
//        try {
//        	detail = (ProductionOrderDetail) commonDao.findByQueryUniqueResult(hql, 
//        			new String[]{"code","reservedOrder","reservedProject"},
//        			new Object[]{spo.getAUFNR(),spo.getRSNUM(),spo.getRSPOS()});
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new BusinessException("根据生产订单号"+spo.getAUFNR()+"和预留号"+spo.getRSNUM()+"预留行项目号"+spo.getRSPOS()+"查询到多条生产订单明细数据!!");
//		}
		detail.setProductionOrder(order);
		detail.setLineNo(i);
		detail.setReservedOrder(spo.getRSNUM());
		detail.setReservedProject(spo.getRSPOS());
    	WmsItem item = this.getItemByCode(spo.getMATNR1()); 
        if (item==null) {
            throw new BusinessException("明细物料【"+spo.getMATNR1()+"】不存在");
        }
        detail.setItem(item);
        this.checkItemFactory(item, order.getFactory());
        detail.setBomCode(spo.getPOSNR());//组件项目号
        if(!StringHelper.isNullOrEmpty(spo.getXLOEK())){ //删除标识X--不做处理，系统记录这个标识，下发拣配的时候过滤有删除标识的明细
        	detail.setDeleteFlag(spo.getXLOEK());
//        	detail.setPlanQuantityBu(0D);//需求数量
//        	detail.setAllocatedQuantityBu(0D);
//        	detail.setPickedQuantityBu(0D);
//        	detail.setShippedQuantityBu(0D);
        }else{
        	detail.setDeleteFlag(null);
        	detail.setPlanQuantityBu(new Double(spo.getBDMNG().trim()));//需求数量
        	detail.setAllocatedQuantityBu(Double.valueOf(spo.getENMNG()==null ? "0": spo.getENMNG().toString()));
        	detail.setPickedQuantityBu(Double.valueOf(spo.getENMNG()==null ? "0": spo.getENMNG().toString()));
        	detail.setShippedQuantityBu(Double.valueOf(spo.getENMNG()==null ? "0": spo.getENMNG().toString())); //已发货数量 --临时用
        	detail.setOldShippedQuantityBu(Double.valueOf(spo.getENMNG()==null ? "0": spo.getENMNG().toString()));//记录接口过来的发运数量
        	detail.setXfQty(Double.valueOf(spo.getENMNG()==null ? "0": spo.getENMNG().toString()));//记录接口过来的发运数量
        }
        
        WmsPackageUnit pku = this.getWmsPackageUnitByItem(item.getId(), spo.getMEINS1());
        detail.setPackageUnit(pku);
        commonDao.store(detail);
        order.addDetail(detail);
        String hql2 = "FROM ProductionOrderDetail d WHERE d.productionOrder.id =:productionOrderId AND d.deleteFlag IS NULL ";
		List<ProductionOrderDetail> pods = commonDao.findByQuery(hql2, new String[]{"productionOrderId"}, new Object[]{order.getId()});
		if(pods.isEmpty()){
			order.setStatus(ProductionOrderStatus.FINISHED);
			commonDao.store(order);
		}else{
			order.setStatus(ProductionOrderStatus.ACTIVE);
		}
	}

	@Override
	public void dealSapReservedData(SapReservedDataArray datas) {
		System.out.println("开始处理 Sap预留主数据..................");
		String type = datas.getTYPE();
		int i=0;
		WmsReservedOrder wro =null;
		for (SapReservedData srd : datas.getDatas()) {
			
			i++;
			String rcode = srd.getRSNUM();//预留单号
			try {
				wro = (WmsReservedOrder)commonDao.findByQueryUniqueResult("FROM WmsReservedOrder wro where wro.code=:wc", "wc", rcode);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException("根据预留单号"+rcode+"查询到多条wms预留单数据!!");
			}
			boolean flag =wro==null?true:false;
			if(!"D".equals(type)){
				//新增
				updateReservedOrder(srd,wro,i,flag);
			}
			else if("D".equals(type) || null!=srd.getXLOEK()){
			 //删除
				 
			}
		}
	}
	
	private void updateReservedOrder(SapReservedData srd, WmsReservedOrder order,int i,boolean flag) {
		if(order == null){
			order = EntityFactory.getEntity(WmsReservedOrder.class);
		}
		order.setCode(srd.getRSNUM());//预留单号
		order.setSapCode(srd.getRSNUM());//预留号
		WmsSapFactory factory = this.getSapFactoryByCode(srd.getWERKS());
		
        order.setFactory(factory);//工厂
        order.setKcd(srd.getUMLGO());//收货库存地
        order.setJzrq(DateUtil.formatDate(srd.getRSDAT()));//基准日期
        order.setYhmc(srd.getUSNAM());//用户名称
        order.setYdlx(srd.getBWART());//移动类型 Z01/Z02/Z03/Z04等
        order.setCbzx(srd.getKOSTL());//成本中心
        order.setCbzxRemark(srd.getKTEXT());//成本中心描述
        order.setReceiveWorker(srd.getSGTXT());//收货人--生成拣货单时记录在产线描述上
        if(flag){
			//新增
        	order.setStatus(ProductionOrderStatus.OPEN);
        	UpdateInfo updateInfo = new UpdateInfo();
            updateInfo.setCreatedTime(new Date());//创建时间
            order.setUpdateInfo(updateInfo);
        }else{
        	//修改
        	if(WmsReservedOrderType.Z02.equals(order.getYdlx()) || WmsReservedOrderType.Z04.equals(order.getYdlx())){
        		String hql = " FROM WmsASN asn WHERE asn.customerBill =:customerBill AND asn.status !=:status ";
        		List<WmsASN> asns = commonDao.findByQuery(hql, new String[]{"customerBill","status"}, new Object[]{order.getCode(),WmsASNStatus.CANCELED});
        		if(!asns.isEmpty()){
        			throw new BusinessException("预留单:"+order.getCode()+"已生成收货单，不允许修改");
        		}
        	}else{
//        		String hql = " FROM WmsPickTicket pick WHERE pick.relatedBill1 =:relatedBill1 AND (pick.status !=:status or pick.status !=:status2) ";
//        		List<WmsPickTicket> picks = commonDao.findByQuery(hql, new String[]{"relatedBill1","status","status2"}, 
//        				new Object[]{order.getCode(),WmsPickTicketStatus.CANCELED,WmsPickTicketStatus.CLOSED});
//        		if(!picks.isEmpty()){
//        			throw new BusinessException("预留单:"+order.getCode()+"已生成拣货单，不允许修改");
//        		}
        	}
    		order.getUpdateInfo().setUpdateTime(new Date());//更新时间
        }
        commonDao.store(order);
        String hql = "FROM WmsReservedOrderDetail detail WHERE detail.reservedOrder.code =:code AND detail.project =:bomCode ";
        WmsReservedOrderDetail detail  = null;
        try {
        	detail = (WmsReservedOrderDetail) commonDao.findByQueryUniqueResult(hql, 
        			new String[]{"code","bomCode"},
        			new Object[]{srd.getRSNUM(),srd.getRSPOS()});
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("根据预留单号"+srd.getRSNUM()+"和行项目"+srd.getRSPOS()+"查询到多条预留单明细数据!!!");
		}
        Double changeQty =0d;//修改后的数量
    	boolean deleteFlag =false; //删除标记
		if(!StringHelper.isNullOrEmpty(srd.getXLOEK())) { //删除标记  
			StringHelper.assertNullOrEmpty(srd.getENMNG(), "有删除标记则ENMNG字段不能为空");
			deleteFlag = true;
			changeQty = Double.valueOf(srd.getENMNG()); //修改后的数量为提货数
		}
		else {
			StringHelper.assertNullOrEmpty(srd.getBDMNG(), "无删除标记则BDMNG字段不能为空");
			changeQty = Double.valueOf(srd.getBDMNG());   //修改后的数量为需求数
		}
		
        if(detail == null ){
    		detail = EntityFactory.getEntity(WmsReservedOrderDetail.class);
    		detail.setReservedOrder(order);
    		detail.setLineNo(i+"");
    		detail.setProject(srd.getRSPOS());
    		detail.setThQty(Double.valueOf(StringHelper.isNullOrEmpty(srd.getENMNG()) ?"0":srd.getENMNG().trim()));//提货数
    		detail.setAllocatedQuantityBu(Double.valueOf(StringHelper.isNullOrEmpty(srd.getENMNG()) ?"0":srd.getENMNG().trim()));
    		detail.setPickedQuantityBu(Double.valueOf(StringHelper.isNullOrEmpty(srd.getENMNG()) ?"0":srd.getENMNG().trim()));
    		detail.setShippedQuantityBu(Double.valueOf(StringHelper.isNullOrEmpty(srd.getENMNG()) ?"0":srd.getENMNG().trim()));
        }else{
    		if(StringHelper.in(order.getYdlx(),new String[]{WmsReservedOrderType.Z01,WmsReservedOrderType.Z03,WmsReservedOrderType.Z311}) ) {//发货类型
	        	if(detail.getIsCreatePick()) { //生成了拣货单
	        		if(DoubleUtils.sub(changeQty, detail.getQuantity()) > 0d) { //数量变大
	        			throw new BusinessException("行项目"+srd.getRSPOS()+"WMS已开始拣配不允许增加数量");
	        		}
	        		
	        		hql = "select d from ReservedOrderDetailPtDetail d where d.reservedOrderDetail.id=:id";
	        		List<ReservedOrderDetailPtDetail> rodptds = commonDao.findByQuery(hql,new String[]{"id"},new Object[]{detail.getId()});
	        		Double resdalloqty = 0d; //预留明细已经分配的数量
	        		for(ReservedOrderDetailPtDetail rodptd : rodptds) {
	        			
	        			WmsPickTicketDetail ptd = rodptd.getPickticketDetail();
	        			resdalloqty = DoubleUtils.add(resdalloqty, ptd.getAllocatedQty());
	        			
	        			if(WmsPickTicketStatus.PARTALLOCATED.equals(ptd.getPickTicket().getStatus())) { //有部分分配的不允许改
	        				throw new BusinessException("行项目"+srd.getRSPOS()+"在WMS中存在部分分配的拣货单，请先联系保管员处理");
	        			}
	        		}
	        		if(deleteFlag && resdalloqty>0d) {
	        			throw new BusinessException("行项目"+srd.getRSPOS()+"WMS已经分配了"+resdalloqty+"不允许删除");
	        		}
	        		
	        		if(DoubleUtils.sub(changeQty, resdalloqty)<0d) {
	        			throw new BusinessException("行项目"+srd.getRSPOS()+"WMS已经分配了"+resdalloqty+"大于修改后数量"+changeQty);
	        		}
	        		//开始修改
	        		Double tmpchangeQty = DoubleUtils.sub(detail.getQuantity(),changeQty); //需要扣减的数量
	        		for(ReservedOrderDetailPtDetail rodptd : rodptds) {
	        			if(tmpchangeQty<=0d) {
	        				break;
	        			}
	        			WmsPickTicketDetail ptd = rodptd.getPickticketDetail();
	        			WmsPickTicket pt = ptd.getPickTicket();
	        			
	        			
	        			Double thisCanCancel = DoubleUtils.sub(ptd.getExpectedQty(), ptd.getAllocatedQty()) ;//本条可以取消的数量
	        			if(thisCanCancel<=0d) {//本条不能取消数量 则跳出
	        				 continue;
	        			}
	        			
	        			if(thisCanCancel<tmpchangeQty) { //整条取消
	        				//扣减thisCanCancel的数量
	        				ptd.setExpectedQty(DoubleUtils.sub(ptd.getExpectedQty(), thisCanCancel));
	        				ptd.setExpectedPackQty(WmsPackageUnitUtils.getPackQty(ptd.getPackageUnit(), ptd.getExpectedQty(), ptd.getItem().getBuPrecision()));
	        				pt.refreshPickTicketQty();
	        				pt.setStatus(pt.defineStatus());
	        				
	        				rodptd.setQuantityBu(DoubleUtils.sub(rodptd.getQuantityBu(), thisCanCancel)); 
	        				tmpchangeQty = DoubleUtils.sub(tmpchangeQty, thisCanCancel);
	        			}
	        			else {
	        				//扣减tmpchangeQty的数量
	        				ptd.setExpectedQty(DoubleUtils.sub(ptd.getExpectedQty(), tmpchangeQty));
	        				ptd.setExpectedPackQty(WmsPackageUnitUtils.getPackQty(ptd.getPackageUnit(), ptd.getExpectedQty(), ptd.getItem().getBuPrecision()));
	        				pt.refreshPickTicketQty();
	        				pt.setStatus(pt.defineStatus());
	        				rodptd.setQuantityBu(DoubleUtils.sub(rodptd.getQuantityBu(), tmpchangeQty)); 
	        				tmpchangeQty=0d;
	        			}
	        			
	        			
	        		}
	        		if(tmpchangeQty>0d) {
	        			throw new BusinessException("根据预留单号"+srd.getRSNUM()+"和行项目"+srd.getRSPOS()+"剩余"+tmpchangeQty+"未取消到拣货单，修改失败");
	        		}
	        		
	        		
	        	}
    		}
    		else { //收货类型
	        	if(detail.getIsCreateASN()){//生成了收货单
		        		 //收货逻辑和发货逻辑差不多。
		        	  hql = "from WmsASNDetail asnd where asnd.asn.billType.code='YLRKD' and asnd.userField1=:detailId ";
		        	  List<WmsASNDetail> asnd = commonDao.findByQuery(hql,new String[]{"detailId"},new Object[]{detail.getId()});
		        	  if(!asnd.isEmpty()) {
		        		  if(asnd.size()>1) {
		        			  throw new BusinessException("根据行项目:"+srd.getRSPOS()+"找到"+asnd.size()+"条收货单明细");
		        		  }
		        		  WmsASNDetail asndetail = asnd.get(0);
		        		  WmsASN asn = asndetail.getAsn();
		        		  Double recQty = asndetail.getReceivedQty();
		        		  if(DoubleUtils.sub(changeQty, recQty)<0d) {
		        			  throw new BusinessException("行项目"+srd.getRSPOS()+"WMS已收货"+recQty+"大于修改后数量"+changeQty);
		        		  }
		        		  
		        		  asndetail.setExpectedQty(changeQty);//修改计划数量
		        		  asndetail.setExpectedPackQty(WmsPackageUnitUtils.getPackQty(asndetail.getPackageUnit(), asndetail.getExpectedQty(), asndetail.getItem().getBuPrecision()));
		        		  asn.refreshQtyBU();
		        		  //收货时判断是否整单收货
		        		  if(StringHelper.in(asn.getStatus(), new String[]{WmsASNStatus.RECEIVING,WmsASNStatus.ACTIVE})) {
		        			  workflowManager.doWorkflow(asn, "wmsASNProcess.receiveAll");
		        		  }
		        		  if(WmsASNStatus.RECEIVED.equals(asn.getStatus())){
		        				//收货完成后记录ASN结束收货日期
		        				asn.setEndReceivedDate(new Date());
		        		  }
		        		  commonDao.store(asndetail);
		        		  this.commonDao.store(asn);
		        	  }
	        	}
    		}
        }
        detail.setDeleteFlag(srd.getXLOEK());//行项目删除标记
        detail.setZhfh(srd.getKZEAR());//最后发货
        WmsItem item = this.getItemByCode(srd.getMATNR());
        if (item==null) {
            throw new BusinessException("明细物料【"+srd.getMATNR()+"】不存在");
        }
        detail.setItem(item);
        this.checkItemFactory(item, factory);
        WmsPackageUnit pku =this.getWmsPackageUnitByItem(item.getId(),  srd.getMEINS());
        detail.setUnit(pku);//包装单位
        detail.setFactory(order.getFactory());//工厂
        detail.setShipLoc(srd.getLGORT());//发出库位
        detail.setRequestDate(DateUtil.formatDate(srd.getBDTER()));//需求日期
        if(!StringHelper.isNullOrEmpty(srd.getXLOEK())){ //有删除标记的需求数量置为提货数
        	detail.setQuantity(changeQty);//需求量
        }else{
        	detail.setQuantity(changeQty);//需求量
        }
        detail.setJdFlag(srd.getSHKZG());//借贷标识
        detail.setLineNoRemark(srd.getSGTXT());//行项目文本
        commonDao.store(detail);
	}

	public Wms2SapInterfaceLog createWms2SapInterfaceLog(String taskType, String type, String function, String fromSys, String toSys, String requestXml) {
        try {
        	Wms2SapInterfaceLog interfaceLog = EntityFactory.getEntity(Wms2SapInterfaceLog.class);
            interfaceLog.setType(type);
            interfaceLog.setFunction(function);
            interfaceLog.setFromSYS(fromSys);
            interfaceLog.setToSYS(toSys);
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
	public void dealSapSaleOutDelivery(SapSaleOutDeliveryArray datas) {
		System.out.println("开始处理Sap外向交货单..................");
        String type = datas.getTYPE();
        int i = 0;
        for(SapSaleOutDelivery order : datas.getSsods()){
        	i++;
        	try {
        		WmsDeliveryOrder deliveryOrder = null;
        		List<WmsDeliveryOrder> deliveryOrders = commonDao.findByQuery("FROM WmsDeliveryOrder "
                        + "WHERE sapCode=:sapCode", "sapCode", order.getVBELN());
        		if(deliveryOrders.isEmpty()){
        			deliveryOrder = EntityFactory.getEntity(WmsDeliveryOrder.class);
             	}
        		if(deliveryOrders.size()>1){
        			throw new BusinessException("根据SAP交货单号"+order.getVBELN()+"找到多条销售交货单数据");
        		}
        		if(deliveryOrders.size()==1){
        			deliveryOrder = deliveryOrders.get(0);
        		}
                if (!"D".equals(type)) {  // 新增
                    saveOrUpdateOutDeliveryOrder(order, deliveryOrder,i);
                } else if ("D".equals(type)) { //删除
                	String hql = "FROM WmsPickTicket pick WHERE pick.relatedBill1 =:relatedBill1 ";
                	List<WmsPickTicket> picks = commonDao.findByQuery(hql, new String[]{"relatedBill1"}, new String[]{deliveryOrder.getCode()});
                	if(!picks.isEmpty()){
                		throw new BusinessException("已生成拣货单，不允许删除");
                	}
                	List<WmsDeliveryOrderDetail> details = commonDao.findByQuery(" FROM WmsDeliveryOrderDetail d WHERE d.deliveryOrder.sapCode=:sapCode AND d.deliveryOrder.project=:project ",  new String[]{"sapCode","project"}, new Object[]{order.getVBELN(),order.getPOSNR()});
            		for(WmsDeliveryOrderDetail detail : details){
            			commonDao.delete(detail);
            		}
            		if(deliveryOrder !=null ){
            			commonDao.delete(deliveryOrder);
            		}
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new BusinessException("类型:"+type + ",sap单条外向交货单处理失败。"+StringHelper.substring(CommonHelper.getErrorMessageByException(ex), 255));
            }
        }
	}
	
	public void dealSapReturnOrderCode(SapReturnOrderCodeArray srocs){
		System.out.println("开始处理 Sap交货单单号回传.........");
		String type = srocs.getTYPE();
		if(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_DOCODE.equals(type)){
			SapReturnOrderCode [] sros = srocs.getSapReturnOrderCodes();
			for(SapReturnOrderCode sro : sros){
				StringHelper.assertNullOrEmpty(sro.getWmsOrderCode(), "wmsOrderCode节点属性不能为空");
				String hql = "FROM WmsDeliveryOrder delOrder WHERE delOrder.code =:code ";
				WmsDeliveryOrder deliveryOrder = (WmsDeliveryOrder) commonDao.findByQueryUniqueResult(hql, 
						new String[]{"code"}, new Object[]{sro.getWmsOrderCode()});
				if(deliveryOrder == null){
					throw new BusinessException("交货单单号:"+sro.getWmsOrderCode()+"WMS中不存在");
				}
				deliveryOrder.setSapCode(sro.getSapCode());
				commonDao.store(deliveryOrder);
				
				//交货单 SAP单号回传给我们时  判断下交货单的接收确认状态， 如果是已接收 创建一个已接收的报文给供应商，如果是已确认状态 同时给供应商一个已接收和一个已确认的报文
				WmsDeliveryOrderManager doManager = (WmsDeliveryOrderManager) applicationContext.getBean("wmsDeliveryOrderManager");
				if(ConfirmStatus.RECEIVED.equals(deliveryOrder.getConfirmStatus())){
					doManager.createWms2SapInterfacelog(deliveryOrder, deliveryOrder.getConfirmStatus());
				}
				if(ConfirmStatus.CONFIRM.equals(deliveryOrder.getConfirmStatus())){
					doManager.createWms2SapInterfacelog(deliveryOrder, ConfirmStatus.RECEIVED);
					
					doManager.createWms2SapInterfacelog(deliveryOrder, ConfirmStatus.CONFIRM);
				}
				
				hql = "FROM Wms2SapInterfaceLog sapLog WHERE sapLog.request =:request ";
				List<Wms2SapInterfaceLog> w2sapLogs = commonDao.findByQuery(hql, 
						new String[]{"request"}, new Object[]{sro.getWmsOrderCode()});
				for(Wms2SapInterfaceLog w2sapLog :w2sapLogs){
					if(w2sapLog == null){
						throw new BusinessException("交货单:"+sro.getWmsOrderCode()+"报文不存在");
					}
					w2sapLog.setDealStatus(InterfaceLogStatus.STAT_FINISH);
					w2sapLog.setErrorLog(sro.getSapCode());
					w2sapLog.setResponseTime(new Date());
					commonDao.store(w2sapLog);
				}
			}
		}else {
			SapReturnOrderCode [] sros = srocs.getSapReturnOrderCodes();
			for(SapReturnOrderCode sro : sros){
				StringHelper.assertNullOrEmpty(sro.getWmsOrderCode(), "wmsOrderCode节点属性不能为空");
				String hql = "FROM TclWmsInventoryLedger l WHERE l.code =:code";
				List<TclWmsInventoryLedger> ledgers = commonDao.findByQuery(hql, "code", sro.getWmsOrderCode());
				if(ledgers.isEmpty()){
					throw new BusinessException("库存台账单号:"+sro.getWmsOrderCode()+"WMS中不存在");
				}
				for(TclWmsInventoryLedger ledger :ledgers){
					ledger.setSapCode(sro.getSapCode());
					commonDao.store(ledger);
				}
				System.out.println("-------------------代码执行--------------------------");
				hql = "FROM Wms2SapInterfaceLog sapLog WHERE sapLog.request =:request ";
				List<Wms2SapInterfaceLog> w2sapLogs = commonDao.findByQuery(hql, 
						new String[]{"request"}, new Object[]{sro.getWmsOrderCode()});
				for(Wms2SapInterfaceLog w2sapLog :w2sapLogs){
					if(w2sapLog == null){
						throw new BusinessException("单号:"+sro.getWmsOrderCode()+"报文不存在");
					}
					w2sapLog.setDealStatus(InterfaceLogStatus.STAT_FINISH);
					w2sapLog.setResponseTime(new Date());
					w2sapLog.setErrorLog(sro.getSapCode());
					commonDao.store(w2sapLog);
				}
			}
		}
	}
	
	private void saveOrUpdateOutDeliveryOrder(SapSaleOutDelivery order, WmsDeliveryOrder deliveryOrder,int i) {
		if(deliveryOrder.isNew()){
			deliveryOrder.setSapCode(order.getVBELN());
	        deliveryOrder.setCode(order.getVBELN());
	        deliveryOrder.setStatus(WmsDeliveryOrderStatus.OPEN);
		}
        
        //客户
        WmsCustomer customer = this.getWmsCustomerByCode(order.getKUNNR());
        if(customer == null){
        	customer = EntityFactory.getEntity(WmsCustomer.class);
        	WmsMasterGroup mg = commonDao.load(WmsMasterGroup.class, 1L);
        	customer.setMasterGroup(mg);
        	customer.setCode(order.getKUNNR());
        	customer.setName(order.getNAME1());
        	customer.setStatus(BaseStatus.ENABLED);
        	commonDao.store(customer);
        }
        deliveryOrder.setCustomer(customer);
        deliveryOrder.setDeliveryDate(DateUtil.formatDate(order.getWADAT()));
        deliveryOrder.setCreatedType(WmsDeliveryOrderCreatedType.SYSTEM);
        deliveryOrder.setProject(order.getPOSNR());
        deliveryOrder.setType(order.getLFART());
       
        String hql = "FROM WmsDeliveryOrderDetail dod WHERE dod.posnr =:posnr AND dod.deliveryOrder.code=:code ";
        WmsDeliveryOrderDetail detail = null;
        List<WmsDeliveryOrderDetail> details=commonDao.findByQuery(hql, new String[]{"posnr","code"}, new Object[]{order.getPOSNR(),order.getVBELN()});
        if(details.size()>1){
        	throw new BusinessException("根据交货单号"+order.getVBELN()+"和行项目"+order.getPOSNR()+"找到"+details.size()+"条交货单明细");
        }
        if(details.size()==1){
        	detail = details.get(0);
        }
        if(detail == null){
        	UpdateInfo updateInfo = new UpdateInfo();
            updateInfo.setCreatedTime(new Date());
            updateInfo.setCreator(order.getERNAM());
            deliveryOrder.setUpdateInfo(updateInfo);
            deliveryOrder.setBillTypeName(WmsDeliveryOrderBillType.XSBILLTYPE);
        	detail = EntityFactory.getEntity(WmsDeliveryOrderDetail.class);
        	detail.setLineNo(i); // 明细行号 当前单明细
        	detail.setPosnr(order.getPOSNR());//行项目
        }else{
	        WmsDeliveryOrder dOrder = commonDao.load(WmsDeliveryOrder.class, detail.getDeliveryOrder().getId());
	        if(!WmsDeliveryOrderStatus.OPEN.equals(dOrder.getStatus())) { //不等于打开 说明已经生成了拣配
	        	throw new BusinessException("WMS已生成拣配单，不允许修改");
	        }
        }
        updateDoOutDeliveryDetail(detail,deliveryOrder,order);
    }
	
	private void updateDoOutDeliveryDetail(WmsDeliveryOrderDetail detail,WmsDeliveryOrder deliveryOrder,SapSaleOutDelivery order) {
		detail.setDeliveryOrder(deliveryOrder);
        WmsItem item =this.getItemByCode(order.getMATNR());
        if (item==null) {
            throw new BusinessException("WMS物料【"+order.getMATNR()+"】未维护");
        }
        detail.setItem(item);
        WmsSapFactory factory = this.getSapFactoryByCode(order.getWERKS());
        Set<WmsDeliveryOrderDetail> details = deliveryOrder.getDetails();
    	for (WmsDeliveryOrderDetail dod : details) {
    		if(!factory.getId().equals(dod.getFactory().getId())){
    			throw new BusinessException("交货单明细的工厂必须一致");
    		}
    	}
        detail.setFactory(factory);
        this.checkItemFactory(item, factory);
        detail.setKcdd(order.getLGORT());
        detail.setPlanQuantityBu(Double.parseDouble(StringHelper.isNullOrEmpty(order.getLFIMG()) ? "0" : order.getLFIMG()));
        detail.setTheDeliveryQuantityBu(Double.parseDouble(StringHelper.isNullOrEmpty(order.getLFIMG()) ?"0":order.getLFIMG()));
        if(WmsDeliveryOrderStatus.OPEN.equals(detail.getDeliveryOrder().getStatus())){
        	detail.setDelivedQuantityBu(0D);
        }
        detail.setPoNo(order.getVGBEL());
        detail.setPoDetailNo(order.getVGPOS());
        WmsPackageUnit pku = this.getWmsPackageUnitByItem(item.getId(), order.getMEINS());
        detail.setPackageUnit(pku);
        //仓库
        String pstype = WmsFactoryXmlb.BZ;
        WmsWarehouse house = null;
        if(null!=pstype){
        	String houseHql = "SELECT wfw.warehouse FROM WmsFactoryWarehouse wfw where wfw.factory=:wf and wfw.type=:wt";
        	house = (WmsWarehouse)commonDao.findByQueryUniqueResult(houseHql, new String[]{"wf","wt"}, new Object[]{factory,pstype});
        }
        if(null!=house){
        	deliveryOrder.setWarehouse(house);
        	commonDao.store(deliveryOrder);
        	commonDao.store(detail);
        }
	}
	
	
	 
	
	private List<String> getXlsTitle(String type) {
		List<String> title = new ArrayList<String>();
		if(type.equals(WmsFactoryXmlb.JS)) {
			title.add("行号");
			title.add("项目类别");
			title.add("供应商");
			title.add("供应商描述");
			title.add("过帐日期");
			title.add("出库单号");
			title.add("出库行项目");
			title.add("工厂");
			title.add("采购组");
			title.add("物料号");
			title.add("物料描述");
			title.add("出库数量");
			title.add("出库单位");
			title.add("库位");
			title.add("移动类型");
			title.add("借贷标识");
			title.add("定价控制类别");
			title.add("税码");
			title.add("应执行单价");
			title.add("价格单位");
			title.add("价格有效从");
			title.add("价格有效到");
			title.add("应执行净额");
			title.add("应执行税额");
			title.add("应执行含税金额");
			title.add("结算标识");
			title.add("财务结算凭证");
			title.add("财务凭证行项目");
			title.add("结算数量");
			title.add("结算金额");
		}
		else if(type.equals(WmsFactoryXmlb.BZ)) {
			title.add("行号");
			title.add("项目类别");
			title.add("供应商");
			title.add("供应商描述");
			title.add("过帐日期");
			title.add("凭证日期");
			title.add("采购凭证");
			title.add("项目");
			title.add("工厂");
			title.add("采购组");
			title.add("物料号");
			title.add("物料描述");
			title.add("入库凭证");
			title.add("入库凭证行");
			title.add("入库数量");
			title.add("入库单位");
			title.add("库位");
			title.add("应执行单价");
			title.add("价格单位");
			title.add("价格有效从");
			title.add("价格有效到");
			title.add("应执行净额");
			title.add("应执行税额");
			title.add("应执行含税金额");
			title.add("过账发票数量");
			title.add("过账发票金额");
			title.add("预制发票数量");
			title.add("预制发票金额");
			title.add("发票数量总计");
			title.add("发票金额总计");
			title.add("送货单号");
		}
		return title;
	}
	
	private List<String> getXlsLineInfo(String type,SapJSCheckOrder sco,SapCheckOrder scobz) {
		List<String> scoInfos = new ArrayList<String>();
		if(type.equals(WmsFactoryXmlb.JS)) {
			scoInfos.add(sco.getLINENO());
			scoInfos.add(sco.getPSTYP());
			scoInfos.add(sco.getLIFNR());
			scoInfos.add(sco.getNAME1());
			scoInfos.add(sco.getBUDAT());
			scoInfos.add(sco.getMBLNR());
			scoInfos.add(sco.getZEILE());
			scoInfos.add(sco.getWERKS());
			scoInfos.add(sco.getEKGRP());
			scoInfos.add(sco.getMATNR());
			scoInfos.add(sco.getMAKTX());
			scoInfos.add(sco.getMENGE());
			scoInfos.add(sco.getBPRME());
			scoInfos.add(sco.getLGORT());
			scoInfos.add(sco.getBWART());
			scoInfos.add(sco.getSHKZG());
			scoInfos.add(sco.getMEPRF());
			scoInfos.add(sco.getMWSKZ());
			scoInfos.add(sco.getNETPR1());
			scoInfos.add(sco.getPEIN2());
			scoInfos.add(sco.getDATAB());
			scoInfos.add(sco.getDATBI());
			scoInfos.add(sco.getNETWR1());
			scoInfos.add(sco.getBTAXA());
			scoInfos.add(sco.getETAXA());
			scoInfos.add(sco.getJSBS());
			scoInfos.add(sco.getBELNR());
			scoInfos.add(sco.getBUZEI());
			scoInfos.add(sco.getBSTMG());
			scoInfos.add(sco.getWRBTR());
		}
		else if(type.equals(WmsFactoryXmlb.BZ)) {
			scoInfos.add(scobz.getLINENO());
			scoInfos.add(scobz.getPSTYP());
			scoInfos.add(scobz.getLIFNR());
			scoInfos.add(scobz.getNAME1());
			scoInfos.add(scobz.getBUDAT());
			scoInfos.add(scobz.getBLDAT());
			scoInfos.add(scobz.getEBELN());
			scoInfos.add(scobz.getEBELP());
			scoInfos.add(scobz.getWERKS());
			scoInfos.add(scobz.getEKGRP());
			scoInfos.add(scobz.getMATNR());
			scoInfos.add(scobz.getMAKTX());
			scoInfos.add(scobz.getMBLNR());
			scoInfos.add(scobz.getZEILE());
			scoInfos.add(scobz.getMENGE());
			scoInfos.add(scobz.getBPRME());
			scoInfos.add(scobz.getLGORT());
			scoInfos.add(scobz.getNETPR1());
			scoInfos.add(scobz.getPEIN2());
			scoInfos.add(scobz.getDATAB());
			scoInfos.add(scobz.getDATBI());
			scoInfos.add(scobz.getNETWR1());
			scoInfos.add(scobz.getBTAXA());
			scoInfos.add(scobz.getETAXA());
			scoInfos.add(scobz.getBPMNG1());
			scoInfos.add(scobz.getDMBTR1());
			scoInfos.add(scobz.getBPMNG2());
			scoInfos.add(scobz.getDMBTR2());
			scoInfos.add(scobz.getBPMNG3());
			scoInfos.add(scobz.getDMBTR3());
			scoInfos.add(scobz.getXBLNR());
		}
		
		
		return scoInfos;
	}
	
	public void dealSapJSCheckOrder(SapJSCheckOrderArray scoas){
		System.out.println("开始处理Sap寄售对账单..................");
		List<String> title = getXlsTitle(WmsFactoryXmlb.JS);
		
		String excelPath = GlobalParamUtils.getGloableStringValue("checkOrderDir");
//		excelPath="/Users/administrator/";
		Map<String,List<List<String>>> supMap = new HashMap<String, List<List<String>>>();
		for(SapJSCheckOrder sco : scoas.getScos()){
			
			String key = sco.getLIFNR()+"###"+StringHelper.substring(sco.getBUDAT(), 6);//文件名
			List<String> scoInfos = getXlsLineInfo(WmsFactoryXmlb.JS, sco,null);
			if(!supMap.containsKey(key)) {
				supMap.put(key, new ArrayList<List<String>>());
				supMap.get(key).add(title);
			}
			supMap.get(key).add(scoInfos);
		}
		Set<String> keys  = supMap.keySet();
		for(String key : keys) {
			
			String[] info = key.split("###");
			String supcode = info[0];
			String rq = info[1];
			

			WmsCheckOrder order = this.getWmsCheckOrder(supcode,rq);
			if(order == null){
				//新增
				order = EntityFactory.getEntity(WmsCheckOrder.class);
			}
			WmsSupplier supplier = (WmsSupplier) commonDao.findByQueryUniqueResult("FROM WmsSupplier WHERE code=:code", 
	                "code", supcode);
			if (supplier==null) {
				throw new BusinessException("WMS供应商【"+supcode+"】不存在"+"日期:"+rq);
			}
			
			String fullFileName = excelPath+key.replaceAll("###", "_")+"_js.xls";
			
			order.setFilepath(fullFileName);
			order.setCode(rq);
			order.setStatus(WmsCheckOrderStatus.OPEN);
			order.setXmlb(WmsFactoryXmlb.JS);
			order.setSupplier(supplier);
		 
			commonDao.store(order);
			
			order.setFilename("<a href='*.tclFileDowload?type=CHECKORDER&id="+CommonHelper.tclBase64Encode(order.getId()+"")+"' target='_blank'>下载附件</a>");
			
			
			ExcelHelper.write2xls(supMap.get(key), fullFileName);
			

			SapJSCheckOrder sco = new SapJSCheckOrder(); //为了发邮件临时组件一个对象
			sco.setLIFNR(supcode);
			sco.setBUDAT(rq);
			createEmailRecord(supplier, sco);//生成邮件
			
		}
		
	}
	public void dealSapCheckOrder(SapCheckOrderArray scoas){
		System.out.println("开始处理Sap标准对账单..................");
		
		List<String> title = getXlsTitle(WmsFactoryXmlb.BZ);
		
		String excelPath = GlobalParamUtils.getGloableStringValue("checkOrderDir");
//		excelPath="/Users/administrator/";
		Map<String,List<List<String>>> supMap = new HashMap<String, List<List<String>>>();
		for(SapCheckOrder sco : scoas.getScos()){
			
			String key = sco.getLIFNR()+"###"+StringHelper.substring(sco.getBUDAT(), 6);
			List<String> scoInfos =getXlsLineInfo(WmsFactoryXmlb.BZ, null,sco); ///////注意传参的位置
			
			if(!supMap.containsKey(key)) {
				supMap.put(key, new ArrayList<List<String>>());
				supMap.get(key).add(title);
			}
			supMap.get(key).add(scoInfos);
		}
		
		
		
		
		Set<String> keys  = supMap.keySet();
		for(String key : keys) {
			
			String[] info = key.split("###");
			String supcode = info[0];
			String rq = info[1];
			
			WmsCheckOrder order = this.getWmsCheckOrder(supcode,rq);
			if(order == null){
				//新增
				order = EntityFactory.getEntity(WmsCheckOrder.class);
			}
			WmsSupplier supplier = (WmsSupplier) commonDao.findByQueryUniqueResult("FROM WmsSupplier WHERE code=:code", 
	                "code", supcode);
			if (supplier==null) {
				throw new BusinessException("WMS供应商【"+supcode+"】不存在"+"日期:"+rq);
			}
			
			String fullFileName = excelPath+key.replaceAll("###", "_")+"_bz.xls";
			
			order.setFilepath(fullFileName);
			order.setCode(rq);
			order.setStatus(WmsCheckOrderStatus.OPEN);
			order.setXmlb(WmsFactoryXmlb.BZ);
			order.setSupplier(supplier);
			
			
			commonDao.store(order);
			order.setFilename("<a href='*.tclFileDowload?type=CHECKORDER&id="+CommonHelper.tclBase64Encode(order.getId()+"")+"' target='_blank'>下载附件</a>");
			
			ExcelHelper.write2xls(supMap.get(key), fullFileName);
			
			
			SapCheckOrder sco = new SapCheckOrder(); //为了发邮件临时组件一个对象
			sco.setLIFNR(supcode);
			sco.setBUDAT(rq);
			createEmailRecord(supplier, sco);//生成邮件
			
		}
	}
	
	public void dealSapProductOrderIn(SapProductOrderInArray spoias){
		System.out.println("开始处理Sap生产订单入库单..................");
		Map<Long,Double> orderDetails = new HashMap<Long, Double>();//生产订单明细
		WmsWarehouse warehouse = null;
		WmsASN asn = EntityFactory.getEntity(WmsASN.class);
		StringHelper.assertNullOrEmpty(spoias.getTYPE(), "TYPE属性不能为空");
		String type = spoias.getTYPE();
		int i =0;
		for(SapProductOrderIn spois : spoias.getSpois()){
			StringHelper.assertNullOrEmpty(spois.getMENGE02(), "MENGE02属性不能为空");
			try{
				String request = spois.getMBLNR()+"_"+spois.getAUFNR();//关键信息
				String checkhql = "SELECT COUNT(i.id) FROM InterfaceLog i WHERE i.request=:request " +
						"AND (i.status !=:status OR i.status IS NULL ) AND i.errorLog IS NULL ";
				Long inters = (Long) commonDao.findByQueryUniqueResult(checkhql, new String[]{"request","status"}, 
						new Object[]{request,InterfaceLogStatus.STAT_FAIL});
				if(inters>1){
					throw new BusinessException("WMS存在未处理的报文或在WMS系统已成功出库，不允许再次出库");
				}
				WmsItem item = this.getItemByCode(spois.getMATNR());
				if(item==null){
					throw new BusinessException("根据编码"+spois.getMATNR()+"未找到物料");
				}
				
				
				if(!WmsItemJITAtt.JIT_DOWNLINE_SETTLE.equals(item.getUserFieldV2())){
					throw new BusinessException("不是JIT下线结算物料");
				}
				
				//校验接口传过来的JIT下线料的条数是否和工单中JIT下线料条数一致
				String itemHql = "FROM ProductionOrderDetail detail WHERE detail.productionOrder.code =:code " +
						"AND detail.item.userFieldV2 =:userFieldV2 AND detail.deleteFlag is null ";
				String itemHql2 = " AND detail.planQuantityBu-detail.shippedQuantityBu >0 ";
				List<ProductionOrderDetail> details = new ArrayList<ProductionOrderDetail>();
				if("01".equals(type)){
					details = commonDao.findByQuery(itemHql+itemHql2, new String[]{"code","userFieldV2"},
							new Object[]{spois.getAUFNR(),WmsItemJITAtt.JIT_DOWNLINE_SETTLE});
				}
				if("02".equals(type)){
					details = commonDao.findByQuery(itemHql, new String[]{"code","userFieldV2"},
							new Object[]{spois.getAUFNR(),WmsItemJITAtt.JIT_DOWNLINE_SETTLE});
				}
				
				if(!Integer.valueOf(spoias.getROWCNT().trim()).equals(details.size())){
					throw new BusinessException("生产订单中有"+details.size()+"条JIT下线料,接口传输为"+spoias.getROWCNT().trim()+"条");
				}
				
				
				String hql = "FROM ProductionOrderDetail detail WHERE detail.productionOrder.code =:code " +
						"AND detail.item.code =:itemCode AND detail.deleteFlag is null ";
				ProductionOrderDetail detail = (ProductionOrderDetail) commonDao.findByQueryUniqueResult(hql, new String[]{"code","itemCode"}, 
						new Object[]{spois.getAUFNR(),spois.getMATNR()});
				if(detail == null){
					throw new BusinessException("未找到对应的生产订单号："+spois.getAUFNR()+"和物料："+spois.getMATNR());
				}
				ProductionOrder productionOrder = commonDao.load(ProductionOrder.class, detail.getProductionOrder().getId());
				/**获取仓库*/
				if(i == 0){
					WmsFactoryWarehouse fFactoryWarehouse =  findWmsFactoryWarehouse(productionOrder.getFactory().getId());
					if (null == fFactoryWarehouse) {
						throw new BusinessException("factory.warehouse.not.found.by.factory", 
								new String[]{productionOrder.getFactory().getName()});
					}
					warehouse = fFactoryWarehouse.getWarehouse();
					i = 1;
				}
				if("01".equals(type)){ //工单入库 
					Double qty = DoubleUtils.round(detail.getPlanQuantityBu()/productionOrder.getPlanQuantity()*Double.valueOf(spois.getMENGE()),3);
					if(!CommonHelper.dealDoubleError(qty).equals(CommonHelper.dealDoubleError(Double.valueOf(spois.getMENGE02())))){
						throw new BusinessException("数量不正确");
					}
					productionOrder.setItemTag(spois.getMBLNR());
					commonDao.store(productionOrder);
					if(!orderDetails.containsKey(detail.getId())){
						orderDetails.put(detail.getId(), qty);
					}
				}else if("02".equals(type)){ //工单取消入库  生成ASN
					//找出库的批次信息
			        hql = "FROM WmsTask task WHERE task.productionDetailId =:productionDetailId ";
			        List<WmsTask> tasks = commonDao.findByQuery(hql, "productionDetailId", detail.getId());
			        if(tasks.isEmpty()){
			        	throw new BusinessException(productionOrder.getCode()+"生产订单未发过货,不能取消入库");
			        }
			        Double qty = detail.getPlanQuantityBu()/productionOrder.getPlanQuantity()*Double.valueOf(spois.getMENGE());
					if(!qty.equals(Double.valueOf(spois.getMENGE02()))){
						throw new BusinessException("数量不正确");
					}
					//取消入库要把工单明细额的发货数量冲销回去
					detail.subAllocatedQuantityBu(qty);
					detail.subPickedQuantityBu(qty);
					detail.subShippedQuantityBu(qty);
					detail.defineStatus();
					commonDao.store(detail);
					if(asn.isNew()){
						WmsBillType billType = (WmsBillType) commonDao.findByQueryUniqueResult("FROM WmsBillType WHERE code=:code", 
				                  "code", WmsAsnGenType.JITRKD);
						if(billType == null){
							throw new BusinessException("未维护JIT下线入库单单据类型");
						}
						String code = wmsBussinessCodeManager.generateCodeByRule(warehouse, billType.getCode()); 
						asn.setWarehouse(warehouse);
						asn.setBillType(billType);
						asn.setCode(code);
						asn.setCompany(getDefaultWmsCompany());
						asn.setCustomerBill(productionOrder.getCode());
						asn.setOrderDate(new Date());
						asn.setStatus(WmsASNStatus.OPEN);
						asn.setUserField7(productionOrder.getFactory().getCode());
						List<WmsLocation> locs = commonDao.findByQuery("FROM WmsLocation receiveLocation "
				                  + "WHERE receiveLocation.status = 'ENABLED' "
				                  + "AND receiveLocation.type = 'STORAGE' "
				                  + "AND receiveLocation.code = '"+WmsLocationCode.JIT+"' "
				                  + "AND receiveLocation.warehouse.id = :warehouseId", 
				                  "warehouseId", warehouse.getId());
				          
				        if (locs!=null && locs.size()>0) {
				        	asn.setReceiveLocation(locs.get(0));
				        } else {
				            throw new BusinessException("当前仓库未维护JIT存货库位!");
				        }
					}
					WmsASNDetail asnDetail = EntityFactory.getEntity(WmsASNDetail.class);
					asnDetail.setLineNo(i*10);
					asnDetail.setItem(item);
					
					this.checkItemFactory(item, productionOrder.getFactory());
					
					asnDetail.setPackageUnit(detail.getPackageUnit());
					asnDetail.setExpectedQty(qty);
			        asnDetail.setExpectedPackQty(qty);
			        asnDetail.setLotInfo(tasks.get(0).getItemKey().getLotInfo());
			        asnDetail.setUserField2(detail.getId().toString());//记录工单明细ID
			        asnDetail.setInventoryStatus("合格");
			        asn.setUserField5(tasks.get(0).getItemKey().getLotInfo().getExtendPropC8());
			        WmsSupplier supplier = this.getSupplierByCode(tasks.get(0).getItemKey().getLotInfo().getSupplierCode());
			        if(supplier == null){
			        	throw new BusinessException("供应商"+tasks.get(0).getItemKey().getLotInfo().getSupplierCode()+"系统中不存在");
			        }
			        asn.setSupplier(supplier);
			        asnDetail.setAsn(asn);
			        asn.addDetail(asnDetail);
			        commonDao.store(asn);
			        commonDao.store(asnDetail);
			        asn.refreshQtyBU();
			        i++;
				}
			}catch(Exception e){
				e.printStackTrace();
				throw new BusinessException("物料凭证号："+spois.getMBLNR()+"生产订单号："+spois.getAUFNR()+"sap生产订单入库单处理失败"+StringHelper.substring(CommonHelper.getErrorMessageByException(e),255)); 
			}
		}
		if("01".equals(type)){
			/**生成JIT出库单*/
			WmsWorkDoc workDoc = new WmsWorkDoc();
			WmsTclWorkDocManager tclWorkDocManager = (WmsTclWorkDocManager) applicationContext.getBean("wmsTclWorkDocManager");
			workDoc = tclWorkDocManager.newWorkDoc(null, workDoc,WmsItemJITAtt.JIT_DOWNLINE_SETTLE,warehouse);
			for(Entry<Long, Double> s :orderDetails.entrySet()){
				ProductionOrderDetail d = commonDao.load(ProductionOrderDetail.class, s.getKey());
				workDoc.setRelatedBillCode(d.getProductionOrder().getCode());
				tclWorkDocManager.dealProductOrderDetail(d, s.getValue(), workDoc.getId());
			}
			//生效
			workDoc.setStatus(WmsWorkDocStatus.ENABLED);
			tclWorkDocManager.activeQuickShippingWorkDoc(workDoc);
			//JIT下线结算料，系统自动做出库
			tclWorkDocManager.shipJitOrder(workDoc);
		}
		if("02".equals(type)){//生成ASN并自动收货上架, 同时给SAP传生产退料数据以及退供应商出库数据
			workflowManager.doWorkflow(asn,"wmsASNProcess.active");
			WmsTclASNManager wmsTclAsnManager = (WmsTclASNManager) applicationContext.getBean("wmsTclASNManager");
			
			//此方法由edi来跑 edi没有session调用规则会报错，此处设置session
			BussinessModelHolder.setThornBusinessModel(asn.getWarehouse().getBaseOrganization().getBusinessModel());
			BaseOrganizationHolder.setThornBaseOrganization(asn.getWarehouse().getBaseOrganization());
			wmsTclAsnManager.receiveAll(asn.getId(),0L);
			//收货完成清空刚才设置的session
			BussinessModelHolder.setThornBusinessModel(null);
			BaseOrganizationHolder.setThornBaseOrganization(null);
		}
	}
	
	public void createWmsPickTicketByProductionOrderDetail(ProductionOrderDetail detail,Double quantity){
		ProductionOrder productionOrder = commonDao.load(ProductionOrder.class, detail.getProductionOrder().getId());
		WmsFactoryWarehouse fFactoryWarehouse = findWmsFactoryWarehouse(productionOrder.getFactory().getId());
		if (null == fFactoryWarehouse) {
			throw new BusinessException("factory.warehouse.not.found.by.factory", new String[]{productionOrder.getFactory().getName()});
		}
		String hql = "from WmsPickTicket pick where pick.relatedBill1 =:relatedBill1 and pick.userField1 =:userField1 ";
		WmsPickTicket pickTicket = (WmsPickTicket) commonDao.findByQueryUniqueResult(hql, new String[]{"relatedBill1","userField1"}, new Object[]{productionOrder.getCode(),productionOrder.getItemTag()});
		if(pickTicket == null){
			pickTicket = productionOrderManager.createWmsPickTicketByProductionOrder(fFactoryWarehouse.getWarehouse(), productionOrder.getBeginDate(),WmsPickticketGenType.SCLLD);
		}
		WmsPickTicketDetail pickTicketDetail = productionOrderManager.creatWmsPickTicketDetail(pickTicket, detail.getItem(), detail.getPackageUnit(), quantity);
		productionOrderManager.storeProductionOrderDetailPtDetail(detail, pickTicketDetail,quantity);
		
		productionOrder.setBeCreatPt(Boolean.TRUE);
		pickTicket.setRelatedBill1(productionOrder.getCode());
		pickTicket.setUserField1(productionOrder.getItemTag());
		this.commonDao.store(pickTicket);
		this.commonDao.store(productionOrder);
	}
	
	public String getMaxLineNoByPickTicketDetail(Long pickTicketId) {
		Integer lineNo = (Integer) commonDao.findByQueryUniqueResult("SELECT MAX(detail.lineNo) FROM WmsPickTicketDetail detail WHERE detail.pickTicket.id = :pickTicketId", 
				new String[] {"pickTicketId"}, new Object[] {pickTicketId});
		if (lineNo == null || lineNo.intValue() == 0) {
			lineNo = 1;
		} else {
			lineNo += 1;
		}

		return ""+lineNo;
	}
	
	public WmsFactoryWarehouse findWmsFactoryWarehouse(Long factoryId){
		String hql = "  FROM WmsFactoryWarehouse fw WHERE fw.factory.id =:factoryId "
						+ " AND fw.type =:type";
		return  (WmsFactoryWarehouse) this.commonDao.findByQueryUniqueResult(hql, new String[]{"factoryId","type"}, new Object[]{factoryId,WmsFactoryXmlb.BZ});
		
	}
	
}