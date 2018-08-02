package com.vtradex.wms.server.service.inventory.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.client.utils.StringUtils;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.wms.server.model.component.LotInfo;
import com.vtradex.wms.server.model.dto.WmsInventoryDto;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.item.WmsCompany;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemKey;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.enums.WmsInventoryLogType;
import com.vtradex.wms.server.service.inventory.WmsDemoInventoryManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.item.WmsItemManager;
import com.vtradex.wms.server.utils.WmsPackageUnitUtils;


public class DefaultWmsDemoInventoryManager extends DefaultBaseManager implements WmsDemoInventoryManager{

	private WmsInventoryManager wmsInventoryManager;
	private WmsItemManager wmsItemManager;

	public DefaultWmsDemoInventoryManager(WmsInventoryManager wmsInventoryManager,WmsItemManager wmsItemManager){
		this.wmsInventoryManager = wmsInventoryManager;
		this.wmsItemManager = wmsItemManager;
	}
	/**
	 * 库存初始化导入数据
	 * */
	@Transactional
	public void initInventoryByImport(Map<String,String> map){
		String companyName=map.get("货主");//1 货主
	      if(companyName==null||"".equals(companyName)){
	    	  throw new BusinessException("货主不能为空,请维护货主！");  
	      }
	      //2仓库
	      String itemCode=map.get("货品代码");//3货品代码
	      if(itemCode==null||"".equals(itemCode)){
	    	  throw new BusinessException("货品代码不能为空,请维护货品代码！");  
	      }
	      String itemName=map.get("货品名称").toString();//4货品名称
	      if(itemName==null||"".equals(itemName)){
	    	  throw new BusinessException("货品名称不能为空,请维护货品名称！");  
	      }
	      String packageDW=map.get("包装单位");//5包装单位
	      if(StringUtils.isEmpty(packageDW)){
	    	  throw new BusinessException("包装单位不能为空,请维护包装单位！");  
	      }
	      String cHDate=map.get("存货日期");//6存货日期
	      String sCDate=map.get("生产日期");//7生产日期
	      String gQDate=map.get("过期日期");//8过期日期
	      String supplier=map.get("供应商");//9供应商
	      String inventoryDou=map.get("库存数量");//10库存数量
	      if(inventoryDou==null||"".equals(inventoryDou)){
	    	  throw new BusinessException("库存数量不能为空,请维护库存数量！");  
	      }
	      String inventoryStatus="";
	      String inventoryStatus1=map.get("库存状态");//11库存状态
	      if(StringUtils.isEmpty(inventoryStatus1)){
		      inventoryStatus="";
	      }

	      String pallet=map.get("托盘号");//12托盘号
	      String locationCode=map.get("库位编码");//13 库位编码
	      if(locationCode==null||"".equals(locationCode)){
	    	  throw new BusinessException("库位编码不能为空,请维护库位编码！");  
	      }
	      WmsInventory inventory=EntityFactory.getEntity(WmsInventory.class);
	      //货主
	      String hql = "from WmsCompany company where company.name=:name";
	      List<WmsCompany> companies=this.commonDao.findByQuery(hql, "name", companyName);
	      if(companies.isEmpty()||companies.size()<=0){
	    	  throw new BusinessException("找不到  "+companyName+" 对应的货主！");
	      }
	      WmsCompany company=companies.get(0);
	      inventory.setCompany(company);
	      // 库位
	      hql="from WmsLocation location where location.code=:code";
	      List<WmsLocation> locations=this.commonDao.findByQuery(hql, "code", locationCode);
	      WmsLocation location =null;
		  if(!locations.isEmpty()&&locations.size()>0){
			  location=locations.get(0);
		  }else{
			  throw new BusinessException("找不到库位编码为"+locationCode+"库位");
		  }
	      inventory.setLocation(location);
	      //货品    根据货品code与货主主数据分组查询
	      hql = "from WmsItem item where item.code=:itemCode and item.masterGroup.id=:masterGroupId";
		  List<WmsItem> items=this.commonDao.findByQuery(hql, new String[]{"itemCode","masterGroupId"}, new Object[]{itemCode,company.getMasterGroup().getId()}) ;
		  WmsItem item = null;
		  if(!items.isEmpty()&&items.size()>0){
			  item = items.get(0);
		  }else{
			  throw new BusinessException("找不到货主为"+companyName+"、货品编码为"+itemCode+"的货品");
		  }
		  inventory.setStatus(inventoryStatus);
			// 包装单位
			WmsPackageUnit packageUnit = (WmsPackageUnit) commonDao
						.findByQueryUniqueResult(
								"from WmsPackageUnit wpu where wpu.item.id=:itemId and wpu.unit=:unit",
								new String[] { "itemId", "unit" }, new Object[] {
										item.getId(), packageDW });
			//包装
			if(packageUnit==null){
				throw new BusinessException("找不到货品编码为"+item.getCode()+"、包装单位为"+packageDW+"的包装");
			}
			inventory.setPackageUnit(packageUnit);
			
			LotInfo lotInfo = new LotInfo();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
			//收货日期
			Date storageDate;
			try {
				storageDate = StringUtils
						.isEmpty(cHDate)
						|| cHDate.equals("-") ? null : sdf
						.parse(cHDate);// 收货日期
			} catch (Exception e) {
				try{
				storageDate = StringUtils
						.isEmpty(cHDate)
						|| cHDate.equals("/") ? null : sdf1
						.parse(cHDate);// 收货日期
				}catch(Exception ee){
					throw new BusinessException("存货日期不合法，请修改");
				}
			}
			lotInfo.setStorageDate(storageDate);
			//生产日期
			Date productDate;
			try {
				productDate = StringUtils
							.isEmpty(sCDate)
							|| sCDate.equals("-") ? null : sdf.parse(sCDate);// 生产日期
			} catch (Exception e) {
				try{
				productDate = StringUtils
						.isEmpty(sCDate)
						|| sCDate.equals("/") ? null : sdf1.parse(sCDate);// 生产日期
				}catch(Exception ee){
					throw new BusinessException("生产日期不合法，请修改");
				}
			}
			lotInfo.setProductDate(productDate);
			//过期日期
			Date expireDate;
			try {
				expireDate= StringUtils.isEmpty(gQDate)
						|| gQDate.equals("-") ? null : sdf.parse(gQDate);// 失效日期
			} catch (Exception e) {
				try{
				expireDate= StringUtils.isEmpty(gQDate)
						|| gQDate.equals("/") ? null : sdf1.parse(gQDate);// 失效日期
				}catch(Exception ee){
					throw new BusinessException("失效日期不合法，请修改");
				}
			}
			lotInfo.setExpireDate(expireDate);
			//供应商
			lotInfo.setSupplierCode(StringUtils.isEmpty(supplier)?null:supplier);
			//库存状态
			inventory.setStatus(StringUtils.isEmpty(inventoryStatus)
					|| inventoryStatus.equals("-") ? null : inventoryStatus);
			//库存数量
			inventory.setQty(StringUtils.isEmpty(inventoryDou)
					|| inventoryDou.equals("-") ? 0D : Double.parseDouble(inventoryDou));
			// 仓库
			hql="from WmsWarehouse warehouse where warehouse.baseOrganization.id=:baseOrganizationId";
			List<WmsWarehouse> warehouse = this.commonDao.findByQuery(hql, "baseOrganizationId", BaseOrganizationHolder.getThornBaseOrganization().getId());
			if(warehouse.isEmpty()||warehouse.size()<=0){
				throw new BusinessException("could.not.find.the.warehouse.for.the.current.organization");
			}
			inventory.setWarehouse(warehouse.get(0));
			// 货品批次属性
			WmsItemKey wmsItemKey = wmsItemManager.getItemKey(warehouse.get(0),
					"", item, lotInfo);
			inventory.setItemKey(wmsItemKey);
			inventory.setItem(item);
			
			//包装数量
			Double packNum = WmsPackageUnitUtils.getPackQty(inventory.getPackageUnit(), inventory.getQty(), inventory.getItem().getBuPrecision());
			
			//存货库位新增
			WmsInventoryDto inventoryDto = new WmsInventoryDto();

			inventoryDto.setWarehouse(inventory.getWarehouse());
			inventoryDto.setCompany(inventory.getCompany());
			inventoryDto.setLocation(inventory.getLocation());
			inventoryDto.setItem(inventory.getItem());
			inventoryDto.setItemKey(inventory.getItemKey());
			inventoryDto.setPackageUnit(inventory.getPackageUnit());
			inventoryDto.setQty(inventory.getQty());
			inventoryDto.setPackQty(packNum);
			inventoryDto.setStatus(inventory.getStatus());
			inventoryDto.setType(WmsInventoryLogType.QTY_CHANGE);
			inventoryDto.setPallet(pallet);
			inventoryDto.setDescript("");
			inventoryDto.setPallet(inventory.getPallet());
			inventoryDto.setCarton("");

			wmsInventoryManager.in(inventoryDto);
	}
}
