package publicGUI.gameJPanel.flappybird;

import java.awt.Graphics;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.ImageIcon;

public class GameStatus {
	// 游戏开始画面
	private Image ready;
	private Image begin;
	// 游戏分数
	private Image[] score;
	// 游戏结束画面
	private Image gameOver;
	private Image scoreBoard;
	private Image[] endScore;
	private Image restart;
	private Image list;

	// 构造方法
	public GameStatus() {
		ready = new ImageIcon("gameResource/flappyBirdRes/ready.png").getImage();
		begin = new ImageIcon("gameResource/flappyBirdRes/begin.png").getImage();
		score = new Image[4];
		score[0] = new ImageIcon("gameResource/flappyBirdRes/0.png").getImage();
		endScore = new Image[4];
		gameOver = new ImageIcon("gameResource/flappyBirdRes/gameover.png").getImage();
		scoreBoard = new ImageIcon("gameResource/flappyBirdRes/ScoreBoard.png").getImage();
		restart = new ImageIcon("gameResource/flappyBirdRes/Buttons.png").getImage();
		list = new ImageIcon("gameResource/flappyBirdRes/Buttons_02.png").getImage();
	}

	// 设置重新开始图片
	public void setRestart() {
		restart = new ImageIcon("gameResource/flappyBirdRes/Buttons_01.png").getImage();
	}

	// 绘制游戏未开始、进行中、结束的画面
	public void drawSelf(Graphics g) {
		if (FlappyBirdGameUI.flag == 0) {
			// 游戏未开始
			g.drawImage(ready, 63, 80, ready.getWidth(null), ready.getHeight(null), null);
			g.drawImage(begin, 53, 250, begin.getWidth(null), begin.getHeight(null), null);

		} else if (FlappyBirdGameUI.flag == 1) {
			// 游戏进行中
			showStartScore(g);
		} else {
			// 游戏结束
			g.drawImage(gameOver, 198 - gameOver.getWidth(null) / 2, 80, gameOver.getWidth(null),
					gameOver.getHeight(null), null);
			g.drawImage(scoreBoard, 198 - scoreBoard.getWidth(null) / 2, 200, scoreBoard.getWidth(null),
					scoreBoard.getHeight(null), null);
			g.drawImage(restart, 27, 390, restart.getWidth(null), restart.getHeight(null), null);
			g.drawImage(list, 203, 390, list.getWidth(null), list.getHeight(null), null);
			showEndScore(g);
		}
	}

	// 显示游戏进行中的得分
	public void showStartScore(Graphics g) {
		// 将得分数转换为字符数组
		String s = String.valueOf(FlappyBirdGameUI.getScore());
		char[] ss = s.toCharArray();
		int start = 198 - ss.length * 20;
		// 对每个字符进行判断，显示相应的图片
		for (int i = 0; i < ss.length; i++) {
			score[i] = new ImageIcon("gameResource/flappyBirdRes/" + ss[i] + ".png").getImage();
			g.drawImage(score[i], start + i * 40, 30, 40, 60, null);
		}
	}

	// 显示游戏结束后的得分和最高分
	public void showEndScore(Graphics g) {
		// 将成绩与文件中保存的成绩比较，若大于文件中的成绩，才将成绩写入文件
		FileOutputStream fos;
		FileInputStream fis;
		int max = FlappyBirdGameUI.getScore();
		int is = 0;
		try {
			fis = new FileInputStream("gameFile\\MaxScore.txt");
			byte[] b = new byte[100];
			int has = fis.read(b);
			String stringScore = new String(b, 0, has);
			is = Integer.valueOf(stringScore);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (max <= is) {
			scoreBoard = new ImageIcon("gameResource/flappyBirdRes/ScoreBoard.png").getImage();
			max = is;
		} else {
			scoreBoard = new ImageIcon("gameResource/flappyBirdRes/NewScoreBoard.png").getImage();
			try {
				fos = new FileOutputStream("gameFile\\MaxScore.txt");
				PrintStream ps = new PrintStream(fos);
				ps.print(max);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 将得分数转换为字符数组
		String s = String.valueOf(FlappyBirdGameUI.getScore());
		char[] ss = s.toCharArray();

		int start = 198 - ss.length * 12;

		// 对每个字符进行判断，显示相应的图片
		for (int i = 0; i < ss.length; i++) {
			endScore[i] = new ImageIcon("gameResource/flappyBirdRes/mini" + ss[i] + ".png").getImage();
			g.drawImage(endScore[i], start + i * 25, 253, 25, 32, null);
		}

		// 显示最高分（先将最高分转换成字符数组）
		String s1 = String.valueOf(max);
		char[] ss1 = s1.toCharArray();
		int start1 = 198 - ss1.length * 12;
		for (int i = 0; i < ss1.length; i++) {
			endScore[i] = new ImageIcon("gameResource/flappyBirdRes/mini" + ss1[i] + ".png").getImage();
			g.drawImage(endScore[i], start1 + i * 25, 320, 25, 32, null);
		}
	}
}
