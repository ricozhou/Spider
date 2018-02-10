package publicGUI.toolJPanel.sendmail;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MailMainGui extends JFrame implements ActionListener {
	public JPanel jp1, jp2;
	public JLabel jlb1, jlb2, jlb3, jlb4, jlb5, jlb6, jlb7, jlb8, jlb9, jlb10;
	public JButton button1, button2, button3, button4, button5, button6, button7;
	public JTextArea jta1, jta2;
	public JScrollPane jsp1;
	public JTextField tt1, tt2, tt3, tt4, tt5, tt6, tt7, tt8, tt9;
	public JPasswordField jpf1;
	public JTabbedPane jtp = new JTabbedPane(JTabbedPane.TOP);
	public JProgressBar jpbar1;
	public JComboBox jbb1, jbb2, jbb3;
	public JFileChooser chooseFile1;
	public File[] fs;
	public MailUtils mu = new MailUtils();
	// 收件人
	public String accUser = "";
	public String[] accUsers;
	public boolean accUserIsOk = false;
	// 发件人
	public String sendUser = "";
	public boolean sendUserIsOk = true;
	// 抄送
	public String copyToUser = "";
	public String[] copyToUsers;
	public boolean copyToUserIsOk = false;
	// 暗抄送
	public String darkCopyToUser = "";
	public String[] darkCopyToUsers;
	public boolean darkCopyToUserIsOk = false;
	// 附件路径和名字
	public Map<String, String> filePathName;
	public List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();

	public MailMainGui() {
		init();
	}

	private void init() {
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp1.setLayout(null);
		jp2.setLayout(null);

		jlb1 = new JLabel("SMTP服务：");
		jlb1.setBounds(20, 10, 100, 25);
		jp1.add(jlb1);

		tt1 = new JTextField();
		tt1.setBounds(90, 10, 180, 25);
		jp1.add(tt1);
		// 默认163
		tt1.setText("smtp.163.com");

		jlb2 = new JLabel("端口：");
		jlb2.setBounds(280, 10, 100, 25);
		jp1.add(jlb2);

		tt2 = new JTextField();
		tt2.setBounds(320, 10, 105, 25);
		jp1.add(tt2);
		// 默认25
		tt2.setText("25");

		jlb3 = new JLabel("发   件   人：");
		jlb3.setBounds(20, 50, 100, 25);
		jp1.add(jlb3);

		tt3 = new JTextField();
		tt3.setBounds(90, 50, 180, 25);
		jp1.add(tt3);
		// 默认156
		tt3.setText("15606218315@163.com");
		tt3.addFocusListener(new FocusListener() {
			// 光标不在文本域
			@Override
			public void focusLost(FocusEvent e) {
				sendUser = tt3.getText().trim();
				if (!"".equals(sendUser)) {
					String[] s = new String[1];
					s[0] = sendUser;
					if (!mu.checkMailFormat(s)) {
						sendUserIsOk = false;
						JOptionPane.showMessageDialog(null, "发件人邮箱格式不正确！请检查！", "提示消息", JOptionPane.WARNING_MESSAGE);
						return;
					} else {
						sendUserIsOk = true;
					}
				}
			}

			@Override
			public void focusGained(FocusEvent e) {

			}
		});

		jlb4 = new JLabel("密码：");
		jlb4.setBounds(280, 50, 100, 25);
		jp1.add(jlb4);

		jpf1 = new JPasswordField();
		jpf1.setBounds(320, 50, 105, 25);
		jp1.add(jpf1);
		// 默认密码
		jpf1.setText("rico840310");

		jlb5 = new JLabel("收   件   人：");
		jlb5.setBounds(20, 80, 100, 25);
		jp1.add(jlb5);

		tt4 = new JTextField();
		tt4.setBounds(90, 80, 335, 25);
		jp1.add(tt4);
		// 监听光标
		tt4.addFocusListener(new FocusListener() {
			// 光标不在文本域
			@Override
			public void focusLost(FocusEvent e) {
				accUser = tt4.getText();
				if (!"".equals(accUser) && !accUser.endsWith(";")) {
					accUser = accUser + ";";
				} else {

				}

				// 验证邮箱格式
				if (!"".equals(accUser)) {
					accUsers = accUser.split(";");
					if (!mu.checkMailFormat(accUsers)) {
						accUserIsOk = false;
						JOptionPane.showMessageDialog(null, "收件人邮箱格式不正确！请检查！", "提示消息", JOptionPane.WARNING_MESSAGE);
						return;
					} else {
						accUserIsOk = true;
					}
				} else {

				}
				tt4.setText(accUser);
			}

			// 光标在文本域
			@Override
			public void focusGained(FocusEvent e) {
			}
		});

		jlb6 = new JLabel("抄          送：");
		jlb6.setBounds(20, 110, 100, 25);
		jp1.add(jlb6);

		tt5 = new JTextField();
		tt5.setBounds(90, 110, 335, 25);
		jp1.add(tt5);
		// 监听光标
		tt5.addFocusListener(new FocusListener() {
			// 光标不在文本域
			@Override
			public void focusLost(FocusEvent e) {
				copyToUser = tt5.getText();
				if (!"".equals(copyToUser) && !copyToUser.endsWith(";")) {
					copyToUser = copyToUser + ";";
				} else {

				}

				// 验证邮箱格式
				if (!"".equals(copyToUser)) {
					copyToUsers = copyToUser.split(";");
					if (!mu.checkMailFormat(copyToUsers)) {
						copyToUserIsOk = false;
						JOptionPane.showMessageDialog(null, "抄送邮箱格式不正确！请检查！", "提示消息", JOptionPane.WARNING_MESSAGE);
						return;
					} else {
						copyToUserIsOk = true;
					}
				} else {

				}
				tt5.setText(copyToUser);
			}

			// 光标在文本域
			@Override
			public void focusGained(FocusEvent e) {
			}
		});

		jlb7 = new JLabel("暗   抄   送：");
		jlb7.setBounds(20, 140, 100, 25);
		jp1.add(jlb7);

		tt6 = new JTextField();
		tt6.setBounds(90, 140, 335, 25);
		jp1.add(tt6);
		// 监听光标
		tt6.addFocusListener(new FocusListener() {
			// 光标不在文本域
			@Override
			public void focusLost(FocusEvent e) {
				darkCopyToUser = tt6.getText();
				if (!"".equals(darkCopyToUser) && !darkCopyToUser.endsWith(";")) {
					darkCopyToUser = darkCopyToUser + ";";
				} else {

				}

				// 验证邮箱格式
				if (!"".equals(darkCopyToUser)) {
					darkCopyToUsers = darkCopyToUser.split(";");
					if (!mu.checkMailFormat(darkCopyToUsers)) {
						darkCopyToUserIsOk = false;
						JOptionPane.showMessageDialog(null, "暗抄送邮箱格式不正确！请检查！", "提示消息", JOptionPane.WARNING_MESSAGE);
						return;
					} else {
						darkCopyToUserIsOk = true;
					}
				} else {

				}
				tt6.setText(darkCopyToUser);
			}

			// 光标在文本域
			@Override
			public void focusGained(FocusEvent e) {
			}
		});

		jlb8 = new JLabel("主          题：");
		jlb8.setBounds(20, 170, 100, 25);
		jp1.add(jlb8);

		tt7 = new JTextField();
		tt7.setBounds(90, 170, 335, 25);
		jp1.add(tt7);

		button1 = new JButton("附件");
		button1.setBounds(18, 200, 65, 25);
		button1.addActionListener(this);
		jp1.add(button1);

		button7 = new JButton("清除");
		button7.setBounds(18, 224, 65, 25);
		button7.addActionListener(this);
		jp1.add(button7);
		button7.setEnabled(false);

		tt8 = new JTextField();
		tt8.setBounds(90, 200, 335, 25);
		jp1.add(tt8);
		tt8.setEditable(false);
		// 监听文本框变化
		tt8.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if ("".equals(tt8.getText().trim())) {
					button7.setEnabled(false);
				} else {
					button7.setEnabled(true);
				}
			}

			// 改变是时候
			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});

		jpbar1 = new JProgressBar();
		jpbar1.setBounds(100, 228, 315, 20);
		jpbar1.setMinimum(1);
		jpbar1.setMaximum(100);
		jpbar1.setVisible(true);
		jp1.add(jpbar1);

		jlb9 = new JLabel("正          文：");
		jlb9.setBounds(20, 250, 100, 25);
		jp1.add(jlb9);

		button6 = new JButton("清空");
		button6.setBounds(20, 280, 60, 25);
		button6.addActionListener(this);
		jp1.add(button6);
		button6.setEnabled(false);

		String[] font = { "楷体", "宋体", "仿宋", "黑体" };
		jbb1 = new JComboBox(font);
		jbb1.setBounds(20, 310, 60, 25);
		jbb1.addActionListener(this);
		jp1.add(jbb1);

		String[] textSize = { "11", "5", "10", "15", "20", "25", "30", "35", "40" };
		jbb2 = new JComboBox(textSize);
		jbb2.setBounds(20, 340, 60, 25);
		jbb2.addActionListener(this);
		jp1.add(jbb2);

		jta1 = new JTextArea();
		jsp1 = new JScrollPane(jta1);
		jsp1.setBounds(90, 250, 335, 290);
		jp1.add(jsp1);

		// 监听文本变化
		jta1.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if ("".equals(jta1.getText().trim())) {
					button6.setEnabled(false);
				} else {
					button6.setEnabled(true);
				}
			}

			// 改变是时候
			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});

		button2 = new JButton("发送");
		button2.setBounds(100, 550, 60, 25);
		button2.addActionListener(this);
		jp1.add(button2);

		button3 = new JButton("定时发送");
		button3.setBounds(175, 550, 80, 25);
		button3.addActionListener(this);
		jp1.add(button3);

		button4 = new JButton("存草稿");
		button4.setBounds(270, 550, 70, 25);
		button4.addActionListener(this);
		jp1.add(button4);

		button5 = new JButton("关闭");
		button5.setBounds(355, 550, 60, 25);
		button5.addActionListener(this);
		jp1.add(button5);

		this.add(jtp);
		jtp.add("发送邮件", jp1);
		jtp.add("接收邮件", jp2);

		this.setTitle("邮箱");
		this.setSize(450, 650);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ImageIcon imageIcon = new ImageIcon("image\\headImage\\mail.png");
		this.setIconImage(imageIcon.getImage());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object key = e.getSource();
		// 监听添加附件
		if (key.equals(button1)) {
			// 打开文件选择器
			chooseFile1 = new JFileChooser();
			// 多选
			chooseFile1.setMultiSelectionEnabled(true);
			// 只能选择文件
			chooseFile1.setFileSelectionMode(0);
			int returnVal = chooseFile1.showOpenDialog(this);
			if (returnVal == chooseFile1.APPROVE_OPTION) {
				// 获取所有选中的文件
				fs = chooseFile1.getSelectedFiles();
				// 检查文件的大小，不能超过50兆
				if (!mu.checkFileSize(fs)) {
					JOptionPane.showMessageDialog(null, "单个文件不能超过50M！请检查！", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				// 获取文件的所有名字,及地址
				filePathName = mu.getFilePathName(fs);
				// 加入list(包含所有添加的文件)
				fileList.add(filePathName);
				// 界面显示以分号相间
				String ShowFileName = mu.getFileNameFormat(filePathName);
				tt8.setText(tt8.getText() + ShowFileName);
			}
		}
		// 监听删除附件
		if (key.equals(button7)) {
			// 清除list集合，显示清零
			fileList.clear();
			tt8.setText("");
			button7.setEnabled(false);
		}

		// 监听发送
		if (key.equals(button2)) {

		}

		// 监听定时发送
		if (key.equals(button3)) {

		}

		// 监听存草稿
		if (key.equals(button4)) {

		}

		// 监听关闭
		if (key.equals(button5)) {

		}
		// 清空正文
		if (key.equals(button6)) {
			jta1.setText("");
			button6.setEnabled(false);
		}

	}
}
