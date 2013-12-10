package de.gravitex.test.action;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class AnnounceOperationalMeeting implements JavaDelegate {

	public void execute(DelegateExecution arg0) throws Exception {
		System.out.println(" ### announcing operational meeting for key : " + arg0.getVariable("applianceKey"));
	}
}
