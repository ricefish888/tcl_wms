package com.vtradex.wms.server.service.task;

import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.task.CrontabLog;
/**
 * @Description:   定时任务日志
 * @Author:        <a href="xuyan.xia@vtradex.net">夏绪焰</a>
 * @CreateDate:    May 31, 2012 3:29:46 PM
 * @version:       v1.0
 */
public interface CrontabLogManager extends BaseManager {
    
    /**保存定时任务日志*/
    @Transactional
    Long storeCrontabLog(Date beginTime, String crontabName,  String result, String remark);
    
    /**保存定时任务成功日志*/
    @Transactional
    Long storeCrontabSuccessLog(Date beginTime, String crontabName);
    
    /**保存定时任务失败日志*/
    @Transactional
    Long storeCrontabErrorLog(Date beginTime, String crontabName, String remark);
    
    /**保存定时任务执行中的日志*/
    @Transactional
    Long storeCrontabRunningLog(Date beginTime, String crontabName);
    
    /***/
    @Transactional
    Long updateCrontabLogToError(Long crontablogid,String remark);
    
    /***/
    @Transactional
    Long updateCrontabLogToSucess(Long crontablogid);
}
