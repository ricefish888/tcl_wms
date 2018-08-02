package com.vtradex.wms.server.model.entity.production;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.entity.base.WmsSapFactory;

/***
预留单
 *
 */
public class WmsReservedOrder extends VersionalEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8291820508500954894L;

	/**编号*/
	private String code;
	
	/**预留号*/
	private String sapCode;
	
	/**工厂*/
	private WmsSapFactory factory;
	
	/**收货库存地*/
	private String kcd;
	/**
	 * {@link ProductionOrderStatus}
	 * */
	private String status = ProductionOrderStatus.OPEN;
	
	/**基准日期*/
	private Date jzrq;
	
	/**用户名称*/
	private String yhmc;
	
	/**移动类型  
	 * 
	 * {@link WmsReservedOrderType}
	 * */
	private String ydlx;
	
	/**成本中心 */
	private String cbzx;
	
	/**成本中心描述*/
	private String cbzxRemark;
	/**收货人*/
	private String receiveWorker;
	
	private Set<WmsReservedOrderDetail> details = new HashSet<WmsReservedOrderDetail>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReceiveWorker() {
		return receiveWorker;
	}

	public void setReceiveWorker(String receiveWorker) {
		this.receiveWorker = receiveWorker;
	}

	public String getSapCode() {
		return sapCode;
	}

	public void setSapCode(String sapCode) {
		this.sapCode = sapCode;
	}

	public WmsSapFactory getFactory() {
		return factory;
	}

	public void setFactory(WmsSapFactory factory) {
		this.factory = factory;
	}

	public String getKcd() {
		return kcd;
	}

	public void setKcd(String kcd) {
		this.kcd = kcd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getJzrq() {
		return jzrq;
	}

	public void setJzrq(Date jzrq) {
		this.jzrq = jzrq;
	}

	public String getYhmc() {
		return yhmc;
	}

	public void setYhmc(String yhmc) {
		this.yhmc = yhmc;
	}

	public String getYdlx() {
		return ydlx;
	}

	public void setYdlx(String ydlx) {
		this.ydlx = ydlx;
	}

	public String getCbzx() {
		return cbzx;
	}

	public void setCbzx(String cbzx) {
		this.cbzx = cbzx;
	}

	public String getCbzxRemark() {
		return cbzxRemark;
	}

	public void setCbzxRemark(String cbzxRemark) {
		this.cbzxRemark = cbzxRemark;
	}

	public Set<WmsReservedOrderDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<WmsReservedOrderDetail> details) {
		this.details = details;
	}

}
