package publicGUI.gameJPanel;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import login.entity.UserInfo;
import publicGUI.entity.PassParameter;
import publicGUI.entity.SetParams;
import publicGUI.gameJPanel.BinaryRain.BinaryRain;
import publicGUI.gameJPanel.flappybird.FlappyBirdGameUI;
import publicGUI.gameJPanel.snake.MainGUI;
import publicGUI.toolJPanel.StandardCalculator;
import publicGUI.utils.GameSomeUtils;
import publicGUI.utils.GetSetProperties;

public class GameJPanel extends JPanel implements ActionListener {
	public JButton button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11,
			button12;
	public SetParams setParams;
	public GetSetProperties gsp = new GetSetProperties();
	public GameSomeUtils gs = new GameSomeUtils();
	public UserInfo userInfo;
	public JFrame jf;
	public PassParameter passParameter;
	public static int[] screenSize;

	public GameJPanel(PassParameter passParameter, JFrame jf) throws IOException {
		// 将之前窗口对象传递过来
		this.jf = jf;
		this.passParameter = passParameter;
		init();
	}

	private void init() {
		//获取屏幕尺寸
		screenSize=gs.getScreenSize();
		userInfo = passParameter.getUserInfo();
		button1 = new JButton("像素鸟");
		button1.setBounds(30, 30, 120, 35);
		button1.addActionListener(this);
		button2 = new JButton("五子棋");
		button2.setBounds(30, 65, 120, 35);
		button2.addActionListener(this);
		button3 = new JButton("贪吃蛇");
		button3.setBounds(30, 100, 120, 35);
		button3.addActionListener(this);

		button4 = new JButton("俄罗斯方块");
		button4.setBounds(180, 30, 120, 35);
		button4.addActionListener(this);
		button5 = new JButton("智能小拼图");
		button5.setBounds(180, 65, 120, 35);
		button5.addActionListener(this);
		button6 = new JButton("飞机大作战");
		button6.setBounds(180, 100, 120, 35);
		button6.addActionListener(this);

		button7 = new JButton("2048");
		button7.setBounds(30, 170, 120, 35);
		button7.addActionListener(this);
		button8 = new JButton("代码雨");
		button8.setBounds(30, 205, 120, 35);
		button8.addActionListener(this);
		button9 = new JButton("发红包");
		button9.setBounds(30, 240, 120, 35);
		button9.addActionListener(this);

		button10 = new JButton("占位符万岁");
		button10.setBounds(330, 30, 120, 35);
		button10.addActionListener(this);
		this.add(button1);
		this.add(button2);
		this.add(button3);
		this.add(button4);
		this.add(button5);
		this.add(button6);
		this.add(button7);
		this.add(button8);
		this.add(button9);
		this.add(button10);
		this.setLayout(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 像素鸟
		if (e.getSource().equals(button1)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						// 启动像素鸟小游戏
						new FlappyBirdGameUI((screenSize[0] - 400) / 2, (screenSize[1] - 600) / 2);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		// 代码雨
		if (e.getSource().equals(button8)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						// 启动代码雨
						// 创建对象
						BinaryRain r = new BinaryRain();
						r.setVisible(true);
						// 启动线程
						r.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		// 贪吃蛇
		if (e.getSource().equals(button3)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						// 启动贪吃蛇
						// 创建对象
						new MainGUI((screenSize[0]-640)/2,(screenSize[1]-700)/2);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

}
