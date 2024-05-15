/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author merveog
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    public static boolean isRunning;
    private DefaultListModel listofMessages = new DefaultListModel();
    private static List<Socket> clientSockets = new ArrayList<>();
    private List<Socket> clientSocketsForMessage = new ArrayList<>();
    private List<String> projectNameList = new ArrayList<>();
    private List<String> activeClients = new ArrayList<>();

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }

    public void startServer() {
        try {
            int port = 3000;
            serverSocket = new ServerSocket(port);
            isRunning = true;
            System.out.println("Server Started");

            Thread listenThread = new Thread(() -> Listen());
            listenThread.start();

        } catch (NumberFormatException e) {

        } catch (IOException e) {

        }
    }

    public void Listen() {
        //clientSockets.clear();
        while (isRunning) {
            try {
                if (!isRunning) {
                    break;
                }
                clientSocket = serverSocket.accept();
                System.out.println("Client accepted: " + clientSocket);

//                activeClients.add(clientSocket);
//                for (Socket clientSocket1 : clientSockets) {
//                    sendBroadcastMessage("A" + clientSocket1);
//                }
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            } catch (IOException ex) {
                if (!isRunning) {
                    break;
                }
                //Logger.getLogger(ServerForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            //clientSockets.clear();
            clientSockets.add(clientSocket);

            clientSocketsForMessage.add(clientSocket);

            DataInputStream sInput = new DataInputStream(clientSocket.getInputStream());
            byte[] buffer = new byte[1024];
            int bytesRead = sInput.read(buffer);
            String message = new String(buffer, 0, bytesRead);
            String[] parts = message.split(",");
            String user = parts[0];
            String project = parts[1];

            activeClients.add(user);
            projectNameList.add(project);

            //
            System.out.println(message);
            listofMessages.addElement(message);

            // buffer = "Server connected to client".getBytes();
            sendMessage(buffer, clientSocket);
            sendActiveClients(project);
            while (isRunning) {
                bytesRead = sInput.read(buffer);
                if (bytesRead == -1) {
                    break;
                }
                if (!isRunning) {
                    break;
                }
                message = new String(buffer, 0, bytesRead);
                System.out.println(message);
                listofMessages.addElement(message);
                clientSocketsForMessage.add(clientSocket);
                sendBroadcastMessage(message);

            }
        } catch (IOException ex) {
            // Logger.getLogger(ServerForm.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                int index = clientSockets.indexOf(clientSocket);
                String projectname = projectNameList.get(index);
                //if (clientSocket != null && !clientSocket.isClosed()) {
                // if (index != -1 && index < activeClients.size() && index < projectNameList.size()) {
                activeClients.remove(index);
                projectNameList.remove(index);
                clientSockets.remove(clientSocket);
                sendActiveClients(projectname);
                // } else {
                //  System.out.println("Index out of bounds.");
                //}
                clientSocket.close();
                // }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendActiveClients(String projectName) {
        StringBuilder messageBuilder = new StringBuilder();

        for (int i = 0; i < activeClients.size(); i++) {
            if (projectNameList.get(i).equals(projectName)) {
                messageBuilder.append(activeClients.get(i)).append(",");
            }
        }
        //sendProjectGc("2," + messageBuilder.toString(), projectname);
        sendBroadcastMessage("a," + messageBuilder.toString() + projectName + ", ");
    }

    public void sendMessage(byte[] msg, Socket clientSocket) {
        try {
            DataOutputStream sOutput = new DataOutputStream(clientSocket.getOutputStream());
            msg[msg.length - 1] = 0x14;
            sOutput.write(msg);
        } catch (IOException err) {
        }
    }

    public void stopServer() {
        try {
            isRunning = false;
            serverSocket.close();
            System.out.println("Server stopped");

        } catch (IOException ex) {
            //    Logger.getLogger(ServerForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void selectedPerson(String projectName, String userName, String message) {
        // İlgili mesajı oluştur
//        String fullMessage = userName + "," + projectName + "," + message;
//
//        // İlgili projeye ve kullanıcıya gönderilecek soketleri bul
//        List<Socket> socketsToSend = new ArrayList<>();
//        for (int i = 0; i < usernameList.size(); i++) {
//            if (usernameList.get(i).equals(userName)) {
//                // Kullanıcı adı eşleşirse, aynı indeksteki soketi al
//                Socket userSocket = clientSockets.get(i);
//                socketsToSend.add(userSocket);
//                break; // İlgili kullanıcıyı bulduğumuzda döngüyü sonlandırabiliriz
//            }
//        }
//
//        // İlgili projeye gönder
//        for (int i = 0; i < projectNameList.size(); i++) {
//            if (projectNameList.get(i).equals(projectName)) {
//                // Proje adı eşleşirse, aynı indeksteki soketi al
//                Socket projectSocket = clientSockets.get(i);
//                socketsToSend.add(projectSocket);
//                break; // İlgili projeyi bulduğumuzda döngüyü sonlandırabiliriz
//            }
//        }
//
//        // Oluşturulan mesajı ilgili soketlere gönder
//        byte[] msgBytes = fullMessage.getBytes();
//        for (Socket socket : socketsToSend) {
//            sendMessage(msgBytes, socket);
//        }
    }

    public void sendProjectGc(String projectName, String message) {

    }

//    public void sendBroadcastMessage(String msg) {
//        //String msg = "Server: " + txtASendM.getText();
//        byte[] msgBytes = msg.getBytes();
//        for (Socket clientSocket : clientSockets) {
//            sendMessage(msgBytes, clientSocket);
//        }
//    }
//    public void sendBroadcastMessage(String msg) {
//        // Mesajı belirlenen formatla başlatın ve bitirin
//        String formattedMsg = "(" + msg + ")";
//
//        // Byte dizisine çevirin
//        byte[] msgBytes = formattedMsg.getBytes();
//
//        // Tüm bağlı istemcilere mesajı gönderin
//        for (Socket clientSocket : clientSockets) {
//            sendMessage(msgBytes, clientSocket);
//        }
//    }
    public void sendBroadcastMessage(String msg) {
        // Mesajı belirlenen formatla başlatın ve bitirin
        StringBuilder formattedMsgBuilder = new StringBuilder();
        formattedMsgBuilder.append(msg);
        formattedMsgBuilder.append(")");

        // Mesajda ')' işaretine kadar olan kısmı alın
        int indexOfClosingBracket = msg.indexOf(")");
        if (indexOfClosingBracket != -1) {
            String partialMsg = msg.substring(0, indexOfClosingBracket);
            formattedMsgBuilder.append(partialMsg);
        }

        // Byte dizisine çevirin
        byte[] msgBytes = formattedMsgBuilder.toString().getBytes();

        // Tüm bağlı istemcilere mesajı gönderin
        for (Socket clientSocket : clientSockets) {
            sendMessage(msgBytes, clientSocket);
        }
    }

}
