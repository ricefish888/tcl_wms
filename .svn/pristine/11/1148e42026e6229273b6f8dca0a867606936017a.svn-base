package com.vtradex.wms.server.model.entity.bol;
import java.util.Date;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.entity.item.WmsItemKey;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.workdoc.WmsWorkDoc;
import com.vtradex.wms.server.utils.DoubleUtils;

/**
 * 发货单明细
 * 
 * @author <a href="mailto:ming.chen@tech.vtradex.com">陈明</a>
 * @since Dec 10, 2015
 */
public class WmsBolDetail extends VersionalEntity {
	
	/**
	 * @Description: serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	/** 发货单*/
	private WmsBol bol;
	/** 拣货单明细*/
	private WmsPickTicketDetail pickTicketDetail;
	/** 计划数量*/
	private double planQty = 0D;
	/** 已拣数量*/
	private double pickedQty = 0D;
	/**自定义日期字段1 */
	private Date userFieldT1;
	/**包装数量*/
	private double packedQty = 0D;
	/**批次属性*/
	private WmsItemKey itemKey;
	/**是否需要包装*/
	private Boolean isPacking = Boolean.FALSE;
	
	/**拣货作业单*/
	private WmsWorkDoc workDoc;
	
	/**
	 * 自定义字段1-2
	 * add :2017-04-20
	 *      wms5.0表结构说明文档V2.2.4
	 */
	private String userField1;
	
	private String userField2;
	
	
	
	public WmsWorkDoc getWorkDoc() {
		return workDoc;
	}
	public void setWorkDoc(WmsWorkDoc workDoc) {
		this.workDoc = workDoc;
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
	public WmsItemKey getItemKey() {
		return itemKey;
	}
	public void setItemKey(WmsItemKey itemKey) {
		this.itemKey = itemKey;
	}
	public WmsBol getBol() {
		return bol;
	}
	public void setBol(WmsBol bol) {
		this.bol = bol;
	}
	public WmsPickTicketDetail getPickTicketDetail() {
		return pickTicketDetail;
	}
	public void setPickTicketDetail(WmsPickTicketDetail pickTicketDetail) {
		this.pickTicketDetail = pickTicketDetail;
	}
	public double getPlanQty() {
		return planQty;
	}
	public void setPlanQty(double planQty) {
		this.planQty = planQty;
	}
	public double getPickedQty() {
		return pickedQty;
	}
	public void setPickedQty(double pickedQty) {
		this.pickedQty = pickedQty;
	}
	
	public Date getUserFieldT1() {
		return userFieldT1;
	}
	public void setUserFieldT1(Date userFieldT1) {
		this.userFieldT1 = userFieldT1;
	}
	
	/**
	 * 发运数量
	 * @Description:
	 */
	public void pickedQty(Double quantityBU){
		this.pickedQty = DoubleUtils.add(this.pickedQty, quantityBU,pickTicketDetail.getItem().getBuPrecision());
	}
	public double getPackedQty() {
		return packedQty;
	}
	public void setPackedQty(double packedQty) {
		this.packedQty = packedQty;
	}
	public Boolean getIsPacking() {
		return isPacking;
	}
	public void setIsPacking(Boolean isPacking) {
		this.isPacking = isPacking;
	}
	
}
