package com.vtradex.wms.server.model.entity.base;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogFunction;
import com.vtradex.wms.webservice.model.InterfaceLogSys;

/***
 * wms传给sap的报文信息
 * @author administrator
 *
 */
public class Wms2SapInterfaceLog extends Entity{

	private static final long serialVersionUID = -7766713970815714460L;
	/**
	 * {@link Wms2SapInterfaceLogType}
	 */
	private String type;
	
	private String function=InterfaceLogFunction.ASYNC;
	
	private String fromSYS = InterfaceLogSys.WMS_SYS;
	
	private String toSYS=InterfaceLogSys.SAP_SYS;
	
	/**插入时间*/
	private Date insertTime = new Date();
	/**
	 * WMS交货单单号
	 */
	private String request;
	/**WMS发送时间*/
	private Date requestTime;
	/**clob 发送报文*/
	private String requestContent;
	
	/**发送次数*/
	private Integer requestCnt;
	
	/**WMS发送状态*/
	private String sendStatus;
	
	/**SAP应答状态*/
	private String receiveStatus;
	/**SAP应答时间*/
	private Date receiveTime;
	
	/**SAP处理状态*/
	private String dealStatus;
	private String response;
	/**SAP处理返回时间*/
	private Date responseTime;
	/**SAP处理结果报文*/
	private String responseContent;
	
	/**单据对象ID*/
	private Long docId;
	private String errorLog;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getFromSYS() {
		return fromSYS;
	}
	public void setFromSYS(String fromSYS) {
		this.fromSYS = fromSYS;
	}
	public String getToSYS() {
		return toSYS;
	}
	public void setToSYS(String toSYS) {
		this.toSYS = toSYS;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	public String getRequestContent() {
		return requestContent;
	}
	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}
	public Integer getRequestCnt() {
		return requestCnt;
	}
	public void setRequestCnt(Integer requestCnt) {
		this.requestCnt = requestCnt;
	}
	public String getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
	public String getReceiveStatus() {
		return receiveStatus;
	}
	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}
	public Date getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public Date getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}
	public String getResponseContent() {
		return responseContent;
	}
	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}
	public Long getDocId() {
		return docId;
	}
	public void setDocId(Long docId) {
		this.docId = docId;
	}
	public String getErrorLog() {
		return errorLog;
	}
	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}
}
