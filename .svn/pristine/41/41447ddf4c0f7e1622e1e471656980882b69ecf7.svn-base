package com.vtradex.wms.server.service.receiving.pojo;

import java.util.List;

import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.wms.server.model.entity.receiving.WmsASN;
import com.vtradex.wms.server.model.entity.workdoc.WmsWorkDoc;
import com.vtradex.wms.server.model.enums.WmsWorkDocStatus;
import com.vtradex.wms.server.service.receiving.WmsASNManager;
import com.vtradex.wms.server.service.receiving.WmsTclASNManager;
import com.vtradex.wms.server.service.receiving.WmsTclNoTransactionalManager;
import com.vtradex.wms.server.service.workdoc.WmsWorkDocManager;

public class DefaultWmsTclNoTransactionalManager 
		extends DefaultWmsNoTransactionalManager implements WmsTclNoTransactionalManager{

	protected WmsASNManager wmsASNManager;
	private WorkflowManager workflowManager;
	private WmsWorkDocManager wmsWorkDocManager;
	
	public DefaultWmsTclNoTransactionalManager(WmsASNManager wmsASNManager,
			WorkflowManager workflowManager, WmsWorkDocManager wmsWorkDocManager) {
		super(wmsASNManager, workflowManager, wmsWorkDocManager);
		this.wmsASNManager = wmsASNManager;
		this.workflowManager = workflowManager;
		this.wmsWorkDocManager = wmsWorkDocManager;
	}

	public void manualCreateWorkDoc(WmsASN wmsAsn) {
		WmsASN wmsAsnInfo = commonDao.load(WmsASN.class, wmsAsn.getId());
//		List<WmsWorkDoc> workDocs = wmsASNManager.manualCreateWorkDocs(wmsAsnInfo);
		WmsTclASNManager tclASNManager = (WmsTclASNManager) applicationContext.getBean("wmsTclASNManager");
		List<WmsWorkDoc> workDocs = tclASNManager.manualCreateWorkDocs(wmsAsnInfo);
		//自动分配
		try {
			for(WmsWorkDoc workDoc : workDocs){
				workflowManager.sendMessage(workDoc, "wmsPutawayWorkDocProcess.autoAllocateByMessage");
				workDoc = load(WmsWorkDoc.class,workDoc.getId());
				if(WmsWorkDocStatus.ALLOCATED.equals(workDoc.getStatus())){
					wmsWorkDocManager.activeWmsWorkDoc(workDoc);
					workflowManager.doWorkflow(workDoc, "wmsPutawayWorkDocProcess.active");
				}
			}
		} catch (Exception e) {
			LocalizedMessage.addLocalizedMessage(e.getMessage());
			e.printStackTrace();
		}
	}

}
