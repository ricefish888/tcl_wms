package com.vtradex.wms.server.service.receiving;

import com.vtradex.wms.server.model.entity.receiving.WmsASN;

public interface WmsTclNoTransactionalManager extends WmsNoTransactionalManager{

	public void manualCreateWorkDoc(WmsASN wmsAsn);
	
}
