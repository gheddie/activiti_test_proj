package de.gravitex.test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import de.gravitex.test.RMIConstants;
import de.gravitex.test.ProcessServerRemote;

public class ProcessingTestClient {

	public static void main(String[] args) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry("localhost", RMIConstants.RMI_PORT);
		ProcessServerRemote remote = (ProcessServerRemote) registry.lookup(RMIConstants.RMI_ID);
		
//		remote.completeTask(null, null, "accountancy");
		remote.completeTask(null, null, "management");
	}
}
