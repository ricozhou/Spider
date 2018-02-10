package login.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import login.entity.SystemMap;

public class LoginUtils {
	// 通过实例化占用一个serverSocket端口来判断是否已经启动
	private static ServerSocket serverSocket = null;
	// 占用一个端口号
	private static final int serverPort = 12345;

	// 获取标的物状态
	public Object[] getSystem() {
		List<String> list = SystemMap.model;
		Object[] system = list.toArray();
		return system;
	}

	// 检查是否被占用,被占用说明已经启动则返回
	public boolean checkSocket() {
		try {
			serverSocket = new ServerSocket(serverPort);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
