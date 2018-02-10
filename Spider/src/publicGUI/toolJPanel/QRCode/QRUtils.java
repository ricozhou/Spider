package publicGUI.toolJPanel.QRCode;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class QRUtils {
	// 校验生成参数
	public boolean checkQRCreate(QRCreateParams qrParams) {
		if (qrParams.getLinkOrText() == null || "".equals(qrParams.getLinkOrText())) {
			return false;
		}
		return true;
	}

	// 缩放图片
	public BufferedImage zoomImage(String absolutePath, int width, int height) {
		double wr = 0, hr = 0;
		File srcFile = new File(absolutePath);
		BufferedImage bufImg = null;
		try {
			bufImg = ImageIO.read(srcFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		Image Itemp = bufImg.getScaledInstance(width, height, bufImg.SCALE_SMOOTH);
		wr = width * 1.0 / bufImg.getWidth();
		hr = height * 1.0 / bufImg.getHeight();
		AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
		Itemp = ato.filter(bufImg, null);
		return (BufferedImage) Itemp;
	}

}
