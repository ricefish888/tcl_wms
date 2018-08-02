package com.vtradex.wms.server.service.filter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vtradex.license.MissingLicenseException;
import com.vtradex.license.client.LicenseManager;
import com.vtradex.thorn.server.config.menu.engine.MenuItem;
import com.vtradex.thorn.server.model.security.ThornBaseOrganization;
import com.vtradex.thorn.server.model.security.ThornBusinessModel;
import com.vtradex.thorn.server.model.security.ThornModelMenu;
import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.thorn.server.service.security.SecurityFilter;
import com.vtradex.thorn.server.service.security.UserManager;
import com.vtradex.thorn.server.servlet.license.UploadLicenseServlet;
import com.vtradex.thorn.server.util.Constant;
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.thorn.server.web.security.BussinessModelHolder;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.service.receiving.WmsTclASNManager;

public class LoginFilter implements Filter  {
	public static final String SINGLE_SIGN_ON = "userId";
	protected static ApplicationContext ac;
	protected String licenseLocation;
	protected ServletContext context;
	
	@Override
	public void destroy() {
		
	}
	
	public  boolean isOnLic() {//是否启用license校验,true=启用,false=不启用
		return true;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		if (requireSetupLicense() && isOnLic()) {
			res.sendRedirect("setupLicense.html");
		}
		else{
			if (isSSO(req)) {
				if(sso(req, res)){
					chain.doFilter(request, response);
				}
			}else{
				if (req.getSession(false) != null) {
					if("logout=true".equals(req.getQueryString())){
						req.getSession(false).invalidate();
					}
				}
				chain.doFilter(request, response);
			}
		}
	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {
		context = cfg.getServletContext();
		ac = WebApplicationContextUtils.getRequiredWebApplicationContext(cfg.getServletContext());
		licenseLocation = cfg.getServletContext().getInitParameter(UploadLicenseServlet.LICENSE_LOCATION_PARAM);
	}
	
	protected boolean requireSetupLicense() {
		LicenseManager licenseManager = (LicenseManager) ac.getBean("licenseManager");
		try {
			if (getLeastFilePath() == null) {
				return true;
			}
			licenseManager.verifyLicense(getLeastFilePath());
		} catch (Exception e) {
			e.printStackTrace();

			return true;

		}
		return false;
	}

	private String getLeastFilePath() throws MissingLicenseException {
		if (!StringUtils.isEmpty(this.licenseLocation)) {
			return this.getServletContext().getRealPath(this.licenseLocation);
		}
		String licensePath = System.getProperty("file.separator") + "WEB-INF" + System.getProperty("file.separator") + "classes"
				+ System.getProperty("file.separator");
		String classesPath = getServletContext().getRealPath(licensePath);
		File file = new File(classesPath);
		File[] fileList = file.listFiles();
		if (fileList == null || fileList.length == 0) {
			throw new MissingLicenseException();
		}
		for (File subFile : fileList) {
			if (subFile.getName().toLowerCase().endsWith(".license")) {
				return subFile.getAbsolutePath();
			}
		}
		return null;
	}

	private ServletContext getServletContext() {
		return context;
	}

	@SuppressWarnings("rawtypes")
	protected boolean isSSO(final HttpServletRequest req) {
		Map parameterMap = req.getParameterMap();
		return parameterMap.containsKey(SINGLE_SIGN_ON) && isSupportSSO();
	}

	protected boolean isSupportSSO() {
		return false;
	}

	protected boolean sso(final HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		final String baseOrganStr = req.getParameter("baseOrgan");
		final Long baseOrgan = StringUtils.isEmpty(baseOrganStr) ? 0L : Long.valueOf(baseOrganStr);

		String userId = req.getParameter(SINGLE_SIGN_ON);
		userId = decodingUserId(userId);
		final UserManager userManager = (UserManager) ac.getBean("userManager");
		ThornUser user = userManager.retrieve(userId);
		if (doAuthenticate(user)) {
			res.sendRedirect("SSOError.html");
			return false;
		}
		ThornBusinessModel bussinessModel = getUserManager().getThornBusinessModelByOrganId(baseOrgan);
		ThornBaseOrganization baseOrganization = getUserManager().getThornBaseOrganizationByOrganId(baseOrgan);

		SecurityFilter sf = buildSecurityFilter(user, baseOrgan, bussinessModel.getId(), user.getLocale().getLanguage());
		req.getSession().setAttribute(Constant.SECURITY_FILTER_IN_SESSION, sf);
		req.getSession().setAttribute(Constant.USER_IN_SESSION, user);
		req.getSession().setAttribute(Constant.LOCALE, user.getLocale().getLanguage());
		req.getSession().setAttribute(Constant.BASE_ORGANIZATION_IN_SESSION, baseOrganization);
		req.getSession().setAttribute(Constant.BUSSINESS_MODEL, bussinessModel);
		res.sendRedirect("index.html");

		UserHolder.setUser(user);
		BussinessModelHolder.setThornBusinessModel(bussinessModel);
		BaseOrganizationHolder.setThornBaseOrganization(baseOrganization);
		
		return true;
	}

	protected SecurityFilter buildSecurityFilter(final ThornUser loginUser, Long baseOrgan, Long bussinessModelId, final String locale) {
		SecurityFilter sf = getUserManager().getSecurityFilter(loginUser, baseOrgan, locale);
		List<MenuItem> menuItems = getUserManager().getMenuItemByBussinessModel(bussinessModelId, ThornModelMenu.PAGE);
		sf.setAllMenuItems(menuItems);
		return sf;
	}

	protected UserManager getUserManager() {
		return (UserManager) (ac.getBean("userManager"));
	}

	protected String decodingUserId(String userId) {
		return userId;
	}

	protected boolean doAuthenticate(ThornUser user) {
		return user == null;
	}

}
