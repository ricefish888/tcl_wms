package com.vtradex.wms.server.service.base.po;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.entity.base.WmsWarning;
import com.vtradex.wms.server.service.base.WmsWarningManager;

/**
 * 成本中心
 */
public class DefaultWmsWarningManager extends DefaultBaseManager implements WmsWarningManager {

	//删除
	public void delete(WmsWarning wmsWarning){
		commonDao.delete(wmsWarning);
	}
	
}
