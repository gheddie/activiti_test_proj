package de.gravitex.test;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

public interface ProcessServerRemote extends Remote {

	public void startProcessInstance(String processName, String processId) throws RemoteException;
	
	public void completeTask(String taskId, Map<String, Object> taskVariables) throws RemoteException;
	
	public List<Task> getTaskyForUserGroup(String groupName) throws RemoteException;
}
