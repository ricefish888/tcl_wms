package com.vtradex.wms.server.model.entity.item;

/**
 * 
 * JITATT属性
 * 
 * @author <a href="zhen.lei@vtradex.com">Yogurt_lei</a>
 * 
 * @date 2017年6月30日 上午10:34:30
 */
public interface WmsItemJITAtt {
	/**非JIT*/
    String NO_JIT = "NO_JIT";
    
    /**JIT上线结算*/
    String JIT_UPLINE_SETTLE = "JIT_UPLINE_SETTLE";
    
    /**JIT下线结算*/
    String JIT_DOWNLINE_SETTLE = "JIT_DOWNLINE_SETTLE";
}
