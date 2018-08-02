package com.vtradex.wms.webservice.utils;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.util.EncodingUtils;
import com.vtradex.wms.server.utils.DateUtil;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.StringHelper;

public class CommonHelper {
	
	
	public static final String VTRADEX_SPLIT_STR="#VDX#";
    /**
     * @Description:   递归获取错误信息 传入一个excpetion
     * @Author:        <a href="xuyan.xia@vtradex.net">夏绪焰</a>
     * @CreateDate:    Nov 19, 2012 3:08:37 PM
     * @param e
     * @return:
     */
    public static String getErrorMessageByException(Throwable e) {
        if(e==null) {
            return null;
        }
        if(e.getMessage()==null && e.getCause()!=null) {
            return getErrorMessageByException(e.getCause());
        }
        return e.getMessage();
    }
    
    /**长度不足n在字符串str后补c*/
    public static String addCharAfterStr(String str,int length,String c) {
        return addCharToStr(str, length, c, false);
    }
    
    /**长度不足n在字符串str前补c*/
    public static String addCharBeforeStr(String str,int length,String c) {
        return addCharToStr(str, length, c, true);
    }
    /**长度不足n在字符串str前面后者后面补c
     * before 为true在前面  false在后面
     * */
    private static String addCharToStr(String str,int length,String c,boolean before) {   
        if(length<=0) {
            return str;
        }
        if(StringHelper.isNullOrEmpty(c) || c.length()!=1) {
            throw new BusinessException("参数错误");
        }
        str = StringHelper.replaceNullToEmpty(str);
        while(str.length()<length) {
            if(before) {
                str = c + str;
            }
            else {
                str = str + c;
            }
        }
        return str;
    }
    
    
    private static Integer ENCODE_ADD_ROUND_NUM_LENGTH=3;
    /**TCL自定义base64加密*/
    public static String tclBase64Encode(String str) {
    	if(StringHelper.isNullOrEmpty(str)) {
    		return null;
    	}
    	String str1 = EncodingUtils.encode(str.getBytes());
    	str1 = getRandNum(ENCODE_ADD_ROUND_NUM_LENGTH)+str1+getRandNum(ENCODE_ADD_ROUND_NUM_LENGTH);
    	return EncodingUtils.encode(str1.getBytes());
    }
    /**TCL自定义base64解密*/
    public static String tclBase64Decode(String str)  {
    	if(StringHelper.isNullOrEmpty(str)) {
    		return null;
    	}
    	try {
	    	String str1 = new String(EncodingUtils.decode(str));
	    	str1=str1.substring(ENCODE_ADD_ROUND_NUM_LENGTH, str1.length());
	    	str1=StringHelper.substring(str1,str1.length()-ENCODE_ADD_ROUND_NUM_LENGTH);
	    	return new String(EncodingUtils.decode(str1));
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		throw new BusinessException("解密出错:"+str);
    	}
    }
    /**获取随机数字*/
    public static String getRandNum(int length) {
    	Random r = new Random();
        String result = "";
        for(int i=0;i<length;i++) {
        	 result=result+r.nextInt(9);
        }
        return result; 
    }
    
    /**处理double的误差*/
    public static Double dealDoubleError(Double d) {
        if(d==null) {
            return null;
        }
        return DoubleUtils.formatByPrecision(d, 8); //小数相减出现无限循环 比如 301.9d - 211.5d; 保留小数后8位
    }
    
    public static void log(String info) {
    	String datestr = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss:SSS");
    	System.out.println(datestr+"---"+info);
    }
    
    public static String trim(String str) {
    	if(StringHelper.isNullOrEmpty(str)) {
    		return str;
    	}
    	return str.trim();
    }
    
    public static void main(String[] args) throws IOException {
		String a ="1234567890";//TVRJek5EWT0wNzIx  TVRJek5EWT00MjQ2
		String m = tclBase64Encode(a);
		String q = tclBase64Decode(m);
		System.out.println(m);
		System.out.println(q);
		System.out.println(q.equals(a));
	}
    /**打印当前方法被调用的堆栈信息*/
    public static void printStackTrace(String keyInfo) {
    	try {
    		String datestr = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss:SSS");
    		Exception e = new Exception(datestr+":为了打印堆栈信息而抛出的虚假异常:"+keyInfo);
    		e.printStackTrace();
    	}
    	catch(Exception ee) {
    		ee.printStackTrace();
    	}
    }
    
}
