package com.vtradex.wms.server.service.sap.pojo;

import java.util.ArrayList;
import java.util.List;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.interfaceLog.InterfaceLog;
import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogTaskType;
import com.vtradex.wms.server.service.sap.SapDataDealManager;
import com.vtradex.wms.server.service.sap.SapRowDataDealManager;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.webservice.model.SapInterfaceType;
import com.vtradex.wms.webservice.model.TaskSubscriber;
import com.vtradex.wms.webservice.sap.base.SapCommonCallback;
import com.vtradex.wms.webservice.sap.base.SapCommonCallbackDetail;
import com.vtradex.wms.webservice.sap.model.SapCheckOrder;
import com.vtradex.wms.webservice.sap.model.SapCheckOrderArray;
import com.vtradex.wms.webservice.sap.model.SapCostCenter;
import com.vtradex.wms.webservice.sap.model.SapCostCenterArray;
import com.vtradex.wms.webservice.sap.model.SapItem;
import com.vtradex.wms.webservice.sap.model.SapItemArray;
import com.vtradex.wms.webservice.sap.model.SapDeliveryOrder;
import com.vtradex.wms.webservice.sap.model.SapDeliveryOrderArray;
import com.vtradex.wms.webservice.sap.model.SapJSCheckOrder;
import com.vtradex.wms.webservice.sap.model.SapJSCheckOrderArray;
import com.vtradex.wms.webservice.sap.model.SapPo;
import com.vtradex.wms.webservice.sap.model.SapPoArray;
import com.vtradex.wms.webservice.sap.model.SapProductOrderArray;
import com.vtradex.wms.webservice.sap.model.SapProductOrder;
import com.vtradex.wms.webservice.sap.model.SapProductOrderIn;
import com.vtradex.wms.webservice.sap.model.SapProductOrderInArray;
import com.vtradex.wms.webservice.sap.model.SapReservedData;
import com.vtradex.wms.webservice.sap.model.SapReservedDataArray;
import com.vtradex.wms.webservice.sap.model.SapReturnOrderCodeArray;
import com.vtradex.wms.webservice.sap.model.SapSaleOutDelivery;
import com.vtradex.wms.webservice.sap.model.SapSaleOutDeliveryArray;
import com.vtradex.wms.webservice.sap.model.SapSupplier;
import com.vtradex.wms.webservice.sap.model.SapSupplierArray;
import com.vtradex.wms.webservice.sap.model.SapWarehouse;
import com.vtradex.wms.webservice.sap.model.SapWarehouseArray;
import com.vtradex.wms.webservice.utils.CommonHelper;
import com.vtradex.wms.webservice.utils.WebServiceHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;

/**SAP 接口数据处理类*/
public class DefaultSapDataDealManager extends DefaultBaseManager implements SapDataDealManager {
	
	protected SapRowDataDealManager sapRowDataDealManager;
	
	public DefaultSapDataDealManager(SapRowDataDealManager sapRowDataDealManager) {
		this.sapRowDataDealManager = sapRowDataDealManager;
	}

	/**
     * 创建报文执行任务
     * 
     * @param taskType {@link InterfaceLogTaskType}
     * @param interfaceLogId
     */
    private Task createInterfaceLogTaskSendToSap(Long interfaceLogId) {
        try {
            Task task = new Task(InterfaceLogTaskType.RECEIVE_BASIC_RESPONSE, TaskSubscriber.INTERFACELOG_RESPONSEINFO2SAP, interfaceLogId);//sub不能为空
            commonDao.store(task);
            return task;
        } catch (Exception e) {
            logger.error("", e);
            throw new BusinessException("保存报文执行任务失败");
        }
    }
	 
	/**处理物料*/
	public void dealSapItem(InterfaceLog interfaceLog) {
		String xml = interfaceLog.getRequestContent();
		SapItemArray sapItemArray = new SapItemArray();
		sapItemArray = (SapItemArray)XmlObjectConver.fromXML(sapItemArray, xml);
		
		Integer rowcnt = Integer.valueOf(sapItemArray.getROWCNT().trim()); //总条数
		SapItem[] items = sapItemArray.getSapItems();
		
		if(!rowcnt.equals(items.length)) {
			String errorInfo = "头项目总条数"+rowcnt+"和实际报文条数"+items.length+"不一致";
			SapCommonCallback cb = WebServiceHelper.getCommonCallbackHeadErrorResponse(SapInterfaceType.SAP_COMMONCALLBACK_ITYPE_ITEM, sapItemArray.getMESSAGEID(), errorInfo);
			
			try {
				sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(cb),errorInfo);
				createInterfaceLogTaskSendToSap(interfaceLog.getId());
			}
			catch(Exception e) {
				e.printStackTrace();
			}
//			return ;
			throw new BusinessException(errorInfo);
		}
		
		String messageId = sapItemArray.getMESSAGEID();
		List<SapCommonCallbackDetail> details = new ArrayList<SapCommonCallbackDetail>();
		//生成业务数据；
		 SapCommonCallbackDetail detail = null;
		 String errorInfo = "";
	     try {
	    	 sapRowDataDealManager.dealSapItem(sapItemArray);
	    	 detail = WebServiceHelper.getSapCommonCallbackDetailSucess("");
	     }
	     catch(Exception e) {
	    	 e.printStackTrace();
	    	 errorInfo = "处理失败:"+StringHelper.substring(CommonHelper.getErrorMessageByException(e),255);
	    	 detail = WebServiceHelper.getSapCommonCallbackDetailError("", errorInfo);
	     }
	    details.add(detail);
	    SapCommonCallback commBack = WebServiceHelper.getCommonCallbackResponse(messageId,SapInterfaceType.SAP_COMMONCALLBACK_ITYPE_ITEM, details);
		sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(commBack),errorInfo);
		createInterfaceLogTaskSendToSap(interfaceLog.getId());
		
		if(!StringHelper.isNullOrEmpty(errorInfo)) {
			throw new BusinessException(errorInfo);
		}
	}

	/**处理仓库*/
    public void dealSapWarehouse(InterfaceLog interfaceLog) {
        String xml  = interfaceLog.getRequestContent();
        SapWarehouseArray sapWarehouseArray = new SapWarehouseArray();
        sapWarehouseArray = (SapWarehouseArray) XmlObjectConver.fromXML(sapWarehouseArray, xml);
        
        Integer rowcnt = Integer.valueOf(sapWarehouseArray.getROWCNT().trim()); //总条数
        
        SapWarehouse[] Warehouses = sapWarehouseArray.getSapWarehouses();
        
        if(!rowcnt.equals(Warehouses.length)) {
            String errorInfo = "头项目总条数"+rowcnt+"和实际报文条数"+Warehouses.length+"不一致";
            SapCommonCallback cb = WebServiceHelper.getCommonCallbackHeadErrorResponse(SapInterfaceType.SAP_COMMONCALLBACK_ITYPE_WAREHOUSE, sapWarehouseArray.getMESSAGEID(), errorInfo);
            
            try {
                sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(cb),errorInfo);
                createInterfaceLogTaskSendToSap(interfaceLog.getId());
            } catch(Exception e) {
                e.printStackTrace();
            }
            
//            return ;
            throw new BusinessException(errorInfo);//抛异常，task状态也改成失败
        }
        
        String messageId = sapWarehouseArray.getMESSAGEID();
        List<SapCommonCallbackDetail> details = new ArrayList<SapCommonCallbackDetail>();
        //生成业务数据；
        SapCommonCallbackDetail detail = null;
        String errorInfo ="";
        //逐条处理生成业务数据
        try {
            sapRowDataDealManager.dealSapWarehouse(sapWarehouseArray);
            detail = WebServiceHelper.getSapCommonCallbackDetailSucess("");
        } catch(Exception e) {
            e.printStackTrace();
            errorInfo = "处理失败:"+StringHelper.substring(CommonHelper.getErrorMessageByException(e), 255);
            detail = WebServiceHelper.getSapCommonCallbackDetailError("", errorInfo);
        }
        details.add(detail);
        SapCommonCallback commBack = WebServiceHelper.getCommonCallbackResponse(messageId,SapInterfaceType.SAP_COMMONCALLBACK_ITYPE_WAREHOUSE, details);
        sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(commBack),errorInfo);
        createInterfaceLogTaskSendToSap(interfaceLog.getId());
        
        if(!StringHelper.isNullOrEmpty(errorInfo)) {
			throw new BusinessException(errorInfo);
		}
    }

    /**处理供应商*/
    public void dealSapSupplier(InterfaceLog interfaceLog) {
        String xml  = interfaceLog.getRequestContent();
        SapSupplierArray sapSupplierArray = new SapSupplierArray();
        sapSupplierArray = (SapSupplierArray) XmlObjectConver.fromXML(sapSupplierArray, xml);
        
        Integer rowcnt = Integer.valueOf(sapSupplierArray.getROWCNT().trim()); //总条数
        
        SapSupplier[] suppliers = sapSupplierArray.getSapSuppliers();
        
        if(!rowcnt.equals(suppliers.length)) {
            String errorInfo = "头项目总条数"+rowcnt+"和实际报文条数"+suppliers.length+"不一致";
            SapCommonCallback cb = WebServiceHelper.getCommonCallbackHeadErrorResponse(SapInterfaceType.SAP_COMMONCALLBACK_ITYPE_SUPPLIER, sapSupplierArray.getMESSAGEID(), errorInfo);
            
            try {
                sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(cb),errorInfo);
                createInterfaceLogTaskSendToSap(interfaceLog.getId());
            } catch(Exception e) {
                e.printStackTrace();
            }
            throw new BusinessException(errorInfo);
        }
        String messageId = sapSupplierArray.getMESSAGEID();
        List<SapCommonCallbackDetail> details = new ArrayList<SapCommonCallbackDetail>();
        //生成业务数据；
        SapCommonCallbackDetail detail = null;
        String errorInfo ="";
            //逐条处理生成业务数据
        try {
            sapRowDataDealManager.dealSapSupplier(sapSupplierArray);
            detail = WebServiceHelper.getSapCommonCallbackDetailSucess("");
        } catch(Exception e) {
            e.printStackTrace();
            errorInfo = "处理失败:"+StringHelper.substring(CommonHelper.getErrorMessageByException(e), 255);
            detail = WebServiceHelper.getSapCommonCallbackDetailError("",errorInfo);
        }
        details.add(detail);
        SapCommonCallback commBack = WebServiceHelper.getCommonCallbackResponse(messageId,SapInterfaceType.SAP_COMMONCALLBACK_ITYPE_SUPPLIER, details);
        sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(commBack),errorInfo);
        createInterfaceLogTaskSendToSap(interfaceLog.getId());
        
        if(!StringHelper.isNullOrEmpty(errorInfo)) {
			throw new BusinessException(errorInfo);
		}
    }

	@Override
	public void dealSapDeliveryOrder(InterfaceLog interfaceLog) {
		String xml  = interfaceLog.getRequestContent();
		SapDeliveryOrderArray spoas = new SapDeliveryOrderArray();
		spoas = (SapDeliveryOrderArray) XmlObjectConver.fromXML(spoas, xml);
		
		Integer rowcnt = Integer.valueOf(spoas.getROWCNT().trim()); //总条数
		
		SapDeliveryOrder [] spas = spoas.getSpoas();
		
		 if(!rowcnt.equals(spas.length)) {
	            String errorInfo = "头项目总条数"+rowcnt+"和实际报文条数"+spas.length+"不一致";
	            SapCommonCallback cb = WebServiceHelper.getCommonCallbackHeadErrorResponse(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_PUO, spoas.getMESSAGEID(), errorInfo);
	            try {
	                sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(cb),errorInfo);
	                createInterfaceLogTaskSendToSap(interfaceLog.getId());
	            } catch(Exception e) {
	                e.printStackTrace();
	            }
	            throw new BusinessException(errorInfo);
        }
		if(rowcnt > 1){
			String errorInfo = "实际报文有多条明细行";
	        SapCommonCallback cb = WebServiceHelper.getCommonCallbackHeadErrorResponse(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_PUO, spoas.getMESSAGEID(), errorInfo);
	        try {
	            sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(cb),errorInfo);
	            createInterfaceLogTaskSendToSap(interfaceLog.getId());
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	        throw new BusinessException(errorInfo);
		}
	   String messageId = spoas.getMESSAGEID();
	   List<SapCommonCallbackDetail> details = new ArrayList<SapCommonCallbackDetail>();
       SapCommonCallbackDetail detail = null;
       String errorInfo ="";
       //逐条处理生成业务数;
       try {
           sapRowDataDealManager.dealSapDeliveryOrder(spoas);
           detail = WebServiceHelper.getSapCommonCallbackDetailSucess("");
       } catch(Exception e) {
    	   e.printStackTrace();
    	   errorInfo = "处理失败:"+StringHelper.substring(CommonHelper.getErrorMessageByException(e), 255);
    	   detail = WebServiceHelper.getSapCommonCallbackDetailError("", errorInfo);
       }
       details.add(detail);
       SapCommonCallback commBack = WebServiceHelper.getCommonCallbackResponse(messageId,SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_PUO, details);
       sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(commBack),errorInfo);
       createInterfaceLogTaskSendToSap(interfaceLog.getId());
       
       if(!StringHelper.isNullOrEmpty(errorInfo)) {
			throw new BusinessException(errorInfo);
       }
	}

	@Override
	public void dealSapPoOrder(InterfaceLog interfaceLog) {
		String xml  = interfaceLog.getRequestContent();
		SapPoArray pos = new SapPoArray();
		pos = (SapPoArray)XmlObjectConver.fromXML(pos, xml);
		
		Integer rowcnt = Integer.valueOf(pos.getROWCNT().trim()); //总条数
		
		SapPo[] sps = pos.getSapPos();
		 if(!rowcnt.equals(sps.length)) {
	            String errorInfo = "头项目总条数"+rowcnt+"和实际报文条数"+sps.length+"不一致";
	            SapCommonCallback cb = WebServiceHelper.getCommonCallbackHeadErrorResponse(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_PO, pos.getMESSAGEID(), errorInfo);
	            try {
	                sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(cb),errorInfo);
	                createInterfaceLogTaskSendToSap(interfaceLog.getId());
	            } catch(Exception e) {
	                e.printStackTrace();
	            }
	            throw new BusinessException(errorInfo);
		 }
		 String messageId = pos.getMESSAGEID();
	     List<SapCommonCallbackDetail> details = new ArrayList<SapCommonCallbackDetail>();
	     SapCommonCallbackDetail detail = null;
	     String errorInfo ="";
	     try {
	    	 sapRowDataDealManager.dealSapPoOrders(pos);
	    	 detail = WebServiceHelper.getSapCommonCallbackDetailSucess("");
	     }
	     catch(Exception e) {
	    	 e.printStackTrace();
	    	 errorInfo = "处理失败:"+StringHelper.substring(CommonHelper.getErrorMessageByException(e), 255);
	         detail = WebServiceHelper.getSapCommonCallbackDetailError("",errorInfo);
	     }
	     details.add(detail);
	     
	     SapCommonCallback commBack = WebServiceHelper.getCommonCallbackResponse(messageId,SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_PO, details);
	     sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(commBack),errorInfo);
	     createInterfaceLogTaskSendToSap(interfaceLog.getId());
	     
	     if(!StringHelper.isNullOrEmpty(errorInfo)) {
				throw new BusinessException(errorInfo);
	     }
	}

	@Override
	public void dealSapDeliveryCode(InterfaceLog interfaceLog) {
		String xml  = interfaceLog.getRequestContent();
		SapReturnOrderCodeArray srocs = new SapReturnOrderCodeArray();
		srocs = (SapReturnOrderCodeArray)XmlObjectConver.fromXML(srocs, xml);
		List<SapCommonCallbackDetail> details = new ArrayList<SapCommonCallbackDetail>();
		SapCommonCallbackDetail detail = null;
		String errorInfo ="";
		try {
            sapRowDataDealManager.dealSapReturnOrderCode(srocs);
            detail = WebServiceHelper.getSapCommonCallbackDetailSucess("");
        } catch(Exception e) {
            e.printStackTrace();
            errorInfo = "处理失败:"+StringHelper.substring(CommonHelper.getErrorMessageByException(e), 255);
            detail = WebServiceHelper.getSapCommonCallbackDetailError("",errorInfo);
        }
        details.add(detail);
	    SapCommonCallback commBack = WebServiceHelper.getCommonCallbackResponse("",SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_DOCODE, details);
	    sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(commBack),errorInfo);
	    createInterfaceLogTaskSendToSap(interfaceLog.getId());
	    if(!StringHelper.isNullOrEmpty(errorInfo)) {
			throw new BusinessException(errorInfo);
	    }
	}

	@Override
	public void dealSapProductOrder(InterfaceLog interfaceLog) {
		String xml  = interfaceLog.getRequestContent();
		SapProductOrderArray spoas = new SapProductOrderArray();
		spoas = (SapProductOrderArray)XmlObjectConver.fromXML(spoas, xml);
		
		Integer rowcnt = Integer.valueOf(spoas.getROWCNT().trim()); //总条数
		
		SapProductOrder[] spos = spoas.getSpos();
		if(!rowcnt.equals(spos.length)) {
            String errorInfo = "头项目总条数"+rowcnt+"和实际报文条数"+spos.length+"不一致";
            SapCommonCallback cb = WebServiceHelper.getCommonCallbackHeadErrorResponse(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_PRO, spoas.getMESSAGEID(), errorInfo);
            try {
                sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(cb),errorInfo);
                createInterfaceLogTaskSendToSap(interfaceLog.getId());
            } catch(Exception e) {
                e.printStackTrace();
            }
            throw new BusinessException(errorInfo);
		}
		 String messageId = spoas.getMESSAGEID();
	     List<SapCommonCallbackDetail> details = new ArrayList<SapCommonCallbackDetail>();

	     SapCommonCallbackDetail detail = null;
	     String errorInfo ="";
    	 //逐条处理生成业务数据
         try {
             sapRowDataDealManager.dealSapProductOrder(spoas);
             detail = WebServiceHelper.getSapCommonCallbackDetailSucess("");
         } catch(Exception e) {
             e.printStackTrace();
             errorInfo = "处理失败:"+StringHelper.substring(CommonHelper.getErrorMessageByException(e), 255);
             detail = WebServiceHelper.getSapCommonCallbackDetailError("",errorInfo);
         }
         details.add(detail);
	     SapCommonCallback commBack = WebServiceHelper.getCommonCallbackResponse(messageId,SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_PRO, details);
	     sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(commBack),errorInfo);
	     createInterfaceLogTaskSendToSap(interfaceLog.getId());
	     if(!StringHelper.isNullOrEmpty(errorInfo)) {
				throw new BusinessException(errorInfo);
	     }
		}

	@Override
	public void dealSapReservedData(InterfaceLog interfaceLog) {
		String xml  = interfaceLog.getRequestContent();
		SapReservedDataArray datas = new SapReservedDataArray();
		datas = (SapReservedDataArray)XmlObjectConver.fromXML(datas, xml);
		
		Integer rowcnt = Integer.valueOf(datas.getROWCNT().trim()); //总条数
		
		SapReservedData[] dts = datas.getDatas();
		if(!rowcnt.equals(dts.length)) {
            String errorInfo = "头项目总条数"+rowcnt+"和实际报文条数"+dts.length+"不一致";
            SapCommonCallback cb = WebServiceHelper.getCommonCallbackHeadErrorResponse(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_DOCODE, datas.getMESSAGEID(), errorInfo);
            try {
                sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(cb),errorInfo);
                createInterfaceLogTaskSendToSap(interfaceLog.getId());
            } catch(Exception e) {
                e.printStackTrace();
            }
            throw new BusinessException(errorInfo);
		}
		String messageId = datas.getMESSAGEID();
		List<SapCommonCallbackDetail> details = new ArrayList<SapCommonCallbackDetail>();
		//生成业务数据
	    SapCommonCallbackDetail detail = null;
	    String errorInfo ="";
	    try {
	       sapRowDataDealManager.dealSapReservedData(datas);
	       detail = WebServiceHelper.getSapCommonCallbackDetailSucess("");
	     } catch(Exception e) {
	       e.printStackTrace();
	       errorInfo = "处理失败:"+StringHelper.substring(CommonHelper.getErrorMessageByException(e), 255);
           detail = WebServiceHelper.getSapCommonCallbackDetailError("",errorInfo);
	     }
	     details.add(detail);
		 SapCommonCallback commBack = WebServiceHelper.getCommonCallbackResponse(messageId,SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_REDATA, details);
		 sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(commBack),errorInfo);
		 createInterfaceLogTaskSendToSap(interfaceLog.getId());
		 if(!StringHelper.isNullOrEmpty(errorInfo)) {
				throw new BusinessException(errorInfo);
	     }
	}

	@Override
	public void dealSapSaleOutDelivery(InterfaceLog interfaceLog) {
		String xml  = interfaceLog.getRequestContent();
		SapSaleOutDeliveryArray datas = new SapSaleOutDeliveryArray();
		datas = (SapSaleOutDeliveryArray)XmlObjectConver.fromXML(datas, xml);
		Integer rowcnt = Integer.valueOf(datas.getROWCNT().trim()); //总条数
		
		SapSaleOutDelivery[] dts = datas.getSsods();
		if(!rowcnt.equals(dts.length)) {
            String errorInfo = "头项目总条数"+rowcnt+"和实际报文条数"+dts.length+"不一致";
            SapCommonCallback cb = WebServiceHelper.getCommonCallbackHeadErrorResponse(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_SOD, datas.getMESSAGEID(), errorInfo);
            try {
                sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(cb),errorInfo);
                createInterfaceLogTaskSendToSap(interfaceLog.getId());
            } catch(Exception e) {
                e.printStackTrace();
            }
            throw new BusinessException(errorInfo);
		}
		/*if(rowcnt > 1){
			String errorInfo = "实际报文有多条明细行";
            SapCommonCallback cb = WebServiceHelper.getCommonCallbackHeadErrorResponse(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_SOD, datas.getMESSAGEID(), errorInfo);
            try {
                sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(cb));
                createInterfaceLogTaskSendToSap(interfaceLog.getId());
            } catch(Exception e) {
                e.printStackTrace();
            }
            return ;
		}*/
		String messageId = datas.getMESSAGEID();
		List<SapCommonCallbackDetail> details = new ArrayList<SapCommonCallbackDetail>();
		//生成业务数据
    	 SapCommonCallbackDetail detail = null;
    	 String errorInfo ="";
	     try {
	         sapRowDataDealManager.dealSapSaleOutDelivery(datas);
	         detail = WebServiceHelper.getSapCommonCallbackDetailSucess("");
	     } catch(Exception e) {
	    	   e.printStackTrace();
	    	   errorInfo = "处理失败:"+StringHelper.substring(CommonHelper.getErrorMessageByException(e), 255);
	           detail = WebServiceHelper.getSapCommonCallbackDetailError("",errorInfo);
	     }
         details.add(detail);
	     SapCommonCallback commBack = WebServiceHelper.getCommonCallbackResponse(messageId,SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_SOD, details);
	     sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(commBack),errorInfo);
	     createInterfaceLogTaskSendToSap(interfaceLog.getId());
	     
	     if(!StringHelper.isNullOrEmpty(errorInfo)) {
				throw new BusinessException(errorInfo);
	     }
	}
	
	public void dealSapCheckOrder(InterfaceLog interfaceLog){
		String xml  = interfaceLog.getRequestContent();
		SapCheckOrderArray scoas = new SapCheckOrderArray();
		scoas = (SapCheckOrderArray) XmlObjectConver.fromXML(scoas, xml);
		Integer rowcnt = Integer.valueOf(scoas.getROWCNT().trim()); //总条数
		SapCheckOrder[] scos = scoas.getScos();
		if(!rowcnt.equals(scos.length)) {
            String errorInfo = "头项目总条数"+rowcnt+"和实际报文条数"+scos.length+"不一致";
            SapCommonCallback cb = WebServiceHelper.getCommonCallbackHeadErrorResponse(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_CHECKORDER, scoas.getMESSAGEID(), errorInfo);
            try {
                sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(cb),errorInfo);
                createInterfaceLogTaskSendToSap(interfaceLog.getId());
            } catch(Exception e) {
                e.printStackTrace();
            }
            throw new BusinessException(errorInfo);
		}
		String messageId = scoas.getMESSAGEID();
		List<SapCommonCallbackDetail> details = new ArrayList<SapCommonCallbackDetail>();
		//生成业务数据
    	SapCommonCallbackDetail detail = null;
    	String errorInfo ="";
    	try{
    		sapRowDataDealManager.dealSapCheckOrder(scoas);
    		detail = WebServiceHelper.getSapCommonCallbackDetailSucess("");
    	}catch(Exception e) {
	    	e.printStackTrace();
	    	errorInfo = "处理失败:"+StringHelper.substring(CommonHelper.getErrorMessageByException(e), 255);
	        detail = WebServiceHelper.getSapCommonCallbackDetailError("",errorInfo);
	    }
        details.add(detail);
	    SapCommonCallback commBack = WebServiceHelper.getCommonCallbackResponse(messageId,SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_CHECKORDER, details);
	    sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(commBack),errorInfo);
	    createInterfaceLogTaskSendToSap(interfaceLog.getId());
	    
	    if(!StringHelper.isNullOrEmpty(errorInfo)) {
			throw new BusinessException(errorInfo);
        }
	}
	
	public void dealSapJSCheckOrder(InterfaceLog interfaceLog){
		String xml  = interfaceLog.getRequestContent();
		SapJSCheckOrderArray scoas = new SapJSCheckOrderArray();
		scoas = (SapJSCheckOrderArray) XmlObjectConver.fromXML(scoas, xml);
		Integer rowcnt = Integer.valueOf(scoas.getROWCNT().trim()); //总条数
		SapJSCheckOrder[] scos = scoas.getScos();
		if(!rowcnt.equals(scos.length)) {
            String errorInfo = "头项目总条数"+rowcnt+"和实际报文条数"+scos.length+"不一致";
            SapCommonCallback cb = WebServiceHelper.getCommonCallbackHeadErrorResponse(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_JSCHECKORDER, scoas.getMESSAGEID(), errorInfo);
            try {
                sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(cb),errorInfo);
                createInterfaceLogTaskSendToSap(interfaceLog.getId());
            } catch(Exception e) {
                e.printStackTrace();
            }
            throw new BusinessException(errorInfo);
		}
		String messageId = scoas.getMESSAGEID();
		List<SapCommonCallbackDetail> details = new ArrayList<SapCommonCallbackDetail>();
		//生成业务数据
    	SapCommonCallbackDetail detail = null;
    	String errorInfo ="";
    	try{
    		sapRowDataDealManager.dealSapJSCheckOrder(scoas);
    		detail = WebServiceHelper.getSapCommonCallbackDetailSucess("");
    	}catch(Exception e) {
	    	e.printStackTrace();
	    	errorInfo = "处理失败:"+StringHelper.substring(CommonHelper.getErrorMessageByException(e), 255);
	        detail = WebServiceHelper.getSapCommonCallbackDetailError("",errorInfo);
	    }
        details.add(detail);
	    SapCommonCallback commBack = WebServiceHelper.getCommonCallbackResponse(messageId,SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_JSCHECKORDER, details);
	    sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(commBack),errorInfo);
	    createInterfaceLogTaskSendToSap(interfaceLog.getId());
	    
	    if(!StringHelper.isNullOrEmpty(errorInfo)) {
			throw new BusinessException(errorInfo);
        }
	}

	@Override
	public void dealSapProductOrderIn(InterfaceLog interfaceLog) {
		System.out.println("开始处理生产订单入库................");
		String xml  = interfaceLog.getRequestContent();
		SapProductOrderInArray spoias = new SapProductOrderInArray();
		spoias = (SapProductOrderInArray) XmlObjectConver.fromXML(spoias, xml);
		Integer rowcnt = Integer.valueOf(spoias.getROWCNT().trim()); //总条数
		SapProductOrderIn[] spos = spoias.getSpois();
		if(!rowcnt.equals(spos.length)) {
            String errorInfo = "头项目总条数"+rowcnt+"和实际报文条数"+spos.length+"不一致";
            SapCommonCallback cb = WebServiceHelper.getCommonCallbackHeadErrorResponse(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_PROIN, spoias.getMESSAGEID(), errorInfo);
            try {
                sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(cb),errorInfo);
                createInterfaceLogTaskSendToSap(interfaceLog.getId());
            } catch(Exception e) {
                e.printStackTrace();
            }
            throw new BusinessException(errorInfo);
		}
		String messageId = spoias.getMESSAGEID();
		List<SapCommonCallbackDetail> details = new ArrayList<SapCommonCallbackDetail>();
		//生成业务数据
    	SapCommonCallbackDetail detail = null;
    	String errorInfo ="";
    	try{
    		sapRowDataDealManager.dealSapProductOrderIn(spoias);
    		detail = WebServiceHelper.getSapCommonCallbackDetailSucess("");
    	}catch(Exception e) {
	    	e.printStackTrace();
	    	errorInfo = "处理失败:"+StringHelper.substring(CommonHelper.getErrorMessageByException(e), 255);
//	    	if(errorInfo.contains("在WMS系统已成功出库，不允许再次出库")){
//	    		detail = WebServiceHelper.getSapCommonCallbackDetailSucess("");
//	    	}else{
	    		detail = WebServiceHelper.getSapCommonCallbackDetailError("",errorInfo);
//	    	}
	    }
        details.add(detail);
	    SapCommonCallback commBack = WebServiceHelper.getCommonCallbackResponse(messageId,SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_PROIN, details);
	    sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(commBack),errorInfo);
	    createInterfaceLogTaskSendToSap(interfaceLog.getId());
	    
	    if(!StringHelper.isNullOrEmpty(errorInfo)) {
			throw new BusinessException(errorInfo);
        }
	}
	
	public void dealSapCostCenter(InterfaceLog interfaceLog){
		System.out.println("开始成本中心主数据................");
		String xml  = interfaceLog.getRequestContent();
		SapCostCenterArray sccs = new SapCostCenterArray();
		sccs =  (SapCostCenterArray) XmlObjectConver.fromXML(sccs, xml);
		Integer rowcnt = Integer.valueOf(sccs.getROWCNT().trim()); //总条数
		SapCostCenter[] scc = sccs.getSccs();
		if(!rowcnt.equals(scc.length)) {
            String errorInfo = "头项目总条数"+rowcnt+"和实际报文条数"+scc.length+"不一致";
            SapCommonCallback cb = WebServiceHelper.getCommonCallbackHeadErrorResponse(SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_COST, sccs.getMESSAGEID(), errorInfo);
            try {
                sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(cb),errorInfo);
                createInterfaceLogTaskSendToSap(interfaceLog.getId());
            } catch(Exception e) {
                e.printStackTrace();
            }
            throw new BusinessException(errorInfo);
		}
		String messageId = sccs.getMESSAGEID();
		List<SapCommonCallbackDetail> details = new ArrayList<SapCommonCallbackDetail>();
		//生成业务数据
    	SapCommonCallbackDetail detail = null;
    	String errorInfo ="";
    	try{
    		sapRowDataDealManager.dealSapCostCenter(sccs);
    		detail = WebServiceHelper.getSapCommonCallbackDetailSucess("");
    	}catch(Exception e) {
	    	e.printStackTrace();
	    	errorInfo = "处理失败:"+StringHelper.substring(CommonHelper.getErrorMessageByException(e), 255);
	        detail = WebServiceHelper.getSapCommonCallbackDetailError("",errorInfo);
	    }
        details.add(detail);
	    SapCommonCallback commBack = WebServiceHelper.getCommonCallbackResponse(messageId,SapInterfaceType.SAP_COMMONCALLBACK_ITEYP_COST, details);
	    sapRowDataDealManager.storeInterfaceLog(interfaceLog, XmlObjectConver.toXML(commBack),errorInfo);
	    createInterfaceLogTaskSendToSap(interfaceLog.getId());
	    
	    if(!StringHelper.isNullOrEmpty(errorInfo)) {
			throw new BusinessException(errorInfo);
        }
	}
}
