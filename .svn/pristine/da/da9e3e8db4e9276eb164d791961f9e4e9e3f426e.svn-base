package com.vtradex.wms.rfserver.service.pickticket;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.rf.common.exception.RfBusinessException;

/**
 * 
 *
 */
public interface TclRfPickticketManager extends RfPickticketManager{
	
	/**
	 * 
	 * @Title: inputItemCommit
	 * 
	 * @Description: 输入货品
	 *
	 * @return Map    
	 *
	 * @throws 
	 *
	 * @author <a href="mailto:wencheng.liu@vtradex.com"/>刘文成/a>
	 *
	 * @date 2016年11月30日 下午4:23:00
	 */
	@Transactional
	public Map inputItemCommit(Map workDocMap);
	/**
	 * 显示作业单信息
	 */
	@Transactional
	public Map showLocCodeAndItemCode(Map workDocMap);
	
	/**
	 * 
	 * @Title: forwardPage
	 * 
	 * @Description: List页面跳转
	 *
	 * @return Map    
	 *
	 * @throws 
	 *
	 * @author <a href="mailto:wencheng.liu@vtradex.com"/>刘文成/a>
	 *
	 * @date 2016年11月30日 下午3:21:58
	 */
	public Map forwardPage(Map workDocMap);
	/**
	 * 显示作业任务信息
	 * @param workDocMap
	 * @return
	 */
	@Transactional
	public Map showWmsTaskInfo(Map workDocMap);
	
	/**
	 * 扫描物料
	 * @param workDocMap
	 * @return
	 */
	@Transactional
	public Map checkItemCodeCommit(Map workDocMap);
	/**
	 * 明细拣选确认
	 * @param workDocMap
	 * @return
	 */
	@Transactional
	public Map pickNumCommit(Map workDocMap);
	/**
	 * 显示作业单整单信息
	 * @param workDocMap
	 * @return
	 */
	@Transactional
	public Map showWorkDocInfos(Map workDocMap);
	/**
	 * 作业单整单确认
	 * @param workDocMap
	 * @return
	 */
	@Transactional
	public Map workDocCommit(Map workDocMap);
	
	/**
	 * 是否确认整单确认
	 */
	@Transactional
	public Map isPickAllCommit(Map workDocMap) throws RfBusinessException;
	
	/**
	 * 明细拣货时返回上一页面
	 */
	@Transactional
	public Map returnBackCommit(Map workDocMap) throws RfBusinessException;
	
	/**
	 * 拣货确认
	 */
	@Transactional
	public Map inputPickNumberCommit(Map workDocMap);
}