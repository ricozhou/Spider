package taobaojud.prosceniumgui.entity;

public class Params {
	//省份
	public String province;
	//地级市
	public String city;
	//存储文件路径
	public String filePath;
	//标的物类型
	public String subjectType;
	//标的物拍卖状态
	public String subjectStatus;
	//是否全部爬取
	public String YNGetAll;
	//是否详细信息
	public String YNGetDetail;
	
	
	public String getYNGetDetail() {
		return YNGetDetail;
	}
	public void setYNGetDetail(String yNGetDetail) {
		YNGetDetail = yNGetDetail;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public String getSubjectStatus() {
		return subjectStatus;
	}
	public void setSubjectStatus(String subjectStatus) {
		this.subjectStatus = subjectStatus;
	}
	public String getYNGetAll() {
		return YNGetAll;
	}
	public void setYNGetAll(String yNGetAll) {
		YNGetAll = yNGetAll;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	@Override
	public String toString() {
		return "Params [province=" + province + ", city=" + city + ", filePath=" + filePath + ", subjectType="
				+ subjectType + ", subjectStatus=" + subjectStatus + ", YNGetAll=" + YNGetAll + ", YNGetDetail="
				+ YNGetDetail + "]";
	}
	
	
}
