package publicGUI.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import login.entity.CommSetParams;
import publicGUI.entity.SetParams;

public class GetSetProperties {
	// 读取配置文件
	public static Properties pro = new Properties();
	public static Properties pro2 = new Properties();

	// 读取文件
	public SetParams getSetMessage() throws IOException {
		FileInputStream fis = new FileInputStream(new File("configFile//set.properties"));
		BufferedReader bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
		pro.load(bf);
		SetParams setParams = new SetParams();
		if (pro.getProperty("autoStart").equals("0")) {
			setParams.setAutoStart(false);
		} else {
			setParams.setAutoStart(true);
		}
		if (pro.getProperty("openMessage").equals("0")) {
			setParams.setOpenMessage(false);
		} else {
			setParams.setOpenMessage(true);
		}
		if (pro.getProperty("rememberFilePath").equals("0")) {
			setParams.setRememberFilePath(false);
		} else {
			setParams.setRememberFilePath(true);
		}
		if (pro.getProperty("autoUpdate").equals("0")) {
			setParams.setAutoUpdate(false);
		} else {
			setParams.setAutoUpdate(true);
		}
		if (pro.getProperty("monitor").equals("0")) {
			setParams.setMonitor(false);
		} else {
			setParams.setMonitor(true);
		}
		setParams.setFilePath(pro.getProperty("filePath"));
		if (pro.getProperty("allowMultiClient").equals("0")) {
			setParams.setAllowMultiClient(false);
		} else {
			setParams.setAllowMultiClient(true);
		}
		if (pro.getProperty("minimizeTray").equals("0")) {
			setParams.setMinimizeTray(false);
		} else {
			setParams.setMinimizeTray(true);
		}
		setParams.setInterfaceStyle(Integer.parseInt(pro.getProperty("interfaceStyle")));
		return setParams;
	}

	// 读取通讯设置
	public CommSetParams getCommSetProperties() throws IOException {
		FileInputStream fis = new FileInputStream(new File("configFile//commset.properties"));
		BufferedReader bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
		pro2.load(bf);
		CommSetParams commSetParams = new CommSetParams();
		commSetParams.setUserName(pro2.getProperty("userName"));
		commSetParams.setServerIP(pro2.getProperty("serverIP"));
		commSetParams.setServerPort(pro2.getProperty("serverPort"));
		commSetParams.setMessageTxt(pro2.getProperty("messageTxt"));
		return commSetParams;
	}
}
