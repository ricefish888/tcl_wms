package com.vtradex.wms.server.service.base.po;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.wms.server.model.entity.base.WmsItemKeeper;
import com.vtradex.wms.server.model.entity.base.WmsSapFactory;
import com.vtradex.wms.server.model.entity.base.WmsSapWarehouseStatus;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.base.WmsItemKeeperManager;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.webservice.utils.ExcelHelper;

/**
 * 成本中心
 */
public class DefaultWmsItemKeeperManager extends DefaultBaseManager implements
		WmsItemKeeperManager {

	// 保存
	public void store(WmsItemKeeper itemKeeper) {

		WmsSapFactory factory = itemKeeper.getFactory();// 工厂
		WmsItem item = itemKeeper.getItem();// 物料
		WmsWarehouse warehouse = itemKeeper.getWarehouse();// 仓库
		
		String hql = "select keep FROM WmsItemKeeper keep "
				+ "LEFT JOIN keep.factory "
				+ "WHERE keep.item.id =:itemId AND keep.warehouse.id =:warehouseId ";
		if (factory == null) {
			hql += "AND keep.factory.id is null";
		} else {
			hql += "AND keep.factory.id =" + factory.getId();
		}
		if(!itemKeeper.isNew()) {
			hql += " and keep.id<>"+itemKeeper.getId();
		}
		WmsItemKeeper wmsItemKeeper = (WmsItemKeeper) commonDao.findByQueryUniqueResult(hql,
				                                      new String[] { "itemId","warehouseId" },
				                                      new Object[] { item.getId(), warehouse.getId() });

		if (itemKeeper.isNew()) {//新建
			if(wmsItemKeeper != null) {
				throw new BusinessException("物料保管员信息已存在，请重新维护");
			}
		}else{//修改
			if(wmsItemKeeper != null){
				throw new BusinessException("物料保管员信息已存在，请重新维护");
			}
		}
		
		itemKeeper.setItem(item);
		itemKeeper.setWarehouse(warehouse);
		itemKeeper.setFactory(factory);
		itemKeeper.setKeeper(itemKeeper.getKeeper());
		itemKeeper.setStatus(WmsSapWarehouseStatus.ENABLED);
		commonDao.store(itemKeeper);

	}

	// 导入
	public void importWmsItemKeeperFile(File file) {
		
		// 仓库
		String warehouseHql = "FROM WmsWarehouse w WHERE w.baseOrganization.id =:Id";
		WmsWarehouse warehouse111 = (WmsWarehouse) commonDao.findByQueryUniqueResult(warehouseHql, "Id",
				BaseOrganizationHolder.getThornBaseOrganization().getId());
        /**暂时放开，上线期间，物料保管员属性比较乱，VMI可以导入物料保管员属性*/
//		if (warehouse111.getCode().equals("VMI")) {   
//			throw new BusinessException("VMI仓库不允许导入！！！");
//		} 
		
//		if (warehouse.getCode().equals("XYJ")) {
//			if ("洗衣机库".equals(warehouseName) || "VMI库".equals(warehouseName)) {
//				if ("1100".equals(factoryCode) || "1000".equals(factoryCode)) {
//					throw new BusinessException("冰箱工厂不允许在洗衣机库里导入！！！");
//				}
//			} else {
//				throw new BusinessException("冰箱不允许在洗衣机库里导入！！！");
//			}
//		} else if (warehouse.getCode().equals("BX")) {
//			if ("冰箱库".equals(warehouseName) || "VMI库".equals(warehouseName)) {
//				if ("2100".equals(factoryCode) || "2000".equals(factoryCode)) {
//					throw new BusinessException("洗衣机工厂不允许在冰箱库里导入！！！");
//				}
//			} else {
//				throw new BusinessException("洗衣机不允许在冰箱库里导入！！！");
//			}
//		}
		
		
		String Hql = "FROM WmsWarehouse w";
		List<WmsWarehouse> all_warehouse =   commonDao.findByQuery(Hql);
		Map<String,WmsWarehouse> w_map = new HashMap<String,WmsWarehouse>();
		for(WmsWarehouse w : all_warehouse ){
			w_map.put(w.getName(),w);
		}
		
		Hql = "FROM WmsSapFactory";
		List<WmsSapFactory> all_fas =   commonDao.findByQuery(Hql);
		Map<String,WmsSapFactory> fas_map = new HashMap<String,WmsSapFactory>();
		for(WmsSapFactory f : all_fas ){
			fas_map.put(f.getCode(),f);
		}
		
		String keeperHql = " FROM ThornUser keeper  WHERE  1=1 "
//	             + " and keeper.loginName =:code " 
	             + " AND keeper.id in "
		         + " ( SELECT u.id FROM ThornUser u,ThornGroup g "
		         + " where u.id in elements(g.users) AND g.code in "
		         + " ('VMICKBGY_GRP','CKBGY_GRP'))";
		
		List<ThornUser> keepers =  commonDao.findByQuery(keeperHql); 
		Map<String,ThornUser> u_map = new HashMap<String,ThornUser>();
		for(ThornUser u : keepers ){
			u_map.put(u.getLoginName(),u);
		}
		
		List<Map<String, Object>> infos = ExcelHelper.parseExcel2List(file);
		
		for (Map<String, Object> info : infos) {
			
			String  warehouseName= (String)info.get("仓库");
			StringHelper.assertNullOrEmpty(warehouseName, "仓库不能为空，请维护物料仓库");
			
			if(!"洗衣机库".equals(warehouseName) && !"冰箱库".equals(warehouseName) && !"VMI库".equals(warehouseName)){
				throw new BusinessException("仓库名称【" + warehouseName + "】，格式不正确，请重新维护!!!");
			}
			//只能导入当前仓库下的物料保管员对应关系
			if(!warehouseName.equals(warehouse111.getName())){
				throw new BusinessException("只能导入"+warehouse111.getName()+"下的物料保管员关系");
			}
			
//			String Hql = "FROM WmsWarehouse w WHERE w.name =:name";
//			WmsWarehouse warehouse1 = (WmsWarehouse) commonDao.findByQueryUniqueResult(Hql,"name",warehouseName);
			WmsWarehouse warehouse1 = w_map.get(warehouseName);
			
			if(warehouse1 == null){
				throw new BusinessException("根据仓库名称【" + warehouseName + "】，未找到仓库!!!");
			}
			String keeperCode = (String) info.get("保管员");
			if (keeperCode == null || "".equals(keeperCode)) {
				throw new BusinessException("保管员不能为空，请维护保管员");
			}
			
			
			String itemCode = (String) info.get("物料编码");
			StringHelper.assertNullOrEmpty(itemCode, "物料编码不能为空，请维护物料编码");

			WmsItem item = (WmsItem) commonDao.findByQueryUniqueResult(
					"FROM WmsItem w WHERE w.code =:code", "code", itemCode);
			if (item == null) {
				throw new BusinessException("根据物料编码【" + itemCode + "】，未找到物料!!!");
			}

			

			
			String factoryCode = (String) info.get("工厂编码");
			WmsSapFactory factory;
			if (factoryCode == null || "".equals(factoryCode)) {
				factory = null;
			} else {
//				factory = (WmsSapFactory) commonDao.findByQueryUniqueResult(
//						"FROM WmsSapFactory w WHERE w.code =:code", "code",
//						factoryCode);
				factory=fas_map.get(factoryCode);
				if (factory == null) {
					throw new BusinessException("根据工厂编码【" + factoryCode
							+ "】，未找到工厂!!!");
				}
			}
			
//			String keeperHql = " FROM ThornUser keeper  WHERE "
//				             + " keeper.loginName =:code " 
//				             + " AND keeper.id in "
//					         + " ( SELECT u.id FROM ThornUser u,ThornGroup g "
//					         + " where u.id in elements(g.users) AND g.code in "
//					         + " ('VMICKBGY_GRP','CKBGY_GRP'))";
//			ThornUser keeper = (ThornUser) commonDao.findByQueryUniqueResult(keeperHql, "code",keeperCode);
			ThornUser keeper = u_map.get(keeperCode);
			if (keeper == null) {
				throw new BusinessException("根据保管员登录名【" + keeperCode
						+ "】，未找到保管员!!!");
			}

			String hql = "select keep FROM WmsItemKeeper keep "
					+ "LEFT JOIN keep.factory "
					+ "WHERE keep.item.id =:itemId AND keep.warehouse.id =:warehouseId ";
			if (factory == null) {
				hql += "AND keep.factory.id is null";
			} else {
				hql += "AND keep.factory.id =" + factory.getId();
			}
			WmsItemKeeper wmsItemKeeper = (WmsItemKeeper) commonDao
					.findByQueryUniqueResult(hql, new String[] { "itemId",
							"warehouseId" }, new Object[] { item.getId(),
							warehouse1.getId() });
			if (wmsItemKeeper == null) {// 不存在,新建
				wmsItemKeeper = EntityFactory.getEntity(WmsItemKeeper.class);

			}
			wmsItemKeeper.setItem(item);
			wmsItemKeeper.setWarehouse(warehouse1);
			wmsItemKeeper.setFactory(factory);
			wmsItemKeeper.setKeeper(keeper);
			wmsItemKeeper.setStatus(WmsSapWarehouseStatus.ENABLED);
			commonDao.store(wmsItemKeeper);
		}
	}
}
