package com.vtradex.wms.server.service.emailrecord;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.wms.server.model.entity.email.EmailRecord;

/***
 * 邮件管理manager
 * */
public interface EmailRecordManager {
	
	/**重新发送*/
	@Transactional
	void resend(EmailRecord er);
	
	/**创建emailrecord 等待发送*/
	@Transactional
	void storeEmailRecordWaitSend(String receiver,String to,String subject,String content,String cc,String emailtype,String realteCode);
	
	@Transactional
	void sendEmail(Long emailRecordId);
	
	@Transactional
	void sendEmail(EmailRecord er);
	
	@Transactional
	void updateEmailRecordToSucess(Long emailRecordId);
	
	@Transactional
	void updateEmailRecordToFail(Long emailRecordId,String errorInfo);


}
