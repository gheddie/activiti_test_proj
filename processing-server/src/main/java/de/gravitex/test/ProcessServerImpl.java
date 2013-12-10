package de.gravitex.test;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.NativeTaskQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

public class ProcessServerImpl extends UnicastRemoteObject implements ProcessServerRemote {

	private static final long serialVersionUID = 1L;
	
	private ProcessEngine processEngine;

	protected ProcessServerImpl() throws RemoteException {
		super();
		initProcessEngine();
	}
	
	private void initProcessEngine() {
		processEngine = ProcessEngineFactory.getDefaultProcessEngine();
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
	
	public List<Task> queryGroupTasks(User user) throws RemoteException {
		List<Group> groupsByUser = processEngine.getIdentityService().createGroupQuery().groupMember(user.getId()).list();
		List<Task> groupTasks = new ArrayList<>();
		for (Group userGroup : groupsByUser) {
			groupTasks.addAll(processEngine.getTaskService().createTaskQuery().taskCandidateGroup(userGroup.getId()).list());
		}		
		return groupTasks;
	}
	
	public List<Task> queryTasksByUser(User user) throws RemoteException {
		return processEngine.getTaskService().createTaskQuery().taskAssignee(user.getId()).list();
	}

	public List<Task> queryTasksNative(String queryString, HashMap<String, Object> parameters) throws RemoteException {
		ManagementService managementService = processEngine.getManagementService();
		NativeTaskQuery qry = processEngine.getTaskService().createNativeTaskQuery().sql(queryString);
		for (String key : parameters.keySet()) {
			qry.parameter(key, parameters.get(key));
		}
		return qry.list();
	}
}
