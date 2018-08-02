package com.vtradex.wms.server.service.pickticket.pojo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.client.ui.page.allocate.page.AllocateConstant;
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomInventory;
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomItem;
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomPackageUnit;
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomPickTicket;
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomPickTicketDetail;
import com.vtradex.wms.server.model.component.LotInfo;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemKey;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetailRequire;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.pickticket.WmsPickticketClientCustomerManager;
import com.vtradex.wms.server.service.pickticket.WmsPickticketManager;
import com.vtradex.wms.server.utils.NewLotInfoParser;

/**
 * 
 * TCL 定制化 手工分配管理
 * 
 * @author <a href="zhen.lei@vtradex.com">Yogurt_lei</a>
 * 
 * @date 2017年7月19日 上午10:54:29
 */
public class DefaultWmsTclPickticketClientManager extends DefaultWmsPickticketClientManager {

    public DefaultWmsTclPickticketClientManager (
            WorkflowManager workflowManager,
            WmsInventoryManager inventoryManager,
            WmsPickticketManager wmsPickticketManager,
            WmsPickticketClientCustomerManager wmsPickticketClientCustomerManager) {
        super(workflowManager, inventoryManager, wmsPickticketManager,
                wmsPickticketClientCustomerManager);
    }
    /**
     * 重写手工分配客户化屏  增加显示 工单号
     */
    public Map findAvailableInventories(Map params) {
		Map result = new HashMap();
		Boolean isClearInventories = (Boolean)params.get(AllocateConstant.IS_CLEAR_INVENTORIES);
		if(isClearInventories) {
			return result;
		}
		Long pickDetailId = (Long)params.get(AllocateConstant.ID);
		WmsPickTicketDetail detail = commonDao.load(WmsPickTicketDetail.class, pickDetailId);
		WmsPickTicket pickTicket = commonDao.load(WmsPickTicket.class, detail.getPickTicket().getId());
		Boolean isFitAsLot = (Boolean)params.get(AllocateConstant.IS_FIT_AS_LOT);
		//Boolean containLockInv = (Boolean)params.get(AllocateConstant.CONTAIN_LOCK_INV);
		StringBuffer hqlBuffer = new StringBuffer();
		//库存查询hql客户化类
		WmsPickticketClientCustomerManager wmsPickticketClientCustomerManager = (WmsPickticketClientCustomerManager) applicationContext.getBean("wmsPickticketClientCustomerManager");
		hqlBuffer = wmsPickticketClientCustomerManager.findAvailableInventoriesHql(hqlBuffer);
		
		Map<String, String> queryPars = (Map<String, String>)params.get(AllocateConstant.QUERY_PARAMS);
		if(queryPars != null && queryPars.size() > 0) {
			for(String key : queryPars.keySet()) {
				if(key.equals("locationCode")) {
					hqlBuffer.append(" AND inventory.location.code LIKE '" + queryPars.get(key) + "'");
				} else if(key.equals("inventoryStatus")){
					hqlBuffer.append(" AND inventory.status LIKE '" + queryPars.get(key) + "'");
				}
			}
		}
		
		List<String> paramNameList = new ArrayList<String>();
		paramNameList.add("itemId");
		paramNameList.add("warehouseId");
		List<Object> valueList = new ArrayList<Object>();
		valueList.add(detail.getItem().getId());
		valueList.add(pickTicket.getWarehouse().getId());
		if(isFitAsLot) {
			List<WmsPickTicketDetailRequire> requires = this.getPickTicketDetailRequire(detail.getId());
			
			for(WmsPickTicketDetailRequire require:requires){
				if("STORAGE_DATE".equals(require.getLotKey()) || "PRODUCT_DATE".equals(require.getLotKey()) || "EXPIRE_DATE".equals(require.getLotKey())||"QA_DATE".equals(require.getLotKey())){
					hqlBuffer
					.append(" AND ")
					.append(NewLotInfoParser.decryptStringOfLotDate(this.changeProToObj(require.getLotKey()), this.joinRequire(require), require.getQueryRequire(),paramNameList,valueList));
				}else{
					hqlBuffer
					.append(" AND ")
					.append(NewLotInfoParser.decryptStringOfLot(this.changeProToObj(require.getLotKey()), this.joinRequire(require), require.getQueryRequire()));
				}
			}
		}
		
		final String hql = hqlBuffer.toString();
		String[] paramNames=new String[paramNameList.size()];
		int i = 0;
		for(String paramName:paramNameList){
			paramNames[i] = paramName;
			i++;
		}
		List<WmsInventory> inventories = commonDao.findByQuery(hql, 
				paramNames, 
				valueList.toArray());
		
		List<CustomInventory> customInventories = new ArrayList<CustomInventory>();
		for(WmsInventory inventory : inventories) {
			CustomInventory customInventory = buildCustomInventory(inventory);
			customInventories.add(customInventory);
		}
		CustomPickTicketDetail customPickTicketDetail = bulidcustomPickTicketDetail(detail);
		customPickTicketDetail.setFitAsLot(isFitAsLot);
		result.put(AllocateConstant.CLIENT_ENTITY,customPickTicketDetail);
		result.put(AllocateConstant.AVAILABLE_RESULT, customInventories);
		return result;
	}
    private CustomInventory buildCustomInventory(WmsInventory inventory) {
		CustomInventory customInventory = new CustomInventory();
		CustomItem customItem = new CustomItem();
		CustomPackageUnit customPackageUnit = new CustomPackageUnit();
		
		WmsItemKey itemKey = commonDao.load(WmsItemKey.class, inventory.getItemKey().getId());
		WmsItem item = commonDao.load(WmsItem.class, itemKey.getItem().getId());
		WmsLocation location = commonDao.load(WmsLocation.class, inventory.getLocation().getId());
		LotInfo lot = itemKey.getLotInfo();
		String supplierName = "";
		String lotInfo = "-";
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String storageDateStr=null;
		String productDateStr=null;
		String expireDateStr =null;
		String qaDateStr = null;
		if(lot != null) {
			if (lot.getStorageDate() != null && !"".equals(lot.getStorageDate())) {
				storageDateStr = sf.format(lot.getStorageDate());
			}
			if (lot.getProductDate() != null && !"".equals(lot.getProductDate())) {
				productDateStr = sf.format(lot.getProductDate());
			}
			if (lot.getExpireDate() != null && !"".equals(lot.getExpireDate())) {
				expireDateStr = sf.format(lot.getExpireDate());
			}
			if (lot.getQaDate() != null && !"".equals(lot.getQaDate())) {
				qaDateStr = sf.format(lot.getQaDate());
			}
			lotInfo = customInventory.toLotInfor(lot.getLot(),storageDateStr,
					productDateStr,expireDateStr,qaDateStr,lot.getSoiCode(),
				lot.getAsnCustomerBill(),supplierName,lot.getSerialNo(),lot.getErpCode(),
				lot.getExtendPropC1(), lot.getExtendPropC2(), lot.getExtendPropC3(), 
				lot.getExtendPropC4(), lot.getExtendPropC5(), lot.getExtendPropC6(), 
				lot.getExtendPropC7(), lot.getExtendPropC8(), lot.getExtendPropC9(), 
				lot.getExtendPropC10(), lot.getExtendPropC11(), lot.getExtendPropC12(), 
				lot.getExtendPropC13(), lot.getExtendPropC14(), lot.getExtendPropC15(), 
				lot.getExtendPropC16(), lot.getExtendPropC17(), lot.getExtendPropC18(), 
				lot.getExtendPropC19(), lot.getExtendPropC20());
		}
		if(lot != null && lot.getSupplierCode() != null) {
			supplierName = lot.getSupplierCode();
		}
		
		WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, inventory.getPackageUnit().getId());

		customItem.setId(item.getId());
		customItem.setCode(item.getCode());
		customItem.setName(item.getName());
		
		customPackageUnit.setId(packageUnit.getId());
		customPackageUnit.setUnit(packageUnit.getUnit());
		customPackageUnit.setConvertFigure(packageUnit.getConvertFigure());
		
		customInventory.setId(inventory.getId());
		customInventory.setAvailableQuantity(inventory.getQty());
		customInventory.setCustomItem(customItem);
		customInventory.setCustomPackageUnit(customPackageUnit);
		customInventory.setLocationCode(location.getCode());
		customInventory.setLotInfo(lotInfo);
		customInventory.setQuantity(inventory.getQty());
		customInventory.setStatus(inventory.getStatus());
		customInventory.setPallet(inventory.getPallet());  //增加托盘号
		customInventory.setProductCode(lot.getExtendPropC13());
		
		String lockStatus = LocalizedMessage.getLocalizedAllMessage("BaseBooleanStatus."+(inventory.getLockStatus()==true?"Y":"N"),
				UserHolder.getLanguage());
		customInventory.setLockStatus(lockStatus);//增加锁定状态
		String InOutLock = LocalizedMessage.getLocalizedAllMessage("WmsInOutLockType."+location.getInOutLock(),
				UserHolder.getLanguage());
		customInventory.setInOutLock(InOutLock);//增加出入库锁
		return customInventory;
	}
    private CustomPickTicketDetail bulidcustomPickTicketDetail(WmsPickTicketDetail detail) {
		CustomPickTicketDetail customPickTicketDetail = new CustomPickTicketDetail();
		CustomItem customItem = new CustomItem();
		CustomPackageUnit customPackageUnit = new CustomPackageUnit();
		
		WmsItem item = commonDao.load(WmsItem.class, detail.getItem().getId());
		customItem.setId(detail.getItem().getId());
		customItem.setCode(item.getCode());
		customItem.setName(item.getName());
		
		WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, detail.getPackageUnit().getId());
		customPackageUnit.setId(packageUnit.getId());
		customPackageUnit.setUnit(packageUnit.getUnit());
		customPackageUnit.setConvertFigure(packageUnit.getConvertFigure());
		
		WmsPickTicket pickTicket = commonDao.load(WmsPickTicket.class, detail.getPickTicket().getId());
		CustomPickTicket customPickTicket = buildCustomMoveDoc(pickTicket);
		
		customPickTicketDetail.setId(detail.getId());
		customPickTicketDetail.setCustomPickTicket(customPickTicket);
		customPickTicketDetail.setCustomItem(customItem);
		customPickTicketDetail.setCustomPackageUnit(customPackageUnit);
		customPickTicketDetail.setPlanQuantity(detail.getExpectedPackQty());
		customPickTicketDetail.setPlanQuantityBU(detail.getExpectedQty());
		customPickTicketDetail.setAllocatedQuantityBU(detail.getAllocatedQty());
		customPickTicketDetail.setMovedQantityBU(detail.getPickedQty());
		customPickTicketDetail.setInventoryStatus(detail.getInventoryStatus());//增加显示库存状态
		String shipLotInfo = "";
		
		List<WmsPickTicketDetailRequire> requires = this.getPickTicketDetailRequire(detail.getId());
		
		for(WmsPickTicketDetailRequire require:requires){
			shipLotInfo += "#" + require.getLotValue1();
		}
		
		if(StringUtils.isEmpty(shipLotInfo)){
			shipLotInfo = "-";
		}
		customPickTicketDetail.setShipLotInfo(shipLotInfo);
		return customPickTicketDetail;
	}
    
    private CustomPickTicket buildCustomMoveDoc(WmsPickTicket pickTicket) {
		CustomPickTicket customPickTicket = new CustomPickTicket();
		customPickTicket.setId(pickTicket.getId());
		customPickTicket.setCode(pickTicket.getCode());
		customPickTicket.setStatus(pickTicket.getStatus());
		customPickTicket.setPlanQuantityBU(pickTicket.getQty());
		customPickTicket.setAllocatedQuantityBU(pickTicket.getAllocateQty());
		customPickTicket.setMovedQuantityBU(pickTicket.getPickQty());
		customPickTicket.setShippedQuantityBU(pickTicket.getShipQty());
		return customPickTicket;
	}
}
