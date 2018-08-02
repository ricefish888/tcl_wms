package com.vtradex.wms.server.model.entity.order;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.entity.base.WmsSapFactory;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetail;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;

/***
 * 
 * 生产订单齐套性
 * @author administrator
 *
 */
public class WmsProductionOrderMeetInfo extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6810630283829237623L;

	
	/**工单 / 预留单 /其它
	 * {@link WmsProductionOrderMeetInfoType}}
	 *  */
	private String type;
	
	private Long detailId;
	
	/**库存是否齐套*/
	private Boolean beMeet;
	
	/**齐套性检查日期*/
	private Date checkDate=new Date();

	/**单号*/
	private String orderCode;

	/**工厂*/
	private WmsSapFactory factory;
	
	/**开始日期*/
	private Date beginDate;
	
	/**结束开始日期*/
	private Date endDate;
	
	private WmsItem item;
	
	private WmsPackageUnit packageUnit;
	
	/**计划数*/
	private Double planQty;
	
	//仓库
	private WmsWarehouse warehouse;
	//是否最新生成的数据
	private Boolean isNewFlag;
	
	/**
	 * 预留单号  
	 */
	private String reservedOrder;
	/**
	 * 预留行项目号 
	 */
	private String reservedProject;
	
	/**产线*/
	private String productLine;
	
	//已发运数量
	private Double shipQty;

	//库存满足量
	private Double invQty ;
	//缺料数量
	private Double shortQty;
	
	/**状态   @link ProductionOrderMeetInfoStatus*/
	private String status;
	
	/**
	 * 产线描述
	 */
	private String lineDesc;
	
	/**需求日期*/
	private Date xqrq;
	
	
	
	public Date getXqrq() {
		return xqrq;
	}

	public void setXqrq(Date xqrq) {
		this.xqrq = xqrq;
	}

	public String getLineDesc() {
		return lineDesc;
	}

	public void setLineDesc(String lineDesc) {
		this.lineDesc = lineDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReservedOrder() {
		return reservedOrder;
	}

	public void setReservedOrder(String reservedOrder) {
		this.reservedOrder = reservedOrder;
	}

	public String getReservedProject() {
		return reservedProject;
	}

	public void setReservedProject(String reservedProject) {
		this.reservedProject = reservedProject;
	}

	public String getProductLine() {
		return productLine;
	}

	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}

	public Double getShipQty() {
		return shipQty;
	}

	public void setShipQty(Double shipQty) {
		this.shipQty = shipQty;
	}

	public Double getInvQty() {
		return invQty;
	}

	public void setInvQty(Double invQty) {
		this.invQty = invQty;
	}

	public Double getShortQty() {
		return shortQty;
	}

	public void setShortQty(Double shortQty) {
		this.shortQty = shortQty;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Boolean getIsNewFlag() {
		return isNewFlag;
	}

	public void setIsNewFlag(Boolean isNewFlag) {
		this.isNewFlag = isNewFlag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public Boolean getBeMeet() {
		return beMeet;
	}

	public void setBeMeet(Boolean beMeet) {
		this.beMeet = beMeet;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public WmsSapFactory getFactory() {
		return factory;
	}

	public void setFactory(WmsSapFactory factory) {
		this.factory = factory;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public WmsPackageUnit getPackageUnit() {
		return packageUnit;
	}

	public void setPackageUnit(WmsPackageUnit packageUnit) {
		this.packageUnit = packageUnit;
	}

	public Double getPlanQty() {
		return planQty;
	}

	public void setPlanQty(Double planQty) {
		this.planQty = planQty;
	}

	
	public WmsProductionOrderMeetInfo(){}
	
	public WmsProductionOrderMeetInfo(String type, Long detailId,
			Boolean beMeet, Date checkDate, String orderCode,
			WmsSapFactory factory, Date beginDate, Date endDate, WmsItem item,
			WmsPackageUnit packageUnit, Double planQty) {
		super();
		this.type = type;
		this.detailId = detailId;
		this.beMeet = beMeet;
		this.checkDate = checkDate;
		this.orderCode = orderCode;
		this.factory = factory;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.item = item;
		this.packageUnit = packageUnit;
		this.planQty = planQty;
	}
	
	
}
