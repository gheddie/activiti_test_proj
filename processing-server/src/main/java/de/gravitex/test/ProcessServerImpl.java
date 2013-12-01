package de.gravitex.test;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

public class ProcessServerImpl extends UnicastRemoteObject implements ProcessServerRemote {

	private static final long serialVersionUID = 1L;
	
	private ProcessEngine processEngine;

	protected ProcessServerImpl() throws RemoteException {
		super();
		initProcessEngine();
		triggerDeployments();
	}
	
	private void triggerDeployments() {
		triggerDeployment("VacationRequest");
		triggerDeployment("JobAppliance");
	}

	private void triggerDeployment(String processName) {
		processEngine.getRepositoryService().createDeployment().addClasspathResource("de/gravitex/testdefinitions/"+processName+".bpmn20.xml").deploy();
	}

	private void initProcessEngine() {
		processEngine = ProcessEngines.getDefaultProcessEngine();
	}

	public void startProcessInstance(String processDefinitionKey, HashMap<String, Object> processVariables) throws RemoteException {
		processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey, processVariables);
		System.out.println("process instance '"+processDefinitionKey+"' started.");
	}
	
	public void completeTask(String taskId, Map<String, Object> taskVariables) throws RemoteException {
		System.out.println("completing task '"+taskId+"'...");
		if ((taskVariables != null) && (taskVariables.size() > 0)) {
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

	public void claimTask(String taskId, String userName) throws RemoteException {
		// TODO claim a task before executing it!!
	}
	
	public List<ProcessDefinition> queryDeployedProcessDefinitions() throws RemoteException {
		return processEngine.getRepositoryService().createProcessDefinitionQuery().list();
	}
}
