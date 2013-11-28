package de.gravitex.test.gui.component;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import de.gravitex.test.ProcessVariableDTO;

public class VariablesTable extends JTable {

	private static final long serialVersionUID = 1L;
	
	private List<ProcessVariableDTO> variablesModel;
	
	public VariablesTable() {
		super();
	}
	
	public void setData(List<ProcessVariableDTO> variables) {
		this.variablesModel = variables;
		Object[][] rowData = new Object[variables.size()][3];
		Object[] singleRow = null;
		int rowIndex = 0;
		for (ProcessVariableDTO variable : variables) {
			singleRow = new Object[] {variable.getFieldName(), variable.getFieldValue(), variable.getFieldClass()};
			rowData[rowIndex] = singleRow;
			rowIndex++;
		}
		Object[] columnNames = { "Feldname", "Feldwert", "Klasse" };
		DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
		setModel(model);
	}
}
