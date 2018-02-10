package publicGUI.communicationJPanel.client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;

import login.entity.CommSetParams;
import login.entity.UserInfo;
import monitoringsystem.screenshotmonitor.ScreenShotMonitor;
import publicGUI.communicationJPanel.client.CommSet;
import publicGUI.communicationJPanel.remote.EventReciveThread;
import publicGUI.communicationJPanel.remote.ImageSendThread;
import publicGUI.communicationJPanel.remote.RemoteServer;
import publicGUI.communicationJPanel.remote.RemoteUI;
import publicGUI.communicationJPanel.server.ChatServer;
import publicGUI.entity.PassParameter;
import publicGUI.entity.SetParams;
import publicGUI.utils.GetSetProperties;
import taobaojud.background.controller.MainGame;
import taobaojud.background.entity.ReturnMessage;
import taobaojud.prosceniumgui.maingame.TaoBaoMainUI;

//通讯GUI
public class CommunicationJPanel extends JPanel implements ActionListener {
	public JFrame jf;
	public PassParameter passParameter;
	public UserInfo userInfo;
	public CommSetParams commSetParams;
	public JLabel jlb1, jlb2, jlb3, jlb4, jlb5;
	public JButton button1, button2, button3, button4, button5, button6, button7, button8;
	public JTextArea jta1, jta3;
	public JTextField jta2;
	// 滚动条
	public JScrollPane jsp1, jsp2, jsp3;
	// 列表
	public JList jl1, jl2;
	// 数据模型
	public DefaultListModel userData;
	public JProgressBar jpbar1;
	// 与服务器交互的两个非常重的流
	public Socket socket;
	public BufferedReader reader;
	public PrintWriter writer;
	public String clientId;// 本客户端的唯一标志
	public String name;
	public String userName = null;
	public String privateName2;
	public String filePath;
	public String ip;
	public List<String> megList = new ArrayList<String>();
	// 标记当前是否已经连接到服务器
	public boolean isConnect = false;
	public static boolean isSendFile = false;
	public static boolean isReceiveFile = false;
	public static boolean isStartServer = false;
	public static boolean isShutDownServer = false;
	public static boolean isGetFocus = false;

	public GetSetProperties gsp = new GetSetProperties();

	public CommunicationJPanel(PassParameter passParameter, JFrame jf) {
		// 将之前窗口对象传递过来
		this.jf = jf;
		this.passParameter = passParameter;
		init();
	}

	// 构造方法
	private void init() {
		userInfo = passParameter.getUserInfo();
		commSetParams = passParameter.getCommSetParams();
		button1 = new JButton("设置");
		button1.setBounds(10, 7, 60, 25);
		button1.addActionListener(this);
		button2 = new JButton("上线");
		button2.setBounds(72, 7, 60, 25);
		button2.addActionListener(this);
		button3 = new JButton("下线");
		button3.setBounds(135, 7, 60, 25);
		button3.setEnabled(false);
		button3.addActionListener(this);
		button4 = new JButton("清除缓存");
		button4.setBounds(197, 7, 80, 25);
		button4.addActionListener(this);
		button6 = new JButton("远程控制");
		button6.setEnabled(false);
		button6.setBounds(280, 7, 80, 25);
		button6.addActionListener(this);
		button7 = new JButton("启动");
		button7.setBounds(361, 7, 60, 25);
		button7.addActionListener(this);
		button8 = new JButton("关闭");
		button8.setBounds(422, 7, 60, 25);
		button8.setEnabled(false);
		button8.addActionListener(this);
		// 只有管理员才能启动服务
		if ("admin".equals(userInfo.getUserName()) || "superadmin".equals(userInfo.getUserName())) {
			button7.setVisible(true);
			button8.setVisible(true);
		} else {
			button7.setVisible(false);
			button8.setVisible(false);
		}

		// 消息区
		jta1 = new JTextArea();
		// 设置为不可编辑
		jta1.setEditable(false);
		jsp1 = new JScrollPane(jta1);
		jsp1.setBounds(10, 35, 350, 240);

		// 发送区
		jta2 = new JTextField();
		// jsp2 = new JScrollPane(jta2);
		jta2.setBounds(10, 276, 350, 57);
		jta2.addFocusListener(new FocusListener() {
			// 光标不在文本域
			@Override
			public void focusLost(FocusEvent e) {
				isGetFocus = false;
			}

			// 光标在文本域
			@Override
			public void focusGained(FocusEvent e) {
				isGetFocus = true;
			}
		});
		jta2.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			// 监听回车发送消息操作
			@Override
			public void keyPressed(KeyEvent e) {
				if (isGetFocus) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						sendMessage();
					}
				}
			}
		});

		// 用户列表
		userData = new DefaultListModel();
		jl1 = new JList(userData);
		this.jl1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// 双击发送文件
				if (e.getClickCount() == 2) {
					int selectedIndex = jl1.getSelectedIndex();
					privateName2 = jl1.getSelectedValue().toString();
					try {
						userName = name + "(" + InetAddress.getLocalHost().getHostAddress() + ")";
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					}
					if (privateName2.equals(userName)) {
						JOptionPane.showMessageDialog(null, "不能向自己发送文件");
						return;
					}
					// 双击打开文件文件选择框
					JFileChooser chooser = new JFileChooser();
					chooser.setDialogTitle("选择文件框");
					chooser.showDialog(jf, "选择");

					// 判定是否选择了文件
					if (chooser.getSelectedFile() != null) {
						// 获取路径
						filePath = chooser.getSelectedFile().getPath();
						File file = new File(filePath);
						// 文件为空
						if (file.length() == 0) {
							JOptionPane.showMessageDialog(null, filePath + "文件为空,不允许发送.");
							return;
						}

						if (!isConnect) {
							return;
						}
						if (selectedIndex == 0) {
							JOptionPane.showMessageDialog(null, "不能群发文件");
							return;
						} else {
							// SENDFILE#目标#本地#文件名#文件长度
							writer.println("SENDFILE#" + privateName2 + "#" + userName + "#" + file.getName() + "#"
									+ new Long(file.length()).intValue());
							System.out.println(1.1);
						}
						writer.flush();

					}
				}
			}
		});

		userData.addElement("群聊");
		// 默认选中群聊
		jl1.setSelectedIndex(0);
		jsp3 = new JScrollPane(jl1);
		// jsp3.setBorder(new TitledBorder("在线用户列表"));
		jsp3.setBounds(361, 35, 110, 163);

		// 文件传输栏
		jlb1 = new JLabel("文件传输：");
		jlb1.setBounds(361, 199, 110, 20);
		jlb1.setVisible(false);

		jlb2 = new JLabel();
		jlb2.setBounds(361, 220, 110, 20);
		jlb2.setVisible(false);

		jlb3 = new JLabel();
		jlb3.setBounds(361, 241, 110, 20);
		jlb3.setVisible(false);

		jpbar1 = new JProgressBar();
		jpbar1.setBounds(361, 262, 110, 14);
		jpbar1.setMinimum(1);
		jpbar1.setMaximum(100);
		jpbar1.setVisible(false);

		button5 = new JButton("发送消息");
		button5.setBounds(361, 279, 110, 50);
		button5.addActionListener(this);
		button5.setEnabled(false);

		this.add(button1);
		this.add(button2);
		this.add(button3);
		this.add(button4);
		this.add(button5);
		this.add(button6);
		this.add(button7);
		this.add(button8);
		this.add(jsp1);
		this.add(jta2);
		this.add(jsp3);
		this.add(jpbar1);
		this.add(jlb1);
		this.add(jlb2);
		this.add(jlb3);
		this.setLayout(null);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object key = e.getSource();
		// 监听设置
		if (key.equals(button1)) {
			// 打开弹窗
			CommSet cs = new CommSet(commSetParams);
		}
		// 监听上线
		if (key.equals(button2)) {
			CommSetParams csp = null;
			try {
				csp = gsp.getCommSetProperties();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			ip = csp.getServerIP();
			int port = Integer.parseInt(csp.getServerPort());
			name = csp.getUserName();
			// 连接
			if (connectServer(ip, port, name)) {
				button2.setEnabled(false);
				button3.setEnabled(true);
				button6.setEnabled(true);
				button5.setEnabled(true);
				jta2.setEditable(true);
				jl1.setSelectedIndex(0);
				isConnect = true;

			} else {
				button2.setEnabled(true);
				button3.setEnabled(false);
				button6.setEnabled(false);
				button5.setEnabled(false);
				jta2.setEditable(false);
				isConnect = false;
			}

		}
		// 监听下线
		if (key.equals(button3)) {
			if (isSendFile || isReceiveFile) {
				JOptionPane.showMessageDialog(null, "正在传输文件中，请勿离开！", "提示信息", JOptionPane.ERROR_MESSAGE);
			} else {
				writer.println("DEL#" + clientId);
				writer.flush();
				userData.clear();
				userData.add(0, "群聊");
				button2.setEnabled(true);
				button3.setEnabled(false);
				button6.setEnabled(false);
				button5.setEnabled(false);
				jta2.setEditable(false);
				jlb1.setVisible(false);
				jlb2.setVisible(false);
				jlb3.setVisible(false);
				jlb2.setText("");
				jlb3.setText("");
				jpbar1.setVisible(false);
				jpbar1.setValue(0);
				jl1.setSelectedIndex(0);
				isConnect = false;
			}
		}
		// 清除缓存
		if (key.equals(button4)) {
			jta1.setText("");
			jlb1.setVisible(false);
			jlb2.setVisible(false);
			jlb3.setVisible(false);
			jlb2.setText("");
			jlb3.setText("");
			jpbar1.setVisible(false);
			jpbar1.setValue(0);
			megList.removeAll(megList);
		}
		// 发送消息
		if (key.equals(button5)) {
			sendMessage();
		}
		// 远程控制
		if (key.equals(button6)) {
			if (!isConnect) {
				return;
			}
			// 获取右侧用户列表的选择对象
			int selectedIndex = jl1.getSelectedIndex();
			if (selectedIndex != 0) {
				String privateName = jl1.getSelectedValue().toString();
				writer.println("REMOTE#" + privateName + "#" + clientId);
				writer.flush();
			} else {
				JOptionPane.showMessageDialog(null, "请选择被控制对象！");
			}
		}
		// 监听启动服务
		if (key.equals(button7)) {

			// 启动线程，以防止阻塞其他程序
			SwingWorker<String, String> sw = new SwingWorker<String, String>() {
				// 此方法处理耗时任务
				@Override
				protected String doInBackground() throws Exception {
					CommSetParams csp;
					try {
						// 读取端口号
						csp = gsp.getCommSetProperties();
						System.out.println(csp);
						isShutDownServer = false;
						isStartServer = true;
						// 初始化服务器
						ChatServer cs = new ChatServer(Integer.parseInt(csp.getServerPort()));
						return "0";
					} catch (IOException e1) {
						isStartServer = false;
						JOptionPane.showMessageDialog(null, "服务器启动失败！", "提示消息", JOptionPane.WARNING_MESSAGE);
						e1.printStackTrace();
						return "1";
					}
				}
			};
			sw.execute();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			// System.out.println(111);
			// System.out.println(isStartServer);
			if (isStartServer) {
				button7.setEnabled(false);
				button8.setEnabled(true);
			} else {
				button7.setEnabled(true);
				button8.setEnabled(false);
			}

			// 同时启动远程控制服务
			SwingWorker<String, String> sw2 = new SwingWorker<String, String>() {
				// 此方法处理耗时任务
				@Override
				protected String doInBackground() throws Exception {
					try {
						// 初始化服务器
						RemoteServer cs = new RemoteServer(8881);
						return "0";
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "远程控制服务器启动失败！", "提示消息", JOptionPane.WARNING_MESSAGE);
						e1.printStackTrace();
						return "1";
					}
				}
			};
			sw2.execute();

		}
		// 监听关闭服务
		if (key.equals(button8)) {
			// isShutDownServer = true;
			// button7.setEnabled(true);
			// button8.setEnabled(false);
		}
	}

	// 发送消息
	public void sendMessage() {
		if (!isConnect) {
			return;
		}
		if ("".equals(jta2.getText().trim())) {
			JOptionPane.showMessageDialog(null, "内容不能为空！");
			return;
		}
		// 获取右侧用户列表的选择对象
		String privateName = jl1.getSelectedValue().toString();
		try {
			userName = name + "(" + InetAddress.getLocalHost().getHostAddress() + ")";
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		int selectedIndex = jl1.getSelectedIndex();
		if (selectedIndex == 0) {
			writer.println("ALL#【" + name + "】：" + jta2.getText().replaceAll("#", ""));
		} else if (privateName.equals(userName)) {
			JOptionPane.showMessageDialog(null, "不能向自己发送！");
			return;
		} else {
			writer.println("PRI#" + privateName + "#" + name + "#" + jta2.getText().replaceAll("#", ""));
		}
		writer.flush();
		jta2.setText("");
	}

	// 连接服务
	private boolean connectServer(String ip, int port, String name) {
		boolean result = false;
		try {
			// 新建socket
			socket = new Socket(ip, port);
			// 获取输出流
			writer = new PrintWriter(socket.getOutputStream());
			// 输入流
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			userName = name + "(" + InetAddress.getLocalHost().getHostAddress() + ")";
			// 连接服务器后立即发送用户名
			writer.println(userName);
			writer.flush();
			MessageThread msgThread = new MessageThread(reader, jta1, writer);
			msgThread.start();
			result = true;
		} catch (Exception ex) {
			result = false;
		}
		return result;
	}

	// 消息线程处理
	class MessageThread extends Thread {
		private BufferedReader reader;
		private PrintWriter writer;
		private JTextArea jta1;

		public MessageThread(BufferedReader reader, JTextArea jta1, PrintWriter writer) {
			this.reader = reader;
			this.jta1 = jta1;
			this.writer = writer;
		}

		public void run() {
			try {
				// 一直读取服务器转发的消息
				while (true) {
					String message = reader.readLine();
					System.out.println(message);
					String msgs[] = message.split("#");
					// 连接上之后服务器发送的连接上的用户名添加至列表
					if (msgs[0].equals("ADD")) {
						userData.addElement(msgs[1]);
					} else if (msgs[0].equals("DEL")) {
						System.out.println("客户端收到删除请求：" + message);
						userData.removeElement(msgs[1]);
					} else if (msgs[0].equals("LIST")) {
						// 一上线服务器发来在线列表，遍历添加
						for (int i = 1; i < msgs.length; i++) {
							userData.addElement(msgs[i]);
						}
					} else if (msgs[0].equals("ID")) {
						clientId = msgs[1];
					} else if (msgs[0].equals("REMOTE")) {
						int result = JOptionPane.showConfirmDialog(null, "【" + msgs[2] + "】想控制你的电脑！");
						if (result == JOptionPane.YES_OPTION) {

							writer.println(message.replace("REMOTE", "ACCEPT"));
							writer.flush();
							// 连接远程服务器 告诉远程服务器跟谁对接 toDo
							Socket socket = new Socket(ip, 8881);
							PrintWriter wr = new PrintWriter(socket.getOutputStream());
							wr.println(msgs[1] + "#" + msgs[2]);
							wr.flush();
							// 启动桌面影像传输的子线程 和 事件接收的子线程 toDo
							ImageSendThread imageThread = new ImageSendThread(
									new DataOutputStream(socket.getOutputStream()));
							imageThread.start();
							// 这个地方并没有启动线程，如果他的输入流没有收到内容的话
							EventReciveThread eventThread = new EventReciveThread(
									new ObjectInputStream(socket.getInputStream()));
							eventThread.start();
						}

					} else if (msgs[0].equals("ACCEPT")) {
						// 连接远程服务器 告诉远程服务器跟谁对接 toDo
						Socket socket = new Socket(ip, 8881);
						PrintWriter wr = new PrintWriter(socket.getOutputStream());
						wr.println(msgs[2] + "#" + msgs[1]);
						wr.flush();
						// 启动UI界面
						RemoteUI ui = new RemoteUI(msgs[1] + "的电脑桌面", socket);
					} else if (msgs[0].equals("YNACCFILE")) {
						System.out.println("是否接受文件？");
						// 清空显示框
						jlb2.setText("");
						jlb3.setText("");

						final String fileName = msgs[3];
						final String sendUserName = msgs[2];
						final int fileSize = Integer.parseInt(msgs[4]);
						// 由于等待目标客户确认是否接收文件是个阻塞状态，所以这里用线程处理
						new Thread() {
							public void run() {
								// 显示是否接收文件对话框
								int result = JOptionPane.showConfirmDialog(null, "是否接受？");
								if (result == 0) {
									// 接收文件
									JFileChooser chooser = new JFileChooser();
									chooser.setDialogTitle("保存文件框");
									// 默认文件名称还有放在当前目录下
									chooser.setSelectedFile(new File(fileName));
									chooser.showDialog(jf, "保存");
									// 保存路径
									String saveFilePath = chooser.getSelectedFile().toString();

									// 创建新的tcp socket 接收数据
									try {
										ServerSocket ss = new ServerSocket(1235);

										// String ip = socket.getInetAddress().getHostAddress();
										String ip = InetAddress.getLocalHost().getHostAddress();
										int port = 1235;
										System.out.println(1.3);
										writer.println("ACCFILE#" + sendUserName + "#" + ip + "#" + port);
										writer.flush();
										System.out.println(1.31);

										isReceiveFile = true;
										// 等待文件来源的客户，输送文件....目标客户从网络上读取文件，并写在本地上
										Socket sk = ss.accept();
										jta1.append(fileName + "：文件保存中.\r\n");
										DataInputStream dis = new DataInputStream( // 从网络上读取文件
												new BufferedInputStream(sk.getInputStream()));
										DataOutputStream dos = new DataOutputStream( // 写在本地上
												new BufferedOutputStream(new FileOutputStream(saveFilePath)));
										jlb1.setVisible(true);
										jlb2.setVisible(true);
										jlb3.setVisible(true);
										jpbar1.setVisible(true);
										int count = 0;
										int num = fileSize / 100;
										int index = 0;
										while (count < fileSize) {
											int t = dis.read();
											dos.write(t);
											count++;

											if (num > 0) {
												if (count % num == 0 && index < 100) {
													jpbar1.setValue(++index);
												}
												jlb2.setText(count + "/" + fileSize);
												jlb3.setText("整体" + index + "%");

											} else {
												jlb2.setText(count + "/" + fileSize);
												jlb3.setText("整体:"
														+ new Double(new Double(count).doubleValue()
																/ new Double(fileSize).doubleValue() * 100).intValue()
														+ "%");
												if (count == fileSize) {
													jpbar1.setValue(100);
												}
											}

										}

										// 给文件来源客户发条提示，文件保存完毕
										PrintWriter out = new PrintWriter(sk.getOutputStream(), true);
										out.println(" 发送给" + name + "的文件[" + fileName + "]" + "文件保存完毕.\r\n");
										out.flush();
										dos.flush();
										dos.close();
										out.close();
										dis.close();
										sk.close();
										ss.close();
										System.out.println(4);
										jta1.append(fileName + "文件保存完毕.存放位置为:" + saveFilePath + "\r\n");
										isReceiveFile = false;
									} catch (Exception e) {
										e.printStackTrace();
									}

								} else {

								}
							};
						}.start();

					} else if (msgs[0].equals("ACCFILE")) {
						jta1.append(privateName2 + "确定接收文件,文件传送中..\r\n");
						// 清空显示框
						jlb2.setText("");
						jlb3.setText("");
						// 对方接收方ip
						final String ip = msgs[2];
						// 端口
						final String port = msgs[3];
						new Thread() {
							public void run() {

								try {
									isSendFile = true;
									// 创建要接收文件的客户套接字
									System.out.println(1.5);
									Socket s = new Socket(ip, Integer.parseInt(port));
									System.out.println(filePath);
									DataInputStream dis = new DataInputStream(new FileInputStream(filePath)); // 本地读取该客户刚才选中的文件
									DataOutputStream dos = new DataOutputStream(
											new BufferedOutputStream(s.getOutputStream()));
									jlb1.setVisible(true);
									jlb2.setVisible(true);
									jlb3.setVisible(true);
									jpbar1.setVisible(true);
									int size = dis.available();
									int count = 0; // 读取次数
									int num = size / 100;
									int index = 0;
									while (count < size) {

										int t = dis.read();
										dos.write(t);
										count++; // 每次只读取一个字节

										if (num > 0) {
											if (count % num == 0 && index < 100) {
												jpbar1.setValue(++index);

											}
											jlb2.setText(count + "/" + size);
											jlb3.setText("整体" + index + "%");
										} else {
											jlb2.setText(count + "/" + size);
											jlb3.setText("整体:" + new Double(new Double(count).doubleValue()
													/ new Double(size).doubleValue() * 100).intValue() + "%");
											if (count == size) {
												jpbar1.setValue(100);
											}
										}
									}
									dos.flush();
									dis.close();
									// 读取目标客户的提示保存完毕的信息...
									BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
									jta1.append(br.readLine() + "\r\n");
									isSendFile = false;
									br.close();
									s.close();
									System.out.println(2);
								} catch (Exception ex) {
									ex.printStackTrace();
								}

							};
						}.start();

					} else {
						System.out.println(msgs[1]);
						// String tarName = jl1.getSelectedValue().toString().substring(0,
						// jl1.getSelectedValue().toString().indexOf("("));
						// 先把每一条消息加入消息列表
						megList.add(msgs[1]);
						jta1.setText("");
						// 遍历，为了区分自己的消息还是他人的
						// 英文处理

						// 中文处理
						for (String meg : megList) {
							// 此消息包含名称和消息，先区分
							String sendName = meg.substring(meg.indexOf("【") + 1, meg.indexOf("】"));
							String sendMeg = meg.substring(meg.indexOf("：") + 1);
							// 如果是自己发的,则靠右显示
							if (sendName.equals(name)) {
								StringBuilder spaceStr = new StringBuilder();
								// if (meg.substring(meg.indexOf("】") + 1).length() > 3) {
								// if ("私信".equals(meg.substring(meg.indexOf("：") + 2, meg.indexOf("：") + 4))) {
								//
								// }
								// }
								int selfMegLen;
								// if ("(".equals(meg.substring(meg.indexOf("：") + 1, meg.indexOf("：") + 2))) {
								// // 私信
								// selfMegLen = 55 - meg.length() + 4 + tarName.length();
								//
								// } else {
								selfMegLen = 55 - meg.length();
								// }
								for (int i = 0; i < selfMegLen * 2 - 51; i++) {
									spaceStr.append("  ");
								}
								meg = spaceStr.toString() + sendMeg + "：【" + sendName + "】";
							}

							jta1.append(meg + "\r\n");
						}
						// 设置滚动条在最下方
						jta1.setCaretPosition(jta1.getText().length());
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "服务器出现异常！");
			}
		}
	}

}
