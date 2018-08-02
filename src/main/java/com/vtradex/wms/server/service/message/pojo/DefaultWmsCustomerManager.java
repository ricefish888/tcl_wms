package com.vtradex.wms.server.service.message.pojo;

import java.util.Map;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;
import com.vtradex.wms.server.model.entity.workdoc.WmsTempTask;
import com.vtradex.wms.server.service.message.WmsCustomerManager;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultWmsCustomerManager extends DefaultBaseManager implements WmsCustomerManager {

	/**
	 * 
	 * @Title: customerMethod
	 * 
	 * @Description: 客户化修改
	 *
	 * @return void    
	 *
	 * @throws 
	 *
	 * @author huangchao
	 *
	 * @date 2016年4月11日 上午11:47:21
	 */
	@Override
	public void customerMethod(WmsInventory inventory, WmsTask task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WmsLocation setTaskToLocation(WmsLocation loc, WmsPickTicketDetail detail, WmsInventory inv) {
		// TODO Auto-generated method stub
		return loc;
	}

	/* (non-Javadoc)
	 * @see com.vtradex.wms.server.service.message.WmsCustomerManager#ptConfirmCustomerMethod(com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail)
	 */
	@Override
	public void ptConfirmCustomerMethod(WmsPickTicketDetail ptDetail,Double qty,WmsTask task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> putawayProblem(Map<String, Object> problem,
			WmsTempTask task) {
		// TODO Auto-generated method stub
		return problem;
	}

	@Override
	public void storeCustomerInf(WmsLocation location) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.vtradex.wms.server.service.message.WmsCustomerManager#pickingProcessCustomerMethod(com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail, java.lang.Double, com.vtradex.wms.server.model.entity.workdoc.WmsTask)
	 */
	@Override
	public Map<String, Object> pickingProcessCustomerMethod(
			WmsPickTicketDetail detail, Double qty, WmsTask task) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.vtradex.wms.server.service.message.WmsCustomerManager#ptAllocateCustomerMethod(com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket)
	 */
	@Override
	public void ptAllocateCustomerMethod(WmsPickTicket pickTicket) {
		// TODO Auto-generated method stub
		
	}



}