package com.vtradex.wms.server.service.order.pojo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.entity.base.Wms2SapInterfaceLogType;
import com.vtradex.wms.server.model.entity.base.WmsSapFactory;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.order.ConfirmStatus;
import com.vtradex.wms.server.model.entity.order.PurchaseOrder;
import com.vtradex.wms.server.model.entity.order.PurchaseOrderDetail;
import com.vtradex.wms.server.model.entity.order.PurchaseOrderStatus;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.interf.InterfaceLogManager;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogTaskType;
import com.vtradex.wms.server.service.order.WmsPurchaseOrderManager;
import com.vtradex.wms.server.utils.PackageUtils;
import com.vtradex.wms.webservice.sap.model.Wms2SapSupplierDocStatus;
import com.vtradex.wms.webservice.sap.model.Wms2SapSupplierDocType;
import com.vtradex.wms.webservice.sap.model.Wms2SapSupplierStatus;
import com.vtradex.wms.webservice.utils.XmlObjectConver;

public class DefaultWmsPurchaseOrderManager extends DefaultBaseManager implements WmsPurchaseOrderManager{

	private WorkflowManager workflowManager;
	private InterfaceLogManager interfaceLogManager;
	
	public DefaultWmsPurchaseOrderManager(WorkflowManager workflowManager,InterfaceLogManager interfaceLogManager){
		this.workflowManager = workflowManager;
		this.interfaceLogManager = interfaceLogManager;
	}
	
	public void saveOrUpdate(PurchaseOrder purchaseOrder){
		
		if (purchaseOrder.isNew()) {
			WmsWarehouse wh = (WmsWarehouse)commonDao.findByQueryUniqueResult("FROM WmsWarehouse warehouse WHERE warehouse.baseOrganization.id = :baseOrganizationId", 
					new String[] {"baseOrganizationId"}, new Object[] {BaseOrganizationHolder.getThornBaseOrganization().getId()});
			purchaseOrder.setWarehouse(wh);
			workflowManager.doWorkflow(purchaseOrder, "purchaseOrderProcess.new");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public String getMaxLineNoByPurchaseOrderDetail(Map param) {
		
		Integer lineNo = (Integer) commonDao.findByQueryUniqueResult("SELECT MAX(detail.lineNo) FROM PurchaseOrderDetail detail WHERE detail.purchaseOrder.id = :purchaseOrderId", 
				new String[] {"purchaseOrderId"}, new Object[] {(Long) param.get("purchaseOrder.id")});
		if (lineNo == null || lineNo.intValue() == 0) {
			lineNo = 1;
		} else {
			lineNo += 1;
		}

		return ""+lineNo;
	}
	
	public void addDetail(Long id, PurchaseOrderDetail purchaseOrderDetail, double expectedPackQty) {
		PurchaseOrder purchaseOrder =commonDao.load(PurchaseOrder.class, id);
		WmsSapFactory factory  = commonDao.load(WmsSapFactory.class, purchaseOrder.getSapFactory().getId());
		if (!purchaseOrder.getStatus().equals(PurchaseOrderStatus.OPEN)) {
			throw new BusinessException("purchaseOrder.status.error");
		}
		if (purchaseOrderDetail.isNew()) {
			purchaseOrderDetail.setFactory(factory);
			purchaseOrderDetail.setPurchaseOrder(purchaseOrder);
			purchaseOrder.addDetail(purchaseOrderDetail);
		} else {
			purchaseOrderDetail = this.commonDao.load(PurchaseOrderDetail.class, purchaseOrderDetail.getId());
		} 
		
		// 预期收货数量计算(convertFigure为1表示是基本包装，基本包装只能有1个，不为1表示是件装)
		WmsPackageUnit unit = commonDao.load(WmsPackageUnit.class, purchaseOrderDetail.getPackageUnit().getId());
		if (purchaseOrderDetail.getPackageUnit().getConvertFigure().intValue() == 1) {
			purchaseOrderDetail.setExpectedQty(PackageUtils.convertBUQuantity(expectedPackQty, unit));
			purchaseOrderDetail.setExpectedPackQty(expectedPackQty);
		} else {
			purchaseOrderDetail.setExpectedPackQty(expectedPackQty);
			purchaseOrderDetail.setExpectedQty(PackageUtils.convertBUQuantity(expectedPackQty, unit));
		}
		purchaseOrder.refreshQtyBU();
		this.commonDao.store(purchaseOrder);
	}
	
	public void removeDetails(PurchaseOrderDetail purchaseOrderDetail) {
		PurchaseOrder purchaseOrder =commonDao.load(PurchaseOrder.class, purchaseOrderDetail.getPurchaseOrder().getId());
		purchaseOrder.removeDetail(purchaseOrderDetail);
		purchaseOrder.refreshQtyBU();
		this.commonDao.store(purchaseOrder);
	}

    @Override
    public void confirm(List<PurchaseOrder> pos) {
        for (PurchaseOrder po : pos) {
            po.setConfirmTime(new Date());
            po.setConfirmor(UserHolder.getUser().getName());
            po.setConfirmStatus(ConfirmStatus.CONFIRM);
            this.commonDao.store(po);
            createWms2SapInterfacelog(po,po.getConfirmStatus());
        }
        
    }

    private void createWms2SapInterfacelog(PurchaseOrder po,String status) {
    	Wms2SapSupplierDocStatus wsds = new Wms2SapSupplierDocStatus();
    	wsds.setType(Wms2SapSupplierDocType.PO);
    	wsds.setOrderNo(po.getCode());
    	if(ConfirmStatus.CONFIRM.equals(status)){
    		wsds.setStatus(Wms2SapSupplierStatus.CONFIRMED);
    	}else if(ConfirmStatus.RECEIVED.equals(status)){
    		wsds.setStatus(Wms2SapSupplierStatus.VIEWED);
    	}else{
    		wsds.setStatus(Wms2SapSupplierStatus.OPENED);
    	}
    	String xml = XmlObjectConver.toXML(wsds) ;
    	interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_SURPPLIERBUSINESS, Wms2SapInterfaceLogType.SURPPLIERBUSINESSBACK, xml, po.getId(),po.getCode());
	}

	@Override
    public void received(List<PurchaseOrder> pos) {
        for (PurchaseOrder po : pos) {
            po.setReceiveTime(new Date());
            po.setReceiver(UserHolder.getUser().getName());
            po.setConfirmStatus(ConfirmStatus.RECEIVED);
            this.commonDao.store(po);
            createWms2SapInterfacelog(po,po.getConfirmStatus());
        }
    }

	@Override
	public void isActivePurchase(PurchaseOrder purchaseOrder) {
		purchaseOrder.setConfirmStatus(ConfirmStatus.OPEN);
		purchaseOrder.setConfirmor(null);
		purchaseOrder.setConfirmTime(null);
		purchaseOrder.setReceiver(null);
		purchaseOrder.setReceiveTime(null);
		this.commonDao.store(purchaseOrder);
	}
	
}
