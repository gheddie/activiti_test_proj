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
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import de.gravitex.test.ProcessServerRemote;

public class ProcessServerImpl extends UnicastRemoteObject implements ProcessServerRemote {

	private static final long serialVersionUID = 1L;
	
	private ProcessEngine processEngine;

	protected ProcessServerImpl() throws RemoteException {
		super();
//		initProcessEngine("VacationRequest", "vacationRequest");
//		initProcessEngine("SimpleVacationRequest", "vacationRequest");
		initProcessEngine("FinancialReport", "financialReport");
	}
	
	private void initProcessEngine(String processName, String processId) {
		// get process engine
		processEngine = ProcessEngines.getDefaultProcessEngine();
		// deploy same process
		RepositoryService repositoryService = processEngine.getRepositoryService();
		startProcessInstance(processEngine, repositoryService, processName, processId);
	}
	
	private void startProcessInstance(ProcessEngine processEngine, RepositoryService repositoryService, String processName, String processId) {
		
		repositoryService.createDeployment().addClasspathResource("de/gravitex/testdefinitions/"+processName+".bpmn20.xml").deploy();
		//start process instance
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("employeeName", "Kermit");
		variables.put("numberOfDays", new Integer(4));
		variables.put("vacationMotivation", "I'm really tired!");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		runtimeService.startProcessInstanceByKey(processId, variables);
	}

	public boolean isLoginValid(String userName) throws RemoteException {
		if ((userName == null) || (userName.length() == 0)) {
			return false;	
		}
		return (userName.equals("peter.pan"));		
	}

	public void completeTask(String taskId, Map<String, Object> taskVariables, String groupName) throws RemoteException {
		System.out.println("completing task '"+taskId+"'...");
		Task task = getNextTaskToDo(groupName);
		if (task != null) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("vacationApproved", "false");
			parameters.put("managerMotivation", "We have a tight deadline!");
			processEngine.getTaskService().complete(task.getId(), parameters);
			System.out.println(task.getName() + " completed.");
		} else {
			System.out.println("no further task available!");
		}
	}
	
	private Task getNextTaskToDo(String groupName) {
		TaskService taskService = processEngine.getTaskService();
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(groupName).list();
		if ((tasks != null) && (tasks.size() > 0)) {
			return tasks.get(0);
		}
		return null;
	}
}
