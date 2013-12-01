package de.gravitex.test.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.activiti.engine.task.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.gravitex.test.ProcessServerRemote;
import de.gravitex.test.RMIConstants;
import de.gravitex.test.gui.component.TaskTable;

public class ProcessGUIClient extends JFrame implements MouseListener {

	private static final long serialVersionUID = 1L;

	private JToolBar tbMain;

	private JButton btnStart;

	private JButton btnFetchTasks;
	
	private JButton btnfinishTask;

	private TaskTable tbProcesses;
	
	private JScrollPane scProcesses;
	
	private JTextArea taVariables;

	protected String taskToResume;

	private ProcessServerRemote processServer;

	private String groupName;

	private Task selectedTask;

	public ProcessGUIClient(String groupName) {
		super();
		this.groupName = groupName;
		setSize(800, 600);
		setLayout(new BorderLayout());
		// ------------------------------------------------
		initProcessEngine();
		// ------------------------------------------------
		tbMain = new JToolBar();
		tbMain.setFloatable(false);
		// ------------------------------------------------
		btnStart = new JButton("start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//...
			}
		});
		// ------------------------------------------------
		btnFetchTasks = new JButton("fetch tasks");
		btnFetchTasks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					List<Task> openTasks = processServer.getTasksForUserGroup(ProcessGUIClient.this.groupName);
					System.out.println(openTasks.size() + " open tasks.");
					for (Task task : openTasks) {
						System.out.println(task.getName() + "[" + task.getId() + "] (process id:"+task.getProcessInstanceId()+")");
					}
					fillTasks(openTasks);
				} catch (RemoteException e2) {
					e2.printStackTrace();
				}
			}
		});
		// ------------------------------------------------
		btnfinishTask = new JButton("finish task");
		btnfinishTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedTask == null) {
					JOptionPane.showMessageDialog(ProcessGUIClient.this, "No task selected ---> returning.");
					return;
				}
				try {
					processServer.completeTask(selectedTask.getId(), null);
					fillTasks();
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(ProcessGUIClient.this, "Could not finish task : " + e1.getMessage());
				} finally {
					selectedTask = null;
				}
			}
		});
		// ------------------------------------------------		
		tbProcesses = new TaskTable();
		tbProcesses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  
		scProcesses = new JScrollPane(tbProcesses);
		tbProcesses.addMouseListener(this);
		add(scProcesses, BorderLayout.CENTER);
		// ------------------------------------------------
		tbMain.add(btnStart);
		tbMain.add(btnFetchTasks);
		tbMain.add(btnfinishTask);
		add(tbMain, BorderLayout.NORTH);
		// ------------------------------------------------
		taVariables = new JTextArea();
		taVariables.setMinimumSize(new Dimension(100, 150));
		add(taVariables, BorderLayout.SOUTH);
		// ------------------------------------------------
		setVisible(true);
	}
	
	private void fillTasks() {
		try {
			tbProcesses.setData(processServer.getTasksForUserGroup(groupName));
		} catch (RemoteException e) {
			e.printStackTrace();
		}				
	}
	
	private void fillTasks(List<Task> tasks) {
		tbProcesses.setData(tasks);
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

	// ---Listener Methods

	public void mouseReleased(MouseEvent e) {
		int selectedRow = ((JTable) e.getSource()).getSelectedRow();
		taskToResume = (String) tbProcesses.getModel().getValueAt(selectedRow, 0);
		// logger.info("task to resume selected : '"+taskToResume+"'.");
	}

	public void mouseClicked(MouseEvent e) {
		System.out.println("click...");
		selectedTask = tbProcesses.getSelectedTask();
	}

	public void mousePressed(MouseEvent e) {
		// ...
	}

	public void mouseEntered(MouseEvent e) {
		// ...
	}

	public void mouseExited(MouseEvent e) {
		// ...
	}

	private static void readDefXml() {
		try {
			File fXmlFile = new File("C:\\eclipseWorkspaces\\INDIGO_PROCESSING\\processing\\ProcessEngine\\src\\de\\gravitex\\processing\\testing\\charts\\testtimerdef.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			Element root = doc.getDocumentElement();
			root.normalize();
			readNodesRecursive(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void readNodesRecursive(Node root) {
		// logger.info("element :" + root.getNodeName());
		NodeList children = root.getChildNodes();
		for (int childIndex = 0; childIndex < children.getLength(); childIndex++) {
			Node child = children.item(childIndex);
			readNodesRecursive((Node) child);
		}
	}

	// ---
	// ---
	// ---

	public static void main(String[] args) {

		// log4j
		// PropertyConfigurator.configure("C:\\log4j_props\\processing_log4j.properties");
		// PropertyConfigurator.configure("/Users/stefan/log4j_props/log4j.properties");

		// start process gui
		new ProcessGUIClient("management");

		// readDefXml();
	}
}