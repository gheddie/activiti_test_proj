/*
 * Created by JFormDesigner on Fri Nov 29 07:57:34 CET 2013
 */

package de.gravitex.test.gui.login;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import de.gravitex.test.core.ProcessingClientSingleton;
import de.gravitex.test.gui.EnhancedProcessTestView;

public class ProcessLogin extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	public ProcessLogin() {
		super(new JDialog());
		initComponents();
		setSize(640, 240);
		setTitle("Prozess-Steuerung");
		putListeners();
		init();
		
		testTaskQuery();
	}

	private void testTaskQuery() {
		try {
			HashMap<String, Object> parameter = new HashMap<>();
			parameter.put("taskDefKey", "meetOperatingDepartment");
			System.out.println(ProcessingClientSingleton.getInstance().queryTasksNative("SELECT * FROM ACT_RU_TASK T WHERE T.TASK_DEF_KEY_ = #{taskDefKey}", parameter).size());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		try {
			ProcessingClientSingleton.getInstance().initProcessEngine();
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}

	private void putListeners() {
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userId = tfUserId.getText();
				if ((userId == null) || (userId.length() == 0)) {
					return;
				}
				String password = tfPassword.getText();
				if ((password == null) || (password.length() == 0)) {
					return;
				}
				try {
					if (ProcessingClientSingleton.getInstance().authenticateUser(userId, password)) {
						setVisible(false);
						new EnhancedProcessTestView().setVisible(true);
					} else {
						JOptionPane.showMessageDialog(ProcessLogin.this, "Unable to authenticate user!");
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		lblSplash = new JLabel();
		pnlCredentials = new JPanel();
		lblUserId = new JLabel();
		tfUserId = new JTextField();
		lblPassword = new JLabel();
		tfPassword = new JPasswordField();
		btnLogin = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0, 0};
		((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0};
		((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
		((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {1.0, 0.0, 1.0E-4};

		//---- lblSplash ----
		lblSplash.setIcon(new ImageIcon(getClass().getResource("/gfx/splash_big.png")));
		contentPane.add(lblSplash, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		//======== pnlCredentials ========
		{
			pnlCredentials.setBorder(new TitledBorder("Benutzerdaten"));
			pnlCredentials.setLayout(new GridBagLayout());
			((GridBagLayout)pnlCredentials.getLayout()).columnWidths = new int[] {0, 0, 0};
			((GridBagLayout)pnlCredentials.getLayout()).rowHeights = new int[] {0, 0, 0};
			((GridBagLayout)pnlCredentials.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
			((GridBagLayout)pnlCredentials.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

			//---- lblUserId ----
			lblUserId.setText("User-ID:");
			pnlCredentials.add(lblUserId, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));
			pnlCredentials.add(tfUserId, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

			//---- lblPassword ----
			lblPassword.setText("Passwort:");
			pnlCredentials.add(lblPassword, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 5), 0, 0));
			pnlCredentials.add(tfPassword, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		contentPane.add(pnlCredentials, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//---- btnLogin ----
		btnLogin.setText("Login");
		contentPane.add(btnLogin, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel lblSplash;
	private JPanel pnlCredentials;
	private JLabel lblUserId;
	private JTextField tfUserId;
	private JLabel lblPassword;
	private JPasswordField tfPassword;
	private JButton btnLogin;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
