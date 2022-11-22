package client.views;

import server.DriveApi;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import org.apache.commons.io.FileUtils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import client.views.FileViewer;
import models.FileModel;

public class ClientView extends JFrame {

    private JPanel contentPane;
    private JTextField txtFindUser;
    private JTextField txtChat;
    private JTextPane chatBoxPane;
    private JButton btnSendMess;
    private JFileChooser filechooser;
    private JButton btnFile;
    private JButton viewFile;
    private JLabel lblNameChatWith;

    private String username;
    private String curentChatUser;

    private DataOutputStream outputStream;

    private ClientConfigView clientConfigView;
    private JPanel onlinePanel;
    private JPanel filePanel;
    private JTable fileTable;
    private ArrayList<JLabel> listUserOnline;
    private DefaultTableModel fileTableModel = new DefaultTableModel(0, 0);
    private ArrayList<FileModel> listFile = new ArrayList<FileModel>();

    private int luongThread = -1;
    private final Thread[] threads = new Thread[1000000];

    /**
     * Create the frame.
     */
    public ClientView(ClientConfigView clientConfigView, String username, DataOutputStream outputStream) {
        this.clientConfigView = clientConfigView;
        this.outputStream = outputStream;
        this.username = username;
        this.curentChatUser = null;

        setTitle("MessConnect");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 982, 720);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(160, 196, 102));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("MessConnect");
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setBackground(new Color(255, 255, 255));
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 57));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(9, 10, 949, 89);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Enter username to search: ");
        lblNewLabel_1.setForeground(new Color(255, 255, 255));
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblNewLabel_1.setBounds(10, 109, 248, 29);
        contentPane.add(lblNewLabel_1);

        txtFindUser = new JTextField();
        txtFindUser.setBounds(262, 111, 236, 29);
        txtFindUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleFindUser(txtFindUser.getText());
            }
        });
        contentPane.add(txtFindUser);
        txtFindUser.setColumns(10);

        JButton btnFind = new JButton("Search");
        btnFind.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnFind.setBounds(508, 111, 90, 29);
        contentPane.add(btnFind);
        btnFind.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                handleFindUser(txtFindUser.getText());

            }
        });

        txtChat = new JTextField();
        txtChat.setFont(new Font("Tahoma", Font.PLAIN, 18));
        txtChat.setText("");
        txtChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSendMess();
            }
        });
        txtChat.setBounds(10, 618, 750, 42);
        contentPane.add(txtChat);
        txtChat.setColumns(10);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        btnFile = new JButton("File");
        btnFile.setEnabled(true);
        btnFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(ClientView.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Path path = selectedFile.toPath();
                    ////////////////////////
                    luongThread++;
                    System.out.println(luongThread);
                    int cur = luongThread;
                    threads[cur] = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (cur != 0) {
                                    threads[cur - 1].join();
                                }
                                final String mimeType = Files.probeContentType(path);
                                final DriveApi drive = new DriveApi();
                                btnFile.setText("Sending...");
                                btnFile.setEnabled(false);
                                handleSendFile(drive.upload(selectedFile.getName(), mimeType, selectedFile.getAbsolutePath()), selectedFile.getName());
                            } catch (IOException ex) {
                                Logger.getLogger(ClientView.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (GeneralSecurityException ex) {
                                Logger.getLogger(ClientView.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ClientView.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    threads[cur].start();
                    ////////////
                }
            }
        });

        btnFile.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnFile.setBounds(770, 617, 110, 42);
        contentPane.add(btnFile);

        btnSendMess = new JButton("Send");
        btnSendMess.setEnabled(false);
        btnSendMess.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                handleSendMess();
            }
        });

        btnSendMess.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnSendMess.setBounds(885, 617, 70, 42);
        contentPane.add(btnSendMess);

        JLabel lblNewLabel_2 = new JLabel("Chatting with:");
        lblNewLabel_2.setForeground(new Color(255, 255, 255));
        lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblNewLabel_2.setBounds(10, 586, 142, 22);
        contentPane.add(lblNewLabel_2);

        JButton btnChatAll = new JButton("@ALL");
        btnChatAll.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnChatAll.setBounds(485, 587, 85, 21);
        btnChatAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtFindUser.setText("all");
                handleFindUser(txtFindUser.getText());
            }
        });
        contentPane.add(btnChatAll);

        lblNameChatWith = new JLabel("@" + curentChatUser);
        lblNameChatWith.setForeground(new Color(0, 255, 0));
        lblNameChatWith.setFont(new Font("Arial", Font.ITALIC, 16));
        lblNameChatWith.setBounds(160, 586, 315, 22);
        contentPane.add(lblNameChatWith);

        JLabel lblUserName = new JLabel("User: " + username);
        lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
        lblUserName.setForeground(new Color(255, 255, 255));
        lblUserName.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblUserName.setBounds(713, 107, 155, 31);
        contentPane.add(lblUserName);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnLogout.setBounds(860, 109, 100, 29);
        contentPane.add(btnLogout);

        btnLogout.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                clientConfigView.showLoginView();
                sendLogout();
                dispose();
            }
        });

        chatBoxPane = new JTextPane();
        chatBoxPane.setFont(new Font("Tahoma", Font.PLAIN, 18));
        chatBoxPane.setContentType("text/html");
        chatBoxPane.setBounds(9, 148, 750, 71);
        chatBoxPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(chatBoxPane);
        scrollPane.setBounds(9, 148, 750, 428);
        contentPane.add(scrollPane);

        onlinePanel = new JPanel();
        onlinePanel.setLayout(new BoxLayout(onlinePanel, BoxLayout.Y_AXIS));
        onlinePanel.setBounds(770, 148, 188, 200);
        onlinePanel.setBackground(Color.WHITE);

        JLabel lblOnline = new JLabel("Online");
        lblOnline.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblOnline.setForeground(Color.GREEN);
        Border border = lblOnline.getBorder();
        Border margin = new EmptyBorder(5, 50, 5, 10);
        lblOnline.setBorder(new CompoundBorder(border, margin));

        onlinePanel.add(lblOnline);
        listUserOnline = new ArrayList<JLabel>();

        txtFindUser.setText("all");
        handleFindUser("all");
        contentPane.add(onlinePanel);
        this.setVisible(true);
        setLocationRelativeTo(null);
        this.setResizable(false);

        filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));
        filePanel.setBounds(770, 350, 188, 210);
        filePanel.setBackground(Color.WHITE);
        contentPane.add(filePanel);

        viewFile = new JButton("View");
        viewFile.setEnabled(true);
        viewFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int row = fileTable.getSelectedRow();
                FileViewer fileViewer = new FileViewer("https://drive.google.com/file/d/" + listFile.get(row).getFileID());
                fileViewer.viewFile();
            }
        });

        viewFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
        viewFile.setBounds(770, 565, 188, 15);
        contentPane.add(viewFile);

        fileTable = new JTable();
        fileTableModel = new DefaultTableModel(0, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        String header[] = new String[]{"Filename"};

        fileTableModel.setColumnIdentifiers(header);
        fileTable.setModel(fileTableModel);

        filePanel.setEnabled(false);

        filePanel.add(new JScrollPane(fileTable));

        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                txtChat.requestFocus();
            }
        });

    }

    protected void sendLogout() {
        try {
            outputStream.writeUTF("O:");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public void handleSendFile(String fileid, String fileName) {

        try {
            outputStream.writeUTF("E:" + "FileID: " + fileid + ", FileName:" + fileName);
            outputStream.writeUTF("M:Sent file " + fileName);
            txtFindUser.setText(curentChatUser.equals("all") ? "all" : curentChatUser);
            handleFindUser(curentChatUser.equals("all") ? "all" : curentChatUser);
            setBodyMess1("Sent file " + fileName);
            btnFile.setText("File");
            btnFile.setEnabled(true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void handleSendMess() {
        try {
            String body = txtChat.getText();
            if (body.isEmpty()) {
                return;
            }
            outputStream.writeUTF("M:" + body);
            setBodyMess1(body);
            txtChat.setText("");
            chatBoxPane.selectAll();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setBodyMess(String string, Color bg, Color fg) {
        try {
            StyledDocument doc = chatBoxPane.getStyledDocument();

            // Define a keyword attribute
            SimpleAttributeSet keyWord = new SimpleAttributeSet();
            StyleConstants.setForeground(keyWord, fg);
            StyleConstants.setBackground(keyWord, bg);
            StyleConstants.setBold(keyWord, true);
            doc.insertString(doc.getLength(), string + "\n", keyWord);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void setFileTableBody(String fileid, String fileName) {
        System.out.println(fileName);
        if (fileName.equals("@Reset")) {
            int rowCount = fileTableModel.getRowCount();
            for (int i = rowCount - 1; i >= 0; i--) {
                fileTableModel.removeRow(i);
            }
            listFile.clear();
            fileTable.removeAll();
        } else {
            //@tuanvu:FileId: 1uumYoKkxA6lWJRJUeTyZ9FBO9SHb3Sd-, FileName: 278091082_643920043568098_6039146758206980854_n.jpg
            listFile.add(new FileModel(fileName.split(",")[0].split(":")[2].substring(1), fileName.split(",")[1].substring(11)));
            fileTableModel.addRow(new Object[]{fileName.split(":")[0] + ":" + fileName.split(":")[3]});
        }
    }

    public void showMessReceivedCurrentUser(String message) {
        System.out.println(message);
        setBodyMess(message, Color.white, Color.BLUE);
    }

    public void showWelcomeMess(String message) {
        setBodyMess("@Welcome: " + message, Color.white, Color.DARK_GRAY);
        this.curentChatUser = txtFindUser.getText();
        txtFindUser.setText("");
        lblNameChatWith.setText("@" + curentChatUser);

    }

    public void setBodyMess2(String string) {
        setBodyMess("@Server: " + string, Color.white, Color.BLUE);

    }

    public void setBodyMess1(String string) {
        setBodyMess("@me: " + string, Color.white, Color.MAGENTA);

    }

    public void handleFindUser(String user) {
        if (user.isEmpty()) {
            return;
        }

        if (user.equals(username)) {
            JOptionPane.showMessageDialog(this, "Can't chat with yourseft!", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            outputStream.writeUTF("F:" + user);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public JTable getFileTable() {
        return fileTable;
    }

    public void setFileTable(JTable fileTable) {
        this.fileTable = fileTable;
    }

    public JTextPane getChatBoxPane() {
        return chatBoxPane;
    }

    public void setChatBoxPane(JTextPane chatBoxPane) {
        this.chatBoxPane = chatBoxPane;
    }

    public void showErrorFindUser() {
        JOptionPane.showMessageDialog(this, "Cant find the username is entered", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showBtnSend() {
        btnSendMess.setEnabled(true);

    }

    public void removeUserOnlineList(String user) {
        for (int i = 0; i < listUserOnline.size(); i++) {
            if (listUserOnline.get(i).getText().equals(user)) {
                onlinePanel.remove(listUserOnline.get(i));
                onlinePanel.repaint();
                listUserOnline.remove(i);
                return;
            }
        }
    }

    public void addUserOnlineList(String username) {
        JLabel newUserOnline = new JLabel(username, JLabel.CENTER);
        newUserOnline.setFont(new Font("Tahoma", Font.PLAIN, 18));
        newUserOnline.setForeground(Color.PINK);
        Border border = newUserOnline.getBorder();
        Border margin = new EmptyBorder(5, 10, 5, 10);
        newUserOnline.setBorder(new CompoundBorder(border, margin));

        newUserOnline.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtFindUser.setText(username);
                handleFindUser(username);
            }
        });

        listUserOnline.add(newUserOnline);
        onlinePanel.revalidate();
        onlinePanel.add(listUserOnline.get(listUserOnline.size() - 1));
    }

}
