package de.gravitex.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

public class TestProcessEngine extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private ProcessEngine processEngine;
	
	private JButton btnFinishTask;
	
	public TestProcessEngine(String processName, String processId) {
		super();
		initProcessEngine(processName, processId);
		setLayout(new BorderLayout());
		//---
		btnFinishTask = new JButton("finish");
		btnFinishTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				finishNextTask();
			}
		});
		add(btnFinishTask, BorderLayout.SOUTH);
		//---
		setSize(800, 600);
		setVisible(true);
	}

	private void initProcessEngine(String processName, String processId) {
		// get process engine
		processEngine = ProcessEngines.getDefaultProcessEngine();
		// deploy same process
		RepositoryService repositoryService = processEngine.getRepositoryService();
		startProcessInstance(processEngine, repositoryService, processName, processId);
	}

	private void finishNextTask() {
		Task task1 = getNextTaskToDo("management");
		if (task1 != null) {
			Map<String, Object> taskVariables = new HashMap<String, Object>();
			taskVariables.put("vacationApproved", "true");
			taskVariables.put("managerMotivation", "We have a tight deadline!");
			processEngine.getTaskService().complete(task1.getId(), taskVariables);
			System.out.println(task1.getName() + " completed.");
		} else {
			System.out.println("no further task available!");
		}
	}

	private void startProcessInstance(ProcessEngine processEngine, RepositoryService repositoryService, String processName, String processId) {
		
		repositoryService.createDeployment().addClasspathResource("de/gravitex/testdefinitions/"+processName+".bpmn20.xml").deploy();
		//start process instance
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("employeeName", "Kermit");
		variables.put("numberOfDays", new Integer(4));
		variables.put("vacationMotivation", "I'm really tired!");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		runtimeService.startProcessInstanceByKey(processId, variables);
	}

	private Task getNextTaskToDo(String groupName) {
		TaskService taskService = processEngine.getTaskService();
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(groupName).list();
		if ((tasks != null) && (tasks.size() > 0)) {
			return tasks.get(0);
		}
		return null;
	}
	
	//---
	
	public static void main(String[] args) {
//		new TestProcessEngine("SimpleVacationRequest", "vacationRequest");
		new TestProcessEngine("VacationRequest", "vacationRequest");
//		new TestProcessEngine("ExecutionListenersProcess", "executionListenersProcess");
	}
}
