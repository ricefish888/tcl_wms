package com.vtradex.wms.webservice.sap.base;

import com.vtradex.wms.server.service.annotation.XStreamCDATA;
import com.vtradex.wms.server.service.annotation.XmlDataNote;
import com.vtradex.wms.webservice.model.SapInterfaceStatus;

/***
 *公共回传接口明细数据
 */
@XmlDataNote("SapCommonCallbackDetail")
public class SapCommonCallbackDetail {
	
 
	
	private String lineNo;
	
	/***
	 * {@link SapInterfaceStatus}
	 */
	private String flag; //成功失败标识
	
	@XStreamCDATA   //有特殊字符  需要CDATA
	private String remark = ""; //记录说明  如果错误则记录错误日志

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	

}
