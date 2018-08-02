package com.vtradex.wms.server.web.servlet;

import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author: 李炎
 */
public class UserSessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent arg0) {

	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		ServletContext servletContext = arg0.getSession().getServletContext();
		Set<HttpSession> sessions = (Set<HttpSession>)servletContext.getAttribute(WMSLoginServlet.SESSIONS);
		if(sessions != null && sessions.contains(arg0.getSession())){
			sessions.remove(arg0.getSession());
			arg0.getSession().invalidate();
		}
	}

}
