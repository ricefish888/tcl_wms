package com.vtradex.wms.server.model.entity.base;

import com.vtradex.thorn.server.model.Entity;

/**
 * 导入模板下载	
 * @author administrator
 *
 */
public class WmsImportTemplateDownload extends Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6059145777820317652L;

	/**类型*/
	private String type;
	
	/**编码*/
	private String code;
	
	/**名称*/
	private String name;
	
	/**文件名称*/
	private String fileName;
	
	/**文件全路径*/
	private String fileFullPath;
	
	/**下载链接*/
	private String downloadUrl;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileFullPath() {
		return fileFullPath;
	}

	public void setFileFullPath(String fileFullPath) {
		this.fileFullPath = fileFullPath;
	}
	
	
}
