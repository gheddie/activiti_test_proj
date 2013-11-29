package de.gravitex.test.gui.component;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.activiti.engine.task.Task;

public class TaskTable extends JTable {

	private static final long serialVersionUID = 1L;
	
	private List<Task> taskModel;
	
	public TaskTable() {
		super();
	}

	public void setData(List<Task> tasks) {
		this.taskModel = tasks;
		Object[][] rowData = new Object[tasks.size()][3];
		Object[] singleRow = null;
		int rowIndex = 0;
		for (Task task : tasks) {
//			singleRow = new Object[] {task.getName(), "Jemanden", task.getProcessInstanceId()};
			singleRow = new Object[] {task.getName(), task.getOwner(), task.getProcessInstanceId()};
			rowData[rowIndex] = singleRow;
			rowIndex++;
		}
		Object[] columnNames = { "Name der Aufgabe", "Zugewiesen an", "Prozess-ID" };
		DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
		setModel(model);
	}

	public Task getSelectedTask() {
		return taskModel.get(getSelectedRow());
	}
}