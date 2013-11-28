/*
 * Created by JFormDesigner on Thu Nov 28 15:26:32 CET 2013
 */

package de.gravitex.test.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.*;
import javax.swing.border.*;

import org.activiti.engine.task.Task;

import de.gravitex.test.ProcessServerRemote;
import de.gravitex.test.RMIConstants;

/**
 * @author User #1
 */
public class ProcessTestView extends JFrame implements MouseListener {
	
	private ProcessServerRemote processServer;
	
	private Task selectedTask;
	
	public ProcessTestView() {
		initComponents();
		setSize(800, 600);
		setTitle("Prozess-Steuerung");
		initProcessEngine();
		putListeners();
	}
	
	private void putListeners() {
		btnStartInstance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					processServer.startProcessInstance("SimpleVacationRequest", "vacationRequest");
				} catch (RemoteException e1) {
					e1.printStackTrace();
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
					JOptionPane.showMessageDialog(ProcessTestView.this, "No task selected ---> returning.");
					return;
				}
				try {
					processServer.completeTask(selectedTask.getId(), null);
					fillTasks();
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(ProcessTestView.this, "Could not finish task : " + e1.getMessage());
				} finally {
					selectedTask = null;
				}
			}
		});		
		//---
		tbTasks.addMouseListener(this);
	}
	
	private void fillTasks() {
		try {
			tbTasks.setData(processServer.getTasksForUserGroup("management"));
		} catch (RemoteException e) {
			e.printStackTrace();
		}				
	}

	private void initProcessEngine() {
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry("localhost", RMIConstants.RMI_PORT);
			processServer = (ProcessServerRemote) registry.lookup(RMIConstants.RMI_ID);
		} catch (RemoteException | NotBoundException e) {
			// e.printStackTrace();
			System.out.println("Fehler beim Initialisieren : " + e.getMessage());
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		tbMain = new JToolBar();
		cbProcessDefinitions = new JComboBox();
		btnStartInstance = new JButton();
		btnFetchTasks = new JButton();
		btnFinishTask = new JButton();
		scTasksMain = new JScrollPane();
		pnlTasks = new JPanel();
		scTasks = new JScrollPane();
		tbTasks = new ProcessTable();
		scVariables = new JScrollPane();
		pnlVariables = new JPanel();
		tbVariables = new JTable();
		scParseVariables = new JScrollPane();
		pnlParseVariables = new JPanel();
		tfParseVariables = new JTextField();
		btnAddVariable = new JButton();
		btnResetVariables = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0, 0};
		((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 80, 78, 48, 0, 0};
		((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0E-4};
		((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 1.0, 1.0, 0.0, 0.0, 1.0E-4};

		//======== tbMain ========
		{
			tbMain.setEnabled(false);
			tbMain.add(cbProcessDefinitions);

			//---- btnStartInstance ----
			btnStartInstance.setText("Instanz starten");
			tbMain.add(btnStartInstance);

			//---- btnFetchTasks ----
			btnFetchTasks.setText("Tasks holen");
			tbMain.add(btnFetchTasks);

			//---- btnFinishTask ----
			btnFinishTask.setText("Task abschliessen");
			tbMain.add(btnFinishTask);
		}
		contentPane.add(tbMain, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//======== scTasksMain ========
		{

			//======== pnlTasks ========
			{
				pnlTasks.setBorder(new TitledBorder("Offene Aufgaben"));
				pnlTasks.setLayout(new BorderLayout());

				//======== scTasks ========
				{
					scTasks.setViewportView(tbTasks);
				}
				pnlTasks.add(scTasks, BorderLayout.CENTER);
			}
			scTasksMain.setViewportView(pnlTasks);
		}
		contentPane.add(scTasksMain, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//======== scVariables ========
		{

			//======== pnlVariables ========
			{
				pnlVariables.setBorder(new TitledBorder("Variablen"));
				pnlVariables.setLayout(new BorderLayout());
				pnlVariables.add(tbVariables, BorderLayout.CENTER);
			}
			scVariables.setViewportView(pnlVariables);
		}
		contentPane.add(scVariables, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//======== scParseVariables ========
		{

			//======== pnlParseVariables ========
			{
				pnlParseVariables.setBorder(new TitledBorder("Variable hinzuf\u00fcgen"));
				pnlParseVariables.setLayout(new BorderLayout());
				pnlParseVariables.add(tfParseVariables, BorderLayout.CENTER);
			}
			scParseVariables.setViewportView(pnlParseVariables);
		}
		contentPane.add(scParseVariables, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//---- btnAddVariable ----
		btnAddVariable.setText("Variable hinzuf\u00fcgen");
		contentPane.add(btnAddVariable, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 5), 0, 0));

		//---- btnResetVariables ----
		btnResetVariables.setText("Variablen l\u00f6schen");
		contentPane.add(btnResetVariables, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JToolBar tbMain;
	private JComboBox cbProcessDefinitions;
	private JButton btnStartInstance;
	private JButton btnFetchTasks;
	private JButton btnFinishTask;
	private JScrollPane scTasksMain;
	private JPanel pnlTasks;
	private JScrollPane scTasks;
	private ProcessTable tbTasks;
	private JScrollPane scVariables;
	private JPanel pnlVariables;
	private JTable tbVariables;
	private JScrollPane scParseVariables;
	private JPanel pnlParseVariables;
	private JTextField tfParseVariables;
	private JButton btnAddVariable;
	private JButton btnResetVariables;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	//---Listener Methods
	
	public void mouseClicked(MouseEvent e) {
		selectedTask = tbTasks.getSelectedTask();
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}
}