package com.vtradex.wms.server.service.inventory.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.wms.server.enumeration.WarehouseEnumeration;
import com.vtradex.wms.server.model.component.LotInfo;
import com.vtradex.wms.server.model.dto.WmsInventoryDto;
import com.vtradex.wms.server.model.entity.base.WmsFactoryXmlb;
import com.vtradex.wms.server.model.entity.base.WmsItemFactory;
import com.vtradex.wms.server.model.entity.base.WmsSapFactory;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.model.entity.inventory.TclWmsMoveType;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.item.WmsCompany;
import com.vtradex.wms.server.model.entity.item.WmsInventoryState;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemKey;
import com.vtradex.wms.server.model.entity.item.WmsMinPackageQty;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.enums.WmsInventoryLogType;
import com.vtradex.wms.server.model.enums.WmsInventoryOperationStatus;
import com.vtradex.wms.server.model.enums.WmsLocationCode;
import com.vtradex.wms.server.service.inventory.WmsInventoryLogManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.inventory.WmsTclInventoryManageManager;
import com.vtradex.wms.server.service.item.TclMessageManager;
import com.vtradex.wms.server.service.item.WmsItemManager;
import com.vtradex.wms.server.service.pickticket.WmsPickticketManager;
import com.vtradex.wms.server.service.receiving.WmsASNManager;
import com.vtradex.wms.server.service.task.WmsTaskManager;
import com.vtradex.wms.server.service.workdoc.WmsWorkDocManager;
import com.vtradex.wms.server.utils.BeanUtils;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.server.utils.WmsPackageUnitUtils;
import com.vtradex.wms.webservice.utils.CommonHelper;
public class DefaultWmsTclInventoryManageManager extends DefaultWmsInventoryManageManager implements WmsTclInventoryManageManager{

	private WmsInventoryManager inventoryManager;
	private WmsItemManager wmsItemManager;
	private TclMessageManager tclMessageManager;
	
	public DefaultWmsTclInventoryManageManager(WorkflowManager workflowManager,
			WmsInventoryManager inventoryManager,
			WmsItemManager wmsItemManager,
			WmsPickticketManager wmsPickticketManager,
			WmsASNManager wmsASNManager,
			WmsInventoryLogManager wmsInventoryLogManager,
			WmsTaskManager wmsTaskManager, WmsWorkDocManager wmsWorkDocManager,TclMessageManager tclMessageManager) {
		super(workflowManager, inventoryManager, wmsItemManager, wmsPickticketManager,
				wmsASNManager, wmsInventoryLogManager, wmsTaskManager,
				wmsWorkDocManager);
		
		this.inventoryManager = inventoryManager;
		this.wmsItemManager = wmsItemManager;
		this.tclMessageManager = tclMessageManager;
	}

	public void changeStatus(WmsInventory inventory,Double newFactoryQty,Long invstatusid,Long newLocId) {
		WmsLocation newLoc = commonDao.load(WmsLocation.class, newLocId);
		if(!WmsInventoryOperationStatus.NORMAL.equals(inventory.getOperationStatus())) {
			throw new BusinessException("作业状态不是【正常】，不允许调拨");
		}
		Double invQty = inventory.getQty(); //原始数量  qty为新数量
		if(newFactoryQty>invQty){
			throw new BusinessException("修改数量大于原始库存数量!!");
		}
		if(CommonHelper.dealDoubleError(newFactoryQty)<0.001D){
			throw new BusinessException("数量不得小于0.001");
		}
		String oldStatus = inventory.getStatus();
		WmsInventoryState newstate = commonDao.load(WmsInventoryState.class, invstatusid);
		if(!StringHelper.in(oldStatus, new String[]{"合格","不良品"})) {
			throw new BusinessException("原始状态必须为合格或不良品");
		}
		if(!StringHelper.in(newstate.getName(), new String[]{"合格","不良品"})) {
			throw new BusinessException("调入状态必须为合格或不良品");
		}
		if(oldStatus.equals(newstate.getName())) {
			throw new BusinessException("原始状态和调入状态不能一样");
		}
		
		if("合格".equals(oldStatus) && "不良品".equals(newstate.getName())) { //合格到不良品
			//库位必须是HLC
			if(!newLoc.getCode().equals(WmsLocationCode.HLC)) {
				throw new BusinessException("合格转不良品移入库位必须是"+WmsLocationCode.HLC);
			}
		}
		else if("不良品".equals(oldStatus) && "合格".equals(newstate.getName())) { //不良品到合格
			//库位不能是HLC
			if(newLoc.getCode().equals(WmsLocationCode.HLC)) {
				throw new BusinessException("不良品转合格移入库位不能是"+WmsLocationCode.HLC);
			}
		}
		else {
			throw new BusinessException("库存状态选择不正确");
		}
		
		
		String descript = "库内正残品调拨";
        //移入库位新增
        WmsInventoryDto inventoryDto = new WmsInventoryDto();
        inventoryDto.setWarehouse(inventory.getWarehouse());
        inventoryDto.setCompany(inventory.getCompany());
        inventoryDto.setLocation(newLoc);
        inventoryDto.setItem(inventory.getItem());
        inventoryDto.setItemKey(inventory.getItemKey());
        inventoryDto.setPackageUnit(inventory.getPackageUnit());
        inventoryDto.setQty(newFactoryQty);
        inventoryDto.setPackQty(WmsPackageUnitUtils.getPackQty(inventory.getPackageUnit(), newFactoryQty, inventory.getItem().getBuPrecision()));
        
        inventoryDto.setStatus(newstate.getName());
        
        inventoryDto.setType(WmsInventoryLogType.ZCP_CHANGE);
        inventoryDto.setDescript(descript);
        inventoryDto.setPallet(inventory.getPallet());
        inventoryDto.setCarton(inventory.getCarton());
        inventoryDto.setPalletSeq(inventory.getPalletSeq());
        
        //移出库位库存扣减
        inventoryManager.out(inventory,newFactoryQty,WmsInventoryLogType.ZCP_CHANGE,descript);
        //移入库位库存新增
        inventoryManager.in(inventoryDto);
		
        tclMessageManager.sendChangeStatusInfo(inventory, newFactoryQty, newstate.getName());
		
		
		
	}

	public void changeType(WmsInventory inventory,Double newFactoryQty,String newFactoryCode) {
		if(!WmsInventoryOperationStatus.NORMAL.equals(inventory.getOperationStatus())) {
			throw new BusinessException("作业状态不是【正常】，不允许内外销调拨");
		}
		Double invQty = inventory.getQty(); //原始数量  qty为新数量
		if(newFactoryQty>invQty){
			throw new BusinessException("修改数量大于原始库存数量!!");
		}
		if(CommonHelper.dealDoubleError(newFactoryQty)<0.001D){
			throw new BusinessException("数量不得小于0.001");
		}
		WmsSapFactory factory = (WmsSapFactory)commonDao.findByQueryUniqueResult("FROM WmsSapFactory factory where factory.code=:fc", "fc",newFactoryCode);
		if(null==factory){
			throw new BusinessException("工厂"+newFactoryCode+"未找到");
		}
		//校验物料是否在两个工厂都存在
		String hql ="FROM WmsItemFactory f WHERE f.item.id =:itemId AND f.factory.code =:factoryCode ";
		List<WmsItemFactory> oldFactorys = commonDao.findByQuery(hql, new String[]{"itemId","factoryCode"}, new Object[]{inventory.getItem().getId(),inventory.getItemKey().getLotInfo().getExtendPropC10()});
		List<WmsItemFactory> newFactorys = commonDao.findByQuery(hql, new String[]{"itemId","factoryCode"}, new Object[]{inventory.getItem().getId(),newFactoryCode});
		if(oldFactorys.isEmpty()){
			throw new BusinessException("物料不属于工厂"+inventory.getItemKey().getLotInfo().getExtendPropC10());
		}
		if(newFactorys.isEmpty()){
			throw new BusinessException("物料不属于工厂"+newFactoryCode);
		}
		WmsWarehouse warehouse = inventory.getWarehouse();
        WmsCompany company = inventory.getCompany();
        WmsItem item = inventory.getItem();
        WmsPackageUnit unit = inventory.getPackageUnit();
        
//        Double packQty = inventory.getPackQty();
        Double packQty = newFactoryQty;
 
        Double qty = WmsPackageUnitUtils.getQtyBU(unit, packQty, item.getBuPrecision());
        
        LotInfo lotInfo = inventory.getItemKey().getLotInfo();
        LotInfo newLotInfo = new LotInfo();
        BeanUtils.copyEntity(newLotInfo, lotInfo);//复制新批次
        newLotInfo.setExtendPropC10(factory.getCode());
        newLotInfo.setExtendPropC11(factory.getName());
        WmsItemKey newItemKey = wmsItemManager.getItemKeyByLotInfoModify(inventory.getWarehouse(), 
        		newLotInfo.getAsnCustomerBill(), inventory.getItem(), newLotInfo);
        System.out.println(inventory.getItemKey().getLotInfo().stringValue());
        System.out.println(newItemKey.getLotInfo().stringValue());
        if(inventory.getItemKey().getLotInfo()!=null){
	        if(inventory.getItemKey().getLotInfo().stringValue().equals(newItemKey.getLotInfo().stringValue())){
	            throw new BusinessException("no.lotInfo.modify");
	        }
        }
        String descript = "库内内外销调拨";
        
        //移入库位新增
        WmsInventoryDto inventoryDto = new WmsInventoryDto();

        inventoryDto.setWarehouse(warehouse);
        inventoryDto.setCompany(company);
        inventoryDto.setLocation(inventory.getLocation());
        inventoryDto.setItem(item);
        inventoryDto.setItemKey(newItemKey);
        inventoryDto.setPackageUnit(unit);
        inventoryDto.setQty(qty);
        inventoryDto.setPackQty(packQty);
        inventoryDto.setStatus(inventory.getStatus());
        inventoryDto.setType(WmsInventoryLogType.NWX_CHANGE);
        inventoryDto.setDescript(descript);
        inventoryDto.setPallet(inventory.getPallet());
        inventoryDto.setCarton(inventory.getCarton());
        inventoryDto.setPalletSeq(inventory.getPalletSeq());
        
        //移出库位库存扣减
        inventoryManager.out(inventory,qty,WmsInventoryLogType.NWX_CHANGE,descript);
        //移入库位库存新增
        inventoryManager.in(inventoryDto);
		
        tclMessageManager.sendChangeTypeInfo(inventory, newFactoryQty, newFactoryCode,inventory.getWarehouse().getCode());
		
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
//	      String itemName=map.get("货品名称").toString();//4货品名称
	    
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
	      inventoryStatus=map.get("库存状态");//11库存状态
	      if(StringUtils.isEmpty(inventoryStatus)){
		      inventoryStatus="";
	      }

	      String pallet=map.get("托盘号");//12托盘号
	      String locationCode=map.get("库位编码");//13 库位编码
	      if(locationCode==null||"".equals(locationCode)){
	    	  throw new BusinessException("库位编码不能为空,请维护库位编码！");  
	      }
	      
	      String lot = map.get("批次号");//14批次号
	      if(lot ==null || "".equals(lot)){
	    	  throw new BusinessException("批次号不能为空,请维护批次号！");  
	      }
	  
	      String extendPropc8 = map.get("项目类别");//16项目类别 0-标准，2-寄售
	      if(extendPropc8 == null || "".equals(extendPropc8)){
	    	  throw new BusinessException("项目类别不能为空,请维护项目类别！");  
	      }
	      
	      String extendPropc10 = map.get("工厂编码");//17工厂编码
	      if(extendPropc10 == null ||"".equals(extendPropc10)){
	    	  throw new BusinessException("工厂编码不能为空,请维护工厂编码！");
	      }
	      
//	      String extendPropc12 = map.get("最小包装量");
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
	      hql="from WmsLocation location where location.code=:code AND location.warehouse.baseOrganization.id =:Id";
	      List<WmsLocation> locations=this.commonDao.findByQuery(hql, new String[]{"code","Id"}, 
	    		                                                 new Object[]{locationCode,BaseOrganizationHolder.getThornBaseOrganization().getId()});
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
			hql = "FROM WmsSupplier w WHERE w.code =:code";
			List<WmsSupplier> wmsSupplier = this.commonDao.findByQuery(hql, "code", supplier);
			if(wmsSupplier.isEmpty() || wmsSupplier.size()<=0 ){
				throw new BusinessException("未找到供应商,请重新维护供应商");
			}
			lotInfo.setExtendPropC9(wmsSupplier.get(0).getName());
			//工厂
			lotInfo.setExtendPropC10(extendPropc10);
			hql = "FROM WmsSapFactory w WHERE w.code =:code";
			List<WmsSapFactory>  factory = this.commonDao.findByQuery(hql, "code", extendPropc10);
			if(factory.isEmpty() || factory.size() <=0){
				throw new BusinessException("未找到工厂,请重新维护工厂");
			}
			lotInfo.setExtendPropC11(factory.get(0).getName());
			//最小包装量
			/*hql = "FROM WmsMinPackageQty w WHERE w.supplier.code =:supplierCode AND w.item.code =:itemCode ";
			List<WmsMinPackageQty> wmsMinPackageQty = this.commonDao.findByQuery(hql, new String[]{"supplierCode","itemCode"},
					new Object[]{supplier,itemCode});
			if(wmsMinPackageQty.isEmpty() || wmsMinPackageQty.size() <= 0){
				throw new BusinessException("未找到最小包装量,请重新维护最小包装量");
			}*/
			if(item.getUserFieldD1()==0 || item.getUserFieldD1() == null){
				throw new BusinessException(item.getCode()+"未维护物料最小包装量,请先维护物料最小包装量");
			}
			lotInfo.setExtendPropC12(item.getUserFieldD1().toString());
			//条码
			lotInfo.setExtendPropC17(item.getCode()+"_"+lot+"_0_0_0");
			//批次号
			lotInfo.setLot(lot);
			
			//项目类别 ,0-标准,2-寄售
			if("标准".equals(extendPropc8)){
				lotInfo.setExtendPropC8(WmsFactoryXmlb.BZ);
			}else if("寄售".equals(extendPropc8)){
				lotInfo.setExtendPropC8(WmsFactoryXmlb.JS);
			}else{
				throw new BusinessException("项目类别不正确,请重新维护项目类别！！");
			}
			
			//库存状态
			inventory.setStatus(StringUtils.isEmpty(inventoryStatus)
					|| inventoryStatus.equals("-") ? null : inventoryStatus);
			//库存数量
			inventory.setQty(StringUtils.isEmpty(inventoryDou)
					|| inventoryDou.equals("-") ? 0D : Double.parseDouble(inventoryDou));
			
			// 仓库
			hql="from WmsWarehouse warehouse where warehouse.baseOrganization.id=:baseOrganizationId";
			List<WmsWarehouse> warehouse = this.commonDao.findByQuery(hql, "baseOrganizationId", BaseOrganizationHolder.getThornBaseOrganization().getId());
			if(warehouse.isEmpty() || warehouse.size()<=0){
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

			inventoryManager.in(inventoryDto);
	}
	
	public void changeHouse(WmsInventory inventory,Double quantity,Long locationId){
		inventory = commonDao.load(WmsInventory.class, inventory.getId());
		System.out.println(inventory.getWarehouse().getId());
		
		//如果是从自管仓调到VMI 且 供应商编码为XN则报错  XN供应商不允许调到VMI
		if(StringHelper.in(inventory.getWarehouse().getCode(), new String[]{WarehouseEnumeration.BX,WarehouseEnumeration.XYJ}) 
				&&inventory.getItemKey().getLotInfo().getSupplierCode().equals("XN")){
			throw new BusinessException("XN供应商不允许调到VMI");
		}
		
		Double invQty = inventory.getQty(); //原始数量  qty为新数量
		if(quantity>invQty){
			throw new BusinessException("修改数量大于原始库存数量!!");
		}
		if(CommonHelper.dealDoubleError(quantity)<0.001D){
			throw new BusinessException("数量不得小于0.001");
		}
		WmsLocation loc = commonDao.load(WmsLocation.class, locationId);
		//库存状态
		String status = "";
		if(loc.getCode().equals(WmsLocationCode.HLC)){
			status = "不良品";
		}
		if(!inventory.getLocation().getCode().equals(WmsLocationCode.HLC) && !loc.getCode().equals(WmsLocationCode.HLC) ){
			 status = inventory.getStatus();
		}		 
		if(inventory.getLocation().getCode().equals(WmsLocationCode.HLC) && !loc.getCode().equals(WmsLocationCode.HLC) ){
			 status="合格";
		}
		WmsWarehouse warehouse = loc.getWarehouse();
        WmsCompany company = inventory.getCompany();
        WmsItem item = inventory.getItem();
        WmsPackageUnit unit = inventory.getPackageUnit();
        
        LotInfo lotInfo = inventory.getItemKey().getLotInfo();
        LotInfo newLotInfo = new LotInfo();
        BeanUtils.copyEntity(newLotInfo, lotInfo);//复制新批次
        
        //如果从冰箱或者洗衣机,调拨到VMI，库存类型为寄售。如果从VMI，调拨到冰箱或者洗衣机,库存类型为标准
        if(inventory.getWarehouse().getCode().equals(WarehouseEnumeration.BX) ||
        		inventory.getWarehouse().getCode().equals(WarehouseEnumeration.XYJ) ){
        	newLotInfo.setExtendPropC8(WmsFactoryXmlb.JS);
        }else if(inventory.getWarehouse().getCode().equals(WarehouseEnumeration.VMI)){
        	newLotInfo.setExtendPropC8(WmsFactoryXmlb.BZ);
        }
        
        WmsItemKey newItemKey = wmsItemManager.getItemKeyByLotInfoModify(inventory.getWarehouse(), 
        		newLotInfo.getAsnCustomerBill(), inventory.getItem(), newLotInfo);
        //自管仓寄售类型的库存移回VMI仓,不能传接口告知SAP
        Boolean sendToSAP = Boolean.TRUE;
        if(inventory.getItemKey().getLotInfo()!=null){
	        if(inventory.getItemKey().getLotInfo().stringValue().equals(newItemKey.getLotInfo().stringValue())){
//	            throw new BusinessException("no.lotInfo.modify");
	        	sendToSAP = Boolean.FALSE;
	        }
        }
        
        Double packQty = quantity;
 
        Double qty = WmsPackageUnitUtils.getQtyBU(unit, packQty, item.getBuPrecision());
        
        String descript = "仓库间调拨";
        
        //移入库位新增
        WmsInventoryDto inventoryDto = new WmsInventoryDto();

        inventoryDto.setWarehouse(warehouse);
        inventoryDto.setCompany(company);
        inventoryDto.setLocation(loc);
        inventoryDto.setItem(item);
        inventoryDto.setItemKey(newItemKey);
        inventoryDto.setPackageUnit(unit);
        inventoryDto.setQty(qty);
        inventoryDto.setPackQty(packQty);
        inventoryDto.setStatus(status);//库存状态
        
        inventoryDto.setDescript(descript);
        inventoryDto.setPallet(inventory.getPallet());
        inventoryDto.setCarton(inventory.getCarton());
        inventoryDto.setPalletSeq(inventory.getPalletSeq());
        if(sendToSAP){
        	inventoryDto.setType(WmsInventoryLogType.ITEM_KEY_CHANGE);
        	//移出库位库存扣减
            inventoryManager.out(inventory,qty,WmsInventoryLogType.ITEM_KEY_CHANGE,descript);
        }else{
        	inventoryDto.setType(WmsInventoryLogType.VMI_IN);
        	//移出库位库存扣减
            inventoryManager.out(inventory,qty,WmsInventoryLogType.VMI_OUT,descript);
        }
        //移入库位库存新增
        inventoryManager.in(inventoryDto);
        if(sendToSAP){
        	tclMessageManager.sendChangeTypeInfo(inventory, qty, inventory.getItemKey().getLotInfo().getExtendPropC10(),warehouse.getCode());
        }
	}
	
	
	/**移位*/
	public  void transposing(WmsInventory inventory,Double newFactoryQty,Long newLocId){
	
		WmsLocation newLoc = commonDao.load(WmsLocation.class, newLocId);
		if(!WmsInventoryOperationStatus.NORMAL.equals(inventory.getOperationStatus())) {
			throw new BusinessException("作业状态不是【正常】，不允许移位");
		}
		Double invQty = inventory.getQty(); //原始数量  qty为新数量
		if(newFactoryQty>invQty){
			throw new BusinessException("移位数量大于原始库存数量!!");
		}
		if(CommonHelper.dealDoubleError(newFactoryQty) < 0.001){
			throw new BusinessException("移位数量不能小于0.001!!");
		}
		//移入库位，判断新增库存的库存状态
		 String status="";
		 if(newLoc.getCode().equals(WmsLocationCode.HLC) || inventory.getLocation().getCode().equals(WmsLocationCode.HLC)){
			 throw new BusinessException("坏料仓的移位请用正残品调拨");
		 }
		 if(newLoc.getCode().equals(WmsLocationCode.HLC)){
			    status="不良品";
         }
		 if(!inventory.getLocation().getCode().equals(WmsLocationCode.HLC) && !newLoc.getCode().equals(WmsLocationCode.HLC) ){
			 status = inventory.getStatus();
		 }		 
		 if(inventory.getLocation().getCode().equals(WmsLocationCode.HLC) && !newLoc.getCode().equals(WmsLocationCode.HLC) ){
			 status="合格";
		 }
		 
		String descript = "库内移位";
        //移入库位新增
        WmsInventoryDto inventoryDto = new WmsInventoryDto();
        inventoryDto.setWarehouse(inventory.getWarehouse());
        inventoryDto.setCompany(inventory.getCompany());
        inventoryDto.setLocation(newLoc);
        inventoryDto.setItem(inventory.getItem());
        inventoryDto.setItemKey(inventory.getItemKey());
        inventoryDto.setPackageUnit(inventory.getPackageUnit());
        inventoryDto.setQty(newFactoryQty);
        inventoryDto.setPackQty(WmsPackageUnitUtils.getPackQty(inventory.getPackageUnit(), newFactoryQty, inventory.getItem().getBuPrecision()));
        
        inventoryDto.setStatus(status);
        
        inventoryDto.setType(WmsInventoryLogType.MOVE);
        inventoryDto.setDescript(descript);
        inventoryDto.setPallet(inventory.getPallet());
        inventoryDto.setCarton(inventory.getCarton());
        inventoryDto.setPalletSeq(inventory.getPalletSeq());
        
        //移出库位库存扣减
        inventoryManager.out(inventory,newFactoryQty,WmsInventoryLogType.MOVE,descript);
        //移入库位库存新增
        inventoryManager.in(inventoryDto);	
	}
	
	
	
	public void modifySupplier(WmsInventory inventory,Long supplierId){
		
		if(!StringHelper.in(inventory.getWarehouse().getCode(), 
				new String[]{WarehouseEnumeration.BX,WarehouseEnumeration.XYJ})){
			throw new BusinessException("库存不在自管仓下，不允许修改供应商!!!");
		}
		if(!inventory.getItemKey().getLotInfo().getSupplierCode().equals("XN")){
			throw new BusinessException("供应商不是XN供应商，不允许修改供应商!!!");
		}
		if(!WmsInventoryOperationStatus.NORMAL.equals(inventory.getOperationStatus())) {
			throw new BusinessException("作业状态不是【正常】，不允许修改供应商");
		}
		WmsSupplier supplier = commonDao.load(WmsSupplier.class, supplierId);
		String supplierCode = supplier.getCode(); //供应商编码
		String supplierName = supplier.getName(); //供应商名称
		
        LotInfo lotInfo = inventory.getItemKey().getLotInfo();
        LotInfo newLotInfo = new LotInfo();
        BeanUtils.copyEntity(newLotInfo, lotInfo);//复制新批次
        
        newLotInfo.setSupplierCode(supplierCode);
        newLotInfo.setExtendPropC9(supplierName);
        
        WmsItemKey newItemKey = wmsItemManager.getItemKeyByLotInfoModify(inventory.getWarehouse(), 
        		newLotInfo.getAsnCustomerBill(), inventory.getItem(), newLotInfo);
        
        System.out.println(inventory.getItemKey().getLotInfo().stringValue());
        System.out.println(newItemKey.getLotInfo().stringValue());
        
        if(inventory.getItemKey().getLotInfo()!=null){
	        if(inventory.getItemKey().getLotInfo().stringValue().equals(newItemKey.getLotInfo().stringValue())){
	            throw new BusinessException("no.lotInfo.modify");
	        }
        }
        String descript = "供应商调整";
        
        //移入库位新增
        WmsInventoryDto inventoryDto = new WmsInventoryDto();

        inventoryDto.setWarehouse(inventory.getWarehouse());
        inventoryDto.setCompany(inventory.getCompany());
        inventoryDto.setLocation(inventory.getLocation());
        inventoryDto.setItem(inventory.getItem());
        inventoryDto.setItemKey(newItemKey);
        inventoryDto.setPackageUnit(inventory.getPackageUnit());
        inventoryDto.setQty(inventory.getQty());
        inventoryDto.setPackQty(inventory.getPackQty());
        inventoryDto.setStatus(inventory.getStatus());
        inventoryDto.setType(WmsInventoryLogType.GYS_CHANGE);
        inventoryDto.setDescript(descript);
        inventoryDto.setPallet(inventory.getPallet());
        inventoryDto.setCarton(inventory.getCarton());
        inventoryDto.setPalletSeq(inventory.getPalletSeq());
        
        //移出库位库存扣减
        inventoryManager.out(inventory,inventory.getQty(),WmsInventoryLogType.GYS_CHANGE,descript);
        //移入库位库存新增
        inventoryManager.in(inventoryDto);	
	}
	
	
	public  void modifyXT(WmsInventory inventory,String xt,Double qty){
		
		if(!WmsInventoryOperationStatus.NORMAL.equals(inventory.getOperationStatus())) {
			throw new BusinessException("作业状态不是【正常】，不允许修改线体");
		}
		
		if(!inventory.getLocation().getCode().equals("XBC")){
			throw new BusinessException("库位编码不是【XBC】，不允许修改线体");
		}
		
		Double invQty = inventory.getQty(); //原始数量  qty为新数量
		if(qty>invQty){
			throw new BusinessException("修改数量大于原始库存数量!!");
		}
		if(CommonHelper.dealDoubleError(qty)<0.001D){
			throw new BusinessException("数量不得小于0.001");
		}
		LotInfo lotInfo = inventory.getItemKey().getLotInfo();
	    LotInfo newLotInfo = new LotInfo();
	    
	    BeanUtils.copyEntity(newLotInfo, lotInfo);//复制新批次
	    
	    newLotInfo.setExtendPropC6(xt);
	    
	    WmsItemKey newItemKey = wmsItemManager.getItemKeyByLotInfoModify(inventory.getWarehouse(), 
	        		newLotInfo.getAsnCustomerBill(), inventory.getItem(), newLotInfo);
	   
	    System.out.println(inventory.getItemKey().getLotInfo().stringValue());
	    System.out.println(newItemKey.getLotInfo().stringValue());
	    
		if (inventory.getItemKey().getLotInfo() != null) {
			if (inventory.getItemKey().getLotInfo().stringValue().equals(newItemKey.getLotInfo().stringValue())) {
				throw new BusinessException("no.lotInfo.modify");
			}
		}
	        
		
		String descript = "线体调整";
        //移入库位新增
        WmsInventoryDto inventoryDto = new WmsInventoryDto();

        inventoryDto.setWarehouse(inventory.getWarehouse());
        inventoryDto.setCompany(inventory.getCompany());
        inventoryDto.setLocation(inventory.getLocation());
        inventoryDto.setItem(inventory.getItem());
        inventoryDto.setItemKey(newItemKey);
        inventoryDto.setPackageUnit(inventory.getPackageUnit());
        inventoryDto.setQty(qty);
        inventoryDto.setPackQty(WmsPackageUnitUtils.getPackQty(inventory.getPackageUnit(), qty, inventory.getItem().getBuPrecision()));
        
        inventoryDto.setStatus(inventory.getStatus());
        
        inventoryDto.setType(WmsInventoryLogType.XT_CHANGE);
        inventoryDto.setDescript(descript);
        inventoryDto.setPallet(inventory.getPallet());
        inventoryDto.setCarton(inventory.getCarton());
        inventoryDto.setPalletSeq(inventory.getPalletSeq());
        
        //移出库位库存扣减
        inventoryManager.out(inventory,qty,WmsInventoryLogType.XT_CHANGE,descript);
        //移入库位库存新增
        inventoryManager.in(inventoryDto);
		
	}
	/**
	 * 自管仓间调拨
	 */
	public void changeHouseInZG(WmsInventory inventory,Double newFactoryQty,String newFactoryCode){

		if(!WmsInventoryOperationStatus.NORMAL.equals(inventory.getOperationStatus())) {
			throw new BusinessException("作业状态不是【正常】，不允许内外销调拨");
		}
		Double invQty = inventory.getQty(); //原始数量  qty为新数量
		if(newFactoryQty>invQty){
			throw new BusinessException("修改数量大于原始库存数量!!");
		}
		if(CommonHelper.dealDoubleError(newFactoryQty)<0.001D){
			throw new BusinessException("数量不得小于0.001");
		}
		WmsSapFactory factory = (WmsSapFactory)commonDao.findByQueryUniqueResult("FROM WmsSapFactory factory where factory.code=:fc", "fc",newFactoryCode);
		if(null==factory){
			throw new BusinessException("工厂"+newFactoryCode+"未找到");
		}
		//校验物料是否在两个工厂都存在
		String hql ="FROM WmsItemFactory f WHERE f.item.id =:itemId AND f.factory.code =:factoryCode ";
		List<WmsItemFactory> oldFactorys = commonDao.findByQuery(hql, new String[]{"itemId","factoryCode"}, new Object[]{inventory.getItem().getId(),inventory.getItemKey().getLotInfo().getExtendPropC10()});
		List<WmsItemFactory> newFactorys = commonDao.findByQuery(hql, new String[]{"itemId","factoryCode"}, new Object[]{inventory.getItem().getId(),newFactoryCode});
		if(oldFactorys.isEmpty()){
			throw new BusinessException("物料不属于工厂"+inventory.getItemKey().getLotInfo().getExtendPropC10());
		}
		if(newFactorys.isEmpty()){
			throw new BusinessException("物料不属于工厂"+newFactoryCode);
		}
		WmsWarehouse warehouse = null;
		hql = "select fw.warehouse from WmsFactoryWarehouse fw where fw.factory.id=:factoryId and fw.type=:type";
		warehouse = (WmsWarehouse) commonDao.findByQueryUniqueResult(hql, new String[]{"factoryId","type"}, 
				new Object[]{factory.getId(),WmsFactoryXmlb.BZ});
		hql = "from WmsLocation loc where loc.code=:code and loc.warehouse.id=:warehouseId ";
		WmsLocation loc = (WmsLocation) commonDao.findByQueryUniqueResult(hql, new String[]{"code","warehouseId"}, 
				new Object[]{inventory.getLocation().getCode(),warehouse.getId()});
        WmsCompany company = inventory.getCompany();
        WmsItem item = inventory.getItem();
        WmsPackageUnit unit = inventory.getPackageUnit();
        
        Double packQty = newFactoryQty;
 
        Double qty = WmsPackageUnitUtils.getQtyBU(unit, packQty, item.getBuPrecision());
        
        LotInfo lotInfo = inventory.getItemKey().getLotInfo();
        LotInfo newLotInfo = new LotInfo();
        BeanUtils.copyEntity(newLotInfo, lotInfo);//复制新批次
        newLotInfo.setExtendPropC10(factory.getCode());
        newLotInfo.setExtendPropC11(factory.getName());
        WmsItemKey newItemKey = wmsItemManager.getItemKeyByLotInfoModify(warehouse, 
        		newLotInfo.getAsnCustomerBill(), inventory.getItem(), newLotInfo);
        System.out.println(inventory.getItemKey().getLotInfo().stringValue());
        System.out.println(newItemKey.getLotInfo().stringValue());
        if(inventory.getItemKey().getLotInfo()!=null){
	        if(inventory.getItemKey().getLotInfo().stringValue().equals(newItemKey.getLotInfo().stringValue())){
	            throw new BusinessException("no.lotInfo.modify");
	        }
        }
        String descript = "自管仓间调拨";
        
        //移入库位新增
        WmsInventoryDto inventoryDto = new WmsInventoryDto();

        inventoryDto.setWarehouse(warehouse);
        inventoryDto.setCompany(company);
        inventoryDto.setLocation(loc);
        inventoryDto.setItem(item);
        inventoryDto.setItemKey(newItemKey);
        inventoryDto.setPackageUnit(unit);
        inventoryDto.setQty(qty);
        inventoryDto.setPackQty(packQty);
        inventoryDto.setStatus(inventory.getStatus());
        inventoryDto.setType(WmsInventoryLogType.ITEM_KEY_CHANGE);
        inventoryDto.setDescript(descript);
        inventoryDto.setPallet(inventory.getPallet());
        inventoryDto.setCarton(inventory.getCarton());
        inventoryDto.setPalletSeq(inventory.getPalletSeq());
        
        //移出库位库存扣减
        inventoryManager.out(inventory,qty,WmsInventoryLogType.ITEM_KEY_CHANGE,descript);
        //移入库位库存新增
        inventoryManager.in(inventoryDto);
		
        tclMessageManager.sendChangeTypeInfo(inventory, newFactoryQty, newFactoryCode,warehouse.getCode());
		
	
	}
}
