package com.vtradex.wms.server.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.webservice.utils.CommonHelper;


/**
 * 注销
 */
public class TclLogoutServlet extends HttpServlet {
	
	
	private static final long serialVersionUID = -4780374239451413232L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		if(UserHolder.getUser()!=null) {
			CommonHelper.log("logout...."+UserHolder.getUser().getLoginName());
		}
		else {
			CommonHelper.log("logout....");
		}
		if (req.getSession(false) != null) {
			req.getSession(false).invalidate();
		}
		UserHolder.setUser(null);
		res.sendRedirect("index.html");
	}
	
	protected void doGet(final HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		this.doPost(req, res);
	}
	 
}
