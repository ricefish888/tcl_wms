package com.vtradex.wms.server.model.entity.base;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;

/**
 * 预警管理
 * */
public class WmsWarning extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;
	
	/**
	 * 预警类型
	 * {@link WmsWarningType}
	 * */
	private String type;
	
	/**
	 * 状态
	 * {@link WmsWarningStatus}
	 * 
	 * */
	private String status;

	/**生成日期*/
	private Date insertDate = new Date();
	
	/**生成时间*/
	private Date insertTime = new Date();
	
	/***预警信息*/
	private String warningInfo;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public String getWarningInfo() {
		return warningInfo;
	}

	public void setWarningInfo(String warningInfo) {
		this.warningInfo = warningInfo;
	}
}
