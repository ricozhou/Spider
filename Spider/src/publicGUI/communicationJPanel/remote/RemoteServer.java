package publicGUI.communicationJPanel.remote;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * 
 * @author jame 远程控制服务器
 *
 */
public class RemoteServer {

	public static HashMap<String, RemoteClient> clients = new HashMap<String, RemoteClient>();

	public RemoteServer(int port) throws IOException {
		ServerSocket server = new ServerSocket(port);
		while (true) {
			Socket socket = server.accept();
			RemoteClient client = new RemoteClient(socket);
			client.start();
		}

	}

}