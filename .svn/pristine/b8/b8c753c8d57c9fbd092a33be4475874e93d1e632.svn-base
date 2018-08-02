package com.vtradex.edi.server.service.task.pojo;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import com.vtradex.edi.server.service.task.TaskManager;
import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;

/**
 * @author <a href="mailto:yan.li@vtradex.com">李炎</a>
 * @description 
 */

public class DefaultTaskManager extends DefaultBaseManager implements TaskManager,ApplicationContextAware{

	public void doHandle(Task task) throws Exception {
		doHandleSubscriber(task.getSubscriber(),task.getMessageId());
	}
	
	protected void doHandleSubscriber(String type,Long id) throws Exception{
		String managerName = StringUtils.substringBefore(type,".");
		String methodName = StringUtils.substringAfter(type,".");
		Object[] params = new Object[]{id};
		Object manager = applicationContext.getBean(managerName);
		Assert.notNull(manager,type + " message not found managerName is not find " + managerName);
		Method method = getExactlyMethod(manager,methodName,params);
		Assert.notNull(method,type + " message not found methodName is not find  " + methodName);
		method.invoke(manager,params);
	}

	private Method getExactlyMethod(Object manager, String methodName, Object...args) throws NoSuchMethodException {
		logger.warn("The args passing to  Manager [" + manager + "] method [" + methodName + "] contains null arg(s)");
		for(Method method : manager.getClass().getMethods()){
			if(method.getName().equals(methodName) && method.getParameterTypes().length == args.length){
				return method;
			}
		}
		return null;
	}
}
