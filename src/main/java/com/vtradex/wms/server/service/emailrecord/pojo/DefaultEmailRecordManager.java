package com.vtradex.wms.server.service.emailrecord.pojo;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.model.message.TaskStatus;
import com.vtradex.thorn.server.service.mail.MailService;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.entity.email.EmailRecord;
import com.vtradex.wms.server.service.emailrecord.EmailRecordManager;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogStatus;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogTaskType;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.webservice.model.TaskSubscriber;

public class DefaultEmailRecordManager extends DefaultBaseManager implements EmailRecordManager {

	protected MailService mailService;
	
	public DefaultEmailRecordManager(MailService mailService) {
		this.mailService = mailService;
	}
	
	public void storeEmailRecordWaitSend(String receiver,String to,String subject,String content,String cc,String emailtype,String realteCode) {
		EmailRecord record = new EmailRecord(receiver, to, subject, content, cc, emailtype, realteCode);
		commonDao.store(record);
		resend(record);
		 
	}
	public void resend(EmailRecord er) {
		 try {
			 er.setStatus(InterfaceLogStatus.STAT_READY);
			 commonDao.store(er);
			 String hql = "FROM Task task WHERE task.messageId =:messageId AND task.status=:status AND task.type=:type ORDER BY task.id DESC";
			 List<Task> tasks = commonDao.findByQuery(hql, new String[]{"messageId","status","type"}, new Object[]{er.getId(),TaskStatus.STAT_FAIL,InterfaceLogTaskType.SEND_EMAIL});
			 if(tasks.isEmpty()){
				 Task task = new Task(InterfaceLogTaskType.SEND_EMAIL, TaskSubscriber.SEND_EMAIL, er.getId());//sub不能为空
		         commonDao.store(task);
			 }else{
				 Task task = tasks.get(0);
				 task.setStatus(TaskStatus.STAT_READY);
				 commonDao.store(task);
			 }
//	            Task task = new Task(InterfaceLogTaskType.SEND_EMAIL, TaskSubscriber.SEND_EMAIL, er.getId());//sub不能为空
//	            commonDao.store(task);
	        } catch (Exception e) {
	            logger.error("", e);
	            throw new BusinessException("保存报文执行任务失败");
	        }
	}
	
	public void sendEmail(Long emailRecordId) {
		EmailRecord er = commonDao.load(EmailRecord.class, emailRecordId);
		if(er==null) {
			throw new BusinessException("邮件管理中未找到序号为"+emailRecordId+"的记录");
		}
		sendEmail(er);
	}
	
	public void sendEmail(EmailRecord er) {
		String[] tos = new String[]{er.getEmailTo()};
		String[] ccs = null;
		if(!StringHelper.isNullOrEmpty(er.getEmailCc())) {
			ccs = new String[]{er.getEmailCc()};
		}
		
		String[] bccs = new String[]{}; 
		String content=er.getContent();
		String subject=er.getTheme();
		String fromPersonal="TCL家用电器（合肥）有限公司[tcljd@tclha.com]"; //客户要求写这个
        File[] f = null;
        if(!StringHelper.isNullOrEmpty(er.getAttachPath())) {
        	File att = new File(er.getAttachPath());
        	f = new File[]{att};
        }
        else {
        	f =new File[0];
        }
		try {
			/**
			邮件地址配置在edi项目的override.properties中，由于是用edi来发邮件。
			里面配置如下；
			mailService.host=smtp.163.com
			mailService.from=xiaxuyan@163.com
			mailService.userName=xiaxuyan@163.com
			mailService.password=*****  这个是授权码  以前是密码 163等改用授权码了。
			*/
//			mailService.setHost("smtp.163.com");
//			mailService.setUserName("xiaxuyan@163.com");
//			mailService.setPassword("****"); //邮件客户端登录的授权码 
//			mailService.setFrom("xiaxuyan@163.com");

			mailService.sendMail(tos, ccs, bccs, content, subject, fromPersonal, f);
 
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new BusinessException("邮件发送失败");
		}
		
		
	}
	
	public void updateEmailRecordToSucess(Long emailRecordId) {
		updateEmailRecord(emailRecordId,InterfaceLogStatus.STAT_FINISH,"");
	}
	
	public void updateEmailRecordToFail(Long emailRecordId,String errorInfo) {
		updateEmailRecord(emailRecordId,InterfaceLogStatus.STAT_FAIL,errorInfo);
	}

	private void updateEmailRecord(Long emailRecordId,String status,String errorInfo) {
		EmailRecord er = commonDao.load(EmailRecord.class, emailRecordId);
		if(er==null) {
			return ;
		}
		
		er.setSendCount(StringHelper.replaceNullToZero(er.getSendCount())+1);
		er.setLastSendTime(new Date());
		er.setStatus(status);
		er.setErrorInfo(StringHelper.substring(errorInfo, 255));
		commonDao.store(er);
	}
	
}
