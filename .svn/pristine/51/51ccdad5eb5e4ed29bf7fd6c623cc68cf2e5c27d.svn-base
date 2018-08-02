package com.vtradex.wms.server.service.task.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.allen_sauer.gwt.voices.client.util.StringUtil;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.entity.base.MidSurpplierUser;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.model.entity.email.EmailRecordType;
import com.vtradex.wms.server.model.entity.order.ConfirmStatus;
import com.vtradex.wms.server.model.entity.order.PurchaseOrder;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrder;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderBillType;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.emailrecord.EmailRecordManager;
import com.vtradex.wms.server.service.item.TclMessageManager;
import com.vtradex.wms.server.service.production.WmsDeliveryOrderManager;
import com.vtradex.wms.server.service.task.CallOracleProcManager;
import com.vtradex.wms.server.service.task.CrontabLogManager;
import com.vtradex.wms.server.service.task.CrontabManager;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.webservice.utils.CommonHelper;
import com.vtradex.wms.webservice.utils.EmailHelper;
import com.vtradex.wms.webservice.utils.ServerUtil;
import com.vtradex.wms.webservice.utils.WebServiceHelper;

/**
 * @Description:   统一的定时任务管理类
 * @Author:        <a href="xuyan.xia@vtradex.net">夏绪焰</a>
 * @CreateDate:    May 28, 2012 9:43:32 AM
 * @version:       v1.0
 */
public class DefaultCrontabManager extends DefaultBaseManager implements CrontabManager {
    //日志
    private CrontabLogManager crontabLogManager;
	private CallOracleProcManager callOracleProcManager;
	protected TclMessageManager tclMessageManager;
	
    public DefaultCrontabManager(CrontabLogManager crontabLogManager ,
            CallOracleProcManager callOracleProcManager,
            TclMessageManager tclMessageManager
            ) {
        this.crontabLogManager = crontabLogManager;
        this.callOracleProcManager = callOracleProcManager;
        this.tclMessageManager = tclMessageManager;
        
    }
    
    /**每天晚上0点前的任务 建议23点开始跑*/
    public void everyDayBeforeZeroTask() {
        if(!ServerUtil.isEdiServer()) {
            return ;
        }
        /**********************************************************************/
        Date beginDate = new Date();
        String crontabName="计算月度齐套率报表";
        Long crontablogid = 0L;
		try {
			crontablogid = crontabLogManager.storeCrontabRunningLog(beginDate, crontabName);
			WmsDeliveryOrderManager wmsDeliveryOrderManager = (WmsDeliveryOrderManager)applicationContext.getBean("wmsDeliveryOrderManager");
			wmsDeliveryOrderManager.gdqt_mon();
			crontabLogManager.updateCrontabLogToSucess(crontablogid);
		} catch (Exception e) {
			crontabLogManager.updateCrontabLogToError(crontablogid, e.getMessage());
			e.printStackTrace();
		}
        
        
    }
    
    /**拣配工单导入 定时任务 由task执行 无事务*/
    public void jpgddr(Long warehouseId) {
    	 if(!ServerUtil.isEdiServer()) {
             return ;
         }
         /**********************************************************************/
         Date beginDate = new Date();
         WmsWarehouse w = commonDao.load(WmsWarehouse.class, warehouseId);
         String crontabName="拣配工单导入定时执行:仓库："+w.getName();
         Long crontablogid = 0L;
         try {
        	 crontablogid = crontabLogManager.storeCrontabRunningLog(beginDate, crontabName);
        	 
        	 tclMessageManager.importProductionOrderByTask(warehouseId);
             
             crontabLogManager.updateCrontabLogToSucess(crontablogid);
         } 
         catch (Exception e) {
             crontabLogManager.updateCrontabLogToError(crontablogid, e.getMessage());
             e.printStackTrace();
         }
         
    }
    
    /**每天0点开始跑的任务 定时任务配置时间建议每天0:10以后*/
    public void everyDayInZeroTask1() {
        if(!ServerUtil.isEdiServer()) {
            return ;
        }
        /**********************************************************************/
        Date beginDate = new Date();
        String crontabName="移动定时任务完成状态信息";
        try {
            callOracleProcManager.MOVE_THORN_TASK();
            crontabLogManager.storeCrontabSuccessLog(beginDate, crontabName);
        } 
        catch (Exception e) {
            crontabLogManager.storeCrontabErrorLog(beginDate, crontabName, e.getMessage());
            e.printStackTrace();
        }
        
        /**********************************************************************/
        
       /* beginDate = new Date();
        crontabName="计算库存结转数据(日结表)";
        try {
            wmsInventoryManager.compute();
            crontabLogManager.storeCrontabSuccessLog(beginDate, crontabName);
        } 
        catch (Exception e) {
            crontabLogManager.storeCrontabErrorLog(beginDate, crontabName, e.getMessage());
            e.printStackTrace();
        } */
        
    }
    
    /**每天0点后的任务 建议每天3:00以后触发*/
    public void everyDayInZeroTask2() {
        if(!ServerUtil.isEdiServer()) {
            return ;
        }
        /**********************************************************************/
        
        Date beginDate = new Date();
        String crontabName="删除无效库存记录";
        try {
//            wmsInventoryManager.deleteInvalidInventory();
            crontabLogManager.storeCrontabSuccessLog(beginDate, crontabName);
        } 
        catch (Exception e) {
            crontabLogManager.storeCrontabErrorLog(beginDate, crontabName, e.getMessage());
            e.printStackTrace();
        }
        
    }
     
    /**仅仅由应用的jvm跑的task*/
    public void appTask1() {
        if(!ServerUtil.isAppServer()) {
            return ;
        }
        /**********************************************************************/
        
    }
    /**
     * 生成采购单和交货单对应的邮件提醒供应商
     */
    public void genEmailRecord(){
    	//采购单
    	String hql = "from PurchaseOrder po where po.confirmStatus=:confirmStatus ";
    	List<PurchaseOrder> pos = commonDao.findByQuery(hql, "confirmStatus", ConfirmStatus.OPEN);
    	Map<Long,List<String>> poMap = new HashMap<Long, List<String>>();
    	for(PurchaseOrder po : pos){
    		if(!poMap.containsKey(po.getSupplier().getId())){
    			List<String> values = new ArrayList<String>();
    			values.add(po.getCode());
    			poMap.put(po.getSupplier().getId(), values);
    		}else{
    			poMap.get(po.getSupplier().getId()).add(po.getCode());
    		}
    	}
    	//一个供应商一封邮件
    	this.genEmailRecordByType(EmailRecordType.PO2SUPPLIER,poMap);
    	//交货单
    	hql = "from WmsDeliveryOrder d where d.confirmStatus=:confirmStatus and d.billTypeName=:billTypeName ";
    	List<WmsDeliveryOrder> dos = commonDao.findByQuery(hql, new String[]{"confirmStatus","billTypeName"}, 
    			new Object[]{ConfirmStatus.OPEN,WmsDeliveryOrderBillType.CGBILLTYPE});
    	Map<Long,List<String>> doMap = new HashMap<Long, List<String>>();
    	for(WmsDeliveryOrder wdo :dos ){
    		if(!doMap.containsKey(wdo.getSupplier().getId())){
    			List<String> values = new ArrayList<String>();
    			values.add(wdo.getSapCode());
    			doMap.put(wdo.getSupplier().getId(), values);
    		}else{
    			doMap.get(wdo.getSupplier().getId()).add(wdo.getSapCode());
    		}
    	}
    	this.genEmailRecordByType(EmailRecordType.DELIVERY2SUPPLIER,doMap);
    }
    /**根据供应商ID获取供应商用户关系*/
    private MidSurpplierUser getMidSurpplierUserBySupplierId(Long supplierId){
    	List<MidSurpplierUser> msus = commonDao.findByQuery("FROM MidSurpplierUser msu where msu.sid=:ms", "ms", supplierId);
		if(msus.isEmpty()){
			return null;
		}
		if(msus.size()>1){
			throw new BusinessException("根据供应商ID"+supplierId+"找到多条供应商用户关系");
		}
    	return msus.get(0);
    }
    
    private void genEmailRecordByType(String type,Map<Long,List<String>> map){
    	Set<Long> keys = map.keySet();
    	for(Long key : keys){
    		String subject = "";
    		String emailtype = "";
    		if(EmailRecordType.PO2SUPPLIER.equals(type)){
    			subject = "采购订单下发通知";
    			emailtype = EmailRecordType.PO2SUPPLIER;
    		}
    		if(EmailRecordType.DELIVERY2SUPPLIER.equals(type)){
    			subject = "交货单下发通知";
    			emailtype = EmailRecordType.DELIVERY2SUPPLIER;
    		}
    		String content = "";
    		Set<String> contents = new HashSet<String>();
    		for(String code : map.get(key)){
    			contents.add(code);
    		}
    		StringBuffer sb = new StringBuffer("");
    		
    		for(String s : contents) {
    			sb.append(s+"，");
    		}
    		String codeStr = sb.toString();
    		
    		if(!StringHelper.isNullOrEmpty(codeStr)) {
    			if(codeStr.endsWith("，")) {
    				codeStr = StringHelper.deleteLastChar(codeStr);
    			}
    		}
    		codeStr = StringHelper.substring(codeStr,1000);
    		if(EmailRecordType.PO2SUPPLIER.equals(type)){
    			subject = "采购订单下发通知";
    			emailtype = EmailRecordType.PO2SUPPLIER;
    			content = "供应商门户平台有未接收确认的采购单"+codeStr+"，请及时登录接收并确认。";
    		}
    		if(EmailRecordType.DELIVERY2SUPPLIER.equals(type)){
    			subject = "交货单下发通知";
    			emailtype = EmailRecordType.DELIVERY2SUPPLIER;
    			content = "供应商门户平台有未接收确认的交货单"+codeStr+"，请及时登录接收并确认。";
    		}
    		
    		
    		MidSurpplierUser msu = this.getMidSurpplierUserBySupplierId(key);
			if(null!=msu){
				ThornUser user = commonDao.load(ThornUser.class, msu.getUid());
				//每个邮箱发邮件
				String emails = user.getEmail();
				if(null!=emails){
					String [] ems = emails.split("\\|");
					for (String em : ems) {
						//异步发邮件
						EmailRecordManager emailRecordManager = (EmailRecordManager)applicationContext.getBean("emailRecordManager");
						emailRecordManager.storeEmailRecordWaitSend(user.getLoginName(),em,subject,content,EmailHelper.getEmailCc(),emailtype,null);
					}
				}
			}
    	}
    }
}




