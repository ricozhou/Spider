package publicGUI.gameJPanel.snake;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.ImageIcon;

//处理数据存储类
public class KeepOnFile {
	// 简单保存到txt文件
	public void keepScoreOnFile(int score, int maxScore) {
		FileOutputStream fos = null;
		// 分数够大才存储
		if (score > maxScore) {
			try {
				fos = new FileOutputStream("gameFile/MaxSnakeScore.txt");
				PrintStream ps = new PrintStream(fos);
				ps.print(score);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 读取保存的分数
	public int getTxtScore() {
		FileInputStream fis = null;
		int is = 0;
		try {
			fis = new FileInputStream("gameFile/MaxSnakeScore.txt");
			byte[] b = new byte[100];
			int has = fis.read(b);
			String stringScore = new String(b, 0, has);
			is = Integer.valueOf(stringScore);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return is;
	}
}
