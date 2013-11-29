package de.gravitex.test;

import org.activiti.engine.delegate.DelegateExecution;

public class ProcessServerUtil {

	public static void debugDelegateVariables(DelegateExecution execution) {
		for (String variableName : execution.getVariableNames()) {
			System.out.println("variable ["+variableName+"] = " + execution.getVariable(variableName));
		}
	}
}
