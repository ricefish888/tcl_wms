package com.vtradex.wms.rfserver.service.order;

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
public interface TclRfOrderManager extends BaseManager{
	
	/**
	 * 扫描订单/线体号提交
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map orderCodeInputCommit(Map orderMap) throws RfBusinessException;
	
	/**
	 * 显示订单信息
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map orderInfo(Map orderMap) throws RfBusinessException;
	
	/**
	 * 扫描货品条码信息
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map orderItemCommit(Map orderMap) throws RfBusinessException;
	
	/**
	 * 扫描生产日期信息
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map beginDateCommit(Map orderMap) throws RfBusinessException;
	
	/**
	 * 扫描货品条码信息
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map productLineItemCommit(Map orderMap) throws RfBusinessException;
	
	/**生产工单退料入库信息*/
	@Transactional
	public Map itemBackInfo(Map orderMap) throws RfBusinessException;
	
	/**工单信息*/
	@Transactional
	public Map poInfo(Map orderMap) throws RfBusinessException;
	
	/**退料扫描确认*/
	@Transactional
	public Map confirm(Map orderMap) throws RfBusinessException;
	
	/**退料扫描修改*/
	@Transactional
	public Map editRecord(Map orderMap) throws RfBusinessException;
	
	/**退料信息展示*/
	@Transactional
	public Map showPos(Map orderMap) throws RfBusinessException;
	
	/**退料生成ASN*/
	@Transactional
	public Map genASN(Map orderMap) throws RfBusinessException;
}
