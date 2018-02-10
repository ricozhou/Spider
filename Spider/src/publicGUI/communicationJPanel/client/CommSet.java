package publicGUI.communicationJPanel.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import login.entity.CommSetParams;
import login.entity.UserInfo;
import publicGUI.utils.SaveSet;

public class CommSet extends JDialog implements ActionListener {
	public CommSetParams commSetParams;
	public JTextField userName, serverIP, serverPort;
	public JButton button1, button2;
	public JLabel jlp1, jlp2, jlp3, jlp4;
	public SaveSet ss = new SaveSet();

	public CommSet(CommSetParams commSetParams) {
		this.commSetParams = commSetParams;
		init();
	}

	private void init() {
		this.setLayout(null);
		setLayout(null);
		jlp1 = new JLabel("*请输入服务器：");
		jlp1.setBounds(20, 20, 100, 25);
		serverIP = new JTextField(20);
		serverIP.setText(commSetParams.getServerIP());
		serverIP.setBounds(130, 20, 120, 25);
		jlp2 = new JLabel("*请输入用户名：");
		jlp2.setBounds(20, 50, 100, 25);
		userName = new JTextField(10);
		userName.setText(commSetParams.getUserName());
		userName.setBounds(130, 50, 120, 25);
		jlp3 = new JLabel("*请输入端口号：");
		jlp3.setBounds(20, 80, 100, 25);
		serverPort = new JTextField(20);
		serverPort.setText(commSetParams.getServerPort());
		serverPort.setBounds(130, 80, 120, 25);

		button1 = new JButton("保存");
		button1.setBounds(40, 170, 80, 25);
		button2 = new JButton("取消");
		button2.setBounds(160, 170, 80, 25);
		button1.addActionListener(this);
		button2.addActionListener(this);

		this.add(jlp1);
		this.add(serverIP);
		this.add(jlp2);
		this.add(userName);
		this.add(jlp3);
		this.add(serverPort);
		this.add(button1);
		this.add(button2);
		ImageIcon imageIcon = new ImageIcon("image\\headImage\\setup1.png");
		this.setIconImage(imageIcon.getImage());
		// 原窗口不能动(模态窗口)
		this.setModal(true);
		this.setTitle("通讯设置");
		this.setSize(300, 250);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听保存按钮
		if (e.getSource().equals(button1)) {
			// 校验参数
			if (!checkParams()) {
				return;
			}
			if (!checkParamsDetail()) {
				return;
			}
			CommSetParams commSetParams2 = new CommSetParams();
			commSetParams2.setUserName(userName.getText().toString().trim());
			commSetParams2.setServerIP(serverIP.getText().toString().trim());
			commSetParams2.setServerPort(serverPort.getText().toString().trim());
			try {
				if (ss.saveCommSetParams(commSetParams2)) {
					JOptionPane.showMessageDialog(null, "保存设置成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "保存设置失败！\n请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
					serverIP.setText(commSetParams.getServerIP());
					userName.setText(commSetParams.getUserName());
					serverPort.setText(commSetParams.getServerPort());
				}
			} catch (HeadlessException | IOException e1) {
				e1.printStackTrace();
			}
		}

		// 监听取消按钮
		if (e.getSource().equals(button2)) {
			dispose();
		}
	}

	// 初步校验修改信息
	private boolean checkParams() {
		if (serverIP.getText().isEmpty() || userName.getText().isEmpty() || serverPort.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "请将设置信息输入完整！", "提示消息", JOptionPane.WARNING_MESSAGE);
		} else if (serverIP.getText().length() > 14) {
			JOptionPane.showMessageDialog(null, "IP格式不正确！\n请重新输入！", "提示消息", JOptionPane.WARNING_MESSAGE);
			serverIP.setText("");
		} else if (userName.getText().length() > 15) {
			JOptionPane.showMessageDialog(null, "用户名不能超过15个字符！\n请重新输入！", "提示消息", JOptionPane.WARNING_MESSAGE);
			userName.setText("");
		} else if (serverPort.getText().length() > 5) {
			JOptionPane.showMessageDialog(null, "端口号不能超过5位数！\n请重新输入！", "提示消息", JOptionPane.WARNING_MESSAGE);
			serverPort.setText("");
		} else {
			return true;
		}
		return false;
	}

	// 详细校验主要校验IP格式
	private boolean checkParamsDetail() {
		String ip = serverIP.getText();
		if (!"".equals(ip) && ip.contains(".") && !ip.startsWith(".")) {
			String s = ip.substring(0, ip.indexOf("."));
			if (s.length() < 4) {
				// 经过复杂校验
				return true;
			}
		}
		JOptionPane.showMessageDialog(null, "IP格式不正确！\n请重新输入！", "提示消息", JOptionPane.WARNING_MESSAGE);
		serverPort.setText("");

		return false;
	}
}
