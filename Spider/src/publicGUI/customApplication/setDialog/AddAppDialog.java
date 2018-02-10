package publicGUI.customApplication.setDialog;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import publicGUI.customApplication.CodeJFrame;
import publicGUI.customApplication.CustomApplicationJPanel;
import publicGUI.customApplication.JarAndExeJFrame;
import publicGUI.customApplication.utils.CodeUtils;

public class AddAppDialog extends JDialog implements ActionListener {
	public JButton button1, button2;
	public JLabel jlp1;
	public JComboBox jcb1, jcb2;
	public CodeUtils cu = new CodeUtils();
	public List<JButton> jbList;

	public AddAppDialog(List<JButton> jbList) {
		this.jbList = jbList;
		init();
	}

	private void init() {

		this.setLayout(null);
		jlp1 = new JLabel("*请选择应用类型：");
		jlp1.setBounds(20, 20, 130, 25);
		String[] appType = { "编程", "Jar文件", "exe文件" };
		jcb1 = new JComboBox(appType);
		jcb1.setBounds(140, 20, 90, 25);

		button1 = new JButton("确定");
		button1.setBounds(40, 150, 80, 25);
		button2 = new JButton("取消");
		button2.setBounds(160, 150, 80, 25);
		button1.addActionListener(this);
		button2.addActionListener(this);

		this.add(jlp1);
		this.add(jcb1);
		this.add(button1);
		this.add(button2);
		// 必须放在前面设置才有效果
		ImageIcon imageIcon = null;
		imageIcon = new ImageIcon("image//newApplication.png");
		this.setTitle("应用添加");
		this.setIconImage(imageIcon.getImage());
		// 原窗口不能动(模态窗口)
		this.setModal(true);
		this.setSize(300, 250);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听确定按钮
		if (e.getSource().equals(button1)) {
			// 下标
			int index = jcb1.getSelectedIndex();
			if (index == 0) {
				// 实例化窗口编写代码
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							// 打开编程窗口
							if (CustomApplicationJPanel.isOpenCodeJFrame) {
								JOptionPane.showMessageDialog(null, "不能同时打开两个编程窗口！", "提示消息",
										JOptionPane.WARNING_MESSAGE);
							} else {
								CodeJFrame cjf = new CodeJFrame(jbList);
								CustomApplicationJPanel.isOpenCodeJFrame = true;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			} else if (index == 1) {
				// 添加jar文件可执行应用
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							// 打开jar应用界面
							JarAndExeJFrame jaej = new JarAndExeJFrame(jbList, 1);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			} else if (index == 2) {
				// 添加exe文件可执行应用
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							// 打开exe应用界面
							JarAndExeJFrame jaej = new JarAndExeJFrame(jbList, 2);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			dispose();
		}
		// 监听取消按钮
		if (e.getSource().equals(button2)) {
			dispose();
		}
	}
}
