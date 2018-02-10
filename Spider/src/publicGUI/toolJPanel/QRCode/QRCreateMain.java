package publicGUI.toolJPanel.QRCode;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCreateMain {
	// 选择编码
	private static final String CHARSET = "utf-8";
	// 图片格式
	private static final String FORMAT_NAME = "JPG";
	// 二维码尺寸
	private static final int QRCODE_SIZE = 300;
	// 插入的图片大小
	private static final int WIDTH = 60;
	private static final int HEIGHT = 60;

	// 生成图片
	public BufferedImage createImage(QRCreateParams qrParams) throws IOException {
		Hashtable hints = new Hashtable();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		hints.put(EncodeHintType.MARGIN, 1);
		BitMatrix bitMatrix = null;
		try {
			bitMatrix = new MultiFormatWriter().encode(qrParams.getLinkOrText(), BarcodeFormat.QR_CODE, QRCODE_SIZE,
					QRCODE_SIZE, hints);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}
		if (qrParams.getInsertImagePath() == null || "".equals(qrParams.getInsertImagePath())) {
			return image;
		}
		// 插入图片 是否压缩图片
		InsertImage(image, qrParams.getInsertImagePath(), true);
		return image;
	}

	// 插入图片
	private void InsertImage(BufferedImage image, String insertImagePath, boolean b) throws IOException {
		File file = new File(insertImagePath);
		if (!file.exists()) {
			return;
		}
		Image src = ImageIO.read(new File(insertImagePath));
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		if (width > WIDTH) {
			width = WIDTH;
		}
		if (height > HEIGHT) {
			height = HEIGHT;
		}
		Image image2 = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = tag.getGraphics();
		g.drawImage(image2, 0, 0, null); // 绘制缩小后的图
		g.dispose();
		src = image2;
		// 插入LOGO
		Graphics2D graph = image.createGraphics();
		int x = (QRCODE_SIZE - width) / 2;
		int y = (QRCODE_SIZE - height) / 2;
		graph.drawImage(src, x, y, width, height, null);
		Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}

	// 保存
	public boolean saveQRImage(QRCreateParams qrParams, BufferedImage bi1) {
		if (bi1 == null) {
			return false;
		}
		if ("".equals(qrParams.getFilePath())) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String imageName = sdf.format(new Date()) + ".jpg";
		try {
			// 写入
			ImageIO.write(bi1, FORMAT_NAME, new File(qrParams.getFilePath() + "/" + imageName));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String analyQR(File file) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		if (image == null) {
			return null;
		}
		BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		Result result = null;
		Hashtable hints = new Hashtable();
		hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
		try {
			result = new MultiFormatReader().decode(bitmap, hints);
		} catch (NotFoundException e) {
			e.printStackTrace();
			return null;
		}
		String resultStr = result.getText();
		return resultStr;
	}

}
