package com.vtradex.wms.server.model.entity.production;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.entity.base.WmsCustomer;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.model.entity.item.WmsBillType;
import com.vtradex.wms.server.model.entity.order.ConfirmStatus;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;

/***
 * 交货单
 * @author administrator
 *
 */
public class WmsDeliveryOrder extends VersionalEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9221374060249470037L;

	private WmsWarehouse warehouse;

	/**    */
	private String code;
	/**  sap交货单号  */
	private String sapCode;
	
	/**
	 * {@link WmsDeliveryOrderStatus}
	 */
	private String status;
	
	/** 供应商*/
	private WmsSupplier supplier;
	
	/**交货日期*/
	private Date deliveryDate;
	
	private String type = "EL";//交货类型 默认取EL  SAP用
	
	/***
	 * 创建方式 
	 * {@link WmsDeliveryOrderCreatedType}
	 */
	private String createdType;
	/**
	 * 项目
	 */
	private String project;

	/**当前是一张交货单一条明细**/
	private Set<WmsDeliveryOrderDetail> details = new HashSet<WmsDeliveryOrderDetail>();

	/**
     * 确认人
     */
    private String confirmor;
    /**
     * 确认时间
     */
    private Date confirmTime;
    /**
     * 确认状态 {@link ConfirmStatus}
     */
    private String confirmStatus = ConfirmStatus.OPEN;
    /**
     * 接收人
     */
    private String receiver;
    /**
     * 接收时间
     */
    private Date receiveTime;
    
    /**
     * 单据类型
     * {@link WmsDeliveryOrderBillType}}
     */
    private String billTypeName;
    
    /**
     * 客户
     * @return
     */
    private WmsCustomer customer;
    
	public WmsCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(WmsCustomer customer) {
		this.customer = customer;
	}

	public String getConfirmor() {
        return confirmor;
    }

    public void setConfirmor(String confirmor) {
        this.confirmor = confirmor;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSapCode() {
		return sapCode;
	}

	public void setSapCode(String sapCode) {
		this.sapCode = sapCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public WmsSupplier getSupplier() {
		return supplier;
	}

	public void setSupplier(WmsSupplier supplier) {
		this.supplier = supplier;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreatedType() {
		return createdType;
	}

	public void setCreatedType(String createdType) {
		this.createdType = createdType;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public Set<WmsDeliveryOrderDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<WmsDeliveryOrderDetail> details) {
		this.details = details;
	}

	public void addDetail(WmsDeliveryOrderDetail detail) {
	    this.details.add(detail);
	}
    
    public void removeDetail(WmsDeliveryOrderDetail detail) {
        this.details.remove(detail);
        detail.setDeliveryOrder(null);
    }

	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
    
}
