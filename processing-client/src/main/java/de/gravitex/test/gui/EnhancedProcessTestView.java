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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import de.gravitex.test.gui.component.*;

import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import de.gravitex.test.ProcessVariableDTO;
import de.gravitex.test.core.ProcessingClientSingleton;
import de.gravitex.test.gui.component.TaskTable;
import de.gravitex.test.gui.component.VariablesTable;

/**
 * @author User #1
 */
public class EnhancedProcessTestView extends JFrame implements MouseListener {

	private static final long serialVersionUID = 1L;

	private Task selectedGroupTask;
	
	private Task selectedUserTask;

	private List<ProcessVariableDTO> processVariables;

	private ProcessDefinition selectedProcessDefinition;

	public EnhancedProcessTestView() {
		initComponents();
		initAdditionalComponents();
		setSize(1200, 900);
		User loggedInUser = ProcessingClientSingleton.getLoggedInUser();
		String userAsString = loggedInUser.getLastName() + ", " + loggedInUser.getFirstName();
		setTitle("Prozess-Steuerung (angemeldet: " + userAsString + ")");
		init();
		putListeners();
		fillVariableTypes();
		fillTasks();
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	}

	private void initAdditionalComponents() {
		tbGroupTasks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbUserTasks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void fillVariableTypes() {
		ComboBoxModel model = new DefaultComboBoxModel<>(new Object[] { ProcessVariableType.BOOLEAN, ProcessVariableType.INTEGER, ProcessVariableType.LONG, ProcessVariableType.STRING });
		cbVariableTypes.setModel(model);
	}

	private void init() {
		fillProcessDefinitions();
		processVariables = new ArrayList<>();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void fillProcessDefinitions() {
		try {
			List<ProcessDefinition> deployedDefinitions = ProcessingClientSingleton.getInstance().queryDeployedProcessDefinitions();
			System.out.println("process server has " + deployedDefinitions.size() + " process definitions deployed.");
			Vector<ProcessDefinition> processDefinitionItems = new Vector<>();
			processDefinitionItems.add(null);
			for (ProcessDefinition processDefinition : deployedDefinitions) {
				processDefinitionItems.add(processDefinition);
			}
			ComboBoxModel model = new DefaultComboBoxModel<>(processDefinitionItems);
			cbProcessDefinitions.setModel(model);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void putListeners() {
		btnStartInstance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startProcessInstance();
			}
		});
		// ---
		btnFetchTasks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillTasks();
			}
		});
		// ---
		btnClaimTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				claimTask();
			}
		});
		// ---
		btnFinishTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				finishTask();
			}
		});
		// ---
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
		// ---
		btnResetVariables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("reset variables...");
				processVariables.clear();
				fillVariables();
			}
		});
		// ---
		cbProcessDefinitions.addItemListener(new ItemListener() {
			@SuppressWarnings("unchecked")
			public void itemStateChanged(ItemEvent e) {
				selectedProcessDefinition = (ProcessDefinition) ((JComboBox<ProcessDefinition>) e.getSource()).getSelectedItem();
			}
		});
		// ---
		tbGroupTasks.addMouseListener(this);
		tbUserTasks.addMouseListener(this);
	}
	
	private void startProcessInstance() {
		if (selectedProcessDefinition == null) {
			JOptionPane.showMessageDialog(EnhancedProcessTestView.this, "Select a process definition!");
			return;
		}
		try {
			ProcessingClientSingleton.getInstance().startProcessInstance(selectedProcessDefinition.getKey(), getProcessVariables());
			fillTasks();
		} catch (RemoteException e1) {
			JOptionPane.showMessageDialog(EnhancedProcessTestView.this, "Unable to start process instance : " + e1.getMessage());
		}
	}
	
	private void finishTask() {
		if (selectedUserTask == null) {
			JOptionPane.showMessageDialog(EnhancedProcessTestView.this, "No user task to finish selected ---> returning.");
			return;
		}
		try {
			ProcessingClientSingleton.getInstance().completeTask(selectedUserTask.getId(), getProcessVariables());
			fillTasks();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(EnhancedProcessTestView.this, "Could not finish task : " + e1.getMessage());
		} finally {
			selectedUserTask = null;
		}
	}
	
	private void claimTask() {
		if (selectedGroupTask == null) {
			JOptionPane.showMessageDialog(EnhancedProcessTestView.this, "No group task ot claim selected ---> returning.");
			return;
		}
		System.out.println("claiming task with id '" + selectedGroupTask.getId() + "'.");
		try {
			ProcessingClientSingleton.getInstance().claimTask(selectedGroupTask.getId());
			fillTasks();
		} catch (RemoteException e1) {
			JOptionPane.showMessageDialog(EnhancedProcessTestView.this, "Could not claim task : " + e1.getMessage());
		} finally {
			selectedGroupTask = null;
		}
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
			//fill group tasks
			tbGroupTasks.setData(ProcessingClientSingleton.getInstance().queryGroupTasks());
			//fill unser tasks
			tbUserTasks.setData(ProcessingClientSingleton.getInstance().queryTasksByLoggedInUser());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		tbMain = new JToolBar();
		btnFetchTasks = new JButton();
		btnFinishTask = new JButton();
		btnClaimTask = new JButton();
		cbProcessDefinitions = new JComboBox();
		btnStartInstance = new JButton();
		pnlGroupTasks = new JPanel();
		scGroupTasks = new JScrollPane();
		tbGroupTasks = new GroupTaskTable();
		pnlUserTasks = new JPanel();
		scUserTasks = new JScrollPane();
		tbUserTasks = new UserTaskTable();
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
		((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {38, 0, 127, 0, 0, 0};
		((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 1.0, 0.0, 1.0, 0.0, 1.0E-4};

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

			//---- btnClaimTask ----
			btnClaimTask.setText("Task reservieren");
			btnClaimTask.setIcon(new ImageIcon(getClass().getResource("/gfx/claim_task.png")));
			tbMain.add(btnClaimTask);
			tbMain.add(cbProcessDefinitions);

			//---- btnStartInstance ----
			btnStartInstance.setText("Instanz starten");
			btnStartInstance.setIcon(new ImageIcon(getClass().getResource("/gfx/start_instance.png")));
			tbMain.add(btnStartInstance);
		}
		contentPane.add(tbMain, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//======== pnlGroupTasks ========
		{
			pnlGroupTasks.setBorder(new TitledBorder("Gruppen-Aufgaben"));
			pnlGroupTasks.setLayout(new GridBagLayout());
			((GridBagLayout)pnlGroupTasks.getLayout()).columnWidths = new int[] {0, 0};
			((GridBagLayout)pnlGroupTasks.getLayout()).rowHeights = new int[] {0, 0};
			((GridBagLayout)pnlGroupTasks.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
			((GridBagLayout)pnlGroupTasks.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

			//======== scGroupTasks ========
			{
				scGroupTasks.setViewportView(tbGroupTasks);
			}
			pnlGroupTasks.add(scGroupTasks, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		contentPane.add(pnlGroupTasks, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//======== pnlUserTasks ========
		{
			pnlUserTasks.setBorder(new TitledBorder("Eigene Aufgaben"));
			pnlUserTasks.setLayout(new GridBagLayout());
			((GridBagLayout)pnlUserTasks.getLayout()).columnWidths = new int[] {0, 0};
			((GridBagLayout)pnlUserTasks.getLayout()).rowHeights = new int[] {0, 0};
			((GridBagLayout)pnlUserTasks.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
			((GridBagLayout)pnlUserTasks.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

			//======== scUserTasks ========
			{
				scUserTasks.setViewportView(tbUserTasks);
			}
			pnlUserTasks.add(scUserTasks, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		contentPane.add(pnlUserTasks, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
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
		contentPane.add(pnlVariables, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
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
		contentPane.add(pnlVariableParser, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		pack();
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JToolBar tbMain;
	private JButton btnFetchTasks;
	private JButton btnFinishTask;
	private JButton btnClaimTask;
	private JComboBox cbProcessDefinitions;
	private JButton btnStartInstance;
	private JPanel pnlGroupTasks;
	private JScrollPane scGroupTasks;
	private GroupTaskTable tbGroupTasks;
	private JPanel pnlUserTasks;
	private JScrollPane scUserTasks;
	private UserTaskTable tbUserTasks;
	private JPanel pnlVariables;
	private JScrollPane scVariables;
	private VariablesTable tbVariables;
	private JPanel pnlVariableParser;
	private JTextField tfVariableName;
	private JTextField tfVariableValue;
	private JComboBox cbVariableTypes;
	private JButton btnAddVariable;
	private JButton btnResetVariables;
	// JFormDesigner - End of variables declaration //GEN-END:variables

	public void mouseClicked(MouseEvent e) {
		switch (((TaskTable) e.getSource()).getTableType()) {
		case GROUP:
			//---------------------------------------
			selectedGroupTask = tbGroupTasks.getSelectedTask();
			//---------------------------------------
			break;
		case USER:
			//---------------------------------------
			selectedUserTask = tbUserTasks.getSelectedTask();
			//---------------------------------------
			break;			
		}
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
