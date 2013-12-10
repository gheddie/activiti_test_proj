package de.gravitex.test.action;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class PrepareMissedAppointments implements JavaDelegate {

	public void execute(DelegateExecution execution) throws Exception {
		execution.setVariable("missedAppointmentCounter", new Integer(0));
	}
}
