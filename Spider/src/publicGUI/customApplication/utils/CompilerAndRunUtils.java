package publicGUI.customApplication.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;

import publicGUI.customApplication.ConsoleSimulator;

import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

public class CompilerAndRunUtils {
	// 三种编译，三种运行
	private String jars = "";
	private String targetDir = "";
	public String jarCmd = "";
	public CodeUtils cu = new CodeUtils();

	// 编码格式，jar包，需要编译的目录，关联查找目录，编译后存放目录，错误信息
	public boolean compiler(String encoding, String jars, String filePath, String sourceDir, String targetDir,
			DiagnosticCollector<JavaFileObject> diagnostics) throws Exception {
		// 获取编译器实例
		// JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		// 此处无法加载如果没有安装jdk
		// 解决方法一，通过手动加载tools.jar获取相关类方法获取编译器再编译
		// 解决方法二，通过Runtime方法执行cmd命令设置Javahome
		JavaCompiler compiler = null;
		try {
			compiler = getJavaCompilerByLocation();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 获取标准文件管理器实例
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		try {
			if (!isnull(filePath) && !isnull(sourceDir) && !isnull(targetDir)) {
				return false;
			}
			// 得到filePath目录下的所有java源文件
			File sourceFile = new File(filePath);
			List<File> sourceFileList = new ArrayList<File>();
			this.targetDir = targetDir;
			// 根据文件判断是否是java文件,并存放List
			getSourceFiles(sourceFile, sourceFileList);
			// 没有java文件，直接返回
			if (sourceFileList.size() == 0) {
				return false;
			}
			// 获取要编译的编译单元
			Iterable<? extends JavaFileObject> compilationUnits = fileManager
					.getJavaFileObjectsFromFiles(sourceFileList);
			// 编译选项，在编译java文件时，编译程序会自动的去寻找java文件引用的其他的java源文件或者class。
			// -sourcepath选项就是定义java源文件的查找目录， -classpath选项就是定义class文件的查找目录。
			Iterable<String> options = Arrays.asList("-encoding", encoding, "-classpath", jars, "-d", targetDir,
					"-sourcepath", sourceDir);
			CompilationTask compilationTask = compiler.getTask(null, fileManager, diagnostics, options, null,
					compilationUnits);
			// 运行编译任务
			return compilationTask.call();
		} finally {
			fileManager.close();
		}
	}

	// 获取非系统编译器
	public JavaCompiler getJavaCompilerByLocation() throws Exception {
		File f1 = new File("libs\\tools.jar");
		String p = f1.getAbsolutePath();
		URL[] urls = new URL[] { new URL("file:/" + p) };
		URLClassLoader myClassLoader1 = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
		Class<?> myClass1 = myClassLoader1.loadClass("com.sun.tools.javac.api.JavacTool");
		JavaCompiler compiler = myClass1.asSubclass(JavaCompiler.class).asSubclass(JavaCompiler.class).newInstance();
		return compiler;
	}

	// 判断不为空
	public boolean isnull(String str) {
		if (null == str) {
			return false;
		} else if ("".equals(str)) {
			return false;
		} else if (str.equals("null")) {
			return false;
		} else {
			return true;
		}
	}

	// 递归查找java文件
	private void getSourceFiles(File sourceFile, List<File> sourceFileList) throws Exception {
		if (sourceFile.exists() && sourceFileList != null) {
			if (sourceFile.isDirectory()) {
				// 得到该目录下以.java结尾的文件或者目录
				File[] childrenFiles = sourceFile.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						if (pathname.isDirectory()) {
							return true;
						} else {
							String name = pathname.getName();
							if (name.endsWith(".java") ? true : false) {
								return true;
							}
							return false;
						}
					}
				});
				// 递归调用
				for (File childFile : childrenFiles) {
					getSourceFiles(childFile, sourceFileList);
				}
			} else {// 若file对象为文件
				sourceFileList.add(sourceFile);
			}
		}
	}

	// 查找jar
	public String getJarFiles(String jarPath) throws Exception {
		File sourceFile = new File(jarPath);
		if (sourceFile.exists()) {
			if (sourceFile.isDirectory()) {
				// 得到该目录下以.jar结尾的文件或者目录
				File[] childrenFiles = sourceFile.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						if (pathname.isDirectory()) {
							return true;
						} else {
							String name = pathname.getName();
							if (name.endsWith(".jar") ? true : false) {
								jars = jars + pathname.getPath() + ";";
								return true;
							}
							return false;
						}
					}
				});
			}
		}
		return jars;
	}

	// 另起进程启动class
	public void startClassPro(String className, JTextArea showArea) {
		File f = new File("customFunction\\bin\\placeholder");
		String fPath = f.getAbsolutePath();
		String path = fPath.substring(0, fPath.length() - 12) + "\\" + className + "\\bin";
		// 获取jar包所有名称,以便于拼接命令
		// 获取jar配置文件中jar包路径,拼接好
		String[] buildPathJar = null;
		String buildJars = "";
		try {
			buildPathJar = cu.getBuildPathJar(
					new File("customFunction\\bin\\" + className + "\\resource\\" + className + "config.properties"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (buildPathJar != null) {
			buildJars = cu.getStringJarByArray(buildPathJar);
		}

		File f2 = new File("customFunction\\bin\\" + className + "\\lib\\");
		String jarPath;
		if (!f2.exists()) {
			jarPath = "";
			jarCmd = "";
		} else {
			jarPath = f2.getAbsolutePath();
			try {
				jarCmd = ".;" + getJarFiles(jarPath) + buildJars;
			} catch (Exception e1) {
				jarCmd = "";
				e1.printStackTrace();
			}
		}
		// 通过另起进程执行
		new SwingWorker<String, String>() {

			@Override
			protected String doInBackground() throws Exception {
				try {
					// cu.runClassFile(path, codeSetParams.getClassName());
					showArea.setText("");
					runClassFile3(path, className, showArea, jarCmd);
				} catch (Throwable e1) {
					JOptionPane.showMessageDialog(null, "运行失败！请检查代码！", "提示消息", JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
				}
				return null;
			}

		}.execute();
	}

	// 命令行执行
	protected void runClassFile3(String path, String className, JTextArea jTextArea, String jarCmd2) throws Exception {
		// jre目录使用相对目录
		// class文件使用绝对目录
		// File jreFile = new File("jre");
		// String jrePath = jreFile.getAbsolutePath() + "\\bin\\java";
		String cmd = "jre\\bin\\java " + "-cp " + jarCmd + path + " " + className;
		Process child = Runtime.getRuntime().exec(cmd);
		// 获取程序输入流
		OutputStream os = child.getOutputStream();
		// 正常输出流和异常输出流
		InputStream stdin = child.getInputStream();
		InputStream stderr = child.getErrorStream();
		// 启动线程
		Thread tIn = new Thread(new ConsoleSimulator(stdin, 0, jTextArea));
		Thread tErr = new Thread(new ConsoleSimulator(stderr, 1, jTextArea));
		tIn.start();
		tErr.start();
		int result = child.waitFor();
		tIn.join();
		tErr.join();
	}

	// 执行Jar
	public void startJarPro(String jarName, String appType) {
		File f2 = new File("customFunction\\bin\\" + jarName + "\\ecut\\" + jarName + "." + appType);
		if (!f2.exists()) {
			return;
		}
		String jarPath = f2.getAbsolutePath();
		// 通过另起进程执行
		new SwingWorker<String, String>() {

			@Override
			protected String doInBackground() throws Exception {
				try {
					if ("jar".equals(appType)) {
						runClassFile4(jarPath);
					} else if ("exe".equals(appType)) {
						runClassFile5(jarPath);
					}
				} catch (Throwable e1) {
					JOptionPane.showMessageDialog(null, "运行失败！请检查" + appType + "！", "提示消息",
							JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
				}
				return null;
			}

		}.execute();
	}

	// 执行exe
	protected void runClassFile5(String jarPath) {
		// 执行项目里的文件
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		final String command = "rundll32 url.dll FileProtocolHandler file://" + jarPath + "";
		try {
			process = runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 执行Jar
	protected void runClassFile4(String jarPath) throws IOException {
		String cmd = "jre\\bin\\java " + "-jar " + jarPath;
		Process child = Runtime.getRuntime().exec(cmd);
	}

}
