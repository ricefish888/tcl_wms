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
import com.vtradex.wms.webservice.sap.model.SapProductOrderIn;
import com.vtradex.wms.webservice.sap.model.SapProductOrderInArray;
import com.vtradex.wms.webservice.sap.model.SapResponse;
import com.vtradex.wms.webservice.utils.WebServiceHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;

/** 
* @ClassName:  Sap-Wms生产订单入库接口实现类
* @Description: SapProductOrderInWebServiceImpl 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-18 上午10:27:00  
*/
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public class SapProductOrderInWebServiceImpl extends DefaultBaseManager implements SapProductOrderInWebService {
	
	public InterfaceLogManager interfaceLogManager;
	
	public SapProductOrderInWebServiceImpl(InterfaceLogManager interfaceLogManager) {
		this.interfaceLogManager = interfaceLogManager;
	}

	@Override
	public SapResponse productOrderIn(SapProductOrderInArray spois) {
		if(null==spois){
			return WebServiceHelper.getSapFailResponse();
		}
		SapProductOrderIn[] spos = spois.getSpois();
		
		if(null==spos || spos.length==0){
			return WebServiceHelper.getSapFailResponse();
		}
		String xml = XmlObjectConver.toXML(spois);
		WebServiceHelper.println(xml);
		Set<String> infos = new HashSet<String>();
		for (SapProductOrderIn spo : spos) {
			infos.add(spo.getMBLNR()+"_"+spo.getAUFNR());
		}
		String request = WebServiceHelper.setToString(infos);
		try {
			this.interfaceLogManager.createSapToWmsInterfaceLog(InterfaceLogTaskType.RECEIVE_PRODUCTORDER_IN, InterfaceLogType.BUSINESS_PRODUCTORDER_IN, xml,request);
		}
		catch(Exception e) {
			return WebServiceHelper.getSapFailResponse();
		}
			return WebServiceHelper.getSapSucessResponse();
		}
}
