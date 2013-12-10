package de.gravitex.test.action;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class AnnounceReviewEscalation implements JavaDelegate {

	public void execute(DelegateExecution arg0) throws Exception {
		System.out.println(" ### announcing review escalation for key : " + arg0.getVariable("applianceKey"));		
	}
}
