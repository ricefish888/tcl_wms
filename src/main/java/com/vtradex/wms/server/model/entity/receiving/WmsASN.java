package com.vtradex.wms.server.model.entity.receiving;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.wms.server.model.component.Contact;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.model.entity.item.WmsCompany;
import com.vtradex.wms.server.model.entity.item.WmsBillType;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.entity.warehouse.WmsDock;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.utils.DoubleUtils;

/**
 * 收货单
 * 
 * @author <a href="mailto:ming.chen@tech.vtradex.com">陈明</a>
 * @since Dec 10, 2015
 */
public class WmsASN extends VersionalEntity {	
	/** 仓库 */
	@UniqueKey
	private WmsWarehouse warehouse;
	/** 货主 */
	@UniqueKey
	private WmsCompany company;
	/** 单据类型 */
	@UniqueKey
	private WmsBillType billType;
	/** 收货单据号 */
	@UniqueKey
	private String code;
	/** 收货月台 */
	private WmsDock receiveDock;
	/** 收货库位 */
	private WmsLocation receiveLocation;
	/** 
	 * 状态
	 * {@link WmsASNStatus}
	 *  */
	private String status;
	/** 总数量 */
	private Double qty = 0.0D;
	/** 收货数量 */
	private Double receiveQty = 0.0D;
	/** 质检数量 */
	private Double quantityQty = 0.0D;
	/**上架数量*/
	private Double putawayQty=0.0D;
	/** 客户单号 */
	private String customerBill;
	/** 相关单号1 */
	private String relatedBill1;
	/** 相关单号2 */
	private String relatedBill2;
	/** 相关单号3 */
	private String relatedBill3;
	/** 订单日期 */
	private Date orderDate;
	/** 预计到货时间 */
	private Date estimateDate;
	/**
	 * 实际到货时间
	 * add:2017-02-23
	 *     wms5.0表结构说明文档V2.0.7
	 */
	private Date actualDate;
	/**开始收货时间*/
	private Date startReceivedDate;
	/**结束收货时间*/
	private Date endReceivedDate;
	/** 发货人代码 */
	private String fromCode;
	/** 发货人 */
	private String fromName;
	/** 联系方式 */
	private Contact fromContact;
	/** 自定义字段1 */
	private String userField1;
	/** 自定义字段2 */
	private String userField2;
	/** 自定义字段3 */
	private String userField3;
	/** 自定义字段4 */
	private String userField4;
	/** 自定义字段5 */
	private String userField5;
	/** 自定义字段6 */
	private String userField6;
	/** 自定义字段7    工厂编码*/
	private String userField7;
	/** 自定义字段8 jit属性*/
	private String userField8;
	/** 自定义字段9 */
	private String userField9;
	
	/**
	 *  自定义字段10
	 * add :2017-04-12
	 *      wms5.0表结构说明文档V2.2.0
	 */
	private String userField10;
	
	/** 收货计划 */
	private WmsPo po;
	
	/** ASN明细清单 */
	private Set<WmsASNDetail> details = new HashSet<WmsASNDetail>();
	/** ASN收货日志 */
	private Set<WmsReceivedRecord> records = new HashSet<WmsReceivedRecord>();
	
	/** 收货库位 */
	private WmsLocation qcLocation;
	
	/** 供应商*/
	private WmsSupplier supplier;
	
	/** 保管员*/
	private ThornUser keeper;
	
	/**是否打印送货单*/
	private Boolean isPrint=Boolean.FALSE;

	public Boolean getIsPrint() {
		return isPrint;
	}
	public void setIsPrint(Boolean isPrint) {
		this.isPrint = isPrint;
	}
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
	public WmsBillType getBillType() {
		return billType;
	}
	public void setBillType(WmsBillType billType) {
		this.billType = billType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public WmsDock getReceiveDock() {
		return receiveDock;
	}
	public void setReceiveDock(WmsDock receiveDock) {
		this.receiveDock = receiveDock;
	}
	public WmsLocation getReceiveLocation() {
		return receiveLocation;
	}
	public void setReceiveLocation(WmsLocation receiveLocation) {
		this.receiveLocation = receiveLocation;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Double getQuantityQty() {
		return quantityQty;
	}
	public void setQuantityQty(Double quantityQty) {
		this.quantityQty = quantityQty;
	}
	public Double getPutawayQty() {
		return putawayQty;
	}
	public void setPutawayQty(Double putawayQty) {
		this.putawayQty = putawayQty;
	}
	public String getCustomerBill() {
		return customerBill;
	}
	public void setCustomerBill(String customerBill) {
		this.customerBill = customerBill;
	}
	public String getRelatedBill1() {
		return relatedBill1;
	}
	public void setRelatedBill1(String relatedBill1) {
		this.relatedBill1 = relatedBill1;
	}
	public String getRelatedBill2() {
		return relatedBill2;
	}
	public void setRelatedBill2(String relatedBill2) {
		this.relatedBill2 = relatedBill2;
	}
	public String getRelatedBill3() {
		return relatedBill3;
	}
	public void setRelatedBill3(String relatedBill3) {
		this.relatedBill3 = relatedBill3;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public Date getEstimateDate() {
		return estimateDate;
	}
	public void setEstimateDate(Date estimateDate) {
		this.estimateDate = estimateDate;
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
	public Set<WmsASNDetail> getDetails() {
		return details;
	}
	public void setDetails(Set<WmsASNDetail> details) {
		this.details = details;
	}
	public Set<WmsReceivedRecord> getRecords() {
		return records;
	}
	public void setRecords(Set<WmsReceivedRecord> records) {
		this.records = records;
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
	
	
	public ThornUser getKeeper() {
		return keeper;
	}
	public void setKeeper(ThornUser keeper) {
		this.keeper = keeper;
	}
	/**
	 * 收货
	 * @param quantity
	 */
	public void receive(double qty,int scale) {
		if (this.receiveQty <= 0)  {
			this.setStartReceivedDate(new Date());
		}
		this.receiveQty = DoubleUtils.add(this.receiveQty,qty,scale);
	}
	
	/**
	 * 取消收货
	 * @param quantity
	 */
	public void cancelReceive(double quantity,int scale) {
		this.receiveQty = DoubleUtils.sub(this.receiveQty, quantity, scale);
		this.setEndReceivedDate(null);
		if (receiveQty <= 0) {
			this.setStartReceivedDate(null);
		}
	}
	
	
	
	/**
	 * 取消收货,删除质检数量
	 * @param quantity
	 */
	public void cancelQuantityQty(double quantity,int scale) {
		this.quantityQty = DoubleUtils.sub(this.quantityQty, quantity, scale);
	}
	
	
	/**
	 * 新增ASN明细
	 * @param detail
	 */
	public void addDetail(WmsASNDetail detail) {
		this.details.add(detail);
	}
	
	/**
	 * 刷新ASN订单数量
	 */
	public void refreshQtyBU() {
		this.qty = 0.0D;
		
		for (WmsASNDetail detail : this.details) {
			this.qty=DoubleUtils.add(this.qty,detail.getExpectedQty(),detail.getItem().getBuPrecision());
//			this.qty += detail.getExpectedQty();
		}
	}
	/** 上架 */
	public void addMovedQty(Double movedQty,int scale) {
		this.putawayQty = DoubleUtils.add(this.putawayQty,movedQty,scale);
//		this.putawayQty += movedQty;
	}
	/**
	 * 移除明细
	 * @param details
	 */
	public void removeDetail(WmsASNDetail detail) {
		this.details.remove(detail);
		detail.setAsn(null);
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
	public String getUserField6() {
		return userField6;
	}
	public void setUserField6(String userField6) {
		this.userField6 = userField6;
	}
	public String getUserField7() {
		return userField7;
	}
	public void setUserField7(String userField7) {
		this.userField7 = userField7;
	}
	
	public String getUserField8() {
		return userField8;
	}
	public void setUserField8(String userField8) {
		this.userField8 = userField8;
	}
	public WmsPo getPo() {
		return po;
	}
	public void setPo(WmsPo po) {
		this.po = po;
	}
	public String getUserField9() {
		return userField9;
	}
	public void setUserField9(String userField9) {
		this.userField9 = userField9;
	}
	
	public String getUserField10() {
		return userField10;
	}
	public void setUserField10(String userField10) {
		this.userField10 = userField10;
	}
	public WmsLocation getQcLocation() {
		return qcLocation;
	}
	public void setQcLocation(WmsLocation qcLocation) {
		this.qcLocation = qcLocation;
	}
	
	public WmsSupplier getSupplier() {
		return supplier;
	}
	public void setSupplier(WmsSupplier supplier) {
		this.supplier = supplier;
	}
	/**
	 * 取消上架数量
	 * @param quantity
	 */
	public void cancelMoveQty(double movedQtyTemp,int scale) {
		this.putawayQty = DoubleUtils.sub(this.putawayQty, movedQtyTemp, scale);
	}
}