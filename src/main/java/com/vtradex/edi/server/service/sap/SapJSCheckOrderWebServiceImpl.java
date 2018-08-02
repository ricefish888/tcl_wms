package com.vtradex.edi.server.service.sap;

import java.util.HashSet;
import java.util.Set;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.service.interf.InterfaceLogManager;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogTaskType;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogType;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.webservice.model.WsConstants;
import com.vtradex.wms.webservice.sap.model.SapJSCheckOrder;
import com.vtradex.wms.webservice.sap.model.SapJSCheckOrderArray;
import com.vtradex.wms.webservice.sap.model.SapResponse;
import com.vtradex.wms.webservice.utils.WebServiceHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public class SapJSCheckOrderWebServiceImpl extends DefaultBaseManager implements SapJSCheckOrderWebService{
	
	public InterfaceLogManager interfaceLogManager;
	
	public SapJSCheckOrderWebServiceImpl(InterfaceLogManager interfaceLogManager) {
		this.interfaceLogManager = interfaceLogManager;
	}
	@Override
	public SapResponse checkJSOrderInfo(SapJSCheckOrderArray scods) {
		if(null==scods){
			return WebServiceHelper.getSapFailResponse();
		}
		SapJSCheckOrder [] ssos = scods.getScos();
		if(null==ssos || ssos.length==0){
			return WebServiceHelper.getSapFailResponse();
		}
		String xml = XmlObjectConver.toXML(scods);
		WebServiceHelper.println(xml);
		Set<String> infos = new HashSet<String>();
		for(SapJSCheckOrder sso : ssos){
			infos.add(sso.getLIFNR()+"_"+StringHelper.substring(sso.getBUDAT(), 6));
		}
		String request = WebServiceHelper.setToString(infos);
		try {
			this.interfaceLogManager.createSapToWmsInterfaceLog(InterfaceLogTaskType.RECEIVE_BUSINESS_REQUEST, InterfaceLogType.BUSINESS_POJSCHECKORDER_SAP2WMS, xml,request);
		}
		catch(Exception e) {
			return WebServiceHelper.getSapFailResponse();
		}
		return WebServiceHelper.getSapSucessResponse();
		}
}
