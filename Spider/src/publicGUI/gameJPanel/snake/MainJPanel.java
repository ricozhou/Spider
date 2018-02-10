package publicGUI.gameJPanel.snake;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

//主界面面板
public class MainJPanel extends JPanel implements KeyListener, ActionListener, Runnable {
	// 按钮,开始，暂停，退出，继续
	public JButton startButton, stopButton, restartButton;
	// 方向，键盘上北下南左西右东0123
	public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;
	// 食物
	public Food food;
	// 蛇
	public Snake snake;
	// 按下按键时记录时间
	public long upTime = 0, downTime = 0, leftTime = 0, rightTime = 0;
	// 相当于速度(阻塞时间)
	public int time;
	// 自己面板传递过来
	public JFrame jf;

	public MainJPanel(Snake snake, Food food, JFrame jf) {
		this.food = food;
		this.snake = snake;
		this.jf = jf;
		// 初始化面板
		initJPanel();
	}

	// 初始化按钮
	private void initJPanel() {
		// 绝对布局
		this.setLayout(null);
		startButton = new JButton("开始");
		startButton.setBounds(14, 8, 80, 30);
		stopButton = new JButton("暂停");
		stopButton.setBounds(104, 8, 55, 30);
		restartButton = new JButton("继续");
		restartButton.setBounds(169, 8, 55, 30);
		this.add(startButton);
		this.add(stopButton);
		this.add(restartButton);
		startButton.addActionListener(this);
		stopButton.addActionListener(this);
		stopButton.setEnabled(false);
		restartButton.addActionListener(this);
		restartButton.setEnabled(false);
		// 键盘监听
		this.addKeyListener(this);
		// 初始化阻塞时间
		time = getTime(MainGUI.gameLevel);
	}

	@Override
	public void paint(Graphics g) {
		// 没有会将button刷掉
		super.paint(g);
		// 绘制页面
		g.setColor(Color.GRAY);
		g.fillRect(15, 45, MainGUI.GRID_ROW * MainGUI.GRID_WIDTH, MainGUI.GRID_ROW * MainGUI.GRID_WIDTH);
		// 绘制边界
		g.setColor(Color.black);
		g.drawRect(15, 45, MainGUI.GRID_ROW * MainGUI.GRID_WIDTH, MainGUI.GRID_ROW * MainGUI.GRID_WIDTH);
		// 绘制保存的最大分数
		g.setFont(new Font("Tahoma", Font.PLAIN, 20));
		g.drawString("Max:" + MainGUI.maxScore, 300, 30);
		// 绘制分数
		g.setFont(new Font("Tahoma", Font.PLAIN, 20));
		g.drawString("Score:" + MainGUI.gameScore, 407, 30);
		// 绘制等级
		g.setFont(new Font("Tahoma", Font.PLAIN, 20));
		g.drawString("Level:" + MainGUI.gameLevel, 515, 30);
		// 画蛇
		snake.drawSnake(g);
		// 画食物
		if (Snake.isEatFood) {
			// 不是新建一个food，而是更新food的坐标（都是以列行为单位）
			food.nextFood();
		}
		food.drawFood(g);
	}

	// 监听键盘操作
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		// 空格键操作开始,暂停,继续
		if (key == KeyEvent.VK_SPACE) {
			if (startButton.isEnabled()) {
				startButtonEvent();
			} else if (stopButton.isEnabled()) {
				stopButtonEvent();
			} else if (restartButton.isEnabled()) {
				restartButtonEvent();
			}
		}

		// 只有进行中键盘上下左右才有效
		if (MainGUI.gameFlag != 1)
			return;
		switch (key) {
		// 反方向时不能操作
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
		case KeyEvent.VK_NUMPAD8:

			if (snake.getDirection() != DOWN && snake.getDirection() != UP) {
				upTime = System.currentTimeMillis();
//				if (getTimeInterval(0)) {
					snake.direction = UP;
					snake.SnakeMove();
					snake.drawSnake(getGraphics());
//				}
			}
			// 顺向加速
			if (snake.getDirection() == UP) {
				time = getFastTime();
			}
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
		case KeyEvent.VK_NUMPAD2:
			if (snake.getDirection() != UP && snake.getDirection() != DOWN) {
				downTime = System.currentTimeMillis();
//				if (getTimeInterval(1)) {
					snake.direction = DOWN;
					snake.SnakeMove();
					snake.drawSnake(getGraphics());
//				}
			}
			if (snake.getDirection() == DOWN) {
				time = getFastTime();
			}
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
		case KeyEvent.VK_NUMPAD4:
			if (snake.getDirection() != RIGHT && snake.getDirection() != LEFT) {
				leftTime = System.currentTimeMillis();
//				if (getTimeInterval(2)) {
					snake.direction = LEFT;
					snake.SnakeMove();
					snake.drawSnake(getGraphics());
//				}
			}
			if (snake.getDirection() == LEFT) {
				time = getFastTime();
			}
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
		case KeyEvent.VK_NUMPAD6:
			// 当方向相反和相同时不能改变方向
			if (snake.getDirection() != LEFT && snake.getDirection() != RIGHT) {
				rightTime = System.currentTimeMillis();
//				if (getTimeInterval(3)) {
					snake.direction = RIGHT;
					snake.SnakeMove();
					snake.drawSnake(getGraphics());
//				}
			}
			if (snake.getDirection() == RIGHT) {
				time = getFastTime();
			}
			break;
		}
	}

//	// 处理按键间隔时间不能太小
//	public boolean getTimeInterval(int key) {
//		if (key == 3) {
//			if ((rightTime - downTime) <= time || (rightTime - upTime) <= time) {
//				return false;
//			}
//		} else if (key == 2) {
//			if ((leftTime - downTime) <= time || (leftTime - upTime) <= time) {
//				return false;
//			}
//		} else if (key == 1) {
//			if ((downTime - rightTime) <= time || (downTime - leftTime) <= time) {
//				return false;
//			}
//		} else if (key == 0) {
//			if ((upTime - rightTime) <= time || (upTime - leftTime) <= time) {
//				return false;
//			}
//		}
//		return true;
//	}

	// 松开按键恢复速度
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
		case KeyEvent.VK_NUMPAD8:
			if (snake.getDirection() == UP) {
				// 恢复速度
				time = getTime(MainGUI.gameLevel);
			}
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
		case KeyEvent.VK_NUMPAD2:
			if (snake.getDirection() == DOWN) {
				time = getTime(MainGUI.gameLevel);
			}
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
		case KeyEvent.VK_NUMPAD4:
			if (snake.getDirection() == LEFT) {
				time = getTime(MainGUI.gameLevel);
			}
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
		case KeyEvent.VK_NUMPAD6:
			if (snake.getDirection() == RIGHT) {
				time = getTime(MainGUI.gameLevel);
			}
			break;
		}
	}

	// 监听按钮
	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听开始
		if (e.getSource().equals(startButton)) {
			startButtonEvent();
		}
		// 监听暂停
		if (e.getSource().equals(stopButton)) {
			stopButtonEvent();
		}
		// 监听继续
		if (e.getSource().equals(restartButton)) {
			restartButtonEvent();
		}
		// 不加这个监听键盘监听器失效
		// 需要把焦点请求到面板上
		this.requestFocus();
	}

	// 开始按钮的监听操作
	public void startButtonEvent() {
		MainGUI.threadFlag = 0;
		MainGUI.gameFlag = 1;
		if (startButton.getText().equals("重新开始")) {
			// 释放面板
			jf.dispose();
			// 重新new对象
			// 注：这个方式重新开始不好，不应该直接新建一个窗口，而是应该重画，初始化jpanel，但为啥不起作用呢？
			new MainGUI(jf.getLocation().x, jf.getLocation().y);
		}
		startButton.setEnabled(false);
		stopButton.setEnabled(true);
		restartButton.setEnabled(false);
	}

	// 停止按钮的监听操作
	public void stopButtonEvent() {
		MainGUI.threadFlag = 0;
		MainGUI.gameFlag = 2;
		startButton.setEnabled(false);
		stopButton.setEnabled(false);
		restartButton.setEnabled(true);
	}

	// 继续按钮的监听操作
	public void restartButtonEvent() {
		MainGUI.threadFlag = 0;
		MainGUI.gameFlag = 1;
		startButton.setEnabled(false);
		stopButton.setEnabled(true);
		restartButton.setEnabled(false);
	}

	@Override
	public void run() {
		// 线程标志为0时一直重画
		while (MainGUI.threadFlag == 0) {
			// 重画
			repaint();
			try {
				Thread.sleep(time);
				// 游戏标志为进行中
				if (MainGUI.gameFlag == 1) {
					// 移动
					snake.SnakeMove();
				}
				if (MainGUI.gameFlag == 3) {
					// 结束
					startButton.setEnabled(true);
					startButton.setText("重新开始");
					stopButton.setEnabled(false);
					restartButton.setEnabled(false);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// 处理时间,正常速度
	public int getTime(int level) {
		int time = 340;
		if (level < 6) {
			time = time - level * 40;
		} else if (level < 11) {
			time = time - 5 * 40 - (level - 5) * 15;
		} else if (level < 16) {
			time = time - 5 * 40 - 5 * 15 - (level - 10) * 5;
		} else if (level < 21) {
			time = time - 5 * 40 - 5 * 15 - 5 * 5 - (level - 15) * 2;
		} else {
			time = 25;
		}
		return time;
	}

	// 按住则改变速度,加速速度
	public int getFastTime() {
		double time = Double.valueOf(getTime(MainGUI.gameLevel));
		time = 0.6 * time;
		return (int) time;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

}
