package de.gravitex.test;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import de.gravitex.test.RMIConstants;

public class ProcessServer {

	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
		ProcessServerImpl impl = new ProcessServerImpl();
		Registry registry = LocateRegistry.createRegistry(RMIConstants.RMI_PORT);
		registry.bind(RMIConstants.RMI_ID, impl);
		System.out.println("process server is started...");
	}
}
