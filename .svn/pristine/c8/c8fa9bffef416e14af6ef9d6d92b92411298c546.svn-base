package com.vtradex.wms.server.model.entity.email;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogStatus;

public class EmailRecord extends Entity{

	private static final long serialVersionUID = 1346148565468137736L;
	/**收件用户*/
	private String receiver;
	/**邮箱*/
	private String emailTo;
	/**主题*/
	private String theme;
	/**内容*/
	private String content;
	
	/**附件名称*/
	private String attachName;
	
	/**附件路径  全路径*/
	private String attachPath;
	
	/**插入时间*/
	private Date beginTime;
	/**最后一次发送时间*/
	private Date lastSendTime;
	/**发送次数*/
	private Integer sendCount=0;
	/**发送状态
	 * 
	 * {@link InterfaceLogStatus}
	 * 
	 * */
	private String status;
	
	
	/**抄送地址*/
	private String emailCc;
	
	/**邮件类型
	 * 
	 * {@link EmailRecordType}
	 * */
	private String emailType;
	
	/**相关单号  如果是 交货单  采购单 对账单  需要记录单号，邮件发送城后需要传接口*/
	private String realateCode;
	
	/**异常内容*/
	private String errorInfo;
	
	public EmailRecord() {
	}
	public EmailRecord(String receiver,String to,String subject,String content,String cc,String emailType) {
		this(receiver, to, subject, content, cc, emailType,null);
				
	}
	public EmailRecord(String receiver,String to,String subject,String content,String cc,String emailType,String relateCode) {
		this.setReceiver(receiver);
		this.setEmailTo(to);
		this.setTheme(subject);
		this.setContent(content);
		this.setBeginTime(new Date());
		this.setEmailCc(cc);
		this.setEmailType(emailType);
		this.setStatus(InterfaceLogStatus.STAT_READY);
		this.setRealateCode(relateCode);
	}
	
	
	
	public String getRealateCode() {
		return realateCode;
	}

	public void setRealateCode(String realateCode) {
		this.realateCode = realateCode;
	}

	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	 
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAttachPath() {
		return attachPath;
	}
	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getLastSendTime() {
		return lastSendTime;
	}
	public void setLastSendTime(Date lastSendTime) {
		this.lastSendTime = lastSendTime;
	}
	public Integer getSendCount() {
		return sendCount;
	}
	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmailType() {
		return emailType;
	}
	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}
	public String getEmailTo() {
		return emailTo;
	}
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}
	public String getAttachName() {
		return attachName;
	}
	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}
	public String getEmailCc() {
		return emailCc;
	}
	public void setEmailCc(String emailCc) {
		this.emailCc = emailCc;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
	

}
