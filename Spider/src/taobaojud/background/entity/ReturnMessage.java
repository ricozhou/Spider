package taobaojud.background.entity;

public class ReturnMessage {
	//用时
	public String date;
	//是否成功
	public String YNsuccess;
	//提示信息
	public String YNMessage;
	
	//文件信息
	public FileMessage fm;
	
	public FileMessage getFm() {
		return fm;
	}
	public void setFm(FileMessage fm) {
		this.fm = fm;
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
		return "ReturnMessage [date=" + date + ", YNsuccess=" + YNsuccess + ", YNMessage=" + YNMessage + ", fm=" + fm
				+ ", getYNMessage()=" + getYNMessage() + ", getDate()=" + getDate() + ", getYNsuccess()="
				+ getYNsuccess() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	
}
