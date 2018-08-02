package com.vtradex.wms.server.model.entity.base;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;

/***
 * 成本中心  SAP传输过来
 * @author administrator
 *
 */
public class WmsCostCenter extends Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4604962815630024414L;

	/**编码*/
	private String code;
	
	/**名称*/
	private String name;
	
	/**有效期*/
	private Date xxpirationDate;
	
	/**冻结标识  X-为冻结*/
	private String freezeFlag;

	public String getFreezeFlag() {
		return freezeFlag;
	}

	public void setFreezeFlag(String freezeFlag) {
		this.freezeFlag = freezeFlag;
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

	public Date getXxpirationDate() {
		return xxpirationDate;
	}

	public void setXxpirationDate(Date xxpirationDate) {
		this.xxpirationDate = xxpirationDate;
	}

}
