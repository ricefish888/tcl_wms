package com.vtradex.wms.rfserver.service.receiving.pojo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils; 
import org.springframework.transaction.annotation.Transactional;

import com.vtradex.rf.common.RfConstant;
import com.vtradex.rf.common.exception.RfBusinessException; 
import com.vtradex.rf.util.GsonUtil;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.DateUtil;
import com.vtradex.thorn.server.util.LocalizedMessage; 
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.thorn.server.web.security.UserHolder; 
import com.vtradex.wms.rfserver.bean.RfAsnVo;
import com.vtradex.wms.rfserver.bean.RfSkuTaskVo;
import com.vtradex.wms.rfserver.common.RfConstantDiy;
import com.vtradex.wms.rfserver.common.RfMessageCode;
import com.vtradex.wms.rfserver.service.receiving.TclRfAsnManager;
import com.vtradex.wms.rfserver.service.receiving.RfPalletPutawayWorkManager;
import com.vtradex.wms.rfserver.service.receiving.RfSKUPutawayWorkManager;
import com.vtradex.wms.server.helper.WmsBarCodeParse;
import com.vtradex.wms.server.model.component.LotInfo;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.model.entity.base.WmsWorker;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.item.WmsBillType;
import com.vtradex.wms.server.model.entity.item.WmsCompany;
import com.vtradex.wms.server.model.entity.item.WmsInventoryState;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemScanCode;
import com.vtradex.wms.server.model.entity.item.WmsLotRule;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.order.WmsTransportOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrder;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderDetail;
import com.vtradex.wms.server.model.entity.receiving.WmsASN;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.entity.receiving.WmsASNStatus;
import com.vtradex.wms.server.model.entity.receiving.WmsReceivedRecord;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;
import com.vtradex.wms.server.model.entity.workdoc.WmsWorkDoc;
import com.vtradex.wms.server.model.enums.BaseStatus;
import com.vtradex.wms.server.model.enums.WmsTaskStatus;
import com.vtradex.wms.server.model.enums.WmsWorkDocStatus;
import com.vtradex.wms.server.model.enums.WmsWorkDocType;
import com.vtradex.wms.server.service.inventory.WmsInventoryManageManager;
import com.vtradex.wms.server.service.receiving.WmsASNDetailManager;
import com.vtradex.wms.server.service.receiving.WmsASNManager;
import com.vtradex.wms.server.service.receiving.WmsNoTransactionalManager;
import com.vtradex.wms.server.service.receiving.WmsTclASNManager;
import com.vtradex.wms.server.service.receiving.WmsTclNoTransactionalManager;
import com.vtradex.wms.server.service.receiving.pojo.DefaultWmsTclASNManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.workdoc.WmsWorkDocManager;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.StringHelper;

/**
 * ASN操作入口处理类
 * @Description:
 * @Author:        <a href="yequan.song@vtradex.net">宋叶全</a>
 * @CreateDate:    2016年1月22日
 * @version:       v1.0
 */
public class DefaultTclRfAsnManager extends DefaultBaseManager implements TclRfAsnManager{
	
	protected WmsTclASNManager wmsTclASNManager;
	 
	public DefaultTclRfAsnManager(WmsTclASNManager wmsTclASNManager) {
		this.wmsTclASNManager = wmsTclASNManager; 
	}
	 
	/**
	 * 获取物料
	 * @param code
	 * @return
	 */
	private WmsItem getWmsItem(String code){
		WmsItem item = (WmsItem)commonDao.findByQueryUniqueResult("FROM WmsItem item where item.code=:ic", "ic", code);
		
		return item;
	}
	/**
	 * 扫描asn单号提交
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map asnCodeInputCommit(Map asnMap) throws RfBusinessException {
		String asnCode = (String) asnMap.get("asnCodeInput");
		if(StringUtils.isEmpty(asnCode)){
			throw new RfBusinessException("收货单号不能为空");
		}
		String hql = "From WmsASN asn where asn.warehouse.baseOrganization.id=:baseOrganizationId and asn.code=:code ";
		String hql2 = " AND asn.keeper.id =:keeperId ";
		String hql3 = " AND asn.keeper IS NULL ";
		WmsASN asn = (WmsASN)commonDao.findByQueryUniqueResult(hql+hql2, new String[]{"baseOrganizationId","code","keeperId"},new Object[]{BaseOrganizationHolder.getThornBaseOrganization().getId(), asnCode,UserHolder.getUser().getId()});
		if(asn==null){
			asn = (WmsASN)commonDao.findByQueryUniqueResult(hql+hql3, new String[]{"baseOrganizationId","code"},new Object[]{BaseOrganizationHolder.getThornBaseOrganization().getId(), asnCode});
			if(asn == null){
				asnMap.put("asnCodeInput", "");
				throw new RfBusinessException("找不到相应的收货单或保管员不是"+UserHolder.getUser().getLoginName(),asnMap);
			}
		}
		if(!(WmsASNStatus.ACTIVE.equals(asn.getStatus())||WmsASNStatus.RECEIVING.equals(asn.getStatus()))){
			asnMap.put("asnCodeInput", "");
			throw new RfBusinessException("收货单状态不为生效和部分收货状态，不允许收货",asnMap);
		}
		WmsBillType billType = commonDao.load( WmsBillType.class,asn.getBillType().getId());
		WmsSupplier supplier = commonDao.load( WmsSupplier.class,asn.getSupplier().getId());
		String userField5 = asn.getUserField5();
		asnMap.put(RfConstant.ENTITY_ID, asn.getId());
		asnMap.put("asnId", asn.getId());
		asnMap.put("asnCode", asn.getCode());
		asnMap.put("asnTypeName", billType.getName());
		asnMap.put("recvQuantity", asn.getReceiveQty());
		asnMap.put("totalQuantity", asn.getQty());
		asnMap.put("supplierCode", supplier.getCode());
		asnMap.put("supplierName", supplier.getName());
		asnMap.put("userField5", "0".equals(userField5)?"标准":"寄售");
		asnMap.put(RfConstant.FORWARD_VALUE, "success");
		return asnMap;
	}

	/**
	 * 显示asn信息
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map asnInfo(Map asnMap) throws RfBusinessException {
		Long asnId = Long.valueOf(asnMap.get("asnId").toString());
		Long detailId = Long.valueOf(asnMap.get("id").toString());
		WmsASNDetail asnDetail = commonDao.load(WmsASNDetail.class, detailId);
		if(asnDetail.getTransportOrderDetail() != null){
			WmsTransportOrderDetail traDetail = commonDao.load(WmsTransportOrderDetail.class, asnDetail.getTransportOrderDetail().getId());
			WmsDeliveryOrderDetail delDetail = commonDao.load(WmsDeliveryOrderDetail.class, traDetail.getDeliveryOrderDetail().getId());
			WmsDeliveryOrder delOrder = commonDao.load(WmsDeliveryOrder.class, delDetail.getDeliveryOrder().getId());
			if(delOrder.getDeliveryDate() !=null){
				asnMap.put("deliveryDate",DateUtil.format(delOrder.getDeliveryDate(), "yyyy-MM-dd"));//交货日期
			}else{
				asnMap.put("deliveryDate","");//交货日期
			}
			asnMap.put("deliveryOrderCode", delOrder.getCode());//交货单号
		}
		Double expectedQty = asnDetail.getExpectedQty();
		WmsItem item = commonDao.load(WmsItem.class, asnDetail.getItem().getId());
		double unReceiveQty = DoubleUtils.sub(expectedQty, asnDetail.getReceivedQty());
		//因为可能带小数点所以用BigDecimal divideAndRemainder方法获得商和余数
		Double minPakQty = Double.valueOf(asnDetail.getLotInfo().getExtendPropC12());
		BigDecimal[] results =  (BigDecimal.valueOf(unReceiveQty)).divideAndRemainder(BigDecimal.valueOf(minPakQty));
		asnMap.put("asnDetailId",asnDetail.getId());
		asnMap.put("itemCode", item.getCode());
		asnMap.put("itemName", item.getName());
		asnMap.put("lot", asnDetail.getLotInfo().getLot());
		asnMap.put("itemId",item.getId()); 
		asnMap.put("expectedQty", expectedQty);
		asnMap.put("unReceiveQty", unReceiveQty);
		asnMap.put("boxQty", results[0]); 
		asnMap.put("qty",  results[1]); 
		asnMap.put("receiveQuantity", unReceiveQty);
		asnMap.put(RfConstant.FORWARD_VALUE, "success");
		WmsASN asn = commonDao.load(WmsASN.class,asnId);
		asnMap.put("recvQuantity", asn.getReceiveQty());
		asnMap.put("receiveQuantity", asnDetail.getExpectedQty()-asnDetail.getReceivedQty());
		asnMap.put("totalQuantity", asn.getQty());
		
		return asnMap;
	}
	
	/**
	 * 扫描货品条码信息
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map asnItemCommit(Map asnMap) throws RfBusinessException {
		Long asnId = Long.valueOf(asnMap.get("asnId").toString());
		String barCode = (String) asnMap.get("asnItemInput");
		if(StringUtils.isEmpty(barCode)){
			throw new RfBusinessException("条码不能为空");
		}
		this.checkBarCode(barCode);
		if(!WmsBarCodeParse.isBarCode(barCode)){
			asnMap.put("asnItemInput", "");
			throw new RfBusinessException("条码格式不正确",asnMap);
		}
		Map map = WmsBarCodeParse.parse(barCode);
		String itemCode = (String)map.get(WmsBarCodeParse.KEY_ITEMCODE);
		String lot = (String) map.get(WmsBarCodeParse.KEY_LOTNO);
		//查询asn明细
		String hql = "select sum(detail.expectedQty),sum(detail.receivedQty),"
				+ "max(cast(detail.lotInfo.extendPropC12,double)),max(detail.id) "
				+ "From WmsASNDetail detail "
				+ "where detail.item.code=:itemCode "
				+ "and detail.asn.id=:asnId AND detail.lotInfo.lot=:lot";
		Object[] detailObj = (Object[])commonDao.findByQueryUniqueResult(hql,
				new String[]{"itemCode","asnId","lot"},
				new Object[]{itemCode,asnId,lot});
		if(detailObj==null){
			asnMap.put("asnItemInput", "");
			throw new RfBusinessException("找不到相应的明细或者该货品已经收货完成",asnMap);
		}
		Double expectedQty =Double.valueOf(detailObj[0].toString());
		if(expectedQty<=0){
			asnMap.put("asnItemInput", "");
			throw new RfBusinessException("找不到相应的明细",asnMap);
		}
		Double receivedQty =Double.valueOf(detailObj[1].toString());
		if(expectedQty<=receivedQty){
			asnMap.put("asnItemInput", "");
			throw new RfBusinessException("该货品已经收货完成",asnMap);
		}
		String minPakQtyStr=detailObj[2]==null?"":detailObj[2].toString();
		if(StringUtils.isEmpty(minPakQtyStr)){
			asnMap.put("asnItemInput", "");
			throw new RfBusinessException("最小包装量为空",asnMap);
		}
		Double minPakQty=0d;
		try{
			minPakQty= Double.valueOf(minPakQtyStr);
		}catch(NumberFormatException e){
			asnMap.put("asnItemInput", "");
			throw new RfBusinessException("最小包装量"+minPakQtyStr+"不是数字",asnMap);
		}
		if(minPakQty<=0){
			asnMap.put("asnItemInput", "");
			throw new RfBusinessException("最小包装量"+minPakQtyStr+"不是大于0的数字",asnMap);
		}
		WmsASNDetail asnDetail = commonDao.load(WmsASNDetail.class, Long.valueOf(detailObj[3].toString()));
		if(asnDetail.getTransportOrderDetail() != null){
			WmsTransportOrderDetail traDetail = commonDao.load(WmsTransportOrderDetail.class, asnDetail.getTransportOrderDetail().getId());
			WmsDeliveryOrderDetail delDetail = commonDao.load(WmsDeliveryOrderDetail.class, traDetail.getDeliveryOrderDetail().getId());
			WmsDeliveryOrder delOrder = commonDao.load(WmsDeliveryOrder.class, delDetail.getDeliveryOrder().getId());
			if(delOrder.getDeliveryDate() !=null){
				asnMap.put("deliveryDate",DateUtil.format(delOrder.getDeliveryDate(), "yyyy-MM-dd"));//交货日期
			}else{
				asnMap.put("deliveryDate","");//交货日期
			}
			asnMap.put("deliveryOrderCode", delOrder.getCode());//交货单号
		}
		expectedQty = asnDetail.getExpectedQty();
//		WmsItem item = commonDao.load(WmsItem.class, asnDetail.getItem().getId());
		double unReceiveQty = DoubleUtils.sub(expectedQty, receivedQty);
		//因为可能带小数点所以用BigDecimal divideAndRemainder方法获得商和余数
		BigDecimal[] results =  (BigDecimal.valueOf(unReceiveQty)).divideAndRemainder(BigDecimal.valueOf(minPakQty));
//		asnMap.put("asnDetailId",asnDetail.getId());
		asnMap.put("itemCode", itemCode);
//		asnMap.put("itemName", item.getName());
		asnMap.put("lot", lot);
//		asnMap.put("itemId",item.getId()); 
//		asnMap.put("expectedQty", expectedQty);
//		asnMap.put("unReceiveQty", unReceiveQty);
		asnMap.put("boxQty", results[0]); 
//		asnMap.put("qty",  results[1]); 
//		asnMap.put("receiveQuantity", unReceiveQty);
		asnMap.put(RfConstant.FORWARD_VALUE, "success");
		return asnMap;
	}
	
	public static void main(String[] args){
		Double unReceiveQty = 1d;
		Double minPakQty = 3.2d;
		Integer boxQty = (int) (unReceiveQty/minPakQty);
		Double qty1 =unReceiveQty%minPakQty;
		  BigDecimal[] results =  (BigDecimal.valueOf(unReceiveQty)).divideAndRemainder(BigDecimal.valueOf(minPakQty));
		System.out.println("----------------箱"+boxQty);
		System.out.println("----------------f个"+results[0]);
		System.out.println("----------------个"+results[1]);
	}
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
	public Map asnReceiveSingle(Map asnMap) throws RfBusinessException{
//		String itemCode =  asnMap.get("itemCode").toString();
		Long asnId = Long.valueOf(asnMap.get("asnId").toString());
		Long detailId = asnMap.get("asnDetailId") == null ? null : Long.valueOf(asnMap.get("asnDetailId").toString());
		if(null == detailId){
			detailId = asnMap.get(RfConstant.ENTITY_ID) == null ? null : Long.valueOf(asnMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(detailId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsASNDetail detail = commonDao.load(WmsASNDetail.class, detailId);
		//收货数量
		double recievQty = asnMap.get("receiveQuantity") == null?0D:Double.valueOf(asnMap.get("receiveQuantity").toString());
		Double unReceiveQty = asnMap.get("unReceiveQty") == null?null:Double.valueOf(asnMap.get("unReceiveQty").toString());
		if(unReceiveQty ==null){
			unReceiveQty = DoubleUtils.sub(detail.getExpectedQty(),detail.getReceivedQty());
		}
		if(recievQty<=0){
			throw new RfBusinessException("收货数量不是大于0的数字");
		}
		if(recievQty>unReceiveQty){
			throw new RfBusinessException("收货数量不能大于可收数量");
		}
//		WmsASN asn = commonDao.load(WmsASN.class,asnId);
//		WmsCompany company = commonDao.load(WmsCompany.class,asn.getCompany().getId());
//		String hql = "From WmsInventoryState itemState where itemState.masterGroup=:masterGroup and itemState.name=:name";
//		
////		WmsInventoryState itemState = (WmsInventoryState)commonDao.findByQueryUniqueResult(hql, new String[]{"masterGroup","name"}, new Object[]{company.getMasterGroup(),"待检"})	;
//		if(itemState==null){
//			throw new RfBusinessException("库存状态管理中找不到待检状态");
//		}
		WmsASN asn = commonDao.load(WmsASN.class, asnId);
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class, asn.getWarehouse().getId());
		String itemHql = "select k.keeper.id from WmsItemKeeper k where k.factory.code =:fcode and k.warehouse.id =:warehouseId and k.item.code=:itemCode ";
		WmsItem item = commonDao.load(WmsItem.class, detail.getItem().getId());
		List<Long> keepers = commonDao.findByQuery(itemHql, new String[]{"fcode","warehouseId","itemCode"}, new Object[]{asn.getUserField7(),asn.getWarehouse().getId(),item.getCode()});
		if(keepers.isEmpty()){
			throw new RfBusinessException("在仓库"+warehouse.getCode()+"和工厂"+asn.getUserField7()+"下未维护物料"+item.getCode()+"的保管员");
		}
		if(keepers.size()>1){
			throw new RfBusinessException("在仓库"+warehouse.getCode()+"和工厂"+asn.getUserField7()+"下物料"+item.getCode()+"维护了多个保管员");
		}
		if(!keepers.get(0).equals(UserHolder.getUser().getId())){
			throw new RfBusinessException("在仓库"+warehouse.getCode()+"和工厂"+asn.getUserField7()+"下物料"+item.getCode()+"的保管员不是当前用户");
		}
	    String hql = "select max(work.id) From WmsWorker work where work.status=:status and work.user.id=:userId  ";
		Long workId = (Long)commonDao.findByQueryUniqueResult(hql, new String[]{"status","userId"}, new Object[]{BaseStatus.ENABLED,UserHolder.getUser().getId()});
		wmsTclASNManager.rfReceiving(asnId, item.getCode(), recievQty, workId==null?0l:workId.longValue(),detailId);
		String asnHql = "SELECT asn.id "
				+ " FROM WmsASN asn "
				+ " LEFT JOIN asn.billType "
				+ " LEFT JOIN asn.receiveLocation "
				+ " LEFT JOIN asn.receiveDock "
				+ " LEFT JOIN asn.company WHERE asn.id =:asnId "
				+ " AND asn.qty > asn.receiveQty AND asn.status IN('ACTIVE','RECEIVING')";
		List<Long> asnList = commonDao.findByQuery(asnHql, new String[]{"asnId"}, new Object[]{asnId});
		
		if (null == asnList || asnList.size() <=0) { 
			asnMap.put(RfConstant.FORWARD_VALUE, "success_all_received");
			asnMap.put(RfConstant.CLEAR_VALUE, "true");
		}else{
			asnMap.put(RfConstant.FORWARD_VALUE, "success_part_received"); 
			asnMap.put("asnId", asnId);
			asnMap.put("asnItemInput","");
			asnMap.put("receiveQuantity",""); 
    		asnMap.put(RfConstant.ENTITY_ID, asnId);
		}
		return asnMap;
	}

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
	public Map asnItemSwitch(Map asnMap) throws RfBusinessException{
		// TODO Auto-generated method stub 
		
		Long asnId = asnMap.get("asnId") == null?null:Long.valueOf(asnMap.get("asnId").toString());
		 
		if (null == asnId || StringUtils.isEmpty(asnId.toString())) {
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		 
		String asnHql = "SELECT new com.vtradex.wms.rfserver.bean.RfAsnVo(asn.id, asn.code, asn.billType.name, asn.customerBill, "
				+ " asn.receiveQty, asn.qty, asn.details.size, asn.receiveLocation.code, asn.receiveDock.name,asn.orderDate) "
				+ " FROM WmsASN asn "
				+ " LEFT JOIN asn.billType "
				+ " LEFT JOIN asn.receiveLocation "
				+ " LEFT JOIN asn.receiveDock "
				+ " LEFT JOIN asn.company WHERE asn.id =:asnId "
				+ " AND asn.qty > asn.receiveQty AND asn.status IN('ACTIVE','RECEIVING')";
		List<RfAsnVo> asnList = commonDao.findByQuery(asnHql, new String[]{"asnId"}, new Object[]{asnId});
		if (null == asnList || asnList.size() <=0) {
			asnMap.put(RfConstant.FORWARD_VALUE, "success_all_received");
			asnMap.put(RfConstant.CLEAR_VALUE, "true");
		}else{
			//清空商品条码
			asnMap.put("asnItemInput", "");
			asnMap.put(RfConstant.FORWARD_VALUE, "success_part_received"); 
		}
		return asnMap;
	}
	/**
	 * 校验物料条码
	 * VMI仓库下不能扫描编码必须扫描条码
	 * @param barCode
	 */
	public void checkBarCode(String barCode){
//		String hql ="FROM WmsWarehouse w WHERE w.baseOrganization.id =:baseOrganizationId ";
//		WmsWarehouse warehouse = (WmsWarehouse) commonDao.findByQueryUniqueResult(hql, 
//				"baseOrganizationId", BaseOrganizationHolder.getThornBaseOrganization().getId());
//		if("VMI".equals(warehouse.getCode())){
//			if(!WmsBarCodeParse.isBarCode(barCode)){
//				throw new RfBusinessException("VMI仓库下必须扫描物料条码");
//			}
//		}
	}
	/*SKU作业货品扫描*/
	public Map skuTaskItemCommit(Map skuTaskItemMap) throws RfBusinessException{
		//System.out.println("skuTaskItemCommit()="+skuTaskItemMap.toString());
		Map<String, Object> result = new HashMap<String, Object>();
		/*校验参数*/
		//Long id = skuTaskItemMap.get(RfConstant.ENTITY_ID) == null?null:Long.valueOf(skuTaskItemMap.get(RfConstant.ENTITY_ID).toString());
		String barCode = skuTaskItemMap.get("itemCodeInput") == null?null:skuTaskItemMap.get("itemCodeInput").toString();
		if(StringUtils.isEmpty(barCode)){
			throw new RfBusinessException("条码不能为空");
		}
		String itemCode = "";
		this.checkBarCode(barCode);
		if(WmsBarCodeParse.isBarCode(barCode)){
			Map map = WmsBarCodeParse.parse(barCode);
			itemCode = (String)map.get(WmsBarCodeParse.KEY_ITEMCODE);
		}else{
			itemCode = barCode;
			WmsItem item = this.getWmsItem(itemCode);
			if(item==null){
				skuTaskItemMap.put("itemCodeInput", "");
				throw new RfBusinessException("物料"+itemCode+"在WMS系统中不存在",skuTaskItemMap);
			}
			if(WmsItemScanCode.SCANCODE_NO.equals(item.getUserFieldV10())){
				skuTaskItemMap.put("itemCodeInput", "");
				throw new RfBusinessException("请扫描条码",skuTaskItemMap);
			}
		}
		Long workDocId =  0l;
		workDocId=skuTaskItemMap.get("workDocId") == null?null:Long.valueOf(skuTaskItemMap.get("workDocId").toString());
		if (null == workDocId || StringUtils.isEmpty(workDocId.toString())) {
			workDocId = skuTaskItemMap.get(RfConstant.ENTITY_ID) == null?null:Long.valueOf(skuTaskItemMap.get(RfConstant.ENTITY_ID).toString());
			if(null == workDocId || StringUtils.isEmpty(workDocId.toString())){
			   throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
			} 
		}
		if (null == itemCode || StringUtils.isEmpty(itemCode)) {
			skuTaskItemMap.put("itemCodeInput", "");
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE,skuTaskItemMap);
		}
		/*
		 * 1.按照上架动线号升序获取上架任务
		 * 2.按照产品默认规则，货品代码输入框仍然支持代码，条码1和条码2字段
		 * 3.如果扫描到的与推荐的货品不一致，且当前上架单存在上架任务的话，则没有提示，继续下一个界面；如果扫描到的与推荐的货品不一致，且当前上架单不存在上架任务的话，则提示“当前货品没有上架任务，请重新扫描！”
		 */
		/*获取推荐的货品*/
		List<RfSkuTaskVo> skuTaskList = this.getSKUTaskItemList(workDocId,"");
		/*创建持久map*/
		Map<String, Object> perResult = new HashMap<String, Object>();
		perResult.putAll(skuTaskItemMap);
		if (skuTaskList.size() > 0) {
			RfSkuTaskVo skuTaskVo = skuTaskList.get(0);
			if (!skuTaskVo.getItemCode().equals(itemCode)) {
				List<RfSkuTaskVo> skuTaskInputList = this.getSKUTaskItemList(workDocId, "  and i.code = '"+itemCode+"' ");
				if (null != skuTaskInputList && skuTaskInputList.size() >= 1) {
					perResult.putAll(GsonUtil.toJsonMap((RfSkuTaskVo)skuTaskInputList.get(0)));
					perResult.put(RfConstantDiy.BE_REFRESH, Boolean.FALSE);
				}else {
					skuTaskItemMap.put("itemCodeInput", "");
					throw new RfBusinessException(RfMessageCode.RF_MESSAGE_WORK_DOC_SKU_ITEM_NO_TASK,skuTaskItemMap);
				}
			}else {
				perResult.putAll(GsonUtil.toJsonMap((RfSkuTaskVo)skuTaskList.get(0)));
			}
		}else {
			skuTaskItemMap.put("itemCodeInput", "");
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_WORK_DOC_MAY_BE_PUT_AWAY_FINISH,skuTaskItemMap);
		}
		Double actualQty=Double.parseDouble(perResult.get("planQty").toString())-Double.parseDouble(perResult.get("putawayQty").toString());
		/*填充页面标签默认值*/
		perResult.put("actualPutatwayQty", actualQty);
		result.put(RfConstant.CLEAR_VALUE, "true");
		result.put(RfConstant.PERSISTENT_VALUE, perResult);
		/*定义重定向标记*/
		result.put(RfConstant.FORWARD_VALUE, "success");
		//System.out.println("skuTaskItemCommit().return()="+result.toString());
		return result;
	}
	
	/*根据作业单ID获取SKU作业任务列表*/
	public List<RfSkuTaskVo> getSKUTaskItemList(Long workDocId,String andHql){
		StringBuffer workDocHql = new StringBuffer("select new com.vtradex.wms.rfserver.bean.RfSkuTaskVo(");
		workDocHql.append("t.workDoc.id, ");
		workDocHql.append("t.id, ");
		workDocHql.append("t.workDoc.id, ");
		workDocHql.append("i.code, ");
		workDocHql.append("i.name, ");
		workDocHql.append("t.planQty, ");
		workDocHql.append("t.putawayQty, ");
		workDocHql.append("u.unit, ");
		workDocHql.append("t.inventoryStatus, ");
		workDocHql.append("l.code, ");
		workDocHql.append("k.lotInfo.lot, ");
		workDocHql.append("k.lotInfo.productDate )");
		workDocHql.append("from WmsTask t ");
		workDocHql.append("left join t.item i ");
		workDocHql.append("left join t.packageUnit u ");
		workDocHql.append("left join t.itemKey k ");
		workDocHql.append("left join t.toLocation l ");
		workDocHql.append("where 1=1 ");
		workDocHql.append("and t.workDoc.id = :workDocId ");
		workDocHql.append("and t.status in ('"+WmsTaskStatus.ENABLED+"','"+WmsTaskStatus.IN_OPERATION+"') ");
		workDocHql.append("order by l.putawaySequence asc ");
		if (!StringUtils.isEmpty(andHql)) {
			workDocHql.insert(workDocHql.indexOf("1=1")+3, " "+andHql+" ");
		}
		return this.commonDao.findByQuery(workDocHql.toString(), new String[]{"workDocId"}, new Object[]{workDocId});
	}
	/**
	 * 显示ASN信息
	 */
	public Map showAsnInfo(Map asnMap) throws RfBusinessException{
		Long asnId = asnMap.get("asnId") == null ? null : Long.valueOf(asnMap.get("asnId").toString());
		if(null == asnId){
			asnId = asnMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(asnMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(StringHelper.isNullOrEmpty(asnId.toString())){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsASN asn = commonDao.load(WmsASN.class,asnId);
		WmsSupplier supplier = commonDao.load(WmsSupplier.class, asn.getSupplier().getId());
		asnMap.put("asnId", asnId);
		asnMap.put("asnCode", asn.getCode());
		asnMap.put("quantity", asn.getReceiveQty());
		asnMap.put("qcQuantity", asn.getQuantityQty());
		asnMap.put("supplierName", supplier.getName());
		asnMap.put("factoryCode", asn.getUserField7());
		asnMap.put("putawayQty", asn.getPutawayQty());
		
		return asnMap;
		
	}
	/**
	 * 整单质检
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map asnQualityAll(Map asnMap) throws RfBusinessException{
		Map<String, Object> result = new HashMap<String, Object>();
		Long asnId = asnMap.get("asnId") == null ? null : Long.valueOf(asnMap.get("asnId").toString());
		if(null == asnId){
			asnId = asnMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(asnMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(StringHelper.isNullOrEmpty(asnId.toString())){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		String hql = "FROM WmsInventoryState wis WHERE wis.name =:name ";
		List<WmsInventoryState> invStates = commonDao.findByQuery(hql, "name", "合格");
		if(invStates.isEmpty()){
			throw new RfBusinessException("未维护合格的库存状态信息");
		}
		wmsTclASNManager.allQcRecord(asnId, invStates.get(0).getId(), UserHolder.getUser().getId());
		
		result.put(RfConstant.FORWARD_VALUE, "success");
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    perResult.putAll(asnMap);
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
		
	}
	/**
	 *创建上架单 
	 */
	public Map createAsnWorkDoc(Map asnMap) throws RfBusinessException{
		Map<String, Object> result = new HashMap<String, Object>();
		Long asnId = asnMap.get("asnId") == null ? null : Long.valueOf(asnMap.get("asnId").toString());
		if(null == asnId){
			asnId = asnMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(asnMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(StringHelper.isNullOrEmpty(asnId.toString())){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsASN wmsAsn = commonDao.load(WmsASN.class, asnId);
		WmsTclNoTransactionalManager wmsNoTransactionalManager = (WmsTclNoTransactionalManager) applicationContext.getBean("wmsTclNoTransactionalManager");
		wmsNoTransactionalManager.manualCreateWorkDoc(wmsAsn);
		result.put(RfConstant.FORWARD_VALUE, "success");
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    perResult.putAll(asnMap);
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	
	/**
	 * 取消收货--校验收货单号和物料条码
	 */
	public Map checkInfo(Map asnMap) throws RfBusinessException{
		String asnCode = asnMap.get("cancelReceiveCodeInput").toString();
		String barCode = asnMap.get("cancelReceiveItemInput").toString();
		if(StringHelper.isNullOrEmpty(barCode) || StringHelper.isNullOrEmpty(asnCode)){
			throw new RfBusinessException("收货单号和货品条码都不能为空");
		}
		
		String itemCode = "";
		this.checkBarCode(barCode);
		if(WmsBarCodeParse.isBarCode(barCode)){
			Map map = WmsBarCodeParse.parse(barCode);
			itemCode = (String)map.get(WmsBarCodeParse.KEY_ITEMCODE);
		}else{
			itemCode = barCode;
			WmsItem item = this.getWmsItem(itemCode);
			if(item ==null){
				asnMap.put("cancelReceiveItemInput", "");
				throw new RfBusinessException("物料"+itemCode+"在WMS系统中不存在",asnMap);
			}
			if(WmsItemScanCode.SCANCODE_NO.equals(item.getUserFieldV10())){
				asnMap.put("cancelReceiveItemInput", "");
				throw new RfBusinessException("请扫描条码",asnMap);
			}
		}
		String hql = "FROM WmsInventory inv WHERE inv.relatedBillCode =:asnCode AND inv.item.code =:itemCode AND inv.status='待检' AND inv.location.type = 'RECEIVE' AND inv.qty >0 ";
		List<WmsInventory> invs = commonDao.findByQuery(hql, new String[]{"asnCode","itemCode"}, new Object[]{asnCode,itemCode});
		if(invs.isEmpty()){
			asnMap.put("cancelReceiveCodeInput", "");
			asnMap.put("cancelReceiveItemInput", "");
			throw new RfBusinessException("收货单号下"+asnCode+"该物料"+itemCode+"收货库位没待检库存，不能取消收货",asnMap);
		}
//		WmsASNDetail detail = commonDao.load(WmsASNDetail.class, Long.valueOf(invs.get(0).getItemKey().getLotInfo().getExtendPropC16()));
		asnMap.put("itemCode", itemCode);
//		asnMap.put("asnDetailId", detail.getId());
		asnMap.put("asnCode", asnCode);
//		asnMap.put("id", detail.getId());
		asnMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(RfConstant.FORWARD_VALUE, "success");
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    perResult.putAll(asnMap);
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	
	/**
	 * 显示收货明细信息
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map showASNDetailInfo(Map asnMap) throws RfBusinessException{
		
		Long asnDetailId = asnMap.get("detailId") == null ? null : Long.valueOf(asnMap.get("detailId").toString());
		if(null == asnDetailId){
			asnDetailId = asnMap.get(RfConstant.ENTITY_ID) == null ? null : Long.valueOf(asnMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(asnDetailId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsASNDetail detail = commonDao.load(WmsASNDetail.class, asnDetailId);
		asnMap.put("asnId", detail.getAsn().getId());
		asnMap.put("asnDetailId", detail.getId());
		asnMap.put("asnCode", detail.getAsn().getCode());
		if(detail.getTransportOrderDetail() != null){
			asnMap.put("deliveryCode", detail.getTransportOrderDetail().getDeliveryOrderDetail().getDeliveryOrder().getCode());
			asnMap.put("deliveryDate", detail.getTransportOrderDetail().getDeliveryOrderDetail().getDeliveryOrder().getDeliveryDate());
		}else{
			asnMap.put("deliveryCode","");
			asnMap.put("deliveryDate","");
		}
		asnMap.put("itemCode", detail.getItem().getCode());
		asnMap.put("itemName", detail.getItem().getName());
		asnMap.put("receivedQty", DoubleUtils.sub(detail.getReceivedQty(), detail.getActualQcQty()));
		
		asnMap.put("asnTypeName", detail.getAsn().getBillType().getName());
		asnMap.put("supplierCode", detail.getAsn().getSupplier().getCode());
		asnMap.put("supplierName", detail.getAsn().getSupplier().getName());
		asnMap.put("userField5", "0".equals(detail.getAsn().getUserField5())?"标准":"寄售");
		asnMap.put("expectedQty", detail.getExpectedQty());
		asnMap.put("unReceiveQty", detail.getExpectedQty()-detail.getReceivedQty());
		asnMap.put(RfConstant.FORWARD_VALUE, "success");
		
		return asnMap;
	}
	
	/**
	 * 确认取消收货
	 */
	public Map cancelReceiveConfirm(Map asnMap) throws RfBusinessException{
		Long asnDetailId = asnMap.get("asnDetailId") == null ? null : Long.valueOf(asnMap.get("asnDetailId").toString());
		if(null == asnDetailId){
			asnDetailId = asnMap.get(RfConstant.ENTITY_ID) == null ? null : Long.valueOf(asnMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(asnDetailId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		Double cancelQty = 0D;
		String cancelQtyStr = asnMap.get("tclCancelReceiveQtyInput") == null ? null : asnMap.get("tclCancelReceiveQtyInput").toString();
		if(cancelQty ==null){
			throw new RfBusinessException("取消数量不能为空");
		}else{
			//判断数字是否正确
			try{
				cancelQty=Double.parseDouble(cancelQtyStr);
			}catch(Exception e){
				throw new RfBusinessException("数量输入格式有误");
			}
			if(cancelQty<0){
				throw new RfBusinessException("数量不能小于0");
			}
		}
		WmsASNDetail detail = commonDao.load(WmsASNDetail.class, asnDetailId);
		if(cancelQty>DoubleUtils.sub(detail.getReceivedQty(), detail.getActualQcQty())){
			throw new RfBusinessException("取消收货数量不能大于可取消收数量");
		}
		String hql = "FROM WmsInventory inv WHERE inv.relatedBillCode =:asnCode AND inv.item.code =:itemCode AND inv.status='待检' AND inv.location.type = 'RECEIVE' AND inv.qty >0 ";
		List<WmsInventory> invs = commonDao.findByQuery(hql, new String[]{"asnCode","itemCode"}, new Object[]{detail.getAsn().getCode(),detail.getItem().getCode()});
		if(invs.isEmpty()){
			throw new RfBusinessException("收货单号下"+detail.getAsn().getCode()+"该物料"+detail.getItem().getCode()+"未没有待检库存，不能取消收货",asnMap);
		}
		
		WmsInventoryManageManager invManager = (WmsInventoryManageManager) applicationContext.getBean("wmsInventoryManageManager");
		Double planCanelQty = cancelQty;//要取消的数量
		for(WmsInventory inv : invs){
			if(planCanelQty==0D){
				break;
			}
			if(planCanelQty>inv.getQty()){
				planCanelQty-=inv.getQty();
				invManager.cancelReceipt(inv, 0L, inv.getQty(), detail.getLineNo().toString());
			}else{
				invManager.cancelReceipt(inv, 0L, planCanelQty, detail.getLineNo().toString());
				planCanelQty=0D;
			}
		}
		
		asnMap.put(RfConstant.CLEAR_VALUE, Boolean.TRUE);
		asnMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		
		return asnMap;
		
	}
	/**
	 * 交货关闭,把未收货的数量退回给交货单
	 */
	public Map asnCloseDeliverOrder(Map asnMap) throws RfBusinessException{
		Long asnId = Long.valueOf(asnMap.get("asnId").toString());
		WmsASN asn = commonDao.load(WmsASN.class, asnId);
		if(!WmsASNStatus.RECEIVING.equals(asn.getStatus())){
			throw new RfBusinessException("收货单未收货，不需要交货关闭!");
		}
		wmsTclASNManager.closeDeliverOrder(asn);
		asnMap.put(RfConstant.CLEAR_VALUE, Boolean.TRUE);
		asnMap.put(RfConstant.FORWARD_VALUE, "success");
		return asnMap;
	}
	/**
	 * 交货关闭提示
	 */
	public Map genConfirmMessage(Map asnMap) throws RfBusinessException{
		asnMap.put(RfConstant.FORWARD_CONFIRM_CODE, "确认交货关闭么？");
		return asnMap;
	}
	/**
	 * 扫描收货单号
	 */
	public Map asnCodeInput(Map asnMap) throws RfBusinessException{
		String asnCode = (String) asnMap.get("asnCodeInput");
		if(StringUtils.isEmpty(asnCode)){
			throw new RfBusinessException("收货单号不能为空");
		}
		String hql = "From WmsASN asn where asn.warehouse.baseOrganization.id=:baseOrganizationId and asn.code=:code ";
		String hql2 = " AND asn.keeper.id =:keeperId ";
		String hql3 = " AND asn.keeper IS NULL ";
		WmsASN asn = (WmsASN)commonDao.findByQueryUniqueResult(hql+hql2, new String[]{"baseOrganizationId","code","keeperId"},new Object[]{BaseOrganizationHolder.getThornBaseOrganization().getId(), asnCode,UserHolder.getUser().getId()});
		if(asn==null){
			asn = (WmsASN)commonDao.findByQueryUniqueResult(hql+hql3, new String[]{"baseOrganizationId","code"},new Object[]{BaseOrganizationHolder.getThornBaseOrganization().getId(), asnCode});
			if(asn == null){
				asnMap.put("asnCodeInput", "");
				throw new RfBusinessException("找不到相应的收货单或保管员不是"+UserHolder.getUser().getLoginName(),asnMap);
			}
		}
		if(!(WmsASNStatus.RECEIVED.equals(asn.getStatus())||WmsASNStatus.RECEIVING.equals(asn.getStatus()))){
			asnMap.put("asnCodeInput", "");
			throw new RfBusinessException("收货单未收货不能质检",asnMap);
		}
		WmsBillType billType = commonDao.load( WmsBillType.class,asn.getBillType().getId());
		WmsSupplier supplier = commonDao.load( WmsSupplier.class,asn.getSupplier().getId());
		String userField5 = asn.getUserField5();
		asnMap.put(RfConstant.ENTITY_ID, asn.getId());
		asnMap.put("asnId", asn.getId());
		asnMap.put("asnCode", asn.getCode());
		asnMap.put(RfConstant.FORWARD_VALUE, "success");
		return asnMap;
	}
	
	/**
	 * 显示待质检信息
	 */
	public Map showQualityInfos(Map asnMap) throws RfBusinessException{
		Long recordId = Long.valueOf(asnMap.get("id").toString());
		WmsReceivedRecord r = commonDao.load(WmsReceivedRecord.class, recordId);
		WmsASN asn = commonDao.load(WmsASN.class,r.getAsnDetail().getAsn().getId());
		WmsSupplier supplier = commonDao.load(WmsSupplier.class, asn.getSupplier().getId());
		asnMap.put("recordId", r.getId());
		asnMap.put("asnCode", asn.getCode());
		asnMap.put("supplierCode", supplier.getCode());
		asnMap.put("supplierName", supplier.getName());
		asnMap.put("factoryCode", asn.getUserField7());
		asnMap.put("itemCode", r.getAsnDetail().getItem().getCode());
		asnMap.put("itemName", r.getAsnDetail().getItem().getName());
		asnMap.put("qcQty", r.getReceivedQty());
		
		return asnMap;
	}
	
	/**
	 * 单一质检确认
	 */
	public Map asnDetailQuality(Map asnMap) throws RfBusinessException{
		Long recordId = asnMap.get("recordId") == null ? null : Long.valueOf(asnMap.get("recordId").toString());
		if(recordId==null){
			recordId = asnMap.get(RfConstant.ENTITY_ID) == null ? null : Long.valueOf(asnMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(recordId==null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsReceivedRecord r = commonDao.load(WmsReceivedRecord.class, recordId);
		Long stateId =  Long.valueOf(asnMap.get("invStatusInput").toString());
		Double qcNumber = asnMap.get("qcQuantity") == null?null:Double.valueOf(asnMap.get("qcQuantity").toString());
		wmsTclASNManager.singleQcRecord(recordId, stateId, UserHolder.getUser().getId(), qcNumber);
		asnMap.put("qcQuantity", "");
		asnMap.put("asnCode", r.getAsnDetail().getAsn().getCode());
		asnMap.put(RfConstant.FORWARD_VALUE, "success");
		return asnMap;
	}
	/**默认收货数量*/
	@Transactional
	public Map receiveQty(Map asnMap) throws RfBusinessException{
		Long detailId = Long.valueOf(asnMap.get("id").toString());
		WmsASNDetail detail = commonDao.load(WmsASNDetail.class, detailId);
		asnMap.put("receiveQuantity", detail.getExpectedQty()-detail.getReceivedQty());
		asnMap.put(RfConstant.FORWARD_VALUE, "success");
		return asnMap;
	}
	/**
	 * 显示上架作业单信息
	 * @param asnMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map showWorkDocInfo(Map asnMap) throws RfBusinessException{
		Map<String, Object> result = new HashMap<String, Object>();
		Long workDocId =  0l;
		workDocId=asnMap.get("workDocId") == null?null:Long.valueOf(asnMap.get("workDocId").toString());
		if (null == workDocId || StringUtils.isEmpty(workDocId.toString())) {
			workDocId = asnMap.get(RfConstant.ENTITY_ID) == null?null:Long.valueOf(asnMap.get(RfConstant.ENTITY_ID).toString());
			if(null == workDocId || StringUtils.isEmpty(workDocId.toString())){
			   throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
			} 
		}
		WmsWorkDoc doc = commonDao.load(WmsWorkDoc.class, workDocId);
		String hql = "FROM WmsTask task WHERE task.workDoc.id =:workDocId ";
		List<WmsTask> tasks = commonDao.findByQuery(hql, "workDocId", workDocId);
		StringBuffer itemInfoStr = new StringBuffer();
		for(int i = 0;i <tasks.size();i++){
//			物料代码：
//			物料名称：
//			数量
			WmsTask task = tasks.get(i);
			String fromLoc = task.getFromLocation().getCode();
			itemInfoStr.append("移出库位:").append(fromLoc).append("\n");
			String toLoc = task.getToLocation().getCode();
			itemInfoStr.append("移入库位:").append(toLoc).append("\n");
			String itemCode = task.getItem().getCode();
			itemInfoStr.append("物料代码:").append(itemCode).append("\n");
			String itemName = task.getItem().getName();
			itemInfoStr.append("物料名称:").append(itemName).append("\n");
			String unit = task.getPackageUnit().getUnit();
			itemInfoStr.append("包装单位:").append(unit).append("\n");
			String lot = task.getItemKey().getLotInfo().getLot();
			itemInfoStr.append("批次号:").append(lot).append("\n");
			Double expectedQty = task.getPlanQty();
			itemInfoStr.append("数量:").append(expectedQty).append("\n");
			if(i<(tasks.size()-1)){
				itemInfoStr.append("----------------------\n");
			}
		}
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    asnMap.put("workDocId", doc.getId());
	    
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
	    perResult.putAll(asnMap);
	    result.put("workDocId", doc.getId());
		result.put("docCode", doc.getCode());
		result.put("docItemInfos", itemInfoStr);
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	/**
	 * 整单上架确认
	 */
	public Map putawayAllCommit(Map asnMap) throws RfBusinessException{
		String workDocId=asnMap.get("workDocId") == null?null:asnMap.get("workDocId").toString();
		if (null == workDocId || StringUtils.isEmpty(workDocId)) {
			workDocId = asnMap.get(RfConstant.ENTITY_ID) == null?null:asnMap.get(RfConstant.ENTITY_ID).toString();
			if(null == workDocId || StringUtils.isEmpty(workDocId)){
			   throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
			} 
		}
		//整单作业确认
		WmsWorkDocManager wmsWorkDocManager = (WmsWorkDocManager) applicationContext.getBean("wmsWorkDocManager");
		wmsWorkDocManager.confirmAllPortal(workDocId, UserHolder.getUser().getId());
		asnMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return asnMap;
	}
	/**
	 * 是否确认上架
	 */
	public Map bePutawayAllCommit(Map asnMap) throws RfBusinessException{
		asnMap.put(RfConstant.FORWARD_CONFIRM_CODE, "是否确认整单上架确认");
		return asnMap;
		
	}
}
