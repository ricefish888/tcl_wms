package com.vtradex.wms.server.model.entity.receiving;

import java.util.Date;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.component.LotInfo;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.order.WmsTransportOrderDetail;
import com.vtradex.wms.server.utils.DoubleUtils;



/**
 * 收货单明细
 * 
 * @author <a href="mailto:ming.chen@tech.vtradex.com">陈明</a>
 * @since Dec 10, 2015
 */
public class WmsASNDetail extends VersionalEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 收货单*/
	@UniqueKey
	private WmsASN asn;
	/** 客户行号*/
	@UniqueKey
	private Integer lineNo;
	/** 货品*/
	private WmsItem item;
	/** 托盘 */
	private String pallet;
	/** 周转箱号 */
	private String carton;
	/** 期待数量*/
	private Double expectedQty = 0.0D;
	/** 包装单位*/
	private WmsPackageUnit packageUnit;
	/** 期待包装数量*/
	private Double expectedPackQty = 0.0D;
	/** 期待拆零数量*/
	private Double expectedUnpackQty = 0.0D;
	/** 批属性 */
	private LotInfo lotInfo;	
	/** 是否需要质检 **/
	private boolean beQuality = false;
	/** 实际收货数量*/
	private Double receivedQty = 0.0D;
	/** 货差数量*/
	private Double shortQty = 0.0D;
	/** 上架数量*/
	private Double movedQty = 0.0D;
	/** 库存状态 */
	private String inventoryStatus;
	/** 收货计划明细 */
	private WmsPoDetail poDetail;
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
	/** 
	 * 自定义字段6-9
	 * add:2017-02-23
	 *      wms5.0表结构说明文档V2.0.7
	 *  
	 */
	private String userField6;
	private String userField7;
	private String userField8;
	private String userField9;
	
	/**
	 * 自定义字段10-20
	 * add:2017-03-02
	 *      wms5.0表结构说明文档V2.0.9
	 */
	private String userField10;
	private String userField11;
	private String userField12;
	private String userField13;
	private String userField14;
	private String userField15;
	private String userField16;
	private String userField17;
	private String userField18;
	private String userField19;
	private String userField20;
	
	private WmsTransportOrderDetail transportOrderDetail;
	
	/**自定义日期字段1 */
	private Date userFieldT1;
	
	/**
	 * wms5.0表结构说明文档V2.2.8
	 * 质检数量
	 */
	private Double qcQty = 0.0D;
	
	
	/**
	 * wms5.0表结构说明文档V2.3.3
	 * 实际质检数量
	 */
	private Double actualQcQty = 0.0D;
	
	public Double getLabel() {
		return label;
	}
	public void setLabel(Double label) {
		this.label = label;
	}
	/** 标签张数*/
	private Double label = 0.0D;
	
	public LotInfo getLotInfo() {
		return lotInfo;
	}
	public void setLotInfo(LotInfo lotInfo) {
		this.lotInfo = lotInfo;
	}
	public WmsASN getAsn() {
		return asn;
	}
	public void setAsn(WmsASN asn) {
		this.asn = asn;
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
	public Double getExpectedQty() {
		return expectedQty;
	}
	public void setExpectedQty(Double expectedQty) {
		this.expectedQty = expectedQty;
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
	public Double getExpectedUnpackQty() {
		return expectedUnpackQty;
	}
	public void setExpectedUnpackQty(Double expectedUnpackQty) {
		this.expectedUnpackQty = expectedUnpackQty;
	}
	public boolean isBeQuality() {
		return beQuality;
	}
	public void setBeQuality(boolean beQuality) {
		this.beQuality = beQuality;
	}
	public Double getReceivedQty() {
		return receivedQty;
	}
	public void setReceivedQty(Double receivedQty) {
		this.receivedQty = receivedQty;
	}
	
	
	public Double getShortQty() {
		return shortQty;
	}
	public void setShortQty(Double shortQty) {
		this.shortQty = shortQty;
	}
	public Double getMovedQty() {
		return movedQty;
	}
	public void setMovedQty(Double movedQty) {
		this.movedQty = movedQty;
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
	public Date getUserFieldT1() {
		return userFieldT1;
	}
	public void setUserFieldT1(Date userFieldT1) {
		this.userFieldT1 = userFieldT1;
	}
	
	public String getInventoryStatus() {
		return inventoryStatus;
	}
	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}
	public WmsPoDetail getPoDetail() {
		return poDetail;
	}
	public void setPoDetail(WmsPoDetail poDetail) {
		this.poDetail = poDetail;
	}
	public Double getQcQty() {
		return qcQty;
	}
	public void setQcQty(Double qcQty) {
		this.qcQty = qcQty;
	}
	/**
	 * 收货
	 * @param quantity
	 */
	public void receive(double qty) {
		this.receivedQty = DoubleUtils.add(this.receivedQty, qty,this.item.getBuPrecision());
		this.asn.receive(qty,this.item.getBuPrecision());
	}
	
	/**
	 * 取消收货
	 * @param quantity
	 */
	public void cancelReceive(double quantity,int scale) {
		this.receivedQty = DoubleUtils.sub(this.receivedQty,quantity,scale);
		
		this.asn.cancelReceive(quantity,scale);
		if(quantity>asn.getQuantityQty()){
			this.asn.cancelQuantityQty(asn.getQuantityQty(), scale);//删除质检数量
		}else{
			this.asn.cancelQuantityQty(quantity, scale);//删除质检数量
		}
		
	}
	/**
	 * 获取未收货数量
	 * @return
	 */
	public double getUnReceivedQtyBU() {
		return DoubleUtils.sub(DoubleUtils.sub(this.expectedQty,this.receivedQty),this.shortQty,this.item.getBuPrecision());
//		return this.expectedQty - this.receivedQty - this.shortQty;
	}
	/** 上架 */
	public void addMovedQty(Double movedQtyTemp) {
		this.movedQty = DoubleUtils.add(this.movedQty, movedQtyTemp,this.item.getBuPrecision());
//		this.movedQty += movedQty;
		asn.addMovedQty(movedQtyTemp,this.item.getBuPrecision());
	}
	/**
	 * 获取未上架数量
	 */
	public double getUnMoveQtyBU(){
		return DoubleUtils.sub(this.receivedQty, this.movedQty,this.item.getBuPrecision());
	}
    /**
     * 
     * @Description:取消上架数量(上架后再取消收货时,需取消ASN的上架数量)
     * @param movedQtyTemp
     * @param scale
     */
	public void cancelMoveQty(double movedQtyTemp,int scale) {
		this.movedQty = DoubleUtils.sub(this.movedQty,movedQtyTemp,scale);
		
		this.asn.cancelMoveQty(movedQtyTemp,scale);
	}
	public Double getActualQcQty() {
		return actualQcQty;
	}
	public void setActualQcQty(Double actualQcQty) {
		this.actualQcQty = actualQcQty;
	}
	
    public WmsTransportOrderDetail getTransportOrderDetail() {
        return transportOrderDetail;
    }
    public void setTransportOrderDetail(WmsTransportOrderDetail transportOrderDetail) {
        this.transportOrderDetail = transportOrderDetail;
    }
    /**
     * 关闭交货时,退回未收货的数量,并把计划数量减去退回数量
     * @param qty
     */
    public void subExceptQty(double qty) {
		this.expectedQty = DoubleUtils.sub(this.expectedQty,qty);
		this.expectedPackQty = DoubleUtils.sub(this.expectedPackQty,qty);
	}
}
