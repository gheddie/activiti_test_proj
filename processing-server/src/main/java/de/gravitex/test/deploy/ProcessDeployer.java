package de.gravitex.test.deploy;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;

import de.gravitex.test.ProcessEngineFactory;

public class ProcessDeployer {

	private ProcessEngine processEngine;

	protected ProcessDeployer() {
		super();
		initProcessEngine();
	}

	private void initProcessEngine() {
		processEngine = ProcessEngineFactory.getDefaultProcessEngine();
	}

	private void deploy() {
//		deploy("de/gravitex/testdefinitions/TimerStart.bpmn20.xml");
//		deploy("de/gravitex/testdefinitions/TimerBoundaryTest.bpmn20.xml");
//		deploy("de/gravitex/testdefinitions/JobAppliance.bpmn20.xml");
//		deploy("de/gravitex/testdefinitions/reviewSalesLead.bpmn20.xml");
//		deploy("de/gravitex/testdefinitions/MessagingTest.bpmn20.xml");
//		deploy("de/gravitex/testdefinitions/MessagingTestStarter.bpmn20.xml");
		deploy("de/gravitex/testdefinitions/JobAppliancePooled.bpmn20.xml");
	}

	private void deploy(String deploymentPath) {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource(deploymentPath).deploy();
	}

	// ---

	public static void main(String[] args) {
		new ProcessDeployer().deploy();
	}
}
