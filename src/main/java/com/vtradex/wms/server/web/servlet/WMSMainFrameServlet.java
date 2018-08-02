package com.vtradex.wms.server.web.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vtradex.thorn.server.servlet.MainFrameServlet;

/**
 * 
 * @author <a href="yan.li@vtradex.com">李炎</a>
 * @description
 * 
 */

public class WMSMainFrameServlet extends MainFrameServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(final HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
//		if(req.getRequestURI().indexOf("*.login") > 0 && requireSetupLicense()){
//			res.getWriter().write("setupLicense");
//		}
//		else 
		if(isSSO(req)){
			sso(req,res);
		}
		else{
			if(req.getSession(false) != null){
				System.out.println("wms session:"+req.getSession().getId() +" ----------"+Thread.currentThread().toString());
				if("logout=true".equals(req.getQueryString())){
					req.getSession(false).invalidate();  //注释掉
				}
				res.sendRedirect("index.html");
//				res.getWriter().write("success");
			}else {
				InputStream inputStream =  Thread.currentThread().getContextClassLoader().getResourceAsStream(getMainFrameHtmlPath());
				byte[] contents = new byte[inputStream.available()];
				inputStream.read(contents);
				res.getOutputStream().write(contents);
				res.getOutputStream().flush();
				res.getOutputStream().close();
				inputStream.close();	
			}
		}
	}

	protected String getMainFrameHtmlPath() {
		return "com" + System.getProperty("file.separator") + "vtradex"
				+ System.getProperty("file.separator") + "wms"
				+ System.getProperty("file.separator") + "server"
				+ System.getProperty("file.separator") + "web"
				+ System.getProperty("file.separator") + "servlet"
				+ System.getProperty("file.separator") + "index.html";
	}
}
