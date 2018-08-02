package com.vtradex.wms.server.service.task.pojo;

import java.util.Date;

import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.task.CrontabLog;
import com.vtradex.wms.server.service.task.CrontabLogManager;
import com.vtradex.wms.server.utils.StringHelper;
/**
 * @Description:   定时任务日志
 * @Author:        <a href="xuyan.xia@vtradex.net">夏绪焰</a>
 * @CreateDate:    May 31, 2012 3:30:48 PM
 * @version:       v1.0
 */
public class DefaultCrontabLogManager extends DefaultBaseManager implements CrontabLogManager {
    
    /**保存定时任务日志*/
    public Long storeCrontabLog(Date beginTime, String crontabName,  String result, String remark) {
        CrontabLog crontabLog = EntityFactory.getEntity(CrontabLog.class);
        crontabLog.setBeginTime(beginTime);
//        crontabLog.setEndTime(new Date());
        crontabLog.setCrontabName(crontabName);
        crontabLog.setResult(result);
        crontabLog.setRemark(StringHelper.substring(remark,2000));
         commonDao.store(crontabLog);
         return crontabLog.getId();
    }
    public Long storeCrontabRunningLog(Date beginTime, String crontabName) {
    	return storeCrontabLog(beginTime, crontabName, "运行中", null);
    }
    
    /**保存定时任务成功日志*/
    public Long storeCrontabSuccessLog(Date beginTime, String crontabName) {
        return storeCrontabLog(beginTime, crontabName, "成功", null);
    }
    
    /**保存定时任务失败日志*/
    public Long storeCrontabErrorLog(Date beginTime, String crontabName, String remark) {
        if(StringHelper.isNullOrEmpty(remark)) {
            remark = "空指针错误";
        }
        return storeCrontabLog(beginTime, crontabName, "失败", remark);
    }
    
    public Long updateCrontabLogToError(Long crontablogid,String remark) {
    	 CrontabLog crontabLog = commonDao.load(CrontabLog.class,crontablogid);
    	 crontabLog.setEndTime(new Date());
    	 crontabLog.setResult("失败");
    	 crontabLog.setRemark(StringHelper.substring(remark,2000));
    	 commonDao.store(crontabLog);
    	 return crontabLog.getId();
    }
    
    public Long updateCrontabLogToSucess(Long crontablogid) {
   	 CrontabLog crontabLog = commonDao.load(CrontabLog.class,crontablogid);
   	 crontabLog.setEndTime(new Date());
   	 crontabLog.setResult("成功");
   	 crontabLog.setRemark(null);
   	 commonDao.store(crontabLog);
   	 return crontabLog.getId();
   }
}
