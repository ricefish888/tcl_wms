package com.vtradex.wms.server.service.count.pojo;

import java.util.List;
import java.util.Set;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.entity.count.WmsCountAdjustWarningType;
import com.vtradex.wms.server.model.entity.count.WmsCountDetail;
import com.vtradex.wms.server.model.entity.count.WmsCountDetailStatus;
import com.vtradex.wms.server.model.entity.count.WmsCountPlan;
import com.vtradex.wms.server.model.entity.count.WmsCountRecord;
import com.vtradex.wms.server.model.entity.item.WmsItemKey;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.service.count.WmsTclCountPlanManager;

public class DefaultWmsTclCountPlanManager 
		extends DefaultBaseManager implements WmsTclCountPlanManager{

	@Override
	public void close(WmsCountPlan plan) {
		
		Set<WmsCountRecord> record = plan.getRecords();
		/***/
		dealRecord(record);
		
		String hql = "from WmsCountDetail w where w.countPlan.id=:countId";
		List<WmsCountDetail> planDets = commonDao.findByQuery(hql,"countId",plan.getId());
		for(WmsCountDetail detail : planDets){
			WmsLocation loc = detail.getLocation();
			if(loc == null){
				continue;
			}
			loc.setCountLock(Boolean.FALSE);
			commonDao.store(loc);
			detail.setStatus(WmsCountDetailStatus.FINISHED);
			commonDao.store(detail);
		}
	}

	@SuppressWarnings("unchecked")
	private void dealRecord(Set<WmsCountRecord> record){
		for(WmsCountRecord countRecord : record){
			//根据货主、库位、货品、库存数量>0查询是否存在多批次(itemKeyId),存在则赋值adjustWarning为多批次调整(国际化)；
			List<WmsItemKey> itemKeys = commonDao.findByQuery(
					"SELECT itemKey FROM WmsInventory inventory"
							+ " LEFT JOIN inventory.itemKey itemKey"
							+ " LEFT JOIN inventory.item item"
							+ " LEFT JOIN inventory.company company"
							+ " WHERE inventory.location = :location"
							+ " AND item.id = :itemId AND inventory.qty > 0"
							+ " AND inventory.operationStatus = 'NORMAL'"
							+ " AND company.id =:companyId"
							+ " ORDER BY itemKey.lotInfo.lot", new String[] {
							"location", "itemId","companyId" }, new Object[] {
							countRecord.getLocation(),
							countRecord.getItem().getId(),
							countRecord.getCountPlan().getCompany().getId()});
			if(itemKeys!=null&&itemKeys.size()>0){
				countRecord.setAdjustWarning(WmsCountAdjustWarningType.MULTI_ITEM_KEY_WARN);
			}else{
				countRecord.setAdjustWarning(WmsCountAdjustWarningType.REQUIRED_ITEM_KEY);
			}
			commonDao.store(countRecord);
		}	
	}
}
