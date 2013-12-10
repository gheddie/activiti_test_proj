package de.gravitex.test.action;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class IncrementMissedAppointments implements JavaDelegate {

	public void execute(DelegateExecution execution) throws Exception {
		Integer missedAppointments = (Integer) execution.getVariable("missedAppointmentCounter");
		missedAppointments += 1;
		execution.setVariable("missedAppointmentCounter", missedAppointments);		
	}
}
