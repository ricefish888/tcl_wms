package com.vtradex.edi.server.service.task;

import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.service.BaseManager;

/**
 * @author <a href="mailto:yan.li@vtradex.com">李炎</a>
 * @description 
 */
public interface TaskManager extends BaseManager {
	
	void doHandle(Task task) throws Exception;
}
