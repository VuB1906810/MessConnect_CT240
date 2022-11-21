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
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.awt.event.ActionEvent;

public class SignupView extends JFrame {

	public void setMessageError(JLabel messageError) {
		this.messageError = messageError;
	}
	private JPanel contentPane;
	private JPasswordField password;
	private JTextField username;
	private JPasswordField rePassword;
	private JLabel messageError;
	
	private ClientConfigView clientConfigView;
	private DataOutputStream outputStream;


	/**
	 * Create the frame.
	 */
	public SignupView() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 416);
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
		lblNewLabel.setBounds(122, 23, 221, 61);
		contentPane.add(lblNewLabel);
		
		username = new JTextField();
		username.setText("");
		username.setFont(new Font("Tahoma", Font.PLAIN, 20));
		username.setBounds(174, 119, 264, 34);
		contentPane.add(username);
		username.setColumns(10);
		
		JButton btnLogin = new JButton("Sign Up");
		btnLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendDataRegister(username.getText(), password.getText());
				
			}
		});
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnLogin.setBounds(95, 328, 275, 41);
		contentPane.add(btnLogin);
		
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
		password.setBounds(174, 178, 264, 34);
		contentPane.add(password);
		
		rePassword = new JPasswordField();
		rePassword.setToolTipText("");
		rePassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
		rePassword.setBounds(174, 238, 264, 34);
		contentPane.add(rePassword);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Re-Password:");
		lblNewLabel_1_1_1.setForeground(Color.WHITE);
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_1_1.setBounds(33, 238, 131, 34);
		contentPane.add(lblNewLabel_1_1_1);
		
		JButton btnNewButton = new JButton("Back");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton.setBounds(337, 74, 101, 21);
		contentPane.add(btnNewButton);
		
		messageError = new JLabel("");
		messageError.setFont(new Font("Tahoma", Font.BOLD, 13));
		messageError.setForeground(Color.ORANGE);
		messageError.setBounds(40, 282, 385, 28);
		contentPane.add(messageError);
		setVisible(true);
	}
	


	protected void sendDataRegister(String username, String password) {
		String data = "R:";
		data += username + '/' + password;
		System.out.println("data :" + data);
		try {
			outputStream.writeUTF(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}



	public SignupView(ClientConfigView clientConfigView, DataOutputStream outputStream) {
		this();
		this.clientConfigView = clientConfigView;
		this.outputStream = outputStream;
	}



	public boolean validateSignup() {
		if(username.getText().length()==0) {
			showError("Please enter username!");
			return false;
		}
			
		if(password.getText().length() < 6) {
			showError("Password length at least 6");
			return false;
		}
			
		if(!rePassword.getText().equals(password.getText())) {
			showError("Confirm password is incorrect");
			return false;
		}
		
		return true;
	}
	public void showError(String message) {
		messageError.setText(message);
	}

}
