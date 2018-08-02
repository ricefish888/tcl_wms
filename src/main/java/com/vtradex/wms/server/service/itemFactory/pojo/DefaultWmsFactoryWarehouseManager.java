package com.vtradex.wms.server.service.itemFactory.pojo;

import java.util.List;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.entity.base.WmsFactoryWarehouse;
import com.vtradex.wms.server.service.itemFactory.WmsFactoryWarehouseManager;

public class DefaultWmsFactoryWarehouseManager extends DefaultBaseManager implements WmsFactoryWarehouseManager{

	
	//保存
	public void save(WmsFactoryWarehouse factoryWarehouse){
		if(factoryWarehouse.isNew()){
			 String hql = "FROM WmsFactoryWarehouse w WHERE w.factory.id =:id AND w.type =:type";
			   
			 List<WmsFactoryWarehouse>  list = commonDao.findByQuery(hql, new String[]{"id","type"}, 
					                                        new Object[]{factoryWarehouse.getFactory().getId(),factoryWarehouse.getType()});
			   
			 if(list.size()>0){
				   throw new BusinessException("此工厂与项目类别已存在");   
			   }
		 commonDao.store(factoryWarehouse);
		}else{
			
			 String hql = "FROM WmsFactoryWarehouse w WHERE w.factory.id =:id AND w.type =:type AND w.id <>:factoryWarehouseId";
			   
			 List<WmsFactoryWarehouse>  list = commonDao.findByQuery(hql, new String[]{"id","type","factoryWarehouseId"}, 
					                                        new Object[]{factoryWarehouse.getFactory().getId(),factoryWarehouse.getType(),factoryWarehouse.getId()});
			   
			 if(list.size()>0){
				   throw new BusinessException("此工厂与项目类别已存在");   
			   }
			
			 commonDao.store(factoryWarehouse);
		}
	}
	
	
	
	//删除
	public void delete(WmsFactoryWarehouse factoryWarehouse){
		commonDao.delete(factoryWarehouse);	
	}
}
