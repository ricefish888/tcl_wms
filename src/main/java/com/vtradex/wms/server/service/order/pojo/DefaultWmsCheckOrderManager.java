package com.vtradex.wms.server.service.order.pojo;

import java.util.Date;
import java.util.List;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.entity.base.Wms2SapInterfaceLogType;
import com.vtradex.wms.server.model.entity.order.WmsCheckOrder;
import com.vtradex.wms.server.model.entity.order.WmsCheckOrderStatus;
import com.vtradex.wms.server.service.interf.InterfaceLogManager;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogTaskType;
import com.vtradex.wms.server.service.order.WmsCheckOrderManager;
import com.vtradex.wms.webservice.sap.model.Wms2SapSupplierDocStatus;
import com.vtradex.wms.webservice.sap.model.Wms2SapSupplierDocType;
import com.vtradex.wms.webservice.sap.model.Wms2SapSupplierStatus;
import com.vtradex.wms.webservice.utils.XmlObjectConver;

public class DefaultWmsCheckOrderManager extends DefaultBaseManager implements WmsCheckOrderManager{
	private InterfaceLogManager interfaceLogManager;
	

	public DefaultWmsCheckOrderManager(InterfaceLogManager interfaceLogManager) {
		this.interfaceLogManager = interfaceLogManager;
	}

	@Override
	public void confirm(List<WmsCheckOrder> cos) {
		for (WmsCheckOrder order : cos) {
			order.setStatus(WmsCheckOrderStatus.CONFIRMED);
			order.setConfirmor(UserHolder.getUser().getName());
			order.setConfrimTime(new Date());
			this.commonDao.store(order);
			createWms2SapInterfacelog(order,order.getStatus());
		}
	}

	@Override
	public void received(List<WmsCheckOrder> cos) {
		for (WmsCheckOrder order : cos) {
			order.setStatus(WmsCheckOrderStatus.RECEIVED);
			order.setReceiver(UserHolder.getUser().getName());
			order.setReceiveTime(new Date());
			this.commonDao.store(order);
			createWms2SapInterfacelog(order,order.getStatus());
		}
	}

	private void createWms2SapInterfacelog(WmsCheckOrder order, String status) {
		Wms2SapSupplierDocStatus wsds = new Wms2SapSupplierDocStatus();
    	wsds.setType(Wms2SapSupplierDocType.CHECKORDER);
    	wsds.setOrderNo(order.getCode());
    	if(WmsCheckOrderStatus.CONFIRMED.equals(status)){
    		wsds.setStatus(Wms2SapSupplierStatus.CONFIRMED);
    	}else if(WmsCheckOrderStatus.RECEIVED.equals(status)){
    		wsds.setStatus(Wms2SapSupplierStatus.VIEWED);
    	}else{
    		wsds.setStatus(Wms2SapSupplierStatus.OPENED);
    	}
    	String xml = XmlObjectConver.toXML(wsds) ;
    	interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_SURPPLIERBUSINESS, Wms2SapInterfaceLogType.SURPPLIERBUSINESSBACK, xml, order.getId(),order.getCode());
	}

}
