package com.vtradex.wms.rfserver.service.inventory.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.vtradex.rf.common.RfConstant;
import com.vtradex.rf.common.exception.RfBusinessException;
import com.vtradex.thorn.client.utils.StringUtils;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.wms.rfserver.service.inventory.TclRfInventoryQueryManager;
import com.vtradex.wms.rfserver.service.receiving.TclRfAsnManager;
import com.vtradex.wms.server.helper.WmsBarCodeParse;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.item.WmsCompany;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemKey;
import com.vtradex.wms.server.model.entity.item.WmsItemScanCode;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.enums.WmsInventoryOperationStatus;
import com.vtradex.wms.server.utils.StringHelper;

public class DefaultTclRfInventoryQueryManager extends DefaultBaseManager implements TclRfInventoryQueryManager{
	
	/**
	 * 获取物料
	 * @param code
	 * @return
	 */
	private WmsItem getWmsItem(String code){
		WmsItem item = (WmsItem)commonDao.findByQueryUniqueResult("FROM WmsItem item where item.code=:ic", "ic", code);
		
		return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map inventoryQueryInputCommit(Map params) {
		String locCode = params.get("inventoryQueryLocationInput") == null?null:params.get("inventoryQueryLocationInput").toString().trim();
		String itemCode = params.get("inventoryQueryItemInput") == null?null:params.get("inventoryQueryItemInput").toString().trim();
		
		//库位/货品不能都为空
		if(StringUtils.isEmpty(itemCode)){
			throw new RfBusinessException("货品不能都为空");
		}
		
		String hql = "from WmsWarehouse warehouse where warehouse.baseOrganization.id = :baseOrganizationId";	  
		 //查询当前仓库
		WmsWarehouse warehouse = (WmsWarehouse) this.commonDao.findByQueryUniqueResult(hql,"baseOrganizationId",
				BaseOrganizationHolder.getThornBaseOrganization().getId());
		//当库位编码不为空时
		if(!StringUtils.isEmpty(locCode)){
		//校验库位是否存在
		hql = "from WmsLocation loc where loc.warehouse.id =:warehouseId and loc.code =:locCode ";
		List<WmsLocation> locs = this.commonDao.findByQuery(hql, new String[]{"warehouseId","locCode"}, 
				new Object[]{warehouse.getId(),locCode});
		if(locs.isEmpty()||locs.size()<=0){
			throw new RfBusinessException("库位不存在!!");
		}
		WmsLocation location = this.commonDao.load(WmsLocation.class, locs.get(0).getId());
		}
		//当货品编码不为空时
		if(!StringUtils.isEmpty(itemCode)){
			TclRfAsnManager tclRfAsnManager = (TclRfAsnManager) applicationContext.getBean("tclRfAsnManager");
			tclRfAsnManager.checkBarCode(itemCode);
			String ic = "";
			WmsItem item = null;
			if(WmsBarCodeParse.isBarCode(itemCode)){
				//查询货品编码对应货品是否存在
				Map map = WmsBarCodeParse.parse(itemCode);
				ic = map.get(WmsBarCodeParse.KEY_ITEMCODE).toString().trim();
				item = this.getWmsItem(itemCode);
				if(null==item){
					params.put("inventoryQueryItemInput", "");
					throw new RfBusinessException("货品不存在!!",params);
				}
			}else{
				ic = itemCode;
				item = this.getWmsItem(itemCode);
				if(null==item){
					params.put("inventoryQueryItemInput", "");
					throw new RfBusinessException("货品不存在!!",params);
				}
				if(WmsItemScanCode.SCANCODE_NO.equals(item.getUserFieldV10())){
					params.put("inventoryQueryItemInput", "");
					throw new RfBusinessException("请扫描条码",params);
				}
			}
			params.put("itemCode", item.getCode());
		}
		params.put("warehouseId", warehouse.getId());
		params.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return params;
	}

	@Override
	public Map inventoryInfo(Map params) {
		Long inventoryId = params.get(RfConstant.ENTITY_ID) == null?null:Long.valueOf(params.get(RfConstant.ENTITY_ID).toString());
		WmsInventory inventory = this.commonDao.load(WmsInventory.class, inventoryId);
		Map<String, Object> result = new HashMap<String, Object>();
		WmsCompany company = this.commonDao.load(WmsCompany.class, inventory.getCompany().getId());
		WmsItem item = this.commonDao.load(WmsItem.class, inventory.getItem().getId());
		WmsLocation location = this.commonDao.load(WmsLocation.class, inventory.getLocation().getId());
		WmsPackageUnit pagUnit = this.commonDao.load(WmsPackageUnit.class, inventory.getPackageUnit().getId());
		WmsItemKey itemKey = this.commonDao.load(WmsItemKey.class, inventory.getItemKey().getId());
//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		result.put("companyName", company.getName());
		result.put("locCode", location.getCode());
		result.put("itemCode", item.getCode());
		result.put("itemName", item.getName());
		result.put("lotNo", itemKey.getLotInfo()!=null?itemKey.getLotInfo().getLot():"");
		result.put("fcode", itemKey.getLotInfo()!=null?itemKey.getLotInfo().getExtendPropC10():"");
		result.put("sname", itemKey.getLotInfo()!=null?itemKey.getLotInfo().getExtendPropC9():"");
		result.put("productDate", itemKey.getLotInfo()!=null?itemKey.getLotInfo().getProductDate():"");
		result.put("unit", pagUnit.getUnit());
		result.put("packQty", inventory.getPackQty());
		result.put("qty", inventory.getQty());
		result.put("inventoryStatus", inventory.getStatus());
		String operationStatus = "";
		if(WmsInventoryOperationStatus.NORMAL.equals(inventory.getOperationStatus())){
			operationStatus="正常";
		}else if(WmsInventoryOperationStatus.READY_OUT.equals(inventory.getOperationStatus())){
			operationStatus="待出";
		}
		result.put("operationStatus", operationStatus);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map previousPage(Map params) {
		Map<String, Object> perResult = new HashMap<String, Object>();
		
		perResult.putAll(params);
		perResult.put(RfConstant.ENTITY_ID, "");
		
		params.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		//清除部分值
		params.put(RfConstant.CLEAR_VALUE, Boolean.TRUE);
		params.put(RfConstant.PERSISTENT_VALUE, perResult);//将持久map放入返回map中
		return params;//返回原map
	}

	@Override
	public Map switchReturn(Map params) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(RfConstant.CLEAR_VALUE, Boolean.TRUE);
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return result;
	}
	
	/**
	 * 显示库存信息
	 * @param params
	 * @return
	 */
	public Map inventorySumInfo(Map params) throws RfBusinessException {
		String barCode = params.get("itemCode") == null?null:params.get("itemCode").toString().trim();
		
		String hql = "from WmsWarehouse warehouse where warehouse.baseOrganization.id = :baseOrganizationId";	  
		 //查询当前仓库
		WmsWarehouse warehouse = (WmsWarehouse) this.commonDao.findByQueryUniqueResult(hql,"baseOrganizationId",
				BaseOrganizationHolder.getThornBaseOrganization().getId());
		
		hql = "SELECT inv.item.code,inv.item.name,inv.status,SUM(inv.qty),inv.itemKey.lotInfo.extendPropC10 " +
				"FROM WmsInventory inv " +
				"WHERE 1=1 and inv.item.code=:itemCode AND inv.operationStatus in ('NORMAL','READY_OUT') " +
			  " AND inv.warehouse.id =:warehouseId AND inv.qty>0 AND inv.location.type ='STORAGE' " +
			  "GROUP BY inv.status,inv.item.code,inv.item.name,inv.itemKey.lotInfo.extendPropC10  ";
		List<Object[]> infos = commonDao.findByQuery(hql, new String[]{"itemCode","warehouseId"}, new Object[]{barCode,warehouse.getId()});
		StringBuffer itemInfoStr = new StringBuffer();
		for(int i = 0;i <infos.size();i++){
//			物料代码：
//			物料名称：
//			库存状态
//			数量
			Object[] info = infos.get(i);
			String factory =info[4].toString();
			itemInfoStr.append("工厂:").append(factory).append("\n");
			String itemCode = info[0].toString();
			itemInfoStr.append("物料代码:").append(itemCode).append("\n");
			String itemName = info[1].toString();
			itemInfoStr.append("物料名称:").append(itemName).append("\n");
			String invStatus = info[2].toString();
			itemInfoStr.append("库存状态:").append(invStatus).append("\n");
			Double expectedQty =Double.valueOf(info[3].toString());
			itemInfoStr.append("数量:").append(expectedQty).append("\n");
			if(i<(infos.size()-1)){
				itemInfoStr.append("----------------------\n");
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
	    perResult.putAll(params);
	    result.put("warehouseName", warehouse.getName());
		result.put("itemInfos", itemInfoStr);
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	/**
	 * 扫描物料编码/条码
	 */
	public Map inventorySumQueryInputCommit(Map params){
		String barCode = params.get("inventoryQueryItemInput") == null?null:params.get("inventoryQueryItemInput").toString().trim();
		if(StringHelper.isNullOrEmpty(barCode)){
			throw new RfBusinessException("物料编码/条码不能为空!!");
		}
		String itemCode = "";
		WmsItem item =null;
		TclRfAsnManager tclRfAsnManager = (TclRfAsnManager) applicationContext.getBean("tclRfAsnManager");
		tclRfAsnManager.checkBarCode(barCode);
		if(WmsBarCodeParse.isBarCode(barCode)){
			Map map = WmsBarCodeParse.parse(barCode);
			itemCode = (String)map.get(WmsBarCodeParse.KEY_ITEMCODE);
			item = this.getWmsItem(itemCode);
			if(null==item){
				params.put("inventoryQueryItemInput", "");
				throw new RfBusinessException("货品不存在!!",params);
			}
		}else{
			itemCode = barCode;
			item = this.getWmsItem(itemCode);
			if(null==item){
				params.put("inventoryQueryItemInput", "");
				throw new RfBusinessException("货品不存在!!",params);
			}
			if(WmsItemScanCode.SCANCODE_NO.equals(item.getUserFieldV10())){
				params.put("inventoryQueryItemInput", "");
				throw new RfBusinessException("请扫描条码",params);
			}
		}
		params.put("itemCode", item.getCode());
		params.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return params;
	}
}
