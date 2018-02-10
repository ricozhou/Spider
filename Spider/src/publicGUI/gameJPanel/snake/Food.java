package publicGUI.gameJPanel.snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;
//食物类
public class Food extends Thread {
	// 食物的位置,也是列行坐标
	public int foodX;
	public int foodY;
	// 食物的大小
	public static final int FOOD_WIDTH = MainGUI.GRID_WIDTH;
	// 随机数
	public static Random random = new Random();
	// 食物的颜色
	public Color foodColor = Color.RED;
	// 构造方法
	public Food() {
		// 初始位置
		this(random.nextInt(MainGUI.GRID_ROW) + 1, random.nextInt(MainGUI.GRID_COL) + 3);
	}

	public int getFoodX() {
		return foodX;
	}

	public void setFoodX(int foodX) {
		this.foodX = foodX;
	}

	public int getFoodY() {
		return foodY;
	}

	public void setFoodY(int foodY) {
		this.foodY = foodY;
	}

	public Food(int foodX, int foodY) {
		this.foodX = foodX;
		this.foodY = foodY;
	}

	// 下一个食物出现的位置
	// 非常重要，不需要新建对象，可以更新位置
	public void nextFood() {
		// 边界,列[1,40],行[3,42]
		this.foodX = (random.nextInt(MainGUI.GRID_ROW) + 1);
		this.foodY = (random.nextInt(MainGUI.GRID_COL) + 3);
	}

	// 画画
	public void drawFood(Graphics g) {
		Color color = g.getColor();
		g.setColor(foodColor);
		// 填充颜色
		g.fillOval(foodX * MainGUI.GRID_WIDTH, foodY * MainGUI.GRID_WIDTH, FOOD_WIDTH, FOOD_WIDTH);
		g.setColor(color);
		// 改变颜色,方便提示食物在哪
		if (foodColor == Color.RED) {
			foodColor = Color.BLUE;
		} else {
			foodColor = Color.RED;
		}
	}
}
