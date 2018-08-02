package com.vtradex.wms.server.model.entity.inventory;

import org.apache.cxf.BusException;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.entity.item.WmsCompany;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemKey;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;
import com.vtradex.wms.server.model.enums.WmsBillOfType;
import com.vtradex.wms.server.model.enums.WmsInventoryOperationStatus;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.WmsPackageUnitUtils;
import com.vtradex.wms.webservice.utils.CommonHelper;

/**
 * 库存
 * 
 * @author <a href="mailto:brofe@163.com">潘宁波</a>
 * @since Dec 10, 2015\
 * 
 * 重写底层  removeQty方法增加<0的校验
 */
public class WmsInventory extends VersionalEntity {

	private static final long serialVersionUID = 1L;

	/** 仓库 */
	private WmsWarehouse warehouse;

	/** 货主 */
	private WmsCompany company;

	/** 库位 */
	private WmsLocation location;

	/** 货品 */
	private WmsItem item;

	/** 批次属性 */
	private WmsItemKey itemKey;

	/** 包装 */
	private WmsPackageUnit packageUnit;

	/** 关联任务 */
	private WmsTask task;

	/** 数量 */
	private Double qty = 0D;

	/** 包装数量 */
	private Double packQty = 0D;

	/** 托盘号 */
	private String pallet;

	/** 周转箱号 */
	private String carton;

	/** 库存状态 */
	private String status;
	/**
	 * 锁定状态
	 */
    private Boolean lockStatus = Boolean.FALSE;
	/**
	 * 作业状态
	 * 
	 * {@link WmsInventoryOperationStatus}
	 */
	private String operationStatus;

	/**
	 * ASN/PT 枚举值，判断相关单据类型是收货还是拣货 {@link WmsBillOfType}
	 */
	private String relatedBillType;
	/**
	 * 相关单据号
	 */
	private String relatedBillCode;

	/**
	 * 托盘序号
	 */
	private Integer palletSeq=0;
    
	public Boolean getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Boolean lockStatus) {
		this.lockStatus = lockStatus;
	}

	public Integer getPalletSeq() {
		return palletSeq;
	}

	public void setPalletSeq(Integer palletSeq) {
		this.palletSeq = palletSeq;
	}

	public WmsInventory() {
	}

	public WmsInventory(WmsWarehouse warehouse, WmsCompany company, WmsLocation location, WmsItem item, WmsItemKey itemKey, WmsPackageUnit packageUnit, Double packQty,
			String status, String operationStatus, String relatedBillType, String relatedBillCode) {
		super();
		this.warehouse = warehouse;
		this.company = company;
		this.location = location;
		this.item = item;
		this.itemKey = itemKey;
		this.packageUnit = packageUnit;
		this.packQty = packQty;
		this.status = status;
		this.operationStatus = operationStatus;
		this.relatedBillType = relatedBillType;
		this.relatedBillCode = relatedBillCode;
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

	public WmsLocation getLocation() {
		return location;
	}

	public void setLocation(WmsLocation location) {
		this.location = location;
	}

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public WmsItemKey getItemKey() {
		return itemKey;
	}

	public void setItemKey(WmsItemKey itemKey) {
		this.itemKey = itemKey;
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
		if(CommonHelper.dealDoubleError(this.qty) <0d) {
			throw new BusinessException("5库存ID"+this.getId()+"被扣成了负数"+CommonHelper.dealDoubleError(this.qty));
		}
	}

	public WmsPackageUnit getPackageUnit() {
		return packageUnit;
	}

	public void setPackageUnit(WmsPackageUnit packageUnit) {
		this.packageUnit = packageUnit;
	}

	public Double getPackQty() {
		return packQty;
	}

	public void setPackQty(Double packQty) {
		this.packQty = packQty;
	}

	public String getPallet() {
		return pallet;
	}

	public void setPallet(String pallet) {
		this.pallet = pallet;
	}

	public String getCarton() {
		return carton;
	}

	public void setCarton(String carton) {
		this.carton = carton;
	}

	public String getOperationStatus() {
		return operationStatus;
	}

	public void setOperationStatus(String operationStatus) {
		this.operationStatus = operationStatus;
	}

	public WmsTask getTask() {
		return task;
	}

	public void setTask(WmsTask task) {
		this.task = task;
	}

	public String getRelatedBillType() {
		return relatedBillType;
	}

	public void setRelatedBillType(String relatedBillType) {
		this.relatedBillType = relatedBillType;
	}

	public String getRelatedBillCode() {
		return relatedBillCode;
	}

	public void setRelatedBillCode(String relatedBillCode) {
		this.relatedBillCode = relatedBillCode;
	}

	/**
	 * 增加包装数量
	 */
	public void addPackQty(Double value) {
		this.packQty = DoubleUtils.add(this.packQty,value,this.item.getBuPrecision());
		this.qty = DoubleUtils.add(this.qty, WmsPackageUnitUtils.getQtyBU(packageUnit, value, this.item.getBuPrecision()));
		if(CommonHelper.dealDoubleError(this.qty) <0d) {
			throw new BusinessException("4库存ID"+this.getId()+"被扣成了负数"+CommonHelper.dealDoubleError(this.qty));
		}
//		this.packQty += value;
//		this.qty += WmsPackageUnitUtils.getQtyBU(packageUnit, value, item.getBuPrecision());
	}

	/**
	 * 减少包装数量
	 */
	public void subPackQty(Double value) {
		this.packQty = DoubleUtils.sub(this.packQty, value,this.item.getBuPrecision());
		this.qty = DoubleUtils.sub(this.qty, WmsPackageUnitUtils.getQtyBU(packageUnit, value, item.getBuPrecision()));
		if(CommonHelper.dealDoubleError(this.qty) <0d) {
			throw new BusinessException("3库存ID"+this.getId()+"被扣成了负数"+CommonHelper.dealDoubleError(this.qty));
		}
//		this.packQty -= value;
//		this.qty -= WmsPackageUnitUtils.getQtyBU(packageUnit, value, item.getBuPrecision());
	}
	

	/**
	 * 增加库存
	 * @param quantity
	 */
	public void addQty(Double value){
		this.qty = DoubleUtils.add(this.qty, value, this.item.getBuPrecision());
		if(CommonHelper.dealDoubleError(this.qty) <0d) {
			throw new BusinessException("2库存ID"+this.getId()+"被扣成了负数"+CommonHelper.dealDoubleError(this.qty));
		}
//		this.qty += value;
		refreshPackageQuantity();
	}

	/**
	 * 减少库存
	 * @param quantity
	 */
	public void removeQty(Double value) {
		this.qty = DoubleUtils.sub(this.qty, value,item.getBuPrecision());
//		this.qty -= value;
		
		if(CommonHelper.dealDoubleError(this.qty) <0d) {
			throw new BusinessException("库存ID"+this.getId()+"被扣成了负数"+CommonHelper.dealDoubleError(this.qty));
		}
		refreshPackageQuantity();
	}

	/**
	 * 刷新包装数
	 */
	private void refreshPackageQuantity() {
		this.packQty = DoubleUtils.div(this.qty, this.packageUnit.getConvertFigure(), this.item.getBuPrecision());
//		this.packQty = Math.ceil(qty / packageUnit.getConvertFigure());
	}
	
}
