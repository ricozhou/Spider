package landmarketnetwork.background.entity;

public class JSLandFileMessage {
	// 是否导入成功
	public boolean bl;

	// 文件地址及名字
	public String lastFilePath;

	public boolean isBl() {
		return bl;
	}

	public void setBl(boolean bl) {
		this.bl = bl;
	}

	public String getLastFilePath() {
		return lastFilePath;
	}

	public void setLastFilePath(String lastFilePath) {
		this.lastFilePath = lastFilePath;
	}

	@Override
	public String toString() {
		return "FileMessage [bl=" + bl + ", lastFilePath=" + lastFilePath + "]";
	}
}
