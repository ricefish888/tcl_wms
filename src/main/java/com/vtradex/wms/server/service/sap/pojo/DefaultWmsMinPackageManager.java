package com.vtradex.wms.server.service.sap.pojo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.security.ThornGroup;
import com.vtradex.thorn.server.model.security.ThornRole;
import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.enumeration.GroupCodeEnum;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsMinPackageQty;
import com.vtradex.wms.server.service.sap.WmsMinPackageManager;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.webservice.utils.ExcelHelper;

public class DefaultWmsMinPackageManager extends DefaultBaseManager implements WmsMinPackageManager{

	@Override
	public void storeMinPackage(WmsMinPackageQty min) {
		WmsMinPackageQty mpk = (WmsMinPackageQty)commonDao.findByQueryUniqueResult("FROM WmsMinPackageQty wmpq where wmpq.supplier=:ws and wmpq.item=:wi", new String[]{"ws","wi"}, new Object[]{min.getSupplier(),min.getItem()});
		if(min.isNew()){
			if(null!=mpk){
				throw new BusinessException("该供应商对应的物料已维护！！");
			}
		}
		if(min.getMinUnit() <= 0D){
			throw new BusinessException("最小包装量不能小于等于0！！");
		}
		commonDao.store(min);
	}
	
	
	
	//导入最小包装量
	public void importWmsMinPackageQtyFile(File file){
		//校验当前用户有无绑定供应商角色,如果绑定了,那么只能导入自己的数据,没有绑定可以导所有供应商的数据
		ThornUser user = UserHolder.getUser();
		ThornGroup group = findGroupByUser(user);
		List<WmsMinPackageQty>  minPackageQtys =readWmsMinPackageQtyFileExcel(file,group);
		for(WmsMinPackageQty minPackageQty : minPackageQtys){
			WmsMinPackageQty wmsMinPackageQty = (WmsMinPackageQty) commonDao.findByQueryUniqueResult("FROM WmsMinPackageQty w WHERE w.supplier.code =:supplierCode AND w.item.code =:itemCode",
					             new String[]{"supplierCode","itemCode"}, new Object[]{minPackageQty.getSupplier().getCode(),minPackageQty.getItem().getCode()}); 
			if(wmsMinPackageQty == null){
				commonDao.store(minPackageQty);
			}else{
				wmsMinPackageQty.setMinUnit(minPackageQty.getMinUnit());
				commonDao.store(wmsMinPackageQty);
			}
		}
	}
	
	
	
	 private List<WmsMinPackageQty> readWmsMinPackageQtyFileExcel(File file,ThornGroup group){
		 
		 List<WmsMinPackageQty> list = new ArrayList<WmsMinPackageQty>();
		 List<Map<String,Object>> infos = ExcelHelper.parseExcel2List(file);
	    	for(Map<String,Object> info : infos){
	    		String supplierCode = (String) info.get("供应商编码");
	    		if(supplierCode == null || "".equals(supplierCode)){
	    			throw new BusinessException("行号"+info.get("EXCEL行号")+"供应商编码不能为空，请维护供应商编码");
	    		}
	    		String supplierHql = "FROM WmsSupplier w WHERE w.code =:code";
	    		WmsSupplier supplier = (WmsSupplier) commonDao.findByQueryUniqueResult(supplierHql, "code", supplierCode);
	    		if(supplier == null){
	    			throw new BusinessException("行号"+info.get("EXCEL行号")+"根据供应商编码"+"【"+supplierCode+"】"+"未找到供应商，请重新维护供应商编码");
	    		}
	    		//line 38
	    		if(null != group && !supplier.getCode().equals(UserHolder.getUser().getLoginName())){
	    			throw new BusinessException("当前用户没有权限导入其它供应商的数据,请联系管理员!!");
	    		}
	    		
	    		String itemCode = (String) info.get("物料编码");
	    		if(itemCode == null || "".equals(itemCode)){
	    			throw new BusinessException("行号"+info.get("EXCEL行号")+"物料编码不能为空，请维护物料编码");
	    		}
	    		String itemHql = "FROM WmsItem w WHERE w.code =:code";
	    		WmsItem item = (WmsItem) commonDao.findByQueryUniqueResult(itemHql, "code", itemCode);
	    		if(item == null){
	    			throw new BusinessException("行号"+info.get("EXCEL行号")+"根据物料编码"+"【"+itemCode+"】"+"未找到物料，请重新维护物料编码");
	    		}
	    		
	    		String minUnit = (String) info.get("最小包装量");
	    		double mu;
	    		try{
	    			 mu = Double.valueOf(minUnit);		    			
	    		}catch(Exception e){
	    			 throw new BusinessException("行号"+info.get("EXCEL行号")+"输入的数据"+"【"+minUnit+"】"+"不正确,请重新输入数据");
	    		}
	    		
	    		if(StringHelper.isNullOrEmpty(minUnit)){
	    			throw new BusinessException("行号"+info.get("EXCEL行号")+"最小包装量不能为空，请维护最小包装量");
	    		}else if(mu <= 0D){//最小包装量不能小于等于0
	    			throw new BusinessException("行号"+info.get("EXCEL行号")+"最小包装量不能小于等于0！！");
	    		}
	    		
	    		WmsMinPackageQty minPackageQty = new WmsMinPackageQty();
	    		minPackageQty.setSupplier(supplier);
	    		minPackageQty.setItem(item);
	    		minPackageQty.setMinUnit(mu);		    			
	    		
	    		
	    		list.add(minPackageQty);
	    	}
		return list;		 
	 }
	 
	 /**判断当前用户有没有绑定供应商角色,有就返回此角色对象,没有返回空   fs */
	 private ThornGroup findGroupByUser(ThornUser tUser){
		 ThornUser user = commonDao.load(ThornUser.class, tUser.getId());
		 for(ThornGroup group : user.getGroups()){
			 if(group.getCode().equals(GroupCodeEnum.SUPPLIER_GRP)){
				 return group;
			 }
		 }
		 return null;
	 }

}
