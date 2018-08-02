package com.vtradex.wms.server.service.itemFactory.pojo;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.entity.base.WmsItemFactory;
import com.vtradex.wms.server.service.itemFactory.WmsItemFactoryManager;

public class DefaultWmsItemFactoryManager extends DefaultBaseManager implements WmsItemFactoryManager{

	@Override
	public void storeItemFactory(WmsItemFactory itemFactory) {
		WmsItemFactory factory = (WmsItemFactory)commonDao.findByQueryUniqueResult("FROM WmsItemFactory itf where itf.factory=:ft and itf.item=:fi", new String[]{"ft","fi"}, new Object[]{itemFactory.getFactory(),itemFactory.getItem()});
		if(itemFactory.isNew()){
			if(null!=factory){
				throw new BusinessException("该工厂对应的物料已维护！！");
			}
		}
		commonDao.store(itemFactory);
	}

}
