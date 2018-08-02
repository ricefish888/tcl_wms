package com.vtradex.wms.server.model.entity.order;

/**
 * 
 * 确认状态
 * 
 * @author <a href="zhen.lei@vtradex.com">Yogurt_lei</a>
 * 
 * @date 2017年7月11日 下午5:28:07
 */
public interface ConfirmStatus {
    /**
     * 打开
     */
    String OPEN = "OPEN";
    /**
     * 已确认
     */
    String CONFIRM = "CONFIRM";
    /**
     * 已接收
     */
    String RECEIVED = "RECEIVED";
}
