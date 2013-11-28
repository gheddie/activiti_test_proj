package de.gravitex.test.gui;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.activiti.engine.task.Task;

public class ProcessTable extends JTable {

	private static final long serialVersionUID = 1L;

	public void setData(List<Task> tasks) {
		Object[][] rowData = new Object[tasks.size()][2];
		Object[] singleRow = null;
		int rowIndex = 0;
		for (Task task : tasks) {
			singleRow = new Object[] {task.getName(), task.getProcessInstanceId()};
			rowData[rowIndex] = singleRow;
			rowIndex++;
		}
		Object[] columnNames = { "Name der Aufgabe", "Prozess-ID" };
		DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
		setModel(model);
	}
}
