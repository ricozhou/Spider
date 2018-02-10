package publicGUI.customApplication.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.tools.JavaCompiler;

import login.entity.CommSetParams;
import publicGUI.customApplication.ConsoleSimulator;
import publicGUI.customApplication.entity.CodeSetParams;

public class CodeUtils {
	public static Properties pro2 = new Properties();
	public static Properties pro = new Properties();

	// 保存设置
	public boolean saveCodeSetParams(CodeSetParams codeSetParams) throws IOException {
		Properties pro = new Properties();
		File file = new File("configFile//codeset.properties");
		FileInputStream fis = null;
		BufferedReader bf = null;
		FileOutputStream fos = null;
		OutputStreamWriter obw = null;
		try {
			fis = new FileInputStream(file);
			bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
			pro.load(bf);
			bf.close();
			pro.setProperty("codeLanguage", String.valueOf(codeSetParams.getCodeLanguage()));
			pro.setProperty("className", codeSetParams.getClassName());
			fos = new FileOutputStream(file);
			obw = new OutputStreamWriter(fos, "utf-8");
			pro.store(obw, null);
			obw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				fos.close();
			}
			if (null != bf) {
				bf.close();
			}
		}
		return false;
	}

	// 获取配置信息
	public CodeSetParams getCodeSetProperties() throws IOException {
		FileInputStream fis = new FileInputStream(new File("configFile//codeset.properties"));
		BufferedReader bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
		pro2.load(bf);
		CodeSetParams codeSetParams = new CodeSetParams();
		codeSetParams.setCodeLanguage(Integer.parseInt(pro2.getProperty("codeLanguage")));
		codeSetParams.setClassName(pro2.getProperty("className"));
		return codeSetParams;
	}

	// 获取配置信息
	public List<String[]> getAppInfoProperties() throws IOException {
		List<String[]> strList = new ArrayList<String[]>();
		FileInputStream fis = new FileInputStream(new File("customFunction//customapp//customappset.properties"));
		BufferedReader bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
		pro.load(bf);
		for (int i = 0; i < 14; i++) {
			strList.add(pro.getProperty("button" + (i + 1)).split("#"));
		}
		return strList;
	}

	public String[] getAppInfoProperties(String buttonName) throws IOException {
		FileInputStream fis = new FileInputStream(new File("customFunction//customapp//customappset.properties"));
		BufferedReader bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
		pro.load(bf);
		String[] classInfo = pro.getProperty(buttonName).split("#");
		return classInfo;
	}

	// 修改配置
	public boolean updateAppInfotParams(String button, String appName, boolean tf, String className, String appType)
			throws IOException {
		Properties pro = new Properties();
		File file = new File("customFunction//customapp//customappset.properties");
		FileInputStream fis = null;
		BufferedReader bf = null;
		FileOutputStream fos = null;
		OutputStreamWriter obw = null;
		try {
			fis = new FileInputStream(file);
			bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
			pro.load(bf);
			bf.close();
			pro.setProperty(button,
					button + "#" + appName + "#" + String.valueOf(tf) + "#" + className + "#" + appType);
			fos = new FileOutputStream(file);
			obw = new OutputStreamWriter(fos, "utf-8");
			pro.store(obw, null);
			obw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				fos.close();
			}
			if (null != bf) {
				bf.close();
			}
		}
		return false;
	}

	// 判断类名是否在配置文件中存在
	public boolean classNameIsInFile(String className) {
		// 先从配置文件获取所有已添加应用的类名
		List<String[]> setList = null;
		try {
			setList = getAppInfoProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 取出类名并存入集合
		List<String> javaList = new ArrayList<String>();
		for (String[] str : setList) {
			javaList.add(str[3]);
		}
		// 获取缓存文件夹所有的文件夹
		if (javaList.contains(className)) {
			return true;
		}
		return false;

	}

	// 对比删除多余缓存的java类
	public void compareJavaFile() throws IOException {
		// 先从配置文件获取所有已添加应用的类名
		List<String[]> setList = getAppInfoProperties();
		// 取出类名并存入集合
		List<String> javaList = new ArrayList<String>();
		for (String[] str : setList) {
			javaList.add(str[3] + ".java");
			javaList.add(str[3] + ".class");
		}
		// 获取缓存文件夹所有的文件
		String path = "customFunction\\bin";
		File f = new File(path);
		if (!f.exists()) {
			return;
		}
		File[] fs = f.listFiles();
		// 遍历对比
		File fo;
		for (int i = 0; i < fs.length; i++) {
			fo = fs[i];
			if (fo.isFile()) {
				if (fo.getName().endsWith(".java") || fo.getName().endsWith(".class")) {
					if (!javaList.contains(fo.getName())) {
						fo.delete();
					}
				}
			}

		}
	}

	// 对比删除多余缓存的java类
	// 删除文件夹
	public void compareJavaFile2() throws IOException {
		// 先从配置文件获取所有已添加应用的类名
		// 类名与文件夹名相同
		List<String[]> setList = getAppInfoProperties();
		// 取出类名并存入集合,包括jar名
		List<String> javaList = new ArrayList<String>();
		for (String[] str : setList) {
			javaList.add(str[3]);
		}
		// 获取缓存文件夹所有的文件夹
		String path = "customFunction\\bin\\";
		File f = new File(path);
		if (!f.exists()) {
			return;
		}
		File[] fs = f.listFiles();
		// 遍历对比
		File fo;
		for (int i = 0; i < fs.length; i++) {
			fo = fs[i];
			if (fo.isDirectory()) {
				if (!javaList.contains(fo.getName())) {
					// 递归删除全部
					deleteAllFile(new File(path + fo.getName()));
				}
			}

		}
	}

	// 递归删除目录下所有文件
	public boolean deleteAllFile(File file) {
		String[] files = file.list();
		if (files != null && files.length > 0) {
			for (String f : files) {
				boolean success = deleteAllFile(new File(file, f));
				if (!success) {
					return false;
				}
			}
		}
		return file.delete();
	}

	// 类名命名规则判断
	public boolean formatClassName(String className) {
		String classPat = "[a-zA-Z]+[0-9a-zA-Z_$]*";
		if ("".equals(className)) {
			return false;
		}
		Pattern p = Pattern.compile(classPat);
		Matcher m = p.matcher(className);
		if (!m.find()) {
			return false;
		}
		String[] javaKeyWords = { "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
				"const", "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
				"for", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new",
				"package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch",
				"synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while", "byValue",
				"cast", "false", "future", "generic", "inner", "operator", "outer", "rest", "true", "var", "goto",
				"const", "null" };
		for (int i = 0; i < javaKeyWords.length; i++) {
			if (javaKeyWords[i].equals(className)) {
				return false;
			}
		}
		return true;
	}

	// 判断是否与缓存文件夹重复
	public boolean compareJavaFromCache(String className) {
		// 获取缓存文件夹所有的文件夹
		String path = "customFunction\\bin\\";
		File f = new File(path);
		if (!f.exists()) {
			return false;
		}
		File[] fs = f.listFiles();
		// 遍历对比
		File fo;
		for (int i = 0; i < fs.length; i++) {
			fo = fs[i];
			if (fo.isDirectory()) {
				if (fo.getName().equals(className)) {
					return false;
				}
			}
		}
		return true;
	}

	// 将文件内容读取呈现在文本域
	public boolean setFileCodeInToJTP(String fname, JTextPane programmeArea) {
		StringBuilder result = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fname), "utf-8"));
			String s = null;
			// 整行读取
			while ((s = br.readLine()) != null) {
				result.append(System.lineSeparator() + s);
			}
			programmeArea.setText("");
			programmeArea.setText(result.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 获取代码
	public String getFileCodeInTo(String fname) {
		StringBuilder result = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fname), "utf-8"));
			String s = null;
			// 整行读取
			while ((s = br.readLine()) != null) {
				result.append(System.lineSeparator() + s);
			}
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 导出文件
	public boolean setCodeToFile(String filePath, String code) throws IOException {
		File f = new File(filePath);
		if (!f.exists()) {
			f.createNewFile();
		}
		FileOutputStream fos;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream(new File(filePath));
			osw = new OutputStreamWriter(fos, "UTF-8");
			osw.write(code);
			osw.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				osw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// 运行java文件/class
	public void runClassFile(String path, String className) throws Exception {
		URL[] urls = new URL[] { new URL("file:/" + path) };
		URLClassLoader loader = new URLClassLoader(urls);
		// 通过反射调用此类
		Class clazz = null;
		clazz = loader.loadClass(className);
		Method m = clazz.getMethod("main", String[].class);
		m.invoke(null, (Object) new String[] {});
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

	// 筛选出显示的按钮
	public String[] getAppDeleteInfo() throws Exception {
		List<String> strList = new ArrayList<String>();
		FileInputStream fis = new FileInputStream(new File("customFunction//customapp//customappset.properties"));
		BufferedReader bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
		pro.load(bf);
		String[] cacheStr2;
		String cacheStr1;
		for (int i = 0; i < 14; i++) {
			cacheStr1 = pro.getProperty("button" + (i + 1));
			cacheStr2 = cacheStr1.split("#");
			if (Boolean.parseBoolean(cacheStr2[2])) {
				// 按钮名，按钮名，应用名，类名,应用类型
				strList.add(cacheStr2[0] + "#" + cacheStr2[0] + "-" + cacheStr2[1] + "-" + cacheStr2[3] + "#"
						+ cacheStr2[4]);
			}
		}
		String[] deleteInfo = new String[strList.size()];
		for (int i = 0; i < strList.size(); i++) {
			deleteInfo[i] = strList.get(i);
		}
		return deleteInfo;
	}

	// 根据类名删除文件
	public boolean deleteFileByClassName(String className) {
		String path = "customFunction\\bin\\" + className;
		if (deleteAllFile(new File(path))) {
			return true;
		}
		// File f = new File(path + "\\" + className + ".java");
		// File f2 = new File(path + "\\" + className + ".class");
		// if (!f.exists() || !f2.exists()) {
		// return false;
		// }
		// if (f.delete() && f2.delete()) {
		// return true;
		// }
		return false;
	}

	// 通过cmd另起进程运行class文件
	public void runClassFile2(String path, String className, JTextArea showArea, String jarCmd) throws Exception {
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
		Thread tIn = new Thread(new ConsoleSimulator(stdin, 0, showArea));
		Thread tErr = new Thread(new ConsoleSimulator(stderr, 1, showArea));
		tIn.start();
		tErr.start();
		int result = child.waitFor();
		tIn.join();
		tErr.join();
	}

	// 创建文件夹
	public void createFileDir(File file) {
		if (!file.exists()) {
			file.mkdirs();
		} else {

		}
	}

	// 导入jar包
	public boolean copyJarToClassDir(String tarDir, File[] fs) throws Exception {
		File f = new File(tarDir);
		createFileDir(f);
		if (fs != null && fs.length > 0) {
			for (int i = 0; i < fs.length; i++) {
				if (fs[i].isFile()) {
					// 复制文件
					copyFile(fs[i], new File(tarDir + fs[i].getName()));
				}
			}
			return true;
		} else {
			return true;
		}
	}

	// 复制文件
	public void copyFile(File sourceFile, File targetFile) throws IOException {
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();

		// 关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}

	// 绝对路径转相对项目路径
	public String getRelativePath(String absolutePath) {
		String relativePath = absolutePath;
		// 获取项目绝对路径
		File f = new File("jre");
		String proPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().length() - 3);
		// 如果jar在项目中,则转换为相对路径,否则不变
		if (absolutePath.startsWith(proPath)) {
			relativePath = absolutePath.substring(absolutePath.indexOf(proPath) + proPath.length(),
					absolutePath.length());
		}
		return relativePath;
	}

	// 读取配置
	public String[] getBuildPathJar(File file) throws Exception {
		Properties pro = new Properties();
		if (!file.exists()) {
			return null;
		} else {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
			pro.load(bf);
			String[] buildpathjar = pro.getProperty("buildpathjar").split("#");
			fis.close();
			bf.close();
			return buildpathjar;
		}
	}

	// 更新配置
	public boolean updateBuildPathJarConfig(String configFilePath, String jarPaths) throws IOException {
		File f = new File(configFilePath);
		File ff = f.getParentFile();
		if (!ff.exists()) {
			ff.mkdirs();
		}
		if (!f.exists()) {
			f.createNewFile();
		}
		Properties pro = new Properties();
		FileInputStream fis = null;
		BufferedReader bf = null;
		FileOutputStream fos = null;
		OutputStreamWriter obw = null;
		try {
			fis = new FileInputStream(f);
			bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
			pro.load(bf);
			bf.close();
			pro.setProperty("buildpathjar", jarPaths);
			fos = new FileOutputStream(f);
			obw = new OutputStreamWriter(fos, "utf-8");
			pro.store(obw, null);
			obw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				fos.close();
			}
			if (null != bf) {
				bf.close();
			}
		}
		return false;
	}

	// 处理字符串
	public String proJarToString(String[] buildPathJar, String[] relativePath) {
		String jarString = "";
		if (buildPathJar == null && relativePath == null) {
			return jarString;
		} else {
			if (buildPathJar != null) {
				for (int i = 0; i < buildPathJar.length; i++) {
					jarString = jarString + buildPathJar[i] + "#";
				}
			}
			if (relativePath != null) {
				for (int i = 0; i < relativePath.length; i++) {
					jarString = jarString + relativePath[i] + "#";
				}
			}
		}
		return jarString;
	}

	// 显示拼接
	public String proJarToString(DefaultListModel jarData, String[] relativePath) {
		String jarString = "";
		if (jarData.size() > 0) {
			for (int i = 0; i < jarData.getSize(); i++) {
				jarString = jarString + jarData.getElementAt(i) + "#";
			}
		}
		if (relativePath != null) {
			for (int i = 0; i < relativePath.length; i++) {
				jarString = jarString + relativePath[i] + "#";
			}
		}
		return jarString;
	}

	// 数组转字符串,绝对路径
	public String getStringJarByArray(String[] buildPathJar) {
		String buildJars = "";
		if (buildPathJar != null && buildPathJar.length > 0) {
			for (int i = 0; i < buildPathJar.length; i++) {
				File f = new File(buildPathJar[i]);
				if (f.exists() && f.isFile()) {
					String name = f.getName();
					if (name.endsWith(".jar")) {
						buildJars = buildJars + f.getAbsolutePath() + ";";
					}
				}
			}
		}
		return buildJars;
	}

}
