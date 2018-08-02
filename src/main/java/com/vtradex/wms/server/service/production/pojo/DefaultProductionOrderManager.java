package com.vtradex.wms.server.service.production.pojo;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.wms.server.enumeration.WarehouseEnumeration;
import com.vtradex.wms.server.enumeration.WmsSapFactoryCodeEnum;
import com.vtradex.wms.server.model.entity.base.Wms2SapInterfaceLogType;
import com.vtradex.wms.server.model.entity.base.WmsFactoryWarehouse;
import com.vtradex.wms.server.model.entity.base.WmsFactoryXmlb;
import com.vtradex.wms.server.model.entity.base.WmsProductionOrderDetailExtend;
import com.vtradex.wms.server.model.entity.base.WmsSapFactory;
import com.vtradex.wms.server.model.entity.item.WmsBillType;
import com.vtradex.wms.server.model.entity.item.WmsCompany;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemJITAtt;
import com.vtradex.wms.server.model.entity.item.WmsItemUnPackingAtt;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.item.WmsPickType;
import com.vtradex.wms.server.model.entity.order.ProductionOrderMeetInfoStatus;
import com.vtradex.wms.server.model.entity.order.WmsProductionOrderMeetInfo;
import com.vtradex.wms.server.model.entity.order.WmsProductionOrderMeetInfoType;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetailRequire;
import com.vtradex.wms.server.model.entity.production.ProductionOrder;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetailPtDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrderStatus;
import com.vtradex.wms.server.model.entity.production.WmsPickingProductionOrder;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.enums.BaseStatus;
import com.vtradex.wms.server.model.enums.WmsLocationType;
import com.vtradex.wms.server.model.enums.WmsLotCategoryType;
import com.vtradex.wms.server.model.enums.WmsPickTicketStatus;
import com.vtradex.wms.server.model.enums.WmsPickticketBillTypeCode;
import com.vtradex.wms.server.model.enums.WmsPickticketGenType;
import com.vtradex.wms.server.model.enums.WmsSOQueryRequireType;
import com.vtradex.wms.server.service.interf.InterfaceLogManager;
import com.vtradex.wms.server.service.item.TclMessageManager;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogTaskType;
import com.vtradex.wms.server.service.pickticket.WmsPickticketManager;
import com.vtradex.wms.server.service.production.ProductionOrderManager;
import com.vtradex.wms.server.service.production.WmsDeliveryOrderManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.utils.DateUtil;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.webservice.model.SapInterfaceType;
import com.vtradex.wms.webservice.sap.base.SapCommonCallback;
import com.vtradex.wms.webservice.sap.base.SapCommonCallbackDetail;
import com.vtradex.wms.webservice.utils.CommonHelper;
import com.vtradex.wms.webservice.utils.ExcelHelper;
import com.vtradex.wms.webservice.utils.WebServiceHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;

public class DefaultProductionOrderManager extends DefaultBaseManager implements ProductionOrderManager {
	
	private WmsBussinessCodeManager wmsBussinessCodeManager;
	private WorkflowManager workflowManager;
	
	public DefaultProductionOrderManager(WmsBussinessCodeManager wmsBussinessCodeManager,
			WorkflowManager workflowManager){
		this.wmsBussinessCodeManager = wmsBussinessCodeManager;
		this.workflowManager = workflowManager;
	}
	

    @Override
    public void importPickingOrder(Map<String, String> rowDataMap) {
    	
        String code = rowDataMap.get("生产订单号");
        code = CommonHelper.addCharBeforeStr(code, 12, "0");
        ProductionOrder po = null;
        try {
            po = (ProductionOrder) commonDao.findByQueryUniqueResult("FROM ProductionOrder po WHERE 1=1 "
                    + "AND po.code=:code", "code", code);
        } catch (Exception ex1) {
            ex1.printStackTrace();
            throw new BusinessException(ex1.getMessage());
        }
        if(po==null) {
        	 throw new BusinessException("未找到工单【"+code+"】");
        }
        po.setCanAllocate(Boolean.TRUE);
        
        commonDao.store(po);
    }

    @Override
    public void importOrderHandleTime(Map<String, String> rowDataMap) {
        String code = rowDataMap.get("生产订单号");
        code = CommonHelper.addCharBeforeStr(code, 12, "0");
        String startHour = rowDataMap.get("开始小时");
        String endHour = rowDataMap.get("结束小时");
        
        ProductionOrder po = null;
        try {
            po = (ProductionOrder) commonDao.findByQueryUniqueResult("FROM ProductionOrder po WHERE 1=1 "
                    + "AND po.code=:code", "code", code);
        } catch (Exception ex1) {
            ex1.printStackTrace();
            throw new BusinessException(ex1);
        }
        if(po==null) {
       	 throw new BusinessException("未找到工单【"+code+"】");
       }
        
        Integer startHourInt = null;
        Integer endHourInt = null;
        
        try {
            startHourInt = Integer.parseInt(startHour);
            endHourInt = Integer.parseInt(endHour);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            throw new BusinessException("开始小时或结束小时必须为正整数");
        }
        
        if (po.getBeginDate()==null || po.getEndDate()==null) {
            throw new BusinessException("工单开始日期或结束日期未填");
        }
        
        if (endHourInt>startHourInt && endHourInt<=24) {
            po.setBeginHour(startHour);
            po.setEndHour(endHour);
            
            Calendar calendar = Calendar.getInstance();
           
            calendar.setTime( DateUtil.getDate(DateUtil.format(po.getBeginDate(), "yyyy-MM-dd"), "yyyy-MM-dd"));
            calendar.add(Calendar.HOUR_OF_DAY, startHourInt);
            po.setBeginTime(calendar.getTime());
            
            calendar.setTime( DateUtil.getDate(DateUtil.format(po.getEndDate(), "yyyy-MM-dd"), "yyyy-MM-dd"));
            calendar.add(Calendar.HOUR_OF_DAY, endHourInt);
            po.setEndTime(calendar.getTime());
            
            commonDao.store(po);
        } else {
            throw new BusinessException("工单结束小时应大于开始小时,并且小时数应小于24!");
        }
        
    }

	@Override
	public void addDetail(Long id,ProductionOrderDetail productionOrderDetail) {
		ProductionOrder productionOrder =commonDao.load(ProductionOrder.class,id);
		if (!productionOrder.getStatus().equals(ProductionOrderStatus.OPEN)) {
			throw new BusinessException("productionOrder.status.error");
		}
		if (productionOrderDetail.isNew()) {
			productionOrderDetail.setProductionOrder(productionOrder);
			productionOrder.addDetail(productionOrderDetail);
		} else {
			productionOrderDetail = this.commonDao.load(ProductionOrderDetail.class, productionOrderDetail.getId());
		}
		
		// 预期收货数量计算(convertFigure为1表示是基本包装，基本包装只能有1个，不为1表示是件装)
		/*WmsPackageUnit unit = commonDao.load(WmsPackageUnit.class, productionOrderDetail.getPackageUnit().getId());
		if (productionOrderDetail.getPackageUnit().getConvertFigure().intValue() == 1) {
			productionOrderDetail.setPlanQuantityBu(PackageUtils.convertBUQuantity(planQuantityBu, unit));
			productionOrderDetail.setPlanQuantityBu(planQuantityBu);
		} else {
			productionOrderDetail.setExpectedPackQty(planQuantityBu);
			productionOrderDetail.setExpectedQty(PackageUtils.convertBUQuantity(expectedPackQty, unit));
		}*/
		//productionOrder.refreshQtyBU();
		this.commonDao.store(productionOrder);	
	}
	/**
	 * WmsFactoryWarehouse
	 * @Description 洗衣机工厂根据生产订单生成拣配单，但是如果是不可拆包物料，则根据线体生成拣货单。
	 * 冰箱工厂根据生产线生成拣配单。
	 * 返回生成的拣货单。
	 */
	public List<WmsPickTicket> creatPickTicketByProductionOrder(List<ProductionOrder> productionOrderList){
		List<WmsPickTicket> picks = new ArrayList<WmsPickTicket>();
		if (productionOrderList.isEmpty()) {
			return picks;
//			throw new BusinessException("creat.pt.by.productionPrder.canAllocate.beCreatPt.not.found");
		}
		
		List<ProductionOrder> bxProductionOrder = new ArrayList<ProductionOrder>();//冰箱数据
		List<ProductionOrder> xyjProductionOrder = new ArrayList<ProductionOrder>();//洗衣机数据
		
		List<ProductionOrder> xyjProductionOrder_bkcb = new ArrayList<ProductionOrder>();//洗衣机数据 不可拆包
		
		for (ProductionOrder productionOrder : productionOrderList) {
			if(ProductionOrderStatus.ACTIVE.equals(productionOrder.getStatus())){
				WmsFactoryWarehouse fFactoryWarehouse = findWmsFactoryWarehouse(productionOrder.getFactory().getId());
				if (null == fFactoryWarehouse) {
					throw new BusinessException("factory.warehouse.not.found.by.factory", new String[]{productionOrder.getFactory().getName()});
				}
				if (fFactoryWarehouse.getWarehouse().getCode().equals(WarehouseEnumeration.BX)) {
					bxProductionOrder.add(productionOrder);
				}else if (fFactoryWarehouse.getWarehouse().getCode().equals(WarehouseEnumeration.XYJ)) {
//					xyjProductionOrder.add(productionOrder);
					Boolean haveBkcb=false;//包含不可拆包物料
					Boolean havaKcb=false;//包含可拆包物料
					for(ProductionOrderDetail d : productionOrder.getDetails()) {
						if(!StringHelper.isNullOrEmpty(d.getItem().getUserFieldV3()) 
								&& WmsItemUnPackingAtt.WAREHOUSE_UNPACKING.equals(d.getItem().getUserFieldV3())) {
							haveBkcb = true;
						}
						else {
							havaKcb = true;
						}
					}
					if(haveBkcb) {
						xyjProductionOrder_bkcb.add(productionOrder);
					}
					if(havaKcb) {
						xyjProductionOrder.add(productionOrder);
					}
				} else {
					throw new BusinessException("仓库编码错误");
					
				}
			}
		}
		
		if (!xyjProductionOrder.isEmpty()) { //洗衣机
			dealDlanQuantityBu(xyjProductionOrder);//洗衣机处理后处理的处理。
			createPtByXyj(xyjProductionOrder,picks);
		}
		if (!bxProductionOrder.isEmpty()) {//冰箱
			createPtByBx(bxProductionOrder,picks);
		}
		if (!xyjProductionOrder_bkcb.isEmpty()) {//洗衣机不可拆包  走冰箱逻辑 根据线体和日期生成拣货单
			createPtByBx(xyjProductionOrder_bkcb,picks);
		}
		
		return picks;
//		/**将创建的拣货单做自动分配*/
//		autoAllocate(picks);
	}
	
	/**
	 * 洗衣机工厂根据生产订单生成拣配单，
	 */
	private void createPtByXyj(List<ProductionOrder> washerFactory,List<WmsPickTicket> picks){
		CommonHelper.log("生成洗衣机拣货单，共计"+washerFactory.size()+"张");
		int i=0;
		for (ProductionOrder productionOrder : washerFactory) {
			i++;
			CommonHelper.log("正在生成洗衣机拣货单，第"+i+"/"+washerFactory.size()+"张");
			WmsFactoryWarehouse fFactoryWarehouse = findWmsFactoryWarehouse(productionOrder.getFactory().getId());
			WmsPickTicket pickTicket = createWmsPickTicketByProductionOrder(fFactoryWarehouse.getWarehouse(), productionOrder.getBeginDate(),WmsPickticketGenType.SCLLD);
			pickTicket.setUserField1(productionOrder.getFactory().getCode()); //工厂编码
			String hql = "FROM ProductionOrderDetail d WHERE d.productionOrder.id =:productionOrderId AND d.deleteFlag IS NULL AND d.planQuantityBu-d.shippedQuantityBu>0 ";
			List<ProductionOrderDetail> pods = commonDao.findByQuery(hql, new String[]{"productionOrderId"}, new Object[]{productionOrder.getId()});
			for (ProductionOrderDetail productionOrderDetail : pods) {
				boolean bkcb = false;//不可拆包
				if(WmsItemUnPackingAtt.WAREHOUSE_UNPACKING.equals(productionOrderDetail.getItem().getUserFieldV3())) {
					 bkcb=true;
				}
				if (!WmsItemJITAtt.JIT_DOWNLINE_SETTLE.equals(productionOrderDetail.getItem().getUserFieldV2()) && !bkcb && !productionOrderDetail.getBeAlonePick()) {//非JIT下线的物料 且 可拆包 且不是单独下发的  才生成明细
					/**拣货单计划数*/
					Double planQty = DoubleUtils.sub(productionOrderDetail.getPlanQuantityBu(), productionOrderDetail.getXfQty());
					if(planQty>0){//有计划数量的才生成明细，现在好多工单接口过来就有发运数量
						WmsPickTicketDetail pickTicketDetail = creatWmsPickTicketDetail(pickTicket, productionOrderDetail.getItem(), 
								productionOrderDetail.getPackageUnit(), planQty);
						storeProductionOrderDetailPtDetail(productionOrderDetail, pickTicketDetail,planQty);
						productionOrderDetail.addXfQty(planQty);
						commonDao.store(productionOrderDetail);
					}
//					productionOrderDetail.setBeCreatePt(Boolean.TRUE);
//					commonDao.store(productionOrderDetail);
				}
			}
			productionOrder.setBeCreatPt(Boolean.TRUE);
			
			pickTicket.setRelatedBill1(productionOrder.getCode());
			pickTicket.setUserField6(productionOrder.getLineDesc());//产线描述
			pickTicket.setUserField7(productionOrder.getProductLine());//产线
			this.commonDao.store(pickTicket);
			this.commonDao.store(productionOrder);
			if(pickTicket.getDetails().isEmpty()){
				this.commonDao.delete(pickTicket);
			}else{
				picks.add(pickTicket);
			}
			//WMS工单生成拣货单时 告知SAP工单已拣配 组装一个回传的报文给SAP
			this.genWmsToSapInterfaceLog(productionOrder.getId(), productionOrder.getCode(),"Y");
		}
	}
	
	/**
	 * 冰箱工厂根据生产线+工厂生成拣配单。
	 */
	private void createPtByBx(List<ProductionOrder> refirgeratorFactory,List<WmsPickTicket> picks){
		Map<String,List<ProductionOrder>> refirgeratorFactoryMap =  groupRefirgeratorFactoryByProductLine(refirgeratorFactory);
		
		Set<String> keys = refirgeratorFactoryMap.keySet();
		for(String key : keys) {
//			String[] keyobj = key.split(CommonHelper.VTRADEX_SPLIT_STR);
//			String proLine = keyobj[0];
//			String factoryCode = keyobj[1];
			List<ProductionOrder> productionOrderList = refirgeratorFactoryMap.get(key);
			
			ProductionOrder tmpProductionOrder = productionOrderList.get(0);
			
			WmsFactoryWarehouse fFactoryWarehouse = findWmsFactoryWarehouse(tmpProductionOrder.getFactory().getId());
			WmsPickTicket pickTicket = createWmsPickTicketByProductionOrder(fFactoryWarehouse.getWarehouse(), tmpProductionOrder.getBeginDate(),WmsPickticketGenType.SCLLD);
			pickTicket.setUserField1(tmpProductionOrder.getFactory().getCode()); //工厂编码
			Map<String,List<ProductionOrderDetail>> productionOrderDetailMap = groupProductionOrderDetailByItem(productionOrderList);
			
			for (Map.Entry<String,List<ProductionOrderDetail>> ProductionOrderDetailEntry : productionOrderDetailMap.entrySet()) {
				List<ProductionOrderDetail> productionOrderDetailList = ProductionOrderDetailEntry.getValue();
				Double totalQty = 0D;
				WmsItem item = commonDao.load(WmsItem.class, productionOrderDetailList.get(0).getItem().getId());
				if(WmsItemUnPackingAtt.WAREHOUSE_UNPACKING.equals(item.getUserFieldV3())){//冰箱生成拣货单根据线体生成，明细不合并，但是不可拆包的明细还是合并
					for (ProductionOrderDetail tmpProductionOrderDetail : productionOrderDetailList) {
						//工单明细计划数量-已下发数量 == 拣货明细的计划数量
						Double planQty = DoubleUtils.sub(tmpProductionOrderDetail.getPlanQuantityBu(), tmpProductionOrderDetail.getXfQty());
						totalQty += planQty;
					}
					if(totalQty>0){
						WmsPickTicketDetail pickTicketDetail = creatWmsPickTicketDetail(pickTicket, productionOrderDetailList.get(0).getItem(), productionOrderDetailList.get(0).getPackageUnit(), 
								totalQty);
						for (ProductionOrderDetail tmpProductionOrderDetail : productionOrderDetailList) {
							Double planQty = DoubleUtils.sub(tmpProductionOrderDetail.getPlanQuantityBu(), tmpProductionOrderDetail.getXfQty());
							if(planQty>0){
								storeProductionOrderDetailPtDetail(tmpProductionOrderDetail, pickTicketDetail,planQty);
								tmpProductionOrderDetail.addXfQty(planQty);
								commonDao.store(tmpProductionOrderDetail);
							}
						}
					}
				}else{
					for (ProductionOrderDetail tmpProductionOrderDetail : productionOrderDetailList) {
						//工单明细计划数量-分配数量 == 拣货明细的计划数量
						Double planQty = DoubleUtils.sub(tmpProductionOrderDetail.getPlanQuantityBu(), tmpProductionOrderDetail.getXfQty());
						if(planQty>0){
							WmsPickTicketDetail pickTicketDetail = creatWmsPickTicketDetail(pickTicket, productionOrderDetailList.get(0).getItem(), productionOrderDetailList.get(0).getPackageUnit(), 
									planQty);
							storeProductionOrderDetailPtDetail(tmpProductionOrderDetail, pickTicketDetail,planQty);
							pickTicketDetail.setUserField1(tmpProductionOrderDetail.getProductionOrder().getCode());
							commonDao.store(pickTicketDetail);
							tmpProductionOrderDetail.addXfQty(planQty);
							commonDao.store(tmpProductionOrderDetail);
						}
					}
				}
			}
			for (ProductionOrder productionOrder : productionOrderList) {
				pickTicket.setRelatedBill1(productionOrder.getProductLine());
				pickTicket.setUserField6(productionOrder.getLineDesc());//产线描述
				pickTicket.setUserField7(productionOrder.getProductLine());//产线
				productionOrder.setBeCreatPt(Boolean.TRUE);
				this.commonDao.store(productionOrder);
				//WMS工单生成拣货单时 告知SAP工单已拣配 组装一个回传的报文给SAP
				this.genWmsToSapInterfaceLog(productionOrder.getId(), productionOrder.getCode(),"Y");
			}
			pickTicket.setAllowShortShip(Boolean.TRUE);
			this.commonDao.store(pickTicket);
			if(pickTicket.getDetails().isEmpty()){
				this.commonDao.delete(pickTicket);
			}else{
				picks.add(pickTicket);
			}
			
			/**刷新明细的处理后计划数量*/
			for(ProductionOrder p : refirgeratorFactory){
				for(ProductionOrderDetail detail : p.getDetails()){
					detail.setDealDlanQuantityBu(detail.getPlanQuantityBu());//处理后计划数量=计划数量
					commonDao.store(detail);
				}
			}
		}
	}
	
	
	/**
	 * @Description 根据生产线分组   key生产线+_+工厂编码+工单开始日期
	 */
	private Map<String,List<ProductionOrder>> groupRefirgeratorFactoryByProductLine(List<ProductionOrder> refirgeratorFactory){
		Map<String,List<ProductionOrder>> refirgeratorFactoryMap = new HashMap<String,List<ProductionOrder>>();
		for (ProductionOrder productionOrder : refirgeratorFactory) {
			String key =productionOrder.getProductLine()+CommonHelper.VTRADEX_SPLIT_STR+productionOrder.getFactory().getCode()+CommonHelper.VTRADEX_SPLIT_STR+DateUtil.format(productionOrder.getBeginDate(), "yyyy-MM-dd");
			if(!refirgeratorFactoryMap.containsKey(key)) {
				List<ProductionOrder> tmpProductionOrderList = new ArrayList<ProductionOrder>();
				refirgeratorFactoryMap.put(key, tmpProductionOrderList);
			}
			List<ProductionOrder> tmpProductionOrderList = refirgeratorFactoryMap.get(key);
			tmpProductionOrderList.add(productionOrder);
			refirgeratorFactoryMap.put(key, tmpProductionOrderList);
		}
		return refirgeratorFactoryMap;
	}
	
	private Map<String,List<ProductionOrderDetail>> groupProductionOrderDetailByItem(List<ProductionOrder> productionOrderList){
		
		Map<String,List<ProductionOrderDetail>> productionOrderDetailMap = new HashMap<String, List<ProductionOrderDetail>>();
		
		for (ProductionOrder productionOrder : productionOrderList) {
			String hql = "FROM ProductionOrderDetail d WHERE d.productionOrder.id =:productionOrderId AND d.deleteFlag IS NULL AND d.planQuantityBu-d.shippedQuantityBu>0 ";
			List<ProductionOrderDetail> pods = commonDao.findByQuery(hql, new String[]{"productionOrderId"}, new Object[]{productionOrder.getId()});
			for (ProductionOrderDetail productionOrderDetail : pods) {
				
				//冰箱是非jit物料生成明细  洗衣机也会调用冰箱的生成逻辑  洗衣机是不可拆包物料才生成。
				boolean isxyj = false; //是否是洗衣机
				if(StringHelper.in(productionOrderDetail.getProductionOrder().getFactory().getCode(),new String[]{WmsSapFactoryCodeEnum.XYJ_NX,WmsSapFactoryCodeEnum.XYJ_WX})) {
					isxyj = true;
				}
				if(isxyj) {//洗衣机
					boolean bkcb = false;//不可拆包
					if(!StringHelper.isNullOrEmpty(productionOrderDetail.getItem().getUserFieldV3())
							&& WmsItemUnPackingAtt.WAREHOUSE_UNPACKING.equals(productionOrderDetail.getItem().getUserFieldV3())) {
						 bkcb=true;
					}
					if(bkcb) {//不可拆包
						String key = productionOrderDetail.getItem().getCode();
						if (!productionOrderDetailMap.containsKey(key)) {
							List<ProductionOrderDetail> productionOrderDetailList = new ArrayList<ProductionOrderDetail>();
							productionOrderDetailMap.put(key, productionOrderDetailList);
						}
						List<ProductionOrderDetail> tmpProductionOrderList = productionOrderDetailMap.get(key);
						tmpProductionOrderList.add(productionOrderDetail);
						productionOrderDetailMap.put(key, tmpProductionOrderList);
						
					}
				}
				else {//冰箱逻辑
					if  (!WmsItemJITAtt.JIT_DOWNLINE_SETTLE.equals(productionOrderDetail.getItem().getUserFieldV2()) && !productionOrderDetail.getBeAlonePick() ) { //非jit下线结算的物料 且不是单独下发的物料 生成明细
						String key = productionOrderDetail.getItem().getCode();
						if (!productionOrderDetailMap.containsKey(key)) {
							List<ProductionOrderDetail> productionOrderDetailList = new ArrayList<ProductionOrderDetail>();
							productionOrderDetailMap.put(key, productionOrderDetailList);
						}
						List<ProductionOrderDetail> tmpProductionOrderList = productionOrderDetailMap.get(key);
						tmpProductionOrderList.add(productionOrderDetail);
						productionOrderDetailMap.put(key, tmpProductionOrderList);
					}
				}
			}
		}
		return productionOrderDetailMap;
	}
	
	
	//保存捡货单明细宇生产单明细关系
	public void storeProductionOrderDetailPtDetail(ProductionOrderDetail productionOrderDetail,WmsPickTicketDetail pickTicketDetail,Double quantityBu){
		ProductionOrderDetailPtDetail productionOrderDetailPtDetail = EntityFactory.getEntity(ProductionOrderDetailPtDetail.class);
		productionOrderDetailPtDetail.setPickticketDetail(pickTicketDetail);
		productionOrderDetailPtDetail.setProductionOrderDetail(productionOrderDetail);
		productionOrderDetailPtDetail.setUnit(pickTicketDetail.getPackageUnit());
		productionOrderDetailPtDetail.setQuantityBu(quantityBu);
		productionOrderDetailPtDetail.setCtDate(new Date());
		this.commonDao.store(productionOrderDetailPtDetail);
	}
	
	private void dealDlanQuantityBu(List<ProductionOrder> washerFactory){
		
		/**刷新明细的处理后计划数量*/
		for(ProductionOrder p : washerFactory){
			for(ProductionOrderDetail detail : p.getDetails()){
				detail.setDealDlanQuantityBu(detail.getPlanQuantityBu());//处理后计划数量=计划数量
				commonDao.store(detail);
			}
		}
		
		if(1==1) {//经过讨论后的流程优化，后面的复杂逻辑不需要了  直接处理后计划数量=计划数量  xuyan.xia 2017-09-12 17:14:06
			return ;
		}
		//更多注释详见:工单拣货数据分析.png
		//获取G1和P1
		Map<Long,List<ProductionOrderDetail>> G1 = new HashMap<Long, List<ProductionOrderDetail>>();
		List<ProductionOrderDetail> G1List = null;
		Map<Long,Double> P1 = new HashMap<Long, Double>();
		Long key = null;
		for (ProductionOrder productionOrder : washerFactory) {
			for (ProductionOrderDetail productionOrderDetail : productionOrder.getDetails()) {
				if(WmsItemUnPackingAtt.WAREHOUSE_UNPACKING.equals(productionOrderDetail.getItem().getUserFieldV3())){
					key = productionOrderDetail.getItem().getId();
					if(G1.containsKey(key)){
						G1List = G1.get(key);
					}else{
						G1List = new ArrayList<ProductionOrderDetail>();
					}
					G1List.add(productionOrderDetail);
					G1.put(key, G1List);
					
					if(!P1.containsKey(key)){
						Double minQty = productionOrderDetail.getItem().getUserFieldD1();//最小包装量
						
						if(null != minQty && minQty > 0){
							P1.put(key, minQty);
						}else{
							throw new BusinessException("WmsMinPackageQty.is.null");
						}
					}
				}else{
					productionOrderDetail.setDealDlanQuantityBu(productionOrderDetail.getPlanQuantityBu());
					commonDao.store(productionOrderDetail);
				}
			}
		}
		//产生WmsProductionOrderDetailExtend,更新订单量(处理后量)
		Double minUnit=0D,dealDlanQuantityBu=0D,oddQty = 0D;
		Iterator<Entry<Long, List<ProductionOrderDetail>>>  g1 = G1.entrySet().iterator();
		while(g1.hasNext()){
			Entry<Long, List<ProductionOrderDetail>> entry = g1.next();
			key = entry.getKey();
			G1List = entry.getValue();
			minUnit = P1.get(key);
			if(G1List.size()==1){
				ProductionOrderDetail p = G1List.get(0);
				p.setDealDlanQuantityBu(p.getPlanQuantityBu());
				commonDao.store(p);
			}else{
				Map<Long,Double> M1 = new HashMap<Long, Double>();
				int i = 0;
				for(ProductionOrderDetail p : G1List){
					i++;
					if(i<G1List.size()){
						dealDlanQuantityBu = DoubleUtils.mul(Math.floor(DoubleUtils.div(p.getPlanQuantityBu(),minUnit,3)), minUnit, 3);//订单量(处理后量)
						oddQty = DoubleUtils.sub(p.getPlanQuantityBu(),dealDlanQuantityBu,3);//零头量
						if(oddQty>0){
							M1.put(p.getId(), oddQty);
						}
					}else{
						dealDlanQuantityBu = p.getPlanQuantityBu();//订单量(处理后量)
						Iterator<Entry<Long, Double>> m1 = M1.entrySet().iterator();
						while(m1.hasNext()){
							Entry<Long, Double> em = m1.next();
							dealDlanQuantityBu += em.getValue();//累加零头量
							//生成WmsProductionOrderDetailExtend
							WmsProductionOrderDetailExtend wmsProductionOrderDetailExtend = new WmsProductionOrderDetailExtend(p.getId(), em.getKey(), em.getValue());
							commonDao.store(wmsProductionOrderDetailExtend);
						}
					}
					p.setDealDlanQuantityBu(dealDlanQuantityBu);
					commonDao.store(p);
				}
			}
		}
	}
	public void invokeMethod(){
//		backGdQty(1063L,WmsPickType.ALLOCATED, 1D);
		LocalizedMessage.setMessage("测试用的,没事别点...");
	}
	//更多注释详见:工单拣货数据分析.png
	@SuppressWarnings("unchecked")
	public void backGdQty(Long id,String type,Double quantity){
		ProductionOrderDetail p = commonDao.load(ProductionOrderDetail.class, id);
		if(p==null){
			throw new BusinessException("ProductionOrderDetail.is.not.exists");
		}
		String L1Qty = "",PQty = "";
		if(WmsPickType.ALLOCATED.equals(type)){
			L1Qty = "allocatedQuantityBU";
			PQty = "allocatedQuantityBu";
		}else if(WmsPickType.WORKING.equals(type)){
			L1Qty = "picQuantityBU";
			PQty = "pickedQuantityBu";
		}else if(WmsPickType.FINISHED.equals(type)){//发运移位逻辑请在发运时自动将多余的量做移位,此方法只负责回写传入类型的量!!!
			L1Qty = "shipQuantityBU";
			PQty = "shippedQuantityBu";
		}else{
			throw new BusinessException("type.is.not.define");
		}
		//业务分析时此段代码请略过
		Class<WmsProductionOrderDetailExtend> cL1 = WmsProductionOrderDetailExtend.class;  
		Field fL1 = null;
		Class<ProductionOrderDetail> cP = ProductionOrderDetail.class;  
		Field fP = null;
		try {
			fL1 = cL1.getField(L1Qty);
			fP = cP.getField(PQty);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		Double allocatedQty = 0D;
		String hql = "FROM WmsProductionOrderDetailExtend L1 WHERE L1.srcId =:srcId AND (L1.oddQty>L1."+L1Qty+" OR L1.oddQty<L1."+L1Qty+") ";
		List<WmsProductionOrderDetailExtend> L1s = commonDao.findByQuery(hql, new String[]{"srcId"}, new Object[]{id});
		if(L1s!=null && L1s.size()>0){
			for(WmsProductionOrderDetailExtend L1 : L1s){
				ProductionOrderDetail descP = commonDao.load(ProductionOrderDetail.class, L1.getDescId());
				if(descP==null){
					throw new BusinessException("ProductionOrderDetail.is.not.exists");
				}
				try {
					allocatedQty = quantity>L1.getOddQty()?L1.getOddQty():quantity;//可用量
					//回写WmsProductionOrderDetailExtend
					fL1.set(L1, Double.valueOf(fL1.get(L1).toString())+allocatedQty);
					commonDao.store(L1);
					//回写ProductionOrderDetail
					fP.set(descP, Double.valueOf(fP.get(descP).toString())+allocatedQty);
					commonDao.store(descP);
					//总量递减
					quantity -= allocatedQty;
					if(quantity<=0){
						break;
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		//剩余量直接回写ProductionOrderDetail
		if(quantity>0){//我这里没有做回写量大于工单计划量时的验证逻辑,注意考虑到分配时数据已把关,已分配逻辑为主
			try {
				fP.set(p, Double.valueOf(fP.get(p).toString())+quantity);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			commonDao.store(p);
		}
	}
	
	/**
	 * 货主 名称默认   默认货主编码D   工单生成的拣货单单据类型编码SCLLD
	 * 单据类型
	 * @param warehouse
	 * @param company
	 * @param orderDate
	 */
	public WmsPickTicket createWmsPickTicketByProductionOrder(WmsWarehouse warehouse,Date orderDate,String billCode){
		WmsPickTicket pickTicket = EntityFactory.getEntity(WmsPickTicket.class);
		pickTicket.setWarehouse(warehouse);
		WmsCompany company = findCompany("D");
		if (null == company) {
			throw new BusinessException("not.found.D.WmsCompany");
		}
		pickTicket.setCompany(company);
		WmsBillType billType = findBillType(billCode);
		if (null == billType) {
//			throw new BusinessException("not.found.SCLLD.WmsBillType");
			throw new BusinessException("请维护单据类型编码为"+billCode+"的主数据");
		}
		pickTicket.setBillType(billType);
		pickTicket.setOrderDate(orderDate);
		if(billCode.equals(WmsPickticketBillTypeCode.SCLLD)){
			if(WarehouseEnumeration.VMI.equals(warehouse.getCode())){
				pickTicket.setShipLocation(findShipLocation(warehouse,WmsLocationType.SHIP));
			}else{
				pickTicket.setShipLocation(findShipLocation(warehouse,WmsLocationType.HANDOVER));
			}
			pickTicket.setUserField3(WmsPickticketGenType.SCLLD);//来源
		}
		else if(billCode.equals(WmsPickticketBillTypeCode.YLCKD)){
			pickTicket.setShipLocation(findShipLocation(warehouse,WmsLocationType.SHIP));
			pickTicket.setUserField3(WmsPickticketGenType.YLCKD);//来源
		}
		else if(billCode.equals(WmsPickticketBillTypeCode.XSJHD)){
			pickTicket.setShipLocation(findShipLocation(warehouse,WmsLocationType.SHIP));
			pickTicket.setUserField3(WmsPickticketGenType.XSJHD);//来源
		}
		String pickTicketCode = wmsBussinessCodeManager.generateCodeByRule(warehouse,pickTicket.getBillType().getCode());
		pickTicket.setAllowShortShip(Boolean.TRUE);//允许短缺发运
		pickTicket.setCode(pickTicketCode); 
		workflowManager.doWorkflow(pickTicket,"wmsPickTicketProcess.new");
		return pickTicket;
	}
	
	public WmsPickTicketDetail creatWmsPickTicketDetail(WmsPickTicket pickTicket,WmsItem item,WmsPackageUnit packageUnit,Double expectedQty){
		WmsPickTicketDetail pickTicketDetail = EntityFactory.getEntity(WmsPickTicketDetail.class);
		pickTicketDetail.setPickTicket(pickTicket);
		pickTicketDetail.setLineNo(Integer.parseInt(getMaxLineNoByPickTicketDetail(pickTicket.getId())));
		pickTicketDetail.setItem(item);
		pickTicketDetail.setPackageUnit(packageUnit);
		pickTicketDetail.setExpectedQty(expectedQty * packageUnit.getConvertFigure());
		pickTicketDetail.setExpectedPackQty(expectedQty * packageUnit.getConvertFigure());
		pickTicketDetail.setAllocatedQty(0.0);
		pickTicketDetail.setPickedQty(0.0);
		pickTicketDetail.setShippedQty(0.0);
		this.commonDao.store(pickTicketDetail);
		pickTicket.addPickTicketDetail(pickTicketDetail);
		pickTicket.refreshPickTicketQty();
		
		
		
		WmsPickTicketDetailRequire ptdr = EntityFactory.getEntity(WmsPickTicketDetailRequire.class);
		
		ptdr.setPickTicketDetail(pickTicketDetail);
		
		ptdr.setLotKey("EXTEND_PROPC10"); //批次属性
		if(StringHelper.isNullOrEmpty(pickTicket.getUserField1())) {
			throw new BusinessException("拣配单工厂编码不能为空");
		}
		ptdr.setLotValue1(pickTicket.getUserField1()); //工厂编码
		ptdr.setQueryRequire(WmsSOQueryRequireType.E); //等于
		ptdr.setLevel(WmsLotCategoryType.FORCEMATCHED);//等级
		ptdr.setAllowModified(false); //不允许修改
		
		commonDao.store(ptdr);;
		
		
		return pickTicketDetail;
	}
	
	
	public String getMaxLineNoByPickTicketDetail(Long pickTicketId) {
		Integer lineNo = (Integer) commonDao.findByQueryUniqueResult("SELECT MAX(detail.lineNo) FROM WmsPickTicketDetail detail WHERE detail.pickTicket.id = :pickTicketId", 
				new String[] {"pickTicketId"}, new Object[] {pickTicketId});
		if (lineNo == null || lineNo.intValue() == 0) {
			lineNo = 1;
		} else {
			lineNo += 1;
		}

		return ""+lineNo;
	}
	
	
	public WmsCompany findCompany(String code){
		String hql = " FROM WmsCompany company WHERE company.code =:code AND company.status =:status";
		return (WmsCompany) this.commonDao.findByQueryUniqueResult(hql, new String[]{"code","status"}, new Object[]{code,BaseStatus.ENABLED});
	}
	
	public WmsBillType findBillType(String code){
		String hql = " FROM WmsBillType billType WHERE billType.code =:code AND billType.status =:status";
		return (WmsBillType) this.commonDao.findByQueryUniqueResult(hql, new String[]{"code","status"}, new Object[]{code,BaseStatus.ENABLED});
	}
	
	private WmsLocation findShipLocation(WmsWarehouse warehouse,String type){
		String hql = " FROM WmsLocation location WHERE location.warehouse.id =:warehouseId "
						+ " AND location.type =:type "
						+ " AND location.status =:status";
		List<WmsLocation> locationList = this.commonDao.findByQuery(hql, new String[]{"warehouseId","type","status"}, 
				new Object[]{warehouse.getId(),
//						WmsLocationType.SHIP,
						type,
						BaseStatus.ENABLED});
		if (locationList.isEmpty()) {
//			throw new BusinessException("ship.locaiton.is.not.found",new String[]{warehouse.getCode()});
			throw new BusinessException("请在"+warehouse.getCode()+"仓库维护好"+type+"库位");
		} else {
			return locationList.get(0);
		}
	}
	
    
	private WmsFactoryWarehouse findWmsFactoryWarehouse(Long factoryId){
		String hql = "  FROM WmsFactoryWarehouse fw WHERE fw.factory.id =:factoryId "
						+ " AND fw.type =:type";
		return  (WmsFactoryWarehouse) this.commonDao.findByQueryUniqueResult(hql, new String[]{"factoryId","type"}, new Object[]{factoryId,WmsFactoryXmlb.BZ});
		
	}

	@Override
	public void removeDetails(ProductionOrderDetail productionOrderDetail) {
		ProductionOrder productionOrder =commonDao.load(ProductionOrder.class, productionOrderDetail.getProductionOrder().getId());
		productionOrder.removeDetail(productionOrderDetail);
		this.commonDao.store(productionOrder);	
	}
	
	/**
	 *   预警逻辑:
     *   预警时间点T预警时，WMS查询7天之内未关闭的工单：
     *  （1）开始时间在T时间点之后的工单不参与计算；
     *  （2）工单结束时间在T时间点之前的（包括这个时间点），WMS累计这些未关闭工单的每种恰时物料需求量m；
     *  （3）T时间点在工单的开始小时、结束小时之间的：
     *   a.根据工单的开始时间和结束时间，按一个小时将工单分成多个单位时间段x，先计算每个工单每种恰时物料每个小时的理论需求量：
     *      单个工单每种恰时物料的单位时间段的理论需求量y=该工单每种恰时物料总需求量/时间段x；
     *   b.该工单到h+4时间点的每种恰时物料的理论需求量n=（T-该工单开始时间）*y；
     *      每种恰时物料的理论总需求量s=m+n。
     *   (4)物料总需求s和该物料的库存量inv比较,s<=inv则齐套
	 */
    @Override
    public void onTimeWarning(Date choseDate) {
    	
    	//可以精确到分钟  但是由于工单上只有小时 建议用户前台选择到小时
    	choseDate = DateUtil.getDate(DateUtil.format(choseDate, "yyyy-MM-dd HH"), "yyyy-MM-dd HH");
    	
    	CommonHelper.log("开始计算JIT齐套信息，选择的时间为："+DateUtil.formatDateToYMD_HM(choseDate));
    	
    	/**当前仓库*/
		WmsWarehouse wh = (WmsWarehouse)commonDao.findByQueryUniqueResult("FROM WmsWarehouse warehouse WHERE warehouse.baseOrganization.id = :baseOrganizationId", 
				new String[] {"baseOrganizationId"}, new Object[] {BaseOrganizationHolder.getThornBaseOrganization().getId()});
		//将工单齐套表数据全部标识为不是最新数据  jit料
		String hql = "update WmsProductionOrderMeetInfo set isNewFlag='N' where warehouse.id=:id and isNewFlag='Y' and type ='JIT' ";
		commonDao.executeByHql(hql, "id", wh.getId());
		
		
		List<String> faccodes = new ArrayList<String>();
		if(wh.getCode().equals(WarehouseEnumeration.XYJ)) {
			faccodes.add(WmsSapFactoryCodeEnum.XYJ_NX);
			faccodes.add(WmsSapFactoryCodeEnum.XYJ_WX);
		}
		else if(wh.getCode().equals(WarehouseEnumeration.BX)) {
			faccodes.add(WmsSapFactoryCodeEnum.BX_NX);
			faccodes.add(WmsSapFactoryCodeEnum.BX_WX);
		}
		else {
			throw new BusinessException("只能在冰箱库或洗衣机库操作此功能");
		}
		CommonHelper.log("开始查询工单信息");
        //查询7天之内未关闭的JIT物料工单明细
        Date choseDateBegin = DateUtil.getOneDayRange(choseDate)[0]; // 选择日期的早上零点
        Date choseDateSub7 = DateUtil.addDayToDate(choseDateBegin, -7); // 减去7天
        //查询出工单
        List<ProductionOrderDetail> pods = commonDao.findByQuery("select pod FROM ProductionOrderDetail pod "
                + "LEFT JOIN pod.productionOrder po "
                + "LEFT JOIN pod.item item "
                + "WHERE 1=1  and pod.deleteFlag is null and pod.planQuantityBu>pod.shippedQuantityBu and po.factory.code in (:faccodes) AND po.status =:status AND item.userFieldV2 <> :jitType "
                + "AND po.beginDate >= :beginTime and po.beginDate<=:choseTime ", 
                new String[]{"faccodes","status", "jitType", "beginTime","choseTime"}, 
                new Object[]{faccodes,ProductionOrderStatus.ACTIVE, WmsItemJITAtt.NO_JIT, choseDateSub7, choseDate});  //开始时间是7天之内的
        
        //key为itemid+_+factoryCode
        Map<String, Map<ProductionOrderDetail,Double>> itemPodsMap = new HashMap<String, Map<ProductionOrderDetail,Double>>();
        for (ProductionOrderDetail pod : pods) {
        	if(pod.getProductionOrder().getBeginTime() == null || pod.getProductionOrder().getEndTime() == null){
        		throw new BusinessException("生产订单【"+pod.getProductionOrder().getCode()+"】开始结束时间为空");
        	}
        	
            Long itemId = pod.getItem().getId();
            Double m = DoubleUtils.sub(pod.getPlanQuantityBu(), pod.getAllocatedQuantityBu());//需求数=计划数量-已分配数
            if(m<=0d) {
            	m=0d;
            	continue;
            }
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(pod.getProductionOrder().getBeginTime());
            Long beginTimeInMills = calendar.getTimeInMillis(); //工单开始时间
            calendar.setTime(choseDate);
            Long tTimeInMills = calendar.getTimeInMillis(); //选择的时间
            calendar.setTime(pod.getProductionOrder().getEndTime());
            Long endTimeInMills = calendar.getTimeInMillis(); //工单结束时间
            
            if (tTimeInMills<beginTimeInMills) {//选择的时间 < 工单开始时间   在选择的时间点工单未开始不计算
                continue;
            } 
            
          //物料 + 工厂编码
            String key = itemId + CommonHelper.VTRADEX_SPLIT_STR + pod.getProductionOrder().getFactory().getCode();
            
            if(!itemPodsMap.containsKey(key)) {
            	Map<ProductionOrderDetail,Double> map = new HashMap<ProductionOrderDetail,Double>();
            	itemPodsMap.put(key, map);
            }
            
            if (tTimeInMills>=endTimeInMills) {//选择的时间>=工单结束时间   整张工单都需要算在内  选择的时间之前工单会全部做完
            	itemPodsMap.get(key).put(pod, m);
            } 
            else if (beginTimeInMills<=tTimeInMills && tTimeInMills<endTimeInMills) { //选择的时间 介于 工单开始时间和工单结束时间之间  需要计算，拆分到每个小时
            	//此处逻辑有问题
            	Long diff = endTimeInMills - beginTimeInMills;
            	Long x = diff/(60*1000);//分钟  工单的开始时间和结束时间之间的分钟数
            	Double y = DoubleUtils.div(pod.getPlanQuantityBu(), x);//工单总需求数量/分钟  算出每个分钟的需求量
            	Double n = DoubleUtils.mul((tTimeInMills-beginTimeInMills)/(60*1000), y, 3);  //工单开始时间到选择时间之间的分钟数 * 每分钟需求量 = 此工单在选择时之前的标准需求量
            	Double sjxq = DoubleUtils.sub(n,pod.getAllocatedQuantityBu()); //实际理论需求 = 标准需求量 - 已分配
            	if(sjxq<=0d) { //已经分配满足了
            		continue; 
            	}
                itemPodsMap.get(key).put(pod, sjxq);
            }
        }
            
        if(itemPodsMap.isEmpty()){
        	return;
        }
        CommonHelper.log("开始获取并组装库存信息");
        WmsDeliveryOrderManager wmsDeliveryOrderManager = (WmsDeliveryOrderManager)applicationContext.getBean("wmsDeliveryOrderManager");
        List<Map<String,Object>> allInvs = wmsDeliveryOrderManager.countWmsInventoryGroupByItemIdFactoryCode(); //查询库存
        Map<String,Double> dealInvs = new HashMap<String,Double>(); //组装后的库存
        for(Map<String,Object> map : allInvs) {
        	Long itemid = Long.valueOf(map.get("itemid")+"");
        	String faccode = (String)map.get("factorycode");
        	Double qty = Double.valueOf(map.get("qty")+"");
        	String key = itemid + CommonHelper.VTRADEX_SPLIT_STR + faccode;
        	if(!dealInvs.containsKey(key)) {
        		dealInvs.put(key, 0D);
        	}
        	dealInvs.put(key, DoubleUtils.add(dealInvs.get(key), qty));
        }
        

        CommonHelper.log("开始计算并写入JIT齐套信息");
        Set<String> keys = itemPodsMap.keySet();
        for(String key : keys) {
        	Map<ProductionOrderDetail,Double> map2 = itemPodsMap.get(key);
        	
        	//需要排序 根据工单开始时间小到大排序
        	
            Map<ProductionOrderDetail,Double> map = new TreeMap<ProductionOrderDetail,Double>(
            		 new Comparator<ProductionOrderDetail>() {
                         public int compare(ProductionOrderDetail obj1, ProductionOrderDetail obj2) {
                             // 升序排序
                             return obj1.getProductionOrder().getBeginTime().compareTo(obj2.getProductionOrder().getBeginTime());
                         }
                     });
            map.putAll(map2);
        	
        	
        	//当前库存
        	Double invqty = 0d;
        	if(dealInvs.get(key)!=null) {
        		invqty = dealInvs.get(key);
        	}
        	
        	Set<ProductionOrderDetail> tmp_pods = map.keySet();
        	for(ProductionOrderDetail pod : tmp_pods) {
        		Double xql = map.get(pod); //理论需求量
        		boolean isMeet = false;
        		String status = ProductionOrderMeetInfoStatus.SHORT;
        		Double kcmzl =0d; //库存满足量
        		if(invqty>0d) {
	        		if(invqty>=xql) { //满足
	        			isMeet =true;
	        			kcmzl = xql;
	        			invqty = DoubleUtils.sub(invqty, xql); //扣减需求量
	        			status = ProductionOrderMeetInfoStatus.COMPLETE;
	        		}
	        		else {
	        			kcmzl = invqty;
	        			invqty=0d;
	        			status = ProductionOrderMeetInfoStatus.PART_COMPLETE;
	        		}
        		}
        		
	    		 WmsProductionOrderMeetInfo pomInfo = EntityFactory.getEntity(WmsProductionOrderMeetInfo.class);
	             pomInfo.setBeMeet(isMeet);
	             pomInfo.setStatus(status);
	             pomInfo.setWarehouse(wh);
	             pomInfo.setCheckDate(choseDate);
	             pomInfo.setType(WmsProductionOrderMeetInfoType.JIT);
	             pomInfo.setDetailId(pod.getId());
	             pomInfo.setOrderCode(pod.getProductionOrder().getCode());
	             pomInfo.setBeginDate(pod.getProductionOrder().getBeginTime());
	             pomInfo.setEndDate(pod.getProductionOrder().getEndTime());
	             pomInfo.setItem(pod.getItem());
	//                 pomInfo.setPlanQty(pod.getPlanQuantityBu());
	             pomInfo.setPlanQty(xql); //理论需求量
	             pomInfo.setPackageUnit(pod.getPackageUnit());
	             pomInfo.setFactory(pod.getProductionOrder().getFactory());
	             pomInfo.setIsNewFlag(true);
	             pomInfo.setReservedOrder(pod.getReservedOrder());
	             pomInfo.setReservedProject(pod.getReservedProject());
	             pomInfo.setProductLine(pod.getProductionOrder().getProductLine());
	             pomInfo.setLineDesc(pod.getProductionOrder().getLineDesc());
	             pomInfo.setXqrq(pod.getProductionOrder().getBeginDate());
	             pomInfo.setShipQty(pod.getAllocatedQuantityBu()); //分配数量
	             pomInfo.setInvQty(kcmzl); //库存满足量
	             Double shortQty = DoubleUtils.sub(pomInfo.getPlanQty(), pomInfo.getInvQty());
	             if(shortQty <0d) {
	            	 shortQty = 0d;
	             }
	             pomInfo.setShortQty(shortQty); //短少量
	             commonDao.store(pomInfo);
        	}
        }
    }
    /**
     * 获取当前仓库
     * @return
     */
    private WmsWarehouse getWarehouse(){
		WmsWarehouse wh = (WmsWarehouse)commonDao.findByQueryUniqueResult("FROM WmsWarehouse warehouse WHERE warehouse.baseOrganization.id = :baseOrganizationId", 
				new String[] {"baseOrganizationId"}, new Object[] {BaseOrganizationHolder.getThornBaseOrganization().getId()});
		return wh;
    }
    /**
     * 工单导入时把数据全部插到数据表WmsPickingProductionOrder中，
     * 同时生成一个ThornTask定时去跑
     * @param file
     */
    public void genProdOrderImportTask(File file){
    	//获取当前仓库
    	WmsWarehouse warehouse = this.getWarehouse();
    	if(WarehouseEnumeration.VMI.equals(warehouse.getCode())){
    		throw new BusinessException("VMI仓库下不允许导入工单");
    	}
    	//清除当前仓库下的数据
    	String hql = "DELETE FROM WmsPickingProductionOrder p where  p.warehouse.id =:warehouseId ";
    	this.commonDao.executeByHql(hql, "warehouseId", warehouse.getId());
    	
    	//解析excel文件并插入数据
    	List<Map<String, Object>> infos=ExcelHelper.parseExcel2List(file);
    	for (Map<String, Object> rowDataMap : infos) {
    		String prodOrderCode = (String)rowDataMap.get("生产订单号");
    		prodOrderCode = CommonHelper.addCharBeforeStr(prodOrderCode, 12, "0");
    		ProductionOrder po = this.checkProdOrder(prodOrderCode,warehouse.getId().toString());
    		WmsPickingProductionOrder ppo = EntityFactory.getEntity(WmsPickingProductionOrder.class);
    		ppo.setWarehouse(warehouse);
    		ppo.setProdOrderCode(po.getCode());
    		commonDao.store(ppo);
    	}
    	//工单拣配导入
    	TclMessageManager  tclMessageManager = (TclMessageManager)applicationContext.getBean("tclMessageManager");
    	tclMessageManager.insertWarehouseAutoImportPro(warehouse.getId());
    }
    
    /**
     * 工单导入生成所有的拣货单集合，用来自动分配
     * @param alreadyCreatePick
     * @param readyCreatePick
     * @param map
     */
    private void genPickTickets(List<WmsPickTicket> alreadyCreatePick,List<ProductionOrder> readyCreatePick,Map<String,String> map){
    	ProductionOrder po = null;
    	Set<String> keys = map.keySet();
    	String error = null;
    	int i=0;
    	for(String code : keys){
    		i++;
    		CommonHelper.log("正在校验第"+i+"/"+keys.size()+"条工单");
    		try {
    			po = this.checkProdOrder(code,map.get(code));
    		}
    		catch (Exception e) {
    			error +=CommonHelper.getErrorMessageByException(e);
			}
    	}
    	if(!StringHelper.isNullOrEmpty(error)) {
    		throw new BusinessException(error);
    	}
    	error = null;
    	for(String code : keys){
    		try {
    			po = this.checkProdOrder(code,map.get(code));
    		}
    		catch (Exception e) {
    			error +=CommonHelper.getErrorMessageByException(e);
			}
//    		po.setLocCode(map.get(code));
    		if(!po.getBeCreatPt()){//如果工单的拣配标识为false,则更新标识为true,最后创建拣货单
    			po.setCanAllocate(Boolean.TRUE);
        		commonDao.store(po);
        		if(!readyCreatePick.contains(po)){
        			readyCreatePick.add(po);
        		}
    		}
    		
    		if(po.getBeCreatPt()){//如果已经生成了拣货单,则添加到alreadyCreatePick,最后做自动分配
    			String poCode = po.getFactory().getCode();
    			/**洗衣机工厂则根据工单号去找到对应拣配单 然后做自动分配*/
    			if(StringHelper.in(poCode, new String[]{WmsSapFactoryCodeEnum.XYJ_NX,WmsSapFactoryCodeEnum.XYJ_WX})){
    				String hql = "from WmsPickTicket where relatedBill1=:relatedBill1 " +
    						"and status in ('OPEN','PARTALLOCATED','PART_SHIP') ";
        	    	List<WmsPickTicket> picks = commonDao.findByQuery(hql,"relatedBill1",code);
        	    	for(WmsPickTicket pick : picks){
        	    		if(!alreadyCreatePick.contains(pick)){
//        	    			if(StringHelper.isNullOrEmpty(po.getLocCode()) || !ProductionWarehouseCode.T2.equals(po.getLocCode()) ){
        	    				//已经生成过拣货单的工单,不需要再生成,直接进行分配就可以了,不重复添加拣货单
                    			alreadyCreatePick.add(pick);
//        	    			}
        	    		}
        	    	}
        	    	hql = "from ProductionOrderDetail pod where pod.productionOrder.id=:productionOrderId " +
        	    			"and pod.item.userFieldV3=:userFieldV3 ";
        	    	List<ProductionOrderDetail> pods = commonDao.findByQuery(hql, new String[]{"productionOrderId","userFieldV3"}, 
        	    			new Object[]{po.getId(),WmsItemUnPackingAtt.WAREHOUSE_UNPACKING});
        	    	if(!pods.isEmpty()){
        	    		/**洗衣机不可拆包的：根据线体加日期查拣配单做自动分配*/
        				hql = "from WmsPickTicket w "
        						+ "where w.relatedBill1=:relatedBill1 "
        						+ "and status in ('OPEN','PARTALLOCATED','PART_SHIP') "
        						+ "and to_char(w.orderDate,'yyyy-mm-dd')=:orderDate" +
        								" and w.userField1 in('2000','2100')";
        				List<WmsPickTicket> wmsPicks = commonDao.findByQuery(hql,
        						new String[]{"relatedBill1","orderDate"},
        						new Object[]{po.getProductLine(),DateUtil.formatDateYMDToStr(po.getBeginDate())});
        				for(WmsPickTicket pick : wmsPicks){
            	    		if(!alreadyCreatePick.contains(pick)){
            	    			//已经生成过拣货单的工单,不需要再生成,直接进行分配就可以了,不重复添加拣货单
                    			alreadyCreatePick.add(pick);
            	    		}
            	    	}
        	    	}
    			}
    			else if(StringHelper.in(poCode, new String[]{WmsSapFactoryCodeEnum.BX_NX,WmsSapFactoryCodeEnum.BX_WX})){
    				/**冰箱工厂根据线体加日期查拣配单做自动分配*/
    				String hql = "from WmsPickTicket w "
    						+ "where w.relatedBill1=:relatedBill1 "
    						+ "and status in ('OPEN','PARTALLOCATED','PART_SHIP') "
    						+ "and to_char(w.orderDate,'yyyy-mm-dd')=:orderDate" +
    						" and w.userField1 in('1000','1100') and (w.userField5 !='退料' or w.userField5 is null) ";
    				List<WmsPickTicket> picks = commonDao.findByQuery(hql,
    						new String[]{"relatedBill1","orderDate"},
    						new Object[]{po.getProductLine(),DateUtil.formatDateYMDToStr(po.getBeginDate())});
    				for(WmsPickTicket pick : picks){
        	    		if(!alreadyCreatePick.contains(pick)){
        	    			//已经生成过拣货单的工单,不需要再生成,直接进行分配就可以了,不重复添加拣货单
                			alreadyCreatePick.add(pick);
        	    		}
        	    	}
    			}
    		}
    	}
    	if(!StringHelper.isNullOrEmpty(error)) {
    		throw new BusinessException(error);
    	}
    	
    	//导入拣配标识后自动生成拣货单
    	List<WmsPickTicket> pts = creatPickTicketByProductionOrder(readyCreatePick);
    	alreadyCreatePick.addAll(pts);
    }
    /**
     * 拣配工单导入
     * @return
     */
    public List<WmsPickTicket> importProductionOrder(){
    	String hql = "FROM WmsPickingProductionOrder p ";
    	List<WmsPickingProductionOrder> pickProdOrders = commonDao.findByQuery(hql);
    	Map<String,String> map = new HashMap<String, String>();
    	for(WmsPickingProductionOrder pickProdOrder : pickProdOrders){
    		if(!map.containsKey(pickProdOrder.getProdOrderCode())){
    			map.put(pickProdOrder.getProdOrderCode(), pickProdOrder.getWarehouse().getId().toString());
    		}
    	}
    	List<WmsPickTicket> alreadyCreatePick = new ArrayList<WmsPickTicket>();//已经生成过拣货单的工单
    	List<ProductionOrder> readyCreatePick = new ArrayList<ProductionOrder>();//还没生成过拣货单的工单
    	
    	this.genPickTickets(alreadyCreatePick, readyCreatePick, map);
    	return alreadyCreatePick; //返回总的所有的拣货单  需要做自动分配
    }
    /**
     * 校验生产订单
     * @param code
     * @return
     */
    private ProductionOrder checkProdOrder(String code,String warehouseId){
    	ProductionOrder po = (ProductionOrder) commonDao.findByQueryUniqueResult("FROM ProductionOrder w WHERE w.code =:code", "code", code);
		if(po == null ){
			 throw new BusinessException("未找到工单【"+code+"】");
		}
		else if(!po.getStatus().equals(ProductionOrderStatus.ACTIVE)){
			if(ProductionOrderStatus.FINISHED.equals(po.getStatus())){
//				throw new BusinessException("工单【"+code+"】,SAP已关单!");
			}else{
				throw new BusinessException("工单["+po.getCode()+"]未生效,无法创建拣配单!");
			}
        }
		else{
			if(po.getBeginTime() == null || po.getEndTime() == null){
			 		throw new BusinessException("工单【"+code+"】,没有导入工单开始结束时间！");
			}
		}
//		String hql2 = "FROM ProductionOrderDetail d WHERE d.productionOrder.id =:productionOrderId AND d.deleteFlag IS NULL ";
//		List<ProductionOrderDetail> pods = commonDao.findByQuery(hql2, new String[]{"productionOrderId"}, new Object[]{po.getId()});
//    	if(pods.isEmpty()){
//    		throw new BusinessException("工单【"+code+"】,SAP已关单!");
//    	}
    	WmsSapFactory f = commonDao.load(WmsSapFactory.class, po.getFactory().getId());
		String facCode = f.getCode();
		/**校验工厂是不是当前仓库下的工厂,不是抛异常true*/
		validateFactory(facCode, po.getCode(),true,warehouseId==null ? null :Long.valueOf(warehouseId));
		return po;
    }
    
    /**
     * 拣配工单导入
     * 
     * 返回需要自动分配的拣货单
     */
    public List<WmsPickTicket> importProductionFile(File file){
    	Map<String,String> map = readProductionFileExcel(file);
    	List<WmsPickTicket> alreadyCreatePick = new ArrayList<WmsPickTicket>();//已经生成过拣货单的工单
    	List<ProductionOrder> readyCreatePick = new ArrayList<ProductionOrder>();//还没生成过拣货单的工单
    	this.genPickTickets(alreadyCreatePick, readyCreatePick, map);
    	return alreadyCreatePick; //返回总的所有的拣货单  需要做自动分配
    }
    
    
    //解析EXCEL
    private Map<String,String> readProductionFileExcel(File file){
    	Map<String,String> map = new HashMap<String, String>();
    	List<Map<String,Object>> infos = ExcelHelper.parseExcel2List(file);
    	for(Map<String,Object> info : infos){
    		String code = (String) info.get("生产订单号");
    		//2017-11-01工单没有T-2的概念了，所以导入的时候加个限制
    		String locCode = info.get("库位") ==null ? null : (String) info.get("库位");;
    		if(!StringHelper.isNullOrEmpty(locCode)){
    			throw new BusinessException("不能导入T-2的物料");
    		}
    		code = CommonHelper.addCharBeforeStr(code, 12, "0");
    		if(StringHelper.isNullOrEmpty(code)){
    			throw new BusinessException("行号"+info.get("EXCEL行号")+"生产订单号不能为空");
    		}
    		if(!map.containsKey(code)){
    			map.put(code, locCode);
    		}
    	}
		return map;
    }
    
    
    //取消拣配单
    public void cancleWmsPickTicket(ProductionOrder order){
    	
    	List<Long> proDetailId = new ArrayList<Long>();//工单明细ID
    	List<Long> pickTicketDetailIds = new ArrayList<Long>();//工单关联的拣配单明细ID
    	List<WmsPickTicket> pickTickets = new ArrayList<WmsPickTicket>();//工单关联的拣配单
    	List<Long> deletePodIds = new ArrayList<Long>();//记录要删除的工单明细ID
    	//有删除标识的不会生成捡配单，所以不用取消拣配
    	String hql = "FROM ProductionOrderDetail d WHERE d.productionOrder.id =:productionOrderId AND d.deleteFlag IS NULL ";
		List<ProductionOrderDetail> pods = commonDao.findByQuery(hql, new String[]{"productionOrderId"}, new Object[]{order.getId()});
    	for(ProductionOrderDetail d : pods){
    		if(null != d.getItem().getUserFieldV2() 
    				&& !d.getItem().getUserFieldV2().equals(WmsItemJITAtt.JIT_DOWNLINE_SETTLE)){//非JIT下线料才会生成PT
    			if(d.getAllocatedQuantityBu() > 0D && DoubleUtils.sub(d.getShippedQuantityBu(), d.getOldShippedQuantityBu())>0){
    				throw new BusinessException("此工单有部分明细已经分配,无法取消拣配,请检查!");
    			}
    			proDetailId.add(d.getId());
    		}
    	}
    	this.canclePickticket(proDetailId, pickTicketDetailIds, pickTickets,deletePodIds, order);
    	
    	WmsDeliveryOrderManager deOrderManager = (WmsDeliveryOrderManager) applicationContext.getBean("wmsDeliveryOrderManager");
    	/**由于仓单拉动可能生成新的拣货单,所以和老的拣货单失去了关联,这里再找老拣货单*/
		hql = "select w.id from WmsPickTicket w where w.userField3=:userField3 "
			+ "and w.relatedBill1=:relatedBill1 and w.allocateQty=0 and w.pickQty=0";
		
		List<Long> pickTicketIds = commonDao.findByQuery(hql,
				new String[]{"userField3","relatedBill1"},
				new Object[]{WmsPickticketGenType.SCLLD,order.getCode()});
		if(pickTicketIds.size() > 0){
			//删除拣货单明细
			hql = "select id from WmsPickTicketDetail w where w.pickTicket.id in (:ids)";
			List<Long> detailIds = commonDao.findByQuery(hql, "ids", pickTicketIds);
			deOrderManager.deleteRelatePickData(detailIds, pickTicketIds, "生产订单");
		}
		order.setBeCreatPt(Boolean.FALSE);
		this.genWmsToSapInterfaceLog(order.getId(), order.getCode(), "N");
		commonDao.store(order);
    }
    
    /**自动分配工单创建的拣货单*/
    public void autoAllocate(List<WmsPickTicket> picks){
    	WmsPickticketManager wmsPickticketManager = (WmsPickticketManager) applicationContext.getBean("wmsPickticketManager");
    	for(WmsPickTicket p : picks){
			//自动分配
			wmsPickticketManager.autoAllocate(p);
		}
    }
    /**
     * WMS工单生成拣货单时  告知SAP工单已拣配  组装一个回传的报文给SAP
     * @param id 工单ID
     * @param code  工单编码
     */
    private void genWmsToSapInterfaceLog(Long id,String code,String flag){
		List<SapCommonCallbackDetail> details = new ArrayList<SapCommonCallbackDetail>();
		SapCommonCallbackDetail detail =  new SapCommonCallbackDetail();
		detail.setFlag(flag);
		details.add(detail);
		SapCommonCallback	commCallback = WebServiceHelper.getCommonCallbackResponse(code, SapInterfaceType.SAP_CODE_PROD_BECREATPT, details);
		String xml = XmlObjectConver.toXML(commCallback);
		InterfaceLogManager interfaceLogManager = (InterfaceLogManager) applicationContext.getBean("interfaceLogManager");
		interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.CALLSAPPRODSTATUS, Wms2SapInterfaceLogType.CALLSAPPRODSTATUS, xml, id, code);
    }


	@Override
	public void unActiveProductOrder(ProductionOrder order) {
		for(ProductionOrderDetail detail : order.getDetails()){
			if(detail.getAllocatedQuantityBu() > 0 || detail.getPickedQuantityBu() > 0){
				throw new BusinessException("此工单已经分配,无法失效!!");
			}
		}
	}
	
	/**
	 * 校验工厂是否是当前仓库下面的
	 * poCode = 工单号
	 * @param isException  是否抛异常 true=抛异常
	 * 返回值是  工厂和仓库不对应返回true
	 */
	@Override
	public Boolean validateFactory(String facCode,String poCode,Boolean isThrowException,Long warehouseId){
		WmsWarehouse wh = null;
		if(warehouseId==null){
			wh = this.getWarehouse();
		}else{
			wh = commonDao.load(WmsWarehouse.class, warehouseId);
		}
		if(wh.getCode().equals(WarehouseEnumeration.XYJ) && !StringHelper.in(facCode,
				new String[]{WmsSapFactoryCodeEnum.XYJ_NX,WmsSapFactoryCodeEnum.XYJ_WX})){
			if(isThrowException){
				throw new BusinessException("洗衣机仓库只能导入洗衣机工厂内/外销的工单,错误工单["+poCode+"]");
			}else{
				return true;
			}
		}else if(wh.getCode().equals(WarehouseEnumeration.BX) && !StringHelper.in(facCode,
				new String[]{WmsSapFactoryCodeEnum.BX_NX,WmsSapFactoryCodeEnum.BX_WX})){
			if(isThrowException){	
				throw new BusinessException("冰箱仓库只能导入冰箱工厂内/外销的工单,错误工单["+poCode+"]");
			}else{
				return true;
			}
		}	
		return false;
	}
	/**
	 * 修改生产订单明细单独下发标识
	 */
	public void changeAlonePick(ProductionOrderDetail poDetail){
//		if(poDetail.getBeCreatePt()){
//			throw new BusinessException("明细已生成拣货单不能下发");
//		}
		WmsItem item = commonDao.load(WmsItem.class, poDetail.getItem().getId());
		if(WmsItemJITAtt.JIT_DOWNLINE_SETTLE.equals(item.getUserFieldV2())){
			throw new BusinessException("JIT下线结算的料不能单独下发生成拣货单");
		}
		if(DoubleUtils.sub(poDetail.getPlanQuantityBu(), poDetail.getXfQty())<=0){
			throw new BusinessException("没有可下发数量的明细不能下发");
		}
		poDetail.setBeAlonePick(Boolean.TRUE);
		commonDao.store(poDetail);
	}
	/**
	 * 有单独下发标识的单独生成拣货单
	 */
	public void createPickByAlonePick(){
		Set<WmsPickTicket> pickTickets = new HashSet<WmsPickTicket>();
		String hql = "SELECT DISTINCT detail.productionOrder.id FROM ProductionOrderDetail detail,WmsFactoryWarehouse fw " +
				" WHERE 1=1 AND detail.beAlonePick =:beAlonePick AND detail.beCreatePt =:beCreatePt AND fw.type='0' " +
				" AND detail.planQuantityBu-detail.xfQty>0 AND detail.productionOrder.status='ACTIVE' " +
				" AND fw.factory.id=detail.productionOrder.factory.id AND fw.warehouse.baseOrganization.id=:baseOrganizationId ";
		List<Long> prodOrderIds = commonDao.findByQuery(hql, new String[]{"beAlonePick","beCreatePt","baseOrganizationId"}, new Object[]{Boolean.TRUE,Boolean.FALSE,BaseOrganizationHolder.getThornBaseOrganization().getId()});
//		List<Long> prodOrderIds = new ArrayList<Long>();
//		prodOrderIds.add(106254L);
		//处理不可拆包的物料
		Map<String,List<ProductionOrderDetail>> bkcbMap = new HashMap<String, List<ProductionOrderDetail>>();
		for(Long prodOrderId : prodOrderIds){
			ProductionOrder order = commonDao.load(ProductionOrder.class, prodOrderId);
			WmsSapFactory f =commonDao.load(WmsSapFactory.class, order.getFactory().getId());
			WmsFactoryWarehouse fFactoryWarehouse = findWmsFactoryWarehouse(order.getFactory().getId());
			hql = "FROM ProductionOrderDetail detail WHERE 1=1 AND detail.planQuantityBu-detail.xfQty>0 AND detail.productionOrder.id =:productionOrderId " +
					" AND detail.beAlonePick =:beAlonePick AND detail.beCreatePt =:beCreatePt AND detail.deleteFlag IS NULL";
			List<ProductionOrderDetail> details = commonDao.findByQuery(hql, new String[]{"productionOrderId","beAlonePick","beCreatePt"}, new Object[]{order.getId(),Boolean.TRUE,Boolean.FALSE});
			WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class, fFactoryWarehouse.getWarehouse().getId());
			
			if (warehouse.getCode().equals(WarehouseEnumeration.BX)) {//冰箱
				//不可拆包的物料生成一条拣货明细
				for (ProductionOrderDetail productionOrderDetail : details) {
					WmsItem wmsItem = commonDao.load(WmsItem.class, productionOrderDetail.getItem().getId());
					if(WmsItemUnPackingAtt.WAREHOUSE_UNPACKING.equals(wmsItem.getUserFieldV3())) {
						String key = wmsItem.getId()+"_"+f.getCode()+"_"+order.getProductLine();
						if(!bkcbMap.containsKey(key)){
							List<ProductionOrderDetail> pods = new ArrayList<ProductionOrderDetail>();
							pods.add(productionOrderDetail);
							bkcbMap.put(key, pods);
						}else{
							bkcbMap.get(key).add(productionOrderDetail);
						}
					}
				}
				WmsPickTicket pickTicket = createWmsPickTicketByProductionOrder(warehouse, order.getBeginDate(),WmsPickticketGenType.SCLLD);
				
				pickTicket.setUserField1(f.getCode()); //工厂编码
				for (ProductionOrderDetail productionOrderDetail : details) {
					WmsItem item = commonDao.load(WmsItem.class, productionOrderDetail.getItem().getId());
					if (!WmsItemJITAtt.JIT_DOWNLINE_SETTLE.equals(item.getUserFieldV2()) && !WmsItemUnPackingAtt.WAREHOUSE_UNPACKING.equals(item.getUserFieldV3()) ) {//非JIT下线的物料 且 可拆包  才生成明细
						/**拣货单计划数*/
						Double planQty = DoubleUtils.sub(productionOrderDetail.getPlanQuantityBu(), productionOrderDetail.getXfQty());
						if(planQty>0){
							WmsPackageUnit unit = commonDao.load(WmsPackageUnit.class, productionOrderDetail.getPackageUnit().getId());
							WmsPickTicketDetail pickTicketDetail = creatWmsPickTicketDetail(pickTicket, productionOrderDetail.getItem(), 
									unit, planQty);
							storeProductionOrderDetailPtDetail(productionOrderDetail, pickTicketDetail,planQty);
							productionOrderDetail.setBeCreatePt(Boolean.TRUE);
							productionOrderDetail.addXfQty(planQty);
							commonDao.store(productionOrderDetail);
							pickTicketDetail.setUserField1(order.getCode());
							commonDao.store(pickTicketDetail);
							if(!order.getBeDetailCreatePt()){//单独下发生成捡配单的也告诉SAP已拣配
								order.setBeDetailCreatePt(Boolean.TRUE);
								this.genWmsToSapInterfaceLog(order.getId(), order.getCode(), "Y");
								commonDao.store(order);
							}
						}
					}
				}
				pickTicket.setRelatedBill1(order.getProductLine());
				pickTicket.setUserField6(order.getLineDesc());//产线描述
				pickTicket.setUserField7(order.getProductLine());//产线
				this.commonDao.store(pickTicket);
				if(!pickTicket.getDetails().isEmpty()){
					pickTickets.add(pickTicket);
				}else{
					this.commonDao.delete(pickTicket);
				}
			}else if (warehouse.getCode().equals(WarehouseEnumeration.XYJ)) {//洗衣机
				WmsPickTicket pickTicket = createWmsPickTicketByProductionOrder(warehouse, order.getBeginDate(),WmsPickticketGenType.SCLLD);
				pickTicket.setUserField1(f.getCode()); //工厂编码
				//不可拆包的物料生成一条拣货明细
				for (ProductionOrderDetail productionOrderDetail : details) {
					WmsItem wmsItem = commonDao.load(WmsItem.class, productionOrderDetail.getItem().getId());
					if(WmsItemUnPackingAtt.WAREHOUSE_UNPACKING.equals(wmsItem.getUserFieldV3())) {
						String key = wmsItem.getId()+"_"+f.getCode()+"_"+order.getProductLine();
						if(!bkcbMap.containsKey(key)){
							List<ProductionOrderDetail> pods = new ArrayList<ProductionOrderDetail>();
							pods.add(productionOrderDetail);
							bkcbMap.put(key, pods);
						}else{
							bkcbMap.get(key).add(productionOrderDetail);
						}
					}
				}
				for (ProductionOrderDetail productionOrderDetail : details) {
					WmsItem item = commonDao.load(WmsItem.class, productionOrderDetail.getItem().getId());
					boolean bkcb = false;//不可拆包
					if(WmsItemUnPackingAtt.WAREHOUSE_UNPACKING.equals(item.getUserFieldV3())) {
						 bkcb=true;
					}
					if (!WmsItemJITAtt.JIT_DOWNLINE_SETTLE.equals(item.getUserFieldV2()) && !bkcb) {//非JIT下线的物料 且 可拆包  才生成明细
						/**拣货单计划数*/
						Double planQty = DoubleUtils.sub(productionOrderDetail.getPlanQuantityBu(), productionOrderDetail.getXfQty());
						if(planQty>0){
							WmsPackageUnit unit = commonDao.load(WmsPackageUnit.class, productionOrderDetail.getPackageUnit().getId());
							WmsPickTicketDetail pickTicketDetail = creatWmsPickTicketDetail(pickTicket, productionOrderDetail.getItem(), 
									unit, planQty);
							storeProductionOrderDetailPtDetail(productionOrderDetail, pickTicketDetail,planQty);
							productionOrderDetail.setBeCreatePt(Boolean.TRUE);
							productionOrderDetail.addXfQty(planQty);
							commonDao.store(productionOrderDetail);
							pickTicket.setRelatedBill1(order.getCode());
							pickTicket.setUserField6(order.getLineDesc());//产线描述
							pickTicket.setUserField7(order.getProductLine());//产线
							if(!order.getBeDetailCreatePt()){//单独下发生成捡配单的也告诉SAP已拣配
								order.setBeDetailCreatePt(Boolean.TRUE);
								this.genWmsToSapInterfaceLog(order.getId(), order.getCode(), "Y");
								commonDao.store(order);
							}
						}
					}
				}
				this.commonDao.store(pickTicket);
				if(!pickTicket.getDetails().isEmpty()){
					pickTickets.add(pickTicket);
				}else{
					this.commonDao.delete(pickTicket);
				}
			}
			this.commonDao.store(order);
		}
		if(!bkcbMap.isEmpty()){
			Set<String> keys = bkcbMap.keySet();
			for(String key : keys){
				List<ProductionOrderDetail> prodOrders = bkcbMap.get(key);
				ProductionOrder po = commonDao.load(ProductionOrder.class, prodOrders.get(0).getProductionOrder().getId());
				WmsSapFactory wsf = commonDao.load(WmsSapFactory.class, po.getFactory().getId());
				WmsFactoryWarehouse fw = findWmsFactoryWarehouse(po.getFactory().getId());
				WmsWarehouse wmsWarehouse = commonDao.load(WmsWarehouse.class, fw.getWarehouse().getId());
				//EDI跑自动分配时，如果没分配到库存，系统会拆成一个自管仓没对应关系的打开的拣货单和一个VMI仓有对应关系的打开的拣货单，这时候单独下发的话可能会找到自管仓打开的
				//拣货单，这样会导致edi后面删除没有对应关系的拣货单时报错，一边修改一边删除会导致死锁，所以此处只找有对应关系的打开状态的拣货单
				hql = "FROM WmsPickTicket pick WHERE pick.relatedBill1=:relatedBill1 AND pick.status=:status " +
				" AND pick.warehouse.baseOrganization.id=:baseOrganizationId AND pick.userField1=:userField1 " +
				" AND EXISTS (SELECT p.id FROM ProductionOrderDetailPtDetail p WHERE p.pickticketDetail.pickTicket.id =pick.id) ";
				
				List<WmsPickTicket> picks = commonDao.findByQuery(hql, new String[]{"relatedBill1","status","baseOrganizationId","userField1"}, 
						new Object[]{po.getProductLine(),WmsPickTicketStatus.OPEN,BaseOrganizationHolder.getThornBaseOrganization().getId(),wsf.getCode()});
				WmsPickTicket bkcbPickTicket = null;
				if(picks.isEmpty()){
					bkcbPickTicket = createWmsPickTicketByProductionOrder(wmsWarehouse, po.getBeginDate(),WmsPickticketGenType.SCLLD);
					bkcbPickTicket.setUserField1(wsf.getCode()); //工厂编码
				}else{
					bkcbPickTicket = picks.get(0);
				}
				
				WmsItem item = commonDao.load(WmsItem.class, prodOrders.get(0).getItem().getId());
				Double totalQty = 0D;
				for(ProductionOrderDetail prodOrder : prodOrders){
					//工单明细计划数量-分配数量 == 拣货明细的计划数量
					Double planQty = DoubleUtils.sub(prodOrder.getPlanQuantityBu(), prodOrder.getXfQty());
					totalQty += planQty;
				}
				if(totalQty>0){
					WmsPackageUnit unit = commonDao.load(WmsPackageUnit.class, prodOrders.get(0).getPackageUnit().getId());
					WmsPickTicketDetail pickTicketDetail = creatWmsPickTicketDetail(bkcbPickTicket, item, unit, totalQty);
					bkcbPickTicket.addPickTicketDetail(pickTicketDetail);
					for(ProductionOrderDetail prodOrder : prodOrders){
						Double planQty = DoubleUtils.sub(prodOrder.getPlanQuantityBu(), prodOrder.getXfQty());
						storeProductionOrderDetailPtDetail(prodOrder, pickTicketDetail,planQty);
						prodOrder.addXfQty(planQty);
						prodOrder.setBeCreatePt(Boolean.TRUE);
						commonDao.store(prodOrder);
					}
					bkcbPickTicket.setRelatedBill1(po.getProductLine());//不可拆包的按产线来
					bkcbPickTicket.setUserField6(po.getLineDesc());//产线描述
					bkcbPickTicket.setUserField7(po.getProductLine());//产线
					commonDao.store(bkcbPickTicket);
					if(!bkcbPickTicket.getDetails().isEmpty()){
						pickTickets.add(bkcbPickTicket);
					}else{
						this.commonDao.delete(bkcbPickTicket);
					}
					if(!po.getBeDetailCreatePt()){//单独下发生成捡配单的也告诉SAP已拣配
						po.setBeDetailCreatePt(Boolean.TRUE);
						this.genWmsToSapInterfaceLog(po.getId(), po.getCode(), "Y");
						commonDao.store(po);
					}
				}
			}
		}
		for(WmsPickTicket pick : pickTickets){
			if(pick.getDetails().isEmpty()){
				this.commonDao.delete(pick);
			}else{
				//自动分配
				WmsPickticketManager wmsPickticketManager = (WmsPickticketManager) applicationContext.getBean("wmsPickticketManager");
				wmsPickticketManager.autoAllocate(pick);
			}
		}
		
	}
	
	public void importAlonePick(Map<String, String> map){
		String code = map.get("生产订单");
        code = CommonHelper.addCharBeforeStr(code, 12, "0");
        String itemCode = map.get("物料编码");
        if(StringHelper.isNullOrEmpty(code) || StringHelper.isNullOrEmpty(itemCode)){
        	throw new BusinessException("生产订单和物料编码都不能为空");
        }
        ProductionOrder po = null;
        try {
            po = (ProductionOrder) commonDao.findByQueryUniqueResult("FROM ProductionOrder po WHERE 1=1 "
                    + "AND po.code=:code", "code", code);
        } catch (Exception ex1) {
            ex1.printStackTrace();
            throw new BusinessException(ex1.getMessage());
        }
        if(po==null) {
        	 throw new BusinessException("未找到工单【"+code+"】");
        }
        List<WmsPickTicket> picks = new ArrayList<WmsPickTicket>();
//        if(po.getBeCreatPt()){
//        	throw new BusinessException("已生成拣货单不能单独下发");
//        }
        String hql = "from WmsItem item where item.code=:code";
        WmsItem item = null;
        try {
        	item = (WmsItem) commonDao.findByQueryUniqueResult(hql, "code", itemCode);
        } catch (Exception ex1) {
            ex1.printStackTrace();
            throw new BusinessException(ex1.getMessage());
        }
        if(item==null) {
        	 throw new BusinessException("未找到物料编码为【"+itemCode+"】的物料");
        }
        
        String hql2 = "from ProductionOrderDetail p where p.productionOrder.id =:productionOrderId " +
        		" and p.item.id=:itemId ";
        List<ProductionOrderDetail> pods = commonDao.findByQuery(hql2, 
        		new String[]{"productionOrderId","itemId"}, new Object[]{po.getId(),item.getId()});
        if(pods.isEmpty()){
        	throw new BusinessException("根据工单号"+po.getCode()+"和物料编码"+itemCode+"未找到工单明细");
        }
        for(ProductionOrderDetail pod :pods){
//        	if(pod.getBeAlonePick()){
//        		throw new BusinessException("工单号"+po.getCode()+"和物料编码"+itemCode+"对应工单明细已标记单独下发，不能重复标记");
//        	}
        	if(pod.getBeCreatePt()){
        		hql = "select distinct p.pickticketDetail.pickTicket from ProductionOrderDetailPtDetail p " +
        				"where p.productionOrderDetail.id=:productionOrderDetailId " +
        				"and p.pickticketDetail.pickTicket.status in('OPEN','PARTALLOCATED') ";
        		picks = commonDao.findByQuery(hql, new String[]{"productionOrderDetailId"}, new Object[]{pod.getId()});
        	}else{
        		this.changeAlonePick(pod);
        	}
        }
        Set<WmsPickTicket> qcPicks =  new HashSet<WmsPickTicket>();//去重
		for(WmsPickTicket wmspickTicket : picks){
			qcPicks.add(wmspickTicket);
		}
        for(WmsPickTicket pick : qcPicks){
        	if(pick.getDetails().isEmpty()){
				this.commonDao.delete(pick);
			}else{
				//自动分配
				if(WmsPickTicketStatus.OPEN.equals(pick.getStatus()) || WmsPickTicketStatus.PARTALLOCATED.equals(pick.getStatus())){
					WmsPickticketManager wmsPickticketManager = (WmsPickticketManager) applicationContext.getBean("wmsPickticketManager");
					wmsPickticketManager.autoAllocate(pick);
				}
			}
        }
	}
	/**
	 * 取消捡配单
	 * @param proDetailId
	 * @param pickTicketDetailIds
	 * @param pickTickets
	 * @param order
	 */
	private void canclePickticket(List<Long> proDetailId,List<Long> pickTicketDetailIds,List<WmsPickTicket> pickTickets,List<Long> deletePodIds,ProductionOrder order){
//		List<Long> proDetailId = new ArrayList<Long>();//工单明细ID
//    	List<Long> pickTicketDetailIds = new ArrayList<Long>();//工单关联的拣配单明细ID
//    	List<WmsPickTicket> pickTickets = new ArrayList<WmsPickTicket>();//工单关联的拣配单
//		List<Long> deletePodIds = new ArrayList<Long>();//记录要删除的工单明细ID  不可拆包料生成捡配单时明细汇总成一条明细了，所以取消拣配的时候不能直接把拣配明细删掉，但可以把拣配明细与工单明细对应关系删掉
		String hql = "";
		WmsDeliveryOrderManager deOrderManager = (WmsDeliveryOrderManager) applicationContext.getBean("wmsDeliveryOrderManager");
    	if(proDetailId.size() > 0){
			
			for(Long pId : proDetailId){//工单明细ID
				ProductionOrderDetail productionOrderDetail = commonDao.load(ProductionOrderDetail.class,pId);
				Double planQty = productionOrderDetail.getPlanQuantityBu();//工单计划数量
				hql = "select p.pickticketDetail "
						+ "from ProductionOrderDetailPtDetail p "
						+ "left join p.pickticketDetail ptd "
						+ "where p.productionOrderDetail.id = :id";
				List<WmsPickTicketDetail> pickTicketDetails = commonDao.findByQuery(hql,"id",pId);//工单明细关联的拣货单明细
				for(WmsPickTicketDetail ptd : pickTicketDetails){//拣货单明细
					if(planQty <= 0){
						break;
					}
					Double exceptQty = ptd.getExpectedQty();//拣货单明细的计划数量
					WmsPickTicket pickTicket = ptd.getPickTicket();
					if(ptd.getAllocatedQty()>0){
						throw new BusinessException("拣配单已分配,请先把拣配单取消分配");
					}
					
					//有的拣货单明细是根据线体合并的,所以不能直接删,而是减掉工单明细的计划数量
					if(exceptQty - planQty <= 0D){//拣货单计划数量-工单计划数量=0,直接删除此明细及相关数据
						pickTicketDetailIds.add(ptd.getId());
					}else{
						//只要拣货单某个明细的计划数量>工单明细的计划数量,此拣货单就不能删除,因为明细关联了多个工单明细
						ptd.setExpectedQty(DoubleUtils.sub(ptd.getExpectedQty(), planQty));//拣货单计划数量-工单计划数量
						ptd.setExpectedPackQty(DoubleUtils.sub(ptd.getExpectedPackQty(), planQty));//拣货单计划数量-工单计划数量
						commonDao.store(ptd);
						deletePodIds.add(pId);
						
					}
					if(planQty - pickTicket.getQty() > 0){
						pickTicket.setQty(0D);
					}else{
						pickTicket.removeQty(planQty);
					}
					commonDao.store(pickTicket);
					planQty = DoubleUtils.sub(planQty, exceptQty) ;//剩余的数量
							
					if(!pickTickets.contains(pickTicket)){
						pickTickets.add(pickTicket);
					}
				}
			}
			if(!pickTicketDetailIds.isEmpty()){
				//删除工单明细和拣货单明细对应关系表的数据
				hql = "delete from ProductionOrderDetailPtDetail w where w.pickticketDetail.id in (:ids)";
				commonDao.executeByHql(hql, "ids", pickTicketDetailIds);
				
				/**删除拣货单明细以及关联的数据*/
				deOrderManager.deleteRelatePickData(pickTicketDetailIds, null, "生产订单");
			}
			
			if(!deletePodIds.isEmpty()){
				//删除工单明细和拣货单明细对应关系表的数据
				hql = "delete from ProductionOrderDetailPtDetail w where w.productionOrderDetail.id in (:ids)";
				commonDao.executeByHql(hql, "ids", deletePodIds);
			}
			
			
			for(WmsPickTicket pickTicket : pickTickets){
				if(pickTicket.getQty() == 0D){
					commonDao.delete(pickTicket);
				}
			}
    	}
		for(Long poDetailId : proDetailId){
			ProductionOrderDetail poDetail = commonDao.load(ProductionOrderDetail.class, poDetailId);
			poDetail.setBeCreatePt(Boolean.FALSE);
			poDetail.setXfQty(0D);
			commonDao.store(poDetail);
		}
	}
	/**
	 * 明细取消捡配单
	 * @param poDetail
	 */
	public void canclePickByAlonePick(ProductionOrderDetail poDetail){
		List<Long> proDetailId = new ArrayList<Long>();//工单明细ID
    	List<Long> pickTicketDetailIds = new ArrayList<Long>();//工单关联的拣配单明细ID
    	List<WmsPickTicket> pickTickets = new ArrayList<WmsPickTicket>();//工单关联的拣配单
    	List<Long> deletePodIds = new ArrayList<Long>();
    	ProductionOrder order = poDetail.getProductionOrder();
    	proDetailId.add(poDetail.getId());
    	this.canclePickticket(proDetailId, pickTicketDetailIds, pickTickets,deletePodIds, order);
    	String hql = "from ProductionOrderDetail pod where pod.productionOrder.id =:productionOrderId " +
    			"and pod.beCreatePt=:beCreatePt ";
    	List<ProductionOrderDetail> pods = commonDao.findByQuery(hql, new String[]{"productionOrderId","beCreatePt"}, 
    			new Object[]{order.getId(),Boolean.TRUE});
    	if(pods.isEmpty()){//为空说明明细单独下发生成的拣配单都取消拣配了,如果工单头也没生成拣配单此时告诉SAP解锁改工单
    		order.setBeDetailCreatePt(Boolean.FALSE);
    		if(!order.getBeCreatPt()){
    			this.genWmsToSapInterfaceLog(order.getId(), order.getCode(), "N");
    		}
    		commonDao.store(order);
    	}
	}
	/**
	 * 删除工单导入记录
	 */
	public void deletePickingProductionOrder(){
		WmsWarehouse w = this.getWarehouse();
		String hql = "delete from WmsPickingProductionOrder ppo where ppo.warehouse.id=:warehouseId ";
		commonDao.executeByHql(hql, "warehouseId", w.getId());
	}
	/**
	 * 工单重新打开后，无法自动下发，需要修改下发数量后再次下发
	 */
	public void changeXfQty(ProductionOrderDetail poDetail){
		if(DoubleUtils.sub(poDetail.getXfQty(), poDetail.getShippedQuantityBu())<=0){
			throw new BusinessException("工单明细没有可下发数量，不能修改");
		}
		poDetail.setXfQty(poDetail.getShippedQuantityBu());
		poDetail.setBeChangeXfQty(Boolean.FALSE);
		commonDao.store(poDetail);
	}
}

