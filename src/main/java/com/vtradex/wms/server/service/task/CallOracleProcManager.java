package com.vtradex.wms.server.service.task;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
/**
 * @Description:   调用oracle 存储过程专用 主要用于defaultcrontabmanager来调用oracle的过程做定时任务
 * @Author:        <a href="xuyan.xia@vtradex.net">夏绪焰</a>
 * @CreateDate:    Nov 12, 2012 3:36:34 PM
 * @version:       v1.0
 */
public interface CallOracleProcManager extends BaseManager {
	
	/**供应商授权导出*/
	@Transactional
	String INSERT_SUP_EXP_PER(final Long supUserId);
    
    /**
     * @Description:   调用MOVE_THORN_TASK过程 将FINISH的task移动到备份表
     * @Author:        <a href="xuyan.xia@vtradex.net">夏绪焰</a>
     * @CreateDate:    Nov 21, 2012 2:33:28 PM
     * @return:
     */
    @Transactional
    String MOVE_THORN_TASK();
    
    /**初始化库存日结*/
    @Transactional
    String INIT_STORAGE_DAILY();
    
//    /**
//     * 库存预警给采购员 并发短信
//     * */
//    @Transactional
//    String ITEM_SAFEINVENTORY_PRO();


}
