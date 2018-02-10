package ZJlandtrade.prosceniumgui.maingame;

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
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import booksmanagement.prosceniumgui.entity.BookInformation;
import booksmanagement.prosceniumgui.utils.BookUtils;
import publicGUI.entity.PassParameter;
import publicGUI.entity.SetParams;
import publicGUI.gameJPanel.GameJPanel;
import publicGUI.setJPanel.MinimizeTrayJPanel;
import publicGUI.setJPanel.SetJPanel;
import publicGUI.toolJPanel.ToolJPanel;
import publicGUI.utils.GetSetProperties;

public class ZJLandMainGUI extends JFrame implements ActionListener {
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
	public String[] tabNames = { "解析浙江土地交易系统", "小工具", "小游戏", "小通讯", "小设置" };

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

	public PassParameter passParameter;
	SetParams setParams = new SetParams();
	public GetSetProperties gsp = new GetSetProperties();
	public BookUtils bu = new BookUtils();

	public ZJLandMainGUI(PassParameter passParameter) throws IOException {
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
		jtf1.setBounds(110, 10, 195, 25);
		jlb1.setBounds(20, 10, 130, 25);
		button1.setBounds(320, 10, 80, 25);
		button1.addActionListener(this); // 添加事件处理
		jp1.add(jtf1);
		jp1.add(jlb1);
		jp1.add(button1);

		// jlb9 = new JLabel("功能选择：");
		// jlb9.setBounds(20, 10, 80, 25);
		// jp1.add(jlb9);
		//
		// jrb1 = new JRadioButton("图书添加");
		// jrb1.setBounds(90, 10, 80, 25);
		// jrb1.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// if (jrb1.isSelected()) {
		// jtf1.setEnabled(true);
		// jtf2.setEnabled(true);
		// jtf3.setEnabled(true);
		// jcb1.setEnabled(true);
		// jck1.setEnabled(true);
		// jck2.setEnabled(true);
		// jtf4.setEnabled(true);
		// jtf5.setEnabled(false);
		// // button1.setEnabled(false);
		// button1.setText("添加");
		// jtf6.setEnabled(false);
		// }
		// }
		// });
		// jp1.add(jrb1);
		// jrb2 = new JRadioButton("图书查询");
		// jrb2.setBounds(250, 10, 80, 25);
		// jrb2.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// if (jrb2.isSelected()) {
		// // jtf1.setEnabled(false);
		// // jtf2.setEnabled(false);
		// // jtf3.setEnabled(false);
		// // jcb1.setEnabled(false);
		// // jck1.setEnabled(false);
		// // jck2.setEnabled(false);
		// // jtf4.setEnabled(false);
		// jtf5.setEnabled(true);
		// // button1.setEnabled(true);
		// button1.setText("查询");
		// jtf6.setEnabled(true);
		//
		// }
		// }
		// });
		// jp1.add(jrb2);
		// bg1 = new ButtonGroup();
		// bg1.add(jrb1);
		// bg1.add(jrb2);
		// jrb1.setSelected(true);
		//
		// jlb10 = new JLabel("书名：");
		// jlb10.setBounds(90, 40, 40, 25);
		// jp1.add(jlb10);
		// jtf5 = new JTextField();
		// jtf5.setBounds(140, 40, 140, 25);
		// jtf5.setEnabled(false);
		// jp1.add(jtf5);
		//
		// jlb11 = new JLabel("作者：");
		// jlb11.setBounds(290, 40, 40, 25);
		// jp1.add(jlb11);
		// jtf6 = new JTextField();
		// jtf6.setBounds(330, 40, 120, 25);
		// jtf6.setEnabled(false);
		// jp1.add(jtf6);
		//
		// button1 = new JButton("添加");
		// button1.setBounds(90, 70, 60, 25);
		// // button1.setEnabled(false);
		// button1.addActionListener(this);
		// jp1.add(button1);
		// button2 = new JButton("重置");
		// button2.setBounds(160, 70, 60, 25);
		// // button2.setVisible(false);
		// button2.addActionListener(this);
		// jp1.add(button2);
		// button3 = new JButton("修改");
		// button3.setBounds(230, 70, 60, 25);
		// button3.setVisible(false);
		// button3.addActionListener(this);
		// jp1.add(button3);
		//
		// // button8 = new JButton("存");
		// // button8.setBounds(230, 70, 50, 25);
		// //// button8.setVisible(false);
		// // button8.addActionListener(this);
		// // jp1.add(button8);
		//
		// button4 = new JButton("上本");
		// button4.setBounds(300, 70, 70, 25);
		// button4.setVisible(false);
		// button4.addActionListener(this);
		// jp1.add(button4);
		// button5 = new JButton("下本");
		// button5.setBounds(380, 70, 70, 25);
		// button5.setVisible(false);
		// button5.addActionListener(this);
		// jp1.add(button5);
		//
		// jlb1 = new JLabel("图书信息：");
		// jlb1.setBounds(20, 100, 80, 25);
		// jp1.add(jlb1);
		//
		// jlb2 = new JLabel("书名：");
		// jlb2.setBounds(90, 100, 40, 25);
		// jp1.add(jlb2);
		// jtf1 = new JTextField();
		// jtf1.setBounds(140, 100, 140, 25);
		// jp1.add(jtf1);
		//
		// jlb3 = new JLabel("作者：");
		// jlb3.setBounds(290, 100, 40, 25);
		// jp1.add(jlb3);
		// jtf2 = new JTextField();
		// jtf2.setBounds(330, 100, 120, 25);
		// jp1.add(jtf2);
		//
		// jlb4 = new JLabel("出版社：");
		// jlb4.setBounds(90, 130, 55, 25);
		// jp1.add(jlb4);
		// jtf3 = new JTextField();
		// jtf3.setBounds(140, 130, 140, 25);
		// jp1.add(jtf3);
		//
		// jlb5 = new JLabel("分类：");
		// jlb5.setBounds(290, 130, 40, 25);
		// jp1.add(jlb5);
		// jcb1 = new JComboBox();
		// jcb1.setBounds(330, 130, 120, 25);
		// jp1.add(jcb1);
		//
		// jlb6 = new JLabel("出版时间：");
		// jlb6.setBounds(90, 160, 80, 25);
		// jp1.add(jlb6);
		// jtf7 = new JTextField();
		// jtf7.setBounds(150, 160, 115, 25);
		// jp1.add(jtf7);
		//
		// jlb7 = new JLabel("入库时间：");
		// jlb7.setBounds(267, 160, 80, 25);
		// jp1.add(jlb7);
		// jtf8 = new JTextField();
		// jtf8.setBounds(330, 160, 120, 25);
		// jp1.add(jtf8);
		//
		// jck1 = new JCheckBox("是否已读");
		// jck1.setSelected(false);
		// jck1.setBounds(85, 190, 80, 25);
		// jp1.add(jck1);
		//
		// jck2 = new JCheckBox("是否想读");
		// jck2.setSelected(false);
		// jck2.setBounds(205, 190, 80, 25);
		// jp1.add(jck2);
		//
		// jlb8 = new JLabel("入库地点：");
		// jlb8.setBounds(290, 190, 80, 25);
		// jp1.add(jlb8);
		// jtf4 = new JTextField();
		// jtf4.setBounds(360, 190, 90, 25);
		// jp1.add(jtf4);
		//
		// jlb11 = new JLabel("阅读指数：");
		// jlb11.setBounds(90, 220, 80, 25);
		// jp1.add(jlb11);
		// jtf9 = new JTextField();
		// jtf9.setBounds(150, 220, 80, 25);
		// jp1.add(jtf9);
		// jlb14 = new JLabel("(1--10分)");
		// jlb14.setBounds(240, 220, 80, 25);
		// jp1.add(jlb14);
		//
		// jlb12 = new
		// JLabel("_____________________________________________________________");
		// jlb12.setBounds(20, 230, 430, 25);
		// jp1.add(jlb12);
		//
		// jlb13 = new JLabel("随机阅读：");
		// jlb13.setBounds(20, 255, 80, 25);
		// jp1.add(jlb13);
		//
		// button6 = new JButton("开始");
		// button6.setBounds(20, 285, 60, 25);
		// jp1.add(button6);
		// button7 = new JButton("一键");
		// button7.setBounds(20, 315, 60, 25);
		// jp1.add(button7);
		//
		// String[] read = { "未读", "不限", "已读" };
		// jcb2 = new JComboBox(read);
		// jcb2.setBounds(90, 255, 70, 25);
		// jp1.add(jcb2);
		// String[] wantRead = { "想读", "不限", "不想" };
		// jcb3 = new JComboBox(wantRead);
		// jcb3.setBounds(170, 255, 70, 25);
		// jp1.add(jcb3);
		// // String[] read={};
		// jcb4 = new JComboBox();
		// jcb4.setBounds(250, 255, 100, 25);
		// jp1.add(jcb4);
		// String[] bookSite = { "苏州", "新沂", "无锡" };
		// jcb5 = new JComboBox(bookSite);
		// jcb5.setBounds(360, 255, 90, 25);
		// jp1.add(jcb5);

		this.getContentPane().add(tp);
		// 主系统
		tp.add(tabNames[0], jp1);
		tp.add(tabNames[1], new ToolJPanel(passParameter, this));
		// 小游戏
		tp.add(tabNames[2], new GameJPanel(passParameter, this));
		// 小通讯
		tp.add(tabNames[3], jp4);
		// 小设置
		tp.add(tabNames[4], new SetJPanel(passParameter, this, button1));

		this.setTitle("解析浙江土地交易系统 - (" + passParameter.getUserInfo().getUserName() + ")");
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
			int state = jfc1.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
			if (state == 1) {
				return;
			} else {
				File f = jfc1.getSelectedFile();// f为选择到的目录
				jtf1.setText(f.getAbsolutePath());
			}
		}


	}


	// 监听菜单栏
	class Listen extends JFrame implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			// 监听切换账号
			if (source.equals(cut)) {

			}

			// 监听退出
			if (source.equals(exit)) {

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
