package publicGUI.communicationJPanel.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;

import publicGUI.communicationJPanel.client.CommunicationJPanel;


//通讯服务器
public class ChatServer {
	public ChatServer(int port) throws IOException {
		ServerSocket server = new ServerSocket(port);
		System.out.println("服务器正常启动，端口号" + port);
		while (!CommunicationJPanel.isShutDownServer) {
			System.out.println(CommunicationJPanel.isShutDownServer);

			ClientThread clientThread = null;
			if (!CommunicationJPanel.isShutDownServer) {
				Socket socket = server.accept();
				clientThread = new ClientThread(socket);
				clientThread.start();
			} else {
				clientThread.stop();
				return;
			}
		}
	}

}
