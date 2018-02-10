package publicGUI.customApplication;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.StyledDocument;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRef;

import publicGUI.customApplication.entity.CodeSetParams;
import publicGUI.customApplication.setDialog.AddAppSet;
import publicGUI.customApplication.setDialog.BuildPathJarDialog;
import publicGUI.customApplication.setDialog.CodeBasicSet;
import publicGUI.customApplication.utils.CodeUtils;
import publicGUI.customApplication.utils.CompilerAndRunUtils;
import taobaojud.background.entity.ReturnMessage;

public class CodeJFrame extends JFrame implements ActionListener {
	// 定义组件
	// 菜单栏
	JMenuBar menub;
	JMenu files;
	JMenu edit;
	JMenu help;
	JMenuItem importItem1;
	JMenuItem exportItem;
	JMenuItem buildPathJar;
	JMenuItem helpItem;

	public JFileChooser chooseFile1, chooseFile2,chooseFile3;

	public JLabel jlb1, jlb2, jlb3, jlb4, jlb5;
	public JButton button1, button2, button3, button4, button5, button6, button7, button8, button9, button10;
	public JButton jButton;
	public String buttonName;
	public JFrame jf;
	// 北部面板
	public JPanel northPanel;
	// 基本设置
	public JButton setButton;
	// 测试
	public JButton testButton;
	// 保存
	public JButton saveButton;
	// 添加
	public JButton addAppButton;
	// 中央面板(编程界面)
	public JPanel centerPanel;
	// 编程文本域
	public JTextPane programmeArea;
	// 中间滚动条面板
	public JScrollPane centerJSP;
	// 中央面板(中间和南部加在一起)(分割面板)
	private JSplitPane centerAndSouthPanel;

	// 南部面板
	public JPanel southPanel;
	// 南部显示区域
	public JTextArea showArea;
	// 南部滚动条面板
	public JScrollPane southJSP;
	public StyledDocument doc = null;
	public CodeUtils cu = new CodeUtils();
	public CompilerAndRunUtils caru = new CompilerAndRunUtils();
	public CodeSetParams codeSetParams;
	// 代码
	public String codeString;
	public boolean isCompiledSucc = false;
	public static boolean isBasicSet = false;
	public boolean showSaveButton = false;
	public boolean isProEditable = false;
	public static String appName = "";
	public List<JButton> jbList;
	public String jarCmd = "";
	public File[] fs;

	// 0是新建，1是修改
	public int status = 0;

	public CodeJFrame(List<JButton> jbList) {
		new CodeJFrame(jbList, null, "", false, false, false, 0, null, null);
	}

	public CodeJFrame(List<JButton> jbList, CodeSetParams codeSetParams, String codeString, boolean isBasicSet,
			boolean showSaveButton, boolean isProEditable, int status, JButton jButton, String buttonName) {
		this.jbList = jbList;
		this.codeSetParams = codeSetParams;
		this.codeString = codeString;
		this.isBasicSet = isBasicSet;
		this.showSaveButton = showSaveButton;
		this.isProEditable = isProEditable;
		this.status = status;
		this.jButton = jButton;
		this.buttonName = buttonName;
		initMenu();
		init();
	}

	// 初始化菜单栏
	private void initMenu() {
		// 加载菜单栏
		menub = new JMenuBar();
		files = new JMenu("File(F)");
		edit = new JMenu("Edit(E)");
		help = new JMenu("Help(H)");
		importItem1 = new JMenuItem("导入代码文件");
		exportItem = new JMenuItem("导出代码文件");
		buildPathJar = new JMenuItem("BuildPathJar");
		helpItem = new JMenuItem("帮助");

		files.setMnemonic('F');
		edit.setMnemonic('E');
		help.setMnemonic('H');
		files.add(importItem1);
		files.add(exportItem);
		edit.add(buildPathJar);
		help.add(helpItem);
		menub.add(files);
		menub.add(edit);
		menub.add(help);
		this.setJMenuBar(menub);
		Listen listen = new Listen();
		importItem1.addActionListener(listen);
		exportItem.addActionListener(listen);
		buildPathJar.addActionListener(listen);
		helpItem.addActionListener(listen);
	}

	// 初始化
	private void init() {
		// 先检查是否有无用的class
		try {
			cu.compareJavaFile2();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		// 北侧
		setButton = new JButton("基本配置");
		setButton.setSize(100, 30);
		setButton.addActionListener(this);
		testButton = new JButton("测试代码");
		testButton.setSize(100, 30);
		testButton.addActionListener(this);
		testButton.setEnabled(false);
		saveButton = new JButton("保存并编译");
		saveButton.setSize(100, 30);
		saveButton.addActionListener(this);
		saveButton.setEnabled(showSaveButton);
		if (status == 1) {
			addAppButton = new JButton("保存应用");
		} else if (status == 0) {
			addAppButton = new JButton("添加应用");
		}
		addAppButton.setSize(100, 30);
		addAppButton.addActionListener(this);
		addAppButton.setEnabled(false);
		// 占位，不太会使用布局
		jlb1 = new JLabel("");
		jlb2 = new JLabel("");
		if (codeSetParams == null) {
			jlb1.setText("");
			jlb2.setText("");
		} else {
			jlb1.setText(codeSetParams.getCodeLanguage() == 0 ? "  语言：Java" : "  语言：Python");
			jlb2.setText("类名：" + codeSetParams.getClassName());
		}
		jlb1.setVisible(true);
		jlb2.setVisible(true);
		northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(1, 6));
		northPanel.add(setButton);
		northPanel.add(testButton);
		northPanel.add(saveButton);
		northPanel.add(addAppButton);
		northPanel.add(jlb1);
		northPanel.add(jlb2);
		northPanel.setBorder(new TitledBorder("菜单区域"));

		// 中间
		// 编程区域
		programmeArea = new JTextPane();
		programmeArea.setEditable(isProEditable);
		programmeArea.setText(codeString);
		// 设置代码区字体
		programmeArea.setFont(new Font("宋体", Font.BOLD, 20));
		// doc = programmeArea.getStyledDocument();

		// 每次编程改变都要记录
		programmeArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				saveButton.setEnabled(true);
				testButton.setEnabled(false);
				addAppButton.setEnabled(false);
				isCompiledSucc = false;
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				saveButton.setEnabled(true);
				testButton.setEnabled(false);
				addAppButton.setEnabled(false);
				isCompiledSucc = false;
			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});
		// 滚动条
		centerJSP = new JScrollPane(programmeArea);
		// 水平需要时出现
		centerJSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// 垂直自动出现
		centerJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(centerJSP);
		centerPanel.setBorder(new TitledBorder("代码区域"));

		showArea = new JTextArea();
		// showArea.setEditable(false);
		southJSP = new JScrollPane(showArea);
		// 水平需要时出现
		southJSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// 垂直自动出现
		southJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		southPanel = new JPanel(new BorderLayout());
		southPanel.add(southJSP);
		southPanel.setBorder(new TitledBorder("输出区域"));

		// 中南两部分分割，中间间隔可以调整大小
		centerAndSouthPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centerPanel, southPanel);
		// 设置分割条大小
		centerAndSouthPanel.setDividerSize(10);
		centerAndSouthPanel.setDividerLocation(380);
		// 设置布局管理器
		this.setTitle("编程窗口");
		this.setSize(600, 600);
		this.setLayout(new BorderLayout());
		this.add(northPanel, "North");
		this.add(centerAndSouthPanel, "Center");

		// 窗体居中
		this.setLocationRelativeTo(null);
		// 当窗体结束的时候自动结束进程
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		ImageIcon imageIcon = new ImageIcon("image\\headImage\\programme.png");
		this.setIconImage(imageIcon.getImage());
		// 监听界面关闭事件
		this.addWindowListener(new WindowAdapter() {
			// 当关闭时
			public void windowClosing(WindowEvent e) {
				// 判断添加应用没有启动则删除已编译文件
				// 遍历配置文件对比已添加应用的类和缓存文件夹里的类，
				// 缓存多余的则删除
				try {
					// cu.compareJavaFile();
					// 删除相关文件夹即可
					cu.compareJavaFile2();
					CustomApplicationJPanel.isOpenCodeJFrame = false;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		jf = this;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object key = e.getSource();
		// 监听设置
		if (key.equals(setButton)) {
			// 打开设置弹窗
			CodeBasicSet cbs = new CodeBasicSet(jlb1, jlb2, saveButton, programmeArea);
		}

		// 监听测试按钮
		// 测试即运行
		// 保存即编译
		if (key.equals(testButton)) {
			if (saveButton.isEnabled()) {
				JOptionPane.showMessageDialog(null, "请先保存代码！", "提示消息", JOptionPane.WARNING_MESSAGE);
			} else {
				if (isCompiledSucc) {
					// 运行
					caru.startClassPro(codeSetParams.getClassName(), showArea);
					// File f = new File("customFunction\\bin\\placeholder");
					// String fPath = f.getAbsolutePath();
					// String path = fPath.substring(0, fPath.length() - 12) + "\\" +
					// codeSetParams.getClassName()
					// + "\\bin";
					// // 获取jar包所有名称,以便于拼接命令
					// File f2 = new File("customFunction\\bin\\" + codeSetParams.getClassName() +
					// "\\lib\\");
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
					//
					// // 通过反射动态执行
					// // 另起线程
					// new SwingWorker<String, String>() {
					//
					// @Override
					// protected String doInBackground() throws Exception {
					// try {
					// // cu.runClassFile(path, codeSetParams.getClassName());
					// // 清空控制台
					// showArea.setText("");
					// // 内部类好像不能访问普通局部变量,改成全局即可
					// cu.runClassFile2(path, codeSetParams.getClassName(), showArea, jarCmd);
					//
					// } catch (Throwable e1) {
					// JOptionPane.showMessageDialog(null, "测试运行失败！请检查代码！", "提示消息",
					// JOptionPane.WARNING_MESSAGE);
					// e1.printStackTrace();
					// }
					// return null;
					// }
					//
					// }.execute();

					// EventQueue.invokeLater(new Runnable() {
					// public void run() {
					// // 运行
					// // 例如e://class//,name:test
					// try {
					// // cu.runClassFile(path, codeSetParams.getClassName());
					// cu.runClassFile2(path, codeSetParams.getClassName(), showArea);
					//
					// } catch (Throwable e1) {
					// JOptionPane.showMessageDialog(null, "测试运行失败！请检查代码！", "提示消息",
					// JOptionPane.WARNING_MESSAGE);
					// e1.printStackTrace();
					// }
					//
					// }
					// });

					// // 生成对象
					// Object obj = null;
					// try {
					// // obj = Class.forName("customFunction\\bin\\test3").newInstance();
					// obj = Class.forName(codeSetParams.getClassName()).newInstance();
					// Class<? extends Object> cls = obj.getClass();
					// // 调用main方法
					// Method m = cls.getMethod("main", String[].class);
					// m.invoke(obj, (Object) new String[] {});
					// } catch (Exception e1) {
					// JOptionPane.showMessageDialog(null, "测试运行失败！请检查代码！", "提示消息",
					// JOptionPane.WARNING_MESSAGE);
					// e1.printStackTrace();
					// }
				} else {
					JOptionPane.showMessageDialog(null, "保存编译失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
				}
			}
		}

		// 监听保存
		if (key.equals(saveButton)) {
			if (!isBasicSet) {
				JOptionPane.showMessageDialog(null, "请先配置基本信息！", "提示消息", JOptionPane.WARNING_MESSAGE);
			} else {
				// 先获取配置信息
				try {
					codeSetParams = cu.getCodeSetProperties();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "保存编译失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
				}
				// 编译
				// 获取代码
				codeString = programmeArea.getText().trim();
				if (checkCode(codeString)) {
					String classDir = "customFunction\\bin\\" + codeSetParams.getClassName() + "\\src\\";
					// 创建文件夹
					File ff = new File(classDir);
					if (!ff.exists()) {
						cu.createFileDir(ff);
					}
					String classCacheFile = classDir + codeSetParams.getClassName() + ".java";
					// 创建缓存java文件
					try {
						cu.setCodeToFile(classCacheFile, codeString);
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					File f = new File(classCacheFile);
					String path = f.getAbsolutePath();

					// 动态编译
					// 此处无法加载如果没有安装jdk
					// 解决方法一，通过手动加载tools.jar获取相关类方法获取编译器再编译
					// 编译方法一
					// JavaCompiler compiler = null;
					// try {
					// compiler = cu.getJavaCompilerByLocation();
					// } catch (Exception e1) {
					// e1.printStackTrace();
					// }
					//
					// // 必须安装jdk或jre查看源码发现是通过java.home环境变量获取的
					// // JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
					// int flag = compiler.run(null, null, null, path);
					// if (flag == 0) {
					// // 编译成功
					// isCompiledSucc = true;
					// saveButton.setEnabled(false);
					// testButton.setEnabled(true);
					// addAppButton.setEnabled(true);
					// } else {
					// JOptionPane.showMessageDialog(null, "保存编译失败！请检查代码！", "提示消息",
					// JOptionPane.WARNING_MESSAGE);
					// isCompiledSucc = false;
					// saveButton.setEnabled(true);
					// testButton.setEnabled(false);
					// addAppButton.setEnabled(false);
					// }

					// 编译二
					File f1 = new File(classDir);
					String filePath = f1.getAbsolutePath();
					File f2 = new File("customFunction\\bin\\" + codeSetParams.getClassName() + "\\lib\\");
					String jarPath;
					if (!f2.exists()) {
						jarPath = "";
					} else {
						jarPath = f2.getAbsolutePath();
					}
					//获取jar配置文件中jar包路径,拼接好
					String[] buildPathJar=null;
					String buildJars="";
					try {
						buildPathJar=cu.getBuildPathJar(new File("customFunction\\bin\\" + codeSetParams.getClassName() + "\\resource\\" + codeSetParams.getClassName() + "config.properties"));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					if(buildPathJar!=null) {
						buildJars=cu.getStringJarByArray(buildPathJar);
					}
					
					File f3 = new File("customFunction\\bin\\" + codeSetParams.getClassName() + "\\bin\\");
					if (!f3.exists()) {
						cu.createFileDir(f3);
					}
					String targetDir = f3.getAbsolutePath();
					try {
						DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
						boolean compilerResult = caru.compiler("UTF-8", caru.getJarFiles(jarPath)+buildJars, filePath, filePath,
								targetDir, diagnostics);
						if (compilerResult) {
							// 编译成功
							isCompiledSucc = true;
							saveButton.setEnabled(false);
							testButton.setEnabled(true);
							addAppButton.setEnabled(true);
						} else {
							JOptionPane.showMessageDialog(null, "保存编译失败！请检查代码！", "提示消息", JOptionPane.WARNING_MESSAGE);
							isCompiledSucc = false;
							saveButton.setEnabled(true);
							testButton.setEnabled(false);
							addAppButton.setEnabled(false);
							for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
								System.out.println(diagnostic.getMessage(null));
							}
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}

					// // 编译三
					// // 当前编译器
					// JavaCompiler cmp = ToolProvider.getSystemJavaCompiler();
					// // Java标准文件管理器
					// StandardJavaFileManager fm = cmp.getStandardFileManager(null, null, null);
					// // Java文件对象
					// JavaFileObject jfo = (JavaFileObject) new
					// StringJavaObject(codeSetParams.getClassName(),
					// codeString);
					// // 编译参数，类似于javac <options>中的options
					// List<String> optionsList = new ArrayList<String>();
					// // 编译文件的存放地方，注意：此处是为Eclipse工具特设的
					// // optionsList.addAll(Arrays.asList("-d", "customFunction\\bin"));
					// optionsList.addAll(Arrays.asList("-d", "./bin"));
					// // 要编译的单元
					// List<JavaFileObject> jfos = Arrays.asList(jfo);
					// // 设置编译环境
					// JavaCompiler.CompilationTask task = cmp.getTask(null, fm, null, optionsList,
					// null, jfos);
					// // 编译成功
					// if (task.call()) {
					// isCompiledSucc = true;
					// saveButton.setEnabled(false);
					// testButton.setEnabled(true);
					// addAppButton.setEnabled(true);
					// } else {
					// JOptionPane.showMessageDialog(null, "保存编译失败！请检查代码！", "提示消息",
					// JOptionPane.WARNING_MESSAGE);
					// isCompiledSucc = false;
					// saveButton.setEnabled(true);
					// testButton.setEnabled(false);
					// addAppButton.setEnabled(false);
					// }
				} else {
					JOptionPane.showMessageDialog(null, "代码为空或格式不正确！请检查！", "提示消息", JOptionPane.WARNING_MESSAGE);
				}
			}

		}

		// 监听添加应用
		if (key.equals(addAppButton)) {
			if (isCompiledSucc && !saveButton.isEnabled()) {
				// 判断类名是否已经添加应用，添加了则修改状态为修改
				// 重来，如已添加则不能再次添加
				if (status != 1) {
					if (cu.classNameIsInFile(codeSetParams.getClassName())) {
						JOptionPane.showMessageDialog(null, "此类应用已存在！一个类只能添加一个应用！", "提示消息",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
				}

				if (status == 1) {
					// 弹窗设置
					AddAppSet aas = new AddAppSet(status);
					if (!"".equals(appName)) {
						jButton.setText(appName);
						// 修改文件
						try {
							cu.updateAppInfotParams(buttonName, appName, true, codeSetParams.getClassName(), "0");
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(null, "保存修改失败！", "提示消息", JOptionPane.WARNING_MESSAGE);
							e1.printStackTrace();
						}
					}
					JOptionPane.showMessageDialog(null, "保存修改成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
					CustomApplicationJPanel.isOpenCodeJFrame = false;
					dispose();
					return;
				} else if (status == 0) {
					// 弹窗设置
					AddAppSet aas = new AddAppSet(status);
					if (!"".equals(appName)) {

						// 遍历之前的按钮，检查是否可见，选择最近一个不可见按钮开始配置
						int count = 0;
						for (JButton jb : jbList) {
							count++;
							if (!jb.isVisible()) {
								// 将应用名和.class文件路径一起写入文件
								int index = jbList.indexOf(jb);
								try {
									if (cu.updateAppInfotParams("button" + (index + 1), appName, true,
											codeSetParams.getClassName(), "0")) {
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
					}
				}
			} else {
				JOptionPane.showMessageDialog(null, "请保存或编译！", "提示消息", JOptionPane.WARNING_MESSAGE);
			}
		}

	}

	// 检查代码格式
	private boolean checkCode(String codeString) {
		if ("".equals(codeString)) {
			return false;
		}
		return true;
	}

	// 监听菜单栏
	class Listen extends JFrame implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			// 监听导入代码文件
			if (source.equals(importItem1)) {
				if (!isBasicSet) {
					JOptionPane.showMessageDialog(null, "请先配置基本信息！", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				// 打开文件选择器
				chooseFile1 = new JFileChooser();
				// 文件过滤
				// 只能选择文件
				chooseFile1.setFileSelectionMode(0);
				// chooseFile1.addChoosableFileFilter(new FileCanChoose());
				chooseFile1.setFileFilter(new FileCanChoose());
				int returnVal = chooseFile1.showOpenDialog(jf);
				if (returnVal == chooseFile1.APPROVE_OPTION) {
					File f = chooseFile1.getSelectedFile();
					String fname = f.getAbsolutePath();
					// 将文件读取并呈现在文本域
					if (cu.setFileCodeInToJTP(fname, programmeArea)) {

					} else {
						JOptionPane.showMessageDialog(null, "读取失败！请稍后重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
					}

				}

			}
			// 监听导出文件
			if (source.equals(exportItem)) {
				if (!isBasicSet) {
					JOptionPane.showMessageDialog(null, "请先配置基本信息！", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				// 获取文本域代码
				String code = programmeArea.getText().trim();
				// 获取类名
				try {
					codeSetParams = cu.getCodeSetProperties();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "配置文件丢失！", "提示消息", JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
					return;
				}
				String className2 = codeSetParams.getClassName();
				if (!"".equals(programmeArea)) {
					// 先打开文件选择器
					chooseFile2 = new JFileChooser();
					// 默认文件名称还有放在当前目录下
					chooseFile2.setSelectedFile(new File(className2 + ".java"));
					// chooseFile2.setFileSelectionMode(1);// 设定只能选择到文件夹
					int state = chooseFile2.showDialog(jf, "另存为");// 此句是打开文件选择器界面的触发语句
					if (state == 1) {
						return;
					} else {
						File f = chooseFile2.getSelectedFile();// f为选择到的目录
						String filePath = f.getAbsolutePath() + className2 + ".java";
						try {
							if (cu.setCodeToFile(filePath, code)) {
								JOptionPane.showMessageDialog(null, "导出成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(null, "导出失败！请稍后重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
							}
						} catch (HeadlessException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "代码为空！请先编写代码！", "帮助", JOptionPane.PLAIN_MESSAGE);
				}
			}

			// 监听buildpathjar
			if (source.equals(buildPathJar)) {
				if (!isBasicSet) {
					JOptionPane.showMessageDialog(null, "请先配置基本信息！", "提示消息", JOptionPane.WARNING_MESSAGE);
				} else {
					// 获取类名
					try {
						codeSetParams = cu.getCodeSetProperties();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "配置文件丢失！", "提示消息", JOptionPane.WARNING_MESSAGE);
						e1.printStackTrace();
						return;
					}
					String className2 = codeSetParams.getClassName();
					//弹出buildpath弹窗，同时读取配置列表（jar路径名称等等）
					try {
						BuildPathJarDialog bpd=new BuildPathJarDialog(className2);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					
					
				}
			}
			// 监听帮助
			if (source.equals(helpItem)) {
				String message = "1.请编写完整代码，包括导入import，但不要带package。\n 2.目前仅支持一个类，请编写在一个.java中。\n 3.请保持配置的类名与public修饰的类一致。\n 4.第三方Jar包在src同级目录lib下。\n 5.资源包可以是文件或文件夹，在src同级目录下。";
				JOptionPane.showMessageDialog(this, message, "帮助", JOptionPane.PLAIN_MESSAGE);
			}

		}

	}

}

// 文本中的Java对象
// class StringJavaObject extends SimpleJavaFileObject {
// // 源代码
// private String content = "";
//
// // 遵循Java规范的类名及文件
// public StringJavaObject(String _javaFileName, String _content) {
// super(_createStringJavaObjectUri(_javaFileName), Kind.SOURCE);
// content = _content;
// }
//
// // 产生一个URL资源路径
// private static URI _createStringJavaObjectUri(String name) {
// // 注意此处没有设置包名
// return URI.create("String:///" + name + Kind.SOURCE.extension);
// }
//
// // 文本文件代码
// @Override
// public CharSequence getCharContent(boolean ignoreEncodingErrors) throws
// IOException {
// return content;
// }
// }

// 文件过滤器
class FileCanChoose extends FileFilter {
	public boolean accept(File file) {
		String name = file.getName();
		return (name.toLowerCase().endsWith(".java") || name.toLowerCase().endsWith(".txt") || file.isDirectory());
	}

	public String getDescription() {
		return "文件：*.java、 *.txt";
	}
}

