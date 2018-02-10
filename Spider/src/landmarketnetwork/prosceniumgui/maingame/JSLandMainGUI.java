package landmarketnetwork.prosceniumgui.maingame;

import java.awt.AWTException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import landmarketnetwork.background.controller.HNLandController;
import landmarketnetwork.background.controller.JSLandController;
import landmarketnetwork.background.controller.ZJLandController;
import landmarketnetwork.background.entity.ReturnJSLandMessage;
import landmarketnetwork.prosceniumgui.entity.JSLandParams;
import landmarketnetwork.prosceniumgui.utils.JSLandGuiUtils;
import publicGUI.communicationJPanel.client.CommunicationJPanel;
import publicGUI.customApplication.CustomApplicationJPanel;
import publicGUI.entity.PassParameter;
import publicGUI.entity.SetParams;
import publicGUI.gameJPanel.GameJPanel;
import publicGUI.setJPanel.MinimizeTrayJPanel;
import publicGUI.setJPanel.SetJPanel;
import publicGUI.toolJPanel.ToolJPanel;
import publicGUI.utils.GetSetProperties;
import publicGUI.utils.SaveSet;

public class JSLandMainGUI extends JFrame implements ActionListener {
	// 菜单栏
	JMenuBar menub;
	JMenu files;
	JMenu formats;
	JMenu help;
	JMenuItem cut;
	JMenuItem exit;
	JMenuItem fonts;
	JMenuItem about;

	public JTabbedPane tp = new JTabbedPane(JTabbedPane.TOP);
	public String[] tabNames = { "解析土地网系统", "小工具", "小游戏", "小通讯", "自定义应用", "小设置" };
	// 常量
	public static final String JSLAND_URL = "http://www.landjs.com/web/gygg_list.aspx?gglx=1";
	public static final String ZJLAND_URL = "http://land.zjgtjy.cn/GTJY_ZJ/go_home";
	public static final String HNLAND_URL = "http://www.hngtjy.cn/home.jsp";

	// 创建组件
	public JPanel jp1, jp4;
	public JLabel jlb1, jlb2, jlb3, jlb4, jlb5, jlb6, jlb7, jlb8, jlb9, jlb10, jlb11, jlb12, jlb13, jlb14, jlb15, jlb16,
			jlb17, jlb18, jlb19, jlb20;
	public JTextField jtf1, jtf2, jtf3, jtf4, jtf5, jtf6, jtf7, jtf8, jtf9, jtf10;
	public JComboBox jcb1, jcb2, jcb3, jcb4, jcb5;
	public JCheckBox jck1, jck2, jck3, jck4, jck5;
	public JRadioButton jrb1, jrb2, jrb3, jrb4, jrb5;
	public ButtonGroup bg1, bg2;
	public JButton button1, button2, button3, button4, button5, button6, button7, button8, button9, button10;
	public JFileChooser jfc1, jfc2;
	// 进度条
	public JProgressBar jpbProcessLoading1;
	public SwingWorker<ReturnJSLandMessage, String> sw1;
	public PassParameter passParameter;
	SetParams setParams = new SetParams();
	public GetSetProperties gsp = new GetSetProperties();
	public JSLandGuiUtils jsgu = new JSLandGuiUtils();
	public SaveSet ss = new SaveSet();
	public JSLandController jslc = new JSLandController();
	public ZJLandController zjlc = new ZJLandController();
	public HNLandController hnlc = new HNLandController();
	public String[] landUse = { "商住", "住宅", "不限", "商业", "其他" };
	public JSLandParams jsLandParams;
	// 全局静态变量控制中止线程
	public static boolean JSLandFlag = true;
	public static boolean ZJLandFlag = true;
	public static boolean HNLandFlag = true;

	public JSLandMainGUI(PassParameter passParameter) throws IOException {
		initMenu();
		passParameter.setSetParams(initSet());
		this.passParameter = passParameter;
		initJFrame();
		init();
	}

	// 初始化参数
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
		cut = new JMenuItem("切换(T)");
		exit = new JMenuItem("退出(X)");
		fonts = new JMenuItem("字体...");
		about = new JMenuItem("关于Reptile(A)");

		files.setMnemonic('F');
		formats.setMnemonic('O');
		help.setMnemonic('H');
		exit.setMnemonic('X');
		cut.setMnemonic('T');
		about.setMnemonic('A');
		// 为控件添加助记符
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		// 为控件添加快捷键
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
		about.addActionListener(listen);
		fonts.setEnabled(false);
	}

	private void initJFrame() throws IOException {
		jp1 = new JPanel();
		jp4 = new JPanel();
		jp1.setLayout(null);

		jlb1 = new JLabel("请选择文件夹：");
		jtf1 = new JTextField();// TextField 目录的路径
		jtf1.setText(setParams.getFilePath());
		jtf1.setEditable(false);
		// 每次文本框改变都要记住最新路径
		jtf1.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// 保存到缓存对象
				setParams.setFilePath(jtf1.getText());
			}

			// 改变是时候
			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});
		button1 = new JButton("文件夹");// 选择
		jfc1 = new JFileChooser();// 文件选择器
		jfc1.setCurrentDirectory(new File(setParams.getFilePath()));// 文件选择器的初始目录定为d盘
		jtf1.setBounds(110, 10, 217, 25);
		jlb1.setBounds(20, 10, 130, 25);
		button1.setBounds(339, 10, 96, 25);
		button1.addActionListener(this); // 添加事件处理
		jp1.add(jtf1);
		jp1.add(jlb1);
		jp1.add(button1);

		// 分界线
		jlb2 = new JLabel("_____________________________________________________________");
		jlb2.setBounds(20, 25, 450, 25);
		jp1.add(jlb2);

		// 选择网站
		jlb14 = new JLabel("选  择  网  站：");
		jlb14.setBounds(20, 50, 130, 25);
		jp1.add(jlb14);
		String[] network = { "江苏土地市场网", "浙江省土地使用权网上交易系统", "河南省国土资源网上交易系统" };
		jcb2 = new JComboBox(network);
		jcb2.setBounds(108, 50, 219, 25);
		jp1.add(jcb2);
		button5 = new JButton("打开网站");
		button5.setBounds(339, 50, 96, 25);
		button5.addActionListener(this); // 添加事件处理
		jp1.add(button5);

		jlb3 = new JLabel("筛  选  条  件：");
		jlb3.setBounds(20, 80, 130, 25);
		jp1.add(jlb3);

		jck1 = new JCheckBox("不限");
		jck1.setSelected(true);
		jck1.setBounds(108, 80, 130, 25);
		jck1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jck1.isSelected()) {
					jtf2.setEnabled(false);
					jtf3.setEnabled(false);
				} else {
					jtf2.setEnabled(true);
					jtf3.setEnabled(true);
				}
			}
		});
		jp1.add(jck1);

		jlb4 = new JLabel("时间条件：");
		jlb4.setBounds(170, 80, 60, 25);
		jtf2 = new JTextField();
		jtf2.setBounds(231, 80, 96, 25);
		jtf2.setEnabled(false);
		jlb5 = new JLabel("-");
		jlb5.setBounds(329, 80, 10, 25);
		jtf3 = new JTextField();
		jtf3.setBounds(339, 80, 96, 25);
		jtf3.setEnabled(false);
		jp1.add(jlb4);
		jp1.add(jlb5);
		jp1.add(jtf2);
		jp1.add(jtf3);

		jck2 = new JCheckBox("不限");
		jck2.setSelected(true);
		jck2.setBounds(108, 110, 60, 25);
		jck2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jck2.isSelected()) {
					jtf4.setEnabled(false);
					jtf5.setEnabled(false);
				} else {
					jtf4.setEnabled(true);
					jtf5.setEnabled(true);
				}
			}
		});
		jp1.add(jck2);

		jlb6 = new JLabel("起始价格：");
		jlb6.setBounds(170, 110, 60, 25);
		jtf4 = new JTextField();
		jtf4.setBounds(231, 110, 96, 25);
		jtf4.setEnabled(false);
		jlb7 = new JLabel("-");
		jlb7.setBounds(329, 110, 10, 25);
		jtf5 = new JTextField();
		jtf5.setBounds(339, 110, 96, 25);
		jtf5.setEnabled(false);
		jlb8 = new JLabel("万元");
		jlb8.setBounds(438, 110, 30, 25);
		jp1.add(jlb6);
		jp1.add(jlb7);
		jp1.add(jlb8);
		jp1.add(jtf4);
		jp1.add(jtf5);

		jck3 = new JCheckBox("不限");
		jck3.setSelected(true);
		jck3.setBounds(108, 140, 130, 25);
		jck3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jck3.isSelected()) {
					jtf6.setEnabled(false);
					jtf7.setEnabled(false);
				} else {
					jtf6.setEnabled(true);
					jtf7.setEnabled(true);
				}
			}
		});
		jp1.add(jck3);

		jlb8 = new JLabel("土地面积：");
		jlb8.setBounds(170, 140, 60, 25);
		jtf6 = new JTextField();
		jtf6.setBounds(231, 140, 96, 25);
		jtf6.setEnabled(false);
		jlb9 = new JLabel("-");
		jlb9.setBounds(329, 140, 10, 25);
		jtf7 = new JTextField();
		jtf7.setBounds(339, 140, 96, 25);
		jtf7.setEnabled(false);
		jlb10 = new JLabel("㎡");
		jlb10.setBounds(438, 140, 30, 25);
		jp1.add(jlb8);
		jp1.add(jlb9);
		jp1.add(jlb10);
		jp1.add(jtf6);
		jp1.add(jtf7);

		jck4 = new JCheckBox("下载附件");
		jck4.setSelected(false);
		jck4.setBounds(108, 170, 70, 25);
		jp1.add(jck4);

		jlb11 = new JLabel("用途：");
		jlb11.setBounds(190, 170, 60, 25);
		jp1.add(jlb11);

		jcb1 = new JComboBox(landUse);
		jcb1.setBounds(227, 170, 60, 25);
		jp1.add(jcb1);

		jlb13 = new JLabel("页数：");
		jlb13.setBounds(295, 170, 40, 25);
		jp1.add(jlb13);

		jtf9 = new JTextField();
		jtf9.setBounds(330, 170, 50, 25);
		jp1.add(jtf9);

		jlb15 = new JLabel("-");
		jlb15.setBounds(381, 170, 4, 25);
		jp1.add(jlb15);

		jtf8 = new JTextField();
		jtf8.setBounds(385, 170, 50, 25);
		jp1.add(jtf8);

		// 进度条
		jpbProcessLoading1 = new JProgressBar();
		jpbProcessLoading1.setStringPainted(true); // 呈现字符串
		jpbProcessLoading1.setBounds(150, 200, 260, 25);
		jpbProcessLoading1.setVisible(false);
		jp1.add(jpbProcessLoading1);

		button2 = new JButton("开始");
		button2.setBounds(110, 230, 80, 25);
		button2.addActionListener(this);
		button3 = new JButton("中止");
		button3.setBounds(240, 230, 80, 25);
		button3.addActionListener(this);
		button3.setEnabled(false);
		button4 = new JButton("重置");
		button4.setBounds(370, 230, 80, 25);
		button4.addActionListener(this);
		jp1.add(button2);
		jp1.add(button3);
		jp1.add(button4);

		this.getContentPane().add(tp);
		// 主系统
		tp.add(tabNames[0], jp1);
		tp.add(tabNames[1], new ToolJPanel(passParameter, this));
		// 小游戏
		tp.add(tabNames[2], new GameJPanel(passParameter, this));
		// 小通讯
		tp.add(tabNames[3], new CommunicationJPanel(passParameter, this));
		// 自定义应用
		tp.add(tabNames[4], new CustomApplicationJPanel(passParameter, this));
		// 小设置
		tp.add(tabNames[5], new SetJPanel(passParameter, this, button1));

		this.setTitle("解析土地市场网系统 - (" + passParameter.getUserInfo().getUserName() + ")");
		this.setVisible(true);
		this.setSize(485, 430);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	}

	// 最小化系统托盘
	protected boolean minimizeTray() throws AWTException {
		MinimizeTrayJPanel mtj = new MinimizeTrayJPanel(this);
		return mtj.init();
	}

	private void init() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object key = e.getSource();
		// 监听选择文件按钮
		if (e.getSource().equals(button1)) {// 判断触发方法的按钮是哪个
			jfc1.setFileSelectionMode(1);// 设定只能选择到文件夹
			int state = jfc1.showOpenDialog(this);// 此句是打开文件选择器界面的触发语句
			if (state == 1) {
				return;
			} else {
				File f = jfc1.getSelectedFile();// f为选择到的目录
				jtf1.setText(f.getAbsolutePath());
			}
		}

		// 监听开始
		if (e.getSource().equals(button2)) {
			long startTime = System.currentTimeMillis();
			jsLandParams = getJSLandParams();
			if (jsgu.checkJSLandParams(jsLandParams)) {
				button2.setEnabled(false);
				button4.setEnabled(false);
				button3.setEnabled(true);
				jpbProcessLoading1.setVisible(true);
				jpbProcessLoading1.setIndeterminate(true); // 设置进度条为不确定模式,默认为确定模式
				jpbProcessLoading1.setString("正在爬取，请耐心等待...");

				// 较复杂，耗时线程swingworker
				sw1 = new SwingWorker<ReturnJSLandMessage, String>() {
					// 此方法是耗时任务
					@Override
					protected ReturnJSLandMessage doInBackground() throws Exception {
						// 调用后台controller方法，并返回相关信息
						ReturnJSLandMessage returnJSLandMessage = null;
						if (jsLandParams.getNetwork() == 0) {
							JSLandMainGUI.JSLandFlag = true;
							returnJSLandMessage = jslc.JSLandController(jsLandParams);
						} else if (jsLandParams.getNetwork() == 1) {
							JSLandMainGUI.ZJLandFlag = true;
							returnJSLandMessage = zjlc.ZJLandController(jsLandParams);
						} else if (jsLandParams.getNetwork() == 2) {
							JSLandMainGUI.HNLandFlag = true;
							returnJSLandMessage = hnlc.HNLandController(jsLandParams);
						}
						return returnJSLandMessage;
					}

					// 此方法是耗时任务完成后的操作
					protected void done() {
						ReturnJSLandMessage rjslm = null;
						try {
							rjslm = get();
						} catch (Exception e) {
							e.printStackTrace();
						}
						jpbProcessLoading1.setIndeterminate(false);
						if (rjslm != null) {
							if (rjslm.getYNsuccess().equals("是") && rjslm.getJslfm().isBl()) {
								jpbProcessLoading1.setString("爬取完成！");
								String s = "完成！共计用时：" + rjslm.getDate() + "";
								JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
							} else {
								jpbProcessLoading1.setString("爬取失败！");
								String s = rjslm.getYNMessage();
								JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
							}
						} else {
							jpbProcessLoading1.setString("爬取已中止！");
							if ("退出".equals(jlb12.getText().toString())) {
								return;
							}
							String s = "爬取已中止！";
							JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
						}
						button2.setEnabled(true);
						button3.setEnabled(false);
						button4.setEnabled(true);
					}
				};
				sw1.execute();
			} else {
				JOptionPane.showMessageDialog(null, "格式不正确！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			}

		}

		// 监听中止
		if (e.getSource().equals(button3)) {
			if (jsLandParams.getNetwork() == 0) {
				JSLandMainGUI.JSLandFlag = false;
			} else if (jsLandParams.getNetwork() == 1) {
				JSLandMainGUI.ZJLandFlag = false;
			} else if (jsLandParams.getNetwork() == 2) {
				JSLandMainGUI.HNLandFlag = false;
			}
			button2.setEnabled(true);
			button3.setEnabled(false);
			button4.setEnabled(true);
			sw1.cancel(true);
		}

		// 监听重置
		if (e.getSource().equals(button4)) {
			clearJSLand();
		}

		// 监听打开网址
		if (e.getSource().equals(button5)) {
			int choiceNetwork = jcb2.getSelectedIndex();
			String landUrl = "http://www.baidu.com";
			if (choiceNetwork == 0) {
				landUrl = JSLandMainGUI.JSLAND_URL;
			} else if (choiceNetwork == 1) {
				landUrl = JSLandMainGUI.ZJLAND_URL;
			} else if (choiceNetwork == 2) {
				landUrl = JSLandMainGUI.HNLAND_URL;
			}
			jsgu.openUrlByBrowser(landUrl);
		}

	}

	private void clearJSLand() {
		jfc1.setCurrentDirectory(new File(setParams.getFilePath()));
		jtf2.setText("");
		jtf2.setEnabled(false);
		jtf3.setText("");
		jtf3.setEnabled(false);
		jtf4.setText("");
		jtf4.setEnabled(false);
		jtf5.setText("");
		jtf5.setEnabled(false);
		jtf6.setText("");
		jtf6.setEnabled(false);
		jtf7.setText("");
		jtf8.setText("");
		jtf9.setText("");
		jtf7.setEnabled(false);
		jcb1.setSelectedIndex(0);
		jck1.setSelected(true);
		jck2.setSelected(true);
		jck3.setSelected(true);
		jck4.setSelected(false);
		button2.setEnabled(true);
		button3.setEnabled(false);
		button4.setEnabled(true);
		jpbProcessLoading1.setVisible(false);
	}

	private JSLandParams getJSLandParams() {
		JSLandParams jsLandParams = new JSLandParams();
		// 文件夹路径
		jsLandParams.setFilePath(jtf1.getText());
		// 获取网站
		jsLandParams.setNetwork(jcb2.getSelectedIndex());

		if (jck1.isSelected()) {
			// 时间条件最小值
			jsLandParams.setMinDate("19700101");
			// 时间最大值
			jsLandParams.setMaxDate("30001231");
		} else {
			// 时间条件最小值
			jsLandParams.setMinDate(
					"".equals(jtf2.getText().trim().toString()) ? "19700101" : jtf2.getText().trim().toString());
			// 时间最大值
			jsLandParams.setMaxDate(
					"".equals(jtf3.getText().trim().toString()) ? "30001231" : jtf3.getText().trim().toString());
		}
		if (jck2.isSelected()) {
			// 价格最小值
			jsLandParams.setMinPrice("0");
			// 价格最大值
			jsLandParams.setMaxPrice("99999999");
		} else {
			// 价格最小值
			jsLandParams
					.setMinPrice("".equals(jtf4.getText().trim().toString()) ? "0" : jtf4.getText().trim().toString());
			// 价格最大值
			jsLandParams.setMaxPrice(
					"".equals(jtf5.getText().trim().toString()) ? "2000000000" : jtf5.getText().trim().toString());
		}
		if (jck3.isSelected()) {
			// 土地面积最小值
			jsLandParams.setMinLandArea("0");
			// 土地面积最大值
			jsLandParams.setMaxLandArea("999999999");
		} else {
			// 土地面积最小值
			jsLandParams.setMinLandArea(
					"".equals(jtf6.getText().trim().toString()) ? "0" : jtf6.getText().trim().toString());
			// 土地面积最大值
			jsLandParams.setMaxLandArea(
					"".equals(jtf7.getText().trim().toString()) ? "2000000000" : jtf7.getText().trim().toString());
		}
		// 是否下载附件
		jsLandParams.setDownlandAttach(jck4.isSelected());
		// 土地用途
		jsLandParams.setLandUse(jcb1.getSelectedItem().toString());
		// 页数
		jsLandParams.setPageNum(jtf8.getText().trim().toString());
		// 最小页数
		jsLandParams.setMinpageNum(jtf9.getText().trim().toString());
		return jsLandParams;
	}

	// 切换系统
	public void exitSystem() {
		if (!ss.reStartPro()) {
			JOptionPane.showMessageDialog(null, "切换系统失败！", "提示消息", JOptionPane.WARNING_MESSAGE);
		}
	}

	// 监听菜单栏
	class Listen extends JFrame implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			// 监听切换账号
			if (source.equals(cut)) {
				if (button2.isEnabled()) {
					exitSystem();
				} else {
					// 自定义弹窗,返回按钮的下标
					String[] option = { "确定", "取消" };
					int index = JOptionPane.showOptionDialog(this, "任务正在进行，是否切换系统？", "提示消息", JOptionPane.YES_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
					if (index == 0) {
						// 无法中止swingworker线程法克
						// 方案一：可以将写入文件程序放在swingworker线程的done（）方法内，这样可以中止到创建xffs即可
						if (sw1.getState().equals(StateValue.STARTED)) {
							jlb12.setText("退出");
							sw1.cancel(true);
						}
						// Thread.interrupted();
						exitSystem();
					}
				}
			}

			// 监听退出
			if (source.equals(exit)) {
				if (button2.isEnabled()) {
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

			// 监听字体
			if (source.equals(fonts)) {

			}

			// 监听帮助
			if (source.equals(about)) {
				String message = " Reptile1.0 - 终于可以重启和自启动了！开森！For Mei！ ";
				JOptionPane.showMessageDialog(null, message, "关于", JOptionPane.PLAIN_MESSAGE);
			}

		}

	}

}
