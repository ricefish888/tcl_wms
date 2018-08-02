package com.vtradex.wms.server.model.entity.base;

import com.vtradex.thorn.server.model.Entity;

/**
 * SAP仓库
 * 
 * @author <a href="mailto:ming.chen@tech.vtradex.com">陈明</a>
 * @since Dec 10, 2015
 */
public class WmsSapWarehouse extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5350907082562219428L;
	/** 工厂*/
	private WmsSapFactory sapFactory;
	 
	/** 仓库编码*/
	private String code;
	/** 仓库名称*/
	private String name;
	/** 状态
	 * {@link WmsSapWarehouseStatus}
	 * */
	private String status = WmsSapWarehouseStatus.ENABLED;
	
	

	public WmsSapFactory getSapFactory() {
		return sapFactory;
	}
	public void setSapFactory(WmsSapFactory sapFactory) {
		this.sapFactory = sapFactory;
	}
	 
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	}
