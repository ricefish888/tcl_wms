package com.vtradex.wms.webservice.utils;
/***
 * 判断是否是接口机
 */
public class ServerUtil {
    
    /**判断是否为接口机 true为接口机  false为非接口机
     * 
     *需要在tomcat的catalina.sh中增加 -Dvtradex_edi=1
     *比如
     * JAVA_OPTS='-server  -Xms1024m -Xmx4096m -XX:PermSize=1024M -XX:MaxPermSize=1024M -Dvtradex_edi=1'
     * 
     * */
    public static boolean isEdiServer() {
        try {
//            return true;
            String temp = System.getProperty("vtradex_edi","0");
            if("1".equals(temp)){ 
                return true ;   
            }
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }
    /**判断是否为应用的节点 true为应用节点  false则不是*/
    public static boolean isAppServer() {
        try {
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}
