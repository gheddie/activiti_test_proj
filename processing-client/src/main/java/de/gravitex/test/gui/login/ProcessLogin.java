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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import de.gravitex.test.gui.EnhancedProcessTestView;

public class ProcessLogin extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	public ProcessLogin() {
		super(new JDialog());
		initComponents();
		setSize(300, 150);
		putListener();
	}

	private void putListener() {
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userName = tfUsername.getText();
				if ((userName == null) || (userName.length() == 0)) {
					return;
				}
				setVisible(false);				
				new EnhancedProcessTestView(userName).setVisible(true);
			}
		});
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		pnlCredentials = new JPanel();
		lblUsername = new JLabel();
		tfUsername = new JTextField();
		lblPassword = new JLabel();
		tfPassword = new JTextField();
		btnLogin = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0};
		((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {1.0, 0.0, 1.0E-4};

		//======== pnlCredentials ========
		{
			pnlCredentials.setBorder(new TitledBorder("Benutzerdaten"));
			pnlCredentials.setLayout(new GridBagLayout());
			((GridBagLayout)pnlCredentials.getLayout()).columnWidths = new int[] {0, 0, 0};
			((GridBagLayout)pnlCredentials.getLayout()).rowHeights = new int[] {0, 0, 0};
			((GridBagLayout)pnlCredentials.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
			((GridBagLayout)pnlCredentials.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

			//---- lblUsername ----
			lblUsername.setText("Benutzername:");
			pnlCredentials.add(lblUsername, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));
			pnlCredentials.add(tfUsername, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
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
		contentPane.add(pnlCredentials, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//---- btnLogin ----
		btnLogin.setText("Login");
		contentPane.add(btnLogin, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel pnlCredentials;
	private JLabel lblUsername;
	private JTextField tfUsername;
	private JLabel lblPassword;
	private JTextField tfPassword;
	private JButton btnLogin;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
