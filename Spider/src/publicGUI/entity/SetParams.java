package publicGUI.entity;

public class SetParams {
	// 是否选择开机自启动
	public boolean autoStart;
	// 是否打开消息通知
	public boolean openMessage;
	// 是否记住文件夹路径
	public boolean rememberFilePath;
	// 文件夹路径
	public String filePath;
	// 是否启动自检查更新
	public boolean autoUpdate;
	// 是否允许多个客户端
	public boolean allowMultiClient;
	// 是否点击X号最小化系统托盘
	public boolean minimizeTray;
	// 界面风格
	public int interfaceStyle;
	//是否监控
	public boolean monitor;

	public boolean isMonitor() {
		return monitor;
	}

	public void setMonitor(boolean monitor) {
		this.monitor = monitor;
	}

	public boolean isAllowMultiClient() {
		return allowMultiClient;
	}

	public void setAllowMultiClient(boolean allowMultiClient) {
		this.allowMultiClient = allowMultiClient;
	}

	public boolean isMinimizeTray() {
		return minimizeTray;
	}

	public void setMinimizeTray(boolean minimizeTray) {
		this.minimizeTray = minimizeTray;
	}

	public int getInterfaceStyle() {
		return interfaceStyle;
	}

	public void setInterfaceStyle(int interfaceStyle) {
		this.interfaceStyle = interfaceStyle;
	}

	public boolean isAutoStart() {
		return autoStart;
	}

	public void setAutoStart(boolean autoStart) {
		this.autoStart = autoStart;
	}

	public boolean isOpenMessage() {
		return openMessage;
	}

	public void setOpenMessage(boolean openMessage) {
		this.openMessage = openMessage;
	}

	public boolean isRememberFilePath() {
		return rememberFilePath;
	}

	public void setRememberFilePath(boolean rememberFilePath) {
		this.rememberFilePath = rememberFilePath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isAutoUpdate() {
		return autoUpdate;
	}

	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	@Override
	public String toString() {
		return "SetParams [autoStart=" + autoStart + ", openMessage=" + openMessage + ", rememberFilePath="
				+ rememberFilePath + ", filePath=" + filePath + ", autoUpdate=" + autoUpdate + ", allowMultiClient="
				+ allowMultiClient + ", minimizeTray=" + minimizeTray + ", interfaceStyle=" + interfaceStyle
				+ ", monitor=" + monitor + "]";
	}

}
