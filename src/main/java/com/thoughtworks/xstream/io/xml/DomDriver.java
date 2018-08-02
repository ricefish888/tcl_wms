/*
 * Copyright (C) 2004, 2005, 2006 Joe Walnes.
 * Copyright (C) 2006, 2007, 2008 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 07. March 2004 by Joe Walnes
 */
package com.thoughtworks.xstream.io.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.vtradex.wms.server.service.annotation.XStreamCDATA;
import com.vtradex.wms.server.service.annotation.XmlDataNote;


public class DomDriver extends AbstractXmlDriver {

    private final String encoding;
    private final DocumentBuilderFactory documentBuilderFactory;

    /**
     * Construct a DomDriver.
     */
    public DomDriver() {
        this(null);
    }

    /**
     * Construct a DomDriver with a specified encoding. The created DomReader will ignore any
     * encoding attribute of the XML header though.
     */
    public DomDriver(String encoding) {
        this(encoding, new XmlFriendlyReplacer());
    }

    /**
     * @since 1.2
     */
    public DomDriver(String encoding, XmlFriendlyReplacer replacer) {
        super(replacer);
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        this.encoding = encoding;
    }

    public HierarchicalStreamReader createReader(Reader xml) {
        return createReader(new InputSource(xml));
    }

    public HierarchicalStreamReader createReader(InputStream xml) {
        return createReader(new InputSource(xml));
    }

    private HierarchicalStreamReader createReader(InputSource source) {
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            if (encoding != null) {
                source.setEncoding(encoding);
            }
            Document document = documentBuilder.parse(source);
            return new DomReader(document, xmlFriendlyReplacer());
        } catch (FactoryConfigurationError e) {
            throw new StreamException(e);
        } catch (ParserConfigurationException e) {
            throw new StreamException(e);
        } catch (SAXException e) {
            throw new StreamException(e);
        } catch (IOException e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamWriter createWriter(Writer out) {
//        return new PrettyPrintWriter(out, xmlFriendlyReplacer());
    	return new PrettyPrintWriter(out) {
			boolean cdata = false;
			Class targetClass = null;
			Stack<Class> targetClassStack = new Stack<Class>();
			int thisDepth = 1;
			@SuppressWarnings("unchecked")
			public void startNode(String name,Class clazz) {
				super.startNode(name, clazz);
				if (depth < thisDepth) {
					targetClassStack.pop();
				}
				thisDepth = depth;
				XmlDataNote xmlDataNote = (XmlDataNote)clazz.getAnnotation(XmlDataNote.class);
				if(xmlDataNote != null){
					targetClassStack.push(clazz);
				}
				targetClass = targetClassStack.lastElement();
				cdata = needCDATA(targetClass, name);
			}

			protected void writeText(QuickWriter writer, String text) {
				if (cdata) {
					writer.write("<![CDATA[" + text + "]]>");
				} else {
					writer.write(text);
				}
			}
		};
    }

    public HierarchicalStreamWriter createWriter(OutputStream out) {
        try {
            return createWriter(encoding != null
                ? new OutputStreamWriter(out, encoding)
                : new OutputStreamWriter(out));
        } catch (UnsupportedEncodingException e) {
            throw new StreamException(e);
        }
    }
    
    private static boolean needCDATA(Class<?> targetClass, String fieldAlias){
    	boolean cdata = false;
    	cdata = existsCDATA(targetClass, fieldAlias);
    	if(cdata) return cdata;
    	if (targetClass == null) {
    		return cdata;
    	}
    	Class<?> superClass = targetClass.getSuperclass();
    	while(!superClass.equals(Object.class)){
	    	cdata = existsCDATA(superClass, fieldAlias);
	    	if(cdata) {
	    		return cdata;
	    	}
	    	superClass = superClass.getClass().getSuperclass();
    	}
    	return false;
	}
    private static boolean existsCDATA(Class<?> clazz, String fieldAlias){
    	if (clazz == null) {
    		return false;
    	}
    	Field[] fields = clazz.getDeclaredFields();
    	for (Field field : fields) {
	    	if(field.getAnnotation(XStreamCDATA.class) != null ){
		    	XStreamAlias xStreamAlias = field.getAnnotation(XStreamAlias.class);
		    	if(null != xStreamAlias){
			    	if(fieldAlias.equals(xStreamAlias.value())) {
			    		return true;
			    	}
		    	}else{
			    	if(fieldAlias.equals(field.getName())) {
			    		return true;
			    	}
		    	}
	    	}
    	}
    	return false;
	}
}