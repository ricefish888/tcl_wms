package com.vtradex.wms.server.service.bol;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.wms.server.model.entity.bol.WmsBol;
import com.vtradex.wms.server.model.entity.bol.WmsBolDetail;

/**
 * 
 * Tcl 定制化Bol业务
 * 
 * @author <a href="zhen.lei@vtradex.com">Yogurt_lei</a>
 * 
 * @date 2017年7月21日 下午12:43:59
 */
public interface WmsTclBolManager {
	@SuppressWarnings("rawtypes")
	@Transactional
	void addBOLDetail(Long bolId,WmsBolDetail detail,List values);
	
	/**调拨类型BOL保存*/
	@Transactional
	public void storeBOLDB(WmsBol bol) ;
	
	
	
	/** 
	 * VMI交接单保存
	 */
	@Transactional
	void storeBOLVMI(WmsBol bol);
	
}
