
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
import javax.swing.SwingUtilities;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *x
 * @author merveog
 */
public class Chat extends javax.swing.JFrame {

    private Socket clientSocket;
    private DataInputStream sInput;
    DataOutputStream sOutput;
    private DefaultListModel list = new DefaultListModel();
    private DefaultListModel list2 = new DefaultListModel();
    private DefaultListModel list3 = new DefaultListModel();
    //private List<String> activeClients = new ArrayList<>();
    private DefaultListModel activeClients = new DefaultListModel();

    /**
     * Creates new form Client
     */
    public Chat() {
        initComponents();
        //Merve Og connectServer();
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

                //Sunucu kapanırsa bytesRead -1 olur:
                if (bytesRead == -1) {
                    disconnectServer("Server closed. Message couldn't send! ");
                    break;
                }

                String message = new String(messageByte, 0, bytesRead);

                System.out.println("M " + message);
                String msg2 = silIkinciVirguleKadar(message);
                System.out.println("....." + msg2);
                if (message.startsWith("Gc") && message.contains(lblName.getText())) {
                    list.addElement(message + "\n");

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

                    String combinedValue = sender + ": " + text;

                    if (projectName.equals(lblName.getText()) && (userName.equals(lblUserName.getText()) || sender.equals("c" + lblUserName.getText()))) {
                        list2.addElement(combinedValue + "\n");
                    }

                }

                if (message.startsWith("a,")) {
                    System.out.println("MESAJIM: " + message);
                    String[] clients = message.substring(2).split(",");
                    System.out.println("MESAJIM2: " + message);

                    for (String client : clients) {
                        System.out.println("c" + client);
                    }
                    System.out.println("C:" + clients[clients.length - 1]);
                    if (clients[clients.length - 2].equals(lblName.getText())) {
                        System.out.println("    ----------  ");
                        activeClients.removeAllElements();
                        // İlk eleman "a" olduğu için atlıyoruz ve sadece isimleri ekliyoruz
                        for (int i = 0; i < clients.length - 2; i++) {

                            System.out.println(clients[i]);

                            // Her bir ismi activeClients listesine ekliyoruz
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
                        System.out.println("Base64 kodu geçersiz: " + ex.getMessage());
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

    public static String silIkinciVirguleKadar(String str) {
        String[] parcalar = str.split(",");  // Virgüllere göre string'i parçalara ayır
        StringBuilder yeniString = new StringBuilder();

        // İlk iki parçayı atlayarak kalan parçaları yeni bir stringe ekle
        for (int i = 2; i < parcalar.length; i++) {
            yeniString.append(parcalar[i]);
            if (i < parcalar.length - 1) {
                yeniString.append(",");
            }
        }

        return yeniString.toString();
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
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtTextInputGc = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTextInputPrivChat = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtAGc = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAPrivChat = new javax.swing.JList<>();
        jButton3 = new javax.swing.JButton();
        btnSendFileGc = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

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
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                            .addComponent(jScrollPane3))
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

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        txtAGc.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(txtAGc);

        txtAPrivChat.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(txtAPrivChat);

        jButton3.setText("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        btnSendFileGc.setText("jButton4");
        btnSendFileGc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendFileGcActionPerformed(evt);
            }
        });

        jLabel2.setText("jLabel2");

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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnSendFileGc)
                                .addGap(270, 270, 270)
                                .addComponent(jButton2))
                            .addComponent(txtTextInputGc, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton3)
                                .addGap(44, 44, 44))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTextInputPrivChat, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7))
                                .addGap(0, 42, Short.MAX_VALUE))))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(btnSendFileGc))
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        if (clientSocket == null || clientSocket.isClosed()) {
            System.out.println("First connect to server! ");
            return;
        }

        String message = "Gc" + lblUserName.getText() + "," + lblName.getText() + ":" + txtTextInputGc.getText() + " x";
        SendMessage(message.getBytes());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (clientSocket == null || clientSocket.isClosed()) {
            System.out.println("First connect to server! ");
            return;
        }
        if (!listOfTeam.isSelectionEmpty() && listOfTeam.getSelectedIndex() != -1) {
            //  System.out.println("Bir öğe seçildi: " + listOfTeam.getSelectedValue());
            String message = "c" + lblUserName.getText() + "," + lblName.getText() + "," + listOfTeam.getSelectedValue().toString() + "," + txtTextInputPrivChat.getText() + ",";

            SendMessage(message.getBytes());
        } else {
            System.out.println("Hiçbir öğe seçilmedi veya liste boş.");
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void btnSendFileGcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendFileGcActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Dosya Seç");

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSend = fileChooser.getSelectedFile();

            try {
                FileInputStream fis = new FileInputStream(fileToSend);
                byte[] fileContent = new byte[(int) fileToSend.length()];
                fis.read(fileContent);
                fis.close();

                String fileName = fileToSend.getName();
                String fileContentBase64 = new String(fileContent, "UTF-8");
                String message = "File:" + fileName + ":" + fileContentBase64;

                SendMessage(message.getBytes());
            } catch (IOException ex) {
                System.out.println("Dosya okunurken bir hata oluştu: " + ex.getMessage());
            }
        }    }//GEN-LAST:event_btnSendFileGcActionPerformed

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
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Chat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSendFileGc;
    private javax.swing.JButton jButton2;
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
