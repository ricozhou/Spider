package login.entity;

public class CommSetParams {
	// 用户名
	public String userName;
	// 服务器IP
	public String serverIP;
	// 服务器端口号
	public String serverPort;
	// 消息列表
	public String messageTxt;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public String getMessageTxt() {
		return messageTxt;
	}

	public void setMessageTxt(String messageTxt) {
		this.messageTxt = messageTxt;
	}

	@Override
	public String toString() {
		return "CommSetParams [userName=" + userName + ", serverIP=" + serverIP + ", serverPort=" + serverPort
				+ ", messageTxt=" + messageTxt + "]";
	}

}
