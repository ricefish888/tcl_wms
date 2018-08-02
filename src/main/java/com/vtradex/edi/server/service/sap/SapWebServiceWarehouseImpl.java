package com.vtradex.edi.server.service.sap;

import java.util.HashSet;
import java.util.Set;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import com.vtradex.wms.server.service.interf.InterfaceLogManager;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogTaskType;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogType;
import com.vtradex.wms.webservice.model.WsConstants;
import com.vtradex.wms.webservice.sap.model.SapResponse;
import com.vtradex.wms.webservice.sap.model.SapWarehouse;
import com.vtradex.wms.webservice.sap.model.SapWarehouseArray;
import com.vtradex.wms.webservice.utils.WebServiceHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;
/***
 * SAP2wms  工厂下的仓库主数据
 */
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public class SapWebServiceWarehouseImpl implements  SapWebServiceWarehouse {
	
	public InterfaceLogManager interfaceLogManager;
	
	public SapWebServiceWarehouseImpl(InterfaceLogManager interfaceLogManager) {
		this.interfaceLogManager = interfaceLogManager;
	}
	
	public SapResponse warehouseInfo(SapWarehouseArray sapWarehouses) {
		if(sapWarehouses==null) {
			return WebServiceHelper.getSapFailResponse();
		}
		SapWarehouse[] warehouses = sapWarehouses.getSapWarehouses();
		if(warehouses == null || warehouses.length==0) {
			return WebServiceHelper.getSapFailResponse();
		}
		
		String xml = XmlObjectConver.toXML(sapWarehouses) ;
		WebServiceHelper.println(xml);
		
		Set<String> infos = new HashSet<String>();
		for(SapWarehouse sw : warehouses){
			infos.add(sw.getLGORT());
		}
		String request = WebServiceHelper.setToString(infos);
		try {
			this.interfaceLogManager.createSapToWmsInterfaceLog(InterfaceLogTaskType.RECEIVE_BASIC_REQUEST, InterfaceLogType.BASIC_WAREHOUSE_SAP2WMS, xml,request);
		}
		catch(Exception e) {
			return WebServiceHelper.getSapFailResponse();
		}
		return WebServiceHelper.getSapSucessResponse();
	}
    
}
