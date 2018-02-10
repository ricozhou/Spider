package login.login;

/* 
 * 功能：系统登录
 * 步骤1：登录界面的静态实现 
 * 步骤2：添加对各个组件的监听。 
 * 步骤3：对用户名和密码进行验证。 
 * author：ywq 
 */
import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import HNlandtrade.prosceniumgui.maingame.HNLandMainGUI;
import landmarketnetwork.prosceniumgui.maingame.JSLandMainGUI;
import ZJlandtrade.prosceniumgui.maingame.ZJLandMainGUI;
import booksmanagement.prosceniumgui.maingame.BookManageGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import login.utils.*;
import monitoringsystem.screenshotmonitor.ScreenShotMonitor;
import publicGUI.entity.PassParameter;
import publicGUI.entity.SetParams;
import publicGUI.utils.GetSetProperties;
import suzhouhouse.prosceniumgui.maingame.SZHmainGui;
import taobaojud.prosceniumgui.maingame.TaoBaoMainUI;
import login.entity.CommSetParams;
import login.entity.Constants;
import login.entity.UserInfo;

public class ProGUILogin extends JFrame implements ActionListener {
	// 实例化方法类
	public LoginUtils lu = new LoginUtils();
	public GetProperties gp = new GetProperties();
	public UserInfo userInfo = new UserInfo();
	public PassParameter passParameter;
	public SleepThread st;

	// 定义组件
	// 登录按钮
	JButton loginButton, resetButton, exitButton;
	// 选择用户
	JRadioButton superJrb, commonJrb;
	// 面板
	JPanel jpanel1, jpanel2, jpanel3, jpanel4, jpanel5;
	// 用户名
	JTextField userNameJtf;
	// 标签
	JLabel jlabel1, jlabel2, jlabel3, jlabel4;
	// 密码框
	JPasswordField passwordJpf;
	// 按钮组
	ButtonGroup buttonGroup;
	// 下拉框
	JComboBox jcomboBox;

	// 设定用户名和密码
	public static final String COMMON_NAME = "admin";
	static final String COMMON_PWD = "admin";
	static final String SUPER_NAME = "superadmin";
	static final String SUPER_PWD = "superadmin";

	// 初始化
	public ProGUILogin(PassParameter passParameter) throws IOException {
		this.passParameter = passParameter;
		if (!passParameter.getSetParams().isAllowMultiClient()) {
			if (!lu.checkSocket()) {
				st = new SleepThread();
				st.start();
				JOptionPane.showMessageDialog(null, "程序已启动！请勿重复启动！", "提示消息", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		init();
	}

	// 单例模式
	// private static ProGUILogin proGUILogin=null;
	// public static ProGUILogin getProGUILogin() {
	// if(proGUILogin==null) {
	// proGUILogin=new ProGUILogin();
	// }
	// return proGUILogin;
	// }
	// 另一种单例模式
	// private static class Singleton{
	// private static ProGUILogin proGUILogin=new ProGUILogin();
	// }
	// public static ProGUILogin getProGUILogin() {
	// return Singleton.proGUILogin;
	// }

	public void init() throws IOException {
		// 选择系统
		jlabel4 = new JLabel("请选择系统：");
		jcomboBox = new JComboBox();
		jcomboBox.setModel(new DefaultComboBoxModel(lu.getSystem()));

		// 创建组件
		loginButton = new JButton("登录");
		resetButton = new JButton("重置");
		exitButton = new JButton("退出");

		// 设置监听
		loginButton.addActionListener(this);
		resetButton.addActionListener(this);
		exitButton.addActionListener(this);

		superJrb = new JRadioButton("超级用户");
		commonJrb = new JRadioButton("普通用户");
		buttonGroup = new ButtonGroup();
		buttonGroup.add(superJrb);
		buttonGroup.add(commonJrb);
		commonJrb.setSelected(true);

		jpanel1 = new JPanel();
		jpanel2 = new JPanel();
		jpanel3 = new JPanel();
		jpanel4 = new JPanel();
		jpanel5 = new JPanel();

		jlabel1 = new JLabel("用户名：");
		jlabel2 = new JLabel("密    码：");
		jlabel3 = new JLabel("权    限：");

		userNameJtf = new JTextField(10);
		// 获取焦点
		// userNameJtf.grabFocus();
		// userNameJtf.requestFocus();
		// userNameJtf.setFocusable(true);
		// userNameJtf.requestFocusInWindow();
		// 以上方法均不可靠
		// 这个方法可靠
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				userNameJtf.requestFocus();
			}
		});
		passwordJpf = new JPasswordField(10);
		// 加入到JPanel中
		jpanel1.add(jlabel1);
		jpanel1.add(userNameJtf);

		jpanel2.add(jlabel2);
		jpanel2.add(passwordJpf);

		jpanel3.add(jlabel3);
		jpanel3.add(superJrb);
		jpanel3.add(commonJrb);

		jpanel4.add(loginButton);
		jpanel4.add(resetButton);
		jpanel4.add(exitButton);

		jpanel5.add(jlabel4);
		jpanel5.add(jcomboBox);

		// 加入JFrame中
		this.add(jpanel5);
		this.add(jpanel1);
		this.add(jpanel2);
		this.add(jpanel3);
		this.add(jpanel4);
		// 设置布局管理器
		this.setLayout(new GridLayout(5, 1));
		// 给窗口设置标题
		this.setTitle("系统登录");
		// 设置窗体大小
		this.setSize(350, 250);
		// 设置窗体初始位置
		// this.setLocation(530, 150);
		// 相对别的窗口或者面板位置（居中）
		this.setLocationRelativeTo(null);
		// 设置当关闭窗口时，保证JVM也退出
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 显示窗体
		this.setVisible(true);
		// 不可改变大小
		this.setResizable(false);
		// 登录按钮绑定回车键(按回车即执行监听器内容)
		this.getRootPane().setDefaultButton(loginButton);
		// 更改图标头像
		this.setIconImage(Toolkit.getDefaultToolkit().createImage("image//login.png"));
		// 字体
		// 无效
		// Font font=new Font("宋体", Font.BOLD, 60);
		// this.setFont(font);
	}

	// 监听按钮
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == Constants.BUTTON_LOGIN) {
			// 从配置文件获取用户信息
			Map<String, String> userMap = null;
			try {
				userMap = gp.getOneUser();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			try {
				commonLogin(userMap, superJrb.isSelected());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			// 如果超级登录
			// if (superJrb.isSelected()) {
			// try {
			// // superLogin();
			// commonLogin(userMap);
			// } catch (ClassNotFoundException | InstantiationException |
			// IllegalAccessException
			// | UnsupportedLookAndFeelException e1) {
			// e1.printStackTrace();
			// }
			// } else if (commonJrb.isSelected()) {
			// try {
			// commonLogin(userMap);
			// } catch (ClassNotFoundException | InstantiationException |
			// IllegalAccessException
			// | UnsupportedLookAndFeelException e1) {
			// e1.printStackTrace();
			// }
			// }

		} else if (e.getActionCommand() == Constants.BUTTON_RESET) {
			clear();
		}

		// 监听退出系统
		if (e.getSource().equals(exitButton)) {
			System.exit(0);
		}

	}

	// 普通用户登录判断方法
	public void commonLogin(Map<String, String> userMap, boolean superCommon) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IOException {
		// if (true) {
		if (isTrue(userMap, superCommon)) {
			clear();
			// 关闭当前界面
			dispose();
			// 创建一个新界面
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					if (Constants.TYPE_TAOBAOJUD.equals(jcomboBox.getSelectedItem())) {
						try {
							TaoBaoMainUI ui = new TaoBaoMainUI(passParameter);
							ui.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (Constants.TYPE_SUZHOUHOUSE.equals(jcomboBox.getSelectedItem())) {
						try {
							SZHmainGui szhgui = new SZHmainGui(passParameter);
							szhgui.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (Constants.TYPE_BOOKMANAGE.equals(jcomboBox.getSelectedItem())) {
						BookManageGUI bmgui = null;
						try {
							bmgui = new BookManageGUI(passParameter);
						} catch (IOException e) {
							e.printStackTrace();
						}
						bmgui.setVisible(true);
					} else if (Constants.TYPE_JSLANDNETWORK.equals(jcomboBox.getSelectedItem())) {
						JSLandMainGUI jslmg = null;
						try {
							jslmg = new JSLandMainGUI(passParameter);
						} catch (IOException e) {
							e.printStackTrace();
						}
						jslmg.setVisible(true);
					} else if (Constants.TYPE_HNLANDTRADE.equals(jcomboBox.getSelectedItem())) {
						HNLandMainGUI hnlmg = null;
						try {
							hnlmg = new HNLandMainGUI(passParameter);
						} catch (IOException e) {
							e.printStackTrace();
						}
						hnlmg.setVisible(true);
					} else if (Constants.TYPE_ZJLANDTRADE.equals(jcomboBox.getSelectedItem())) {
						ZJLandMainGUI zjlmg = null;
						try {
							zjlmg = new ZJLandMainGUI(passParameter);
						} catch (IOException e) {
							e.printStackTrace();
						}
						zjlmg.setVisible(true);
					}
				}
			});
		} else if (userNameJtf.getText().isEmpty() && passwordJpf.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "请输入用户名和密码！", "提示消息", JOptionPane.WARNING_MESSAGE);
		} else if (userNameJtf.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "请输入用户名！", "提示消息", JOptionPane.WARNING_MESSAGE);
		} else if (passwordJpf.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "请输入密码！", "提示消息", JOptionPane.WARNING_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "用户名或者密码错误！\n请重新输入", "提示消息", JOptionPane.ERROR_MESSAGE);
			// 清空输入框
			clear();
		}
	}

	// 超级用户登录判断方法
	public void superLogin() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		if (SUPER_NAME.equals(userNameJtf.getText()) && SUPER_PWD.equals(passwordJpf.getText())) {
			// System.out.println("登录成功");
			clear();
			// 关闭当前界面
			dispose();
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
		} else if (userNameJtf.getText().isEmpty() && passwordJpf.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "请输入用户名和密码！", "提示消息", JOptionPane.WARNING_MESSAGE);
		} else if (userNameJtf.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "请输入用户名！", "提示消息", JOptionPane.WARNING_MESSAGE);
		} else if (passwordJpf.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "请输入密码！", "提示消息", JOptionPane.WARNING_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "用户名或者密码错误！\n请重新输入", "提示消息", JOptionPane.ERROR_MESSAGE);
			// 清空输入框
			clear();
		}
	}

	// 清空文本框和密码框
	public void clear() {
		userNameJtf.setText("");
		passwordJpf.setText("");
		// userNameJtf.grabFocus();
		// userNameJtf.requestFocus();
		// userNameJtf.setFocusable(true);
		userNameJtf.requestFocusInWindow();
	}

	public boolean isTrue(Map<String, String> userMap, boolean superCommon) {
		if (superCommon) {
			if (SUPER_NAME.equals(userNameJtf.getText()) && SUPER_PWD.equals(passwordJpf.getText())) {
				userInfo.setUserName(userNameJtf.getText().toString().trim());
				userInfo.setPassword(passwordJpf.getText().toString());
				passParameter.setUserInfo(userInfo);
				return true;
			}
		} else {
			if (COMMON_NAME.equals(userNameJtf.getText()) && COMMON_PWD.equals(passwordJpf.getText())) {
				userInfo.setUserName(userNameJtf.getText().toString().trim());
				userInfo.setPassword(passwordJpf.getText().toString());
				passParameter.setUserInfo(userInfo);
				return true;
			}
		}
		if (userMap == null) {
			return false;
		}
		for (Map.Entry<String, String> entry : userMap.entrySet()) {
			if (entry.getKey().equals(userNameJtf.getText()) && entry.getValue().equals(passwordJpf.getText())) {
				userInfo.setUserName(userNameJtf.getText());
				userInfo.setPassword(passwordJpf.getText());
				passParameter.setUserInfo(userInfo);
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		// 监控程序，自启动，每隔一小时发送一张屏幕截图通过邮件
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SetParams setParams = null;
				try {
					setParams = new GetSetProperties().getSetMessage();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				// 处理
				new ScreenShotMonitor().manageMonitor(setParams.isMonitor());
			}

		});

		// Login ms = new Login();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// 获取界面参数
				SetParams setParams = null;
				// 获取通讯设置
				CommSetParams commSetParams = new CommSetParams();
				PassParameter passParameter = new PassParameter();
				GetSetProperties gsp = new GetSetProperties();
				try {
					setParams = gsp.getSetMessage();
					commSetParams = gsp.getCommSetProperties();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				passParameter.setSetParams(setParams);
				passParameter.setCommSetParams(commSetParams);
				try {
					if (setParams.getInterfaceStyle() == 0) {
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					} else if (setParams.getInterfaceStyle() == 1) {
						UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
					} else if (setParams.getInterfaceStyle() == 2) {
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
					} else if (setParams.getInterfaceStyle() == 3) {
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					} else if (setParams.getInterfaceStyle() == 4) {
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
					} else if (setParams.getInterfaceStyle() == 5) {
						// 苹果扁平不理想风格
						UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
					} else if (setParams.getInterfaceStyle() == 6) {
						// 国人牛逼主题，值得学习
						// 初始化字体
						InitGlobalFont(new Font("微软雅黑", Font.PLAIN, 11));
						// 设置本属性将改变窗口边框样式定义
						// 系统默认样式 osLookAndFeelDecorated
						// 强立体半透明 translucencyAppleLike
						// 弱立体感半透明 translucencySmallShadow
						// 普通不透明 generalNoTranslucencyShadow
						BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
						// 设置主题为BeautyEye
						org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
						// 隐藏“设置”按钮
						UIManager.put("RootPane.setupButtonVisible", false);
						// 开启/关闭窗口在不活动时的半透明效果
						// 设置此开关量为false即表示关闭之，BeautyEye LNF中默认是true
						BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
						// 设置BeantuEye外观下JTabbedPane的左缩进
						// 改变InsetsUIResource参数的值即可实现
						UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(3, 20, 2, 20));
						// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} else if (setParams.getInterfaceStyle() == 7) {
						// 木质感+xp风格
						// UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
						// 柔和黑
						// UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
						// 椭圆按钮+黄色按钮背景
						// 其他风格一
						UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
						// //椭圆按钮+绿色按钮背景
						// UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
						// //纯XP风格
						// UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
						// //黑色风格
						// UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
						// // 普通swing风格+蓝色边框
						// UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
						// //黄色风格
						// UIManager.setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
						// //椭圆按钮+翠绿色按钮背景+金属质感
						// 金属质感
						// UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
						// // xp清新风格
						// UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
						// //布质感+swing纯风格,报错
						// UIManager.setLookAndFeel("com.jtattoo.plafacryl.AcrylLookAndFeel");
					} else if (setParams.getInterfaceStyle() == 8) {
						// 其他风格二
						// 金属质感
						UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {
					ProGUILogin ms = new ProGUILogin(passParameter);
					// getProGUILogin();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// 初始化字体
	private static void InitGlobalFont(Font font) {
		FontUIResource fontRes = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontRes);
			}
		}
	}
}

// 重复启动计时线程
class SleepThread extends Thread {
	@Override
	public void run() {
		try {
			Thread.sleep(3000);
			System.exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}