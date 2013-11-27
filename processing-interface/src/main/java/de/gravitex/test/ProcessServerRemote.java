package de.gravitex.test;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ProcessServerRemote extends Remote {

	public boolean isLoginValid(String userName) throws RemoteException;
	
	public void completeTask(String taskId, Map<String, Object> taskVariables) throws RemoteException;
}
