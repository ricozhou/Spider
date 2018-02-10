package landmarketnetwork.background.entity;

public class ReturnJSLandMessage {
	// 用时
	public String date;
	// 是否成功
	public String YNsuccess;
	// 提示信息
	public String YNMessage;

	// 文件信息
	public JSLandFileMessage jslfm;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getYNsuccess() {
		return YNsuccess;
	}

	public void setYNsuccess(String yNsuccess) {
		YNsuccess = yNsuccess;
	}

	public String getYNMessage() {
		return YNMessage;
	}

	public void setYNMessage(String yNMessage) {
		YNMessage = yNMessage;
	}

	public JSLandFileMessage getJslfm() {
		return jslfm;
	}

	public void setJslfm(JSLandFileMessage jslfm) {
		this.jslfm = jslfm;
	}

	@Override
	public String toString() {
		return "ReturnJSLandMessage [date=" + date + ", YNsuccess=" + YNsuccess + ", YNMessage=" + YNMessage
				+ ", jslfm=" + jslfm + "]";
	}

}
