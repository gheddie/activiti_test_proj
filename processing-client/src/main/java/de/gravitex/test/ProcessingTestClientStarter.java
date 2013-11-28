package de.gravitex.test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import de.gravitex.test.gui.ProcessTestView;

public class ProcessingTestClientStarter {

	public static void main(String[] args) throws RemoteException, NotBoundException {
		new ProcessTestView().setVisible(true);
		//---
//		ParserUtil.parseVariable("vacationApproved#true#java.lang.Boolean");
	}
}
