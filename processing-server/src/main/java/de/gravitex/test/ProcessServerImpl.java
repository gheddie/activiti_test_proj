package de.gravitex.test;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.task.Task;

public class ProcessServerImpl extends UnicastRemoteObject implements ProcessServerRemote {

	private static final long serialVersionUID = 1L;
	
	private ProcessEngine processEngine;

	protected ProcessServerImpl() throws RemoteException {
		super();
		initProcessEngine();
	}
	
	private void initProcessEngine() {
		processEngine = ProcessEngines.getDefaultProcessEngine();
	}

	public void startProcessInstance(String processName, String processId, HashMap<String, Object> variables) throws RemoteException {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource("de/gravitex/testdefinitions/"+processName+".bpmn20.xml").deploy();
		//start process instance
		processEngine.getRuntimeService().startProcessInstanceByKey(processId, variables);
		System.out.println("process instance '"+processId+"' started.");
	}
	
	public void completeTask(String taskId, Map<String, Object> taskVariables) throws RemoteException {
		System.out.println("completing task '"+taskId+"'...");
		if (taskVariables != null) {
			System.out.println("provided variables:");
			int index = 0;
			for (String key: taskVariables.keySet()) {
				System.out.println("["+index+"] "+key+"='"+taskVariables.get(key)+"'");
				index++;
			}
		} else {
			System.out.println("provided variables:NONE!!");
		}
		processEngine.getTaskService().complete(taskId, taskVariables);
	}
	
	public List<Task> getTasksForUserGroup(String groupName) throws RemoteException {
		return processEngine.getTaskService().createTaskQuery().taskCandidateGroup(groupName).list();
	}

	public List<Task> getAllTasks() throws RemoteException {
		return processEngine.getTaskService().createTaskQuery().list();
	}
}
