package de.gravitex.test.core;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

import de.gravitex.test.ProcessServerRemote;
import de.gravitex.test.RMIConstants;

public class ProcessingClientSingleton {

	private static ProcessingClientSingleton instance = null;

	private static User loggedInUser;
	
	private ProcessServerRemote processServer;

	private ProcessingClientSingleton() {
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
	}

	public static ProcessingClientSingleton getInstance() {
		if (instance == null) {
			instance = new ProcessingClientSingleton();
		}
		return instance;
	}
	
	public void initProcessEngine() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry("localhost", RMIConstants.RMI_PORT);
		processServer = (ProcessServerRemote) registry.lookup(RMIConstants.RMI_ID);		
	}

	public List<ProcessDefinition> queryDeployedProcessDefinitions() throws RemoteException {
		return processServer.queryDeployedProcessDefinitions();
	}
	
	public List<Group> queryUserGroups() throws RemoteException {
		return processServer.queryUserGroups();
	}

	public void claimTask(String taskId) throws RemoteException {
		processServer.claimTask(taskId, loggedInUser.getId());
	}

	public void startProcessInstance(String key, HashMap<String, Object> processVariables) throws RemoteException {
		 processServer.startProcessInstance(key, processVariables);		
	}

	public void completeTask(String taskId, HashMap<String, Object> processVariables) throws RemoteException {
		processServer.completeTask(taskId, processVariables);
	}

	public List<Task> getAllTasks() throws RemoteException {
		return processServer.getAllTasks();
	}

	public List<Task> getTasksForUserGroup(String groupName) throws RemoteException {
		return processServer.getTasksForUserGroup(groupName);
	}

	public boolean authenticateUser(String userId, String password) throws RemoteException {
		List<User> usersById = processServer.queryUsersById(userId);
		if ((usersById == null) || (usersById.size() == 0)) {
			System.out.println("no users found for id '"+userId+"'.");
			return false;
		}
		for (User user : usersById) {
			if (user.getPassword().equals(password)) {
				loggedInUser = user;
				return true;
			}
		}
		return false;
	}

	public static User getLoggedInUser() {
		return loggedInUser;
	}

	public List<Task> queryTasksByLoggedInUser() throws RemoteException {
		return processServer.queryTasksByUser(loggedInUser);
	}
}
