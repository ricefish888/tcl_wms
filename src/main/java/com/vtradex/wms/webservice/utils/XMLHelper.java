package com.vtradex.wms.webservice.utils;

import java.io.StringWriter;
import java.text.SimpleDateFormat;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.vtradex.thorn.server.exception.BusinessException;

/**
 * @author <a href="mailto:brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.14 $Date: 2014/10/29 05:12:17 $
 */
public class XMLHelper {
	public static final String ENCODING = "UTF-8";
	public static final Boolean formatXml = Boolean.TRUE; //是否格式化xml
	
	/**
	 * 格式化输出
	 */
	public static String prettyXML(Document doc) {

	    if(!formatXml) { //不格式化则直接返回
	        return doc.asXML();
	    }
	    
	    try {
            // 格式化输出格式
            OutputFormat format = OutputFormat.createPrettyPrint();
//            format.setEncoding("utf-8");  //不设置就用默认
            StringWriter writer = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(writer, format); // 格式化输出流
            xmlWriter.write(doc);// 将document写入到输出流
            xmlWriter.close();
            return writer.toString();
        }
        catch (Exception e) {
            throw new BusinessException(e);
        }
	}
	/***格式化xml字符串*/
	public static String prettyXML(String xml) {
        try {
            Document document = DocumentHelper.parseText(xml);
            return prettyXML(document);
        }
        catch (Exception e) {
            throw new BusinessException(e);
        }
    }
	 
	 
	
}
