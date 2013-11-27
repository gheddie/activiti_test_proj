package de.gravitex.test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import org.activiti.engine.task.Task;

public class ProcessingTestClient {

	public static void main(String[] args) throws RemoteException, NotBoundException {
		
		Registry registry = LocateRegistry.getRegistry("localhost", RMIConstants.RMI_PORT);
		ProcessServerRemote processServer = (ProcessServerRemote) registry.lookup(RMIConstants.RMI_ID);
		
//		processServer.startProcessInstance("VacationRequest", "vacationRequest");
//		processServer.startProcessInstance("SimpleVacationRequest", "vacationRequest");
//		processServer.startProcessInstance("FinancialReport", "financialReport");

		List<Task> openTasks = processServer.getTaskyForUserGroup("management");
		System.out.println(openTasks.size() + " open tasks.");
		for (Task task : openTasks) {
			System.out.println(task.getName() + "["+task.getId()+"]");
		}
		
		//---
		
//		processServer.completeTask("25", null);
	}
}
