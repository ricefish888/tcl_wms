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
import com.vtradex.wms.webservice.sap.model.SapSupplier;
import com.vtradex.wms.webservice.sap.model.SapSupplierArray;
import com.vtradex.wms.webservice.utils.WebServiceHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;
/***
 * SAP2wms  供应商主数据
 */
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public class SapWebServiceSupplierImpl implements  SapWebServiceSupplier {
	
	public InterfaceLogManager interfaceLogManager;
	
	public SapWebServiceSupplierImpl(InterfaceLogManager interfaceLogManager) {
		this.interfaceLogManager = interfaceLogManager;
	}
	 /***
     * SAP2wms  供应商主数据
     */
	public SapResponse supplierInfo(SapSupplierArray sapSuppliers) {
		if(sapSuppliers==null) {
			return WebServiceHelper.getSapFailResponse();
		}
		SapSupplier[] suppliers = sapSuppliers.getSapSuppliers();
		if(suppliers == null || suppliers.length==0) {
			return WebServiceHelper.getSapFailResponse();
		}
		
		String xml = XmlObjectConver.toXML(sapSuppliers) ;
		WebServiceHelper.println(xml);
		
		Set<String> infos = new HashSet<String>();
		for(SapSupplier ss : suppliers){
			infos.add(ss.getLIFNR());
		}
		String request = WebServiceHelper.setToString(infos);
		try {
			this.interfaceLogManager.createSapToWmsInterfaceLog(InterfaceLogTaskType.RECEIVE_BASIC_REQUEST, InterfaceLogType.BASIC_SUPPLIER_SAP2WMS, xml,request);
		}
		catch(Exception e) {
			return WebServiceHelper.getSapFailResponse();
		}
		return WebServiceHelper.getSapSucessResponse();
	}
    
}
