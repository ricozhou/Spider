package publicGUI.setJPanel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import login.entity.UserInfo;
import login.login.ProGUILogin;
import publicGUI.entity.PassParameter;
import publicGUI.entity.SetParams;
import publicGUI.utils.CommonUtils;
import publicGUI.utils.GetSetProperties;
import publicGUI.utils.SaveSet;

public class SetJPanel extends JPanel implements ActionListener {
	public JLabel jlp1, jlp2, jlp3, jlp4, jlp5;
	public JCheckBox jcbox1, jcbox2, jcbox3, jcbox4, jcbox5, jcbox6, jcbox7;
	public JButton button1, button2, button3, button;
	public JComboBox jcomboBox1;
	public SetParams setParams;
	public GetSetProperties gsp = new GetSetProperties();
	public UserInfo userInfo;
	public JFrame jf;
	public SaveSet ss = new SaveSet();
	public CommonUtils cu = new CommonUtils();
	public PassParameter passParameter;

	public SetJPanel(PassParameter passParameter, JFrame jf, JButton button) throws IOException {
		// 将之前窗口对象传递过来
		this.jf = jf;
		this.button = button;
		this.passParameter = passParameter;
		// jf.setExtendedState(JFrame.ICONIFIED);
		init();
	}

	public void init() throws IOException {
		userInfo = passParameter.getUserInfo();
		// 获取配置信息
		setParams = passParameter.getSetParams();
		// 设计设置面板
		jlp1 = new JLabel("常规设置：");
		jlp1.setBounds(20, 20, 100, 25);
		jcbox1 = new JCheckBox("是否选择开机自启动", setParams.isAutoStart());
		jcbox1.setBounds(90, 40, 150, 25);
		jcbox2 = new JCheckBox(" 是否打开消息通知", setParams.isOpenMessage());
		jcbox2.setBounds(90, 60, 150, 25);
		jcbox3 = new JCheckBox("是否记住文件夹路径", setParams.isRememberFilePath());
		jcbox3.setBounds(90, 80, 150, 25);
		jcbox5 = new JCheckBox("是否点击X号最小化系统托盘", setParams.isMinimizeTray());
		jcbox5.setBounds(280, 40, 200, 25);
		jlp4 = new JLabel("界面风格：");
		jlp4.setBounds(280, 60, 60, 25);
		jcomboBox1 = new JComboBox();
		jcomboBox1.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) { // 选项状态更改事件
//				changeTheme();
			}
		});
		jcomboBox1.setModel(new DefaultComboBoxModel(cu.getInterfaceStyle()));
		// 设置默认显示值
		jcomboBox1.setSelectedIndex(setParams.getInterfaceStyle());
		;
		jcomboBox1.setBounds(350, 60, 110, 25);
		jlp2 = new JLabel("个人设置：");
		jlp2.setBounds(20, 110, 100, 25);
		button1 = new JButton("修改用户及密码");
		button1.setBounds(93, 140, 140, 20);
		button1.addActionListener(this);
		button2 = new JButton("修改个人信息");
		button2.setBounds(93, 170, 140, 20);
		button2.addActionListener(this);
		jlp3 = new JLabel("高级设置：");
		jlp3.setBounds(20, 200, 100, 25);
		jcbox4 = new JCheckBox("是否启动自检查更新", setParams.isAutoUpdate());
		jcbox4.setBounds(90, 220, 150, 25);
		jcbox6 = new JCheckBox("是否允许多个客户端", setParams.isAllowMultiClient());
		jcbox6.setBounds(90, 240, 150, 25);
		jcbox7 = new JCheckBox("是否允许对桌面监控", setParams.isMonitor());
		jcbox7.setBounds(90, 260, 150, 25);
		// 超级用户才显示
		if ("superadmin".equals(userInfo.getUserName())) {
			jcbox7.setVisible(true);
		} else {
			jcbox7.setVisible(false);
		}
		button3 = new JButton("保存");
		button3.setBounds(350, 280, 100, 25);
		button3.addActionListener(this);

		this.add(jlp1);
		this.add(jcbox1);
		this.add(jcbox2);
		this.add(jlp2);
		this.add(button1);
		this.add(button2);
		this.add(jlp3);
		this.add(jlp4);
		this.add(jcbox3);
		this.add(jcbox4);
		this.add(jcbox5);
		this.add(jcbox6);
		this.add(jcbox7);
		this.add(jcomboBox1);
		this.add(button3);
		this.setLayout(null);
	}

	protected void changeTheme() {
		int index = jcomboBox1.getSelectedIndex();
		//需要更新界面
		javax.swing.SwingUtilities.updateComponentTreeUI(this);
		try {
			if (index == 0) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			} else if (index == 1) {
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			} else if (index == 2) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			} else if (index == 3) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} else if (index == 4) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
			} else if (index == 5) {
				// 苹果扁平不理想风格
				UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
			} else if (index == 6) {
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
			}
//			SwingUtilities.updateComponentTreeUI(this); 
			javax.swing.SwingUtilities.updateComponentTreeUI(this);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听修改用户按钮
		if (e.getSource().equals(button1)) {
			if (userInfo.getUserName().equals(ProGUILogin.COMMON_NAME)) {
				JOptionPane.showMessageDialog(null, "超级管理员无法更改！", "提示消息", JOptionPane.WARNING_MESSAGE);
				return;
			}
			UserDialog ujp = new UserDialog(userInfo);
		}

		// 监听修改个人信息按钮
		if (e.getSource().equals(button2)) {
			UserImageUpload uiu = new UserImageUpload();
		}

		// 监听修改设置按钮
		if (e.getSource().equals(button3)) {
			setParams.setAutoStart(jcbox1.isSelected());
			setParams.setOpenMessage(jcbox2.isSelected());
			setParams.setRememberFilePath(jcbox3.isSelected());
			setParams.setAutoUpdate(jcbox4.isSelected());
			setParams.setAllowMultiClient(jcbox6.isSelected());
			setParams.setMinimizeTray(jcbox5.isSelected());
			setParams.setInterfaceStyle(jcomboBox1.getSelectedIndex());
			if ("superadmin".equals(userInfo.getUserName())) {
				setParams.setMonitor(jcbox7.isSelected());
			}
			if (ss.saveSet(setParams)) {
				// JOptionPane.showMessageDialog(null, "修改设置成功！", "提示消息",
				// JOptionPane.WARNING_MESSAGE);
				// 自定义弹窗,返回按钮的下标
				String[] option = { "是", "否" };
				int index = JOptionPane.showOptionDialog(this, "设置更改完成，重启生效！\n是否立即重启？", "提示消息", JOptionPane.YES_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
				if (index == 0) {
					// 是否正在进行任务
					if (!button.isEnabled()) {
						// 表示是否终止
						String[] option2 = { "强制重启", "稍后重试" };
						int index2 = JOptionPane.showOptionDialog(this, "请注意！任务正在进行！\n是否立即重启？", "提示消息",
								JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, option2, option2[0]);
						if (index2 == 0) {
							// 重启方法
							if (!ss.reStartPro()) {
								JOptionPane.showMessageDialog(null, "重启失败！\n请稍后重试！", "提示消息",
										JOptionPane.WARNING_MESSAGE);
							}
						}
					} else {
						// 重启方法
						if (!ss.reStartPro()) {
							JOptionPane.showMessageDialog(null, "重启失败！\n请稍后重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
						}
					}

				}
			} else {
				JOptionPane.showMessageDialog(null, "修改设置失败！\n请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
			}
		}
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
