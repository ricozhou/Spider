package publicGUI.gameJPanel.snake;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

//主类,窗口
public class MainGUI extends JFrame {
	// 食物
	public Food food;
	// 蛇
	public Snake snake;
	// 游戏标志，0准备中，1正在进行，2暂停,3游戏结束,初始化为0
	public static int gameFlag = 0;
	// 方格的边长
	public static final int GRID_WIDTH = 15;
	// 方格的行数
	public static final int GRID_ROW = 40;
	// 方格的列数
	public static final int GRID_COL = 40;
	// 窗口尺寸
	public static int jframeX = GRID_WIDTH * GRID_ROW + 40;
	public static int jframeY = GRID_WIDTH * GRID_COL + 100;
	// 得分,初始化为0
	public static int gameScore = 0;
	// 保存的最大分数
	public static int maxScore = 0;
	// 等级
	public static int gameLevel = 1;
	// 线程状态标志，0开始，1结束，初始化为1
	public static int threadFlag = 0;
	// 主面板
	public MainJPanel jpanel;
	// 线程
	public Thread thread;
	// 窗口坐标
	public int x, y;

	// 构造方法
	public MainGUI(int x, int y) {
		this.x = x;
		this.y = y;
		// 初始化对象
		init();
		// 初始化frame
		initJFrame();
	}

	// 初始化游戏
	private void init() {
		// 游戏状态为准备
		MainGUI.gameFlag = 0;
		// 线程为开始
		MainGUI.threadFlag = 0;
		// 得分为0
		MainGUI.gameScore = 0;
		// 等级
		MainGUI.gameLevel = 1;

		// 最大分数
		maxScore = new KeepOnFile().getTxtScore();

		// 初始化食物
		food = new Food();
		// 初始化蛇
		snake = new Snake(food);
		jpanel = new MainJPanel(snake, food, this);
		// 启动线程
		thread = new Thread(jpanel);
		thread.start();
	}

	private void initJFrame() {
		this.add(jpanel);
		// 设置窗体相关属性
		// 不可变窗口
		this.setResizable(false);
		// 标题
		this.setTitle("贪吃蛇");
		// 背景颜色
		this.setBackground(Color.WHITE);
		// 可见
		this.setVisible(true);
		// 大小
		this.setSize(jframeX, jframeY);
		// 坐标
		this.setLocation(x, y);
		// 图标
		// ImageIcon imageIcon = new ImageIcon(getClass().getResource("/snake.jpg"));
		ImageIcon imageIcon = new ImageIcon("gameResource/Snake/snake.jpg");
		// 设置标题栏的图标为face.gif
		this.setIconImage(imageIcon.getImage());
		// 监听界面关闭事件
		this.addWindowListener(new WindowAdapter() {
			// 当关闭时
			public void windowClosing(WindowEvent e) {
				MainGUI.threadFlag = 1;
				thread.stop();
			}
		});
		// 设置关闭种类,关闭虚拟机
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
