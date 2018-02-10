package suzhouhouse.prosceniumgui.maingame;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
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
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
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
import javax.swing.Timer;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import login.entity.UserInfo;
import login.login.ProGUILogin;
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
import suzhouhouse.background.controller.HouseQueryController;
import suzhouhouse.background.controller.HouseShowController;
import suzhouhouse.background.controller.MegAnnoAndTradeMegController;
import suzhouhouse.background.controller.PermitPresaleController;
import suzhouhouse.background.entity.ReturnHouseMessage;
import suzhouhouse.prosceniumgui.entity.HouseQueryParams;
import suzhouhouse.prosceniumgui.entity.HouseShowParams;
import suzhouhouse.prosceniumgui.entity.MegAnnoAndTradeMegParams;
import suzhouhouse.prosceniumgui.entity.PermitPresaleParams;
import suzhouhouse.prosceniumgui.utils.HouseGuiUtils;

public class SZHmainGui extends JFrame implements ActionListener {
	// 菜单栏
	JMenuBar menub;
	JMenu files;
	JMenu formats;
	JMenu help;
	JMenuItem cut;
	JMenuItem exit;
	JMenuItem fonts;
	JMenuItem about;

	public JButton button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11,
			button12, button13, button14, button15;
	public JLabel jlp1, jlp2, jlp3, jlp4, jlp5, jlp6, jlp7, jlp8, jlp9, jlp10, jlp11, jlp12, jlp13, jlp14, jlp15, jlp16,
			jlp17, jlp18, jlp19, jlp20, jlp21, jlp22, jlp23, jlp24, jlp25, jlp26, jlp27, jlp28, jlp29, jlp112, jlp113,
			jlp114, jlp115;
	public JRadioButton jrb1, jrb2, jrb3, jrb4, jrb5, jrb6;
	public ButtonGroup bg, bg2;
	// 文件选择器
	public JFileChooser jfc, jfc2;
	public JTextField tt1, tt2, tt3, tt4, tt5, tt6, tt7, tt8, tt9, tt10;
	public JComboBox jcb1, jcb2, jcb3, jcb4;
	// 进度条
	public JProgressBar jpbProcessLoading1, jpbProcessLoading2, jpbProcessLoading3, jpbProcessLoading4,
			jpbProcessLoading5;
	public SwingWorker<ReturnHouseMessage, String> sw1, sw2, sw3, sw4, sw5;

	JTabbedPane tp = new JTabbedPane(JTabbedPane.TOP);
	private String[] tabNames = { "解析系统1", "小工具", "小游戏", "小通讯", "自定义应用", "小设置", "解析系统2" };
	SetParams setParams = new SetParams();
	public GetSetProperties gsp = new GetSetProperties();
	public PassParameter passParameter;
	public HouseGuiUtils hgu = new HouseGuiUtils();
	public SaveSet ss = new SaveSet();
	// 后台方法
	public HouseShowController hsc = new HouseShowController();
	public PermitPresaleController ppc = new PermitPresaleController();
	public HouseQueryController hqc = new HouseQueryController();
	public MegAnnoAndTradeMegController maatmc = new MegAnnoAndTradeMegController();

	// 全局静态变量控制中止线程
	public static boolean houseShowFlag = true;
	public static boolean houseQueryFlag = true;
	public static boolean permitPresaleFlag = true;

	// 构造函数
	public SZHmainGui(PassParameter passParameter) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException, IOException {
		initMenu();
		passParameter.setSetParams(initSet());
		this.passParameter = passParameter;
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

	public void init() throws IOException {
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

		jlp6 = new JLabel("请选择文件夹：");
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
		button1 = new JButton("选择");// 选择
		jfc = new JFileChooser();// 文件选择器
		jfc.setCurrentDirectory(new File(setParams.getFilePath()));// 文件选择器的初始目录定为d盘
		tt1.setBounds(110, 10, 195, 25);
		jlp6.setBounds(20, 10, 130, 25);
		button1.setBounds(310, 10, 60, 25);
		button1.addActionListener(this); // 添加事件处理
		jp1.add(tt1);
		jp1.add(jlp6);
		jp1.add(button1);

		button14 = new JButton("打开网址");
		button14.setBounds(370, 10, 80, 25);
		button14.addActionListener(this);
		jp1.add(button14);

		// 分界线
		jlp14 = new JLabel("_____________________________________________________________");
		jlp14.setBounds(20, 25, 450, 25);
		jp1.add(jlp14);

		jlp1 = new JLabel("可售楼盘展示：");
		jlp1.setBounds(20, 50, 130, 25);
		jp1.add(jlp1);

		jrb1 = new JRadioButton("模式一:少量");
		jrb2 = new JRadioButton("模式二:大量");
		jrb1.setSelected(true);

		jrb1.setBounds(110, 50, 130, 25);
		jrb2.setBounds(110, 110, 130, 25);
		jrb1.addItemListener(new ItemListener() {
			// 模式一选中则模式二不能用
			@Override
			public void itemStateChanged(ItemEvent e) {
				tt2.setEnabled(true);
				tt3.setEnabled(true);
				jcb1.setEnabled(true);
				tt4.setEnabled(false);
				jrb3.setEnabled(false);
			}
		});
		jrb2.addItemListener(new ItemListener() {
			// 模式二选中则模式一不能用
			@Override
			public void itemStateChanged(ItemEvent e) {
				tt2.setEnabled(false);
				tt3.setEnabled(false);
				jcb1.setEnabled(false);
				tt4.setEnabled(true);
				jrb3.setEnabled(true);
			}
		});
		bg = new ButtonGroup();
		bg.add(jrb1);
		bg.add(jrb2);
		jp1.add(jrb1);
		jp1.add(jrb2);

		// 少量
		jlp7 = new JLabel("项目名称：");
		jlp7.setBounds(250, 50, 60, 25);
		tt2 = new JTextField();
		tt2.setBounds(310, 50, 140, 25);
		jlp8 = new JLabel("公司名称：");
		jlp8.setBounds(250, 80, 60, 25);
		tt3 = new JTextField();
		tt3.setBounds(310, 80, 140, 25);
		jlp9 = new JLabel("项目区域：");
		jlp9.setBounds(110, 80, 60, 25);
		jcb1 = new JComboBox();
		jcb1.setBounds(170, 80, 70, 25);
		jcb1.setModel(new DefaultComboBoxModel(hgu.getAreaType()));
		jp1.add(jcb1);
		jp1.add(jlp9);
		button2 = new JButton("开始");
		button2.setBounds(110, 170, 80, 25);
		button2.addActionListener(this);
		button4 = new JButton("中止");
		button4.setBounds(240, 170, 80, 25);
		button4.setEnabled(false);
		button4.addActionListener(this);
		button5 = new JButton("重置");
		button5.setBounds(370, 170, 80, 25);
		button5.addActionListener(this);
		jp1.add(jlp8);
		jp1.add(button2);
		jp1.add(button4);
		jp1.add(button5);
		jp1.add(tt3);
		jp1.add(jlp7);
		jp1.add(tt2);
		// 大量
		jlp10 = new JLabel("填写页数：");
		jlp10.setBounds(250, 110, 60, 25);
		tt4 = new JTextField();
		tt4.setBounds(310, 110, 60, 25);
		jp1.add(tt4);
		jp1.add(jlp10);
		jrb3 = new JRadioButton("爬取全部");
		jrb3.setSelected(false);
		jrb3.setBounds(380, 110, 80, 25);
		// 初始状态
		tt4.setEnabled(false);
		jrb3.setEnabled(false);
		jp1.add(jrb3);

		// 进度条
		jpbProcessLoading1 = new JProgressBar();
		jpbProcessLoading1.setStringPainted(true); // 呈现字符串
		jpbProcessLoading1.setBounds(150, 140, 260, 25);
		jpbProcessLoading1.setVisible(false);
		jp1.add(jpbProcessLoading1);
		// 分界线
		jlp13 = new JLabel("_____________________________________________________________");
		jlp13.setBounds(20, 190, 450, 25);
		jp1.add(jlp13);

		jlp3 = new JLabel("预售许可信息：");
		jlp3.setBounds(20, 220, 130, 25);
		jp1.add(jlp3);
		jlp11 = new JLabel("项目区域：");
		jlp11.setBounds(110, 220, 60, 25);
		jcb2 = new JComboBox();
		jcb2.setBounds(170, 220, 70, 25);
		jcb2.setModel(new DefaultComboBoxModel(hgu.getAreaType()));
		jlp12 = new JLabel("填写页数：");
		jlp12.setBounds(250, 220, 60, 25);
		tt5 = new JTextField();
		tt5.setBounds(310, 220, 60, 25);
		jp1.add(tt5);
		jp1.add(jlp12);
		jp1.add(jcb2);
		jp1.add(jlp11);

		// 进度条
		jpbProcessLoading2 = new JProgressBar();
		jpbProcessLoading2.setStringPainted(true); // 呈现字符串
		jpbProcessLoading2.setBounds(150, 250, 260, 25);
		jpbProcessLoading2.setVisible(false);
		jp1.add(jpbProcessLoading2);

		button3 = new JButton("开始");
		button3.setBounds(110, 280, 80, 25);
		button3.addActionListener(this);
		button6 = new JButton("中止");
		button6.setBounds(240, 280, 80, 25);
		button6.addActionListener(this);
		button6.setEnabled(false);
		button7 = new JButton("重置");
		button7.setBounds(370, 280, 80, 25);
		button7.addActionListener(this);
		jp1.add(button3);
		jp1.add(button6);
		jp1.add(button7);

		// 面板二
		jlp15 = new JLabel("请选择文件夹：");
		tt6 = new JTextField();// TextField 目录的路径
		tt6.setText(setParams.getFilePath());
		tt6.setEditable(false);
		// 每次文本框改变都要记住最新路径
		tt6.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// 保存到缓存对象
				setParams.setFilePath(tt6.getText());
			}

			// 改变是时候
			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});
		button8 = new JButton("选择");// 选择
		jfc2 = new JFileChooser();// 文件选择器
		jfc2.setCurrentDirectory(new File(setParams.getFilePath()));// 文件选择器的初始目录定为d盘
		tt6.setBounds(110, 10, 195, 25);
		jlp15.setBounds(20, 10, 130, 25);
		button8.setBounds(310, 10, 60, 25);
		button8.addActionListener(this); // 添加事件处理
		jp2.add(tt6);
		jp2.add(jlp15);
		jp2.add(button8);

		button15 = new JButton("打开网址");
		button15.setBounds(370, 10, 80, 25);
		button15.addActionListener(this);
		jp2.add(button15);

		// 分界线
		jlp16 = new JLabel("_____________________________________________________________");
		jlp16.setBounds(20, 25, 450, 25);
		jp2.add(jlp16);

		jlp2 = new JLabel("可售房源查询：");
		jlp2.setBounds(20, 50, 130, 25);
		jp2.add(jlp2);
		jlp17 = new JLabel("装修情况：");
		jlp17.setBounds(110, 50, 100, 25);
		jp2.add(jlp17);
		jrb4 = new JRadioButton("全部");
		jrb5 = new JRadioButton("全装修房");
		jrb6 = new JRadioButton("毛坯房");
		jrb4.setSelected(true);
		jrb4.setBounds(180, 50, 60, 25);
		jrb5.setBounds(265, 50, 80, 25);
		jrb6.setBounds(360, 50, 70, 25);
		bg2 = new ButtonGroup();
		bg2.add(jrb4);
		bg2.add(jrb5);
		bg2.add(jrb6);
		jp2.add(jrb4);
		jp2.add(jrb5);
		jp2.add(jrb6);
		jlp18 = new JLabel("项目区域：");
		jlp18.setBounds(110, 80, 60, 25);
		jcb3 = new JComboBox();
		jcb3.setBounds(170, 80, 60, 25);
		jcb3.setModel(new DefaultComboBoxModel(hgu.getAreaType()));
		jp2.add(jlp18);
		jp2.add(jcb3);
		jlp19 = new JLabel("房屋总价：");
		jlp19.setBounds(240, 80, 60, 25);
		tt7 = new JTextField();
		tt7.setBounds(300, 80, 60, 25);
		jlp20 = new JLabel("-");
		jlp20.setBounds(362, 80, 10, 25);
		tt8 = new JTextField();
		tt8.setBounds(372, 80, 60, 25);
		jlp21 = new JLabel("元");
		jlp21.setBounds(435, 80, 15, 25);
		jp2.add(jlp19);
		jp2.add(jlp20);
		jp2.add(jlp21);
		jp2.add(tt7);
		jp2.add(tt8);

		jlp22 = new JLabel("房屋用途：");
		jlp22.setBounds(110, 110, 60, 25);
		jcb4 = new JComboBox();
		jcb4.setBounds(170, 110, 60, 25);
		jcb4.setModel(new DefaultComboBoxModel(hgu.getHouseUseType()));
		jp2.add(jlp22);
		jp2.add(jcb4);
		jlp23 = new JLabel("房屋面积：");
		jlp23.setBounds(240, 110, 60, 25);
		tt9 = new JTextField();
		tt9.setBounds(300, 110, 60, 25);
		jlp24 = new JLabel("-");
		jlp24.setBounds(362, 110, 10, 25);
		tt10 = new JTextField();
		tt10.setBounds(372, 110, 60, 25);
		jlp25 = new JLabel("㎡");
		jlp25.setBounds(435, 110, 15, 25);
		jp2.add(jlp23);
		jp2.add(jlp24);
		jp2.add(jlp25);
		jp2.add(tt9);
		jp2.add(tt10);

		// 进度条
		jpbProcessLoading3 = new JProgressBar();
		jpbProcessLoading3.setStringPainted(true); // 呈现字符串
		jpbProcessLoading3.setBounds(150, 140, 260, 25);
		jpbProcessLoading3.setVisible(false);
		jp2.add(jpbProcessLoading3);

		button9 = new JButton("开始");
		button9.setBounds(110, 170, 80, 25);
		button9.addActionListener(this);
		button10 = new JButton("中止");
		button10.setBounds(240, 170, 80, 25);
		button10.addActionListener(this);
		button10.setEnabled(false);
		button11 = new JButton("重置");
		button11.setBounds(370, 170, 80, 25);
		button11.addActionListener(this);
		jp2.add(button9);
		jp2.add(button10);
		jp2.add(button11);

		// 分界线
		jlp26 = new JLabel("_____________________________________________________________");
		jlp26.setBounds(20, 190, 450, 25);
		jp2.add(jlp26);

		jlp4 = new JLabel("可售房源信息公示：");
		jlp4.setBounds(20, 220, 130, 25);
		jp2.add(jlp4);
		// 进度条
		jpbProcessLoading4 = new JProgressBar();
		jpbProcessLoading4.setStringPainted(true); // 呈现字符串
		jpbProcessLoading4.setBounds(140, 220, 220, 25);
		jpbProcessLoading4.setVisible(false);
		jp2.add(jpbProcessLoading4);
		button12 = new JButton("开始");
		button12.setBounds(370, 220, 80, 25);
		button12.addActionListener(this);
		jp2.add(button12);

		jlp5 = new JLabel("即时成交房屋信息：");
		jlp5.setBounds(20, 250, 130, 25);
		jp2.add(jlp5);
		// 进度条
		jpbProcessLoading5 = new JProgressBar();
		jpbProcessLoading5.setStringPainted(true); // 呈现字符串
		jpbProcessLoading5.setBounds(140, 250, 220, 25);
		jpbProcessLoading5.setVisible(false);
		jp2.add(jpbProcessLoading5);
		button13 = new JButton("开始");
		button13.setBounds(370, 250, 80, 25);
		button13.addActionListener(this);
		jp2.add(button13);

		this.add(jp1);
		this.getContentPane().add(tp);
		// 主系统
		tp.add(tabNames[0], jp1);
		tp.add(tabNames[6], jp2);
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
		this.setTitle("解析苏州房产网系统 - (" + passParameter.getUserInfo().getUserName() + ")");
		this.setSize(485, 420);
		// this.setLocation(500, 200);
		// 相对别的窗口或者面板位置（居中）
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		// 登录按钮绑定回车键(按回车即执行监听器内容)
		this.getRootPane().setDefaultButton(button1);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听选择文件按钮
		if (e.getSource().equals(button1)) {// 判断触发方法的按钮是哪个
			jfc.setFileSelectionMode(1);// 设定只能选择到文件夹
			int state = jfc.showOpenDialog(this);// 此句是打开文件选择器界面的触发语句
			if (state == 1) {
				return;
			} else {
				File f = jfc.getSelectedFile();// f为选择到的目录
				tt1.setText(f.getAbsolutePath());
			}
		}
		if (e.getSource().equals(button8)) {// 判断触发方法的按钮是哪个
			jfc2.setFileSelectionMode(1);// 设定只能选择到文件夹
			int state2 = jfc2.showOpenDialog(this);// 此句是打开文件选择器界面的触发语句
			if (state2 == 1) {
				return;
			} else {
				File f2 = jfc2.getSelectedFile();// f为选择到的目录
				tt6.setText(f2.getAbsolutePath());
			}
		}
		// 监听楼盘展示开始按钮
		if (e.getSource().equals(button2)) {
			long startTime = System.currentTimeMillis();
			HouseShowParams houseShowParams = getHouseShowParams();
			if (hgu.checkHouseShowParams(houseShowParams)) {
				button2.setEnabled(false);
				button5.setEnabled(false);
				button4.setEnabled(true);
				jpbProcessLoading1.setVisible(true);
				jpbProcessLoading1.setIndeterminate(true); // 设置进度条为不确定模式,默认为确定模式
				jpbProcessLoading1.setString("正在爬取，请耐心等待...");

				// 较复杂，耗时线程swingworker
				sw1 = new SwingWorker<ReturnHouseMessage, String>() {
					// 此方法是耗时任务
					@Override
					protected ReturnHouseMessage doInBackground() throws Exception {
						// 开始时更改状态为开始
						SZHmainGui.houseShowFlag = true;
						// 调用后台controller方法，并返回相关信息
						ReturnHouseMessage returnHouseMessage = hsc.houseShowController(houseShowParams);
						return returnHouseMessage;
					}

					// 此方法是耗时任务完成后的操作
					protected void done() {
						ReturnHouseMessage rhm = null;
						try {
							rhm = get();
						} catch (Exception e) {
							e.printStackTrace();
						}
						jpbProcessLoading1.setIndeterminate(false);
						if (rhm != null) {
							if (rhm.getYNsuccess().equals("是") && rhm.getHfm().isBl()) {
								jpbProcessLoading1.setString("爬取完成！");
								String s = "完成！共计用时：" + rhm.getDate() + "";
								JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
							} else {
								jpbProcessLoading1.setString("爬取失败！");
								String s = rhm.getYNMessage();
								JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
							}
						} else {
							jpbProcessLoading1.setString("爬取已中止！");
							if ("退出".equals(jlp112.getText().toString())) {
								return;
							}
							String s = "爬取已中止！";
							JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
						}
						button2.setEnabled(true);
						button4.setEnabled(false);
						button5.setEnabled(true);
					}

				};
				sw1.execute();
			} else {
				JOptionPane.showMessageDialog(null, "格式不正确！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			}

		}
		// 监听预售证开始按钮
		if (e.getSource().equals(button3)) {
			long startTime = System.currentTimeMillis();
			PermitPresaleParams permitPresaleParams = getPermitPresaleParams();
			if (hgu.checkPermitPresaleParams(permitPresaleParams)) {
				button3.setEnabled(false);
				button7.setEnabled(false);
				button6.setEnabled(true);
				jpbProcessLoading2.setVisible(true);
				jpbProcessLoading2.setIndeterminate(true); // 设置进度条为不确定模式,默认为确定模式
				jpbProcessLoading2.setString("正在爬取，请耐心等待...");

				// 较复杂，耗时线程swingworker
				sw2 = new SwingWorker<ReturnHouseMessage, String>() {

					@Override
					protected ReturnHouseMessage doInBackground() throws Exception {
						SZHmainGui.permitPresaleFlag = true;
						ReturnHouseMessage returnHouseMessage = ppc.permitPresaleController(permitPresaleParams);
						return returnHouseMessage;
					}

					// 此方法是耗时任务完成后的操作
					protected void done() {
						ReturnHouseMessage rhm = null;
						try {
							rhm = get();
						} catch (Exception e) {
							e.printStackTrace();
						}
						jpbProcessLoading2.setIndeterminate(false);
						if (rhm != null) {
							if (rhm.getYNsuccess().equals("是") && rhm.getHfm().isBl()) {
								jpbProcessLoading2.setString("爬取完成！");
								String s = "完成！共计用时：" + rhm.getDate() + "";
								JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
							} else {
								jpbProcessLoading2.setString("爬取失败！");
								String s = rhm.getYNMessage();
								JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
							}
						} else {
							jpbProcessLoading2.setString("爬取已中止！");
							if ("退出".equals(jlp113.getText().toString())) {
								return;
							}
							String s = "爬取已中止！";
							JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
						}
						button3.setEnabled(true);
						button6.setEnabled(false);
						button7.setEnabled(true);
					}

				};

				sw2.execute();
			} else {
				JOptionPane.showMessageDialog(null, "格式不正确！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			}
		}
		// 监听可售房源开始按钮
		if (e.getSource().equals(button9)) {
			long startTime = System.currentTimeMillis();
			HouseQueryParams houseQueryParams = getHouseQueryParams();
			if (hgu.checkHouseQueryParams(houseQueryParams)) {
				button9.setEnabled(false);
				button11.setEnabled(false);
				button10.setEnabled(true);
				jpbProcessLoading3.setVisible(true);
				jpbProcessLoading3.setIndeterminate(true); // 设置进度条为不确定模式,默认为确定模式
				jpbProcessLoading3.setString("正在爬取，请耐心等待...");

				// 较复杂，耗时线程swingworker
				sw3 = new SwingWorker<ReturnHouseMessage, String>() {

					@Override
					protected ReturnHouseMessage doInBackground() throws Exception {
						SZHmainGui.houseQueryFlag = true;
						ReturnHouseMessage returnHouseMessage = hqc.houseQueryController(houseQueryParams);
						return returnHouseMessage;
					}

				};
				sw3.execute();
			} else {
				JOptionPane.showMessageDialog(null, "格式不正确！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			}
		}
		// 监听可售信息公示开始按钮
		if (e.getSource().equals(button12)) {
			long startTime = System.currentTimeMillis();
			MegAnnoAndTradeMegParams maatmp = getMegAnnoAndTradeMegParams();
			button12.setEnabled(false);
			jpbProcessLoading4.setVisible(true);
			jpbProcessLoading4.setIndeterminate(true); // 设置进度条为不确定模式,默认为确定模式
			jpbProcessLoading4.setString("正在爬取，请耐心等待...");

			// 较复杂，耗时线程swingworker
			sw4 = new SwingWorker<ReturnHouseMessage, String>() {

				@Override
				protected ReturnHouseMessage doInBackground() throws Exception {
					ReturnHouseMessage returnHouseMessage = maatmc.megAnnoController(maatmp);
					return returnHouseMessage;
				}

				// 此方法是耗时任务完成后的操作
				protected void done() {
					ReturnHouseMessage rhm = null;
					try {
						rhm = get();
					} catch (Exception e) {
						e.printStackTrace();
					}
					jpbProcessLoading4.setIndeterminate(false);
					if (rhm != null) {
						if (rhm.getYNsuccess().equals("是") && rhm.getHfm().isBl()) {
							jpbProcessLoading4.setString("爬取完成！");
							String s = "完成！共计用时：" + rhm.getDate() + "";
							JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
						} else {
							jpbProcessLoading4.setString("爬取失败！");
							String s = rhm.getYNMessage();
							JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
						}
					} else {
						jpbProcessLoading4.setString("爬取失败！");
						if ("退出".equals(jlp113.getText().toString())) {
							return;
						}
						String s = "爬取失败！";
						JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
					}
					button12.setEnabled(true);
				}

			};
			sw4.execute();
		}
		// 监听即时成交信息开始按钮
		if (e.getSource().equals(button13)) {
			long startTime = System.currentTimeMillis();
			MegAnnoAndTradeMegParams maatmp = getMegAnnoAndTradeMegParams();
			button13.setEnabled(false);
			jpbProcessLoading5.setVisible(true);
			jpbProcessLoading5.setIndeterminate(true); // 设置进度条为不确定模式,默认为确定模式
			jpbProcessLoading5.setString("正在爬取，请耐心等待...");

			// 较复杂，耗时线程swingworker
			sw5 = new SwingWorker<ReturnHouseMessage, String>() {

				@Override
				protected ReturnHouseMessage doInBackground() throws Exception {
					ReturnHouseMessage returnHouseMessage = maatmc.tradeMegController(maatmp);
					return returnHouseMessage;
				}

				// 此方法是耗时任务完成后的操作
				protected void done() {
					ReturnHouseMessage rhm = null;
					try {
						rhm = get();
					} catch (Exception e) {
						e.printStackTrace();
					}
					jpbProcessLoading5.setIndeterminate(false);
					if (rhm != null) {
						if (rhm.getYNsuccess().equals("是") && rhm.getHfm().isBl()) {
							jpbProcessLoading5.setString("爬取完成！");
							String s = "完成！共计用时：" + rhm.getDate() + "";
							JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
						} else {
							jpbProcessLoading5.setString("爬取失败！");
							String s = rhm.getYNMessage();
							JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
						}
					} else {
						jpbProcessLoading5.setString("爬取失败！");
						if ("退出".equals(jlp113.getText().toString())) {
							return;
						}
						String s = "爬取失败！";
						JOptionPane.showMessageDialog(null, s, "提示消息", JOptionPane.WARNING_MESSAGE);
					}
					button13.setEnabled(true);
				}
			};
			sw5.execute();
		}

		// 监听可售楼盘展示中止按钮
		if (e.getSource().equals(button4)) {
			// 更改状态
			SZHmainGui.houseShowFlag = false;
			button2.setEnabled(true);
			button4.setEnabled(false);
			button5.setEnabled(true);
			sw1.cancel(true);
			// 延迟三秒当后台停止后将状态更改回来，防止其他逻辑调用公共方法出现bug
			// try {
			// Thread.sleep(3000);
			// SZHmainGui.houseShowFlag = true;
			// } catch (InterruptedException e1) {
			// e1.printStackTrace();
			// }
		}
		// 监听预售许可证中止按钮
		if (e.getSource().equals(button6)) {
			// 更改状态
			SZHmainGui.permitPresaleFlag = false;
			button3.setEnabled(true);
			button6.setEnabled(false);
			button7.setEnabled(true);
			sw2.cancel(true);
			// 延迟三秒当后台停止后将状态更改回来，防止其他逻辑调用公共方法出现bug
			// try {
			// Thread.sleep(3000);
			// SZHmainGui.permitPresaleFlag = true;
			// } catch (InterruptedException e1) {
			// e1.printStackTrace();
			// }
		}

		// 监听重置按钮
		if (e.getSource().equals(button5)) {
			clearHouseShow();
		}
		// 监听重置按钮
		if (e.getSource().equals(button7)) {
			clearPermitPresale();
		}
		// 监听重置按钮
		if (e.getSource().equals(button11)) {
			clearHouseQuery();
		}

		// 打开网页
		if (e.getSource().equals(button14) || e.getSource().equals(button15)) {
			hgu.openUrlByBrowser("http://www.szfcweb.com/");
		}

	}

	// 获取可售楼盘展示参数
	private HouseShowParams getHouseShowParams() {
		HouseShowParams houseShowParams = new HouseShowParams();
		if (jrb1.isSelected()) {
			// 获取模式
			houseShowParams.setSelectPattern(0);
			// 获取文件夹路径
			houseShowParams.setFilePath(tt1.getText());
			// 获取项目名称
			houseShowParams.setProjectName(tt2.getText().trim().toString());
			// 获取公司名称
			houseShowParams.setCompanyName(tt3.getText().trim().toString());
			// 获取项目区域
			houseShowParams.setProjectArea(jcb1.getSelectedItem().toString());
			// 获取页数
			houseShowParams.setPageNumber("0");
		} else if (jrb2.isSelected()) {
			// 获取模式
			houseShowParams.setSelectPattern(1);
			// 获取文件夹路径
			houseShowParams.setFilePath(tt1.getText());
			// 获取页数
			// 不填默认一千页
			houseShowParams.setPageNumber(
					"".equals(tt4.getText().trim().toString()) ? "1000" : (tt4.getText().trim().toString()));
			// 是否全部
			houseShowParams.setAllData(jrb3.isSelected());
			// 获取项目名称
			houseShowParams.setProjectName("");
			// 获取公司名称
			houseShowParams.setCompanyName("");
			// 获取项目区域
			houseShowParams.setProjectArea("工业园区");
		}
		return houseShowParams;
	}

	// 获取预售许可证参数
	private PermitPresaleParams getPermitPresaleParams() {
		PermitPresaleParams permitPresaleParams = new PermitPresaleParams();
		// 获取路径
		permitPresaleParams.setFilePath(tt1.getText());
		// 获取区域
		permitPresaleParams.setProjectArea(jcb2.getSelectedItem().toString());
		// 获取页数
		// 不填默认一千页
		permitPresaleParams
				.setPageNumber("".equals(tt5.getText().trim().toString()) ? "1000" : (tt5.getText().trim().toString()));
		return permitPresaleParams;
	}

	// 获取可售房源参数
	private HouseQueryParams getHouseQueryParams() {
		HouseQueryParams houseQueryParams = new HouseQueryParams();
		// 获取文件夹路径
		houseQueryParams.setFilePath(tt6.getText());
		// 获取装修情况
		houseQueryParams.setFitmentSelect(jrb4.isSelected() ? 0 : (jrb5.isSelected() ? 1 : 2));
		// 获取项目区域
		houseQueryParams.setProjectArea(jcb3.getSelectedItem().toString());
		// 房屋用途
		houseQueryParams.setHouseUse(jcb4.getSelectedItem().toString());
		// 房屋最低价
		houseQueryParams.setHouseMinPrice(tt7.getText().trim().toString());
		// 房屋最高价
		houseQueryParams.setHouseMaxPrice(tt8.getText().trim().toString());
		// 房屋最小面积
		houseQueryParams.setHouseMinArea(tt9.getText().trim().toString());
		// 房屋最大面积
		houseQueryParams.setHouseMaxArea(tt10.getText().trim().toString());
		return houseQueryParams;
	}

	// 获取可售查询和即时成交的参数
	public MegAnnoAndTradeMegParams getMegAnnoAndTradeMegParams() {
		MegAnnoAndTradeMegParams maatmp = new MegAnnoAndTradeMegParams();
		// 获取路径
		maatmp.setFilePath(tt6.getText());
		return maatmp;
	}

	private void clearHouseQuery() {
		button9.setEnabled(true);
		button10.setEnabled(false);
		jrb4.setSelected(true);
		jcb3.setModel(new DefaultComboBoxModel(hgu.getAreaType()));
		jcb4.setModel(new DefaultComboBoxModel(hgu.getHouseUseType()));
		tt7.setText("");
		tt8.setText("");
		tt9.setText("");
		tt10.setText("");
		jpbProcessLoading3.setVisible(false);
	}

	private void clearPermitPresale() {
		button3.setEnabled(true);
		button6.setEnabled(false);
		jfc.setCurrentDirectory(new File(setParams.getFilePath()));
		jcb2.setModel(new DefaultComboBoxModel(hgu.getAreaType()));
		tt5.setText("");
		jpbProcessLoading2.setVisible(false);
	}

	private void clearHouseShow() {
		jfc.setCurrentDirectory(new File(setParams.getFilePath()));
		jrb1.setSelected(true);
		tt2.setText("");
		tt3.setText("");
		jcb1.setModel(new DefaultComboBoxModel(hgu.getAreaType()));
		tt4.setText("");
		tt4.setEnabled(false);
		jrb3.setSelected(false);
		jrb3.setEnabled(false);
		button2.setEnabled(true);
		button4.setEnabled(false);
		jpbProcessLoading1.setVisible(false);
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
				if (button1.isEnabled() && button2.isEnabled() && button3.isEnabled() && button9.isEnabled()
						&& button12.isEnabled() && button13.isEnabled()) {
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
							jlp112.setText("退出");
							sw1.cancel(true);
						}
						if (sw2.getState().equals(StateValue.STARTED)) {
							jlp113.setText("退出");
							sw2.cancel(true);
						}
						if (sw3.getState().equals(StateValue.STARTED)) {
							sw3.cancel(true);
						}
						if (sw4.getState().equals(StateValue.STARTED)) {
							sw4.cancel(true);
						}
						if (sw5.getState().equals(StateValue.STARTED)) {
							sw5.cancel(true);
						}
						// Thread.interrupted();
						exitSystem();
					}
				}
			}

			// 监听退出
			if (source.equals(exit)) {
				if (button1.isEnabled() && button2.isEnabled() && button3.isEnabled() && button9.isEnabled()
						&& button12.isEnabled() && button13.isEnabled()) {
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
