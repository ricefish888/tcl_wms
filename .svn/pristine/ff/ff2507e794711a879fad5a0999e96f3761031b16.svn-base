package com.vtradex.wms.rfserver.service.bol.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils; 
 









import com.vtradex.rf.common.RfConstant;
import com.vtradex.rf.common.exception.RfBusinessException; 
import com.vtradex.rf.util.GsonUtil;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.DateUtil;
import com.vtradex.thorn.server.util.LocalizedMessage; 
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.thorn.server.web.security.UserHolder; 
import com.vtradex.wms.rfserver.bean.RfAsnVo;
import com.vtradex.wms.rfserver.bean.RfSkuTaskVo;
import com.vtradex.wms.rfserver.common.RfConstantDiy;
import com.vtradex.wms.rfserver.common.RfMessageCode;
import com.vtradex.wms.rfserver.service.bol.TclRfBolManager;
import com.vtradex.wms.rfserver.service.order.TclRfOrderManager;
import com.vtradex.wms.rfserver.service.receiving.TclRfAsnManager;
import com.vtradex.wms.rfserver.service.receiving.RfPalletPutawayWorkManager;
import com.vtradex.wms.rfserver.service.receiving.RfSKUPutawayWorkManager;
import com.vtradex.wms.server.enumeration.WmsTclWorkDocType;
import com.vtradex.wms.server.helper.WmsBarCodeParse;
import com.vtradex.wms.server.model.component.LotInfo;
import com.vtradex.wms.server.model.entity.base.WmsProductionOrderCheck;
import com.vtradex.wms.server.model.entity.bol.WmsBol;
import com.vtradex.wms.server.model.entity.bol.WmsBolDetail;
import com.vtradex.wms.server.model.entity.bol.WmsBolStatus;
import com.vtradex.wms.server.model.entity.bol.WmsBolType;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.item.WmsBillType;
import com.vtradex.wms.server.model.entity.item.WmsCompany;
import com.vtradex.wms.server.model.entity.item.WmsInventoryState;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemHandOverAtt;
import com.vtradex.wms.server.model.entity.item.WmsItemJITAtt;
import com.vtradex.wms.server.model.entity.item.WmsLotRule;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.production.ProductionOrder;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsWorkDocType;
import com.vtradex.wms.server.model.entity.receiving.WmsASN;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.entity.receiving.WmsASNStatus;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;
import com.vtradex.wms.server.model.entity.workdoc.WmsWorkDoc;
import com.vtradex.wms.server.model.enums.BaseStatus;
import com.vtradex.wms.server.model.enums.WmsLocationCode;
import com.vtradex.wms.server.model.enums.WmsLocationType;
import com.vtradex.wms.server.model.enums.WmsTaskStatus;
import com.vtradex.wms.server.model.enums.WmsWorkDocStatus;
import com.vtradex.wms.server.service.bol.WmsBolManager;
import com.vtradex.wms.server.service.bol.WmsTclBolManager;
import com.vtradex.wms.server.service.receiving.WmsASNDetailManager;
import com.vtradex.wms.server.service.receiving.WmsASNManager;
import com.vtradex.wms.server.service.receiving.WmsNoTransactionalManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.workdoc.WmsTclWorkDocManager;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.webservice.utils.CommonHelper;

 
/**
 * 订单RF逻辑类
 * @author admin
 *
 */
public class DefaultTclRfBolManager extends DefaultBaseManager implements TclRfBolManager{
	 private WmsBolManager wmsBolManager;
	public DefaultTclRfBolManager(WmsBolManager wmsBolManager) {
		 this.wmsBolManager = wmsBolManager;
	}
	 

	@SuppressWarnings("unchecked")
	@Override
	public Map bolCodeInputCommit(Map bolMap) throws RfBusinessException {
		String bolCode = (String) bolMap.get("bolCodeInput");
		if(StringUtils.isEmpty(bolCode)){
			throw new RfBusinessException("交接单号不能为空");
		}
		
		String hql = "From WmsBol bol where  bol.warehouse.baseOrganization.id=:baseOrganizationId and bol.code=:code ";
		
		WmsBol bol = (WmsBol)commonDao.findByQueryUniqueResult(hql, new String[]{"baseOrganizationId","code"},new Object[]{BaseOrganizationHolder.getThornBaseOrganization().getId(), bolCode});
		if(bol==null){
			bolMap.put("bolCodeInput", "");
			throw new RfBusinessException("未找到交接单",bolMap);
		}
		if(!WmsBolStatus.ACTIVE.equals(bol.getStatus())){
			bolMap.put("bolCodeInput", "");
			throw new RfBusinessException("交接单状态不为激活",bolMap);
		}
		hql ="select detail.pickTicketDetail.item.code, detail.pickTicketDetail.item.name ,sum(detail.planQty) From WmsBolDetail detail where detail.bol.id=:bolId group by detail.pickTicketDetail.item.code, detail.pickTicketDetail.item.name";
		List<Object[]> details = commonDao.findByQuery(hql, "bolId", bol.getId());
		StringBuffer itemInfoStr = new StringBuffer();
		for(int i = 0;i <details.size();i++){
//			物料代码：
//			物料名称：
//			数量
			Object[] detail = details.get(i);
			String itemCode = detail[0].toString();
			itemInfoStr.append("物料代码:").append(itemCode).append("\n");
			String itemName = detail[1].toString();
			itemInfoStr.append("物料名称:").append(itemName).append("\n");
			Double expectedQty =Double.valueOf(detail[2].toString());
			itemInfoStr.append("数量:").append(expectedQty).append("\n");
			if(i<(details.size()-1)){
				itemInfoStr.append("----------------------\n");
			}
		}
		if(WmsBolType.VMI.equals(bol.getType())){
			bolMap.put("bolId", bol.getId());
			bolMap.put("itemInfos", itemInfoStr);
			bolMap.put("bolCode",bol.getCode());
			bolMap.put("bolType", bol.getType());
			bolMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		}else if(WmsBolType.DB.equals(bol.getType())){
			bolMap.put("bolId", bol.getId());
			bolMap.put("itemInfos", itemInfoStr);
			bolMap.put("bolCode", bol.getCode());
			bolMap.put("bolType", bol.getType());
			bolMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		}else{
			throw new RfBusinessException("没有找到对应的单据类型!!");
		}
		return bolMap;
	}



	@Override
	public Map bolInfo(Map bolMap) throws RfBusinessException {
		Map<String, Object> result = new HashMap<String, Object>();
		Long bolId = bolMap.get("bolId") == null ? null : Long.valueOf(bolMap.get("bolId").toString());
		if(null == bolId){
			bolId = bolMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(bolMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(bolId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsBol bol = commonDao.load(WmsBol.class, bolId);
		
		String hql ="select detail.pickTicketDetail.item.code, detail.pickTicketDetail.item.name ,sum(detail.planQty) From WmsBolDetail detail where detail.bol.id=:bolId group by detail.pickTicketDetail.item.code, detail.pickTicketDetail.item.name";
		List<Object[]> details = commonDao.findByQuery(hql, "bolId", bol.getId());
		StringBuffer itemInfoStr = new StringBuffer();
		for(int i = 0;i <details.size();i++){
//			物料代码：
//			物料名称：
//			数量
			Object[] detail = details.get(i);
			String itemCode = detail[0].toString();
			itemInfoStr.append("物料代码:").append(itemCode).append("\n");
			String itemName = detail[1].toString();
			itemInfoStr.append("物料名称:").append(itemName).append("\n");
			Double expectedQty =Double.valueOf(detail[2].toString());
			itemInfoStr.append("数量:").append(expectedQty).append("\n");
			if(i<(details.size()-1)){
				itemInfoStr.append("----------------------\n");
			}
		}
		if(WmsBolType.VMI.equals(bol.getType())){
			result.put("cName", bol.getCustomer().getName());
			bolMap.put("bolId", bol.getId());
			result.put("bolId", bol.getId());
		    result.put("itemInfos", itemInfoStr);
		    result.put("bolCode", bol.getCode());
		    result.put("bolType", bol.getType());
		    result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		}else if(WmsBolType.DB.equals(bol.getType())){
			bolMap.put("bolId", bol.getId());
			result.put("bolId", bol.getId());
		    result.put("itemInfos", itemInfoStr);
		    result.put("bolCode", bol.getCode());
		    result.put("bolType", bol.getType());
		    result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		}else if(WmsBolType.BOL.equals(bol.getType())){
			bolMap.put("bolId", bol.getId());
			result.put("bolId", bol.getId());
		    result.put("itemInfos", itemInfoStr);
		    result.put("bolCode", bol.getCode());
		    result.put("bolType", bol.getType());
		    result.put("remark", bol.getRemark());
		    result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		}else{
			throw new RfBusinessException("没有找到对应的单据类型!!");
		}
		Map<String, Object> perResult = new HashMap<String, Object>();
	    
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
	    perResult.putAll(bolMap);
	    perResult.put("bolId", bol.getId());
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}



	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map bolShipCommit(Map bolMap) throws RfBusinessException {
		Long bolId = bolMap.get("bolId") == null ? null : Long.valueOf(bolMap.get("bolId").toString());
		if(null == bolId){
			bolId = bolMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(bolMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(bolId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsBol bol = commonDao.load(WmsBol.class, bolId);
		if(WmsBolStatus.OPEN.equals(bol.getStatus())){
			if(bol.getDetails().size()<=0){
				throw new BusinessException("出库单未添加作业单");
			}
			bol.setStatus(WmsBolStatus.ACTIVE);
		}
		//调用发运
		wmsBolManager.shipBol(bol);
		bol.setStatus(WmsBolStatus.SHIPPED);
		commonDao.store(bol);
		bolMap.put(RfConstant.CLEAR_VALUE, Boolean.TRUE);
		bolMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return bolMap;
	}



	@SuppressWarnings("unchecked")
	@Override
	public Map cancelShip(Map bolMap) throws RfBusinessException {
		// TODO Auto-generated method stub
		bolMap.put(RfConstant.CLEAR_VALUE, Boolean.TRUE);
		bolMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return bolMap;
	}
	/**
	 * 创建出库单
	 */
	public Map createBol(Map bolMap) throws RfBusinessException{
		Map<String, Object> result = new HashMap<String, Object>();
		String remark = bolMap.get("remarkInput") ==null ? null : bolMap.get("remarkInput").toString().trim();
		WmsBol bol = EntityFactory.getEntity(WmsBol.class);
		bol.setExpectedDeliveryTime(new Date());
		bol.setKeeper(UserHolder.getUser());
		bol.setRemark(remark);
		bol.setType("BOL");
		wmsBolManager.storeBOL(bol);
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
	    perResult.putAll(bolMap);
	    perResult.put("bolId", bol.getId());
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	/**
	 * 添加作业单
	 */
	public Map addWorkDoc(Map bolMap) throws RfBusinessException{
		Long bolId = bolMap.get("bolId") == null ? null : Long.valueOf(bolMap.get("bolId").toString());
		if(null == bolId){
			bolId = bolMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(bolMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(bolId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    bolMap.put("bolId", bolId);
	    
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
	    perResult.putAll(bolMap);
	    result.put("bolId", bolId);
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	/**
	 * 显示配送作业单信息
	 */
	public Map showWorkDocInfo(Map bolMap) throws RfBusinessException{
		Long docId = bolMap.get("docId") == null ? null : Long.valueOf(bolMap.get("docId").toString());
		if(null == docId){
			docId = bolMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(bolMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(docId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsWorkDoc workDoc = commonDao.load(WmsWorkDoc.class, docId);
		String invHql = " SELECT inv.item.code,inv.item.name,SUM(inv.qty),inv.location.code FROM WmsInventory inv WHERE inv.qty >0 AND inv.location.type ='HANDOVER' "+
				 		" AND inv.operationStatus= 'NORMAL' AND inv.location.countLock ='N' " +
				 		" AND inv.itemKey.lotInfo.extendPropC20 =:workDocCode GROUP BY inv.item.code,inv.item.name,inv.location.code ";
		List<Object[]> invs = commonDao.findByQuery(invHql, new String[]{"workDocCode"}, new Object[]{workDoc.getCode()});
//		String hql = "FROM WmsTask task WHERE task.workDoc.id =:workDocId ";
//		List<WmsTask> tasks = commonDao.findByQuery(hql, "workDocId", docId);
		StringBuffer itemInfoStr = new StringBuffer();
		for(int i = 0;i <invs.size();i++){
//			物料代码：
//			物料名称：
//			数量
			Object[] inv = invs.get(i);
			String itemCode = inv[0].toString();
			itemInfoStr.append("物料代码:").append(itemCode).append("\n");
			String itemName = inv[1].toString();
			itemInfoStr.append("物料名称:").append(itemName).append("\n");
			Double expectedQty = Double.valueOf(inv[2].toString());
			itemInfoStr.append("数量:").append(expectedQty).append("\n");
			String locCode = inv[3].toString();
			itemInfoStr.append("移出库位:").append(locCode).append("\n");
			if(i<(invs.size()-1)){
				itemInfoStr.append("----------------------\n");
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    bolMap.put("docId", workDoc.getId());
	    
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
	    perResult.putAll(bolMap);
	    result.put("docId", workDoc.getId());
		result.put("docCode", workDoc.getCode());
		result.put("docItemInfos", itemInfoStr);
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	
	/**
	 * 显示出库作业单信息
	 */
	public Map showBolWorkDocInfo(Map bolMap) throws RfBusinessException{
		Long docId = bolMap.get("docId") == null ? null : Long.valueOf(bolMap.get("docId").toString());
		if(null == docId){
			docId = bolMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(bolMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(docId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsWorkDoc workDoc = commonDao.load(WmsWorkDoc.class, docId);
		String hql = "FROM WmsTask task WHERE task.workDoc.id =:workDocId ";
		List<WmsTask> tasks = commonDao.findByQuery(hql, "workDocId", docId);
		StringBuffer itemInfoStr = new StringBuffer();
		for(int i = 0;i <tasks.size();i++){
//			物料代码：
//			物料名称：
//			数量
			WmsTask task = tasks.get(i);
			WmsItem item = commonDao.load(WmsItem.class, task.getItem().getId());
			String itemCode = item.getCode();
			itemInfoStr.append("物料代码:").append(itemCode).append("\n");
			String itemName = item.getName();
			itemInfoStr.append("物料名称:").append(itemName).append("\n");
			Double expectedQty = task.getPickedQty();
			itemInfoStr.append("数量:").append(expectedQty).append("\n");
			if(i<(tasks.size()-1)){
				itemInfoStr.append("----------------------\n");
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    bolMap.put("docId", workDoc.getId());
	    
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
	    perResult.putAll(bolMap);
	    result.put("docId", workDoc.getId());
		result.put("docCode", workDoc.getCode());
		result.put("docItemInfos", itemInfoStr);
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	/**
	 * 作业单确认 加入到出库单
	 * @param bolMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map addWorkDocConfirm(Map bolMap) throws RfBusinessException{
		Long bolId = bolMap.get("bolId") == null ? null : Long.valueOf(bolMap.get("bolId").toString());
		if(null == bolId){
			bolId = bolMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(bolMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(bolId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		Long docId = bolMap.get("docId") == null ? null : Long.valueOf(bolMap.get("docId").toString());
		if(null == docId){
			docId = bolMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(bolMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(docId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsWorkDoc workDoc = commonDao.load(WmsWorkDoc.class, docId);
		String hql = "FROM WmsTask task WHERE task.workDoc.id =:workDocId AND task.status IN('"+WmsTaskStatus.IN_OPERATION+"','"+WmsTaskStatus.FINISH+"') ";
		List<WmsTask> tasks = commonDao.findByQuery(hql, "workDocId", docId);
		for(WmsTask task : tasks){
			hql = "FROM WmsBolDetail detail WHERE detail.workDoc.id =:workDocId AND detail.bol.id IS NULL AND detail.isPacking = 'N' AND detail.itemKey.id=:itemKeyId ";
			List<WmsBolDetail> details = commonDao.findByQuery(hql, new String[]{"workDocId","itemKeyId"}, new Object[]{workDoc.getId(),task.getItemKey().getId()});
			if(!details.isEmpty()){
				WmsTclBolManager tclBolManager = (WmsTclBolManager) applicationContext.getBean("wmsTclBolManager");
				List<Double> qtyList = new ArrayList<Double>();
				if(task.getPickedQty() >= details.get(0).getPickedQty()){
					qtyList.add(details.get(0).getPickedQty());
				}else{
					qtyList.add(task.getPickedQty());
				}
				tclBolManager.addBOLDetail(bolId, details.get(0), qtyList);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
	    perResult.putAll(bolMap);
//		perResult.put("docId", workDoc.getId());
		perResult.put("docCode", workDoc.getCode());
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	/**
	 * 配送单添加明细
	 * @param bolMap
	 * @param pickedQty
	 * @param code
	 * @throws RfBusinessException
	 */
	private void genQuickShipping(Map bolMap,Double pickedQty,String code) throws RfBusinessException{
		Long quickWorkDocId = bolMap.get("quickWorkDocId") == null ? null : Long.valueOf(bolMap.get("quickWorkDocId").toString());//获取配送单ID
		WmsWorkDoc quickWorkDoc = commonDao.load(WmsWorkDoc.class, quickWorkDocId);
		if(!StringHelper.in(quickWorkDoc.getStatus(), new String[]{WmsWorkDocStatus.ALLOCATED,
			WmsWorkDocStatus.READY_ALLOCATE,WmsWorkDocStatus.PART_ALLOCATE})){
			throw new RfBusinessException("配送单状态错误"+quickWorkDoc.getStatus());
		}
		WmsTclWorkDocManager tclWorkDocManager = (WmsTclWorkDocManager) applicationContext.getBean("wmsTclWorkDocManager");
		
		String hql = "FROM WmsInventory inv WHERE inv.itemKey.lotInfo.extendPropC20 =:workDocCode AND inv.qty>0 AND inv.operationStatus= 'NORMAL'" +
		" AND inv.location.countLock = 'N' AND inv.warehouse.baseOrganization.id =:baseOrganizationId AND inv.location.type =:type ";
		List<WmsInventory> invs = commonDao.findByQuery(hql, new String[]{"workDocCode","baseOrganizationId","type"}, new Object[]{code,
				quickWorkDoc.getWarehouse().getBaseOrganization().getId(),WmsLocationType.HANDOVER});
		if(!invs.isEmpty()){
			Double qty = pickedQty;//需要加入到配送单的数量
				for(WmsInventory inv : invs){
				if(qty <= 0){
					break;
				}
				List<Double> qtyList = new ArrayList<Double>();
				if(qty > inv.getQty()){
					qtyList.add(CommonHelper.dealDoubleError(inv.getQty()));
				}else{
					qtyList.add(CommonHelper.dealDoubleError(qty));
				}
				qty -=inv.getQty();
				if(WmsItemHandOverAtt.T_1_AREA.equals(inv.getItem().getUserFieldV1())){
					tclWorkDocManager.addToMoveDocDetail(quickWorkDoc.getId(), inv, qtyList);
				}else{//线边交接
					hql = "FROM WmsWorkDoc doc WHERE doc.userField2 =:userField2 " +
							" AND doc.status =:status AND doc.warehouse.baseOrganization.id =:baseOrganizationId AND doc.relatedBillCode=:relatedBillCode ";
					WmsWorkDoc xbQuickWorkDoc = (WmsWorkDoc) commonDao.findByQueryUniqueResult(hql, new String[]{"userField2","status","baseOrganizationId","relatedBillCode"}, 
							new Object[]{WmsWorkDocType.LINE_EDGE,WmsWorkDocStatus.READY_ALLOCATE,quickWorkDoc.getWarehouse().getBaseOrganization().getId(),quickWorkDoc.getCode()});
					if(xbQuickWorkDoc == null){
						xbQuickWorkDoc = EntityFactory.getEntity(WmsWorkDoc.class);
						xbQuickWorkDoc.setUserField2(WmsWorkDocType.LINE_EDGE);
						xbQuickWorkDoc.setUserField4(quickWorkDoc.getUserField4());
						xbQuickWorkDoc.setRelatedBillCode(quickWorkDoc.getCode());
						xbQuickWorkDoc.setStatus(WmsWorkDocStatus.READY_ALLOCATE);
						WmsLocation loc = getWmsLocation(quickWorkDoc.getWarehouse());
						tclWorkDocManager.newWorkDoc(loc.getId(), xbQuickWorkDoc, null, quickWorkDoc.getWarehouse());
					}
					tclWorkDocManager.addToMoveDocDetail(xbQuickWorkDoc.getId(), inv, qtyList);
					bolMap.put("xbQuickWorkDocId", xbQuickWorkDoc.getId());
				}
			}
		}
	}
	/**
	 * 根据作业单创建配送单
	 */
	public Map createQuickShipping(Map bolMap) throws RfBusinessException{
		Long docId = bolMap.get("workDocId") == null ? null : Long.valueOf(bolMap.get("workDocId").toString());
		if(null == docId){
			docId = bolMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(bolMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(docId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsWorkDoc doc = commonDao.load(WmsWorkDoc.class, docId);//获取要加入的作业单
		
		Long quickWorkDocId = bolMap.get("quickWorkDocId") == null ? null : Long.valueOf(bolMap.get("quickWorkDocId").toString());//获取配送单ID
		
		this.genQuickShipping(bolMap, doc.getPickedQty(), doc.getCode());
		
		Map<String, Object> result = new HashMap<String, Object>();
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
	    perResult.putAll(bolMap);
	    perResult.put("quickWorkDocId", quickWorkDocId);//配送单ID
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	/**
	 * 根据交接单创建配送单
	 * @param bolMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map createQuickShippingByBol(Map bolMap) throws RfBusinessException{
		Long bolId = bolMap.get("bolId") == null ? null : Long.valueOf(bolMap.get("bolId").toString());
		if(null == bolId){
			bolId = bolMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(bolMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(bolId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsBol bol = commonDao.load(WmsBol.class, bolId);//获取要加入配送单的交接单
		
		Long quickWorkDocId = bolMap.get("quickWorkDocId") == null ? null : Long.valueOf(bolMap.get("quickWorkDocId").toString());//获取配送单ID
		
		this.genQuickShipping(bolMap, bol.getQty(), bol.getCode());
		
		Map<String, Object> result = new HashMap<String, Object>();
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
	    perResult.putAll(bolMap);
	    perResult.put("quickWorkDocId", quickWorkDocId);//配送单ID
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
		
	}
	/**
	 * 获取调拨库位
	 * @return
	 */
	private WmsLocation getWmsLocation(WmsWarehouse warehouse){
		String hql = "FROM WmsLocation loc WHERE loc.code=:code AND loc.status=:status AND loc.warehouse.id =:warehouseId";
		List<WmsLocation> locs = commonDao.findByQuery(hql, new String[]{"code","status","warehouseId"}, new Object[]{WmsLocationCode.T1,BaseStatus.ENABLED,warehouse.getId()});
		if(locs.isEmpty()){
			throw new RfBusinessException("未维护调拨库位信息，请先维护");
		}
		return locs.get(0);
	}
	/**
	 * 创建配送单
	 */
	public Map createWmsWorkDoc(Map quickWorkDocMap) throws RfBusinessException{
		Map<String, Object> result = new HashMap<String, Object>();
		String remark = quickWorkDocMap.get("remarkInput") ==null ? null : quickWorkDocMap.get("remarkInput").toString().trim();
		
		WmsWorkDoc quickWorkDoc = EntityFactory.getEntity(WmsWorkDoc.class);
		quickWorkDoc.setUserField2(WmsWorkDocType.T_1_AREA);
		quickWorkDoc.setUserField4(remark);
		quickWorkDoc.setStatus(WmsWorkDocStatus.READY_ALLOCATE);
		
		WmsWarehouse warehouse = (WmsWarehouse)commonDao.findByQueryUniqueResult("FROM WmsWarehouse warehouse "
                + "WHERE warehouse.baseOrganization.id = :baseOrganizationId", 
                new String[] {"baseOrganizationId"}, 
                new Object[] {BaseOrganizationHolder.getThornBaseOrganization().getId()});
		WmsLocation loc = getWmsLocation(warehouse);
		
		WmsTclWorkDocManager tclWorkDocManager = (WmsTclWorkDocManager) applicationContext.getBean("wmsTclWorkDocManager");
		tclWorkDocManager.newWorkDoc(loc.getId(), quickWorkDoc, null, warehouse);
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
	    perResult.putAll(quickWorkDocMap);
	    perResult.put("quickWorkDocId", quickWorkDoc.getId());//配送单ID
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	/**
	 * 显示配送单信息
	 * @param bolMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map showquickWorkDocInfo(Map quickWorkDocMap) throws RfBusinessException{
		Long docId = quickWorkDocMap.get("quickWorkDocId") == null ? null : Long.valueOf(quickWorkDocMap.get("quickWorkDocId").toString());
		if(null == docId){
			docId = quickWorkDocMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(quickWorkDocMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(docId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsWorkDoc workDoc = commonDao.load(WmsWorkDoc.class, docId);
		String hql = "FROM WmsTask task WHERE task.workDoc.id =:workDocId ";
		List<WmsTask> tasks = commonDao.findByQuery(hql, "workDocId", docId);
		StringBuffer itemInfoStr = new StringBuffer();
		for(int i = 0;i <tasks.size();i++){
//			物料代码：
//			物料名称：
//			数量
			WmsTask task = tasks.get(i);
			if(task.getProductionDetailId() != null){
				ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, task.getProductionDetailId());
				if(pod != null){
					itemInfoStr.append("工单号:").append(pod.getProductionOrder().getCode()).append("\n");
				}
			}
			String itemCode = task.getItem().getCode();
			itemInfoStr.append("物料代码:").append(itemCode).append("\n");
			String itemName = task.getItem().getName();
			itemInfoStr.append("物料名称:").append(itemName).append("\n");
			Double expectedQty = task.getPlanQty();
			itemInfoStr.append("数量:").append(expectedQty).append("\n");
			if(i<(tasks.size()-1)){
				itemInfoStr.append("----------------------\n");
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    quickWorkDocMap.put("quickWorkDocId", workDoc.getId());
	    
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
	    perResult.putAll(quickWorkDocMap);
	    result.put("quickWorkDocId", workDoc.getId());
		result.put("quickWorkDocCode", workDoc.getCode());
		result.put("remark", workDoc.getUserField4());
		result.put("quickWorkDocItemInfos", itemInfoStr);
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	
	/**
	 * 添加配送单
	 */
	public Map addQuickWorkDoc(Map quickWorkDocMap) throws RfBusinessException{
		Long quickWorkDocId = quickWorkDocMap.get("quickWorkDocId") == null ? null : Long.valueOf(quickWorkDocMap.get("quickWorkDocId").toString());
		if(null == quickWorkDocId){
			quickWorkDocId = quickWorkDocMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(quickWorkDocMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(quickWorkDocId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    quickWorkDocMap.put("quickWorkDocId", quickWorkDocId);
	    
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
	    perResult.putAll(quickWorkDocMap);
	    result.put("quickWorkDocId", quickWorkDocId);
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	
	public Map addXBCWorkDoc(Map quickWorkDocMap) throws RfBusinessException{
		
		return this.addQuickWorkDoc(quickWorkDocMap);
	}
	
	/**
	 * 配送单发运
	 */
	public Map quickWorkDocShipCommit(Map quickWorkDocMap) throws RfBusinessException{
		//直接出库配送单ID
		Long quickWorkDocId = quickWorkDocMap.get("quickWorkDocId") == null ? null : Long.valueOf(quickWorkDocMap.get("quickWorkDocId").toString());
		if(null == quickWorkDocId){
			quickWorkDocId = quickWorkDocMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(quickWorkDocMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(quickWorkDocId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		//线边出库配送单ID
		Long xbQuickWorkDocId = quickWorkDocMap.get("xbQuickWorkDocId") == null ? null : Long.valueOf(quickWorkDocMap.get("xbQuickWorkDocId").toString());
		
		WmsWorkDoc quickWorkDoc = commonDao.load(WmsWorkDoc.class, quickWorkDocId);
		WmsTclWorkDocManager tclWorkDocManager = (WmsTclWorkDocManager) applicationContext.getBean("wmsTclWorkDocManager");
		//配送单才执行--之前不知道为啥拣货作业单也走这里了，现在加个限制
		if(quickWorkDoc.getQty()>0 && WmsTclWorkDocType.QUICK_SHIPPING.equals(quickWorkDoc.getType())){
			if(StringHelper.in(quickWorkDoc.getStatus(), new String[]{WmsWorkDocStatus.ALLOCATED,
				WmsWorkDocStatus.PART_ALLOCATE,WmsWorkDocStatus.READY_ALLOCATE})){
				tclWorkDocManager.activeQuickShippingWorkDoc(quickWorkDoc);
				quickWorkDoc.setStatus(WmsWorkDocStatus.ENABLED);
			}
		}else{
			throw new RfBusinessException("配送单未添加明细不能生效");
		}
		if(xbQuickWorkDocId != null){
			WmsWorkDoc xbQuickWorkDoc = commonDao.load(WmsWorkDoc.class, xbQuickWorkDocId);
			if(xbQuickWorkDoc.getQty()>0 && WmsTclWorkDocType.QUICK_SHIPPING.equals(xbQuickWorkDoc.getType())){
				if(StringHelper.in(xbQuickWorkDoc.getStatus(), new String[]{WmsWorkDocStatus.ALLOCATED,
					WmsWorkDocStatus.PART_ALLOCATE,WmsWorkDocStatus.READY_ALLOCATE})){
					tclWorkDocManager.activeQuickShippingWorkDoc(xbQuickWorkDoc);
					xbQuickWorkDoc.setStatus(WmsWorkDocStatus.ENABLED);
				}
			}else{
				throw new RfBusinessException("配送单未添加明细不能生效");
			}
		}
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    Map<String, Object> result = new HashMap<String, Object>();
	    
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
	    perResult.putAll(quickWorkDocMap);
	    perResult.put("quickWorkDocId", quickWorkDoc.getId());//配送单ID
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	/**
	 * VMI创建配送单
	 * @param quickWorkDocMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map genWmsWorkDoc(Map quickWorkDocMap) throws RfBusinessException{
		Map<String, Object> result = new HashMap<String, Object>();
		String remark = quickWorkDocMap.get("remarkInput") ==null ? null : quickWorkDocMap.get("remarkInput").toString().trim();
		Long warehouseId = quickWorkDocMap.get("warehouseInput") ==null ? null : Long.valueOf(quickWorkDocMap.get("warehouseInput").toString());
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class, warehouseId);
		
		WmsWorkDoc quickWorkDoc = EntityFactory.getEntity(WmsWorkDoc.class);
		quickWorkDoc.setUserField2(WmsWorkDocType.T_1_AREA);
		quickWorkDoc.setUserField4("VMI_"+remark);
		quickWorkDoc.setStatus(WmsWorkDocStatus.READY_ALLOCATE);
		WmsLocation loc = this.getWmsLocation(warehouse);
		WmsTclWorkDocManager tclWorkDocManager = (WmsTclWorkDocManager) applicationContext.getBean("wmsTclWorkDocManager");
		tclWorkDocManager.newWorkDoc(loc.getId(), quickWorkDoc, null, warehouse);
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
	    perResult.putAll(quickWorkDocMap);
	    perResult.put("quickWorkDocId", quickWorkDoc.getId());//配送单ID
	    perResult.put("customerCode", warehouse.getCode());//收货人
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	
	public Map createJITWorkDoc(Map quickWorkDocMap) throws RfBusinessException{
		Map<String, Object> result = new HashMap<String, Object>();
		String remark = quickWorkDocMap.get("remarkInput") ==null ? null : quickWorkDocMap.get("remarkInput").toString().trim();
		
		WmsWorkDoc jitWorkDoc = EntityFactory.getEntity(WmsWorkDoc.class);
		jitWorkDoc.setUserField3(WmsItemJITAtt.JIT_UPLINE_SETTLE);
		jitWorkDoc.setUserField4(remark);
		jitWorkDoc.setKeeper(UserHolder.getUser());
		jitWorkDoc.setStatus(WmsWorkDocStatus.READY_ALLOCATE);
		WmsTclWorkDocManager tclWorkDocManager = (WmsTclWorkDocManager) applicationContext.getBean("wmsTclWorkDocManager");
		tclWorkDocManager.newWorkDoc(null, jitWorkDoc, WmsItemJITAtt.JIT_UPLINE_SETTLE, null);
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
	    perResult.putAll(quickWorkDocMap);
	    perResult.put("quickWorkDocId", jitWorkDoc.getId());//JIT出库单ID
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	/**
	 * 显示工单信息
	 * @param quickWorkDocMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map showPodInfo(Map quickWorkDocMap) throws RfBusinessException{
		Long podId = quickWorkDocMap.get("id") ==null ? null : Long.valueOf(quickWorkDocMap.get("id").toString());
		ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, podId);
		quickWorkDocMap.put("poCode", pod.getProductionOrder().getCode());
		quickWorkDocMap.put("itemCode", pod.getItem().getCode());
		quickWorkDocMap.put("itemName", pod.getItem().getName());
		quickWorkDocMap.put("planQty", pod.getPlanQuantityBu());
		quickWorkDocMap.put("allocatedQty", pod.getAllocatedQuantityBu());
		quickWorkDocMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return quickWorkDocMap;
		
	}
	/**
	 * 校验工单
	 */
	public Map checkPo(Map quickWorkDocMap) throws RfBusinessException{
		String prodCode = quickWorkDocMap.get("prodCodeInput") ==null ? null : quickWorkDocMap.get("prodCodeInput").toString().trim();
		if(StringHelper.isNullOrEmpty(prodCode)){
			throw new RfBusinessException("工单号不能为空");
		}
		String hql = "FROM ProductionOrder po WHERE po.code=:code ";
		ProductionOrder po = (ProductionOrder) commonDao.findByQueryUniqueResult(hql, "code", prodCode);
		if(po == null){
			quickWorkDocMap.put("prodCodeInput", "");
			throw new RfBusinessException("工单"+prodCode+"不存在",quickWorkDocMap);
		}
		quickWorkDocMap.put("prodCodeInput", prodCode);//工单号
		quickWorkDocMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return quickWorkDocMap;
	}
	/**
	 * JIT上线结算添加明细
	 */
	public Map addPodDetail(Map quickWorkDocMap) throws RfBusinessException{
		Long podId = quickWorkDocMap.get("id") ==null ? null : Long.valueOf(quickWorkDocMap.get("id").toString());
		ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, podId);//工单明细
		Long jitWorkDocId = quickWorkDocMap.get("quickWorkDocId") ==null ? null : Long.valueOf(quickWorkDocMap.get("quickWorkDocId").toString());
		if(jitWorkDocId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		Double allocatedQty = 0D;//分配数量
		String allocatedQtyStr = quickWorkDocMap.get("allocatedQtyInput") ==null ? null : quickWorkDocMap.get("allocatedQtyInput").toString();
		if(allocatedQtyStr ==null){
			throw new RfBusinessException("取消数量不能为空");
		}else{
			//判断数字是否正确
			try{
				allocatedQty=Double.parseDouble(allocatedQtyStr);
			}catch(Exception e){
				throw new RfBusinessException("数量输入格式有误");
			}
			if(allocatedQty<0){
				throw new RfBusinessException("数量不能小于0");
			}
		}
		if(allocatedQty>pod.getPlanQuantityBu()-pod.getAllocatedQuantityBu()){
			throw new RfBusinessException("分配数量不能大于待分配数量");
		}
		List<Double> qtyList = new ArrayList<Double>();
		qtyList.add(allocatedQty);
		WmsWorkDoc jitWorkDoc = commonDao.load(WmsWorkDoc.class, jitWorkDocId);
		WmsTclWorkDocManager tclWorkDocManager = (WmsTclWorkDocManager) applicationContext.getBean("wmsTclWorkDocManager");
		tclWorkDocManager.addDetail(pod, qtyList, jitWorkDocId);
		quickWorkDocMap.put("prodCodeInput", "");
		quickWorkDocMap.put("allocatedQtyInput", "");
		quickWorkDocMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return quickWorkDocMap;
	}
	/**
	 * JIT上线结算删除明细
	 */
	public Map deleteDetail(Map quickWorkDocMap) throws RfBusinessException{
		Long jitWorkDocId = quickWorkDocMap.get("quickWorkDocId") ==null ? null : Long.valueOf(quickWorkDocMap.get("quickWorkDocId").toString());
		if(jitWorkDocId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsWorkDoc jitWorkDoc = commonDao.load(WmsWorkDoc.class, jitWorkDocId);
		quickWorkDocMap.put("workDocCode", jitWorkDoc.getCode());
		quickWorkDocMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return quickWorkDocMap;
	}
	/**
	 * JIT上线结算删除明细
	 */
	public Map delDetail(Map quickWorkDocMap) throws RfBusinessException{
		Long taskId = quickWorkDocMap.get("id") ==null ? null : Long.valueOf(quickWorkDocMap.get("id").toString());
		if(taskId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsTask task = commonDao.load(WmsTask.class, taskId);
		WmsTclWorkDocManager tclWorkDocManager = (WmsTclWorkDocManager) applicationContext.getBean("wmsTclWorkDocManager");
		tclWorkDocManager.deleteMoveDocDetail(task);
		quickWorkDocMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return quickWorkDocMap;
	}
	/**
	 * 显示要删除的JIT出库单明细的信息
	 */
	public Map showTaskInfo(Map quickWorkDocMap) throws RfBusinessException{
		Long taskId = quickWorkDocMap.get("id") ==null ? null : Long.valueOf(quickWorkDocMap.get("id").toString());
		if(taskId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsTask task = commonDao.load(WmsTask.class, taskId);
		
		if(task.getProductionDetailId() != null){
			ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, task.getProductionDetailId());
			if(pod != null){
				quickWorkDocMap.put("poCode",pod.getProductionOrder().getCode());
			}
		}
		quickWorkDocMap.put("itemCode",task.getItem().getCode());
		quickWorkDocMap.put("itemName",task.getItem().getName());
		quickWorkDocMap.put("planQty",task.getPlanQty());
		return quickWorkDocMap;
	}
	/**
	 * JIT出库
	 */
	public Map shipConfirm(Map quickWorkDocMap) throws RfBusinessException{
		Long workDocId = quickWorkDocMap.get("id") ==null ? null : Long.valueOf(quickWorkDocMap.get("id").toString());
		if(workDocId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsWorkDoc workDoc = commonDao.load(WmsWorkDoc.class, workDocId);
		WmsTclWorkDocManager tclWorkDocManager = (WmsTclWorkDocManager) applicationContext.getBean("wmsTclWorkDocManager");
		if(WmsWorkDocStatus.READY_ALLOCATE.equals(workDoc.getStatus())){
			tclWorkDocManager.activeQuickShippingWorkDoc(workDoc);
			workDoc.setStatus(WmsWorkDocStatus.ENABLED);
		}
		tclWorkDocManager.shipJitOrder(workDoc);
		quickWorkDocMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return quickWorkDocMap;
	}
	/**
	 * JIT出库页面跳转
	 */
	public Map forwardPage(Map quickWorkDocMap) throws RfBusinessException{
		quickWorkDocMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return quickWorkDocMap;
	}
}
