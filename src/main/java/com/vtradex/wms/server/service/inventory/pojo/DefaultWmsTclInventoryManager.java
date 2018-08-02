package com.vtradex.wms.server.service.inventory.pojo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.wms.server.model.dto.WmsInventoryDto;
import com.vtradex.wms.server.model.entity.base.WmsBarCodeRepPrintRecord;
import com.vtradex.wms.server.model.entity.base.WmsFactoryXmlb;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetail;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;
import com.vtradex.wms.server.model.enums.WmsInventoryLogType;
import com.vtradex.wms.server.model.enums.WmsInventoryOperationStatus;
import com.vtradex.wms.server.model.enums.WmsLocationCode;
import com.vtradex.wms.server.model.enums.WmsPickticketGenType;
import com.vtradex.wms.server.service.base.WmsLocationManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryLogManager;
import com.vtradex.wms.server.service.inventory.WmsTclInventoryManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.utils.StringHelper;

public class DefaultWmsTclInventoryManager extends DefaultWmsInventoryManager implements WmsTclInventoryManager{

	protected WmsInventoryLogManager wmsInventoryLogManager;
	public DefaultWmsTclInventoryManager(
			WmsInventoryLogManager wmsInventoryLogManager,
			WmsRuleManager wmsRuleManager,
			WmsLocationManager wmsLocationManager,
			WorkflowManager workFlowManager) {
		super(wmsInventoryLogManager, wmsRuleManager, wmsLocationManager,
				workFlowManager);
		this.wmsInventoryLogManager=wmsInventoryLogManager;
		
	}
	/**获取退拣库位*/
	private WmsLocation getTjLocation(Long wareHouseId) {
		String hql = "from WmsLocation w "
				+ "where w.code=:code and status='ENABLED' and w.warehouse.id=:id";
		
		List<WmsLocation> locations = commonDao.findByQuery(hql,new String[]{"code","id"},new Object[]{WmsLocationCode.TJ,wareHouseId});
		if(locations.size() <= 0){
			throw new BusinessException("目的仓库没有维护"+WmsLocationCode.TJ+"库位,请检查!!");
		}
		return locations.get(0);
		
	}
	
	@Override
	public void backUp(WmsInventory inventory, Double backUpQty) {
		// TODO Auto-generated method stub
		if (inventory.getQty() < backUpQty) {
			throw new BusinessException("库存数量大于退拣数量");
		}
		//库存类型是寄售的直接退拣到VMI库
		WmsWarehouse wh = inventory.getLocation().getWarehouse();
		if(WmsFactoryXmlb.JS.equals(inventory.getItemKey().getLotInfo().getExtendPropC8())){
			String hql = "FROM WmsWarehouse warehouse WHERE warehouse.code='VMI'";
			wh = (WmsWarehouse)commonDao.findByQueryUniqueResult(hql, "", "");
		}
		WmsLocation tjLoc = getTjLocation(wh.getId());//退拣库位
		
		String descript="退拣";
		//移出库位库存扣减
        out(inventory,backUpQty,WmsInventoryLogType.BACKUP,descript);
        //移入库位库存新增
        
        
        
        //移入库位新增
        WmsInventoryDto inventoryDto = this.getWmsInventoryDto(inventory, backUpQty);
        inventoryDto.setWarehouse(wh);
        inventoryDto.setLocation(tjLoc);
        inventoryDto.setRelatedBillCode(null);
        inventoryDto.setRelatedBillType(null);
        inventoryDto.setType(WmsInventoryLogType.BACKUP);
        inventoryDto.setDescript(descript);
        inventoryDto.setInvRelatedBillCode(null);
        in(inventoryDto);
        
//		
//		
//		if(inventory.getQty().doubleValue() == backUpQty){
//			inventory.setRelatedBillCode(null);
//			inventory.setRelatedBillType(null);
//			inventory.setOperationStatus(WmsInventoryOperationStatus.NORMAL);
//			
//			inventory.setLocation(tjLoc);//放到退拣库位
//			
//			commonDao.store(inventory);
//			
//			
//		} else {
//			inventory.removeQty(backUpQty);
//			commonDao.store(inventory);
//
//			WmsInventory newInventory = new WmsInventory();
//			try {
//				this.copyProperty(inventory, newInventory);
//				newInventory.setId(null);
//				newInventory.setPackQty(0D);
//				newInventory.setQty(0D);
//				newInventory.setRelatedBillCode(null);
//				newInventory.setRelatedBillType(null);
//				newInventory.setOperationStatus(WmsInventoryOperationStatus.NORMAL);
//				newInventory.addQty(backUpQty);
//				
//				newInventory.setLocation(tjLoc);////放到退拣库位
//				
//				commonDao.store(newInventory);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		WmsInventoryDto invDto = this.getWmsInventoryDto(inventory, backUpQty);
//		//描述字段记录退拣数量
//		invDto.setDescript(backUpQty.toString());
//		//退拣时,变化数量为0
//		wmsInventoryLogManager.createWmsInventoryLog(invDto, 0D, DefaultWmsInventoryLogManager.INVENTORY_LOG_OPR_NO_CHANGE, WmsInventoryLogType.BACKUP);
//
	}

	
	
	/**
	 * 条码补打
	 */
	public Map print(List<WmsInventory> inventorys){
		Map result = new HashMap();
		Map<Long,String> reportValue = new HashMap<Long, String>();
		Map printNums = new HashMap(); 
		for (WmsInventory inventory : inventorys) {	
			Long id = inventory.getId();
			String params = ";id="+id;
			reportValue.put(id, "printBuLabel.raq&raqParams="+params);
		}
		if(!reportValue.isEmpty()){
			result.put(IPage.REPORT_VALUES, reportValue);
			result.put(IPage.REPORT_PRINT_NUM, 1);
		}
		return result;	
	}
}
