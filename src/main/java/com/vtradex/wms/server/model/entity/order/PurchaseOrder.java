package com.vtradex.wms.server.model.entity.order;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.component.Contact;
import com.vtradex.wms.server.model.entity.base.WmsSapFactory;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.model.entity.item.WmsCompany;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.utils.DoubleUtils;

public class PurchaseOrder extends VersionalEntity{

	private static final long serialVersionUID = 2053804973253305255L;

	/**  所属仓库*/
	private WmsWarehouse warehouse; 
	
	/** 货主 */
	@UniqueKey
	private WmsCompany company;
	
	/**供应商*/
	private WmsSupplier supplier;
	
	/**采购订单号*/
	private String code;
	/**采购订单类型*/
	private String bsart;
	/**订单创建日期*/
	private Date  creatDate;
	
	/**采购组织*/
	private String ekorg;
	
	/**采购组*/
	private String ekgrp;
	
	/**工厂*/
	private WmsSapFactory sapFactory;
	/** 总数量 */
	private Double qty = 0.0D;
	/** 收货数量 */
	private Double receiveQty = 0.0D;
	//分配数量
	private Double allotQty = 0.0D;
	/** 实际到货时间*/
	private Date actualDate;
	/**开始收货时间*/
	private Date startReceivedDate;
	/**结束收货时间*/
	private Date endReceivedDate;
	
	/**
	 * @link{PurchaseOrderStatus}
	 */
	private String status;
	
	/** 发货人代码 */
	private String fromCode;
	/** 发货人 */
	private String fromName;
	/** 联系方式 */
	private Contact fromContact;
	/** 自定义字段1 * */
	private String userField1;
	/** 自定义字段2  * */
	private String userField2;
	/** 自定义字段3 
	 * 删除标识
	 * */
	private String userField3;
	/** 自定义字段4 
	 *退货标识 
	 * */
	private String userField4;
	/** 自定义字段5 
	 * */
	private String userField5;
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

	private Set<PurchaseOrderDetail> details = new HashSet<PurchaseOrderDetail>();

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public WmsCompany getCompany() {
		return company;
	}

	public void setCompany(WmsCompany company) {
		this.company = company;
	}

	public WmsSupplier getSupplier() {
		return supplier;
	}

	public void setSupplier(WmsSupplier supplier) {
		this.supplier = supplier;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBsart() {
		return bsart;
	}

	public void setBsart(String bsart) {
		this.bsart = bsart;
	}

	public Date getCreatDate() {
		return creatDate;
	}

	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}

	public String getEkorg() {
		return ekorg;
	}

	public void setEkorg(String ekorg) {
		this.ekorg = ekorg;
	}

	public String getEkgrp() {
		return ekgrp;
	}

	public void setEkgrp(String ekgrp) {
		this.ekgrp = ekgrp;
	}
	

	public WmsSapFactory getSapFactory() {
		return sapFactory;
	}

	public void setSapFactory(WmsSapFactory sapFactory) {
		this.sapFactory = sapFactory;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public Double getReceiveQty() {
		return receiveQty;
	}

	public void setReceiveQty(Double receiveQty) {
		this.receiveQty = receiveQty;
	}

	public Date getActualDate() {
		return actualDate;
	}

	public void setActualDate(Date actualDate) {
		this.actualDate = actualDate;
	}

	public Date getStartReceivedDate() {
		return startReceivedDate;
	}

	public void setStartReceivedDate(Date startReceivedDate) {
		this.startReceivedDate = startReceivedDate;
	}

	public Date getEndReceivedDate() {
		return endReceivedDate;
	}

	public void setEndReceivedDate(Date endReceivedDate) {
		this.endReceivedDate = endReceivedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFromCode() {
		return fromCode;
	}

	public void setFromCode(String fromCode) {
		this.fromCode = fromCode;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public Contact getFromContact() {
		return fromContact;
	}

	public void setFromContact(Contact fromContact) {
		this.fromContact = fromContact;
	}

	public String getUserField1() {
		return userField1;
	}

	public void setUserField1(String userField1) {
		this.userField1 = userField1;
	}

	public String getUserField2() {
		return userField2;
	}

	public void setUserField2(String userField2) {
		this.userField2 = userField2;
	}

	public String getUserField3() {
		return userField3;
	}

	public void setUserField3(String userField3) {
		this.userField3 = userField3;
	}

	public String getUserField4() {
		return userField4;
	}

	public void setUserField4(String userField4) {
		this.userField4 = userField4;
	}

	public String getUserField5() {
		return userField5;
	}

	public void setUserField5(String userField5) {
		this.userField5 = userField5;
	}

	public Set<PurchaseOrderDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<PurchaseOrderDetail> details) {
		this.details = details;
	}

	public void addDetail(PurchaseOrderDetail purchaseOrderDetail){
		this.details.add(purchaseOrderDetail);
	}
	
	public Double getAllotQty() {
		return allotQty;
	}

	public void setAllotQty(Double allotQty) {
		this.allotQty = allotQty;
	}

	public void refreshQtyBU() {
		this.qty = 0.0D;
		for (PurchaseOrderDetail detail : this.details) {
			this.qty=DoubleUtils.add(this.qty,detail.getExpectedQty(),detail.getItem().getBuPrecision());
		}
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

    public void refreshAllotQty() {
		this.allotQty = 0.0D;
		for (PurchaseOrderDetail detail : this.details) {
			this.allotQty=DoubleUtils.add(this.allotQty,detail.getAllotQty(),detail.getItem().getBuPrecision());
		}
	}
	
    public void refreshReceiveQty() {
        this.receiveQty = 0.0D;
        for (PurchaseOrderDetail detail : this.details) {
            this.receiveQty = DoubleUtils.add(this.receiveQty, detail.getReceivedQty(), detail.getItem().getBuPrecision());
        }
    }
	
	/**
	 * 移除明细
	 * @param details
	 */
	public void removeDetail(PurchaseOrderDetail purchaseOrderDetail) {
		this.details.remove(purchaseOrderDetail);
		purchaseOrderDetail.setPurchaseOrder(null);
		refreshAllotQty();
		refreshQtyBU();
		refreshReceiveQty();
	}
}
