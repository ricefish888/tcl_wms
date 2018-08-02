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
import com.vtradex.wms.webservice.sap.model.SapReturnOrderCode;
import com.vtradex.wms.webservice.sap.model.SapReturnOrderCodeArray;
import com.vtradex.wms.webservice.utils.WebServiceHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;

/**
 * SAP2wms  单号回传接口  交货单WMS传给SAP,SAP处理成功后回传单号给WMS
 * @return
 * */
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public class SapWebServiceReturnOrderCodeImpl implements SapWebServiceReturnOrderCode{
	public InterfaceLogManager interfaceLogManager;
	
	public SapWebServiceReturnOrderCodeImpl(InterfaceLogManager interfaceLogManager) {
		this.interfaceLogManager = interfaceLogManager;
	}
	 
	public SapResponse returnOrderCodeInfo(SapReturnOrderCodeArray sapReturnOrderCodes) {
		if(sapReturnOrderCodes==null) {
			return WebServiceHelper.getSapFailResponse();
		}
		SapReturnOrderCode[] ocs = sapReturnOrderCodes.getSapReturnOrderCodes();
		if(ocs == null || ocs.length==0) {
			return WebServiceHelper.getSapFailResponse();
		}
		String xml = XmlObjectConver.toXML(sapReturnOrderCodes) ;
		WebServiceHelper.println(xml);
		
		Set<String> infos = new HashSet<String>();
		for(SapReturnOrderCode oc : ocs){
			infos.add(oc.getWmsOrderCode());
		}
		String request = WebServiceHelper.setToString(infos);
		try {
			this.interfaceLogManager.createSapToWmsInterfaceLog(InterfaceLogTaskType.RECEIVE_BUSINESS_REQUEST, InterfaceLogType.BUSINESS_ORDERCODE_SAP2WMS, xml,request);
		}
		catch(Exception e) {
			return WebServiceHelper.getSapFailResponse();
		}
		return WebServiceHelper.getSapSucessResponse();
		}
}
