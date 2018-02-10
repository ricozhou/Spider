package publicGUI.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JButton;

import login.entity.CommSetParams;
import login.entity.UserInfo;
import net.jimmc.jshortcut.JShellLink;
import publicGUI.entity.SetParams;

public class SaveSet {
	// 保存修改修改用户及密码
	public boolean saveUserInfo(UserInfo userInfo) throws IOException {
		Properties pro = new Properties();
		File file = new File("configFile//user.properties");
		FileInputStream fis = null;
		BufferedReader bf = null;
		FileOutputStream fos = null;
		OutputStreamWriter obw = null;
		try {
			fis = new FileInputStream(file);
			bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
			pro.load(bf);
			bf.close();
			pro.setProperty("commonUser1", userInfo.getUserName());
			pro.setProperty("commonPwd1", userInfo.getPassword());
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

	// 保存其他设置
	public boolean saveSet(SetParams setParams) {
		// 写入快捷方式
		String lnk = "Spider.exe.lnk";
		if (!setAutoStart(setParams.isAutoStart(), lnk)) {
			return false;
		}
		Properties pro = new Properties();
		File file = new File("configFile//set.properties");
		FileInputStream fis = null;
		BufferedReader bf = null;
		FileOutputStream fos = null;
		OutputStreamWriter obw = null;
		try {
			fis = new FileInputStream(file);
			bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
			pro.load(bf);
			bf.close();
			pro.setProperty("autoStart", setParams.isAutoStart() ? "1" : "0");
			pro.setProperty("openMessage", setParams.isOpenMessage() ? "1" : "0");
			pro.setProperty("rememberFilePath", setParams.isRememberFilePath() ? "1" : "0");
			pro.setProperty("autoUpdate", setParams.isAutoUpdate() ? "1" : "0");
			pro.setProperty("allowMultiClient", setParams.isAllowMultiClient() ? "1" : "0");
			pro.setProperty("minimizeTray", setParams.isMinimizeTray() ? "1" : "0");
			pro.setProperty("monitor", setParams.isMonitor() ? "1" : "0");
			pro.setProperty("filePath", setParams.getFilePath());
			pro.setProperty("interfaceStyle", String.valueOf(setParams.getInterfaceStyle()));
			fos = new FileOutputStream(file);
			obw = new OutputStreamWriter(fos, "utf-8");
			pro.store(obw, null);
			obw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != bf) {
				try {
					bf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	// 写入快捷方式
	public boolean setAutoStart(boolean yesAutoStart, String lnk) {
		File f = new File(lnk);
		String p = f.getAbsolutePath();
		String startFolder = "";
		String osName = System.getProperty("os.name");
		String str = System.getProperty("user.home");
		if (osName.equals("Windows 7") || osName.equals("Windows 8") || osName.equals("Windows 10")
				|| osName.equals("Windows Server 2012 R2") || osName.equals("Windows Server 2014 R2")
				|| osName.equals("Windows Server 2016 R2")) {
			startFolder = System.getProperty("user.home")
					+ "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";
		}
		if (osName.endsWith("Windows XP")) {
			startFolder = System.getProperty("user.home") + "\\「开始」菜单\\程序\\启动";
		}
		if (setRunBySys(yesAutoStart, p, startFolder, lnk)) {
			return true;
		}
		return false;
	}

	// 设置是否随系统启动
	public boolean setRunBySys(boolean b, String path, String path2, String lnk) {
		File file = new File(path2 + "\\" + lnk);
		Runtime run = Runtime.getRuntime();
		File f = new File(lnk);

		// 复制
		try {
			if (b) {
				// 判断是否隐藏，注意用系统copy布置为何隐藏文件不生效
				if (f.isHidden()) {
					// 取消隐藏
					try {
						Runtime.getRuntime().exec("attrib -H \"" + path + "\"");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (!file.exists()) {
					run.exec("cmd /c copy " + formatPath(path) + " " + formatPath(path2));
				}
				// 延迟0.5秒防止复制需要时间
				Thread.sleep(500);
				// 再次隐藏监控文件
				if ("mailmonitorone.exe".equals(lnk)) {
					try {
						Runtime.getRuntime().exec("attrib +H \"" + path + "\"");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				if (file.exists()) {
					if (file.isHidden()) {
						// 取消隐藏
						try {
							Runtime.getRuntime().exec("attrib -H \"" + file.getAbsolutePath() + "\"");
						} catch (IOException e) {
							e.printStackTrace();
						}
						Thread.sleep(500);
					}
					run.exec("cmd /c del " + formatPath(file.getAbsolutePath()));
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 解决路径中空格问题
	private String formatPath(String path) {
		if (path == null) {
			return "";
		}
		return path.replaceAll(" ", "\" \"");
	}

	// 复制文件
	// 无操作权限
	public boolean copyFile(String oldPath, String newPath) {
		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				inStream = new FileInputStream(oldPath); // 读入原文件
				fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != inStream) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != fs) {
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// 重启方法
	// 成功
	public boolean reStartPro() {
		// 方法1：先启动另外一个程序，然后退出,在让另外程序启动这个,另外一个退出
		final Runtime runtime = Runtime.getRuntime();
		// 运行路径
		// String parentPath =
		// getClass().getResource("../").getFile().toString().substring(1);
		File file = new File("reStart.exe");
		if (!file.exists()) {
			File file2 = new File("reStart.bat");
			if (file2.exists()) {
				String p = file2.getAbsolutePath();
				try {
					Process process2 = runtime.exec("rundll32 url.dll FileProtocolHandler file://" + p + "");
					System.exit(0);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			} else {
				return false;
			}
		}
		String path = file.getAbsolutePath();
		Process process = null;
		try {
			final String command = "rundll32 url.dll FileProtocolHandler file://" + path + "";
			process = runtime.exec(command);
			System.exit(0);
		} catch (final Exception e1) {
			return false;
		}
		return false;
	}

	// 保存通讯设置
	public boolean saveCommSetParams(CommSetParams commSetParams) throws IOException {
		Properties pro = new Properties();
		File file = new File("configFile//commset.properties");
		FileInputStream fis = null;
		BufferedReader bf = null;
		FileOutputStream fos = null;
		OutputStreamWriter obw = null;
		try {
			fis = new FileInputStream(file);
			bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
			pro.load(bf);
			bf.close();
			pro.setProperty("userName", commSetParams.getUserName());
			pro.setProperty("serverIP", commSetParams.getServerIP());
			pro.setProperty("serverPort", commSetParams.getServerPort());
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

	// 根据路径和名字修改文件名
	public boolean reNameFile(String path, String newPathName) {
		File f1 = new File(path);
		File f2 = new File(newPathName);
		if (f1.renameTo(f2)) {
			return true;
		} else {
			return false;
		}
	}

	// 保存头像
	public boolean saveImage(String fname, BufferedImage bi1) {
		if (bi1 == null) {
			return false;
		}
		if ("".equals(fname)) {
			return false;
		}
		try {
			// 写入
			ImageIO.write(bi1, "png", new File("image//image.png"));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
