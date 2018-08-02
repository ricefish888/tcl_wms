package com.vtradex.wms.server.service.receiving.pojo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.vtradex.sequence.service.sequence.SequenceGenerater;
import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.wms.server.helper.WmsBarCodeParse;
import com.vtradex.wms.server.model.component.LotInfo;
import com.vtradex.wms.server.model.dto.WmsInventoryDto;
import com.vtradex.wms.server.model.entity.base.WmsBarCodeRepPrintRecord;
import com.vtradex.wms.server.model.entity.base.WmsItemKeeper;
import com.vtradex.wms.server.model.entity.base.WmsSapFactory;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.model.entity.base.WmsSupplierPrintCount;
import com.vtradex.wms.server.model.entity.base.WmsSupplierPrintCountType;
import com.vtradex.wms.server.model.entity.base.WmsWorker;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.item.WmsBillType;
import com.vtradex.wms.server.model.entity.item.WmsCompany;
import com.vtradex.wms.server.model.entity.item.WmsInventoryState;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemKey;
import com.vtradex.wms.server.model.entity.item.WmsLotRule;
import com.vtradex.wms.server.model.entity.item.WmsMinPackageQty;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.order.PurchaseOrder;
import com.vtradex.wms.server.model.entity.order.PurchaseOrderDetail;
import com.vtradex.wms.server.model.entity.order.PurchaseOrderDetailStatus;
import com.vtradex.wms.server.model.entity.order.WmsTransportOrder;
import com.vtradex.wms.server.model.entity.order.WmsTransportOrderDetail;
import com.vtradex.wms.server.model.entity.order.WmsTransportOrderDetailStatus;
import com.vtradex.wms.server.model.entity.order.WmsTransportOrderStatus;
import com.vtradex.wms.server.model.entity.production.ProductionOrder;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrder;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderDetailStatus;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderStatus;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderDetail;
import com.vtradex.wms.server.model.entity.receiving.WmsASN;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.entity.receiving.WmsASNStatus;
import com.vtradex.wms.server.model.entity.receiving.WmsPo;
import com.vtradex.wms.server.model.entity.receiving.WmsPoDetail;
import com.vtradex.wms.server.model.entity.receiving.WmsQcRecord;
import com.vtradex.wms.server.model.entity.receiving.WmsReceivedRecord;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;
import com.vtradex.wms.server.model.entity.workdoc.WmsWorkDoc;
import com.vtradex.wms.server.model.enums.BaseStatus;
import com.vtradex.wms.server.model.enums.WmsAsnGenType;
import com.vtradex.wms.server.model.enums.WmsBillOfType;
import com.vtradex.wms.server.model.enums.WmsInventoryLogType;
import com.vtradex.wms.server.model.enums.WmsInventoryOperationStatus;
import com.vtradex.wms.server.model.enums.WmsLocationType;
import com.vtradex.wms.server.model.enums.WmsSupplierCode;
import com.vtradex.wms.server.model.enums.WmsWorkDocStatus;
import com.vtradex.wms.server.model.enums.WmsWorkDocType;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.item.TclMessageManager;
import com.vtradex.wms.server.service.item.WmsItemManager;
import com.vtradex.wms.server.service.receiving.WmsTclASNManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.PackageUtils;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.server.web.servlet.ReportPrintServlet;


/**
 * 
 * Tcl 定制化发货单业务
 * 
 * @author <a href="zhen.lei@vtradex.com">Yogurt_lei</a>
 * 
 * @date 2017年7月21日 下午2:32:24
 */
public class DefaultWmsTclASNManager extends DefaultWmsASNManager implements WmsTclASNManager {
	
    protected WorkflowManager workflowManager;
    protected final SequenceGenerater sequenceGenerater;
    protected TclMessageManager tclMessageManager;
    protected WmsInventoryManager inventoryManager;
    protected WmsItemManager wmsItemManager;
    protected WmsRuleManager wmsRuleManager;
    protected WmsBussinessCodeManager wmsBussinessCodeManager;
    private SimpleDateFormat sdfym = new SimpleDateFormat("yyyyMM");
    
    public DefaultWmsTclASNManager(WorkflowManager workflowManager,
            WmsInventoryManager inventoryManager,
            WmsItemManager wmsItemManager,
            WmsBussinessCodeManager wmsBussinessCodeManager,
            WmsRuleManager wmsRuleManager, SequenceGenerater sequenceGenerater,TclMessageManager tclMessageManager) {
        super(workflowManager, inventoryManager, wmsItemManager,
                wmsBussinessCodeManager, wmsRuleManager);
        this.workflowManager = workflowManager;
        this.sequenceGenerater = sequenceGenerater;
        this.tclMessageManager = tclMessageManager;
        this.inventoryManager = inventoryManager;
        this.wmsItemManager = wmsItemManager;
        this.wmsRuleManager = wmsRuleManager;
        this.wmsBussinessCodeManager = wmsBussinessCodeManager;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void isActiveAsn(WmsASN asn) {
        super.isActiveAsn(asn);
//        WmsSupplier supplier = asn.getSupplier();//供应商
        List<String> codes = commonDao.findByQuery("SELECT detail.item.code FROM WmsASNDetail detail "
                + "WHERE detail.asn.id=:asnId AND detail.lotInfo.lot IS NUll", "asnId", asn.getId());
        Set<String> uniqueCodes = new HashSet<String>(codes);
        for (String code : uniqueCodes) {
            String preLot = sequenceGenerater.generateSequence(code + sdfym.format(new Date()), 3);
            String lot = org.apache.commons.lang3.StringUtils.substringAfter(preLot, code);
            
            for (WmsASNDetail detail : asn.getDetails()) {
                if (code.equals(detail.getItem().getCode()) && detail.getLotInfo().getLot()==null) {
                    detail.getLotInfo().setLot(lot);
//                    detail.getLotInfo().setExtendPropC16(detail.getId()+"");//不能存asndetail  会造成在库存里面是多条记录，rf拣货需要拣多次
                  detail.getLotInfo().setExtendPropC17(getBarcode(detail));
                }
                else {
                	continue;
                }
            }
        }
        
      //判断批次属性
        for(WmsASNDetail d : asn.getDetails()) {
        	LotInfo lotInfo = d.getLotInfo();
        	if(StringHelper.isNullOrEmpty(lotInfo.getSupplierCode())) {
        		throw new BusinessException("明细行"+d.getLineNo()+"供应商编码不能为空");
        	}
        	if(StringHelper.isNullOrEmpty(lotInfo.getExtendPropC9())) {
        		throw new BusinessException("明细行"+d.getLineNo()+"供应商名称不能为空");
        	}
        	if(StringHelper.isNullOrEmpty(lotInfo.getExtendPropC10())) {
        		throw new BusinessException("明细行"+d.getLineNo()+"工厂编码不能为空");
        	}
        	if(StringHelper.isNullOrEmpty(lotInfo.getExtendPropC11())) {
        		throw new BusinessException("明细行"+d.getLineNo()+"工厂名称不能为空");
        	}
        }
        
        /**刷新最小包装量和标签数量*/
        WmsASN wmsAsn = commonDao.load(WmsASN.class, asn.getId());
        refreshDetail(wmsAsn);
        
    }
    public String getBarcode(WmsASNDetail detail) {
    	if(StringHelper.isNullOrEmpty(detail.getLotInfo().getLot())) {
    		throw new BusinessException("收货单明细批次号不能为空，生成条码错误");
    	}
    	
    	return detail.getItem().getCode() + WmsBarCodeParse.KEY_SPLIT 
    			+ detail.getLotInfo().getLot() + WmsBarCodeParse.KEY_SPLIT 
//    			+ detail.getId()
    			+detail.getAsn().getId() //detailid改成asnid
    			+ WmsBarCodeParse.KEY_SPLIT 
    			+ "0"+ WmsBarCodeParse.KEY_SPLIT 
    			+ "0";
    	
    }
    
    @Override
    public void addDetail(Long id, WmsASNDetail detail, double expectedPackQty) {
		// 当前明细对应ASN状态校验
		if(detail.getPoDetail()!=null){
//			throw new BusinessException("this.asnDetail.has.poDetail.can.not.be.modify");
			Double unReceivePackQty = PackageUtils.convertPackQuantity(detail.getPoDetail().getUnReceivedQtyBU(), detail.getPackageUnit());
			if(expectedPackQty> unReceivePackQty){
				throw new BusinessException("this.asnDetail.qty.can.not.more.than.poDetail's.unReceiveQty");		
			}
		}
		WmsASN asn =commonDao.load(WmsASN.class, id);
		
		
		
		//supplier
		if (null == asn.getSupplier()) {
			throw new BusinessException("add.asnDetail.WmsAsn.Supplier.is.null");
		}
		if (!StringUtils.hasText(asn.getUserField5())) {
			throw new BusinessException("add.asnDetail.WmsAsn.userField5.is.null");
		}
		
		WmsLotRule lotRule = detail.getItem().getLotRule();
		
		if (detail.getLotInfo() != null) {
			detail.getLotInfo().prepare(lotRule, detail.getItem(),asn.getCode());
		}else{
			LotInfo lotInfo=new LotInfo();
			detail.setLotInfo(lotInfo);
		}
		detail.getLotInfo().setSupplierCode(asn.getSupplier().getCode());
		detail.getLotInfo().setExtendPropC8(asn.getUserField5());
		detail.getLotInfo().setExtendPropC9(asn.getSupplier().getName());
		detail.getLotInfo().setExtendPropC7(asn.getCode());//ASN单号
	
		if(!StringHelper.isNullOrEmpty(asn.getUserField7())) {
			String factorycode = asn.getUserField7();
			WmsSapFactory factory = (WmsSapFactory)commonDao.findByQueryUniqueResult("FROM WmsSapFactory factory where factory.code=:fc", "fc",factorycode);
			if(null==factory){
				throw new BusinessException("工厂"+factorycode+"未找到");
			}
			
			detail.getLotInfo().setExtendPropC10(factory.getCode());
			detail.getLotInfo().setExtendPropC11(factory.getName());
			
		}
		
		
		if (!asn.getStatus().equals(WmsASNStatus.OPEN)) {
			throw new BusinessException("asn.status.error");
		}
		if (detail.isNew()) {
			detail.setAsn(asn);
			asn.addDetail(detail);
		} else {
			detail = this.commonDao.load(WmsASNDetail.class, detail.getId());
			//单据类型为正常入库单、预留入库单、调拨入库单或补货入库单，不允许修改
			if(asn.getBillType().getCode().equals(WmsAsnGenType.ZCRKD)
					|| asn.getBillType().getCode().equals(WmsAsnGenType.BHRKD)
					|| asn.getBillType().getCode().equals(WmsAsnGenType.DBRKD) 
					|| asn.getBillType().getCode().equals(WmsAsnGenType.YLRKD)){
				throw new BusinessException("单据类型为正常入库单、预留入库单、调拨入库单或补货入库单，不允许修改");
			}
		} 
		
		// 预期收货数量计算(convertFigure为1表示是基本包装，基本包装只能有1个，不为1表示是件装)
		WmsPackageUnit unit = commonDao.load(WmsPackageUnit.class, detail.getPackageUnit().getId());
		if (detail.getPackageUnit().getConvertFigure().intValue() == 1) {
			detail.setExpectedQty(PackageUtils.convertBUQuantity(expectedPackQty, unit));
			detail.setExpectedPackQty(expectedPackQty);
		} else {
			detail.setExpectedPackQty(expectedPackQty);
			detail.setExpectedQty(PackageUtils.convertBUQuantity(expectedPackQty, unit));
		}
		// 标签张数
		WmsMinPackageQty minQty = (WmsMinPackageQty) commonDao.findByQueryUniqueResult(" FROM WmsMinPackageQty wmp WHERE wmp.supplier =:sl and wmp.item =:im",
				new String[]{"sl","im"},
				new Object[]{asn.getSupplier(),detail.getItem()});
		commonDao.store(detail);
	
		//取到标签张数
		double qty = 0d;
		if(minQty != null){
		 qty = DoubleUtils.div(detail.getExpectedPackQty(), minQty.getMinUnit());
		 detail.setLabel(Math.ceil(qty));
		}if(minQty == null){
			detail.setLabel(0d);
		}			
		
		asn.refreshQtyBU();
	}
	@Override
	public void refreshDetail(WmsASN asn) {
		
		for (WmsASNDetail wmsASNDetail : asn.getDetails()) {
			wmsASNDetail.getLotInfo().setExtendPropC12(wmsASNDetail.getItem().getUserFieldD1().toString());//最小包装量
			commonDao.store(wmsASNDetail);
			// 标签张数
			WmsMinPackageQty minQty = (WmsMinPackageQty) commonDao.
					findByQueryUniqueResult(" FROM WmsMinPackageQty wmp WHERE wmp.supplier =:sl and wmp.item =:im",
					new String[]{"sl","im"},
					new Object[]{asn.getSupplier(),wmsASNDetail.getItem()});
			if(minQty==null && !asn.getSupplier().getCode().equals(WmsSupplierCode.XN)) {
				wmsASNDetail.setLabel(0D);
				wmsASNDetail.getLotInfo().setExtendPropC12(wmsASNDetail.getItem().getUserFieldD1().toString());
				commonDao.store(wmsASNDetail);
				continue;
			}else if(minQty==null && asn.getSupplier().getCode().equals(WmsSupplierCode.XN)){
				//虚拟供应商没有找到最小包装量就默认为1
				wmsASNDetail.setLabel(Math.ceil(1));
				wmsASNDetail.getLotInfo().setExtendPropC12(wmsASNDetail.getItem().getUserFieldD1().toString());
				commonDao.store(wmsASNDetail);
				continue;
			}
			//取到标签张数
			double qty = 0d;
			qty = DoubleUtils.div(wmsASNDetail.getExpectedPackQty(), minQty.getMinUnit());
			wmsASNDetail.setLabel(Math.ceil(qty));
			commonDao.store(wmsASNDetail);
		}
		
	}
	
    /**asndetail 和本次收货数量*/
    public Long receivedWriteBackPoAndDo2(WmsASNDetail detail,Double recQty) {
    	WmsASN asn = commonDao.load(WmsASN.class, detail.getAsn().getId());
    	WmsBillType billType = commonDao.load(WmsBillType.class, asn.getBillType().getId());
    	//预留收货回写数量
    	if(WmsAsnGenType.YLRKD.equals(billType.getCode())){
    		WmsReservedOrderDetail rod = commonDao.load(WmsReservedOrderDetail.class, Long.valueOf(detail.getUserField1()));
    		if(rod == null){
    			throw new BusinessException("跟据预留收货单"+asn.getCode()+"未找到对应的预留明细，不能收货");
    		}
    		rod.addShippedQuantityBu(recQty);
			commonDao.store(rod);
    	}
    	WmsTransportOrderDetail tod = null;
    	if(detail.getTransportOrderDetail()!=null){
    		tod = commonDao.load(WmsTransportOrderDetail.class,  detail.getTransportOrderDetail().getId());
    	}
        if (tod != null) {// 若该asn由配货单创建而来 对相应单据进行回写
        	
        	tod.setReceiveQty(tod.getReceiveQty()+recQty);//收货时回写配货单收货数量
        	if(tod.getReceiveQty()<tod.getOldQuantity()){
        		tod.setStatus(WmsTransportOrderDetailStatus.RECEIVING);
        	}else{
        		tod.setStatus(WmsTransportOrderDetailStatus.RECEIVED);
        	}
        	commonDao.store(tod);
        	
            WmsDeliveryOrderDetail dod = commonDao.load(WmsDeliveryOrderDetail.class, tod.getDeliveryOrderDetail().getId());
            PurchaseOrderDetail pod = dod.getPurchaseOrderDetail();
            PurchaseOrder po = pod.getPurchaseOrder();

            pod.addReceivedQty(recQty);
            pod.subAllotQty(recQty);
            po.refreshAllotQty();
            po.refreshReceiveQty();
            
            dod.addDelivedQuantityBu(recQty);
            dod.subTheDeliveryQuantityBu(recQty);//本次交货数量-收货数量
            dod.subQuantity(recQty);
            
            if(pod.getReceivedQty()>=pod.getExpectedQty()){//实收数量大于计划数量  状态改完成
            	pod.setStatus(PurchaseOrderDetailStatus.FINISH);
            }else{
            	pod.setStatus(PurchaseOrderDetailStatus.UNFINISH);
            }
            
            if(dod.getDelivedQuantityBu()>=dod.getPlanQuantityBu()){
            	dod.setStatus(WmsDeliveryOrderDetailStatus.FINISH);
            }else{
            	dod.setStatus(WmsDeliveryOrderDetailStatus.UNFINISH);
            }
            

            if (pod.getReceivedDate() == null) {
                pod.setReceivedDate(new Date());
            }
            
            if (pod.getReceivedLoc() == null) {
            	WmsLocation location = commonDao.load(WmsLocation.class, detail.getAsn().getReceiveLocation().getId());
                pod.setReceivedLoc(location.getCode());
            }
            commonDao.store(pod);
            commonDao.store(dod);
            /**返回交货单ID,ASN处理完之后根据交货单明细收货数量处理交货单的状态*/
            return dod.getDeliveryOrder().getId();
        }
        return null;
    }
    
	@Override
    public void receivedWriteBackPoAndDo(WmsASN asn) {
//    	List<Long> ids = new ArrayList<Long>();//存储交货单ID
//        for (WmsASNDetail detail : asn.getDetails()) {
//            Long id = this.receivedWriteBackPoAndDo2(detail);
//            
//            if(null != id && !ids.contains(id)){
//            	ids.add(id);
//            }
//        }
    }
    
    /**
     * 重写收货方法 用于收货回传SAP
     * param logType   vmi交接到自管仓为 {@link WmsInventoryLogType.VMI_IN},其它的为收货
     */
    @SuppressWarnings("unused")
	public void receiving(WmsASNDetail detail, WmsASN asn,double receiveQty,Long packageUnitId,Long workerId,String status,Boolean isShip,String logType) {		
    	
		
    	if(null==asn.getReceiveLocation()){
    		throw new BusinessException("receieLocation.is.empty.not.allowed.to.receive");
    	}
		LotInfo lotInfo =detail.getLotInfo();
		
		if(StringHelper.isNullOrEmpty(lotInfo.getExtendPropC12()) || Double.valueOf(lotInfo.getExtendPropC12()) <= 0D) {
			throw new BusinessException("未维护最小包装量，请维护最小包装量后点击刷新标签功能");
		}
    	WmsPackageUnit packageUnit = this.commonDao.load(WmsPackageUnit.class, packageUnitId);
	    WmsLocation recLoc = this.commonDao.load(WmsLocation.class, asn.getReceiveLocation().getId());
	    
	    //判断物料的JIT属性与交接属性不能为空
	    WmsItem item = this.commonDao.load(WmsItem.class, detail.getItem().getId());
	    if((item.getUserFieldV1()==null || "".equals(item.getUserFieldV1()==null)) && (item.getUserFieldV2()==null || "".equals(item.getUserFieldV2()==null))){
	    	throw new BusinessException("物料的交接属性与JIT属性不能为空,请维护物料属性");
	    }
	    if(BaseStatus.DISABLED.equals(asn.getSupplier().getStatus())){
	    	throw new BusinessException("供应商已失效不能收货");
	    }
	    
	    tclMessageManager.sendReceiveInfo2SAP(detail,receiveQty);
		//收货时产生收货库存
	    WmsReceivedRecord record = this.createInventory(detail,packageUnit,recLoc,lotInfo,receiveQty,status,workerId,detail.getPallet(),detail.getCarton(),isShip,logType);
	    
	    if(detail.getPoDetail()!=null){
	    //如果明细存在对应的PO,则判断PO是否整单收货
	    	WmsPoDetail poDetail = this.commonDao.load(WmsPoDetail.class,detail.getPoDetail().getId());
	    	WmsPo po = this.commonDao.load(WmsPo.class,poDetail.getPo().getId());
	    	workflowManager.doWorkflow(po, "wmsPoProcess.receive");
		}
	    //收货时判断是否整单收货
		workflowManager.doWorkflow(asn, "wmsASNProcess.receiveAll");
		if(WmsASNStatus.RECEIVED.equals(asn.getStatus())){
			//收货完成后记录ASN结束收货日期
			asn.setEndReceivedDate(new Date());
			this.commonDao.store(asn);
		}
		//JIT下线料 改成入库的时候回传给SAP了，此处注掉
//		if(WmsAsnGenType.ZCRKD.equals(asn.getBillType().getCode()) && WmsItemJITAtt.JIT_DOWNLINE_SETTLE.equals(detail.getItem().getUserFieldV2())){
//			WmsJITDownLineRecord jdr = EntityFactory.getEntity(WmsJITDownLineRecord.class);
//			jdr.setDetail(detail);
//			jdr.setAsnCode(asn.getCode());
//			jdr.setBackQuantity(0D);
//			commonDao.store(jdr);
//		}
		this.receivedWriteBackPoAndDo2(detail, receiveQty); //回写数量
	}
    
    /*整单快捷收货*/
	public void receiveAll(Long asnId,Long workerId,String itemStateId) {
		WmsASN asn = commonDao.load(WmsASN.class, asnId);
		for (WmsASNDetail detail : asn.getDetails()) {
			if (detail.getUnReceivedQtyBU() > 0) {
				//默认库存状态""为合格
				this.detailReceive(detail, detail.getPackageUnit().getId(), detail.getUnReceivedQtyBU(), itemStateId, workerId);
			}
		}
	}
    
    /*整单收货*/
	public void receiveAll(Long asnId,Long workerId) {
		List<Long> ids = new ArrayList<Long>();//交货单ID
		WmsASN asn = commonDao.load(WmsASN.class, asnId);
		String status = "";
		Boolean isShip = Boolean.TRUE;
		Long orderId = 0l;//配货单ID
		String logType = null;//日志类型,如果VMI交接到自管仓,那么产生的日期为VMI入库
		if(null != workerId && workerId == -1L){//vmi传-1
			workerId = 0L;
			logType = WmsInventoryLogType.VMI_IN;
		}
		for (WmsASNDetail detail : asn.getDetails()) {
			if (detail.getUnReceivedQtyBU() > 0) {
				status = detail.getInventoryStatus();
				WmsInventoryState itemState = (WmsInventoryState) commonDao.findByQueryUniqueResult(" FROM WmsInventoryState w WHERE w.name =:sl ",
						new String[]{"sl"},
						new Object[]{detail.getInventoryStatus()});
				if(itemState != null){
					isShip = itemState.getIsShip();
				}
				//默认库存状态""为合格
				this.receiving(detail,asn,PackageUtils.convertPackQuantity(detail.getUnReceivedQtyBU(), 
						detail.getPackageUnit().getConvertFigure(), detail.getItem().getBuPrecision()),
						detail.getPackageUnit().getId(),workerId, status,isShip,logType);
				
				if(null != detail.getTransportOrderDetail()){//如果手工新建则不用回写配货单
					orderId = detail.getTransportOrderDetail().getTransportOrder().getId();
					Long deliverId = detail.getTransportOrderDetail().getDeliveryOrderDetail().getDeliveryOrder().getId();
					if(!ids.contains(deliverId)){
						ids.add(deliverId);
					}
				}
			}
		}
		
		/**根据明细的收货数量更新交货单的状态 fs 2017-8-2 09:29:08*/
        for(Long id : ids){
        	WmsDeliveryOrder deliveryOrder = commonDao.load(WmsDeliveryOrder.class, id);
        	List<WmsDeliveryOrderDetail> details = commonDao.findByQuery("from "
        			+ "WmsDeliveryOrderDetail d where d.deliveryOrder.id="+deliveryOrder.getId());
        	status = deliveryOrder.getStatus();
        	for(WmsDeliveryOrderDetail dod : details){
        		if(dod.getDelivedQuantityBu() > 0
        				&& dod.getDelivedQuantityBu() < dod.getPlanQuantityBu()){
        			//0<交货数量<计划数量,则确定部分收货,不用检查其它明细,跳出循环
        			status = WmsDeliveryOrderStatus.PARTIALRECEIPT;
        			break;
        		}else if(dod.getDelivedQuantityBu() == dod.getPlanQuantityBu() 
        					|| dod.getDelivedQuantityBu() - dod.getPlanQuantityBu() == 0){
        			//计划数量=交货数量,则收货完成,继续检查其它明细
        			status = WmsDeliveryOrderStatus.FINISH;
        		}
        	}
        	deliveryOrder.setStatus(status);
			commonDao.store(deliveryOrder);
        }
		
		/**更新配货单状态*/
		updateTransportOrderStatus(Boolean.TRUE, orderId);
	}
   
	/**
     * RF收货
     * @param asnId
     * @param itemCode
     * @param receiveQty
     * @param itemStateId
     * @param workerId
     */
    @SuppressWarnings("unchecked")
	public void rfReceiving(Long asnId,String itemCode,double receiveQty,Long workerId,Long detailId){
    	WmsASNDetail asnDetail = commonDao.load(WmsASNDetail.class, detailId);
    	
    	String hql2 = "From WmsInventoryState itemState where itemState.masterGroup=:masterGroup and itemState.name=:name";
    	WmsInventoryState itemState = (WmsInventoryState)commonDao.findByQueryUniqueResult(hql2, new String[]{"masterGroup","name"}, 
				new Object[]{asnDetail.getAsn().getCompany().getMasterGroup(),asnDetail.getInventoryStatus()})	;
		if(itemState==null){
			throw new BusinessException("库存状态"+asnDetail.getInventoryStatus()+"未找到");
		}
		//明细收货
		this.detailReceive(asnDetail, asnDetail.getPackageUnit().getId(),receiveQty, itemState.getId()+"",workerId);
//    	//查询出没有收货的明细
//    	StringBuffer sb = new StringBuffer();
//    	String hql = "From WmsASNDetail detail where detail.expectedQty>detail.receivedQty and detail.item.code=:itemCode and detail.asn.id=:asnId ";
//    	sb.append(hql);
//    	//回写收货数量时，有交货单的按交货单的交货日期排序，没有则按收货明细ID排序 -- 升序 
//    	if(asnDetail.getTransportOrderDetail() != null){
//    		sb.append(" ORDER BY detail.transportOrderDetail.deliveryOrderDetail.deliveryOrder.deliveryDate ASC");
//    	}else{
//    		sb.append(" ORDER BY detail.id ASC");
//    	}
//    	List<WmsASNDetail> details = commonDao.findByQuery(sb.toString(),new String[]{"itemCode","asnId"},new Object[]{itemCode,asnId});
//    	for(WmsASNDetail detail:details){
//    		if(receiveQty<=0){
//    			break;
//    		}
//    		double unReceiveQty = DoubleUtils.sub(detail.getExpectedQty(), detail.getReceivedQty());
//    		double detailReceiveQty = 0d;
//    		if(unReceiveQty>=receiveQty){
//    			detailReceiveQty =receiveQty;
//    			receiveQty=0d;
//    		}else{
//    			detailReceiveQty=unReceiveQty;
//    			receiveQty=DoubleUtils.sub(receiveQty,unReceiveQty);
//    		}
//    		
//    		String hql2 = "From WmsInventoryState itemState where itemState.masterGroup=:masterGroup and itemState.name=:name";
//    		
//    		WmsInventoryState itemState = (WmsInventoryState)commonDao.findByQueryUniqueResult(hql2, new String[]{"masterGroup","name"}, 
//    				new Object[]{detail.getAsn().getCompany().getMasterGroup(),detail.getInventoryStatus()})	;
//    		if(itemState==null){
//    			throw new BusinessException("库存状态"+detail.getInventoryStatus()+"未找到");
//    		}
//			
//    		//明细收货
//    		this.detailReceive(detail, detail.getPackageUnit().getId(),detailReceiveQty, itemState.getId()+"",workerId);
//    	}
//    	if(receiveQty>0){
//    		throw new RfBusinessException("收货数量不能大于可收数量");
//    	}
    }
    @SuppressWarnings("unchecked")
	public void allQcRecord(Long asnId, Long qcStatusId,Long workerId) {
		WmsASN asn = commonDao.load(WmsASN.class, asnId);
		String qcStatus = commonDao.load(WmsInventoryState.class, qcStatusId).getName();
		String originalStatus = "";
		Map<String, Object> value = null;
		try{
			value = wmsRuleManager.getRuleTableDetail("R101_质检初始状态规则表",asn.getCompany().getName());
		}catch(Exception ex){
			throw new BusinessException(ex.getLocalizedMessage());
		}
		 if(value!=null) {
			 originalStatus = value.get("质检初始库存状态").toString();
		    if(qcStatus.equals(originalStatus)) {
		    	throw new BusinessException("qc.status.equals.qc.original.inventory.status");
		    }
		 }else {
			 throw new BusinessException("can.not.find.qc.original.status");
		 }
		 // 校验收货数量是否=库存需质检数量
		 String hql = "SELECT sum(receivedQty) FROM WmsReceivedRecord WHERE asnDetail.asn.id=:asnId AND inventoryStatus=:inventoryStatus";
		 Double receivedQty = (Double) commonDao.findByQueryUniqueResult(hql, new String[]{"asnId","inventoryStatus"}, new Object[]{asnId,originalStatus});
		 hql = "SELECT sum(qty) FROM WmsInventory WHERE location.id=:locationId AND status=:status AND relatedBillCode=:relatedBillCode AND operationStatus='NORMAL' ";
		 Double invQty = (Double) commonDao.findByQueryUniqueResult(hql, new String[]{"locationId","status","relatedBillCode"}, new Object[]{asn.getReceiveLocation().getId(),originalStatus,asn.getCode()});
		 
		 if(receivedQty==null){
			 throw new BusinessException("can.not.find.receive.record");
		 }else if(invQty==null) {
			 throw new BusinessException("can.not.find.qc.inventory");
		 }
		 else if(receivedQty.doubleValue()!=invQty.doubleValue()) {
			 throw new BusinessException("recieved.qc.qty.not.equals.inventory.qc.qty");
		 }
		 // 更新收货记录库存状态
		 hql = "FROM WmsReceivedRecord WHERE asnDetail.asn.id=:asnId AND inventoryStatus=:inventoryStatus";
		 List<WmsReceivedRecord> recordList = commonDao.findByQuery(hql, new String[]{"asnId","inventoryStatus"}, new Object[]{asnId,originalStatus});
		 WmsASNDetail asnDetail = null;
		 for(WmsReceivedRecord record:recordList) {
			 record.setInventoryStatus(qcStatus);
			 commonDao.store(record);
			 //更新明细实际质检数量，更新ASN实际质检数量
			 asnDetail = record.getAsnDetail();
			 asnDetail.setActualQcQty(asnDetail.getReceivedQty());
			 commonDao.store(asnDetail);
			 asn.setQuantityQty(DoubleUtils.add(asn.getQuantityQty(), asnDetail.getActualQcQty(), asnDetail.getItem().getBuPrecision()));
			 commonDao.store(asn);
			 tclMessageManager.sendQcRecordInfo2SAP(asnDetail, qcStatus, record.getReceivedQty());
		 }
		 // 更新收货库存的库存状态
		 hql = "FROM WmsInventory WHERE location.id=:locationId AND status=:status AND relatedBillCode=:relatedBillCode AND operationStatus='NORMAL' ";
		 List<WmsInventory> invList = commonDao.findByQuery(hql, new String[]{"locationId","status","relatedBillCode"}, new Object[]{asn.getReceiveLocation().getId(),originalStatus,asn.getCode()});
		 if(invList!=null && invList.size()>0) {
			 for(WmsInventory inv:invList) {
				 WmsQcRecord qcRecord = new WmsQcRecord();
				 qcRecord.setInventoryStatusOld(originalStatus);
				 qcRecord.setInventoryStatusNew(qcStatus);
				 qcRecord.setItem(inv.getItem());
				 qcRecord.setLocation(asn.getReceiveLocation());
				 qcRecord.setLotInfo(inv.getItemKey().getLotInfo());
				 qcRecord.setQcQty(inv.getQty());
				 qcRecord.setRelatedBill(inv.getRelatedBillCode());
				 qcRecord.setWorker(workerId==null?null:commonDao.load(WmsWorker.class, workerId));
				 commonDao.store(qcRecord);
				 WmsInventoryDto inventoryDto = inventoryManager.getWmsInventoryDto(inv,inv.getQty());
			     inventoryDto.setType(WmsInventoryLogType.STATUS_CHANGE);
			     inventoryManager.out(inv, inv.getQty(), WmsInventoryLogType.STATUS_CHANGE, "质检");
			     inventoryDto.setStatus(qcStatus);
				 inventoryManager.in(inventoryDto);
			 }
		 }
	}

	@SuppressWarnings("unchecked")
	@Override
	public void singleQcRecord(Long recordId, Long qcStatusId, Long workerId,double qcNumber) {
		WmsReceivedRecord record = commonDao.load(WmsReceivedRecord.class, recordId);
		WmsASN asn = commonDao.load(WmsASN.class, record.getAsnDetail().getAsn().getId());
		WmsASNDetail asnDetail = record.getAsnDetail();
		String qcStatus = commonDao.load(WmsInventoryState.class, qcStatusId).getName();
		// 登记数量不能超过收货记录收货数量
		if(qcNumber>record.getReceivedPackQty().doubleValue()) {
			throw new BusinessException("qc.qty.greater.received.qty");
		}
		String originalStatus = "";
		Map<String, Object> value = null;
		try{
			value = wmsRuleManager.getRuleTableDetail("R101_质检初始状态规则表",asn.getCompany().getName());
		}catch(Exception ex){
			throw new BusinessException(ex.getLocalizedMessage());
		}
		
		// 查询质检初始状态规则表，判断当前收货记录的状态是否在规则表中存在
		if(value==null) {
			throw new BusinessException("qc.origial.status.not.maintain");
		}else if(value.get("质检初始库存状态").toString().equals(qcStatus)){
			throw new BusinessException("qc.status.equals.qc.original.inventory.status");
		}else if(asnDetail.getActualQcQty()>0 && !value.get("质检初始库存状态").toString().equals(record.getInventoryStatus())){
			throw new BusinessException("is.already.qc.record");
		}else if(!value.get("质检初始库存状态").toString().equals(record.getInventoryStatus())){
			//throw new BusinessException("qc.origial.status.not.equals.receive.status");
			throw new BusinessException("can.not.find.receive.record");
		}
		originalStatus = value.get("质检初始库存状态").toString();
		// 判断当前收货记录的状态是否在规则表中存在
		if(originalStatus.equals(qcStatus)) {
			throw new BusinessException("qc.status.equals.qc.original.inventory.status");
		}
//		根据ASN、货品、库存状态、库存属性、库位【ASN维护字段收货库位】查询库存，循环库存结果list：
//		①、当登记数量>=库存数量时，直接修改库存对应库存状态;
//		②、当登记数量<库存数量时，将当前库存拆分成两条，一条原状态库存，一质检后状态库存【数量为登记数量】，拆分库存时，使用库存公共方法；
//		③、循环扣减登记数量；
//		循环完库存结果list时，当登记数量仍然>0，则提示错误""可用库存数量不足,登记失败!"";
		String hql = "SELECT sum(qty) FROM WmsInventory WHERE location.id=:locationId AND status=:status AND relatedBillCode=:relatedBillCode AND itemKey.id=:itemKeyId AND operationStatus='NORMAL' ";
		Double invQty = (Double) commonDao.findByQueryUniqueResult(hql, new String[]{"locationId","status","relatedBillCode","itemKeyId"}, new Object[]{asn.getReceiveLocation().getId(),originalStatus,asn.getCode(),record.getItemKey().getId()});
		if(invQty==null || qcNumber>invQty.doubleValue()) {
			throw new BusinessException("available.inventory.qty.not.enough");
		}
		hql = "FROM WmsInventory WHERE location.id=:locationId AND status=:status AND relatedBillCode=:relatedBillCode AND itemKey.id=:itemKeyId AND operationStatus='NORMAL' ";
		List<WmsInventory> invList = commonDao.findByQuery(hql, new String[]{"locationId","status","relatedBillCode","itemKeyId"}, new Object[]{asn.getReceiveLocation().getId(),originalStatus,asn.getCode(),record.getItemKey().getId()});
		if(invList!=null && invList.size()>0) {
			double changeQty = 0D;
			double num = qcNumber;
			WmsQcRecord qcRecord = new WmsQcRecord();
			qcRecord.setInventoryStatusOld(originalStatus);
			qcRecord.setInventoryStatusNew(qcStatus);
			qcRecord.setItem(record.getItemKey().getItem());
			qcRecord.setLocation(asn.getReceiveLocation());
			qcRecord.setLotInfo(record.getItemKey().getLotInfo());
			qcRecord.setQcQty(num);
			qcRecord.setRelatedBill(asn.getCode());
			qcRecord.setWorker(workerId == null ? null : commonDao.load(WmsWorker.class, workerId));
			for(WmsInventory inv:invList) {
				if(num<=0) {
					break;
				}else if(num>=inv.getQty().doubleValue()) {
					changeQty = inv.getQty().doubleValue();
					
				}else{
					changeQty = num;
				}
				num = DoubleUtils.sub(num,changeQty,asnDetail.getItem().getBuPrecision());
				WmsInventoryDto inventoryDto = inventoryManager.getWmsInventoryDto(inv,changeQty);
				inventoryDto.setType(WmsInventoryLogType.STATUS_CHANGE);
				inventoryManager.out(inv, changeQty, WmsInventoryLogType.STATUS_CHANGE, "质检");
				inventoryDto.setStatus(qcStatus);
				inventoryManager.in(inventoryDto);
				asnDetail.setActualQcQty(DoubleUtils.add(asnDetail.getActualQcQty(),changeQty,asnDetail.getItem().getBuPrecision()));
				asn.setQuantityQty(DoubleUtils.add(asn.getQuantityQty(),changeQty,asnDetail.getItem().getBuPrecision()));
			}
			if(num>0) {
				throw new BusinessException("available.inventory.qty.not.enough");
			}
			commonDao.store(asnDetail);
			commonDao.store(asn);
			commonDao.store(qcRecord);
			//当收货记录的收货数量>质检数量时,拆分收货记录
			if(record.getReceivedQty().doubleValue()>qcNumber) {
				WmsReceivedRecord newRecord = new WmsReceivedRecord();
				try {
					copyProperty(record,newRecord);
				} catch (Exception e) {
					new BusinessException(e.getLocalizedMessage());
				}
				newRecord.setId(null);
				newRecord.setReceivedQty(qcNumber);
				newRecord.setReceivedPackQty(PackageUtils.convertBUQuantity(newRecord.getReceivedQty(), asnDetail.getPackageUnit()));
				newRecord.setInventoryStatus(qcStatus);
				record.setReceivedQty(DoubleUtils.sub(record.getReceivedQty().doubleValue(),qcNumber,asnDetail.getItem().getBuPrecision()));
				record.setReceivedPackQty(PackageUtils.convertBUQuantity(record.getReceivedQty(), asnDetail.getPackageUnit()));
				commonDao.store(newRecord);
				commonDao.store(record);
			}else{
			//当收货记录的收货数量等于质检记录时,直接修改质检状态;
				record.setInventoryStatus(qcStatus);
				commonDao.store(record);
			}
		}
		
		tclMessageManager.sendQcRecordInfo2SAP(asnDetail, qcStatus, qcNumber);
	}
	
	@SuppressWarnings("unused")
	public void storeASN(WmsASN asn) {
		if (asn.isNew()) {
			Date startReceivedDate = asn.getStartReceivedDate();
			Date endReceivedDate = asn.getEndReceivedDate();
			if(startReceivedDate != null && endReceivedDate != null && startReceivedDate.after(endReceivedDate)) {
				throw new BusinessException("this.asn.start.received.date.after.end.received.date");
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd"); 
			WmsWarehouse wh = (WmsWarehouse)commonDao.findByQueryUniqueResult("FROM WmsWarehouse warehouse WHERE warehouse.baseOrganization.id = :baseOrganizationId", 
					new String[] {"baseOrganizationId"}, new Object[] {BaseOrganizationHolder.getThornBaseOrganization().getId()});
			asn.setWarehouse(wh);
			String asnCode = wmsBussinessCodeManager.generateCodeByRule(wh, asn.getBillType().getCode());
			asn.setCode(asnCode);
			if(WmsAsnGenType.SCTLD.equals(asn.getBillType().getCode())){
				String hql = "FROM ProductionOrder order WHERE order.code =:code ";
				ProductionOrder order = (ProductionOrder) commonDao.findByQueryUniqueResult(hql, "code", asn.getCustomerBill());
				if(order == null){
					throw new BusinessException("生产退料单客户单号必须有值且为对应的生产订单工单号");
				}
			}
			workflowManager.doWorkflow(asn, "wmsASNProcess.new");
		}
		if(asn.getBillType().getCode().equals(WmsAsnGenType.ZCRKD)
				|| asn.getBillType().getCode().equals(WmsAsnGenType.BHRKD)
				|| asn.getBillType().getCode().equals(WmsAsnGenType.DBRKD) 
				|| asn.getBillType().getCode().equals(WmsAsnGenType.YLRKD)
				|| asn.getBillType().getCode().equals(WmsAsnGenType.SCTLD)){
			throw new BusinessException("单据类型为正常入库单、预留入库单、调拨入库单、生产退料单或补货入库单，不允许新建或修改");
		}
		//查找Asn明细
		String hql ="FROM WmsASNDetail w WHERE w.asn.id =:id";
		List<WmsASNDetail> detail = commonDao.findByQuery(hql, new String[]{"id"}, new Object[]{asn.getId()});
		if(detail.size() > 0){
			throw new BusinessException("asn存在明细，不允许修改。请删除asn明细之后,在修改!!!");
		}
		StringHelper.assertNullOrEmpty(asn.getUserField7(), "工厂不能为空");
		/**其它入库单的ASN 项目类别必须是标准*/
		if(asn.getBillType().getCode().equals(WmsAsnGenType.QTRKD)
				&& asn.getUserField5().equals("2")){
			throw new BusinessException("其它入库单的项目类别必须是标准");
		}
		if(WmsAsnGenType.PYRKD.equals(asn.getBillType().getCode()) && StringHelper.isNullOrEmpty(asn.getUserField6())){
			throw new BusinessException("盘盈入库单成本中心必填");
		}
	}
	
	

	/**
	 * 打印标签
	 */
	public Map printLabel(List<WmsASN> wmsAsns){
		Map result = new HashMap();
		Map<Long,String> reportValue = new HashMap<Long, String>();
		Map printNums = new HashMap(); 
		for (WmsASN wmsAsn : wmsAsns) {	
			String hql = "SELECT w.id FROM WmsASNDetail w where w.asn.id =:id";
			List<Long> ids = commonDao.findByQuery(hql, new String[]{"id"}, new Object[]{wmsAsn.getId()});
			for(Long id : ids){
				WmsASNDetail asnDetail = commonDao.load(WmsASNDetail.class, id);
				if(asnDetail.getLabel() == null || asnDetail.getLabel() == 0D){
					throw new BusinessException("请维护最小包装量");
				}
				String params = ";id="+id;
				reportValue.put(id, "printLabel.raq&raqParams="+params);
				printNums.put(id, asnDetail.getLabel().intValue());
			}
		}
		if(!reportValue.isEmpty()){
			result.put(IPage.REPORT_VALUES, reportValue);
			result.put(IPage.REPORT_PRINT_NUM, printNums);
		}
		return result;	
	}
	
	
	
	/**
	 * 条码补打
	 */
	public boolean print(Map map){
		List<Long> ids = (List<Long>) map.get("parentIds");
		for (Long id : ids) {	
		WmsBarCodeRepPrintRecord record  = commonDao.load(WmsBarCodeRepPrintRecord.class, id);
			record.setPrintFlag(Boolean.TRUE);
			record.setPrintTime(new Date());
			commonDao.store(record);
	}
		return true;	
	}

	
	public boolean printSupplierLabel(Map map){
		List<Long> ids = (List<Long>) map.get("parentIds");
		for(Long id : ids){
			String hql = "FROM WmsASNDetail w WHERE w.asn.id =:id";
			List<WmsASNDetail> details = commonDao.findByQuery(hql, "id", id);
			for(WmsASNDetail detail : details ){
				if(detail.getLabel() == null || detail.getLabel() == 0D){
					throw new BusinessException("请维护最小包装量");
				}
			}
		}		
		return true;
	}
	
	/*单一明细收货*/
	public void detailReceive(WmsASNDetail detail, Long packageUnitId,double quantity, String itemStateId,Long workerId) {

		String status = "";
		Boolean isShip = Boolean.TRUE;
		if(StringHelper.isNullOrEmpty(itemStateId)){
			status = BaseStatus.NULLVALUE;//默认库存状态为空
		}else {
			WmsInventoryState itemState = commonDao.load(WmsInventoryState.class, Long.valueOf(itemStateId));
			status = itemState.getName();
			isShip = itemState.getIsShip();
		}
		String logType = null;//日志类型,如果VMI交接到自管仓,那么产生的日期为VMI入库
		if(null != workerId && workerId == -1L){//vmi传-1
			workerId = 0L;
			logType = WmsInventoryLogType.VMI_IN;
		}
		WmsASN asn = this.commonDao.load(WmsASN.class,  detail.getAsn().getId());
		this.receiving(detail,asn,quantity, packageUnitId,workerId,status,isShip,logType);
		
		//		super.detailReceive(detail, packageUnitId, quantity, itemStateId, workerId);
	
		/**回写配货单状态*/
		List<WmsASNDetail> asnDetails = commonDao.findByQuery("from WmsASNDetail a where a.asn.id="+detail.getAsn().getId());
		Boolean isReceiveAll = Boolean.TRUE;//是否收货完成
		for(WmsASNDetail a : asnDetails){
			if(a.getExpectedQty() - a.getReceivedQty() > 0){
				isReceiveAll = Boolean.FALSE;
				break;
			}
		}
		/**更新配货单状态*/
		if(null != detail.getTransportOrderDetail()){
			updateTransportOrderStatus(isReceiveAll,detail.getTransportOrderDetail().getTransportOrder().getId());
			/***/
			WmsDeliveryOrderDetail orderDetail = commonDao.load(WmsDeliveryOrderDetail.class,
								detail.getTransportOrderDetail().getDeliveryOrderDetail().getId());
////			System.out.println(orderDetail.getTheDeliveryQuantityBu()+"=="+orderDetail.getDelivedQuantityBu());
////			orderDetail.subTheDeliveryQuantityBu(quantity);//本次交货数量-收货数量
////			System.out.println(orderDetail.getTheDeliveryQuantityBu()+"=="+orderDetail.getDelivedQuantityBu());
////			orderDetail.addDelivedQuantityBu(quantity);//已交货数量+收货数量
////			System.out.println(orderDetail.getTheDeliveryQuantityBu()+"=="+orderDetail.getDelivedQuantityBu());
//			commonDao.store(orderDetail);
			
			WmsDeliveryOrder deliveryOrder = commonDao.load(WmsDeliveryOrder.class, orderDetail.getDeliveryOrder().getId());
	    	String hql = "select sum(d.planQuantityBu),sum(d.delivedQuantityBu) from WmsDeliveryOrderDetail d where d.deliveryOrder.id=:id";
	    	List<Object[]> values = commonDao.findByQuery(hql,"id",deliveryOrder.getId());
	    	Double planQty = values.get(0)[0] == null ? 0d : (Double) values.get(0)[0];//计划数量
	    	Double deliverQty = values.get(0)[1] == null ? 0d : (Double) values.get(0)[1];//已交货数量
//	    	String status = "";
	    	if(planQty - deliverQty > 0 && deliverQty > 0){//计划数量>已交货数量 && 已交货数量>0  部分收货
	    		status = WmsDeliveryOrderStatus.PARTIALRECEIPT;
	    	}else if(planQty - deliverQty <= 0){//计划数量 == 交货数量  完成
	    		status = WmsDeliveryOrderStatus.FINISH;
	    	}else if(deliverQty == 0){//已交货数量==0  生效
	    		status = WmsDeliveryOrderStatus.ACTIVE;
	    	}
			deliveryOrder.setStatus(status);
			commonDao.store(deliveryOrder);
		}
	}
	
	/**
	 * 
	 * @param isReceiveAll  true=收货完成,false=部分收货
	 * @param orderId 配货单ID
	 */
	void updateTransportOrderStatus(Boolean isReceiveAll,Long orderId){
		WmsTransportOrder order = commonDao.load(WmsTransportOrder.class, orderId);
		if(order == null){
			return ;
		}
		String status = WmsTransportOrderStatus.RECEIVED;
		/**一个配送单可能生成多个ASN,所以isReceiveAll为true需要再校验下配送单明细的已交货数量=计划交货数量*/
		if(!isReceiveAll){
			status = WmsTransportOrderStatus.RECEIVING;
		}else{
			for(WmsTransportOrderDetail detail : order.getDetails()){
				WmsDeliveryOrderDetail wod = commonDao.load(WmsDeliveryOrderDetail.class, detail.getDeliveryOrderDetail().getId());
				if(detail.getQuantity() - wod.getDelivedQuantityBu() > 0 ){//明细未全部交货
					status = WmsTransportOrderStatus.RECEIVING;
					break;
				}
			}
		}
		order.setStatus(status);
		commonDao.store(order);
	}
	
	
	
	
	public void removeDetails(WmsASNDetail detail) {
		/*加载ASN,并更新删除明细后的asn总数*/
		WmsASN asn = commonDao.load(WmsASN.class, detail.getAsn().getId());
        
		//单据类型为正常入库单、预留入库单、调拨入库单或补货入库单，删除明细
		if(asn.getBillType().getCode().equals(WmsAsnGenType.ZCRKD)
				|| asn.getBillType().getCode().equals(WmsAsnGenType.BHRKD)
				|| asn.getBillType().getCode().equals(WmsAsnGenType.DBRKD) 
				|| asn.getBillType().getCode().equals(WmsAsnGenType.YLRKD)){
			throw new BusinessException("单据类型为正常入库单、预留入库单、调拨入库单或补货入库单，不允许删除");
		}
		
		asn.removeDetail(detail);
		asn.refreshQtyBU();
		if (null != asn.getPo()) {
			/*检查单一PO绑定的明细是否全部删除,是则解绑PO*/
			Double expectedQtyTotal = (Double)this.commonDao.findByQueryUniqueResult(
					"select sum(ad.expectedQty) from WmsASNDetail ad where ad.asn.id = :asnId "
					+ "and exists(select 'x' from WmsPoDetail pd where pd.id = ad.poDetail.id and pd.po.id = :poId)", 
					new String[]{"asnId","poId"}, 
					new Object[]{asn.getId(),asn.getPo().getId()});
			if (null == expectedQtyTotal) {
				asn.setPo(null);
			}
		}
		this.commonDao.store(asn);
	}
	
	
	/**
	 * 打印asn明细
	 */
	public Map printWmsASNDetail(WmsASNDetail detail,Integer quantity){
		/**记录供应商打印的张数*/
		String supplier = detail.getLotInfo().getSupplierCode();//供应商编码
		String supplierName = detail.getLotInfo().getExtendPropC9();//供应商名称
//		String ip = ReportPrintServlet.getLocalIp();
		String type="ASN打印标签";
		/**保存到数据库*/
		saveSupplyCount(supplier, null, quantity.doubleValue(), supplierName, detail,1,detail.getAsn().getCode(),type);
		
		Map result = new HashMap();
		Map<Long,String> reportValue = new HashMap<Long, String>();
		Map printNums = new HashMap(); 
		if(detail.getLabel() == null || detail.getLabel() == 0D){
			throw new BusinessException("请维护最小包装量");
		}
		//获取asn明细Id
		Long id = detail.getId();
		String params = ";id="+id;
		reportValue.put(id, "printLabel.raq&raqParams="+params);
			
		if(!reportValue.isEmpty()){
			result.put(IPage.REPORT_VALUES, reportValue);
			result.put(IPage.REPORT_PRINT_NUM, quantity);
		}
		return result;
	}
	
	
	 /**
     *打印asn明细,判断标签数量
     */
	public boolean printWmsSupplierASNDetail(Map map){
	    //获取asn明细Id	
		List<Long> ids = (List<Long>) map.get("parentIds");
		for(Long id : ids){
			WmsASNDetail detail = commonDao.load(WmsASNDetail.class, id);
			if(detail.getLabel() == null || detail.getLabel() == 0D){
				throw new BusinessException("请维护最小包装量");
			}
		}		
		return true;	
		
	}
	
	/**
	 * 初始化方法
	 */
	public RowData getQuantity(Map map){
		Long id = (Long) map.get("id");
		WmsASNDetail detail = commonDao.load(WmsASNDetail.class, id);
		int quantity = detail.getLabel().intValue();
		RowData r = new RowData();
		r.addColumnValue(quantity);
		return r;
		
	}
	
	//打印
	public Map printSupplierWmsASN(List<WmsASNDetail> details) {
		Map result = new HashMap();
		Map<Long, String> reportValue = new HashMap<Long, String>();
		Map printNums = new HashMap();
		for (WmsASNDetail detail : details) {
			if (detail.getLabel() == null || detail.getLabel() == 0D) {
				throw new BusinessException("请维护最小包装量");
			}
			// 获取asn明细Id
			Long id = detail.getId();
			String params = ";id=" + id;
			reportValue.put(id, "printLabel.raq&raqParams=" + params);
			printNums.put(id, detail.getLabel().intValue());
		}
		if (!reportValue.isEmpty()) {
			result.put(IPage.REPORT_VALUES, reportValue);
			result.put(IPage.REPORT_PRINT_NUM, printNums);
		}
		return result;

	}
	/**
	 * 重写创建上架作业单方法，记录保管员
	 */
	public List<WmsWorkDoc> createWorkDocByAsn(WmsASN asn,String wareHouserCode){
		
		String hql = "from WmsWorkDoc workDoc where workDoc.relatedBillCode =:relatedBillCode and workDoc.status = 'READY_ALLOCATE'";
		List<WmsWorkDoc> workDocs = this.commonDao.findByQuery(hql,"relatedBillCode",asn.getCode());
		if(workDocs.size() > 0 && !workDocs.isEmpty()){
			return workDocs;
		}
		workDocs.clear();
		Map<ThornUser, List<WmsASNDetail>> map = new HashMap<ThornUser, List<WmsASNDetail>>();
//		//收货时，创建workDoc
		WmsWarehouse warehouse =(WmsWarehouse)commonDao.load(WmsWarehouse.class, asn.getWarehouse().getId());
		asn = commonDao.load(WmsASN.class, asn.getId());
		for(WmsASNDetail detail : asn.getDetails()){
			Long itemId = detail.getItem().getId();
			String facCode = asn.getUserField7();
			
			ThornUser user = findKeeperByAsn(detail.getId(),itemId,facCode,warehouse.getId());
			List<WmsASNDetail> details = new ArrayList<WmsASNDetail>();
			if(map.get(user) != null){//保管员已经存在map里
				details = map.get(user);
			}
			details.add(detail);
			map.put(user, details);
		}
		Set<ThornUser> keys = map.keySet();
		for(ThornUser k : keys){//一个key生成一个上架单
			WmsWorkDoc workDoc = new WmsWorkDoc();
			workDoc.setType(WmsWorkDocType.PUTAWAY);
			workDoc.setWarehouse(warehouse);
			//单号编码规则生成作业单号
			String workDocCode = wmsBussinessCodeManager.generateCodeByRule(warehouse,workDoc.getType());
			workDoc.setCode(workDocCode);
			workDoc.setKeeper(k);
			workDoc.setRelatedBillCode(asn.getCode());
			workDoc.setStatus(WmsWorkDocStatus.READY_ALLOCATE);
			this.commonDao.store(workDoc);
			workDocs.add(workDoc);
		}
//		String hql = "from WmsWorkDoc workDoc where workDoc.relatedBillCode =:relatedBillCode and workDoc.status = 'READY_ALLOCATE'";
//		WmsWorkDoc workDoc = new WmsWorkDoc();
//		List<WmsWorkDoc> workDocs = this.commonDao.findByQuery(hql,"relatedBillCode",asn.getCode());
//		if(workDocs.size()>0&&!workDocs.isEmpty()){
//			workDoc = workDocs.get(0);
//		}else{
//			workDoc.setType(WmsWorkDocType.PUTAWAY);
//			workDoc.setWarehouse(warehouse);
//			//单号编码规则生成作业单号
//			String workDocCode = wmsBussinessCodeManager.generateCodeByRule(warehouse,workDoc.getType());
//			workDoc.setCode(workDocCode);
//			workDoc.setKeeper(asn.getKeeper());
//			workDoc.setRelatedBillCode(asn.getCode());
//			workDoc.setStatus(WmsWorkDocStatus.READY_ALLOCATE);
//			this.commonDao.store(workDoc);
//		}
		return workDocs;
	}
	
	/**
     *  手工创建上架单
     * @param wmsAsn
     */
	public List<WmsWorkDoc> manualCreateWorkDocs(WmsASN wmsAsn) {
//		String hql = "from WmsInventory a where a.relatedBillCode=:relatedBillCode and a.status='待检'";
//		List<Long> invIds = commonDao.findByQuery(hql,"relatedBillCode",wmsAsn.getCode());
//		if(invIds.size() > 0){
//			throw new BusinessException("待检库存无法上架,请先做质检!!");
//		}
				
//		WmsWorkDoc workDoc = super.manualCreateWorkDoc(wmsAsn);
		
		WmsWarehouse warehouse = this.commonDao.load(WmsWarehouse.class,  wmsAsn.getWarehouse().getId());
		//生成上架单
		List<WmsWorkDoc> workDocs = this.createWorkDocByAsn(wmsAsn, warehouse.getCode());	
		
		String hql = "from WmsInventory inv where inv.relatedBillCode=:asnCode "
				+ " and inv.location.id=:locId and inv.operationStatus='NORMAL' "
				+ " and inv.task IS NULL and inv.qty > 0 and inv.status !='待检' ";
		List<WmsInventory> invList = this.commonDao.findByQuery(hql,new String[]{"asnCode","locId"},new Object[]{wmsAsn.getCode(),wmsAsn.getReceiveLocation().getId()});
		if(invList.size() > 0){
			for(WmsInventory inventory : invList){
				WmsTask task = this.createWorkTask(wmsAsn,inventory);
				inventory.setTask(task);
				this.commonDao.store(inventory);
				//找到保管员,根据保管员找到对应上架单,然后绑定task
				ThornUser user = findKeeperByAsn(null, inventory.getItem().getId(), inventory.getItemKey().getLotInfo().getExtendPropC10(), inventory.getWarehouse().getId());
				Boolean flag = Boolean.FALSE;//标记有没有找到对应的上架作业单
				for(WmsWorkDoc workDoc : workDocs){
					if(user.getId() == workDoc.getKeeper().getId()){
						task.setWorkDoc(workDoc);
						this.commonDao.store(task);
						workDoc.refreshQuantity(task.getPlanQty(),task.getItem().getBuPrecision());//刷新上架作业单数量
						this.commonDao.store(workDoc);
						flag = Boolean.TRUE;
						break;
					}
				}
				if(!flag){
					throw new BusinessException("根据保管员"+user.getName()+"没有找到对应的上架单!!");
				}
				inventory.setOperationStatus(WmsInventoryOperationStatus.READY_OUT);//修改库存操作状态
				this.commonDao.store(inventory);
			}
		}
		else{
			throw new BusinessException("该ASN已创建作业单或者ASN未做质检!");
		}
		return workDocs;
	}
	
	//修改保管员
	public void modifyKeeper(WmsASN wmsAsn){
		commonDao.store(wmsAsn);
	}

	@Override
	public void closeDeliverOrder(WmsASN asn) {
		Set<WmsASNDetail> details = asn.getDetails();
		String status = "-";//asn的状态,根据明细的计划数量来判断
		for(WmsASNDetail d : details){
			WmsTransportOrderDetail transportOrderDetail = d.getTransportOrderDetail();
			Double restQty = DoubleUtils.sub(d.getExpectedQty(), d.getReceivedQty());//期待数量-收货数量=可回退给交货单明细的数量
			if(null == transportOrderDetail || restQty <= 0){//如果不是配货单生成的asn或者asn明细已收货完成,则处理下条明细
				status = WmsASNStatus.RECEIVED;//有asn明细已经收货完成
				continue;
			}
			transportOrderDetail.setStatus(WmsTransportOrderDetailStatus.RECEIVED);
			WmsDeliveryOrderDetail deliveryOrderDetail = transportOrderDetail.getDeliveryOrderDetail();
			Double phQty = deliveryOrderDetail.getQuantity();//配货数量
			if(phQty == 0){
				continue;
			}
			
			if(phQty - restQty >= 0){//配货数量>=可回退数量,直接减可回退数量
				deliveryOrderDetail.subQuantity(restQty);//交货单明细：配货数量
				d.subExceptQty(restQty);//asn明细：计划数量-回退数量
				
				transportOrderDetail.subQuantity(restQty);
				
				commonDao.store(deliveryOrderDetail);
				commonDao.store(d);
				commonDao.store(transportOrderDetail);
				
				if(d.getExpectedQty() > 0){
					status = WmsASNStatus.RECEIVED;//有asn明细已经收货完成
				}
			}else{//配货数量<可回退数量,减去
				throw new BusinessException("配货数量["+phQty+"]不能小于回退数量"+restQty+",请检查!!");
			}
		}
		asn.setStatus(status);//asn状态=收货完成
		if(status.equals("-")){
			asn.setStatus(WmsASNStatus.CANCELED);//明细的计划数量全是0,asn状态=取消
		}
		commonDao.store(asn);
	}
	
	/**
	 * 根据asn明细找保管员,先根据物料+仓库+工厂找,没找到再根据物料+仓库+工厂为空的条件再找一次,还没找到就报错
	 * @param asnDetailId
	 * @param itemId
	 * @param facCode
	 * @param warehouseId
	 * @return
	 */
	private ThornUser findKeeperByAsn(Long asnDetailId,Long itemId,String facCode,Long warehouseId){
		String hql = "from WmsItemKeeper w where w.warehouse.id=:wId and w.item.id=:itemId and w.factory.code=:code";
		
		List<WmsItemKeeper> keeps = commonDao.findByQuery(hql,new String[]{"wId","itemId","code"},
							new Object[]{warehouseId,itemId,facCode});
		if(keeps.size() <= 0){
			hql = "from WmsItemKeeper w where w.warehouse.id=:wId and w.item.id=:itemId and w.factory is null";
			keeps = commonDao.findByQuery(hql,new String[]{"wId","itemId"},new Object[]{warehouseId,itemId});
			if(keeps.size() <= 0){
//				WmsItem item = commonDao.load(WmsItem.class, itemId);
				throw new BusinessException("根据asn明细序号为["+asnDetailId+"]的物料+工厂"+facCode+"没有找到对应的保管员,请检查!!");
			}else{
				return keeps.get(0).getKeeper();
			}
		}else{
			return keeps.get(0).getKeeper();
		}
	}
	

	/**
	 * 收货时产生库存
	 */
	public WmsReceivedRecord createInventory(WmsASNDetail detail,WmsPackageUnit packageUnit,WmsLocation recLoc,LotInfo lotInfo,double receiveQty,
			String status,Long workerId,String pallet,String carton,Boolean isShip,String logType){
		
		detail = this.commonDao.load(WmsASNDetail.class, detail.getId());
		WmsASN asn = commonDao.load(WmsASN.class, detail.getAsn().getId());
		// 根据ASN明细信息创建ItemKey信息
		WmsItemKey itemKey = wmsItemManager.getItemKey(detail.getAsn().getWarehouse(), 
				((asn.getCustomerBill()==null||"".equals(asn.getCustomerBill())) ? asn.getCode():asn.getCustomerBill()), detail.getItem(), lotInfo);
//		WmsItemKey itemKey = this.commonDao.load(WmsItemKey.class, 1L);
		WmsReceivedRecord record = getReceivedRecord(detail, packageUnit, itemKey, status, 
				recLoc,workerId,pallet,carton);
		WmsItem item = this.commonDao.load(WmsItem.class, detail.getItem().getId());
		WmsWarehouse warehouse = this.commonDao.load(WmsWarehouse.class, asn.getWarehouse().getId());
		WmsCompany company = this.commonDao.load(WmsCompany.class, asn.getCompany().getId());
		double receiveQtyBU = PackageUtils.convertBUQuantity(receiveQty, packageUnit, item);//包装换算

		Map<String, Object> extendValue = null;
		// 超收验证
		try {
			extendValue = wmsRuleManager.getRuleTableDetail(
					"R101_货品超收规则表", asn.getCompany().getName(), asn
							.getBillType().getName(), detail.getItem()
							.getClass1());
		} catch (Exception ex) {
			//throw new BusinessException(ex.getLocalizedMessage());
		}
		if (extendValue != null) {
			Double extendRate = DoubleUtils
					.div(Double.parseDouble(extendValue.get("超收比例").toString()),
							100);
			// ①、ASN已绑定PO明细,其他绑定该PO明细不超收时，判断ASN明细.已收货数量+当次收货数量+
			// 其他绑定该PO明细的期待数量汇总<=（超收比例+1）*PO明细.期待数量,大于则报错"收货数量已超过超收比例";
			// ②、ASN已绑定PO明细,其他绑定该PO明细超收时，判断ASN明细.已收货数量+当次收货数量+
			// 其他绑定该PO明细的收货数量汇总<=（超收比例+1）*PO明细.期待数量,大于则报错"收货数量已超过超收比例";
			if (null != detail.getPoDetail()) {
				WmsPoDetail poDetail = this.commonDao.load(WmsPoDetail.class,
						detail.getPoDetail().getId());
				Double sumNum = DoubleUtils.mul(poDetail.getExpectedQty(),
						1 + extendRate, detail.getItem().getBuPrecision());
				// 其他绑定PO明细可收数量汇总
				Double poAdetailNum = 0.0D;
				if (poDetail.getExpectedQty().doubleValue() != detail
						.getExpectedQty().doubleValue()) {
					poAdetailNum = getPoDetailNum(detail);
				}
				if (detail.getReceivedQty().doubleValue() + receiveQtyBU
						+ poAdetailNum > sumNum.doubleValue()) {
					throw new BusinessException(
							"receive.qty.more.than.extend.rate");
				}
			} else {
				// ③、ASN未绑定PO明细时,则当次收货数量+ASN明细.已收货数量<=超收比例*ASN明细.期待数量，
				// 大于则报错“收货数量已超过超收比例!”；
				Double sumNum = DoubleUtils.mul(detail.getExpectedQty(),
						1 + extendRate, detail.getItem().getBuPrecision());
				if (detail.getReceivedQty().doubleValue() + receiveQtyBU > sumNum
						.doubleValue()) {
					throw new BusinessException(
							"receive.qty.more.than.extend.rate");
				}
			}
		} else {
			if (receiveQtyBU > detail.getUnReceivedQtyBU()) {
				throw new BusinessException(
						"receiving.quantity.is.greater.than.the.quantity.of.unReceived");
			}
		}

		//如果是托盘收货,判断该托盘号在库存中是否已存在!
		if(!com.vtradex.thorn.client.utils.StringUtils.isEmpty(pallet)){
			String hql = "from WmsInventory inv where inv.pallet =:pallet and inv.warehouse.id=:warehouseId and inv.qty>0 "
					+ " and inv.location.type IN ('STORAGE')";
			List<WmsInventory> invs =  this.commonDao.findByQuery(hql, new String[]{"pallet","warehouseId"}, 
					new Object[]{pallet,warehouse.getId()});
			if(!invs.isEmpty()||invs.size()>0){
				throw new BusinessException("this.pallet.already.exist.can.not.allow.receive");
		}
		}
		
		// 根据收货包装和数量写收货记录
		record.setReceivedPackQty(receiveQty);
		record.setReceivedQty(receiveQtyBU);
		
		/*刷新asn和asn明细的收货数量*/
        detail.receive(receiveQtyBU);
        /*判断asn明细是否关联了PO,是则刷新po和po明细的收货数量*/
        if (null != detail.getPoDetail()) {
        	WmsPoDetail poDetail = this.commonDao.load(WmsPoDetail.class, detail.getPoDetail().getId());
        	poDetail.receive(receiveQtyBU);
        	this.commonDao.store(poDetail);
		}
        //保存已收货的lotInfo  去除保存(保留原值)
//        detail.setLotInfo(lotInfo);
        //设置建议质检数量
        try{
        	Map<String, Object> value = wmsRuleManager.getRuleTableDetail("R101_质检判定规则表",asn.getCompany().getName(), asn.getBillType().getName(),detail.getItem().getQcClass(),lotInfo.getSupplierCode());
    	    if(value!=null) {
    	    	detail.setQcQty(detail.getReceivedQty()*(Double.parseDouble(value.get("质检比例").toString()))/100);
    	    }
            this.commonDao.store(detail);
        }catch(Exception ex){
        	throw new BusinessException(ex.getLocalizedMessage());
        }
	    

		WmsInventoryDto inventoryDto = new WmsInventoryDto();
		WmsBillType biType = this.commonDao.load(WmsBillType.class, asn.getBillType().getId());

		inventoryDto.setWarehouse(warehouse);
		inventoryDto.setCompany(company);
		inventoryDto.setLocation(recLoc);
		inventoryDto.setItem(item);
		inventoryDto.setItemKey(itemKey);
		inventoryDto.setPackageUnit(packageUnit);
		inventoryDto.setPackQty(receiveQty);
		inventoryDto.setQty(receiveQtyBU);
		inventoryDto.setStatus(status);
		if(WmsLocationType.RECEIVE.equals(asn.getReceiveLocation().getType())){
		   inventoryDto.setRelatedBillCode(asn.getCode());
		   inventoryDto.setInvRelatedBillCode(asn.getCode());
		}
		inventoryDto.setRelatedObjBillType(biType.getCode());
		inventoryDto.setRelatedBillType(WmsBillOfType.ASN);
		if(null != logType){
			inventoryDto.setType(logType);
		}else{
			inventoryDto.setType(WmsInventoryLogType.RECEIVING);
		}
		inventoryDto.setPallet(pallet);
		inventoryDto.setCarton(carton);
		if(isShip){
			//如果库存状态是否可发为是,则收货时生成的库存锁定状态为否
			inventoryDto.setLockStatus(Boolean.FALSE);
		}else{
			//如果库存状态是否可发为否,则收货时生成的库存锁定状态为是
			inventoryDto.setLockStatus(Boolean.TRUE);
		}
		inventoryManager.in(inventoryDto);//收货时产生库存
		
		return record;
	}
	
	private Double getPoDetailNum(WmsASNDetail detail){
		String hql = "SELECT sum(expectedQty) FROM WmsASNDetail WHERE expectedQty>=receivedQty AND poDetail.id=:poDetailId AND id!="+detail.getId();
		Double expectQty = (Double) commonDao.findByQueryUniqueResult(hql, new String[]{"poDetailId"}, new Object[]{detail.getPoDetail().getId()});
		expectQty = expectQty==null?0.0D:expectQty;
		hql = "SELECT sum(receivedQty) FROM WmsASNDetail WHERE expectedQty<receivedQty AND poDetail.id=:poDetailId AND id!="+detail.getId();
		Double receivedQty = (Double) commonDao.findByQueryUniqueResult(hql, new String[]{"poDetailId"}, new Object[]{detail.getPoDetail().getId()});
		receivedQty = receivedQty==null?0.0D:receivedQty;
		return expectQty+receivedQty;
	}
	
	/**
	 * 创建收货记录
	 * @param detail
	 * @param itemKey
	 * @param recLoc
	 */
	private WmsReceivedRecord getReceivedRecord(WmsASNDetail detail, WmsPackageUnit packageUnit, WmsItemKey itemKey, 
			String status, WmsLocation recLoc,Long workerId,String pallet,String carton) {
		// 调用库存状态规则获取当前明细的库存状态
//		String inventoryStatus = "";
				
				
		// 生成收货记录
		WmsReceivedRecord record = EntityFactory.getEntity(WmsReceivedRecord.class);
		
		record.setAsnDetail(detail);
		record.setItemKey(itemKey);
		record.setInventoryStatus(status);
		record.setPackageUnit(packageUnit.getUnit());
		record.setConvertFigure(packageUnit.getConvertFigure());
		record.setLocationId(recLoc.getId());
		record.setPallet(pallet);
		record.setCarton(carton);
		record.setWorker(commonDao.load(WmsWorker.class, workerId));
		
		commonDao.store(record);
		return record;
	}

	@Override
	public void countPrintTime(String parentIds,String ip,String raq) {
		int size = 0;//asn明细条数
		String asnCode = "";
		if(raq.equals("printSupplierWmsASNDetailPage.raq")){//送单明细条码补打
			String[] ids = parentIds.split(",");
			for(int i = 0;i < ids.length;i++){
				Long id = Long.valueOf(ids[i]);
				WmsASNDetail detail = commonDao.load(WmsASNDetail.class, id);
				String supplier = detail.getLotInfo().getSupplierCode();//供应商编码
				String supplierName = detail.getLotInfo().getExtendPropC9();//供应商名称
				WmsASN asn = commonDao.load(WmsASN.class, detail.getAsn().getId());
				asnCode = asn.getCode();
				String type = WmsSupplierPrintCountType.TMBD;
				/**保存到数据库*/
				saveSupplyCount(supplier, ip, 1d, supplierName, detail,1,asnCode,type);
			}
		}else if(raq.equals("receipt.raq")){//打印收货单
			Long id = Long.valueOf(parentIds);
			WmsASN asn = commonDao.load(WmsASN.class, id);
			String supplier = asn.getSupplier().getCode();//供应商编码
			String supplierName = asn.getSupplier().getName();//供应商名称
			size = asn.getDetails().size();//明细条数
			asnCode = asn.getCode();//asn单号
			Double qty = Math.ceil((double) size/9d);//打印张数
			String type =WmsSupplierPrintCountType.DYSHD;
			/**保存到数据库*/
			saveSupplyCount(supplier, ip, qty, supplierName, null,size,asnCode,type);
		}else if(raq.equals("directPrintLabelPage.raq")){//送货单打印标签
			String[] ids = parentIds.split(",");
			for(int i = 0;i < ids.length;i++){
				Long id = Long.valueOf(ids[i]);
				WmsASN asn = commonDao.load(WmsASN.class, id);
				for(WmsASNDetail detail : asn.getDetails()){
					String supplier = detail.getLotInfo().getSupplierCode();//供应商编码
					String supplierName = detail.getLotInfo().getExtendPropC9();//供应商名称
					Double qty = detail.getLabel(); //打印张数
					asnCode = asn.getCode();
					String type = WmsSupplierPrintCountType.YLDYBQ;
					/**保存到数据库*/
					saveSupplyCount(supplier, ip, qty, supplierName, detail,1,asnCode,type);
				}
			}
		}
	}
	
	/**记录供应商打印张数*/
	private void saveSupplyCount(String supplier,String ip,Double qty,
			String supplierName,WmsASNDetail detail,int lineCount,String asnCode,String type){
		Integer minQty = qty.intValue();
		
		WmsSupplierPrintCount wsp = new WmsSupplierPrintCount();
		wsp.setSupplier(supplier);
		wsp.setClientIp(ip);
		wsp.setPrintTimes(minQty);
		wsp.setSupplierName(supplierName);
		wsp.setPrintType(type);
		wsp.setAsnDetail(detail);
		wsp.setLineCount(lineCount);
		wsp.setAsnCode(asnCode);
		commonDao.store(wsp);
	}
	
	
	
	/**
	 * 是否打印
	 */
	public void isPrint(String parentIds,String raq){
		if(raq.equals("receipt.raq")){
			Long id = Long.valueOf(parentIds);
			WmsASN asn = commonDao.load(WmsASN.class, id);
			asn.setIsPrint(Boolean.TRUE);
			commonDao.store(asn);
		}	
	}
	
	
	
	/**
	 * 拣货作业单打印次数
	 */
	public void printTimes(String parentIds,String raq){
		if (raq.equals("printWork.raq") || raq.equals("TGYSCKD.raq") 
				|| raq.equals("YLCKD.raq") ) {
			String[] ids = parentIds.split(",");
			for (int i = 0; i < ids.length; i++) {
				Long id = Long.valueOf(ids[i]);
				WmsWorkDoc doc = commonDao.load(WmsWorkDoc.class, id);
				Integer j = doc.getPrintTimes();
				if (j == null) {
					j = 0;
				}
				j = j + 1;
				doc.setPrintTimes(j);
				commonDao.store(doc);
			}
		}
	}
}
