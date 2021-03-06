package com.vtradex.wms.server.service.supplier;

import java.io.File;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;

/** 
* @ClassName:  供应商Manager
* @Description: WmsSurpplyManager
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-3 下午4:29:21  
*/
public interface WmsSupplierManager extends BaseManager{
	/**供应商保存*/
	@Transactional
	void storeSurpplier(WmsSupplier surpplier);
	/**供应商失效*/
	@Transactional
	void unActiveSurpplier(WmsSupplier surpplier);
	/**供应商生效*/
	@Transactional
	void activeSurpplier(WmsSupplier surpplier);
	
	/**
	 * 导入
	 */
	@Transactional
	void importWmsSupplierFile(File file);
	
}
