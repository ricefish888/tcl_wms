package com.vtradex.wms.rfserver.service.bol;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.rf.common.exception.RfBusinessException;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;

 
/**
 * 订单RF逻辑类
 * @author admin
 *
 */
public interface TclRfBolManager extends BaseManager{
	
	/**
	 * 扫描交接单号
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map bolCodeInputCommit(Map bolMap) throws RfBusinessException;
	
	/**
	 * 显示bol信息
	 * @param bolMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map bolInfo(Map bolMap) throws RfBusinessException;
	
	/**
	 * 发运确认
	 * @param bolMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map bolShipCommit(Map bolMap) throws RfBusinessException;
	
	/**
	 * 取消
	 * @param bolMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map cancelShip(Map bolMap) throws RfBusinessException;
	
	/**
	 * 创建出库单 
	 */
	@Transactional
	public Map createBol(Map bolMap) throws RfBusinessException;
	
	/**
	 * 添加作业单
	 * @param bolMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map addWorkDoc(Map bolMap) throws RfBusinessException;
	
	/**
	 * 显示作业单信息
	 */
	@Transactional
	public Map showWorkDocInfo(Map bolMap) throws RfBusinessException;
	
	/**
	 * 作业单确认 加入到出库单
	 * @param bolMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map addWorkDocConfirm(Map bolMap) throws RfBusinessException;
	
	/**
	 * 根据作业单创建配送单
	 * @param bolMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map createQuickShipping(Map bolMap) throws RfBusinessException;
	
	/**
	 * 创建配送单
	 */
	@Transactional
	public Map createWmsWorkDoc(Map bolMap) throws RfBusinessException;
	
	/**
	 * 显示配送单信息
	 * @param bolMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map showquickWorkDocInfo(Map bolMap) throws RfBusinessException;
	
	/**
	 * 添加配送单
	 */
	@Transactional
	public Map addQuickWorkDoc(Map quickWorkDocMap) throws RfBusinessException;
	/**
	 * VMI添加线边仓作业单
	 */
	@Transactional
	public Map addXBCWorkDoc(Map quickWorkDocMap) throws RfBusinessException;
	
	/**
	 * 根据交接单创建配送单
	 * @param bolMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map createQuickShippingByBol(Map bolMap) throws RfBusinessException;
	
	/**
	 * 配送单发运
	 */
	@Transactional
	public Map quickWorkDocShipCommit(Map bolMap) throws RfBusinessException;
	
	/**
	 * VMI创建配送单
	 * @param quickWorkDocMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map genWmsWorkDoc(Map quickWorkDocMap) throws RfBusinessException;
	/**
	 * 创建JIT上线结算出库单
	 * @param quickWorkDocMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map createJITWorkDoc(Map quickWorkDocMap) throws RfBusinessException;
	
	/**
	 * 显示工单信息
	 * @param quickWorkDocMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map showPodInfo(Map quickWorkDocMap) throws RfBusinessException;
	/**
	 * 校验工单
	 * @param quickWorkDocMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map checkPo(Map quickWorkDocMap) throws RfBusinessException;
	/**
	 * JIT上线结算添加明细
	 * @param quickWorkDocMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map addPodDetail(Map quickWorkDocMap) throws RfBusinessException;
	
	/**
	 * JIT上线结算删除明细
	 */
	@Transactional
	public Map deleteDetail(Map quickWorkDocMap) throws RfBusinessException;
	/**
	 * JIT上线结算删除明细
	 */
	@Transactional
	public Map delDetail(Map quickWorkDocMap) throws RfBusinessException;
	/**
	 * 显示要删除的JIT出库单明细的信息
	 * @param quickWorkDocMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map showTaskInfo(Map quickWorkDocMap) throws RfBusinessException;
	
	/**
	 * JIT出库
	 */
	@Transactional
	public Map shipConfirm(Map quickWorkDocMap) throws RfBusinessException;
	
	/**
	 * JIT出库页面跳转
	 */
	public Map forwardPage(Map quickWorkDocMap) throws RfBusinessException;
	
	/**
	 * 显示出库作业单信息
	 */
	public Map showBolWorkDocInfo(Map bolMap) throws RfBusinessException;
	
}
