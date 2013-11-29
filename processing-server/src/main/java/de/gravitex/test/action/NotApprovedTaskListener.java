package de.gravitex.test.action;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class NotApprovedTaskListener implements TaskListener {

	private static final long serialVersionUID = 1L;

	public void notify(DelegateTask delegate) {
		System.out.println("Sorry, "+delegate.getVariable("employeeName")+", your vation request request for "+delegate.getVariable("numberOfDays")+" days was not approved...");
	}
}
