package com.vtradex.edi.server.utils;

import java.util.Date;
import java.util.List;

import com.vtradex.edi.server.AbstractTaskHelper;
import com.vtradex.edi.server.service.task.TaskManager;
import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.model.message.TaskConfig;
import com.vtradex.thorn.server.model.message.TaskMonitor;
import com.vtradex.thorn.server.model.message.TaskMonitorStatus;
import com.vtradex.thorn.server.model.message.TaskStatus;
import com.vtradex.thorn.server.service.message.IMessageService;
import com.vtradex.thorn.server.util.DateUtil;

/**
 * 多线程Task处理，不建议使用
 * 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1 $Date: 2014/08/05 05:40:18 $
 */
@SuppressWarnings("unchecked")
public class TaskHelper extends AbstractTaskHelper {

	protected IMessageService messageService;
	protected TaskManager ediTaskManager;

	public TaskHelper(TaskManager ediTaskManager, IMessageService messageService) {
		this.ediTaskManager = ediTaskManager;
		this.messageService = messageService;
	}
	
	public void doHandleMail() {
		logger.error("TaskHelper[doHandleMail] started....................");
		while (true) {
			List<Long> taskIds = loadTaskIdsByTypeAndReadyStatus(Task.TASK_MAIL, this.getDefaultProcessCount());
			for (Long id : taskIds) {
				Task task = ediTaskManager.load(Task.class, id);
				try {
					ediTaskManager.doHandle(task);
					task.setFinshStatus();
				} catch (Exception e) {
					logger.error("", e);
					task.setFailStatus();
				}
				messageService.storeEntity(task);
			}
			try {
				Thread.sleep(getRefreshInterval(""));
			} catch (InterruptedException e) {
				logger.error("", e);
			}
			if (!isApplicationAvailable) {
				logger.error("TaskHelper[doHandleMail] end....................");
				break;
			}
		}
	}
	
	/**
	 * 处理任务
	 */
	public void doHandle() {
		logger.error("TaskHelper[doHandle] started....................");
		List<Object[]> taskGroups = getAllTaskGroup();
		for (Object[] tcArr : taskGroups) {
			Thread thread = new Thread(new TaskGroupRunnable(tcArr[0].toString(), Integer.valueOf(tcArr[1].toString())));
			thread.start();
		}
		if (!isApplicationAvailable) {
			logger.error("TaskHelper[doHandle] end....................");
		}
	}
	
	
	
	


	/**
	 * @param taskType 任务类型
	 * @param processCount 单次处理量
	 * @return
	 */
	private List<Long> loadTaskIdsByTypeAndReadyStatus(String taskType, int processCount) {
		String hql = "SELECT t.id FROM Task t WHERE t.nextProcessTime <= :currentDateTime and t.status=:status AND t.type = :taskType Order By t.id ASC";
		return messageService.findByQueryAndParamNum(hql,
				new String[] { "currentDateTime", "status", "taskType" }, new Object[] {new Date(), TaskStatus.STAT_READY, taskType }, processCount);
	}
	
	/**
	 * @param taskType 任务类型
	 * @return
	 */
	protected List<Long> loadTaskIdsByTypeAndReadyStatus(String taskType) {
		String hql = "SELECT t.id FROM Task t WHERE t.status=:status AND t.taskType = :taskType Order By t.id ASC";
		return messageService.findByQuery(hql,
				new String[] { "status", "taskType" }, new Object[] {TaskStatus.STAT_READY, taskType });
	}
	
	/**
	 * @param taskGroup 任务分组
	 * @param random 随机数
	 * @return
	 */
	private List<Long> loadTaskGroupByRandom(String taskGroup, int random) {
		String taskTypeHql = "SELECT tc.id FROM TaskConfig tc where tc.taskGroup = :taskGroup and tc.minThreshold <= :random and tc.maxThreshold >= :random";
		return messageService.findByQuery(taskTypeHql, 
				new String[]{"taskGroup", "random"}, new Object[]{taskGroup, random});
	}
	
	/**
	 * 一个分组线程，负责执行该分组下所有任务，执行顺序按时间片
	 */
	private class TaskGroupRunnable implements Runnable {
		
		String taskGroup;
		Integer maxThreshold; 
		
		/**
		 * @param taskGroup 分组名称
		 * @param maxThreshold 分组中最大阀值
		 */
		public TaskGroupRunnable(String taskGroup, Integer maxThreshold) {
			this.taskGroup = taskGroup;
			this.maxThreshold = maxThreshold;
		}

		public void run() {
			while (true) {
				int random = (int)(Math.random() * this.maxThreshold);
				List<Long> taskConfigIds = loadTaskGroupByRandom(taskGroup, random);
				if (taskConfigIds == null || taskConfigIds.size() <= 0) {
					try {
						Thread.sleep(TaskHelper.this.refreshInterval / 10);
					} catch (InterruptedException e) {
						logger.error("", e);
						continue;
					}
				}
				boolean flag = false;
				for (Long tcId : taskConfigIds) { // 处理任务分组
					TaskConfig taskConfig = null;
					try {
						taskConfig = ediTaskManager.load(TaskConfig.class, tcId);
					} catch (Exception ex) {
						logger.error("设置任务监控状态异常", ex);
						continue;
					}
					
					try {
						if (taskConfig.getBeEnabled()) {
							refreshTaskMonitor(TaskMonitorStatus.READY_PROCESS, taskConfig); // 更新任务监控状态
						} else {
							refreshTaskMonitor(TaskMonitorStatus.UN_ACTIVE, taskConfig); // 更新任务监控状态, 未激活的不处理 Task
							continue;
						}
					} catch (Exception ex) {
						logger.error("统计任务监控数据异常", ex);
						continue;
					}
					
					int processCount = taskConfig.getProcessCount() <= 0 ? TaskHelper.this.getDefaultProcessCount("") : taskConfig.getProcessCount();
					List<Long> taskIds = loadTaskIdsByTypeAndReadyStatus(taskConfig.getTaskType(), processCount);
					if (taskIds == null || taskIds.size() <= 0) {
						flag = true;
					}
					for (Long id : taskIds) { // 处理任务
						Task task = null;
						try {
							task = ediTaskManager.load(Task.class, id);
							task.setStartTime(new Date());
							ediTaskManager.doHandle(task);
							task.setFinshStatus();
						} catch (Exception e) {
							logger.error("", e);
							if (task != null) {
								task.setRepeatCount(task.getRepeatCount() + 1);
								Date nextProcessTime = DateUtil.increaseDate(new Date(), DateUtil.MINUTE, task.getRepeatCount());
								task.setFailStatus(nextProcessTime, taskConfig.getRepeatCount());
							}
						}
						if (task != null) {
							messageService.storeEntity(task);
						}
					}
					try {
						if (flag) {
							Thread.sleep(TaskHelper.this.getRefreshInterval() / 10);
						} else {
							Thread.sleep(TaskHelper.this.getRefreshInterval());
						}
					} catch (InterruptedException e) {
						logger.error("", e);
					}
					try {
						if (!flag) {
							refreshTaskMonitor(TaskMonitorStatus.PROCESSING, taskConfig);
						}
					} catch (Exception ex) {
						logger.error("统计任务监控数据异常", ex);
						continue;
					}
				}
			}
		}
	}
	
	/**
	 * 获取所有可用的任务分组
	 */
	private List<Object[]> getAllTaskGroup () {
		String allTaskGroupHql = "select tc.taskGroup, max(tc.maxThreshold) from TaskConfig tc group by tc.taskGroup";
		return messageService.findByQuery(allTaskGroupHql, new String[]{}, new Object[]{});
	}
	
	/**
	 * 实时刷新任务监控数据
	 * 
	 * @param monitorStatus {@link TaskMonitorStatus}
	 * @param tc {@link TaskConfig}
	 */
	private void refreshTaskMonitor(String monitorStatus, TaskConfig tc) throws Exception {
		
		String taskCurrentDateFilterHql = " and YEAR(t.createTime) = YEAR(CURRENT_DATE()) " +
				                          " and MONTH(t.createTime)=MONTH(CURRENT_DATE()) " +
				                          " and DAY(t.createTime) = DAY(CURRENT_DATE())";
		
		String taskMonitorCurrentDateFilterHql = " and YEAR(tm.monitorTime) = YEAR(CURRENT_DATE()) " +
								                 " and MONTH(tm.monitorTime)=MONTH(CURRENT_DATE()) " +
								                 " and DAY(tm.monitorTime) = DAY(CURRENT_DATE())";
		
		Integer readyProcessCount = 0;
		Integer repeatProcessCount = 0;
		Integer successCount = 0;
		Integer failCount = 0;
		
		// 完成数、失败数, 只统计当天
		String normalStatHql = "select count(t.status), t.status from Task t " +
				" where t.type = :taskType and t.status in('FINISH', 'FAIL') " + taskCurrentDateFilterHql + " group by t.status";
		List<Object[]> normalStats =  messageService.findByQuery(normalStatHql, new String[]{"taskType"}, new Object[]{tc.getTaskType()});
		if (normalStats != null && normalStats.size() > 0) {
			for (Object[] objs : normalStats) {
				if (TaskStatus.STAT_FINISH.equals(objs[1])) {
					successCount = new Integer(objs[0].toString());
				} else {
					failCount = new Integer(objs[0].toString());
				}
			}
		}
		
		// 待执行数, 统计所有
		String readyStatHql = "select count(t.id) from Task t where t.status = 'READY' and t.repeatCount = 0 and t.type = :taskType";
		List<Object> readyStats =  messageService.findByQuery(readyStatHql, new String[]{"taskType"}, new Object[]{tc.getTaskType()});
		if (readyStats != null && readyStats.size() > 0) {
			readyProcessCount = new Integer(readyStats.get(0).toString());
		}
		
		// 重试次数
		Date currentDate = new Date();
		String repeatStatHql = "select count(t.id) from Task t where t.status = 'READY' and t.repeatCount > 0 and t.type = :taskType and t.nextProcessTime <= :currentDateTime and t.repeatCount <= :defaultRepeatCount";
		List<Object> repeatStats =  messageService.findByQuery(repeatStatHql, new String[]{"taskType", "currentDateTime", "defaultRepeatCount"}, new Object[]{tc.getTaskType(), currentDate, tc.getRepeatCount()});
		if (repeatStats != null && repeatStats.size() > 0) {
			repeatProcessCount = new Integer(repeatStats.get(0).toString());
		}
		
		String taskMonitorHql = "FROM TaskMonitor tm where tm.taskGroup = :taskGroup and tm.taskType = :taskType " + taskMonitorCurrentDateFilterHql;
		List<TaskMonitor> taskMonitors = messageService.findByQuery(taskMonitorHql, new String[]{"taskGroup", "taskType"}, new Object[]{tc.getTaskGroup(), tc.getTaskType()});
		TaskMonitor taskMonitor = null;
		if (taskMonitors == null || taskMonitors.isEmpty()) {
			taskMonitor = new TaskMonitor();
			taskMonitor.setTaskGroup(tc.getTaskGroup());
			taskMonitor.setTaskType(tc.getTaskType());
		} else {
			taskMonitor = taskMonitors.get(0);
		}
		taskMonitor.setMonitorTime(currentDate);
		taskMonitor.setPriority(tc.getTimeSlice());
		taskMonitor.setReadyProcessCount(readyProcessCount);
		taskMonitor.setRepeatProcessCount(repeatProcessCount);
		taskMonitor.setSuccessCount(successCount);
		taskMonitor.setFailCount(failCount);
		taskMonitor.setStatus(monitorStatus);
		messageService.storeEntity(taskMonitor);
	}
}
