package de.gravitex.test;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

public class ProcessServerImpl extends UnicastRemoteObject implements ProcessServerRemote {

	private static final long serialVersionUID = 1L;
	
	private ProcessEngine processEngine;

	protected ProcessServerImpl() throws RemoteException {
		super();
		initProcessEngine();
//		prepareH2dB();
	}
	
	private void prepareH2dB() {
		
		//deployments
//		triggerDeployment("VacationRequest");
//		triggerDeployment("JobAppliance");
		triggerDeployment("FinancialReport");
		
		IdentityService identityService = processEngine.getIdentityService();
		
		//users
//		identityService.newUser("stefan");
		
		//users groups				
		//...
	}

	private void triggerDeployment(String processName) {
		processEngine.getRepositoryService().createDeployment().addClasspathResource("de/gravitex/testdefinitions/"+processName+".bpmn20.xml").deploy();
	}

	private void initProcessEngine() {
		processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
				  .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
				  .setJdbcUrl("jdbc:postgresql://localhost/activiti_test")
				  .setJdbcUsername("postgres")
				  .setJdbcPassword("pgvedder")
				  .setJobExecutorActivate(false)
				  .setDatabaseSchemaUpdate("false")
				  .buildProcessEngine();
		System.out.println("init process engine : " + processEngine);
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

	public void claimTask(String taskId, String userId) throws RemoteException {
		processEngine.getTaskService().claim(taskId, userId);
	}
	
	public List<ProcessDefinition> queryDeployedProcessDefinitions() throws RemoteException {
		return processEngine.getRepositoryService().createProcessDefinitionQuery().list();
	}
	
	public List<Group> queryUserGroups() throws RemoteException {
		return processEngine.getIdentityService().createGroupQuery().list();
	}
	
	public List<User> queryUsersByGroup(String groupName) throws RemoteException {
		return processEngine.getIdentityService().createUserQuery().memberOfGroup(groupName).list();
	}
	
	public List<User> queryUsersById(String userId) throws RemoteException {
		System.out.println("querying users by id '"+userId+"'...");
		List<User> userList = processEngine.getIdentityService().createUserQuery().userId(userId).list();
		if (userList != null) {
			System.out.println("queried "+userList.size()+" users by id '"+userId+"'.");
		} else {
			System.out.println("queried NO users by id '"+userId+"'.");
		}
		return userList;
	}

	public List<Task> queryTasksByUser(User user) throws RemoteException {
		return processEngine.getTaskService().createTaskQuery().taskAssignee(user.getId()).list();
	}
}
