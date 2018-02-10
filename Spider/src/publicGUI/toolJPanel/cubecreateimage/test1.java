package publicGUI.toolJPanel.cubecreateimage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;

import javax.imageio.ImageIO;

public class test1 {

	/**
	 * @param srcImageFile
	 *            源图像地址
	 * @param descImageFile
	 *            切片目标文件夹
	 * @param destWidth
	 *            目标切片宽度
	 * @param destHeight
	 *            目标切片高度
	 */
	public static void cut(String srcImageFile, String descImageFile, int destWidth, int destHeight) {
		try {
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			// 源图宽度
			int srcWidth = bi.getWidth();
			// 源图高度
			int srcHeight = bi.getHeight();
			if (srcWidth > destWidth && srcHeight > destHeight) {
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				// 切片横向数量
				int cols = 0;
				// 切片纵向数量
				int rows = 0;
				// 计算切片的横向和纵向数量
				if (srcWidth % destWidth == 0) {
					cols = srcWidth / destWidth;
				} else {
					cols = (int) Math.floor(srcWidth / destWidth) + 1;
				}
				if (srcHeight % destHeight == 0) {
					rows = srcHeight / destHeight;
				} else {
					rows = (int) Math.floor(srcHeight / destHeight) + 1;
				}
				Image img;
				ImageFilter cropFilter;
				// 循环建立切片
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						// 四个参数分别为图像起点坐标和宽高
						cropFilter = new CropImageFilter(j * destWidth, i * destHeight, (j + 1) * destWidth,
								(i + 1) * destHeight);
						img = Toolkit.getDefaultToolkit()
								.createImage(new FilteredImageSource(image.getSource(), cropFilter));
						BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
						Graphics g = tag.getGraphics();
						g.drawImage(img, 0, 0, null); // 绘制缩小后的图
						g.dispose();
						// 输出为文件
						ImageIO.write(tag, "JPEG", new File(descImageFile + "pre_map_" + i + "_" + j + ".jpg"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		cut("c://001.jpg", "c://002.jpg", 2000, 1200);
	}

}