package com.vtradex.wms.server.service.base.po;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.entity.base.WmsCostCenter;
import com.vtradex.wms.server.service.base.WmsCostCenterManager;
import com.vtradex.wms.server.utils.DateUtil;
import com.vtradex.wms.webservice.utils.ExcelHelper;

/**
 * 成本中心
 */
public class DefaultWmsCostCenterManager extends DefaultBaseManager implements WmsCostCenterManager {
	
	//导入
	public void importWmsCostCenterFile(File file){
		
		List<Map<String,Object>> infos = ExcelHelper.parseExcel2List(file);
    	for(Map<String,Object> info : infos){
    		String code = (String) info.get("编码");
    		if(code == null || "".equals(code)){
    			throw new BusinessException("行号"+info.get("EXCEL行号")+"编码不能为空，请维护编码");
    		}
    		String name = (String) info.get("名称");
    		if(name == null || "".equals(name)){
    			throw new BusinessException("行号"+info.get("EXCEL行号")+"名称不能为空，请维护名称");
    		}
    		String xxpirationDate = (String) info.get("有效期");
    		if(xxpirationDate == null || "".equals(xxpirationDate)){
    			throw new BusinessException("行号"+info.get("EXCEL行号")+"有效期不能为空，请维护有效期");
    		}
    		
    		if(xxpirationDate.length() - 8 != 0){
    			throw new BusinessException("行号"+info.get("EXCEL行号")+"有效期【"+xxpirationDate+"】格式有误,请检查");
    		}
    		Date date = DateUtil.formatDate(xxpirationDate);
    		if(date==null){
    			throw new BusinessException("行号"+info.get("EXCEL行号")+"有效期【"+xxpirationDate+"】格式有误,请检查");
    		}
    		
			String hql = "FROM WmsCostCenter w WHERE w.code =:code ";
			WmsCostCenter wmsCostCenter = (WmsCostCenter) commonDao.findByQueryUniqueResult(hql,new String[]{"code"},
			                            		   new Object[]{code.trim()});
			if(wmsCostCenter == null){//不存在,新建
				wmsCostCenter = EntityFactory.getEntity(WmsCostCenter.class);
			}
			wmsCostCenter.setCode(code.trim());
			wmsCostCenter.setName(name.trim());
			wmsCostCenter.setXxpirationDate(date);
			commonDao.store(wmsCostCenter);
			
		}
	}
}
