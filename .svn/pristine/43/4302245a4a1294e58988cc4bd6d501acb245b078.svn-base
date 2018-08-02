package com.vtradex.wms.server.model.entity.order;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;

/***
 * 配货单  由供应商新建
 * @author administrator
 *
 */
public class WmsTransportOrder extends VersionalEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7187483327298287113L;

	/**配货单号 由规则生成*/
	private String code;
	
	/**供应商*/
	private WmsSupplier supplier;
	
	/**配货日期*/
	private Date orderDate;
	
	/***
	 * 状态
	 * {@link WmsTransportOrderStatus}
	 */
	private String status;
	
	private Set<WmsTransportOrderDetail> details = new HashSet<WmsTransportOrderDetail>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public WmsSupplier getSupplier() {
		return supplier;
	}

	public void setSupplier(WmsSupplier supplier) {
		this.supplier = supplier;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<WmsTransportOrderDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<WmsTransportOrderDetail> details) {
		this.details = details;
	}

}
