package publicGUI.customApplication.setDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;

import publicGUI.customApplication.utils.CodeUtils;

public class BuildPathJarDialog extends JDialog implements ActionListener {
	public JButton button1, button2, button3, button4;
	public JLabel jlb1, jlb2, jlb3, jlb4;
	public JFileChooser chooseFile1;
	public File[] fs;
	public JScrollPane jsp1;
	public JList jl1;
	public String className;
	public CodeUtils cu = new CodeUtils();
	public String configFilePath;
	public String[] buildPathJar;
	public String jarPaths;
	public String[] relativePath;
	// 数据模型
	public DefaultListModel jarData;

	public BuildPathJarDialog(String className) throws Exception {
		this.className = className;
		init();
	}

	private void init() throws Exception {
		this.setLayout(null);
		jlb1 = new JLabel("已选：");
		jlb1.setBounds(20, 20, 60, 25);
		// 读取配置
		// 拼接配置文件所在的位置
		configFilePath = "customFunction\\bin\\" + className + "\\resource\\" + className + "config.properties";
		buildPathJar = cu.getBuildPathJar(new File(configFilePath));
		jarData = new DefaultListModel();
		jl1 = new JList(jarData);
		// 只能单选
		jl1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if (buildPathJar == null) {

		} else {
			for (int i = 0; i < buildPathJar.length; i++) {
				jarData.addElement(buildPathJar[i]);
			}
			// 默认选中第一个
			jl1.setSelectedIndex(0);
		}

		jsp1 = new JScrollPane(jl1);
		jsp1.setBounds(50, 20, 250, 270);

		button1 = new JButton("添加");
		button1.setBounds(310, 20, 60, 25);
		button1.addActionListener(this);

		button2 = new JButton("删除");
		button2.setBounds(310, 50, 60, 25);
		button2.addActionListener(this);
		if (jarData.size() > 0) {
			button2.setEnabled(true);
		} else {
			button2.setEnabled(false);
		}

		button3 = new JButton("保存");
		button3.setBounds(100, 305, 80, 25);
		button3.addActionListener(this);

		button4 = new JButton("关闭");
		button4.setBounds(220, 305, 80, 25);
		button4.addActionListener(this);

		this.add(button1);
		this.add(button2);
		this.add(button3);
		this.add(button4);
		this.add(jlb1);
		this.add(jsp1);

		// 必须放在前面设置才有效果
		ImageIcon imageIcon = new ImageIcon("image\\headImage\\setup2.png");
		this.setIconImage(imageIcon.getImage());
		// 原窗口不能动(模态窗口)
		this.setModal(true);
		this.setTitle("Build Path Jar");
		this.setSize(400, 380);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听添加
		if (e.getSource().equals(button1)) {
			// 弹出选择框
			// 打开文件选择器
			chooseFile1 = new JFileChooser();
			// 多选
			chooseFile1.setMultiSelectionEnabled(true);
			// 只能选择文件
			chooseFile1.setFileSelectionMode(0);
			// 文件过滤
			// chooseFile1.addChoosableFileFilter(new FileCanChoose());
			chooseFile1.setFileFilter(new FileCanChoose3());
			int returnVal = chooseFile1.showOpenDialog(this);
			if (returnVal == chooseFile1.APPROVE_OPTION) {
				// 获取jar的所有路径
				fs = chooseFile1.getSelectedFiles();
			} else {
				return;
			}
			// 判断路径是否在本程序内，进行一些路径的处理
			relativePath = new String[fs.length];
			for (int i = 0; i < fs.length; i++) {
				relativePath[i] = cu.getRelativePath(fs[i].getAbsolutePath());
			}
			// 处理数组变字符串
			// jarPaths = cu.proJarToString(buildPathJar, relativePath);
			// 用显示的拼接
			// jarPaths = cu.proJarToString(jarData, relativePath);
			// 显示到界面
			// String[] jars = jarPaths.split("#");
			// if (jars != null) {
			// for (int i = 0; i < jars.length; i++) {
			// jarData.addElement(jars[i]);
			// }
			// button2.setEnabled(true);
			// } else {
			//
			// }

			if (relativePath != null) {
				for (int i = 0; i < relativePath.length; i++) {
					if (jarData.size() > 0) {
						int count = 0;
						for (int j = 0; j < jarData.size(); j++) {
							if (!jarData.get(j).equals(relativePath[i])) {
								count++;
							} else {
								// JOptionPane.showMessageDialog(null, "请勿重复添加！", "提示消息",
								// JOptionPane.WARNING_MESSAGE);
							}
						}
						if (count == jarData.size()) {
							jarData.addElement(relativePath[i]);
						}
					} else {
						jarData.addElement(relativePath[i]);
					}

				}
				button2.setEnabled(true);
			} else {

			}

		}
		// 监听删除
		if (e.getSource().equals(button2)) {
			// 获取选择中的jar
			if (jarData.size() > 0) {

				if (jl1.getSelectedValue() != null) {
					String chooseJar = jl1.getSelectedValue().toString();
					// 从列表中删除
					jarData.removeElement(chooseJar);
				} else {

				}
			} else {

			}
			if (jarData.size() < 1) {
				button2.setEnabled(false);
			}

		}

		// 监听保存
		if (e.getSource().equals(button3)) {
			// 获取列表中的所有路径
			String jarPaths2 = "";
			if (jarData.size() > 0) {
				for (int i = 0; i < jarData.getSize(); i++) {
					jarPaths2 = jarPaths2 + jarData.getElementAt(i) + "#";
				}
			} else {
				return;
			}

			// 将所有路径写入文件(追加)
			try {
				if (cu.updateBuildPathJarConfig(configFilePath, jarPaths2)) {
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Build Path Jar失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		// 监听关闭
		if (e.getSource().equals(button4)) {
			dispose();
		}
	}
}

// 文件过滤器
class FileCanChoose3 extends FileFilter {
	public boolean accept(File file) {
		String name = file.getName();
		return (name.toLowerCase().endsWith(".jar") || file.isDirectory());
	}

	public String getDescription() {
		return "文件：.jar";
	}
}
