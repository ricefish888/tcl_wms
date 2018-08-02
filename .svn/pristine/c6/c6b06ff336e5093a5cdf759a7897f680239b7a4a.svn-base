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
import com.vtradex.wms.webservice.sap.model.SapItem;
import com.vtradex.wms.webservice.sap.model.SapItemArray;
import com.vtradex.wms.webservice.sap.model.SapResponse;
import com.vtradex.wms.webservice.utils.WebServiceHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;


@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public class SapWebServiceImpl implements SapWebService { 
	
	public InterfaceLogManager interfaceLogManager;
	
	public SapWebServiceImpl(InterfaceLogManager interfaceLogManager) {
		this.interfaceLogManager = interfaceLogManager;
	}

	 /***
     * SAP2wms  物料主数据
     */
	public SapResponse itemInfo(SapItemArray sapItemArray) {
		if(sapItemArray==null) {
			return WebServiceHelper.getSapFailResponse();
		}
		SapItem[] items = sapItemArray.getSapItems();
		if(items == null || items.length==0) {
			return WebServiceHelper.getSapFailResponse();
		}
		
		String xml = XmlObjectConver.toXML(sapItemArray) ;
		WebServiceHelper.println(xml);
		
		Set<String> infos = new HashSet<String>();
		for(SapItem item : items){
			infos.add(item.getMATNR());
		}
		String request = WebServiceHelper.setToString(infos);
		try {
			this.interfaceLogManager.createSapToWmsInterfaceLog(InterfaceLogTaskType.RECEIVE_BASIC_REQUEST, InterfaceLogType.BASIC_ITEM_SAP2WMS, xml,request);
		}
		catch(Exception e) {
			return WebServiceHelper.getSapFailResponse();
		}
		return WebServiceHelper.getSapSucessResponse();
	}
	
  
}  