package com.vtradex.edi.server.service.sap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.interfaceLog.InterfaceLog;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.DateUtil;
import com.vtradex.wms.server.enumeration.WmsSapFactoryCodeEnum;
import com.vtradex.wms.server.model.entity.base.WmsItemFactory;
import com.vtradex.wms.server.model.entity.base.WmsSapFactory;
import com.vtradex.wms.server.model.entity.inventory.TclWmsInventoryLedger;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.order.PurchaseOrder;
import com.vtradex.wms.server.model.entity.order.PurchaseOrderDetail;
import com.vtradex.wms.server.model.entity.order.WmsTransportOrderDetail;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrder;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetailPtDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrderStatus;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrder;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderStatus;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrder;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderType;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.enums.WmsPickTicketStatus;
import com.vtradex.wms.server.service.interf.InterfaceLogManager;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogFunction;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogTaskType;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogType;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.webservice.model.InterfaceLogSys;
import com.vtradex.wms.webservice.model.SapInterfaceType;
import com.vtradex.wms.webservice.sap.base.SapCommonCallbackDetail;
import com.vtradex.wms.webservice.sap.model.SapCheckOrderInfo;
import com.vtradex.wms.webservice.sap.model.SapCheckOrderInfoArray;
import com.vtradex.wms.webservice.sap.model.SapPodEditType;
import com.vtradex.wms.webservice.utils.WebServiceHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;

public class SapCheckOrderInfoWebServiceImpl extends DefaultBaseManager implements SapCheckOrderInfoWebService {

public InterfaceLogManager interfaceLogManager;
	
	public SapCheckOrderInfoWebServiceImpl(InterfaceLogManager interfaceLogManager) {
		this.interfaceLogManager = interfaceLogManager;
	}

	@Override
	public SapCommonCallbackDetail CheckOrderInfo(SapCheckOrderInfoArray scois) {
		if(null==scois){
			return WebServiceHelper.getSapCommonCallbackDetailError("", "抬头不能为空");
		}
		SapCheckOrderInfo[] scoi = scois.getScoi();
		
		if(null==scoi || scoi.length==0){
			return WebServiceHelper.getSapCommonCallbackDetailError("", "明细信息不能为空");
		}
		String xml = XmlObjectConver.toXML(scois);
		Set<String> infos = new HashSet<String>();
		infos.add(scois.getEBELN());
		String request = WebServiceHelper.setToString(infos);
		
		//生成一个报文不生成task 只是记录，不管接口校验是否成功，都先把报文存下来
		InterfaceLog interfaceLog =interfaceLogManager.createFinishedInterfaceLog(InterfaceLogTaskType.CHECKORDERINFO, InterfaceLogType.CHECKORDERINFO,
				InterfaceLogFunction.SYNC, InterfaceLogSys.SAP_SYS, InterfaceLogSys.WMS_SYS, xml, request);
	
		try {
			if(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_PO.equals(scois.getTYPE())){//采购订单
				for(SapCheckOrderInfo sco : scoi){
					if(!StringHelper.isNullOrEmpty(sco.getLOEKZ())){
						//删除采购订单明细
						String detailHql = "FROM PurchaseOrderDetail detail WHERE detail.purchaseOrder.code=:code AND detail.ebelp =:ebelp ";
						String checkHql = "FROM WmsDeliveryOrderDetail wd WHERE wd.purchaseOrderDetail.id=:wpId";
						List<WmsDeliveryOrderDetail> wdods = null;
						PurchaseOrderDetail detail = null;
						
						List<PurchaseOrderDetail> details = commonDao.findByQuery(detailHql, new String[]{"code","ebelp"}, new Object[]{scois.getEBELN(),sco.getEBELP()});
						
						if(details.isEmpty()){
							throw new BusinessException("根据采购单号"+scois.getEBELN()+"和行项目"+sco.getEBELP()+"未找到采购单明细，删除失败");
						}
						if(details.size()>1){
							throw new BusinessException("根据采购单号"+scois.getEBELN()+"和行项目:"+sco.getEBELP()+"找到了 "+details.size()+"条记录,删除失败");
						}
						detail = details.get(0);
						wdods = commonDao.findByQuery(checkHql, "wpId", detail.getId());
						
						for(WmsDeliveryOrderDetail dod : wdods){
							if(dod.getPlanQuantityBu()>0){
								throw new BusinessException("WMS交货单已有交货数量，不能删除采购单");
							}
						}
					}
					String hql = "FROM PurchaseOrderDetail detail WHERE detail.purchaseOrder.code =:code AND detail.ebelp =:ebelp ";
			        List<PurchaseOrderDetail> details  = commonDao.findByQuery(hql, 
			        		new String[]{"code","ebelp"}, new Object[]{scois.getEBELN(),sco.getEBELP()});
			        if(details.isEmpty()){
			        	//找不到就是新增
			        	//throw new BusinessException("根据采购单号:"+scois.getEBELN()+"和行项目:"+sco.getEBELP()+"未找到采购订单明细");
			        }else{
			        	if(details.size()>1){
				        	throw new BusinessException("根据采购单号:"+scois.getEBELN()+"和行项目:"+sco.getEBELP()+"找到"+details.size()+"条采购订单明细");
				        }
				        PurchaseOrderDetail detail = details.get(0);
				        double d1 = Double.valueOf(sco.getMENGE()); //修改数量
				        double d2 = DoubleUtils.add(detail.getAllotQty(), detail.getReceivedQty());//采购单已分配的数量
				        if(DoubleUtils.sub(d1, d2)<0){//修改的数量 小于已分配的数量 不允许修改
				        	throw new BusinessException("采购订单号为【"+scois.getEBELN()+"】行项目号为:"+sco.getEBELP()+"修改失败!修改后数量"+d1+"小于已分配数量"+d2);
				        }
			        }
				}
			}else if(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_PUO.equals(scois.getTYPE())){//交货单
				for(SapCheckOrderInfo sco : scoi){
					if(!StringHelper.isNullOrEmpty(sco.getLOEKZ())){
						String hql ="FROM WmsTransportOrderDetail t WHERE t.deliveryOrderDetail.deliveryOrder.sapCode =:dCode ";
		            	List<WmsTransportOrderDetail> wtods = commonDao.findByQuery(hql, "dCode", scois.getEBELN());
		            	if(!wtods.isEmpty()){
		            		throw new BusinessException("WMS已加入配货单此交货单不能删除");
		            	}
					}
					String deliveryDate = sco.getEINDT();//交货日期
					String hql = "FROM WmsDeliveryOrderDetail d WHERE d.deliveryOrder.sapCode =:code AND d.posnr =:posnr ";
					List<WmsDeliveryOrderDetail> details = commonDao.findByQuery(hql, new String[]{"code","posnr"}, new Object[]{scois.getEBELN(),sco.getEBELP()});
					if(details.isEmpty()){
						//找不到就是新增
			        	//throw new BusinessException("根据交货单号:"+scois.getEBELN()+"和行项目:"+sco.getEBELP()+"未找到交货单明细");
			        }else{
			        	if(details.size()>1){
				        	throw new BusinessException("根据交货单号:"+scois.getEBELN()+"和行项目:"+sco.getEBELP()+"找到"+details.size()+"条交货单明细");
				        }
				        WmsDeliveryOrderDetail detail = details.get(0);
				        WmsDeliveryOrder order = commonDao.load(WmsDeliveryOrder.class, detail.getDeliveryOrder().getId());
				        if(!DateUtil.format(order.getDeliveryDate(),"yyyyMMdd").equals(deliveryDate)){
				        	if(detail.getQuantity()>0){
				        		throw new BusinessException("WMS已有配货数量不能修改日期");
				        	}
				        }
				        //交货单明细调整的数量
		        		Double adjustQty = DoubleUtils.sub(Double.parseDouble(sco.getMENGE()),detail.getPlanQuantityBu());
		        		if(adjustQty>=0){ //交货单明细增加交货数量
		        			//获取PO明细单数量
		        			PurchaseOrderDetail pod = commonDao.load(PurchaseOrderDetail.class, detail.getPurchaseOrderDetail().getId());
		        			PurchaseOrder o = commonDao.load(PurchaseOrder.class, pod.getPurchaseOrder().getId());
		        			hql = "SELECT SUM(dod.planQuantityBu) FROM WmsDeliveryOrderDetail dod WHERE dod.purchaseOrderDetail.id =:purchaseOrderDetailId  ";
		        			Double allDodQty = (Double) commonDao.findByQueryUniqueResult(hql, new String[]{"purchaseOrderDetailId"}, new Object[]{pod.getId()});
		        			
		        			if(DoubleUtils.sub(allDodQty+adjustQty,pod.getExpectedQty())>0){
		        				//增加数量大于PO单的计划数量
		        				throw new BusinessException("修改数量大于采购订单计划数量请先修改采购订单数量!!!"+o.getCode());
		        			}
		        		}else{//交货单明细减少交货数量
		        			Double unDeliveryQuantityBu = DoubleUtils.sub(detail.getTheDeliveryQuantityBu(),detail.getQuantity());//未配货数量
		        			if(unDeliveryQuantityBu<-adjustQty){//交货单剩余未配货的数量小于修改后的调整数量 不允许修改
		        				hql ="FROM WmsTransportOrderDetail t WHERE t.deliveryOrderDetail.id =:deliveryOrderDetailId ";
		                    	List<WmsTransportOrderDetail> wtods = commonDao.findByQuery(hql, "deliveryOrderDetailId", detail.getId());
		                    	if(!wtods.isEmpty()){
		                    		throw new BusinessException("WMS已加入配货单不允许修改");
		                    	}
		        			}
		        		}
			        }
				}
			}else if(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_SOD.equals(scois.getTYPE())){//销售交货单
				
				for(SapCheckOrderInfo sco : scoi){
					 
					String hql = "FROM WmsDeliveryOrderDetail d WHERE d.deliveryOrder.code =:code AND d.posnr =:posnr ";
					List<WmsDeliveryOrderDetail> details = commonDao.findByQuery(hql, new String[]{"code","posnr"}, new Object[]{scois.getEBELN(),sco.getEBELP()});
					if(details.isEmpty()){
						//找不到就是新增
			        	//throw new BusinessException("根据交货单号:"+scois.getEBELN()+"和行项目:"+sco.getEBELP()+"未找到交货单明细");
			        }else{
			        	if(details.size()>1){
				        	throw new BusinessException("根据交货单号:"+scois.getEBELN()+"和行项目:"+sco.getEBELP()+"找到"+details.size()+"条交货单明细");
				        }
				        WmsDeliveryOrderDetail detail = details.get(0);
				        WmsDeliveryOrder order = commonDao.load(WmsDeliveryOrder.class, detail.getDeliveryOrder().getId());
				        /**
				         * 销售交货单发运传SAP后，SAP过账会再调一次我们的接口，此时需要判断，如果是过账就返回成功给SAP
				         * 过账成功的条件：SAP传的报文条数与WMS销售交货单明细条数一致，且发运数量也一致
				         * 否则告知SAP不能修改
				         * 带删除标识的默认为发运数量是0，因为SAP过账的时候传过来的发运数量可能不是0,但是带删除标识，这样的数据我们也认为SAP发运数量就是0，与WMS系统一致就过
				         */
				        Double shipQty = Double.valueOf(sco.getMENGE());
				        if(!StringHelper.isNullOrEmpty(sco.getLOEKZ())){
				        	shipQty = 0D;
				        }
				        hql = "FROM WmsDeliveryOrderDetail d WHERE d.deliveryOrder.code =:code";
				        List<WmsDeliveryOrderDetail> dods = commonDao.findByQuery(hql, "code", order.getCode());
				        if(!WmsDeliveryOrderStatus.OPEN.equals(order.getStatus())) { //不等于打开 说明已经生成了拣配,只有已拣配已发运的才能过
				        	if(dods.size() != scoi.length || !detail.getDelivedQuantityBu().equals(shipQty)){
				        		throw new BusinessException("WMS已生成拣配单且未发运，不允许修改");
				        	}
				        }
			        }
				}
			}
			else if(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_REDATA.equals(scois.getTYPE())) {//预留修改实时接口
				/***
				 * SAP每次会把修改的预留的明细传过来，比如原来有10个明细行，修改了3个明细行，则在一个报文里传3个明细行过来。
				 * WMS处理逻辑是3个明细行全部可以修改通过，则校验通过，只要有一个不通过，则不通过。
				 * 
				 * 如果预留明细行没有生成拣货单，则随便修改。
				 * 如果预留明细行生成了拣货单，预留的数量可以改小，不能改大，如果要改大，通过销售再下一张预留单或者在预留单里面添加一个明细的方式实现。
				 * 如果报文中根据预留号和行项目号在WMS没找到，则认为是新增明细行，可以修改。
				 * 对于数量改小：比如原预留行的计划数是100，wms生成了拣货单，一张80的已分配，和一张20的打开，则sap最小可以调整为80，wms把打开的预留里面的信息相应扣除数量。
				 * 
				 * sap可以传一个X的删除标记过来，相当于是数量改成0的逻辑。
				 * 
				 * 时间和库位随便改。
				 * 
				 */
				for(SapCheckOrderInfo sco : scoi){
					String hql = "FROM WmsReservedOrderDetail d WHERE d.reservedOrder.code =:code AND d.project =:project ";
					List<WmsReservedOrderDetail> details = commonDao.findByQuery(hql, new String[]{"code","project"},
							new Object[]{scois.getEBELN(),sco.getEBELP()});
					if(details.isEmpty()){
						//找不到就是新增  可以通过
			        }
					else{
			        	if(details.size()>1){
				        	throw new BusinessException("根据预留单号:"+scois.getEBELN()+"和行项目:"+sco.getEBELP()+"找到"+details.size()+"条预留单明细");
				        }
			        	WmsReservedOrderDetail detail = details.get(0);
			        	WmsReservedOrder order = commonDao.load(WmsReservedOrder.class, detail.getReservedOrder().getId());
			        	
			        	Double changeQty =0d;//修改后的数量
			        	boolean deleteFlag =false; //删除标记
		        		if(!StringHelper.isNullOrEmpty(sco.getLOEKZ())) { //删除标记  
		        			StringHelper.assertNullOrEmpty(sco.getENMNG(), "有删除标记则ENMNG字段不能为空");
		        			deleteFlag = true;
		        			changeQty = Double.valueOf(sco.getENMNG()); //修改后的数量为提货数
		        		}
		        		else {
		        			StringHelper.assertNullOrEmpty(sco.getMENGE(), "无删除标记则MENGE字段不能为空");
		        			changeQty = Double.valueOf(sco.getMENGE());   //修改后的数量为计划数
		        		}
			        	
			        	if(StringHelper.in(order.getYdlx(),new String[]{WmsReservedOrderType.Z01,WmsReservedOrderType.Z03,WmsReservedOrderType.Z311}) ) {//发货类型
				        	if(detail.getIsCreatePick()) { //生成了拣货单
				        		if(DoubleUtils.sub(changeQty, detail.getQuantity()) > 0d) { //数量变大
				        			throw new BusinessException("行项目"+sco.getEBELP()+"WMS已开始拣配不允许增加数量");
				        		}
				        		
				        		hql = "select d.pickticketDetail from ReservedOrderDetailPtDetail d where d.reservedOrderDetail.id=:id";
				        		List<WmsPickTicketDetail> ptds = commonDao.findByQuery(hql,new String[]{"id"},new Object[]{detail.getId()});
				        		Double resdalloqty = 0d; //预留明细已经分配的数量
				        		for(WmsPickTicketDetail ptd : ptds) {
				        			resdalloqty = DoubleUtils.add(resdalloqty, ptd.getAllocatedQty());
				        			WmsPickTicket pt = commonDao.load(WmsPickTicket.class, ptd.getPickTicket().getId());
				        			if(WmsPickTicketStatus.PARTALLOCATED.equals(pt.getStatus())) { //有部分分配的不允许改
				        				throw new BusinessException("行项目"+sco.getEBELP()+"在WMS中存在部分分配的拣货单，请先联系保管员处理");
				        			}
				        		}
				        		if(deleteFlag && resdalloqty>0d) {
				        			throw new BusinessException("行项目"+sco.getEBELP()+"WMS已经分配了"+resdalloqty+"不允许删除");
				        		}
				        		if(DoubleUtils.sub(changeQty, resdalloqty)<0d) {
				        			throw new BusinessException("行项目"+sco.getEBELP()+"WMS已经分配了"+resdalloqty+"大于修改后数量"+changeQty);
				        		}
				        	}
			        	}
			        	else {//收货类型
				        	if(detail.getIsCreateASN()) { //生成了收货单
				        		if(DoubleUtils.sub(changeQty, detail.getQuantity()) > 0d) { //数量变大
				        			throw new BusinessException("行项目"+sco.getEBELP()+"WMS已开始收货不允许增加数量");
				        		}
				        		 //收货逻辑和发货逻辑差不多。
				        	  hql = "from WmsASNDetail asnd where asnd.asn.billType.code='YLRKD' and asnd.userField1=:detailId ";
				        	  List<WmsASNDetail> asnd = commonDao.findByQuery(hql,new String[]{"detailId"},new Object[]{detail.getId()});
				        	  if(!asnd.isEmpty()) {
				        		  if(asnd.size()>1) {
				        			  throw new BusinessException("根据行项目:"+sco.getEBELP()+"找到"+asnd.size()+"条收货单明细");
				        		  }
				        		  WmsASNDetail asndetail = asnd.get(0);
				        		  Double recQty = asndetail.getReceivedQty();
				        		  if(DoubleUtils.sub(changeQty, recQty)<0d) {
				        			  throw new BusinessException("行项目"+sco.getEBELP()+"WMS已收货"+recQty+"大于修改后数量"+changeQty);
				        		  }
				        	  }
				        	}
			        	}
			        }
				}
			}else if(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_PRO.equals(scois.getTYPE())){//生产订单
				//生产订单校验还需要实时的修改工单信息，所以需要带事务
				interfaceLogManager.checkProductionOrder(scois);
			}
		}
		catch(Exception e) {
			SapCommonCallbackDetail result= WebServiceHelper.getSapCommonCallbackDetailError("", e.getMessage());
			interfaceLogManager.updateInterfaceLogToFail(interfaceLog.getId(), XmlObjectConver.toXML(result), e.getMessage());
			e.printStackTrace();
			return result;
		}
		SapCommonCallbackDetail sucResult = WebServiceHelper.getSapCommonCallbackDetailSucess("");
		interfaceLogManager.updateInterfaceLogToSucess(interfaceLog.getId(), XmlObjectConver.toXML(sucResult), "");
		return sucResult;
	}
	
	/**
	 * 通过ptd与pod对应关系表找到工单明细对应的拣货单明细
	 * @param proDetailId
	 * @return
	 */
	private List<WmsPickTicketDetail> getPickTicketDetail(Long proDetailId){
		String hql = "select pdptd.pickticketDetail from ProductionOrderDetailPtDetail pdptd " +
				"where pdptd.productionOrderDetail.id =:productionOrderDetailId";
		List<WmsPickTicketDetail> ptds = commonDao.findByQuery(hql, new String[]{"productionOrderDetailId"}, 
				new Object[]{proDetailId});
		return ptds;
	}
	/**
	 * 更新拣货单及对应关系表
	 * @param proDetailId
	 * @param qty
	 */
	private void updatePtdAndPodInfo(Long proDetailId,Double qty){
		List<WmsPickTicketDetail> ptds = this.getPickTicketDetail(proDetailId);
		Double cancelQty = qty;
		for(WmsPickTicketDetail ptd :ptds){
			if(cancelQty<=0){
				break;
			}
			String hql = " from ProductionOrderDetailPtDetail p where p.productionOrderDetail.id=:productionOrderDetailId " +
					" and p.pickticketDetail.id=:pickticketDetailId ";
			ProductionOrderDetailPtDetail podpt = (ProductionOrderDetailPtDetail) commonDao.findByQueryUniqueResult(hql, new String[]{"productionOrderDetailId","pickticketDetailId"},
					new Object[]{proDetailId,ptd.getId()});
			WmsPickTicket pt = commonDao.load(WmsPickTicket.class, ptd.getPickTicket().getId());
			if(WmsPickTicketStatus.OPEN.equals(pt.getStatus())){
				if(DoubleUtils.sub(cancelQty, ptd.getExpectedQty())<=0){
					pt.removeQty(cancelQty);
					commonDao.store(pt);
					ptd.setExpectedPackQty(DoubleUtils.sub(ptd.getExpectedPackQty(), cancelQty));
					ptd.setExpectedQty(DoubleUtils.sub(ptd.getExpectedQty(), cancelQty));
					podpt.removeQuantityBu(cancelQty);
					cancelQty=0D;
				}else{
					cancelQty-=ptd.getExpectedQty();
					podpt.removeQuantityBu(ptd.getExpectedQty());
					pt.removeQty(ptd.getExpectedQty());
					commonDao.store(pt);
					ptd.setExpectedPackQty(0D);
					ptd.setExpectedQty(0D);
				}
				ptd.setPickTicket(pt);
				commonDao.store(ptd);
				commonDao.store(podpt);
				if(podpt.getQuantityBu()<=0){
					commonDao.delete(podpt);
				}
				if(ptd.getExpectedQty()<=0){
					//删除拣货明细批次属性要求
					hql = "delete from WmsPickTicketDetailRequire pdr where pdr.pickTicketDetail.id =:pickTicketDetailId ";
					commonDao.executeByHql(hql, "pickTicketDetailId",ptd.getId());
					//退拣的没删除BOLDetail,此时删除
					hql = "delete from WmsBolDetail d where d.pickTicketDetail.id=:pickTicketDetailId ";
					commonDao.executeByHql(hql, "pickTicketDetailId",ptd.getId());
					//删除拣货明细
					hql = "delete from WmsPickTicketDetail p where p.id =:pId ";
					commonDao.executeByHql(hql, "pId",ptd.getId());
				}
				if(pt.getQty()<=0){
					//删除拣货单
					hql = "delete from WmsPickTicket pt where pt.id =:ptId ";
					commonDao.executeByHql(hql, "ptId",pt.getId());
				}
			}
		}
	}
	/**
	 * 查找生产订单明细
	 * @param poCode
	 * @param resCode
	 * @param resProject
	 * @return
	 */
	private ProductionOrderDetail getProductionOrderDetail (String poCode, String resCode,String resProject){
		String hql = "from ProductionOrderDetail pod where pod.productionOrder.code =:productionOrderCode " +
		"and pod.reservedOrder =:reservedOrder and pod.reservedProject=:reservedProject ";
		List<ProductionOrderDetail> podetails = commonDao.findByQuery(hql, new String[]{"productionOrderCode","reservedOrder","reservedProject"},
				new Object[]{poCode,resCode,resProject});
		if(podetails.isEmpty()){
			throw new BusinessException("根据生产订单号:"+poCode+"预留号:"+resCode+"预留行项目:"+resProject+"在WMS中未找到生产订单明细");
		}
		if(podetails.size()>1){
			throw new BusinessException("根据生产订单号:"+poCode+"预留号:"+resCode+"预留行项目:"+resProject+"在WMS中找到"+podetails.size()+"条生产订单明细");
		}
		return podetails.get(0);
	}
	/**
	 * 获取最大行号
	 * @param poCode
	 * @return
	 */
	private Integer getMaxLineNo(String poCode){
		String hql =  "select MAX(pod.lineNo) from ProductionOrderDetail pod where pod.productionOrder.code =:productionOrderCode ";
		Integer lineNo = (Integer) commonDao.findByQueryUniqueResult(hql, "productionOrderCode", poCode);
		return lineNo==null ? 10 : lineNo+10;
	}
	/**
	 * 校验生产订单明细是否可以修改
	 * @param pod
	 */
	private void checkProDetail(ProductionOrderDetail pod){
		Double checkQty = pod.getAllocatedQuantityBu();
		ProductionOrder po = commonDao.load(ProductionOrder.class, pod.getProductionOrder().getId());
		if(DoubleUtils.sub(pod.getAllocatedQuantityBu(), pod.getPlanQuantityBu())>0){
			checkQty = pod.getPlanQuantityBu();
		}
		//考虑到不可拆包的物料多分的情况下,先判断工单明细是否有分配未发的,如果有再判断工单对应的拣货单是否有分配未发的,还有就报错
		if(pod.getAllocatedQuantityBu()>0 && DoubleUtils.sub(pod.getShippedQuantityBu(), checkQty)<0){
			String hql = "select pdptd.pickticketDetail from ProductionOrderDetailPtDetail pdptd " +
			"where pdptd.productionOrderDetail.productionOrder.id =:productionOrderId";
			List<WmsPickTicketDetail> checkPtds = commonDao.findByQuery(hql, new String[]{"productionOrderId"}, 
					new Object[]{po.getId()});
			for(WmsPickTicketDetail checkPtd :checkPtds){
				if(checkPtd.getAllocatedQty()>0 && DoubleUtils.sub(checkPtd.getShippedQty(), checkPtd.getAllocatedQty())<0){
					throw new BusinessException("WMS存在已分配但是未发运的拣货明细，预留行项目号_"+pod.getReservedProject());
				}
			}
		}
		//有种情况，WMS发运后把台账报文传给SAP，但是SAP还没来得及处理，生产订单被操作员关单，这时如果SAP再处理发运的台账报文处理不了
		String hql = "from TclWmsInventoryLedger t where t.reservedCode=:reservedCode " +
				"and t.resProject=:resProject and t.sapCode is null";
		List<TclWmsInventoryLedger> ledgers = commonDao.findByQuery(hql,new String[]{"reservedCode","resProject"},
				new Object[]{pod.getReservedOrder(),pod.getReservedProject()}	);
		if(!ledgers.isEmpty()){
			throw new BusinessException("生产订单发料的台账记录SAP还没处理，预留行项目号_"+pod.getReservedProject());
		}
	}
}
