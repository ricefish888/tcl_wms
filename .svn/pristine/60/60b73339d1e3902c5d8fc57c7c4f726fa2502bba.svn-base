package com.vtradex.wms.server.model.entity.warehouse;
import java.util.Date;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.enums.BaseStatus;
import com.vtradex.wms.server.model.enums.WmsLocationStatus;

public class WmsLocation extends Entity {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 仓库
	 */
	@UniqueKey
	private WmsWarehouse warehouse;
	/**
	 * 库区
	 */
	private WmsZone zone;
	/**
	 * 工作区
	 */
	private WmsWorkArea workArea;
	
	/** 库位代码*/
	@UniqueKey
	private String code;
	
	/** 
	 * 状态
	 * 
	 * {@link BaseStatus}
	 */
	private String status = BaseStatus.ENABLED;
	/** 
	 * 库位类型
	 *@WmsLocationType
	 */
	private String type;
	/**
	 * 操作特性
	 * N: 正常
	 * M: 多进深库位
	 * @WmsLocationExeType
	 */
	
	private String exeType;
	/**
	 * 拣选库位分类
	 */
	private String allocationCategory;
	/**
	 * 上架库位分类
	 */
	private String putawayCategory;
	/**
	 * 承载类型
	 */
	private WmsLocationCapacity locationCapacity;
	/**
	 *巷道  人走的过道，或者立库堆垛机
		1：左边
		2： 右边
	 *modify:2017-02-23
	 *       wms5.0表结构说明文档V2.0.7
	 *int修改成String
	 *
	 */
	private  String aisle;
	/** 排*/
	private Integer lineNo = 0;
	/** 列*/
	private Integer columnNo = 0;
	/** 层*/
	private Integer layerNo = 0;
	
	/**
	 * 上架动线号
	 */
	private Integer putawaySequence = 0;
	/**
	 * 拣货动线号
	 */
	private Integer pickingSequence = 0;
	
	 /** 空拣存状态*/
    private String locationStatus= WmsLocationStatus.EMPTY;;
    
    /**
     * 库位盘点锁
     */
    private Boolean countLock = Boolean.FALSE;
    
    /**
     * 盘点时间
     */
    private Date cycleDate;
    
    /**
     * 异常标志
     */
    private Boolean exceptionFlag = Boolean.FALSE;
    
    /**
     * 库满度
     */
    private Double useRate = 0D;
    
    /**
	 * 动碰次数
	 */
	private Integer touchTimes = 0;
    
    
    /**
     * 货品混放策略
     * @WmsMixRuleType
     */
    private String mixRule;
    
    /**
     * 出入库锁
     * @WmsInOutLockType
     */
    private String inOutLock;

    /**
	 * 盘点动线号
	 */
	private Integer checkingSequence = 0;
	
	/**
	 * 描述
	 */
	private String description;
	
	
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	public Integer getTouchTimes() {
		return touchTimes;
	}

	public void setTouchTimes(Integer touchTimes) {
		this.touchTimes = touchTimes;
	}

	public Integer getCheckingSequence() {
		return checkingSequence;
	}

	public void setCheckingSequence(Integer checkingSequence) {
		this.checkingSequence = checkingSequence;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public WmsZone getZone() {
		return zone;
	}

	public void setZone(WmsZone zone) {
		this.zone = zone;
	}

	public WmsWorkArea getWorkArea() {
		return workArea;
	}

	public void setWorkArea(WmsWorkArea workArea) {
		this.workArea = workArea;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExeType() {
		return exeType;
	}

	public void setExeType(String exeType) {
		this.exeType = exeType;
	}

	public String getAllocationCategory() {
		return allocationCategory;
	}

	public void setAllocationCategory(String allocationCategory) {
		this.allocationCategory = allocationCategory;
	}

	public String getPutawayCategory() {
		return putawayCategory;
	}

	public void setPutawayCategory(String putawayCategory) {
		this.putawayCategory = putawayCategory;
	}

	public String getAisle() {
		return aisle;
	}

	public void setAisle(String aisle) {
		this.aisle = aisle;
	}

	public Integer getLineNo() {
		return lineNo;
	}

	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}

	public Integer getColumnNo() {
		return columnNo;
	}

	public void setColumnNo(Integer columnNo) {
		this.columnNo = columnNo;
	}

	public Integer getLayerNo() {
		return layerNo;
	}

	public void setLayerNo(Integer layerNo) {
		this.layerNo = layerNo;
	}

	public Integer getPutawaySequence() {
		return putawaySequence;
	}

	public void setPutawaySequence(Integer putawaySequence) {
		this.putawaySequence = putawaySequence;
	}

	public Integer getPickingSequence() {
		return pickingSequence;
	}

	public void setPickingSequence(Integer pickingSequence) {
		this.pickingSequence = pickingSequence;
	}

	public String getLocationStatus() {
		return locationStatus;
	}

	public void setLocationStatus(String locationStatus) {
		this.locationStatus = locationStatus;
	}

	public Boolean getCountLock() {
		return countLock;
	}

	public void setCountLock(Boolean countLock) {
		this.countLock = countLock;
	}

	public Date getCycleDate() {
		return cycleDate;
	}

	public void setCycleDate(Date cycleDate) {
		this.cycleDate = cycleDate;
	}

	public Boolean getExceptionFlag() {
		return exceptionFlag;
	}

	public void setExceptionFlag(Boolean exceptionFlag) {
		this.exceptionFlag = exceptionFlag;
	}

	public Double getUseRate() {
		return useRate;
	}

	public void setUseRate(Double useRate) {
		this.useRate = useRate;
	}

	public WmsLocationCapacity getLocationCapacity() {
		return locationCapacity;
	}

	public void setLocationCapacity(WmsLocationCapacity locationCapacity) {
		this.locationCapacity = locationCapacity;
	}

	public String getMixRule() {
		return mixRule;
	}

	public void setMixRule(String mixRule) {
		this.mixRule = mixRule;
	}

	public String getInOutLock() {
		return inOutLock;
	}

	public void setInOutLock(String inOutLock) {
		this.inOutLock = inOutLock;
	}
    
	/**
	 * 盘点锁
	 * */
	public void countLockLocations(){
		if(!this.countLock){
			this.countLock = Boolean.TRUE;
		}else{
			throw new BusinessException("location.is.counted");
		}
	}
	/**
	 * 盘点解锁
	 * */
	public void countUnLockLocations(){
		if(this.countLock){
			this.countLock = Boolean.FALSE;
		}
		this.exceptionFlag = Boolean.FALSE;
	}
    
}
