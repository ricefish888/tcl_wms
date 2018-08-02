package com.vtradex.wms.server.service.format;

import java.util.List;

import com.vtradex.thorn.server.dao.CommonDao;
import com.vtradex.thorn.server.format.Formatter;
import com.vtradex.wms.server.model.entity.base.WmsSupplier;
import com.vtradex.wms.server.utils.StringHelper;

public class SupplierNameFormatter implements Formatter{
	protected CommonDao commonDao;
	public SupplierNameFormatter(CommonDao commonDao){
		this.commonDao = commonDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public String format(String paramString1, Object paramObject,List paramList, String paramString2) {
		String supplerName = "";
		String temp = paramList.get(8).toString().trim();
		if(!StringHelper.isNullOrEmpty(temp)){
			WmsSupplier supplier = (WmsSupplier)commonDao.findByQueryUniqueResult("FROM WmsSupplier su WHERE su.code=:sc", "sc", temp);
			if(null!=supplier){
				supplerName = supplier.getName();
			}
		}
		return supplerName;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public String exportFormat(String paramString1, Object paramObject,List paramList, String paramString2) {
		return format(paramString1,paramObject,paramList,paramString2);
	}

}
