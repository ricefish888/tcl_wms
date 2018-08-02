package com.vtradex.edi.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import com.vtradex.edi.server.utils.GlobalParamUtils;

public abstract class AbstractTaskHelper implements ApplicationListener {
	
	protected final Log logger = LogFactory.getLog(this.getClass());

	/** 服务是否启动 */
	protected boolean isApplicationAvailable = false;

	/** 线程默认休息时间 10s */
	protected int refreshInterval = 10000;

	/** 休息间隔间执行的条数 */
	protected int waitingThreshold = 25;
	
	/** 线程默认处理量 */
	protected int defaultProcessCount = 50;
	
	public int getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(int refreshInterval) {
		this.refreshInterval = refreshInterval;
	}


	public int getWaitingThreshold() {
		return waitingThreshold;
	}


	public void setWaitingThreshold(int waitingThreshold) {
		this.waitingThreshold = waitingThreshold;
	}


	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextClosedEvent) {
			logger.info("Context closing......................");
			isApplicationAvailable = false;
		} else if (event instanceof ContextRefreshedEvent) {
			logger.info("Context starting......................");
			isApplicationAvailable = true;
		}
	}
	
	
	public int getRefreshInterval(String id){
		int num = GlobalParamUtils.getGloableIntegerValue(id);
		return num < 1000 ? refreshInterval : num;
	}
	
	public int getDefaultProcessCount() {
		return defaultProcessCount;
	}

	public int getDefaultProcessCount(String globalId) {
		int num = GlobalParamUtils.getGloableIntegerValue(globalId);
		return num < 50 ? defaultProcessCount : num;
	}

	public void setDefaultProcessCount(int defaultProcessCount) {
		this.defaultProcessCount = defaultProcessCount;
	}
}
