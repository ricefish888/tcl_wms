package com.vtradex.wms.server.model.entity.pickticket;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.component.Contact;
import com.vtradex.wms.server.model.entity.warehouse.WmsDock;
import com.vtradex.wms.server.model.entity.warehouse.WmsLocation;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.entity.base.WmsCarrier;
import com.vtradex.wms.server.model.entity.base.WmsCustomer;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.model.entity.item.WmsBillType;
import com.vtradex.wms.server.model.entity.item.WmsCompany;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.enums.WmsPickTicketStatus;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.webservice.utils.CommonHelper;

/**
 * 拣货单
 * 
 * @ClassName: WmsPickTicket
 * 
 * @author YangLiu
 *
 * @date 2015年12月10日 上午11:49:45 
 */
public class WmsPickTicket extends VersionalEntity {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 所属仓库
	 */
	private WmsWarehouse warehouse; 
	
	/**
	 * 所属货主
	 */
	private WmsCompany company; 
	
	/** 拣货单号  */
	@UniqueKey
	private String code;
	
	/**
	 * 波次单
	 */
	private WmsWaveDoc waveDoc; 
	
	/**
	 * 单据类型
	 */
	private WmsBillType billType; 
	
	/** 是否原始单据 */
	private Boolean isOriginal = Boolean.TRUE;
	
	/** 是否可执行 */
	private Boolean isExecutable = Boolean.TRUE;
	
	/**原始单据id*/
	private Long originalId;
	
	/** 是否锁单 */
	private Boolean isHold = Boolean.FALSE;
	
	/** 
	 * 状态
	 *  
	 * {@link WmsPickTicketStatus} 
	 */
	private String status;
	
	/** 总数量  */
	private Double qty = 0D;
	
	/** 已分配数量  */
	private Double allocateQty = 0D;
	
	/** 拣货数量 */
	private Double pickQty = 0D;
	
	/** 发货数量  */
	private Double shipQty = 0D;
	
	/** 相关单号1 */
	private String relatedBill1;
	
	/** 相关单号2 */
	private String relatedBill2;
	
	/** 相关单号3 */
	private String relatedBill3;
	
	/** 收货人 */
	private WmsCustomer customer;
	
	/**收货人名称 */
	private String shipToName;
	
	/** 联系方式*/
	private Contact contact;
	
	/** 订单日期 */
	private Date orderDate;
	
	/** 优先级 */
	private Integer priority = 0;
	
	/** 预计发货时间 */
	private Date intendShipDate;
	
	/** 预计备货完成时间 */
	private Date expectedPickFinishDate;
	
	/** 发货时间 */
	private Date finshDate;
	
	/** 描述 */
	private String description;
	
	/**  发货月台 */
	private WmsDock shipDock;
	
	/**  备货库位  */
	private WmsLocation shipLocation;
	
	/**  承运商  */
	private WmsCarrier carrier;
	
	/** 预计到达时间 */
	private Date requireArriveDate;
	
	/** 是否允许短缺发运 */
	private Boolean allowShortShip=Boolean.FALSE;
	
	/** 自定义字段1 出库工厂编码*/
	private String userField1;
	
	/** 自定义字段2 */
	private String userField2;
	
	/** 自定义字段3   单据来源*/
	private String userField3;
	
	/** 自定义字段4 */
	private String userField4;
	
	/** 自定义字段5  备注*/
	private String userField5;
	
	/** 自定义字段6 产线描述*/
	private String userField6;
	
	/** 自定义字段7 */
	private String userField7;
	
	/** 自定义字段8 */
	private String userField8;
	
	/** 自定义字段9 */
	private String userField9;
	
	/** 自定义字段10 */
	private String userField10;
	
	/** 自定义字段11 */
	private String userField11;
	
	/** 自定义字段12 */
	private String userField12;
	
	/** 自定义字段13 */
	private String userField13;
	
	/** 自定义字段14 */
	private String userField14;
	
	/** 自定义字段15 */
	private String userField15;
	
	/** 自定义字段16 */
	private String userField16;
	
	/** 自定义字段17 */
	private String userField17;
	
	/** 自定义字段18 */
	private String userField18;
	
	/** 自定义字段19 */
	private String userField19;
	
	/** 自定义字段20 */
	private String userField20;
	
	/** 自定义字段21 */
	private String userField21;
	
	/** 自定义字段22 */
	private String userField22;
	
	/** 自定义字段23 */
	private String userField23;
	
	/** 自定义字段24 */
	private String userField24;
	
	/** 拣货明细清单 */
	private Set<WmsPickTicketDetail> details = new HashSet<WmsPickTicketDetail>();
	/**是否越库*/
	private Boolean allowCross = Boolean.FALSE;
	
	/**
	 * 路线
	 */
	private String route;
	
	/** 是否允许批次属性置换 */
	private Boolean allowLotSwap = Boolean.FALSE;
	
	/** 等待补货 */
	private Boolean waitReplenish = Boolean.FALSE;
	
	
	/**供应商 退供应商出库需要用到*/
	private WmsSupplier supplier;
	
	
	public WmsSupplier getSupplier() {
		return supplier;
	}

	public void setSupplier(WmsSupplier supplier) {
		this.supplier = supplier;
	}

	public Boolean getIsOriginal() {
		return isOriginal;
	}

	public void setIsOriginal(Boolean isOriginal) {
		this.isOriginal = isOriginal;
	}

	public Boolean getIsExecutable() {
		return isExecutable;
	}

	public void setIsExecutable(Boolean isExecutable) {
		this.isExecutable = isExecutable;
	}

	public Long getOriginalId() {
		return originalId;
	}

	public void setOriginalId(Long originalId) {
		this.originalId = originalId;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public Boolean getAllowCross() {
		return allowCross;
	}

	public void setAllowCross(Boolean allowCross) {
		this.allowCross = allowCross;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof WmsPickTicket)) {
			return false;
		}
		WmsPickTicket castOther = (WmsPickTicket) other;
        return new EqualsBuilder().append(code, castOther.getCode()).isEquals();
	}
	
	@Override
	public int hashCode() {
        return new HashCodeBuilder().append(code).toHashCode();
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public WmsWaveDoc getWaveDoc() {
		return waveDoc;
	}

	public void setWaveDoc(WmsWaveDoc waveDoc) {
		this.waveDoc = waveDoc;
	}

	public WmsBillType getBillType() {
		return billType;
	}

	public void setBillType(WmsBillType billType) {
		this.billType = billType;
	}

	public Boolean getIsHold() {
		return isHold;
	}

	public void setIsHold(Boolean isHold) {
		this.isHold = isHold;
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
		this.qty = CommonHelper.dealDoubleError(qty);
	}

	public Double getAllocateQty() {
		return allocateQty;
	}

	public void setAllocateQty(Double allocateQty) {
		this.allocateQty = allocateQty;
	}

	public Double getPickQty() {
		return pickQty;
	}

	public void setPickQty(Double pickQty) {
		this.pickQty = pickQty;
	}

	public Double getShipQty() {
		return shipQty;
	}

	public void setShipQty(Double shipQty) {
		this.shipQty = shipQty;
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

	public WmsCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(WmsCustomer customer) {
		this.customer = customer;
	}

	public String getShipToName() {
		return shipToName;
	}

	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Date getIntendShipDate() {
		return intendShipDate;
	}

	public void setIntendShipDate(Date intendShipDate) {
		this.intendShipDate = intendShipDate;
	}

	public Date getExpectedPickFinishDate() {
		return expectedPickFinishDate;
	}

	public void setExpectedPickFinishDate(Date expectedPickFinishDate) {
		this.expectedPickFinishDate = expectedPickFinishDate;
	}

	public Date getFinshDate() {
		return finshDate;
	}

	public void setFinshDate(Date finshDate) {
		this.finshDate = finshDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public WmsDock getShipDock() {
		return shipDock;
	}

	public void setShipDock(WmsDock shipDock) {
		this.shipDock = shipDock;
	}

	public WmsLocation getShipLocation() {
		return shipLocation;
	}

	public void setShipLocation(WmsLocation shipLocation) {
		this.shipLocation = shipLocation;
	}

	public WmsCarrier getCarrier() {
		return carrier;
	}

	public void setCarrier(WmsCarrier carrier) {
		this.carrier = carrier;
	}

	public Date getRequireArriveDate() {
		return requireArriveDate;
	}

	public void setRequireArriveDate(Date requireArriveDate) {
		this.requireArriveDate = requireArriveDate;
	}

	public Boolean getAllowShortShip() {
		return allowShortShip;
	}

	public void setAllowShortShip(Boolean allowShortShip) {
		this.allowShortShip = allowShortShip;
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

	public String getUserField11() {
		return userField11;
	}

	public void setUserField11(String userField11) {
		this.userField11 = userField11;
	}

	public String getUserField12() {
		return userField12;
	}

	public void setUserField12(String userField12) {
		this.userField12 = userField12;
	}

	public String getUserField13() {
		return userField13;
	}

	public void setUserField13(String userField13) {
		this.userField13 = userField13;
	}

	public String getUserField14() {
		return userField14;
	}

	public void setUserField14(String userField14) {
		this.userField14 = userField14;
	}

	public String getUserField15() {
		return userField15;
	}

	public void setUserField15(String userField15) {
		this.userField15 = userField15;
	}

	public String getUserField16() {
		return userField16;
	}

	public void setUserField16(String userField16) {
		this.userField16 = userField16;
	}

	public String getUserField17() {
		return userField17;
	}

	public void setUserField17(String userField17) {
		this.userField17 = userField17;
	}

	public String getUserField18() {
		return userField18;
	}

	public void setUserField18(String userField18) {
		this.userField18 = userField18;
	}

	public String getUserField19() {
		return userField19;
	}

	public void setUserField19(String userField19) {
		this.userField19 = userField19;
	}

	public String getUserField20() {
		return userField20;
	}

	public void setUserField20(String userField20) {
		this.userField20 = userField20;
	}

	public Set<WmsPickTicketDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<WmsPickTicketDetail> details) {
		this.details = details;
	}
	
	
	public String getUserField21() {
		return userField21;
	}

	public void setUserField21(String userField21) {
		this.userField21 = userField21;
	}

	public String getUserField22() {
		return userField22;
	}

	public void setUserField22(String userField22) {
		this.userField22 = userField22;
	}

	public String getUserField23() {
		return userField23;
	}

	public void setUserField23(String userField23) {
		this.userField23 = userField23;
	}

	public String getUserField24() {
		return userField24;
	}

	public void setUserField24(String userField24) {
		this.userField24 = userField24;
	}

	public Boolean getAllowLotSwap() {
		return allowLotSwap;
	}

	public void setAllowLotSwap(Boolean allowLotSwap) {
		this.allowLotSwap = allowLotSwap;
	}

	public Boolean getWaitReplenish() {
		return waitReplenish;
	}

	public void setWaitReplenish(Boolean waitReplenish) {
		this.waitReplenish = waitReplenish;
	}

	/**
	 * 发货单新增明细
	 * 
	 * @param pickTicketDetail
	 */
	public void addPickTicketDetail(WmsPickTicketDetail detail) {
		detail.setPickTicket(this);

		this.details.add(detail);

		this.refreshPickTicketQty();
	}

	/**
	 * 发货单删除明细
	 * 
	 * @param detail
	 */
	public void removeDetails(WmsPickTicketDetail detail) {
		detail.setPickTicket(null);

		this.details.remove(detail);

		this.refreshPickTicketQty();
	}

	/**
	 * 刷新发货单期待数量
	 */
	public void refreshPickTicketQty() {
		this.qty = 0.0D;
		this.shipQty = 0.0D;
		
		for (WmsPickTicketDetail detail : this.details) {
			this.qty += detail.getExpectedQty();
			this.shipQty += detail.getShippedQty();
		}
		this.qty=CommonHelper.dealDoubleError(qty);
		this.shipQty = CommonHelper.dealDoubleError(shipQty);
	}

	/**
	 * 刷新发货单分配数量
	 * @param allocateQuantity
	 */
	public void refreshAlloatedQtyBU() {
		this.allocateQty = 0.0D;
		
		for (WmsPickTicketDetail detail : this.details) {
			this.allocateQty =DoubleUtils.add(this.allocateQty, detail.getAllocatedQty());
		}
		this.allocateQty=CommonHelper.dealDoubleError(this.allocateQty);
	}

	/**
	 * 刷新发货单发运数量
	 */
	public void refreshShippedQty() {
		this.shipQty = 0.0D;
		
		for (WmsPickTicketDetail detail : this.details) {
			this.shipQty = DoubleUtils.add(this.shipQty,detail.getShippedQty());
		}
		this.shipQty=CommonHelper.dealDoubleError(this.shipQty);
	}

	/**
	 * 增加分配数量BU
	 * @param quantityBU
	 */
	public void allocate(Double quantityBU,int scale){
		this.allocateQty = DoubleUtils.add(this.allocateQty, quantityBU,scale);
		/*if(this.allocateQty == 0){
			this.status = WmsPickTicketStatus.OPEN;
		}else if(this.qty.doubleValue() == this.allocateQty.doubleValue()){
			this.status = WmsPickTicketStatus.ALLOCATED;
		}else{
			this.status = WmsPickTicketStatus.PARTALLOCATED;
		}*/
		if(this.waveDoc != null){
			waveDoc.allocate(quantityBU,scale);
		}
		this.allocateQty=CommonHelper.dealDoubleError(this.allocateQty);
	}
	
	/**
	 * 增加拣货数量BU
	 * @param quantityBU
	 */
	public void pickedQty(Double quantityBU,int scale){
		this.pickQty = DoubleUtils.add(this.pickQty, quantityBU,scale);
		/*if(this.pickQty.doubleValue() == this.qty.doubleValue()){
			this.status = WmsPickTicketStatus.PICK_FINISHED;
		}else{
			this.status = WmsPickTicketStatus.WORKING;
		}*/
		this.pickQty=CommonHelper.dealDoubleError(this.pickQty);
	}
	
	/**
	 * 增加发运数量BU
	 * @param quantityBU
	 */
	public void shipQty(Double quantityBU,int scale){
		this.shipQty = DoubleUtils.add(this.shipQty, quantityBU,scale);
		/*if(this.shipQty.doubleValue() == this.qty.doubleValue()){
			this.status = WmsPickTicketStatus.FINISHED;
		}else{
			this.status = WmsPickTicketStatus.PART_SHIP;
		}*/
		this.shipQty=CommonHelper.dealDoubleError(this.shipQty);
	}
	
	/**
	 * 取消分配数量BU
	 * @param quantityBU
	 */
	public void unallocate(Double quantityBU){
		this.allocateQty = DoubleUtils.sub(this.allocateQty,quantityBU);
		this.allocateQty=CommonHelper.dealDoubleError(this.allocateQty);
	}
	
	/**
	 * 获取未分配数量
	 * @return
	 */
	public Double getUnAllocateQuantityBU() {
		Double q= DoubleUtils.sub(this.qty,this.allocateQty);
		q=CommonHelper.dealDoubleError(q);
		return q;
	}
	
	/**
	 * @param qty2
	 */
	public void removeQty(Double qty){
		this.qty = DoubleUtils.sub(this.qty,qty);
		this.qty=CommonHelper.dealDoubleError(this.qty);
	}
	
	/**
	 * 状态定义
	 * @Description:
	 * @Author:        <a href="yequan.song@vtradex.net">宋叶全</a>
	 * @CreateDate:    2016年1月8日
	 * @return:
	 * @arithMetic:
	 * @exception:
	 */
	public String defineStatus(){
		
		this.allocateQty=CommonHelper.dealDoubleError(this.allocateQty);
		this.pickQty=CommonHelper.dealDoubleError(this.pickQty);
		this.qty=CommonHelper.dealDoubleError(this.qty);
		this.shipQty=CommonHelper.dealDoubleError(this.shipQty);
		
		String status=this.status;
		if(this.allocateQty<=0d) {
			status = WmsPickTicketStatus.OPEN;
		}
		else {
			status = WmsPickTicketStatus.ALLOCATED;
			for (WmsPickTicketDetail ptd : this.getDetails()) {
				if(DoubleUtils.sub(ptd.getExpectedQty() , ptd.getAllocatedQty()) > 0) {
					status = WmsPickTicketStatus.PARTALLOCATED;
				}
			}
		}
		
		if(status.equals(WmsPickTicketStatus.ALLOCATED)){//已分配
			
			if(this.pickQty>0) {//说明拣货了
				status = WmsPickTicketStatus.PICK_FINISHED; //默认拣货完成
				for (WmsPickTicketDetail ptd : this.getDetails()) {
					if(DoubleUtils.sub(ptd.getAllocatedQty() , ptd.getPickedQty()) > 0) { //没有捡完
						status = WmsPickTicketStatus.WORKING;
					}
				}
			}
			
			if(status.equals( WmsPickTicketStatus.PICK_FINISHED)) { //拣货完成
				if(this.shipQty>0) {//说明发货了
					status = WmsPickTicketStatus.FINISHED; //默认发运完成
					for (WmsPickTicketDetail ptd : this.getDetails()) {
						if(DoubleUtils.sub(ptd.getPickedQty(), ptd.getShippedQty()) > 0) { //没有发完
							status = WmsPickTicketStatus.PART_SHIP;
						}
					}
				}
			}
			 
			
			
//			
//			if(this.pickQty.doubleValue() >= this.qty.doubleValue()){
//				if(this.shipQty.doubleValue() >= this.qty.doubleValue()){
//					//如果拣货数量=计划数量&&发运数量=计划数量时,状态为完成
//					status = WmsPickTicketStatus.FINISHED;
//				}else{
//					//如果拣货数量=计划数量&&发运数量!=计划数量时,状态为拣货完成
//					status = WmsPickTicketStatus.PICK_FINISHED;
//				}
//			}else if(this.pickQty.doubleValue()>0&&this.pickQty.doubleValue()< this.qty.doubleValue()){
//				//如果分配数量=计划数量时&&拣货数量>0
//				status = WmsPickTicketStatus.WORKING;	
//			}else{
//				//如果分配数量=计划数量时,状态为已分配
//				status = WmsPickTicketStatus.ALLOCATED;
//			}
		}else if(status.equals(WmsPickTicketStatus.PARTALLOCATED)){
			if(this.qty.doubleValue()>this.allocateQty.doubleValue()&&
					this.allocateQty.doubleValue()==this.pickQty.doubleValue()&&this.shipQty.doubleValue()>0D){
				//如果分配数量<计划数量并且分配数量==拣货数量并且发运数量>0时,状态为部分发运
				status = WmsPickTicketStatus.PART_SHIP;
			}else if(this.allocateQty.doubleValue() >= this.qty.doubleValue() 
					&& this.allocateQty.doubleValue()==this.pickQty.doubleValue() 
					&& this.shipQty.doubleValue()>0D){//不可拆包物料的分配数量可能大于计划数量
				status = WmsPickTicketStatus.PART_SHIP;
			}else{
				//如果分配数量!=计划数量时,状态为部分分配
				status = WmsPickTicketStatus.PARTALLOCATED;
			}
		}
		
		
//		下面是老逻辑,不适用不可拆包物料
//		if(this.allocateQty == 0){
//			//如果分配数量=0&&拣货数量>0时,状态为部分分配(超拣情况下进行退拣)
//			if(this.allocateQty == 0 && this.pickQty.doubleValue()>0){
//				status = WmsPickTicketStatus.PARTALLOCATED;
//			}else{
//			    status = WmsPickTicketStatus.OPEN;
//			}
//		}else if(this.qty.doubleValue() <= this.allocateQty.doubleValue()){
//			
//			if(this.pickQty.doubleValue() >= this.qty.doubleValue()){
//				if(this.shipQty.doubleValue() == this.qty.doubleValue()){
//					//如果拣货数量=计划数量&&发运数量=计划数量时,状态为完成
//					status = WmsPickTicketStatus.FINISHED;
//				}else{
//					//如果拣货数量=计划数量&&发运数量!=计划数量时,状态为拣货完成
//					status = WmsPickTicketStatus.PICK_FINISHED;
//				}
//			}else if(this.pickQty.doubleValue()>0&&this.pickQty.doubleValue()< this.qty.doubleValue()){
//				//如果分配数量=计划数量时&&拣货数量>0
//				status = WmsPickTicketStatus.WORKING;	
//			}else{
//				//如果分配数量=计划数量时,状态为已分配
//				status = WmsPickTicketStatus.ALLOCATED;
//			}
//		}else if(this.qty.doubleValue()>this.allocateQty.doubleValue()&&
//				this.allocateQty.doubleValue()==this.pickQty.doubleValue()&&this.shipQty.intValue()>0){
//			//如果分配数量<计划数量并且分配数量==拣货数量并且发运数量>0时,状态为部分发运
//			status = WmsPickTicketStatus.PART_SHIP;
//		}else{
//			//如果分配数量!=计划数量时,状态为部分分配
//			status = WmsPickTicketStatus.PARTALLOCATED;
//		}
//		
		return status;
	}
	
	/**
	 * 刷新单头的所有数量
	 */
	public void refreshAllQty() {
		this.allocateQty = 0.0D;
		this.qty = 0.0D;
		this.pickQty = 0.0D;
		
		for (WmsPickTicketDetail detail : this.details) {
			this.allocateQty =DoubleUtils.add(this.allocateQty, detail.getAllocatedQty());
			this.qty =DoubleUtils.add(this.qty, detail.getExpectedQty());
			this.pickQty =DoubleUtils.add(this.pickQty, detail.getPickedQty());
		}
		this.allocateQty=CommonHelper.dealDoubleError(this.allocateQty);
		this.pickQty=CommonHelper.dealDoubleError(this.pickQty);
		this.qty=CommonHelper.dealDoubleError(this.qty);
	}
}
