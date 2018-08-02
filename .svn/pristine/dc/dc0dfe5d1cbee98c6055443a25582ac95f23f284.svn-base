package com.vtradex.wms.rfserver.service.pickticket.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vtradex.rf.common.RfConstant;
import com.vtradex.rf.common.exception.RfBusinessException;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.rfserver.common.RfMessageCode;
import com.vtradex.wms.rfserver.service.delivery.TclRfDeliveryManager;
import com.vtradex.wms.rfserver.service.pickticket.TclRfPickticketManager;
import com.vtradex.wms.rfserver.service.receiving.TclRfAsnManager;
import com.vtradex.wms.server.helper.WmsBarCodeParse;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemKey;
import com.vtradex.wms.server.model.entity.item.WmsItemScanCode;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.production.ProductionOrder;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;
import com.vtradex.wms.server.model.entity.workdoc.WmsTaskGroup;
import com.vtradex.wms.server.model.entity.workdoc.WmsWorkDoc;
import com.vtradex.wms.server.model.enums.WmsLocationCode;
import com.vtradex.wms.server.model.enums.WmsTaskGroupStatus;
import com.vtradex.wms.server.model.enums.WmsTaskStatus;
import com.vtradex.wms.server.model.enums.WmsTaskType;
import com.vtradex.wms.server.model.enums.WmsWorkDocStatus;
import com.vtradex.wms.server.service.workdoc.WmsWorkDocManager;
import com.vtradex.wms.server.utils.DateUtils;
import com.vtradex.wms.server.utils.StringHelper;



public class DefaultTclRfPickticketManager extends DefaultRfPickticketManager implements TclRfPickticketManager{
	
	protected WmsWorkDocManager wmsWorkDocManager;

	public DefaultTclRfPickticketManager(WmsWorkDocManager wmsWorkDocManager) {
		super(wmsWorkDocManager);
		this.wmsWorkDocManager = wmsWorkDocManager;
 
	}
 
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
	@Override
	public Map inputItemCommit(Map workDocMap) {
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<String, Object>();
		result.putAll(workDocMap);
		String itemCodeStr=workDocMap.get("pickItemInput") == null?null:workDocMap.get("pickItemInput").toString();
		if(null==itemCodeStr||"".equals(itemCodeStr)){
			throw new RfBusinessException("this.item.can.not.be.null");
		}
		
		//检查该库位是否在任务组或者作业单中
		long taskId = workDocMap.get("taskId") == null ? null
				: Long.valueOf(workDocMap.get("taskId").toString());
		WmsTask task=commonDao.load(WmsTask.class, taskId);
		
		String itemCode = "";
		String lotNo = "";
		TclRfAsnManager tclRfAsnManager = (TclRfAsnManager) applicationContext.getBean("tclRfAsnManager");
		tclRfAsnManager.checkBarCode(itemCodeStr);
		if(WmsBarCodeParse.isBarCode(itemCodeStr)){
			Map infos = WmsBarCodeParse.parse(itemCodeStr);
			itemCode = (String)infos.get(WmsBarCodeParse.KEY_ITEMCODE);
			WmsItem item = (WmsItem)commonDao.findByQueryUniqueResult("FROM WmsItem item where item.code=:ic", "ic", itemCode);
			if(null==item){
				workDocMap.put("pickItemInput", "");
				throw new RfBusinessException("物料"+itemCode+"不存在!!",workDocMap);
			}
			lotNo = (String) infos.get(WmsBarCodeParse.KEY_LOTNO);//批号
			String asnId = infos.get(WmsBarCodeParse.KEY_ASN_ID).toString().trim();//收货明细ID
			String genBarCode=workDocMap.get("genBarCodeInput") == null?null:workDocMap.get("genBarCodeInput").toString();
			if("true".equals(genBarCode)){//生成条码补打记录
				try{
					TclRfDeliveryManager tclRfDeliveryManager = (TclRfDeliveryManager) applicationContext.getBean("tclRfDeliveryManager");
					tclRfDeliveryManager.genBarCode(item, lotNo, itemCodeStr, asnId);
				}catch(RfBusinessException e){
					workDocMap.put("pickItemInput", "");
					throw new RfBusinessException(e.getCode(),workDocMap);
				}
				
			}
			//拣货的批次必须和分配的批次一致  物料+批号匹配→→20170921--现改成只要作业单下有扫描的物料和批号就可以
//			if(!itemCode.equals(task.getItem().getCode()) || !lotNo.equals(task.getItemKey().getLotInfo().getLot())){
//				workDocMap.put("pickItemInput", "");
//				throw new RfBusinessException("物料编码"+itemCode+"或批号"+lotNo+"不是拣选分配的物料编码或批号",workDocMap);
//			}
		}else{
			itemCode = itemCodeStr;
			WmsItem item = (WmsItem)commonDao.findByQueryUniqueResult("FROM WmsItem item where item.code=:ic", "ic", itemCode);
			if(null==item){
				workDocMap.put("pickItemInput", "");
				throw new RfBusinessException("物料"+itemCode+"不存在!!",workDocMap);
			}
			if(WmsItemScanCode.SCANCODE_NO.equals(item.getUserFieldV10())){
				workDocMap.put("pickItemInput", "");
				throw new RfBusinessException("请扫描条码",workDocMap);
			}
		}
		WmsWorkDoc workDoc=commonDao.load(WmsWorkDoc.class, task.getWorkDoc().getId());
		String lotHql = " AND task.itemKey.lotInfo.lot =:lot";
		if(task.getTaskGroup()!=null){
			WmsTaskGroup group=commonDao.load(WmsTaskGroup.class, task.getTaskGroup().getId());
			String hql="from WmsTask task where task.taskGroup.id=:groupId "
					+ " and task.fromLocation.id=:locId and task.status in (:status)"
					+ " and task.type=:type and (task.item.code=:itemCode or task.item.barCode=:itemCode or task.item.barCode2=:itemCode)";
			List<WmsTask> tasks = new ArrayList<WmsTask>();
			if(StringHelper.isNullOrEmpty(lotNo)){
				tasks=commonDao.findByQuery(hql, 
						new String[]{"groupId","locId","status","type","itemCode"}, 
						new Object[]{group.getId(),task.getFromLocation().getId(),
						Arrays.asList(WmsTaskStatus.ENABLED,WmsTaskStatus.IN_OPERATION),WmsTaskType.PICKING,itemCode});
			}else{
				tasks=commonDao.findByQuery(hql+lotHql, 
						new String[]{"groupId","locId","status","type","itemCode","lot"}, 
						new Object[]{group.getId(),task.getFromLocation().getId(),
						Arrays.asList(WmsTaskStatus.ENABLED,WmsTaskStatus.IN_OPERATION),WmsTaskType.PICKING,itemCode,lotNo});
			}
			if(tasks.size()<=0){
				throw new RfBusinessException("this.item.and.loc.is.not.in.the.group");
			}
			task=tasks.get(0);
		}else{
			List<WmsTask> tasks = new ArrayList<WmsTask>();
			String hql="from WmsTask task where task.workDoc.id=:workDocId "
					+ " and task.fromLocation.id=:locId and task.status in (:status)"
					+ " and task.type=:type and (task.item.code=:itemCode or task.item.barCode=:itemCode or task.item.barCode2=:itemCode)";
			if(StringHelper.isNullOrEmpty(lotNo)){
				tasks=commonDao.findByQuery(hql, 
						new String[]{"workDocId","locId","status","type","itemCode"}, 
						new Object[]{workDoc.getId(),task.getFromLocation().getId(),
						Arrays.asList(WmsTaskStatus.ENABLED,WmsTaskStatus.IN_OPERATION),WmsTaskType.PICKING,itemCode});
			}else{
				tasks=commonDao.findByQuery(hql+lotHql, 
						new String[]{"workDocId","locId","status","type","itemCode","lot"}, 
						new Object[]{workDoc.getId(),task.getFromLocation().getId(),
						Arrays.asList(WmsTaskStatus.ENABLED,WmsTaskStatus.IN_OPERATION),WmsTaskType.PICKING,itemCode,lotNo});
			}
			if(tasks.size()<=0){
				throw new RfBusinessException("this.item.and.loc.is.not.in.the.workDoc");
			}
			task=tasks.get(0);
		}
		//设置移位数量
		Map<String, Object> qtyMap = new HashMap<String, Object>();
		qtyMap.put("task.id", task.getId());
//		Double qty=wmsWorkDocManager.getPickedQty(qtyMap);
		Double qty=this.getUnPickedQty(task,itemCode,lotNo);
		result.put(RfConstant.FORWARD_VALUE, "success");
		//持久MAP
    	Map<String, Object> perResult = new HashMap<String, Object>();
		perResult.putAll(workDocMap);
		perResult.put("taskId", task.getId());
		perResult.put("pickNumber", qty);
		perResult.put("pickNumberInput", qty);
        result.put(RfConstant.PERSISTENT_VALUE, perResult);//将持久map放入返回map中
        result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	
	}
	/**
	 * 获取未作业数量
	 * @return
	 */
	private Double getUnPickedQty(WmsTask task,String itemCode,String lot){
		if(StringHelper.isNullOrEmpty(lot)){
			String hql = "SELECT SUM(task.planQty-task.pickedQty) FROM WmsTask task WHERE task.item.code=:itemCode "+
			"AND task.workDoc.id =:workDocId AND task.status !='FINISH' AND task.fromLocation.id =:fromLocationId " +
			"GROUP BY task.item.code,task.item.name,task.fromLocation.id,task.workDoc.id ";
			Double qty = (Double) commonDao.findByQueryUniqueResult(hql, new String[]{"itemCode","workDocId","fromLocationId"},
					new Object[]{itemCode,task.getWorkDoc().getId(),task.getFromLocation().getId()});
			return qty;
		}else{
			String hql = "SELECT SUM(task.planQty-task.pickedQty) FROM WmsTask task WHERE task.item.code=:itemCode AND task.itemKey.lotInfo.lot=:lot " +
			"AND task.workDoc.id =:workDocId AND task.status !='FINISH' AND task.fromLocation.id =:fromLocationId " +
			"GROUP BY task.item.code,task.item.name,task.itemKey.lotInfo.lot,task.fromLocation.id,task.workDoc.id ";
			Double qty = (Double) commonDao.findByQueryUniqueResult(hql, new String[]{"itemCode","lot","workDocId","fromLocationId"},
					new Object[]{itemCode,lot,task.getWorkDoc().getId(),task.getFromLocation().getId()});
			return qty;
		}
	}
	/**
	 * 显示作业单信息
	 */
	public Map showLocCodeAndItemCode(Map workDocMap){
		Map<String, Object> result = new HashMap<String, Object>();
		result.putAll(workDocMap);
		Long taskId=0l;
		taskId = workDocMap.get("taskId") == null ? null
				: Long.valueOf(workDocMap.get("taskId").toString());
		if(taskId==null){
			taskId = workDocMap.get("id") == null ? null
					: Long.valueOf(workDocMap.get("id").toString());
		}
		WmsTask wmsTask=commonDao.load(WmsTask.class, taskId);
		WmsWorkDoc workDoc=commonDao.load(WmsWorkDoc.class, wmsTask.getWorkDoc().getId());
		result.put("workCode", workDoc.getCode());
		result.put("productLine", workDoc.getProductLine());
		result.put("lineStr", workDoc.getUserField5());
		result.put("poCode", workDoc.getProductOrderCode());
		result.put("relatedBillCode", workDoc.getRelatedBillCode());
		result.put("toLocCode", wmsTask.getToLocation().getCode());
		
		String hql = "SELECT task.item.code,task.item.name,task.itemKey.lotInfo.lot,task.itemKey.lotInfo.supplierCode," +
				"SUM(task.planQty),SUM(task.pickedQty),task.fromLocation.id,task.workDoc.id FROM WmsTask task " +
				"WHERE task.workDoc.id =:workDocId AND task.status !='FINISH' AND task.fromLocation.id =:fromLocationId " +
				"GROUP BY task.item.code,task.item.name,task.itemKey.lotInfo.lot,task.itemKey.lotInfo.supplierCode,task.fromLocation.id,task.workDoc.id ";
		List<Object[]> tasks = commonDao.findByQuery(hql, new String[]{"workDocId","fromLocationId"}, 
				new Object[]{workDoc.getId(),wmsTask.getFromLocation().getId()});
		this.showInfos(result, tasks);
		return result;
	}
	
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
	public Map forwardPage(Map workDocMap){
		Long workId = workDocMap.get(RfConstant.ENTITY_ID) == null ? null
				: Long.valueOf(workDocMap.get(RfConstant.ENTITY_ID).toString());
		List<WmsTaskGroup> groups=commonDao.findByQuery("from WmsTaskGroup group where group.id in (select task.taskGroup.id from "
				+ " WmsTask task where task.workDoc.id=:workDocId and task.status in (:status) "
				+ " and task.type=:type )",
				new String[]{"workDocId","status","type"},
				new Object[]{workId,Arrays.asList(WmsTaskStatus.ENABLED,WmsTaskStatus.IN_OPERATION),WmsTaskType.PICKING});
		if(groups.size()<=1){
			//没有任务组，或者任务组为1时，跳转第三个页面：显示明细
			workDocMap.put(RfConstant.FORWARD_VALUE, "successThree");
			List<WmsTask> tasks=commonDao.findByQuery("from WmsTask task where task.workDoc.id=:workDocId and task.status in (:status) "
					+ " and task.type=:type "
					+ " order by task.fromLocation.pickingSequence asc",
					new String[]{"workDocId","status","type"},
					new Object[]{workId,Arrays.asList(WmsTaskStatus.ENABLED,WmsTaskStatus.IN_OPERATION),WmsTaskType.PICKING});
			if(tasks.size()<=0){
				throw new RfBusinessException("can.not.find.the.tasks");
			}
			Long taskId=tasks.get(0).getId();
			WmsTask task = commonDao.load(WmsTask.class, taskId);
			WmsLocation loc = commonDao.load(WmsLocation.class, task.getFromLocation().getId());
			// 持久MAP
			Map<String, Object> perResult = new HashMap<String, Object>();
			perResult.putAll(workDocMap);
			perResult.put("taskId",taskId);
			perResult.put("pickLocInput", loc.getCode());
			workDocMap.put(RfConstant.PERSISTENT_VALUE, perResult);// 将持久map放入返回map
			workDocMap.put(RfConstant.CLEAR_VALUE, "true");
			workDocMap.put(RfConstant.FORWARD_VALUE, "successThree");
		}else{
			workDocMap.put(RfConstant.FORWARD_VALUE, "successTwo");
		}
		return workDocMap;
	}
	/**
	 * 显示作业任务信息
	 */
	public Map showWmsTaskInfo(Map workDocMap){
		Map<String, Object> result = new HashMap<String, Object>();
		Long taskId=0l;
		taskId = workDocMap.get("taskId") == null ? null
				: Long.valueOf(workDocMap.get("taskId").toString());
		if(taskId==null){
			taskId = workDocMap.get("id") == null ? null
					: Long.valueOf(workDocMap.get("id").toString());
		}
		WmsTask wmsTask=commonDao.load(WmsTask.class, taskId);
		WmsWorkDoc workDoc=commonDao.load(WmsWorkDoc.class, wmsTask.getWorkDoc().getId());
		result.put("workCode", workDoc.getCode());
		result.put("productLine", workDoc.getProductLine());
		result.put("lineStr", workDoc.getUserField5());
		result.put("poCode", workDoc.getProductOrderCode());
		result.put("relatedBillCode", workDoc.getRelatedBillCode());
		result.put("toLocCode", wmsTask.getToLocation().getCode());
		
		String hql = "SELECT task.item.code,task.item.name,task.itemKey.lotInfo.lot,task.itemKey.lotInfo.supplierCode," +
		"SUM(task.planQty),SUM(task.pickedQty),MAX(task.fromLocation.id),task.workDoc.id FROM WmsTask task " +
		"WHERE task.workDoc.id =:workDocId AND task.status !='FINISH' AND task.item.code =:itemCode " +
		"GROUP BY task.item.code,task.item.name,task.itemKey.lotInfo.lot,task.itemKey.lotInfo.supplierCode,task.fromLocation.id,task.workDoc.id ";
		List<Object[]> tasks = commonDao.findByQuery(hql, new String[]{"workDocId","itemCode"}, 
		new Object[]{workDoc.getId(),wmsTask.getItem().getCode()});
		this.showInfos(result, tasks);
		
		
//		workDocMap.put("workCode", workDoc.getCode());
//		workDocMap.put("relatedBillCode", wmsTask.getRelatedBillCode());
//		workDocMap.put("productOrderCode", workDoc.getProductOrderCode());
//		workDocMap.put("productLine", workDoc.getProductLine());
//		workDocMap.put("lineStr", workDoc.getUserField5());
//		workDocMap.put("poCode", workDoc.getProductOrderCode());
//		workDocMap.put("loc",wmsTask.getFromLocation().getCode());
//		workDocMap.put("itemCode", wmsTask.getItem().getCode());
//		workDocMap.put("itemName", wmsTask.getItem().getName());
//		workDocMap.put("qty", wmsTask.getPickedQty());
//		workDocMap.put("planQty",wmsTask.getPlanQty());
//		workDocMap.put("supplierCode", wmsTask.getItemKey().getLotInfo().getSupplierCode());
//		WmsSupplier supplier = this.getSupplierByCode(wmsTask.getItemKey().getLotInfo().getSupplierCode());
//		workDocMap.put("supplierName", supplier.getName());
//		workDocMap.put("lot", wmsTask.getItemKey().getLotInfo().getLot());
		return result;
	}
	private WmsSupplier getSupplierByCode(String code){
		String hql = "from WmsSupplier s where s.code =:code ";
		WmsSupplier supplier = (WmsSupplier) commonDao.findByQueryUniqueResult(hql, "code", code);
		if(supplier == null){
			throw new RfBusinessException("供应商不存在");
		}
		return supplier;
		
	}
	/**
	 * 扫描物料
	 * @param workDocMap
	 * @return
	 */
	public Map checkItemCodeCommit(Map workDocMap) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.putAll(workDocMap);
		String itemCodeStr=workDocMap.get("pickItemInput") == null?null:workDocMap.get("pickItemInput").toString();
		if(null==itemCodeStr||"".equals(itemCodeStr)){
			throw new RfBusinessException("物料编码不能为空");
		}
		
		//检查该库位是否在任务组或者作业单中
		long taskId = workDocMap.get("id") == null ? null
				: Long.valueOf(workDocMap.get("id").toString());
		WmsTask task=commonDao.load(WmsTask.class, taskId);
		String itemCode = "";
		String lotNo = "";
		TclRfAsnManager tclRfAsnManager = (TclRfAsnManager) applicationContext.getBean("tclRfAsnManager");
		Boolean flag = Boolean.TRUE;
		if("0".equals(itemCodeStr)){
			flag = Boolean.FALSE;
		}
		if(flag){
			tclRfAsnManager.checkBarCode(itemCodeStr);
			if(WmsBarCodeParse.isBarCode(itemCodeStr)){
				Map infos = WmsBarCodeParse.parse(itemCodeStr);
				itemCode = (String)infos.get(WmsBarCodeParse.KEY_ITEMCODE);
				WmsItem item = (WmsItem)commonDao.findByQueryUniqueResult("FROM WmsItem item where item.code=:ic", "ic", itemCode);
				if(null==item){
					workDocMap.put("pickItemInput", "");
					throw new RfBusinessException("物料"+itemCode+"不存在!!",workDocMap);
				}
				lotNo = (String) infos.get(WmsBarCodeParse.KEY_LOTNO);//批号
				if(!itemCode.equals(task.getItem().getCode()) || !lotNo.equals(task.getItemKey().getLotInfo().getLot())){
					workDocMap.put("pickItemInput", "");
					throw new RfBusinessException("物料编码"+itemCode+"或批号"+lotNo+"不是拣选分配的物料编码或批号",workDocMap);
				}
//				String detailId = infos.get(WmsBarCodeParse.KEY_ASN_DETAIL_ID).toString().trim();//收货明细ID
//				String genBarCode=workDocMap.get("genBarCodeInput") == null?null:workDocMap.get("genBarCodeInput").toString();
//				if("true".equals(genBarCode)){//生成条码补打记录
//					try{
//						TclRfDeliveryManager tclRfDeliveryManager = (TclRfDeliveryManager) applicationContext.getBean("tclRfDeliveryManager");
//						tclRfDeliveryManager.genBarCode(item, lotNo, itemCodeStr, detailId);
//					}catch(RfBusinessException e){
//						workDocMap.put("pickItemInput", "");
//						throw new RfBusinessException(e.getCode(),workDocMap);
//					}
//				}
			}else{
				itemCode = itemCodeStr;
				if(!itemCode.equals(task.getItem().getCode())){
					workDocMap.put("pickItemInput", "");
					throw new RfBusinessException("物料"+itemCode+"不在当前拣货任务中!!",workDocMap);
				}
				if(WmsItemScanCode.SCANCODE_NO.equals(task.getItem().getUserFieldV10())){
					workDocMap.put("pickItemInput", "");
					throw new RfBusinessException("请扫描条码",workDocMap);
				}
			}
		}
		//生成条码补打记录
		Map barCodeinfos = WmsBarCodeParse.parse(task.getItemKey().getLotInfo().getExtendPropC17());
		String asnId = barCodeinfos.get(WmsBarCodeParse.KEY_ASN_ID).toString().trim();//收货明细ID
		String genBarCode=workDocMap.get("genBarCodeInput") == null?null:workDocMap.get("genBarCodeInput").toString();
		if("true".equals(genBarCode)){
			try{
				TclRfDeliveryManager tclRfDeliveryManager = (TclRfDeliveryManager) applicationContext.getBean("tclRfDeliveryManager");
				tclRfDeliveryManager.genBarCode(task.getItem(), task.getItemKey().getLotInfo().getLot(), task.getItemKey().getLotInfo().getExtendPropC17(), asnId);
			}catch(RfBusinessException e){
				workDocMap.put("pickItemInput", "");
				throw new RfBusinessException(e.getCode(),workDocMap);
			}
		}
		//设置移位数量
		Map<String, Object> qtyMap = new HashMap<String, Object>();
		qtyMap.put("task.id", task.getId());
		Double qty=this.getUnPickedQty(task, task.getItem().getCode(), task.getItemKey().getLotInfo().getLot());
		result.put(RfConstant.FORWARD_VALUE, "success");
		//持久MAP
    	Map<String, Object> perResult = new HashMap<String, Object>();
		perResult.putAll(workDocMap);
		perResult.put("taskId", task.getId());
		perResult.put("pickNumber", qty);
		perResult.put("pickNumberInput", qty);
        result.put(RfConstant.PERSISTENT_VALUE, perResult);//将持久map放入返回map中
        result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	/**
	 * 明细单一拣选确认
	 */
	public Map pickNumCommit(Map workDocMap){
		String number=workDocMap.get("pickNumberInput") == null?null:workDocMap.get("pickNumberInput").toString();
		Double pickNumber=workDocMap.get("pickNumber") == null?null:Double.parseDouble(workDocMap.get("pickNumber").toString());
		String workCodeSearch = workDocMap.get("workCodeSearch") == null?null : workDocMap.get("workCodeSearch").toString();
		//判断数字合法性
		Double qty=0d;
		if (null == number) {
			throw new RfBusinessException("数量不得为空");
		}else{
			//判断数字是否正确
			try{
				qty=Double.parseDouble(number);
			}catch(Exception e){
				throw new RfBusinessException("数量输入错误");
			}
			if(qty<=0){
				throw new RfBusinessException("数量不能为小于等于0的数");
			}
			if(qty>pickNumber){
				throw new RfBusinessException("数量不得大于待拣选数量");
			}
		}
		Long taskId =workDocMap.get("taskId") == null ? null : Long
					.valueOf(workDocMap.get("taskId").toString());
		WmsTask	task=commonDao.load(WmsTask.class,taskId);
		
		String taskHql="from WmsTask task where task.workDoc.id=:workDocId "
			+ " and task.fromLocation.id=:locId and task.status in (:status)"
			+ " and task.type=:type AND task.item.code =:itemCode";
		List<WmsTask> wmsTasks=commonDao.findByQuery(taskHql, 
			new String[]{"workDocId","locId","status","type","itemCode"}, 
			new Object[]{task.getWorkDoc().getId(),task.getFromLocation().getId(),
			Arrays.asList(WmsTaskStatus.ENABLED,WmsTaskStatus.IN_OPERATION),WmsTaskType.PICKING,task.getItem().getCode()});
		try{
			Double inPutQty = qty;
			for(WmsTask wmsTask : wmsTasks){
				Double planPickQty = wmsTask.getPlanQty()-wmsTask.getPickedQty();
				if(inPutQty<=0){
					break;
				}
				if(inPutQty <planPickQty){
					wmsWorkDocManager.singleWorkConfirm(wmsTask, wmsTask, inPutQty,null);
					inPutQty=0D;
				}else{
					wmsWorkDocManager.singleWorkConfirm(wmsTask, wmsTask, planPickQty,null);
					inPutQty-=planPickQty;
				}
			}
		    
		}catch(Exception e){
			throw new RfBusinessException(e.getMessage());
		}
		
		workDocMap.put(RfConstant.FORWARD_VALUE, "success");
		workDocMap.put(RfConstant.CLEAR_VALUE, "true");
		
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> perResult = new HashMap<String, Object>();
		perResult.put("workCodeSearch", workCodeSearch);
		result.put(RfConstant.PERSISTENT_VALUE, perResult);//将持久map放入返回map中
		result.put(RfConstant.FORWARD_VALUE, "success");
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	/**
	 * 显示整单作业单信息
	 * @param workDocMap
	 * @return
	 */
	public Map showWorkDocInfos(Map workDocMap){
		Map<String, Object> result = new HashMap<String, Object>();
		result.putAll(workDocMap);
		Long workDocId = 0L;
		workDocId = workDocMap.get("id") == null ? null : Long.valueOf(workDocMap.get("id").toString());
		if(workDocId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsWorkDoc workDoc = commonDao.load(WmsWorkDoc.class, workDocId);
		result.put("workCode", workDoc.getCode());
		result.put("productLine", workDoc.getProductLine());
		result.put("lineStr", workDoc.getUserField5());
		result.put("poCode", workDoc.getProductOrderCode());
		
		String hql = "SELECT task.item.code,task.item.name,task.itemKey.lotInfo.lot,task.itemKey.lotInfo.supplierCode," +
		"SUM(task.planQty),SUM(task.pickedQty),MAX(task.fromLocation.id),task.workDoc.id FROM WmsTask task " +
		"WHERE task.workDoc.id =:workDocId AND task.status !='FINISH' " +
		"GROUP BY task.item.code,task.item.name,task.itemKey.lotInfo.lot,task.itemKey.lotInfo.supplierCode,task.workDoc.id,task.fromLocation.id ";
		List<Object[]> tasks = commonDao.findByQuery(hql, new String[]{"workDocId"}, new Object[]{workDoc.getId()});
		this.showInfos(result, tasks);
		return result;
		
	}
	/**
	 * 显示信息
	 * @param result
	 * @param tasks
	 */
	private void showInfos(Map<String, Object> result,List<Object[]> tasks){
		StringBuffer itemInfoStr = new StringBuffer();
		for(int i = 0;i <tasks.size();i++){
//			物料代码：
//			物料名称：
//			数量
			Object[] o = tasks.get(i);
			String itemCode = o[0].toString();
			itemInfoStr.append("物料代码:").append(itemCode).append("\n");
			String itemName = o[1].toString();
			itemInfoStr.append("物料名称:").append(itemName).append("\n");
			String supplierCode = o[3].toString();
			itemInfoStr.append("供应商编码:").append(supplierCode).append("\n");
			WmsSupplier supplier = this.getSupplierByCode(supplierCode);
			itemInfoStr.append("供应商名称:").append(supplier.getName()).append("\n");
			String lot = o[2].toString();
			itemInfoStr.append("批号:").append(lot).append("\n");
			if(o[6]!=null){
				WmsLocation location=commonDao.load(WmsLocation.class, Long.valueOf(o[6].toString()));
				itemInfoStr.append("库位:").append(location.getCode()).append("\n");
			}
			Double expectedQty = Double.valueOf(o[4].toString());
			itemInfoStr.append("计划数量:").append(expectedQty).append("\n");
			Double pickedQty = Double.valueOf(o[5].toString());
			itemInfoStr.append("已拣数量:").append(pickedQty).append("\n");
			if(i<(tasks.size()-1)){
				itemInfoStr.append("----------------------\n");
			}
//			if(null!=task.getTaskGroup()){
//				WmsTaskGroup group=commonDao.load(WmsTaskGroup.class, task.getTaskGroup().getId());
//				result.put("wmsTaskGroupName",group==null?null:group.getName());
//			}
			if(o[7]!=null){
				WmsWorkDoc workDoc = commonDao.load(WmsWorkDoc.class, Long.valueOf(o[7].toString()));
				WmsPickTicket pickTicket=(WmsPickTicket) commonDao
				.findByQueryUniqueResult("from WmsPickTicket pick where pick.code=:code and pick.warehouse.id=:warehouseId", 
				new String[]{"code","warehouseId"}, 
				new Object[]{workDoc.getRelatedBillCode(),workDoc.getWarehouse().getId()});
				result.put("customer",pickTicket==null?null:pickTicket.getShipToName());
				result.put("orderDate", pickTicket==null?null:DateUtils.format(pickTicket.getOrderDate(), "yyyy-MM-dd"));
			}
			result.put("itemCode", itemCode);
			result.put("itemName", itemName);
			result.put("qty", pickedQty);
			result.put("planQty", expectedQty);
			result.put("itemInfos", itemInfoStr);
		}
	}
	
	/**
	 * 作业单整单确认
	 * @param workDocMap
	 * @return
	 */
	public Map workDocCommit(Map workDocMap){
		Long workDocId = 0L;
		workDocId = workDocMap.get("id") == null ? null : Long.valueOf(workDocMap.get("id").toString());
		if(workDocId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}try{
			wmsWorkDocManager.confirmAllPortal(workDocId.toString(), UserHolder.getUser().getId());
		}catch(Exception e){
			throw new RfBusinessException(e.getMessage());
		}
		workDocMap.put(RfConstant.FORWARD_VALUE, "success");
		return workDocMap;
	}
	
	/**
	 * 是否确认整单确认
	 */
	public Map isPickAllCommit(Map workDocMap) throws RfBusinessException{
		workDocMap.put(RfConstant.FORWARD_CONFIRM_CODE, "是否确认整单作业确认");
		return workDocMap;
	}
	/**
	 * 明细拣货时返回上一页面
	 */
	public Map returnBackCommit(Map workDocMap) throws RfBusinessException{
		String workCodeSearch = workDocMap.get("workCodeSearch") == null?null : workDocMap.get("workCodeSearch").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> perResult = new HashMap<String, Object>();
		perResult.put("workCodeSearch", workCodeSearch);
		result.put(RfConstant.PERSISTENT_VALUE, perResult);//将持久map放入返回map中
		result.put(RfConstant.FORWARD_VALUE, "success");
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}
	/**
	 * 拣货确认
	 */
	public Map inputPickNumberCommit(Map workDocMap) {
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<String, Object>();
		result.putAll(workDocMap);
		String number=workDocMap.get("pickNumberInput") == null?null:workDocMap.get("pickNumberInput").toString();
		Double pickNumber=workDocMap.get("pickNumber") == null?null:Double.parseDouble(workDocMap.get("pickNumber").toString());
		//判断数字合法性
		Double qty=0d;
		if (null == number) {
			throw new RfBusinessException("this.number.can.not.be.null");
		}else{
			//判断数字是否正确
			try{
				qty=Double.parseDouble(number);
			}catch(Exception e){
				throw new RfBusinessException("moveNumber.is.error");
			}
			if(qty<=0){
				throw new RfBusinessException("moveNumber.too.low");
			}
			if(qty>pickNumber){
				throw new RfBusinessException("number.is.out.of.range");
			}
		}
		Long taskId =workDocMap.get("taskId") == null ? null : Long
					.valueOf(workDocMap.get("taskId").toString());
		WmsTask	task=commonDao.load(WmsTask.class,taskId);
		
		String taskHql="from WmsTask task where task.workDoc.id=:workDocId "
			+ " and task.fromLocation.id=:locId and task.status in (:status)"
			+ " and task.type=:type AND task.item.code =:itemCode";
		List<WmsTask> wmsTasks=commonDao.findByQuery(taskHql, 
			new String[]{"workDocId","locId","status","type","itemCode"}, 
			new Object[]{task.getWorkDoc().getId(),task.getFromLocation().getId(),
			Arrays.asList(WmsTaskStatus.ENABLED,WmsTaskStatus.IN_OPERATION),WmsTaskType.PICKING,task.getItem().getCode()});
		try{
			Double inPutQty = qty;
			for(WmsTask wmsTask : wmsTasks){
				Double planPickQty = wmsTask.getPlanQty()-wmsTask.getPickedQty();
				if(inPutQty<=0){
					break;
				}
				if(inPutQty <planPickQty){
					wmsWorkDocManager.singleWorkConfirm(wmsTask, wmsTask, inPutQty,null);
					inPutQty=0D;
				}else{
					wmsWorkDocManager.singleWorkConfirm(wmsTask, wmsTask, planPickQty,null);
					inPutQty-=planPickQty;
				}
			}
		    
		}catch(Exception e){
			throw new RfBusinessException(e.getMessage());
		}
		//判断该任务是否还有未完成的数量，有则返回本页面
		//未拣货数量
		Map<String, Object> qtyMap = new HashMap<String, Object>();
		qtyMap.put("task.id", task.getId());
		Double unPickNumber = pickNumber-qty;
		if(unPickNumber>0){
			result.put(RfConstant.FORWARD_VALUE, "successReturn");//返回当前页面
			//持久MAP
	    	Map<String, Object> perResult = new HashMap<String, Object>();
			perResult.putAll(workDocMap);
			perResult.put("taskId", task.getId());
			perResult.put("pickNumber", unPickNumber);
			perResult.put("pickNumberInput", unPickNumber);
	        result.put(RfConstant.PERSISTENT_VALUE, perResult);//将持久map放入返回map中
	        result.put(RfConstant.CLEAR_VALUE, "true");
			return result;
		}else{
			//判断该库位是否还有拣货任务，有则跳转至输入货品页面
			
			WmsWorkDoc workDoc=commonDao.load(WmsWorkDoc.class, task.getWorkDoc().getId());
			if (WmsWorkDocStatus.FINISH.equals(workDoc.getStatus())) {
				result.put(RfConstant.FORWARD_VALUE, "successOne");// 返回作业列表页面
				result.put(RfConstant.CLEAR_VALUE, "true");
				return result;
			} 
			if(task.getTaskGroup()!=null){
				WmsTaskGroup group=commonDao.load(WmsTaskGroup.class, task.getTaskGroup().getId());
				if (WmsTaskGroupStatus.FINISH.equals(group.getStatus())) {
					// 判断作业单未完成，任务组列完成状态，否则跳转任务组页面
						result.put(RfConstant.FORWARD_VALUE, "successTwo");// 返回任务组列表页面
						// 持久MAP
						Map<String, Object> perResult = new HashMap<String, Object>();
						perResult.put("workId", workDoc.getId());
						result.put(RfConstant.PERSISTENT_VALUE, perResult);// 将持久map放入返回map
						result.put(RfConstant.CLEAR_VALUE, "true");
						result.put(RfConstant.ENTITY_ID, task.getWorkDoc().getId());
						return result;
				}
				String hql="from WmsTask task where task.taskGroup.id=:groupId "
						+ " and task.fromLocation.id=:id and task.status in (:status)"
						+ " and task.type=:type ";
				List<WmsTask> tasks=commonDao.findByQuery(hql, 
						new String[]{"groupId","code","status","type"}, 
						new Object[]{group.getId(),task.getFromLocation().getId(),
						Arrays.asList(WmsTaskStatus.ENABLED,WmsTaskStatus.IN_OPERATION),WmsTaskType.PICKING});
				//相同库位还有其他货品未操作，跳转至输入货品页面
				if(tasks.size()>0){
					result.put(RfConstant.FORWARD_VALUE, "successToFive");//返回输入货品代码页面
					//持久MAP
			    	Map<String, Object> perResult = new HashMap<String, Object>();
			    	perResult.putAll(workDocMap);
					perResult.put("taskId", tasks.get(0).getId());
					perResult.put("pickItemInput", tasks.get(0).getId());
					perResult.put("pickNumberInput", tasks.get(0).getId());
			        result.put(RfConstant.PERSISTENT_VALUE, perResult);//将持久map放入返回map
					result.put(RfConstant.CLEAR_VALUE, "true");
					return result;
				}else {
					result.put(RfConstant.FORWARD_VALUE, "successThree");// 返回输入库位
					//该任务组存在未完成的库位
					tasks=commonDao.findByQuery("from WmsTask task where task.taskGroup.id=:groupId and task.status in (:status)"
							+ " and task.type=:type "
							+ "order by task.fromLocation.pickingSequence asc",
							new String[]{"groupId","status","type"},
							new Object[]{group.getId(),Arrays.asList(WmsTaskStatus.ENABLED,WmsTaskStatus.IN_OPERATION),WmsTaskType.PICKING});
					// 持久MAP
					Map<String, Object> perResult = new HashMap<String, Object>();
					perResult.put("taskId", tasks.get(0).getId());
					result.put(RfConstant.PERSISTENT_VALUE, perResult);// 将持久map放入返回map
					result.put(RfConstant.CLEAR_VALUE, "true");
					return result;
				}
			}else{
				String hql="from WmsTask task where task.workDoc.id=:workDocId "
						+ " and task.fromLocation.id=:locId and task.status in (:status)"
						+ " and task.type=:type ";
				List<WmsTask> tasks=commonDao.findByQuery(hql, 
						new String[]{"workDocId","locId","status","type"}, 
						new Object[]{workDoc.getId(),task.getFromLocation().getId(),
						Arrays.asList(WmsTaskStatus.ENABLED,WmsTaskStatus.IN_OPERATION),WmsTaskType.PICKING});
				if(tasks.size()>0){
					result.put(RfConstant.FORWARD_VALUE, "successToFive");//返回输入货品代码页面
					//持久MAP
			    	Map<String, Object> perResult = new HashMap<String, Object>();
			    	perResult.putAll(workDocMap);
					perResult.put("taskId", tasks.get(0).getId());
					perResult.put("pickItemInput", "");
					perResult.put("pickNumberInput", "");
			        result.put(RfConstant.PERSISTENT_VALUE, perResult);//将持久map放入返回map
					result.put(RfConstant.CLEAR_VALUE, "true");
					return result;
				}else{
					result.put(RfConstant.FORWARD_VALUE, "successThree");// 返回输入库位页面
					//该作业单存在未完成的库位
					tasks=commonDao.findByQuery("from WmsTask task where task.workDoc.id=:workDocId and task.status in (:status)"
							+ " and task.type=:type "
							+ "order by task.fromLocation.pickingSequence asc",
							new String[]{"workDocId","status","type"},
							new Object[]{workDoc.getId(),Arrays.asList(WmsTaskStatus.ENABLED,WmsTaskStatus.IN_OPERATION),WmsTaskType.PICKING});
					// 持久MAP
					Map<String, Object> perResult = new HashMap<String, Object>();
					perResult.put("taskId", tasks.get(0).getId());
					result.put(RfConstant.PERSISTENT_VALUE, perResult);// 将持久map放入返回map
					result.put(RfConstant.CLEAR_VALUE, "true");
					return result;
				}
			}
		}
	}
}
