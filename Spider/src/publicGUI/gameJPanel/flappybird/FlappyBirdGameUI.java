package publicGUI.gameJPanel.flappybird;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

//主界面
public class FlappyBirdGameUI implements MouseListener, MouseMotionListener, KeyListener {
	// 游戏界面组成 gamePanel : 游戏画面的容器，也是画板，主要绘制游戏界面 bird : 游戏中的主角，即小鸟 pipe : 游戏中的障碍物，即管道
	// score : 游戏得分
	private MyPanel gamePanel;
	private Bird bird;
	private Pipe[] pipe;
	private static int score;
	Thread gp;
	JFrame jf = new JFrame();
	// 管道夹缝宽度
	public static final int w = 100;
	// 两个管道之间的宽度
	public static final int d = 250;
	// 游戏状态标志 0 ： 游戏未开始 1 ： 游戏进行中 2： 游戏结束
	public static int flag = 0;
	// 中止线程标志
	public static int stopThread = 0;
	// 游戏开始时间
	public static long start = 0;

	// 声音播放器(一个专门播放声音的线程)
	private PlaySounds player;
	// 游戏状态
	private GameStatus gs;
	// 管道位置
	private int x = 400;
	private int[] ypoints = { new Random().nextInt(322 - FlappyBirdGameUI.w) + FlappyBirdGameUI.w + 10,
			new Random().nextInt(322 - FlappyBirdGameUI.w) + FlappyBirdGameUI.w + 10,
			new Random().nextInt(322 - FlappyBirdGameUI.w) + FlappyBirdGameUI.w + 10,
			new Random().nextInt(322 - FlappyBirdGameUI.w) + FlappyBirdGameUI.w + 10 };

	public FlappyBirdGameUI(int x,int y) {
		FlappyBirdGameUI.flag = 0;

		FlappyBirdGameUI.stopThread=0;
		// 初始化游戏得分
		FlappyBirdGameUI.score = 0;
		// 初始化游戏状态
		gs = new GameStatus();
		// 初始化游戏开始时间
		FlappyBirdGameUI.start = System.currentTimeMillis();
		// 初始化声音播放器
		player = new PlaySounds();
		// 初始化小鸟
		bird = new Bird(80, 260, 10, 5, 60);
		// 初始化管道
		pipe = new Pipe[4];
		for (int i = 0; i < ypoints.length; i++) {
			pipe[i] = new Pipe(x + i * FlappyBirdGameUI.d, ypoints[i], 7);
		}
		// 游戏画面
		gamePanel = new MyPanel(bird, pipe, gs, player);
		// 添加鼠标监听
		gamePanel.addMouseListener(this);
		gamePanel.addMouseMotionListener(this);
		jf.addKeyListener(this);
		// 启动线程
		bird.start();
		player.start();
		for (int i = 0; i < pipe.length; i++) {
			pipe[i].start();
		}
		gp = new Thread(gamePanel);
		gp.start();
		// 设置窗体相关属性
//		jf.setSize(400, 600);
		jf.setBounds(x, y, 400, 600);
//		jf.setLocationRelativeTo(null);
		jf.add(gamePanel);
		jf.setResizable(false);
		jf.setTitle("flappy bird");
		jf.setVisible(true);
		ImageIcon imageIcon = new ImageIcon("gameResource/flappyBirdRes/title.png");
		// 设置标题栏的图标为face.gif
		jf.setIconImage(imageIcon.getImage());
		// 监听界面关闭事件
		jf.addWindowListener(new WindowAdapter() {
			// 当关闭时
			public void windowClosing(WindowEvent e) {
				stopThread=1;
				bird.stop();
				player.stop();
				gp.stop();
			}
		});
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	// 获取游戏得分
	public static int getScore() {
		return FlappyBirdGameUI.score;
	}

	// 加分
	public static void addScore() {
		if (FlappyBirdGameUI.flag == 1) {
			FlappyBirdGameUI.score++;
		}
	}

	// 改变游戏状态
	public void setStatus(int status) {
		FlappyBirdGameUI.flag = status;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		mouseAndKeyEventListener(x, y);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		mouseAndKeyEventListener(-1, -1);
	}

	public void mouseAndKeyEventListener(int x, int y) {
		// 将小鸟的状态设置为飞行状态
		bird.setFlyStatus();
		// 当游戏结束后，根据点击鼠标的位置 来判断是不是点击了重新开始按钮
		if (FlappyBirdGameUI.flag == 2) {
			if ((x == -1 && y == -1) || (x > 27 && x < 183 && y > 390 && y < 483)) {
				jf.dispose();
				FlappyBirdGameUI.flag = 0;
				FlappyBirdGameUI.score = 0;
				FlappyBirdGameUI.start = System.currentTimeMillis();
				new FlappyBirdGameUI(jf.getX(),jf.getY());
			}
			return;
		}

//		//启动线程
//		FlappyBirdGameUI.stopThread=0;
		// 将游戏状态设置为游戏进行中
		FlappyBirdGameUI.flag = 1;
		// 将声音播放器设置为播放游戏进行中相关的音乐
		player.setIsPlay(1);
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
