package com.vtradex.wms.server.service.base.po;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.entity.base.WmsImportTemplateDownload;
import com.vtradex.wms.server.service.base.WmsDownloadFileManager;
import com.vtradex.wms.server.utils.GlobalParamUtils;
import com.vtradex.wms.webservice.utils.CommonHelper;

public class DefaultWmsDownloadFileManager 
		extends DefaultBaseManager implements WmsDownloadFileManager{

	@Override
	public void saveData(WmsImportTemplateDownload download) {
		
		String dir = GlobalParamUtils.getGloableStringValue("downloadFileDir"); //取全局参数
		download.setFileFullPath(dir+download.getFileName());
		commonDao.store(download); //先保存一下 生成ID
		String url = "<a href='*.tclFileDowload?type=TEMPLEATE&id="+CommonHelper.tclBase64Encode(download.getId()+"")+"' target='_blank'>下载附件</a>";
		download.setDownloadUrl(url);
		commonDao.store(download);
	}

}
