package suzhouhouse.background.entity;

public class ReturnHouseMessage {
	// 用时
	public String date;
	// 是否成功
	public String YNsuccess;
	// 提示信息
	public String YNMessage;

	// 文件信息
	public HouseFileMessage hfm;

	public HouseFileMessage getHfm() {
		return hfm;
	}

	public void setHfm(HouseFileMessage hfm) {
		this.hfm = hfm;
	}

	public String getYNMessage() {
		return YNMessage;
	}

	public void setYNMessage(String yNMessage) {
		YNMessage = yNMessage;
	}

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

	@Override
	public String toString() {
		return "ReturnHouseMessage [date=" + date + ", YNsuccess=" + YNsuccess + ", YNMessage=" + YNMessage + ", hfm="
				+ hfm + "]";
	}

}
