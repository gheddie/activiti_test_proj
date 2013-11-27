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
		
//		System.out.println(remote.isLoginValid("peter.pan"));
//		System.out.println(remote.isLoginValid("sdf�lksdf"));
//		System.out.println(remote.isLoginValid(""));
//		System.out.println(remote.isLoginValid(null));
		
		remote.completeTask(null, null);
	}
}
