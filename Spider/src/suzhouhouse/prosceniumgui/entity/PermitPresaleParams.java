package suzhouhouse.prosceniumgui.entity;

public class PermitPresaleParams {
	// 项目区域
	public String projectArea;
	// 填写页数
	public String pageNumber;
	// 存储文件路径
	public String filePath;
	public String getProjectArea() {
		return projectArea;
	}
	public void setProjectArea(String projectArea) {
		this.projectArea = projectArea;
	}
	public String getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	@Override
	public String toString() {
		return "PermitPresaleParams [projectArea=" + projectArea + ", pageNumber=" + pageNumber + ", filePath="
				+ filePath + "]";
	}
	
}
