package login.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GetProperties {
	// 读取配置文件
	public static Properties pro = new Properties();

	// 读取文件
	public Map<String, String> getOneUser() throws IOException {
		FileInputStream fis;
		BufferedReader bf = null;
		try {
			fis = new FileInputStream(new File("configFile//user.properties"));
			bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
			pro.load(bf);
			Map<String, String> map = new HashMap<String, String>();
			String user;
			String password;
			user = pro.getProperty("commonUser1");
			password = pro.getProperty("commonPwd1");
			map.put(user, password);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bf.close();
		}
		return null;
	}
}
