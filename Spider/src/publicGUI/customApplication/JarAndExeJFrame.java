package publicGUI.customApplication;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileFilter;

import publicGUI.customApplication.utils.CodeUtils;

public class JarAndExeJFrame extends JFrame implements ActionListener {
	public JButton button1, button2, button3, button4;
	public JLabel jlp1, jlp2, jlp3, jlp4;
	public JComboBox jcb1, jcb2;
	public JTextField jtf1, jtf2, jtf3;
	public CodeUtils cu = new CodeUtils();
	public JLabel jlb1, jlb2;
	public JButton saveButton;
	public JFileChooser chooseFile1;
	public JProgressBar jpbProcessLoading;
	public File file;
	public List<JButton> jbList;
	public int appType;
	public String msg1;
	public String appName;
	public String jarName;

	public JarAndExeJFrame(List<JButton> jbList, int appType) {
		this.appType = appType;
		this.jbList = jbList;
		init();
	}

	private void init() {
		this.setLayout(null);
		if (appType == 1) {
			jlp1 = new JLabel("*请选择Jar文件：");
			button3 = new JButton("选择Jar");
			msg1 = "Jar";
		} else if (appType == 2) {
			jlp1 = new JLabel("*请选择Exe文件：");
			button3 = new JButton("选择Exe");
			msg1 = "Exe";
		}
		jlp1.setBounds(20, 20, 100, 25);
		button3.setBounds(130, 20, 120, 25);
		button3.addActionListener(this);
		button3.setVisible(true);

		jlp2 = new JLabel("请选择资源包：");
		jlp2.setBounds(25, 50, 100, 25);
		button4 = new JButton("选择资源包");
		button4.setBounds(130, 50, 120, 25);
		button4.addActionListener(this);
		button4.setVisible(true);

		jlp3 = new JLabel("*请创建应用名：");
		jlp3.setBounds(20, 80, 100, 25);
		jtf1 = new JTextField(10);
		jtf1.setBounds(130, 80, 120, 25);

		// 进度条
		jpbProcessLoading = new JProgressBar();
		jpbProcessLoading.setStringPainted(true); // 呈现字符串
		jpbProcessLoading.setBounds(40, 110, 200, 25);
		jpbProcessLoading.setVisible(false);

		button1 = new JButton("保存");
		button1.setBounds(40, 170, 80, 25);
		button2 = new JButton("取消");
		button2.setBounds(160, 170, 80, 25);
		button1.addActionListener(this);
		button2.addActionListener(this);

		this.add(jlp1);
		this.add(jlp2);
		this.add(jlp3);
		this.add(jtf1);
		this.add(button1);
		this.add(button2);
		this.add(button3);
		this.add(button4);
		this.add(jpbProcessLoading);
		// 必须放在前面设置才有效果
		ImageIcon imageIcon = new ImageIcon("image\\headImage\\setup2.png");
		this.setIconImage(imageIcon.getImage());
		if (appType == 1) {
			this.setTitle("添加Jar应用");
		} else if (appType == 2) {
			this.setTitle("添加Exe应用");
		}
		this.setSize(300, 250);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听添加Jar或者exe
		if (e.getSource().equals(button3)) {
			// 打开文件选择器
			chooseFile1 = new JFileChooser();
			// 多选
			// chooseFile1.setMultiSelectionEnabled(true);
			// 只能选择文件
			chooseFile1.setFileSelectionMode(0);
			// 文件过滤
			// chooseFile1.addChoosableFileFilter(new FileCanChoose());
			chooseFile1.setFileFilter(new FileCanChoose2(appType));
			int returnVal = chooseFile1.showOpenDialog(this);
			if (returnVal == chooseFile1.APPROVE_OPTION) {
				file = chooseFile1.getSelectedFile();
				// 判断名字是否重复
				if (!cu.compareJavaFromCache(file.getName())) {
					JOptionPane.showMessageDialog(null, "文件名重复！请修改!", "提示消息", JOptionPane.WARNING_MESSAGE);
					file = null;
					return;
				} else {
					jarName = file.getName().substring(0, file.getName().lastIndexOf("."));
				}
			}
		}

		// 监听添加资源包
		if (e.getSource().equals(button4)) {

		}

		// 监听保存按钮
		if (e.getSource().equals(button1)) {
			if (file == null) {
				JOptionPane.showMessageDialog(null, "请选择" + msg1 + "文件！", "提示消息", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if ("".equals(jtf1.getText().trim())) {
				JOptionPane.showMessageDialog(null, "请输入应用名！", "提示消息", JOptionPane.WARNING_MESSAGE);
			} else {
				if (!checkParams()) {
					jtf1.setText("");
					return;
				} else {
					appName = jtf1.getText().trim().replaceAll("#", "");
					// 开始添加
					// 首先复制Jar文件至指定文件夹
					try {
						// 判断是否同名
						if (!cu.compareJavaFromCache(jarName)) {
							JOptionPane.showMessageDialog(null, msg1 + "重名！", "提示消息", JOptionPane.WARNING_MESSAGE);
							return;
						}

						jpbProcessLoading.setVisible(true);
						jpbProcessLoading.setIndeterminate(true); // 设置进度条为不确定模式,默认为确定模式
						jpbProcessLoading.setString("正在导入" + msg1 + "，请稍等！...");
						// 保存复制jar包到指定文件夹
						// 目标目录
						String tarDir = "customFunction\\bin\\" + jarName + "\\ecut\\";
						// 导入jar包
						File[] fs = new File[1];
						fs[0] = file;
						if (cu.copyJarToClassDir(tarDir, fs)) {
							jpbProcessLoading.setIndeterminate(false);
							jpbProcessLoading.setString("导入" + msg1 + "文件成功！");
						} else {
							jpbProcessLoading.setString("");
							jpbProcessLoading.setVisible(false);
							JOptionPane.showMessageDialog(null, "导入" + msg1 + "失败！\n请重试！", "提示消息",
									JOptionPane.WARNING_MESSAGE);
						}
					} catch (Exception e1) {
						jpbProcessLoading.setString("");
						jpbProcessLoading.setVisible(false);
						JOptionPane.showMessageDialog(null, "导入失败！\n请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
						e1.printStackTrace();
						return;
					}
					// 添加应用
					if (!"".equals(appName)) {
						// 遍历之前的按钮，检查是否可见，选择最近一个不可见按钮开始配置
						int count = 0;
						for (JButton jb : jbList) {
							count++;
							if (!jb.isVisible()) {
								// 将应用名和.class文件路径一起写入文件
								int index = jbList.indexOf(jb);
								try {
									if (cu.updateAppInfotParams("button" + (index + 1), appName, true, jarName,
											String.valueOf(appType))) {
										jb.setText(appName);
										jb.setVisible(true);
										// 遍历配置文件对比已添加应用的类和缓存文件夹里的类，
										// 缓存多余的则删除
										try {
											cu.compareJavaFile2();
										} catch (IOException e1) {
											e1.printStackTrace();
										}
										JOptionPane.showMessageDialog(null, "添加应用成功！", "提示消息",
												JOptionPane.WARNING_MESSAGE);
										break;
									} else {
										JOptionPane.showMessageDialog(null, "添加应用失败！请重试！", "提示消息",
												JOptionPane.WARNING_MESSAGE);
										return;
									}
								} catch (IOException e1) {
									e1.printStackTrace();
								}

							} else if (count == 14) {
								JOptionPane.showMessageDialog(null, "应用过多！请删除部分后重试！", "提示消息",
										JOptionPane.WARNING_MESSAGE);
							}
						}
						dispose();
					}
				}
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
}

// 文件过滤器
class FileCanChoose2 extends FileFilter {
	public int appType;

	public FileCanChoose2(int appType) {
		this.appType = appType;
	}

	public boolean accept(File file) {
		String name = file.getName();
		if (appType == 1) {
			return (name.toLowerCase().endsWith(".jar") || file.isDirectory());
		} else if (appType == 2) {
			return (name.toLowerCase().endsWith(".exe") || file.isDirectory());
		} else {
			return (name.toLowerCase().endsWith("") || file.isDirectory());
		}
	}

	public String getDescription() {
		if (appType == 1) {
			return "文件：.jar";
		} else if (appType == 2) {
			return "文件：.exe";
		} else {
			return "文件：.";
		}
	}
}
