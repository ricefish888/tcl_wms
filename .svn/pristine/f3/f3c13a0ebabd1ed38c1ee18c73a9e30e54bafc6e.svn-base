package com.vtradex.wms.server.model.task;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;
/**
 * @Description:   定时任务日志
 * @Author:        <a href="xuyan.xia@vtradex.net">夏绪焰</a>
 * @CreateDate:    Nov 1, 2012 3:15:07 PM
 * @version:       v1.0
 */
public class CrontabLog extends Entity {
    
    private static final long serialVersionUID = 1321671594091008839L;

    /**定时任务名称*/
    private String crontabName; 
    
    /**任务开始时间*/
    private Date beginTime;
    
    /**任务结束时间*/
    private Date endTime;
    
    /**结果 成功失败*/
    private String result;
    
    /**备注 失败则填写异常内容*/
    private String remark;

    public String getCrontabName() {
        return crontabName;
    }

    public void setCrontabName(String crontabName) {
        this.crontabName = crontabName;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
}
