package com.vtradex.wms.server.model.entity.base;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;

/**
 * 预警管理
 * */
public class WmsAccountCloseDay extends Entity{
	
	private static final long serialVersionUID = -1722070415768772459L;
	/**账单关闭时间*/
	private Date accountCloseTime;
	/**账单关闭时间*/
	private Date maxAccountCloseTime;
	
	public Date getMaxAccountCloseTime() {
		return maxAccountCloseTime;
	}
	public void setMaxAccountCloseTime(Date maxAccountCloseTime) {
		this.maxAccountCloseTime = maxAccountCloseTime;
	}
	public Date getAccountCloseTime() {
		return accountCloseTime;
	}
	public void setAccountCloseTime(Date accountCloseTime) {
		this.accountCloseTime = accountCloseTime;
	}
	
	
	 
	
	
}
