package com.vtradex.wms.rfserver.service.count;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.rf.common.exception.RfBusinessException;
import com.vtradex.thorn.server.service.BaseManager;


/**
 * 
 * @ClassName: TclRfCountPlanManager
 * 
 * @author jianxiang.hang
 *
 * @date 2017年8月24日 上午09:52:38 
 *
 * @Description: 盘点登记
 */
public interface TclRfCountPlanManager extends BaseManager{
	
	/**
	 * 
	 * @Title: showLocationCode
	 * 
	 * @Description: 按库位的盘点动线号(查询盘点明细为待分配状态)，从小到大提示库位
	 *
	 * @return Map    
	 *
	 * @throws 
	 *
	 * @author wencheng.liu
	 *
	 * @date 2016年9月5日 上午10:17:07
	 */
	@Transactional
	public Map showLocationCode(Map countPlanMap) throws RfBusinessException;
	
	/**
	 * 
	 * @Title: locationInputCommit
	 * 
	 * @Description: 库位输入，校验
	 *  允许实际扫描的库位不按照系统建议的顺序：
     *  1）当实际扫描库位不在盘点计划里时，提示错误“该库位不在当前盘点计划内，请重新扫描库位！”；
     *  2）当实际库位对应盘点明细正在盘点中时，提示”该库位正在盘点,请扫描其他库位！”；
	 *  明细变成盘点中
	 * @return Map    
	 *
	 * @throws 
	 *
	 * @author wencheng.liu
	 *
	 * @date 2016年9月5日 上午11:02:32
	 */
	@Transactional
	public Map locationInputCommit(Map countPlanMap) throws RfBusinessException;
	
	/**
	 * 
	 * @Title: showItemCode
	 * 
	 * @Description: 显示货品名称代码
	 *
	 * @return Map    
	 *
	 * @throws 
	 *
	 * @author wencheng.liu
	 *
	 * @date 2016年9月5日 下午2:13:04
	 */
	@Transactional
	public Map showItemCode(Map countPlanMap) throws RfBusinessException;
	
	/**
	 * 
	 * @Title: itemInputCommit
	 * 
	 * @Description: 校验货品
	 *
	 * @return Map    
	 *
	 * @throws 
	 *
	 * @author wencheng.liu
	 *
	 * @date 2016年9月5日 下午2:18:46
	 */
	@Transactional
	public Map itemInputCommit(Map countPlanMap) throws RfBusinessException;
	
	/**
	 * 
	 * @Title: countPlanItemForNullCommit
	 * 
	 * @Description: 缺货登记：及盘点数量为0
	 *
	 * @return Map    
	 *
	 * @throws 
	 *
	 * @author wencheng.liu
	 *
	 * @date 2016年9月5日 下午2:25:08
	 */
	@Transactional
	public Map countPlanItemForNullCommit(Map countPlanMap) throws RfBusinessException;
	
	/**
	 * 
	 * @Title: showNumber
	 * 
	 * @Description: 显示原始数量
	 *
	 * @return Map    
	 *
	 * @throws 
	 *
	 * @author wencheng.liu
	 *
	 * @date 2016年9月5日 下午2:59:25
	 */
	@Transactional
	public Map showNumber(Map countPlanMap) throws RfBusinessException;
	
	/**
	 * 
	 * @Title: confirmInputCommit
	 * 
	 * @Description:输入盘点数量
	 *
	 * @return Map    
	 *
	 * @throws 
	 *
	 * @author wencheng.liu
	 *
	 * @date 2016年9月5日 下午3:02:36
	 */
	@Transactional
	public Map confirmInputCommit(Map countPlanMap) throws RfBusinessException;
	
	/**
	 * 
	 * @Title: showInsertLocationCode
	 * 
	 * @Description: 盘点增补输入货品页面显示库位
	 *
	 * @return Map    
	 *
	 * @throws 
	 *
	 * @author wencheng.liu
	 *
	 * @date 2016年9月5日 下午3:30:42
	 */
	@Transactional
	public Map showInsertLocationCode(Map countPlanMap) throws RfBusinessException;
	
	/**
	 * 
	 * @Title: countInsertItemInputCommit
	 * 
	 * @Description: 校验货品是否存在
	 *
	 * @return Map    
	 *
	 * @throws 
	 *
	 * @author wencheng.liu
	 *
	 * @date 2016年9月5日 下午3:33:56
	 */
	@Transactional
	public Map countInsertItemInputCommit(Map countPlanMap) throws RfBusinessException;
	
	/**
	 * 
	 * @Title: showCountInsertNumber
	 * 
	 * @Description:输入盘点增补数量页面显示
	 *
	 * @return Map    
	 *
	 * @throws 
	 *
	 * @author wencheng.liu
	 *
	 * @date 2016年9月5日 下午4:39:41
	 */
	@Transactional
	public Map showCountInsertNumber(Map countPlanMap) throws RfBusinessException;
	
	/**
	 * 
	 * @Title: countInsertNumberCommit
	 * 
	 * @Description: 盘点增补数量输入
	 *
	 * @return Map    
	 *
	 * @throws 
	 *
	 * @author wencheng.liu
	 *
	 * @date 2016年9月5日 下午4:43:56
	 */
	@Transactional
	public Map countInsertNumberCommit(Map countPlanMap) throws RfBusinessException;
	
	/**
	 * 
	 * @Title: changeLocation
	 * 
	 * @Description:切换库位
	 *
	 * @return Map    
	 *
	 * @throws 
	 *
	 * @author wencheng.liu
	 *
	 * @date 2016年9月5日 下午4:59:37
	 */
	@Transactional
	public Map changeLocation(Map countPlanMap) throws RfBusinessException;
}
