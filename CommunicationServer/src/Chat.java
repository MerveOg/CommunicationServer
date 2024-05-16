
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import java.util.Base64;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 * file
 *
 * @author merveog
 */
public class Chat extends javax.swing.JFrame {

    private Socket clientSocket;
    private DataInputStream sInput;
    DataOutputStream sOutput;
    private DefaultListModel list = new DefaultListModel();
    private DefaultListModel list2 = new DefaultListModel();
    private DefaultListModel list3 = new DefaultListModel();
    private DefaultListModel activeClients = new DefaultListModel();

    /**
     * Creates new form Client
     */
    public Chat() {
        initComponents();

    }

    public Chat(String userName, String project, String key) {
        initComponents();
        this.lblUserName.setText(userName);
        this.lblKey.setText(key);
        this.lblName.setText(project);
        connectServer();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnectServer("Disconnected....");
            }
        });
        txtAGc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Check if it's a click event
                if (e.getClickCount() == 1) {

                    Object selectedValue = txtAGc.getSelectedValue();
                    if (selectedValue != null && selectedValue instanceof String) {
                        // Check if the selected value represents a file
                        String selectedFileName = (String) selectedValue;
                        if (selectedFileName.contains(".ipynb") || selectedFileName.contains(".jpg") || selectedFileName.contains(".pdf") || selectedFileName.contains(".png") || selectedFileName.contains(".txt")) {
                            System.out.println("Selected file name: " + selectedFileName);
                            // If it's a file, open it

                            openFile(selectedFileName);
                        } else {
                            System.out.println("It isn't file.");
                        }
                    }
                }
            }
        });

    }

    private void openFile(String fileName) {
        try {
            Desktop.getDesktop().open(new File(fileName));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
            System.out.println("Error opening file: " + ex.getMessage());
        }
    }

    public void connectServer() {
        try {
            txtAGc.setModel(list);

            txtAPrivChat.setModel(list2);
            jList1.setModel(activeClients);
            int port = 3000;
            String ip = "127.0.0.1";

            try {
                clientSocket = new Socket(ip, port);
                String msg = lblUserName.getText() + "," + lblName.getText() + ", ";

                sInput = new DataInputStream(clientSocket.getInputStream());
                sOutput = new DataOutputStream(clientSocket.getOutputStream());
                SendMessage(msg.getBytes());

                Thread listenThread = new Thread(() -> Listen());
                listenThread.start();

            } catch (IOException ex) {
                System.out.println("Couldn't connect to server! ");
            }
        } catch (NumberFormatException e) {

        }
    }

    private void Listen() {
        while (true) {
            try {
                if (clientSocket.isClosed()) {
                    System.out.println("Socket is closed.");
                    break;
                }

                byte[] messageByte = new byte[1024];
                int bytesRead = sInput.read(messageByte);

                if (bytesRead == -1) {
                    disconnectServer("Server closed. Message couldn't send! ");
                    break;
                }

                String message = new String(messageByte, 0, bytesRead);

                System.out.println("MGc " + message);

                if (message.startsWith("Gc") && message.contains(lblName.getText())) {
                    String[] split = message.split(",");
                    String user = split[1];
                    String userMessage = split[3];
                    list.addElement(user + userMessage + "\n");

                }
                if (message.startsWith("c")) {
                    String[] parts = message.split(",");
                    for (String part : parts) {
                        System.out.println(part);
                    }
                    String sender = parts[0];
                    String projectName = parts[1];
                    String userName = parts[2];
                    String text = parts[3];

                    String combinedValue = sender.substring(1) + ": " + text;

                    if (projectName.equals(lblName.getText()) && (userName.equals(lblUserName.getText()) || sender.equals("c" + lblUserName.getText()))) {
                        list2.addElement(combinedValue + "\n");
                    }

                }

                if (message.startsWith("a,")) {

                    String[] clients = message.substring(2).split(",");

                    if (clients[clients.length - 2].equals(lblName.getText())) {

                        //Need to update active users.
                        activeClients.removeAllElements();
                     
                        for (int i = 0; i < clients.length - 2; i++) {
                            // Adding every active user-client to my active users list.
                            if (!activeClients.contains(clients[i])) {
                                activeClients.addElement(clients[i]);
                                System.out.println("i: " + clients[i]);
                            }

                        }
                    }

                }
                if (message.startsWith("File:")) {
                    System.out.println("File: " + message);
                    String[] parts = message.split(":");
                    String fileName = parts[1];
                    String fileContentBase64 = parts[2];

                    try {
                        byte[] fileContent = Base64.getDecoder().decode(fileContentBase64);

                        receiveFile(fileName, fileContent);
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Base64 code is invalid: " + ex.getMessage());
                    }
                }

            } catch (IOException ex) {
                if (clientSocket.isClosed()) {
                    System.out.println("Socket closed");
                    break;
                }
            }

        }
    }

    private void receiveFile(String fileName, byte[] fileContent) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(fileContent);
            fos.close();

            SwingUtilities.invokeLater(() -> {

                list.addElement(fileName);

            });
        } catch (IOException ex) {
            System.out.println("Dosya alınırken bir hata oluştu: " + ex.getMessage());
        }
    }

    public void SendMessage(byte[] msg) {
        try {

            DataOutputStream sOutput = new DataOutputStream(clientSocket.getOutputStream());
            msg[msg.length - 1] = 0x14;
            sOutput.write(msg);
        } catch (IOException err) {

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblKey = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listOfTeam = new javax.swing.JList<>();
        lblUserName = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtTextInputGc = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTextInputPrivChat = new javax.swing.JTextField();
        btsnSendGc = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtAGc = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAPrivChat = new javax.swing.JList<>();
        jButton3 = new javax.swing.JButton();
        btnSendFileGc = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setPreferredSize(new java.awt.Dimension(290, 692));

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel1.setText("Project Name");

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel3.setText("Project Key");

        lblKey.setFont(new java.awt.Font("Helvetica Neue", 0, 11)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel5.setText("Project Team");

        listOfTeam.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listOfTeam);

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList1);

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel2.setText("Active Team Members");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblKey, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(20, Short.MAX_VALUE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                                .addComponent(jScrollPane3))
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel3)
                .addGap(28, 28, 28)
                .addComponent(lblKey, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setPreferredSize(new java.awt.Dimension(911, 692));

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jLabel6.setText("Project Groupchat");

        txtTextInputGc.setText("Write something here... ");
        txtTextInputGc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTextInputGcMouseClicked(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jLabel7.setText("Private Chat");

        txtTextInputPrivChat.setText("Write something here... ");

        btsnSendGc.setText("Send");
        btsnSendGc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btsnSendGcActionPerformed(evt);
            }
        });

        txtAGc.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        txtAGc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAGcMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(txtAGc);

        txtAPrivChat.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(txtAPrivChat);

        jButton3.setText("Send");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        btnSendFileGc.setText("Choose a file");
        btnSendFileGc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendFileGcActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnSendFileGc, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btsnSendGc))
                            .addComponent(txtTextInputGc, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addComponent(txtTextInputPrivChat))
                            .addComponent(jButton3))
                        .addGap(0, 26, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTextInputGc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTextInputPrivChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btsnSendGc)
                    .addComponent(jButton3)
                    .addComponent(btnSendFileGc))
                .addGap(138, 138, 138))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTextInputGcMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTextInputGcMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTextInputGcMouseClicked

    private void btsnSendGcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsnSendGcActionPerformed

        if (clientSocket == null || clientSocket.isClosed()) {
            System.out.println("First connect to server! ");
            return;
        }

        String message = "Gc," + lblUserName.getText() + "," + lblName.getText() + ",:" + txtTextInputGc.getText() + ", x";
        SendMessage(message.getBytes());
    }//GEN-LAST:event_btsnSendGcActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (clientSocket == null || clientSocket.isClosed()) {
            System.out.println("First connect to server! ");
            return;
        }
        if (!listOfTeam.isSelectionEmpty() && listOfTeam.getSelectedIndex() != -1) {
            String message = "c" + lblUserName.getText() + "," + lblName.getText() + "," + listOfTeam.getSelectedValue().toString() + "," + txtTextInputPrivChat.getText() + ", ";

            SendMessage(message.getBytes());
        } else {
            JOptionPane.showMessageDialog(rootPane, "You didn't select a team member or list is empty!");
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void btnSendFileGcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendFileGcActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a file!");

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSend = fileChooser.getSelectedFile();

            try {
                FileInputStream fis = new FileInputStream(fileToSend);
                byte[] fileContent = new byte[(int) fileToSend.length()];
                fis.read(fileContent);
                fis.close();

                String fileName = fileToSend.getName();
                String fileContentBase64 = Base64.getEncoder().encodeToString(fileContent);
                String message = "File:" + fileName + ":" + fileContentBase64;

                SendMessage(message.getBytes());
            } catch (IOException ex) {
                System.out.println("Dosya okunurken bir hata oluştu: " + ex.getMessage());
            }

        }    }//GEN-LAST:event_btnSendFileGcActionPerformed

    private void txtAGcMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAGcMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAGcMouseClicked

    private void disconnectServer(String disconnectMessage) {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                if (sInput != null) {
                    sInput.close();
                }
                clientSocket.close();
                System.out.println(disconnectMessage);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSendFileGc;
    private javax.swing.JButton btsnSendGc;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    public javax.swing.JLabel lblKey;
    public javax.swing.JLabel lblName;
    public javax.swing.JLabel lblUserName;
    public javax.swing.JList<String> listOfTeam;
    private javax.swing.JList<String> txtAGc;
    private javax.swing.JList<String> txtAPrivChat;
    private javax.swing.JTextField txtTextInputGc;
    private javax.swing.JTextField txtTextInputPrivChat;
    // End of variables declaration//GEN-END:variables
}
