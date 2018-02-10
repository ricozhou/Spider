package taobaojud.prosceniumgui.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import taobaojud.prosceniumgui.entity.Params;

public class BasicUtils {
	// 验证前台参数
	public boolean checkParam(Params param) {
		if (param.getFilePath() == "" && param.getFilePath() == null) {
			return false;
		}
		return true;
	}

	// 转化时间
	public String turnDate3(long s, long e) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		String date = df.format(e - s);
		return date;
	}

	// 判断文件是否存在
	public boolean YNFileExist(String lastFilePath) {
		File file = new File(lastFilePath);
		if (!file.exists() || !file.isFile()) {
			return false;
		}
		return true;
	}

	// 打开文件
	public boolean OpenExcelFile(String lastFilePath) {
		File file = new File(lastFilePath);
		try {
			if (file.renameTo(file)) {
				java.awt.Desktop.getDesktop().open(file);
				// Runtime.getRuntime().exec(lastFilePath);
				return true;
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 删除文件
	public boolean DeleteExcelFile(String lastFilePath) {
		try {
			File file = new File(lastFilePath);
			// 以重命名方式判断是否被占用
			if (file.renameTo(file)) {
				file.delete();
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 系统默认浏览器打开网址
	public void openUrlByBrowser(String url) {
		URI uri = null;
		try {
			uri = new URI(url);
			Desktop.getDesktop().browse(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
