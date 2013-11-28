package de.gravitex.test;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
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

	public void startProcessInstance(String processName, String processId) throws RemoteException {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource("de/gravitex/testdefinitions/"+processName+".bpmn20.xml").deploy();
		//start process instance
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("employeeName", "Kermit");
		variables.put("numberOfDays", new Integer(4));
		variables.put("vacationMotivation", "I'm really tired!");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		runtimeService.startProcessInstanceByKey(processId, variables);
		
		System.out.println("process instance '"+processId+"' started.");
	}
	
	public void completeTask(String taskId, Map<String, Object> taskVariables) throws RemoteException {
		System.out.println("completing task '"+taskId+"'...");
		processEngine.getTaskService().complete(taskId, taskVariables);
	}
	
	public List<Task> getTasksForUserGroup(String groupName) throws RemoteException {
		return processEngine.getTaskService().createTaskQuery().taskCandidateGroup(groupName).list();
	}
}
