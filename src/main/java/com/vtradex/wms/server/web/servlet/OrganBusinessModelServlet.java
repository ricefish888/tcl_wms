package com.vtradex.wms.server.web.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gwt.json.client.JSONArray;
import com.vtradex.thorn.server.dao.CommonDao;
import com.vtradex.thorn.server.model.security.ThornBaseOrganization;
import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.thorn.server.service.security.BaseOrganizationManager;
import com.vtradex.thorn.server.servlet.BaseHttpServlet;
import com.vtradex.thorn.server.util.Constant;
import com.vtradex.thorn.server.util.JsonUtil;

public class OrganBusinessModelServlet extends BaseHttpServlet{
	protected final Log logger = LogFactory.getLog(this.getClass());
	private static final long serialVersionUID = -9113561178739716230L;

	@Override
	public void process(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		getAll2Json(req, res);
	}
	
	private void getAll2Json(HttpServletRequest req, HttpServletResponse res) throws UnsupportedEncodingException, IOException{
		String msg="";
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html; charset=utf-8");
		String searchName=req.getParameter("searchName");
		ThornUser user=(ThornUser)req.getSession().getAttribute(Constant.USER_IN_SESSION);
		if(user==null){
			msg="user is null";
			logger.error(msg);
			res.getOutputStream().write(msg.getBytes("UTF-8"));
			return;
		}
		if(StringUtils.isBlank(searchName)){
			BaseOrganizationManager baseOrganizationManager=(BaseOrganizationManager)ac.getBean("baseOrganizationManager");
			msg=new JsonUtil().toJson(baseOrganizationManager.getByUser(user));
		}else{
			CommonDao commonDao=(CommonDao)ac.getBean("commonDao");
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("u",user);
			params.put("searchName","%"+searchName+"%");
			String hql="select distinct organ from ThornUser u,ThornGroup gup,ThornRole r, ThornBaseOrganization organ join fetch organ.businessModel model where (model.name like :searchName or organ.name like :searchName) and u=:u and u.id in elements(gup.users) and organ.id in elements(u.organs) and gup.id in elements(r.groups)";
			List<ThornBaseOrganization> organs=commonDao.query(hql, params);
			msg=new JsonUtil().toJson(organs);
		}
		res.getOutputStream().write(msg.getBytes("UTF-8"));
	}
}
