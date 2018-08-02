package com.vtradex.wms.server.service.interf;


import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.model.interfaceLog.InterfaceLog;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.entity.base.Wms2SapInterfaceLog;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogFunction;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogTaskType;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogType;
import com.vtradex.wms.webservice.model.InterfaceLogSys;
import com.vtradex.wms.webservice.sap.model.SapCheckOrderInfoArray;


public interface InterfaceLogManager extends BaseManager {
	
	InterfaceLog getInterfaceLog(Long id);

//	@Transactional
//	InterfaceLog createInterfaceLog(String status, String interfaceType,String function, String fromSYS, String toSYS, Date requestTime, String requestContent, String responseContent, String reference);
	
	
	/**异步处理sap传给wms的数据
	 * 此方法不能加事务
	 * */
	void dealInterfaceLog(Long interfaceLogId);
	
	/**wms异步传输单据给sap
	 * 此方法不能加事务
	 * */
	void sendWms2SapInterfaceLog(Long wms2SapInterfaceLog);
	
	/**将wms的处理结果从interface的responseContent字段通过接口传到sap
	 * 此方法不能加事务
	 */
	void sendResponseToSap(Long interfaceLogId);
	
	/**发邮件 不加事务*/
	 void sendEmail(Long emailRecordId) ;
	
	/***
	 * 
	 * @param taskType
	 * @param type
	 * @param requestXml
	 * @param info  关键信息  存在log的request中
	 * @return
	 */
	@Transactional
	public InterfaceLog createSapToWmsInterfaceLog(String taskType, String type, String requestXml,String info);
	
	/** 
	* @Title: 创建wms给sap的日志
	* @Description: createWmsToSapInterfaceLog 
	* @return InterfaceLog   
	* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
	* @date 2017-7-17 上午10:46:53  
	* @throws 
	*/
	@Transactional
	public Wms2SapInterfaceLog createWmsToSapInterfaceLog(String taskType, String type, String requestXml,Long refrenceId,String request);
	/**
     * 创建报文
     * 
     * @param taskType {@link InterfaceLogTaskType}
     * @param type {@link InterfaceLogType}
     * @param function {@link InterfaceLogFunction}
     * @param fromSys {@link InterfaceLogSys}
     * @param toSys {@link InterfaceLogSys}
     * @param requestXml
     */
    @Transactional
    public InterfaceLog createInterfaceLog(String taskType, String type, String function, String fromSys, String toSys, String requestXml,String request);
    
    /**更新interfaceLog成成功*/
    @Transactional
    public InterfaceLog updateInterfaceLogToSucess(Long id,String responseXml,String info);
    
    /**更新interfaceLog成失败*/
    @Transactional
    public InterfaceLog updateInterfaceLogToFail(Long id,String responseXml,String info);
    
    /**创建wms2sap交货单对象*/
    @Transactional
    void createWms2SapDeliveryOrder(Long deliveryOrderId);
    
    /**创建wms2sap报文*/
    @Transactional
    Wms2SapInterfaceLog createWms2SapInterfaceLog(String code,String taskType, String type, String function, String fromSys, String toSys, String requestXml);
    @Transactional
    public void createSupplier2SapInterfacelog(Long emailRecordId);
    
    /**重新执行失败的日志*/
    @Transactional
    void resendFailInterfaceLog(InterfaceLog log);
    
    /**重新执行失败的报文信息*/
    @Transactional
    void resendFailWms2SapInterfaceLog(Wms2SapInterfaceLog log);
    
    /**
     * 创建报文
     * 
     * @param taskType {@link InterfaceLogTaskType}
     * @param type {@link InterfaceLogType}
     * @param function {@link InterfaceLogFunction}
     * @param fromSys {@link InterfaceLogSys}
     * @param toSys {@link InterfaceLogSys}
     * @param requestXml
     */
    @Transactional
    public InterfaceLog createFinishedInterfaceLog(String taskType, String type, String function, String fromSys, String toSys, String requestXml,String request);
    
    /**
     * 实时接口校验工单会直接走修改逻辑，所以需要带事务
     * @param scois
     */
    @Transactional
    public void checkProductionOrder(SapCheckOrderInfoArray scois);
}
