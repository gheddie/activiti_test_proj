package de.gravitex.test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import de.gravitex.test.gui.login.ProcessLogin;

public class ProcessingTestClientStarter {

	public static void main(String[] args) throws RemoteException, NotBoundException {
		new ProcessLogin().setVisible(true);
	}
}
