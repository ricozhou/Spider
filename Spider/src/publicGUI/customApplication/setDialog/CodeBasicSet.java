package publicGUI.customApplication.setDialog;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileFilter;

import publicGUI.customApplication.CodeJFrame;
import publicGUI.customApplication.entity.CodeSetParams;
import publicGUI.customApplication.utils.CodeUtils;

public class CodeBasicSet extends JDialog implements ActionListener {
	public JButton button1, button2, button3, button4;
	public JLabel jlp1, jlp2, jlp3, jlp4;
	public JComboBox jcb1, jcb2;
	public JTextField jtf1, jtf2, jtf3;
	public CodeUtils cu = new CodeUtils();
	public JLabel jlb1, jlb2;
	public JButton saveButton;
	public JTextPane programmeArea;
	public JFileChooser chooseFile1;
	public JProgressBar jpbProcessLoading;
	public File[] fs;

	public CodeBasicSet(JLabel jlb1, JLabel jlb2, JButton saveButton, JTextPane programmeArea) {
		this.jlb1 = jlb1;
		this.jlb2 = jlb2;
		this.saveButton = saveButton;
		this.programmeArea = programmeArea;
		init();
	}

	private void init() {

		this.setLayout(null);
		jlp1 = new JLabel("*请选择语言：");
		jlp1.setBounds(20, 20, 100, 25);
		String[] codeLan = { "Java", "Python" };
		jcb1 = new JComboBox(codeLan);
		jcb1.setBounds(130, 20, 120, 25);
		jlp2 = new JLabel("*请输入类名：");
		jlp2.setBounds(20, 50, 100, 25);

		jtf1 = new JTextField(10);
		jtf1.setBounds(130, 50, 120, 25);
		// jtf1.addFocusListener(new FocusListener() {
		// // 重要方法,监听光标获取失去
		// // 比对输入的类是否与缓存文件夹类重名
		// @Override
		// public void focusLost(FocusEvent e) {
		// String className = jtf1.getText().trim();
		// // 类名命名规则判断
		// if (cu.formatClassName(className)) {
		// // 判断是否与缓存文件夹重复
		// if (!cu.compareJavaFromCache(className)) {
		// JOptionPane.showMessageDialog(null, "类名重复！请重新命名！", "提示消息",
		// JOptionPane.WARNING_MESSAGE);
		// jtf1.setText("");
		// return;
		// }
		// } else {
		// JOptionPane.showMessageDialog(null, "命名失败！", "提示消息",
		// JOptionPane.WARNING_MESSAGE);
		// jtf1.setText("");
		// return;
		// }
		// }
		//
		// @Override
		// public void focusGained(FocusEvent e) {
		//
		// }
		// });
		jlp3 = new JLabel("请导入Jar包：");
		jlp3.setBounds(20, 80, 100, 25);
		button3 = new JButton("选择Jar包");
		button3.setBounds(130, 80, 120, 25);
		button3.addActionListener(this);
		button3.setVisible(true);

		// 进度条
		jpbProcessLoading = new JProgressBar();
		jpbProcessLoading.setStringPainted(true); // 呈现字符串
		jpbProcessLoading.setBounds(40, 110, 200, 25);
		jpbProcessLoading.setVisible(false);

		jlp4 = new JLabel("请导入资源包：");
		jlp4.setBounds(20, 110, 100, 25);
		button4 = new JButton("选择资源包");
		button4.setBounds(130, 110, 120, 25);
		button4.addActionListener(this);
		button4.setVisible(true);
		// jtf3 = new JTextField(10);
		// jtf3.setBounds(130, 110, 120, 25);

		button1 = new JButton("保存");
		button1.setBounds(40, 170, 80, 25);
		button2 = new JButton("取消");
		button2.setBounds(160, 170, 80, 25);
		button1.addActionListener(this);
		button2.addActionListener(this);

		this.add(jlp1);
		this.add(jcb1);
		this.add(jlp2);
		this.add(jlp3);
		this.add(jlp4);
		this.add(jtf1);
		// this.add(jtf2);
		// this.add(jtf3);
		this.add(button1);
		this.add(button2);
		this.add(button3);
		this.add(button4);
		this.add(jpbProcessLoading);

		// 必须放在前面设置才有效果
		ImageIcon imageIcon = new ImageIcon("image\\headImage\\setup2.png");
		this.setIconImage(imageIcon.getImage());
		// 原窗口不能动(模态窗口)
		this.setModal(true);
		this.setTitle("自定义应用基本设置");
		this.setSize(300, 250);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 多选jar
		if (e.getSource().equals(button3)) {
			// 打开文件选择器
			chooseFile1 = new JFileChooser();
			// 多选
			chooseFile1.setMultiSelectionEnabled(true);
			// 只能选择文件
			chooseFile1.setFileSelectionMode(0);
			// 文件过滤
			// chooseFile1.addChoosableFileFilter(new FileCanChoose());
			chooseFile1.setFileFilter(new FileCanChoose());
			int returnVal = chooseFile1.showOpenDialog(this);
			if (returnVal == chooseFile1.APPROVE_OPTION) {
				fs = chooseFile1.getSelectedFiles();
			}
		}
		// 监听保存按钮
		if (e.getSource().equals(button1)) {
			// 校验参数
			if (!checkParams()) {
				return;
			}
			String className = jtf1.getText().trim();
			// 类名命名规则判断
			if (cu.formatClassName(className)) {
				// 判断是否与缓存文件夹重复
				if (!cu.compareJavaFromCache(className)) {
					JOptionPane.showMessageDialog(null, "类名重复！请重新命名！", "提示消息", JOptionPane.WARNING_MESSAGE);
					jtf1.setText("");
					return;
				}
			} else {
				JOptionPane.showMessageDialog(null, "命名失败！请检查类名是否合法！", "提示消息", JOptionPane.WARNING_MESSAGE);
				jtf1.setText("");
				return;
			}
			CodeSetParams codeSetParams = new CodeSetParams();
			// 获取语言类型
			codeSetParams.setCodeLanguage(jcb1.getSelectedIndex());
			// 获取类名
			codeSetParams.setClassName(jtf1.getText().trim().toString());
			try {
				jpbProcessLoading.setVisible(true);
				jpbProcessLoading.setIndeterminate(true); // 设置进度条为不确定模式,默认为确定模式
				jpbProcessLoading.setString("正在保存配置，请稍等！...");
				if (cu.saveCodeSetParams(codeSetParams)) {
					// 保存完类名，开始复制jar包到指定文件夹
					// 目标目录
					String tarDir = "customFunction\\bin\\" + codeSetParams.getClassName() + "\\lib\\";
					// 导入jar包
					if (cu.copyJarToClassDir(tarDir, fs)) {
						jpbProcessLoading.setIndeterminate(false);
						jpbProcessLoading.setString("保存配置成功！");
						// JOptionPane.showMessageDialog(null, "保存设置成功！", "提示消息",
						// JOptionPane.WARNING_MESSAGE);
						jlb1.setText(codeSetParams.getCodeLanguage() == 0 ? "  语言：Java" : "  语言：Python");
						jlb2.setText("类名：" + codeSetParams.getClassName());
						programmeArea.setText("public class " + codeSetParams.getClassName() + " {\r\n"
								+ "    public static void main(String[] args) {\r\n\r\n" + "    }\r\n}");
						saveButton.setEnabled(true);
						programmeArea.setEditable(true);
						CodeJFrame.isBasicSet = true;
						dispose();
					} else {
						jpbProcessLoading.setString("");
						jpbProcessLoading.setVisible(false);
						JOptionPane.showMessageDialog(null, "导入Jar包失败！\n请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
					}

				} else {
					jpbProcessLoading.setString("");
					jpbProcessLoading.setVisible(false);
					JOptionPane.showMessageDialog(null, "保存设置失败！\n请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
				}
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				jpbProcessLoading.setString("");
				jpbProcessLoading.setVisible(false);
				JOptionPane.showMessageDialog(null, "保存设置失败！\n请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
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
		return true;
	}

}

// 文件过滤器
class FileCanChoose extends FileFilter {
	public boolean accept(File file) {
		String name = file.getName();
		return (name.toLowerCase().endsWith(".jar") || file.isDirectory());
	}

	public String getDescription() {
		return "文件：.jar";
	}
}
