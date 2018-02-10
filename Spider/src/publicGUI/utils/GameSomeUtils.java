package publicGUI.utils;

import java.awt.Dimension;
import java.awt.Toolkit;

public class GameSomeUtils {
	// 获取屏幕尺寸，包括任务栏
	public int[] getScreenSize() {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screensize.getWidth();
		int height = (int) screensize.getHeight();
		int[] xy = { width, height };
		return xy;
	}

}
