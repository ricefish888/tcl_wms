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
import com.vtradex.wms.webservice.sap.model.SapPo;
import com.vtradex.wms.webservice.sap.model.SapPoArray;
import com.vtradex.wms.webservice.sap.model.SapResponse;
import com.vtradex.wms.webservice.utils.WebServiceHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;
/***
 * SAP2wms  采购订单
 */
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public class SapWebServicePoImpl implements  SapWebServicePo {
	
	public InterfaceLogManager interfaceLogManager;
	
	public SapWebServicePoImpl(InterfaceLogManager interfaceLogManager) {
		this.interfaceLogManager = interfaceLogManager;
	}
	 /***
     * SAP2wms  采购订单主数据
     */
	public SapResponse poInfo(SapPoArray sapPos) {
		if(sapPos==null) {
			return WebServiceHelper.getSapFailResponse();
		}
		SapPo[] pos = sapPos.getSapPos();
		if(pos == null || pos.length==0) {
			return WebServiceHelper.getSapFailResponse();
		}
		
		String xml = XmlObjectConver.toXML(sapPos) ;
		WebServiceHelper.println(xml);
		
		Set<String> infos = new HashSet<String>();
		for(SapPo po : pos){
			infos.add(po.getEBELN());
		}
		String request = WebServiceHelper.setToString(infos);
		try {
			this.interfaceLogManager.createSapToWmsInterfaceLog(InterfaceLogTaskType.RECEIVE_BUSINESS_REQUEST, InterfaceLogType.BUSINESS_PO_SAP2WMS, xml,request);
		}
		catch(Exception e) {
			return WebServiceHelper.getSapFailResponse();
		}
		return WebServiceHelper.getSapSucessResponse();
	}
    
}
