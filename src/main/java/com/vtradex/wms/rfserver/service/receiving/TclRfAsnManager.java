package com.vtradex.wms.rfserver.service.receiving;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.rf.common.exception.RfBusinessException;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;

/**
 * ASN操作入口处理类
 * @Description:
 * @Author:        <a href="yequan.song@vtradex.net">宋叶全</a>
 * @CreateDate:    2016年1月22日
 * @version:       v1.0
 */
public interface TclRfAsnManager extends BaseManager{
	/**
	 * 扫描asn单号提交
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map asnCodeInputCommit(Map asnMap) throws RfBusinessException ;
	
	/**
	 * 显示asn信息
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map asnInfo(Map asnMap) throws RfBusinessException ;
	
	/**
	 * 扫描货品条码信息
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map asnItemCommit(Map asnMap) throws RfBusinessException ;
	
	
	/**
	 * RF 收货
	 * @Description:
	 * @Author:        <a href="yequan.song@vtradex.net">宋叶全</a>
	 * @CreateDate:    2016年1月26日
	 * @param asnMap
	 * @return:
	 * @arithMetic:
	 * @exception:
	 */
	public Map asnReceiveSingle(Map asnMap) throws RfBusinessException;
	
	
	/**
	 * 货品切换
	 * @Description:
	 * @Author:        <a href="yequan.song@vtradex.net">宋叶全</a>
	 * @CreateDate:    2016年1月26日
	 * @param asnMap
	 * @return:
	 * @arithMetic:
	 * @exception:
	 */
	public Map asnItemSwitch(Map asnMap) throws RfBusinessException;
	
	
	public Map skuTaskItemCommit(Map skuTaskItemMap) throws RfBusinessException;
	
	/**
	 * 显示ASN信息
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map showAsnInfo(Map asnMap) throws RfBusinessException;
	
	/**
	 * 整单质检
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map asnQualityAll(Map asnMap) throws RfBusinessException;
	
	/**
	 * 创建上架单
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map createAsnWorkDoc(Map asnMap) throws RfBusinessException;
	
	/**
	 * 取消收货--校验收货单号和物料条码
	 */
	@Transactional
	public Map checkInfo(Map asnMap) throws RfBusinessException;
	
	/**
	 * 显示收货明细信息
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map showASNDetailInfo(Map asnMap) throws RfBusinessException;
	
	/**
	 * 确认取消收货
	 */
	@Transactional
	public Map cancelReceiveConfirm(Map asnMap) throws RfBusinessException;
	
	/**
	 * 交货关闭,把未收货的数量退回给交货单
	 */
	@Transactional
	public Map asnCloseDeliverOrder(Map asnMap) throws RfBusinessException;
	
	/**
	 * 交货关闭提示
	 */
	public Map genConfirmMessage(Map asnMap) throws RfBusinessException;
	
	/**
	 * 扫描收货单号
	 */
	@Transactional
	public Map asnCodeInput(Map asnMap) throws RfBusinessException;
	
	/**
	 * 显示待质检信息
	 */
	@Transactional
	public Map showQualityInfos(Map asnMap) throws RfBusinessException;
	
	/**
	 * 单一质检确认
	 */
	@Transactional
	public Map asnDetailQuality(Map asnMap) throws RfBusinessException;
	
	/**默认收货数量*/
	@Transactional
	public Map receiveQty(Map asnMap) throws RfBusinessException;
	
	/**
	 * 校验物料条码
	 * VMI仓库下不能扫描编码必须扫描条码
	 * @param barCode
	 */
	@Transactional
	public void checkBarCode(String barCode);
	/**
	 * 显示上架作业单信息
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map showWorkDocInfo(Map asnMap) throws RfBusinessException;
	
	/**
	 * 整单上架确认
	 */
	@Transactional
	public Map putawayAllCommit(Map asnMap) throws RfBusinessException;
	
	/**
	 * 是否确认上架
	 */
	@Transactional
	public Map bePutawayAllCommit(Map asnMap) throws RfBusinessException;
}
