package com.vtradex.wms.server.model.entity.inventory;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.wms.server.model.component.LotInfo;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.utils.StringHelper;

/** 
* @ClassName: 库存日结对象
*/
public class WmsStorageDaily extends Entity{

	private static final long serialVersionUID = -8936539222400622513L;
	
//	/** 仓库*/  //不能加仓库，因为vmi的料收货是在VMI仓库 如果生产领料交接到自管仓 则从自管仓出库，造成日结分仓库就不对。
//	@UniqueKey
//	private WmsWarehouse warehouse;
	/** 结转日期*/
	@UniqueKey
	private Date computeDate;
	/**物料*/
	@UniqueKey
	private WmsItem item;//物料
	/**批次属性hashcode*/
	@UniqueKey
	private String lotInfoHashCode;
	/**批次属性*/
	private LotInfo lotInfo;
	/**库存状态*/
	@UniqueKey
	private String inventoryStatus;
	
	/**WMS期初数量 */
	private Double startCount = 0d;
	/**wms入库数量*/
	private Double inCount = 0d;
	/**wms出库数量*/
	private Double outCount = 0d;
	
	/**WMS期末数量*/
	private Double endCount = 0d;
	
	/**非限制数量  即合格品数量*/
	private Double unlimCount = 0d;
	/**质检库存 即待检库存数量*/
	private Double checkInventory = 0d;
	


	public void setEndCount(Double endCount) {
		this.endCount = endCount;
		if(StringHelper.isNullOrEmpty(inventoryStatus)) {
			throw new BusinessException("库存状态不能为空");
		}
		if("待检".equals(inventoryStatus)) {
			this.checkInventory=this.endCount;
			this.unlimCount=0d;
		}
		else {
			this.unlimCount = this.endCount;
			this.checkInventory=0d;
		}
		
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof WmsStorageDaily))
			return false;
		WmsStorageDaily castOther = (WmsStorageDaily) other;
		return new EqualsBuilder().//append(warehouse, castOther.warehouse).
				append(computeDate,castOther.computeDate).
				append(item, castOther.item).
				append(lotInfoHashCode, castOther.lotInfoHashCode).
				append(inventoryStatus, castOther.inventoryStatus).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().//append(warehouse).
				append(computeDate).
				append(item).
				append(lotInfoHashCode).
				append(inventoryStatus).toHashCode();
	}
	
	public Double getEndCount() {
		return endCount;
	}

//	public WmsWarehouse getWarehouse() {
//		return warehouse;
//	}
//
//	public void setWarehouse(WmsWarehouse warehouse) {
//		this.warehouse = warehouse;
//	}

	public Date getComputeDate() {
		return computeDate;
	}

	public void setComputeDate(Date computeDate) {
		this.computeDate = computeDate;
	}

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public String getLotInfoHashCode() {
		return lotInfoHashCode;
	}

	public void setLotInfoHashCode(String lotInfoHashCode) {
		this.lotInfoHashCode = lotInfoHashCode;
	}

	public LotInfo getLotInfo() {
		return lotInfo;
	}

	public void setLotInfo(LotInfo lotInfo) {
		this.lotInfo = lotInfo;
		this.lotInfoHashCode=BeanUtils.getFormat(this.lotInfo.stringValue());
	}

	public String getInventoryStatus() {
		return inventoryStatus;
	}

	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}

	public Double getStartCount() {
		return startCount;
	}

	public void setStartCount(Double startCount) {
		this.startCount = startCount;
	}

	public Double getInCount() {
		return inCount;
	}

	public void setInCount(Double inCount) {
		this.inCount = inCount;
	}

	public Double getOutCount() {
		return outCount;
	}

	public void setOutCount(Double outCount) {
		this.outCount = outCount;
	}


	public Double getUnlimCount() {
		return unlimCount;
	}

	public void setUnlimCount(Double unlimCount) {
		this.unlimCount = unlimCount;
	}

	public Double getCheckInventory() {
		return checkInventory;
	}

	public void setCheckInventory(Double checkInventory) {
		this.checkInventory = checkInventory;
	}
 
}
