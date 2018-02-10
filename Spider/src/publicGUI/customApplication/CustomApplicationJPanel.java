package publicGUI.customApplication;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import publicGUI.customApplication.setDialog.AddAppDialog;
import publicGUI.customApplication.setDialog.DeleteAndEditDialog;
import publicGUI.customApplication.utils.CodeUtils;
import publicGUI.customApplication.utils.CompilerAndRunUtils;
import publicGUI.entity.PassParameter;

//自定义应用
public class CustomApplicationJPanel extends JPanel implements ActionListener {
	public JFrame jf;
	public PassParameter passParameter;
	public JLabel jlb1, jlb2, jlb3, jlb4, jlb5;
	public JButton button0, button01, button02, button1, button2, button3, button4, button5, button6, button7, button8,
			button9, button10, button11, button12, button13, button14;
	public CodeUtils cu = new CodeUtils();
	public CompilerAndRunUtils caru = new CompilerAndRunUtils();
	public List<String[]> strList;
	// 全部按钮集合
	public List<JButton> jbList;
	// 显示按钮集合
	public List<JButton> jbList2;
	public String[] classInfo;
	public String className2;
	public static boolean isDeleteSucc = false;
	public static boolean isOpenCodeJFrame = false;
	public String jarCmd = "";

	public CustomApplicationJPanel(PassParameter passParameter, JFrame jf) throws IOException {
		// 将之前窗口对象传递过来
		this.jf = jf;
		this.passParameter = passParameter;
		init();
	}

	// 初始化
	private void init() throws IOException {
		// 按钮集合
		jbList = new ArrayList<JButton>();
		// 读取配置信息
		strList = cu.getAppInfoProperties();
		jlb1 = new JLabel("自定义应用：");
		jlb1.setBounds(20, 30, 100, 25);

		button0 = new JButton(new ImageIcon("image//newApplication.png"));
		button0.setPreferredSize(new Dimension(50, 50));
		// 不要边框
		button0.setBorder(null);
		button0.setBounds(410, 245, 50, 50);
		button0.addActionListener(this);

		button01 = new JButton(new ImageIcon("image//delete.png"));
		button01.setPreferredSize(new Dimension(50, 50));
		// 不要边框
		button01.setBorder(null);
		button01.setBounds(410, 50, 50, 50);
		button01.addActionListener(this);

		button02 = new JButton(new ImageIcon("image//edit.png"));
		button02.setPreferredSize(new Dimension(50, 50));
		// 不要边框
		button02.setBorder(null);
		button02.setBounds(410, 148, 50, 50);
		button02.addActionListener(this);

		button1 = new JButton(strList.get(0)[1]);
		button1.setBounds(100, 50, 120, 35);
		button1.setVisible(Boolean.parseBoolean(strList.get(0)[2]));
		button1.addActionListener(this);
		jbList.add(button1);

		button2 = new JButton(strList.get(1)[1]);
		button2.setBounds(100, 85, 120, 35);
		button2.setVisible(Boolean.parseBoolean(strList.get(1)[2]));
		button2.addActionListener(this);
		jbList.add(button2);

		button3 = new JButton(strList.get(2)[1]);
		button3.setBounds(100, 120, 120, 35);
		button3.setVisible(Boolean.parseBoolean(strList.get(2)[2]));
		button3.addActionListener(this);
		jbList.add(button3);

		button4 = new JButton(strList.get(3)[1]);
		button4.setBounds(100, 155, 120, 35);
		button4.setVisible(Boolean.parseBoolean(strList.get(3)[2]));
		button4.addActionListener(this);
		jbList.add(button4);

		button5 = new JButton(strList.get(4)[1]);
		button5.setBounds(100, 190, 120, 35);
		button5.setVisible(Boolean.parseBoolean(strList.get(4)[2]));
		button5.addActionListener(this);
		jbList.add(button5);

		button6 = new JButton(strList.get(5)[1]);
		button6.setBounds(100, 225, 120, 35);
		button6.setVisible(Boolean.parseBoolean(strList.get(5)[2]));
		button6.addActionListener(this);
		jbList.add(button6);

		button7 = new JButton(strList.get(6)[1]);
		button7.setBounds(100, 260, 120, 35);
		button7.setVisible(Boolean.parseBoolean(strList.get(6)[2]));
		button7.addActionListener(this);
		jbList.add(button7);

		button8 = new JButton(strList.get(7)[1]);
		button8.setBounds(250, 50, 120, 35);
		button8.setVisible(Boolean.parseBoolean(strList.get(7)[2]));
		button8.addActionListener(this);
		jbList.add(button8);

		button9 = new JButton(strList.get(8)[1]);
		button9.setBounds(250, 85, 120, 35);
		button9.setVisible(Boolean.parseBoolean(strList.get(8)[2]));
		button9.addActionListener(this);
		jbList.add(button9);

		button10 = new JButton(strList.get(9)[1]);
		button10.setBounds(250, 120, 120, 35);
		button10.setVisible(Boolean.parseBoolean(strList.get(9)[2]));
		button10.addActionListener(this);
		jbList.add(button10);

		button11 = new JButton(strList.get(10)[1]);
		button11.setBounds(250, 155, 120, 35);
		button11.setVisible(Boolean.parseBoolean(strList.get(10)[2]));
		button11.addActionListener(this);
		jbList.add(button11);

		button12 = new JButton(strList.get(11)[1]);
		button12.setBounds(250, 190, 120, 35);
		button12.setVisible(Boolean.parseBoolean(strList.get(11)[2]));
		button12.addActionListener(this);
		jbList.add(button12);

		button13 = new JButton(strList.get(12)[1]);
		button13.setBounds(250, 225, 120, 35);
		button13.setVisible(Boolean.parseBoolean(strList.get(12)[2]));
		button13.addActionListener(this);
		jbList.add(button13);

		button14 = new JButton(strList.get(13)[1]);
		button14.setBounds(250, 260, 120, 35);
		button14.setVisible(Boolean.parseBoolean(strList.get(13)[2]));
		button14.addActionListener(this);
		jbList.add(button14);

		this.add(button0);
		this.add(button01);
		this.add(button02);
		this.add(button1);
		this.add(button2);
		this.add(button3);
		this.add(button4);
		this.add(button5);
		this.add(button6);
		this.add(button7);
		this.add(button8);
		this.add(button9);
		this.add(button10);
		this.add(button11);
		this.add(button12);
		this.add(button13);
		this.add(button14);
		this.add(jlb1);
		this.setLayout(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object key = e.getSource();
		// 监听添加按钮
		if (key.equals(button0)) {
			// 弹窗选择添加应用的类型
			AddAppDialog aad = new AddAppDialog(jbList);
		}

		// 监听删除/修改
		if (key.equals(button01) || key.equals(button02)) {
			// 获取显示的按钮集合
			jbList2 = new ArrayList<JButton>();
			JButton jb;
			if (button1.isVisible()) {
				jbList2.add(button1);
			}
			if (button2.isVisible()) {
				jbList2.add(button2);
			}
			if (button3.isVisible()) {
				jbList2.add(button3);
			}
			if (button4.isVisible()) {
				jbList2.add(button4);
			}
			if (button5.isVisible()) {
				jbList2.add(button5);
			}
			if (button6.isVisible()) {
				jbList2.add(button6);
			}
			if (button7.isVisible()) {
				jbList2.add(button7);
			}
			if (button8.isVisible()) {
				jbList2.add(button8);
			}
			if (button9.isVisible()) {
				jbList2.add(button9);
			}
			if (button10.isVisible()) {
				jbList2.add(button10);
			}
			if (button11.isVisible()) {
				jbList2.add(button11);
			}
			if (button12.isVisible()) {
				jbList2.add(button12);
			}
			if (button13.isVisible()) {
				jbList2.add(button13);
			}
			if (button14.isVisible()) {
				jbList2.add(button14);
			}
			String message = "编辑";
			if (key.equals(button01)) {
				message = "删除";
			} else if (key.equals(button02)) {
				message = "修改";
			}
			if (jbList2.size() == 0) {
				JOptionPane.showMessageDialog(null, "无可" + message + "应用！", "提示消息", JOptionPane.WARNING_MESSAGE);
				return;
			}
			// 读取配置获取按钮的状态信息,根据显示信息删除/修改
			String[] deleteInfo;
			try {
				deleteInfo = cu.getAppDeleteInfo();
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, message + "失败！请稍后重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
				e1.printStackTrace();
				return;
			}
			// 打开弹窗
			if (key.equals(button01)) {
				DeleteAndEditDialog dd = new DeleteAndEditDialog(deleteInfo, 0, jbList2, null);
			} else if (key.equals(button02)) {
				DeleteAndEditDialog dd = new DeleteAndEditDialog(deleteInfo, 1, jbList2, jbList);
			}
		}

		// 监听打开应用
		if (key.equals(button1)) {
			String buttonName = strList.get(0)[0];
			openExcClass(buttonName);
		}
		if (key.equals(button2)) {
			String buttonName = strList.get(1)[0];
			openExcClass(buttonName);
		}

		if (key.equals(button3)) {
			String buttonName = strList.get(2)[0];
			openExcClass(buttonName);
		}

		if (key.equals(button4)) {
			String buttonName = strList.get(3)[0];
			openExcClass(buttonName);
		}

		if (key.equals(button5)) {
			String buttonName = strList.get(4)[0];
			openExcClass(buttonName);
		}

		if (key.equals(button6)) {
			String buttonName = strList.get(5)[0];
			openExcClass(buttonName);
		}

		if (key.equals(button7)) {
			String buttonName = strList.get(6)[0];
			openExcClass(buttonName);
		}

		if (key.equals(button8)) {
			String buttonName = strList.get(7)[0];
			openExcClass(buttonName);
		}

		if (key.equals(button9)) {
			String buttonName = strList.get(8)[0];
			openExcClass(buttonName);
		}

		if (key.equals(button10)) {
			String buttonName = strList.get(9)[0];
			openExcClass(buttonName);
		}

		if (key.equals(button11)) {
			String buttonName = strList.get(10)[0];
			openExcClass(buttonName);
		}

		if (key.equals(button12)) {
			String buttonName = strList.get(11)[0];
			openExcClass(buttonName);
		}
		if (key.equals(button13)) {
			String buttonName = strList.get(12)[0];
			openExcClass(buttonName);
		}
		if (key.equals(button14)) {
			String buttonName = strList.get(13)[0];
			openExcClass(buttonName);
		}

	}

	private void openExcClass(String buttonName) {
		// 需要重新读取配置
		try {
			classInfo = cu.getAppInfoProperties(buttonName);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		// 运行
		if ("0".equals(classInfo[4])) {
			caru.startClassPro(classInfo[3], new JTextArea());
		} else if ("1".equals(classInfo[4])) {
			caru.startJarPro(classInfo[3],"jar");
		} else if ("2".equals(classInfo[4])) {
			caru.startJarPro(classInfo[3],"exe");
		}

		// File f = new File("customFunction\\bin\\placeholder");
		// String fPath = f.getAbsolutePath();
		// String path = fPath.substring(0, fPath.length() - 12) + "\\" + classInfo[3] +
		// "\\bin";
		// // 获取jar包所有名称,以便于拼接命令
		// File f2 = new File("customFunction\\bin\\" + classInfo[3] + "\\lib\\");
		// String jarPath;
		// if (!f2.exists()) {
		// jarPath = "";
		// jarCmd = "";
		// } else {
		// jarPath = f2.getAbsolutePath();
		// try {
		// jarCmd = ".;" + caru.getJarFiles(jarPath);
		// } catch (Exception e1) {
		// jarCmd = "";
		// e1.printStackTrace();
		// }
		// }
		// // 通过另起进程执行
		// new SwingWorker<String, String>() {
		//
		// @Override
		// protected String doInBackground() throws Exception {
		// try {
		// // cu.runClassFile(path, codeSetParams.getClassName());
		// cu.runClassFile2(path, classInfo[3], new JTextArea(), jarCmd);
		// } catch (Throwable e1) {
		// JOptionPane.showMessageDialog(null, "运行失败！请检查代码！", "提示消息",
		// JOptionPane.WARNING_MESSAGE);
		// e1.printStackTrace();
		// }
		// return null;
		// }
		//
		// }.execute();

		// EventQueue.invokeLater(new Runnable() {
		// public void run() {
		// // 通过反射动态执行
		// // 运行
		// // 例如e://class//,name:test
		// try {
		// cu.runClassFile(path, classInfo[3]);
		// } catch (Exception e1) {
		// JOptionPane.showMessageDialog(null, "运行失败！请检查代码！", "提示消息",
		// JOptionPane.WARNING_MESSAGE);
		// e1.printStackTrace();
		// }
		// }
		// });

		// // 生成对象
		// Object obj = null;
		// try {
		// System.out.println(classInfo[3]);
		// obj = Class.forName(classInfo[3]).newInstance();
		// Class<? extends Object> cls = obj.getClass();
		// // 调用main方法
		// Method m = cls.getMethod("main", String[].class);
		// m.invoke(obj, (Object) new String[] {});
		// } catch (Exception e) {
		// JOptionPane.showMessageDialog(null, "运行失败！请检查代码！", "提示消息",
		// JOptionPane.WARNING_MESSAGE);
		// e.printStackTrace();
		// }
	}
}
