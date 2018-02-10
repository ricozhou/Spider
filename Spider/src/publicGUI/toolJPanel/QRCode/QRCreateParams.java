package publicGUI.toolJPanel.QRCode;

public class QRCreateParams {
	// 保存路径
	public String filePath;
	// 是否插入图片
	public boolean insertImage;
	// 插入图片路径
	public String InsertImagePath;
	// 信息，链接或者文本
	public String linkOrText;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isInsertImage() {
		return insertImage;
	}

	public void setInsertImage(boolean insertImage) {
		this.insertImage = insertImage;
	}

	public String getInsertImagePath() {
		return InsertImagePath;
	}

	public void setInsertImagePath(String insertImagePath) {
		InsertImagePath = insertImagePath;
	}

	public String getLinkOrText() {
		return linkOrText;
	}

	public void setLinkOrText(String linkOrText) {
		this.linkOrText = linkOrText;
	}

}
