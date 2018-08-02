package com.vtradex.wms.server.web.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.thorn.server.util.Constant;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.service.receiving.WmsTclASNManager;
import com.vtradex.wms.server.utils.StringHelper;


/**
 * 记录打印条码的张数
 */
public class ReportPrintServlet extends HttpServlet {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6350341816422535974L;

	protected ApplicationContext ac;
	
	private WmsTclASNManager wmsTclASNManager;

	public WmsTclASNManager getTclAsnManager(){
		if(wmsTclASNManager == null){
			return (WmsTclASNManager)ac.getBean("wmsTclASNManager");
		}
		return wmsTclASNManager;
	}

	public void init(ServletConfig sc) throws ServletException {
		super.init(sc);
		ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc.getServletContext());
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		ThornUser loginUser = (ThornUser)req.getSession().getAttribute(Constant.USER_IN_SESSION);
		String printName = loginUser.getName();
		req.setCharacterEncoding("UTF-8");
		// 获得当前打印报表的名称和参数的ID
		String[] param = req.getParameter("param").split(";");
		String parentIds="";//parentIds则代表打印的是标签
		String raq = "";
		String id = "";//ID有值则代表打印的是收货单

		//把获取的参数值保存到一个Map
//		Map<Object, Object> maps = new HashMap<Object, Object>();
		for (String pr : param){
			String[] prs = pr.split("=");
			if (prs[0].equals("parentIds")){
				parentIds = prs[1];
			}
			if (prs[0].equals("raq")){
				raq = prs[1];
			}
			if (prs[0].equals("id")){
				parentIds = prs[1];
			}
		}
		
		if(!StringHelper.in(raq, new String[]{"printSupplierWmsASNDetailPage.raq","receipt.raq","directPrintLabelPage.raq",
				"printWork.raq","YLCKD.raq","TGYSCKD.raq"})){
			return;
		}
		
		String ip = getLocalIp(req);//返回客户端ip
		getTclAsnManager().countPrintTime(parentIds,ip,raq);
		getTclAsnManager().isPrint(parentIds,raq);
		getTclAsnManager().printTimes(parentIds,raq);
		
	}
	
	protected void doGet(final HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		this.doPost(req, res);
	}
	
	/**获取客户端IP*/
	public static String getLocalIp(HttpServletRequest request) {
		String remoteAddr = request.getRemoteAddr();
		String forwarded = request.getHeader("X-Forwarded-For");
		String realIp = request.getHeader("X-Real-IP");
	
		String ip = null;
		if (realIp == null) {
			if (forwarded == null) {
				ip = remoteAddr;
			} else {
				ip = remoteAddr + "/" + forwarded.split(",")[0];
			}
		} else {
			if (realIp.equals(forwarded)) {
				ip = realIp;
			} else {
				if(forwarded != null){
					forwarded = forwarded.split(",")[0];
				}
				ip = realIp + "/" + forwarded;
			}
		}
		return ip;
	}
}
