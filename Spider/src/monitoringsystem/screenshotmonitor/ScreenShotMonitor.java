package monitoringsystem.screenshotmonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import publicGUI.utils.SaveSet;

//监控类
public class ScreenShotMonitor {
	public void manageMonitor(boolean yn) {
		String lnk = "mailmonitorone.exe";
		// 写入或者删除
		new SaveSet().setAutoStart(yn, lnk);
		if (yn) {
			//执行
			startMonitor(lnk);
		}
	}

	public void startMonitor(String lnk) {
		//执行项目里的文件
		final Runtime runtime = Runtime.getRuntime();
		File file = new File("mailmonitorone.exe");
		String path = file.getAbsolutePath();
		Process process = null;
		final String command = "rundll32 url.dll FileProtocolHandler file://" + path + "";
		try {
			process = runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
