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
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    public static boolean isRunning;
    private static List<Socket> clientSockets = new ArrayList<>();
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
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void Listen() {
        while (isRunning) {
            try {
                if (!isRunning) {
                    break;
                }

                clientSocket = serverSocket.accept();
                System.out.println("Client accepted: " + clientSocket);
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();

            } catch (IOException ex) {
                if (!isRunning) {
                    break;
                }
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            clientSockets.add(clientSocket);

            DataInputStream sInput = new DataInputStream(clientSocket.getInputStream());
            byte[] buffer = new byte[1024];
            int bytesRead = sInput.read(buffer);
            String message = new String(buffer, 0, bytesRead);
            String[] parts = message.split(",");
            String user = parts[0];
            String project = parts[1];

            //Adding user and projectname infos to these lists so i can update my active users list in Chat class.
            activeClients.add(user);
            projectNameList.add(project);

            System.out.println(message);

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
                //System.out.println(message);
                sendBroadcastMessage(message);

            }
        } catch (IOException ex) {
        } finally {
            try {
                int index = clientSockets.indexOf(clientSocket);
                String projectname = projectNameList.get(index);

                //When clients close their page, i have to update active clients list. So i removed this client from lists.
                activeClients.remove(index);
                projectNameList.remove(index);
                clientSockets.remove(clientSocket);
                byte[] buffer = new byte[1024];
                sendMessage(buffer, clientSocket);

                sendActiveClients(projectname);

                clientSocket.close();

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
            System.out.println("Server closed");

        } catch (IOException ex) {
            System.out.println("Server didn't close");
        }
    }

    public void sendBroadcastMessage(String msg) {
        StringBuilder formattedMsgBuilder = new StringBuilder();
        formattedMsgBuilder.append(msg);
        formattedMsgBuilder.append(")");

        // take the message till ) part. 
        int indexOfClosingBracket = msg.indexOf(")");
        if (indexOfClosingBracket != -1) {
            String partialMsg = msg.substring(0, indexOfClosingBracket);
            formattedMsgBuilder.append(partialMsg);
        }

        // turn into byte array
        byte[] msgBytes = formattedMsgBuilder.toString().getBytes();

        // Send message to every client
        for (Socket clientSocket : clientSockets) {
            sendMessage(msgBytes, clientSocket);
        }
    }

}
