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
import com.vtradex.wms.webservice.model.WsConstants;
import com.vtradex.wms.webservice.sap.model.SapResponse;
import com.vtradex.wms.webservice.sap.model.SapSaleOutDelivery;
import com.vtradex.wms.webservice.sap.model.SapSaleOutDeliveryArray;
import com.vtradex.wms.webservice.utils.WebServiceHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;

/** 
* @ClassName: Sap销售外向交货单处理
* @Description: SapWebServiceSaleOutDeliveryImpl 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-13 上午9:48:39  
*/
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public class SapWebServiceSaleOutDeliveryImpl extends DefaultBaseManager implements SapWebServiceSaleOutDelivery{
	
	public InterfaceLogManager interfaceLogManager;
	
	public SapWebServiceSaleOutDeliveryImpl(InterfaceLogManager interfaceLogManager) {
		this.interfaceLogManager = interfaceLogManager;
	}
	@Override
	public SapResponse saleOutDeliveryInfo(SapSaleOutDeliveryArray ssods) {
		if(null==ssods){
			return WebServiceHelper.getSapFailResponse();
		}
		SapSaleOutDelivery [] ssos = ssods.getSsods();
		if(null==ssos || ssos.length==0){
			return WebServiceHelper.getSapFailResponse();
		}
		String xml = XmlObjectConver.toXML(ssods) ;
		WebServiceHelper.println(xml);
		
		Set<String> infos = new HashSet<String>();
		for(SapSaleOutDelivery sso : ssos){
			infos.add(sso.getVBELN());
		}
		String request = WebServiceHelper.setToString(infos);
		try {
			this.interfaceLogManager.createSapToWmsInterfaceLog(InterfaceLogTaskType.RECEIVE_BUSINESS_REQUEST, InterfaceLogType.BUSINESS_SALEOUTDELIVERY_SAP2WMS, xml,request);
		}
		catch(Exception e) {
			return WebServiceHelper.getSapFailResponse();
		}
		return WebServiceHelper.getSapSucessResponse();
		}
}
