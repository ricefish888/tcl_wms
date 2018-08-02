package com.vtradex.wms.rfserver.service.delivery;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.rf.common.exception.RfBusinessException;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.entity.item.WmsItem;

/** 
* @ClassName: Tcl交接单处理类
* @Description: TclRfDeliveryManager 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-26 下午2:06:46  
*/
@SuppressWarnings("rawtypes")
public interface TclRfDeliveryManager extends BaseManager{
	/**显示收货人信息*/
	@Transactional
	public Map deliveryInfo(Map deliveryMap) throws RfBusinessException;
	
	/**显示BOL信息*/
	@Transactional
	public Map bolInfo(Map deliveryMap) throws RfBusinessException;
	
	/**显示拣货作业单信息*/
	@Transactional
	public Map pickInfo(Map deliveryMap) throws RfBusinessException;
	
	/**创建BOL*/
	@Transactional
	public Map createBol(Map deliveryMap) throws RfBusinessException ;
	
	/**显示Bol可添加明细*/
	@Transactional
	public Map showDetail(Map deliveryMap) throws RfBusinessException ;
	
	/**删除BOL明细*/
	@Transactional
	public Map delDetail(Map deliveryMap) throws RfBusinessException ;
	
	/**删除BOL后明细*/
	@Transactional
	public Map delInfo(Map deliveryMap) throws RfBusinessException ;
	
	/**删除确认*/
	@Transactional
	public Map delConfirm(Map deliveryMap) throws RfBusinessException ;
	
	/**Bol添加明细*/
	@Transactional
	public Map addDetail(Map deliveryMap) throws RfBusinessException ;
	
	/**选择作业单*/
	@Transactional
	public Map showDetails(Map deliveryMap) throws RfBusinessException ;
	
	/**物料条码验证*/
	@Transactional
	public Map checkCode(Map deliveryMap) throws RfBusinessException ;

	/**整单出库单扫描*/
	@Transactional
	public Map deliveryCodeInputCommit(Map deliveryMap) throws RfBusinessException ;
	
	/**整单出库确认*/
	@Transactional
	public Map deliveryOutCommit(Map deliveryMap) throws RfBusinessException ;
	
	/**整单-明细出库取消*/
	@Transactional
	public Map cancelOut(Map deliveryMap) throws RfBusinessException ;
	
	/**显示整单出库信息*/
	@Transactional
	public Map showdeliveryInfo(Map deliveryMap) throws RfBusinessException ;

	/**明细出库单扫描*/
	@Transactional
	public Map detailCodeInputCommit(Map deliveryMap) throws RfBusinessException ;
	
	/**明细出库单物料扫描*/
	@Transactional
	public Map itemCodeInputCommit(Map deliveryMap) throws RfBusinessException ;
	
	/**明细出库单信息*/
	@Transactional
	public Map showdetailInfo(Map deliveryMap) throws RfBusinessException ;
	
	/**明细出库确认*/
	@Transactional
	public Map detailOutCommit(Map deliveryMap) throws RfBusinessException ;
	
	/**配送单出库页面跳转*/
	@Transactional
	public Map workDocCommitBt(Map deliveryMap) throws RfBusinessException;
	/**显示拣货作业单信息*/
	@Transactional
	public Map workDocInfo(Map deliveryMap) throws RfBusinessException;
	
	/**添加明细*/
	@Transactional
	public Map addBolDetails(Map deliveryMap) throws RfBusinessException;
	
	/**
	 * 生成条码补打记录
	 * @param item
	 * @param lotNo
	 * @param barCode
	 * @param deatilId
	 */
	@Transactional
	public void genBarCode(WmsItem item,String lotNo,String barCode,String detailId );
	/**
	 * 扫描物料
	 * @param deliveryMap
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map confirmItemCode(Map deliveryMap) throws RfBusinessException;
	/**
	 * 显示BOLDetail信息
	 * @param deliveryMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map showBolDetailInfo(Map deliveryMap) throws RfBusinessException;
	/**
	 * 校验物料是否存在
	 * @param deliveryMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map queryItem(Map deliveryMap) throws RfBusinessException;
	
	/**
	 * 显示物料属性信息
	 */
	@Transactional
	public Map showWmsItemInfo(Map deliveryMap) throws RfBusinessException;
	/**
	 * 删除配送单
	 * @param deliveryMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map deleteWorkDoc(Map deliveryMap) throws RfBusinessException;
	/**
	 * 交接单生效后交接管理才看到
	 * @param deliveryMap
	 * @return
	 * @throws RfBusinessException
	 */
	@Transactional
	public Map activeDetailBt(Map deliveryMap) throws RfBusinessException;
}
