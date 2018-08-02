package com.vtradex.wms.server.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.FileCopyUtils;

import com.vtradex.thorn.server.dao.CommonDao;
import com.vtradex.thorn.server.servlet.BaseHttpServlet;
import com.vtradex.wms.server.model.entity.base.WmsImportTemplateDownload;
import com.vtradex.wms.server.model.entity.order.WmsCheckOrder;
import com.vtradex.wms.server.model.entity.order.WmsCheckOrderStatus;
import com.vtradex.wms.webservice.utils.CommonHelper;

public class TclFileDownloadServlet extends BaseHttpServlet {

	private static final long serialVersionUID = -5819071918882193195L;
	protected final Log logger = LogFactory.getLog(getClass());
	public static final String FILE_NAME_PARAM = "fileName";

	public TclFileDownloadServlet() {
	}
	
	private void showErrorInfo(HttpServletResponse resp,String errorInfo) throws ServletException, IOException{
		String charcode="GBK";
		resp.setCharacterEncoding(charcode);
		resp.getOutputStream().write(("<script>alert('"+errorInfo+"');window.close();</script>").getBytes(charcode)); 
	}

	public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CommonDao commonDao = (CommonDao) this.ac.getBean("commonDao");

		String type = req.getParameter("type");
		String idStr = req.getParameter("id");
		if("CHECKORDER".equals(type)) {//对账单
			Long id = 0L;
			try {
				idStr=CommonHelper.tclBase64Decode(idStr);
				id = Long.valueOf(idStr);
			}
			catch(Exception e) {
				showErrorInfo(resp, "参数解密错误!");
				return;
			}
			WmsCheckOrder checkOrder = commonDao.load(WmsCheckOrder.class, id);
			if(checkOrder == null ){
				showErrorInfo(resp, "下载参数错误!");
				return;
			}
			if(WmsCheckOrderStatus.OPEN.equals(checkOrder.getStatus())) {
				showErrorInfo(resp, "对账单接收后才能下载附件!");
				return;
			}
			File f = new File(checkOrder.getFilepath()) ;//路径
			if(f == null || !f.exists()) {
				showErrorInfo(resp, "下载文件不存在！");
				return;
			}
			InputStream is =   new FileInputStream(f);
			resp.setHeader("content-disposition", "attachment; filename="+f.getName());
			FileCopyUtils.copy(is, resp.getOutputStream());
		}
		else if("TEMPLEATE".equals(type)) {//模板下载
			Long id = 0L;
			try {
				idStr=CommonHelper.tclBase64Decode(idStr);
				id = Long.valueOf(idStr);
			}
			catch(Exception e) {
				showErrorInfo(resp, "参数解密错误!");
				return;
			}
			WmsImportTemplateDownload data = commonDao.load(WmsImportTemplateDownload.class, id);
			if(data == null ){
				showErrorInfo(resp, "下载参数错误!");
				return;
			}
			 
			File f = new File(data.getFileFullPath()) ;//路径
			if(f == null || !f.exists()) {
				showErrorInfo(resp, "下载文件不存在！");
				return;
			}
			InputStream is =   new FileInputStream(f);
			resp.setHeader("content-disposition", "attachment; filename="+f.getName());
			FileCopyUtils.copy(is, resp.getOutputStream());
		}
		
		 
	}
}
