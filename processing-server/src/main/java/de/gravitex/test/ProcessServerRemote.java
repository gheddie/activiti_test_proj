package de.gravitex.test;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

public interface ProcessServerRemote extends Remote {

	public void startProcessInstance(String processDefinitionKey, HashMap<String, Object> processVariables) throws RemoteException;
	
	public void completeTask(String taskId, Map<String, Object> taskVariables) throws RemoteException;
	
	public void claimTask(String taskId, String userName) throws RemoteException;
	
	public List<ProcessDefinition> queryDeployedProcessDefinitions() throws RemoteException;
	
	public List<Group> queryUserGroups() throws RemoteException;
	
	public List<User> queryUsersByGroup(String groupName) throws RemoteException;
	
	public List<User> queryUsersById(String userId) throws RemoteException;
	
	public List<Task> queryGroupTasks(User user) throws RemoteException;

	public List<Task> queryTasksByUser(User user) throws RemoteException;
}
