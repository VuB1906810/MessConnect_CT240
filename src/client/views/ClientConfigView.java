package client.views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client.controllers.ConnectThread;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class ClientConfigView extends JFrame {

    private JPanel contentPane;
    private JTextField txtIP;
    private JTextField txtPort;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private ConnectThread connect;
    private LoginView loginView;
    private SignupView signUpView;

    /**
     * Launch the application.
     */
    public static void main(String[] args) throws IOException {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    ClientConfigView frame = new ClientConfigView();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ClientConfigView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 252);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("MessConnect");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 33));
        lblNewLabel.setBounds(125, 5, 200, 60);
        contentPane.add(lblNewLabel);

        txtIP = new JTextField("127.0.0.1");
        txtIP.setBounds(158, 81, 183, 20);
        contentPane.add(txtIP);
        txtIP.setColumns(10);

        txtIP.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        JLabel lblNewLabel_1 = new JLabel("Connect to MessConnect server");
        lblNewLabel_1.setBounds(150, 53, 200, 14);
        contentPane.add(lblNewLabel_1);

        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });
        btnConnect.setBounds(172, 179, 89, 23);
        contentPane.add(btnConnect);

        JLabel lblNewLabel_2 = new JLabel("IP Server");
        lblNewLabel_2.setBounds(88, 84, 70, 14);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel();
        lblNewLabel_3.setForeground(Color.RED);
        lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblNewLabel_3.setBounds(105, 154, 216, 14);
        contentPane.add(lblNewLabel_3);

        txtPort = new JTextField("7244");
        txtPort.setColumns(10);
        txtPort.setBounds(158, 112, 70, 20);
        contentPane.add(txtPort);

        txtPort.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();

            }
        });

        JLabel lblNewLabel_2_1 = new JLabel("Port");
        lblNewLabel_2_1.setBounds(88, 115, 70, 14);
        contentPane.add(lblNewLabel_2_1);
    }

    protected void connectToServer() {
        try {
            InetAddress ip = InetAddress.getByName("localhost");
            socket = new Socket(ip, Integer.parseInt(txtPort.getText()));
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            connect = new ConnectThread(this, socket, inputStream, outputStream);
            connect.start();

            this.dispose();

            showLoginView();

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Can not connect to the server with this address and port, please try again!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public void showSignupView() {
        signUpView = new SignupView(this, outputStream);
    }

    public void disposeSignUpView() {
        signUpView.dispose();
    }

    public SignupView getSignUpView() {
        return signUpView;
    }

    public void showClientView(String username) {

        connect.setClientView(new ClientView(this, username, outputStream));
        loginView.dispose();
    }

    public void showLoginView() {
        loginView = new LoginView(this, outputStream);

    }
}
