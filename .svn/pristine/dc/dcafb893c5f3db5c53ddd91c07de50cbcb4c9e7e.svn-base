package com.vtradex.wms.server.service.item.pojo;

import java.util.List;
import java.util.Map;

import com.mysql.jdbc.StringUtils;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.wms.server.model.entity.base.WmsItemFactory;
import com.vtradex.wms.server.model.entity.base.WmsSapFactory;
import com.vtradex.wms.server.model.entity.item.UnitLevel;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemHandOverAtt;
import com.vtradex.wms.server.model.entity.item.WmsItemJITAtt;
import com.vtradex.wms.server.model.entity.item.WmsItemScanCode;
import com.vtradex.wms.server.model.entity.item.WmsItemUnPackingAtt;
import com.vtradex.wms.server.model.entity.item.WmsLotRule;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.enums.BaseStatus;
import com.vtradex.wms.server.model.enums.WmsLocationCode;
import com.vtradex.wms.server.service.item.WmsTclItemManager;
import com.vtradex.wms.server.utils.StringHelper;

public class DefaultWmsTclItemManager extends DefaultWmsItemManager implements WmsTclItemManager {

	public DefaultWmsTclItemManager(WorkflowManager workflowManager) {
		super(workflowManager);
	}

	public void storeWmsItem(WmsItem item, Long companyId) {
		if(!StringHelper.isNullOrEmpty(item.getUserFieldV2())
				&& (item.getUserFieldV2().equals(WmsItemJITAtt.JIT_DOWNLINE_SETTLE) 
				|| item.getUserFieldV2().equals(WmsItemJITAtt.JIT_UPLINE_SETTLE))){
			item.setPutawayGroup(WmsLocationCode.JIT);
		}
		super.storeWmsItem(item, companyId);
		
		if(StringUtils.isNullOrEmpty(item.getUserFieldV1())){
			throw new BusinessException("交接属性必填,不能为空");
		}
		
		if(StringUtils.isNullOrEmpty(item.getUserFieldV2())){
			throw new BusinessException("JIT属性必填,不能为空");
		}
		
		
		
		
	}
	
	
	/**
	 * 导入物料
	 */
	public void initWmsItemByImport(Map<String,String> map){		
		String itemCode = map.get("物料代码");
		if(itemCode == null || "".equals(itemCode)){
			throw new BusinessException("物料代码属性不能为空,请维护物料代码");
		}
		
		String name = map.get("货品描述");
		if(name == null || "".equals(name)){
			throw new BusinessException("物料名称属性不能为空,请维护物料名称");
		}
		
		String baseUnit = map.get("基本包装单位");
		if(baseUnit == null || "".equals(baseUnit)){
			throw new BusinessException("基本包装单位属性不能为空,请维护基本包装单位");
		}
		
		String putawayGroup = map.get("上架分组");
		String allocationGroup = map.get("拣选分组");
		
		
		
		String userFieldV1 = map.get("交接属性");
		
		String userFieldV2 = map.get("JIT属性");
		
		String userFieldV3 = map.get("拆包属性");
		
		String userFieldV6 = map.get("物料类型");
		if(userFieldV6 == null || "".equals(userFieldV6)){
			throw new BusinessException("物料类型不能为空,请维护物料类型");
		}
		String userFieldV8 = map.get("物料组");
		if(userFieldV8 == null || "".equals(userFieldV8)){
			throw new BusinessException("物料组不能为空,请维护物料组");
		}
		
		String description = map.get("物料组描述");
		if(description == null || "".equals(description)){
			throw new BusinessException("物料组描述不能为空,请维护物料组描述");
		}
		
		String userFieldV4 = map.get("采购组");
		if(userFieldV4 == null || "".equals(userFieldV4)){
			throw new BusinessException("采购组不能为空,请维护采购组");
		}
		String userFieldV9 = map.get("采购组描述");
		if(userFieldV9 == null || "".equals(userFieldV9)){
			throw new BusinessException("采购组描述不能为空,请维护采购组描述");
		}
		
		String userFieldV7 = map.get("跨工厂物料状态");
		
		
		String descriptionq = map.get("单位描述");
		
		
		String factoryCode =  map.get("工厂");
		
		if(factoryCode == null || "".equals(factoryCode)){
			throw new BusinessException("工厂属性不能为空,请维护工厂");
		}
		
		WmsItem item = null;
		item = (WmsItem) commonDao.findByQueryUniqueResult("FROM WmsItem w WHERE w.code =:code", "code", itemCode);
	    
	    if(item == null){
	    	item =  new WmsItem();
	    }
	    
	    WmsLotRule lotRule = getDefaultLotRule();
	    
	    item.setCode(itemCode);
	    item.setBarCode(itemCode);
	    item.setName(name);
	    item.setBaseUnit(baseUnit);
	    item.setLotRule(lotRule);
	    item.setCountLotRule(lotRule);
	    item.setBuPrecision(5);//精度默认为5
	    item.setStatus(BaseStatus.ENABLED);
	    item.setWeight(0d);
	    item.setVolume(0d);
	  
	    
	    item.setAllocationGroup(allocationGroup);
	    item.setDescription(description);
	    item.setUserFieldV4(userFieldV4);//采购组
	    item.setUserFieldV6(userFieldV6);//物料类型
	    item.setUserFieldV8(userFieldV8);//物料组
	    item.setUserFieldV7(userFieldV7);//跨工厂物料状态
	    item.setUserFieldV9(userFieldV9);//采购组描述
	    if(userFieldV8.startsWith("2106") || userFieldV8.startsWith("3106")
        		|| userFieldV8.startsWith("3206") || userFieldV8.startsWith("3306")){
        	item.setUserFieldV10(WmsItemScanCode.SCANCODE_YES);
        }else{
        	item.setUserFieldV10(WmsItemScanCode.SCANCODE_NO);
        }
	    if("T-1区交接".equals(userFieldV1)){
	    	item.setUserFieldV1(WmsItemHandOverAtt.T_1_AREA);
	    }else if("线边交接".equals(userFieldV1)){
	    	item.setUserFieldV1(WmsItemHandOverAtt.LINE_EDGE);
	    }
	  
     
	    if("非JIT".equals(userFieldV2)){
	    	item.setUserFieldV2(WmsItemJITAtt.NO_JIT);
	    }else if("JIT上线结算".equals(userFieldV2)){
	    	item.setUserFieldV2(WmsItemJITAtt.JIT_UPLINE_SETTLE);
	    }else if("JIT下线结算".equals(userFieldV2)){
	    	item.setUserFieldV2(WmsItemJITAtt.JIT_DOWNLINE_SETTLE);
	    }
	    
	    if("仓库不可拆包".equals(userFieldV3)){
	    	 item.setUserFieldV3(WmsItemUnPackingAtt.WAREHOUSE_UNPACKING);
	    }else{
	    	item.setUserFieldV3(null);
	    }
	    //如果JIT属性 为  JIT上线结算 或 JIT下线结算 ,上架组为 JIT 反之就为输入的数据
	    if("JIT上线结算".equals(userFieldV2) || "JIT下线结算".equals(userFieldV2)){
	    	item.setPutawayGroup(WmsLocationCode.JIT);
	    }else{
	    	item.setPutawayGroup(putawayGroup);
	    }
	    
	    
	    if(item.isNew()){
	    	item.setMasterGroup(lotRule.getMasterGroup());
	    	WmsPackageUnit packageUnit = EntityFactory.getEntity(WmsPackageUnit.class);
	    	packageUnit.setConvertFigure(1D);
    		packageUnit.setUnit(item.getBaseUnit());
    		packageUnit.setUnitLevel(UnitLevel.A);
    		packageUnit.setWeight(item.getWeight());
    		packageUnit.setVolume(item.getVolume());
    		packageUnit.setDescription(descriptionq);
    		item.addPackageUnit(packageUnit);  	
	    }
	    commonDao.store(item);
	    
	    WmsSapFactory factory = (WmsSapFactory) commonDao.findByQueryUniqueResult("FROM WmsSapFactory WHERE code=:code", 
                "code", factoryCode);
        if (null==factory) {
            throw new BusinessException("未维护编码【"+factoryCode+"】的sap工厂信息!");
        }
        WmsItemFactory itemFactory = (WmsItemFactory) commonDao.findByQueryUniqueResult("FROM WmsItemFactory "
                + "WHERE item.id=:itemId AND factory.id=:factoryId", 
                new String[]{"itemId", "factoryId"}, new Object[]{item.getId(), factory.getId()});
        
        if (null==itemFactory) {
            itemFactory = EntityFactory.getEntity(WmsItemFactory.class);
            itemFactory.setItem(item);
            itemFactory.setFactory(factory);
            commonDao.store(itemFactory);
        }
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

	/**导入物料属性*/
	@Override
	public void initWmsItemAttrByImport(Map<String, String> map) {
		String itemCode = map.get("物料代码");
		WmsItem item = null;
		if(StringHelper.isNullOrEmpty(itemCode)){
			throw new BusinessException("物料条码为空!!");
		}
		item = (WmsItem)commonDao.findByQueryUniqueResult("FROM WmsItem item WHERE item.code=:ic", "ic", itemCode.trim());
		if(null==item){
			throw new BusinessException("没有找到对应物料!!");
		}
		String v1 = map.get("交接属性");
		if(!StringHelper.isNullOrEmpty(v1)){
			if("T-1区交接".equals(v1)){
				item.setUserFieldV1(WmsItemHandOverAtt.T_1_AREA);
			}else if("线边交接".equals(v1)){
				item.setUserFieldV1(WmsItemHandOverAtt.LINE_EDGE);
			}else{
				throw new BusinessException("交接属性没维护!!");
			}
		}else{
			throw new BusinessException("交接属性必填!!");
		}
		String v2 = map.get("JIT属性");
		if(!StringHelper.isNullOrEmpty(v2)){
			if("非JIT".equals(v2)){
				item.setUserFieldV2(WmsItemJITAtt.NO_JIT);
			}else if("JIT上线结算".equals(v2)){
				item.setUserFieldV2(WmsItemJITAtt.JIT_UPLINE_SETTLE);
			}else if("JIT下线结算".equals(v2)){
				item.setUserFieldV2(WmsItemJITAtt.JIT_DOWNLINE_SETTLE);
			}else{
				throw new BusinessException("JIT属性没维护!!");
			}
		}else{
			throw new BusinessException("JIT属性必填!!");
		}
		
		if("JIT上线结算".equals(v2) || "JIT下线结算".equals(v2)){
			item.setPutawayGroup(WmsLocationCode.JIT);
		}
		String v3 = map.get("拆包属性");
		if(!StringHelper.isNullOrEmpty(v3)){
			if("仓库不可拆包".equals(v3)){
				item.setUserFieldV3(WmsItemUnPackingAtt.WAREHOUSE_UNPACKING);
			}else{
				throw new BusinessException("拆包属性不存在!!");
			}
		}else{
			item.setUserFieldV3(null);
		}
		
		String v4 = map.get("最小包装量");
		if(StringHelper.isNullOrEmpty(v4)){
			throw new BusinessException("最小包装量必填!!");
		}
		Double minQty = 0D;
		try {
			minQty = Double.valueOf(v4);
		} catch (Exception e) {
			throw new BusinessException("最小包装量["+v4+"]格式有误,请检查!!");
		}
		item.setUserFieldD1(minQty);
		commonDao.store(item);
		try {//传接口
			workflowManager.sendMessage(item,"itemProcess.edit");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}

	@Override
	public void initWmsItemUpByImport(Map<String, String> map) {
		String itemCode = map.get("物料代码");
		WmsItem item = null;
		if(StringHelper.isNullOrEmpty(itemCode)){
			throw new BusinessException("物料条码为空!!");
		}
		item = (WmsItem)commonDao.findByQueryUniqueResult("FROM WmsItem item WHERE item.code=:ic", "ic", itemCode);
		if(null==item){
			throw new BusinessException("没有找到对应物料!!");
		}
		String up1 = map.get("上架组");
		if(!StringHelper.isNullOrEmpty(up1)){
			item.setPutawayGroup(up1);
		}
		String up2 = map.get("分配组");
		if(!StringHelper.isNullOrEmpty(up2)){
			item.setAllocationGroup(up2);
		}
		String userFieldV10 = map.get("是否可录入编码");
		if(StringHelper.isNullOrEmpty(userFieldV10)){
			throw new BusinessException("是否可录入编码不能为空!!");
		}
		if("是".equals(userFieldV10)){
			item.setUserFieldV10(WmsItemScanCode.SCANCODE_YES);
		}else{
			item.setUserFieldV10(WmsItemScanCode.SCANCODE_NO);
		}
		commonDao.store(item);
	}
	
	public void editWmsItemScan(WmsItem item,String userFieldV10){
		item.setUserFieldV10(userFieldV10);
		commonDao.store(item);
	}
}