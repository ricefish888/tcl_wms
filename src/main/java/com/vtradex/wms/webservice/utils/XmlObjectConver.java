package com.vtradex.wms.webservice.utils;

import java.lang.reflect.Field;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.vtradex.wms.server.service.annotation.XmlDataNote;
import com.vtradex.wms.webservice.sap.model.SapItemArray;

/**
 * xml 与 对象互相转换
 * */
public class XmlObjectConver {
	
	private static final String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";
	 

	public static String toXML(Object object){
		XStream xstream = new XStream(new DomDriver("UTF-8"));
		addXStreamAlias(xstream,object.getClass(),true);
		return  xmlHead + xstream.toXML(object);
	}
	 
	public static Object fromXML(Object object , String xml){
		XStream xstream = new XStream(new DomDriver("UTF-8"));
		addXStreamAlias(xstream,object.getClass(),true);
	
//		return (Object)xstream.fromXML(xml);
		object=(Object)xstream.fromXML(xml);
		return object;
	}
	public static void addXStreamAlias(XStream xstream,Class clazz,boolean first) {
		Field[] fields = clazz.getDeclaredFields();
		XmlDataNote xmlDataNote;
		Class fieldClazz;
		if (first) {
			xmlDataNote = (XmlDataNote)clazz.getAnnotation(XmlDataNote.class);
			xstream.alias(xmlDataNote.value(), clazz);
		}
		for (Field field : fields) {
			fieldClazz = field.getType();
			if (fieldClazz.isPrimitive() || fieldClazz.equals(String.class)
				|| fieldClazz.equals(Integer.class)
				|| fieldClazz.equals(Long.class)
				|| fieldClazz.equals(Float.class)
				|| fieldClazz.equals(Double.class)) {
				continue;
			}
			if (fieldClazz.isArray()) {
				try {
					fieldClazz = Class.forName(fieldClazz.getName().substring(2,fieldClazz.getName().length()-1));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			xmlDataNote = (XmlDataNote) fieldClazz.getAnnotation(XmlDataNote.class);
			xstream.alias(xmlDataNote.value(), fieldClazz);
			addXStreamAlias(xstream,fieldClazz,false);
		}
	}
	
	public static void main(String[] args) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SapItemArray> <sapItems> <SapItem><MESSAGE__ID><![CDATA[111]]></MESSAGE__ID>  <MAKT__MAKTX/> </SapItem>"
				+ "  <SapItem> <MESSAGE__ID><![CDATA[22a<d>a2]]></MESSAGE__ID> </SapItem> </sapItems> </SapItemArray>";
		SapItemArray a = new SapItemArray();
		a=(SapItemArray)fromXML(a, xml);
		System.out.println(a);
	}
}
