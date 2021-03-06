package com.vtradex.edi.server.service.sap;


import java.util.HashSet;
import java.util.Set;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.service.interf.InterfaceLogManager;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogTaskType;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogType;
import com.vtradex.wms.webservice.sap.model.SapCostCenter;
import com.vtradex.wms.webservice.sap.model.SapCostCenterArray;
import com.vtradex.wms.webservice.sap.model.SapResponse;
import com.vtradex.wms.webservice.utils.WebServiceHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;

public class SapCostCenterWebServiceImpl  extends DefaultBaseManager implements SapCostCenterWebService{

	public InterfaceLogManager interfaceLogManager;
	
	public SapCostCenterWebServiceImpl(InterfaceLogManager interfaceLogManager) {
		this.interfaceLogManager = interfaceLogManager;
	}

	@Override
	public SapResponse CostCenter(SapCostCenterArray sccs) {
		if(null==sccs){
			return WebServiceHelper.getSapFailResponse();
		}
		SapCostCenter[] scc = sccs.getSccs();
		
		if(null==scc || scc.length==0){
			return WebServiceHelper.getSapFailResponse();
		}
		String xml = XmlObjectConver.toXML(sccs);
		WebServiceHelper.println(xml);
		Set<String> infos = new HashSet<String>();
		for (SapCostCenter s : scc) {
			infos.add(s.getKOSTL());
		}
		String request = WebServiceHelper.setToString(infos);
		try {
			this.interfaceLogManager.createSapToWmsInterfaceLog(InterfaceLogTaskType.RECEIVE_BASIC_REQUEST, InterfaceLogType.BASIC_COSTCENTER_SAP2WMS, xml,request);
		}
		catch(Exception e) {
			return WebServiceHelper.getSapFailResponse();
		}
			return WebServiceHelper.getSapSucessResponse();
		}

}
