package com.vtradex.wms.rfserver.service.inventory;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.rf.common.exception.RfBusinessException;
import com.vtradex.thorn.server.service.BaseManager;


/** 
* @ClassName: RF库存查询
* @Description: TclRfInventoryQueryManager 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-8-8 下午6:01:28  
*/
public interface TclRfInventoryQueryManager extends BaseManager{
	/** 
	* @Title: inventoryQueryInputCommit 
	* @Description: 扫描库位,校验库位;扫描货品,校验货品
	* @return Map   
	* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
	* @date 2017-8-8 下午6:10:15  
	* @throws 
	*/
	
	@Transactional
	public Map inventoryQueryInputCommit(Map params);
	
	/** 
	* @Title: inventoryInfo 
	* @Description: 库存信息
	* @return Map   
	* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
	* @date 2017-8-9 上午9:52:19  
	* @throws 
	*/
	
	@Transactional
	public Map inventoryInfo(Map params);
	
	/** 
	* @Title: previousPage 
	* @Description: 返回上一页
	* @return Map   
	* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
	* @date 2017-8-9 上午9:58:25  
	* @throws 
	*/
	
	@Transactional
	public Map previousPage(Map params);
	
	/** 
	* @Title: SwitchReturn 
	* @Description: 返回
	* @return Map   
	* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
	* @date 2017-8-9 上午10:00:22  
	* @throws 
	*/
	
	@Transactional
	public Map switchReturn(Map params);
	/**
	 * 显示库存信息
	 * @param params
	 * @return
	 */
	@Transactional
	public Map inventorySumInfo(Map params) throws RfBusinessException;
	
	/**
	 * 扫描物料编码/条码
	 */
	@Transactional
	public Map inventorySumQueryInputCommit(Map params);
}
