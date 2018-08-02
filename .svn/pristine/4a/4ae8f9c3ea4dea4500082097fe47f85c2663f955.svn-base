package com.vtradex.edi.server.service.sap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.webservice.model.WsConstants;
import com.vtradex.wms.webservice.sap.model.SapCostCenterArray;
import com.vtradex.wms.webservice.sap.model.SapResponse;

/**
 * sap-wms成本中心主数据接口
 * @author Administrator
 *
 */
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public interface SapCostCenterWebService extends BaseManager{
	@WebMethod 
    @WebResult SapResponse CostCenter(@WebParam SapCostCenterArray sccs);
}
