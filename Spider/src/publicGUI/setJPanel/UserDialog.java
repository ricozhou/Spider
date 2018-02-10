package publicGUI.setJPanel;

import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import login.entity.Constants;
import login.entity.UserInfo;
import publicGUI.utils.SaveSet;
import taobaojud.prosceniumgui.maingame.TaoBaoMainUI;

//自定义弹窗
public class UserDialog extends JDialog implements ActionListener {
	JTextField userName;
	JPasswordField userPwd, newUserPwd1, newUserPwd2;
	JButton button1, button2;
	JLabel jlp1, jlp2, jlp3, jlp4;
	public UserInfo userInfo;
	public SaveSet ss=new SaveSet();
	public UserDialog(UserInfo userInfo) {
		init(userInfo);
	}

	private void init(UserInfo userInfo) {
		this.userInfo=userInfo;
		setLayout(null);
		jlp1 = new JLabel("*请输入原密码：");
		jlp1.setBounds(20, 20, 100, 25);
		userPwd = new JPasswordField(20);
		userPwd.setBounds(130, 20, 120, 25);
		jlp2 = new JLabel("*请输入用户名：");
		jlp2.setBounds(20, 50, 100, 25);
		userName = new JTextField(10);
		userName.setText(userInfo.getUserName());
		userName.setBounds(130, 50, 120, 25);
		jlp3 = new JLabel("*请输入新密码：");
		jlp3.setBounds(20, 80, 100, 25);
		newUserPwd1 = new JPasswordField(20);
		newUserPwd1.setBounds(130, 80, 120, 25);
		jlp4 = new JLabel("*请确认新密码：");
		jlp4.setBounds(20, 110, 100, 25);
		newUserPwd2 = new JPasswordField(20);
		newUserPwd2.setBounds(130, 110, 120, 25);

		button1 = new JButton("保存");
		button1.setBounds(40, 170, 80, 25);
		button2 = new JButton("取消");
		button2.setBounds(160, 170, 80, 25);
		button1.addActionListener(this);
		button2.addActionListener(this);

		this.add(jlp1);
		this.add(userPwd);
		this.add(jlp2);
		this.add(userName);
		this.add(jlp3);
		this.add(newUserPwd1);
		this.add(jlp4);
		this.add(newUserPwd2);
		this.add(button1);
		this.add(button2);
		ImageIcon imageIcon = new ImageIcon("image\\headImage\\changeKey.png");
		this.setIconImage(imageIcon.getImage());
		// 原窗口不能动(模态窗口)
		this.setModal(true);
		this.setTitle("修改用户及密码");
		this.setSize(300, 250);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//监听保存按钮
		if(e.getSource().equals(button1)) {
			//校验参数
			if(!checkParams()) {
				return;
			}
			if(!checkParamsDetail()) {
				return;
			}
			userInfo.setUserName(userName.getText().toString().trim());
			userInfo.setPassword(newUserPwd1.getText().toString().trim());
			try {
				if(ss.saveUserInfo(userInfo)) {
					JOptionPane.showMessageDialog(null, "修改用户名及密码成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
					dispose();
				}else {
					JOptionPane.showMessageDialog(null, "修改用户名及密码失败！\n请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
					userName.setText(userInfo.getUserName());
				}
			} catch (HeadlessException | IOException e1) {
				e1.printStackTrace();
			}
		}
		
		//监听取消按钮
		if(e.getSource().equals(button2)) {
			dispose();
		}
	}

	//初步校验修改信息
	private boolean checkParams() {
		if(userPwd.getText().isEmpty()||userName.getText().isEmpty()||newUserPwd1.getText().isEmpty()||newUserPwd2.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "请将信息输入完整！", "提示消息", JOptionPane.WARNING_MESSAGE);
		} else if (!userPwd.getText().equals(userInfo.getPassword())) {
			JOptionPane.showMessageDialog(null, "密码不正确！\n请重新输入！", "提示消息", JOptionPane.WARNING_MESSAGE);
			userPwd.setText("");
		} else if (userName.getText().length()>15) {
			JOptionPane.showMessageDialog(null, "用户名不能超过15个字符！\n请重新输入！", "提示消息", JOptionPane.WARNING_MESSAGE);
			userName.setText("");
		} else if (newUserPwd1.getText().length()>15||newUserPwd2.getText().length()>15) {
			JOptionPane.showMessageDialog(null, "密码不能超过15个字符！\n请重新输入！", "提示消息", JOptionPane.WARNING_MESSAGE);
			newUserPwd1.setText("");
			newUserPwd2.setText("");
		} else if(!newUserPwd1.getText().equals(newUserPwd2.getText())){
			JOptionPane.showMessageDialog(null, "密码不一致！\n请重新输入", "提示消息", JOptionPane.ERROR_MESSAGE);
			newUserPwd1.setText("");
			newUserPwd2.setText("");
		}else {
			return true;
		}
		return false;
	}

	//详细校验
	private boolean checkParamsDetail() {
		return true;
	}
}
