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
import com.vtradex.wms.webservice.sap.model.SapCheckOrder;
import com.vtradex.wms.webservice.sap.model.SapCheckOrderArray;
import com.vtradex.wms.webservice.sap.model.SapResponse;
import com.vtradex.wms.webservice.utils.WebServiceHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;
/** 
* @ClassName: 采购对账单标准处理类
* @Description: SapCheckOrderWebServiceImpl 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-14 下午5:42:36  
*/
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public class SapCheckOrderWebServiceImpl extends DefaultBaseManager implements SapCheckOrderWebService{
	public InterfaceLogManager interfaceLogManager;
	
	public SapCheckOrderWebServiceImpl(InterfaceLogManager interfaceLogManager) {
		this.interfaceLogManager = interfaceLogManager;
	}

	@Override
	public SapResponse checkOrderInfo(SapCheckOrderArray ssods) {
		if(null==ssods){
			return WebServiceHelper.getSapFailResponse();
		}
		SapCheckOrder[] scos =ssods.getScos();
		if(null==scos || scos.length==0){
			return WebServiceHelper.getSapFailResponse();
		}
		String xml = XmlObjectConver.toXML(ssods) ;
		WebServiceHelper.println(xml);
		Set<String> infos = new HashSet<String>();
		for(SapCheckOrder sco : scos){
			infos.add(sco.getLIFNR()+"_"+StringHelper.substring(sco.getBUDAT(), 6));
		}
		String request = WebServiceHelper.setToString(infos);
		try {
			this.interfaceLogManager.createSapToWmsInterfaceLog(InterfaceLogTaskType.RECEIVE_BUSINESS_REQUEST, InterfaceLogType.BUSINESS_POCHECKORDER_SAP2WMS, xml,request);
		}
		catch(Exception e) {
			return WebServiceHelper.getSapFailResponse();
		}
			return WebServiceHelper.getSapSucessResponse();
		}
}
