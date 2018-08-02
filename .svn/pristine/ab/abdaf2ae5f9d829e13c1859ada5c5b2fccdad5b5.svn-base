package com.vtradex.wms.server.service.order.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.enumeration.WmsSapFactoryCodeEnum;
import com.vtradex.wms.server.model.component.LotInfo;
import com.vtradex.wms.server.model.entity.base.WmsFactoryWarehouse;
import com.vtradex.wms.server.model.entity.base.WmsFactoryXmlb;
import com.vtradex.wms.server.model.entity.base.WmsItemKeeper;
import com.vtradex.wms.server.model.entity.base.WmsShippingLotTruck;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.model.entity.item.WmsBillType;
import com.vtradex.wms.server.model.entity.item.WmsCompany;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsLotRule;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.order.WmsTransportOrderDetail;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrderStatus;
import com.vtradex.wms.server.model.entity.production.ReservedOrderDetailPtDetail;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrder;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderType;
import com.vtradex.wms.server.model.entity.receiving.WmsASN;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.entity.receiving.WmsASNStatus;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.enums.WmsAsnGenType;
import com.vtradex.wms.server.model.enums.WmsPickticketBillTypeCode;
import com.vtradex.wms.server.model.enums.WmsPickticketGenType;
import com.vtradex.wms.server.service.order.WmsReservedOrderManager;
import com.vtradex.wms.server.service.pickticket.WmsPickticketManager;
import com.vtradex.wms.server.service.production.ProductionOrderManager;
import com.vtradex.wms.server.service.production.WmsDeliveryOrderManager;
import com.vtradex.wms.server.service.receiving.WmsASNManager;
import com.vtradex.wms.server.service.receiving.WmsTclASNManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.PackageUtils;
import com.vtradex.wms.server.utils.StringHelper;

public class DefaultWmsReservedOrderManager extends DefaultBaseManager implements WmsReservedOrderManager{
	
	private ProductionOrderManager productionOrderManager;
	private WmsBussinessCodeManager wmsBussinessCodeManager;
	private WmsASNManager wmsASNManager;
	private WorkflowManager workflowManager;
	public DefaultWmsReservedOrderManager(ProductionOrderManager productionOrderManager,
			 WmsBussinessCodeManager wmsBussinessCodeManager,WmsASNManager wmsASNManager,
			 WorkflowManager workflowManager){
		this.productionOrderManager = productionOrderManager;
		this.wmsBussinessCodeManager = wmsBussinessCodeManager;
		this.wmsASNManager = wmsASNManager;
		this.workflowManager = workflowManager;
	}
	
	//保存
	public void saveReservedOrder(WmsReservedOrder reservedOrder){
		commonDao.store(reservedOrder);
	}	
	
	
	
	//保存明细
	public void saveOrderDetail(Long id,WmsReservedOrderDetail wmsReservedOrderDetail){
		if(wmsReservedOrderDetail.isNew()){
			WmsReservedOrder reservedOrder = commonDao.load(WmsReservedOrder.class, id);
			wmsReservedOrderDetail.setReservedOrder(reservedOrder);
			if(wmsReservedOrderDetail.getQuantity() == null){
				throw new BusinessException("需求数量不能为空");
			}
			if(wmsReservedOrderDetail.getQuantity() <= 0 ){
				throw new BusinessException("需求数量不能小于等于0");
			}
			if(wmsReservedOrderDetail.getThQty() == null){
				throw new BusinessException("提货数量不能为空");
			}
			if(wmsReservedOrderDetail.getThQty() <= 0  ){
				throw new BusinessException("提货数量不能小于等于0");
			}
			commonDao.store(wmsReservedOrderDetail);
		}else{
			if(wmsReservedOrderDetail.getQuantity() == null){
				throw new BusinessException("需求数量不能为空");
			}
			if(wmsReservedOrderDetail.getQuantity() <= 0 ){
				throw new BusinessException("需求数量不能小于等于0");
			}
			if(wmsReservedOrderDetail.getThQty() == null){
				throw new BusinessException("提货数量不能为空");
			}
			if(wmsReservedOrderDetail.getThQty() <= 0  ){
				throw new BusinessException("提货数量不能小于等于0");
			}
			commonDao.store(wmsReservedOrderDetail);
		}	
	}
	
	
	
	//删除明细
	public void deleteOrderDetail(WmsReservedOrderDetail wmsReservedOrderDetail){
		commonDao.delete(wmsReservedOrderDetail);
	}



	@Override
	public void activeReserveOrder(WmsReservedOrder reservedOrder) {
		
		if(null == reservedOrder.getYdlx() || "".equals(reservedOrder.getYdlx())) {
			return;
		}
		//生成收货单不在此处生成了，通过明细批量生成
//		if(reservedOrder.getYdlx().equals(WmsReservedOrderType.Z02) 
//				||reservedOrder.getYdlx().equals(WmsReservedOrderType.Z04)){//入库类型预留单 生成ASN
//			createAsnByReservedOrder(reservedOrder);
//			return;
//		}
		
//		WmsFactoryWarehouse fFactoryWarehouse = findWmsFactoryWarehouse(reservedOrder.getFactory().getId());
		/**创建拣货单*/
//		createPtBywasherFactory(reservedOrder,fFactoryWarehouse); 拣货单现在根据预留单明细来生成
	}
	
	public WmsFactoryWarehouse findWmsFactoryWarehouse(Long factoryId){
		String hql = "  FROM WmsFactoryWarehouse fw WHERE fw.factory.id =:factoryId "
						+ " AND fw.type =:type";
		return  (WmsFactoryWarehouse) this.commonDao.findByQueryUniqueResult(hql, new String[]{"factoryId","type"}, new Object[]{factoryId,WmsFactoryXmlb.BZ});
		
	}
	
	public List<WmsReservedOrder> findNoCreatReservedOrder(){
		List<WmsReservedOrder> list = commonDao.findByQuery("from WmsReservedOrder where status='OPEN'");
		return list;
	}
	
	/**添加预留单明细和拣货单明细对应关系*/
	private void storeProductionOrderDetailPtDetail(WmsReservedOrderDetail reservedOrderDetail,WmsPickTicketDetail pickTicketDetail,Double quantityBu){
		ReservedOrderDetailPtDetail odp = new ReservedOrderDetailPtDetail
						(reservedOrderDetail, pickTicketDetail, quantityBu, reservedOrderDetail.getUnit());
		
		this.commonDao.store(odp);
	}

	/**
	 * 根据预留明细生成收货单，预留单+保管员 分单
	 * @param reservedOrderDetails
	 * @param detailValues
	 */
	public void createAsnByRod(List<WmsReservedOrderDetail> reservedOrderDetails){
		WmsCompany company = (WmsCompany) commonDao.findByQueryUniqueResult("from WmsCompany where code='D'","","");
		List<WmsASN> asns = new ArrayList<WmsASN>();
		List<Object[]> detailValues = new ArrayList<Object[]>();
		int i = 0;
		for(WmsReservedOrderDetail wod : reservedOrderDetails){
			if(!StringHelper.isNullOrEmpty(wod.getDeleteFlag())){
				continue;
			}
			WmsASN asn = null;
			if(wod.getQuantity() <=0 ){
				continue;
			}
			i++;
			asn = this.genASN(asn, wod, detailValues, asns, company, i);
            /**创建ASN明细*/
    		createAsnByValues(detailValues,asn);
    		detailValues.clear();//清空list
    		wod.setIsCreateASN(Boolean.TRUE);
    		commonDao.store(wod);
		}
		for(WmsASN wmsASN : asns){
			workflowManager.doWorkflow(wmsASN, "wmsASNProcess.active");
	        workflowManager.doWorkflow(wmsASN, "wmsTclASNProcess.label");
		}
	}
	@Override
	public void createAsnByReservedOrder(WmsReservedOrder reservedOrder) {
		
		List<WmsReservedOrderDetail> details = (List<WmsReservedOrderDetail>) reservedOrder.getDetails();
		this.createAsnByRod(details);
		
	}
	
	@Override
	public WmsASN createAsnByValues(List<Object[]> detailValues,WmsASN asn){
		for(int i = 0;i < detailValues.size();i++){
			Object[] obj = detailValues.get(i);
			//0行号,1库存状态,2物料,3包装单位,4配送单明细,5数量,6货主,7供应商名称,8供应商编码,9供应商名称10预留明细ID
			WmsASNDetail asnDetail = EntityFactory.getEntity(WmsASNDetail.class);
			asnDetail.setLineNo((Integer)obj[0]);
            asnDetail.setInventoryStatus((String)obj[1]);
            WmsItem item = (WmsItem)obj[2];
            asnDetail.setItem(item);
            WmsPackageUnit unit = (WmsPackageUnit)obj[3];
            asnDetail.setPackageUnit(unit);
            asnDetail.setTransportOrderDetail((WmsTransportOrderDetail)obj[4]);
            asnDetail.setExpectedPackQty(PackageUtils.convertPackQuantity((Double)obj[5],unit));
            asnDetail.setUserField1(obj[10]+"");
            
            WmsLotRule lotRule = asnDetail.getItem().getLotRule();
            if (asnDetail.getLotInfo() != null) {
                asnDetail.getLotInfo().prepare(lotRule, asnDetail.getItem(), asn.getCode());
                asnDetail.getLotInfo().setExtendPropC12(item.getUserFieldD1().toString());//最小包装量
            }else{
                LotInfo lotInfo = new LotInfo();
                lotInfo.setExtendPropC8((String)obj[6]);
                lotInfo.setExtendPropC9((String)obj[7]);
                //工厂编码
                lotInfo.setExtendPropC10((String)obj[8]);
                //工厂名称
                lotInfo.setExtendPropC11((String)obj[9]);
                lotInfo.setExtendPropC18("1");//预留单生成ASN的物料  优先出库
                lotInfo.setExtendPropC12(item.getUserFieldD1().toString());//最小包装量
                asnDetail.setLotInfo(lotInfo);
            }
            asnDetail.setAsn(asn);
            wmsASNManager.addDetail(asn.getId(), asnDetail, asnDetail.getExpectedPackQty());
        }
        return asn;
	}

	@Override
	public void unActiveOrder(WmsReservedOrder reservedOrder) {
		for(WmsReservedOrderDetail detail : reservedOrder.getDetails()){
			if(detail.getAllocatedQuantityBu() > 0 || detail.getPickedQuantityBu() > 0){
				throw new BusinessException("此预留单已经分配,无法失效!");
			}
		}
		
		if(reservedOrder.getYdlx().equals(WmsReservedOrderType.Z02) 
				||reservedOrder.getYdlx().equals(WmsReservedOrderType.Z04)){//入库类型预留单 生成ASN
			List<WmsASN> asns = commonDao.findByQuery("FROM WmsASN asn WHERE asn.customerBill=:customerBill", 
	                "customerBill",reservedOrder.getCode());
	        
	        if (asns!=null && asns.size()>0) {
	            for (WmsASN asn : asns) {
	                if (WmsASNStatus.RECEIVING.equals(asn.getStatus()) || WmsASNStatus.RECEIVED.equals(asn.getStatus())) {
	                    throw new BusinessException("该配货单存在已收货ASN不允许失效!");
	                }
	                
	            }
	        }
	        commonDao.deleteAll(asns);
		}
	}

	/**
	 * 现在逻辑改为根据预留单号+工厂来生成拣货单	
	 * key = 预留号, value = 预留单明细list 
	 * 一个key生成一个拣货单,value生成拣货单明细
	 * @author fs 
	 */
	@Override
	public List<WmsPickTicket> createPickByReserveOrderDetail(List<WmsReservedOrderDetail> details) {
		//同一个预留单下，明细的工厂可能不一样，所以生成拣货单时要按单号+明细工厂来生成
		List<WmsPickTicket> picks = new ArrayList<WmsPickTicket>();//返回拣货单list
		Map<String,List<WmsReservedOrderDetail>> refirgeratorFactory = new HashMap<String, List<WmsReservedOrderDetail>>();//冰箱1000工厂数据
		Map<String,List<WmsReservedOrderDetail>> refirgerator2Factory = new HashMap<String, List<WmsReservedOrderDetail>>();//冰箱1100工厂数据
		Map<String,List<WmsReservedOrderDetail>> washerFactory = new HashMap<String, List<WmsReservedOrderDetail>>();//洗衣机2000工厂数据
		Map<String,List<WmsReservedOrderDetail>> washer2Factory = new HashMap<String, List<WmsReservedOrderDetail>>();//洗衣机2100工厂数据
		for (WmsReservedOrderDetail detail : details) {
			if(!StringHelper.isNullOrEmpty(detail.getDeleteFlag())){
				continue;
			}
			WmsReservedOrder reservedOrder = commonDao.load(WmsReservedOrder.class, detail.getReservedOrder().getId());
			String code = reservedOrder.getCode();//工单号
			if(!reservedOrder.getStatus().equals(ProductionOrderStatus.ACTIVE)){
				throw new BusinessException("预留单["+reservedOrder.getCode()+"]未生效,无法生成拣配单!");
			}
			detail = commonDao.load(WmsReservedOrderDetail.class, detail.getId());
			String ydlx = reservedOrder.getYdlx();//移动类型
			
			if(!StringHelper.in(ydlx, new String[]{WmsReservedOrderType.Z01,WmsReservedOrderType.Z03,WmsReservedOrderType.Z311})) {
				continue;
			}
			if(detail.getQuantity() <= 0 ){
				continue;
			}
			if (StringHelper.in(detail.getFactory().getCode(),new String[]{WmsSapFactoryCodeEnum.BX_NX})) {
				List<WmsReservedOrderDetail> bxList = null;
				if(refirgeratorFactory.get(code) == null){
					bxList = new ArrayList<WmsReservedOrderDetail>();
					refirgeratorFactory.put(code, bxList);
				}
				bxList = refirgeratorFactory.get(code);
				bxList.add(detail);
				refirgeratorFactory.put(code, bxList);//一个key(预留号)生成一个拣货单
			}
			else if (StringHelper.in(detail.getFactory().getCode(),new String[]{WmsSapFactoryCodeEnum.XYJ_NX})) {
				List<WmsReservedOrderDetail> xyjList = null;
				if(washerFactory.get(code) == null){
					xyjList = new ArrayList<WmsReservedOrderDetail>();
					washerFactory.put(code, xyjList);
				}
				xyjList = washerFactory.get(code);
				xyjList.add(detail);
				washerFactory.put(code, xyjList);//一个key(预留号)生成一个拣货单
			}else if(WmsSapFactoryCodeEnum.BX_WX.equals(detail.getFactory().getCode())){
				List<WmsReservedOrderDetail> bx2List = null;
				if(refirgerator2Factory.get(code) == null){
					bx2List = new ArrayList<WmsReservedOrderDetail>();
					refirgerator2Factory.put(code, bx2List);
				}
				bx2List = refirgerator2Factory.get(code);
				bx2List.add(detail);
				refirgerator2Factory.put(code, bx2List);//一个key(预留号)生成一个拣货单
			}else if(WmsSapFactoryCodeEnum.XYJ_WX.equals(detail.getFactory().getCode())){
				List<WmsReservedOrderDetail> xyj2List = null;
				if(washer2Factory.get(code) == null){
					xyj2List = new ArrayList<WmsReservedOrderDetail>();
					washer2Factory.put(code, xyj2List);
				}
				xyj2List = washer2Factory.get(code);
				xyj2List.add(detail);
				washer2Factory.put(code, xyj2List);//一个key(预留号)生成一个拣货单
			} 
			else {
				throw new BusinessException("工厂编码"+detail.getFactory().getCode()+"错误");
			}
		}
		if (!washerFactory.isEmpty()) {
			createPtBywasherFactory(washerFactory,picks);
		}
		if (!refirgeratorFactory.isEmpty()) {
			createPtBywasherFactory(refirgeratorFactory,picks);
		}
		if (!washer2Factory.isEmpty()) {
			createPtBywasherFactory(washer2Factory,picks);
		}
		if (!refirgerator2Factory.isEmpty()) {
			createPtBywasherFactory(refirgerator2Factory,picks);
		}
		
		return picks;
	}
	/**洗衣机工厂*/
	public void createPtBywasherFactory(Map<String,List<WmsReservedOrderDetail>> map,List<WmsPickTicket> picks){
		Set<String> key = map.keySet();//工单号
		for(String k : key){//一个key一张拣货单
			List<WmsReservedOrderDetail> detailList = map.get(k);
		
			WmsFactoryWarehouse fFactoryWarehouse = findWmsFactoryWarehouse(detailList.get(0).getFactory().getId());
			/**创建拣货单*/
			WmsPickTicket pickTicket = productionOrderManager.createWmsPickTicketByProductionOrder(fFactoryWarehouse.getWarehouse(),
					detailList.get(0).getReservedOrder().getJzrq(),WmsPickticketBillTypeCode.YLCKD);
			
			pickTicket.setUserField1(detailList.get(0).getFactory().getCode()); //工厂编码
			pickTicket.setUserField4(detailList.get(0).getReservedOrder().getCbzx());//成本中心
			if(StringHelper.isNullOrEmpty(detailList.get(0).getReservedOrder().getReceiveWorker())){
				pickTicket.setUserField6(detailList.get(0).getFactory().getCode()+"_"+detailList.get(0).getReservedOrder().getYdlx());//收货人
			}else{
				pickTicket.setUserField6(detailList.get(0).getReservedOrder().getReceiveWorker());//收货人
			}
			//单号编码规则
			String groupNo = wmsBussinessCodeManager.generateCodeByRule(pickTicket.getWarehouse(),"FZ");
			
			for (WmsReservedOrderDetail reservedOrderDetail : detailList) {
				Double qty = reservedOrderDetail.getQuantity()-StringHelper.replaceNullToZero(reservedOrderDetail.getAllocatedQuantityBu());
				WmsPickTicketDetail pickTicketDetail = productionOrderManager.creatWmsPickTicketDetail
				(pickTicket, reservedOrderDetail.getItem(), reservedOrderDetail.getUnit(), qty);
//				pickTicketDetail.setExpectedPackQty(reservedOrderDetail.getQuantity());//计划包装数
				storeProductionOrderDetailPtDetail(reservedOrderDetail, pickTicketDetail,qty);
				
				reservedOrderDetail.setIsCreatePick(Boolean.TRUE);//生成交货单标识
				reservedOrderDetail.setGroupNo(groupNo);//分组号
				commonDao.store(reservedOrderDetail);
			}
			pickTicket.setRelatedBill1(groupNo);
			this.commonDao.store(pickTicket);
			picks.add(pickTicket);
		}	
	}

	@Override
	public void cancelPick(WmsReservedOrderDetail detail) {
		
		List<Long> middleId = new ArrayList<Long>();//关系表id
		List<Long> pickDetId = new ArrayList<Long>();//拣货单明细id
		List<Long> pickIds = new ArrayList<Long>();;//拣货单id
		
		String groupNo = detail.getGroupNo();//分组号
		String hql = "select id from WmsReservedOrderDetail where groupNo=:groupNo ";
		List<Long> detailIds = commonDao.findByQuery(hql+" and allocatedQuantityBu>0","groupNo",groupNo);
		if(detailIds.size() > 0){
			throw new BusinessException("分组号["+groupNo+"]下面有部分预留单明细已经分配,无法取消拣配");
		}
		detailIds = commonDao.findByQuery(hql,"groupNo",groupNo);
		if(detailIds.size() <= 0){//如果用户选中多条相同分组号的明细,后面的分组号就不会找到数据了
			return;
		}
		hql = "select r.id,r.pickticketDetail.pickTicket.id,r.pickticketDetail.id "
				+ "from ReservedOrderDetailPtDetail r where r.reservedOrderDetail.id in (:ids)";
		List<Object[]> list = commonDao.findByQuery(hql,"ids",detailIds);
		for(Object[] obj : list){
			middleId.add((Long)obj[0]);
			pickDetId.add((Long)obj[2]);
			if(!pickIds.contains(obj[1])){
				pickIds.add((Long)obj[1]);
			}
		}
		hql = "from WmsPickTicket p where allocateQty>0 and p.id in (:ids)";
		List<WmsPickTicket> pickTickets = commonDao.findByQuery(hql,"ids",pickIds);//已作业的拣货单
		if(pickTickets.size() > 0){
			throw new BusinessException("拣货单已经分配,预留单无法取消拣配");
		}
		hql = "from WmsPickTicket p where allocateQty=0 and pickQty=0 and p.id in (:ids)";
		pickTickets = commonDao.findByQuery(hql,"ids",pickIds);//打开的拣货单
		
		//删除预留单明细和拣货单明细对应关系表的数据
		hql = "delete from ReservedOrderDetailPtDetail w where w.id in (:ids)";
		commonDao.executeByHql(hql, "ids", middleId);
		
		WmsDeliveryOrderManager deOrderManager = (WmsDeliveryOrderManager) applicationContext.getBean("wmsDeliveryOrderManager");
		//删除拣货单明细以及批次信息
		deOrderManager.deleteRelatePickData(pickDetId,null, "预留单");
		
		//删除拣货单
		commonDao.deleteAll(pickTickets);
		
		hql = "select w.id from WmsPickTicket w where w.userField3=:userField3 "
				+ "and w.relatedBill1=:relatedBill1 and w.allocateQty=0";
		
		List<Long> pIds = commonDao.findByQuery(hql,
				new String[]{"userField3","relatedBill1"},
				new Object[]{WmsPickticketGenType.YLCKD,groupNo});
		if(pIds.size() > 0){
			
			hql = "select id from WmsPickTicketDetail w where w.pickTicket.id in (:ids)";
			List<Long> pickDetatilIds = commonDao.findByQuery(hql, "ids", pIds);
			
			if(pickDetatilIds.size() > 0){
				//删除拣货单明细以及批次信息
				deOrderManager.deleteRelatePickData(pickDetatilIds,pIds, "预留单");
			}
		}
		
		//将预留单明细的分组号清空,并将是否生成拣货单标识设为否
		for(Long id : detailIds){
			WmsReservedOrderDetail r = commonDao.load(WmsReservedOrderDetail.class,id);
			r.setGroupNo(null);
			r.setIsCreatePick(Boolean.FALSE);
			commonDao.store(r);
		}
	}

	@Override
	public void createPickNoTransaction(List<WmsReservedOrderDetail> details) {
		
		WmsReservedOrderManager reservedOrderManager = (WmsReservedOrderManager) applicationContext.getBean("wmsReservedOrderManager");
		/**创建拣货单---此方法要有事务*/
		List<WmsPickTicket> picks = reservedOrderManager.createPickByReserveOrderDetail(details);
		
		/**自动分配--无事务*/
		WmsPickticketManager wmsPickticketManager = (WmsPickticketManager) applicationContext.getBean("wmsPickticketManager");
    	for(WmsPickTicket p : picks){
    		try {
				//自动分配
				wmsPickticketManager.autoAllocate(p);
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    		
		}
	}
	/**
	 * 取消生成的收货单
	 */
	public void cancelASN(WmsReservedOrderDetail detail){
		if(detail.getAllocatedQuantityBu() > 0 || detail.getPickedQuantityBu() > 0){
			throw new BusinessException("此预留单明细已经分配,无法取消!");
		}
		String hql = "from WmsASNDetail detail where detail.userField1 =:userField1 ";
		List<WmsASNDetail> details = commonDao.findByQuery(hql, "userField1", detail.getId().toString());
		if(details.isEmpty()){
			throw new BusinessException("根据预留明细未找到对应生成的收货单明细");
		}
		for(WmsASNDetail asnDetail : details){
			WmsASN asn = commonDao.load(WmsASN.class, asnDetail.getAsn().getId());
			if(asn.getReceiveQty()>0){
				throw new BusinessException("收货单:"+asn.getCode()+"已收货,请先把收货单取消收货");
			}
			asn.removeDetail(asnDetail);
			commonDao.delete(asnDetail);
			if(asn.getDetails().isEmpty()){
				commonDao.delete(asn);
			}
		}
		detail.setIsCreateASN(Boolean.FALSE);
		commonDao.store(detail);
	}
	/**
	 * 预留出库取消，生成相应的入库单同时把预留明细的相应数量扣减
	 */
	public void cancelShip(WmsReservedOrderDetail detail,Double cancelQty){
		//生成ASN
		WmsASN asn = EntityFactory.getEntity(WmsASN.class);
		WmsReservedOrder reservedOrder = commonDao.load(WmsReservedOrder.class, detail.getReservedOrder().getId());
		String hql = "from WmsShippingLotTruck s where s.subRelateId=:subRelateId ";
		List<WmsShippingLotTruck> trucks = commonDao.findByQuery(hql, "subRelateId", detail.getId());
		WmsBillType billType = (WmsBillType) commonDao.findByQueryUniqueResult("FROM WmsBillType WHERE code=:code", 
	                "code", WmsAsnGenType.YLTLD);
        if (billType==null) {
            throw new BusinessException("未找到编码为【"+WmsAsnGenType.YLTLD+"】的单据类型！");
        }
		if(!trucks.isEmpty()){
			WmsShippingLotTruck t = trucks.get(0);
			asn.setWarehouse(t.getWarehouse());
			asn.setCompany(t.getCompany());
	        asn.setBillType(billType);
	        String code = wmsBussinessCodeManager.generateCodeByRule(t.getWarehouse(), asn.getBillType().getCode()); 
	        asn.setCode(code);
	        asn.setOrderDate(reservedOrder.getJzrq());
	        asn.setCustomerBill(reservedOrder.getCode());
	        hql = "from WmsSupplier where code=:code";
	        WmsSupplier supplier = (WmsSupplier) commonDao.findByQueryUniqueResult(hql,"code",t.getLotInfo().getSupplierCode());
	        asn.setFromCode( supplier.getCode());
	        asn.setFromName(supplier.getName());
	        asn.setSupplier(supplier);
	        asn.setStatus(WmsASNStatus.OPEN);
	        ThornUser bgy = this.getBgy(t.getItem().getCode(), t.getWarehouse().getCode(), detail.getFactory().getCode());
	        asn.setKeeper(bgy);
	        asn.setUserField7(reservedOrder.getFactory().getCode());
	        asn.setUserField5(t.getLotInfo().getExtendPropC8());// 项目类别
	        List<WmsLocation> locs = commonDao.findByQuery("FROM WmsLocation receiveLocation "
	                + "WHERE receiveLocation.status = 'ENABLED' "
	                + "AND receiveLocation.type = 'RECEIVE' "
	                + "AND receiveLocation.warehouse.id = :baseOrganId", 
	                "baseOrganId", asn.getWarehouse().getId());
	        
	        if (locs!=null && locs.size()>0) {
	            asn.setReceiveLocation(locs.get(0));
	        } else {
	            throw new BusinessException("当前仓库未维护收货库位!");
	        }
	        commonDao.store(asn);
	        WmsASNDetail asnDetail = EntityFactory.getEntity(WmsASNDetail.class);
			asnDetail.setLineNo(10);
            asnDetail.setInventoryStatus(t.getInventoryStatus());
            asnDetail.setItem(t.getItem());
            asnDetail.setPackageUnit(t.getPackageUnit());
            asnDetail.setTransportOrderDetail(null);
            asnDetail.setExpectedQty(cancelQty);
            asnDetail.setExpectedPackQty(PackageUtils.convertPackQuantity(cancelQty,t.getPackageUnit()));
            asnDetail.setUserField1(detail.getId()+"");
            asnDetail.setLotInfo(t.getLotInfo());
            asnDetail.setAsn(asn);
            wmsASNManager.addDetail(asn.getId(), asnDetail, asnDetail.getExpectedPackQty());
		}else{
			WmsCompany company = (WmsCompany) commonDao.findByQueryUniqueResult("from WmsCompany where code='D'","","");
			List<WmsASN> asns = new ArrayList<WmsASN>();
			List<Object[]> detailValues = new ArrayList<Object[]>();
			asn = this.genASN(asn, detail, detailValues, asns, company, 1);
			asn.setBillType(billType);
			commonDao.store(asn);
		}
		workflowManager.doWorkflow(asn, "wmsASNProcess.active");
        //自动收货
        WmsTclASNManager wmsTclAsnManager = (WmsTclASNManager) applicationContext.getBean("wmsTclASNManager");
        wmsTclAsnManager.receiveAll(asn.getId(),0L);
        //扣减预留明细的相应数量
		detail.subAllocatedQuantityBu(cancelQty);
		detail.subPickedQuantityBu(cancelQty);
		detail.setShippedQuantityBu(DoubleUtils.sub(detail.getShippedQuantityBu(), cancelQty));
		detail.setIsCreatePick(Boolean.FALSE);
		detail.setGroupNo(null);
		commonDao.store(detail);
		//删除预留明细与拣货单明细对应关系
		hql = "delete from ReservedOrderDetailPtDetail p where p.reservedOrderDetail.id =:reservedOrderDetailId ";
		commonDao.executeByHql(hql, "reservedOrderDetailId", detail.getId());
	}
	/**
	 * 获取保管员
	 * @param itemCode
	 * @param warehouseCode
	 * @param factoryCode
	 * @return
	 */
	private ThornUser getBgy(String itemCode,String warehouseCode,String factoryCode){
		WmsItemKeeper itemKeeper = null;
        String baseWmsItemKeeperHql = "FROM WmsItemKeeper WHERE item.code=:itemCode AND warehouse.code=:warehouseCode";
        List<WmsItemKeeper> wmsItemKeepers = commonDao.findByQuery(baseWmsItemKeeperHql + " AND factory.code=:factoryCode", 
                new String[]{"itemCode", "warehouseCode", "factoryCode"}, 
                new Object[]{itemCode, warehouseCode, factoryCode});
        if (wmsItemKeepers!=null && wmsItemKeepers.size()>0) {
            itemKeeper = wmsItemKeepers.get(0);
        } else {
            wmsItemKeepers = commonDao.findByQuery(baseWmsItemKeeperHql + " AND factory IS NULL", 
                    new String[]{"itemCode", "warehouseCode"},
                    new Object[]{itemCode, warehouseCode});
            if (wmsItemKeepers!=null && wmsItemKeepers.size()>0) {
                itemKeeper = wmsItemKeepers.get(0);
            }
        }
        if(itemKeeper != null) {
      	 return itemKeeper.getKeeper();
        }
        return null;
	}
	
	private WmsASN genASN(WmsASN asn ,WmsReservedOrderDetail wod,List<Object[]> detailValues,List<WmsASN> asns,WmsCompany company,int i){
		WmsReservedOrder reservedOrder = commonDao.load(WmsReservedOrder.class, wod.getReservedOrder().getId());
		WmsFactoryWarehouse fFactoryWarehouse = findWmsFactoryWarehouse(reservedOrder.getFactory().getId());
        ThornUser bgy = this.getBgy(wod.getItem().getCode(), fFactoryWarehouse.getWarehouse().getCode(), wod.getFactory().getCode());
        String bgyid="0";
        if(bgy != null) {
      	  bgyid = bgy.getId()+"";
        }
        bgy =  commonDao.load(ThornUser.class, Long.valueOf(bgyid)); //可能为空  保管员
        //行号,库存状态,物料,包装单位,配送单明细,数量,货主,供应商名称,供应商编码,供应商名称,预留明细ID
		Object[] obj = new Object[]{i*10,"合格",wod.getItem(),
				wod.getUnit(),null,wod.getQuantity(),null,null,
				wod.getFactory().getCode(),wod.getFactory().getName(),wod.getId()};
		detailValues.add(obj);
		WmsSupplier supplier = (WmsSupplier) commonDao.
				findByQueryUniqueResult("from WmsSupplier where code='XN'","","");
		if(supplier == null){
			throw new BusinessException("系统未维护虚拟供应商,无法生成ASN");
		}
		//因为SAP同一个预留单下明细工厂可能不一样，所以分单要加上明细工厂
		String hql = "FROM WmsASN asn WHERE asn.customerBill=:customerBill AND asn.status=:status " +
				"AND asn.userField7=:factoryCode ";
		String bgyHql = " AND asn.keeper.id =:keeperId";
		String orHql = " AND asn.keeper is null ";
		List<WmsASN> wmsASNs = new ArrayList<WmsASN>();
		if(bgy==null){
			wmsASNs = commonDao.findByQuery(hql+orHql, new String[]{"customerBill","status","factoryCode"}, 
					new Object[]{reservedOrder.getCode(),WmsASNStatus.OPEN,wod.getFactory().getCode()});
		}else{
			wmsASNs = commonDao.findByQuery(hql+bgyHql, new String[]{"customerBill","status","factoryCode","keeperId"}, 
					new Object[]{reservedOrder.getCode(),WmsASNStatus.OPEN,wod.getFactory().getCode(),bgy.getId()});
		}
		if(wmsASNs.isEmpty()){
			asn = EntityFactory.getEntity(WmsASN.class);
		}else{
			asn = wmsASNs.get(0);
		}
		if(asn.isNew()){
	        asn.setCompany(company);
	        WmsBillType billType = (WmsBillType) commonDao.findByQueryUniqueResult("FROM WmsBillType WHERE code=:code", 
	                "code", WmsAsnGenType.YLRKD);
	        if (billType==null) {
	            throw new BusinessException("未找到编码为【"+WmsAsnGenType.YLRKD+"】的单据类型！");
	        }
	        asn.setBillType(billType);
	        asn.setWarehouse(fFactoryWarehouse.getWarehouse());
	        String code = wmsBussinessCodeManager.generateCodeByRule(fFactoryWarehouse.getWarehouse(), asn.getBillType().getCode()); 
	        asn.setCode(code);
	        asn.setOrderDate(reservedOrder.getJzrq());
	        asn.setCustomerBill(reservedOrder.getCode());
	        asn.setFromCode( supplier.getCode());
	        asn.setFromName(supplier.getName());
	        asn.setSupplier(supplier);
	        asn.setStatus(WmsASNStatus.OPEN);
	        asn.setKeeper(bgy);
	        asn.setUserField7(wod.getFactory().getCode());
	        asn.setUserField5(WmsFactoryXmlb.BZ);// 项目类别
	        List<WmsLocation> locs = commonDao.findByQuery("FROM WmsLocation receiveLocation "
	                + "WHERE receiveLocation.status = 'ENABLED' "
	                + "AND receiveLocation.type = 'RECEIVE' "
	                + "AND receiveLocation.warehouse.id = :baseOrganId", 
	                "baseOrganId", asn.getWarehouse().getId());
	        
	        if (locs!=null && locs.size()>0) {
	            asn.setReceiveLocation(locs.get(0));
	        } else {
	            throw new BusinessException("当前仓库未维护收货库位!");
	        }
	        commonDao.store(asn);
	        asns.add(asn);
		}
		return asn;
	}
	
}
