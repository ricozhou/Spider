package taobaojud.prosceniumgui.maingame;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/* 
 * 功能：司法拍卖系统
 * 步骤1：登录界面的静态实现 
 * 步骤2：添加对各个组件的监听。 
 * 步骤3：对用户名和密码进行验证。 
 * author：ywq 
 */
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import login.entity.CommSetParams;
import login.entity.UserInfo;
import login.login.ProGUILogin;
import publicGUI.communicationJPanel.client.CommunicationJPanel;
import publicGUI.customApplication.CustomApplicationJPanel;
import publicGUI.entity.PassParameter;
import publicGUI.entity.SetParams;
import publicGUI.gameJPanel.GameJPanel;
import publicGUI.setJPanel.MinimizeTrayJPanel;
import publicGUI.setJPanel.SetJPanel;
import publicGUI.toolJPanel.Screenshot;
import publicGUI.toolJPanel.ToolJPanel;
import publicGUI.utils.GetSetProperties;
import publicGUI.utils.SaveSet;
import taobaojud.background.controller.MainGame;
import taobaojud.background.entity.ReturnMessage;
import taobaojud.background.service.AnalyzeUrl;
import taobaojud.prosceniumgui.entity.Params;
import taobaojud.prosceniumgui.utils.BasicUtils;
import taobaojud.prosceniumgui.utils.GuiUtils;

public class TaoBaoMainUI extends JFrame implements ActionListener, KeyListener {
	// 定义组件
	// 菜单栏
	JMenuBar menub;
	JMenu files;
	JMenu formats;
	JMenu help;
	JMenuItem newWindow;
	JMenuItem cut;
	JMenuItem exit;
	JMenuItem fonts;
	JMenuItem about;

	// 进度条
	Timer timer;
	JProgressBar jpbProcessLoading;
	JButton button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11 = null;
	JPanel jp1, jp2, jp3 = null;
	JLabel jlb1, jlb2, jlb3, jlb4, jlp8, jlp13, jlp14, jlp15, jlp16 = null;
	// 复选框
	JCheckBox jcbox1, jcbox2, jcbox3, jcbox4, jcbox5;
	// 文件选择器
	JFileChooser jfc;
	JTextField tt1;
	JLabel jlp5 = new JLabel("");
	JLabel jlp6 = new JLabel("");
	JLabel jlp7 = new JLabel("");
	JLabel jlp12 = new JLabel("");
	JRadioButton jrb1, jrb2, jrb3 = null;
	ButtonGroup bg = null;
	long startJsTime;
	// 下拉框
	private JComboBox cjcb;
	private JComboBox jcb, jcb2, jcb3;
	public BasicUtils bu = new BasicUtils();
	public GuiUtils gu = new GuiUtils();
	public GetSetProperties gsp = new GetSetProperties();
	String province = (String) gu.getProvince()[0];
	SwingWorker<ReturnMessage, String> sw;
	CountThread thread;
	JTabbedPane tp = new JTabbedPane(JTabbedPane.TOP);
	private String[] tabNames = { "解析系统", "小工具", "小游戏", "小通讯", "自定义应用", "小设置" };
	SetParams setParams = new SetParams();
	SaveSet ss = new SaveSet();
	public PassParameter passParameter;
	public JFrame jf;
	// 全局静态变量控制中止线程
	public static boolean taoBaoJudFlag = true;

	// 构造函数
	public TaoBaoMainUI(PassParameter passParameter) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException, IOException {
		initMenu();
		this.jf = jf;
		passParameter.setSetParams(initSet());
		this.passParameter = passParameter;
		init();
	}

	// 加载设置信息
	public SetParams initSet() throws IOException {
		setParams = gsp.getSetMessage();
		if (!setParams.isRememberFilePath()) {
			setParams.setFilePath("E:\\");
		}
		return setParams;
	}

	// 初始化菜单栏
	private void initMenu() {
		// 加载菜单栏
		menub = new JMenuBar();
		files = new JMenu("File(F)");
		formats = new JMenu("Format(O)");
		help = new JMenu("Help(H)");
		newWindow = new JMenuItem("新窗口(N)");
		cut = new JMenuItem("切换(T)");
		exit = new JMenuItem("退出(X)");
		fonts = new JMenuItem("字体...");
		about = new JMenuItem("关于Reptile(A)");

		files.setMnemonic('F');
		formats.setMnemonic('O');
		help.setMnemonic('H');
		exit.setMnemonic('X');
		cut.setMnemonic('T');
		newWindow.setMnemonic('N');
		about.setMnemonic('A');
		// 为控件添加助记符
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		newWindow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		// 为控件添加快捷键
		files.add(newWindow);
		files.add(cut);
		files.add(exit);
		formats.add(fonts);
		help.add(about);
		menub.add(files);
		menub.add(formats);
		menub.add(help);
		this.setJMenuBar(menub);
		Listen listen = new Listen();
		exit.addActionListener(listen);
		cut.addActionListener(listen);
		newWindow.addActionListener(listen);
		about.addActionListener(listen);
		fonts.setEnabled(false);
	}

	public void init() throws IOException {
		// String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		// UIManager.setLookAndFeel(lookAndFeel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 创建组件
		JPanel jp1 = new JPanel();
		JPanel jp2 = new JPanel();
		JPanel jp3 = new JPanel();
		JPanel jp4 = new JPanel();

		jp1.setLayout(null);
		jp2.setLayout(null);
		jp3.setLayout(null);
		jp4.setLayout(null);

		// 选择标的物类型和状态
		JLabel jlp9 = new JLabel();
		jlp9.setText("请选择标的物的类型：");
		jlp9.setBounds(20, 20, 130, 25);
		jp1.add(jlp9);
		// 类型
		jcb2 = new JComboBox();
		jcb2.setBounds(160, 20, 80, 25);
		jp1.add(jcb2);
		jcb2.setModel(new DefaultComboBoxModel(gu.getSubjectType()));
		// 监听选择事件
		jcb2.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (jcb2.getSelectedItem().equals("土地")) {
					// 详细信息可用
					jrb3.setEnabled(true);
				} else {
					jrb3.setEnabled(false);
				}
			}
		});

		// 选择状态
		JLabel jlp10 = new JLabel();
		jlp10.setText("请选择拍卖状态：");
		jlp10.setBounds(260, 20, 120, 25);
		jp1.add(jlp10);
		// 状态
		jcb3 = new JComboBox();
		jcb3.setBounds(370, 20, 80, 25);
		jp1.add(jcb3);
		jcb3.setModel(new DefaultComboBoxModel(gu.getSubjectStatus()));

		// 所在地
		JLabel jlp1 = new JLabel();
		jlp1.setText("请选择标的物所在地：");
		jlp1.setBounds(20, 50, 130, 25);
		jp1.add(jlp1);

		// 获取默认的市/县
		jcb = new JComboBox();
		jcb.setBounds(160, 50, 80, 25);
		jp1.add(jcb);
		jcb.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) { // 选项状态更改事件
				itemChange();
			}
		});
		jcb.setModel(new DefaultComboBoxModel(gu.getProvince())); // 添加省份信息

		JLabel jlp2 = new JLabel();
		jlp2.setText("省");
		jlp2.setBounds(250, 50, 30, 25);
		jp1.add(jlp2);

		cjcb = new JComboBox();
		cjcb.setBounds(280, 50, 80, 25);
		jp1.add(cjcb);
		cjcb.setModel(new DefaultComboBoxModel(gu.getCity(province)));

		JLabel jlp3 = new JLabel();
		jlp3.setText("市（县）");
		jlp3.setBounds(370, 50, 80, 25);
		jp1.add(jlp3);

		button1 = new JButton();
		button1.setBounds(20, 230, 75, 25);
		jp1.add(button1);
		button1.setText("开始");
		button1.addActionListener(this); // 添加事件处理

		// 强制中止线程
		button6 = new JButton();
		button6.setBounds(110, 230, 75, 25);
		jp1.add(button6);
		button6.setText("中止");
		button6.setEnabled(false);
		button6.addActionListener(this); // 添加事件处理

		button2 = new JButton();
		button2.setBounds(200, 230, 75, 25);
		jp1.add(button2);
		button2.setText("重置");
		button2.addActionListener(this); // 添加事件处理

		button7 = new JButton();
		button7.setBounds(290, 230, 75, 25);
		jp1.add(button7);
		button7.setText("切换");
		button7.addActionListener(this); // 添加事件处理

		button8 = new JButton();
		button8.setBounds(380, 230, 75, 25);
		jp1.add(button8);
		button8.setText("退出");
		button8.addActionListener(this); // 添加事件处理

		JLabel jlp4 = new JLabel("请选择导出文件目录：");
		tt1 = new JTextField();// TextField 目录的路径
		tt1.setText(setParams.getFilePath());
		tt1.setEditable(false);
		// 每次文本框改变都要记住最新路径
		tt1.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// 保存到缓存对象
				setParams.setFilePath(tt1.getText());
			}

			// 改变是时候
			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});
		button3 = new JButton("文件夹");// 选择
		jfc = new JFileChooser();// 文件选择器
		jfc.setCurrentDirectory(new File(setParams.getFilePath()));// 文件选择器的初始目录定为d盘
		jlp4.setBounds(20, 80, 150, 25);
		tt1.setBounds(160, 80, 200, 25);
		button3.setBounds(370, 80, 80, 25);
		button3.addActionListener(this); // 添加事件处理
		jp1.add(jlp4);
		jp1.add(tt1);
		jp1.add(button3);

		// 判断是否全部获取
		jlp8 = new JLabel("请选择是否全部爬取：");
		jrb1 = new JRadioButton("全部爬取");
		jrb2 = new JRadioButton("爬取首页");
		jrb3 = new JRadioButton("详细信息");
		jrb3.setEnabled(false);
		jrb3.setSelected(false);
		jrb2.setSelected(true);
		jlp8.setBounds(20, 110, 150, 25);
		jrb1.setBounds(160, 110, 100, 25);
		jrb2.setBounds(265, 110, 100, 25);
		jrb3.setBounds(370, 110, 100, 25);
		bg = new ButtonGroup();
		bg.add(jrb1);
		bg.add(jrb2);
		jp1.add(jlp8);
		jp1.add(jrb1);
		jp1.add(jrb2);
		jp1.add(jrb3);

		// 进度条
		jpbProcessLoading = new JProgressBar();
		jpbProcessLoading.setStringPainted(true); // 呈现字符串
		jpbProcessLoading.setBounds(115, 140, 255, 25);
		jpbProcessLoading.setVisible(false);
		jp1.add(jpbProcessLoading);

		// 设置显示时间（秒表）
		jlp5.setBounds(20, 170, 150, 25);
		jlp6.setBounds(160, 170, 150, 25);
		jp1.add(jlp5);
		jp1.add(jlp6);

		// 设置打开文件按钮
		button4 = new JButton();
		button4.setBounds(280, 170, 80, 25);
		jp1.add(button4);
		button4.setText("打开");
		button4.addActionListener(this); // 添加事件处理
		// 默认不可见
		button4.setVisible(false);

		// 设置删除文件按钮
		button5 = new JButton();
		button5.setBounds(370, 170, 80, 25);
		jp1.add(button5);
		button5.setText("删除");
		button5.addActionListener(this); // 添加事件处理
		// 默认不可见
		button5.setVisible(false);

		button9 = new JButton("打开网页");
		button9.setBounds(20, 200, 80, 25);
		button9.addActionListener(this);
		jp1.add(button9);

		// 分界线
		JLabel jlp11 = new JLabel("______________________________________________________________");
		jlp11.setBounds(20, 250, 450, 25);
		jp1.add(jlp11);

		// 暂时创建一个隐藏的label供传参数使用（swingworker不太会使用，只能先这样了）
		// 全局静态变量即可
		jlp7.setVisible(false);
		jlp12.setVisible(false);

		// this.add(jp1);

		this.getContentPane().add(tp);
		// 主系统
		tp.add(tabNames[0], jp1);
		// 小工具
		// tp.add(tabNames[1], jp3);
		tp.add(tabNames[1], new ToolJPanel(passParameter, this));
		// 小游戏
		tp.add(tabNames[2], new GameJPanel(passParameter, this));
		// 小通讯
		tp.add(tabNames[3], new CommunicationJPanel(passParameter, this));
		// 自定义应用
		tp.add(tabNames[4], new CustomApplicationJPanel(passParameter, this));
		// 小设置
		tp.add(tabNames[5], new SetJPanel(passParameter, this, button1));

		// 设置布局管理器
		this.setTitle("解析淘宝司法拍卖系统 - (" + passParameter.getUserInfo().getUserName() + ")");
		// 根据主题改变窗体大小
		if (setParams.getInterfaceStyle() == 6) {
			this.setSize(485, 450);
		} else {
			this.setSize(485, 420);
		}
		// this.setLocation(500, 200);
		// 相对别的窗口或者面板位置（居中）
		this.setLocationRelativeTo(null);
		if (setParams.isMinimizeTray()) {
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		} else {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		this.setVisible(true);
		this.setResizable(false);
		// 登录按钮绑定回车键(按回车即执行监听器内容)
		// 会影响其他面板，如通信发送消息监听，暂时取消
		// this.getRootPane().setDefaultButton(button1);
		// 更改图标头像
		this.setIconImage(Toolkit.getDefaultToolkit().createImage("image//image.png"));
		// 监听界面关闭事件
		this.addWindowListener(new WindowAdapter() {
			// 当关闭时
			public void windowClosing(WindowEvent e) {
				if (setParams.isMinimizeTray()) {
					try {
						// 若不支持托盘或者初始化失败,则提示
						if (!minimizeTray()) {
							JOptionPane.showMessageDialog(null, "系统不支持托盘，请更改设置！", "提示消息", JOptionPane.WARNING_MESSAGE);
						}
					} catch (AWTException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		this.requestFocus();
		this.addKeyListener(this);

	}

	// 最小化系统托盘
	protected boolean minimizeTray() throws AWTException {
		MinimizeTrayJPanel mtj = new MinimizeTrayJPanel(this);
		return mtj.init();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听选择文件按钮
		if (e.getSource().equals(button3)) {// 判断触发方法的按钮是哪个
			jfc.setFileSelectionMode(1);// 设定只能选择到文件夹
			int state = jfc.showOpenDialog(this);// 此句是打开文件选择器界面的触发语句
			if (state == 1) {
				return;
			} else {
				File f = jfc.getSelectedFile();// f为选择到的目录
				tt1.setText(f.getAbsolutePath());
			}
		}

		// 监听开始按钮
		if (e.getSource().equals(button1)) {
			long startTime = System.currentTimeMillis();
			// 开始之前先验证参数
			Params param = new Params();
			param.setProvince(jcb.getSelectedItem().toString());
			param.setCity(cjcb.getSelectedItem().toString());
			param.setFilePath(tt1.getText());
			param.setSubjectType(jcb2.getSelectedItem().toString());
			param.setSubjectStatus(jcb3.getSelectedItem().toString());
			if (jrb1.isSelected()) {
				param.setYNGetAll("全部爬取");
			} else {
				param.setYNGetAll("爬取首页");
			}
			if (jrb3.isEnabled()) {
				if (jrb3.isSelected()) {
					param.setYNGetDetail("详细信息");
				} else {
					param.setYNGetDetail("简单信息");
				}
			} else {
				param.setYNGetDetail("简单信息");
			}
			if (bu.checkParam(param)) {
				button1.setEnabled(false);
				button2.setEnabled(false);
				button6.setEnabled(true);
				jpbProcessLoading.setVisible(true);
				jpbProcessLoading.setIndeterminate(true); // 设置进度条为不确定模式,默认为确定模式
				jpbProcessLoading.setString("正在爬取，请耐心等待...");
				// 接入计时器（秒表）
				startJsTime = System.currentTimeMillis();
				jlp5.setText("爬取已开始！已进行：");
				thread = new CountThread();
				thread.start();

				// 较复杂，耗时线程swingworker
				sw = new SwingWorker<ReturnMessage, String>() {

					// 此方法处理耗时任务
					@Override
					protected ReturnMessage doInBackground() throws Exception {
						TaoBaoMainUI.taoBaoJudFlag = true;
						ReturnMessage rmsg = new MainGame().mainProcess(param);
						return rmsg;
					}

					// 此方法是耗时任务完成后的操作
					protected void done() {
						ReturnMessage rmsg = null;
						boolean yn = false;
						try {
							rmsg = get();
						} catch (Exception e) {
							e.printStackTrace();
						}

						thread.stop();
						// thread.destroy();
						jpbProcessLoading.setIndeterminate(false);
						if (rmsg != null) {
							// 失败之后也要判断文件是否存在以便于是否显示打开或者删除按钮
							if (rmsg.getFm() != null) {
								yn = bu.YNFileExist(rmsg.getFm().getLastFilePath());
							}
							if (rmsg.getYNsuccess().equals("成功")) {
								if (yn) {
									jlp7.setText(rmsg.getFm().getLastFilePath());
									button4.setVisible(true);
									button5.setVisible(true);
								}
								jlp5.setText("爬取已完成！共进行：");
								jpbProcessLoading.setString("爬取完成！");
								long endTime = System.currentTimeMillis();
								rmsg.setDate(bu.turnDate3(startTime, endTime));
								String s = "完成！共计用时：" + rmsg.getDate() + "";
								JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
							} else {
								if (yn) {
									jlp7.setText(rmsg.getFm().getLastFilePath());
									button4.setVisible(true);
									button5.setVisible(true);
								}
								jpbProcessLoading.setString("爬取失败！请重试！");
								jlp5.setText("爬取已失败！已进行：");
								String s = rmsg.getYNMessage();
								JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
							}
						} else {
							if (yn) {
								jlp7.setText(rmsg.getFm().getLastFilePath());
								button4.setVisible(true);
								button5.setVisible(true);
							}
							jpbProcessLoading.setString("爬取已中止！");
							jlp5.setText("爬取已失败！已进行：");
							if ("退出".equals(jlp12.getText().toString())) {
								return;
							}
							String s = "爬取已中止！";
							JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
						}
						button1.setEnabled(true);
						button2.setEnabled(true);
						button6.setEnabled(false);
					}
				};
				sw.execute();

			}
		}

		// 监听打开按钮
		if (e.getSource().equals(button4)) {
			if (bu.YNFileExist(jlp7.getText())) {
				if (!bu.OpenExcelFile(jlp7.getText())) {
					String s = "打开失败！文件已打开或被占用！";
					JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				String s = "打开失败！文件不存在或者不是文件！";
				JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
			}
		}

		// 监听删除按钮
		if (e.getSource().equals(button5)) {
			if (bu.YNFileExist(jlp7.getText())) {
				if (!bu.DeleteExcelFile(jlp7.getText())) {
					String s = "删除失败！文件被占用！";
					JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
				} else {
					String s = "删除成功！";
					JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				String s = "删除失败！文件不存在或者不是文件！";
				JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
			}
		}

		// 监听重置按钮
		if (e.getSource().equals(button2)) {
			clear();
		}

		// 监听中止按钮
		if (e.getSource().equals(button6)) {
			TaoBaoMainUI.taoBaoJudFlag = false;
			// 注意 此线程表面上取消但后台依然运行
			sw.cancel(true);
			thread.stop();
			button1.setEnabled(true);
			button2.setEnabled(true);
			button6.setEnabled(false);
		}

		// 监听切换按钮
		if (e.getSource().equals(button7)) {
			if (button1.isEnabled()) {
				exitSystem();
			} else {
				// 自定义弹窗,返回按钮的下标
				String[] option = { "确定", "取消" };
				int index = JOptionPane.showOptionDialog(this, "任务正在进行，是否切换系统？", "提示消息", JOptionPane.YES_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
				if (index == 0) {
					// 无法中止swingworker线程法克
					// 方案一：可以将写入文件程序放在swingworker线程的done（）方法内，这样可以中止到创建xffs即可
					jlp12.setText("退出");
					sw.cancel(true);
					// Thread.interrupted();
					exitSystem();
				}
			}
		}

		// 监听退出
		if (e.getSource().equals(button8)) {
			if (button1.isEnabled()) {
				System.exit(0);
			} else {
				// 自定义弹窗,返回按钮的下标
				String[] option = { "确定", "取消" };
				int index = JOptionPane.showOptionDialog(this, "任务正在进行，是否退出系统？", "提示消息", JOptionPane.YES_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
				if (index == 0) {
					System.exit(0);
				}
			}
		}

		// 打开网页
		if (e.getSource().equals(button9)) {
			bu.openUrlByBrowser(AnalyzeUrl.ORIGINAL_URL);
		}
	}

	// 切换系统
	public void exitSystem() {
		// dispose();
		// EventQueue.invokeLater(new Runnable() {
		// public void run() {
		// try {
		// ProGUILogin ms = new ProGUILogin();
		// // ProGUILogin.getProGUILogin();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// });
		if (!ss.reStartPro()) {
			JOptionPane.showMessageDialog(null, "切换系统失败！", "提示消息", JOptionPane.WARNING_MESSAGE);
		}
	}

	// 重置
	public void clear() {
		jcb.setModel(new DefaultComboBoxModel(gu.getProvince()));
		cjcb.setModel(new DefaultComboBoxModel(gu.getCity(province)));
		jcb2.setModel(new DefaultComboBoxModel(gu.getSubjectType()));
		jcb3.setModel(new DefaultComboBoxModel(gu.getSubjectStatus()));
		tt1.setText(setParams.getFilePath());
		jlp5.setText("");
		jlp6.setText("");
		button4.setVisible(false);
		button5.setVisible(false);
		button6.setEnabled(false);
		jrb2.setSelected(true);
		jpbProcessLoading.setVisible(false);
		jrb3.setEnabled(false);
	}

	private void itemChange() {
		String selectProvince = (String) jcb.getSelectedItem();
		cjcb.removeAllItems(); // 清空市/县列表
		String[] arrCity = gu.getCity(selectProvince); // 获取市/县
		cjcb.setModel(new DefaultComboBoxModel(arrCity)); // 重新添加市/县列表的值
	}

	// 计时线程
	private class CountThread extends Thread {
		private CountThread() {
			setDaemon(true);
		}

		@Override
		public void run() {
			while (true) {
				long elapsed = System.currentTimeMillis() - startJsTime;
				jlp6.setText(format(elapsed));

				try {
					sleep(1); // 1毫秒更新一次显示
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}

		// 将毫秒数格式化
		private String format(long elapsed) {
			int hour, minute, second, milli;
			milli = (int) (elapsed % 1000);
			elapsed = elapsed / 1000;
			second = (int) (elapsed % 60);
			elapsed = elapsed / 60;
			minute = (int) (elapsed % 60);
			elapsed = elapsed / 60;
			hour = (int) (elapsed % 60);
			return String.format("%02d:%02d:%02d %03d", hour, minute, second, milli);
		}
	}

	// 监听菜单栏
	class Listen extends JFrame implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			// 监听切换账号
			if (source.equals(cut)) {
				if (button1.isEnabled()) {
					exitSystem();
				} else {
					// 自定义弹窗,返回按钮的下标
					String[] option = { "确定", "取消" };
					int index = JOptionPane.showOptionDialog(this, "任务正在进行，是否切换系统？", "提示消息", JOptionPane.YES_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
					if (index == 0) {
						// 无法中止swingworker线程法克
						// 方案一：可以将写入文件程序放在swingworker线程的done（）方法内，这样可以中止到创建xffs即可
						jlp12.setText("退出");
						sw.cancel(true);
						// Thread.interrupted();
						exitSystem();
					}
				}
			}

			// 监听退出
			if (source.equals(exit)) {
				if (button1.isEnabled()) {
					System.exit(0);
				} else {
					// 自定义弹窗,返回按钮的下标
					String[] option = { "确定", "取消" };
					int index = JOptionPane.showOptionDialog(this, "任务正在进行，是否退出系统？", "提示消息", JOptionPane.YES_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
					if (index == 0) {
						System.exit(0);
					}
				}
			}
			// 监听新窗口
			if (source.equals(newWindow)) {
				// 获取配置
				// // 获取界面参数
				// SetParams setParams2 = null;
				// // 获取通讯设置
				// CommSetParams commSetParams2 = new CommSetParams();
				// PassParameter passParameter2 = new PassParameter();
				// GetSetProperties gsp = new GetSetProperties();
				// try {
				// setParams2 = gsp.getSetMessage();
				// commSetParams2 = gsp.getCommSetProperties();
				// } catch (IOException e2) {
				// e2.printStackTrace();
				// }
				// passParameter2.setSetParams(setParams2);
				// passParameter2.setCommSetParams(commSetParams2);
				// try {
				// if (setParams2.getInterfaceStyle() == 0) {
				// UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
				// } else if (setParams2.getInterfaceStyle() == 1) {
				// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
				// } else if (setParams2.getInterfaceStyle() == 2) {
				// UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
				// } else if (setParams2.getInterfaceStyle() == 3) {
				// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				// } else if (setParams2.getInterfaceStyle() == 4) {
				// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
				// }
				// } catch (Exception e1) {
				// e1.printStackTrace();
				// }

				// 重置配置

				// 启动窗口
				// 创建一个新界面
				// MainUI ui = new MainUI();
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							TaoBaoMainUI ui = new TaoBaoMainUI(passParameter);
							ui.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}

			// 监听字体
			if (source.equals(fonts)) {

			}

			// 监听帮助
			if (source.equals(about)) {
				String message = " Spider1.0 - 终于可以重启和自启动了！开森！For Mei！ ";
				JOptionPane.showMessageDialog(null, message, "关于", JOptionPane.PLAIN_MESSAGE);
			}

		}

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	// 监听截图快捷键
	// 单纯用java代码实现全局监听键盘是不行的，必须调用c语言，待定
	@Override
	public void keyPressed(KeyEvent e) {

		if (e.isControlDown() && e.isAltDown() && e.getKeyCode() == KeyEvent.VK_A) {
			// EventQueue.invokeLater(new Runnable() {
			// public void run() {
			try {
				Screenshot accessibleScreenshot = new Screenshot(this, true);
				accessibleScreenshot.setVisible(true);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			// }
			// });
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

}
