package publicGUI.customApplication.setDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import publicGUI.customApplication.CodeJFrame;

public class AddAppSet extends JDialog implements ActionListener {
	public JButton button1, button2;
	public JLabel jlp1, jlp2, jlp3, jlp4;
	public JComboBox jcb1, jcb2;
	public JTextField jtf1, jtf2, jtf3;
	public int status = 0;

	public AddAppSet(int status) {
		this.status = status;
		init();
	}

	private void init() {
		this.setLayout(null);
		jlp1 = new JLabel("*请输入应用名：");
		jlp1.setBounds(20, 20, 100, 25);
		jtf1 = new JTextField(10);
		jtf1.setBounds(130, 20, 120, 25);

		button1 = new JButton("确定");
		button1.setBounds(40, 150, 80, 25);
		button2 = new JButton("取消");
		button2.setBounds(160, 150, 80, 25);
		button1.addActionListener(this);
		button2.addActionListener(this);

		this.add(jlp1);
		this.add(jtf1);
		this.add(button1);
		this.add(button2);
		// 必须放在前面设置才有效果
		ImageIcon imageIcon = new ImageIcon("image\\headImage\\setup3.png");
		this.setIconImage(imageIcon.getImage());
		// 原窗口不能动(模态窗口)
		this.setModal(true);
		if (status == 0) {
			this.setTitle("应用添加设置");
		} else if (status == 1) {
			this.setTitle("应用名修改设置");
		}
		this.setSize(300, 250);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听确定按钮
		if (e.getSource().equals(button1)) {
			// 校验参数
			if (!checkParams()) {
				jtf1.setText("");
				return;
			} else {
				CodeJFrame.appName = jtf1.getText().trim().replaceAll("#", "");
				dispose();
			}

		}
		// 监听取消按钮
		if (e.getSource().equals(button2)) {
			dispose();
		}
	}

	// 初步校验修改信息
	private boolean checkParams() {
		if ("".equals(jtf1.getText().trim()) || jtf1.getText().trim().length() > 5) {
			JOptionPane.showMessageDialog(null, "应用名为空或超过六个字符！", "提示消息", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	// 获取应用名
	// public String getAppName() {
	// return jtf1.getText().trim();
	// }

}
