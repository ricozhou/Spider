package publicGUI.communicationJPanel.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;

import publicGUI.communicationJPanel.client.CommunicationJPanel;

//服务器线程
public class ClientThread extends Thread {
	// 需要所有客户端列表
	public static HashMap<String, ClientThread> clients = new HashMap<String, ClientThread>();

	// 服务器程序读取客户端消息的读取器
	private BufferedReader reader;
	// 服务器程序向客户端发送消息的写入器
	private PrintWriter writer;
	// 当前客户端的用户名
	private String userName;
	// 当前客户端的IP地址
	private String userIp;
	// 当前客户端对应的Socket对象
	private Socket socket;

	// 初始化
	// 协议：以“#”为分割界限，第一元素为标识符命令特征，第二个为目标用户的名字IP，
	// 第三个是自己发送自己的IP，接下来是别的参数
	public ClientThread(Socket socket) {
		this.socket = socket;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());

			// 客户端建立链接后第一件事情应该是向服务器发送注册的用户名
			this.userName = reader.readLine();
			// 获取客户端的IP地址
			// this.userIp = socket.getInetAddress().getHostAddress();
			// System.out.println(userName + userIp + "加入聊天室！");
			System.out.println(userName + "加入聊天室！");
			// 告诉其他客户端，当前客户端上线
			// sendMessage("ADD#" + this.userName + "(" + this.userIp + ")");
			sendMessage("ADD#" + this.userName);
			// 将当前对象添加到服务器的客户端列表
			// clients.put(userName + "(" + userIp + ")", this);
			clients.put(userName, this);
			// writer.println("ID#" + userName + "(" + userIp + ")");
			writer.println("ID#" + userName);
			writer.flush();
			// 服务器应该给当前的客户端发送一个已经在线的用户列表
			StringBuffer str = new StringBuffer("LIST");
			// 准备用户列表
			for (Entry<String, ClientThread> client : clients.entrySet()) {
				ClientThread clientThread = client.getValue();
				// str.append("#" + clientThread.userName + "(" + clientThread.userIp + ")");
				str.append("#" + clientThread.userName);
			}
			// 发送在线用户列表给当前的客户端
			writer.println(str);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 进行消息群发
	public void sendMessage(String message) {
		for (Entry<String, ClientThread> client : clients.entrySet()) {
			ClientThread clientThread = client.getValue();
			try {
				clientThread.writer.println(message);
				clientThread.writer.flush();
			} catch (Exception ex) {
				// 客户端如果断开连接就需要移除
				// clients.remove(userName + "(" + userIp + ")");
				clients.remove(userName);
			}
		}
	}

	// 专门用来接收客户端消息并且按照约定的协议进行解析处理
	@Override
	public void run() {
		try {
			System.out.println(CommunicationJPanel.isShutDownServer);

			while (!CommunicationJPanel.isShutDownServer) {
				System.out.println(CommunicationJPanel.isShutDownServer);
				// 线程标志
				if (CommunicationJPanel.isShutDownServer) {
					return;
				} else {

					// 获取客户端的消息
					String message = reader.readLine();
					String[] msgs = message.split("#");
					System.out.println(message);
					if (msgs[0].equals("DEL")) {
						// 从服务端的客户端列表清除
						// clients.remove(this.userName + "(" + this.userIp + ")");
						clients.remove(this.userName);
						// 告诉其他客户端，当前用户下线
						sendMessage(message);
						return;
						// 私聊
					} else if (msgs[0].equals("PRI")) {
						ClientThread targetClient = clients.get(msgs[1]);
						String targName = msgs[1].substring(0, msgs[1].indexOf("("));
						// 发给自己
						this.writer.println("PRI#【" + msgs[2] + "】(私信" + targName + ")：" + msgs[3]);
						this.writer.flush();
						// 发给目标
						targetClient.getWriter()
								// .println("【" + this.userName + "(" + this.userIp + ")" + "】私信你说:" + msgs[2]);
								.println("PRI#【" + msgs[2] + "】(私信" + targName + ")：" + msgs[3]);
						targetClient.getWriter().flush();
					} else if (msgs[0].equals("REMOTE")) {
						// 拿到被控制的远程对象 REMOTE#TARGET#SOURCE
						ClientThread target = clients.get(msgs[1]);
						target.getWriter().println(message);
						target.getWriter().flush();
					} else if (msgs[0].equals("ACCEPT")) {
						// ACCEPT#TARGET#SOURCE
						ClientThread target = clients.get(msgs[2]);
						target.getWriter().println(message);
						target.getWriter().flush();
					} else if (msgs[0].equals("ALL")) {
						sendMessage("ALL#" + msgs[1]);
					} else if (msgs[0].equals("SENDFILE")) {
						// 发送文件
						ClientThread targetClient = clients.get(msgs[1]);
						targetClient.getWriter()
								.println("YNACCFILE#" + msgs[1] + "#" + msgs[2] + "#" + msgs[3] + "#" + msgs[4]);
						System.out.println(1.2);
						targetClient.getWriter().flush();
					} else if (msgs[0].equals("ACCFILE")) {
						System.out.println(1.4);
						// 接受文件
						ClientThread targetClient = clients.get(msgs[1]);
						targetClient.getWriter().println("ACCFILE#" + msgs[1] + "#" + msgs[2] + "#" + msgs[3]);
						targetClient.getWriter().flush();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public BufferedReader getReader() {
		return reader;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
