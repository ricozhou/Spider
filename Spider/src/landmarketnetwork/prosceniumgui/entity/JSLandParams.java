package landmarketnetwork.prosceniumgui.entity;

public class JSLandParams {
	// 文件夹路径
	public String filePath;
	// 网站选择
	public int network;
	// 时间条件最小值
	public String minDate;
	// 时间最大值
	public String maxDate;
	// 价格最小值
	public String minPrice;
	// 价格最大值
	public String maxPrice;
	// 土地面积最小值
	public String minLandArea;
	// 土地面积最大值
	public String maxLandArea;
	// 是否下载附件
	public boolean downlandAttach;
	// 土地用途
	public String landUse;
	// 页数
	public String pageNum;
	// 最小页数
	public String minpageNum;

	public String getMinpageNum() {
		return minpageNum;
	}

	public void setMinpageNum(String minpageNum) {
		this.minpageNum = minpageNum;
	}

	public int getNetwork() {
		return network;
	}

	public void setNetwork(int network) {
		this.network = network;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getMinDate() {
		return minDate;
	}

	public void setMinDate(String minDate) {
		this.minDate = minDate;
	}

	public String getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}

	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getMinLandArea() {
		return minLandArea;
	}

	public void setMinLandArea(String minLandArea) {
		this.minLandArea = minLandArea;
	}

	public String getMaxLandArea() {
		return maxLandArea;
	}

	public void setMaxLandArea(String maxLandArea) {
		this.maxLandArea = maxLandArea;
	}

	public boolean isDownlandAttach() {
		return downlandAttach;
	}

	public void setDownlandAttach(boolean downlandAttach) {
		this.downlandAttach = downlandAttach;
	}

	public String getLandUse() {
		return landUse;
	}

	public void setLandUse(String landUse) {
		this.landUse = landUse;
	}

	@Override
	public String toString() {
		return "JSLandParams [filePath=" + filePath + ", network=" + network + ", minDate=" + minDate + ", maxDate="
				+ maxDate + ", minPrice=" + minPrice + ", maxPrice=" + maxPrice + ", minLandArea=" + minLandArea
				+ ", maxLandArea=" + maxLandArea + ", downlandAttach=" + downlandAttach + ", landUse=" + landUse
				+ ", pageNum=" + pageNum + ", minpageNum=" + minpageNum + "]";
	}

}
