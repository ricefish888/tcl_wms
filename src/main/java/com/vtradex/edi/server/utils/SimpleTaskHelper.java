package com.vtradex.edi.server.utils;

import java.util.List;

import com.vtradex.edi.server.AbstractTaskHelper;
import com.vtradex.edi.server.service.task.TaskManager;
import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.model.message.TaskStatus;
import com.vtradex.thorn.server.service.message.IMessageService;
import com.vtradex.wms.server.service.item.TclMessageManager;
import com.vtradex.wms.server.service.task.CrontabManager;
import com.vtradex.wms.server.utils.DateUtil;

/**
 * 任务处理
 * 
 * @author <a href="mailto:brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.2 $Date: 2014/11/24 05:30:52 $
 */
@SuppressWarnings("unchecked")
public class SimpleTaskHelper extends AbstractTaskHelper {
	
	protected IMessageService messageService;
	
	protected TaskManager ediTaskManager;
	
	protected TclMessageManager tclMessageManager;
	
	protected CrontabManager crontabManager;
	
	private static int cnt= 500;//每次取出的条数
	
	private static int emailcnt =100;
	
//	private static final int SEND_SMS_SLEEP=5*60*1000; //发短信睡眠时间。
//	
//	private static final int SEND_DATA_TO_ERP_SLEEP = 5*60*1000;//发送包装单位到erp的睡眠时间
//	
//	private static final int SEND_INV_DATA_TO_ERP_SLEEP = 5*60*1000;//发送库存信息到erp的睡眠时间

	public SimpleTaskHelper(TaskManager ediTaskManager, 
			IMessageService messageService,
			TclMessageManager tclMessageManager) {
		this.ediTaskManager = ediTaskManager;
		this.messageService = messageService;
		this.tclMessageManager = tclMessageManager;
	}
	private static final String SEND_TO_SAP_TASK_TYPE =  "'SEND_DELIVERY','SEND_ITEMPROPERTY','SEND_SURPPLIERBUSINESS','SEND_DAYLYINVENTORY','SEND_CANCELRECEIVEINFO','SEND_PICKCONFIRMINFO','SEND_RECEIVEINFO','SEND_QCRECORDINFO','SEND_PRODUCTIONINFO','SEND_SHIPRESINFO','SEND_PRDRETURNINFO','SEND_OTHEROUTINFO','SEND_OTHERININFO','SEND_BFOUTORININFO','SEND_KNDBINFO','SEND_DBCKINFO','SEND_XSJHDINFO'";

	private static final String SEND_EMAIL_TYPE="'SEND_EMAIL'";
	
	private static final String SEND_IMPORT_PRODORDER="'IMPORTPRODUCTIONORDER'";
	/***
     * 取标准需要标准edi跑的异步任务 由于task表里还有同步telnet规则上线下线的任务 此处只取message中的需要标准edi跑的异步消息
     */
	protected List<Long> loadTaskIdsByMustStandardEdiRun() {
        String hql = "SELECT t.id FROM Task t WHERE t.status=:status " +
        		"AND t.type not in ("+SEND_EMAIL_TYPE+","+SEND_TO_SAP_TASK_TYPE + ","+SEND_IMPORT_PRODORDER+" ) Order By t.id ASC";
        return messageService.findByQueryAndParamNum(hql, new String[] { "status", }, new Object[] {TaskStatus.STAT_READY }, cnt);
    }
	
	/***
     * 取EMAIL任务
     */
	protected List<Long> loadTaskIdsByMustStandardEdiRun_EMAIL() {
        String hql = "SELECT t.id FROM Task t WHERE t.status=:status " +
        		"AND t.type = "+SEND_EMAIL_TYPE+" Order By t.id ASC";
        return messageService.findByQueryAndParamNum(hql, new String[] { "status", }, new Object[] {TaskStatus.STAT_READY }, emailcnt);
    }
	
	/***
     * 取工单导入任务
     */
	protected List<Long> loadTaskIdsByMustStandardEdiRun_IMPPO() {
        String hql = "SELECT t.id FROM Task t WHERE t.status=:status " +
        		"AND t.type = "+SEND_IMPORT_PRODORDER+" Order By t.id ASC";
        return messageService.findByQueryAndParamNum(hql, new String[] { "status", }, new Object[] {TaskStatus.STAT_READY }, emailcnt);
    }
	
	/***
     * 
     */
	protected List<Long> loadTaskIdsWms2SAP() {
        String hql = "SELECT t.id FROM Task t WHERE t.status=:status " +
        		   "AND t.type in ("+SEND_TO_SAP_TASK_TYPE+") Order By t.id ASC";
        return messageService.findByQueryAndParamNum(hql, new String[] { "status", }, new Object[] {TaskStatus.STAT_READY }, cnt);
    }
	
	protected List<Long> loadWaiTaskIds(){
		String hql = "SELECT t.id FROM Task t WHERE t.status=:status " +
		"AND t.type in ('SEND_WAIT')";
		return messageService.findByQueryAndParamNum(hql, new String[] { "status", }, new Object[] {TaskStatus.STAT_READY }, cnt);

	}
	/**
	 * 处理email任务
	 */
	public void doHandleEmail() {
	    System.out.println(DateUtil.getDateTime()+" "+"SimpleTaskHelper[doHandleEmail] started....................");
        while (true) {
            List<Long> taskIds = loadTaskIdsByMustStandardEdiRun_EMAIL();
            for (Long id : taskIds) {
                Task task = ediTaskManager.load(Task.class, id);
                try {
                    ediTaskManager.doHandle(task);
                    task.setFinshStatus();
                } 
                catch (Exception e) {
                    task.setFailStatus();
                }
                messageService.storeEntity(task);
            }
            try {
                Thread.sleep(60*1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!isApplicationAvailable) {
                System.out.println("SimpleTaskHelper[doHandle] end....................");
                break;
            }
        }
	}
	/**
	 * 处理任务
	 */
	public void doHandle() {
	    System.out.println(DateUtil.getDateTime()+" "+"SimpleTaskHelper[doHandle] started....................");
        while (true) {
            List<Long> taskIds = loadTaskIdsByMustStandardEdiRun();
            for (Long id : taskIds) {
                Task task = ediTaskManager.load(Task.class, id);
                try {
                    ediTaskManager.doHandle(task);
                    task.setFinshStatus();
                } 
                catch (Exception e) {
                    task.setFailStatus();
                }
                messageService.storeEntity(task);
            }
            try {
                Thread.sleep(5*1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!isApplicationAvailable) {
                System.out.println("SimpleTaskHelper[doHandle] end....................");
                break;
            }
        }
	}
	
	/**
	 * 处理wms2sap任务
	 */
	public void doWms2SAPHandle() {
	    System.out.println(DateUtil.getDateTime()+" "+"SimpleTaskHelper[doWms2SAPHandle] started....................");
        while (true) {
            List<Long> taskIds = loadTaskIdsWms2SAP();
            for (Long id : taskIds) {
                Task task = ediTaskManager.load(Task.class, id);
                try {
                    ediTaskManager.doHandle(task);
                    task.setFinshStatus();
                } 
                catch (Exception e) {
                    task.setFailStatus();
                }
                messageService.storeEntity(task);
            }
            try {
                Thread.sleep(5*1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!isApplicationAvailable) {
                System.out.println("SimpleTaskHelper[doHandle] end....................");
                break;
            }
        }
	}
	/**生成台账XML报文，准备传sap*/
	public void doGenLedgerXmlHandle() {
		System.out.println(DateUtil.getDateTime()+" "+"SimpleTaskHelper[doGenLedgerXmlHandle] started....................");
        while (true) {
        	try {
        		tclMessageManager.genSendToSapLog();
        	}
        	catch(Exception e) {
        		e.printStackTrace();
        	}
            try {
                Thread.sleep(10*60*1000L); //睡10分钟
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!isApplicationAvailable) {
                System.out.println("SimpleTaskHelper[doGenLedgerXmlHandle] end....................");
                break;
            }
        }
	}
	
	/**拣配工单自动导入执行*/
	public void doProductionOrderJPImportHandle() {
		
		System.out.println(DateUtil.getDateTime()+" "+"SimpleTaskHelper[doProductionOrderJPImportHandle] started....................");
		 while (true) {
	            List<Long> taskIds = loadTaskIdsByMustStandardEdiRun_IMPPO();
	            for (Long id : taskIds) {
	                Task task = ediTaskManager.load(Task.class, id);
	                try {
	                    ediTaskManager.doHandle(task);
	                    task.setFinshStatus();
	                } 
	                catch (Exception e) {
	                    task.setFailStatus();
	                }
	                messageService.storeEntity(task);
	            }
	            try {
	                Thread.sleep(60*1000L); //睡眠60秒
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            if (!isApplicationAvailable) {
	                System.out.println("SimpleTaskHelper[doProductionOrderJPImportHandle] end....................");
	                break;
	            }
	        }
		
	}
	

	
}