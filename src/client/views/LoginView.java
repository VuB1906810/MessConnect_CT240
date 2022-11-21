package client.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import models.UserModel;
import server.database.MySQLDB;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;

public class LoginView extends JFrame {

	private JPanel contentPane;
	private JTextField username;
	private JPasswordField password;
	private JLabel messageError;

	private DataOutputStream outputStream;
	private ClientConfigView clientConfigView;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public LoginView() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 360);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(160, 196, 102));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);

		JLabel lblNewLabel = new JLabel("MessConnect");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 33));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 23, 446, 61);
		contentPane.add(lblNewLabel);

		username = new JTextField();
		username.setText("");
		username.setFont(new Font("Tahoma", Font.PLAIN, 20));
		username.setBounds(153, 119, 285, 34);
		contentPane.add(username);
		username.setColumns(10);

		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnLogin.setBounds(76, 255, 145, 41);
		contentPane.add(btnLogin);

		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validateLogin()) {
					sendDataLogin(username.getText(), password.getText());
				}
			}
		});

		JButton btnSignup = new JButton("Sign Up");
		btnSignup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clientConfigView.showSignupView();
			}
		});
		btnSignup.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnSignup.setBounds(255, 255, 145, 41);
		contentPane.add(btnSignup);

		JLabel lblNewLabel_1 = new JLabel("Username:");
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(33, 119, 110, 34);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Password:");
		lblNewLabel_1_1.setForeground(Color.WHITE);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_1.setBounds(33, 178, 110, 34);
		contentPane.add(lblNewLabel_1_1);

		password = new JPasswordField();
		password.setToolTipText("");
		password.setFont(new Font("Tahoma", Font.PLAIN, 20));
		password.setBounds(153, 178, 285, 34);
		contentPane.add(password);

		password.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (validateLogin()) {
					sendDataLogin(username.getText(), password.getText());
				}
			}
		});

		messageError = new JLabel("");
		messageError.setFont(new Font("Tahoma", Font.BOLD, 15));
		messageError.setForeground(Color.ORANGE);
		messageError.setBounds(53, 222, 359, 23);
		contentPane.add(messageError);
		setVisible(true);
	}

	protected void sendDataLogin(String username, String password) {
		String data = "L:";
		data += username + '/' + password;
		System.out.println("data :" + data);
		try {
			outputStream.writeUTF(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LoginView(ClientConfigView clientConfigView, DataOutputStream outputStream) {
		this();
		this.clientConfigView = clientConfigView;
		this.outputStream = outputStream;
	}

	public boolean validateLogin() {
		if (username.getText().length() == 0) {
			messageError.setText("Please enter username!");
			return false;
		}
		if (password.getText().length() < 6) {
			messageError.setText("Password length at least 6");
			return false;
		}

		return true;
	}

	public void showError(String message) {
		messageError.setText(message);
	}
}
