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
import com.vtradex.wms.webservice.sap.model.SapDeliveryOrder;
import com.vtradex.wms.webservice.sap.model.SapDeliveryOrderArray;
import com.vtradex.wms.webservice.sap.model.SapResponse;
import com.vtradex.wms.webservice.utils.WebServiceHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;

/** 
* @ClassName: SapWebServicePurchaseOrderImpl 
* @Description: TODO
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-5 下午2:05:43  
*/
/**
 *  SAP2wms  采购交货单
 * @return
 * */
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public class SapWebServiceDeliveryOrderImpl implements SapWebServiceDeliveryOrder{
	public InterfaceLogManager interfaceLogManager;
	
	public SapWebServiceDeliveryOrderImpl(InterfaceLogManager interfaceLogManager) {
		this.interfaceLogManager = interfaceLogManager;
	}
	 /***
     * SAP2wms 采购交货单主数据
     */
	public SapResponse deliveryOrderInfo(SapDeliveryOrderArray sapDeliveryOrders) {
		if(sapDeliveryOrders==null) {
			return WebServiceHelper.getSapFailResponse();
		}
		SapDeliveryOrder[] spoas = sapDeliveryOrders.getSpoas();
		if(spoas == null || spoas.length==0) {
			return WebServiceHelper.getSapFailResponse();
		}
		String xml = XmlObjectConver.toXML(sapDeliveryOrders) ;
		WebServiceHelper.println(xml);
		
		Set<String> infos = new HashSet<String>();
		for(SapDeliveryOrder sdo : spoas){
			infos.add(sdo.getVBELN());
		}
		String request = WebServiceHelper.setToString(infos);
		try {
			this.interfaceLogManager.createSapToWmsInterfaceLog(InterfaceLogTaskType.RECEIVE_BUSINESS_REQUEST, InterfaceLogType.BUSINESS_DELIVERYORDER_SAP2WMS, xml,request);
		}
		catch(Exception e) {
			return WebServiceHelper.getSapFailResponse();
		}
		return WebServiceHelper.getSapSucessResponse();
		}
}
