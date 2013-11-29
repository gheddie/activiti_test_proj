/*
 * Created by JFormDesigner on Fri Nov 29 10:21:23 CET 2013
 */

package de.gravitex.test.gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import org.activiti.engine.task.Task;

import de.gravitex.test.ProcessServerRemote;
import de.gravitex.test.ProcessVariableDTO;
import de.gravitex.test.ProcessingParserUtil;
import de.gravitex.test.RMIConstants;
import de.gravitex.test.gui.component.TaskTable;
import de.gravitex.test.gui.component.VariablesTable;

/**
 * @author User #1
 */
public class EnhancedProcessTestView extends JFrame implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	private ProcessServerRemote processServer;
	
	private Task selectedTask;
	
	private List<ProcessVariableDTO> processVariables;

	/** Die angemeldete Benutzergruppe */
	private String groupName;
	
	public EnhancedProcessTestView(String groupName) {
		this.groupName = groupName;
		initComponents();
		setSize(800, 600);
		setTitle("Prozess-Steuerung (angemeldet: "+groupName+")");
		init();
		putListeners();
		fillVariableTypes();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void fillVariableTypes() {
		ComboBoxModel model = new DefaultComboBoxModel<>(new Object[] {ProcessVariableType.BOOLEAN, ProcessVariableType.INTEGER, ProcessVariableType.LONG, ProcessVariableType.STRING});		
		cbVariableTypes.setModel(model);
	}

	private void init() {
		//process engine
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry("localhost", RMIConstants.RMI_PORT);
			processServer = (ProcessServerRemote) registry.lookup(RMIConstants.RMI_ID);
		} catch (RemoteException | NotBoundException e) {
			// e.printStackTrace();
			System.out.println("Fehler beim Initialisieren : " + e.getMessage());
		}
		//other stuff
		processVariables = new ArrayList<>();
	}
	
	private void putListeners() {
		btnStartInstance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					processServer.startProcessInstance("VacationRequest", "vacationRequest", getProcessVariables());
				} catch (RemoteException e1) {
					JOptionPane.showMessageDialog(EnhancedProcessTestView.this, "Unable to start process instance : " + e1.getMessage());
				}
			}
		});
		//---
		btnFetchTasks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillTasks();
			}
		});
		//---
		btnFinishTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedTask == null) {
					JOptionPane.showMessageDialog(EnhancedProcessTestView.this, "No task selected ---> returning.");
					return;
				}
				try {
					processServer.completeTask(selectedTask.getId(), getProcessVariables());
					fillTasks();
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(EnhancedProcessTestView.this, "Could not finish task : " + e1.getMessage());
				} finally {
					selectedTask = null;
				}
			}
		});		
		//---
		btnAddVariable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("adding variable...");
				String variableName = tfVariableName.getText();
				if ((variableName == null) || (variableName.length() == 0)) {
					JOptionPane.showMessageDialog(EnhancedProcessTestView.this, "Enter a variable name!");
					return;
				}
				String variableValue = tfVariableValue.getText();
				if ((variableValue == null) || (variableValue.length() == 0)) {
					JOptionPane.showMessageDialog(EnhancedProcessTestView.this, "Enter a variable value!");
					return;
				}
				processVariables.add(new ProcessVariableDTO(variableName, variableValue, ((ProcessVariableType) cbVariableTypes.getSelectedItem()).asClass()));
				fillVariables();
			}
		});		
		//---
		btnResetVariables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("reset variables...");
				processVariables.clear();
				fillVariables();
			}
		});		
		//---
		tbTasks.addMouseListener(this);
	}
	
	private void fillVariables() {
		tbVariables.setData(processVariables);				
	}
	
	private HashMap<String, Object> getProcessVariables() {
		HashMap<String, Object> variables = new HashMap<>();
		for (ProcessVariableDTO dto : processVariables) {
			variables.put(dto.getFieldName(), dto.getFieldValue());
		}
		return variables;
	}
	
	private void fillTasks() {
		try {
			tbTasks.setData(processServer.getTasksForUserGroup(groupName));
//			tbTasks.setData(processServer.getAllTasks());
		} catch (RemoteException e) {
			e.printStackTrace();
		}				
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		tbMain = new JToolBar();
		btnFetchTasks = new JButton();
		btnFinishTask = new JButton();
		cbProcessDefinitions = new JComboBox();
		btnStartInstance = new JButton();
		pnlTasks = new JPanel();
		scTasks = new JScrollPane();
		tbTasks = new TaskTable();
		pnlVariables = new JPanel();
		scVariables = new JScrollPane();
		tbVariables = new VariablesTable();
		pnlVariableParser = new JPanel();
		tfVariableName = new JTextField();
		tfVariableValue = new JTextField();
		cbVariableTypes = new JComboBox();
		btnAddVariable = new JButton();
		btnResetVariables = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {38, 0, 0, 0, 0};
		((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 1.0, 1.0, 0.0, 1.0E-4};

		//======== tbMain ========
		{
			tbMain.setFloatable(false);

			//---- btnFetchTasks ----
			btnFetchTasks.setText("Tasks holen");
			btnFetchTasks.setIcon(new ImageIcon(getClass().getResource("/gfx/fetch_tasks.png")));
			tbMain.add(btnFetchTasks);

			//---- btnFinishTask ----
			btnFinishTask.setText("Task abschliessen");
			btnFinishTask.setIcon(new ImageIcon(getClass().getResource("/gfx/finish_task.png")));
			tbMain.add(btnFinishTask);
			tbMain.add(cbProcessDefinitions);

			//---- btnStartInstance ----
			btnStartInstance.setText("Instanz starten");
			btnStartInstance.setIcon(new ImageIcon(getClass().getResource("/gfx/start_instance.png")));
			tbMain.add(btnStartInstance);
		}
		contentPane.add(tbMain, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//======== pnlTasks ========
		{
			pnlTasks.setBorder(new TitledBorder("Offene Aufgaben"));
			pnlTasks.setLayout(new GridBagLayout());
			((GridBagLayout)pnlTasks.getLayout()).columnWidths = new int[] {0, 0};
			((GridBagLayout)pnlTasks.getLayout()).rowHeights = new int[] {0, 0};
			((GridBagLayout)pnlTasks.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
			((GridBagLayout)pnlTasks.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

			//======== scTasks ========
			{
				scTasks.setViewportView(tbTasks);
			}
			pnlTasks.add(scTasks, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		contentPane.add(pnlTasks, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//======== pnlVariables ========
		{
			pnlVariables.setBorder(new TitledBorder("Variablen"));
			pnlVariables.setLayout(new GridBagLayout());
			((GridBagLayout)pnlVariables.getLayout()).columnWidths = new int[] {0, 0};
			((GridBagLayout)pnlVariables.getLayout()).rowHeights = new int[] {0, 0};
			((GridBagLayout)pnlVariables.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
			((GridBagLayout)pnlVariables.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

			//======== scVariables ========
			{
				scVariables.setViewportView(tbVariables);
			}
			pnlVariables.add(scVariables, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		contentPane.add(pnlVariables, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//======== pnlVariableParser ========
		{
			pnlVariableParser.setBorder(new TitledBorder("Variablen editieren"));
			pnlVariableParser.setLayout(new GridBagLayout());
			((GridBagLayout)pnlVariableParser.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0};
			((GridBagLayout)pnlVariableParser.getLayout()).rowHeights = new int[] {0, 0};
			((GridBagLayout)pnlVariableParser.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 0.0, 0.0, 1.0E-4};
			((GridBagLayout)pnlVariableParser.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};
			pnlVariableParser.add(tfVariableName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 5), 0, 0));
			pnlVariableParser.add(tfVariableValue, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 5), 0, 0));
			pnlVariableParser.add(cbVariableTypes, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 5), 0, 0));

			//---- btnAddVariable ----
			btnAddVariable.setText("Hinzuf\u00fcgen");
			btnAddVariable.setIcon(new ImageIcon(getClass().getResource("/gfx/add_variable.png")));
			pnlVariableParser.add(btnAddVariable, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 5), 0, 0));

			//---- btnResetVariables ----
			btnResetVariables.setText("Alle l\u00f6schen");
			btnResetVariables.setIcon(new ImageIcon(getClass().getResource("/gfx/clear_variables.png")));
			pnlVariableParser.add(btnResetVariables, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		contentPane.add(pnlVariableParser, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JToolBar tbMain;
	private JButton btnFetchTasks;
	private JButton btnFinishTask;
	private JComboBox cbProcessDefinitions;
	private JButton btnStartInstance;
	private JPanel pnlTasks;
	private JScrollPane scTasks;
	private TaskTable tbTasks;
	private JPanel pnlVariables;
	private JScrollPane scVariables;
	private VariablesTable tbVariables;
	private JPanel pnlVariableParser;
	private JTextField tfVariableName;
	private JTextField tfVariableValue;
	private JComboBox cbVariableTypes;
	private JButton btnAddVariable;
	private JButton btnResetVariables;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public void mouseClicked(MouseEvent e) {
		selectedTask = tbTasks.getSelectedTask();	
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}
