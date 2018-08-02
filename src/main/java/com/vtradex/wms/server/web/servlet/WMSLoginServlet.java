package com.vtradex.wms.server.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vtradex.thorn.server.exception.AuthenticationException;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.holder.UpdateInfoHodler;
import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.thorn.server.servlet.LoginServlet;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.utils.DateUtil;

public class WMSLoginServlet extends LoginServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1127841168305217136L;

	public static final String SESSIONS = "SESSIONS";

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//		super.doPost(req, res);
		  String loginName = req.getParameter("login_name");
		  /*  56 */     String password = req.getParameter("password");
		  /*  57 */     String locale = req.getParameter("locale");
		  /*  58 */     String module = req.getParameter("module");
		  /*     */     
		  /*  60 */     ThornUser loginUser = null;
		  /*     */     try {
		  /*  62 */       loginUser = authenticateUser(loginName, password, locale);
		  /*     */     } catch (AuthenticationException e) {
		  /*  64 */       e.printStackTrace();
		  /*  65 */       String hintMsg = LocalizedMessage.getLocalizedMessage(e.getClass().getSimpleName(), locale);
		  /*  66 */       res.getOutputStream().write(hintMsg.getBytes("UTF-8"));
		  /*  67 */       return;
		  /*     */     }
		  /*  69 */     res.getOutputStream().write("success".getBytes());
		  /*  70 */     req.getSession().setAttribute("userInSession", loginUser);
		  /*  71 */     req.getSession().setAttribute("LOCALE", locale);
		  /*  72 */     req.getSession().setAttribute("module", module);
		  /*  73 */     UserHolder.setUser(loginUser);
		  /*  74 */     UpdateInfoHodler.addUser(loginUser);

		// 增加session

		ThornUser u = UserHolder.getUser();
		if (u == null) {
			throw new BusinessException("session未取到用户");
		}
		try {
			req.getSession().setAttribute("SESSION_LOGIN_NAME", u.getLoginName());
			
//			System.out.println(u.getLoginName());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("设置用户session失败" + DateUtil.getDateTime());
			String hintMsg = LocalizedMessage.getLocalizedMessage( "设置用户session失败",req.getParameter("locale"));
			res.getOutputStream().write(hintMsg.getBytes("UTF-8"));
			return;
		}

	}
}
