package com.vtradex.wms.rfserver.service.move;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;

/** 
* @ClassName: 按货品移位处理类 
* @Description: RfNPMoveBySkuManager
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-8-16 下午5:41:33  
*/
public interface TclRfNPMoveBySkuManager extends BaseManager{
	
	/**===============================无计划移位======================================*/
	/**库位扫描*/
	@Transactional
	Map<String,Object> tclMoveBySkuLocationCommit(Map<String,Object> params);
	
	/**库位信息*/
	@Transactional
	Map<String,Object> loctionInfo(Map<String,Object> params);
	
	/**货品扫描*/
	@Transactional
	Map<String,Object> tclMoveBySkuItemCommit(Map<String,Object> params);
	
	/**库存信息*/
	@Transactional
	Map<String,Object> inventoryInfo(Map<String,Object> params);
	
	/**移位确认*/
	@Transactional
	Map<String,Object> moveBySkuComfirmCommit(Map<String,Object> params);
	
	/**移位验证*/
	@Transactional
	Map<String,Object> moveBySkuComfirmCommitValidate(Map<String,Object> params);
	
	
	
	
	
	/**==============================有计划移位============================*/
	/** 
	* @Title: locationInputCommit 
	* @Description: 按库位的拣选动线号，从小到大提示任务，扫描库位时如跟任务所在库位不一致，
	* 则系统查找，是否实际扫描的库位存在此移位单的任务，
	* 如果只存在一个任务，则调整到第4个页面，否则调整到第3个页面。
	* @return Map<String,Object>   
	* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
	* @date 2017-8-17 下午5:31:18  
	* @throws 
	*/
	@Transactional
	Map<String,Object> locationInputCommit(Map<String,Object> params);
	
	
	/** 
	* @Title: showMoveCode 
	* @Description:按货品计划移位显示选中的移位单号与库位
	* @return Map<String,Object>   
	* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
	* @date 2017-8-17 下午5:46:09  
	* @throws 
	*/
	@Transactional
	Map<String,Object> showMoveCode(Map<String,Object> params);
	
	
	/** 
	* @Title: showItemCode 
	* @Description: 货品展示
	* @return Map<String,Object>   
	* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
	* @date 2017-8-17 下午6:12:38  
	* @throws 
	*/
	@Transactional
	Map<String,Object> showItemCode(Map<String,Object> params);
	
	
	/** 
	* @Title: itemInputCommit 
	* @Description: 检验货品是否一致
	* @return Map<String,Object>   
	* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
	* @date 2017-8-17 下午6:16:33  
	* @throws 
	*/
	@Transactional
	Map<String,Object> itemInputCommit(Map<String,Object> params);
	
	
	/** 
	* @Title: showTargetLocation 
	* @Description: 显示目标库位
	* @return Map<String,Object>   
	* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
	* @date 2017-8-17 下午6:44:55  
	* @throws 
	*/
	@Transactional
	Map<String,Object> showTargetLocation(Map<String,Object> params);
	
	
	/** 
	* @Title: targetLocationInputCommit 
	* @Description: 移位
	* @return Map<String,Object>   
	* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
	* @date 2017-8-17 下午6:51:49  
	* @throws 
	*/
	@Transactional
	Map<String,Object> targetLocationInputCommit(Map<String,Object> params);
	
	
	/** 
	* @Title: moveBySkuComfirmCommitValidate 
	* @Description: TODO
	* @return Map<String,Object>   
	* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
	* @date 2017-8-17 下午7:04:04  
	* @throws 
	*/
	
	@Transactional
	Map<String,Object> moveBySkuComfirmCommitValidates(Map<String,Object> params);
	
	
	
}
