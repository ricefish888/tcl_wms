package com.vtradex.wms.server.model.entity.order;

import java.util.Date;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.entity.base.WmsSapFactory;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.utils.DoubleUtils;

public class PurchaseOrderDetail extends Entity{
	
	private static final long serialVersionUID = 1L;

	private PurchaseOrder purchaseOrder;
	
	@UniqueKey
	private Integer lineNo;
	
	/** 货品*/
	private WmsItem item;
	/** 期待数量*/
	private Double expectedQty = 0.0D;
	
	/**分配数量  交货单生成的时候 会增加采购订单明细的分配数量*/
	private Double allotQty = 0.0D;
	
	/** 实际收货数量*/
	private Double receivedQty = 0.0D;
	
	/** 包装单位*/
	private WmsPackageUnit packageUnit;
	/** 期待包装数量*/
	private Double expectedPackQty = 0.0D;
	
	/** 
	 * 库存状态
	 *  */
	private String inventoryStatus;
	
	/**交货日期 */
	private Date receivedDate;
	
	/**交货点*/
	private String receivedLoc;
	
	/**工厂*/
	private WmsSapFactory factory;
	
	/**项目*/
	private String ebelp;
	
	/**
	 * @link{WmsFactoryXmlb}
	 * 项目类别 0为标准2为寄售*/
	private String pstyp;
	
	/** 自定义字段1 
	 库存地点	 标准Y004，寄售V001
	 * */
	private String userField1;
	/** 自定义字段2 
	 * */
	private String userField2;
	/** 自定义字段3 */
	private String userField3;
	/** 自定义字段4 */
	private String userField4;
	/** 自定义字段5 */
	private String userField5;
	
	/**
	 * 状态
	 * {@link PurchaseOrderDetailStatus}}
	 */
	private String status=PurchaseOrderDetailStatus.UNFINISH;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}
	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	public Integer getLineNo() {
		return lineNo;
	}
	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}
	public WmsItem getItem() {
		return item;
	}
	public void setItem(WmsItem item) {
		this.item = item;
	}
	public Double getExpectedQty() {
		return expectedQty;
	}
	public void setExpectedQty(Double expectedQty) {
		this.expectedQty = expectedQty;
	}
	public Double getReceivedQty() {
		return receivedQty;
	}
	public void setReceivedQty(Double receivedQty) {
		this.receivedQty = receivedQty;
	}
	public WmsPackageUnit getPackageUnit() {
		return packageUnit;
	}
	public void setPackageUnit(WmsPackageUnit packageUnit) {
		this.packageUnit = packageUnit;
	}
	public Double getExpectedPackQty() {
		return expectedPackQty;
	}
	public void setExpectedPackQty(Double expectedPackQty) {
		this.expectedPackQty = expectedPackQty;
	}
	public String getInventoryStatus() {
		return inventoryStatus;
	}
	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	
	public WmsSapFactory getFactory() {
		return factory;
	}
	public void setFactory(WmsSapFactory factory) {
		this.factory = factory;
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
	public String getReceivedLoc() {
		return receivedLoc;
	}
	public void setReceivedLoc(String receivedLoc) {
		this.receivedLoc = receivedLoc;
	}
	public String getEbelp() {
		return ebelp;
	}
	public void setEbelp(String ebelp) {
		this.ebelp = ebelp;
	}
	public String getPstyp() {
		return pstyp;
	}
	public void setPstyp(String pstyp) {
		this.pstyp = pstyp;
	}
	public Double getAllotQty() {
		return allotQty;
	}
	public void setAllotQty(Double allotQty) {
		this.allotQty = allotQty;
	}
	
	public void addAllotQty(Double allotQty){
	    this.allotQty = DoubleUtils.add(this.allotQty, allotQty);
	}
	
	public void subAllotQty(Double allotQty) {
	    this.allotQty = DoubleUtils.sub(this.allotQty, allotQty);
	}
	
	public void addReceivedQty(Double receivedQty) {
	    this.receivedQty = DoubleUtils.add(this.receivedQty, receivedQty);
	}
	
	public void subReceivedQty(Double receivedQty) {
        this.receivedQty = DoubleUtils.sub(this.receivedQty, receivedQty);
    }
}
