package suzhouhouse.prosceniumgui.entity;

public class HouseQueryParams {
	//文件夹路径
	public String filePath;
	// 装修情况
	public int fitmentSelect;
	// 项目区域
	public String projectArea;
	// 房屋最低价
	public String houseMinPrice;
	// 房屋最高价
	public String houseMaxPrice;
	// 房屋用途
	public String houseUse;
	// 房屋最低面积
	public String houseMinArea;
	// 房屋最高面积
	public String houseMaxArea;
	public int getFitmentSelect() {
		return fitmentSelect;
	}
	public void setFitmentSelect(int fitmentSelect) {
		this.fitmentSelect = fitmentSelect;
	}
	public String getProjectArea() {
		return projectArea;
	}
	public void setProjectArea(String projectArea) {
		this.projectArea = projectArea;
	}
	public String getHouseUse() {
		return houseUse;
	}
	public void setHouseUse(String houseUse) {
		this.houseUse = houseUse;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getHouseMinPrice() {
		return houseMinPrice;
	}
	public void setHouseMinPrice(String houseMinPrice) {
		this.houseMinPrice = houseMinPrice;
	}
	public String getHouseMaxPrice() {
		return houseMaxPrice;
	}
	public void setHouseMaxPrice(String houseMaxPrice) {
		this.houseMaxPrice = houseMaxPrice;
	}
	public String getHouseMinArea() {
		return houseMinArea;
	}
	public void setHouseMinArea(String houseMinArea) {
		this.houseMinArea = houseMinArea;
	}
	public String getHouseMaxArea() {
		return houseMaxArea;
	}
	public void setHouseMaxArea(String houseMaxArea) {
		this.houseMaxArea = houseMaxArea;
	}
	@Override
	public String toString() {
		return "HouseQueryParams [fitmentSelect=" + fitmentSelect + ", projectArea=" + projectArea + ", houseMinPrice="
				+ houseMinPrice + ", houseMaxPrice=" + houseMaxPrice + ", houseUse=" + houseUse + ", houseMinArea="
				+ houseMinArea + ", houseMaxArea=" + houseMaxArea + "]";
	}
	

}
