package publicGUI.toolJPanel.screenrecording;

public class ScreenRecParams {
	// 输出文件夹
	public String filePath;
	// 设置帧数
	public int frameNum;
	// 是否录制声音
	public boolean recordVoice;
	// 是否无障碍录屏
	public boolean barrierFreeScreen;
	//记录张数名称
	public int screenNum;
	

	public int getScreenNum() {
		return screenNum;
	}

	public void setScreenNum(int screenNum) {
		this.screenNum = screenNum;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getFrameNum() {
		return frameNum;
	}

	public void setFrameNum(int frameNum) {
		this.frameNum = frameNum;
	}

	public boolean isRecordVoice() {
		return recordVoice;
	}

	public void setRecordVoice(boolean recordVoice) {
		this.recordVoice = recordVoice;
	}

	public boolean isBarrierFreeScreen() {
		return barrierFreeScreen;
	}

	public void setBarrierFreeScreen(boolean barrierFreeScreen) {
		this.barrierFreeScreen = barrierFreeScreen;
	}

	@Override
	public String toString() {
		return "ScreenRecParams [filePath=" + filePath + ", frameNum=" + frameNum + ", recordVoice=" + recordVoice
				+ ", barrierFreeScreen=" + barrierFreeScreen + ", screenNum=" + screenNum + "]";
	}

}
