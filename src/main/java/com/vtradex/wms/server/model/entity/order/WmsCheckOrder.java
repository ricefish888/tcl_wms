package com.vtradex.wms.server.model.entity.order;

import java.util.Date;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.entity.base.WmsFactoryXmlb;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;

/***
 * 对账单
 * @author administrator
 *
 */
public class WmsCheckOrder extends VersionalEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1685915504019145750L;

	private WmsSupplier supplier;
	
	/**
	 * 编码
	 * 过账日期BUDAT的年月  需要处理  一次过来的对账单BUDAT肯定在同一个月份*/
	private String code;
	
	/***
	 * {@link WmsFactoryXmlb}
	 */
	private String xmlb;
	
	
	/**附件名称 超链接形式*/
	private String filename;
	
	/**附件路径*/
	private String filepath;
	
	/***
	 * {@link WmsCheckOrderStatus}
	 */
	private String status=ConfirmStatus.OPEN;
	
	/**确认时间*/
	private Date confrimTime;
	/**
     * 确认人
     */
    private String confirmor;
    /**
	 * 接收人
	 */
	private String receiver;
	/**
	 * 接收时间
	 */
	private Date receiveTime;
	public WmsSupplier getSupplier() {
		return supplier;
	}
	public void setSupplier(WmsSupplier supplier) {
		this.supplier = supplier;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getXmlb() {
		return xmlb;
	}
	public void setXmlb(String xmlb) {
		this.xmlb = xmlb;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getConfrimTime() {
		return confrimTime;
	}
	public void setConfrimTime(Date confrimTime) {
		this.confrimTime = confrimTime;
	}
	public String getConfirmor() {
		return confirmor;
	}
	public void setConfirmor(String confirmor) {
		this.confirmor = confirmor;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public Date getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	
}
