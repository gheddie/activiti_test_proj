package de.gravitex.test.action;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class NotApprovedAction implements ExecutionListener {

	private static final long serialVersionUID = 1L;

	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("Not approved...");
	}
}
