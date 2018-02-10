package publicGUI.gameJPanel.snake;

import java.awt.Color;
import java.awt.Graphics;

public class SnakeNode {
	// 每个蛇身节点的坐标
	public int snakeNodeX;
	public int snakeNodeY;

	// 构造方法
	public SnakeNode() {

	}

	public SnakeNode(int snakeNodeX, int snakeNodeY) {
		this.snakeNodeX = snakeNodeX;
		this.snakeNodeY = snakeNodeY;
	}

	public int getSnakeNodeX() {
		return snakeNodeX;
	}

	public void setSnakeNodeX(int snakeNodeX) {
		this.snakeNodeX = snakeNodeX;
	}

	public int getSnakeNodeY() {
		return snakeNodeY;
	}

	public void setSnakeNodeY(int snakeNodeY) {
		this.snakeNodeY = snakeNodeY;
	}

}
