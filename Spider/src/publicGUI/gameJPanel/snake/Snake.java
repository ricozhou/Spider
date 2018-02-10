package publicGUI.gameJPanel.snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

//蛇类
public class Snake extends Thread {
	// 头坐标
	// 本坐标都是以列（x）行（y）为单位处理,以保证蛇和食物在一个行列中
	public int tx;
	public int ty;
	// 食物
	public Food food;
	// 是否被吃
	public static boolean isEatFood;
	// 处理数据存储
	public KeepOnFile kof = new KeepOnFile();
	// 方向,上北下南左西右东0123
	public int direction;
	// 0123上北下南左西右东，第一个是x变化格数，第二个是y变化格数
	public static final int DIR[][] = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };
	// 节点集合就是蛇
	private List<SnakeNode> snake = new ArrayList<SnakeNode>();

	// 构造方法
	public Snake(Food food) {
		this.food = food;
		// 初始只有一个,方向向右
		direction = 3;
		// 初始化三列五行
		snake.add(new SnakeNode(3, 5));
	}

	// 蛇长度，集合大小
	public int getSnakeLength() {
		return snake.size();
	}

	// 获取方向
	public int getDirection() {
		return direction;
	}

	// 设置方向
	public void setDirection(int direction) {
		this.direction = direction;
	}

	// 重要方法，蛇头移动
	public SnakeNode headMove() {
		// 头部方块运动一次，上北下南坐标变化一个格子,头部坐标
		tx = snake.get(0).getSnakeNodeX() + DIR[direction][0];
		ty = snake.get(0).getSnakeNodeY() + DIR[direction][1];
		// 蛇头移动后的新坐标
		SnakeNode node = new SnakeNode(tx, ty);
		return node;
	}

	// 移动(重点方法！！！！！！！！！如何移动)
	public void SnakeMove() {

		// 获取移动后的蛇头
		SnakeNode head = headMove();
		// 先把原集合的最后一个节点拿出来，等吃了之后在加在最后
		SnakeNode sn = snake.get(snake.size() - 1);
		// 依次往后挪，给蛇头留位置
		for (int i = snake.size() - 1; i > 0; i--) {
			snake.set(i, snake.get(i - 1));
		}
		// 设置第一个为蛇头
		snake.set(0, head);
		// 如果死了
		if (checkIsDead()) {
			MainGUI.threadFlag = 1;
			MainGUI.gameFlag = 3;
			// 保存
			kof.keepScoreOnFile(MainGUI.gameScore, MainGUI.maxScore);
			// 提示信息
			JOptionPane.showMessageDialog(null, "You were dead!", "Message", JOptionPane.ERROR_MESSAGE);
		}
		// 如果吃到食物
		if (eatFood(food)) {
			// 下一个，添加到第一个
			head = headMove();
			// 将原集合最后一个添加到现集合最后一个
			snake.add(sn);
		}
	}

	// 画点
	public void drawSnake(Graphics g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < snake.size(); i++) {
			// 画圆填充(x,y,width,height)
			g.fillOval(snake.get(i).getSnakeNodeX() * MainGUI.GRID_WIDTH,
					snake.get(i).getSnakeNodeY() * MainGUI.GRID_WIDTH, MainGUI.GRID_WIDTH, MainGUI.GRID_WIDTH);
		}
	}

	// 检查状态
	public boolean checkIsDead() {
		// 碰到墙壁死(以行数为单位)
		if (tx > 40) {
			return true;
		}
		if (tx < 1) {
			return true;
		}
		if (ty > 42) {
			return true;
		}
		if (ty < 3) {
			return true;
		}

		// 碰到自己也是死
		// 遍历所有，头碰到任何部位都是死
		for (int i = 1; i < snake.size(); i++) {
			if (snake.get(0).getSnakeNodeX() == snake.get(i).getSnakeNodeX()
					&& snake.get(0).getSnakeNodeY() == snake.get(i).getSnakeNodeY()) {
				return true;
			}
		}
		return false;
	}

	// 是否吃到食物
	// 此方法有问题，导致吃到食物的时候有所延迟
	public boolean eatFood(Food food) {
		// if(direction==0){
		// if ((tx == (food.getFoodX()+1)) && ty == food.getFoodY())
		// {
		// isEatFood = true;
		// return true;
		// }
		// }else if(direction==1){
		// if ((tx == (food.getFoodX()-1)) && ty == food.getFoodY())
		// {
		// isEatFood = true;
		// return true;
		// }
		// }else if(direction==2){
		// if ((tx == food.getFoodX()) && ty == (food.getFoodY()+1))
		// {
		// isEatFood = true;
		// return true;
		// }
		// }else if(direction==3){
		// if ((tx == (food.getFoodX()-1)) && ty == (food.getFoodY()-1))
		// {
		// isEatFood = true;
		// return true;
		// }
		// }
		if ((tx == food.getFoodX()) && ty == food.getFoodY()) {
			// 吃一个五分
			MainGUI.gameScore += 5;
			if (MainGUI.gameScore >= MainGUI.gameLevel * 20) {
				// 20分一个等级
				MainGUI.gameLevel += 1;
			}
			isEatFood = true;
			return true;
		}
		isEatFood = false;
		return false;
	}
}
